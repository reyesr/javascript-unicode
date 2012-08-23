package net.kornr.util.table;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.text.ParseException;


public class CsvFileReader {

	private String sep;
	private InputStream inputStream;
	private String charset;
	LineNumberReader linereader = null;
	private boolean hasHeader = true;
	
	private class Converter implements DataTable.ColumnProcessor {

		private int col;
		IColumnTypeConverter converter;
		
		public Converter(int col, IColumnTypeConverter conv) {
			this.col = col;
			this.converter = conv;
		}
		
		@Override
		public int getColumn() {
			return col;
		}

		@Override
		public Object process(Object o) {
			try {
				return converter.convert(o.toString());
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		}
		
	}
	
	public CsvFileReader(File input, String separator, String charset) throws FileNotFoundException {
		this(new FileInputStream(input), separator, charset);
	}

	public CsvFileReader(InputStream input, String separator, String charset) {
		this.inputStream = input;
		this.sep = separator;
		this.charset = charset;
	}

	public boolean open() {
		InputStreamReader reader = new InputStreamReader(inputStream);
		linereader = new LineNumberReader(reader);
		return true;
	}

	public DataTable readFull() throws IOException {
		DataTable table = new DataTable();
		String line;
		
		// First, read the headers
		if (hasHeader) {
			line = linereader.readLine();
			if (line != null) {
				String[] headers = line.split(sep);
				for (String h: headers) {
					table.addHeader(h.trim());
				}
			}
		}
		
		// Then read all the data as String
		while ((line=linereader.readLine()) != null) {
			String[] elements = line.split(sep);
			Object[] oels = new Object[elements.length];
			for (int i=0; i<elements.length; ++i) {
				oels[i] = elements[i];
			}
			table.addRow(oels);
		}
		
		return table;
	}

	public boolean getHasHeader() {
		return hasHeader;
	}

	public void setHasHeader(boolean hasHeader) {
		this.hasHeader = hasHeader;
	}
	
}
