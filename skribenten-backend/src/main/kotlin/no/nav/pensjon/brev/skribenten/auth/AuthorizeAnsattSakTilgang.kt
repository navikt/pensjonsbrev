package no.nav.pensjon.brev.skribenten.auth

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.util.*
import io.ktor.util.*
import no.nav.pensjon.brev.skribenten.brevredigering.application.HentBrevService
import no.nav.pensjon.brev.skribenten.fagsystem.FagsakService
import no.nav.pensjon.brev.skribenten.model.Pdl
import no.nav.pensjon.brev.skribenten.model.Pen
import no.nav.pensjon.brev.skribenten.model.SaksId
import no.nav.pensjon.brev.skribenten.routes.brevId
import no.nav.pensjon.brev.skribenten.services.PdlService
import org.slf4j.LoggerFactory

const val SAKSID_PARAM = "saksId"
val SakKey = AttributeKey<Pen.SakSelection>("AuthorizeAnsattSakTilgang:sak")

private val logger = LoggerFactory.getLogger("AuthorizeAnsattSakTilgang")

open class AuthorizeAnsattSakTilgangConfiguration {
    lateinit var pdlService: PdlService
    lateinit var fagsakService: FagsakService
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
    lateinit var hentBrevService: HentBrevService
}

val AuthorizeAnsattSakTilgangForBrev =
    createRouteScopedPlugin("AuthorizeAnsattSakTilgangForBrev", ::AuthorizeAnsattSakTilgangForBrevConfiguration) {
        on(PrincipalInContext.Hook) { call ->
            val brevId = call.parameters.brevId()
            val brev = pluginConfig.hentBrevService.hentBrevInfo(brevId)

            if (brev != null) {
                validerTilgangTilSak(call, brev.saksId)
            }
        }
    }

private suspend fun RouteScopedPluginBuilder<out AuthorizeAnsattSakTilgangConfiguration>.validerTilgangTilSak(
    call: ApplicationCall,
    saksId: SaksId
) {
    validerTilgangTilSak(pluginConfig.fagsakService, saksId, pluginConfig.pdlService, { call.attributes.put(SakKey, it) })
        .takeIf { it }
        ?: call.respond(HttpStatusCode.NotFound, "Sak ikke funnet")
}

suspend fun validerTilgangTilSak(penService: FagsakService, saksId: SaksId, pdlService: PdlService, putSak: (Pen.SakSelection) -> Unit = {}): Boolean {
    val sak = penService.hentSak(saksId)
    if (sak != null) {
        putSak(sak)
        val harTilgang = pdlService.hentAdressebeskyttelse(sak.pid, Pen.finnBehandlingsnummer(sak.sakType))
            ?.saksbehandlerHarTilgangTilGradering()
            ?: true

        if (!harTilgang) {
            logger.warn("Tilgang til sak avvist: sak med id $saksId har adressebeskyttelse")
            return false
        }
        return true
    } else {
        logger.info("Tilgang til sak avvist: sak med id $saksId ikke funnet")
        return false
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