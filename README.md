Javascript Unicode Library
==========================

Provides support to javascript for unicode operations such as decomposition 
and diacritical mark removal.

In order to cope with the large set of unicode data and operation with a minimal 
memory and network footprint, this library provides optional data loading so that
only the functions needed by the application have their data loaded.

For instance, to be able to call the <tt>lowercase_nomark()</tt> function, the following
script loading are required:

    * <script type="text/javascript" src="src/normalizer_lowercase_nomark.js"></script>
    * <script type="text/javascript" src="src/unicode.js"></script>

Note that the <tt>unicode.js</tt> file must be loaded last.

The library sets itself in the <tt>net.kornr.unicode</tt> namespace to avoid collision.


Unicode Functions
-----------------

## <tt>net.kornr.unicode.lowercase_nomark(some_string)</tt>

Returns a lower-cased, decomposed form, with all the diacritical marks removed.

### Requirement
    * <script type="text/javascript" src="normalizer_lowercase_nomark.js"></script>
    * <script type="text/javascript" src="unicode.js"></script>

### Example

    var UC = net.kornr.unicode;
    var mystring = UC.lowercase_nomark("Ça brûle")); // returns "ca brule"
    

## <tt>net.kornr.unicode.lowercase(some_string)</tt>

Returns a lower-cased, decomposed form. The string may become larger, due to the 
decomposition that converts single characters into several characters (ie. a letter 
and its diacritical marks)

### Requirement
    * <script type="text/javascript" src="normalizer_lowercase.js"></script>
    * <script type="text/javascript" src="unicode.js"></script>

### Example

    var mystring = net.kornr.unicode.lowercase_nomark("Ça brûle")); // returns "ça brûle"
    
## <tt>net.kornr.unicode.uppercase_nomark(some_string)</tt>

Returns an upper-cased and decomposed form, with all the diacritical marks removed.

### Requirement
    * <script type="text/javascript" src="normalizer_uppercase_nomark.js"></script>
    * <script type="text/javascript" src="unicode.js"></script>

### Example

    var UC = net.kornr.unicode;
    var mystring = UC.lowercase_nomark("Ça brûle")); // returns "CA BRULE"
    

## <tt>net.kornr.unicode.uppercase(some_string)</tt>

Returns an upper-cased, decomposed form. The string may become larger, due to the 
decomposition that converts single characters into several characters (ie. a letter 
and its diacritical marks)

### Requirement
    * <script type="text/javascript" src="normalizer_uppercase.js"></script>
    * <script type="text/javascript" src="unicode.js"></script>

### Example

    var mystring = net.kornr.unicode.lowercase_nomark("Ça brûle")); // returns "ÇA BRÛLE"
    
## <tt>net.kornr.unicode.is_letter(some_string)</tt>

Returns true if str is either a charCode for a letter, or a string that only contains letters,
false otherwise.

### Requirement
    * <script type="text/javascript" src="categ_letters.js"></script>
    * <script type="text/javascript" src="unicode.js"></script>

### Example

    var a = net.kornr.unicode.is_letter(" ")); // returns false
    var b = net.kornr.unicode.is_letter("A")); // returns true
    var c = net.kornr.unicode.is_letter("1")); // returns false
    
## <tt>net.kornr.unicode.is_number(some_string)</tt>

Returns true if str is either a charCode for a number, or a string that only contains numbers,
false otherwise.

### Requirement
    * <script type="text/javascript" src="categ_numbers.js"></script>
    * <script type="text/javascript" src="unicode.js"></script>

### Example

    var a = net.kornr.unicode.is_letter(" ")); // returns false
    var b = net.kornr.unicode.is_letter("A")); // returns false
    var c = net.kornr.unicode.is_letter("1")); // returns true
    
## <tt>net.kornr.unicode.is_letter_number(some_string)</tt>

Returns true if str is either a charCode for a letter or a number, or a string that only contains letters and numbers,
false otherwise.

### Requirement
    * <script type="text/javascript" src="categ_letters_numbers.js"></script>
    * <script type="text/javascript" src="unicode.js"></script>

### Example

    var a = net.kornr.unicode.is_letter(" ")); // returns false
    var b = net.kornr.unicode.is_letter("A")); // returns true
    var c = net.kornr.unicode.is_letter("1")); // returns true
    
## <tt>net.kornr.unicode.is_punct(some_string)</tt>

Returns true if some_string is either a charCode for a punctuation sign, or a string that only contains punctuations,
This includes the following unicode categories:
	Pc	Connector_Punctuation	a connecting punctuation mark, like a tie
	Pd	Dash_Punctuation	a dash or hyphen punctuation mark
	Ps	Open_Punctuation	an opening punctuation mark (of a pair)
	Pe	Close_Punctuation	a closing punctuation mark (of a pair)
	Pi	Initial_Punctuation	an initial quotation mark
	Pf	Final_Punctuation	a final quotation mark
	Po	Other_Punctuation	a punctuation mark of other type

### Requirement
    * <script type="text/javascript" src="categ_puncts.js"></script>
    * <script type="text/javascript" src="unicode.js"></script>


## <tt>net.kornr.unicode.is_separator(some_string)</tt>

Returns true if some_string is either a charCode for a separator, or a string that only contains separators,
This includes the following unicode categories:
	Zs	Space_Separator	a space character (of various non-zero widths)
	Zl	Line_Separator	U+2028 LINE SEPARATOR only
	Zp	Paragraph_Separator	U+2029 PARAGRAPH SEPARATOR only

### Requirement
    * <script type="text/javascript" src="categ_separators.js"></script>
    * <script type="text/javascript" src="unicode.js"></script>

## <tt>net.kornr.unicode.is_control(some_string)</tt>

Returns true if some_string is either a charCode for a control code, or a string that only contains control codes,

### Requirement
    * <script type="text/javascript" src="categ_controls.js"></script>
    * <script type="text/javascript" src="unicode.js"></script>

## <tt>net.kornr.unicode.is_punct_separator(some_string)</tt>

Returns true if some_string is either a charCode for a separator or a punctuation sign, or a string that only contains
one of those characters.

### Requirement
    * <script type="text/javascript" src="categ_puncts_separators.js"></script>
    * <script type="text/javascript" src="unicode.js"></script>

## <tt>net.kornr.unicode.is_punct_separator_control(some_string)</tt>

Returns true if some_string is either a charCode for a separator, a punctuation or a control sign, or a string that only contains
one of those characters.

### Requirement
    * <script type="text/javascript" src="categ_puncts_separators_controls.js"></script>
    * <script type="text/javascript" src="unicode.js"></script>

## <tt>net.kornr.unicode.is_math(some_string)</tt>

Returns true if some_string is a math sign or a string only composed of math signs.

### Requirement
    * <script type="text/javascript" src="categ_maths.js"></script>
    * <script type="text/javascript" src="unicode.js"></script>

## <tt>net.kornr.unicode.is_currency(some_string)</tt>

Returns true if some_string is a currency symbol or a string only composed of currencies.

### Requirement
    * <script type="text/javascript" src="categ_currencies.js"></script>
    * <script type="text/javascript" src="unicode.js"></script>
    