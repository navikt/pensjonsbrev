package no.nav.pensjon.brev.pdfbygger.typst

/**
 * Control characters that should be stripped from output.
 * These are non-printable characters that could cause issues in the output.
 */
internal val CHARACTER_BLOCKLIST =
    hashSetOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 11, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 127)

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
