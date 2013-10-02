package com.revbingo.kss;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

public class ParserTest {
	
	private KssParser scssParsed;
	private KssParser lessParsed;
	private KssParser sassParsed;
	private KssParser cssParsed;
	private KssParser multiParsed;
	
	@Before
	public void setup() throws Exception {
		scssParsed = new KssParser("src/test/resources/scss");
		lessParsed = new KssParser("src/test/resources/less");
		sassParsed = new KssParser("src/test/resources/sass");
		cssParsed = new KssParser("src/test/resources/css");
		multiParsed = new KssParser("src/test/resources/scss", "src/test/resources/less");
	}
	
//	def setup
//    @scss_parsed = Kss::Parser.new('test/fixtures/scss')
//    @sass_parsed = Kss::Parser.new('test/fixtures/sass')
//    @css_parsed = Kss::Parser.new('test/fixtures/css')
//    @less_parsed = Kss::Parser.new('test/fixtures/less')
//    @multiple_parsed = Kss::Parser.new('test/fixtures/scss', 'test/fixtures/less')
//  end
//
	
	@Test
	public void parsesKSSCommentsInSCSS() throws Exception {
		assertThat(scssParsed.getSection("2.1.1").getDescription(), is("Your standard form button."));
	}
	
	@Test
	public void parsesKSSKeysThatAreWordsInSCSS() throws Exception {
		assertThat(scssParsed.getSection("Buttons.Big").getDescription(), is("A big button"));
	}
	
	@Test
	public void parsesKSSCommentsInLESS() throws Exception {
		assertThat(lessParsed.getSection("2.1.1").getDescription(), is("Your standard form button."));
	}
	
	@Test
	public void parsesKSSKeysThatAreWordsInLESS() throws Exception {
		assertThat(lessParsed.getSection("Buttons.Big").getDescription(), is("A big button"));
	}
	
	@Test
	public void parsesKSSMultiLineCommentsInSASS() throws Exception {
		assertThat(sassParsed.getSection("2.1.1").getDescription(), is("Your standard form button."));
	}
	
	@Test
	public void parsesKSSSingleLineCommentsInSASS() throws Exception {
		assertThat(sassParsed.getSection("2.2.1").getDescription(), is("A button suitable for giving stars to someone."));
	}

	@Test
	public void parsesKSSKeysThatAreWordsInSASS() throws Exception {
		assertThat(sassParsed.getSection("Buttons.Big").getDescription(), is("A big button"));
	}

	@Test
	public void parsesKSSCommentsInCSS() throws Exception {
		assertThat(cssParsed.getSection("2.1.1").getDescription(), is("Your standard form button."));
	}
	
	@Test
	public void parsesKSSKeysThatAreWordsInCSS() throws Exception {
		assertThat(cssParsed.getSection("Buttons.Big").getDescription(), is("A big button"));
	}

	@Test
	public void parsesKSSKeysThatAreWordPhrasesInCSS() throws Exception {
		assertThat(cssParsed.getSection("Buttons - Truly Lime").getDescription(), is("A button truly lime in color"));
	}
	
	@Test
	public void parsesNestedSCSSDocuments() throws Exception {
		assertThat(scssParsed.getSection("3.0.0").getDescription(), is("Your standard form element."));
		assertThat(scssParsed.getSection("3.0.1").getDescription(), is("Your standard text input box."));
	}
	
	@Test
	public void parsesNestedLESSDocuments() throws Exception {
		assertThat(lessParsed.getSection("3.0.0").getDescription(), is("Your standard form element."));
		assertThat(lessParsed.getSection("3.0.1").getDescription(), is("Your standard text input box."));
	}
	
	@Test
	public void parsesNestedSASSDocuments() throws Exception {
		assertThat(sassParsed.getSection("3.0.0").getDescription(), is("Your standard form element."));
		assertThat(sassParsed.getSection("3.0.1").getDescription(), is("Your standard text input box."));
	}
	
	@Test
	@Ignore("make me work")
	public void publicSectionsReturnsHashOfSections() throws Exception {
		assertThat(cssParsed.sections.size(), is(5));
	}
	
//	  test "public sections returns hash of sections" do
//	    assert_equal 5, @css_parsed.sections.count
//	  end
//
	
	@Test
	public void parseMultiPaths() throws Exception {
		assertThat(multiParsed.sections.size(), is(7));
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
		
		assertThat(new KssParser(scssInput).getSection("3.0.0").getDescription(), is("Your standard form element."));
		assertThat(new KssParser(scssInput).getSection("3.0.1").getDescription(), is("Your standard text input box."));
		
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
//	  test "parse with no styleguide reference comment" do
//	    scss_input =<<-'EOS'
//	      // Nothing here
//	      //
//	      // No styleguide reference.
//	      input[type="text"] {
//	        border: 1px solid #ccc;
//	      }
//	    EOS
//
//	    assert Kss::Parser.new(scss_input)
//	  end
}
