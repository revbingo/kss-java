package com.revbingo.kss;

public class CommentParser {

	public static Boolean isSingleLineComment(String line) {
		return line.matches("^\\s*//.*");
	}

	public static Boolean isStartOfMultiLineComment(String line) {
		return line.matches("^\\s*/\\*.*");
	}

	public static Boolean isEndOfMultiLineComment(String line) {
		return line.matches(".*\\*\\/");
	}

	public static String parseSingleLine(String line) {
		return line.replaceFirst("\\s*//", "").trim();
	}

	public static String parseMultiLine(String line) {
		return line.replaceFirst("\\s*/\\*", "").replaceFirst("\\*/", "").trim();
	}
}
