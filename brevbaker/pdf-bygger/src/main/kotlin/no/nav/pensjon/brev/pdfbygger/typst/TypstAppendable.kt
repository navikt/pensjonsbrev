package no.nav.pensjon.brev.pdfbygger.typst

@DslMarker
internal annotation class LatexAppendableMarker

@LatexAppendableMarker
internal class TypstAppendable(private val output: Appendable) {


    private fun typstString(s: String, escape: Boolean) =
        if (escape)
            s.typstEscape()
        else
            s

    @LatexAppendableMarker
    internal class CommandBuilder(private val typstAppendable: TypstAppendable) {
        fun arg(argBuilder: TypstAppendable.() -> Unit) {
            typstAppendable.output.append("{")
            argBuilder(typstAppendable)
            typstAppendable.output.append("}")
        }
    }
}

