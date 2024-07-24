package no.nav.pensjon.brev.skribenten.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import no.nav.pensjon.brev.skribenten.auth.AuthorizeAnsattSakTilgang
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.Pen
import no.nav.pensjon.brev.skribenten.services.BrevredigeringService
import no.nav.pensjon.brev.skribenten.services.SpraakKode
import no.nav.pensjon.brevbaker.api.model.LanguageCode

fun Route.sakBrev(brevredigeringService: BrevredigeringService) =
    route("/brev") {
        post<Api.OpprettBrevRequest> { request ->
            val sak: Pen.SakSelection = call.attributes[AuthorizeAnsattSakTilgang.sakKey]
            val spraak = request.spraak.toLanguageCode()
            val avsenderEnhetsId = request.avsenderEnhetsId?.takeIf { it.isNotBlank() }

            brevredigeringService.opprettBrev(call, sak, request.brevkode, spraak, avsenderEnhetsId, request.saksbehandlerValg)
                .onOk { brev ->
                    call.respond(HttpStatusCode.Created, brev)
                }.onError { message, statusCode ->
                    call.application.log.error("$statusCode - Feil ved oppretting av brev ${request.brevkode}: $message")
                    call.respond(HttpStatusCode.InternalServerError, "Feil ved oppretting av brev.")
                }
        }

        put<Api.OppdaterBrevRequest>("/{brevId}") { request ->
            val brevId = call.parameters.getOrFail<Long>("brevId")

            brevredigeringService.oppdaterBrev(call, brevId, request.saksbehandlerValg, request.redigertBrev)
                ?.onOk { brev -> call.respond(HttpStatusCode.OK, brev)}
                ?.onError { message, statusCode ->
                    call.application.log.error("$statusCode - Feil ved oppdatering av brev ${brevId}: $message")
                    call.respond(HttpStatusCode.InternalServerError, "Feil ved oppdatering av brev.")
                }
                ?: call.respond(HttpStatusCode.NotFound, "Brev med brevid: $brevId ikke funnet")
        }

        patch<Api.DelvisOppdaterBrevRequest>("/{brevId}") { request ->
            val brevId = call.parameters.getOrFail<Long>("brevId")
            brevredigeringService.delvisOppdaterBrev(brevId, request)?.also { call.respond(HttpStatusCode.OK, it) }
                ?: call.respond(HttpStatusCode.NotFound, "Brev med id $brevId: ikke funnet")
        }

        delete("/{brevId}") {
            val brevId = call.parameters.getOrFail<Long>("brevId")

            if (brevredigeringService.slettBrev(brevId)) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.NotFound, "Brev med id $brevId: ikke funnet")
            }
        }

        get("/{brevId}") {
            val brevId = call.parameters.getOrFail<Long>("brevId")

            brevredigeringService.hentBrev(call, brevId)
                ?.onOk { brev ->
                    call.respond(HttpStatusCode.OK, brev)
                }?.onError { message, statusCode ->
                    call.application.log.error("$statusCode - Feil ved henting av brev: $message")
                    call.respond(HttpStatusCode.InternalServerError, "Feil ved henting av brev.")
                }
                ?: call.respond(HttpStatusCode.NotFound, "Brev med brevid: $brevId ikke funnet")
        }

        get {
            val sak: Pen.SakSelection = call.attributes[AuthorizeAnsattSakTilgang.sakKey]

            call.respond(
                HttpStatusCode.OK,
                brevredigeringService.hentBrevForSak(sak.saksId)
            )
        }

        // TODO: Slett når frontend er endret til å bruke get
        post("/{brevId}/pdf") {
            val brevId = call.parameters.getOrFail<Long>("brevId")

            brevredigeringService.hentEllerOpprettPdf(call, brevId)
                ?.onOk { call.respondBytes(it, ContentType.Application.Pdf, HttpStatusCode.Created) }
                ?.onError { message, _ -> call.respond(HttpStatusCode.InternalServerError, message) }
                ?: call.respond(HttpStatusCode.NotFound, "Fant ikke brev: $brevId")
        }

        get("/{brevId}/pdf") {
            val brevId = call.parameters.getOrFail<Long>("brevId")
            brevredigeringService.hentEllerOpprettPdf(call, brevId)
                ?.onOk { call.respondBytes(it, ContentType.Application.Pdf, HttpStatusCode.OK) }
                ?.onError { message, _ -> call.respond(HttpStatusCode.InternalServerError, message) }
                ?: call.respond(HttpStatusCode.NotFound, "Fant ikke brev: $brevId")
        }

        post("/{brevId}/pdf/send") {
            val brevId = call.parameters.getOrFail<Long>("brevId")
            brevredigeringService.sendBrev(call, brevId)
                ?.onOk { call.respond(HttpStatusCode.OK, it) }
                ?.onError { error, _ -> call.respond(HttpStatusCode.InternalServerError, error) }
                ?: call.respond(HttpStatusCode.NotFound, "Fant ikke PDF")
        }
    }

private fun SpraakKode.toLanguageCode(): LanguageCode =
    when (this) {
        SpraakKode.NB -> LanguageCode.BOKMAL
        SpraakKode.NN -> LanguageCode.NYNORSK
        SpraakKode.EN -> LanguageCode.ENGLISH
        SpraakKode.FR, SpraakKode.SE -> throw BadRequestException("Brevbaker støtter ikke SpraakKode: ${this.name}")
    }