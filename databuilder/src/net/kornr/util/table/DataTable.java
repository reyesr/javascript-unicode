package net.kornr.util.table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.LinkedList;

public class DataTable {

	ArrayList<String> headers;
	ArrayList<Object[]> rows;
	int maxColumns = 0;

	public interface Map<X,Y> {
		public X get(Y y);
		public Collection<X> getAll(Y y);
	}
	
	private class MapImpl<X,Y> implements DataTable.Map<X,Y> {
		private int keyCol;
		private int dataCol;
		private IIdentityMatcher matcher;
		
		public MapImpl(int keyCol, int dataCol, IIdentityMatcher matcher) {
			this.keyCol = keyCol;
			this.dataCol = dataCol;
			this.matcher = matcher;
		}

		@Override
		public X get(Y y) {
			return (X) DataTable.this.map(y, keyCol, matcher, dataCol);
		}

		@Override
		public Collection<X> getAll(Y y) {
			return DataTable.this.mapAll(y, keyCol, matcher, dataCol);
		}
	}

	public class CachedMap<X,Y> implements Map<X,Y> {
		
		private Map<X,Y> back;
		private int MAX_ENTRIES = 100;
		private java.util.Map<Y,X> cache = new LinkedHashMap<Y,X>(MAX_ENTRIES+1, .75F, true) {
		    // This method is called just after a new entry has been added
		    public boolean removeEldestEntry(java.util.Map.Entry eldest) {
		        return size() > MAX_ENTRIES;
		    }
		};
		private java.util.Map<Y,Collection<X>> cacheAll = new LinkedHashMap<Y,Collection<X>>(MAX_ENTRIES+1, .75F, true) {
		    // This method is called just after a new entry has been added
		    public boolean removeEldestEntry(java.util.Map.Entry eldest) {
		        return size() > MAX_ENTRIES;
		    }
		};
		
		public CachedMap(Map<X,Y> map) {
			back = map;
		}
		
		public X get(Y y) {
			X res = (X) cache.get(y);
			if (res == null && cache.containsKey(y) == false) {
				res = back.get(y);
				cache.put(y, res);
			}
			return res;
		}
		public Collection<X> getAll(Y y) {
			Collection<X> res = (Collection<X>) cacheAll.get(y);
			if (res == null && cacheAll.containsKey(y) == false) {
				res = back.getAll(y);
				cacheAll.put(y, res);
			}
			return res;
		}
	}

	public interface RowProcessor {
		public void process(Object[] row);
	}
	
	public interface ColumnProcessor {
		public int getColumn();
		public Object process(Object o);
	}

	public static abstract class AbstractColumnProcessor implements ColumnProcessor {
		int col;
		public AbstractColumnProcessor(int col) {
			this.col = col;
		}
		public int getColumn() { return col; }
		abstract public Object process(Object o);
	}

	public class Cursor implements Enumeration<Object[]> {

		private IIdentityMatcher matcher;
		private int index = -1;
		
		public Cursor(IIdentityMatcher matcher) {
			this.matcher = matcher;
		}
		
		@Override
		public boolean hasMoreElements() {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public Object[] nextElement() {
			// TODO Auto-generated method stub
			return null;
		}
	}
	
	public DataTable() {
		headers = new ArrayList<String>();
		rows = new ArrayList<Object[]>();
	}
	
	public void addHeader(String name) {
		headers.add(name);
		maxColumns = Math.max(maxColumns, headers.size()); 
	}

	public Object[] getHeaders() {
		return headers.toArray(new Object[0]);
	}
	
	public int findIndexForColumn(String colName) {
		int index = 0;
		for (String s: headers) {
			if (s.equalsIgnoreCase(colName)) {
				return index;
			}
			++index;
		}
		if (index >= headers.size()) {
			throw new RuntimeException("Column " + colName + " not found");
		}
		return index;
	}
	
	public void addRow(Object[] data) {
		rows.add(data);
		maxColumns = Math.max(maxColumns, data.length); 
	}
	
	public Object[] at(int index) {
		return rows.get(index);
	}
	
	public int length() {
		return rows.size();
	}

	public int getMaxColumns() {
		return maxColumns;
	}
	
	public void convertColumn(ColumnProcessor proc) {
		int col = proc.getColumn();
		for (Object[] oo : rows) {
			if (oo.length>col) {
				oo[col] = proc.process(oo[col]);
			}
		}
	}

//	private final static boolean safeEquals(Object o1, Object o2) {
//		if (o1 == o2) {
//			return true;
//		}
//		if (o1 != null) {
//			return o1.equals(o2);
//		} else if (o2 != null) {
//			return o2.equals(o1);
//		}
//		return false;
//	}
	
	static public DataTable leftJoin(DataTable table1, DataTable table2, int table1idcol, int table2idcol, int[] table1cols, int[] table2cols, IIdentityMatcher matcher) {
		DataTable result = new DataTable();
		
		for (int c: table1cols) {
			result.addHeader(table1.getHeader(c));
		}
		for (int c: table2cols) {
			result.addHeader(table2.getHeader(c));
		}
		
		for (Object[] row1 : table1.rows) {
			for (Object[] row2 : table2.rows) {
				if (matcher.matches(row1[table1idcol], row2[table2idcol])) {
					Object[] nrow = new Object[table1cols.length + table2cols.length];
					int nrowIndex = 0;
					for (int c1: table1cols) {
						nrow[nrowIndex++] = row1[c1];
					}
					for (int c2: table2cols) {
						nrow[nrowIndex++] = row2[c2];
					}
					result.addRow(nrow);
				}
			}
		}

		return result;
	}
	
	public String getHeader(int col) {
		return headers.get(col);
	}

	public void iterateRows(RowProcessor proc) {
		for (Object[] oo : rows) {
			proc.process(oo);
		}
	}

	public void iterateRows(RowProcessor proc, int keyCol, IIdentityMatcher matcher, Object keyValue) {
		for (Object[] oo : rows) {
			if (matcher.matches(oo[keyCol], keyValue)) {
				proc.process(oo);
			}
		}
	}

	
	public void iterateColumn(ColumnProcessor proc) {
		int col = proc.getColumn();
		for (Object[] oo : rows) {
			proc.process(oo[col]);
		}
	}

	public <X,Y> DataTable.Map<X, Y> createMapper(int keyColumn, int dataColumn, IIdentityMatcher matcher) {
		return new MapImpl<X, Y>(keyColumn, dataColumn, matcher);
	}
	
	public <X,Y> Y map(X key, int keyColumn, IIdentityMatcher matcher, int dataColumn) {
		for (Object[] oo : rows) {
			if (matcher.matches(key, oo[keyColumn])) {
				return (Y)oo[dataColumn];
			}
		}
		return null;
	}

	public <X,Y> Collection<Y> mapAll(X key, int keyColumn, IIdentityMatcher matcher, int dataColumn) {
		LinkedList<Y> result = new LinkedList<Y>();
		for (Object[] oo : rows) {
			if (matcher.matches(key, oo[keyColumn])) {
				Object o = oo[dataColumn];
				result.add((Y)o);
			}
		}
		return result;
	}

	public void dump(int max) {
		StringBuffer buf = new StringBuffer();
		Object[] row;
		for (String head: headers) {
			System.out.print(head + ";\t");
		}
		max = Math.min(max, rows.size());

		System.out.println("");
		for (int i=0; i<max; ++i) {
			buf.setLength(0);
			row = at(i);
			buf.append(row.length + " :: ");
			for (int j=0; j<row.length; ++j) {
				if (row[j]!=null) {
					buf.append(row[j]);
				}
				buf.append("\t;");
			}
			
			System.out.println(buf.toString());
		}
	}
	
	
}
