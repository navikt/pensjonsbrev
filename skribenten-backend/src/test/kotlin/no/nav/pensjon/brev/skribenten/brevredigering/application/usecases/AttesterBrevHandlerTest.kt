package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.Testbrevkoder
import no.nav.pensjon.brev.skribenten.auth.withPrincipal
import no.nav.pensjon.brev.skribenten.brevredigering.domain.AttesterBrevPolicy
import no.nav.pensjon.brev.skribenten.brevredigering.domain.FerdigRedigertPolicy
import no.nav.pensjon.brev.skribenten.isFailure
import no.nav.pensjon.brev.skribenten.isSuccess
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.VedtaksId
import no.nav.pensjon.brevbaker.api.model.ElementTags
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.BlockImpl.ParagraphImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.*

class AttesterBrevHandlerTest : BrevredigeringHandlerTestBase() {

    @Test
    suspend fun `kan attestere vedtaksbrev som er klar`() {
        val brev = opprettBrev(brevkode = Testbrevkoder.VEDTAKSBREV, vedtaksId = VedtaksId(1234)).resultOrFail()
        assertThat(veksleKlarStatus(brev, klar = true)).isSuccess {
            assertThat(it.status).isEqualTo(Dto.BrevStatus.ATTESTERING)
        }

        val resultat = attester(brev, attestant = attestant1Principal, frigiReservasjon = true)

        assertThat(resultat).isSuccess { attestert ->
            assertThat(attestert.info.attestertAv).isEqualTo(attestant1Principal.navIdent)
            assertThat(attestert.info.redigeresAv).isNull()
            assertThat(attestert.redigertBrev.signatur.attesterendeSaksbehandlerNavn).isEqualTo(attestant1Principal.fullName)
            assertThat(attestert.info.status).isEqualTo(Dto.BrevStatus.KLAR)
        }
    }

    @Test
    suspend fun `kan ikke attestere hvis ikke attestant`() {
        val brev = opprettBrev(brevkode = Testbrevkoder.VEDTAKSBREV, vedtaksId = VedtaksId(1234)).resultOrFail()
        veksleKlarStatus(brev, klar = true).resultOrFail()

        val resultat = attester(brev, attestant = saksbehandler2Principal)
        assertThat(resultat).isFailure<AttesterBrevPolicy.KanIkkeAttestere.HarIkkeAttestantrolle, _, _>()
    }

    @Test
    suspend fun `kan ikke attestere informasjonsbrev`() {
        val brev = opprettBrev(brevkode = Testbrevkoder.INFORMASJONSBREV, vedtaksId = VedtaksId(1234)).resultOrFail()
        veksleKlarStatus(brev, klar = true).resultOrFail()

        val resultat = attester(brev, attestant = attestant1Principal)
        assertThat(resultat).isFailure<AttesterBrevPolicy.KanIkkeAttestere.KanIkkeAttestereInformasjonsbrev, _, _>()
    }

    @Test
    suspend fun `kan attestere brev som ikke er klar ved aa markere det som klar automatisk`() {
        val brev = opprettBrev(brevkode = Testbrevkoder.VEDTAKSBREV, vedtaksId = VedtaksId(1234)).resultOrFail()

        val resultat = attester(brev, attestant = attestant1Principal)
        assertThat(resultat).isSuccess {
            assertThat(it.info.status).isEqualTo(Dto.BrevStatus.KLAR)
            assertThat(it.info.attestertAv).isEqualTo(attestant1Principal.navIdent)
            assertThat(it.redigertBrev.signatur.attesterendeSaksbehandlerNavn).isEqualTo(attestant1Principal.fullName)
        }
    }

    @Test
    suspend fun `kan ikke attestere kladd med uredigerte fritekstfelter og brevet markeres ikke som klar`() {
        brevbakerService.renderMarkupResultat = { felles ->
            letter.copy(
                blocks = listOf(
                    ParagraphImpl(2, true, listOf(LiteralImpl(3, "fyll inn fritekst", tags = setOf(ElementTags.FRITEKST))))
                )
            ).medSignatur(
                saksbehandler = felles.signerendeSaksbehandlere?.saksbehandler,
                attestant = felles.signerendeSaksbehandlere?.attesterendeSaksbehandler,
            )
        }

        val brev = opprettBrev(brevkode = Testbrevkoder.VEDTAKSBREV, vedtaksId = VedtaksId(1234)).resultOrFail()

        val resultat = attester(brev, attestant = attestant1Principal)
        assertThat(resultat).isFailure<FerdigRedigertPolicy.IkkeFerdigRedigert.FritekstFelterUredigert, _, _>()

        val etterAttestForsoek = hentBrev(brev.info.id).resultOrFail()
        assertThat(etterAttestForsoek.info.status).isEqualTo(Dto.BrevStatus.KLADD)
        assertThat(etterAttestForsoek.info.attestertAv).isNull()
    }

    @Test
    suspend fun `kan ikke attestere eget brev`() {
        val brev = opprettBrev(
            brevkode = Testbrevkoder.VEDTAKSBREV,
            vedtaksId = VedtaksId(1234),
            principal = attestant1Principal
        ).resultOrFail()

        veksleKlarStatus(brev, klar = true, principal = attestant1Principal).resultOrFail()

        val resultat = attester(brev, attestant = attestant1Principal)
        assertThat(resultat).isFailure<AttesterBrevPolicy.KanIkkeAttestere.KanIkkeAttestereEgetBrev, _, _>()
    }

    @Test
    suspend fun `kan ikke attestere brev som allerede er attestert av annen`() {
        val brev = opprettBrev(brevkode = Testbrevkoder.VEDTAKSBREV, vedtaksId = VedtaksId(1234)).resultOrFail()
        veksleKlarStatus(brev, klar = true).resultOrFail()

        // Første attestering
        attester(brev, attestant = attestant1Principal, frigiReservasjon = true)

        // Forsøk på ny attestering med annen attestant
        val resultat = attester(brev, attestant = attestant2Principal)
        assertThat(resultat).isFailure<AttesterBrevPolicy.KanIkkeAttestere.AlleredeAttestertAvAnnen, _, _>()
    }

    @Test
    suspend fun `beholder reservasjon hvis frigiReservasjon er false`() {
        val brev = opprettBrev(brevkode = Testbrevkoder.VEDTAKSBREV, vedtaksId = VedtaksId(1234)).resultOrFail()
        veksleKlarStatus(brev, klar = true).resultOrFail()

        val resultat = attester(brev, attestant = attestant1Principal, frigiReservasjon = false)

        assertThat(resultat).isSuccess { attestert ->
            assertThat(attestert.info.redigeresAv).isEqualTo(attestant1Principal.navIdent)
        }
    }

    @Test
    suspend fun `returnerer null hvis brev ikke finnes`() {
        val resultat = withPrincipal(attestant1Principal) {
            brevredigeringFacade.attesterBrev(AttesterBrevHandler.Request(brevId = BrevId(-9999L)))
        }

        assertThat(resultat).isNull()
    }

    @Test
    suspend fun `kan redigerere attestant signatur`() {
        val brev = opprettBrev(brevkode = Testbrevkoder.VEDTAKSBREV, vedtaksId = VedtaksId(1234), reserverForRedigering = true).resultOrFail()
        veksleKlarStatus(brev, klar = true).resultOrFail()

        val redigertAttestantSignatur = brev.redigertBrev.withSignaturAttestant("Ny attestant signatur")
        val attestert = attester(
            brev,
            nyttRedigertbrev = redigertAttestantSignatur,
            attestant = attestant1Principal,
            frigiReservasjon = true
        )
        assertThat(attestert).isSuccess {
            assertThat(it.redigertBrev.signatur.attesterendeSaksbehandlerNavn).isEqualTo("Ny attestant signatur")
        }
    }
}




