package no.nav.pensjon.brev.skribenten.auth

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.util.*
import io.ktor.util.*
import no.nav.pensjon.brev.skribenten.model.Pdl
import no.nav.pensjon.brev.skribenten.model.Pen
import no.nav.pensjon.brev.skribenten.services.PdlService
import no.nav.pensjon.brev.skribenten.services.PenService
import no.nav.pensjon.brev.skribenten.services.ServiceResult
import org.slf4j.LoggerFactory

const val SAKSID_PARAM = "saksId"
val SakKey = AttributeKey<Pen.SakSelection>("AuthorizeAnsattSakTilgang:sak")

private val logger = LoggerFactory.getLogger("AuthorizeAnsattSakTilgang")

class AuthorizeAnsattSakTilgangConfiguration {
    lateinit var pdlService: PdlService
    lateinit var penService: PenService
}

val AuthorizeAnsattSakTilgang =
    createRouteScopedPlugin("AuthorizeAnsattSakTilgang", ::AuthorizeAnsattSakTilgangConfiguration) {
        on(PrincipalInContext.Hook) { call ->
            val saksId = call.parameters.getOrFail(SAKSID_PARAM)
            val pdlService = pluginConfig.pdlService
            val penService = pluginConfig.penService

            val ikkeTilgang =
                penService.hentSak(saksId).map { sak ->
                    call.attributes.put(SakKey, sak)
                    sjekkAdressebeskyttelse(pdlService.hentAdressebeskyttelse(sak.foedselsnr, sak.sakType.behandlingsnummer), PrincipalInContext.require())
                }.catch(::AuthAnsattSakTilgangResponse)

            if (ikkeTilgang != null) {
                call.respond(ikkeTilgang.status, ikkeTilgang.melding)
            }
        }
    }

private fun sjekkAdressebeskyttelse(
    adressebeskyttelse: ServiceResult<List<Pdl.Gradering>>,
    principal: UserPrincipal,
): AuthAnsattSakTilgangResponse? =
    adressebeskyttelse.map { gradering ->
        val adGrupper = gradering.mapNotNull { it.toADGruppe() }

        if (adGrupper.any { !principal.isInGroup(it) }) {
            logger.warn("Tilgang til sak avvist for ${principal.navIdent}: har ikke tilgang til gradering")
            AuthAnsattSakTilgangResponse("", HttpStatusCode.NotFound)
        } else {
            null // får tilgang
        }
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
