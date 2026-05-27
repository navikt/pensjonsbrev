package no.nav.pensjon.brev.template.expression

import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.safe
import no.nav.pensjon.brevbaker.api.model.LetterMarkupWithDataUsage.Property
import no.nav.pensjon.brevbaker.api.model.LetterMarkupWithDataUsageImpl
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class SelectorUsageTest {

    private data class RotModel(val aModel: AModel?)
    private data class AModel(val testFelt: Boolean?)

    private val testFeltSelector = object : TemplateModelSelector<AModel, Boolean?> {
        override val className: String = AModel::class.qualifiedName!!
        override val propertyName: String = "testFelt"
        override val propertyType: String = "kotlin.Boolean"
        override val selector: AModel.() -> Boolean? = AModel::testFelt
    }
    private val aModelSelector = object : TemplateModelSelector<RotModel, AModel?> {
        override val className: String = RotModel::class.qualifiedName!!
        override val propertyName: String = "aModel"
        override val propertyType: String = AModel::class.qualifiedName!!
        override val selector: RotModel.() -> AModel? = RotModel::aModel
    }

    @OptIn(InterneDataklasser::class)
    private fun TemplateModelSelector<*, *>.usageId(): Property =
        LetterMarkupWithDataUsageImpl.PropertyImpl(typeName = this.className, propertyName = this.propertyName)

    private val argument = Expression.FromScope.Argument<RotModel>()
    private val Expression<AModel?>.testFeltSafe: Expression<Boolean?>
        get() = UnaryOperation.SafeCall(testFeltSelector).invoke(this)

    private val Expression<RotModel>.aModel: Expression<AModel?>
        get() = UnaryOperation.Select(aModelSelector).invoke(this)

    private fun <T : Any> T.asScope(): RootExpressionScope<T> =
        RootExpressionScope(this, FellesFactory.felles, Language.Bokmal, SelectorUsage())

    private fun assertUsage(
        scope: RootExpressionScope<*>,
        expectedSelector: TemplateModelSelector<*, *>
    ) {
        assertThat(scope.selectorUsage!!.propertyUsage).contains(expectedSelector.usageId())
    }

    @Test
    fun `markerer et felt som brukt`() {
        val scope = RotModel(AModel(true)).asScope()
        argument.aModel.testFeltSafe.eval(scope)
        assertUsage(scope, testFeltSelector)
    }

    @Test
    fun `markerer et felt som brukt naar parent er null`() {
        val scope = RootExpressionScope(RotModel(null), FellesFactory.felles, Language.Bokmal, SelectorUsage())
        argument.aModel.testFeltSafe.eval(scope)
        assertThat(scope.selectorUsage!!.propertyUsage).contains(testFeltSelector.usageId())
        assertUsage(scope, testFeltSelector)
    }

    @Test
    fun `markerer felt som brukt naar den har ytre operasjoner paa safe selector`() {
        val scope = RotModel(null).asScope()
        val expr = argument.aModel.testFeltSafe.equalTo(true).and(false.expr())

        expr.eval(scope)
        assertUsage(scope, testFeltSelector)
    }

    @Test
    fun `markerer felt som brukt naar den brukes i en binary safe operation`() {
        val scope = RotModel(null).asScope()
        val expr = argument.aModel.testFeltSafe.safe(BinaryOperation.Equal(), null.expr())

        expr.eval(scope)
        assertUsage(scope, testFeltSelector)
    }
}