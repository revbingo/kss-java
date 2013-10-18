package com.revbingo.kss;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;
import org.junit.Before;
import org.junit.Test;

public class CommentParserTest {

	private ArrayList<String> parsedComments;
	
	@Before
	public void loadFixtures() throws IOException {
		String filePath = "src/test/resources/comments.txt";
		CommentParser unit = new CommentParser(FileUtils.readFileToString(new File(filePath)));
		parsedComments = unit.blocks();
	}
	
	@Test
	public void detectsSingleLineSyntax() throws Exception {
		assertThat(CommentParser.isSingleLineComment("// yuuuuup"), is(true));
		assertThat(CommentParser.isSingleLineComment("nooooope"), is(false));
	}
	
	@Test
	public void detectsStartOfMultiLineCommentSyntax() throws Exception {
		assertThat(CommentParser.isStartOfMultiLineComment("/* yuuuup"), is(true));
		assertThat(CommentParser.isStartOfMultiLineComment("noooope"), is(false));
	}
	
	@Test
	public void detectsEndOfMultiLineCommentSyntax() throws Exception {
		assertThat(CommentParser.isEndOfMultiLineComment(" yuuuup */"), is(true));
		assertThat(CommentParser.isEndOfMultiLineComment("noooope"), is(false));
	}

	@Test
	public void parsesSingleLineCommentSyntax() throws Exception {
		assertThat(CommentParser.parseSingleLine("// yuuuuup"), is(" yuuuuup"));
	}

	@Test
	public void parsesMultiLineCommentSyntax() throws Exception {
		assertThat(CommentParser.parseMultiLine("/* yuuuuup */"), is(" yuuuuup"));
	}
	
	@Test
	public void parsesCorrectNumberOfCommentBlocks() throws Exception {
		assertThat(parsedComments.size(), is(7));
	}
	
	@Test
	public void handlesWindowsLineEndings() throws Exception {
		String windowsString = "/* I'm a Mac\r\nAnd I'm a PC! */";
		
		assertThat(new CommentParser(windowsString).blocks().size(), is(1));
	}
	
	@Test
	public void handlesUnixLineEndings() throws Exception {
		String unixString = "/* I'm a Mac\nAnd I'm a PC! */";
		
		assertThat(new CommentParser(unixString).blocks().size(), is(1));
	}
	
	@Test
	public void handlesOldMacLineEndings() throws Exception {
		String unixString = "/* I'm a Mac\rAnd I'm a PC! */";
		
		assertThat(new CommentParser(unixString).blocks().size(), is(1));
	}
	
	@Test
	public void findsSingleLineCommentStyles() throws Exception {
		String expectedComment = "This comment block has comment identifiers on every line.\n\nFun fact: this is Kyle's favorite comment syntax!";
		
		assertThat(parsedComments.contains(expectedComment), is(true));
	}
	
	@Test
	public void findsBlockStyleCommentStyles() throws Exception {
		String expectedComment = "This comment block is a block-style comment syntax.\n\nThere's only two identifier across multiple lines.";
		assertThat(parsedComments.contains(expectedComment), is(true));
		
		expectedComment = "This is another common multi-line comment style.\n\nIt has stars at the begining of every line.";
		assertThat(parsedComments.contains(expectedComment), is(true));
	}

	@Test
	public void handlesMixedStyles() throws Exception {
		String expectedComment = "This comment has a /* comment */ identifier inside of it!";
		assertThat(parsedComments.contains(expectedComment), is(true));
		
		expectedComment = "Look at my //cool// comment art!";
		assertThat(parsedComments.contains(expectedComment), is(true));
	}
	
	@Test
	public void handlesIndentedComments() throws Exception {
		assertThat(parsedComments.contains("Indented single-line comment."), is(true));
		assertThat(parsedComments.contains("Indented block comment."), is(true));
	}
	
}
