/**
 * Sources:
 * - outlook-encapsulated-html.rtf/.html: the MS-OXRTFEX spec example and its expected output, from
 *   mazira/rtf-stream-parser@f112deb test/examples/, MIT, https://github.com/mazira/rtf-stream-parser
 * - Inline snippets marked "fra Outlook" are copied from rtf-stream-parser's
 *   test/de-encapsulate.test.ts (MIT), where they are noted as seen in the wild.
 * - outlook365-fromhtml.rtf: syntetisk, etter strukturen i Outlook 365-utklipp.
 */
import { describe, expect, test } from "vitest";

import { extractEncapsulatedContent as extractFromTokens } from "~/Brevredigering/LetterEditor/actions/rtf/extractEncapsulation";
import { createByteDecoder } from "~/Brevredigering/LetterEditor/actions/rtf/rtfDecoding";
import { tokenizeRtf } from "~/Brevredigering/LetterEditor/actions/rtf/tokenizeRtf";
import specExpectedHtml from "~test/fixtures/rtf/outlook-encapsulated-html.html?raw";
import specExample from "~test/fixtures/rtf/outlook-encapsulated-html.rtf?raw";
import outlook365 from "~test/fixtures/rtf/outlook365-fromhtml.rtf?raw";

const extractEncapsulatedContent = (rtf: string) => extractFromTokens(tokenizeRtf(rtf), createByteDecoder(rtf));

const html = (rtf: string) => {
  const result = extractEncapsulatedContent(rtf);
  return result?.format === "html" ? result.html : undefined;
};

describe("extractEncapsulatedContent", () => {
  test("restores the MS-OXRTFEX spec example exactly", () => {
    expect(html(specExample)).toBe(specExpectedHtml);
  });

  test("restores an Outlook 365 mail body and drops the RTF duplicates", () => {
    const result = html(outlook365) ?? "";

    expect(result).toContain("<h1>Vedtak om alderspensjon<o:p></o:p></h1>");
    expect(result).toContain("Vi har <b>innvilget</b> søknaden din om <i>alderspensjon</i> fra 1.&nbsp;mai 2026.");
    expect(result).toContain('<ul style="margin-top:0cm" type="disc"><li class="MsoListParagraph"');
    expect(result).toContain("Utbetaling i utlandet skjer i euro (€).<o:p></o:p></li></ol>");
    expect(result).toContain("<th ");
    expect(result).toContain('<p class="MsoNormal">kr 25&nbsp;000<o:p></o:p></p></td>');
    expect(result).toContain('Les mer på <a href="https://www.nav.no">nav.no</a>.');
    // RTF-only duplicates under \htmlrtf, list markers, the image blob and {\*\mhtmltag} copies are gone.
    expect(result).not.toContain("\u00B7");
    expect(result).not.toContain("1.\t");
    expect(result).not.toContain("89504e47");
    expect(result.match(/<img /g)).toHaveLength(1);
  });

  test("returns undefined for RTF that is not encapsulated", () => {
    expect(extractEncapsulatedContent("{\\rtf1\\ansi\\deff0 {\\fonttbl{\\f0 Arial;}}Hei\\par}")).toBeUndefined();
    expect(extractEncapsulatedContent("")).toBeUndefined();
  });

  test("requires \\fromhtml1 in the header, before any group or text", () => {
    expect(extractEncapsulatedContent("{\\rtf1\\ansi{\\fonttbl}\\fromhtml1{\\*\\htmltag <p>}}")).toBeUndefined();
    expect(extractEncapsulatedContent("{\\rtf1\\ansi tekst\\fromhtml1{\\*\\htmltag <p>}}")).toBeUndefined();
    expect(extractEncapsulatedContent("{\\rtf1\\ansi\\fromhtml0{\\*\\htmltag <p>}}")).toBeUndefined();
  });

  test("restores encapsulated plain text (\\fromtext)", () => {
    expect(
      extractEncapsulatedContent("{\\rtf1\\ansi\\fromtext{\\fonttbl{\\f0 Arial;}}Linje 1\\par <ikke html>}"),
    ).toEqual({
      format: "text",
      text: "Linje 1\r\n<ikke html>",
    });
  });

  test("escapes text outside htmltag, and keeps markup inside it", () => {
    expect(html("{\\rtf1\\ansi\\fromhtml1{\\*\\htmltag <p>}a < b & c{\\*\\htmltag </p>}}")).toBe(
      "<p>a &lt; b &amp; c</p>",
    );
  });

  test("decodes escapes inside htmltag (fra Outlook)", () => {
    expect(html("{\\rtf1\\ansi\\ansicpg1252\\fromhtml1\\t6\\t7{\\*\\htmltag <sometag\\{/>}}")).toBe("<sometag{/>");
    expect(html("{\\rtf1\\ansi\\ansicpg1252\\fromhtml1\\t6\\t7{\\*\\htmltag \\lquote hi\\bullet}}")).toBe("‘hi•");
    expect(html("{\\rtf1\\ansi\\ansicpg1253\\fromhtml1\\t6\\t7{\\*\\htmltag\\'f0}}")).toBe("π");
  });

  test("suppresses \\htmlrtf content inside htmltag (fra Outlook)", () => {
    const rtf =
      "{\\rtf1\\ansi\\ansicpg1252\\fromhtml1\\t6\\t7" +
      "{\\*\\htmltag241 <!--[if gte mso 15]>&nbsp;\\htmlrtf .\\u32  \\htmlrtf0<![endif]-->}}";

    expect(html(rtf)).toBe("<!--[if gte mso 15]>&nbsp;<![endif]-->");
  });

  test("keeps link text from \\fldrslt and drops the field instruction (fra Outlook)", () => {
    const rtf = [
      "{\\rtf1\\ansi\\ansicpg1252\\fromhtml1\\t6\\t7",
      '{\\*\\htmltag84 <a href="mailto:address@emailhost.net">}',
      '\\htmlrtf {\\field{\\*\\fldinst{HYPERLINK "mailto:address@emailhost.net"}}',
      "{\\fldrslt\\cf1\\ul \\htmlrtf0 address@emailhost.net\\htmlrtf }\\htmlrtf0 \\htmlrtf }\\htmlrtf0",
      "{\\*\\htmltag92 </a>}",
      "}",
    ].join("");

    expect(html(rtf)).toBe('<a href="mailto:address@emailhost.net">address@emailhost.net</a>');
  });

  test("drops \\pntext list markers (fra Outlook)", () => {
    const rtf = [
      "{\\rtf1\\ansi\\ansicpg1252\\fromhtml1\\t5\\t6\\t7",
      "{\\*\\htmltag64 <li>}",
      "\\htmlrtf {{\\*\\pn\\pnlvlblt\\pnf2\\pnindent360{\\pntxtb\\'b7}}\\htmlrtf0 \\li360 \\fi-360 {\\pntext *\\tab}Item 1",
      "{\\*\\htmltag244 <o:p>}",
      "{\\*\\htmltag252 </o:p>}",
      "\\htmlrtf\\par}\\htmlrtf0",
      "{\\*\\htmltag72 </li>}",
      "}",
    ].join("");

    expect(html(rtf)).toBe("<li>Item 1<o:p></o:p></li>");
  });

  test("ignores unknown \\* destinations such as \\formatConverter (fra Outlook)", () => {
    const rtf =
      "{\\rtf1\\ansi\\fbidis\\ansicpg936\\deff0\\fromhtml1{\\fonttbl}" +
      "{\\*\\generator Microsoft Exchange Server;}{\\*\\formatConverter converted from html;}}";

    expect(html(rtf)).toBe("");
  });

  test("appends a trailing nested RTF signature as text (fra Outlook)", () => {
    const rtf =
      String.raw`{\rtf1\ansi\ansicpg1252\fromhtml1 \deff0 {\fonttbl {\f0\fswiss\fcharset0 Arial;} {\f1\fmodern Courier New;} {\f2\fnil\fcharset2 Symbol;} {\f3\fmodern\fcharset0 Courier New;} }` +
      String.raw`{\colortbl\red0\green0\blue0;\red0\green0\blue255;}\pard\plain\deftab360 \f0\fs24` +
      String.raw`{\*\htmltag19 <html>}{\*\htmltag34 <head>}{\*\htmltag41 </head>}{\*\htmltag50 <body>}` +
      String.raw`{\htmlrtf0 hello\htmlrtf}\htmlrtf0 ` +
      String.raw`{\*\htmltag58 </body>}{\*\htmltag27 </html>}` +
      String.raw`{\rtf1\ansi\ansicpg1252\deff0\deflang1033 {\fonttbl {\f0\fnil\fcharset0 ; } }\plain ******\par Only the individual sender is responsible for the content of the\par message.}}`;

    expect(html(rtf)).toBe(
      "<html><head></head><body>hello</body></html>******\r\nOnly the individual sender is responsible for the content of the\r\nmessage.",
    );
  });
});
