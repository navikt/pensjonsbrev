package no.nav.pensjon.brev.brevoppskrift.kotlin

/** Always quoted; user text can never become Kotlin code, interpolation, imports or paths. */
fun kotlinString(value: String): String = buildString {
    append('"')
    value.forEach { char ->
        append(when (char) {
            '\\' -> "\\\\"
            '"' -> "\\\""
            '$' -> "\\$"
            '\n' -> "\\n"
            '\r' -> "\\r"
            '\t' -> "\\t"
            else -> if (char.code < 32 || char.code in 0x7f..0x9f || char.isSurrogate()) "\\u${char.code.toString(16).padStart(4, '0')}" else char.toString()
        })
    }
    append('"')
}
