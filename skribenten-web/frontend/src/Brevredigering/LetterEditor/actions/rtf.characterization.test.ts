// Temporary characterization safety net for the RTF refactor. Deleted when the refactor is done.
import { expect, test } from "vitest";

import { extractEncapsulatedContent } from "~/Brevredigering/LetterEditor/actions/rtf/extractEncapsulation";
import { interpretNativeRtf } from "~/Brevredigering/LetterEditor/actions/rtf/interpretNativeRtf";
import outlookEncapsulatedHtml from "~test/fixtures/rtf/outlook-encapsulated-html.rtf?raw";
import outlook365 from "~test/fixtures/rtf/outlook365-fromhtml.rtf?raw";
import wordpad from "~test/fixtures/rtf/richedit-wordpad.rtf?raw";
import wordNumberedLists from "~test/fixtures/rtf/word-numbered-lists.rtf?raw";
import word365 from "~test/fixtures/rtf/word365-nb.rtf?raw";
import word2003Sample from "~test/fixtures/rtf/word2003-sample.rtf?raw";

const H = String.raw`{\rtf1\ansi\ansicpg1252\deff0{\fonttbl{\f0 Calibri;}}`;

const corpus: Record<string, string> = {
  "fixture outlook-encapsulated-html.rtf": outlookEncapsulatedHtml,
  "fixture outlook365-fromhtml.rtf": outlook365,
  "fixture richedit-wordpad.rtf": wordpad,
  "fixture word-numbered-lists.rtf": wordNumberedLists,
  "fixture word2003-sample.rtf": word2003Sample,
  "fixture word365-nb.rtf": word365,
  "cp1257 hex": String.raw`{\rtf1\ansi\ansicpg1257 \pard \'e0\'fe\par}`,
  "cp932 DBCS": String.raw`{\rtf1\ansi\ansicpg932 \pard \'93\'fa\'96\'7b\par}`,
  "ansicpg in body text": String.raw`{\rtf1\ansi Tekst \\ansicpg1251 \'e6\par}`,
  "hidden + deleted": H + String.raw`\pard Synlig {\v skjult }{\deleted slettet }tekst\par}`,
  "pntext bullets (WordPad)":
    H +
    String.raw`{\pntext\f1\'b7\tab}{\*\pn\pnlvlblt\pnf1\pnindent0{\pntxtb\'b7}}\pard En\par{\pntext\f1\'b7\tab}Two\par}`,
  "pn numbered without pntext": H + String.raw`{\*\pn\pnlvlbody\pndec}\pard Punkt\par}`,
  "star listtext numbered": String.raw`{\rtf1\ansi\pard\ls2{\*\listtext 1.\tab}First\par\pard\ls2{\*\listtext 2.\tab}Second\par}`,
  "table trhdr":
    H +
    String.raw`\trowd\trhdr\cellx1000\cellx2000\pard\intbl A\cell B\cell\row\trowd\cellx1000\cellx2000\pard\intbl 1\cell 2\cell\row\pard Etter\par}`,
  "table without intbl": String.raw`{\rtf1\ansi\trowd\cellx2000\cellx4000 A\cell B\cell\row\trowd\cellx2000\cellx4000 C\cell D\cell\row}`,
  "nested table":
    H +
    String.raw`\trowd\cellx2000\pard\intbl Ytre {\*\nesttableprops\trowd\cellx1000\nestrow}{\nonesttables Ikke}\itap2 Indre\nestcell\itap1\cell\row\pard\par}`,
  "edge blank pars": H + String.raw`\pard\par Tekst\par\par}`,
  "empty list item mid-list":
    H + String.raw`\pard\ls1{\listtext\'b7\tab}a\par\pard\ls1{\listtext\'b7\tab}\par\pard\ls1{\listtext\'b7\tab}b\par}`,
  "multi-space and tabs": H + String.raw`\pard Hei  \tab  der\par}`,
  "footnote and annotation": H + String.raw`\pard Tekst{\*\footnote Fotnote}{\*\annotation Kommentar} slutt\par}`,
  "field link text": H + String.raw`\pard Se {\field{\*\fldinst HYPERLINK "https://nav.no"}{\fldrslt nav.no}} her\par}`,
  "numbered heading":
    H +
    String.raw`{\stylesheet{\s1\outlinelevel0 heading 1;}}\pard\s1\ls1{\listtext 1\tab}Innledning\par\pard Tekst\par}`,
  "fromhtml1 with nested signature": String.raw`{\rtf1\ansi\fromhtml1 {\*\htmltag <p>}Hei{\*\htmltag </p>}{\rtf1 Signatur\par}}`,
  "fromhtml1 outside header": String.raw`{\rtf1\ansi{\fonttbl{\f0 A;}}\pard Tekst\par{\*\x\fromhtml1}}`,
  fromtext: String.raw`{\rtf1\ansi\fromtext1 Line one\par Line two}`,
  "u with hex fallback": H + String.raw`\pard \u248\'f8 og \uc2\u229\'e5\'e5 ok\par}`,
  "trailing paragraph without par": String.raw`{\rtf1\ansi\pard First\par\pard No trailing par}`,
  "partial selection": String.raw`{\rtf1\ansi\ansicpg1252\deflang1033\b  ikke}`,
  "not rtf": "hello {world",
  truncated: H + String.raw`\pard Hei {\b der`,
  "bold+italic": H + String.raw`\pard {\b\i begge}\par}`,
  "char style in stylesheet": String.raw`{\rtf1{\stylesheet{\s0 Normal;}{\*\cs10\outlinelevel0 heading 1 Char;}}\pard\cs10 Tekst\par}`,
  empty: "",
  "header only": String.raw`{\rtf1\ansi\ansicpg1252\deflang1033{\fonttbl{\f0 Calibri;}}}`,
  "bin data": H + String.raw`\pard A{\*\blipuid 1}{\pict\bin4 abcd}B\par}`,
};

function parse(rtf: string): unknown {
  return extractEncapsulatedContent(rtf) ?? interpretNativeRtf(rtf);
}

test("RTF clipboard parsing is unchanged", async () => {
  const result = Object.fromEntries(Object.entries(corpus).map(([name, rtf]) => [name, parse(rtf)]));
  await expect(JSON.stringify(result, null, 1)).toMatchFileSnapshot("./__characterization__/rtf.snap.txt");
});
