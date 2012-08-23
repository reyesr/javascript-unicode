package net.kornr.util.table;

import java.text.ParseException;

public interface IColumnTypeConverter {

	public void reset();
	public boolean checkType(String data);
	public boolean needConversion();
	public Object convert(String data) throws ParseException;
	
}
