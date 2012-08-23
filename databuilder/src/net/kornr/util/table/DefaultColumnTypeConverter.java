package net.kornr.util.table;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Pattern;

public class DefaultColumnTypeConverter implements IColumnTypeConverter {

	static private Pattern DEFAULT_INTEGER_REGEX = Pattern.compile("[0-9]+");
	static private Pattern DEFAULT_DECIMAL_REGEX = Pattern	.compile("[0-9]+\\.[0-9]*");
	static private Pattern DEFAULT_DATE_REGEX = Pattern.compile("[0-9]+-[0-9]+-[0-9]+");

	private static enum Type {
		STRING, INTEGER, DECIMAL, DATE
	};

	private Type type = Type.STRING;
	private Pattern integerRegex = DEFAULT_INTEGER_REGEX;
	private Pattern decimalRegex = DEFAULT_DECIMAL_REGEX;
	private Pattern dateRegex = DEFAULT_DATE_REGEX;

	private DateFormat dateFormat = new SimpleDateFormat();
	
	@Override
	public void reset() {
		type = Type.STRING;
	}

	@Override
	public boolean checkType(String data) {

		switch (type) {
		case STRING:
			if (integerRegex.matcher(data).matches()) {
				type = Type.INTEGER;
			} else if (decimalRegex.matcher(data).matches()) {
				type = Type.DECIMAL;
			} else if (dateRegex.matcher(data).matches()) {
				type = Type.DATE;
			} else {
				return false;
			}

			return true;
		case INTEGER:
			if (integerRegex.matcher(data).matches()) {
				return true;
			} else if (decimalRegex.matcher(data).matches()) {
				type = Type.DECIMAL;
				return true;
			}
			type = Type.STRING;
			break;

		case DECIMAL:
			if (decimalRegex.matcher(data).matches()) {
				return true;
			}
			type = Type.STRING;
			break;

		case DATE:
			if (dateRegex.matcher(data).matches()) {
				return true;
			}
			type = Type.STRING;
			break;
		}
		return false;
	}

	@Override
	public Object convert(String data) throws ParseException {
		switch (type) {
		case STRING:
			return data;
		case INTEGER:
			return Integer.parseInt(data);
		case DECIMAL:
			return Double.parseDouble(data);
		case DATE:
			return dateFormat.parse(data);
		}
		return data;
	}

	@Override
	public boolean needConversion() {
		return type != Type.STRING;
	}

}
