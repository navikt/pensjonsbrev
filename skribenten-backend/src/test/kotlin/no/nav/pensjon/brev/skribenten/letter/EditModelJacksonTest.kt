package no.nav.pensjon.brev.skribenten.letter

import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import no.nav.pensjon.brev.skribenten.services.LetterMarkupModule
import no.nav.pensjon.brevbaker.api.model.LetterMarkupImpl
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import java.time.LocalDate

class EditModelJacksonTest {

    val objectMapper = jacksonObjectMapper().apply {
        registerModule(JavaTimeModule())
        registerModule(LetterMarkupModule)
        registerModule(Edit.JacksonModule)

        enable(SerializationFeature.INDENT_OUTPUT)
        disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
    }

    @Test
    fun `kan deserialisere gjeldende EditModel`() {
        val expected = editedLetter()
        val json = objectMapper.writeValueAsString(expected)
        val actual = objectMapper.readValue(json, Edit.Letter::class.java)

        Assertions.assertThat(actual).isEqualTo(expected)
    }

    // TODO: Følgende tester kan slettes når vi fjerner bakoverkompatibilitet for title som ren string
    val tittelSomStringJson = """
            {
              "title" : "En tittel",
              "sakspart" : {
                "gjelderNavn" : "Test Testeson",
                "gjelderFoedselsnummer" : "1234568910",
                "vergeNavn" : null,
                "saksnummer" : "1234",
                "dokumentDato" : "2025-08-12"
              },
              "blocks" : [ ],
              "signatur" : {
                "hilsenTekst" : "Med vennlig hilsen",
                "saksbehandlerRolleTekst" : "Saksbehandler",
                "saksbehandlerNavn" : "Kjersti Saksbehandler",
                "attesterendeSaksbehandlerNavn" : null,
                "navAvsenderEnhet" : "Nav Familie- og pensjonsytelser Porsgrunn"
              },
              "deletedBlocks" : [ ]
            }
        """.trimIndent()

    @Test
    fun `kan deserialisere gammel EditModel`() {
        val expected = editedLetter(dokumentDato = LocalDate.of(2025, 8, 12)).copy(
            title = Edit.Title(listOf(Edit.ParagraphContent.Text.Literal(null, "", editedText = "En tittel")))
        )

        val actual = objectMapper.readValue(tittelSomStringJson, Edit.Letter::class.java)

        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `konvertert string tittel vil ikke regnes som redigert og vil bli erstattet fra fersk rendering`() {
        val konvertertTittel = objectMapper.readValue(tittelSomStringJson, Edit.Letter::class.java)
        assertThat(konvertertTittel.title.deletedContent).isEmpty()
        assertThat(konvertertTittel.title.text).noneMatch { it.isEdited() }
        assertThat(konvertertTittel.title.text).noneMatch { it.isNew() }

        val nyTittelFraBrevbaker = listOf(
            LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl(
                100,
                "Ny tekst fra brevbaker "
            ),
            LetterMarkupImpl.ParagraphContentImpl.TextImpl.VariableImpl(
                101,
                "med variabel"
            ),
            LetterMarkupImpl.ParagraphContentImpl.TextImpl.LiteralImpl(
                102,
                " og mer tekst"
            ),
        )
        val mergetMedBrevbaker = konvertertTittel.updateEditedLetter(letter().copy(title = nyTittelFraBrevbaker))
        assertThat(mergetMedBrevbaker.title.text).isEqualTo(nyTittelFraBrevbaker.toEdit(null))
    }
}