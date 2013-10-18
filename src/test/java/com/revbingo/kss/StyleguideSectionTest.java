package com.revbingo.kss;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class StyleguideSectionTest {

	private String commentText;
	private StyleguideSection unit;
	
	@Before
	public void setup() {
		commentText = "# Form Button\n"
					+ "\n"
					+ "Your standard form button.\n"
					+ "\n"
					+ "Markup: <a class=\"button $modifierClass\">Button</a>\n"
					+ "\n"
					+ ":hover    - Highlights when hovering.\n"
					+ ":disabled - Dims the button when disabled.\n"
					+ ".primary  - Indicates button is the primary action.\n"
					+ ".smaller  - A smaller button\n"
					+ "\n"
					+ "Styleguide 2.1.1.\n";
		
		unit = new StyleguideSection(commentText, "example.css");
	}
	
	@Test
	public void parsesTheDescription() throws Exception {
		assertThat(unit.getDescription(), is("# Form Button\n\nYour standard form button."));
	}

	@Test
	public void parsesTheModifiers() throws Exception {
		assertThat(unit.getModifiers().size(), is(4));
	}
	
	@Test
	public void oarsesAModifiersNames() throws Exception {
		assertThat(unit.getModifiers().get(0).name, is(":hover"));
	}
	
	@Test
	public void parsesAModifiersDescription() throws Exception {
		assertThat(unit.getModifiers().get(0).description, is("Highlights when hovering."));
	}
	
	@Test
	public void parsesTheStyleguideReference() throws Exception {
		assertThat(unit.getSectionReference(), is("2.1.1"));
	}

	@Test
	public void parsesTheMarkup() throws Exception {
		assertThat(unit.getMarkup(), is("<a class=\"button $modifierClass\">Button</a>"));
	}
	
	@Test
	public void ignoresMarkupIfItIsntIncluded() throws Exception {
		String noMarkupText = "# Form Button\n"
				+ "\n"
				+ "Your standard form button.\n"
				+ "\n"
				+ ":hover    - Highlights when hovering.\n"
				+ ":disabled - Dims the button when disabled.\n"
				+ ".primary  - Indicates button is the primary action.\n"
				+ ".smaller  - A smaller button\n"
				+ "\n"
				+ "Styleguide 2.1.1.\n";
	
		StyleguideSection unit = new StyleguideSection(noMarkupText, "example.css");
		
		assertThat(unit.getMarkup(), is(nullValue()));
		assertThat(unit.getDescription(), is("# Form Button\n\nYour standard form button."));
		assertThat(unit.getModifiers().get(0).description, is("Highlights when hovering."));
		assertThat(unit.getSectionReference(), is("2.1.1"));
	}
	
	@Test
	public void parsesWordPhrasesAsStyleGuideReferences() throws Exception {
		String newCommentText = commentText.replace("2.1.1", "Buttons - Truly Lime");
		
		StyleguideSection unit = new StyleguideSection(newCommentText, "example.css");
		assertThat(unit.getSectionReference(), is("Buttons - Truly Lime"));
	}
}
