package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import io.ktor.client.engine.mock.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import no.nav.brev.brevbaker.PDFCompilationOutput
import no.nav.pensjon.brev.skribenten.brevbaker.RenderService
import no.nav.pensjon.brev.skribenten.SharedPostgres
import no.nav.pensjon.brev.skribenten.Testbrevkoder
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.brevredigering.domain.DocumentEntity
import no.nav.brev.BrevLandmodell.Landkode
import no.nav.pensjon.brev.skribenten.brevredigering.domain.P1RedigerbarDto
import no.nav.pensjon.brev.skribenten.brevredigering.domain.P1RedigerbarDto.Postnummer
import no.nav.pensjon.brev.skribenten.brevredigering.domain.P1RedigerbarDto.Poststed
import no.nav.pensjon.brev.skribenten.brevredigering.domain.P1RedigerbarDto.UtfyllendeInstitusjon
import no.nav.pensjon.brev.skribenten.model.Sakstype
import no.nav.pensjon.brev.skribenten.copy
import no.nav.pensjon.brev.skribenten.db.DocumentTable
import no.nav.pensjon.brev.skribenten.isSuccess
import no.nav.pensjon.brev.skribenten.letter.letter
import no.nav.pensjon.brev.skribenten.letter.toEdit
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.OboClientConfig
import no.nav.pensjon.brev.skribenten.auth.FakeAuthService
import no.nav.pensjon.brev.skribenten.foerstesidegenerator.FoerstesidegeneratorClient
import no.nav.pensjon.brev.skribenten.model.SaksId
import no.nav.pensjon.brev.skribenten.services.PenClientStub
import no.nav.pensjon.brev.skribenten.vedlegg.PDFVedleggAppender
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.BlockImpl.ParagraphImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl
import no.nav.pensjon.brevbaker.api.model.PDFVedlegg
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.junit.jupiter.api.Test
import java.time.LocalDate

class HentEllerOpprettPdfHandlerTest : BrevredigeringHandlerTestBase() {

    @Test
    suspend fun `hentPdf skal opprette et Document med referanse til Brevredigering`() {
        val brev = opprettBrev().resultOrFail()

        assertThat(hentEllerOpprettPdf(brev)).isSuccess {
            // Må testes individuelt fordi brevdata blir endret på av BrevdataService etter PenService svarer
            assertThat(it.document.dokumentDato).isEqualTo(LocalDate.now())
            assertThat(it.document.pdf).isEqualTo(stagetPDF)
            assertThat(it.document.redigertBrevHash).isEqualTo(brev.redigertBrevHash)
            assertThat(it.rendretBrevErEndret).isFalse()
        }
    }

    @Test
    suspend fun `sletter relaterte documents ved sletting av brevredigering`() {
        val brev = opprettBrev().resultOrFail()

        assertThat(hentEllerOpprettPdf(brev)).isSuccess()
        assertThat(slettBrev(brev)).isSuccess()

        assertThat(hentBrev(brev.info.id)).isNull()
        transaction {
            assertThat(DocumentEntity.find { DocumentTable.brevredigering eq brev.info.id }).hasSize(0)
        }
    }

    @Test
    suspend fun `dobbel oppretting av pdf er ikke mulig`() {
        val brev = opprettBrev().resultOrFail()

        val jobs = coroutineScope {
            (0..<10).map {
                async(Dispatchers.IO) {
                    hentEllerOpprettPdf(brev)
                }
            }
        }
        awaitAll(*jobs.toTypedArray())
        transaction {
            assertThat(DocumentEntity.find { DocumentTable.brevredigering eq brev.info.id }).hasSize(1)
        }
    }

    @Test
    suspend fun `Document redigertBrevHash er stabil`() {
        val brev = opprettBrev().resultOrFail()

        val firstHash = hentEllerOpprettPdf(brev).resultOrFail().document.redigertBrevHash
        transaction { BrevredigeringEntity[brev.info.id].document = null }

        val secondHash = hentEllerOpprettPdf(brev).resultOrFail().document.redigertBrevHash
        assertThat(firstHash).isEqualTo(secondHash)
    }

    @Test
    suspend fun `Document redigertBrevHash endres basert paa redigertBrev`() {
        val brev = opprettBrev().resultOrFail()
        val firstHash = hentEllerOpprettPdf(brev).resultOrFail().document.redigertBrevHash

        assertThat(
            oppdaterBrev(
                brevId = brev.info.id,
                nyttRedigertbrev = letter(ParagraphImpl(1, true, listOf(LiteralImpl(1, "blue pill")))).toEdit()
            )
        ).isSuccess()
        val secondHash = hentEllerOpprettPdf(brev).resultOrFail().document.redigertBrevHash

        assertThat(firstHash).isNotEqualTo(secondHash)
    }

    @Test
    suspend fun `hentPdf rendrer ny pdf om den ikke er basert paa gjeldende redigertBrev`() {
        val brev = opprettBrev().resultOrFail()
        stagePdf("min første pdf".encodeToByteArray())

        assertThat(hentEllerOpprettPdf(brev)).isSuccess {
            assertThat(it.document.pdf).isEqualTo("min første pdf".encodeToByteArray())
        }
        assertThat(
            oppdaterBrev(
                brevId = brev.info.id,
                nyttRedigertbrev = letter(ParagraphImpl(1, true, listOf(LiteralImpl(1, "blue pill")))).toEdit()
            )
        ).isSuccess()

        stagePdf("min andre pdf".encodeToByteArray())

        assertThat(hentEllerOpprettPdf(brev)).isSuccess {
            assertThat(it.document.pdf).isEqualTo("min andre pdf".encodeToByteArray())
        }
    }

    @Test
    suspend fun `hentPdf rendrer ny pdf om pesysdata er endra`() {
        val brev = opprettBrev().resultOrFail()

        stagePdf("min første pdf".encodeToByteArray())
        val first = hentEllerOpprettPdf(brev).resultOrFail()

        stagePdf("min andre pdf".encodeToByteArray())
        penService.pesysBrevdata = brevdataResponseData.copy(brevdata = Api.GeneriskBrevdata().also { it["a"] = "b" })
        val second = hentEllerOpprettPdf(brev)

        assertThat(second).isSuccess {
            assertThat(it.document.pdf).isEqualTo("min andre pdf".encodeToByteArray())
            assertThat(it.document.pdf).isNotEqualTo(first.document.pdf)
        }
    }

    @Test
    suspend fun `hentPdf informerer om at rendretBrev er endret pga pesysdata`() {
        val brev = opprettBrev().resultOrFail()

        stagePdf("min første pdf".encodeToByteArray())
        val first = hentEllerOpprettPdf(brev).resultOrFail()

        stagePdf("min andre pdf".encodeToByteArray())
        brevbakerService.renderMarkupResultat = {
            letter(ParagraphImpl(1, true, listOf(LiteralImpl(1, "red pill"), LiteralImpl(99, "new text"))))
                .medSignatur(
                    saksbehandler = it.signerendeSaksbehandlere?.saksbehandler,
                    attestant = it.signerendeSaksbehandlere?.attesterendeSaksbehandler
                )
        }
        penService.pesysBrevdata = brevdataResponseData.copy(brevdata = Api.GeneriskBrevdata().also { it["a"] = "b" })
        val second = hentEllerOpprettPdf(brev)

        assertThat(second).isSuccess {
            assertThat(it).isNotEqualTo(first)
            assertThat(it.rendretBrevErEndret).isTrue()
            assertThat(it.document.pdf).isEqualTo("min andre pdf".encodeToByteArray())
        }
    }

    @Test
    suspend fun `hentPdf rendrer ny pdf om dokumentdato er endra`() {
        val brev = opprettBrev().resultOrFail()

        stagePdf("min første pdf".encodeToByteArray())
        assertThat(hentEllerOpprettPdf(brev)).isSuccess {
            assertThat(it.document.pdf).isEqualTo("min første pdf".encodeToByteArray())
        }

        stagePdf("min andre pdf".encodeToByteArray())
        penService.pesysBrevdata = brevdataResponseData.copy(
            felles = brevdataResponseData.felles.copy(
                dokumentDato = brevdataResponseData.felles.dokumentDato.plusDays(2),
                saksnummer = sak1.saksId.toString(),
            )
        )

        assertThat(hentEllerOpprettPdf(brev)).isSuccess {
            assertThat(it.document.pdf).isEqualTo("min andre pdf".encodeToByteArray())
            assertThat(it.rendretBrevErEndret).isFalse()
        }
        assertThat(brevbakerService.renderPdfKall.last().sakspart.dokumentDato).isEqualTo(penService.pesysBrevdata!!.felles.dokumentDato)
    }

    @Test
    suspend fun `hentPdf rendrer ikke ny pdf om den er basert paa gjeldende redigertBrev`() {
        val brev = opprettBrev().resultOrFail()

        stagePdf("min første pdf".encodeToByteArray())
        assertThat(hentEllerOpprettPdf(brev)).isSuccess {
            assertThat(it.document.pdf).isEqualTo("min første pdf".encodeToByteArray())
        }

        stagePdf("min andre pdf".encodeToByteArray())
        assertThat(hentEllerOpprettPdf(brev)).isSuccess {
            assertThat(it.document.pdf).isEqualTo("min første pdf".encodeToByteArray())
        }
    }

    @Test
    suspend fun `kan hente pdf for p1`() {
        val hentP1DataHandler = HentP1DataHandler(
            penClient = object : PenClientStub() {
                override suspend fun hentP1VedleggData(saksId: SaksId, spraak: LanguageCode) = P1RedigerbarDto(
                    innehaver = P1RedigerbarDto.P1Person(
                        fornavn = "Peder",
                        etternavn = "Ås",
                        etternavnVedFoedsel = null,
                        foedselsdato = null,
                        adresselinje = "Testgata 1",
                        poststed = Poststed("Testvik"),
                        postnummer = Postnummer("0001"),
                        landkode = Landkode("NO"),
                    ),
                    forsikrede = P1RedigerbarDto.P1Person(
                        fornavn = "Lars",
                        etternavn = "Holm",
                        etternavnVedFoedsel = null,
                        foedselsdato = LocalDate.of(1990, 3, 1),
                        adresselinje = "Storgata 1",
                        poststed = Poststed("Testvik"),
                        postnummer = Postnummer("0002"),
                        landkode = Landkode("NO"),
                    ),
                    sakstype = Sakstype("ALDER"),
                    innvilgedePensjoner = emptyList(),
                    avslaattePensjoner = emptyList(),
                    utfyllendeInstitusjon = UtfyllendeInstitusjon(
                        navn = "NAV",
                        adresselinje = "Arbeids- og velferdsetaten",
                        poststed = Poststed("Oslo"),
                        postnummer = Postnummer("0001"),
                        landkode = Landkode("NO"),
                        institusjonsID = null,
                        faksnummer = null,
                        telefonnummer = null,
                        epost = null,
                    ),
                )
            },
            database = SharedPostgres.database,
        )
        brevbakerService.redigerbareMaler[Testbrevkoder.P1] = informasjonsbrev

        val brev = opprettBrev(brevkode = Testbrevkoder.P1).resultOrFail()

        val handler = HentEllerOpprettPdfHandler(
            brevdataService = brevdataService,
            renderService = RenderService(brevbakerService),
            brevmalService = brevmalService,
            hentP1DataHandler = hentP1DataHandler,
            genererFoerstesideHandler = GenererFoerstesideHandler(
                FoerstesidegeneratorClient(
                    config = OboClientConfig(url = "http://localhost", scope = "test"),
                    authService = FakeAuthService,
                    clientEngine = MockEngine { respond("", HttpStatusCode.OK) },
                )
            ),
            database = SharedPostgres.database,
            pdfVedleggAppender = object : PDFVedleggAppender {
                override fun leggPaaVedlegg(pdfCompilationOutput: ByteArray, attachments: List<PDFVedlegg>, spraak: LanguageCode) = pdfCompilationOutput
            }
        )
        assertThat(hentEllerOpprettPdf(brev, handler = handler)).isSuccess {
            assertThat(it.document.pdf).isEqualTo(stagetPDF)
        }
    }
}