package com.bintian.learn.jsontodb;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * JSON转数据库示例程序
 */
public class JsonToDatabaseExample {

    public static void main(String[] args) {
        // 示例JSON
        String jsonExample = "{\n" +
                "  \"version\": \"2.0\",\n" +
                "  \"Project\": {\n" +
                "    \"shortName\": \"MS11_26RC01\",\n" +
                "    \"uuid\": \"6da03aca-31e4-4c51-b549-94c73e17e467\",\n" +
                "    \"customAttributeSets\": [\n" +
                "      {\n" +
                "        \"objectState\": \"Edited\",\n" +
                "        \"shortName\": \"CustomAttrSet1\",\n" +
                "        \"uuid\": \"c891fba1-26f2-4462-8f7b-d00fb44da817\",\n" +
                "        \"attributes\": [\n" +
                "          {\n" +
                "            \"type\": [\n" +
                "              \"VSAX.Infrastructure.CustomAttributes:IntegerAttribute\",\n" +
                "              \"DEC\"\n" +
                "            ],\n" +
                "            \"objectState\": \"Edited\",\n" +
                "            \"shortName\": \"GenMsgCycleTime\",\n" +
                "            \"targetClassName\": \"CanFrame\",\n" +
                "            \"defaultValue\": \"0\",\n" +
                "            \"maxValue\": \"30000\",\n" +
                "            \"minValue\": \"0\"\n" +
                "          }\n" +
                "        ]\n" +
                "      }\n" +
                "    ]\n" +
                "  }\n" +
                "}";

        Connection connection = null;

        try {
            // 连接数据库（使用H2内存数据库作为示例）
            // 实际使用时，请替换为您的数据库连接信息
            connection = DriverManager.getConnection(
                    "jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
                    "sa",
                    ""
            );

            System.out.println("数据库连接成功！");

            // 创建转换器
            EnhancedJsonToDatabaseConverter converter = new EnhancedJsonToDatabaseConverter(connection);

            // 处理JSON
            System.out.println("\n开始处理JSON...\n");
            converter.processJson(jsonExample);

            System.out.println("\n处理完成！");

            // 查看创建的表
            printDatabaseStructure(connection);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 打印数据库结构
     */
    private static void printDatabaseStructure(Connection connection) throws Exception {
        System.out.println("\n========== 数据库表结构 ==========");

        var metaData = connection.getMetaData();
        var tables = metaData.getTables(null, null, "%", new String[]{"TABLE"});

        while (tables.next()) {
            String tableName = tables.getString("TABLE_NAME");
            System.out.println("\n表名: " + tableName);

            var columns = metaData.getColumns(null, null, tableName, null);
            System.out.println("  字段:");
            while (columns.next()) {
                String columnName = columns.getString("COLUMN_NAME");
                String columnType = columns.getString("TYPE_NAME");
                int columnSize = columns.getInt("COLUMN_SIZE");
                System.out.println("    - " + columnName + " (" + columnType +
                        (columnSize > 0 ? "(" + columnSize + ")" : "") + ")");
            }
            columns.close();
        }
        tables.close();

        System.out.println("\n================================");
    }
}