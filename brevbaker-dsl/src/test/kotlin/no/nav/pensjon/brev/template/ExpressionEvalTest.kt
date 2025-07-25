package no.nav.pensjon.brev.template

import no.nav.brev.brevbaker.FellesFactory
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.Felles
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test


class ExpressionEvalTest {
    data class SomeDto(val name: String, val kortNavn: String?)

    private val nameSelector = SimpleSelector(SomeDto::name)
    private val Expression<SomeDto>.name
        get() = UnaryOperation.Select(nameSelector).invoke(this)

    private val kortNavnSelector = SimpleSelector(SomeDto::kortNavn)
    private val Expression<SomeDto>.kortNavn
        get() = UnaryOperation.Select(kortNavnSelector).invoke(this)

    private val saksnummerSelector = SimpleSelector(Felles::saksnummer)
    private val Expression<Felles>.saksnummer
        get() = UnaryOperation.Select(saksnummerSelector).invoke(this)

    private val scope = ExpressionScope(SomeDto("Ole", null), FellesFactory.felles, Language.Bokmal)
    private val argumentExpr = Expression.FromScope.Argument<SomeDto>()
    private val fellesExpr = Expression.FromScope.Felles

    @Test
    fun `eval Literal returns literal`() {
        val evaluated: Int = Expression.Literal(5).eval(scope)

        assertEquals(5, evaluated)
    }

    @Test
    fun `eval Argument returns argument value`() {
        val evaluated: String = argumentExpr.name.eval(scope)

        assertEquals(scope.argument.name, evaluated)
    }

    @Test
    fun `eval optional argument field returns argument value`() {
        val evaluated: String? = argumentExpr.kortNavn.eval(ExpressionScope(scope.argument.copy(kortNavn = "O"), scope.felles, scope.language))

        assertEquals("O", evaluated)
    }

    @Test
    fun `eval optional argument field without value returns null`() {
        val evaluated: String? = argumentExpr.kortNavn.eval(scope)

        assertNull(scope.argument.kortNavn) // tester ingenting om kortNavn har en verdi
        assertNull(evaluated)
    }

    @Test
    fun `eval can give default value for optional argument field`() {
        val evaluated = argumentExpr.kortNavn.ifNull("J").eval(scope)
        argumentExpr.name.ifNull("J").eval(scope)

        assertEquals("J", evaluated)
    }

    @Test
    fun `eval BinaryInvoke returns expected value`() {
        val evaluated: String = BinaryOperation.Concat("h".expr(), "ei".expr()).eval(scope)

        assertEquals("hei", evaluated)
    }

    @Test
    fun `eval UnaryInvoke returns expected value`() {
        val evaluated: String = UnaryOperation.ToString(4.expr()).eval(scope)
        assertEquals("4", evaluated)
    }

    @Test
    fun `eval can select fields from felles`() {
        val evaluated = fellesExpr.saksnummer.eval(scope)
        assertEquals(FellesFactory.felles.saksnummer, evaluated)
    }

    @Test
    fun `eval FromScope will select a value from scope`() {
        val evaluated: Language = Expression.FromScope.Language.eval(scope)
        assertEquals(scope.language, evaluated)
    }

    @Test
    fun `eval SafeApplication can be used for null-safe calls when value is null`() {
        val evaluated: String? = argumentExpr.kortNavn.safe { plus("hei") }.eval(scope)
        assertEquals(null, evaluated)
    }

    @Test
    fun `eval SafeApplication can be used for null-safe calls when value is non-null`() {
        val scope = ExpressionScope(SomeDto("Ole", "KortNavn"), FellesFactory.felles, Language.Bokmal)
        val evaluated: String? = argumentExpr.kortNavn.safe { plus("hei") }.eval(scope)
        assertEquals("KortNavnhei", evaluated)
    }

    internal data class NestedDto(val nested: SomeDto? = null)
    private val nestedSelector = SimpleSelector(NestedDto::nested)
    private val Expression<NestedDto>.nested
        get() = UnaryOperation.Select(nestedSelector).invoke(this)

    @Test
    fun `eval SafeApplication can be nested when value is null`() {
        val scope = ExpressionScope(NestedDto(), FellesFactory.felles, Language.Bokmal)
        val argumentExpr = Expression.FromScope.Argument<NestedDto>()

        val evaluated: String? = argumentExpr.nested.safe { kortNavn.safe { plus("hei") } }.eval(scope)
        assertEquals(null, evaluated)
    }

    @Test
    fun `eval SafeApplication can be nested when value is non-null`() {
        val scope = ExpressionScope(NestedDto(SomeDto("Ole", "KortNavn")), FellesFactory.felles, Language.Bokmal)
        val argumentExpr = Expression.FromScope.Argument<NestedDto>()

        val evaluated: String? = argumentExpr.nested.safe { kortNavn.safe { plus("hei") } }.eval(scope)
        assertEquals("KortNavnhei", evaluated)
    }

}