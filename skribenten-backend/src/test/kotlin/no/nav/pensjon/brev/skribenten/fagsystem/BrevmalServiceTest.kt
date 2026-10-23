package no.nav.pensjon.brev.skribenten.fagsystem

import kotlinx.coroutines.runBlocking
import no.nav.brev.InternKonstruktoer
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.skribenten.Features
import no.nav.pensjon.brev.skribenten.brevbaker.BrevbakerService
import no.nav.pensjon.brev.skribenten.override
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.*
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.BrevdataDto.BrevkontekstCode.*
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.BrevdataDto.DokumentType.N
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.Brevkoder.FRITEKSTBREV_KODE
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.Brevkoder.POSTERINGSGRUNNLAG_KODE
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.Brevkoder.POSTERINGSGRUNNLAG_VIRK0101_KODE
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.Brevkoder.POSTERINGSGRUNNLAG_VIRK0102_KODE
import no.nav.pensjon.brev.skribenten.fagsystem.pesys.PenClient.KravStoettetAvDatabyggerResult
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.LetterMetadata as SkribentenLetterMetadata
import no.nav.pensjon.brev.skribenten.model.VedtaksId
import no.nav.pensjon.brev.skribenten.model.Sakstype
import no.nav.pensjon.brev.skribenten.services.FakeBrevbakerService
import no.nav.pensjon.brev.skribenten.services.FakeBrevmetadataService
import no.nav.pensjon.brev.skribenten.services.PenClientStub
import no.nav.pensjon.brev.skribenten.services.notYetStubbed
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.ListAssert
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.parallel.Execution
import org.junit.jupiter.api.parallel.ExecutionMode

val TEST_VEDTAKS_ID = VedtaksId(1234)

@OptIn(InternKonstruktoer::class)
@Execution(ExecutionMode.SAME_THREAD)
class BrevmalServiceTest {
    private val brevbakerbrev = listOf(
        TemplateDescription.Redigerbar(
            name = "brevbaker mal",
            letterDataClass = RedigerbarBrevdata::class.java.name,
            languages = listOf(LanguageCode.BOKMAL),
            metadata = LetterMetadata(
                "brevbaker brev",
                LetterMetadata.Distribusjonstype.VIKTIG,
                LetterMetadata.Brevtype.INFORMASJONSBREV
            ),
            kategori = TemplateDescription.Redigerbar.Brevkategori("INFORMASJONSBREV"),
            brevkontekst = TemplateDescription.Brevkontekst.ALLE,
            sakstyper = setOf(TemplateDescription.Redigerbar.Sakstype("S1"), TemplateDescription.Redigerbar.Sakstype("S2")),
        )
    )

    private val brevbakerService: BrevbakerService = FakeBrevbakerService(maler = brevbakerbrev)

    private fun lagBrevmalService(
        penClient: PenClient = PenClientStub(),
        brevmetadataService: BrevmetadataService = FakeBrevmetadataService()
    ): BrevmalService = BrevmalService(brevbakerService, penClient, brevmetadataService)

    private val testOkBrev = BrevdataDto(
        redigerbart = true,
        dekode = "dekode",
        brevkategori = BrevdataDto.BrevkategoriCode.INFORMASJON,
        dokType = BrevdataDto.DokumentType.U,
        sprak = listOf(SpraakKode.NB),
        visIPselv = false,
        utland = null,
        brevregeltype = BrevdataDto.BrevregeltypeCode.OVRIGE,
        brevkravtype = null,
        brevkontekst = ALLTID,
        dokumentkategori = BrevdataDto.DokumentkategoriCode.IB,
        synligForVeileder = null,
        prioritet = null,
        brevkodeIBrevsystem = "BREV_KODE",
        brevgruppe = null,
        brevsystem = BrevdataDto.BrevSystem.GAMMEL
    )

    private val testOkVedtakBrev = testOkBrev.copy(brevkontekst = VEDTAK, brevkodeIBrevsystem = "BREV_KODE_SAK")
    private val testOkSakBrev = testOkBrev.copy(brevkontekst = SAK, brevkodeIBrevsystem = "BREV_KODE_VEDTAK")


    @Test
    fun `brevmal for sakskontekst vises for sakskontekst`() {
        assertThatBrevmalerInSakskontekst(testOkSakBrev).anyMatch { it.id == testOkSakBrev.brevkodeIBrevsystem }
    }

    @Test
    fun `brevmal for vedtakskontekst vises ikke for sakskontekst`() {
        assertThatBrevmalerInSakskontekst(testOkVedtakBrev).noneMatch { it.id == testOkVedtakBrev.brevkodeIBrevsystem }
    }

    @Test
    fun `utfiltrert brevmal vises ikke`() {
        assertThatBrevmalerInSakskontekst(testOkSakBrev.copy(brevkodeIBrevsystem = "PE_IY_05_301")).noneMatch { it.id == "PE_IY_05_301" }
    }

    private val testEblankett = testOkBrev.copy(
        brevkodeIBrevsystem = "E001",
        brevkategori = null,
        brevkontekst = null,
        dokumentkategori = BrevdataDto.DokumentkategoriCode.E_BLANKETT,
    )

    @Test
    fun `eblanketter vises om forespurt`() {
        Features.override(Features.eblanketter, true)
        val brevmetadataService = FakeBrevmetadataService(eblanketter = listOf(testEblankett))

        runBlocking {
            val brevmaler = lagBrevmalService(brevmetadataService = brevmetadataService).hentBrevmalerForSak(sakType = Sakstype("UFOREP"), includeEblanketter = true)
            assertThat(brevmaler).anyMatch { it.id == testEblankett.brevkodeIBrevsystem && it.dokumentkategoriCode == BrevdataDto.DokumentkategoriCode.E_BLANKETT }
        }
    }

    @Test
    fun `eblanketter vises ikke om ikke forespurt`() {
        Features.override(Features.eblanketter, true)
        val brevmetadataService = FakeBrevmetadataService(eblanketter = listOf(testEblankett))

        runBlocking {
            val brevmaler = lagBrevmalService(brevmetadataService = brevmetadataService).hentBrevmalerForSak(sakType = Sakstype("UFOREP"), includeEblanketter = false)
            assertThat(brevmaler).noneMatch { it.id == testEblankett.brevkodeIBrevsystem }
        }
    }

    @Test
    fun `eblanketter vises ikke naar toggle er av`() {
        Features.override(Features.eblanketter, false)
        val brevmetadataService = FakeBrevmetadataService(
            brevmaler = listOf(testOkSakBrev),
            eblanketter = listOf(testEblankett),
        )

        runBlocking {
            val service = lagBrevmalService(brevmetadataService = brevmetadataService)
            assertThat(service.hentBrevmalerForSak(sakType = Sakstype("UFOREP"), includeEblanketter = true))
                .noneMatch { it.id == testEblankett.brevkodeIBrevsystem }
                .anyMatch { it.id == testOkSakBrev.brevkodeIBrevsystem }
            assertThat(service.hentBrevmaler(includeEblanketter = true))
                .noneMatch { it.id == testEblankett.brevkodeIBrevsystem }
        }
    }

    @Test
    fun `eblanketter vises i vedtakskontekst om forespurt`() {
        Features.override(Features.eblanketter, true)
        val brevmetadataService = FakeBrevmetadataService(eblanketter = listOf(testEblankett))
        val penService = object : PenClientStub() {
            override suspend fun hentIsKravStoettetAvDatabygger(vedtaksId: VedtaksId) = KravStoettetAvDatabyggerResult(emptyMap())
        }

        runBlocking {
            val brevmaler = lagBrevmalService(penService, brevmetadataService).hentBrevmalerForVedtak(Sakstype("UFOREP"), true, TEST_VEDTAKS_ID)
            assertThat(brevmaler).anyMatch { it.id == testEblankett.brevkodeIBrevsystem }
        }
    }

    @Test
    fun `filtrerer ut ikke redigerbare brev`() {
        assertThatBrevmalerInSakskontekst(testOkSakBrev.copy(redigerbart = false, brevkodeIBrevsystem = "autobrev")).noneMatch { it.id == "autobrev" }
    }

    @Test
    fun `viser vedtaksbrev i vedtaks kontekst`() {
        assertThatBrevmalerInVedtaksKontekst(testOkVedtakBrev, sakstype = Sakstype("UFOREP")).anyMatch { it.id == testOkVedtakBrev.brevkodeIBrevsystem }
    }

    @Test
    fun `viser ikke brev for sakskontekst i vedtakskontekst`() {
        assertThatBrevmalerInVedtaksKontekst(testOkSakBrev, sakstype = Sakstype("UFOREP")).noneMatch { it.id == testOkSakBrev.brevkodeIBrevsystem }
    }

    @Test
    fun `viser brev for med ALLTID i vedtakskontekst`() {
        assertThatBrevmalerInVedtaksKontekst(testOkSakBrev.copy(brevkontekst = ALLTID), sakstype = Sakstype("UFOREP")).anyMatch { it.id == testOkSakBrev.brevkodeIBrevsystem }
    }

    @Test
    fun `viser ikke vedtaksbrev mal paa nytt alderspensjon regelverk naar vedtaket er pa gammel alderspensjon beregning`() {
        assertThatBrevmalerInVedtaksKontekst(
            testOkVedtakBrev.copy(brevregeltype = BrevdataDto.BrevregeltypeCode.NN, brevkodeIBrevsystem = "nytt regelverk"),
            sakstype = Sakstype("ALDER"),
            isKravPaaGammeltRegelverk = true
        ).noneMatch { it.id == "nytt regelverk" }
    }

    @Test
    fun `viser vedtaksbrev mal paa nytt alderspensjon regelverk naar vedtaket er pa ny alderspensjon beregning`() {
        assertThatBrevmalerInVedtaksKontekst(
            testOkVedtakBrev.copy(brevregeltype = BrevdataDto.BrevregeltypeCode.GN, brevkodeIBrevsystem = "nytt regelverk"),
            sakstype = Sakstype("ALDER"),
            isKravPaaGammeltRegelverk = false,
        ).anyMatch { it.id == "nytt regelverk" }
    }

    @Test
    fun `viser ikke vedtaksbrev mal paa gammelt alderspensjon regelverk naar vedtaket er pa ny alderspensjon beregning`() {
        assertThatBrevmalerInVedtaksKontekst(
            testOkVedtakBrev.copy(brevregeltype = BrevdataDto.BrevregeltypeCode.GG, brevkodeIBrevsystem = "gammelt regelverk"),
            sakstype = Sakstype("ALDER"),
            isKravPaaGammeltRegelverk = false,
        ).noneMatch { it.id == "gammelt regelverk" }
    }

    @Test
    fun `brevmetadata setter redigerbarBrevTittel`() {
        val brevMetadata = listOf(
            testOkBrev.copy(brevkodeIBrevsystem = "notat", dokType = N),
            testOkBrev.copy(brevkodeIBrevsystem = FRITEKSTBREV_KODE),
        ) + Brevkoder.ikkeRedigerbarBrevtittel.map { testOkBrev.copy(brevkodeIBrevsystem = it, dokType = N) }
        assertThatBrevmalerInSakskontekst(brevMetadata)
            .anyMatch { it.id == FRITEKSTBREV_KODE && it.redigerbarBrevtittel }
            .anyMatch { it.id == "notat" && it.redigerbarBrevtittel }
            .anyMatch { it.id == POSTERINGSGRUNNLAG_KODE && !it.redigerbarBrevtittel }
            .anyMatch { it.id == POSTERINGSGRUNNLAG_VIRK0101_KODE && !it.redigerbarBrevtittel }
            .anyMatch { it.id == POSTERINGSGRUNNLAG_VIRK0102_KODE && !it.redigerbarBrevtittel }
    }

    @Test
    fun `exstream autobrev uten spraak antas aa vaere bokmaal`() {
        val autobrev = testOkSakBrev.copy(sprak = null, redigerbart = false)
        assertThat(SkribentenLetterMetadata.Legacy(autobrev, Sakstype("UFOREP")).toApi().spraak).containsExactly(SpraakKode.NB)
    }

    @Test
    fun `redigerbart exstream brev uten spraak faar ingen spraak`() {
        val redigerbart = testOkSakBrev.copy(sprak = null)
        assertThat(SkribentenLetterMetadata.Legacy(redigerbart, Sakstype("UFOREP")).toApi().spraak).isEmpty()
    }

    @Test
    fun `inkluderer brevbakerbrev`() = runBlocking {
        val brevmalerAssert = assertThatBrevmalerInVedtaksKontekst(testOkVedtakBrev, false, Sakstype("S2"))
        for (brev in brevbakerbrev) {
            brevmalerAssert.anyMatch { it.id == brev.name }
        }
    }

    private fun assertThatBrevmalerInVedtaksKontekst(
        brevdataDto: BrevdataDto,
        inkluderEblanketter: Boolean = false,
        sakstype: Sakstype,
        isKravPaaGammeltRegelverk: Boolean = true
    ): ListAssert<Api.Brevmal> {
        val brevmetadataService = FakeBrevmetadataService(
            brevmaler = listOf(brevdataDto),
        )

        val penService = object : PenClientStub() {
            override suspend fun hentIsKravPaaGammeltRegelverk(vedtaksId: VedtaksId) =
                if (vedtaksId == TEST_VEDTAKS_ID) isKravPaaGammeltRegelverk else notYetStubbed("Mangler stub for vedtaksId: $vedtaksId")

            override suspend fun hentIsKravStoettetAvDatabygger(vedtaksId: VedtaksId) =
                if (vedtaksId == TEST_VEDTAKS_ID) KravStoettetAvDatabyggerResult(emptyMap()) else notYetStubbed("Mangler stub for vedtaksId: $vedtaksId")
        }

        return runBlocking {
            val brevmaler = lagBrevmalService(penService, brevmetadataService).hentBrevmalerForVedtak(sakstype, inkluderEblanketter, TEST_VEDTAKS_ID)
            return@runBlocking assertThat(brevmaler)
        }
    }

    private fun assertThatBrevmalerInSakskontekst(brevdataDto: BrevdataDto): ListAssert<Api.Brevmal> =
        assertThatBrevmalerInSakskontekst(listOf(brevdataDto))

    private fun assertThatBrevmalerInSakskontekst(brevdataDto: List<BrevdataDto>) = runBlocking {
        val brevmaler = lagBrevmalService(
            brevmetadataService = FakeBrevmetadataService(
                brevmaler = brevdataDto,
            )
        ).hentBrevmalerForSak(Sakstype("UFOREP"), false)
        return@runBlocking assertThat(brevmaler)
    }
}