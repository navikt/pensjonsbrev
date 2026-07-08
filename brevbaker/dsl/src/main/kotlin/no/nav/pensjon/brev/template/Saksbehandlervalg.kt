package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgEnum
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgVerdi
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgVerdi.WithDefault
import no.nav.pensjon.brev.template.Expression.UnaryInvoke
import no.nav.pensjon.brev.template.UnaryOperation.Select
import no.nav.pensjon.brev.template.dsl.TemplateRootScope
import kotlin.reflect.KClass

@OptIn(BrevbakerDSLInternal::class)
class SaksbehandlerValgBuilder<LetterData : RedigerbarBrevdata<SaksbehandlervalgIDSL, *>>(val id: String, val displayText: String, val clazz: KClass<LetterData>, @PublishedApi internal val scope: TemplateRootScope<*, LetterData>) {
    init {
        require(scope.saksbehandlervalg.containsKey(id).not()) { "Saksbehandlervalg med id $id allerede definert" }
    }

    fun bool(default: Boolean = false): Expression<Boolean> = createSaksbehandlervalg(WithDefault(SaksbehandlervalgVerdi.Bool(id, displayText), default))

    fun int(default: Int): Expression<Int?> = createSaksbehandlervalg(WithDefault(SaksbehandlervalgVerdi.Integer(id, displayText), default))
    fun int(): Expression<Int?> = createSaksbehandlervalg(SaksbehandlervalgVerdi.Integer(id, displayText))

    fun text(default: String): Expression<String> = createSaksbehandlervalg(WithDefault(SaksbehandlervalgVerdi.Text(id, displayText), default))
    fun text(): Expression<String?> = createSaksbehandlervalg(SaksbehandlervalgVerdi.Text(id, displayText))

    inline fun <reified T> enum(default: T): Expression<T> where T : SaksbehandlerValgEnum, T : Enum<T> = enum(T::class, default)
    inline fun <reified T> enum(): Expression<T?> where T : SaksbehandlerValgEnum, T : Enum<T> = enum(T::class)

    @BrevbakerDSLInternal
    fun <T> enum(clazz: KClass<T>): Expression<T?> where T : SaksbehandlerValgEnum, T : Enum<T> =
        createSaksbehandlervalg(SaksbehandlervalgVerdi.Enum(id, displayText, clazz))

    @BrevbakerDSLInternal
    fun <T> enum(clazz: KClass<T>, default: T): Expression<T> where T : SaksbehandlerValgEnum, T : Enum<T> =
        createSaksbehandlervalg(WithDefault(SaksbehandlervalgVerdi.Enum(id, displayText, clazz), default))

    private fun <T> createSaksbehandlervalg(saksbehandlervalgVerdi: SaksbehandlervalgVerdi<T>): UnaryInvoke<Map<String, *>, T> {
        scope.lagreSaksbehandlervalg(id, saksbehandlervalgVerdi)
        return UnaryInvoke(
            UnaryInvoke(scope.argument, Select(SaksbehandlervalgIDSLSelector(saksbehandlervalgVerdi.typename, id, clazz))),
            Select(EttSaksbehandlervalgSelector(id, saksbehandlervalgVerdi))
        )
    }
}

private class SaksbehandlervalgIDSLSelector<LetterData : RedigerbarBrevdata<SaksbehandlervalgIDSL, *>>(
    override val propertyType: String,
    override val propertyName: String,
    clazz: KClass<LetterData>
) : TemplateModelSelector<LetterData, Map<String, *>> {
    override val className = clazz.qualifiedName!!
    override val selector: LetterData.() -> Map<String, *> = { (saksbehandlerValg as Map<String, *>) }
}

private class EttSaksbehandlervalgSelector<Type>(
    override val propertyName: String,
    val saksbehandlervalgVerdi: SaksbehandlervalgVerdi<Type>
) : TemplateModelSelector<Map<String, *>, Type> {
    override val className: String = "SaksbehandlervalgIDSL"
    override val selector: Map<String, *>.() -> Type = { saksbehandlervalgVerdi.getValue(this) }
    override val propertyType: String
        get() = saksbehandlervalgVerdi.typename
}

inline fun <reified LetterData : RedigerbarBrevdata<SaksbehandlervalgIDSL, *>> TemplateRootScope<*, LetterData>.saksbehandlervalg(id: String, displayText: String) = SaksbehandlerValgBuilder(id, displayText, LetterData::class, this)