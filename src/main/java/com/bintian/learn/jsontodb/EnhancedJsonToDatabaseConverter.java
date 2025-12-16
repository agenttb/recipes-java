package com.bintian.learn.jsontodb;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 增强版JSON到数据库转换器
 * 支持更深层次的嵌套和更复杂的数据结构
 */
public class EnhancedJsonToDatabaseConverter {

    private Connection connection;
    private ObjectMapper objectMapper;
    private Map<String, TableSchema> tableSchemas;
    private Set<String> createdTables;
    private Map<String, String> uuidCache; // 缓存已插入记录的UUID

    // 配置选项
    private boolean useAutoIncrement = true;
    private boolean createIndexes = true;
    private int maxNestingLevel = 10; // 最大嵌套层级

    public EnhancedJsonToDatabaseConverter(Connection connection) {
        this.connection = connection;
        this.objectMapper = new ObjectMapper();
        this.tableSchemas = new ConcurrentHashMap<>();
        this.createdTables = ConcurrentHashMap.newKeySet();
        this.uuidCache = new ConcurrentHashMap<>();
    }

    /**
     * 处理JSON文件
     */
    public void processJson(String jsonString) throws Exception {
        JsonNode rootNode = objectMapper.readTree(jsonString);

        System.out.println("阶段1: 分析JSON结构...");
        analyzeJsonStructure(rootNode, null, null, 0);

        System.out.println("\n阶段2: 创建数据库表...");
        createTables();

        System.out.println("\n阶段3: 插入数据...");
        insertData(rootNode, null, null, null, 0);

        System.out.println("\n阶段4: 创建索引...");
        if (createIndexes) {
            createIndexes();
        }

        System.out.println("\n处理完成!");
    }

    /**
     * 分析JSON结构
     */
    private void analyzeJsonStructure(JsonNode node, String parentTableName,
                                      String currentPath, int level) {
        if (level > maxNestingLevel) {
            System.out.println("警告: 达到最大嵌套层级 " + maxNestingLevel);
            return;
        }

        if (node.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> fields = node.fields();

            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> entry = fields.next();
                String fieldName = entry.getKey();
                JsonNode fieldValue = entry.getValue();

                String newPath = currentPath == null ? fieldName : currentPath + "." + fieldName;

                if (fieldValue.isValueNode()) {
                    // 基本类型字段
                    if (parentTableName != null) {
                        addColumnToSchema(parentTableName, fieldName, fieldValue);
                    }
                } else if (fieldValue.isArray() && isSimpleArray(fieldValue)) {
                    // 简单数组（如["str1", "str2"]），作为单个字段存储
                    if (parentTableName != null) {
                        addColumnToSchema(parentTableName, fieldName, fieldValue);
                    }
                } else if (fieldValue.isObject()) {
                    // 嵌套对象
                    handleNestedObject(fieldName, fieldValue, parentTableName, newPath, level);
                } else if (fieldValue.isArray()) {
                    // 对象数组
                    handleArray(fieldName, fieldValue, parentTableName, newPath, level);
                }
            }
        }
    }

    /**
     * 处理嵌套对象
     */
    private void handleNestedObject(String fieldName, JsonNode fieldValue,
                                    String parentTableName, String path, int level) {
        if (parentTableName == null) {
            // 根对象
            TableSchema schema = getOrCreateSchema(fieldName);
            schema.addColumn("uuid", "VARCHAR(255)", true);
            analyzeJsonStructure(fieldValue, fieldName, path, level + 1);
        } else {
            // 嵌套对象，创建新表
            TableSchema schema = getOrCreateSchema(fieldName);
            schema.addColumn("uuid", "VARCHAR(255)", true);

            // 创建关联表
            createRelationshipSchema(parentTableName, fieldName);

            analyzeJsonStructure(fieldValue, fieldName, path, level + 1);
        }
    }

    /**
     * 处理数组
     */
    private void handleArray(String fieldName, JsonNode fieldValue,
                             String parentTableName, String path, int level) {
        TableSchema schema = getOrCreateSchema(fieldName);
        schema.addColumn("uuid", "VARCHAR(255)", true);

        // 如果有父表，创建关联
        if (parentTableName != null) {
            createRelationshipSchema(parentTableName, fieldName);
        }

        // 分析数组元素 - 先收集所有字段
        if (fieldValue.size() > 0) {
            // 第一步：遍历所有元素，收集所有可能的字段
            Set<String> allFields = new LinkedHashSet<>();
            for (JsonNode element : fieldValue) {
                if (element.isObject()) {
                    element.fieldNames().forEachRemaining(allFields::add);
                }
            }

            // 第二步：为每个字段分析类型（使用第一个非空值）
            for (String field : allFields) {
                for (JsonNode element : fieldValue) {
                    if (element.has(field) && !element.get(field).isNull()) {
                        JsonNode fieldNode = element.get(field);
                        if (fieldNode.isValueNode()) {
                            addColumnToSchema(fieldName, field, fieldNode);
                            break; // 找到第一个非空值就够了
                        }
                    }
                }
            }

            // 第三步：递归处理嵌套结构
            for (JsonNode element : fieldValue) {
                if (element.isObject()) {
                    Iterator<Map.Entry<String, JsonNode>> fields = element.fields();
                    while (fields.hasNext()) {
                        Map.Entry<String, JsonNode> entry = fields.next();
                        String subFieldName = entry.getKey();
                        JsonNode subFieldValue = entry.getValue();

                        if (subFieldValue.isObject() && !subFieldValue.isValueNode()) {
                            handleNestedObject(subFieldName, subFieldValue, fieldName,
                                    path + "." + subFieldName, level + 1);
                        } else if (subFieldValue.isArray()) {
                            handleArray(subFieldName, subFieldValue, fieldName,
                                    path + "." + subFieldName, level + 1);
                        }
                    }
                }
            }
        }
    }

    /**
     * 添加列到表结构
     */
    private void addColumnToSchema(String tableName, String columnName, JsonNode value) {
        TableSchema schema = getOrCreateSchema(tableName);

        // 处理特殊情况：type字段可能是数组，需要转换为字符串
        if (value.isArray()) {
            // 数组字段作为TEXT存储
            schema.addColumn(columnName, "TEXT", false);
        } else if (value.isValueNode()) {
            String columnType = inferColumnType(columnName, value);
            schema.addColumn(columnName, columnType, false);
        }
    }

    /**
     * 推断列类型
     */
    private String inferColumnType(String columnName, JsonNode node) {
        // 特殊字段处理
        if (columnName.equalsIgnoreCase("uuid") || columnName.toLowerCase().contains("uuid")) {
            return "VARCHAR(255)";
        }
        if (columnName.toLowerCase().contains("name") || columnName.toLowerCase().contains("state")) {
            return "VARCHAR(500)";
        }

        // 基于值类型推断
        if (node.isInt()) {
            return "INT";
        } else if (node.isLong()) {
            return "BIGINT";
        } else if (node.isDouble() || node.isFloat()) {
            return "DOUBLE";
        } else if (node.isBoolean()) {
            return "BOOLEAN";
        } else if (node.isTextual()) {
            String text = node.asText();
            if (text.length() > 500) {
                return "TEXT";
            } else if (text.length() > 255) {
                return "VARCHAR(500)";
            } else {
                return "VARCHAR(255)";
            }
        }

        return "TEXT";
    }

    /**
     * 获取或创建表结构
     */
    private TableSchema getOrCreateSchema(String tableName) {
        return tableSchemas.computeIfAbsent(tableName, TableSchema::new);
    }

    /**
     * 创建关联表结构
     */
    private void createRelationshipSchema(String parentTable, String childTable) {
        String relationTableName = parentTable + "_" + childTable + "_rel";
        TableSchema relSchema = getOrCreateSchema(relationTableName);

        if (useAutoIncrement) {
            relSchema.addColumn("id", "INT AUTO_INCREMENT PRIMARY KEY", false);
        }
        relSchema.addColumn(parentTable + "_uuid", "VARCHAR(255)", false);
        relSchema.addColumn(childTable + "_uuid", "VARCHAR(255)", false);
        relSchema.isRelationTable = true;
    }

    /**
     * 创建所有表
     */
    private void createTables() throws SQLException {
        for (TableSchema schema : tableSchemas.values()) {
            if (!createdTables.contains(schema.tableName)) {
                createTable(schema);
                createdTables.add(schema.tableName);
            }
        }
    }

    /**
     * 创建单个表
     */
    private void createTable(TableSchema schema) throws SQLException {
        StringBuilder sql = new StringBuilder();
        sql.append("CREATE TABLE IF NOT EXISTS `").append(schema.tableName).append("` (");

        List<String> columnDefs = new ArrayList<>();

        // 如果不是关联表且没有主键，添加自增主键
        if (!schema.isRelationTable && useAutoIncrement &&
                !schema.hasColumn("id") && !schema.hasColumn("uuid")) {
            columnDefs.add("id INT AUTO_INCREMENT PRIMARY KEY");
        }

        for (TableSchema.ColumnInfo column : schema.getColumns()) {
            StringBuilder colDef = new StringBuilder();
            colDef.append("`").append(column.name).append("` ").append(column.type);

            if (column.isUnique && !column.type.contains("PRIMARY KEY")) {
                colDef.append(" UNIQUE");
            }

            columnDefs.add(colDef.toString());
        }

        sql.append(String.join(", ", columnDefs));
        sql.append(")");

        try (Statement stmt = connection.createStatement()) {
            System.out.println(sql);
            stmt.execute(sql.toString());
            System.out.println("  ✓ 创建表: " + schema.tableName +
                    " (" + schema.getColumns().size() + " 列)");
        }
    }

    /**
     * 创建索引
     */
    private void createIndexes() throws SQLException {
        for (TableSchema schema : tableSchemas.values()) {
            // 为UUID字段创建索引
            if (schema.hasColumn("uuid")) {
                String indexName = "idx_" + schema.tableName + "_uuid";
                String sql = "CREATE INDEX IF NOT EXISTS " + indexName +
                        " ON `" + schema.tableName + "` (uuid)";
                try (Statement stmt = connection.createStatement()) {
                    stmt.execute(sql);
                }
            }

            // 为关联表的外键创建索引
            if (schema.isRelationTable) {
                for (TableSchema.ColumnInfo column : schema.getColumns()) {
                    if (column.name.endsWith("_uuid")) {
                        String indexName = "idx_" + schema.tableName + "_" +
                                column.name.replace("_uuid", "");
                        String sql = "CREATE INDEX IF NOT EXISTS " + indexName +
                                " ON `" + schema.tableName + "` (`" + column.name + "`)";
                        try (Statement stmt = connection.createStatement()) {
                            stmt.execute(sql);
                        }
                    }
                }
            }
        }
        System.out.println("  ✓ 索引创建完成");
    }

    /**
     * 插入数据
     */
    private String insertData(JsonNode node, String tableName, String parentTableName,
                              String parentUuid, int level) throws SQLException {
        if (level > maxNestingLevel) {
            return null;
        }

        if (node.isObject()) {
            String currentUuid = extractOrGenerateUuid(node);

            if (tableName != null) {
                // 检查是否已经插入过这条记录
                String cacheKey = tableName + ":" + currentUuid;
                if (uuidCache.containsKey(cacheKey)) {
                    // 如果需要，仍然创建关联
                    if (parentTableName != null && parentUuid != null) {
                        insertRelationship(parentTableName, tableName, parentUuid, currentUuid);
                    }
                    return currentUuid;
                }

                // 收集基本字段值
                Map<String, Object> values = new LinkedHashMap<>();

                Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
                while (fields.hasNext()) {
                    Map.Entry<String, JsonNode> entry = fields.next();
                    String fieldName = entry.getKey();
                    JsonNode fieldValue = entry.getValue();

                    if (fieldValue.isValueNode()) {
                        values.put(fieldName, getNodeValue(fieldValue));
                    } else if (fieldValue.isArray() && isSimpleArray(fieldValue)) {
                        // 处理简单数组（如字符串数组）
                        values.put(fieldName, arrayToString(fieldValue));
                    }
                }

                // 插入记录
                insertRecord(tableName, values);
                uuidCache.put(cacheKey, currentUuid);

                // 创建与父表的关联
                if (parentTableName != null && parentUuid != null) {
                    insertRelationship(parentTableName, tableName, parentUuid, currentUuid);
                }

                // 处理嵌套结构
                fields = node.fields();
                while (fields.hasNext()) {
                    Map.Entry<String, JsonNode> entry = fields.next();
                    String fieldName = entry.getKey();
                    JsonNode fieldValue = entry.getValue();

                    if (fieldValue.isObject() && !fieldValue.isValueNode()) {
                        insertData(fieldValue, fieldName, tableName, currentUuid, level + 1);
                    } else if (fieldValue.isArray() && !isSimpleArray(fieldValue)) {
                        for (JsonNode element : fieldValue) {
                            if (element.isObject()) {
                                insertData(element, fieldName, tableName, currentUuid, level + 1);
                            }
                        }
                    }
                }
            } else {
                // 根对象处理
                Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
                while (fields.hasNext()) {
                    Map.Entry<String, JsonNode> entry = fields.next();
                    String fieldName = entry.getKey();
                    JsonNode fieldValue = entry.getValue();

                    if (fieldValue.isObject()) {
                        insertData(fieldValue, fieldName, null, null, level + 1);
                    } else if (fieldValue.isArray()) {
                        for (JsonNode element : fieldValue) {
                            if (element.isObject()) {
                                insertData(element, fieldName, null, null, level + 1);
                            }
                        }
                    }
                }
            }

            return currentUuid;
        }

        return null;
    }

    /**
     * 提取或生成UUID
     */
    private String extractOrGenerateUuid(JsonNode node) {
        if (node.has("uuid")) {
            return node.get("uuid").asText();
        }
        return UUID.randomUUID().toString();
    }

    /**
     * 判断是否为简单数组（仅包含基本类型）
     */
    private boolean isSimpleArray(JsonNode arrayNode) {
        if (!arrayNode.isArray() || arrayNode.size() == 0) {
            return false;
        }

        for (JsonNode element : arrayNode) {
            if (!element.isValueNode()) {
                return false;
            }
        }
        return true;
    }

    /**
     * 将简单数组转为字符串
     */
    private String arrayToString(JsonNode arrayNode) {
        List<String> values = new ArrayList<>();
        for (JsonNode element : arrayNode) {
            values.add(element.asText());
        }
        return String.join(",", values);
    }

    /**
     * 获取节点值
     */
    private Object getNodeValue(JsonNode node) {
        if (node.isInt()) {
            return node.asInt();
        } else if (node.isLong()) {
            return node.asLong();
        } else if (node.isDouble() || node.isFloat()) {
            return node.asDouble();
        } else if (node.isBoolean()) {
            return node.asBoolean();
        } else if (node.isNull()) {
            return null;
        } else {
            return node.asText();
        }
    }

    /**
     * 插入记录
     */
    private void insertRecord(String tableName, Map<String, Object> values) throws SQLException {
        if (values.isEmpty()) {
            return;
        }

        StringBuilder sql = new StringBuilder();
        sql.append("INSERT INTO `").append(tableName).append("` (");

        List<String> columns = new ArrayList<>(values.keySet());
        sql.append(columns.stream()
                .map(col -> "`" + col + "`")
                .reduce((a, b) -> a + ", " + b)
                .orElse(""));

        sql.append(") VALUES (");
        sql.append(String.join(", ", Collections.nCopies(columns.size(), "?")));
        sql.append(")");

        try (PreparedStatement pstmt = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < columns.size(); i++) {
                Object value = values.get(columns.get(i));
                pstmt.setObject(i + 1, value);
            }
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("插入失败 - 表: " + tableName);
            System.err.println("SQL: " + sql.toString());
            System.err.println("字段: " + columns);
            System.err.println("值: " + values);
            throw e;
        }
    }

    /**
     * 插入关联关系
     */
    private void insertRelationship(String parentTable, String childTable,
                                    String parentUuid, String childUuid) throws SQLException {
        String relationTableName = parentTable + "_" + childTable + "_rel";

        // 检查关联是否已存在
        String checkSql = "SELECT COUNT(*) FROM `" + relationTableName + "` WHERE " +
                "`" + parentTable + "_uuid` = ? AND `" + childTable + "_uuid` = ?";

        try (PreparedStatement checkStmt = connection.prepareStatement(checkSql)) {
            checkStmt.setString(1, parentUuid);
            checkStmt.setString(2, childUuid);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return; // 关联已存在
            }
        }

        String sql = "INSERT INTO `" + relationTableName + "` (" +
                "`" + parentTable + "_uuid`, `" + childTable + "_uuid`) VALUES (?, ?)";

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, parentUuid);
            pstmt.setString(2, childUuid);
            pstmt.executeUpdate();
        }
    }

    /**
     * 表结构类
     */
    public static class TableSchema {
        String tableName;
        private Map<String, ColumnInfo> columns;
        boolean isRelationTable = false;

        TableSchema(String tableName) {
            this.tableName = tableName;
            this.columns = new LinkedHashMap<>();
        }

        void addColumn(String name, String type, boolean isUnique) {
            if (!columns.containsKey(name)) {
                columns.put(name, new ColumnInfo(name, type, isUnique));
            }
        }

        boolean hasColumn(String name) {
            return columns.containsKey(name);
        }

        Collection<ColumnInfo> getColumns() {
            return columns.values();
        }

        static class ColumnInfo {
            String name;
            String type;
            boolean isUnique;

            ColumnInfo(String name, String type, boolean isUnique) {
                this.name = name;
                this.type = type;
                this.isUnique = isUnique;
            }
        }
    }

    // Getter和Setter方法
    public void setUseAutoIncrement(boolean useAutoIncrement) {
        this.useAutoIncrement = useAutoIncrement;
    }

    public void setCreateIndexes(boolean createIndexes) {
        this.createIndexes = createIndexes;
    }

    public void setMaxNestingLevel(int maxNestingLevel) {
        this.maxNestingLevel = maxNestingLevel;
    }
}