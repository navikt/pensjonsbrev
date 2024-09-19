package no.nav.pensjon.brev.skribenten.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import io.ktor.util.pipeline.*
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.SaksbehandlerValg
import no.nav.pensjon.brev.skribenten.services.Dto2ApiService
import no.nav.pensjon.brev.skribenten.services.BrevredigeringService
import no.nav.pensjon.brev.skribenten.services.ServiceResult
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("no.nav.brev.skribenten.routes.Brev")

fun Route.brev(brevredigeringService: BrevredigeringService, dto2ApiService: Dto2ApiService) {

    suspend fun PipelineContext<Unit, ApplicationCall>.respond(brevResponse: ServiceResult<Dto.Brevredigering>?) =
        brevResponse?.map { dto2ApiService.toApi(call, it) }
            ?.onOk { brev -> call.respond(HttpStatusCode.OK, brev) }
            ?.onError { message, statusCode ->
                logger.error("$statusCode - Feil ved oppdatering av brev: $message")
                call.respond(HttpStatusCode.InternalServerError, "Feil ved oppdatering av brev.")
            }
            ?: call.respond(HttpStatusCode.NotFound, "Fant ikke brev")

    route("/brev") {
        put<Edit.Letter>("/{brevId}/redigertBrev") { request ->
            respond(
                brevredigeringService.oppdaterBrev(
                    call = call,
                    saksId = null,
                    brevId = call.parameters.getOrFail<Long>("brevId"),
                    nyeSaksbehandlerValg = null,
                    nyttRedigertbrev = request,
                )
            )
        }

        put<SaksbehandlerValg>("/{brevId}/saksbehandlerValg") { request ->
            respond(
                brevredigeringService.oppdaterBrev(
                    call = call,
                    saksId = null,
                    brevId = call.parameters.getOrFail<Long>("brevId"),
                    nyeSaksbehandlerValg = request,
                    nyttRedigertbrev = null,
                )
            )
        }

        put<String>("/{brevId}/signatur") { signatur ->
            if (signatur.trim().isNotEmpty()) {
                respond(brevredigeringService.oppdaterSignatur(call, brevId = call.parameters.getOrFail<Long>("brevId"), signaturSignerende = signatur))
            } else {
                call.respond(HttpStatusCode.BadRequest, "Signatur kan ikke være tom")
            }
        }

        get("/{brevId}/reservasjon") {
            val brevId = call.parameters.getOrFail<Long>("brevId")
            brevredigeringService.fornyReservasjon(call, brevId)
                ?.also { call.respond(it) }
                ?: call.respond(HttpStatusCode.NotFound, "Fant ikke brev med id: $brevId")
        }
    }
}
