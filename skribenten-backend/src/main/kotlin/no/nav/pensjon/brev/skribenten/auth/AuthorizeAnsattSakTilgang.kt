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
import no.nav.pensjon.brev.skribenten.auth.AuthorizeAnsattSakTilgang.SAKSID_PARAM
import no.nav.pensjon.brev.skribenten.auth.AuthorizeAnsattSakTilgang.sakKey
import no.nav.pensjon.brev.skribenten.principal
import no.nav.pensjon.brev.skribenten.services.*
import org.slf4j.LoggerFactory

object AuthorizeAnsattSakTilgang {
    const val NAME = "AuthorizeAnsattSakTilgang"
    const val SAKSID_PARAM = "saksId"
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
            val saksId = call.parameters.getOrFail(SAKSID_PARAM)
            val navIdent = principal.navIdent

            val sakDeferred = async { penService.hentSak(call, saksId) }
            val enheterDeferred = async { navansattService.hentNavAnsattEnhetListe(call, navIdent) }

            val ikkeTilgang = sakDeferred.await().map { sak ->
                call.attributes.put(sakKey, sak)

                // Rekkefølgen på disse har betydning. Om sjekkEnhetstilgang kjøres først så vil vi svare med "Mangler enhetstilgang til sak".
                // - Dette avslører at det finnes en sak for angitt saksId.
                // - Men det avslører ikke at fodselsnummer eksisterer og at det er en adressebeskyttet person.
                sjekkAdressebeskyttelse(pdlService.hentAdressebeskyttelse(call, sak.foedselsnr), principal)
                    ?: sjekkEnhetstilgang(navIdent, sak, enheterDeferred)
            }.catch(::AuthAnsattSakTilgangResponse)

            if (ikkeTilgang != null) {
                call.respond(ikkeTilgang.status, ikkeTilgang.melding)
            }
        }
    }
}

private fun sjekkAdressebeskyttelse(
    adressebeskyttelse: ServiceResult<List<PdlService.Gradering>>,
    principal: UserPrincipal
): AuthAnsattSakTilgangResponse? =
    adressebeskyttelse.map { gradering ->
        val adGrupper = gradering.mapNotNull { it.toADGruppe() }

        if (adGrupper.any { !principal.isInGroup(it) }) {
            logger.warn("Tilgang til sak avvist for ${principal.navIdent}: har ikke tilgang til gradering")
            AuthAnsattSakTilgangResponse("", HttpStatusCode.NotFound)
        } else null // får tilgang
    }.catch { _, status ->
        when (status) {
            HttpStatusCode.Forbidden -> AuthAnsattSakTilgangResponse("", HttpStatusCode.NotFound)
            else -> {
                AuthAnsattSakTilgangResponse("En feil oppstod ved validering av tilgang til sak", HttpStatusCode.InternalServerError)
            }
        }
    }

private suspend fun sjekkEnhetstilgang(
    navIdent: String,
    sak: PenService.SakSelection,
    enheterResult: Deferred<ServiceResult<List<NAVEnhet>>>
): AuthAnsattSakTilgangResponse? =
    enheterResult.await().map { enheter ->
        if (enheter.none { it.id == sak.enhetId }) {
            logger.warn("Tilgang til sak ${sak.saksId} avvist for $navIdent: mangler tilgang til enhet ${sak.enhetId}")
            AuthAnsattSakTilgangResponse("Mangler enhetstilgang til sak", HttpStatusCode.Forbidden)
        } else null // får tilgang
    }.catch { msg, status ->
        logger.error("Kunne ikke henter NAVenheter for ansatt $navIdent: $status - $msg")
        AuthAnsattSakTilgangResponse("En feil oppstod ved henting av NAVEnheter for ansatt: $navIdent", HttpStatusCode.InternalServerError)
    }

private data class AuthAnsattSakTilgangResponse(val melding: String, val status: HttpStatusCode)

private fun PdlService.Gradering?.toADGruppe(): ADGroup? =
    when (this) {
        PdlService.Gradering.FORTROLIG -> ADGroups.fortroligAdresse
        PdlService.Gradering.STRENGT_FORTROLIG -> ADGroups.strengtFortroligAdresse
        PdlService.Gradering.STRENGT_FORTROLIG_UTLAND -> ADGroups.strengtFortroligUtland
        PdlService.Gradering.INGEN, null -> null
    }