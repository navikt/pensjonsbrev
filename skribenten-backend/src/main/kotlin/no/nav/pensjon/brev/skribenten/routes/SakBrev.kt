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
import no.nav.pensjon.brev.skribenten.services.BrevredigeringFacade
import no.nav.pensjon.brev.skribenten.services.BrevredigeringService
import no.nav.pensjon.brev.skribenten.services.Dto2ApiService
import no.nav.pensjon.brev.skribenten.services.P1ServiceImpl
import no.nav.pensjon.brev.skribenten.services.SpraakKode
import no.nav.pensjon.brev.skribenten.usecase.EndreDistribusjonstypeHandler
import no.nav.pensjon.brev.skribenten.usecase.EndreMottakerHandler
import no.nav.pensjon.brev.skribenten.usecase.HentBrevAttesteringHandler
import no.nav.pensjon.brev.skribenten.usecase.HentBrevHandler
import no.nav.pensjon.brev.skribenten.usecase.OpprettBrevHandlerImpl
import no.nav.pensjon.brev.skribenten.usecase.OppdaterBrevHandler
import no.nav.pensjon.brev.skribenten.usecase.VeksleKlarStatusHandler
import no.nav.pensjon.brevbaker.api.model.LanguageCode

fun Route.sakBrev(
    brevbakerService: BrevbakerService,
    brevredigeringService: BrevredigeringService,
    p1Service: P1ServiceImpl,
    brevredigeringFacade: BrevredigeringFacade,
    dto2ApiService: Dto2ApiService,
) =
    route("/brev") {
        suspend fun RoutingContext.respond(brevResponse: Dto.Brevredigering?) {
            if (brevResponse != null) {
                call.respond(dto2ApiService.toApi(brevResponse))
            } else {
                call.respond(HttpStatusCode.NotFound, "Fant ikke brev")
            }
        }

        get {
            val sak: Pen.SakSelection = call.attributes[SakKey]

            call.respond(
                HttpStatusCode.OK,
                brevredigeringFacade.hentBrevForSak(sak.saksId).map { dto2ApiService.toApi(it) }
            )
        }

        post<Api.OpprettBrevRequest> { request ->
            val sak: Pen.SakSelection = call.attributes[SakKey]
            val spraak = request.spraak.toLanguageCode()
            val avsenderEnhetsId = request.avsenderEnhetsId

            val brev = brevredigeringFacade.opprettBrev(
                OpprettBrevHandlerImpl.Request(
                    saksId = sak.saksId,
                    vedtaksId = request.vedtaksId,
                    brevkode = request.brevkode,
                    spraak = spraak,
                    avsenderEnhetsId = avsenderEnhetsId,
                    saksbehandlerValg = request.saksbehandlerValg,
                    reserverForRedigering = true,
                    mottaker = request.mottaker?.toDto(),
                )
            )

            apiRespond(dto2ApiService, brev, successStatus = HttpStatusCode.Created)
        }

        route("/{brevId}") {
            get {
                val brevId = call.parameters.getOrFail<Long>("brevId")
                val reserver = call.request.queryParameters["reserver"].toBoolean()

                val brev = brevredigeringFacade.hentBrev(
                    HentBrevHandler.Request(
                        brevId = brevId,
                        reserverForRedigering = reserver,
                    )
                )

                apiRespond(dto2ApiService, brev)
            }

            put<Api.OppdaterBrevRequest> { request ->
                val brevId = call.parameters.getOrFail<Long>("brevId")
                val frigiReservasjon = call.request.queryParameters["frigiReservasjon"].toBoolean()

                val result = brevredigeringFacade.oppdaterBrev(
                    OppdaterBrevHandler.Request(
                        brevId = brevId,
                        nyeSaksbehandlerValg = request.saksbehandlerValg,
                        nyttRedigertbrev = request.redigertBrev,
                        frigiReservasjon = frigiReservasjon,
                    )
                )

                apiRespond(dto2ApiService, result)
            }

            patch<Api.DelvisOppdaterBrevRequest> { request ->
                val brevId = call.parameters.getOrFail<Long>("brevId")
                val sak: Pen.SakSelection = call.attributes[SakKey]

                val brev = brevredigeringService.delvisOppdaterBrev(
                    saksId = sak.saksId,
                    brevId = brevId,
                    alltidValgbareVedlegg = request.alltidValgbareVedlegg,
                )

                respond(brev)
            }

            put<Api.DistribusjonstypeRequest>("/distribusjon") { request ->
                val brevId = call.parameters.getOrFail<Long>("brevId")

                val brev = brevredigeringFacade.endreDistribusjonstype(
                    EndreDistribusjonstypeHandler.Request(
                        brevId = brevId,
                        type = request.distribusjon,
                    )
                )

                apiRespond(dto2ApiService, brev)
            }

            put<Api.OppdaterKlarStatusRequest>("/status") { request ->
                val brevId = call.parameters.getOrFail<Long>("brevId")

                val resultat = brevredigeringFacade.veksleKlarStatus(
                    VeksleKlarStatusHandler.Request(
                        brevId = brevId,
                        klar = request.klar,
                    )
                )

                apiRespond(dto2ApiService, resultat?.then { it.info })
            }

            delete {
                val brevId = call.parameters.getOrFail<Long>("brevId")
                val sak: Pen.SakSelection = call.attributes[SakKey]

                if (brevredigeringService.slettBrev(sak.saksId, brevId)) {
                    call.respond(HttpStatusCode.NoContent)
                } else {
                    call.respond(HttpStatusCode.NotFound, "Fant ikke brev med id: $brevId")
                }
            }

            route("/mottaker") {
                put<Api.OppdaterMottakerRequest> { request ->
                    val brevId = call.parameters.getOrFail<Long>("brevId")
                    val resultat = brevredigeringFacade.endreMottaker(
                        EndreMottakerHandler.Request(brevId = brevId, mottaker = request.mottaker.toDto())
                    )

                    apiRespond(dto2ApiService, resultat?.then { it.info })
                }

                delete {
                    val brevId = call.parameters.getOrFail<Long>("brevId")
                    val resultat = brevredigeringFacade.endreMottaker(
                        EndreMottakerHandler.Request(brevId = brevId, mottaker = null)
                    )

                    apiRespond(dto2ApiService, resultat?.then { it.info })
                }
            }

            route("/pdf") {
                get {
                    val brevId = call.parameters.getOrFail<Long>("brevId")
                    val sak: Pen.SakSelection = call.attributes[SakKey]
                    val pdf = brevredigeringService.hentEllerOpprettPdf(sak.saksId, brevId)

                    if (pdf != null) {
                        call.respond(pdf)
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Fant ikke brev med id: $brevId")
                    }
                }

                post("/send") {
                    val brevId = call.parameters.getOrFail<Long>("brevId")
                    val sak: Pen.SakSelection = call.attributes[SakKey]

                    val resultat = brevredigeringService.sendBrev(saksId = sak.saksId, brevId = brevId)

                    if (resultat != null) {
                        call.respond(resultat)
                    } else {
                        call.respond(HttpStatusCode.NotFound, "Fant ikke PDF")
                    }
                }
            }

            route("/attestering") {
                get {
                    val brevId = call.parameters.getOrFail<Long>("brevId")
                    val reserver = call.request.queryParameters["reserver"].toBoolean()

                    val resultat = brevredigeringFacade.hentBrevAttestering(
                        HentBrevAttesteringHandler.Request(
                            brevId = brevId,
                            reserverForRedigering = reserver,
                        )
                    )

                    apiRespond(dto2ApiService, resultat)
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

            route("/p1") {
                get {
                    val brevId = call.parameters.getOrFail<Long>("brevId")
                    val sak: Pen.SakSelection = call.attributes[SakKey]
                    val p1Data = p1Service.hentP1Data(brevId, sak.saksId)
                    if (p1Data != null) {
                        call.respond(p1Data)
                    } else {
                        call.respond(HttpStatusCode.NotFound)
                    }

                }

                post<Api.GeneriskBrevdata> { p1Data ->
                    val brevId = call.parameters.getOrFail<Long>("brevId")
                    val sak: Pen.SakSelection = call.attributes[SakKey]
                    call.respond(p1Service.lagreP1Data(p1Data, brevId, sak.saksId))
                }
            }

            get("/alltidValgbareVedlegg") {
                val brevId = call.parameters.getOrFail<Long>("brevId")
                call.respond(brevbakerService.getAlltidValgbareVedlegg(brevId))
            }
        }
    }

private fun SpraakKode.toLanguageCode(): LanguageCode =
    when (this) {
        SpraakKode.NB -> LanguageCode.BOKMAL
        SpraakKode.NN -> LanguageCode.NYNORSK
        SpraakKode.EN -> LanguageCode.ENGLISH
        SpraakKode.FR, SpraakKode.SE -> throw BadRequestException("Brevbaker støtter ikke SpraakKode: ${this.name}")
    }