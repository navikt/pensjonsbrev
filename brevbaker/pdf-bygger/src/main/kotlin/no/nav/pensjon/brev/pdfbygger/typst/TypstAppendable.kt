package no.nav.pensjon.brev.pdfbygger.typst

import no.nav.pensjon.brev.pdfbygger.latex.LatexAppendable

@DslMarker
internal annotation class LatexAppendableMarker

@LatexAppendableMarker
internal class TypstAppendable(private val output: Appendable) {


    private fun typstString(s: String, escape: Boolean) =
        if (escape)
            s.typstEscape()
        else
            s
    fun appendDictionary(name: String, map: Map<String, String?>) {
        output.appendLine("#let $name = (")
        map.forEach { (key, value) ->
            output.appendLine("    $key: ${formatTypstValue(value)},")
        }
        output.appendLine(")")
    }

    fun appendDictionary(name: String, map: Map<String, Any?>, @Suppress("UNUSED_PARAMETER") anyValue: Unit = Unit) {
        output.appendLine("#let $name = (")
        map.forEach { (key, value) ->
            output.appendLine("    $key: ${formatTypstValue(value)},")
        }
        output.appendLine(")")
    }

    private fun formatTypstValue(value: Any?): String = when (value) {
        null -> "none"
        is String -> "\"${value.replace("\\", "\\\\").replace("\"", "\\\"")}\""
        is Boolean -> if (value) "true" else "false"
        is List<*> -> "(\n${value.joinToString(",\n") { "      ${formatTypstValue(it)}" }}\n    )"
        else -> value.toString()
    }

    @LatexAppendableMarker
    internal class CommandBuilder(private val typstAppendable: TypstAppendable) {
        fun arg(argBuilder: TypstAppendable.() -> Unit) {
            typstAppendable.output.append("{")
            argBuilder(typstAppendable)
            typstAppendable.output.append("}")
        }
    }
}
