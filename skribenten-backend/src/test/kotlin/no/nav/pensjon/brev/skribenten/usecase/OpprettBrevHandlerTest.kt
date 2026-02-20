package no.nav.pensjon.brev.skribenten.usecase

import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevkode
import no.nav.pensjon.brev.skribenten.Testbrevkoder
import no.nav.pensjon.brev.skribenten.auth.withPrincipal
import no.nav.pensjon.brev.skribenten.db.Hash
import no.nav.pensjon.brev.skribenten.domain.OpprettBrevPolicy.KanIkkeOppretteBrev.*
import no.nav.pensjon.brev.skribenten.isFailure
import no.nav.pensjon.brev.skribenten.isSuccess
import no.nav.pensjon.brev.skribenten.letter.toEdit
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.Dto.Mottaker.ManueltAdressertTil.ANNEN
import no.nav.pensjon.brev.skribenten.model.NorskPostnummer
import no.nav.pensjon.brev.skribenten.model.VedtaksId
import no.nav.pensjon.brev.skribenten.services.EnhetId
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.Instant
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration

class OpprettBrevHandlerTest : BrevredigeringTest() {

    @Test
    suspend fun `kan opprette brev`() {
        val saksbehandlerValg = Api.GeneriskBrevdata().apply { put("valg1", true) }
        val brev = opprettBrev(reserverForRedigering = true, saksbehandlerValg = saksbehandlerValg)

        assertThat(brevbakerService.renderMarkupKall.first()).isEqualTo(Testbrevkoder.INFORMASJONSBREV to LanguageCode.ENGLISH)
        assertThat(brev).isSuccess {
            assertThat(it.info.brevkode.kode()).isEqualTo(Testbrevkoder.INFORMASJONSBREV.kode())
            assertThat(it.redigertBrev).isEqualTo(letter.toEdit())
        }
    }

    @Test
    suspend fun `initialiserer signatur for brev`() {
        val brev = opprettBrev(reserverForRedigering = true)

        assertThat(brev).isSuccess {
            assertThat(it.redigertBrev.signatur).isNotNull
            assertThat(it.redigertBrev.signatur.saksbehandlerNavn).isEqualTo(saksbehandler1Principal.fullName)
            assertThat(it.info.opprettetAv).isEqualTo(saksbehandler1Principal.navIdent)
        }
    }

    @Test
    suspend fun `kan opprette brev i vedtakskontekst`() {
        val vedtaksId = VedtaksId(5678)

        val brev = opprettBrev(brevkode = Testbrevkoder.VEDTAKSBREV, vedtaksId = vedtaksId)
        assertThat(brev).isSuccess {
            assertThat(it.info.vedtaksId).isEqualTo(vedtaksId)
            assertThat(it.info.brevkode.kode()).isEqualTo(Testbrevkoder.VEDTAKSBREV.kode())
        }
        penService.verifyHentPesysBrevdata(sak1.saksId, vedtaksId, Testbrevkoder.VEDTAKSBREV, PRINCIPAL_NAVENHET_ID)
    }

    @Test
    suspend fun `brev i vedtakskontekst maa ha vedtaksId`() {
        assertThat(opprettBrev(brevkode = Testbrevkoder.VEDTAKSBREV)).isFailure<BrevmalKreverVedtaksId, _, _>()
        assertThat(opprettBrev(brevkode = Testbrevkoder.VARSELBREV)).isFailure<BrevmalKreverVedtaksId, _, _>()
    }

    @Test
    suspend fun `kan ikke opprette brev for mal som ikke finnes`() {
        val ukjentBrevkode = RedigerbarBrevkode("UKJENT_BREVKODE")
        val brev = opprettBrev(brevkode = ukjentBrevkode)
        assertThat(brev).isFailure<BrevmalFinnesIkke, _, _>()
    }

    @Test
    suspend fun `status er KLADD for et nytt brev`() {
        val brev = opprettBrev()

        assertThat(brev).isSuccess {
            assertThat(it.info.status).isEqualTo(Dto.BrevStatus.KLADD)
        }
    }

    @Test
    suspend fun `kan ikke opprette et brev med avsenderEnhet uten tilgang`() {
        val brev = withPrincipal(saksbehandler1Principal) {
            opprettBrev(
                sak = sak1,
                brevkode = Testbrevkoder.INFORMASJONSBREV,
                avsenderEnhetsId = EnhetId("9998"),
            )
        }
        assertThat(brev).isFailure<IkkeTilgangTilEnhet, _, _>()
    }

    @Test
    suspend fun `brev kan reserveres for redigering gjennom opprett brev`() {
        val brev = opprettBrev(reserverForRedigering = true)

        assertThat(brev).isSuccess {
            assertThat(it.info.redigeresAv).isEqualTo(saksbehandler1Principal.navIdent)
            assertThat(it.info.sistReservert).isBetween(Instant.now() - 10.minutes.toJavaDuration(), Instant.now())
        }
    }

    @Test
    suspend fun `kan overstyre mottaker av brev`() {
        val mottaker = Dto.Mottaker.norskAdresse(
            navn = "Anon Y. Mouse",
            postnummer = NorskPostnummer("0001"),
            poststed = "Andeby",
            adresselinje1 = "Andebyveien 1",
            adresselinje2 = null,
            adresselinje3 = null,
            manueltAdressertTil = ANNEN
        )
        val brev = opprettBrev(mottaker = mottaker)

        assertThat(brev).isSuccess {
            assertThat(it.info.mottaker).isEqualTo(mottaker)
            assertThat(it.redigertBrev.sakspart.annenMottakerNavn).isEqualTo(mottaker.navn)
        }
    }

    @Test
    suspend fun `opprettBrev setter redigertBrevHash`() {
        assertThat(opprettBrev(reserverForRedigering = true)).isSuccess {
            assertThat(it.redigertBrevHash).isEqualTo(Hash.read(letter.toEdit()))
        }
    }

}