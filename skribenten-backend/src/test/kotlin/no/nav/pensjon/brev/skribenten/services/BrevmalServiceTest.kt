package no.nav.pensjon.brev.skribenten.services

import io.ktor.server.application.*
import io.ktor.server.plugins.callid.*
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import no.nav.pensjon.brev.skribenten.services.BrevdataDto.BrevkontekstCode.SAK
import no.nav.pensjon.brev.skribenten.services.BrevdataDto.BrevkontekstCode.VEDTAK
import no.nav.pensjon.brev.skribenten.services.BrevdataDto.DokumentType.N
import no.nav.pensjon.brev.skribenten.services.Brevkoder.FRITEKSTBREV_KODE
import no.nav.pensjon.brev.skribenten.services.Brevkoder.POSTERINGSGRUNNLAG_KODE
import no.nav.pensjon.brev.skribenten.services.Brevkoder.POSTERINGSGRUNNLAG_VIRK0101_KODE
import no.nav.pensjon.brev.skribenten.services.Brevkoder.POSTERINGSGRUNNLAG_VIRK0102_KODE
import no.nav.pensjon.brev.skribenten.services.PenService.SakType.ALDER
import no.nav.pensjon.brev.skribenten.services.PenService.SakType.UFOREP
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.ListAssert
import kotlin.test.Test

const val TEST_VEDTAKS_ID = "1234"

class BrevmalServiceTest {
    private val mockCall = mockk<ApplicationCall> {
        every { callId } returns "utrolig kul callId"
    }
    private val penService: PenService = mockk()
    private val brevmetadataService: BrevmetadataService = mockk()
    private val brevmalService = BrevmalService(penService, brevmetadataService)
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
        brevkontekst = BrevdataDto.BrevkontekstCode.ALLTID,
        dokumentkategori = BrevdataDto.DokumentkategoriCode.IB,
        synligForVeileder = null,
        prioritet = null,
        brevkodeIBrevsystem = "BREV_KODE",
        brevgruppe = null,
        brevsystem = BrevdataDto.BrevSystem.GAMMEL
    )

    private val testOkVedtakBrev = testOkBrev.copy(brevkontekst = VEDTAK)
    private val testOkSakBrev = testOkBrev.copy(brevkontekst = SAK)


    @Test
    fun `brevmal for sakskontekst vises for sakskontekst`() {
        assertThatBrevmalerInSakskontekst(testOkSakBrev).hasSize(1)
    }

    @Test
    fun `brevmal for vedtakskontekst vises ikke for sakskontekst`() {
        assertThatBrevmalerInSakskontekst(testOkVedtakBrev).isEmpty()
    }

    @Test
    fun `utfiltrert brevmal vises ikke`() {
        assertThatBrevmalerInSakskontekst(testOkSakBrev.copy(brevkodeIBrevsystem = "PE_IY_05_301")).isEmpty()
    }

    @Test
    fun `eblanketter vises om forespurt`() {
        coEvery {
            brevmetadataService.hentMaler(any(), UFOREP, true)
        }.returns(
            BrevmetadataService.Brevmaler(
                eblanketter = listOf(testOkBrev),
                maler = emptyList()
            )
        )
        runBlocking {
            val brevmaler = brevmalService.hentBrevmalerForSak(
                mockCall, UFOREP, includeEblanketter = true
            )
            assertThat(brevmaler).hasSize(1)
        }
    }

    @Test
    fun `filtrerer ut ikke redigerbare brev`() {
        assertThatBrevmalerInSakskontekst(testOkSakBrev.copy(redigerbart = false)).isEmpty()
    }

    @Test
    fun `viser vedtaksbrev i vedtaks kontekst`() {
        assertThatBrevmalerInVedtaksKontekst(testOkVedtakBrev, sakType = UFOREP).hasSize(1)
    }

    @Test
    fun `viser brev for sakskontekst i vedtakskontekst`() {
        assertThatBrevmalerInVedtaksKontekst(testOkSakBrev, sakType = UFOREP).isEmpty()
    }

    @Test
    fun `viser ikke vedtaksbrev mal paa nytt alderspensjon regelverk naar vedtaket er pa gammel alderspensjon beregning`() {
        assertThatBrevmalerInVedtaksKontekst(
            testOkVedtakBrev.copy(brevregeltype = BrevdataDto.BrevregeltypeCode.NN),
            sakType = ALDER,
            isKravPaaGammeltRegelverk = true
        ).isEmpty()
    }

    @Test
    fun `viser vedtaksbrev mal paa nytt alderspensjon regelverk naar vedtaket er pa ny alderspensjon beregning`() {
        assertThatBrevmalerInVedtaksKontekst(
            testOkVedtakBrev.copy(brevregeltype = BrevdataDto.BrevregeltypeCode.GN),
            sakType = ALDER,
            isKravPaaGammeltRegelverk = false,
        ).hasSize(1)
    }

    @Test
    fun `viser ikke vedtaksbrev mal paa gammelt alderspensjon regelverk naar vedtaket er pa ny alderspensjon beregning`() {
        assertThatBrevmalerInVedtaksKontekst(
            testOkVedtakBrev.copy(brevregeltype = BrevdataDto.BrevregeltypeCode.GG),
            sakType = ALDER,
            isKravPaaGammeltRegelverk = false,
        ).hasSize(0)
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


    private fun assertThatBrevmalerInVedtaksKontekst(
        brevdataDto: BrevdataDto,
        inkluderEblanketter: Boolean = false,
        sakType: PenService.SakType,
        isKravPaaGammeltRegelverk: Boolean = true
    ): ListAssert<LetterMetadata> {
        coEvery { brevmetadataService.hentMaler(any(), any(), any()) }.returns(
            BrevmetadataService.Brevmaler(emptyList(), listOf(brevdataDto))
        )
        coEvery { penService.hentIsKravPaaGammeltRegelverk(any(), TEST_VEDTAKS_ID) }.returns(ServiceResult.Ok(isKravPaaGammeltRegelverk))

        return runBlocking {
            val brevmaler = brevmalService.hentBrevmalerForVedtak(mockk<ApplicationCall>(), sakType, inkluderEblanketter, TEST_VEDTAKS_ID)
            return@runBlocking assertThat(brevmaler)
        }
    }

    private fun assertThatBrevmalerInSakskontekst(brevdataDto: BrevdataDto): ListAssert<LetterMetadata> =
        assertThatBrevmalerInSakskontekst(listOf(brevdataDto))

    private fun assertThatBrevmalerInSakskontekst(brevdataDto: List<BrevdataDto>): ListAssert<LetterMetadata> {
        coEvery { brevmetadataService.hentMaler(any(), any(), any()) }.returns(
            BrevmetadataService.Brevmaler(emptyList(), brevdataDto)
        )
        return runBlocking {
            val brevmaler = brevmalService.hentBrevmalerForSak(mockCall, UFOREP, false)
            return@runBlocking assertThat(brevmaler)
        }
    }
}