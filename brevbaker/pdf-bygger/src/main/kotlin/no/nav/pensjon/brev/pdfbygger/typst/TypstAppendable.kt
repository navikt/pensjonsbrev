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
        output.append("#let $name = (")
        map.forEach { (key, value) ->
            output.appendLine("    $key: \"${value?.typstEscape()}\",")
        }
        output.appendLine(")")
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
