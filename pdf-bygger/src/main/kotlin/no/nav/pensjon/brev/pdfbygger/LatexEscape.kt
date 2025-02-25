package no.nav.pensjon.brev.pdfbygger

internal val CHARACTER_BLOCKLIST =
    hashSetOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 11, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30, 31, 127)

internal fun String.latexEscape(): String =
    this.map {
        if (CHARACTER_BLOCKLIST.contains(it.code)) {
            ""
        } else {
            when (it) {
                '#' -> """\#"""
                '$' -> """\$"""
                '%' -> """\%"""
                '&' -> """\&"""
                '\\' -> """\textbackslash{}"""
                '^' -> """\textasciicircum{}"""
                '_' -> """\_"""
                '{' -> """\{"""
                '}' -> """\}"""
                '~' -> """\textasciitilde{}"""
                else -> it.toString()
            }
        }
    }.joinToString(separator = "")
