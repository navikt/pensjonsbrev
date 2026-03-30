package no.nav.pensjon.brev.pdfbygger.typst

/**
 * A string that has already been escaped for Typst content mode.
 * Use this to safely pass pre-escaped content without risk of double-escaping.
 */
@JvmInline
internal value class EscapedTypstContent(val value: String)

/**
 * Characters that should be stripped from output.
 *
 * Includes:
 * - C0 control characters (0-31, except HT=9, LF=10, CR=13)
 * - DEL (127)
 * - C1 control characters (0x80-0x9F)
 * - Undefined/unassigned code points and gaps within script blocks
 * - Characters not covered by installed fonts (fonts-noto-core)
 * - CJK and related blocks (would need fonts-noto-cjk)
 * - Private Use Area (no standard font covers these)
 * - Halfwidth/Fullwidth Forms and Specials
 *
 * Non-CJK scripts (Arabic, Hebrew, Devanagari, Thai, etc.) are mostly covered by the
 * fonts-noto-core package installed in the Docker image, combined with Typst's
 * built-in font fallback mechanism. Individual undefined/unassigned gaps within those
 * blocks are listed explicitly below.
 */
internal val CHARACTER_BLOCKLIST: HashSet<Int> = hashSetOf<Int>().apply {
    // --- C0 Controls / Basic Latin ---
    addAll(0x0000..0x0008); add(0x000B); addAll(0x000E..0x001F); addAll(0x007F..0x009F)

    // --- Greek and Coptic ---
    addAll(0x0378..0x0379); addAll(0x0380..0x0383); add(0x038B); add(0x038D); add(0x03A2)

    // --- Armenian ---
    add(0x0530); addAll(0x0557..0x0558); addAll(0x058B..0x058C)

    // --- Hebrew ---
    add(0x0590); addAll(0x05C8..0x05CF); addAll(0x05EC..0x05EE); add(0x05F6); addAll(0x05F8..0x05FF)

    // --- Arabic ---
    add(0x061D); addAll(0x06E9..0x06ED)

    // --- Syriac ---
    add(0x070E); addAll(0x074B..0x074C)

    // --- Thaana ---
    addAll(0x07B2..0x07BF)

    // --- NKo ---
    addAll(0x07FB..0x07FC)

    // --- Samaritan ---
    addAll(0x082E..0x082F); add(0x083F)

    // --- Mandaic ---
    addAll(0x085C..0x085D); addAll(0x085F..0x089F)

    // --- Arabic Extended-A ---
    add(0x08B5); addAll(0x08BF..0x08D2)

    // --- Bengali ---
    add(0x0984); addAll(0x098D..0x098E); addAll(0x0991..0x0992); add(0x09A9); add(0x09B1); addAll(0x09B3..0x09B5)
    addAll(0x09BA..0x09BB); addAll(0x09C5..0x09C6); addAll(0x09C9..0x09CA); addAll(0x09CF..0x09D6)
    addAll(0x09D8..0x09DB); add(0x09DE); addAll(0x09E4..0x09E5); addAll(0x09FF..0x0A00)

    // --- Gurmukhi ---
    add(0x0A04); addAll(0x0A0B..0x0A0E); addAll(0x0A11..0x0A12); add(0x0A29); add(0x0A31); add(0x0A34); add(0x0A37)
    addAll(0x0A3A..0x0A3B); add(0x0A3D); addAll(0x0A43..0x0A46); addAll(0x0A49..0x0A4A); addAll(0x0A4E..0x0A50)
    addAll(0x0A52..0x0A58); add(0x0A5D); addAll(0x0A5F..0x0A65); addAll(0x0A77..0x0A80)

    // --- Gujarati ---
    add(0x0A84); add(0x0A8E); add(0x0A92); add(0x0AA9); add(0x0AB1); add(0x0AB4); addAll(0x0ABA..0x0ABB); add(0x0AC6)
    add(0x0ACA); addAll(0x0ACE..0x0ACF); addAll(0x0AD1..0x0ADF); addAll(0x0AE4..0x0AE5); addAll(0x0AF2..0x0AF8)

    // --- Oriya ---
    add(0x0B00); add(0x0B04); addAll(0x0B0D..0x0B0E); addAll(0x0B11..0x0B12); add(0x0B29); add(0x0B31); add(0x0B34)
    addAll(0x0B3A..0x0B3B); addAll(0x0B45..0x0B46); addAll(0x0B49..0x0B4A); addAll(0x0B4E..0x0B55)
    addAll(0x0B58..0x0B5B); add(0x0B5E); addAll(0x0B64..0x0B65); addAll(0x0B78..0x0B81)

    // --- Tamil ---
    add(0x0B84); addAll(0x0B8B..0x0B8D); add(0x0B91); addAll(0x0B96..0x0B98); add(0x0B9B); add(0x0B9D)
    addAll(0x0BA0..0x0BA2); addAll(0x0BA5..0x0BA7); addAll(0x0BAB..0x0BAD); addAll(0x0BBA..0x0BBD)
    addAll(0x0BC3..0x0BC5); add(0x0BC9); addAll(0x0BCE..0x0BCF); addAll(0x0BD1..0x0BD6); addAll(0x0BD8..0x0BE5)
    addAll(0x0BFB..0x0BFF)

    // --- Telugu ---
    add(0x0C0D); add(0x0C11); add(0x0C29); addAll(0x0C3A..0x0C3C); add(0x0C45); add(0x0C49); addAll(0x0C4E..0x0C54)
    add(0x0C57); addAll(0x0C5B..0x0C5F); addAll(0x0C64..0x0C65); addAll(0x0C70..0x0C76)

    // --- Kannada ---
    add(0x0C8D); add(0x0C91); add(0x0CA9); add(0x0CB4); addAll(0x0CBA..0x0CBB); add(0x0CC5); add(0x0CC9)
    addAll(0x0CCE..0x0CD4); addAll(0x0CD7..0x0CDD); add(0x0CDF); addAll(0x0CE4..0x0CE5); add(0x0CF0)
    addAll(0x0CF3..0x0CFF)

    // --- Malayalam ---
    add(0x0D0D); add(0x0D11); add(0x0D45); add(0x0D49); addAll(0x0D50..0x0D53); addAll(0x0D64..0x0D65)

    // --- Sinhala ---
    add(0x0D80); add(0x0D84); addAll(0x0D97..0x0D99); add(0x0DB2); add(0x0DBC); addAll(0x0DBE..0x0DBF)
    addAll(0x0DC7..0x0DC9); addAll(0x0DCB..0x0DCE); add(0x0DD5); add(0x0DD7); addAll(0x0DE0..0x0DE5)
    addAll(0x0DF0..0x0DF1); addAll(0x0DF5..0x0E00)

    // --- Thai ---
    addAll(0x0E3B..0x0E3E); addAll(0x0E5C..0x0E80)

    // --- Lao ---
    add(0x0E83); add(0x0E85); add(0x0E8B); add(0x0EA4); add(0x0EA6); addAll(0x0EBE..0x0EBF); add(0x0EC5); add(0x0EC7)
    addAll(0x0ECE..0x0ECF); addAll(0x0EDA..0x0EDB); addAll(0x0EE0..0x0EFF)

    // --- Tibetan ---
    add(0x0F48); addAll(0x0F6D..0x0F70); add(0x0F98); add(0x0FBD); add(0x0FCD); addAll(0x0FDB..0x0FFF)

    // --- Georgian ---
    add(0x10C6); addAll(0x10C8..0x10CC); addAll(0x10CE..0x10CF)

    // --- Hangul Jamo ---
    addAll(0x1100..0x115E); addAll(0x1161..0x11FF)

    // --- Ethiopic ---
    add(0x1249); addAll(0x124E..0x124F); add(0x1257); add(0x1259); addAll(0x125E..0x125F); add(0x1289)
    addAll(0x128E..0x128F); add(0x12B1); addAll(0x12B6..0x12B7); add(0x12BF); add(0x12C1); addAll(0x12C6..0x12C7)
    add(0x12D7); add(0x1311); addAll(0x1316..0x1317); addAll(0x135B..0x135C); addAll(0x137D..0x137F)

    // --- Ethiopic Supplement ---
    addAll(0x139A..0x139F)

    // --- Cherokee ---
    addAll(0x13FE..0x13FF)

    // --- Ogham ---
    add(0x1680); addAll(0x169D..0x169F)

    // --- Runic ---
    addAll(0x16F9..0x16FF)

    // --- Tagalog ---
    add(0x170D); addAll(0x1715..0x171F)

    // --- Hanunoo ---
    addAll(0x1737..0x173F)

    // --- Buhid ---
    addAll(0x1754..0x175F)

    // --- Tagbanwa ---
    add(0x176D); add(0x1771); addAll(0x1774..0x177F)

    // --- Khmer ---
    addAll(0x17DE..0x17DF); addAll(0x17EA..0x17EF); addAll(0x17FA..0x17FF)

    // --- Mongolian (entire block — no Noto Mongolian font available) ---
    addAll(0x1800..0x18AF)

    // --- Unified Canadian Aboriginal Syllabics Extended ---
    addAll(0x18F6..0x18FF)

    // --- Limbu ---
    add(0x191F); addAll(0x192C..0x192F); addAll(0x193C..0x193F); addAll(0x1941..0x1943)

    // --- Tai Le ---
    addAll(0x196E..0x196F); addAll(0x1975..0x197F)

    // --- New Tai Lue ---
    addAll(0x19AC..0x19AF); addAll(0x19CA..0x19CF); addAll(0x19DB..0x19DD)

    // --- Buginese ---
    addAll(0x1A1C..0x1A1D)

    // --- Tai Tham ---
    add(0x1A5F); addAll(0x1A7D..0x1A7E); addAll(0x1A8A..0x1A8F); addAll(0x1A9A..0x1A9F); addAll(0x1AAE..0x1AFF)

    // --- Balinese ---
    addAll(0x1B4C..0x1B4F); addAll(0x1B7D..0x1B7F)

    // --- Batak ---
    addAll(0x1BF4..0x1BFB)

    // --- Lepcha ---
    addAll(0x1C38..0x1C3A); addAll(0x1C4A..0x1C4C)

    // --- Cyrillic Extended-C ---
    addAll(0x1C89..0x1C8F)

    // --- Georgian Extended ---
    addAll(0x1CBB..0x1CBC)

    // --- Sundanese Supplement ---
    addAll(0x1CC7..0x1CFF)

    // --- Phonetic Extensions Supplement ---
    addAll(0x1DBF..0x1DFA)

    // --- Greek Extended ---
    addAll(0x1F16..0x1F17); addAll(0x1F1E..0x1F1F); addAll(0x1F46..0x1F47); addAll(0x1F4E..0x1F4F); add(0x1F58)
    add(0x1F5A); add(0x1F5C); add(0x1F5E); addAll(0x1F7E..0x1F7F); add(0x1FB5); add(0x1FC5); addAll(0x1FD4..0x1FD5)
    add(0x1FDC); addAll(0x1FF0..0x1FF1); add(0x1FF5); add(0x1FFF)

    // --- Superscripts and Subscripts ---
    addAll(0x2072..0x2073); add(0x208F); addAll(0x209D..0x209F)

    // --- Currency Symbols ---
    addAll(0x20C1..0x20FF)

    // --- Number Forms ---
    addAll(0x218C..0x218F)

    // --- Control Pictures ---
    addAll(0x2427..0x243F)

    // --- Optical Character Recognition ---
    addAll(0x244B..0x245F)

    // --- Dingbats ---
    add(0x2705); add(0x270A); add(0x2728); add(0x274C); add(0x274E); addAll(0x2754..0x2755); addAll(0x2795..0x2797)
    add(0x27B0)

    // --- Miscellaneous Symbols and Arrows ---
    addAll(0x2B74..0x2B75); add(0x2B96)

    // --- Glagolitic ---
    add(0x2C2F); add(0x2C5F)

    // --- Coptic ---
    addAll(0x2CF4..0x2CF8)

    // --- Georgian Supplement ---
    add(0x2D26); addAll(0x2D28..0x2D2C); addAll(0x2D2E..0x2D2F)

    // --- Tifinagh ---
    addAll(0x2D68..0x2D6E); addAll(0x2D71..0x2D7E)

    // --- Ethiopic Extended ---
    addAll(0x2D97..0x2D9F); add(0x2DA7); add(0x2DAF); add(0x2DB7); add(0x2DBF); add(0x2DC7); add(0x2DCF); add(0x2DD7)
    add(0x2DDF)

    // --- Supplemental Punctuation (tail of block, unassigned) ---
    addAll(0x2E53..0x2E7F)

    // --- CJK Radicals Supplement, Kangxi Radicals, Ideographic Description Characters ---
    // --- CJK Symbols/Punctuation, Hiragana, Katakana, Bopomofo, Hangul Compat Jamo ---
    // --- Kanbun, Bopomofo Ext, CJK Strokes, Katakana Phonetic Ext ---
    // --- Enclosed CJK Letters, CJK Compatibility ---
    // --- CJK Unified Ideographs Extension A ---
    // --- Yijing Hexagram Symbols ---
    // --- CJK Unified Ideographs ---
    // (all require fonts-noto-cjk which is not installed)
    addAll(0x2E80..0x9FFF)

    // --- Yi Syllables, Yi Radicals (no Noto Yi font available) ---
    addAll(0xA000..0xA4CF)

    // --- Vai ---
    addAll(0xA62C..0xA63F)

    // --- Bamum ---
    addAll(0xA6F8..0xA6FF)

    // --- Latin Extended-D ---
    addAll(0xA7C0..0xA7C1); addAll(0xA7CB..0xA7F1)

    // --- Syloti Nagri ---
    addAll(0xA82D..0xA82F)

    // --- Common Indic Number Forms ---
    addAll(0xA83A..0xA83F)

    // --- Phags-pa ---
    addAll(0xA878..0xA87F)

    // --- Saurashtra ---
    addAll(0xA8C6..0xA8CD); addAll(0xA8DA..0xA8DF)

    // --- Rejang ---
    addAll(0xA954..0xA95E)

    // --- Hangul Jamo Extended-A ---
    addAll(0xA960..0xA97F)

    // --- Javanese ---
    add(0xA9CE); addAll(0xA9DA..0xA9DD)

    // --- Myanmar Extended-B ---
    add(0xA9FF)

    // --- Cham ---
    addAll(0xAA37..0xAA3F); addAll(0xAA4E..0xAA4F); addAll(0xAA5A..0xAA5B)

    // --- Tai Viet ---
    addAll(0xAAC3..0xAADA)

    // --- Meetei Mayek Extensions ---
    addAll(0xAAF7..0xAB00)

    // --- Ethiopic Extended-A ---
    addAll(0xAB07..0xAB08); addAll(0xAB0F..0xAB10); addAll(0xAB17..0xAB1F); add(0xAB27); add(0xAB2F)

    // --- Latin Extended-E ---
    addAll(0xAB6C..0xAB6F)

    // --- Meetei Mayek ---
    addAll(0xABEE..0xABEF); addAll(0xABFA..0xD7FF)

    // --- Private Use Area ---
    addAll(0xE000..0xFAFF)

    // --- Alphabetic Presentation Forms ---
    addAll(0xFB07..0xFB12); addAll(0xFB18..0xFB1C)

    // --- Arabic Presentation Forms-A ---
    addAll(0xFBC2..0xFBD2); addAll(0xFD40..0xFD4F); addAll(0xFD90..0xFD91); addAll(0xFDC8..0xFDEF)
    addAll(0xFDFE..0xFDFF)

    // --- Vertical Forms ---
    addAll(0xFE10..0xFE2D)

    // --- CJK Compatibility Forms ---
    addAll(0xFE30..0xFE3C); addAll(0xFE3F..0xFE40); addAll(0xFE45..0xFE65)

    // --- Small Form Variants ---
    addAll(0xFE67..0xFE6F)

    // --- Arabic Presentation Forms-B ---
    add(0xFE75); addAll(0xFEFD..0xFFFF)
}

/**
 * Escapes a string for use in Typst content mode (inside [...] content blocks).
 *
 * In Typst content mode, the following characters have special meaning and need escaping:
 * - `\` - escape character (must be escaped first)
 * - `#` - starts code/function calls
 * - `*` - bold markup
 * - `_` - emphasis/italic markup
 * - `` ` `` - raw/code markup
 * - `$` - math mode
 * - `<` - label start
 * - `>` - label end
 * - `@` - reference/citation
 * - `[` - content block start
 * - `]` - content block end
 *
 * All these characters are escaped with a backslash prefix.
 */
internal fun String.typstEscape(): String =
    this.map {
        if (CHARACTER_BLOCKLIST.contains(it.code)) {
            ""
        } else {
            when (it) {
                '\\' -> "\\\\"
                '#' -> "\\#"
                '*' -> "\\*"
                '_' -> "\\_"
                '`' -> "\\`"
                '$' -> "\\$"
                '<' -> "\\<"
                '>' -> "\\>"
                '@' -> "\\@"
                '[' -> "\\["
                ']' -> "\\]"
                else -> it.toString()
            }
        }
    }.joinToString(separator = "")

/**
 * Escapes a string for use inside Typst string literals (inside "..." quotes).
 *
 * In Typst string literals:
 * - `\` must be escaped as `\\`
 * - `"` must be escaped as `\"`
 * - Newlines should be escaped as `\n`
 * - Tabs should be escaped as `\t`
 */
internal fun String.typstStringEscape(): String =
    this.map {
        if (CHARACTER_BLOCKLIST.contains(it.code)) {
            ""
        } else {
            when (it) {
                '\\' -> "\\\\"
                '"' -> "\\\""
                '\n' -> "\\n"
                '\r' -> "\\r"
                '\t' -> "\\t"
                else -> it.toString()
            }
        }
    }.joinToString(separator = "")
