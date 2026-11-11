import { describe, expect, test } from "vitest";

import { projectElements } from "~test/support/pasteTestUtils";

import { htmlToElements } from "./htmlToElements";

const parse = (html: string) => projectElements(htmlToElements(html));

describe("htmlToElements", () => {
  describe("inline text", () => {
    test("spans become plain text", () => {
      expect(parse("<span>1</span>")).toEqual(["TEXT: 1"]);
    });

    test("bold and italic", () => {
      expect(
        parse("<b>fet</b><span>-</span><strong>sterk</strong><i>kursiv</i><span>-</span><em>uthevet</em>"),
      ).toEqual(["TEXT: **fet**", "TEXT: -", "TEXT: **sterk**", "TEXT: _kursiv_", "TEXT: -", "TEXT: _uthevet_"]);
    });

    test("the first emphasis wins when bold and italic are nested", () => {
      expect(parse("<b><i>fet</i></b><i><b>kursiv</b></i>")).toEqual(["TEXT: **fet**", "TEXT: _kursiv_"]);
    });

    test("neighbouring text with the same font is merged", () => {
      expect(parse("<span>1<span></span></span><span>2<span></span></span")).toEqual(["TEXT: 12"]);
    });

    test("whitespace is collapsed", () => {
      expect(parse("<span>a  \n b</span>\n  <span> </span>")).toEqual(["TEXT: a b "]);
    });

    test("text before a paragraph is moved into it", () => {
      expect(parse("tekst<p>p1</p>etter")).toEqual(["P: tekstp1", "TEXT: etter"]);
    });

    test("bold text around a paragraph", () => {
      expect(parse("<strong>b1 b2<p>44</p></strong><span>s1</span>")).toEqual(["P: **b1 b244**", "TEXT: s1"]);
    });
  });

  describe("blocks", () => {
    test("paragraphs and headings", () => {
      expect(parse("<h1>T1</h1><h2>T2</h2><h3>T3</h3><p>P1</p>")).toEqual(["H1: T1", "H2: T2", "H3: T3", "P: P1"]);
    });

    test("an empty paragraph is kept as a line break", () => {
      expect(parse("<p>a</p><p></p><p>b</p>")).toEqual(["P: a", "P: ", "P: b"]);
    });

    test("h4 to h6 are not supported and become text", () => {
      expect(parse("<h4>T4</h4><h5>T5</h5><h6>T6</h6>")).toEqual(["TEXT: T4T5T6"]);
    });

    test("headings keep the text of nested paragraphs and lists", () => {
      expect(parse("<h2>a<p>b</p><ul><li>c</li></ul></h2>")).toEqual(["H2: abc"]);
    });

    test("lists and headings inside a paragraph are lifted out of it", () => {
      expect(parse("<p>før<ul><li>punkt</li></ul>etter</p>")).toEqual(["P: før", "• punkt", "P: etter"]);
    });

    test("Google Docs markup", () => {
      expect(
        parse(
          "<div><p><span><span>Punktliste</span></span><span> </span></p></div><div><ul><li><p><span><span>Første punkt</span></span><span> </span></p></li></ul></div><div><ul><li><p><span><span>Andre punkt</span></span><span> </span></p></li></ul></div>",
        ),
      ).toEqual(["P: Punktliste ", "• Første punkt ", "• Andre punkt "]);
    });

    test("scripts, images and unknown attributes are removed", () => {
      expect(parse('<p onclick="x()">a<img src=x onerror=alert(1)><script>alert(1)</script></p>')).toEqual(["P: a"]);
    });
  });

  describe("lists", () => {
    test("list type follows the tag", () => {
      expect(parse("<ul><li>1</li></ul><ol><li>2</li></ol>")).toEqual(["• 1", "1. 2"]);
    });

    test("an empty item is kept", () => {
      expect(parse("<ul><li></li></ul>")).toEqual(["• "]);
    });

    test("nested lists are flattened and marked as nested", () => {
      expect(parse("<ul><li>A<ul><li>B</li><li>C</li></ul></li><li>D</li></ul>")).toEqual([
        "• A",
        "• (n) B",
        "• (n) C",
        "• D",
      ]);
    });

    test("nested items keep their own list type", () => {
      expect(parse("<ul><li>A<ol><li>B</li></ol></li></ul>")).toEqual(["• A", "1. (n) B"]);
    });

    test("three levels of nesting", () => {
      expect(parse("<ul><li>A<ul><li>B<ul><li>C</li></ul></li></ul></li></ul>")).toEqual(["• A", "• (n) B", "• (n) C"]);
    });

    test("text after a nested list becomes its own item", () => {
      expect(parse("<ul><li>before<ul><li>nested</li></ul>after</li></ul>")).toEqual([
        "• before",
        "• (n) nested",
        "• after",
      ]);
    });

    test("an item with only a nested list has no empty parent item", () => {
      expect(parse("<ul><li><ul><li>B</li></ul></li></ul>")).toEqual(["• (n) B"]);
    });

    test("paragraphs and headings in an item become its text, tables are dropped", () => {
      expect(parse("<ul><li><p>a</p><h1>b</h1><table><tr><td>c</td></tr></table></li></ul>")).toEqual(["• ab"]);
    });
  });

  describe("tables", () => {
    test("th cells in the first row become the header", () => {
      expect(parse("<table><tr><th>A</th><th>B</th></tr><tr><td>1</td><td>2</td></tr></table>")).toEqual([
        "TABLE",
        "  th: A | B",
        "  tr: 1 | 2",
      ]);
    });

    test("thead is used as header", () => {
      expect(parse("<table><thead><tr><th>H</th></tr></thead><tbody><tr><td>A</td></tr></tbody></table>")).toEqual([
        "TABLE",
        "  th: H",
        "  tr: A",
      ]);
    });

    test("without th there is no header", () => {
      expect(parse("<table><tr><td>A</td><td>B</td></tr><tr><td>C</td></tr></table>")).toEqual([
        "TABLE",
        "  tr: A | B",
        "  tr: C",
      ]);
    });

    test("a malformed table with an image is sanitized", () => {
      expect(
        parse("<table>\n<tr><th>Header 1</th></tr\n<tr><td>Row 1</td></tr>\n<img src=x onerror=alert(1)>\n</table>"),
      ).toEqual(["TABLE", "  th: Header 1", "  tr: Row 1"]);
    });

    test("cell content is flattened to text and whitespace runs are dropped", () => {
      expect(parse("<table><tr><td><p>a</p> <ul><li>b</li></ul><h1>c</h1></td></tr></table>")).toEqual([
        "TABLE",
        "  tr: ab",
      ]);
    });

    test("an empty table has no rows", () => {
      expect(parse("<table></table>")).toEqual(["TABLE"]);
    });

    test("a table inside a paragraph is dropped", () => {
      expect(parse("<p>a<table><tr><td>x</td></tr></table>b</p>")).toEqual(["P: a", "P: b"]);
    });
  });
});
