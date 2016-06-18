package com.neozant.interfaces;

import java.sql.ResultSet;

public interface IFileWriter {

	public void exportDataToFile(ResultSet rs, String fileName);
}
