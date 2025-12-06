package com.bintian.learn.parser;

import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 简单实现：基于 genericLine 捕获并解析 BU_/BO_/SG_ 行，构建 nodes 与 messages。
 * 设计要点：
 * - 兼容当前生成的 parser（只有 genericLine 等规则可用）
 * - 对 BO_/SG_ 做宽松字符串解析以提取常用字段
 */
public class DbcListenerImpl extends DbcBaseListener {
    public final List<String> nodes = new ArrayList<>();
    public final Map<Long, DbcSignal.DbcMessage> messages = new LinkedHashMap<>();

    private DbcSignal.DbcMessage currentMessage = null;

    // 用于解析 BO_ 行：BO_ <id> <name> : <dlc> <transmitter>
    private static final Pattern BO_PATTERN = Pattern.compile(
            "^BO_\\s+(\\d+)\\s+([^:\\s]+)\\s*:\\s*(\\d+)\\s+([^\\s;]+)", Pattern.CASE_INSENSITIVE);

    // 用于解析 SG_ 行（宽松）：SG_ <name> : <start>\\|<length>@<byteOrder>([+-]) \\(factor,offset\\) \\[min\\|max\\] \"unit\" receivers
    private static final Pattern SG_PATTERN = Pattern.compile(
            "^SG_\\s+([^:\\s]+)\\s*:\\s*(\\d+)\\|(\\d+)@([01])([+-])\\s*\\(([^,\\)]+)\\s*,\\s*([^\\)]+)\\)\\s*\\[([^\\|\\]]+)\\|([^\\]]+)\\]\\s*(\"[^\"]*\")?\\s*(.*)$",
            Pattern.CASE_INSENSITIVE);

    @Override
    public void enterGenericLine(DbcParser.GenericLineContext ctx) {
        String raw = ctx.getText();
        if (raw == null) return;
        String line = raw.trim();
        if (line.endsWith(";")) {
            line = line.substring(0, line.length() - 1).trim();
        }

        if (line.startsWith("BU_")) {
            parseBuLine(line);
            return;
        }

        if (line.startsWith("BO_")) {
            parseBoLine(line);
            return;
        }

        if (line.startsWith("SG_")) {
            parseSgLine(line);
            return;
        }

        // 其它行可按需扩展
    }

    private void parseBuLine(String line) {
        // 支持 "BU_ : NODE1 NODE2" 或 "BU_ NODE1 NODE2"
        String payload = line.substring(3).trim();
        if (payload.startsWith(":")) {
            payload = payload.substring(1).trim();
        }
        if (payload.isEmpty()) return;
        String[] parts = payload.split("[\\s,]+");
        for (String p : parts) {
            if (!p.isEmpty()) nodes.add(p);
        }
    }

    private void parseBoLine(String line) {
        Matcher m = BO_PATTERN.matcher(line);
        if (!m.find()) return;
        try {
            long id = Long.parseLong(m.group(1));
            String name = m.group(2);
            int dlc = Integer.parseInt(m.group(3));
            String transmitter = m.group(4);

            currentMessage = new DbcSignal.DbcMessage(id, name, dlc, transmitter);
            messages.put(id, currentMessage);
        } catch (Exception ignored) {}
    }

    private void parseSgLine(String line) {
        Matcher m = SG_PATTERN.matcher(line);
        if (!m.find()) {
            // 如果匹配不到复杂模式，尝试简单解析 name 和接收者
            simpleSgParse(line);
            return;
        }
        if (currentMessage == null) return;

        try {
            DbcSignal sig = new DbcSignal();
            sig.name = m.group(1);
            sig.startBit = Integer.parseInt(m.group(2));
            sig.length = Integer.parseInt(m.group(3));
            sig.byteOrder = "1".equals(m.group(4)) ? "Intel" : "Motorola";
            sig.isSigned = "-".equals(m.group(5));
            sig.factor = Double.parseDouble(m.group(6));
            sig.offset = Double.parseDouble(m.group(7));
            sig.min = Double.parseDouble(m.group(8));
            sig.max = Double.parseDouble(m.group(9));
            String unit = m.group(10);
            if (unit != null && unit.length() >= 2) {
                sig.unit = unit.substring(1, unit.length() - 1);
            }
            String receiversPart = m.group(11);
            if (receiversPart != null && !receiversPart.trim().isEmpty()) {
                String[] rs = receiversPart.trim().split("[,\\s]+");
                for (String r : rs) {
                    if (!r.isEmpty()) sig.receivers.add(r);
                }
            }
            currentMessage.signals.add(sig);
        } catch (Exception ignored) {}
    }

    private void simpleSgParse(String line) {
        // 尝试找 SG_ <name> : ... "unit" Receiver1,Receiver2
        if (currentMessage == null) return;
        String t = line.substring(3).trim(); // after SG_
        int colon = t.indexOf(':');
        if (colon < 0) return;
        String name = t.substring(0, colon).trim();
        DbcSignal sig = new DbcSignal();
        sig.name = name;
        // 尽量抽取接收者（最后的字符串 tokens）
        int quote = t.lastIndexOf('"');
        String tail = t.substring(colon + 1);
        if (quote >= 0) {
            // 尝试接收者在最后
            String after = t.substring(quote + 1).trim();
            if (!after.isEmpty()) {
                String[] rs = after.split("[,\\s]+");
                for (String r : rs) {
                    if (!r.isEmpty() && !r.equals(";")) sig.receivers.add(r);
                }
            }
        } else {
            // 直接找最后的空格分割
            String[] tokens = tail.trim().split("\\s+");
            if (tokens.length > 0) {
                String last = tokens[tokens.length - 1];
                if (!last.equals(";")) {
                    String[] rs = last.split(",");
                    for (String r : rs) {
                        if (!r.isEmpty()) sig.receivers.add(r);
                    }
                }
            }
        }
        currentMessage.signals.add(sig);
    }

    @Override
    public void enterVersionDeclaration(DbcParser.VersionDeclarationContext ctx) {
        // 可选：记录版本信息或忽略
    }

    @Override
    public void enterValDeclaration(DbcParser.ValDeclarationContext ctx) {
        // placeholder: VAL_ 行的解析可在此实现
    }
}
