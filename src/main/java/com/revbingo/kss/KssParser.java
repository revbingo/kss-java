package com.revbingo.kss;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;


public class KssParser {

	public static final Pattern STYLEGUIDE_PATTERN = Pattern.compile("(?i)(?<!No )Styleguide [0-9A-Za-z ]+");
	private Map<String, Section> sections = new HashMap<String, Section>();
	
	IOFileFilter cssFilter = new IOFileFilter() {

		@Override
		public boolean accept(File dir, String filename) {
			return accept(dir);
		}

		@Override
		public boolean accept(File filename) {
			String name = filename.getName();
			return name.endsWith("css") 
					|| name.endsWith("scss")
					|| name.endsWith("less")
					|| name.endsWith("sass");
		}
		
	};
	public KssParser(String... pathsOrStrings) {
		for(String pathOrString : pathsOrStrings) {
			File f = new File(pathOrString);
			if(f.exists() && f.isDirectory()) {
				Iterator<File> fileIterator = FileUtils.iterateFiles(f, cssFilter, null);
				while(fileIterator.hasNext()) {
					File cssfile = fileIterator.next();
					addKssBlocks(cssfile.getAbsolutePath(), cssfile.getName());
				}
			} else {
				String kssString = pathOrString;
				addKssBlocks(kssString, "");
			}
		}
	}

	private void addKssBlocks(String kssString, String fileName) {
		CommentParser parser = new CommentParser(kssString);
		for(String block : parser.blocks()) {
			if(isKssBlock(block)) {
				addSection(block, "");
			}
		}
	}

	public Section getSection(String section) {
		return sections.get(section);
	}
	
	public static boolean isKssBlock(String block) {
		String[] lines = block.split("\n\n");
		String styleguideLine = lines[lines.length - 1];
		return STYLEGUIDE_PATTERN.matcher(styleguideLine).find();
	}
	
	public void addSection(String block, String filename) {
		Section section = new Section(block, filename);
		sections.put(section.getSection(), section);
	}
	
	public Map<String, Section> getSections() {
		return sections;
	}
}
