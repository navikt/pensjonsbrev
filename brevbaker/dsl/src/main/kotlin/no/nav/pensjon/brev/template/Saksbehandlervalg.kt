package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgEnum
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgIDSL
import no.nav.pensjon.brev.api.model.maler.SaksbehandlervalgVerdi
import no.nav.pensjon.brev.template.dsl.TemplateRootScope
import kotlin.also

class SBWrapper(val displayText: String, val scope: TemplateRootScope<*, *>) {
    fun bool(default: Boolean = false) = Expression.FromScope.Saksbehandlervalg(displayText, default).also { scope.saksbehandlervalg[displayText] = SaksbehandlervalgVerdi.Bool(default) }
    fun int(default: Int? = null) = Expression.FromScope.Saksbehandlervalg(displayText, default).also { scope.saksbehandlervalg[displayText] = SaksbehandlervalgVerdi.Integer(default) }
    fun <T : SaksbehandlerValgEnum> enum(default: T?) = Expression.FromScope.Saksbehandlervalg(displayText, default).also { scope.saksbehandlervalg[displayText] = SaksbehandlervalgVerdi.Enum(default) }
}

fun <LetterData: RedigerbarBrevdata<SaksbehandlervalgIDSL, *>> TemplateRootScope<*, LetterData>.saksbehandlervalg(displayText: String) = SBWrapper(displayText, this)