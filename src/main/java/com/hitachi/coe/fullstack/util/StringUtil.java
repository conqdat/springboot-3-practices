package com.hitachi.coe.fullstack.util;

import com.hitachi.coe.fullstack.model.ConfigKey;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class StringUtil {

    private StringUtil(){}

    public static boolean isMatchedValidation(ConfigKey configKey, String cellStringValue ){
        if ( configKey.getValidation() != null ) {
            String regexPattern = configKey.getValidation().trim();
            return Pattern.compile(regexPattern).matcher(cellStringValue).matches();
        }
        return true;
    }

    public static String formatSpace (String excelHeader) {
        // replace NBSP with white space and remove suspicious characters, work with replaceAll() but not with replace()
        return excelHeader.split("\\r?\\n")[0].replaceAll("\\u00a0", " ").replaceAll("[\\s\\p{Zs}]+", "").replaceAll("[\uFEFF-\uFFFF]", "").trim();
    }

	/**
	 * combine 2 string
	 * 
	 * @param s1 String
	 * @return String combined
	 */
	public static String combineString(String s1, String s2) {
		return s1.trim().concat(s2.trim());
	}

	/**
	 * Capitalizes the first letter of the given string.
	 * 
	 * @param s The string to capitalize.
	 * @return The capitalized string.
	 */
	public static String capitalize(String s) {
		if (s.length() == 0)
			return s;
		return s.substring(0, 1).toUpperCase() + s.substring(1);
	}

	/**
	 * @param string is the cell value from excel file
	 * @return to normal string without unknown symbols
	 * @deprecated use {@link #removeSymbols(String, List)}
	 *             or {@link #removeSymbols(String, String...)} instead
	 */
	public static String removeUnknownSymbol(String string) {
		Pattern pattern = Pattern.compile("[%`\"-]");
		return pattern.matcher(string).replaceAll("");
	}

	/**
	 *
	 * @param configKey configkey corresponding to column
	 * @param cellStringValue string value
	 * @return if length of string is valid or not
	 */
	public static boolean isLengthValid(ConfigKey configKey, String cellStringValue){
		if ( configKey.getMax() != null && configKey.getMin() == null )
			return cellStringValue.length() <= configKey.getMax();
		if ( configKey.getMin() != null && configKey.getMax() == null)
			return cellStringValue.length() >= configKey.getMin();
		return (configKey.getMin() <= cellStringValue.length()) && (cellStringValue.length() <= configKey.getMax());
	}

	/**
	 * Removes specified symbols from a given string.
	 *
	 * @param str     the input {@link String} from which symbols will be removed
	 * @param symbols the {@link List} of symbols to remove
	 * @return the input {@link String} with the specified symbols removed
	 * @author tmihto
	 */
	public static String removeSymbols(String str, List<String> symbols) {
		Set<String> uniqueSymbols = new HashSet<>(symbols);
		String regex = uniqueSymbols.stream()
				.map(Pattern::quote)
				.collect(Collectors.joining("|"));
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.replaceAll("");
	}

	/**
	 * Removes specified symbols from a given string.
	 *
	 * @param str     the input {@link String} from which symbols will be removed
	 * @param symbols the varargs of symbols to remove
	 * @return the input {@link String} with the specified symbols removed
	 * @author tmihto
	 */
	public static String removeSymbols(String str, String... symbols) {
		String regex = Arrays.stream(symbols)
				.map(Pattern::quote)
				.collect(Collectors.joining("|"));
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.replaceAll("");
	}

}
