package com.neozant.filehelper;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.neozant.interfaces.IFileWriter;




public class WriteCsv implements IFileWriter{

	@Override
	public void exportDataToFile(ResultSet res, String fileName) {
		
		
		
		try {

			int colunmCount = res.getMetaData().getColumnCount();

			FileWriter fw = new FileWriter(fileName);

			for (int i = 1; i <= colunmCount; i++) {
				fw.append(res.getMetaData().getColumnName(i));
				fw.append(",");

			}

			fw.append(System.getProperty("line.separator"));

			while (res.next()) {
				for (int i = 1; i <= colunmCount; i++) {

					// you can update it here by using the column type but i am
					// fine with the data so just converting
					// everything to string first and then saving
					if (res.getObject(i) != null) {
						String data = res.getObject(i).toString();
						fw.append(data);
						fw.append(",");
					} else {
						String data = "null";
						fw.append(data);
						fw.append(",");
					}
				}
				// new line entered after each row
				fw.append(System.getProperty("line.separator"));
			}
			fw.flush();
			fw.close();

		} catch (SQLException e) {
			System.err.println("WriteCsv:: ERROR SQLException while loading JDBC driver:"+e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("WriteCsv:: ERROR IOException while loading JDBC driver:"+e.getMessage());
			e.printStackTrace();
		}
         
		
	}

	
	
	

}
