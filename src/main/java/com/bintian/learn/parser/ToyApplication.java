package com.bintian.learn.parser;


import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ToyApplication {
    public static void main(String[] args) {
        String filePath = "E:\\antlr\\Dbc.txt"; // 你的文件路径

        try {
            CharStream input = CharStreams.fromFileName(filePath, StandardCharsets.UTF_8);
            DbcLexer lexer = new DbcLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            DbcParser parser = new DbcParser(tokens);

            // 调用根规则 dbcFile()
            ParseTree tree = parser.dbcFile();

            ParseTreeWalker walker = new ParseTreeWalker();
            DbcListenerImpl extractor = new DbcListenerImpl();

            walker.walk(extractor, tree);

            System.out.println("=== 解析完成 ===");
            System.out.println("Nodes: " + extractor.nodes);
            System.out.println("Messages Count: " + extractor.messages.size());
            System.out.println("------------------------------------------------");

            int count = 0;
            for (DbcSignal.DbcMessage msg : extractor.messages.values()) {
                if (count++ >= 3) break;
                System.out.println(msg);
                for (DbcSignal sig : msg.signals) {
                    System.out.println(sig);
                    if (!sig.valueTable.isEmpty()) {
                        System.out.println("      Values: " + sig.valueTable);
                    }
                }
                System.out.println();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
