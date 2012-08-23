package net.kornr.js.unicode;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import net.kornr.util.table.ColumnProcessors;
import net.kornr.util.table.DataTable;

public class BuildCharTypes {	

	
	public static void main(String[] args) throws IOException {
		DataTable table = Misc.read(args[0]);
		table.convertColumn(new ColumnProcessors.HexaStringToInteger(0));

		Misc.createJSFile(new File("categ_letters_numbers.js"), "categ_letters_numbers_data", new UCTypeList().build(table, "LN", false).stack(false).dumpAsArray(false, 120));
		Misc.createJSFile(new File("categ_letters.js"), "categ_letters_data", new UCTypeList().build(table, "L", false).stack(false).dumpAsArray(false, 120));
		Misc.createJSFile(new File("categ_numbers.js"), "categ_numbers_data", new UCTypeList().build(table, "N", false).stack(false).dumpAsArray(false, 120));
		Misc.createJSFile(new File("categ_non_letters_numbers.js"), "categ_non_letters_numbers_data", new UCTypeList().build(table, "LN", true).stack(false).dumpAsArray(false, 120));

		Misc.createJSFile(new File("categ_puncts.js"), "categ_punct_data", new UCTypeList().build(table, "P", false).stack(false).dumpAsArray(false, 120));
		Misc.createJSFile(new File("categ_separators.js"), "categ_separators_data", new UCTypeList().build(table, "Z", false).stack(false).dumpAsArray(false, 120));
		Misc.createJSFile(new File("categ_puncts_separators.js"), "categ_puncts_separators_data", new UCTypeList().build(table, "PZ", false).stack(false).dumpAsArray(false, 120));
		Misc.createJSFile(new File("categ_puncts_separators_controls.js"), "categ_puncts_separators_controls_data", new UCTypeList().build(table, new String[]{"Pc","Pd","Ps","Pe","Pi","Pf","Po","Zs","Zl", "Zp","Cc"}, false).stack(false).dumpAsArray(false, 120));
		Misc.createJSFile(new File("categ_controls.js"), "categ_controls_data", new UCTypeList().build(table, new String[]{"Cc"}, false).stack(false).dumpAsArray(false, 120));
		Misc.createJSFile(new File("categ_maths.js"), "categ_maths_data", new UCTypeList().build(table, new String[]{"Sm"}, false).stack(false).dumpAsArray(false, 120));
		Misc.createJSFile(new File("categ_currencies.js"), "categ_currencies_data", new UCTypeList().build(table, new String[]{"Sc"}, false).stack(false).dumpAsArray(false, 120));

	}
	
}
