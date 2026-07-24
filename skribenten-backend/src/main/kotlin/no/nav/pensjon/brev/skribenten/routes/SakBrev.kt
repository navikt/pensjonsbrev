package no.nav.pensjon.brev.skribenten.routes

import io.ktor.http.*
import io.ktor.server.application.Application
import io.ktor.server.plugins.*
import io.ktor.server.plugins.di.dependencies
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.pensjon.brev.skribenten.auth.SakKey
import no.nav.pensjon.brev.skribenten.brevredigering.application.usecases.*
import no.nav.pensjon.brev.skribenten.brevredigering.domain.P1RedigerbarDto
import no.nav.pensjon.brev.skribenten.common.asSuccess
import no.nav.pensjon.brev.skribenten.fagsystem.Fagsak
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.SpraakKode
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.toDto
import no.nav.pensjon.brev.skribenten.services.Dto2ApiService
import no.nav.pensjon.brevbaker.api.model.LanguageCode

context(app: Application)
fun Route.sakBrev() =
    route("/brev") {
        val dto2ApiService: Dto2ApiService by app.dependencies
        val hentBrevForSak: HentBrevForSakHandler by app.dependencies

        get {
            val sak: Fagsak = call.attributes[SakKey]
            respondSuccess(hentBrevForSak(HentBrevForSakHandler.Request(sak.saksId))?.asSuccess()) {
                respond(HttpStatusCode.OK, it.map { brev -> dto2ApiService.toApi(brev) })
            }
        }

        val opprettBrev: OpprettBrevHandler by app.dependencies
        post {
            val request = call.receive<Api.OpprettBrevRequest>()
            val sak: Fagsak = call.attributes[SakKey]
            val spraak = request.spraak.toLanguageCode()
            val avsenderEnhetsId = request.avsenderEnhetsId

            val brev = opprettBrev(
                OpprettBrevHandler.Request(
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
            val hentBrev: HentBrevHandler by app.dependencies

            get {
                val brevId = call.parameters.brevId()
                val reserver = call.request.queryParameters["reserver"].toBoolean()

                val brev = hentBrev(
                    HentBrevHandler.Request(
                        brevId = brevId,
                        reserverForRedigering = reserver,
                    )
                )

                apiRespond(dto2ApiService, brev)
            }

            val oppdaterBrev: OppdaterBrevHandler by app.dependencies
            put {
                val request = call.receive<Api.OppdaterBrevRequest>()
                val brevId = call.parameters.brevId()
                val frigiReservasjon = call.request.queryParameters["frigiReservasjon"].toBoolean()

                val result = oppdaterBrev(
                    OppdaterBrevHandler.Request(
                        brevId = brevId,
                        nyeSaksbehandlerValg = request.saksbehandlerValg,
                        nyttRedigertbrev = request.redigertBrev,
                        frigiReservasjon = frigiReservasjon,
                    )
                )

                apiRespond(dto2ApiService, result)
            }

            // TODO: fjern når frontend er oppdatert til å bruke endreValgteVedlegg-endepunktet
            val endreValgteVedlegg: EndreValgteVedleggHandler by app.dependencies
            patch {
                val request = call.receive<Api.DelvisOppdaterBrevRequest>()
                val brevId = call.parameters.brevId()

                if (request.alltidValgbareVedlegg != null) {
                    val brev = endreValgteVedlegg(
                        EndreValgteVedleggHandler.Request(
                            brevId = brevId,
                            alltidValgbareVedlegg = request.alltidValgbareVedlegg,
                        )
                    )
                    apiRespond(dto2ApiService, brev)
                } else {
                    val brev = hentBrev(HentBrevHandler.Request(brevId = brevId, reserverForRedigering = false))
                    apiRespond(dto2ApiService, brev)
                }
            }

            val endreDistribusjonstype: EndreDistribusjonstypeHandler by app.dependencies
            put("/distribusjon") {
                val request = call.receive<Api.DistribusjonstypeRequest>()
                val brevId = call.parameters.brevId()

                val brevInfo = endreDistribusjonstype(
                    EndreDistribusjonstypeHandler.Request(
                        brevId = brevId,
                        type = request.distribusjon,
                    )
                )

                apiRespond(dto2ApiService, brevInfo)
            }

            put("/valgteVedlegg") {
                val request = call.receive<Api.ValgteVedleggRequest>()
                val brevId = call.parameters.brevId()

                val brev = endreValgteVedlegg(
                    EndreValgteVedleggHandler.Request(
                        brevId = brevId,
                        alltidValgbareVedlegg = request.valgteVedlegg,
                    )
                )

                apiRespond(dto2ApiService, brev)
            }

            route("/redigerbareVedlegg") {
                val hentRedigerbareVedlegg: HentRedigerbareVedleggHandler by app.dependencies
                get {
                    val brevId = call.parameters.brevId()

                    val result = hentRedigerbareVedlegg(
                        HentRedigerbareVedleggHandler.Request(brevId = brevId)
                    )

                    respondSuccess(result?.asSuccess()) { respond(it) }
                }
                route("{vedleggId}") {
                    val hentRedigertVedlegg: HentRedigertVedleggHandler by app.dependencies
                    get {
                        val brevId = call.parameters.brevId()
                        val vedleggId = call.parameters.vedleggId()

                        val result = hentRedigertVedlegg(
                            HentRedigertVedleggHandler.Request(brevId = brevId, vedleggId = vedleggId)
                        )

                        respondOutcome(dto2ApiService, result) { respond(it) }
                    }

                    val endreRedigertVedlegg: EndreRedigertVedleggHandler by app.dependencies
                    put<Api.RedigertVedleggRequest> { request ->
                        val brevId = call.parameters.brevId()
                        val vedleggId = call.parameters.vedleggId()

                        val brev = endreRedigertVedlegg(
                            EndreRedigertVedleggHandler.Request(
                                brevId = brevId,
                                vedleggId = vedleggId,
                                redigertVedlegg = request.redigertVedlegg,
                            )
                        )

                        apiRespond(dto2ApiService, brev)
                    }

                    val slettRedigertVedlegg: SlettRedigertVedleggHandler by app.dependencies
                    delete {
                        val brevId = call.parameters.brevId()
                        val vedleggId = call.parameters.vedleggId()

                        val brev = slettRedigertVedlegg(
                            SlettRedigertVedleggHandler.Request(brevId = brevId, vedleggId = vedleggId)
                        )

                        apiRespond(dto2ApiService, brev)
                    }
                }
            }

            val veksleKlarStatus: VeksleKlarStatusHandler by app.dependencies
            put("/status") {
                val request = call.receive<Api.OppdaterKlarStatusRequest>()
                val brevId = call.parameters.brevId()

                val brevInfo = veksleKlarStatus(
                    VeksleKlarStatusHandler.Request(
                        brevId = brevId,
                        klar = request.klar,
                    )
                )

                apiRespond(dto2ApiService, brevInfo)
            }

            val slettBrev: SlettBrevHandler by app.dependencies
            delete {
                val brevId = call.parameters.brevId()

                val result = slettBrev(SlettBrevHandler.Request(brevId = brevId))
                apiRespond(dto2ApiService, result)
            }

            route("/mottaker") {
                val endreMottaker: EndreMottakerHandler by app.dependencies

                put {
                    val request = call.receive<Api.OppdaterMottakerRequest>()
                    val brevId = call.parameters.brevId()
                    val brevInfo = endreMottaker(
                        EndreMottakerHandler.Request(brevId = brevId, mottaker = request.mottaker.toDto())
                    )

                    apiRespond(dto2ApiService, brevInfo)
                }

                delete {
                    val brevId = call.parameters.brevId()
                    val brevInfo = endreMottaker(
                        EndreMottakerHandler.Request(brevId = brevId, mottaker = null)
                    )

                    apiRespond(dto2ApiService, brevInfo)
                }
            }

            route("/pdf") {
                val hentEllerOpprettPdf: HentEllerOpprettPdfHandler by app.dependencies
                get {
                    val brevId = call.parameters.brevId()
                    val sak: Fagsak = call.attributes[SakKey]

                    val result = hentEllerOpprettPdf(HentEllerOpprettPdfHandler.Request(brevId = brevId, fagsak = sak))
                    apiRespond(dto2ApiService, result)
                }

                val sendBrev: SendBrevHandler by app.dependencies
                post("/send") {
                    val brevId = call.parameters.brevId()

                    val resultat = sendBrev(SendBrevHandler.Request(brevId = brevId))
                    apiRespond(dto2ApiService, resultat)
                }
            }

            route("/attestering") {
                val hentBrevAttestering: HentBrevAttesteringHandler by app.dependencies

                get {
                    val brevId = call.parameters.brevId()
                    val reserver = call.request.queryParameters["reserver"].toBoolean()

                    val resultat = hentBrevAttestering(
                        HentBrevAttesteringHandler.Request(
                            brevId = brevId,
                            reserverForRedigering = reserver,
                        )
                    )

                    apiRespond(dto2ApiService, resultat)
                }

                val attesterBrev: AttesterBrevHandler by app.dependencies
                put {
                    val request = call.receive<Api.OppdaterAttesteringRequest>()
                    val brevId = call.parameters.brevId()
                    val frigiReservasjon = call.request.queryParameters["frigiReservasjon"].toBoolean()

                    val resultat = attesterBrev(
                        AttesterBrevHandler.Request(
                            brevId = brevId,
                            nyeSaksbehandlerValg = request.saksbehandlerValg,
                            nyttRedigertbrev = request.redigertBrev,
                            frigiReservasjon = frigiReservasjon,
                        )
                    )

                    apiRespond(dto2ApiService, resultat)
                }
            }

            // TODO: Request/response body er sterkt typet i frontend, men ikke her i backend.
            route("/p1") {
                val hentP1Data: HentP1DataHandler by app.dependencies
                get {
                    val brevId = call.parameters.brevId()
                    val sak: Fagsak = call.attributes[SakKey]

                    val result = hentP1Data(HentP1DataHandler.Request(brevId = brevId, saksId = sak.saksId))
                    respondSuccess(result?.asSuccess()) { respond(it) }
                }

                val lagreP1Data: LagreP1DataHandler by app.dependencies
                post<P1RedigerbarDto> { p1Data ->
                    val brevId = call.parameters.brevId()
                    val sak: Fagsak = call.attributes[SakKey]

                    val result = lagreP1Data(LagreP1DataHandler.Request(brevId = brevId, saksId = sak.saksId, p1Data = p1Data))
                    respondSuccess(result?.asSuccess()) { respond(it) }
                }
            }

            val hentAlltidValgbareVedlegg: HentAlltidValgbareVedleggHandler by app.dependencies
            get("/alltidValgbareVedlegg") {
                val brevId = call.parameters.brevId()

                val result = hentAlltidValgbareVedlegg(
                    HentAlltidValgbareVedleggHandler.Request(brevId = brevId)
                )

                respondSuccess(result?.asSuccess()) { respond(it) }
            }

            val leggVedFoersteside: LeggVedFoerstesideHandler by app.dependencies
            put("/foersteside") {
                val brevId = call.parameters.brevId()
                val request = call.receive<Api.OppdaterFoerstesideRequest>()
                val resultat = leggVedFoersteside(LeggVedFoerstesideHandler.Request(brevId, request.leggVedFoersteside))

                apiRespond(dto2ApiService, resultat)
            }
        }
    }

fun SpraakKode.toLanguageCode(): LanguageCode =
    when (this) {
        SpraakKode.NB -> LanguageCode.BOKMAL
        SpraakKode.NN -> LanguageCode.NYNORSK
        SpraakKode.EN -> LanguageCode.ENGLISH
        SpraakKode.FR, SpraakKode.SE -> throw BadRequestException("Brevbaker støtter ikke SpraakKode: ${this.name}")
    }