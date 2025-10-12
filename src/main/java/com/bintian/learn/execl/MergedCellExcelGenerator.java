package com.bintian.learn.execl;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;

public class MergedCellExcelGenerator {

    public static void main(String[] args) {
        // 创建工作簿
        try (Workbook workbook = new XSSFWorkbook()) {
            // 创建工作表
            Sheet sheet = workbook.createSheet("合并单元格示例");

            // --- 数据准备 ---
            int totalRows = 30; // 总共生成30行数据用于演示
            int totalCols = 10; // 总共10列

            // --- 写入数据并创建单元格 ---
            for (int i = 0; i < totalRows; i++) {
                Row row = sheet.createRow(i);
                for (int j = 0; j < totalCols; j++) {
                    Cell cell = row.createCell(j);
                    // 填充一些示例数据
                    cell.setCellValue("行 " + (i + 1) + ", 列 " + (j + 1));
                }
            }

            // --- 合并单元格 ---

            // **第一列：每10个单元格合并**
            // 循环步长为10
            for (int i = 0; i < totalRows; i += 10) {
                // 定义合并区域：(起始行, 结束行, 起始列, 结束列)
                // 结束行要小心，不要超出总行数
                int endRow = Math.min(i + 9, totalRows - 1);
                if (i < endRow) { // 只有当起始行小于结束行时才合并
                    sheet.addMergedRegion(new CellRangeAddress(i, endRow, 0, 0));
                }
            }

            // **第二列：每5个单元格合并**
            // 循环步长为5
            for (int i = 0; i < totalRows; i += 5) {
                int endRow = Math.min(i + 4, totalRows - 1);
                if (i < endRow) {
                    sheet.addMergedRegion(new CellRangeAddress(i, endRow, 1, 1));
                }
            }

            // 为了美观，可以给合并后的单元格设置一个居中样式
            CellStyle style = workbook.createCellStyle();
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            style.setAlignment(HorizontalAlignment.CENTER);

            // 将样式应用到第一列和第二列
            for(int i = 0; i < totalRows; i++) {
                sheet.getRow(i).getCell(0).setCellStyle(style);
                sheet.getRow(i).getCell(1).setCellStyle(style);
            }


            // --- 写入文件 ---
            try (FileOutputStream outputStream = new FileOutputStream("MergedCellsExample.xlsx")) {
                workbook.write(outputStream);
                System.out.println("文件 MergedCellsExample.xlsx 已成功生成！");
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("生成Excel文件时出错。");
        }
    }
}
