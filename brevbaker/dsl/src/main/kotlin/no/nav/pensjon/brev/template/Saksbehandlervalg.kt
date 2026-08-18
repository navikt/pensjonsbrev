package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgEnum
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgVerdi
import no.nav.pensjon.brev.template.Expression.UnaryInvoke
import no.nav.pensjon.brev.template.UnaryOperation.Select
import no.nav.pensjon.brev.template.dsl.TemplateRootScope
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import kotlin.reflect.KClass

class SaksbehandlerValgBuilder<LetterData : RedigerbarBrevdata<SaksbehandlervalgIDSL, *>>(private val id: String, private val displayText: String, private val clazz: KClass<LetterData>, private val scope: TemplateRootScope<*, LetterData>) {
    init {
        require(scope.saksbehandlervalg.containsKey(id).not()) { "Saksbehandlervalg med id $id allerede definert" }
    }

    fun bool(): Expression<Boolean> = createSaksbehandlervalg(SaksbehandlervalgVerdi.Bool(id, displayText)).ifNull(false)

    fun int(): Expression<Int?> = createSaksbehandlervalg(SaksbehandlervalgVerdi.Integer(id, displayText))

    fun text(): Expression<String?> = createSaksbehandlervalg(SaksbehandlervalgVerdi.Text(id, displayText))

    inline fun <reified T> enum(): Expression<T?> where T : SaksbehandlerValgEnum, T : Enum<T> = enum(T::class)

    @BrevbakerDSLInternal
    fun <T> enum(clazz: KClass<T>): Expression<T?> where T : SaksbehandlerValgEnum, T : Enum<T> =
        createSaksbehandlervalg(SaksbehandlervalgVerdi.Enum(id, displayText, clazz))

    private fun <T> createSaksbehandlervalg(saksbehandlervalgVerdi: SaksbehandlervalgVerdi<T>): UnaryInvoke<SaksbehandlervalgIDSL, T> {
        scope.lagreSaksbehandlervalg(id, saksbehandlervalgVerdi)
        return UnaryInvoke(
            UnaryInvoke(scope.argument, Select(SaksbehandlervalgIDSLSelector(clazz))),
            Select(EttSaksbehandlervalgSelector(id, saksbehandlervalgVerdi))
        )
    }
}

private class SaksbehandlervalgIDSLSelector<LetterData : RedigerbarBrevdata<SaksbehandlervalgIDSL, *>>(
    clazz: KClass<LetterData>
) : TemplateModelSelector<LetterData, SaksbehandlervalgIDSL> {
    override val className = clazz.qualifiedName!!
    override val propertyName: String = "saksbehandlerValg"
    override val propertyType: String = SaksbehandlervalgIDSL::class.qualifiedName!!
    override val selector: LetterData.() -> SaksbehandlervalgIDSL = { saksbehandlerValg }
}

private class EttSaksbehandlervalgSelector<Type>(
    override val propertyName: String,
    val saksbehandlervalgVerdi: SaksbehandlervalgVerdi<Type>
) : TemplateModelSelector<SaksbehandlervalgIDSL, Type> {
    override val className: String = SaksbehandlervalgIDSL::class.qualifiedName!!
    override val selector: SaksbehandlervalgIDSL.() -> Type = { saksbehandlervalgVerdi.getValue(this) }
    override val propertyType: String
        get() = saksbehandlervalgVerdi.typename
}

inline fun <reified LetterData : RedigerbarBrevdata<SaksbehandlervalgIDSL, *>> TemplateRootScope<*, LetterData>.saksbehandlervalg(id: String, displayText: String) = SaksbehandlerValgBuilder(id, displayText, LetterData::class, this)