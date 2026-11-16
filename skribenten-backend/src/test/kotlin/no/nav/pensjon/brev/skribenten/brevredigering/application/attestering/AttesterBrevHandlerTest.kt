package no.nav.pensjon.brev.skribenten.brevredigering.application.attestering

import no.nav.pensjon.brev.skribenten.Testbrevkoder
import no.nav.pensjon.brev.skribenten.auth.UserPrincipal
import no.nav.pensjon.brev.skribenten.auth.withPrincipal
import no.nav.pensjon.brev.skribenten.brevredigering.application.BrevredigeringHandlerTestBase
import no.nav.pensjon.brev.skribenten.brevredigering.domain.AttesterBrevPolicy
import no.nav.pensjon.brev.skribenten.brevredigering.domain.FerdigRedigertPolicy
import no.nav.pensjon.brev.skribenten.brevredigering.domain.MarkupVersjon
import no.nav.pensjon.brev.skribenten.brevredigering.domain.asV1
import no.nav.pensjon.brev.skribenten.brevredigering.domain.blocks
import no.nav.pensjon.brev.skribenten.brevredigering.domain.gjelderNavn
import no.nav.pensjon.brev.skribenten.brevredigering.domain.navAvsenderEnhet
import no.nav.pensjon.brev.skribenten.brevredigering.domain.title
import no.nav.pensjon.brev.skribenten.isFailure
import no.nav.pensjon.brev.skribenten.isSuccess
import no.nav.pensjon.brev.skribenten.letter.v2.MarkupV2
import no.nav.pensjon.brev.skribenten.letter.v2.letter as letterV2
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.VedtaksId
import no.nav.brev.brevbaker.markup.outline.EditBehaviour
import no.nav.pensjon.brevbaker.api.model.ElementTags
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.BlockImpl.ParagraphImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

class AttesterBrevHandlerTest : BrevredigeringHandlerTestBase() {

    @BeforeEach
    fun stageV2() = stageMarkupV2()

    private suspend fun vedtaksbrev(versjon: MarkupVersjon, reserverForRedigering: Boolean = false, principal: UserPrincipal = saksbehandler1Principal) =
        opprettBrev(
            brevkode = Testbrevkoder.VEDTAKSBREV,
            vedtaksId = VedtaksId(1234),
            reserverForRedigering = reserverForRedigering,
            principal = principal,
            markupVersjon = versjon,
        ).resultOrFail()

    @ParameterizedTest
    @EnumSource(MarkupVersjon::class)
    suspend fun `kan attestere vedtaksbrev som er klar`(versjon: MarkupVersjon) {
        val brev = vedtaksbrev(versjon)
        assertThat(veksleKlarStatus(brev, klar = true)).isSuccess {
            assertThat(it.status).isEqualTo(Dto.BrevStatus.ATTESTERING)
        }

        val resultat = attester(brev, attestant = attestant1Principal, frigiReservasjon = true)

        assertThat(resultat).isSuccess { attestert ->
            assertThat(attestert.redigertBrev.markupVersjon).isEqualTo(versjon)
            assertThat(attestert.info.attestertAv).isEqualTo(attestant1Principal.navIdent)
            assertThat(attestert.info.redigeresAv).isNull()
            assertThat(attestert.redigertBrev.attesterendeSaksbehandlerNavn).isEqualTo(attestant1Principal.fullName)
            assertThat(attestert.info.status).isEqualTo(Dto.BrevStatus.KLAR)
        }
    }

    @ParameterizedTest
    @EnumSource(MarkupVersjon::class)
    suspend fun `kan ikke attestere hvis ikke attestant`(versjon: MarkupVersjon) {
        val brev = vedtaksbrev(versjon)
        veksleKlarStatus(brev, klar = true).resultOrFail()

        val resultat = attester(brev, attestant = saksbehandler2Principal)
        assertThat(resultat).isFailure<AttesterBrevPolicy.KanIkkeAttestere.HarIkkeAttestantrolle, _, _>()
    }

    @ParameterizedTest
    @EnumSource(MarkupVersjon::class)
    suspend fun `kan ikke attestere informasjonsbrev`(versjon: MarkupVersjon) {
        val brev = opprettBrev(brevkode = Testbrevkoder.INFORMASJONSBREV, vedtaksId = VedtaksId(1234), markupVersjon = versjon).resultOrFail()
        veksleKlarStatus(brev, klar = true).resultOrFail()

        val resultat = attester(brev, attestant = attestant1Principal)
        assertThat(resultat).isFailure<AttesterBrevPolicy.KanIkkeAttestere.KanIkkeAttestereInformasjonsbrev, _, _>()
    }

    @ParameterizedTest
    @EnumSource(MarkupVersjon::class)
    suspend fun `kan attestere brev som ikke er klar ved aa markere det som klar automatisk`(versjon: MarkupVersjon) {
        val brev = vedtaksbrev(versjon)

        val resultat = attester(brev, attestant = attestant1Principal)
        assertThat(resultat).isSuccess {
            assertThat(it.info.status).isEqualTo(Dto.BrevStatus.KLAR)
            assertThat(it.info.attestertAv).isEqualTo(attestant1Principal.navIdent)
            assertThat(it.redigertBrev.attesterendeSaksbehandlerNavn).isEqualTo(attestant1Principal.fullName)
        }
    }

    @ParameterizedTest
    @EnumSource(MarkupVersjon::class)
    suspend fun `kan ikke attestere kladd med uredigerte fritekstfelter og brevet markeres ikke som klar`(versjon: MarkupVersjon) {
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
        brevbakerService.renderMarkupV2Resultat = { felles ->
            letterV2(
                MarkupV2.paragraph(2, MarkupV2.literal(3, "fyll inn fritekst", editBehaviour = EditBehaviour.FRITEKST)),
                signatur = MarkupV2.signatur(
                    saksbehandlerNavn = felles.signerendeSaksbehandlere?.saksbehandler,
                    attesterendeSaksbehandlerNavn = felles.signerendeSaksbehandlere?.attesterendeSaksbehandler,
                ),
            )
        }

        val brev = vedtaksbrev(versjon)

        val resultat = attester(brev, attestant = attestant1Principal)
        assertThat(resultat).isFailure<FerdigRedigertPolicy.IkkeFerdigRedigert.FritekstFelterUredigert, _, _>()

        val etterAttestForsoek = hentBrev(brev.info.id).resultOrFail()
        assertThat(etterAttestForsoek.info.status).isEqualTo(Dto.BrevStatus.KLADD)
        assertThat(etterAttestForsoek.info.attestertAv).isNull()
    }

    @ParameterizedTest
    @EnumSource(MarkupVersjon::class)
    suspend fun `kan ikke attestere eget brev`(versjon: MarkupVersjon) {
        val brev = vedtaksbrev(versjon, principal = attestant1Principal)

        veksleKlarStatus(brev, klar = true, principal = attestant1Principal).resultOrFail()

        val resultat = attester(brev, attestant = attestant1Principal)
        assertThat(resultat).isFailure<AttesterBrevPolicy.KanIkkeAttestere.KanIkkeAttestereEgetBrev, _, _>()
    }

    @ParameterizedTest
    @EnumSource(MarkupVersjon::class)
    suspend fun `kan ikke attestere brev som allerede er attestert av annen`(versjon: MarkupVersjon) {
        val brev = vedtaksbrev(versjon)
        veksleKlarStatus(brev, klar = true).resultOrFail()

        attester(brev, attestant = attestant1Principal, frigiReservasjon = true)

        val resultat = attester(brev, attestant = attestant2Principal)
        assertThat(resultat).isFailure<AttesterBrevPolicy.KanIkkeAttestere.AlleredeAttestertAvAnnen, _, _>()
    }

    @ParameterizedTest
    @EnumSource(MarkupVersjon::class)
    suspend fun `beholder reservasjon hvis frigiReservasjon er false`(versjon: MarkupVersjon) {
        val brev = vedtaksbrev(versjon)
        veksleKlarStatus(brev, klar = true).resultOrFail()

        val resultat = attester(brev, attestant = attestant1Principal, frigiReservasjon = false)

        assertThat(resultat).isSuccess { attestert ->
            assertThat(attestert.info.redigeresAv).isEqualTo(attestant1Principal.navIdent)
        }
    }

    @Test
    suspend fun `returnerer null hvis brev ikke finnes`() {
        val resultat = withPrincipal(attestant1Principal) {
            attesterBrev(AttesterBrevHandler.Request(brevId = BrevId(-9999L), saksId = sak1.saksId))
        }

        assertThat(resultat).isNull()
    }

    @ParameterizedTest
    @EnumSource(MarkupVersjon::class)
    suspend fun `kan redigerere attestant signatur`(versjon: MarkupVersjon) {
        val brev = vedtaksbrev(versjon, reserverForRedigering = true)
        veksleKlarStatus(brev, klar = true).resultOrFail()

        val redigertAttestantSignatur = brev.redigertBrev.withSignaturAttestant("Ny attestant signatur")
        val attestert = attester(
            brev,
            nyttRedigertbrev = redigertAttestantSignatur,
            attestant = attestant1Principal,
            frigiReservasjon = true
        )
        assertThat(attestert).isSuccess {
            assertThat(it.redigertBrev.attesterendeSaksbehandlerNavn).isEqualTo("Ny attestant signatur")
        }
    }

    @ParameterizedTest
    @EnumSource(MarkupVersjon::class)
    suspend fun `attestering merger ikke inn endringer fra malen, men oppdaterer sakspart og malstyrt signatur`(versjon: MarkupVersjon) {
        val brev = vedtaksbrev(versjon)
        veksleKlarStatus(brev, klar = true).resultOrFail()

        stagEndretMal()

        assertThat(attester(brev, attestant = attestant1Principal)).isSuccess {
            assertThat(it.redigertBrev.blocks()).isEqualTo(brev.redigertBrev.blocks())
            assertThat(it.redigertBrev.title()).isEqualTo(brev.redigertBrev.title())
            assertThat(it.redigertBrev.gjelderNavn()).isEqualTo("Nytt Navn")
            assertThat(it.redigertBrev.navAvsenderEnhet()).isEqualTo("Ny avsenderenhet")
            assertThat(it.redigertBrev.saksbehandlerNavn).isEqualTo(brev.redigertBrev.saksbehandlerNavn)
            assertThat(it.redigertBrev.attesterendeSaksbehandlerNavn).isEqualTo(attestant1Principal.fullName)
            if (versjon == MarkupVersjon.V1) {
                assertThat(it.redigertBrev.asV1().signatur.hilsenTekst).isEqualTo("Ny hilsen")
            }
        }
    }
}
