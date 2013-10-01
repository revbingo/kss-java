package com.revbingo.kss;

import java.util.regex.Pattern;

public class CommentParser {

	public static boolean isSingleLineComment(String line) {
		return line.matches("^\\s*//.*");
	}
}
