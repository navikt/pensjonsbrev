package no.nav.pensjon.brev.ktlint

import com.pinterest.ktlint.rule.engine.core.api.AutocorrectDecision
import com.pinterest.ktlint.rule.engine.core.api.ElementType.CALL_EXPRESSION
import com.pinterest.ktlint.rule.engine.core.api.ElementType.DOT_QUALIFIED_EXPRESSION
import com.pinterest.ktlint.rule.engine.core.api.ElementType.IMPORT_DIRECTIVE
import com.pinterest.ktlint.rule.engine.core.api.ElementType.IMPORT_LIST
import com.pinterest.ktlint.rule.engine.core.api.ElementType.REFERENCE_EXPRESSION
import com.pinterest.ktlint.rule.engine.core.api.ElementType.VALUE_ARGUMENT
import com.pinterest.ktlint.rule.engine.core.api.ElementType.VALUE_ARGUMENT_LIST
import com.pinterest.ktlint.rule.engine.core.api.ElementType.WHITE_SPACE
import com.pinterest.ktlint.rule.engine.core.api.KtlintKotlinCompiler
import com.pinterest.ktlint.rule.engine.core.api.Rule
import com.pinterest.ktlint.rule.engine.core.api.RuleAutocorrectApproveHandler
import com.pinterest.ktlint.rule.engine.core.api.RuleId
import com.pinterest.ktlint.rule.engine.core.api.ifAutocorrectAllowed
import org.jetbrains.kotlin.com.intellij.lang.ASTNode
import org.jetbrains.kotlin.com.intellij.psi.impl.source.tree.PsiWhiteSpaceImpl

/**
 * Flags DSL calls that can bake a non-deterministic value (e.g. `LocalDate.now()`) into the `Expression` tree:
 *
 * - `Expression<T?>.ifNull(then)` - the `then` fallback.
 * - `ifElse(condition, ifTrue, ifFalse)` - all three arguments, including `condition`.
 *
 * Background: `stableHashCode` is computed over the whole `Expression` tree (including its `condition`/branch
 * sub-expressions), which brevbaker relies on to detect whether a rendered letter's content changed. A fallback,
 * branch, or condition such as `.ifNull(LocalDate.now())` or `ifElse(date.equalTo(LocalDate.now()), a, b)` makes that
 * hash change every day even though nothing about the letter template itself changed. These arguments must evaluate
 * to the same value on every evaluation - i.e. effectively a compile-time constant (literal, enum constant, or a
 * constructor/function call built purely from such constants).
 *
 * If you actually need "today's date" inside a template, use `no.nav.pensjon.brev.template.dsl.expression.localDateNow`
 * instead of `LocalDate.now()`: it is a dedicated `Expression<LocalDate>` (`UnaryOperation.LocalDateNow`) whose
 * `stableHashCode` is a fixed constant - the current date is only resolved when the expression is evaluated, not
 * baked into the tree, so it doesn't destabilize the hash. When the whole argument is exactly `LocalDate.now()` (or
 * `LocalDate.now().expr()`), this rule can auto-correct it to `localDateNow`.
 *
 * Note this is a syntactic (AST-only) check, not full compile-time constant evaluation: it does not verify that
 * `val`/`var` references used as arguments are themselves constant. It specifically targets calls to known volatile/
 * non-deterministic functions (clock/random sources), which is the failure mode this rule was introduced to prevent.
 */
class NoNonDeterministicExpressionLiteralRule :
    Rule(
        ruleId = RuleId("$PENSJONSBREV_RULE_SET_ID:no-nondeterministic-expression-literal"),
        about =
            About(
                maintainer = "pensjonsbrev",
                repositoryUrl = "https://github.com/navikt/pensjonsbrev",
                issueTrackerUrl = "https://github.com/navikt/pensjonsbrev/issues",
            ),
    ),
    RuleAutocorrectApproveHandler {

    override fun beforeVisitChildNodes(
        node: ASTNode,
        emit: (offset: Int, errorMessage: String, canBeAutoCorrected: Boolean) -> AutocorrectDecision,
    ) {
        if (node.elementType != CALL_EXPRESSION) return
        val calleeName = node.findChildByType(REFERENCE_EXPRESSION)?.text ?: return
        val valueArgumentList = node.findChildByType(VALUE_ARGUMENT_LIST) ?: return
        val valueArguments = valueArgumentList.getChildren(null).filter { it.elementType == VALUE_ARGUMENT }

        val argumentsToCheck =
            when (calleeName) {
                // `<receiver>.ifNull(then)`, not some unrelated local function also named `ifNull`.
                "ifNull" -> {
                    if (node.treeParent?.elementType != DOT_QUALIFIED_EXPRESSION) return
                    valueArguments
                }
                // `ifElse(condition, ifTrue, ifFalse)`: check every argument, `condition` included, since it is
                // also part of the `Expression` tree that `stableHashCode` is computed over.
                "ifElse" -> valueArguments
                else -> return
            }

        for (argument in argumentsToCheck) {
            val offendingCallName = findNonDeterministicCall(argument) ?: continue
            val valueExpression = argument.valueExpression()

            if (offendingCallName == "now" && valueExpression?.text in LOCAL_DATE_NOW_REPLACEABLE_TEXTS) {
                emit(
                    valueExpression!!.startOffset,
                    "$calleeName(...) argument calls 'LocalDate.now()', which changes stableHashCode every day even " +
                        "though the template didn't change. Use " +
                        "'no.nav.pensjon.brev.template.dsl.expression.localDateNow' instead: it resolves the date " +
                        "when the expression is evaluated, instead of baking it into the expression tree.",
                    true,
                ).ifAutocorrectAllowed {
                    createLocalDateNowNode()?.let { replacement ->
                        valueExpression.treeParent.replaceChild(valueExpression, replacement)
                        replacement.addImportIfMissing(LOCAL_DATE_NOW_IMPORT)
                    }
                }
            } else {
                emit(
                    argument.startOffset,
                    "$calleeName(...) argument must be a compile-time constant, but calls non-deterministic " +
                        "'$offendingCallName()'. This makes stableHashCode change over time even though the " +
                        "template didn't change. Use a fixed literal/constant value instead.",
                    false,
                )
            }
        }
    }

    /** The actual expression content of a value argument, e.g. skipping the `then =` part of a named argument. */
    private fun ASTNode.valueExpression(): ASTNode? =
        getChildren(null).lastOrNull { it.elementType != WHITE_SPACE }

    private fun createLocalDateNowNode(): ASTNode? =
        KtlintKotlinCompiler
            .createASTNodeFromText("localDateNow")
            ?.findChildByType(REFERENCE_EXPRESSION)

    /** Adds `import [importPath]` to this node's file, unless it is already imported (e.g. via this exact import). */
    private fun ASTNode.addImportIfMissing(importPath: String) {
        val fileNode = generateSequence(this) { it.treeParent }.last()
        val importList = fileNode.findChildByType(IMPORT_LIST) ?: return
        val importText = "import $importPath"
        val wildcardImportText = "import ${importPath.substringBeforeLast('.')}.*"
        val alreadyImported =
            importList
                .getChildren(null)
                .any {
                    it.elementType == IMPORT_DIRECTIVE &&
                        (it.text.trim() == importText || it.text.trim() == wildcardImportText)
                }
        if (alreadyImported) return

        val newImportDirective =
            KtlintKotlinCompiler
                .createPsiFileFromText("Import.kt", "$importText\n")
                .node
                .findChildByType(IMPORT_LIST)
                ?.findChildByType(IMPORT_DIRECTIVE)
                ?: return

        if (importList.getChildren(null).any { it.elementType == IMPORT_DIRECTIVE }) {
            importList.addChild(PsiWhiteSpaceImpl("\n"), null)
        }
        importList.addChild(newImportDirective, null)

        // The whitespace separating the import list from the rest of the file is a sibling of the import list
        // itself (not a child), and only exists once there is something to separate.
        val nextSibling = importList.treeNext
        if (nextSibling == null || nextSibling.elementType != WHITE_SPACE || !nextSibling.text.contains("\n")) {
            fileNode.addChild(PsiWhiteSpaceImpl("\n"), nextSibling)
        }
    }

    private fun findNonDeterministicCall(node: ASTNode): String? {
        if (node.elementType == CALL_EXPRESSION) {
            val calleeName = node.findChildByType(REFERENCE_EXPRESSION)?.text
            if (calleeName != null && calleeName in NON_DETERMINISTIC_CALL_NAMES) {
                return calleeName
            }
        }
        for (child in node.getChildren(null)) {
            findNonDeterministicCall(child)?.let { return it }
        }
        return null
    }

    private companion object {
        // Names of functions that return a different value on every call, commonly used (accidentally) as ifNull
        // fallbacks or ifElse branches: java.time `now()`/`today()`-style factories, and clock/random sources.
        val NON_DETERMINISTIC_CALL_NAMES: Set<String> =
            setOf(
                "now",
                "today",
                "currentTimeMillis",
                "nanoTime",
                "randomUUID",
                "nextInt",
                "nextLong",
                "nextDouble",
                "nextFloat",
                "nextBoolean",
                "nextBytes",
            )

        // Textual shapes of an argument that are safely & unambiguously auto-correctable to `localDateNow`.
        val LOCAL_DATE_NOW_REPLACEABLE_TEXTS: Set<String> =
            setOf(
                "LocalDate.now()",
                "LocalDate.now().expr()",
            )

        const val LOCAL_DATE_NOW_IMPORT = "no.nav.pensjon.brev.template.dsl.expression.localDateNow"
    }
}
