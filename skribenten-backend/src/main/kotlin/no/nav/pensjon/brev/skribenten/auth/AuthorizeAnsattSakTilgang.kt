package no.nav.pensjon.brev.skribenten.auth

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.di.dependencies
import io.ktor.server.response.*
import io.ktor.server.util.*
import io.ktor.util.*
import no.nav.pensjon.brev.skribenten.brevredigering.application.oppslag.HentBrevInfoHandler
import no.nav.pensjon.brev.skribenten.common.Cache
import no.nav.pensjon.brev.skribenten.common.Cacheomraade
import no.nav.pensjon.brev.skribenten.common.asSuccess
import no.nav.pensjon.brev.skribenten.common.cached
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

val AuthorizeAnsattSakTilgang =
    createRouteScopedPlugin("AuthorizeAnsattSakTilgang", ::AuthorizeAnsattSakTilgangConfiguration) {
        val pdlService: PdlService by application.dependencies
        val fagsakService: FagsakService by application.dependencies
        val cache: Cache by application.dependencies

        on(PrincipalInContext.Hook) { call ->
            if (call.isHandled) return@on

            val saksId = SaksId(call.parameters.getOrFail<Long>(SAKSID_PARAM))
            validerTilgangTilSak(fagsakService, pdlService, cache, call, saksId)
        }
    }

val AuthorizeAnsattSakTilgangForBrev =
    createRouteScopedPlugin("AuthorizeAnsattSakTilgangForBrev", ::AuthorizeAnsattSakTilgangConfiguration) {
        val pdlService: PdlService by application.dependencies
        val fagsakService: FagsakService by application.dependencies
        val hentBrevInfo: HentBrevInfoHandler by application.dependencies
        val cache: Cache by application.dependencies

        on(PrincipalInContext.Hook) { call ->
            if (call.isHandled) return@on

            val brevId = call.parameters.brevId()
            val brevInfo = hentBrevInfo(HentBrevInfoHandler.Request(brevId))?.asSuccess()?.value
            if (brevInfo != null) {
                validerTilgangTilSak(fagsakService, pdlService, cache, call, brevInfo.saksId)
            } else {
                logger.info("Tilgang til brev avvist: brev med id $brevId ikke funnet")
                call.respond(HttpStatusCode.NotFound, "Brev ikke funnet")
            }
        }
    }

private suspend fun validerTilgangTilSak(
    fagsakService: FagsakService,
    pdlService: PdlService,
    cache: Cache,
    call: ApplicationCall,
    saksId: SaksId
) = validerTilgangTilSak(fagsakService, saksId, pdlService, cache)
        ?.also { call.attributes.put(SakKey, it) }
        ?: call.respond(HttpStatusCode.NotFound, "Sak ikke funnet")

suspend fun validerTilgangTilSak(fagsakService: FagsakService, saksId: SaksId, pdlService: PdlService, cache: Cache): Fagsak? =
    cache.cached(Cacheomraade.FAGSAK, Pair(saksId, PrincipalInContext.require().navIdent)) {
        val sak = fagsakService.hentSak(saksId)
        if (sak != null) {
            val harTilgang = pdlService.hentAdressebeskyttelse(sak.pid, sak.behandlingsnumre)
                ?.saksbehandlerHarTilgangTilGradering()
                ?: true

            if (!harTilgang) {
                logger.warn("Tilgang til sak avvist: sak med id $saksId har adressebeskyttelse")
                null
            } else {
                sak
            }
        } else {
            logger.info("Tilgang til sak avvist: sak med id $saksId ikke funnet")
            null
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