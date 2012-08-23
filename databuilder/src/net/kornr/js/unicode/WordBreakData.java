package net.kornr.js.unicode;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.LinkedList;

public class WordBreakData {

	private LinkedList<Object> data = new LinkedList<Object>();
	
	static class DataPair {
		Integer data1, data2;
		
		public String toString() {
			return "["+data1+","+data2+"]";
		}
	}
	
	public WordBreakData open(File file) throws IOException {
		Reader reader = new FileReader(file);
		LineNumberReader linereader = new LineNumberReader(reader);
		
		String line;
		while ((line=linereader.readLine()) != null) {
			line = line.trim();
			if (line.length() != 0 && line.charAt(0)!='#') {
				String[] split = line.split(" ");
				System.out.println(split[0]);
				addData(split[0]);
			}
		}
		return this;
	}

	private void addData(String str) {
		String[] splt = str.split("\\.\\.");
		if (splt.length == 1) {
			System.out.println("SINGLE: " + splt[0]);
			data.add(Integer.valueOf(splt[0], 16));
		} else {
			DataPair dp = new DataPair();
			dp.data1 = Integer.valueOf(splt[0], 16);
			dp.data2 = Integer.valueOf(splt[1], 16);
			data.add(dp);
		}
		
	}

	public String toJavascript(int wrapWidth) {
		StringBuilder buf = new StringBuilder();
		int width = 1;
		buf.append("[");
		boolean first = true;
		String append = null;
		
		for (Object o : data) {
			if (!first) {
				++width;
				buf.append(",");
			}
			first = false;
			
			if (width >= wrapWidth) {
				buf.append("\n\t");
				width = 0;
			}
			append = o.toString();
			width += append.length();
			buf.append(o.toString());
		}
		buf.append("];\n");
		return buf.toString();
	}
	
}
