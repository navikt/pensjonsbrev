package no.nav.pensjon.brev.latex

import com.natpryce.hamkrest.and
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.endsWith
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.startsWith
import no.nav.pensjon.brev.template.latexEscape
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream

class LatexPrintWriterTest {

    lateinit var output: ByteArrayOutputStream
    lateinit var printWriter: LatexPrintWriter

    @BeforeEach
    fun setup() {
        output = ByteArrayOutputStream()
        printWriter = LatexPrintWriter(output)
    }

    fun printedString(): String {
        printWriter.close()
        return String(output.toByteArray())
    }

    @Test
    fun `print writes the string`() {
        val expected = "heisann dette er en test"
        printWriter.print(expected)

        assertThat(printedString(), equalTo(expected))
    }

    @Test
    fun `println writes the string with newline`() {
        val expected = "heisann dette er en test"
        printWriter.println(expected)

        assertThat(printedString(), startsWith(expected) and endsWith("\n"))
    }

    @Test
    fun `print escapes latex meta characters`() {
        val expected = """heisann dette er _en test"""
        printWriter.print(expected)
        assertThat(printedString(), equalTo(expected.latexEscape()))
    }

    @Test
    fun `println escapes latex meta characters`() {
        val expected = """heisann dette er _en test"""
        printWriter.println(expected)
        assertThat(printedString(), startsWith(expected.latexEscape()))
    }

    @Test
    fun `printCmd prints as command`() {
        val cmd = "paragraph"
        printWriter.printCmd(cmd)
        assertThat(printedString(), startsWith("""\$cmd"""))
    }

    @Test
    fun `printCmd prints arguments in curly brackets`() {
        val cmd = "paragraph"
        val arg1 = "mitt første argument"
        val arg2 = "mitt andre argument"
        printWriter.printCmd(cmd, arg1, arg2)
        assertThat(printedString(), startsWith("""\$cmd{$arg1}{$arg2}"""))
    }

    @Test
    fun `printCmd ends with newline`() {
        printWriter.printCmd("paragraph")
        assertThat(printedString(), endsWith("\n"))
    }

    @Test
    fun `printCmd escapes latex meta characters of arguments`() {
        val cmd = "paragraph"
        val arg = "min fine paragraf med _"
        printWriter.printCmd(cmd, arg)
        assertThat(printedString(), startsWith("""\$cmd{${arg.latexEscape()}}"""))
    }

    @Test
    fun `printCmd can print non-escaped arguments`() {
        val cmd = "paragraph"
        val arg = """\other{hei_sann}"""
        printWriter.printCmd(cmd, arg, escape = false)
        assertThat(printedString(), startsWith("""\$cmd{$arg}"""))
    }

    @Test
    fun `printNewCmd prints newcommand`() {
        val cmd = "mycommand"
        val body = """kroppen til vår cmd"""
        printWriter.printNewCmd(cmd, body)
        assertThat(printedString(), startsWith("""\newcommand{\$cmd}{$body}"""))
    }

    @Test
    fun `printNewCmd escapes body with latex meta characters`() {
        val cmd = "mycommand"
        val body = """kroppen til vår cmd med _"""
        printWriter.printNewCmd(cmd, body)
        assertThat(printedString(), startsWith("""\newcommand{\$cmd}{${body.latexEscape()}}"""))
    }

    @Test
    fun `printNewCmd can print body without escaping latex meta characters`() {
        val cmd = "mycommand"
        val body = """\othercmd{hei_sann}"""
        printWriter.printNewCmd(cmd, body, escape = false)
        assertThat(printedString(), startsWith("""\newcommand{\$cmd}{${body}}"""))
    }

    @Test
    fun `printCmd with CommandBuilder prints as command`() {
        val cmd = "paragraph"
        printWriter.printCmd(cmd) { /* This empty block is important to test correct function */ }
        assertThat(printedString(), startsWith("""\$cmd"""))
    }


    @Test
    fun `printCmd with CommandBuilder prints arguments in curly brackets`() {
        val cmd = "paragraph"
        val arg1 = "mitt første argument"
        val arg2 = "mitt andre argument"
        printWriter.printCmd(cmd) {
            arg { it.print(arg1) }
            arg { it.print(arg2) }
        }
        assertThat(printedString(), startsWith("""\$cmd{$arg1}{$arg2}"""))
    }

    @Test
    fun `printCmd with CommandBuilder ends with newline`() {
        printWriter.printCmd("paragraph") { arg { it.print("hei") } }
        assertThat(printedString(), endsWith("\n"))
    }

}
