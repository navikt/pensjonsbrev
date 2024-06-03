package no.nav.pensjon.brev.skribenten.auth

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.util.*
import io.ktor.util.*
import kotlinx.coroutines.coroutineScope
import no.nav.pensjon.brev.skribenten.auth.AuthorizeAnsattSakTilgang.NAME
import no.nav.pensjon.brev.skribenten.auth.AuthorizeAnsattSakTilgang.SAKSID_PARAM
import no.nav.pensjon.brev.skribenten.auth.AuthorizeAnsattSakTilgang.sakKey
import no.nav.pensjon.brev.skribenten.principal
import no.nav.pensjon.brev.skribenten.services.*
import no.nav.pensjon.brev.skribenten.services.PenService.SakSelection
import org.slf4j.LoggerFactory

object AuthorizeAnsattSakTilgang {
    const val NAME = "AuthorizeAnsattSakTilgang"
    const val SAKSID_PARAM = "saksId"
    val sakKey = AttributeKey<SakSelection>("AuthorizeAnsattSakTilgang:sak")
}

private val logger = LoggerFactory.getLogger(AuthorizeAnsattSakTilgang::class.java)

@Suppress("FunctionName")
fun AuthorizeAnsattSakTilgang(
    pdlService: PdlService,
    penService: PenService,
) = createRouteScopedPlugin(NAME) {
    on(AuthenticationChecked) { call ->
        coroutineScope {
            val principal = call.principal()
            val saksId = call.parameters.getOrFail(SAKSID_PARAM)

            val ikkeTilgang = penService.hentSak(call, saksId).map { sak ->
                call.attributes.put(sakKey, sak)
                sjekkAdressebeskyttelse(pdlService.hentAdressebeskyttelse(call, sak.foedselsnr, sak.sakType.behandlingsnummer), principal)
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

fun harTilgangTilSakSinEnhet(navAnsattEnheter: List<NAVEnhet>, penSakEnheter: List<String>): Boolean =
    penSakEnheter.any { sakEnhet -> navAnsattEnheter.any { sakEnhet == it.id } }

private data class AuthAnsattSakTilgangResponse(val melding: String, val status: HttpStatusCode)

private fun PdlService.Gradering?.toADGruppe(): ADGroup? =
    when (this) {
        PdlService.Gradering.FORTROLIG -> ADGroups.fortroligAdresse
        PdlService.Gradering.STRENGT_FORTROLIG -> ADGroups.strengtFortroligAdresse
        PdlService.Gradering.STRENGT_FORTROLIG_UTLAND -> ADGroups.strengtFortroligUtland
        PdlService.Gradering.INGEN, null -> null
    }