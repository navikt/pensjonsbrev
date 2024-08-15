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
            val sak: Pen.SakSelection = call.attributes[AuthorizeAnsattSakTilgang.sakKey]

            brevredigeringService.oppdaterBrev(
                call = call,
                saksId = sak.saksId,
                brevId = brevId,
                nyeSaksbehandlerValg = request.saksbehandlerValg,
                nyttRedigertbrev = request.redigertBrev
            )?.onOk { brev -> call.respond(HttpStatusCode.OK, brev)}
                ?.onError { message, statusCode ->
                    call.application.log.error("$statusCode - Feil ved oppdatering av brev ${brevId}: $message")
                    call.respond(HttpStatusCode.InternalServerError, "Feil ved oppdatering av brev.")
                }
                ?: call.respond(HttpStatusCode.NotFound, "Fant ikke brev med id: $brevId")
        }

        patch<Api.DelvisOppdaterBrevRequest>("/{brevId}") { request ->
            val brevId = call.parameters.getOrFail<Long>("brevId")
            val sak: Pen.SakSelection = call.attributes[AuthorizeAnsattSakTilgang.sakKey]

            brevredigeringService.delvisOppdaterBrev(saksId = sak.saksId, brevId = brevId, patch = request)
                ?.also { call.respond(HttpStatusCode.OK, it) }
                ?: call.respond(HttpStatusCode.NotFound, "Fant ikke brev med id: $brevId")
        }

        delete("/{brevId}") {
            val brevId = call.parameters.getOrFail<Long>("brevId")
            val sak: Pen.SakSelection = call.attributes[AuthorizeAnsattSakTilgang.sakKey]

            if (brevredigeringService.slettBrev(sak.saksId, brevId)) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.NotFound, "Fant ikke brev med id: $brevId")
            }
        }

        get("/{brevId}") {
            val sak: Pen.SakSelection = call.attributes[AuthorizeAnsattSakTilgang.sakKey]
            val brevId = call.parameters.getOrFail<Long>("brevId")
            val reserver = call.parameters["reserver"].toBoolean()

            brevredigeringService.hentBrev(call, sak.saksId, brevId, reserver)
                ?.onOk { brev ->
                    call.respond(HttpStatusCode.OK, brev)
                }?.onError { message, statusCode ->
                    call.application.log.error("$statusCode - Feil ved henting av brev: $message")
                    call.respond(HttpStatusCode.InternalServerError, "Feil ved henting av brev.")
                }
                ?: call.respond(HttpStatusCode.NotFound, "Fant ikke brev med id: $brevId")
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
            val sak: Pen.SakSelection = call.attributes[AuthorizeAnsattSakTilgang.sakKey]

            brevredigeringService.hentEllerOpprettPdf(call, sak.saksId, brevId)
                ?.onOk { call.respondBytes(it, ContentType.Application.Pdf, HttpStatusCode.Created) }
                ?.onError { message, _ -> call.respond(HttpStatusCode.InternalServerError, message) }
                ?: call.respond(HttpStatusCode.NotFound, "Fant ikke brev med id: $brevId")
        }

        get("/{brevId}/pdf") {
            val brevId = call.parameters.getOrFail<Long>("brevId")
            val sak: Pen.SakSelection = call.attributes[AuthorizeAnsattSakTilgang.sakKey]

            brevredigeringService.hentEllerOpprettPdf(call, sak.saksId, brevId)
                ?.onOk { call.respondBytes(it, ContentType.Application.Pdf, HttpStatusCode.OK) }
                ?.onError { message, _ -> call.respond(HttpStatusCode.InternalServerError, message) }
                ?: call.respond(HttpStatusCode.NotFound, "Fant ikke brev med id: $brevId")
        }

        post("/{brevId}/pdf/send") {
            val brevId = call.parameters.getOrFail<Long>("brevId")
            val sak: Pen.SakSelection = call.attributes[AuthorizeAnsattSakTilgang.sakKey]

            brevredigeringService.sendBrev(call = call, saksId = sak.saksId, brevId = brevId)
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