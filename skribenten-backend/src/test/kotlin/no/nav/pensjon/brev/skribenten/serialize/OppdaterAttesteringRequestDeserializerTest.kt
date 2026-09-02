package no.nav.pensjon.brev.skribenten.serialize

import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.letter.editedLetter
import no.nav.pensjon.brev.skribenten.model.Api
import no.nav.pensjon.brev.skribenten.skribentenServerJackson
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class OppdaterAttesteringRequestDeserializerTest {

    private val mapper = jacksonObjectMapper().skribentenServerJackson()

    private val redigertBrev = editedLetter(
        Edit.Block.Paragraph(
            id = 1,
            editable = true,
            content = listOf(Edit.ParagraphContent.Text.Literal(id = 1, text = "En tekst")),
        )
    )

    @Test
    fun `ignorerer saksbehandlerValg fra gamle klienter`() {
        val json = mapper.valueToTree<ObjectNode>(Api.OppdaterAttesteringRequest(redigertBrev)).apply {
            set<ObjectNode>("saksbehandlerValg", mapper.valueToTree(mapOf("etFelt" to "en verdi")))
        }

        val result = mapper.readValue<Api.OppdaterAttesteringRequest>(mapper.writeValueAsString(json))

        assertThat(result.redigertBrev).isEqualTo(redigertBrev)
    }
}
