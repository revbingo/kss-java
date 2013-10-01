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
}
