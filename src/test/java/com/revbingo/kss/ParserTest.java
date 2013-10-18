package com.revbingo.kss;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;

import java.io.File;

import org.junit.Before;
import org.junit.Test;

public class ParserTest {
	
	private KssParser scssParsed;
	private KssParser lessParsed;
	private KssParser sassParsed;
	private KssParser cssParsed;
	private KssParser multiParsed;
	
	@Before
	public void setup() throws Exception {
		scssParsed = new KssParser(new File("src/test/resources/scss"));
		lessParsed = new KssParser(new File("src/test/resources/less"));
		sassParsed = new KssParser(new File("src/test/resources/sass"));
		cssParsed = new KssParser(new File("src/test/resources/css"));
		multiParsed = new KssParser(new File("src/test/resources/scss"), new File("src/test/resources/less"));
	}
	
	@Test
	public void parsesKSSCommentsInSCSS() throws Exception {
		assertThat(scssParsed.getStyleguideSection("2.1.1").getDescription(), is("Your standard form button."));
	}
	
	@Test
	public void parsesKSSKeysThatAreWordsInSCSS() throws Exception {
		assertThat(scssParsed.getStyleguideSection("Buttons.Big").getDescription(), is("A big button"));
	}
	
	@Test
	public void parsesKSSCommentsInLESS() throws Exception {
		assertThat(lessParsed.getStyleguideSection("2.1.1").getDescription(), is("Your standard form button."));
	}
	
	@Test
	public void parsesKSSKeysThatAreWordsInLESS() throws Exception {
		assertThat(lessParsed.getStyleguideSection("Buttons.Big").getDescription(), is("A big button"));
	}
	
	@Test
	public void parsesKSSMultiLineCommentsInSASS() throws Exception {
		assertThat(sassParsed.getStyleguideSection("2.1.1").getDescription(), is("Your standard form button."));
	}
	
	@Test
	public void parsesKSSSingleLineCommentsInSASS() throws Exception {
		assertThat(sassParsed.getStyleguideSection("2.2.1").getDescription(), is("A button suitable for giving stars to someone."));
	}

	@Test
	public void parsesKSSKeysThatAreWordsInSASS() throws Exception {
		assertThat(sassParsed.getStyleguideSection("Buttons.Big").getDescription(), is("A big button"));
	}

	@Test
	public void parsesKSSCommentsInCSS() throws Exception {
		assertThat(cssParsed.getStyleguideSection("2.1.1").getDescription(), is("Your standard form button."));
	}
	
	@Test
	public void parsesKSSKeysThatAreWordsInCSS() throws Exception {
		assertThat(cssParsed.getStyleguideSection("Buttons.Big").getDescription(), is("A big button"));
	}

	@Test
	public void parsesKSSKeysThatAreWordPhrasesInCSS() throws Exception {
		assertThat(cssParsed.getStyleguideSection("Buttons - Truly Lime").getDescription(), is("A button truly lime in color"));
	}
	
	@Test
	public void parsesNestedSCSSDocuments() throws Exception {
		assertThat(scssParsed.getStyleguideSection("3.0.0").getDescription(), is("Your standard form element."));
		assertThat(scssParsed.getStyleguideSection("3.0.1").getDescription(), is("Your standard text input box."));
	}
	
	@Test
	public void parsesNestedLESSDocuments() throws Exception {
		assertThat(lessParsed.getStyleguideSection("3.0.0").getDescription(), is("Your standard form element."));
		assertThat(lessParsed.getStyleguideSection("3.0.1").getDescription(), is("Your standard text input box."));
	}
	
	@Test
	public void parsesNestedSASSDocuments() throws Exception {
		assertThat(sassParsed.getStyleguideSection("3.0.0").getDescription(), is("Your standard form element."));
		assertThat(sassParsed.getStyleguideSection("3.0.1").getDescription(), is("Your standard text input box."));
	}
	
	@Test
	public void publicSectionsReturnsHashOfSections() throws Exception {
		assertThat(cssParsed.getStyleguideSections().size(), is(5));
	}
	
	@Test
	public void parseMultiPaths() throws Exception {
		assertThat(multiParsed.getStyleguideSections().size(), is(7));
	}

	@Test
	public void parseFromString() throws Exception {
		String scssInput = "// Your standard form element.\n" +
							"//\n" +
							"// Styleguide 3.0.0\n" +
							"form {\n" +
							"\n" + 
							"  // Your standard text input box.\n" +
							"  //\n" +
							"  // Styleguide 3.0.1\n" +
							"  input[type=\"text\"] {\n" +
							"    border: 1px solid #ccc;\n" +
							"  }\n" +
							"}\n";
		
		assertThat(new KssParser(scssInput).getStyleguideSection("3.0.0").getDescription(), is("Your standard form element."));
		assertThat(new KssParser(scssInput).getStyleguideSection("3.0.1").getDescription(), is("Your standard text input box."));
		
	}
	
	@Test
	public void parseWithNoStyleguideReferenceComment() throws Exception {
		String scssInput = 
				"// Nothing here\n" +
				"//\n" +
				"// No styleguide reference.\n" +
				"input[type=\"text\"] {\n" +
				"  border: 1px solid #ccc;\n" +
				"}\n";
	
		try {
			new KssParser(scssInput);
		} catch(Exception e) {
			fail();
		}
	}
}
