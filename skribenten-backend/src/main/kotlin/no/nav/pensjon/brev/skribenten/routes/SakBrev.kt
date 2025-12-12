package no.nav.pensjon.brev.skribenten.routes

import io.ktor.http.*
import io.ktor.server.plugins.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import no.nav.pensjon.brev.skribenten.auth.SakKey
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.Pen
import no.nav.pensjon.brev.skribenten.model.toDto
import no.nav.pensjon.brev.skribenten.services.BrevbakerService
import no.nav.pensjon.brev.skribenten.services.BrevredigeringService
import no.nav.pensjon.brev.skribenten.services.Dto2ApiService
import no.nav.pensjon.brev.skribenten.services.P1ServiceImpl
import no.nav.pensjon.brev.skribenten.services.SpraakKode
import no.nav.pensjon.brevbaker.api.model.LanguageCode

fun Route.sakBrev(dto2ApiService: Dto2ApiService, brevbakerService: BrevbakerService, brevredigeringService: BrevredigeringService, p1Service: P1ServiceImpl) =
    route("/brev") {
        suspend fun RoutingContext.respond(brevResponse: Dto.Brevredigering?) {
            if (brevResponse != null) {
                call.respond(dto2ApiService.toApi(brevResponse))
            } else {
                call.respond(HttpStatusCode.NotFound, "Fant ikke brev")
            }
        }

        post<Api.OpprettBrevRequest> { request ->
            val sak: Pen.SakSelection = call.attributes[SakKey]
            val spraak = request.spraak.toLanguageCode()
            val avsenderEnhetsId = request.avsenderEnhetsId?.takeIf { it.isNotBlank() }

            val brev = brevredigeringService.opprettBrev(
                sak = sak,
                vedtaksId = request.vedtaksId,
                brevkode = request.brevkode,
                spraak = spraak,
                avsenderEnhetsId = avsenderEnhetsId,
                saksbehandlerValg = request.saksbehandlerValg,
                reserverForRedigering = true,
                mottaker = request.mottaker?.toDto(),
            )

            call.respond(HttpStatusCode.Created, dto2ApiService.toApi(brev))
        }

        put<Api.OppdaterBrevRequest>("/{brevId}") { request ->
            val brevId = call.parameters.getOrFail<Long>("brevId")
            val sak: Pen.SakSelection = call.attributes[SakKey]
            val frigiReservasjon = call.request.queryParameters["frigiReservasjon"].toBoolean()

            val brev = brevredigeringService.oppdaterBrev(
                saksId = sak.saksId,
                brevId = brevId,
                nyeSaksbehandlerValg = request.saksbehandlerValg,
                nyttRedigertbrev = request.redigertBrev,
                frigiReservasjon = frigiReservasjon,
            )

            respond(brev)
        }

        patch<Api.DelvisOppdaterBrevRequest>("/{brevId}") { request ->
            val brevId = call.parameters.getOrFail<Long>("brevId")
            val sak: Pen.SakSelection = call.attributes[SakKey]

            val brev = brevredigeringService.delvisOppdaterBrev(
                saksId = sak.saksId,
                brevId = brevId,
                laastForRedigering = request.laastForRedigering,
                distribusjonstype = request.distribusjonstype,
                mottaker = request.mottaker?.toDto(),
                alltidValgbareVedlegg = request.alltidValgbareVedlegg,
            )

            respond(brev)
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

            respond(brevredigeringService.hentBrev(sak.saksId, brevId, reserver))
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
            val pdf = brevredigeringService.hentEllerOpprettPdf(sak.saksId, brevId)

            if (pdf != null) {
                call.respond(pdf)
            } else {
                call.respond(HttpStatusCode.NotFound, "Fant ikke brev med id: $brevId")
            }
        }

        route("/{brevId}/attestering") {
            get {
                val sak: Pen.SakSelection = call.attributes[SakKey]
                val brevId = call.parameters.getOrFail<Long>("brevId")
                val reserver = call.request.queryParameters["reserver"].toBoolean()

                respond(brevredigeringService.hentBrevAttestering(sak.saksId, brevId, reserver))
            }

            put<Api.OppdaterAttesteringRequest> { request ->
                val brevId = call.parameters.getOrFail<Long>("brevId")
                val sak: Pen.SakSelection = call.attributes[SakKey]
                val frigiReservasjon = call.request.queryParameters["frigiReservasjon"].toBoolean()

                respond(
                    brevredigeringService.attester(
                        saksId = sak.saksId,
                        brevId = brevId,
                        nyeSaksbehandlerValg = request.saksbehandlerValg,
                        nyttRedigertbrev = request.redigertBrev,
                        frigiReservasjon = frigiReservasjon,
                    )
                )
            }
        }

        post("/{brevId}/pdf/send") {
            val brevId = call.parameters.getOrFail<Long>("brevId")
            val sak: Pen.SakSelection = call.attributes[SakKey]

            val resultat = brevredigeringService.sendBrev(saksId = sak.saksId, brevId = brevId)

            if (resultat != null) {
                call.respond(resultat)
            } else {
                call.respond(HttpStatusCode.NotFound, "Fant ikke PDF")
            }
        }

        route("/{brevId}/p1") {
            post<Api.GeneriskBrevdata>{ p1Data ->
                val brevId = call.parameters.getOrFail<Long>("brevId")
                val sak: Pen.SakSelection = call.attributes[SakKey]
                call.respond(p1Service.lagreP1Data(p1Data, brevId, sak.saksId))
            }
            get {
                val brevId = call.parameters.getOrFail<Long>("brevId")
                val sak: Pen.SakSelection = call.attributes[SakKey]
                val p1Data = p1Service.hentP1Data(brevId, sak.saksId)
                if(p1Data != null) {
                    call.respond(p1Data)
                } else {
                    call.respond(HttpStatusCode.NotFound)
                }

            }
        }

        get("/{brevId}/alltidValgbareVedlegg") {
            val brevId = call.parameters.getOrFail<Long>("brevId")
            call.respond(brevbakerService.getAlltidValgbareVedlegg(brevId))
        }
    }

private fun SpraakKode.toLanguageCode(): LanguageCode =
    when (this) {
        SpraakKode.NB -> LanguageCode.BOKMAL
        SpraakKode.NN -> LanguageCode.NYNORSK
        SpraakKode.EN -> LanguageCode.ENGLISH
        SpraakKode.FR, SpraakKode.SE -> throw BadRequestException("Brevbaker støtter ikke SpraakKode: ${this.name}")
    }