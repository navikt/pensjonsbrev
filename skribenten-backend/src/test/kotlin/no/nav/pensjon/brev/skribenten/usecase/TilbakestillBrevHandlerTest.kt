package no.nav.pensjon.brev.skribenten.usecase

import no.nav.pensjon.brev.skribenten.domain.BrevmalFinnesIkke
import no.nav.pensjon.brev.skribenten.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.isFailure
import no.nav.pensjon.brev.skribenten.isSuccess
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.letter.Edit.Block.Paragraph
import no.nav.pensjon.brev.skribenten.letter.Edit.ParagraphContent.Text.Literal
import no.nav.pensjon.brev.skribenten.letter.toEdit
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brevbaker.api.model.TemplateModelSpecification
import no.nav.pensjon.brevbaker.api.model.TemplateModelSpecification.FieldType
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class TilbakestillBrevHandlerTest : BrevredigeringTest() {

    @Test
    suspend fun `kan tilbakestille brev`() {
        val saksbehandlerValg = Api.GeneriskBrevdata().apply {
            put("ytelse", "uføre")
            put("inkluderAfpTekst", false)
        }
        val brev = opprettBrev(saksbehandlerValg = saksbehandlerValg).resultOrFail()

        // Oppdater brevet
        oppdaterBrev(
            brevId = brev.info.id,
            nyeSaksbehandlerValg = Api.GeneriskBrevdata().apply {
                put("ytelse", "uføre")
                put("inkluderAfpTekst", true)
                put("land", "Spania")
            },
            nyttRedigertbrev = brev.redigertBrev.copy(
                blocks = brev.redigertBrev.blocks + Paragraph(
                    2,
                    true,
                    listOf(Literal(1, "original text", Edit.ParagraphContent.Text.FontType.PLAIN, "and blue pill"))
                )
            ),
        ).resultOrFail()

        // Set up model specification for tilbakestill
        brevbakerService.modelSpecificationResultat = TemplateModelSpecification(
            types = mapOf(
                "BrevData1" to mapOf(
                    "saksbehandlerValg" to FieldType.Object(false, "SaksbehandlerValg1"),
                ),
                "SaksbehandlerValg1" to mapOf(
                    "ytelse" to FieldType.Scalar(false, FieldType.Scalar.Kind.STRING),
                    "land" to FieldType.Scalar(true, FieldType.Scalar.Kind.STRING),
                    "inkluderAfpTekst" to FieldType.Scalar(false, FieldType.Scalar.Kind.BOOLEAN),
                ),
            ),
            letterModelTypeName = "BrevData1",
        )

        val tilbakestilt = tilbakestillBrev(brevId = brev.info.id)

        assertThat(tilbakestilt).isSuccess {
            assertThat(it.redigertBrev).isEqualTo(letter.toEdit())
            assertThat(it.saksbehandlerValg).isEqualTo(Api.GeneriskBrevdata().apply {
                // Kun booleans og nullables blir tilbakestilt, så dermed forventes ytelse å fortsatt være "uføre" og land å være null
                put("ytelse", "uføre")
                put("inkluderAfpTekst", false)
                put("land", null)
            })
        }
    }

    @Test
    suspend fun `kan ikke tilbakestille brev som ikke eksisterer`() {
        val tilbakestilt = tilbakestillBrev(brevId = BrevId(1099))
        assertThat(tilbakestilt).isNull()
    }

    @Test
    suspend fun `tilbakestill feiler når model specification ikke finnes`() {
        val brev = opprettBrev().resultOrFail()
        brevbakerService.modelSpecificationResultat = null

        val tilbakestilt = tilbakestillBrev(brevId = brev.info.id)

        assertThat(tilbakestilt).isFailure<BrevmalFinnesIkke, _, _>()
    }

    @Test
    suspend fun `kan ikke tilbakestille brev som redigeres av andre`() {
        val brev = opprettBrev(reserverForRedigering = true).resultOrFail()

        val tilbakestilt = tilbakestillBrev(brevId = brev.info.id, principal = saksbehandler2Principal)

        assertThat(tilbakestilt).isFailure<BrevreservasjonPolicy.ReservertAvAnnen, _, _> {
            assertThat(it.eksisterende.reservertAv).isEqualTo(saksbehandler1Principal.navIdent)
        }
    }

    private suspend fun tilbakestillBrev(
        brevId: BrevId,
        principal: no.nav.pensjon.brev.skribenten.auth.UserPrincipal = saksbehandler1Principal,
    ) = no.nav.pensjon.brev.skribenten.auth.withPrincipal(principal) {
        brevredigeringFacade.tilbakestillBrev(TilbakestillBrevHandler.Request(brevId = brevId))
    }
}