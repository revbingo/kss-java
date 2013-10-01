package com.revbingo.kss;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.Test;

public class CommentParserTest {

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
		assertThat(CommentParser.parseSingleLine("// yuuuuup"), is("yuuuuup"));
	}

	@Test
	public void parsesMultiLineCommentSyntax() throws Exception {
		assertThat(CommentParser.parseMultiLine("/* yuuuuup */"), is("yuuuuup"));
	}
	
	@Test
	public void findsSingleLineCommentStyles() throws Exception {
		
	}
//	  test "finds single-line comment styles" do
//	    expected  = <<comment
//	This comment block has comment identifiers on every line.
//
//	Fun fact: this is Kyle's favorite comment syntax!
//	comment
//	    assert @parsed_comments.include? expected.rstrip
//	  end
//
//	  test "finds block-style comment styles" do
//	        expected  = <<comment
//	This comment block is a block-style comment syntax.
//
//	There's only two identifier across multiple lines.
//	comment
//	    assert @parsed_comments.include? expected.rstrip
//
//
//	      expected  = <<comment
//	This is another common multi-line comment style.
//
//	It has stars at the begining of every line.
//	comment
//	  assert @parsed_comments.include? expected.rstrip
//
//	  end
//
//	  test "handles mixed styles" do
//	    expected = "This comment has a /* comment */ identifier inside of it!"
//	    assert @parsed_comments.include? expected
//
//	    expected = "Look at my //cool// comment art!"
//	    assert @parsed_comments.include? expected
//	  end
//
//	  test "handles indented comments" do
//	    assert @parsed_comments.include? "Indented single-line comment."
//	    assert @parsed_comments.include? "Indented block comment."
//	  end
}
