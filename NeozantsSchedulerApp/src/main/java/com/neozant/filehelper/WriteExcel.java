package com.neozant.filehelper;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;

import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

import org.apache.log4j.Logger;

import com.neozant.interfaces.IFileWriter;


public class WriteExcel implements IFileWriter{

	final static Logger logger = Logger.getLogger(WriteExcel.class);
	private WritableCellFormat timesBoldUnderline;
	private WritableCellFormat times;
	private String inputFile;

	public void setOutputFile(String inputFile) {
		this.inputFile = inputFile;
	}

	// MINE

	private void writeContent(ResultSet rs) throws IOException, WriteException {
		File file = new File(inputFile);
		WorkbookSettings wbSettings = new WorkbookSettings();

		wbSettings.setLocale(new Locale("en", "EN"));

		WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
		workbook.createSheet("Report", 0);
		WritableSheet excelSheet = workbook.getSheet(0);

		createSheetLabel(excelSheet, rs);
		// createContent(excelSheet);

		workbook.write();
		workbook.close();
	}

	private void createSheetLabel(WritableSheet sheet, ResultSet rs)
			throws WriteException {
		// Lets create a times font
		WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);
		// Define the cell format
		times = new WritableCellFormat(times10pt);
		// Lets automatically wrap the cells
		times.setWrap(true);

		// create create a bold font with unterlines
		WritableFont times10ptBoldUnderline = new WritableFont(
				WritableFont.TIMES, 10, WritableFont.BOLD, false,
				UnderlineStyle.SINGLE);
		timesBoldUnderline = new WritableCellFormat(times10ptBoldUnderline);
		// Lets automatically wrap the cells
		timesBoldUnderline.setWrap(true);

		CellView cv = new CellView();
		cv.setFormat(times);
		cv.setFormat(timesBoldUnderline);
		cv.setAutosize(true);

		// Write a few headers

		int colunmCount;
		try {
			colunmCount = rs.getMetaData().getColumnCount();

			//COLUMN NAME
			for (int i = 1; i <= colunmCount; i++) {
				 String columnName=rs.getMetaData().getColumnName(i);
				 System.out.println("WriteExcel::COLUMN NAME WE GET IS:"+columnName);
				 addCaption(sheet, i - 1, 0,columnName);
			}

			//POPULATE CONTENT
			int rowNumber = 1;
			while (rs.next()) {
				for (int i = 1; i <= colunmCount; i++) {

					if (rs.getObject(i) != null) {
						 String data= rs.getObject(i).toString();
						// System.out.println("WriteExcel:: DATA WE GET IS:"+data);
						addLabel(sheet, i - 1, rowNumber, data);
					}
				}

				rowNumber++;

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void exportDataToFile(ResultSet rs, String fileName) {

		// "/Volumes/DATA/WORK/NEOZANT/EBSSqlReports/Testing.xls";

		logger.info("OUTPUT FILE NAME WE GET IS:::"+fileName);
		setOutputFile(fileName);
		
		System.out.println("OUTPUT FILE NAME WE GET IS:::"+fileName);

		try {
			writeContent(rs);
		} catch (WriteException e) {
			System.err.println("WriteExcel:: ERROR WriteException we get is:"+e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("WriteExcel:: ERROR IOException we get is:"+e.getMessage());
			e.printStackTrace();
			
			
		}

		System.out.println("Please check the result file under  " + fileName);
	}

	// END MINE

	private void addCaption(WritableSheet sheet, int column, int row, String s)
			throws RowsExceededException, WriteException {
		Label label;
		label = new Label(column, row, s, timesBoldUnderline);
		sheet.addCell(label);
	}

	private void addLabel(WritableSheet sheet, int column, int row, String s)
			throws WriteException, RowsExceededException {
		Label label;
		label = new Label(column, row, s, times);
		sheet.addCell(label);
	}
	
	
	

	/*public static void main(String[] args) throws WriteException, IOException {
		WriteExcel test = new WriteExcel();

		String fileName = "/Volumes/DATA/WORK/NEOZANT/EBSSqlReports/Testing.xls";

		test.setOutputFile(fileName);
		// test.write();

		System.out.println("Please check the result file under  " + fileName);
	}*/
} 
