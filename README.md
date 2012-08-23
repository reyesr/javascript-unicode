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
    
