package no.nav.pensjon.brev.skribenten.routes

import io.ktor.http.*
import io.ktor.server.application.Application
import io.ktor.server.plugins.di.dependencies
import io.ktor.server.request.receive
import io.ktor.server.response.*
import io.ktor.server.routing.*
import no.nav.pensjon.brev.skribenten.Features
import no.nav.pensjon.brev.skribenten.auth.SakKey
import no.nav.pensjon.brev.skribenten.brevredigering.application.attestering.AttesterBrevHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.attestering.HentBrevAttesteringHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.attestering.HentRedigertVedleggAttesteringHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.attestering.LagreAttestertBrevHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.attestering.LagreAttestertVedleggHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.livssyklus.OpprettBrevHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.oppslag.HentBrevHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.redigering.DiffBrevHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.redigering.OppdaterBrevHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.redigering.TilbakestillBrevHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.vedlegg.EndreRedigertVedleggHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.vedlegg.EndreValgteVedleggHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.vedlegg.HentRedigertVedleggHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.vedlegg.TilbakestillRedigertVedleggHandler
import no.nav.pensjon.brev.skribenten.brevredigering.domain.MarkupVersjon
import no.nav.pensjon.brev.skribenten.brevredigering.domain.VersjonertBrev
import no.nav.pensjon.brev.skribenten.brevredigering.domain.VersjonertVedlegg
import no.nav.pensjon.brev.skribenten.brevredigering.domain.somV2
import no.nav.pensjon.brev.skribenten.fagsystem.Fagsak
import no.nav.pensjon.brev.skribenten.letter.v2.EditV2
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.ApiV2
import no.nav.pensjon.brev.skribenten.model.toDto
import no.nav.pensjon.brev.skribenten.services.Dto2ApiService

context(app: Application)
fun Route.sakBrevV2() =
    route("/brev/v2") {
        val dto2ApiService: Dto2ApiService by app.dependencies

        val opprettBrev: OpprettBrevHandler by app.dependencies
        post {
            if (!Features.markupV2.isEnabled()) {
                call.respond(HttpStatusCode.NotFound)
                return@post
            }
            val request = call.receive<Api.OpprettBrevRequest>()
            val sak: Fagsak = call.attributes[SakKey]

            val brev = opprettBrev(
                OpprettBrevHandler.Request(
                    saksId = sak.saksId,
                    vedtaksId = request.vedtaksId,
                    brevkode = request.brevkode,
                    spraak = request.spraak.toLanguageCode(),
                    avsenderEnhetsId = request.avsenderEnhetsId,
                    saksbehandlerValg = request.saksbehandlerValg,
                    reserverForRedigering = true,
                    mottaker = request.mottaker?.toDto(),
                    markupVersjon = MarkupVersjon.V2,
                )
            )

            apiRespondV2(dto2ApiService, brev, successStatus = HttpStatusCode.Created)
        }

        route("/{brevId}") {
            val hentBrev: HentBrevHandler by app.dependencies
            get {
                val sak: Fagsak = call.attributes[SakKey]
                val brev = hentBrev(
                    HentBrevHandler.Request(
                        brevId = call.parameters.brevId(),
                        saksId = sak.saksId,
                        reserverForRedigering = call.request.queryParameters["reserver"].toBoolean(),
                        markupVersjon = MarkupVersjon.V2,
                    )
                )
                apiRespondV2(dto2ApiService, brev)
            }

            val oppdaterBrev: OppdaterBrevHandler by app.dependencies
            put<ApiV2.OppdaterBrevRequest> { request ->
                val sak: Fagsak = call.attributes[SakKey]
                val result = oppdaterBrev(
                    OppdaterBrevHandler.Request(
                        brevId = call.parameters.brevId(),
                        saksId = sak.saksId,
                        nyeSaksbehandlerValg = request.saksbehandlerValg,
                        nyttRedigertbrev = VersjonertBrev.V2(request.redigertBrev),
                        frigiReservasjon = call.request.queryParameters["frigiReservasjon"].toBoolean(),
                    )
                )
                apiRespondV2(dto2ApiService, result)
            }

            put<EditV2.Letter>("/redigertBrev") { request ->
                val sak: Fagsak = call.attributes[SakKey]
                val result = oppdaterBrev(
                    OppdaterBrevHandler.Request(
                        brevId = call.parameters.brevId(),
                        saksId = sak.saksId,
                        nyttRedigertbrev = VersjonertBrev.V2(request),
                        frigiReservasjon = call.request.queryParameters["frigiReservasjon"].toBoolean(),
                    )
                )
                apiRespondV2(dto2ApiService, result)
            }

            val endreValgteVedlegg: EndreValgteVedleggHandler by app.dependencies
            put<Api.ValgteVedleggRequest>("/valgteVedlegg") { request ->
                val sak: Fagsak = call.attributes[SakKey]
                val brev = endreValgteVedlegg(
                    EndreValgteVedleggHandler.Request(
                        brevId = call.parameters.brevId(),
                        saksId = sak.saksId,
                        alltidValgbareVedlegg = request.valgteVedlegg,
                        markupVersjon = MarkupVersjon.V2,
                    )
                )
                apiRespondV2(dto2ApiService, brev)
            }

            val tilbakestillBrev: TilbakestillBrevHandler by app.dependencies
            post("/tilbakestill") {
                val sak: Fagsak = call.attributes[SakKey]
                val resultat = tilbakestillBrev(
                    TilbakestillBrevHandler.Request(brevId = call.parameters.brevId(), saksId = sak.saksId, markupVersjon = MarkupVersjon.V2)
                )
                apiRespondV2(dto2ApiService, resultat)
            }

            val diffBrev: DiffBrevHandler by app.dependencies
            post<EditV2.Letter>("/diff") { request ->
                val sak: Fagsak = call.attributes[SakKey]
                val split = call.request.queryParameters["split"]?.toBoolean() ?: false
                val result = diffBrev(DiffBrevHandler.Request(call.parameters.brevId(), sak.saksId, VersjonertBrev.V2(request), split))
                respondOutcome(dto2ApiService, result) { respond(HttpStatusCode.OK, (it as DiffBrevHandler.Resultat.V2).response) }
            }

            route("/redigerbareVedlegg/{vedleggId}") {
                val hentRedigertVedlegg: HentRedigertVedleggHandler by app.dependencies
                get {
                    val sak: Fagsak = call.attributes[SakKey]
                    val result = hentRedigertVedlegg(
                        HentRedigertVedleggHandler.Request(
                            brevId = call.parameters.brevId(),
                            saksId = sak.saksId,
                            vedleggId = call.parameters.vedleggId(),
                            markupVersjon = MarkupVersjon.V2,
                        )
                    )
                    respondOutcome(dto2ApiService, result) { respond(it.somV2()) }
                }

                val endreRedigertVedlegg: EndreRedigertVedleggHandler by app.dependencies
                put<ApiV2.RedigertVedleggRequest> { request ->
                    val sak: Fagsak = call.attributes[SakKey]
                    val result = endreRedigertVedlegg(
                        EndreRedigertVedleggHandler.Request(
                            brevId = call.parameters.brevId(),
                            saksId = sak.saksId,
                            vedleggId = call.parameters.vedleggId(),
                            redigertVedlegg = VersjonertVedlegg.V2(request.redigertVedlegg),
                        )
                    )
                    respondOutcome(dto2ApiService, result) { respond(it.somV2()) }
                }

                val tilbakestillRedigertVedlegg: TilbakestillRedigertVedleggHandler by app.dependencies
                delete {
                    val sak: Fagsak = call.attributes[SakKey]
                    val result = tilbakestillRedigertVedlegg(
                        TilbakestillRedigertVedleggHandler.Request(
                            brevId = call.parameters.brevId(),
                            saksId = sak.saksId,
                            vedleggId = call.parameters.vedleggId(),
                            markupVersjon = MarkupVersjon.V2,
                        )
                    )
                    respondOutcome(dto2ApiService, result) { respond(it.somV2()) }
                }
            }

            route("/attestering") {
                val hentBrevAttestering: HentBrevAttesteringHandler by app.dependencies
                get {
                    val sak: Fagsak = call.attributes[SakKey]
                    val resultat = hentBrevAttestering(
                        HentBrevAttesteringHandler.Request(
                            brevId = call.parameters.brevId(),
                            saksId = sak.saksId,
                            reserverForRedigering = call.request.queryParameters["reserver"].toBoolean(),
                            markupVersjon = MarkupVersjon.V2,
                        )
                    )
                    apiRespondV2(dto2ApiService, resultat)
                }

                val attesterBrev: AttesterBrevHandler by app.dependencies
                put<ApiV2.OppdaterAttesteringRequest> { request ->
                    val sak: Fagsak = call.attributes[SakKey]
                    val resultat = attesterBrev(
                        AttesterBrevHandler.Request(
                            brevId = call.parameters.brevId(),
                            saksId = sak.saksId,
                            nyttRedigertbrev = VersjonertBrev.V2(request.redigertBrev),
                            frigiReservasjon = call.request.queryParameters["frigiReservasjon"].toBoolean(),
                        )
                    )
                    apiRespondV2(dto2ApiService, resultat)
                }

                val lagreAttestertBrev: LagreAttestertBrevHandler by app.dependencies
                put<EditV2.Letter>("/redigertBrev") { request ->
                    val sak: Fagsak = call.attributes[SakKey]
                    val resultat = lagreAttestertBrev(
                        LagreAttestertBrevHandler.Request(
                            brevId = call.parameters.brevId(),
                            saksId = sak.saksId,
                            nyttRedigertbrev = VersjonertBrev.V2(request),
                            frigiReservasjon = call.request.queryParameters["frigiReservasjon"].toBoolean(),
                        )
                    )
                    apiRespondV2(dto2ApiService, resultat)
                }

                route("/redigerbareVedlegg/{vedleggId}") {
                    val hentRedigertVedleggAttestering: HentRedigertVedleggAttesteringHandler by app.dependencies
                    get {
                        val sak: Fagsak = call.attributes[SakKey]
                        val result = hentRedigertVedleggAttestering(
                            HentRedigertVedleggAttesteringHandler.Request(
                                brevId = call.parameters.brevId(),
                                saksId = sak.saksId,
                                vedleggId = call.parameters.vedleggId(),
                                markupVersjon = MarkupVersjon.V2,
                            )
                        )
                        respondOutcome(dto2ApiService, result) { respond(it.somV2()) }
                    }

                    val lagreAttestertVedlegg: LagreAttestertVedleggHandler by app.dependencies
                    put<ApiV2.RedigertVedleggRequest> { request ->
                        val sak: Fagsak = call.attributes[SakKey]
                        val result = lagreAttestertVedlegg(
                            LagreAttestertVedleggHandler.Request(
                                brevId = call.parameters.brevId(),
                                saksId = sak.saksId,
                                vedleggId = call.parameters.vedleggId(),
                                redigertVedlegg = VersjonertVedlegg.V2(request.redigertVedlegg),
                            )
                        )
                        respondOutcome(dto2ApiService, result) { respond(it.somV2()) }
                    }
                }
            }
        }
    }
