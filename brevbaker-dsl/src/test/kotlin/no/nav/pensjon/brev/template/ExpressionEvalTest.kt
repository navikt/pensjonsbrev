package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brevbaker.api.model.Felles
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test


class ExpressionEvalTest {

    data class SomeDto(val name: String, val kortNavn: String?)

    private val nameSelector = object : TemplateModelSelector<SomeDto, String> {
        override val className = "FakeSomeDtoNavnSelector"
        override val propertyName = "value"
        override val propertyType = "String"
        override val selector = SomeDto::name
    }

    private val Expression<SomeDto>.name
        get() = Expression.UnaryInvoke(this, UnaryOperation.Select(nameSelector))

    private val kortNavnSelector = object : TemplateModelSelector<SomeDto, String?> {
        override val className = "FakeKortNavnSelector"
        override val propertyName = "value"
        override val propertyType = "String"
        override val selector = SomeDto::kortNavn
    }
    private val Expression<SomeDto>.kortNavn
        get() = Expression.UnaryInvoke(this, UnaryOperation.Select(kortNavnSelector))

    private val saksnummerSelector = object : TemplateModelSelector<Felles, String> {
        override val className = "FakeFellesSelector"
        override val propertyName = "value"
        override val propertyType = "String"
        override val selector = Felles::saksnummer
    }

    private val Expression<Felles>.saksnummer
        get() = Expression.UnaryInvoke(this, UnaryOperation.Select(saksnummerSelector))

    private val scope = ExpressionScope(SomeDto("Ole", null), Fixtures.felles, Language.Bokmal)
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
        val evaluated: String = Expression.BinaryInvoke(
            Expression.Literal("h"),
            Expression.Literal("ei"),
            BinaryOperation.Concat
        ).eval(scope)

        assertEquals("hei", evaluated)
    }

    @Test
    fun `eval UnaryInvoke returns expected value`() {
        val evaluated: String = Expression.UnaryInvoke(
            Expression.Literal(4),
            UnaryOperation.ToString
        ).eval(scope)

        assertEquals("4", evaluated)
    }

    @Test
    fun `eval can select fields from felles`() {
        val evaluated = fellesExpr.saksnummer.eval(scope)
        assertEquals(Fixtures.felles.saksnummer, evaluated)
    }

    @Test
    fun `eval FromScope will select a value from scope`() {
        val evaluated: Language = Expression.FromScope.Language.eval(scope)
        assertEquals(scope.language, evaluated)
    }

}