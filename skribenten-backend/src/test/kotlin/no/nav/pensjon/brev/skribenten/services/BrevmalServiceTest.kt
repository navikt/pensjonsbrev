package no.nav.pensjon.brev.skribenten.services

import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.skribenten.Features
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.services.BrevdataDto.BrevkontekstCode.*
import no.nav.pensjon.brev.skribenten.services.BrevdataDto.DokumentType.N
import no.nav.pensjon.brev.skribenten.services.Brevkoder.FRITEKSTBREV_KODE
import no.nav.pensjon.brev.skribenten.services.Brevkoder.POSTERINGSGRUNNLAG_KODE
import no.nav.pensjon.brev.skribenten.services.Brevkoder.POSTERINGSGRUNNLAG_VIRK0101_KODE
import no.nav.pensjon.brev.skribenten.services.Brevkoder.POSTERINGSGRUNNLAG_VIRK0102_KODE
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.ListAssert
import org.junit.jupiter.api.Test

const val TEST_VEDTAKS_ID = "1234"

class BrevmalServiceTest {
    private val brevbakerbrev = listOf(
        TemplateDescription.Redigerbar(
            name = "brevbaker mal",
            letterDataClass = EmptyRedigerbarBrevdata::class.java.name,
            languages = listOf(LanguageCode.BOKMAL),
            metadata = no.nav.pensjon.brevbaker.api.model.LetterMetadata(
                "brevbaker brev",
                false,
                no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VIKTIG,
                no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.INFORMASJONSBREV
            ),
            kategori = TemplateDescription.Brevkategori.INFORMASJONSBREV,
            brevkontekst = TemplateDescription.Brevkontekst.ALLE,
            sakstyper = Sakstype.all,
        )
    )

    private val penService: PenService = mockk()
    private val brevmetadataService: BrevmetadataService = mockk()
    private val brevbakerService: BrevbakerService = mockk {
        coEvery { getTemplates() } returns ServiceResult.Ok(brevbakerbrev)
    }
    private val brevmalService = BrevmalService(penService, brevmetadataService, brevbakerService)
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

    @Test
    fun `eblanketter vises om forespurt`() {
        coEvery {
            brevmetadataService.getEblanketter()
        } returns listOf(testOkBrev.copy(brevkodeIBrevsystem = "e-blankett-kode"))

        runBlocking {
            val brevmaler = brevmalService.hentBrevmalerForSak(sakType = Sakstype.UFOREP, includeEblanketter = true)
            assertThat(brevmaler).anyMatch { it.id == "e-blankett-kode" }
        }
    }

    @Test
    fun `filtrerer ut ikke redigerbare brev`() {
        assertThatBrevmalerInSakskontekst(testOkSakBrev.copy(redigerbart = false, brevkodeIBrevsystem = "autobrev")).noneMatch { it.id == "autobrev" }
    }

    @Test
    fun `viser vedtaksbrev i vedtaks kontekst`() {
        assertThatBrevmalerInVedtaksKontekst(testOkVedtakBrev, sakstype = Sakstype.UFOREP).anyMatch { it.id == testOkVedtakBrev.brevkodeIBrevsystem }
    }

    @Test
    fun `viser ikke brev for sakskontekst i vedtakskontekst`() {
        assertThatBrevmalerInVedtaksKontekst(testOkSakBrev, sakstype = Sakstype.UFOREP).noneMatch { it.id == testOkSakBrev.brevkodeIBrevsystem }
    }

    @Test
    fun `viser brev for med ALLTID i vedtakskontekst`() {
        assertThatBrevmalerInVedtaksKontekst(testOkSakBrev.copy(brevkontekst = ALLTID), sakstype = Sakstype.UFOREP).anyMatch { it.id == testOkSakBrev.brevkodeIBrevsystem }
    }

    @Test
    fun `viser ikke vedtaksbrev mal paa nytt alderspensjon regelverk naar vedtaket er pa gammel alderspensjon beregning`() {
        assertThatBrevmalerInVedtaksKontekst(
            testOkVedtakBrev.copy(brevregeltype = BrevdataDto.BrevregeltypeCode.NN, brevkodeIBrevsystem = "nytt regelverk"),
            sakstype = Sakstype.ALDER,
            isKravPaaGammeltRegelverk = true
        ).noneMatch { it.id == "nytt regelverk" }
    }

    @Test
    fun `viser vedtaksbrev mal paa nytt alderspensjon regelverk naar vedtaket er pa ny alderspensjon beregning`() {
        assertThatBrevmalerInVedtaksKontekst(
            testOkVedtakBrev.copy(brevregeltype = BrevdataDto.BrevregeltypeCode.GN, brevkodeIBrevsystem = "nytt regelverk"),
            sakstype = Sakstype.ALDER,
            isKravPaaGammeltRegelverk = false,
        ).anyMatch { it.id == "nytt regelverk" }
    }

    @Test
    fun `viser ikke vedtaksbrev mal paa gammelt alderspensjon regelverk naar vedtaket er pa ny alderspensjon beregning`() {
        assertThatBrevmalerInVedtaksKontekst(
            testOkVedtakBrev.copy(brevregeltype = BrevdataDto.BrevregeltypeCode.GG, brevkodeIBrevsystem = "gammelt regelverk"),
            sakstype = Sakstype.ALDER,
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
    fun `inkluderer brevbakerbrev om feature er aktivert`() = runBlocking {
        Features.override(Features.brevbakerbrev, true)
        val brevmalerAssert = assertThatBrevmalerInVedtaksKontekst(testOkVedtakBrev, false, Sakstype.ALDER)
        for (brev in brevbakerbrev) {
            brevmalerAssert.anyMatch { it.id == brev.name }
        }
    }

    @Test
    fun `inkluderer ikke brevbakerbrev om feature er deaktivert`() = runBlocking {
        Features.override(Features.brevbakerbrev, false)
        val brevmalerAssert = assertThatBrevmalerInVedtaksKontekst(testOkVedtakBrev, false, Sakstype.ALDER)
        for (brev in brevbakerbrev) {
            brevmalerAssert.noneMatch { it.id == brev.name }
        }
    }

    private fun assertThatBrevmalerInVedtaksKontekst(
        brevdataDto: BrevdataDto,
        inkluderEblanketter: Boolean = false,
        sakstype: Sakstype,
        isKravPaaGammeltRegelverk: Boolean = true
    ): ListAssert<Api.Brevmal> {
        coEvery { brevmetadataService.getBrevmalerForSakstype(any()) } returns listOf(brevdataDto)
        coEvery { penService.hentIsKravPaaGammeltRegelverk(TEST_VEDTAKS_ID) }.returns(ServiceResult.Ok(isKravPaaGammeltRegelverk))

        return runBlocking {
            val brevmaler = brevmalService.hentBrevmalerForVedtak(sakstype, inkluderEblanketter, TEST_VEDTAKS_ID)
            return@runBlocking assertThat(brevmaler)
        }
    }

    private fun assertThatBrevmalerInSakskontekst(brevdataDto: BrevdataDto): ListAssert<Api.Brevmal> =
        assertThatBrevmalerInSakskontekst(listOf(brevdataDto))

    private fun assertThatBrevmalerInSakskontekst(brevdataDto: List<BrevdataDto>): ListAssert<Api.Brevmal> {
        coEvery { brevmetadataService.getBrevmalerForSakstype(any()) } returns brevdataDto

        return runBlocking {
            val brevmaler = brevmalService.hentBrevmalerForSak(Sakstype.UFOREP, false)
            return@runBlocking assertThat(brevmaler)
        }
    }
}