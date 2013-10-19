kss-java
========

A port of [github.com/kneath/kss] (v0.5.0) to Java

## What is it?

The [KSS spec](https://github.com/kneath/kss/blob/master/SPEC.md) defines a standard for documenting CSS (including LESS, SASS, and SCSS) 
that you can use to generate "living styleguides", just like you can [see on github](https://github.com/styleguide/css). 

## How do I use it?

The kss-java project gives you a parser that can understand the KSS specification.  So given:

```css
/*
A button suitable for giving stars to someone.

Markup: <a class="button -modifierClass"></a>

:hover             - Subtle hover highlight.
.stars-given       - A highlight indicating you've already given a star.
.stars-given:hover - Subtle hover highlight on top of stars-given styling.
.disabled          - Dims the button to indicate it cannot be used.

Styleguide 2.1.3.
*/
a.button.star{
  ...
}
a.button.star.stars-given{
  ...
}
a.button.star.disabled{
  ...
}
```

you can try:

```java
KssParser parser = new KssParser(new File("route/to/my/stylesheets/"));

StyleguideSection section = parser.getStyleguideSection("2.1.3");

section.getDescription(); // "A button suitable for giving stars to someone."
section.getSectionReference() ; // "2.1.3"
section.getModifiers().size(); // 4
section.getMarkup(); // "<a class="button -modifierClass"></a>"

Modifier modifier = section.getModifiers().get(0);
modifier.name; // ":hover"
modifier.description; // "Subtle hover highlight"

```

The `File` passed to the constructor defines a directory to recursively search for CSS, LESS, SASS or SCSS files. 
Instead of passing a `File`, you can also pass a `String` to be parsed.

If you are a user of Play! Framework 1.2, you can automagically create styleguides in your webapp with the 
[kss-play] (https://github.com/revbingo/kss-play) module
