package no.nav.pensjon.brev.pdfbygger.typst

@DslMarker
internal annotation class TypstAppendableMarker

@TypstAppendableMarker
internal class TypstAppendable(private val output: Appendable) {

    /**
     * Append text to output, optionally escaping special Typst content characters.
     */
    internal fun append(s: String, escape: Boolean = true) {
        output.append(if (escape) s.typstEscape() else s)
    }

    /**
     * Append text followed by newline, optionally escaping special Typst content characters.
     */
    internal fun appendln(s: String = "", escape: Boolean = true) {
        output.appendLine(if (escape) s.typstEscape() else s)
    }

    /**
     * Append a Typst function call in code mode (inside #{...}).
     * Does NOT add # prefix since we're already in code mode.
     * Example output: `paragraph[content]` or `title1[content]`
     */
    internal fun appendCodeFunction(name: String, builder: FunctionBuilder.() -> Unit) {
        output.append(name)
        FunctionBuilder(this).builder()
        output.appendLine()
    }

    /**
     * Append a Typst function call in markup mode (outside #{...}).
     * Adds # prefix for function calls.
     * Example output: `#functionName[content]` or `#functionName(args)[content]`
     */
    internal fun appendFunction(name: String, builder: FunctionBuilder.() -> Unit) {
        output.append("#$name")
        FunctionBuilder(this).builder()
        output.appendLine()
    }

    /**
     * Append a Typst function call with content block, e.g., `#paragraph[Hello world]`.
     */
    internal fun appendFunctionWithContent(name: String, contentBuilder: TypstAppendable.() -> Unit) {
        output.append("#$name[")
        contentBuilder()
        output.append("]")
        output.appendLine()
    }

    /**
     * Append a content block `[content]`.
     */
    internal fun appendContentBlock(contentBuilder: TypstAppendable.() -> Unit) {
        output.append("[")
        contentBuilder()
        output.append("]")
    }

    /**
     * Append a dictionary definition like `#let name = (key: value, ...)`.
     */
    fun appendDictionary(name: String, map: Map<String, String?>) {
        output.appendLine("#let $name = (")
        map.forEach { (key, value) ->
            output.appendLine("    $key: ${formatTypstValue(value)},")
        }
        output.appendLine(")")
    }

    /**
     * Append a dictionary definition with any value types.
     */
    fun appendDictionary(name: String, map: Map<String, Any?>, @Suppress("UNUSED_PARAMETER") anyValue: Unit = Unit) {
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
                // Always add trailing comma to ensure it's treated as an array, not a parenthesized expression
                "(\n${value.joinToString(",\n") { "      ${formatTypstValue(it)}" }},\n    )"
            }
        }
        else -> value.toString()
    }

    @TypstAppendableMarker
    internal class FunctionBuilder(private val typstAppendable: TypstAppendable) {
        /**
         * Append a content argument `[content]` (markup mode).
         */
        fun content(contentBuilder: TypstAppendable.() -> Unit) {
            typstAppendable.output.append("[")
            contentBuilder(typstAppendable)
            typstAppendable.output.append("]")
        }

        /**
         * Append a code block argument `{code}` (code mode).
         * Use this when the content needs to call functions without # prefix.
         */
        fun codeBlock(contentBuilder: TypstAppendable.() -> Unit) {
            typstAppendable.output.append("{")
            contentBuilder(typstAppendable)
            typstAppendable.output.append("}")
        }

        /**
         * Append parenthesized arguments `(arg1, arg2, ...)`.
         */
        fun args(argsBuilder: ArgsBuilder.() -> Unit) {
            typstAppendable.output.append("(")
            ArgsBuilder(typstAppendable).argsBuilder()
            typstAppendable.output.append(")")
        }
    }

    @TypstAppendableMarker
    internal class ArgsBuilder(private val typstAppendable: TypstAppendable) {
        private var first = true

        private fun separator() {
            if (!first) typstAppendable.output.append(", ")
            first = false
        }

        /**
         * Append a named argument like `name: value`.
         */
        fun namedArg(name: String, value: String) {
            separator()
            typstAppendable.output.append("$name: ${typstAppendable.formatTypstValue(value)}")
        }

        /**
         * Append a named argument with an integer value.
         */
        fun namedArg(name: String, value: Int) {
            separator()
            typstAppendable.output.append("$name: $value")
        }

        /**
         * Append a named argument with a boolean value.
         */
        fun namedArg(name: String, value: Boolean) {
            separator()
            typstAppendable.output.append("$name: ${if (value) "true" else "false"}")
        }

        /**
         * Append a raw (unescaped) named argument.
         */
        fun namedArgRaw(name: String, value: String) {
            separator()
            typstAppendable.output.append("$name: $value")
        }

        /**
         * Append a content block argument `[content]`.
         */
        fun contentArg(contentBuilder: TypstAppendable.() -> Unit) {
            separator()
            typstAppendable.output.append("[")
            contentBuilder(typstAppendable)
            typstAppendable.output.append("]")
        }

        /**
         * Append a raw (unescaped) positional argument.
         */
        fun rawArg(value: String) {
            separator()
            typstAppendable.output.append(value)
        }

        /**
         * Append a raw function call with a content block argument, e.g., `table.cell(colspan: 2)[content]`.
         * This ensures no comma is added between the function call and the content block.
         */
        fun rawArgWithContent(functionCall: String, contentBuilder: TypstAppendable.() -> Unit) {
            separator()
            typstAppendable.output.append(functionCall)
            typstAppendable.output.append("[")
            contentBuilder(typstAppendable)
            typstAppendable.output.append("]")
        }
    }
}
