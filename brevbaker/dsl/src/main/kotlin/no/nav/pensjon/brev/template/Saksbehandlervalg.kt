package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgEnum
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgVerdi
import no.nav.pensjon.brev.template.dsl.TemplateRootScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import kotlin.also

@OptIn(BrevbakerDSLInternal::class)
class SaksbehandlerValgBuilder<LetterData : RedigerbarBrevdata<SaksbehandlervalgIDSL, *>> internal constructor(val id: String, val displayText: String, @PublishedApi internal val scope: TemplateRootScope<*, LetterData>) {
    init {
        require(scope.saksbehandlervalg.containsKey(id).not()) { "Saksbehandlervalg med id $id allerede definert" }
    }

    fun bool(default: Boolean = false): Expression<Boolean> = Expression.BinaryInvoke(
        scope.argument,
        id.expr(),
        BinaryOperation.EttSaksbehandlervalg<LetterData, Boolean>()
    ).also { scope.saksbehandlervalg(id, SaksbehandlervalgVerdi.Bool(default, displayText)) }

    fun int(default: Int? = null): Expression<Int?> = Expression.BinaryInvoke(
        scope.argument,
        id.expr(),
        BinaryOperation.EttSaksbehandlervalg<LetterData, Int?>()
    ).also { scope.saksbehandlervalg(id, SaksbehandlervalgVerdi.Integer(default, displayText)) }

    fun text(default: String?): Expression<String?> = Expression.BinaryInvoke(
        scope.argument,
        id.expr(),
        BinaryOperation.EttSaksbehandlervalg<LetterData, String?>()
    ).also { scope.saksbehandlervalg(id, SaksbehandlervalgVerdi.Text(default, displayText)) }

    inline fun <reified T> enum(default: T? = null): Expression<T?> where T : SaksbehandlerValgEnum, T : Enum<T> = Expression.BinaryInvoke(
        scope.argument,
        id.expr(),
        BinaryOperation.EttSaksbehandlervalg<LetterData, T?>()
    ).also { scope.saksbehandlervalg(id, SaksbehandlervalgVerdi.Enum(default, displayText, T::class.java as Class<out Enum<*>?>)) }
}

fun <LetterData : RedigerbarBrevdata<SaksbehandlervalgIDSL, *>> TemplateRootScope<*, LetterData>.saksbehandlervalg(id: String, displayText: String) = SaksbehandlerValgBuilder(id, displayText, this)