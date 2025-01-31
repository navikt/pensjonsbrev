package no.nav.pensjon.brev.pdfbygger

import com.natpryce.hamkrest.and
import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.endsWith
import com.natpryce.hamkrest.equalTo
import com.natpryce.hamkrest.startsWith
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class LatexAppendableTest {

    private lateinit var output: StringBuilder
    private lateinit var appendable: LatexAppendable

    @BeforeEach
    fun setup() {
        output = StringBuilder()
        appendable = LatexAppendable(output)
    }

    private fun printedString(): String {
        return String(output)
    }

    @Test
    fun `print writes the string`() {
        val expected = "heisann dette er en test"
        appendable.append(expected)

        assertThat(printedString(), equalTo(expected))
    }

    @Test
    fun `println writes the string with newline`() {
        val expected = "heisann dette er en test"
        appendable.appendln(expected)

        assertThat(printedString(), startsWith(expected) and endsWith("\n"))
    }

    @Test
    fun `print escapes latex meta characters`() {
        val expected = """heisann dette er _en test"""
        appendable.append(expected)
        assertThat(printedString(), equalTo(expected.latexEscape()))
    }

    @Test
    fun `println escapes latex meta characters`() {
        val expected = """heisann dette er _en test"""
        appendable.appendln(expected)
        assertThat(printedString(), startsWith(expected.latexEscape()))
    }

    @Test
    fun `printCmd prints as command`() {
        val cmd = "paragraph"
        appendable.appendCmd(cmd)
        assertThat(printedString(), startsWith("""\$cmd"""))
    }

    @Test
    fun `printCmd prints arguments in curly brackets`() {
        val cmd = "paragraph"
        val arg1 = "mitt første argument"
        val arg2 = "mitt andre argument"
        appendable.appendCmd(cmd, arg1, arg2)
        assertThat(printedString(), startsWith("""\$cmd{$arg1}{$arg2}"""))
    }

    @Test
    fun `printCmd ends with newline`() {
        appendable.appendCmd("paragraph")
        assertThat(printedString(), endsWith("\n"))
    }

    @Test
    fun `printCmd escapes latex meta characters of arguments`() {
        val cmd = "paragraph"
        val arg = "min fine paragraf med _"
        appendable.appendCmd(cmd, arg)
        assertThat(printedString(), startsWith("""\$cmd{${arg.latexEscape()}}"""))
    }

    @Test
    fun `printCmd can print non-escaped arguments`() {
        val cmd = "paragraph"
        val arg = """\other{hei_sann}"""
        appendable.appendCmd(cmd, arg, escape = false)
        assertThat(printedString(), startsWith("""\$cmd{$arg}"""))
    }

    @Test
    fun `printNewCmd prints newcommand`() {
        val cmd = "mycommand"
        val body = """kroppen til vår cmd"""
        appendable.appendNewCmd(cmd, body)
        assertThat(printedString(), startsWith("""\newcommand{\$cmd}{$body}"""))
    }

    @Test
    fun `printNewCmd escapes body with latex meta characters`() {
        val cmd = "mycommand"
        val body = """kroppen til vår cmd med _"""
        appendable.appendNewCmd(cmd, body)
        assertThat(printedString(), startsWith("""\newcommand{\$cmd}{${body.latexEscape()}}"""))
    }

    @Test
    fun `printNewCmd can print body without escaping latex meta characters`() {
        val cmd = "mycommand"
        val body = """\othercmd{hei_sann}"""
        appendable.appendNewCmd(cmd, body, escape = false)
        assertThat(printedString(), startsWith("""\newcommand{\$cmd}{${body}}"""))
    }

    @Test
    fun `printCmd with CommandBuilder prints as command`() {
        val cmd = "paragraph"
        appendable.appendCmd(cmd) { /* This empty block is important to test correct function */ }
        assertThat(printedString(), startsWith("""\$cmd"""))
    }


    @Test
    fun `printCmd with CommandBuilder prints arguments in curly brackets`() {
        val cmd = "paragraph"
        val arg1 = "mitt første argument"
        val arg2 = "mitt andre argument"
        appendable.appendCmd(cmd) {
            arg { append(arg1) }
            arg { append(arg2) }
        }
        assertThat(printedString(), startsWith("""\$cmd{$arg1}{$arg2}"""))
    }

    @Test
    fun `printCmd with CommandBuilder ends with newline`() {
        appendable.appendCmd("paragraph") { arg { append("hei") } }
        assertThat(printedString(), endsWith("\n"))
    }

    @Test
    fun `printNewCmd with bodybuilder prints new command`() {
        val name = "mycmd"
        appendable.appendNewCmd(name) {
            appendln("heisann")
        }
        assertThat(printedString(), startsWith("""\newcommand{\$name}"""))
    }

    @Test
    fun `printNewCmd with bodybuilder prints body in curly brackets`() {
        appendable.apply {
            appendNewCmd("mycmd") {
                appendln("heisann")
            }
        }
        assertThat(printedString().replace("\n", ""), endsWith("{heisann}"))
    }

    @Test
    fun `printNewCmd with bodybuilder can have body that invokes other cmd`() {
        val invoke = "otherCmd"
        appendable.appendNewCmd("mycmd") {
            appendCmd(invoke)
        }
        assertThat(printedString().replace("\n", ""), endsWith("""{\$invoke}"""))
    }

    @Test
    fun `printCmd allows square brackets in argument`() {
        appendable.appendCmd("aCmd", "regularArg", "X[l]")
        assertThat(printedString().replace("\n", ""), equalTo("""\aCmd{regularArg}{X[l]}"""))
    }
}