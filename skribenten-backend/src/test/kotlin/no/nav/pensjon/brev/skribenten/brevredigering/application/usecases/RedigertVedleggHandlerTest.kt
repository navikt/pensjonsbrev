package no.nav.pensjon.brev.skribenten.brevredigering.application.usecases

import no.nav.pensjon.brev.skribenten.auth.UserPrincipal
import no.nav.pensjon.brev.skribenten.auth.withPrincipal
import no.nav.pensjon.brev.skribenten.brevredigering.application.RedigerbartVedleggInfo
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringEntity
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevredigeringError
import no.nav.pensjon.brev.skribenten.brevredigering.domain.BrevreservasjonPolicy
import no.nav.pensjon.brev.skribenten.brevredigering.domain.VedleggFinnesIkkeIMal
import no.nav.pensjon.brev.skribenten.common.Outcome
import no.nav.pensjon.brev.skribenten.db.DocumentTable
import no.nav.pensjon.brev.skribenten.brevredigering.domain.DocumentEntity
import no.nav.pensjon.brev.skribenten.isFailure
import no.nav.pensjon.brev.skribenten.isSuccess
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.letter.toEdit
import no.nav.pensjon.brev.skribenten.letter.toMarkup
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.model.BrevId
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.VedleggId
import no.nav.pensjon.brevbaker.api.model.LetterMarkup
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl
import no.nav.pensjon.brevbaker.api.model.TemplateModelSpecification
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.BlockImpl.ParagraphImpl
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.v1.core.eq
import org.jetbrains.exposed.v1.jdbc.transactions.transaction
import org.junit.jupiter.api.Test

class RedigertVedleggHandlerTest : BrevredigeringHandlerTestBase() {

    private fun attachment(
        tekst: String,
        tittel: String = "Vedlegg tittel",
        editedTekst: String? = null,
        editedTittel: String? = null,
    ): Edit.Attachment =
        Edit.Attachment(
            title = Edit.Title(listOf(Edit.ParagraphContent.Text.Literal(id = 1, text = tittel, editedText = editedTittel))),
            blocks = listOf(
                Edit.Block.Paragraph(
                    id = 2,
                    editable = true,
                    content = listOf(Edit.ParagraphContent.Text.Literal(id = 21, text = tekst, editedText = editedTekst, parentId = 2)),
                )
            ),
            deletedBlocks = emptySet(),
            includeSakspart = false,
        )

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

    private suspend fun hentRedigerbareVedlegg(
        brevId: BrevId,
        principal: UserPrincipal = saksbehandler1Principal,
    ): Outcome<List<RedigerbartVedleggInfo>, BrevredigeringError>? =
        withPrincipal(principal) {
            hentRedigerbareVedlegg(
                HentRedigerbareVedleggHandler.Request(brevId = brevId, saksId = sak1.saksId)
            )
        }

    private suspend fun endreVedlegg(
        brevId: BrevId,
        vedleggId: String,
        vedlegg: Edit.Attachment,
        principal: UserPrincipal = saksbehandler1Principal,
    ): Outcome<Edit.Attachment, BrevredigeringError>? =
        withPrincipal(principal) {
            endreRedigertVedlegg(
                EndreRedigertVedleggHandler.Request(brevId = brevId, saksId = sak1.saksId, vedleggId = VedleggId(vedleggId), redigertVedlegg = vedlegg)
            )
        }

    private suspend fun hentVedlegg(
        brevId: BrevId,
        vedleggId: String,
        principal: UserPrincipal = saksbehandler1Principal,
    ): Outcome<Edit.Attachment, BrevredigeringError>? =
        withPrincipal(principal) {
            hentRedigertVedlegg(
                HentRedigertVedleggHandler.Request(brevId = brevId, saksId = sak1.saksId, vedleggId = VedleggId(vedleggId))
            )
        }

    private suspend fun tilbakestillVedlegg(
        brevId: BrevId,
        vedleggId: String,
        principal: UserPrincipal = saksbehandler1Principal,
    ): Outcome<Edit.Attachment, BrevredigeringError>? =
        withPrincipal(principal) {
            tilbakestillRedigertVedlegg(
                TilbakestillRedigertVedleggHandler.Request(brevId = brevId, saksId = sak1.saksId, vedleggId = VedleggId(vedleggId))
            )
        }

    private suspend fun lagreRedigertVedlegg(brevId: BrevId, redigertTekst: String = "Redigert"): String {
        brevbakerService.renderRedigerbareVedleggResultat = mapOf(VedleggId("vedlegg1") to malVedlegg(1 to "Foerste"))
        val redigert = hentVedlegg(brevId, "vedlegg1").resultOrFail().medRedigertTekst(redigertTekst)
        assertThat(endreVedlegg(brevId, "vedlegg1", redigert)).isSuccess()
        return redigertTekst
    }

    private fun lagretVedlegg(brevId: BrevId): Edit.Attachment =
        transaction { BrevredigeringEntity[brevId].hentRedigertVedlegg(VedleggId("vedlegg1"))!! }

    private fun antallDokumenter(brevId: BrevId): Int =
        transaction { DocumentEntity.find { DocumentTable.brevredigering eq brevId }.count().toInt() }

    private fun answerWithEditedAttachmentResult(vararg vedlegg: Pair<String, Edit.Attachment>) {
        brevbakerService.renderRedigerbareVedleggResultat = vedlegg.associate { (vedleggId, attachment) ->
            VedleggId(vedleggId) to attachment.toMarkup()
        }
    }

    @Test
    suspend fun `kan lagre og hente redigert vedlegg`() {
        val brev = opprettBrev().resultOrFail()
        val vedlegg = attachment("Overstyrt innhold")
        answerWithEditedAttachmentResult("vedlegg1" to vedlegg)

        assertThat(endreVedlegg(brev.info.id, "vedlegg1", vedlegg)).isSuccess()
        assertThat(hentVedlegg(brev.info.id, "vedlegg1").resultOrFail()).isEqualTo(vedlegg)
    }

    @Test
    suspend fun `hent gir feil naar vedlegget ikke finnes i malen`() {
        val brev = opprettBrev().resultOrFail()

        assertThat(hentVedlegg(brev.info.id, "finnesIkke")).isFailure<VedleggFinnesIkkeIMal, _, _> {
            assertThat(it.vedleggId).isEqualTo(VedleggId("finnesIkke"))
        }
    }

    @Test
    suspend fun `hent uten overstyring gir vedlegget slik det produseres fra mal`() {
        val brev = opprettBrev().resultOrFail()
        brevbakerService.renderRedigerbareVedleggResultat = mapOf(VedleggId("vedlegg1") to attachment("Mal-innhold").toMarkup())

        val hentet = hentVedlegg(brev.info.id, "vedlegg1").resultOrFail()
        assertThat(hentet.toMarkup()).isEqualTo(attachment("Mal-innhold").toMarkup())
    }

    @Test
    suspend fun `kan overstyre flere vedlegg paa samme brev`() {
        val brev = opprettBrev().resultOrFail()
        val vedlegg1 = attachment("Innhold 1")
        val vedlegg2 = attachment("Innhold 2")
        answerWithEditedAttachmentResult("vedlegg1" to vedlegg1, "vedlegg2" to vedlegg2)

        assertThat(endreVedlegg(brev.info.id, "vedlegg1", vedlegg1)).isSuccess()
        assertThat(endreVedlegg(brev.info.id, "vedlegg2", vedlegg2)).isSuccess()

        assertThat(hentVedlegg(brev.info.id, "vedlegg1").resultOrFail()).isEqualTo(vedlegg1)
        assertThat(hentVedlegg(brev.info.id, "vedlegg2").resultOrFail()).isEqualTo(vedlegg2)
    }

    @Test
    suspend fun `oppdatering av vedlegg erstatter innholdet`() {
        val brev = opprettBrev().resultOrFail()

        answerWithEditedAttachmentResult("vedlegg1" to attachment("Foerste"))
        assertThat(endreVedlegg(brev.info.id, "vedlegg1", attachment("Foerste"))).isSuccess()

        val oppdatert = attachment("Andre")
        answerWithEditedAttachmentResult("vedlegg1" to oppdatert)
        assertThat(endreVedlegg(brev.info.id, "vedlegg1", oppdatert)).isSuccess()

        assertThat(hentVedlegg(brev.info.id, "vedlegg1").resultOrFail()).isEqualTo(oppdatert)
    }

    @Test
    suspend fun `lagring av vedlegg som ikke finnes i malen gir feil og lagrer ingenting`() {
        val brev = opprettBrev().resultOrFail()
        val vedlegg = attachment("Innhold")
        answerWithEditedAttachmentResult("vedlegg1" to vedlegg)
        assertThat(endreVedlegg(brev.info.id, "vedlegg1", vedlegg)).isSuccess()

        brevbakerService.renderRedigerbareVedleggResultat = emptyMap()

        assertThat(endreVedlegg(brev.info.id, "vedlegg1", vedlegg.medRedigertTekst("Ny tekst")))
            .isFailure<VedleggFinnesIkkeIMal, _, _>()

        answerWithEditedAttachmentResult("vedlegg1" to vedlegg)
        assertThat(hentVedlegg(brev.info.id, "vedlegg1").resultOrFail()).isEqualTo(vedlegg)
    }

    @Test
    suspend fun `lagring merger inn nye blokker fra malen og returnerer det sammenslaatte vedlegget`() {
        val brev = opprettBrev().resultOrFail()
        brevbakerService.renderRedigerbareVedleggResultat = mapOf(VedleggId("vedlegg1") to malVedlegg(1 to "Foerste"))
        val redigert = hentVedlegg(brev.info.id, "vedlegg1").resultOrFail()

        brevbakerService.renderRedigerbareVedleggResultat = mapOf(VedleggId("vedlegg1") to malVedlegg(1 to "Foerste", 2 to "Andre"))
        val sammenslaatt = endreVedlegg(brev.info.id, "vedlegg1", redigert).resultOrFail()

        assertThat(sammenslaatt.blocks.map { it.id }).containsExactly(1, 2)
        assertThat(hentVedlegg(brev.info.id, "vedlegg1").resultOrFail().blocks.map { it.id }).containsExactly(1, 2)
    }

    @Test
    suspend fun `lagring beholder saksbehandlers redigerte tekst ved merging`() {
        val brev = opprettBrev().resultOrFail()
        brevbakerService.renderRedigerbareVedleggResultat = mapOf(VedleggId("vedlegg1") to malVedlegg(1 to "Mal-tekst"))
        val redigert = hentVedlegg(brev.info.id, "vedlegg1").resultOrFail().medRedigertTekst("Saksbehandlers tekst")

        brevbakerService.renderRedigerbareVedleggResultat = mapOf(VedleggId("vedlegg1") to malVedlegg(1 to "Mal-tekst", 2 to "Andre"))
        val sammenslaatt = endreVedlegg(brev.info.id, "vedlegg1", redigert).resultOrFail()

        assertThat(sammenslaatt.blocks.map { it.id }).containsExactly(1, 2)
        assertThat(sammenslaatt.foersteLiteral().editedText).isEqualTo("Saksbehandlers tekst")
    }

    @Test
    suspend fun `henting merger inn endringer fra malen uten aa lagre dem`() {
        val brev = opprettBrev().resultOrFail()
        brevbakerService.renderRedigerbareVedleggResultat = mapOf(VedleggId("vedlegg1") to malVedlegg(1 to "Foerste"))
        val redigert = hentVedlegg(brev.info.id, "vedlegg1").resultOrFail().medRedigertTekst("Redigert")
        assertThat(endreVedlegg(brev.info.id, "vedlegg1", redigert)).isSuccess()

        brevbakerService.renderRedigerbareVedleggResultat = mapOf(VedleggId("vedlegg1") to malVedlegg(1 to "Foerste", 2 to "Andre"))
        assertThat(hentVedlegg(brev.info.id, "vedlegg1").resultOrFail().blocks.map { it.id }).containsExactly(1, 2)

        // Merging ved henting skal ikke persisteres
        brevbakerService.renderRedigerbareVedleggResultat = mapOf(VedleggId("vedlegg1") to malVedlegg(1 to "Foerste"))
        assertThat(hentVedlegg(brev.info.id, "vedlegg1").resultOrFail().blocks.map { it.id }).containsExactly(1)
    }

    @Test
    suspend fun `henting gir feil naar malen ikke lenger har vedlegget`() {
        val brev = opprettBrev().resultOrFail()
        val vedlegg = attachment("Innhold")
        answerWithEditedAttachmentResult("vedlegg1" to vedlegg)
        assertThat(endreVedlegg(brev.info.id, "vedlegg1", vedlegg)).isSuccess()

        brevbakerService.renderRedigerbareVedleggResultat = emptyMap()

        assertThat(hentVedlegg(brev.info.id, "vedlegg1")).isFailure<VedleggFinnesIkkeIMal, _, _>()
    }

    @Test
    suspend fun `tilbakestilling forkaster overstyringen`() {
        val brev = opprettBrev().resultOrFail()
        val malen = attachment("Innhold")
        answerWithEditedAttachmentResult("vedlegg1" to malen)

        assertThat(endreVedlegg(brev.info.id, "vedlegg1", malen.medRedigertTekst("Redigert"))).isSuccess()
        assertThat(tilbakestillVedlegg(brev.info.id, "vedlegg1")).isSuccess()

        assertThat(hentVedlegg(brev.info.id, "vedlegg1").resultOrFail()).isEqualTo(malen)
    }

    @Test
    suspend fun `tilbakestilling returnerer vedlegget slik malen produserer det naa`() {
        val brev = opprettBrev().resultOrFail()
        brevbakerService.renderRedigerbareVedleggResultat = mapOf(VedleggId("vedlegg1") to malVedlegg(1 to "Foerste"))
        val redigert = hentVedlegg(brev.info.id, "vedlegg1").resultOrFail().medRedigertTekst("Redigert")
        assertThat(endreVedlegg(brev.info.id, "vedlegg1", redigert)).isSuccess()

        brevbakerService.renderRedigerbareVedleggResultat = mapOf(VedleggId("vedlegg1") to malVedlegg(1 to "Foerste", 2 to "Andre"))
        val tilbakestilt = tilbakestillVedlegg(brev.info.id, "vedlegg1").resultOrFail()

        assertThat(tilbakestilt).isEqualTo(malVedlegg(1 to "Foerste", 2 to "Andre").toEdit())
        assertThat(tilbakestilt.foersteLiteral().editedText).isNull()
        assertThat(hentVedlegg(brev.info.id, "vedlegg1").resultOrFail()).isEqualTo(tilbakestilt)
    }

    @Test
    suspend fun `tilbakestilling gir feil naar vedlegget ikke finnes i malen`() {
        val brev = opprettBrev().resultOrFail()

        assertThat(tilbakestillVedlegg(brev.info.id, "finnesIkke")).isFailure<VedleggFinnesIkkeIMal, _, _> {
            assertThat(it.vedleggId).isEqualTo(VedleggId("finnesIkke"))
        }
    }

    @Test
    suspend fun `tilbakestilling beholder reservasjonen`() {
        val brev = opprettBrev().resultOrFail()
        val malen = attachment("Innhold")
        answerWithEditedAttachmentResult("vedlegg1" to malen)
        assertThat(endreVedlegg(brev.info.id, "vedlegg1", malen.medRedigertTekst("Redigert"))).isSuccess()

        assertThat(tilbakestillVedlegg(brev.info.id, "vedlegg1")).isSuccess()

        assertThat(hentBrevInfo(brev.info.id).resultOrFail().redigeresAv).isEqualTo(saksbehandler1Principal.navIdent)
    }

    @Test
    suspend fun `endring av redigert vedlegg foerer til ny rendring ved neste pdf-henting`() {
        val brev = opprettBrev().resultOrFail()
        assertThat(hentEllerOpprettPdf(brev)).isSuccess()
        val rendringerFoer = brevbakerService.renderPdfKall.size

        answerWithEditedAttachmentResult("vedlegg1" to attachment("Innhold"))
        assertThat(endreVedlegg(brev.info.id, "vedlegg1", attachment("Innhold"))).isSuccess()

        assertThat(hentEllerOpprettPdf(brev)).isSuccess()
        assertThat(brevbakerService.renderPdfKall.size).isGreaterThan(rendringerFoer)
    }

    @Test
    suspend fun `tilbakestilling av redigert vedlegg foerer til ny rendring ved neste pdf-henting`() {
        val brev = opprettBrev().resultOrFail()
        answerWithEditedAttachmentResult("vedlegg1" to attachment("Innhold"))
        assertThat(endreVedlegg(brev.info.id, "vedlegg1", attachment("Innhold"))).isSuccess()
        assertThat(hentEllerOpprettPdf(brev)).isSuccess()
        val rendringerFoer = brevbakerService.renderPdfKall.size

        assertThat(tilbakestillVedlegg(brev.info.id, "vedlegg1")).isSuccess()

        assertThat(hentEllerOpprettPdf(brev)).isSuccess()
        assertThat(brevbakerService.renderPdfKall.size).isGreaterThan(rendringerFoer)
    }

    @Test
    suspend fun `overstyrt vedlegg sendes til brevbaker ved rendring`() {
        val brev = opprettBrev().resultOrFail()
        val vedlegg = attachment("Overstyrt innhold")
        answerWithEditedAttachmentResult("vedlegg1" to vedlegg)
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
        answerWithEditedAttachmentResult("vedlegg1" to vedlegg)
        assertThat(endreVedlegg(brev.info.id, "vedlegg1", vedlegg)).isSuccess()
        assertThat(hentEllerOpprettPdf(brev)).isSuccess()
        assertThat(antallDokumenter(brev.info.id)).isEqualTo(1)

        assertThat(endreVedlegg(brev.info.id, "vedlegg1", vedlegg)).isSuccess()
        assertThat(antallDokumenter(brev.info.id)).isEqualTo(1)
    }

    @Test
    suspend fun `tilbakestilling av vedlegg som ikke er overstyrt beholder dokumentet`() {
        val brev = opprettBrev().resultOrFail()
        answerWithEditedAttachmentResult("vedlegg1" to attachment("Innhold"))
        assertThat(hentEllerOpprettPdf(brev)).isSuccess()
        assertThat(antallDokumenter(brev.info.id)).isEqualTo(1)

        assertThat(tilbakestillVedlegg(brev.info.id, "vedlegg1")).isSuccess()
        assertThat(antallDokumenter(brev.info.id)).isEqualTo(1)
    }

    @Test
    suspend fun `henting av brev for redigering merger inn malendringer i lagrede vedlegg`() {
        val brev = opprettBrev().resultOrFail()
        val redigert = lagreRedigertVedlegg(brev.info.id)

        brevbakerService.renderRedigerbareVedleggResultat = mapOf(VedleggId("vedlegg1") to malVedlegg(1 to "Foerste", 2 to "Andre"))
        assertThat(hentBrev(brev.info.id, reserverForRedigering = true)).isSuccess()

        assertThat(lagretVedlegg(brev.info.id).blocks.map { it.id }).containsExactly(1, 2)
        assertThat(lagretVedlegg(brev.info.id).foersteLiteral().editedText).isEqualTo(redigert)
    }

    @Test
    suspend fun `oppdatering av brev merger inn malendringer i lagrede vedlegg`() {
        val brev = opprettBrev().resultOrFail()
        lagreRedigertVedlegg(brev.info.id)

        brevbakerService.renderRedigerbareVedleggResultat = mapOf(VedleggId("vedlegg1") to malVedlegg(1 to "Foerste", 2 to "Andre"))
        assertThat(oppdaterBrev(brev.info.id)).isSuccess()

        assertThat(lagretVedlegg(brev.info.id).blocks.map { it.id }).containsExactly(1, 2)
    }

    @Test
    suspend fun `tilbakestilling av brev merger inn malendringer i lagrede vedlegg`() {
        val brev = opprettBrev().resultOrFail()
        lagreRedigertVedlegg(brev.info.id)

        brevbakerService.renderRedigerbareVedleggResultat = mapOf(VedleggId("vedlegg1") to malVedlegg(1 to "Foerste", 2 to "Andre"))
        brevbakerService.modelSpecificationResultat = TemplateModelSpecification(types = emptyMap(), letterModelTypeName = "BrevData1")
        assertThat(
            withPrincipal(saksbehandler1Principal) {
                tilbakestillBrev(TilbakestillBrevHandler.Request(brevId = brev.info.id, saksId = sak1.saksId))
            }
        ).isSuccess()

        assertThat(lagretVedlegg(brev.info.id).blocks.map { it.id }).containsExactly(1, 2)
    }

    @Test
    suspend fun `utdatert vedlegg gir beskjed om at det rendrede brevet er endret`() {
        val brev = opprettBrev().resultOrFail()
        lagreRedigertVedlegg(brev.info.id)
        assertThat(hentEllerOpprettPdf(brev)).isSuccess()

        brevbakerService.renderRedigerbareVedleggResultat = mapOf(VedleggId("vedlegg1") to malVedlegg(1 to "Foerste", 2 to "Andre"))
        penService.pesysBrevdata = brevdataResponseData.copy(brevdata = Api.GeneriskBrevdata().also { it["a"] = "b" })

        assertThat(hentEllerOpprettPdf(brev).resultOrFail().rendretBrevErEndret).isTrue()
    }

    @Test
    suspend fun `brev uten overstyrte vedlegg rendrer ingen vedlegg fra malen`() {
        val brev = opprettBrev().resultOrFail()
        brevbakerService.renderRedigerbareVedleggResultat = mapOf(VedleggId("vedlegg1") to malVedlegg(1 to "Foerste"))

        brevbakerService.renderRedigerbartVedleggKall.clear()
        assertThat(hentBrev(brev.info.id, reserverForRedigering = true)).isSuccess()
        assertThat(oppdaterBrev(brev.info.id)).isSuccess()
        assertThat(hentEllerOpprettPdf(brev)).isSuccess()

        assertThat(brevbakerService.renderRedigerbartVedleggKall).isEmpty()
    }

    @Test
    suspend fun `brev med overstyrt vedlegg rendrer kun det overstyrte vedlegget`() {
        val brev = opprettBrev().resultOrFail()
        lagreRedigertVedlegg(brev.info.id)
        brevbakerService.renderRedigerbareVedleggResultat = mapOf(
            VedleggId("vedlegg1") to malVedlegg(1 to "Foerste"),
            VedleggId("vedlegg2") to malVedlegg(1 to "Et annet vedlegg"),
        )

        brevbakerService.renderRedigerbartVedleggKall.clear()
        assertThat(oppdaterBrev(brev.info.id)).isSuccess()

        assertThat(brevbakerService.renderRedigerbartVedleggKall).containsExactly(VedleggId("vedlegg1"))
    }

    @Test
    suspend fun `kan ikke endre vedlegg for brev som redigeres av andre`() {
        val brev = opprettBrev(reserverForRedigering = true).resultOrFail()

        assertThat(endreVedlegg(brev.info.id, "vedlegg1", attachment("Innhold"), saksbehandler2Principal))
            .isFailure<BrevreservasjonPolicy.ReservertAvAnnen, _, _>()
    }

    @Test
    suspend fun `kan ikke tilbakestille vedlegg for brev som redigeres av andre`() {
        val brev = opprettBrev().resultOrFail()
        answerWithEditedAttachmentResult("vedlegg1" to attachment("Innhold"))
        assertThat(endreVedlegg(brev.info.id, "vedlegg1", attachment("Innhold"))).isSuccess()

        val reservert = opprettBrev(reserverForRedigering = true).resultOrFail()
        assertThat(tilbakestillVedlegg(reservert.info.id, "vedlegg1", saksbehandler2Principal))
            .isFailure<BrevreservasjonPolicy.ReservertAvAnnen, _, _>()
    }

    @Test
    suspend fun `vedlegg slettes naar brevet slettes`() {
        val brev = opprettBrev().resultOrFail()
        answerWithEditedAttachmentResult("vedlegg1" to attachment("Innhold"))
        assertThat(endreVedlegg(brev.info.id, "vedlegg1", attachment("Innhold"))).isSuccess()

        assertThat(slettBrev(brev)).isSuccess()

        assertThat(hentVedlegg(brev.info.id, "vedlegg1")).isNull()
    }

    @Test
    suspend fun `hentRedigerbareVedlegg bruker maltittelen naar vedlegget ikke er overstyrt`() {
        val brev = opprettBrev().resultOrFail()
        brevbakerService.renderRedigerbareVedleggResultat =
            mapOf(VedleggId("vedlegg1") to attachment("Mal-innhold", tittel = "Mal tittel").toMarkup())

        val info = hentRedigerbareVedlegg(brev.info.id).resultOrFail()

        assertThat(info).hasSize(1)
        assertThat(info.first().vedleggId).isEqualTo(VedleggId("vedlegg1"))
        assertThat(info.first().tittel).isEqualTo("Mal tittel")
    }

    @Test
    suspend fun `hentRedigerbareVedlegg returnerer den redigerte tittelen naar vedlegget er overstyrt`() {
        val brev = opprettBrev().resultOrFail()
        brevbakerService.renderRedigerbareVedleggResultat =
            mapOf(VedleggId("vedlegg1") to attachment("Mal-innhold", tittel = "Mal tittel").toMarkup())
        assertThat(endreVedlegg(brev.info.id, "vedlegg1", attachment("Mal-innhold", tittel = "Mal tittel", editedTittel = "Redigert tittel")))
            .isSuccess()

        val info = hentRedigerbareVedlegg(brev.info.id).resultOrFail()

        assertThat(info).hasSize(1)
        assertThat(info.first().vedleggId).isEqualTo(VedleggId("vedlegg1"))
        assertThat(info.first().tittel).isEqualTo("Redigert tittel")
    }

    @Test
    suspend fun `hentRedigerbareVedlegg bruker den ferske maltittelen naar det overstyrte vedlegget ikke har redigert tittel`() {
        val brev = opprettBrev().resultOrFail()
        brevbakerService.renderRedigerbareVedleggResultat =
            mapOf(VedleggId("vedlegg1") to attachment("Mal-innhold", tittel = "Gammel maltittel").toMarkup())
        assertThat(endreVedlegg(brev.info.id, "vedlegg1", attachment("Mal-innhold", tittel = "Gammel maltittel", editedTekst = "Redigert innhold")))
            .isSuccess()

        brevbakerService.renderRedigerbareVedleggResultat =
            mapOf(VedleggId("vedlegg1") to attachment("Mal-innhold", tittel = "Ny maltittel").toMarkup())

        val info = hentRedigerbareVedlegg(brev.info.id).resultOrFail()

        assertThat(info).hasSize(1)
        assertThat(info.first().tittel).isEqualTo("Ny maltittel")
    }

    @Test
    suspend fun `hentRedigerbareVedlegg returnerer tom liste og henter ikke brevdata naar malen ikke har redigerbare vedlegg`() {
        val brev = opprettBrev().resultOrFail()
        brevbakerService.harRedigerbareVedleggResultat = false

        penService.utfoerteHentPesysBrevdataKall.clear()
        val info = hentRedigerbareVedlegg(brev.info.id).resultOrFail()

        assertThat(info).isEmpty()
        assertThat(penService.utfoerteHentPesysBrevdataKall).isEmpty()
    }

    @Test
    suspend fun `hentRedigerbareVedlegg henter brevdata naar malen har redigerbare vedlegg`() {
        val brev = opprettBrev().resultOrFail()
        brevbakerService.harRedigerbareVedleggResultat = true
        brevbakerService.renderRedigerbareVedleggResultat =
            mapOf(VedleggId("vedlegg1") to attachment("Mal-innhold", tittel = "Mal tittel").toMarkup())

        penService.utfoerteHentPesysBrevdataKall.clear()
        val info = hentRedigerbareVedlegg(brev.info.id).resultOrFail()

        assertThat(info).hasSize(1)
        assertThat(penService.utfoerteHentPesysBrevdataKall).isNotEmpty()
    }
}
