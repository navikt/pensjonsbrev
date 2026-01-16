package no.nav.pensjon.brev.skribenten.usecase

import no.nav.pensjon.brev.skribenten.Testbrevkoder
import no.nav.pensjon.brev.skribenten.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.domain.KlarTilSendingPolicy
import no.nav.pensjon.brev.skribenten.domain.RedigerBrevPolicy
import no.nav.pensjon.brev.skribenten.isFailure
import no.nav.pensjon.brev.skribenten.isSuccess
import no.nav.pensjon.brev.skribenten.letter.letter
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brevbaker.api.model.ElementTags
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.BlockImpl.ParagraphImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import no.nav.pensjon.brev.skribenten.letter.Edit.Block.Paragraph as E_Paragraph
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.Literal as E_Literal

class VeksleKlarStatusHandlerTest : BrevredigeringTest() {

    private val markupMedFritekst = letter(
        ParagraphImpl(
            1,
            true,
            listOf(
                LiteralImpl(12, "Vi har "),
                LiteralImpl(13, "dato", tags = setOf(ElementTags.FRITEKST)),
                LiteralImpl(14, " mottatt søknad.")
            )
        )
    )

    @Test
    suspend fun `informasjonsbrev faar status klar`() {
        val brev = opprettBrev().resultOrFail()
        assertThat(brev.info.status).isEqualTo(Dto.BrevStatus.KLADD)

        assertThat(veksleKlarStatus(brev, true)).isSuccess {
            assertThat(it.info.status).isEqualTo(Dto.BrevStatus.KLAR)
        }

    }

    @Test
    suspend fun `informasjonsbrev i vedtakskontekst faar status KLAR`() {
        val brev = opprettBrev(brevkode = Testbrevkoder.VARSELBREV, vedtaksId = 1).resultOrFail()

        assertThat(veksleKlarStatus(brev, true)).isSuccess {
            assertThat(it.info.status).isEqualTo(Dto.BrevStatus.KLAR)
        }
    }

    @Test
    suspend fun `vedtaksbrev faar status ATTESTERING`() {
        val brev = opprettBrev(brevkode = Testbrevkoder.VEDTAKSBREV, vedtaksId = 1).resultOrFail()

        assertThat(veksleKlarStatus(brev, true)).isSuccess {
            assertThat(it.info.status).isEqualTo(Dto.BrevStatus.ATTESTERING)
        }
    }

    @Test
    suspend fun `attestering fjernes om brevet laases opp igjen`() {
        val brev = opprettBrev(brevkode = Testbrevkoder.VEDTAKSBREV, vedtaksId = 1).resultOrFail()

        assertThat(veksleKlarStatus(brev, true)).isSuccess()

        val attestert = attester(brev, attestant = attestantPrincipal, frigiReservasjon = true)

        assertThat(attestert?.info?.status).isEqualTo(Dto.BrevStatus.KLAR)
        assertThat(attestert?.info?.attestertAv).isEqualTo(attestantPrincipal.navIdent)
        assertThat(attestert?.redigertBrev?.signatur?.attesterendeSaksbehandlerNavn).isEqualTo(attestantPrincipal.fullName)

        assertThat(veksleKlarStatus(brev, false)).isSuccess {
            assertThat(it.info.status).isEqualTo(Dto.BrevStatus.KLADD)
            assertThat(it.info.attestertAv).isNull()
            assertThat(it.redigertBrev.signatur.attesterendeSaksbehandlerNavn).isNull()
        }
    }

    @Test
    suspend fun `kan ikke markere brev klar til sending om ikke alle fritekst er fylt ut`() {
        brevbakerService.renderMarkupResultat = { markupMedFritekst }
        val brev = opprettBrev().resultOrFail()

        assertThat(veksleKlarStatus(brev, true)).isFailure<KlarTilSendingPolicy.IkkeKlarTilSending.FritekstFelterUredigert, _, _>()
    }

    @Test
    suspend fun `kan markere brev klar til sending om alle fritekst er fylt ut`() {
        brevbakerService.renderMarkupResultat = { markupMedFritekst }
        val brev = opprettBrev().resultOrFail()

        assertThat(
            oppdaterBrev(
                brevId = brev.info.id,
                nyttRedigertbrev = brev.redigertBrev.copy(
                    blocks = listOf(
                        E_Paragraph(
                            1,
                            true,
                            listOf(
                                E_Literal(12, "Vi har "),
                                E_Literal(13, "dato", tags = setOf(ElementTags.FRITEKST), editedText = "redigert"),
                                E_Literal(14, " mottatt søknad.")
                            )
                        )
                    )
                )
            )
        ).isSuccess()

        assertThat(veksleKlarStatus(brev = brev, klar = true)).isSuccess {
            assertThat(it.info.status).isEqualTo(Dto.BrevStatus.KLAR)
        }
    }

    @Test
    suspend fun `kan ikke markere brev reservert av annen som klar til sending`() {
        val brev = opprettBrev(reserverForRedigering = true).resultOrFail()

        assertThat(veksleKlarStatus(brev, true, principal = saksbehandler2Principal))
            .isFailure<BrevreservasjonPolicy.ReservertAvAnnen, _, _>()
    }

    @Test
    suspend fun `kan ikke sette arkivert brev tilbake til kladd`() {
        val brev = opprettBrev().resultOrFail()
        arkiverBrev(brev).resultOrFail()

        assertThat(veksleKlarStatus(brev, false))
            .isFailure<RedigerBrevPolicy.KanIkkeRedigere.ArkivertBrev, _, _>()
    }

    @Test
    suspend fun `beholder ikke reservasjon`() {
        val brev = opprettBrev().resultOrFail()

        assertThat(veksleKlarStatus(brev, klar = true))
            .isSuccess {
                assertThat(it.info.redigeresAv).isNotEqualTo(saksbehandler1Principal.navIdent)
            }
    }
}