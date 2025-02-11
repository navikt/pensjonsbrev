package no.nav.pensjon.brev.pdfbygger

@DslMarker
internal annotation class LatexAppendableMarker

@LatexAppendableMarker
internal class LatexAppendable(private val output: Appendable) {

    internal fun append(s: String, escape: Boolean = true) {
        output.append(latexString(s, escape))
    }

    internal fun appendln(s: String, escape: Boolean = true) {
        output.appendLine(latexString(s, escape))
    }

    internal fun appendCmd(cmd: String, vararg args: String, escape: Boolean = true) {
        output.append("""\$cmd""")
        args.map { latexString(it, escape) }
            .forEach { output.append("""{$it}""") }
        output.appendLine()
    }

    internal fun appendCmd(cmd: String, argBuilder: CommandBuilder.() -> Unit) {
        output.append("""\$cmd""")
        CommandBuilder(this).argBuilder()
        output.appendLine()
    }

    internal fun appendNewCmd(name: String, body: String, escape: Boolean = true) {
        output.appendLine("""\newcommand{\$name}{${latexString(body, escape)}}""")
    }

    internal fun appendNewCmd(name: String, writeBody: LatexAppendable.() -> Unit) {
        output.append("""\newcommand{\$name}{""")
        writeBody()
        output.appendLine("}")
    }

    private fun latexString(s: String, escape: Boolean) =
        if (escape)
            s.latexEscape()
        else
            s

    @LatexAppendableMarker
    internal class CommandBuilder(private val latexAppendable: LatexAppendable) {
        fun arg(argBuilder: LatexAppendable.() -> Unit) {
            latexAppendable.output.append("{")
            argBuilder(latexAppendable)
            latexAppendable.output.append("}")
        }
    }
}

