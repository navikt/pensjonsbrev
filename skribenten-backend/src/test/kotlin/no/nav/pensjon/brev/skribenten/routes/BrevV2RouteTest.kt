@file:OptIn(no.nav.brev.InternKonstruktoer::class)

package no.nav.pensjon.brev.skribenten.routes

import no.nav.pensjon.brev.skribenten.brevredigering.application.BrevredigeringHandlerTestBase.RedigerbareVedlegg
import com.fasterxml.jackson.module.kotlin.readValue
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BasicAuthCredentials
import io.ktor.client.plugins.auth.providers.basic
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.plugins.di.dependencies
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import io.ktor.server.routing.*
import io.ktor.server.testing.*
import no.nav.pensjon.brev.skribenten.Features
import no.nav.pensjon.brev.skribenten.Testbrevkoder
import no.nav.pensjon.brev.skribenten.auth.PrincipalInContext
import no.nav.pensjon.brev.skribenten.auth.SakKey
import no.nav.pensjon.brev.skribenten.brevredigering.application.BrevredigeringHandlerTestBase
import no.nav.pensjon.brev.skribenten.brevredigering.application.p1.HentP1DataHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.p1.LagreP1DataHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.redigering.DiffBrevHandler
import no.nav.pensjon.brev.skribenten.brevredigering.application.vedlegg.HentAlltidValgbareVedleggHandler
import no.nav.pensjon.brev.skribenten.brevredigering.domain.FeilMarkupVersjonException
import no.nav.pensjon.brev.skribenten.brevredigering.domain.MarkupVersjon
import no.nav.pensjon.brev.skribenten.db.databaseObjectMapper
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.SpraakKode
import no.nav.pensjon.brev.skribenten.letter.v2.EditV2
import no.nav.pensjon.brev.skribenten.letter.v2.MarkupV2
import no.nav.pensjon.brev.skribenten.letter.v2.ContentIndexV2
import no.nav.pensjon.brev.skribenten.letter.v2.DiffSegmentV2
import no.nav.pensjon.brev.skribenten.letter.v2.UnifiedDiffV2
import no.nav.pensjon.brev.skribenten.letter.v2.attachment
import no.nav.pensjon.brev.skribenten.letter.v2.letter as letterV2
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.ApiV2
import no.nav.pensjon.brev.skribenten.model.SaksbehandlervalgMap
import no.nav.pensjon.brev.skribenten.model.VedtaksId
import no.nav.pensjon.brev.skribenten.observability.BrevoppdateringObservability
import no.nav.pensjon.brev.skribenten.override
import no.nav.pensjon.brev.skribenten.services.Dto2ApiService
import no.nav.pensjon.brev.skribenten.services.FakeNorg2Service
import no.nav.pensjon.brev.skribenten.services.NavEnhet
import no.nav.pensjon.brev.skribenten.services.PenClientStub
import no.nav.pensjon.brev.skribenten.skribentenContenNegotiation
import no.nav.pensjon.brevbaker.api.model.TemplateModelSpecification
import no.nav.pensjon.brevbaker.api.model.AlltidValgbartVedleggBrevkode
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.VedleggId
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode
import org.junit.jupiter.api.parallel.Isolated

@Isolated
@Execution(ExecutionMode.SAME_THREAD)
class BrevV2RouteTest : BrevredigeringHandlerTestBase() {
    private val basePath = "/sak/${sak1.saksId}/brev"
    private val mapper = databaseObjectMapper

    @AfterEach
    fun resetToggle() {
        Features.override(Features.markupV2, false)
    }

    private fun routeTest(block: suspend ApplicationTestBuilder.(HttpClient) -> Unit) = testApplication {
        brevbakerService.modelSpecificationResultat = TemplateModelSpecification(types = emptyMap(), letterModelTypeName = "Test")
        brevbakerService.renderMarkupV2Resultat = { felles ->
            letterV2(
                MarkupV2.paragraph(1, MarkupV2.literal(2, "original")),
                signatur = MarkupV2.signatur(
                    saksbehandlerNavn = felles.signerendeSaksbehandlere?.saksbehandler,
                    attesterendeSaksbehandlerNavn = felles.signerendeSaksbehandlere?.attesterendeSaksbehandler,
                ),
            )
        }
        application {
            skribentenContenNegotiation()
            install(StatusPages) {
                exception<FeilMarkupVersjonException> { call, cause -> call.respond(HttpStatusCode.Conflict, cause.message ?: "") }
            }
            install(Authentication) {
                basic("test") {
                    validate {
                        if (it.password != "test") null else when (it.name) {
                            "test" -> saksbehandler1Principal
                            "attestant" -> attestant1Principal
                            else -> null
                        }
                    }
                }
            }
            intercept(ApplicationCallPipeline.Call) {
                call.attributes.put(SakKey, fagsak1)
            }
            dependencies {
                provide { opprettBrev }
                provide { hentBrevInfoHandler }
                provide { hentBrev }
                provide { oppdaterBrev }
                provide { tilbakestillBrev }
                provide { DiffBrevHandler(brevtilgang, brevdataService, brevmalService) }
                provide { veksleKlarStatus }
                provide { hentEllerOpprettPdf }
                provide { sendBrevHandler }
                provide { hentBrevForSakHandler }
                provide { endreValgteVedlegg }
                provide { endreDistribusjonstype }
                provide { hentRedigerbareVedlegg }
                provide { hentRedigertVedlegg }
                provide { endreRedigertVedlegg }
                provide { tilbakestillRedigertVedlegg }
                provide { slettBrevHandler }
                provide { endreMottaker }
                provide { hentBrevAttestering }
                provide { attesterBrev }
                provide { lagreAttestertBrev }
                provide { hentEllerOpprettAttesteringPdf }
                provide { hentRedigerbareVedleggAttestering }
                provide { hentRedigertVedleggAttestering }
                provide { lagreAttestertVedlegg }
                provide { HentP1DataHandler(brevtilgang, PenClientStub()) }
                provide { LagreP1DataHandler(brevtilgang) }
                provide { HentAlltidValgbareVedleggHandler(brevtilgang, brevmalService) }
                provide { leggVedFoersteside }
                provide { BrevoppdateringObservability() }
                provide {
                    Dto2ApiService(
                        brevmalService,
                        navAnsattService,
                        FakeNorg2Service(mapOf(PRINCIPAL_NAVENHET_ID.value to NavEnhet(PRINCIPAL_NAVENHET_ID, "Testenhet"))),
                        samhandlerService,
                    )
                }
            }
            routing {
                authenticate("test") {
                    install(PrincipalInContext)
                    route("/sak/{saksId}") {
                        sakBrev()
                        sakBrevV2()
                    }
                }
            }
        }
        val authenticatedClient = createClient {
            install(Auth) {
                basic {
                    credentials { BasicAuthCredentials("test", "test") }
                    sendWithoutRequest { true }
                }
            }
        }
        block(authenticatedClient)
    }

    private suspend fun HttpClient.createV2(request: Api.OpprettBrevRequest = createRequest()): ApiV2.BrevResponse {
        val response = post("$basePath/v2") {
            contentType(ContentType.Application.Json)
            setBody(mapper.writeValueAsString(request))
        }
        assertThat(response.status).isEqualTo(HttpStatusCode.Created)
        return mapper.readValue(response.bodyAsText())
    }

    private fun createRequest() = Api.OpprettBrevRequest(
        brevkode = no.nav.pensjon.brev.skribenten.Testbrevkoder.INFORMASJONSBREV,
        spraak = SpraakKode.EN,
        avsenderEnhetsId = PRINCIPAL_NAVENHET_ID,
        saksbehandlerValg = SaksbehandlervalgMap(),
        reserverForRedigering = true,
        mottaker = null,
        vedtaksId = null,
    )

    @Test
    fun `toggle av hindrer v2-oppretting men v1 virker fortsatt`() = routeTest { client ->
        Features.override(Features.markupV2, false)
        assertThat(client.post("$basePath/v2").status).isEqualTo(HttpStatusCode.NotFound)
        val response = client.post(basePath) {
            contentType(ContentType.Application.Json)
            setBody(mapper.writeValueAsString(createRequest()))
        }
        assertThat(response.status).isEqualTo(HttpStatusCode.Created)
        assertThat(mapper.readValue<Api.BrevResponse>(response.bodyAsText()).info.markupVersjon).isEqualTo(MarkupVersjon.V1)
    }

    @Test
    fun `v2 kan opprettes redigeres gjenapnes tilbakestilles og sendes naar toggle slaas av`() = routeTest { client ->
        Features.override(Features.markupV2, true)
        val original = client.createV2()
        val path = "$basePath/v2/${original.info.id}"
        val paragraph = original.redigertBrev.blocks.single() as EditV2.Block.Paragraph
        val edited = original.redigertBrev.copy(
            blocks = listOf(paragraph.copy(content = listOf((paragraph.content.single() as EditV2.Text.Literal).copy(editedText = "edited")))),
        )
        Features.override(Features.markupV2, false)
        val update = client.put(path) {
            contentType(ContentType.Application.Json)
            setBody(mapper.writeValueAsString(ApiV2.OppdaterBrevRequest(SaksbehandlervalgMap(), edited)))
        }
        assertThat(update.status).isEqualTo(HttpStatusCode.OK)
        val reopened = mapper.readValue<ApiV2.BrevResponse>(client.get(path).bodyAsText())
        assertThat(reopened.redigertBrev).isEqualTo(edited)
        assertThat(reopened.redigertBrevHash).isNotEqualTo(original.redigertBrevHash)
        assertThat(client.get("$basePath/${original.info.id}").status).isEqualTo(HttpStatusCode.Conflict)

        assertThat(client.get("$basePath/${original.info.id}/pdf").status).isEqualTo(HttpStatusCode.OK)
        assertThat(brevbakerService.renderPdfV2Kall).hasSize(1)
        assertThat(brevbakerService.renderPdfKall).isEmpty()

        val reset = client.post("$path/tilbakestill")
        assertThat(reset.status).isEqualTo(HttpStatusCode.OK)
        val resetLetter = mapper.readValue<ApiV2.BrevResponse>(reset.bodyAsText()).redigertBrev
        assertThat(((resetLetter.blocks.single() as EditV2.Block.Paragraph).content.single() as EditV2.Text.Literal).editedText).isNull()

        assertThat(client.get("$basePath/${original.info.id}/pdf").status).isEqualTo(HttpStatusCode.OK)
        assertThat(client.put("$basePath/${original.info.id}/status") {
            contentType(ContentType.Application.Json)
            setBody("""{"klar":true}""")
        }.status).isEqualTo(HttpStatusCode.OK)
        assertThat(client.post("$basePath/${original.info.id}/pdf/send").status).isEqualTo(HttpStatusCode.OK)
        assertThat(penService.utfoerteSendBrevKall).hasSize(1)
        assertThat(client.get(path).status).isEqualTo(HttpStatusCode.NotFound)
    }

    @Test
    fun `v2-endepunkter avviser v1-brev uten aa endre det`() = routeTest { client ->
        Features.override(Features.markupV2, true)
        val v1 = mapper.readValue<Api.BrevResponse>(client.post(basePath) {
            contentType(ContentType.Application.Json)
            setBody(mapper.writeValueAsString(createRequest()))
        }.bodyAsText())

        assertThat(client.get("$basePath/v2/${v1.info.id}").status).isEqualTo(HttpStatusCode.Conflict)
        assertThat(client.post("$basePath/v2/${v1.info.id}/tilbakestill").status).isEqualTo(HttpStatusCode.Conflict)
        assertThat(hentBrev(v1.info.id).resultOrFail().redigertBrevHash).isEqualTo(v1.redigertBrevHash)
    }

    @Test
    fun `v2-vedlegg kan lagres gjenapnes flettes rendres og tilbakestilles med toggle av`() = routeTest { client ->
        Features.override(Features.markupV2, true)
        val brev = client.createV2()
        Features.override(Features.markupV2, false)
        val vedleggId = VedleggId("vedlegg")
        val original = attachment(MarkupV2.paragraph(10, MarkupV2.literal(11, "original"), MarkupV2.variable(12, "before")))
        brevbakerService.redigerbareVedlegg = RedigerbareVedlegg.V2(mapOf(vedleggId to original))
        val path = "$basePath/v2/${brev.info.id}/redigerbareVedlegg/${vedleggId.id}"
        val get = client.get(path)
        assertThat(get.status).isEqualTo(HttpStatusCode.OK)
        val vedlegg = mapper.readValue<EditV2.Attachment>(get.bodyAsText())
        val paragraph = vedlegg.blocks.single() as EditV2.Block.Paragraph
        val edited = vedlegg.copy(blocks = listOf(paragraph.copy(content = listOf(
            (paragraph.content.first() as EditV2.Text.Literal).copy(editedText = "edited"),
            paragraph.content.last(),
        ))))
        assertThat(client.put(path) {
            contentType(ContentType.Application.Json)
            setBody(mapper.writeValueAsString(ApiV2.RedigertVedleggRequest(edited)))
        }.status).isEqualTo(HttpStatusCode.OK)
        assertThat(mapper.readValue<EditV2.Attachment>(client.get(path).bodyAsText())).isEqualTo(edited)
        assertThat(client.get("$basePath/${brev.info.id}/redigerbareVedlegg/${vedleggId.id}").status).isEqualTo(HttpStatusCode.Conflict)

        brevbakerService.redigerbareVedlegg = RedigerbareVedlegg.V2(mapOf(
            vedleggId to attachment(MarkupV2.paragraph(10, MarkupV2.literal(11, "original"), MarkupV2.variable(12, "after"))),
        ))
        val merged = mapper.readValue<EditV2.Attachment>(client.get(path).bodyAsText()).blocks.single() as EditV2.Block.Paragraph
        assertThat((merged.content.first() as EditV2.Text.Literal).editedText).isEqualTo("edited")
        assertThat((merged.content.last() as EditV2.Text.Variable).text).isEqualTo("after")
        assertThat(client.get("$basePath/v2/${brev.info.id}?reserver=true").status).isEqualTo(HttpStatusCode.OK)
        assertThat(client.get("$basePath/${brev.info.id}/pdf").status).isEqualTo(HttpStatusCode.OK)
        assertThat(brevbakerService.renderPdfV2VedleggKall.last().getValue(vedleggId).blocks)
            .isEqualTo(attachment(MarkupV2.paragraph(10, MarkupV2.literal(11, "edited"), MarkupV2.variable(12, "after"))).blocks)

        val selected = listOf(AlltidValgbartVedleggBrevkode("kode", "Valgt vedlegg", emptySet()))
        assertThat(client.put("$basePath/v2/${brev.info.id}/valgteVedlegg") {
            contentType(ContentType.Application.Json)
            setBody(mapper.writeValueAsString(Api.ValgteVedleggRequest(selected)))
        }.status).isEqualTo(HttpStatusCode.OK)
        assertThat(mapper.readValue<ApiV2.BrevResponse>(client.get("$basePath/v2/${brev.info.id}").bodyAsText()).valgteVedlegg).isEqualTo(selected)

        val reset = client.delete(path)
        assertThat(reset.status).isEqualTo(HttpStatusCode.OK)
        val resetParagraph = mapper.readValue<EditV2.Attachment>(reset.bodyAsText()).blocks.single() as EditV2.Block.Paragraph
        assertThat((resetParagraph.content.first() as EditV2.Text.Literal).editedText).isNull()
        assertThat(client.get("$basePath/${brev.info.id}/pdf").status).isEqualTo(HttpStatusCode.OK)
        assertThat(brevbakerService.renderPdfV2VedleggKall.last()).isEmpty()
    }

    @Test
    fun `v2-diff viser redigert tekst i baade split og unified format`() = routeTest { client ->
        Features.override(Features.markupV2, true)
        val original = client.createV2()
        Features.override(Features.markupV2, false)
        val paragraph = original.redigertBrev.blocks.single() as EditV2.Block.Paragraph
        val edited = original.redigertBrev.copy(
            blocks = listOf(paragraph.copy(content = listOf((paragraph.content.single() as EditV2.Text.Literal).copy(editedText = "edited")))),
        )
        val path = "$basePath/v2/${original.info.id}/diff"
        val split = client.post("$path?split=true") {
            contentType(ContentType.Application.Json)
            setBody(mapper.writeValueAsString(edited))
        }
        assertThat(split.status).isEqualTo(HttpStatusCode.OK)
        val splitDiff = mapper.readValue<DiffBrevHandler.ResponseV2.Split>(split.bodyAsText())
        assertThat(splitDiff.inserts).containsExactly(DiffSegmentV2(ContentIndexV2.BlockContentIndex(0, 0), 0, 6))
        assertThat(splitDiff.deletes).containsExactly(DiffSegmentV2(ContentIndexV2.BlockContentIndex(0, 0), 0, 8))
        assertThat(splitDiff.rendretBrev).isEqualTo(original.redigertBrev)

        val unified = client.post(path) {
            contentType(ContentType.Application.Json)
            setBody(mapper.writeValueAsString(edited))
        }
        assertThat(unified.status).isEqualTo(HttpStatusCode.OK)
        val unifiedDiff = mapper.readValue<DiffBrevHandler.ResponseV2.Unified>(unified.bodyAsText())
        val blockEdit = unifiedDiff.editedBlocks.getValue(0) as UnifiedDiffV2.BlockEdit.TextBlockEdit
        assertThat(blockEdit.textEdits.getValue(0).inserts).containsExactly(UnifiedDiffV2.TextSegment(0, 6))
        assertThat(blockEdit.textEdits.getValue(0).deletes).containsExactly(UnifiedDiffV2.DeletedTextSegment(0, 8, "original"))
        assertThat(mapper.readValue<ApiV2.BrevResponse>(client.get("$basePath/v2/${original.info.id}").bodyAsText()).redigertBrev)
            .isEqualTo(original.redigertBrev)
    }

    @Test
    fun `v2-vedtaksbrev og vedlegg kan attesteres av annen bruker med toggle av`() = routeTest { client ->
        Features.override(Features.markupV2, true)
        val original = client.createV2(createRequest().copy(brevkode = Testbrevkoder.VEDTAKSBREV, vedtaksId = VedtaksId(1234)))
        Features.override(Features.markupV2, false)
        val path = "$basePath/v2/${original.info.id}/attestering"
        assertThat(client.get("$path?reserver=true").status).isEqualTo(HttpStatusCode.Forbidden)
        assertThat(client.put("$basePath/${original.info.id}/status") {
            contentType(ContentType.Application.Json)
            setBody("""{"klar":true}""")
        }.status).isEqualTo(HttpStatusCode.OK)
        val attestant = createClient {
            install(Auth) {
                basic {
                    credentials { BasicAuthCredentials("attestant", "test") }
                    sendWithoutRequest { true }
                }
            }
        }
        val hent = attestant.get("$path?reserver=true")
        assertThat(hent.status).isEqualTo(HttpStatusCode.OK)
        val forAttestering = mapper.readValue<ApiV2.BrevResponse>(hent.bodyAsText())
        assertThat(forAttestering.redigertBrev.signatur.attesterendeSaksbehandlerNavn).isEqualTo(attestant1Principal.fullName)
        val paragraph = forAttestering.redigertBrev.blocks.single() as EditV2.Block.Paragraph
        val edited = forAttestering.redigertBrev.copy(
            blocks = listOf(paragraph.copy(content = listOf((paragraph.content.single() as EditV2.Text.Literal).copy(editedText = "attested")))),
        )
        assertThat(attestant.put("$path/redigertBrev") {
            contentType(ContentType.Application.Json)
            setBody(mapper.writeValueAsString(edited))
        }.status).isEqualTo(HttpStatusCode.OK)
        assertThat(mapper.readValue<ApiV2.BrevResponse>(attestant.get(path).bodyAsText()).redigertBrev).isEqualTo(edited)

        val vedleggId = VedleggId("attestering")
        brevbakerService.redigerbareVedlegg = RedigerbareVedlegg.V2(mapOf(vedleggId to attachment(MarkupV2.paragraph(10, MarkupV2.literal(11, "original")))))
        val vedleggPath = "$path/redigerbareVedlegg/${vedleggId.id}"
        val vedleggResponse = attestant.get(vedleggPath)
        assertThat(vedleggResponse.status).isEqualTo(HttpStatusCode.OK)
        val vedlegg = mapper.readValue<EditV2.Attachment>(vedleggResponse.bodyAsText())
        val vedleggParagraph = vedlegg.blocks.single() as EditV2.Block.Paragraph
        val editedVedlegg = vedlegg.copy(blocks = listOf(vedleggParagraph.copy(content = listOf(
            (vedleggParagraph.content.single() as EditV2.Text.Literal).copy(editedText = "attested attachment"),
        ))))
        assertThat(attestant.put(vedleggPath) {
            contentType(ContentType.Application.Json)
            setBody(mapper.writeValueAsString(ApiV2.RedigertVedleggRequest(editedVedlegg)))
        }.status).isEqualTo(HttpStatusCode.OK)
        assertThat(mapper.readValue<EditV2.Attachment>(attestant.get(vedleggPath).bodyAsText())).isEqualTo(editedVedlegg)
        assertThat(attestant.get("$basePath/${original.info.id}/attestering/pdf").status).isEqualTo(HttpStatusCode.OK)
        assertThat(brevbakerService.renderPdfV2VedleggKall.last().getValue(vedleggId).blocks)
            .isEqualTo(attachment(MarkupV2.paragraph(10, MarkupV2.literal(11, "attested attachment"))).blocks)

        val attestert = attestant.put("$path?frigiReservasjon=true") {
            contentType(ContentType.Application.Json)
            setBody(mapper.writeValueAsString(ApiV2.OppdaterAttesteringRequest(edited)))
        }
        assertThat(attestert.status).isEqualTo(HttpStatusCode.OK)
        val status = mapper.readValue<ApiV2.BrevResponse>(attestert.bodyAsText()).info.status as Api.BrevStatus.Klar
        assertThat(status.attestertAv?.id).isEqualTo(attestant1Principal.navIdent)
    }

    @Test
    fun `v2-oppretting krever innlogging`() = routeTest {
        Features.override(Features.markupV2, true)
        assertThat(this.client.post("$basePath/v2").status).isEqualTo(HttpStatusCode.Unauthorized)
    }
}
