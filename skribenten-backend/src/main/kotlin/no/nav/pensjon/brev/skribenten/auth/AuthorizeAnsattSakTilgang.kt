package no.nav.pensjon.brev.skribenten.auth

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.di.dependencies
import io.ktor.server.response.*
import io.ktor.server.util.*
import io.ktor.util.*
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.HentBrevInfoHandler
import no.nav.pensjon.brev.skribenten.common.asSuccess
import no.nav.pensjon.brev.skribenten.fagsystem.Fagsak
import no.nav.pensjon.brev.skribenten.fagsystem.FagsakService
import no.nav.pensjon.brev.skribenten.model.Pdl
import no.nav.pensjon.brev.skribenten.model.SaksId
import no.nav.pensjon.brev.skribenten.routes.brevId
import no.nav.pensjon.brev.skribenten.services.PdlService
import org.slf4j.LoggerFactory

const val SAKSID_PARAM = "saksId"
val SakKey = AttributeKey<Fagsak>("AuthorizeAnsattSakTilgang:sak")

private val logger = LoggerFactory.getLogger("AuthorizeAnsattSakTilgang")

open class AuthorizeAnsattSakTilgangConfiguration

// TODO: Vurder om disse to pluginene bør erstattes med policy-klasser som kan brukes i usecasene direkte.
//       Fordelen er at det blir mer eksplisitt, men samtidig så må det huskes på å kalle dem i alle usecasene.

val AuthorizeAnsattSakTilgang =
    createRouteScopedPlugin("AuthorizeAnsattSakTilgang", ::AuthorizeAnsattSakTilgangConfiguration) {
        val pdlService: PdlService by application.dependencies
        val fagsakService: FagsakService by application.dependencies

        on(PrincipalInContext.Hook) { call ->
            if (call.isHandled) return@on

            val saksId = SaksId(call.parameters.getOrFail<Long>(SAKSID_PARAM))
            validerTilgangTilSak(fagsakService, pdlService, call, saksId)
        }
    }

val AuthorizeAnsattSakTilgangForBrev =
    createRouteScopedPlugin("AuthorizeAnsattSakTilgangForBrev", ::AuthorizeAnsattSakTilgangConfiguration) {
        val pdlService: PdlService by application.dependencies
        val fagsakService: FagsakService by application.dependencies
        val hentBrevInfo: HentBrevInfoHandler by application.dependencies

        on(PrincipalInContext.Hook) { call ->
            val brevId = call.parameters.brevId()
            val brev = hentBrevInfo(HentBrevInfoHandler.Request(brevId))?.asSuccess()

            if (brev != null) {
                validerTilgangTilSak(fagsakService, pdlService, call, brev.saksId)
            }
        }
    }

private suspend fun validerTilgangTilSak(
    fagsakService: FagsakService,
    pdlService: PdlService,
    call: ApplicationCall,
    saksId: SaksId
) = validerTilgangTilSak(fagsakService, saksId, pdlService)
        ?.also { call.attributes.put(SakKey, it) }
        ?: call.respond(HttpStatusCode.NotFound, "Sak ikke funnet")

suspend fun validerTilgangTilSak(fagsakService: FagsakService, saksId: SaksId, pdlService: PdlService): Fagsak? {
    val sak = fagsakService.hentSak(saksId)
    if (sak != null) {
        val harTilgang = pdlService.hentAdressebeskyttelse(sak.pid, sak.behandlingsnumre)
            ?.saksbehandlerHarTilgangTilGradering()
            ?: true

        if (!harTilgang) {
            logger.warn("Tilgang til sak avvist: sak med id $saksId har adressebeskyttelse")
            return null
        }
        return sak
    } else {
        logger.info("Tilgang til sak avvist: sak med id $saksId ikke funnet")
        return null
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