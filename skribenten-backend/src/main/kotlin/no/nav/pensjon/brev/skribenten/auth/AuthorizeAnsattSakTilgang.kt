package no.nav.pensjon.brev.skribenten.auth

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.util.*
import io.ktor.util.*
import no.nav.pensjon.brev.skribenten.model.Pdl
import no.nav.pensjon.brev.skribenten.model.Pen
import no.nav.pensjon.brev.skribenten.model.SaksId
import no.nav.pensjon.brev.skribenten.routes.brevId
import no.nav.pensjon.brev.skribenten.services.BrevredigeringFacade
import no.nav.pensjon.brev.skribenten.services.PdlService
import no.nav.pensjon.brev.skribenten.services.PenService
import org.slf4j.LoggerFactory

const val SAKSID_PARAM = "saksId"
val SakKey = AttributeKey<Pen.SakSelection>("AuthorizeAnsattSakTilgang:sak")

private val logger = LoggerFactory.getLogger("AuthorizeAnsattSakTilgang")

open class AuthorizeAnsattSakTilgangConfiguration {
    lateinit var pdlService: PdlService
    lateinit var penService: PenService
}

// TODO: Vurder om disse to pluginene bør erstattes med policy-klasser som kan brukes i usecasene direkte.
//       Fordelen er at det blir mer eksplisitt, men samtidig så må det huskes på å kalle dem i alle usecasene.

val AuthorizeAnsattSakTilgang =
    createRouteScopedPlugin("AuthorizeAnsattSakTilgang", ::AuthorizeAnsattSakTilgangConfiguration) {
        on(PrincipalInContext.Hook) { call ->
            if (call.isHandled) return@on

            val saksId = SaksId(call.parameters.getOrFail<Long>(SAKSID_PARAM))
            validerTilgangTilSak(call, saksId)
        }
    }

class AuthorizeAnsattSakTilgangForBrevConfiguration : AuthorizeAnsattSakTilgangConfiguration() {
    lateinit var brevredigeringFacade: BrevredigeringFacade
}

val AuthorizeAnsattSakTilgangForBrev =
    createRouteScopedPlugin("AuthorizeAnsattSakTilgangForBrev", ::AuthorizeAnsattSakTilgangForBrevConfiguration) {
        on(PrincipalInContext.Hook) { call ->
            val brevId = call.parameters.brevId()
            val brev = pluginConfig.brevredigeringFacade.hentBrevInfo(brevId)

            if (brev != null) {
                validerTilgangTilSak(call, brev.saksId)
            }
        }
    }

private suspend fun RouteScopedPluginBuilder<out AuthorizeAnsattSakTilgangConfiguration>.validerTilgangTilSak(
    call: ApplicationCall,
    saksId: SaksId
) {
    val pdlService = pluginConfig.pdlService
    val penService = pluginConfig.penService

    val sak = penService.hentSak(saksId)

    if (sak != null) {
        call.attributes.put(SakKey, sak)
        val harTilgang = pdlService.hentAdressebeskyttelse(sak.foedselsnr, Pen.finnBehandlingsnummer(sak.sakType))
            ?.saksbehandlerHarTilgangTilGradering()
            ?: true

        if (!harTilgang) {
            logger.warn("Tilgang til sak avvist: sak med id $saksId har adressebeskyttelse")
            call.respond(HttpStatusCode.NotFound, "Sak ikke funnet")
        }
    } else {
        logger.info("Tilgang til sak avvist: sak med id $saksId ikke funnet")
        call.respond(status = HttpStatusCode.NotFound, "Sak ikke funnet")
    }
}

private suspend fun List<Pdl.Gradering>.saksbehandlerHarTilgangTilGradering(): Boolean {
    val principal = PrincipalInContext.require()

    return mapNotNull { it.toADGruppe() }.all { principal.isInGroup(it) }
}

private fun Pdl.Gradering?.toADGruppe(): ADGroup? =
    when (this) {
        Pdl.Gradering.FORTROLIG -> ADGroups.fortroligAdresse
        Pdl.Gradering.STRENGT_FORTROLIG, Pdl.Gradering.STRENGT_FORTROLIG_UTLAND -> ADGroups.strengtFortroligAdresse
        Pdl.Gradering.UGRADERT, null -> null
    }