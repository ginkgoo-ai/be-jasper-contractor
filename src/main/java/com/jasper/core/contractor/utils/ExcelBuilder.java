/*
 * Copyright (c) All rights Reserved, Designed By Willie Chen
 *
 * @author: Willie Chen
 * @date:   2024/8/23 09:38
 * Note: this content is limited to internal circulation of the company and is not allowed to be leaked or used for other commercial purposes
 */

package com.jasper.core.contractor.utils;


import lombok.Getter;
import lombok.Setter;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFDataValidationHelper;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.xssf.usermodel.XSSFDataValidationHelper;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Excel builder
 *
 * @author Willie Chen
 */
@Setter
@Getter
public class ExcelBuilder implements Closeable {
    private static final String HIDDEN_SHEET_NAME = "hiddenSheet";
    private final Workbook workbook;
    private final Map<String, Map<Integer, Integer>> sheetColumnWidthMap;
    private CellStyle headerStyle;
    private CellStyle cellStyle;
    private CellStyle footerStyle;


    /**
     * constructor
     */
    private ExcelBuilder() {
        workbook = creteWorkbook();
        sheetColumnWidthMap = new HashMap<>();

        headerStyle = createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font font = createFont();
        font.setColor(IndexedColors.BLACK.index);
        font.setBold(true);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);
        headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setFont(font);

        cellStyle = createCellStyle();
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderTop(BorderStyle.THIN);

        footerStyle = createCellStyle();
        footerStyle.setBorderTop(BorderStyle.THIN);
        footerStyle.setAlignment(HorizontalAlignment.RIGHT);
        Font footerFont = createFont();
        footerFont.setColor(IndexedColors.BLACK.index);
        footerFont.setBold(true);
        footerFont.setFontHeightInPoints((short) 16);
        footerStyle.setFont(footerFont);
    }

    private Workbook creteWorkbook() {

        return new XSSFWorkbook();

    }

    /**
     * Create a new builder
     *
     * @return ExcelBuilder
     */
    public static ExcelBuilder create() {
        return new ExcelBuilder();
    }

    public CellStyle createCellStyle() {
        return workbook.createCellStyle();
    }

    public Font createFont() {
        return workbook.createFont();
    }

    /**
     * Create sheet
     *
     * @param sheetName Sheet name
     * @return Sheet
     */
    public Sheet createSheet(String sheetName) {
        return workbook.createSheet(sheetName);
    }

    /**
     * Create Header
     *
     * @param sheet   Sheet
     * @param columns Column values
     * @return Row
     */
    public Row createHeader(Sheet sheet, List<Object> columns) {
        Row header = createRow(0, sheet, columns, headerStyle, null);
        header.setHeight((short) (2 * 256));
        return header;
    }

    /**
     * Create footer
     *
     * @param sheet   Sheet
     * @param columns Column values
     * @return
     */
    public Row createFooter(int rowNumber, Sheet sheet, List<Object> columns) {
        Row header = createRow(rowNumber, sheet, columns, footerStyle, null);
        header.setHeight((short) (2 * 256));
        return header;
    }

    /**
     * Create Row
     *
     * @param rowNumber row number(index base 0)
     * @param sheet     Sheet
     * @param columns   Column values
     * @return Row
     */
    public Row createRow(int rowNumber, Sheet sheet, List<Object> columns) {
        return createRow(rowNumber, sheet, columns, cellStyle, null);
    }

    /**
     * Create row with cell style
     *
     * @param rowNumber row number(index base 0)
     * @param sheet     Sheet
     * @param columns   Column values
     * @param cellStyle Cell Style
     * @return Row
     */
    public Row createRow(int rowNumber, Sheet sheet, List<Object> columns, CellStyle cellStyle, List<Short> columnFormats) {
        Row row = sheet.createRow(rowNumber);
        Map<Integer, Integer> columnWidthMap = Objects.requireNonNullElse(sheetColumnWidthMap.get(sheet.getSheetName()), new HashMap<>());
        for (int i = 0; i < columns.size(); i++) {

            Object column = columns.get(i);
            String str = column == null ? " " : column.toString();
            int newWidth = str.getBytes().length * 512;
            int oldWidth = Objects.requireNonNullElse(columnWidthMap.get(i), 0);
            if (newWidth > oldWidth) {
                sheet.setColumnWidth(i, newWidth);
                columnWidthMap.put(i, newWidth);
            }
            if (!cellStyle.equals(footerStyle)) {
                if (i < columns.size() - 1) {
                    cellStyle.setBorderRight(BorderStyle.NONE);
                } else {
                    cellStyle.setBorderRight(BorderStyle.THIN);
                }
            }
            Short columnFormat = null;
            if (columnFormats != null) {
                columnFormat = columnFormats.get(i);
            }

            createCell(i, row, cellStyle, column, columnFormat);
        }
        sheetColumnWidthMap.put(sheet.getSheetName(), columnWidthMap);
        return row;
    }

    public void setColumnWidth(Sheet sheet, int columnIndex, int columnWidth) {
        Map<Integer, Integer> columnWidthMap = Objects.requireNonNullElse(sheetColumnWidthMap.get(sheet.getSheetName()), new HashMap<>());
        columnWidthMap.put(columnIndex, columnWidth);
        sheet.setColumnWidth(columnIndex, columnWidth);
        sheetColumnWidthMap.put(sheet.getSheetName(), columnWidthMap);
    }

    /**
     * Create a cell
     *
     * @param index     Index
     * @param row       Row
     * @param cellStyle Cell Style
     * @param value     Value
     */
    private void createCell(int index, Row row, CellStyle cellStyle, Object value, Short columnFormat) {
        Cell cell = row.createCell(index);
        if (cellStyle != null) {
            cell.setCellStyle(cellStyle);
        }
        if (value == null) {
            return;
        }
        if (value instanceof BigDecimal decimal) {
            cell.setCellValue(decimal.doubleValue());
        } else if (value instanceof Timestamp timestamp) {
            cell.setCellValue(timestamp);
        } else if (value instanceof Integer intValue) {
            cell.setCellValue(intValue);
        } else if (value instanceof Double doubleValue) {
            cell.setCellValue(doubleValue);
        } else if (value instanceof Float floatValue) {
            cell.setCellValue(floatValue);
        } else if (value instanceof Long longValue) {
            cell.setCellValue(longValue);
        } else {
            cell.setCellValue(value.toString());
        }


    }

    private DataValidationHelper createDataValidationHelper(Sheet sheet) {
        if (sheet instanceof XSSFSheet xssfSheet) {
            return new XSSFDataValidationHelper(xssfSheet);
        } else {
            return new HSSFDataValidationHelper((HSSFSheet) sheet);
        }
    }

    public void createTextLengthConstraint(Sheet sheet, short columnIndex, int min, int max, String errorMessage) {
        DataValidationHelper dataValidationHelper = createDataValidationHelper(sheet);
        DataValidationConstraint constraint = dataValidationHelper.createTextLengthConstraint(DataValidationConstraint.OperatorType.BETWEEN, String.valueOf(min), String.valueOf(max));

        CellRangeAddressList cellRangeAddressList = new CellRangeAddressList(1, 65535, columnIndex, columnIndex);
        DataValidation dataValidation = dataValidationHelper.createValidation(constraint, cellRangeAddressList);

        dataValidation.createErrorBox("文本长度无效", errorMessage);
        dataValidation.setShowErrorBox(true);
        dataValidation.setSuppressDropDownArrow(true);
        sheet.addValidationData(dataValidation);
    }

    public void createRegexConstraint(Sheet sheet, short columnIndex, String regex, String errorMessage) {
        DataValidationHelper dataValidationHelper = createDataValidationHelper(sheet);
        DataValidationConstraint constraint = dataValidationHelper.createCustomConstraint(regex);

        CellRangeAddressList cellRangeAddressList = new CellRangeAddressList(1, 65535, columnIndex, columnIndex);
        DataValidation dataValidation = dataValidationHelper.createValidation(constraint, cellRangeAddressList);

        dataValidation.createErrorBox("输入无效", errorMessage);
        dataValidation.setShowErrorBox(true);
        dataValidation.setSuppressDropDownArrow(true);
        sheet.addValidationData(dataValidation);
    }


    public void createNumberConstraint(Sheet sheet, short columnIndex, String errorMessage) {
        DataValidationHelper dataValidationHelper = createDataValidationHelper(sheet);
        DataValidationConstraint constraint = dataValidationHelper.createDecimalConstraint(DataValidationConstraint.OperatorType.BETWEEN, "0", String.valueOf(Integer.MAX_VALUE));

        CellRangeAddressList cellRangeAddressList = new CellRangeAddressList(1, 65535, columnIndex, columnIndex);
        DataValidation dataValidation = dataValidationHelper.createValidation(constraint, cellRangeAddressList);

        dataValidation.createErrorBox("无效数字", errorMessage);
        dataValidation.setShowErrorBox(true);
        dataValidation.setSuppressDropDownArrow(true);
        sheet.addValidationData(dataValidation);
    }


    /**
     * Save to byte array
     *
     * @return byte array
     * @throws IOException
     */
    public byte[] saveToByteArray() throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            workbook.write(out);
            IOUtils.close(workbook);
            return out.toByteArray();
        }
    }

    public void save(OutputStream out) throws IOException {
        workbook.write(out);
        IOUtils.close(workbook);
    }

    /**
     * Close builder
     *
     * @throws IOException Exception
     */
    @Override
    public void close() throws IOException {
        IOUtils.close(workbook);
    }

}
