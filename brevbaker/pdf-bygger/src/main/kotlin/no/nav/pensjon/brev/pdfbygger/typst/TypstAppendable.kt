package no.nav.pensjon.brev.pdfbygger.typst

import java.io.OutputStreamWriter

@DslMarker
annotation class TypstDslMarker

@TypstDslMarker
class TypstFileWriter(private val output: OutputStreamWriter) {
    fun codeScope(codeBuilder: TypstCodeScope.() -> Unit) {
        TypstCodeScope(output).apply(codeBuilder)
    }
}

/**
 * Scope for Typst markup/content mode (inside `[...]` content blocks).
 *
 * In this scope you can:
 * - [appendContent] to write user-facing text (always escaped)
 * - [appendCode] to write inline Typst code like `#emph[`, `#linebreak()`
 */
@TypstDslMarker
class TypstMarkupScope(private val output: Appendable) {

    /**
     * Append user-facing text. Uses Typst's #str() function to safely render the string
     */
    fun appendContent(s: String) {
        output.append("#str(\"${s.typstStringEscape()}\")")
    }

    /**
     * Append pre-escaped content. No additional escaping is performed.
     */
    fun appendContent(s: EscapedTypstContent) {
        output.append(s.value)
    }

    /**
     * Append inline Typst code within a content block. Never escapes.
     * Use for Typst syntax like `#emph[``]`, `#linebreak()`.
     */
    fun appendCode(s: String) {
        output.append(s)
    }
}

/**
 * Scope for Typst code mode (top-level file or inside `#{...}` blocks).
 *
 * In this scope you can:
 * - [appendCodeln] to write lines of Typst code
 * - [appendCodeFunction] to write function calls with content/args builders
 * - [appendDictionary] to write `#let name = (...)` dictionary definitions
 */
@TypstDslMarker
class TypstCodeScope(private val output: Appendable) {

    /**
     * Append raw Typst code followed by a newline. Never escapes.
     * Use for complete lines of Typst code like imports, `#show`, `#{`, etc.
     */
    fun appendCodeln(s: String = "") {
        output.appendLine(s)
    }

    /**
     * Append a Typst function call in code mode.
     */
    fun appendCodeFunction(name: String, builder: FunctionCallBuilder.() -> Unit) {
        output.append(name)
        FunctionCallBuilder(this).builder()
        output.appendLine()
    }

    /**
     * Append a dictionary definition like `#let name = (key: value, ...)`.
     */
    fun appendDictionary(name: String, map: Map<String, Any?>) {
        output.appendLine("#let $name = (")
        map.forEach { (key, value) ->
            output.appendLine("    $key: ${formatTypstValue(value)},")
        }
        output.appendLine(")")
    }

    /**
     * Format a value for use in Typst code (dictionaries, function arguments).
     * Strings are escaped using typstStringEscape for use inside "..." literals.
     */
    private fun formatTypstValue(value: Any?): String = when (value) {
        null -> "none"
        is String -> "\"${value.typstStringEscape()}\""
        is Boolean -> if (value) "true" else "false"
        is Int -> value.toString()
        is List<*> -> {
            if (value.isEmpty()) {
                "()"
            } else {
                // Trailing comma ensures Typst treats single-element lists as arrays, not parenthesized expressions
                val elements = value.joinToString(",\n") { "  ${formatTypstValue(it)}" }
                """
                    |(
                    |$elements,
                    |)
                """.trimMargin()
            }
        }
        else -> value.toString()
    }

    @TypstDslMarker
    class FunctionCallBuilder(private val codeScope: TypstCodeScope) {

        /**
         * Append a trailing content block `[content]` (enters markup mode).
         */
        fun content(contentBuilder: TypstMarkupScope.() -> Unit) {
            codeScope.output.append("[")
            TypstMarkupScope(codeScope.output).contentBuilder()
            codeScope.output.append("]")
        }

        /**
         * Append parenthesized arguments `(...)`.
         */
        fun args(argsBuilder: ArgsBuilder.() -> Unit) {
            codeScope.output.append("(")
            ArgsBuilder(codeScope).argsBuilder()
            codeScope.output.append(")")
        }
    }

    @TypstDslMarker
    class ArgsBuilder(private val codeScope: TypstCodeScope) {
        private var first = true

        private fun separator() {
            if (!first) codeScope.output.append(", ")
            first = false
        }

        /**
         * Append a named argument with an integer value.
         */
        fun namedArg(name: String, value: Int) {
            separator()
            codeScope.output.append("$name: $value")
        }

        /**
         * Append a named argument with a boolean value.
         */
        fun namedArg(name: String, value: Boolean) {
            separator()
            codeScope.output.append("$name: ${if (value) "true" else "false"}")
        }

        /**
         * Append a raw (unescaped) named argument.
         */
        fun namedArgRaw(name: String, value: String) {
            separator()
            codeScope.output.append("$name: $value")
        }

        /**
         * Append a content block argument `[content]` inside parenthesized args.
         */
        fun contentArg(contentBuilder: TypstMarkupScope.() -> Unit) {
            separator()
            codeScope.output.append("[")
            TypstMarkupScope(codeScope.output).contentBuilder()
            codeScope.output.append("]")
        }

        /**
         * Append a raw (unescaped) positional argument.
         */
        fun rawArg(value: String) {
            separator()
            codeScope.output.append(value)
        }
    }
}
