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
import no.nav.pensjon.brev.skribenten.model.Pdl
import no.nav.pensjon.brev.skribenten.model.Pen
import no.nav.pensjon.brev.skribenten.principal
import no.nav.pensjon.brev.skribenten.services.*
import org.slf4j.LoggerFactory

object AuthorizeAnsattSakTilgang {
    const val NAME = "AuthorizeAnsattSakTilgang"
    const val SAKSID_PARAM = "saksId"
    val sakKey = AttributeKey<Pen.SakSelection>("AuthorizeAnsattSakTilgang:sak")
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
    adressebeskyttelse: ServiceResult<List<Pdl.Gradering>>,
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

private data class AuthAnsattSakTilgangResponse(val melding: String, val status: HttpStatusCode)

private fun Pdl.Gradering?.toADGruppe(): ADGroup? =
    when (this) {
        Pdl.Gradering.FORTROLIG -> ADGroups.fortroligAdresse
        Pdl.Gradering.STRENGT_FORTROLIG -> ADGroups.strengtFortroligAdresse
        Pdl.Gradering.STRENGT_FORTROLIG_UTLAND -> ADGroups.strengtFortroligUtland
        Pdl.Gradering.INGEN, null -> null
    }