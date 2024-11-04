package avatar.util.excel;

import avatar.util.checkParams.ErrorDealUtil;
import jxl.Sheet;
import jxl.Workbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class MyXlsReader {

	private Workbook workbook;
	private final int initRowIndex = 2;
	private final int initOneIndex = 1;

	public MyXlsReader(String fileName) {
		File file = new File(fileName);
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(file);
			workbook = Workbook.getWorkbook(fis);
		} catch (Exception e) {
			ErrorDealUtil.printError(e);
			releaseResource();
			throw new RuntimeException(String.format("Open %s error!", fileName), e);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					ErrorDealUtil.printError(e);
				}
			}
		}
	}

	public MyXlsReader(InputStream inputStream) {
		InputStream fis = null;
		try {
			fis = inputStream;
			workbook = Workbook.getWorkbook(fis);
		} catch (Exception e) {
			ErrorDealUtil.printError(e);
			releaseResource();
			throw new RuntimeException(String.format("Open %s error!", inputStream), e);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					ErrorDealUtil.printError(e);
				}
			}
		}
	}

	public Sheet getSheet(String sheetName) {
		if (workbook != null) {
			return workbook.getSheet(sheetName);
		}
		return null;
	}

	public String[] getSheetNames() {
		return workbook.getSheetNames();
	}

	/**

	 *

	 */
	public <T> T getScalarColumnData(String sheetName, int columnIndex,
									 ExcelObjectBuilder<T> builder) {
		if (columnIndex <= 0) {
			throw new IllegalArgumentException("columnIndex cannot less than 1.");
		}
		Sheet sheet = getSheet(sheetName);
		if (sheet == null) {
			return null;
		}
		int colLength = sheet.getColumns();
		int rowLength = sheet.getRows();
		if (colLength <= 1) {
			return null;
		}
		ExcelResultSet ers = new ExcelResultSet(1, rowLength);
		ers.next();
		for (int row = 0; row < rowLength; row++) {
			ers.updateCellValue(
				row,
				sheet.getCell(initRowIndex - 1, row).getContents().trim(),
				sheet.getCell(columnIndex, row));
		}
		if (!ers.first()) {
			return null;
		}
		return builder.build(ers);
	}

//	/**

//	 *

//	 */
//	public <T> ArrayList<T> getColumnDataList(String sheetName, int startColumnIndex,
//											  ExcelObjectBuilder<T> builder) {
//		ArrayList<T> resultList = new ArrayList<T>();
//		if (startColumnIndex <= 0) {
//			throw new IllegalArgumentException("startColumnIndex cannot less than 1.");
//		}
//		Sheet sheet = getSheet(sheetName);
//		if (sheet == null) {
//			return null;
//		}
//		int colLength = sheet.getColumns();
//		int rowLength = sheet.getRows();
//		if (colLength <= 1) {
//			return resultList;
//		}
//		ExcelResultSet ers = new ExcelResultSet(colLength - startColumnIndex, rowLength);
//		for (int col = startColumnIndex; col < colLength; col++) {
//			if (!ers.next()) {
//				break;
//			}
//			for (int row = 0; row < rowLength; row++) {
//				ers.updateCellValue(
//					row,
//					sheet.getCell(initRowIndex - 1, row).getContents().trim(),
//					sheet.getCell(col, row));
//			}
//		}
//		ers.beforeFirst();
//		while (ers.next()) {
//			resultList.add(builder.build(ers));
//		}
//		return resultList;
//	}

	/**

	 *

	 */
	public <T> T getScalarRowData(String sheetName, int rowIndex, ExcelObjectBuilder<T> builder) {
		if (rowIndex <= 0) {
			throw new IllegalArgumentException("rowIndex cannot less than 1.");
		}
		Sheet sheet = getSheet(sheetName);
		if (sheet == null) {
			return null;
		}
		int colLength = sheet.getColumns();
		if (colLength <= 1) {
			return null;
		}
		ExcelResultSet ers = new ExcelResultSet(1, colLength);
		ers.next();
		for (int col = 0; col < colLength; col++) {
			ers.updateCellValue(
				col,
				sheet.getCell(col, initRowIndex - 1).getContents().trim(),
				sheet.getCell(col, rowIndex));
		}
		if (!ers.first()) {
			return null;
		}
		return builder.build(ers);
	}

//	/**

//	 *

//	 */
//	public <T> ArrayList<T> getRowDataList(String sheetName, int startRowIndex,
//										   ExcelObjectBuilder<T> builder) {
//		ArrayList<T> resultList = new ArrayList<T>();
//		if (startRowIndex <= 0) {
//			throw new IllegalArgumentException("startRowIndex cannot less than 1.");
//		}
//		Sheet sheet = getSheet(sheetName);
//		if (sheet == null) {
//			return null;
//		}
//		int colLength = sheet.getColumns();
//		int rowLength = sheet.getRows();
//		if (rowLength <= 1) {
//			return resultList;
//		}
//		ExcelResultSet ers = new ExcelResultSet(rowLength - startRowIndex, colLength);
//		for (int row = startRowIndex; row < rowLength; row++) {
//			if (!ers.next()) {
//				break;
//			}
//			for (int col = 0; col < colLength; col++) {
//				ers.updateCellValue(
//					col,
//					sheet.getCell(col, initRowIndex - 1).getContents().trim(),
//					sheet.getCell(col, row));
//			}
//		}
//		ers.beforeFirst();
//		while (ers.next()) {
//			resultList.add(builder.build(ers));
//		}
//		return resultList;
//	}

	/**
	 * Remember to close me!!!
	 */
	public void close() {
		releaseResource();
	}

	private void releaseResource() {
		if (workbook != null) {
			workbook.close();
			workbook = null;
		}
	}
}
