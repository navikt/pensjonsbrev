package no.nav.pensjon.brev.skribenten.brevredigering.application.livssyklus

import no.nav.pensjon.brev.skribenten.brevredigering.domain.asV1
import no.nav.pensjon.brev.skribenten.letter.v2.letter as letterV2
import no.nav.pensjon.brev.skribenten.letter.v2.MarkupV2
import no.nav.pensjon.brev.skribenten.brevredigering.domain.VersjonertMarkup
import no.nav.pensjon.brev.skribenten.brevredigering.domain.MarkupVersjon
import no.nav.pensjon.brev.skribenten.brevredigering.application.BrevredigeringHandlerTestBase
import no.nav.brev.BrevLandmodell.Landkode

import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevkode
import no.nav.pensjon.brev.skribenten.Testbrevkoder
import no.nav.pensjon.brev.skribenten.auth.withPrincipal
import no.nav.pensjon.brev.skribenten.brevredigering.domain.Adresselinje
import no.nav.pensjon.brev.skribenten.db.Hash
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevmalFinnesIkke
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.brevredigering.domain.Navn
import no.nav.pensjon.brev.skribenten.brevredigering.domain.OpprettBrevPolicy.KanIkkeOppretteBrev.*
import no.nav.pensjon.brev.skribenten.brevredigering.domain.Poststed
import no.nav.pensjon.brev.skribenten.isFailure
import no.nav.pensjon.brev.skribenten.isSuccess
import no.nav.pensjon.brev.skribenten.letter.toEdit
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.Dto.Mottaker.ManueltAdressertTil.ANNEN
import no.nav.pensjon.brev.skribenten.model.NorskPostnummer
import no.nav.pensjon.brev.skribenten.model.SaksbehandlervalgMap
import no.nav.pensjon.brev.skribenten.model.SaksbehandlervalgVerdi
import no.nav.pensjon.brev.skribenten.model.VedtaksId
import no.nav.pensjon.brev.skribenten.services.EnhetId
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource
import java.time.Instant
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration

class OpprettBrevHandlerTest : BrevredigeringHandlerTestBase() {

    @Test
    suspend fun `kan opprette brev med markup v2`() {
        val markup = letterV2(MarkupV2.paragraph(1, MarkupV2.literal(1, "red pill")))
        brevbakerService.renderMarkupV2Resultat = { markup }

        val brev = opprettBrev(markupVersjon = MarkupVersjon.V2)

        assertThat(brev).isSuccess {
            assertThat(it.info.markupVersjon).isEqualTo(MarkupVersjon.V2)
            assertThat(it.redigertBrev).isEqualTo(VersjonertMarkup.V2(markup).toEdit())
            assertThat(transaction { BrevredigeringEntity[it.info.id].markupVersjon }).isEqualTo(MarkupVersjon.V2)
        }
    }

    @Test
    suspend fun `kan opprette brev`() {
        val saksbehandlerValg = SaksbehandlervalgMap().apply { put("valg1", SaksbehandlervalgVerdi.Boolean(true)) }
        val brev = opprettBrev(reserverForRedigering = true, saksbehandlerValg = saksbehandlerValg)

        assertThat(brevbakerService.renderMarkupKall.first()).isEqualTo(Testbrevkoder.INFORMASJONSBREV to LanguageCode.ENGLISH)
        assertThat(brev).isSuccess {
            assertThat(it.info.brevkode.kode()).isEqualTo(Testbrevkoder.INFORMASJONSBREV.kode())
            assertThat(it.redigertBrev.asV1()).isEqualTo(letter.toEdit())
        }
    }

    @Test
    suspend fun `initialiserer signatur for brev`() {
        val brev = opprettBrev(reserverForRedigering = true)

        assertThat(brev).isSuccess {
            assertThat(it.redigertBrev.asV1().signatur).isNotNull
            assertThat(it.redigertBrev.asV1().signatur.saksbehandlerNavn).isEqualTo(saksbehandler1Principal.fullName)
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
            navn = Navn("Anon Y. Mouse"),
            postnummer = NorskPostnummer("0001"),
            poststed = Poststed("Andeby"),
            adresselinje1 = Adresselinje("Andebyveien 1"),
            adresselinje2 = null,
            adresselinje3 = null,
            manueltAdressertTil = ANNEN
        )
        val brev = opprettBrev(mottaker = mottaker)

        assertThat(brev).isSuccess {
            assertThat(it.info.mottaker).isEqualTo(mottaker)
            assertThat(it.redigertBrev.asV1().sakspart.annenMottakerNavn).isEqualTo(mottaker.navn?.value)
            assertThat(transaction { BrevredigeringEntity[it.info.id].mottaker }).isEqualTo(mottaker)
        }
    }

    @ParameterizedTest
    @EnumSource(value = Dto.Mottaker.ManueltAdressertTil::class, names = ["BRUKER", "ANNEN"])
    suspend fun `lagrer adresseringsvalg og setter riktig navn for utenlandsk mottaker`(adressertTil: Dto.Mottaker.ManueltAdressertTil) {
        val mottaker = Dto.Mottaker.utenlandskAdresse(
            navn = Navn("Test Mottaker"),
            adresselinje1 = Adresselinje("Adresse 1"),
            adresselinje2 = null,
            adresselinje3 = null,
            landkode = Landkode("SE"),
            manueltAdressertTil = adressertTil,
        )

        assertThat(opprettBrev(mottaker = mottaker)).isSuccess {
            assertThat(it.info.mottaker).isEqualTo(mottaker)
            assertThat(transaction { BrevredigeringEntity[it.info.id].mottaker }).isEqualTo(mottaker)
            assertThat(it.redigertBrev.asV1().sakspart.annenMottakerNavn).isEqualTo(if (adressertTil == ANNEN) mottaker.navn?.value else null)
        }
    }

    @Test
    suspend fun `opprettBrev setter redigertBrevHash`() {
        assertThat(opprettBrev(reserverForRedigering = true)).isSuccess {
            assertThat(it.redigertBrevHash).isEqualTo(Hash.read(letter.toEdit()))
        }
    }

    @Test
    suspend fun `lagrer statiskFagsystemBrevdata paa brevet og den rundtripper via databasen`() {
        val statiskFagsystemBrevdata = Api.GeneriskBrevdata().apply { put("fraFagsystem", "ja") }

        val brev = opprettBrev(statiskFagsystemBrevdata = statiskFagsystemBrevdata)

        assertThat(brev).isSuccess {
            val lagretPaaBrevet = transaction { BrevredigeringEntity[it.info.id].statiskFagsystemBrevdata }
            assertThat(lagretPaaBrevet).isEqualTo(statiskFagsystemBrevdata)
        }
    }

    @Test
    suspend fun `statiskFagsystemBrevdata er null paa brevet naar den ikke er oppgitt`() {
        val brev = opprettBrev()

        assertThat(brev).isSuccess {
            val lagretPaaBrevet = transaction { BrevredigeringEntity[it.info.id].statiskFagsystemBrevdata }
            assertThat(lagretPaaBrevet).isNull()
        }
    }

}