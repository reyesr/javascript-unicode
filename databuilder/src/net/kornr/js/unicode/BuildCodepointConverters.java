package net.kornr.js.unicode;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import net.kornr.util.table.ColumnProcessors;
import net.kornr.util.table.CsvFileReader;
import net.kornr.util.table.DataTable;
import net.kornr.util.table.DataTable.ColumnProcessor;

/**
 * Creates javascript data for codepoint converters combining several operation into one. 
 * The created data are optimized for memory consumption and data size.
 *
 * The arrays contains the following elements:
 * - [cp1,cp2, 'A', cp3]: any codepoint between cp1 and cp2 inclusive can be converted as cp3
 * - [cp1,cp2, 'R', offset]: any codepoint between cp1 and cp2 inclusive can be converted by adding the offset value to its initial value
 * - [cp1,cp2] : codepoint cp1 can be converted into cp2
 * 
 * @author Rodrigo Reyes
 *
 */
public class BuildCodepointConverters {
	
	static class RelativeChangeStruct {
		public int minCodePoint, maxCodePoint;
		public int shift;
		public String toString() {
			return "[" + minCodePoint + "," + maxCodePoint + ",'R'," + shift + "]";
		}
	}

	static class AbsoluteChangeStruct {
		public int minCodePoint, maxCodePoint;
		public int codePoint;
		public String toString() {
			return "[" + minCodePoint + "," + maxCodePoint + ",'A'," + codePoint + "]";
		}
	}

	static class CodePointPair {
		public Integer origin;
		public Object target;
		
		public CodePointPair(Integer org, Integer target) {
			this.origin = org;
			this.target = target;
		}
		
		public CodePointPair(Integer org, List<Integer> target) {
			this.origin = org;
			this.target = target;
		}
		
		public String toString() {
			return origin +":"+target;
		}
	}

	static public LinkedList<CodePointPair> buildList(DataTable table, int convOffset, boolean decompose, boolean removeMark) {
		LinkedList<CodePointPair> result = new LinkedList<CodePointPair>();
		LinkedList<Integer> decomposition = new LinkedList<Integer>();

		// First, create an index for better performances
		HashMap<Integer, Object[]> index = new HashMap<Integer, Object[]>();
		for (int i=0, max=table.length(); i<max; ++i) {
			Object[] line = table.at(i);
			index.put((Integer)line[0], line);
		}
		
		for (int i=0, max=table.length(); i<max; ++i) {
			Object[] line = table.at(i);
			Integer codePoint = (Integer)line[0];
			Integer org = codePoint;
			
			// First, convert to another case if needed
			if (convOffset > 0 && line.length > convOffset && line[convOffset] != null) {
				codePoint = (Integer)line[convOffset];
				line = index.get(codePoint); // Swap line with the lowercase one
			}

			// Then remove any mark, if needed
			String can = (String)line[5];
			if (decompose && can != null && can.trim().length()>0) {
				
				decomposition.clear();

				String[] split = can.trim().split(" ");
				for (String s: split) {
					if (s.charAt(0) != '<') {
						Integer value = Integer.valueOf(s, 16);
						decomposition.add(value);
					}
				}
				
				if (removeMark) {
					for (ListIterator<Integer> li=decomposition.listIterator(); li.hasNext(); ) {
						Integer val = li.next();
						Object[] drow = index.get(val);
						if (drow != null) {
							String type = (String)drow[2];
							if (type != null && type.length()>0 && type.charAt(0) == 'M') {
								li.remove();
							}
						} else {
							li.remove();
						}
					}
				}
				
				switch(decomposition.size()) {
				case 0:
					result.add(new CodePointPair(org, codePoint));
					break;
				case 1:
					if (!decomposition.get(0).equals(org)) {
						result.add(new CodePointPair(org, decomposition.get(0)));
					}
					break;
				default:
					LinkedList<Integer> dc = new LinkedList<Integer>();
					dc.addAll(decomposition);
					result.add(new CodePointPair(org, dc));
					break;
				}

			} else {
				if (org != codePoint) {
					result.add(new CodePointPair(org, codePoint));
				}
			}
			// Then concatenate to stack
		}
		
		return result;
	}

	
	static public LinkedList<CodePointPair> buildLowerCaseDecomposeNoMarkList(DataTable table) {
		return buildList(table, 13, true, true);
	}

	static public LinkedList<CodePointPair> buildLowerCaseDecomposeList(DataTable table) {
		return buildList(table, 13, true, false);
	}

	static public LinkedList<CodePointPair> buildUpperCaseDecomposeNoMarkList(DataTable table) {
		return buildList(table, 12, true, true);
	}

	static public LinkedList<CodePointPair> buildUpperCaseDecomposeList(DataTable table) {
		return buildList(table, 12, true, false);
	}

	static public LinkedList<CodePointPair> buildTitleCaseDecomposeNoMarkList(DataTable table) {
		return buildList(table, 14, true, true);
	}

	static public LinkedList<CodePointPair> buildTitleCaseDecomposeList(DataTable table) {
		return buildList(table, 14, true, false);
	}

	static public LinkedList<CodePointPair> buildTitleCaseList(DataTable table) {
		return buildList(table, 14, false, false);
	}


	static public List<Object> stackPairs(List<CodePointPair> list) {
		LinkedList<Object> result = new LinkedList<Object>();
		
		Object last = null;
		for (ListIterator<CodePointPair> i=list.listIterator(); i.hasNext(); ) {
			CodePointPair cpp = i.next();
			
			if (last == null) {
				last = cpp;
			} else {
				if (cpp.target instanceof List) {
					result.add(last);
					last = cpp;
				} else {
					if (last instanceof CodePointPair) {
						if (((CodePointPair)last).target instanceof List) {
							result.add(last);
							last = cpp;
						} else {
							CodePointPair cpplast = (CodePointPair)last;
							int lastshift = (Integer)cpplast.target - cpplast.origin;
							int curshift = (Integer)cpp.target - cpp.origin;
							
							if (cpplast.target.equals(cpp.target)) {
								AbsoluteChangeStruct acs = new AbsoluteChangeStruct();
								acs.minCodePoint = Math.min(cpplast.origin, cpp.origin);
								acs.maxCodePoint = Math.max(cpplast.origin, cpp.origin);
								acs.codePoint = (Integer)cpplast.target;
								last = acs;
							} else if (lastshift == curshift){
								RelativeChangeStruct rcs = new RelativeChangeStruct();
								rcs.minCodePoint = Math.min(cpplast.origin, cpp.origin);
								rcs.maxCodePoint = Math.max(cpplast.origin, cpp.origin);
								rcs.shift = lastshift;
								last = rcs;
							} else {
								result.add(last);
								last = cpp;
							}
						}
					} else if (last instanceof RelativeChangeStruct) {
						RelativeChangeStruct rcs = (RelativeChangeStruct)last;
						int curshift = (Integer)cpp.target - cpp.origin;
						if (rcs.shift == curshift) {
							rcs.minCodePoint = Math.min(rcs.minCodePoint, cpp.origin);
							rcs.maxCodePoint = Math.max(rcs.maxCodePoint, cpp.origin);
						} else {
							result.add(last);
							last = cpp;
						}
					} else if (last instanceof AbsoluteChangeStruct) {
						AbsoluteChangeStruct acs = (AbsoluteChangeStruct)last;
						if (acs.codePoint == (Integer)cpp.target) {
							acs.minCodePoint = Math.min(acs.minCodePoint, cpp.origin);
							acs.maxCodePoint = Math.max(acs.maxCodePoint, cpp.origin);
						} else {
							result.add(last);
							last = cpp;							
						}
					}
				}
			}
		}
		result.add(last);
		
		return result;
	}

	static public String toJavascript(List<Object> data, int wrapWidth) {
		StringBuilder buf = new StringBuilder();
		int width = 1;
		buf.append("[");
		boolean first = true;
		String append = "";
			
		for (ListIterator i=data.listIterator(); i.hasNext(); ) {
			Object obj = i.next();
			if (!first) {
				buf.append(",");
				++width;
			} else {
				first = false;
			}
			if (width > wrapWidth) {
				buf.append("\n\t");
				width = 0;
			}

			if (obj instanceof CodePointPair) {
				CodePointPair cpp = (CodePointPair)obj;
				append=("[" + cpp.origin +"," + cpp.target + "]");
			} else if (obj instanceof AbsoluteChangeStruct) {
				AbsoluteChangeStruct acs = (AbsoluteChangeStruct)obj;
				append=("[" + acs.minCodePoint +"," + acs.maxCodePoint + ",'A'," + acs.codePoint + "]");
			} else if (obj instanceof RelativeChangeStruct) {
				RelativeChangeStruct rcs = (RelativeChangeStruct)obj;
				append=("[" + rcs.minCodePoint +"," + rcs.maxCodePoint + ",'R'," + rcs.shift + "]");
			}
			if (width+append.length() > wrapWidth) {
				buf.append("\n\t");
				width = 0;
			}
			buf.append(append);
			width += append.length();
			append = null;
		}
		buf.append("];\n");
		return buf.toString();
	}
	
	static public void createJSFile(File file, String varname, List<Object> data) throws IOException {
		Misc.createJSFile(file, varname, toJavascript(data,120));
	}

	static public void make(String filename, String varname, LinkedList<CodePointPair> data) throws IOException {
		createJSFile(new File(filename), varname, stackPairs(data));
	}
	
	public static void main(String[] args) throws IOException {
		DataTable table = Misc.read(args[0]);
		
		table.convertColumn(new ColumnProcessors.HexaStringToInteger(0));
		table.convertColumn(new ColumnProcessors.HexaStringToInteger(12));
		table.convertColumn(new ColumnProcessors.HexaStringToInteger(13));
		table.convertColumn(new ColumnProcessors.HexaStringToInteger(14));
		
		// Fix the title case column
		for (int i=0,max=table.length(); i<max; ++i) {
			Object[]row = table.at(i);
			if (row.length > 14 && row[14] == null) {
				row[14] = row[12];
			}
		}
		
		createJSFile(new File("normalizer_lowercase.js"), "norm_lowercase_data", stackPairs(buildLowerCaseDecomposeList(table)));
		createJSFile(new File("normalizer_lowercase_nomark.js"), "norm_lowercase_nomark_data", stackPairs(buildLowerCaseDecomposeNoMarkList(table)));
		createJSFile(new File("normalizer_uppercase.js"), "norm_uppercase_data", stackPairs(buildUpperCaseDecomposeList(table)));
		createJSFile(new File("normalizer_uppercase_nomark.js"), "norm_uppercase_nomark_data", stackPairs(buildUpperCaseDecomposeNoMarkList(table)));

//		createJSFile(new File("normalizer_titlecase_nomark.js"), "norm_titlecase_nomark_data", stackPairs(buildTitleCaseDecomposeNoMarkList(table)));

		
	}
}
