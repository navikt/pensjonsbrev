package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgEnum
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgVerdi
import no.nav.pensjon.brev.template.Expression.UnaryInvoke
import no.nav.pensjon.brev.template.UnaryOperation.Select
import no.nav.pensjon.brev.template.dsl.TemplateRootScope
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import kotlin.reflect.KClass

class SaksbehandlerValgBuilder(private val id: String, private val displayText: String, private val scope: TemplateRootScope<*, RedigerbarBrevdata<*>>) {
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
            UnaryInvoke(scope.argument, saksbehandlervalgIDSLSelector),
            Select(EttSaksbehandlervalgSelector(id, saksbehandlervalgVerdi))
        )
    }
}

private val saksbehandlervalgIDSLSelector = Select(
    object : TemplateModelSelector<RedigerbarBrevdata<*>, SaksbehandlervalgIDSL> {
        override val className = RedigerbarBrevdata::class.qualifiedName!!
        override val propertyName: String = "saksbehandlerValg"
        override val propertyType: String = SaksbehandlervalgIDSL::class.qualifiedName!!
        override val selector: RedigerbarBrevdata<*>.() -> SaksbehandlervalgIDSL = RedigerbarBrevdata<*>::saksbehandlerValg
    }
)

private class EttSaksbehandlervalgSelector<Type>(
    override val propertyName: String,
    val saksbehandlervalgVerdi: SaksbehandlervalgVerdi<Type>
) : TemplateModelSelector<SaksbehandlervalgIDSL, Type> {
    override val className: String = SaksbehandlervalgIDSL::class.qualifiedName!!
    override val selector: SaksbehandlervalgIDSL.() -> Type = { saksbehandlervalgVerdi.getValue(this) }
    override val propertyType: String
        get() = saksbehandlervalgVerdi.typename
}

// Mottakertypen må ha en reell generisk typeparameter (Data) fremfor en nakent projisert
// TemplateRootScope<*, RedigerbarBrevdata<*>>: Kotlin sin subtyping tillater ikke at
// TemplateRootScope<*, RedigerbarBrevdata<KonkretType>> matcher en invariant nøstet
// stjerneprojeksjon som mottakertype (kun `Data` som egen typeparameter fungerer).
// SaksbehandlerValgBuilder bryr seg ikke om hvilken konkret Data-type det er snakk om
// (den opererer kun på det typeslettede saksbehandlerValg-feltet), så det er trygt å
// utvide til RedigerbarBrevdata<*> her.
@Suppress("UNCHECKED_CAST")
fun <Data : FagsystemBrevdata> TemplateRootScope<*, RedigerbarBrevdata<Data>>.saksbehandlervalg(id: String, displayText: String) =
    SaksbehandlerValgBuilder(id, displayText, this as TemplateRootScope<*, RedigerbarBrevdata<*>>)