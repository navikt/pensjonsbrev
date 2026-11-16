package no.nav.pensjon.brev.skribenten.routes

import io.ktor.server.application.*
import io.ktor.server.plugins.di.dependencies
import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.auth.SakKey
import no.nav.pensjon.brev.skribenten.brevredigering.application.oppslag.HentBrevInfoHandler
import no.nav.pensjon.brev.skribenten.brevredigering.domain.FeilMarkupVersjonException
import no.nav.pensjon.brev.skribenten.brevredigering.domain.MarkupVersjon
import no.nav.pensjon.brev.skribenten.common.asSuccess

class RequireMarkupVersionConfiguration {
    var version: MarkupVersjon = MarkupVersjon.V1
}

val RequireMarkupVersion =
    createRouteScopedPlugin("RequireMarkupVersion", ::RequireMarkupVersionConfiguration) {
        val hentBrevInfo: HentBrevInfoHandler by application.dependencies
        val version = pluginConfig.version

        on(PrincipalInContext.Hook) { call ->
            if (call.isHandled) return@on

            val brevInfo = hentBrevInfo(HentBrevInfoHandler.Request(call.parameters.brevId()))?.asSuccess()?.value
            if (brevInfo != null && brevInfo.saksId == call.attributes.getOrNull(SakKey)?.saksId && brevInfo.markupVersjon != version) {
                throw FeilMarkupVersjonException(brevInfo.markupVersjon, version)
            }
        }
    }
