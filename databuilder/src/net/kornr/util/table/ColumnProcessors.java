package net.kornr.util.table;

public class ColumnProcessors {

	public static class ToInteger extends DataTable.AbstractColumnProcessor {
		public ToInteger(int col) {
			super(col);
		}

		@Override
		public Object process(Object o) {
			if (o != null) {
				Double d =  Double.parseDouble(o.toString());
				return d.intValue();
			}
			return o;
		}
	}
	
	public static class HexaStringToInteger extends DataTable.AbstractColumnProcessor {
		public HexaStringToInteger(int col) {
			super(col);
		}

		@Override
		public Object process(Object o) {
			if (o != null && o.toString().length()>0) {
				Integer i = Integer.valueOf(o.toString(), 16);
				return i;
			}
			return null;
		}
	}
 
	
}
