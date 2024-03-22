package com.bintian.learn.tool;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class FileTool {
    public static void main(String[] args) {

        try {

            Path source = Paths.get("D:\\kafka-data\\my-topic-0");
            Path target = Paths.get("D:\\kafka-data\\my-topic-0.b2bd908720314b0ba10a63220433cfba-delete");
            Files.move(source, target, StandardCopyOption.ATOMIC_MOVE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
