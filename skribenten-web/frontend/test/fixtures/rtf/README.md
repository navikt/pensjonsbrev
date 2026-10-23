# RTF fixtures

Test files for pasting RTF into the letter editor (`src/Brevredigering/LetterEditor/actions/rtf/`). Most are copied
from the test suites of other open source RTF parsers, pinned to a commit. Files are verbatim copies unless noted.
Each subfolder from another project contains that project's `LICENSE`.

| Folder              | Source                                                                   | License                                | Copyright                         |
| ------------------- | ------------------------------------------------------------------------ | -------------------------------------- | --------------------------------- |
| `rtf2xml/`          | [paulhtremblay/rtf2xml](https://github.com/paulhtremblay/rtf2xml)        | MIT, [`LICENSE`](rtf2xml/LICENSE)         | (c) 2016 Paul Tremblay            |
| `rtfpipe/`          | [erdomke/RtfPipe](https://github.com/erdomke/RtfPipe)                    | MIT, [`LICENSE`](rtfpipe/LICENSE)         | (c) 2018 Eric Domke               |
| `rtfjs/`            | [tbluemel/rtf.js](https://github.com/tbluemel/rtf.js)                    | MIT, [`LICENSE`](rtfjs/LICENSE)           | (c) 2015 Thomas Bluemel           |
| `rtfstreamparser/`  | [mazira/rtf-stream-parser](https://github.com/mazira/rtf-stream-parser)  | MIT, [`LICENSE`](rtfstreamparser/LICENSE) | (c) 2016-2018 Mazira, LLC; example content (c) Microsoft, see below |
| `synthetic/`        | Written for Skribenten                                                   | Part of this repository                | Nav                               |

The expectations in the tests are Skribenten's supported subset; the HTML the other parsers produce is not used as an
oracle.

## rtf2xml/

[paulhtremblay/rtf2xml at fc3f36ba081e642926f89f7535ec48f3490d40be](https://github.com/paulhtremblay/rtf2xml/tree/fc3f36ba081e642926f89f7535ec48f3490d40be).
Each `<name>.rtf` is a copy of
`https://github.com/paulhtremblay/rtf2xml/blob/fc3f36ba081e642926f89f7535ec48f3490d40be/test/test_files/good/<name>.rtf`:

- Word: `bullet_list`, `char_upper_ranges`, `char_upper_ranges_hex`, `complex_list_bullet`,
  `complex_list_diff_styles`, `escaped_text`, `fields_simple_format_switch`, `heading_with_section`,
  `headings_mixed`, `hyperlink`, `inline_over_para`, `italics_plain`, `list_in_table`, `list_with_indented_items`,
  `lists_with_breaks`, `nested_lists_indents`, `sections_next_page`, `simple_page_break1`, `simple_page_break2`,
  `symbol`, `table_different_cell_widths`, `table_empty_row`, `table_simple`, `table_with_header`, `zapf_dingbats`
- OpenOffice: `open_office_hello_world`, `open_office_special_char`
- TextEdit on macOS: `os_x_text_edit_faces_mixed`, `os_x_text_edit_italics_mixed`

Used in `interpretNativeRtf.reference.test.ts` and (`complex_list_bullet`) `paste.rtf.test.ts`.

## rtfpipe/

[erdomke/RtfPipe at 9851ab97d693037073d567d74346184ae2f3d4b3](https://github.com/erdomke/RtfPipe/tree/9851ab97d693037073d567d74346184ae2f3d4b3):

- `Headings`, `List`, `ListGoogle`, `ListWord`, `merged_cells`: copies of
  `https://github.com/erdomke/RtfPipe/blob/9851ab97d693037073d567d74346184ae2f3d4b3/RtfPipe.Tests/Files/<name>.rtf`
- `nested_lists_indents_word`, `outline_list`: copies of
  `https://github.com/erdomke/RtfPipe/blob/9851ab97d693037073d567d74346184ae2f3d4b3/RtfPipe.Tests/Files/rtf2xml/<name>.rtf`
  (RtfPipe keeps these among its rtf2xml test files)

Used in `interpretNativeRtf.reference.test.ts`.

## rtfjs/

[tbluemel/rtf.js at 85fddf55b2f262bfd450769120c18c9ccef1f21c](https://github.com/tbluemel/rtf.js/tree/85fddf55b2f262bfd450769120c18c9ccef1f21c):

- `hyperlink`, `implicit-par`, `pile-of-poo`, `sample` (Microsoft Word 11): `<name>.rtf` is a copy of
  `https://github.com/tbluemel/rtf.js/blob/85fddf55b2f262bfd450769120c18c9ccef1f21c/test/rtf-test-files/<name>/source.rtf`
- `simple5` (Msftedit 5.41, i.e. WordPad/RichEdit): copy of
  `https://github.com/tbluemel/rtf.js/blob/85fddf55b2f262bfd450769120c18c9ccef1f21c/samples/.common/data/rtf/simple5.rtf`
- `wmf-and-emf-excerpt` (Microsoft Word, numbered lists): **excerpt**, not verbatim, of
  `https://github.com/tbluemel/rtf.js/blob/85fddf55b2f262bfd450769120c18c9ccef1f21c/test/rtf-test-files/wmf-and-emf/source.rtf`.
  Trimmed from 1.7 MB to the header, list tables and one list section; images and CRs removed.

Used in `interpretNativeRtf.reference.test.ts` and `interpretNativeRtf.fixtures.test.ts`.

## rtfstreamparser/

[mazira/rtf-stream-parser at f112deb2cd93797a2dbc28aa38d8e143b86a79d4](https://github.com/mazira/rtf-stream-parser/tree/f112deb2cd93797a2dbc28aa38d8e143b86a79d4):

- `encapsulated.rtf`, `encapsulated.html`: copies of
  `https://github.com/mazira/rtf-stream-parser/blob/f112deb2cd93797a2dbc28aa38d8e143b86a79d4/test/examples/encapsulated.<ext>`

Used in `extractEncapsulation.test.ts`, together with inline snippets copied from
[`test/de-encapsulate.test.ts`](https://github.com/mazira/rtf-stream-parser/blob/f112deb2cd93797a2dbc28aa38d8e143b86a79d4/test/de-encapsulate.test.ts).

**Microsoft content:** both files are the example in section 3.1 ("Encapsulating HTML into RTF") of
[MS-OXRTFEX: Rich Text Format (RTF) Extensions Algorithm](https://learn.microsoft.com/en-us/openspecs/exchange_server_protocols/ms-oxrtfex),
and its expected output. The example is covered by Microsoft copyrights, not by the MIT license of
rtf-stream-parser. The Intellectual Property Rights Notice for Open Specifications Documentation, at the start of the
specification, permits this use:

> Copyrights. This documentation is covered by Microsoft copyrights. Regardless of any other terms that are contained
> in the terms of use for the Microsoft website that hosts this documentation, you can make copies of it in order to
> develop implementations of the technologies that are described in this documentation and can distribute portions of
> it in your implementations that use these technologies or in your documentation as necessary to properly document
> the implementation. You can also distribute in your implementation, with or without modification, any schemas, IDLs,
> or code samples that are included in the documentation. […]

## synthetic/

Written for Skribenten, since no freely licensed modern clipboard captures were found:

- `word365-nb.rtf`: structured according to the Word 365 clipboard format (Norwegian Bokmål). Used in
  `interpretNativeRtf.fixtures.test.ts` and `paste.rtf.test.ts`.
- `outlook365-fromhtml.rtf`: structured according to the Outlook 365 clipboard format (HTML encapsulated in RTF). Used
  in `extractEncapsulation.test.ts` and `paste.rtf.test.ts`.

## Trying a fixture in the browser

`rtf-file-to-clipboard.sh` (macOS) puts a fixture on the clipboard as RTF, so it can be pasted into the editor. Run
it without arguments to pick a fixture; see the script header for options.
