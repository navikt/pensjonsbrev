package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.auth.UserPrincipal
import no.nav.pensjon.brev.skribenten.auth.withPrincipal
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.db.DocumentTable
import no.nav.pensjon.brev.skribenten.brevredigering.domain.DocumentEntity
import no.nav.pensjon.brev.skribenten.isFailure
import no.nav.pensjon.brev.skribenten.isSuccess
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.letter.toMarkup
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brev.skribenten.model.Dto
import no.nav.pensjon.brev.skribenten.model.VedleggId
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.junit.jupiter.api.Test

class RedigertVedleggHandlerTest : BrevredigeringHandlerTestBase() {

    private fun attachment(tekst: String, tittel: String = "Vedlegg tittel"): Edit.Attachment =
        Edit.Attachment(
            title = Edit.Title(listOf(Edit.ParagraphContent.Text.Literal(id = null, text = tittel))),
            blocks = listOf(
                Edit.Block.Paragraph(
                    id = null,
                    editable = true,
                    content = listOf(Edit.ParagraphContent.Text.Literal(id = null, text = tekst)),
                )
            ),
            deletedBlocks = emptySet(),
            includeSakspart = false,
        )

    private suspend fun hentRedigerbareVedlegg(
        brevId: BrevId,
        principal: UserPrincipal = saksbehandler1Principal,
    ): Outcome<List<RedigerbartVedleggInfo>, BrevredigeringError>? =
        withPrincipal(principal) {
            brevredigeringFacade.hentRedigerbareVedlegg(
                HentRedigerbareVedleggHandler.Request(brevId = brevId)
            )
        }

    private suspend fun endreVedlegg(
        brevId: BrevId,
        vedleggId: String,
        vedlegg: Edit.Attachment,
        principal: UserPrincipal = saksbehandler1Principal,
    ): Outcome<Dto.Brevredigering, BrevredigeringError>? =
        withPrincipal(principal) {
            brevredigeringFacade.endreRedigertVedlegg(
                EndreRedigertVedleggHandler.Request(brevId = brevId, vedleggId = VedleggId(vedleggId), redigertVedlegg = vedlegg)
            )
        }

    private suspend fun hentVedlegg(
        brevId: BrevId,
        vedleggId: String,
        principal: UserPrincipal = saksbehandler1Principal,
    ): Outcome<Edit.Attachment, BrevredigeringError>? =
        withPrincipal(principal) {
            brevredigeringFacade.hentRedigertVedlegg(
                HentRedigertVedleggHandler.Request(brevId = brevId, vedleggId = VedleggId(vedleggId))
            )
        }

    private suspend fun slettVedlegg(
        brevId: BrevId,
        vedleggId: String,
        principal: UserPrincipal = saksbehandler1Principal,
    ): Outcome<Dto.Brevredigering, BrevredigeringError>? =
        withPrincipal(principal) {
            brevredigeringFacade.slettRedigertVedlegg(
                SlettRedigertVedleggHandler.Request(brevId = brevId, vedleggId = VedleggId(vedleggId))
            )
        }

    private fun antallDokumenter(brevId: BrevId): Int =
        transaction { DocumentEntity.find { DocumentTable.brevredigering eq brevId }.count().toInt() }

    @Test
    suspend fun `kan lagre og hente redigert vedlegg`() {
        val brev = opprettBrev().resultOrFail()
        val vedlegg = attachment("Overstyrt innhold")

        assertThat(endreVedlegg(brev.info.id, "vedlegg1", vedlegg)).isSuccess()
        assertThat(hentVedlegg(brev.info.id, "vedlegg1").resultOrFail()).isEqualTo(vedlegg)
    }

    @Test
    suspend fun `hent gir null naar vedlegg ikke er overstyrt`() {
        val brev = opprettBrev().resultOrFail()

        assertThat(hentVedlegg(brev.info.id, "finnesIkke")).isNull()
    }

    @Test
    suspend fun `hent uten overstyring gir vedlegget slik det produseres fra mal`() {
        val brev = opprettBrev().resultOrFail()
        brevbakerService.renderRedigerbareVedleggResultat = mapOf("vedlegg1" to attachment("Mal-innhold").toMarkup())

        val hentet = hentVedlegg(brev.info.id, "vedlegg1").resultOrFail()
        assertThat(hentet.toMarkup()).isEqualTo(attachment("Mal-innhold").toMarkup())
    }

    @Test
    suspend fun `kan overstyre flere vedlegg paa samme brev`() {
        val brev = opprettBrev().resultOrFail()
        val vedlegg1 = attachment("Innhold 1")
        val vedlegg2 = attachment("Innhold 2")

        assertThat(endreVedlegg(brev.info.id, "vedlegg1", vedlegg1)).isSuccess()
        assertThat(endreVedlegg(brev.info.id, "vedlegg2", vedlegg2)).isSuccess()

        assertThat(hentVedlegg(brev.info.id, "vedlegg1").resultOrFail()).isEqualTo(vedlegg1)
        assertThat(hentVedlegg(brev.info.id, "vedlegg2").resultOrFail()).isEqualTo(vedlegg2)
    }

    @Test
    suspend fun `oppdatering av eksisterende overstyring erstatter innholdet uten merging`() {
        val brev = opprettBrev().resultOrFail()

        assertThat(endreVedlegg(brev.info.id, "vedlegg1", attachment("Foerste"))).isSuccess()
        val oppdatert = attachment("Andre")
        assertThat(endreVedlegg(brev.info.id, "vedlegg1", oppdatert)).isSuccess()

        assertThat(hentVedlegg(brev.info.id, "vedlegg1").resultOrFail()).isEqualTo(oppdatert)
    }

    @Test
    suspend fun `kan slette overstyrt vedlegg`() {
        val brev = opprettBrev().resultOrFail()

        assertThat(endreVedlegg(brev.info.id, "vedlegg1", attachment("Innhold"))).isSuccess()
        assertThat(slettVedlegg(brev.info.id, "vedlegg1")).isSuccess()

        assertThat(hentVedlegg(brev.info.id, "vedlegg1")).isNull()
    }

    @Test
    suspend fun `lagring av vedlegg nullstiller dokumentet`() {
        val brev = opprettBrev().resultOrFail()
        assertThat(hentEllerOpprettPdf(brev)).isSuccess()
        assertThat(antallDokumenter(brev.info.id)).isEqualTo(1)

        assertThat(endreVedlegg(brev.info.id, "vedlegg1", attachment("Innhold"))).isSuccess()
        assertThat(antallDokumenter(brev.info.id)).isEqualTo(0)
    }

    @Test
    suspend fun `sletting av vedlegg nullstiller dokumentet`() {
        val brev = opprettBrev().resultOrFail()
        assertThat(endreVedlegg(brev.info.id, "vedlegg1", attachment("Innhold"))).isSuccess()
        assertThat(hentEllerOpprettPdf(brev)).isSuccess()
        assertThat(antallDokumenter(brev.info.id)).isEqualTo(1)

        assertThat(slettVedlegg(brev.info.id, "vedlegg1")).isSuccess()
        assertThat(antallDokumenter(brev.info.id)).isEqualTo(0)
    }

    @Test
    suspend fun `overstyrt vedlegg sendes til brevbaker ved rendring`() {
        val brev = opprettBrev().resultOrFail()
        val vedlegg = attachment("Overstyrt innhold")
        assertThat(endreVedlegg(brev.info.id, "vedlegg1", vedlegg)).isSuccess()

        brevbakerService.renderPdfRedigerteVedleggKall.clear()
        assertThat(hentEllerOpprettPdf(brev)).isSuccess()

        val sendteVedlegg = brevbakerService.renderPdfRedigerteVedleggKall.last()
        assertThat(sendteVedlegg).containsOnlyKeys(VedleggId("vedlegg1"))
        assertThat(sendteVedlegg.getValue(VedleggId("vedlegg1"))).isEqualTo(vedlegg.toMarkup())
    }

    @Test
    suspend fun `uten overstyring sendes ingen redigerte vedlegg til brevbaker`() {
        val brev = opprettBrev().resultOrFail()

        brevbakerService.renderPdfRedigerteVedleggKall.clear()
        assertThat(hentEllerOpprettPdf(brev)).isSuccess()

        assertThat(brevbakerService.renderPdfRedigerteVedleggKall.last()).isEmpty()
    }

    @Test
    suspend fun `lagring av uendret vedlegg beholder dokumentet`() {
        val brev = opprettBrev().resultOrFail()
        val vedlegg = attachment("Innhold")
        assertThat(endreVedlegg(brev.info.id, "vedlegg1", vedlegg)).isSuccess()
        assertThat(hentEllerOpprettPdf(brev)).isSuccess()
        assertThat(antallDokumenter(brev.info.id)).isEqualTo(1)

        assertThat(endreVedlegg(brev.info.id, "vedlegg1", vedlegg)).isSuccess()
        assertThat(antallDokumenter(brev.info.id)).isEqualTo(1)
    }

    @Test
    suspend fun `sletting av vedlegg som ikke er overstyrt beholder dokumentet`() {
        val brev = opprettBrev().resultOrFail()
        assertThat(hentEllerOpprettPdf(brev)).isSuccess()
        assertThat(antallDokumenter(brev.info.id)).isEqualTo(1)

        assertThat(slettVedlegg(brev.info.id, "finnesIkke")).isSuccess()
        assertThat(antallDokumenter(brev.info.id)).isEqualTo(1)
    }

    @Test
    suspend fun `kan ikke endre vedlegg for brev som redigeres av andre`() {
        val brev = opprettBrev(reserverForRedigering = true).resultOrFail()

        assertThat(endreVedlegg(brev.info.id, "vedlegg1", attachment("Innhold"), saksbehandler2Principal))
            .isFailure<BrevreservasjonPolicy.ReservertAvAnnen, _, _>()
    }

    @Test
    suspend fun `kan ikke slette vedlegg for brev som redigeres av andre`() {
        val brev = opprettBrev().resultOrFail()
        assertThat(endreVedlegg(brev.info.id, "vedlegg1", attachment("Innhold"))).isSuccess()

        val reservert = opprettBrev(reserverForRedigering = true).resultOrFail()
        assertThat(slettVedlegg(reservert.info.id, "vedlegg1", saksbehandler2Principal))
            .isFailure<BrevreservasjonPolicy.ReservertAvAnnen, _, _>()
    }

    @Test
    suspend fun `vedlegg slettes naar brevet slettes`() {
        val brev = opprettBrev().resultOrFail()
        assertThat(endreVedlegg(brev.info.id, "vedlegg1", attachment("Innhold"))).isSuccess()

        assertThat(slettBrev(brev)).isSuccess()

        assertThat(hentVedlegg(brev.info.id, "vedlegg1")).isNull()
    }

    @Test
    suspend fun `hentRedigerbareVedlegg bruker maltittelen naar vedlegget ikke er overstyrt`() {
        val brev = opprettBrev().resultOrFail()
        brevbakerService.renderRedigerbareVedleggResultat =
            mapOf("vedlegg1" to attachment("Mal-innhold", tittel = "Mal tittel").toMarkup())

        val info = hentRedigerbareVedlegg(brev.info.id).resultOrFail()

        assertThat(info).hasSize(1)
        assertThat(info.first().vedleggId).isEqualTo(VedleggId("vedlegg1"))
        assertThat(info.first().tittel).isEqualTo("Mal tittel")
    }

    @Test
    suspend fun `hentRedigerbareVedlegg returnerer den redigerte tittelen naar vedlegget er overstyrt`() {
        val brev = opprettBrev().resultOrFail()
        brevbakerService.renderRedigerbareVedleggResultat =
            mapOf("vedlegg1" to attachment("Mal-innhold", tittel = "Mal tittel").toMarkup())
        assertThat(endreVedlegg(brev.info.id, "vedlegg1", attachment("Redigert innhold", tittel = "Redigert tittel")))
            .isSuccess()

        val info = hentRedigerbareVedlegg(brev.info.id).resultOrFail()

        assertThat(info).hasSize(1)
        assertThat(info.first().vedleggId).isEqualTo(VedleggId("vedlegg1"))
        assertThat(info.first().tittel).isEqualTo("Redigert tittel")
    }
}
