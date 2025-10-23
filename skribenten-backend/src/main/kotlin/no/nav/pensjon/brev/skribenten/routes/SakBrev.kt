package no.nav.pensjon.brev.skribenten.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import no.nav.pensjon.brev.skribenten.auth.SakKey
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.Pen
import no.nav.pensjon.brev.skribenten.model.toDto
import no.nav.pensjon.brev.skribenten.services.BrevredigeringService
import no.nav.pensjon.brev.skribenten.services.Dto2ApiService
import no.nav.pensjon.brev.skribenten.services.SpraakKode
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import org.slf4j.LoggerFactory

private val logger = LoggerFactory.getLogger("no.nav.brev.skribenten.routes.SakBrev")

fun Route.sakBrev(dto2ApiService: Dto2ApiService, brevredigeringService: BrevredigeringService) =
    route("/brev") {

        post<Api.OpprettBrevRequest> { request ->
            val sak: Pen.SakSelection = call.attributes[SakKey]
            val spraak = request.spraak.toLanguageCode()
            val avsenderEnhetsId = request.avsenderEnhetsId?.takeIf { it.isNotBlank() }

            brevredigeringService.opprettBrev(
                sak = sak,
                vedtaksId = request.vedtaksId,
                brevkode = request.brevkode,
                spraak = spraak,
                avsenderEnhetsId = avsenderEnhetsId,
                saksbehandlerValg = request.saksbehandlerValg,
                reserverForRedigering = true,
                mottaker = request.mottaker?.toDto(),
            ).onOk { brev ->
                call.respond(HttpStatusCode.Created, dto2ApiService.toApi(brev))
            }.onError { message, statusCode ->
                if (statusCode == HttpStatusCode.BadRequest) {
                    logger.warn("$statusCode - Feil ved oppretting av brev ${request.brevkode}: $message")
                    call.respond(HttpStatusCode.BadRequest, message)
                } else {
                    logger.error("$statusCode - Feil ved oppretting av brev ${request.brevkode}: $message")
                    call.respond(HttpStatusCode.InternalServerError, "Feil ved oppretting av brev.")
                }
            }
        }

        put<Api.OppdaterBrevRequest>("/{brevId}") { request ->
            val brevId = call.parameters.getOrFail<Long>("brevId")
            val sak: Pen.SakSelection = call.attributes[SakKey]
            val frigiReservasjon = call.request.queryParameters["frigiReservasjon"].toBoolean()

            brevredigeringService.oppdaterBrev(
                saksId = sak.saksId,
                brevId = brevId,
                nyeSaksbehandlerValg = request.saksbehandlerValg,
                nyttRedigertbrev = request.redigertBrev,
                frigiReservasjon = frigiReservasjon,
            )?.onOk { brev -> call.respond(HttpStatusCode.OK, dto2ApiService.toApi(brev)) }
                ?.onError { message, statusCode ->
                    logger.error("$statusCode - Feil ved oppdatering av brev ${brevId}: $message")
                    call.respond(HttpStatusCode.InternalServerError, "Feil ved oppdatering av brev.")
                }
                ?: call.respond(HttpStatusCode.NotFound, "Fant ikke brev med id: $brevId")
        }

        patch<Api.DelvisOppdaterBrevRequest>("/{brevId}") { request ->
            val brevId = call.parameters.getOrFail<Long>("brevId")
            val sak: Pen.SakSelection = call.attributes[SakKey]

            brevredigeringService.delvisOppdaterBrev(
                saksId = sak.saksId,
                brevId = brevId,
                laastForRedigering = request.laastForRedigering,
                distribusjonstype = request.distribusjonstype,
                mottaker = request.mottaker?.toDto(),
            )?.also { call.respond(HttpStatusCode.OK, dto2ApiService.toApi(it)) }
                ?: call.respond(HttpStatusCode.NotFound, "Fant ikke brev med id: $brevId")
        }

        delete("/{brevId}") {
            val brevId = call.parameters.getOrFail<Long>("brevId")
            val sak: Pen.SakSelection = call.attributes[SakKey]

            if (brevredigeringService.slettBrev(sak.saksId, brevId)) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.NotFound, "Fant ikke brev med id: $brevId")
            }
        }

        delete("/{brevId}/mottaker") {
            val brevId = call.parameters.getOrFail<Long>("brevId")
            val sak: Pen.SakSelection = call.attributes[SakKey]

            if (brevredigeringService.fjernOverstyrtMottaker(brevId, sak.saksId)) {
                call.respond(HttpStatusCode.NoContent)
            } else {
                call.respond(HttpStatusCode.NotFound, "Fant ikke brev med id: $brevId")
            }
        }

        get("/{brevId}") {
            val sak: Pen.SakSelection = call.attributes[SakKey]
            val brevId = call.parameters.getOrFail<Long>("brevId")
            val reserver = call.request.queryParameters["reserver"].toBoolean()

            brevredigeringService.hentBrev(sak.saksId, brevId, reserver)
                ?.onOk { brev ->
                    call.respond(HttpStatusCode.OK, dto2ApiService.toApi(brev))
                }?.onError { message, statusCode ->
                    call.application.log.error("$statusCode - Feil ved henting av brev: $message")
                    call.respond(HttpStatusCode.InternalServerError, "Feil ved henting av brev.")
                }
                ?: call.respond(HttpStatusCode.NotFound, "Fant ikke brev med id: $brevId")
        }

        get {
            val sak: Pen.SakSelection = call.attributes[SakKey]

            call.respond(
                HttpStatusCode.OK,
                brevredigeringService.hentBrevForSak(sak.saksId).map { dto2ApiService.toApi(it) }
            )
        }

        get("/{brevId}/pdf") {
            val brevId = call.parameters.getOrFail<Long>("brevId")
            val sak: Pen.SakSelection = call.attributes[SakKey]

            brevredigeringService.hentEllerOpprettPdf(sak.saksId, brevId)
                ?.onOk { call.respondBytes(it, ContentType.Application.Pdf, HttpStatusCode.OK) }
                ?.onError { message, _ -> call.respond(HttpStatusCode.InternalServerError, message) }
                ?: call.respond(HttpStatusCode.NotFound, "Fant ikke brev med id: $brevId")
        }

        route("/{brevId}/attestering") {
            get {
                val sak: Pen.SakSelection = call.attributes[SakKey]
                val brevId = call.parameters.getOrFail<Long>("brevId")
                val reserver = call.request.queryParameters["reserver"].toBoolean()

                brevredigeringService.hentBrevAttestering(sak.saksId, brevId, reserver)
                    ?.onOk { call.respond(HttpStatusCode.OK, dto2ApiService.toApi(it)) }
                    ?.onError { message, statusCode ->
                        call.application.log.error("$statusCode - Feil ved henting av brev: $message")
                        call.respond(HttpStatusCode.InternalServerError, "Feil ved henting av brev.")
                    }
                    ?: call.respond(HttpStatusCode.NotFound, "Fant ikke brev med id: $brevId")
            }

            put<Api.OppdaterAttesteringRequest> { request ->
                val brevId = call.parameters.getOrFail<Long>("brevId")
                val sak: Pen.SakSelection = call.attributes[SakKey]
                val frigiReservasjon = call.request.queryParameters["frigiReservasjon"].toBoolean()

                brevredigeringService.attester(
                    saksId = sak.saksId,
                    brevId = brevId,
                    nyeSaksbehandlerValg = request.saksbehandlerValg,
                    nyttRedigertbrev = request.redigertBrev,
                    frigiReservasjon = frigiReservasjon,
                )?.onOk { brev -> call.respond(HttpStatusCode.OK, dto2ApiService.toApi(brev)) }
                    ?.onError { message, statusCode ->
                        logger.error("$statusCode - Feil ved oppdatering av attestering ${brevId}: $message")
                        call.respond(HttpStatusCode.InternalServerError, "Feil ved oppdatering av attestering.")
                    }
                    ?: call.respond(HttpStatusCode.NotFound, "Fant ikke brev med id: $brevId")
            }
        }

        post("/{brevId}/pdf/send") {
            val brevId = call.parameters.getOrFail<Long>("brevId")
            val sak: Pen.SakSelection = call.attributes[SakKey]

            brevredigeringService.sendBrev(saksId = sak.saksId, brevId = brevId)
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