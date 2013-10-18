package com.revbingo.kss;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommentParser {

	private String stringToRead;
	private ArrayList<String> blocks;
	private boolean parsed;
	private boolean preserveWhitespace = false;
	
	public CommentParser(String input) {
		stringToRead = input;
		blocks = new ArrayList<String>();
		parsed = false;
	}
	
	public void preserveWhitespace(boolean preserve) {
		preserveWhitespace = preserve;
	}

	public ArrayList<String> blocks() {
		if(!parsed) {
			blocks = parseBlocksInput(stringToRead);
		}
		return blocks;
	}

	private ArrayList<String> parseBlocksInput(String input) {
		String currentBlock = "";
		boolean inside_single_line_block = false;
		boolean inside_multi_line_block = false;
		
		for(String line : input.split("\n")) {
			if(isSingleLineComment(line)) {
				String parsed = parseSingleLine(line);
				if(inside_single_line_block) {
					currentBlock += "\n" + parsed;
				} else {
					currentBlock = parsed;
					inside_single_line_block = true;
				}
			}
			
			if(isStartOfMultiLineComment(line) || inside_multi_line_block) {
				String parsed = parseMultiLine(line);
				if(inside_multi_line_block) {
					currentBlock += "\n" + parsed;
				} else {
					currentBlock = parsed;
					inside_multi_line_block = true;
				}
			}
			
			if(isEndOfMultiLineComment(line)) {
				inside_multi_line_block = false;
			}
			
			if(!isSingleLineComment(line) && !inside_multi_line_block) {
				if(currentBlock != null) {
					blocks.add(normalize(currentBlock));
				}
				
				inside_single_line_block = false;
				currentBlock = null;
			}
		}
		this.parsed = true;
		return blocks;
	}

	private String normalize(String currentBlock) {
		if(preserveWhitespace) return currentBlock;
		
		currentBlock = currentBlock.replaceAll("(?m)^\\s*\\*", "");
		
		Integer indentSize = null;
		StringBuffer unindented = new StringBuffer();
		Pattern precedingWhitespacePattern = Pattern.compile("^\\s*");
		for(String piece : currentBlock.split("\n")) {
			int precedingWhitespace = 0;
			Matcher m = precedingWhitespacePattern.matcher(piece);
			String match = "";
			if(m.find()) {
				match = m.group();
			}
			precedingWhitespace = match.length();
			if(indentSize == null) {
				indentSize = precedingWhitespace;
			}
			
			if(!piece.equals("")) {
				if(indentSize <= precedingWhitespace && indentSize > 0) {
					unindented.append(piece.substring(indentSize));
				} else {
					unindented.append(piece);
				}
			}
			unindented.append("\n");
		}
		return unindented.toString().trim();
	}

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
		return rstrip(line.replaceFirst("\\s*//", ""));
	}

	public static String parseMultiLine(String line) {
		return rstrip(line.replaceFirst("\\s*/\\*", "").replaceFirst("\\*/", ""));
	}
	
	private static String rstrip(String input) {
		return input.replaceAll("\\s*$", "");
	}
}
