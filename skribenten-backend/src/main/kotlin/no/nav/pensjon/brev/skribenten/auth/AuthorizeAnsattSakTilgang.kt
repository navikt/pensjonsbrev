package no.nav.pensjon.brev.skribenten.auth

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.util.*
import io.ktor.util.*
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import no.nav.pensjon.brev.skribenten.auth.AuthorizeAnsattSakTilgang.NAME
import no.nav.pensjon.brev.skribenten.auth.AuthorizeAnsattSakTilgang.SAK_ID_PARAM
import no.nav.pensjon.brev.skribenten.auth.AuthorizeAnsattSakTilgang.sakKey
import no.nav.pensjon.brev.skribenten.principal
import no.nav.pensjon.brev.skribenten.services.*
import org.slf4j.LoggerFactory

object AuthorizeAnsattSakTilgang {
    const val NAME = "AuthorizeAnsattSakTilgang"
    const val SAK_ID_PARAM = "sakId"
    val sakKey = AttributeKey<PenService.SakSelection>("AuthorizeAnsattSakTilgang:sak")
}

private val logger = LoggerFactory.getLogger(AuthorizeAnsattSakTilgang::class.java)

@Suppress("FunctionName")
fun AuthorizeAnsattSakTilgang(
    navansattService: NavansattService,
    pdlService: PdlService,
    penService: PenService,
) = createRouteScopedPlugin(NAME) {
    on(AuthenticationChecked) { call ->
        coroutineScope {
            val principal = call.principal()
            val sakId = call.parameters.getOrFail(SAK_ID_PARAM)
            val navIdent = principal.navIdent

            val sakDeferred = async { penService.hentSak(call, sakId) }
            val enheterDeferred = async { navansattService.hentNavAnsattEnhetListe(call, navIdent) }

            val ikkeTilgang = sakDeferred.await().map { sak ->
                call.attributes.put(sakKey, sak)

                val adressebeskyttelseDeffered = async { pdlService.hentAdressebeskyttelse(call, sak.foedselsnr) }

                sjekkEnhetstilgang(navIdent, sak, enheterDeferred)
                    ?: sjekkAdressebeskyttelse(adressebeskyttelseDeffered.await(), principal)
            }.catch(::AuthAnsattSakTilgangResponse)

            if (ikkeTilgang != null) {
                call.respond(ikkeTilgang.status, ikkeTilgang.melding)
            }
        }
    }
}

private fun sjekkAdressebeskyttelse(adressebeskyttelse: ServiceResult<List<PdlService.Gradering>>, principal: UserPrincipal): AuthAnsattSakTilgangResponse? =
    adressebeskyttelse.map { gradering ->
        val adGrupper = gradering.mapNotNull { it.toADGruppe() }

        if (adGrupper.any { !principal.isInGroup(it) }) {
            logger.warn("Tilgang til sak avvist for ${principal.navIdent}: har ikke tilgang til gradering")
            AuthAnsattSakTilgangResponse("", HttpStatusCode.NotFound)
        } else null // får tilgang
    }.catch { msg, status ->
        logger.warn("En feil oppstod ved sjekk av adressebeskyttelse: $status - $msg")
        AuthAnsattSakTilgangResponse("En feil oppstod ved sjekk av adressebeskyttelse", HttpStatusCode.InternalServerError)
    }

private suspend fun sjekkEnhetstilgang(navIdent: String, sak: PenService.SakSelection, enheterResult: Deferred<ServiceResult<List<NAVEnhet>>>): AuthAnsattSakTilgangResponse? =
    if (sak.enhetId.isNullOrBlank()) {
        logger.error("Tilgang til sak ${sak.sakId} avvist fordi den mangler enhet")
        AuthAnsattSakTilgangResponse("Sak er ikke tilordnet enhet", HttpStatusCode.BadRequest)
    } else {
        enheterResult.await().map { enheter ->
            if (enheter.none { it.id == sak.enhetId }) {
                logger.warn("Tilgang til sak ${sak.sakId} avvist for $navIdent: mangler tilgang til enhet ${sak.enhetId}")
                AuthAnsattSakTilgangResponse("Mangler enhetstilgang til sak", HttpStatusCode.Forbidden)
            } else null // får tilgang
        }.catch { msg, status ->
            logger.error("Kunne ikke henter NAVenheter for ansatt $navIdent: $status - $msg")
            AuthAnsattSakTilgangResponse("En feil oppstod ved henting av NAVEnheter for ansatt: $navIdent", HttpStatusCode.InternalServerError)
        }
    }

private data class AuthAnsattSakTilgangResponse(val melding: String, val status: HttpStatusCode)

private fun PdlService.Gradering?.toADGruppe(): ADGroup? =
    when (this) {
        PdlService.Gradering.FORTROLIG -> ADGroups.fortroligAdresse
        PdlService.Gradering.STRENGT_FORTROLIG -> ADGroups.strengtFortroligAdresse
        PdlService.Gradering.STRENGT_FORTROLIG_UTLAND -> ADGroups.strengtFortroligUtland
        PdlService.Gradering.INGEN, null -> null
    }