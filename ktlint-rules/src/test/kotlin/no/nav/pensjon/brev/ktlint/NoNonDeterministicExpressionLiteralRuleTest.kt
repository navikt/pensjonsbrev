package no.nav.pensjon.brev.ktlint

import com.pinterest.ktlint.test.KtLintAssertThat.Companion.assertThatRule
import com.pinterest.ktlint.test.LintViolation
import org.junit.jupiter.api.Test

class NoNonDeterministicExpressionLiteralRuleTest {
    private val ruleAssertThat = assertThatRule { NoNonDeterministicExpressionLiteralRule() }

    @Test
    fun `flags LocalDate now as ifNull fallback with a specific message and auto-corrects it to localDateNow`() {
        val code =
            """
            val a = x.ifNull(LocalDate.now())
            """.trimIndent()
        val formattedCode =
            """
            import no.nav.pensjon.brev.template.dsl.expression.localDateNow
            val a = x.ifNull(localDateNow)
            """.trimIndent()
        ruleAssertThat(code)
            .hasLintViolation(
                1,
                18,
                "ifNull(...) argument calls 'LocalDate.now()', which changes stableHashCode every day even though " +
                    "the template didn't change. Use 'no.nav.pensjon.brev.template.dsl.expression.localDateNow' " +
                    "instead: it resolves the date when the expression is evaluated, instead of baking it into " +
                    "the expression tree.",
            ).isFormattedAs(formattedCode)
    }

    @Test
    fun `flags LocalDate now wrapped with expr as ifNull fallback and auto-corrects it to localDateNow`() {
        val code =
            """
            val a = x.ifNull(LocalDate.now().expr())
            """.trimIndent()
        val formattedCode =
            """
            import no.nav.pensjon.brev.template.dsl.expression.localDateNow
            val a = x.ifNull(localDateNow)
            """.trimIndent()
        ruleAssertThat(code)
            .hasLintViolation(
                1,
                18,
                "ifNull(...) argument calls 'LocalDate.now()', which changes stableHashCode every day even though " +
                    "the template didn't change. Use 'no.nav.pensjon.brev.template.dsl.expression.localDateNow' " +
                    "instead: it resolves the date when the expression is evaluated, instead of baking it into " +
                    "the expression tree.",
            ).isFormattedAs(formattedCode)
    }

    @Test
    fun `flags non-deterministic call nested inside the ifNull fallback expression`() {
        val code =
            """
            val a = x.ifNull(then = Wrapper(Instant.now()))
            """.trimIndent()
        ruleAssertThat(code)
            .hasLintViolationWithoutAutoCorrect(
                1,
                18,
                "ifNull(...) argument must be a compile-time constant, but calls non-deterministic 'now()'. " +
                    "This makes stableHashCode change over time even though the template didn't change. " +
                    "Use a fixed literal/constant value instead.",
            )
    }

    @Test
    fun `flags Instant now as ifNull fallback with the generic message and no auto-correct`() {
        val code =
            """
            val a = x.ifNull(Instant.now())
            """.trimIndent()
        ruleAssertThat(code)
            .hasLintViolationWithoutAutoCorrect(
                1,
                18,
                "ifNull(...) argument must be a compile-time constant, but calls non-deterministic 'now()'. " +
                    "This makes stableHashCode change over time even though the template didn't change. " +
                    "Use a fixed literal/constant value instead.",
            )
    }

    @Test
    fun `flags random number generator calls used as ifNull fallback`() {
        val code =
            """
            val a = x.ifNull(Random.nextInt())
            """.trimIndent()
        ruleAssertThat(code)
            .hasLintViolationWithoutAutoCorrect(
                1,
                18,
                "ifNull(...) argument must be a compile-time constant, but calls non-deterministic 'nextInt()'. " +
                    "This makes stableHashCode change over time even though the template didn't change. " +
                    "Use a fixed literal/constant value instead.",
            )
    }

    @Test
    fun `does not flag literal, enum, or constant-built ifNull fallbacks`() {
        val code =
            """
            val a = x.ifNull(Kroner(0))
            val b = x.ifNull(emptyList())
            val c = x.ifNull(LocalDate.of(2000, 1, 1))
            val d = x.ifNull(AlderspensjonBeregnetEtter.EGEN)
            val e = x.ifNull(false)
            val f = x.ifNull("some text")
            """.trimIndent()
        ruleAssertThat(code).hasNoLintViolations()
    }

    @Test
    fun `does not flag unrelated functions named ifNull`() {
        val code =
            """
            fun ifNull(value: Int) = value

            val a = ifNull(LocalDate.now().year)
            """.trimIndent()
        ruleAssertThat(code).hasNoLintViolations()
    }

    @Test
    fun `flags non-deterministic value used as ifElse branch with a specific message and auto-corrects it`() {
        val code =
            """
            val a = ifElse(somePredicate, someDate, LocalDate.now().expr())
            """.trimIndent()
        val formattedCode =
            """
            import no.nav.pensjon.brev.template.dsl.expression.localDateNow
            val a = ifElse(somePredicate, someDate, localDateNow)
            """.trimIndent()
        ruleAssertThat(code)
            .hasLintViolation(
                1,
                41,
                "ifElse(...) argument calls 'LocalDate.now()', which changes stableHashCode every day even though " +
                    "the template didn't change. Use 'no.nav.pensjon.brev.template.dsl.expression.localDateNow' " +
                    "instead: it resolves the date when the expression is evaluated, instead of baking it into " +
                    "the expression tree.",
            ).isFormattedAs(formattedCode)
    }

    @Test
    fun `flags non-deterministic value used as first ifElse branch with a specific message and auto-corrects it`() {
        val code =
            """
            val a = ifElse(somePredicate, LocalDate.now(), someDate)
            """.trimIndent()
        val formattedCode =
            """
            import no.nav.pensjon.brev.template.dsl.expression.localDateNow
            val a = ifElse(somePredicate, localDateNow, someDate)
            """.trimIndent()
        ruleAssertThat(code)
            .hasLintViolation(
                1,
                31,
                "ifElse(...) argument calls 'LocalDate.now()', which changes stableHashCode every day even though " +
                    "the template didn't change. Use 'no.nav.pensjon.brev.template.dsl.expression.localDateNow' " +
                    "instead: it resolves the date when the expression is evaluated, instead of baking it into " +
                    "the expression tree.",
            ).isFormattedAs(formattedCode)
    }

    @Test
    fun `flags non-deterministic call used in the ifElse condition too`() {
        val code =
            """
            val a = ifElse(someFlag.equalTo(Random.nextInt()), someDate, otherDate)
            """.trimIndent()
        ruleAssertThat(code)
            .hasLintViolationWithoutAutoCorrect(
                1,
                16,
                "ifElse(...) argument must be a compile-time constant, but calls non-deterministic 'nextInt()'. " +
                    "This makes stableHashCode change over time even though the template didn't change. " +
                    "Use a fixed literal/constant value instead.",
            )
    }

    @Test
    fun `does not flag ifElse with literal or constant-built branches`() {
        val code =
            """
            val a = ifElse(data.harUtbetaling, Kroner(0), Kroner(1))
            val b = ifElse(sakType.equalTo(SakType.BARNEPENSJON), Constants.URL.expr(), Constants.OTHER_URL.expr())
            """.trimIndent()
        ruleAssertThat(code).hasNoLintViolations()
    }

    @Test
    fun `does not flag localDateNow used as ifNull fallback or ifElse branch`() {
        val code =
            """
            val a = x.ifNull(localDateNow)
            val b = ifElse(somePredicate, localDateNow, someDate)
            """.trimIndent()
        ruleAssertThat(code).hasNoLintViolations()
    }

    @Test
    fun `does not add a duplicate import when localDateNow is already imported`() {
        val code =
            """
            import no.nav.pensjon.brev.template.dsl.expression.localDateNow

            val a = x.ifNull(LocalDate.now())
            """.trimIndent()
        val formattedCode =
            """
            import no.nav.pensjon.brev.template.dsl.expression.localDateNow

            val a = x.ifNull(localDateNow)
            """.trimIndent()
        ruleAssertThat(code)
            .hasLintViolation(
                3,
                18,
                "ifNull(...) argument calls 'LocalDate.now()', which changes stableHashCode every day even though " +
                    "the template didn't change. Use 'no.nav.pensjon.brev.template.dsl.expression.localDateNow' " +
                    "instead: it resolves the date when the expression is evaluated, instead of baking it into " +
                    "the expression tree.",
            ).isFormattedAs(formattedCode)
    }

    @Test
    fun `flags non-deterministic call nested in a showIf predicate`() {
        val code =
            """
            val a = showIf(virkFom.greaterThan(LocalDate.now())) {
                paragraph { }
            }
            """.trimIndent()
        ruleAssertThat(code)
            .hasLintViolationWithoutAutoCorrect(
                1,
                16,
                "showIf(...) argument must be a compile-time constant, but calls non-deterministic 'now()'. " +
                    "This makes stableHashCode change over time even though the template didn't change. " +
                    "Use a fixed literal/constant value instead.",
            )
    }

    @Test
    fun `flags non-deterministic call nested in an orShowIf predicate`() {
        val code =
            """
            val a = showIf(somePredicate) {
                paragraph { }
            }.orShowIf(virkFom.lessThan(LocalDate.of(LocalDate.now().year, Month.JANUARY, 1))) {
                paragraph { }
            }
            """.trimIndent()
        ruleAssertThat(code)
            .hasLintViolationWithoutAutoCorrect(
                3,
                12,
                "orShowIf(...) argument must be a compile-time constant, but calls non-deterministic 'now()'. " +
                    "This makes stableHashCode change over time even though the template didn't change. " +
                    "Use a fixed literal/constant value instead.",
            )
    }

    @Test
    fun `flags LocalDate now used directly as a showIf predicate expression with a specific message and auto-corrects it`() {
        val code =
            """
            val a = showIf(LocalDate.now().expr().notNull()) {
                paragraph { }
            }
            """.trimIndent()
        ruleAssertThat(code).hasLintViolationWithoutAutoCorrect(
            1,
            16,
            "showIf(...) argument must be a compile-time constant, but calls non-deterministic 'now()'. " +
                "This makes stableHashCode change over time even though the template didn't change. " +
                "Use a fixed literal/constant value instead.",
        )
    }

    @Test
    fun `flags non-deterministic value used as ifNotNull expr1 argument`() {
        // The nested `.ifNull(LocalDate.now())` is reported twice: once via the outer `ifNotNull(...)` check (generic
        // message, since the whole `expr1` argument isn't exactly `LocalDate.now()`), and once via the inner
        // `ifNull(...)` check itself (specific message, auto-correctable).
        val code =
            """
            val a = ifNotNull(someDate.ifNull(LocalDate.now())) { date ->
                paragraph { }
            }
            """.trimIndent()
        ruleAssertThat(code)
            .hasLintViolations(
                LintViolation(
                    1,
                    19,
                    "ifNotNull(...) argument must be a compile-time constant, but calls non-deterministic 'now()'. " +
                        "This makes stableHashCode change over time even though the template didn't change. " +
                        "Use a fixed literal/constant value instead.",
                    canBeAutoCorrected = false,
                ),
                LintViolation(
                    1,
                    35,
                    "ifNull(...) argument calls 'LocalDate.now()', which changes stableHashCode every day even though " +
                        "the template didn't change. Use 'no.nav.pensjon.brev.template.dsl.expression.localDateNow' " +
                        "instead: it resolves the date when the expression is evaluated, instead of baking it into " +
                        "the expression tree.",
                ),
            ).isFormattedAs(
                """
                import no.nav.pensjon.brev.template.dsl.expression.localDateNow
                val a = ifNotNull(someDate.ifNull(localDateNow)) { date ->
                    paragraph { }
                }
                """.trimIndent(),
            )
    }

    @Test
    fun `does not flag showIf or orShowIf with literal or constant-built predicates`() {
        val code =
            """
            val a = showIf(sakType.equalTo(SakType.BARNEPENSJON)) {
                paragraph { }
            }.orShowIf(antallBarn.greaterThan(0)) {
                paragraph { }
            }
            """.trimIndent()
        ruleAssertThat(code).hasNoLintViolations()
    }

}
