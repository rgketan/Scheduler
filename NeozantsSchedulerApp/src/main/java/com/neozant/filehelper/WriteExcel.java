package com.neozant.filehelper;
import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Types;
import java.util.Locale;

import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.Number;
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

	//private static final String DEFAULT_NUMBER_FORMAT = null;
	
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
		//times.setWrap(true);

		// create create a bold font with unterlines
		WritableFont times10ptBoldUnderline = new WritableFont(WritableFont.TIMES, 11);
				//WritableFont.TIMES, 10, WritableFont.BOLD, false,
				//UnderlineStyle.SINGLE);
				times10ptBoldUnderline.setBoldStyle(WritableFont.BOLD);
				
						
		timesBoldUnderline = new WritableCellFormat(times10ptBoldUnderline);
		// Lets automatically wrap the cells
		//timesBoldUnderline.setWrap(true);

		//CellView cv = new CellView();
		//cv.setFormat(times);
		//cv.setFormat(timesBoldUnderline);
		//cv.setAutosize(true);

		// Write a few headers

		int colunmCount;
		try {
			colunmCount = rs.getMetaData().getColumnCount();

			//COLUMN NAME
			for (int i = 1; i <= colunmCount; i++) {
				 String columnName=rs.getMetaData().getColumnName(i);
				 logger.info("WriteExcel::COLUMN NAME WE GET IS:"+columnName);
				 //System.out.println("WriteExcel::COLUMN NAME WE GET IS:"+columnName);
				 addCaption(sheet, i - 1, 0,columnName);
			}

			//POPULATE CONTENT
			int rowNumber = 1;
			while (rs.next()) {
				ResultSetMetaData rsmd = rs.getMetaData();
				
				for (int i = 1; i <= colunmCount; i++) {

					int type = rsmd.getColumnType(i);
					
					if (rs.getObject(i) != null) {
						//System.out.println("*****TYPE WE GET IS::::"+type);
						
						if (type == Types.INTEGER || type == Types.NUMERIC 
						    || type == Types.BIGINT || type == Types.DECIMAL ) {
			                //out.print(rs.getString(i));
							// float value=rs.getFloat(i);
							
							 double value=rs.getDouble(i);
							 
							 //System.out.println("*****VALUE OF NUMBER WE GET::::"+value);
			            	 addNumber(sheet, i - 1, rowNumber, value);
			            	 /*if(i-1 == 7)
			            	 logger.info("ITS A NUMBER: "+value+"||STRING :"+rs.getObject(i).toString());*/
							
			            } else {
			            	//logger.info("ITS A STRING");
			                //out.print(rs.getLong(i));
			            	 String data= rs.getObject(i).toString();
			            	 
			            	// System.out.println("*****VALUE OF STRING WE GET::::"+data);
							 addLabel(sheet, i - 1, rowNumber, data);
			            }
						
						
						
					}
				}

				rowNumber++;

			}

			logger.info("WriteExcel:: DATA ENTERED INTO EXCEL SHEET FOR RESULTSET:");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			
			logger.error("WriteExcel::ERROR WHILE WRITING RESULTSET TO EXCEL SHEET");
			e.printStackTrace();
		}

	}

	public void exportDataToFile(ResultSet rs, String fileName) {

		// "/Volumes/DATA/WORK/NEOZANT/EBSSqlReports/Testing.xls";

		logger.info("OUTPUT FILE NAME WE GET IS:::"+fileName);
		setOutputFile(fileName);
		
		//System.out.println("OUTPUT FILE NAME WE GET IS:::"+fileName);

		try {
			writeContent(rs);
		} catch (WriteException e) {
			System.err.println("WriteExcel:: ERROR WriteException we get is:"+e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("WriteExcel:: ERROR IOException we get is:"+e.getMessage());
			e.printStackTrace();
			
			
		}

		logger.info("Please check the result file under  " + fileName);
		//System.out.println("Please check the result file under  " + fileName);
	}

	// END MINE

	private void addCaption(WritableSheet sheet, int column, int row, String s)
			throws RowsExceededException, WriteException {
		Label label;
		label = new Label(column, row, s, timesBoldUnderline);
		
		sheet.addCell(label);
		
		//sheet.setColumnView(column, s.length());
		
		// Cell[] cell =sheet.getColumn(column);
		 
		 
	}

	private void addLabel(WritableSheet sheet, int column, int row, String s)
			throws WriteException, RowsExceededException {
		Label label;
		label = new Label(column, row, s, times);
		/*NumberFormat decimalNo = new NumberFormat("#.0"); 
		NumberFormat nf = new NumberFormat(DEFAULT_NUMBER_FORMAT);
		WritableCellFormat numberFormat = new WritableCellFormat();
		//write to datasheet
		//Number numberCell = new Number(c, r, val, st)
		label.setCellFormat(cf);
		cell.setFormat(numberFormat);
		*/
		sheet.addCell(label);
		CellView cell = sheet.getColumnView(column);
		cell.setAutosize(true);
        sheet.setColumnView(column, cell);
	}
	
	
	private void addNumber(WritableSheet sheet, int column, int row,
			double value) throws WriteException, RowsExceededException {
			
			
		    Number number;
		    number = new Number(column, row, value, times);
		    sheet.addCell(number);
		    
		    CellView cell = sheet.getColumnView(column);
		    cell.setAutosize(true);
	        sheet.setColumnView(column, cell);
	}
	
	

	/*public static void main(String[] args) throws WriteException, IOException {
		WriteExcel test = new WriteExcel();

		String fileName = "/Volumes/DATA/WORK/NEOZANT/EBSSqlReports/Testing.xls";

		test.setOutputFile(fileName);
		// test.write();

		System.out.println("Please check the result file under  " + fileName);
	}*/
} 
