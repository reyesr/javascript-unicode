package net.kornr.js.unicode;

import java.io.IOException;
import java.util.LinkedList;
import net.kornr.util.table.DataTable;

public class UCTypeList {

	/**
	 * A structure that associates a codepoint and some data
	 */
	static class CPair {
		public Integer codepoint;
		public Object data;
		
		public CPair(Integer cp, Object data) {
			this.codepoint = cp;
			this.data = data;
		}
	}

	/**
	 * Extends a CPair and adds another codepoint so that both codepoints represent a range
	 *
	 */
	static class StackedCPair extends CPair {

		public Integer codepoint2;

		public StackedCPair(Integer cp1, Integer cp2, Object data) {
			super(cp1, data);
			codepoint2 = cp2;
		}
	}

	
	private LinkedList<CPair> data = new LinkedList<UCTypeList.CPair>(); 

	/**
	 * Builds the initial data for this list.
	 * 
	 * @param table a DataTable with unicode data
	 * @param accepted a String containing a list of letters used to filter the unicode categories (for instance "L" accepts the categories Lu,Ll,Lt,Lm,Lo)
	 * @param invert if true, accepts anything BUT the letters contained in the accepted parameter
	 * @return this object (for chaining)
	 */
	public UCTypeList build(DataTable table, String accepted, boolean invert) {
		LinkedList<CPair> res = new LinkedList<CPair>();
		for (int i=0,max=table.length(); i<max; ++i) {
			Object[] row = table.at(i);
			boolean test = accepted.indexOf(((String)row[2]).charAt(0))>=0;
			if (invert) {
				test = !test;
			}
			if (test) {
				res.add(new CPair((Integer)row[0], row[2]));
			}
		}
		data = res;
		return this;
	}

	/**
	 * Aggregates the elements of this list using StackedCPair
	 * @param checkData if true, prevents the merging if the data are not equals. If false, ignores the data.
	 * @return this object
	 */
	public UCTypeList stack(boolean checkData) {
		LinkedList<CPair> res = new LinkedList<CPair>();
		CPair last = null;
		for (CPair p : data) {
			if (last == null) {
				last = p;
			} else {
				if (!(last instanceof StackedCPair)) {
					CPair lastpair = (CPair)last;
					if (lastpair.codepoint == (p.codepoint-1) && (checkData?p.data.equals(lastpair.data):true)) {
						last = new StackedCPair(lastpair.codepoint, p.codepoint, p.data);
					} else {
						res.add(last);
						last = p;
					}
				} else {
					StackedCPair sp = (StackedCPair)last;
					
					if (p instanceof StackedCPair) {
						StackedCPair cursp = (StackedCPair)p;
						if ((sp.codepoint2 == cursp.codepoint-1) && (checkData?sp.data.equals(cursp.data):true)) {
							sp.codepoint2 = cursp.codepoint2;
						}
					} else { // then it's a CPair
						if (sp.codepoint2 == (p.codepoint-1) && (checkData?sp.data.equals(p.data):true)) {
							sp.codepoint2 = p.codepoint;
						} else {
							res.add(last);
							last = p;
						}
					}
					
				}
			}
		}
		res.add(last);
		data = res;
		return this;
	}

	public String dumpAsObject(int wrapWidth) throws IOException {
		StringBuilder strData = new StringBuilder();
		strData.append("{");
		int width = 1;
		String append;
		boolean first = true;
		for (Object o: data) {
			
			if (strData.length()>4096) {
				return strData.toString();
			}
			
			if (o instanceof StackedCPair) {
				throw new RuntimeException("Can't dump StackedCPair as object");
			} else {
				CPair p = (CPair)o;
				append = (first?"":",")+p.codepoint +":'"+p.data.toString()+"'";
			}
			
			first = false;
			if (width + append.length() > wrapWidth) {
				width = 1;
				strData.append("\n\t");
			}
			strData.append(append);
			width += append.length();
		}
		
		strData.append("};\n");
		return strData.toString();
	}

	public String dumpAsArray(boolean dumpValue, int wrapWidth) throws IOException {
		StringBuilder strData = new StringBuilder();
		strData.append("[");
		int width = 1;
		String append;
		boolean first = true;
		for (Object o: data) {
			
			if (strData.length()>4096) {
				return strData.toString();
			}

			if (o instanceof StackedCPair) {
				StackedCPair sp = (StackedCPair)o;
				if (dumpValue) {
					append = (first?"[":",[")+sp.codepoint+","+sp.codepoint2+",'"+sp.data.toString()+"']";
				} else {
					append = (first?"[":",[")+sp.codepoint+","+sp.codepoint2+"]";
				}
			} else {
				CPair p = (CPair)o;
				if (dumpValue) {
					append = (first?"[":",[")+p.codepoint +",'" + p.data.toString() +"']";
				} else {
					append = (first?"":",")+p.codepoint;
				}
			}
			
			first = false;
			if (width + append.length() > wrapWidth) {
				width = 1;
				strData.append("\n\t");
			}
			strData.append(append);
			width += append.length();
		}
		
		strData.append("];\n");
		return strData.toString();
	}
	
}
