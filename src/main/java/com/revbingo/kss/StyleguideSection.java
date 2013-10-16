package com.revbingo.kss;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StyleguideSection {

	private String raw;
	private String filename;
	private String[] commentSections;
	private String styleGuideSection;
	
	public StyleguideSection(String commentText, String filename) {
		this.raw = commentText;
		this.filename = filename;
	}

	public String getDescription() {
		StringBuffer description = new StringBuffer();
		for(String section : commentSections()) {
			if(!isSectionComment(section) && !isModifierComment(section)) {
				description.append(section);
				description.append("\n\n");
			}
		}
		return description.toString().substring(0, description.length() - 2);
	}
	
	public String[] commentSections() {
		commentSections = raw.split("\n\n");
		return commentSections;
	}

	private boolean isSectionComment(String section) {
		return section.equals(getSectionComment());
	}
	
	private String getSectionComment() {
		for(String comment : commentSections()) {
			Matcher m = KssParser.STYLEGUIDE_PATTERN.matcher(comment);
			if(m.find()) {
				return comment;
			}
		}
		return null;
	}
	
	private boolean isModifierComment(String section) {
		return getModifiersComment().equals(section);
	}
	
	private String getModifiersComment() {
		String lastComment = ""; 
		String[] commentSections = commentSections();
		for(int i=1; i <= commentSections.length - 1; i++) {
			if(!isSectionComment(commentSections[i])) {
				lastComment = commentSections[i];
			}
		}
		return lastComment;
	}

	public String getSection() {
		if(styleGuideSection != null) return styleGuideSection;
		
		String cleaned = getSectionComment().trim().replaceAll("\\.$", "");
		Matcher m = Pattern.compile("Styleguide (.+)").matcher(cleaned);
		if(m.find()) {
			return m.group(1);
		}
		return null;
	}

	public ArrayList<Modifier> getModifiers() {
		Integer lastIndent = null;
		ArrayList<Modifier> modifiers = new ArrayList<Modifier>();
		
		String modifiersComment = getModifiersComment();
		if(modifiersComment == null) return modifiers;
		
		Pattern precedingWhitespacePattern = Pattern.compile("^\\s*");
		
		for(String modifierLine : modifiersComment.split("\n")) {
			if("".equals(modifierLine.trim())) continue;
			Matcher m = precedingWhitespacePattern.matcher(modifierLine);
			String match = "";
			if(m.find()) {
				match = m.group();
			}
			int indent = match.length();
			
			if(lastIndent != null && indent > lastIndent) {
				//?
			} else {
				String name = modifierLine.split(" - ")[0].trim();
				String desc = modifierLine.split(" - ")[1].trim();
				if(name != null && desc != null) {
					Modifier modifier = new Modifier(name, desc);
					modifiers.add(modifier);
				}
			}
			
			lastIndent = indent;
		}
		return modifiers;
	}
}
