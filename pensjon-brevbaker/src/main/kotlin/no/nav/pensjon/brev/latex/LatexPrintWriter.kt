package no.nav.pensjon.brev.latex

import no.nav.pensjon.brev.template.latexEscape
import java.io.Closeable
import java.io.OutputStream
import java.io.PrintWriter

class LatexPrintWriter(output: OutputStream) : Closeable {
    val printWriter = PrintWriter(output, true, Charsets.UTF_8)

    fun print(s: String, escape: Boolean = true) {
        printWriter.print(latexString(s, escape))
    }

    fun println(s: String, escape: Boolean = true) {
        printWriter.println(latexString(s, escape))
    }

    fun printCmd(cmd: String, vararg args: String, escape: Boolean = true) {
        printWriter.print("""\$cmd""")
        args.map { latexString(it, escape) }
            .forEach { printWriter.print("""{$it}""") }
        printWriter.println()
    }

    fun printCmd(cmd: String, argBuilder: CommandBuilder.() -> Unit) {
        printWriter.print("""\$cmd""")
        CommandBuilder(this).argBuilder()
        printWriter.println()
    }

    fun printNewCmd(name: String, body: String, escape: Boolean = true) {
        printWriter.println("""\newcommand{\$name}{${latexString(body, escape)}}""")
    }

    fun printNewCmd(name: String, writeBody: (LatexPrintWriter) -> Unit) {
        printWriter.print("""\newcommand{\$name}{""")
        writeBody(this)
        printWriter.println("}")
    }

    override fun close() {
        printWriter.close()
    }

    private fun latexString(s: String, escape: Boolean) =
        if (escape)
            s.latexEscape()
        else
            s
}

class CommandBuilder(private val latexPrinter: LatexPrintWriter) {

    fun arg(argBuilder: (LatexPrintWriter) -> Unit) {
        latexPrinter.printWriter.print("{")
        argBuilder(latexPrinter)
        latexPrinter.printWriter.print("}")
    }
}