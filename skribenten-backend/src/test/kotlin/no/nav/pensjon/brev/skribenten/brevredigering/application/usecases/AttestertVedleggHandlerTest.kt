package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.Testbrevkoder
import no.nav.pensjon.brev.skribenten.auth.UserPrincipal
import no.nav.pensjon.brev.skribenten.auth.withPrincipal
import no.nav.pensjon.brev.skribenten.brevredigering.application.RedigerbartVedleggInfo
import no.nav.pensjon.brev.skribenten.brevredigering.domain.AttesterBrevPolicy
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.brevredigering.domain.VedleggFinnesIkkeIMal
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.isFailure
import no.nav.pensjon.brev.skribenten.isSuccess
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.letter.toEdit
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.VedtaksId
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.VedleggId
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.BlockImpl.ParagraphImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.junit.jupiter.api.Test

class AttestertVedleggHandlerTest : BrevredigeringHandlerTestBase() {

    private val vedlegg1 = VedleggId("vedlegg1")

    private fun malVedlegg(vararg avsnitt: Pair<Int, String>, tittel: String = "Mal tittel"): LetterMarkup.Attachment =
        LetterMarkupImpl.AttachmentImpl(
            title = listOf(LiteralImpl(1, tittel)),
            blocks = avsnitt.map { (id, tekst) -> ParagraphImpl(id, true, listOf(LiteralImpl(id * 10, tekst))) },
            includeSakspart = false,
        )

    private fun Edit.Attachment.foersteLiteral(): Edit.ParagraphContent.Text.Literal =
        (blocks.first() as Edit.Block.Paragraph).content.first() as Edit.ParagraphContent.Text.Literal

    private fun Edit.Attachment.medRedigertTekst(tekst: String): Edit.Attachment {
        val foerste = blocks.first() as Edit.Block.Paragraph
        val literal = foerste.content.first() as Edit.ParagraphContent.Text.Literal
        return copy(blocks = listOf(foerste.copy(content = listOf(literal.copy(editedText = tekst)))) + blocks.drop(1))
    }

    private fun lagretVedlegg(brevId: BrevId): Edit.Attachment =
        transaction { BrevredigeringEntity[brevId].hentRedigertVedlegg(vedlegg1)!! }

    private fun stagMal(vararg avsnitt: Pair<Int, String>, tittel: String = "Mal tittel") {
        brevbakerService.renderRedigerbareVedleggResultat = mapOf(vedlegg1 to malVedlegg(*avsnitt, tittel = tittel))
    }

    private suspend fun klartVedtaksbrevMedRedigertVedlegg(redigertTekst: String = "Redigert"): Dto.Brevredigering {
        stagMal(1 to "Foerste")
        val brev = opprettBrev(brevkode = Testbrevkoder.VEDTAKSBREV, vedtaksId = VedtaksId(1234)).resultOrFail()

        val fraMalen = withPrincipal(saksbehandler1Principal) {
            hentRedigertVedlegg(HentRedigertVedleggHandler.Request(brevId = brev.info.id, saksId = sak1.saksId, vedleggId = vedlegg1))
        }.resultOrFail()

        assertThat(
            withPrincipal(saksbehandler1Principal) {
                endreRedigertVedlegg(
                    EndreRedigertVedleggHandler.Request(
                        brevId = brev.info.id,
                        saksId = sak1.saksId,
                        vedleggId = vedlegg1,
                        redigertVedlegg = fraMalen.medRedigertTekst(redigertTekst),
                    )
                )
            }
        ).isSuccess()

        veksleKlarStatus(brev, klar = true).resultOrFail()
        return brev
    }

    private suspend fun hentBrevAttestering(
        brevId: BrevId,
        reserverForRedigering: Boolean,
        attestant: UserPrincipal = attestant1Principal,
    ): Outcome<Dto.Brevredigering, BrevredigeringError>? = withPrincipal(attestant) {
        hentBrevAttestering(
            HentBrevAttesteringHandler.Request(brevId = brevId, saksId = sak1.saksId, reserverForRedigering = reserverForRedigering)
        )
    }

    private suspend fun hentVedleggForAttestering(
        brevId: BrevId,
        vedleggId: VedleggId = vedlegg1,
        attestant: UserPrincipal = attestant1Principal,
    ): Outcome<Edit.Attachment, BrevredigeringError>? = withPrincipal(attestant) {
        hentRedigertVedleggAttestering(
            HentRedigertVedleggAttesteringHandler.Request(brevId = brevId, saksId = sak1.saksId, vedleggId = vedleggId)
        )
    }

    private suspend fun hentVedleggstitlerForAttestering(
        brevId: BrevId,
        attestant: UserPrincipal = attestant1Principal,
    ): Outcome<List<RedigerbartVedleggInfo>, BrevredigeringError>? = withPrincipal(attestant) {
        hentRedigerbareVedleggAttestering(
            HentRedigerbareVedleggAttesteringHandler.Request(brevId = brevId, saksId = sak1.saksId)
        )
    }

    private suspend fun lagreVedleggSomAttestant(
        brevId: BrevId,
        redigertVedlegg: Edit.Attachment,
        vedleggId: VedleggId = vedlegg1,
        attestant: UserPrincipal = attestant1Principal,
        frigiReservasjon: Boolean = false,
    ): Outcome<Edit.Attachment, BrevredigeringError>? = withPrincipal(attestant) {
        lagreAttestertVedlegg(
            LagreAttestertVedleggHandler.Request(
                brevId = brevId,
                saksId = sak1.saksId,
                vedleggId = vedleggId,
                redigertVedlegg = redigertVedlegg,
                frigiReservasjon = frigiReservasjon,
            )
        )
    }

    @Test
    suspend fun `henting av vedlegg for attestering merger ikke inn malendringer`() {
        val brev = klartVedtaksbrevMedRedigertVedlegg()
        stagMal(1 to "Foerste", 2 to "Andre")

        assertThat(hentVedleggForAttestering(brev.info.id)).isSuccess {
            assertThat(it.blocks.map { blokk -> blokk.id }).containsExactly(1)
            assertThat(it.foersteLiteral().editedText).isEqualTo("Redigert")
        }
        assertThat(lagretVedlegg(brev.info.id).blocks.map { it.id }).containsExactly(1)
    }

    @Test
    suspend fun `henting av vedlegg for attestering faller tilbake til malen naar vedlegget ikke er overstyrt`() {
        val brev = opprettBrev(brevkode = Testbrevkoder.VEDTAKSBREV, vedtaksId = VedtaksId(1234)).resultOrFail()
        stagMal(1 to "Foerste")
        veksleKlarStatus(brev, klar = true).resultOrFail()

        assertThat(hentVedleggForAttestering(brev.info.id)).isSuccess {
            assertThat(it).isEqualTo(malVedlegg(1 to "Foerste").toEdit())
        }
    }

    @Test
    suspend fun `henting av vedlegg for attestering gir feil naar vedlegget ikke finnes i malen`() {
        val brev = klartVedtaksbrevMedRedigertVedlegg()

        assertThat(hentVedleggForAttestering(brev.info.id, vedleggId = VedleggId("finnesIkke")))
            .isFailure<VedleggFinnesIkkeIMal, _, _> {
                assertThat(it.vedleggId).isEqualTo(VedleggId("finnesIkke"))
            }
    }

    @Test
    suspend fun `attestantens lagring merger ikke inn malendringer`() {
        val brev = klartVedtaksbrevMedRedigertVedlegg()
        val lagret = lagretVedlegg(brev.info.id)
        stagMal(1 to "Foerste", 2 to "Andre")

        assertThat(lagreVedleggSomAttestant(brev.info.id, lagret.medRedigertTekst("Attestantens tekst"))).isSuccess {
            assertThat(it.blocks.map { blokk -> blokk.id }).containsExactly(1)
            assertThat(it.foersteLiteral().editedText).isEqualTo("Attestantens tekst")
        }
        assertThat(lagretVedlegg(brev.info.id).foersteLiteral().editedText).isEqualTo("Attestantens tekst")
        assertThat(lagretVedlegg(brev.info.id).blocks.map { it.id }).containsExactly(1)
    }

    @Test
    suspend fun `attestanten frigir reservasjonen naar frigiReservasjon er true`() {
        val brev = klartVedtaksbrevMedRedigertVedlegg()
        val lagret = lagretVedlegg(brev.info.id)

        assertThat(lagreVedleggSomAttestant(brev.info.id, lagret, frigiReservasjon = false)).isSuccess()
        assertThat(hentBrevInfo(brev.info.id).resultOrFail().redigeresAv).isEqualTo(attestant1Principal.navIdent)

        assertThat(lagreVedleggSomAttestant(brev.info.id, lagret, frigiReservasjon = true)).isSuccess()
        assertThat(hentBrevInfo(brev.info.id).resultOrFail().redigeresAv).isNull()
    }

    @Test
    suspend fun `attestanten kan ikke lagre vedlegg paa et informasjonsbrev`() {
        stagMal(1 to "Foerste")
        val brev = opprettBrev().resultOrFail()

        assertThat(lagreVedleggSomAttestant(brev.info.id, malVedlegg(1 to "Foerste").toEdit()))
            .isFailure<AttesterBrevPolicy.KanIkkeAttestere.KanIkkeAttestereInformasjonsbrev, _, _>()
    }

    @Test
    suspend fun `henting av vedleggstitler for attestering merger ikke lagrede vedlegg`() {
        val brev = klartVedtaksbrevMedRedigertVedlegg()
        stagMal(1 to "Foerste", 2 to "Andre", tittel = "Ny maltittel")

        assertThat(hentVedleggstitlerForAttestering(brev.info.id)).isSuccess {
            assertThat(it).hasSize(1)
            assertThat(it.first().tittel).isEqualTo("Mal tittel")
        }
        assertThat(lagretVedlegg(brev.info.id).blocks.map { it.id }).containsExactly(1)
    }

    @Test
    suspend fun `henting av brev for attestering merger ikke lagrede vedlegg`() {
        val brev = klartVedtaksbrevMedRedigertVedlegg()
        stagMal(1 to "Foerste", 2 to "Andre")

        assertThat(hentBrevAttestering(brev.info.id, reserverForRedigering = true)).isSuccess()

        assertThat(lagretVedlegg(brev.info.id).blocks.map { it.id }).containsExactly(1)
        assertThat(lagretVedlegg(brev.info.id).foersteLiteral().editedText).isEqualTo("Redigert")
    }

    @Test
    suspend fun `attestering merger ikke lagrede vedlegg`() {
        val brev = klartVedtaksbrevMedRedigertVedlegg()
        stagMal(1 to "Foerste", 2 to "Andre")

        assertThat(attester(brev)).isSuccess()

        assertThat(lagretVedlegg(brev.info.id).blocks.map { it.id }).containsExactly(1)
        assertThat(lagretVedlegg(brev.info.id).foersteLiteral().editedText).isEqualTo("Redigert")
    }

    @Test
    suspend fun `attestering-pdf varsler ikke om utdatert vedlegg`() {
        val brev = klartVedtaksbrevMedRedigertVedlegg()
        assertThat(hentEllerOpprettAttesteringPdf(brev)).isSuccess()

        stagMal(1 to "Foerste", 2 to "Andre")
        penService.pesysBrevdata = brevdataResponseData.copy(brevdata = Api.GeneriskBrevdata().also { it["a"] = "b" })

        assertThat(hentEllerOpprettAttesteringPdf(brev)).isSuccess {
            assertThat(it.rendretBrevErEndret).isFalse()
        }
    }
}
