package no.nav.pensjon.brev.planleggepensjon

import no.nav.brev.brevbaker.FeatureToggleDummy
import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.LetterTestRenderer
import no.nav.pensjon.brev.api.model.FeatureToggleSingleton
import no.nav.pensjon.brev.planleggepensjon.simulering.AfpOffentligLivsvarigSimulering
import no.nav.pensjon.brev.planleggepensjon.simulering.ApSimuleringBrev
import no.nav.pensjon.brev.planleggepensjon.simulering.LivsvarigOffentligAfp
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.Block
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

class LivsvarigOffentligAfpTest {
    private val tittel = "Livsvarig AFP i offentlig sektor"
    private val beskrivelse = "AFP-beløpet er hentet fra <Statens pensjonskasse>."

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `viser bare tittel og avsnitt uten AFP-tabellgrunnlag`(medApTabeller: Boolean) {
        val blocks = render(AfpOffentligLivsvarigSimulering(beskrivelse = beskrivelse), medApTabeller)

        assertEquals(1, blocks.filterIsInstance<Block.Title3>().count { it.content.text() == tittel })
        val tittelIndex = blocks.indexOfFirst { it is Block.Title3 && it.content.text() == tittel }
        val avsnitt = assertInstanceOf(Block.Paragraph::class.java, blocks[tittelIndex + 1])
        assertEquals(beskrivelse, avsnitt.content.filterIsInstance<ParagraphContent.Text>().text())
        assertTrue(tables(blocks).none { it.header.colSpec.any { column -> "AFP" in column.headerContent.text.text() } })
    }

    @ParameterizedTest
    @ValueSource(ints = [0, 8347])
    fun `viser tabell og frontendtekst ved helt uttak ogsaa med null kroner`(maanedligBeloep: Int) {
        val blocks = render(
            AfpOffentligLivsvarigSimulering(
                vedHeltUttak = LivsvarigOffentligAfp(65, Kroner(maanedligBeloep * 12), Kroner(maanedligBeloep)),
                beskrivelse = beskrivelse,
            ),
        )

        val afpTabell = tables(blocks).single { it.header.colSpec.first().headerContent.text.text() == "AFP i offentlig sektor" }
        assertEquals("Kr per måned", afpTabell.header.colSpec[1].headerContent.text.text())
        assertEquals("Livsvarig AFP", afpTabell.rows[0].cells[0].text.text())
        assertEquals(maanedligBeloep.toString(), afpTabell.rows[0].cells[1].text.text().filter(Char::isDigit))
        assertEquals("Sum AFP", afpTabell.rows[1].cells[0].text.text())
        assertTrue(afpTabell.rows[1].cells[0].text.all { it.fontType == ParagraphContent.Text.FontType.BOLD })

        val tittelIndex = blocks.indexOfFirst { it is Block.Title3 && it.content.text() == tittel }
        val tabellAvsnitt = assertInstanceOf(Block.Paragraph::class.java, blocks[tittelIndex + 1])
        assertEquals(afpTabell, tabellAvsnitt.content.single())
        val beskrivelseAvsnitt = assertInstanceOf(Block.Paragraph::class.java, blocks[tittelIndex + 2])
        assertEquals(beskrivelse, beskrivelseAvsnitt.content.filterIsInstance<ParagraphContent.Text>().text())
        assertEquals(1, tables(blocks).count { it.header.colSpec.first().headerContent.text.text() == "Alderspensjon og AFP" })
    }

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `viser bare en AFP-tabell naar baade gradert og helt uttak finnes`(medApTabeller: Boolean) {
        val afp = requireNotNull(Fixtures.createBrevDtoMedAfpOffentligLivsvarig().pesysData.simulering.afpOffentligLivsvarig)
        val blocks = render(afp.copy(beskrivelse = beskrivelse), medApTabeller)
        val afpTabeller = tables(blocks).filter { it.header.colSpec.first().headerContent.text.text() == "AFP i offentlig sektor" }

        assertEquals(1, blocks.filterIsInstance<Block.Title3>().count { it.content.text() == tittel })
        assertEquals(listOf("4000"), afpTabeller.map { it.rows[0].cells[1].text.text().filter(Char::isDigit) })
        assertEquals(1, blocks.filterIsInstance<Block.Paragraph>().count {
            it.content.filterIsInstance<ParagraphContent.Text>().text() == beskrivelse
        })
        assertEquals(if (medApTabeller) 2 else 0, tables(blocks).count {
            it.header.colSpec.first().headerContent.text.text() == "Alderspensjon og AFP"
        })
    }

    @Test
    fun `viser en AFP-tabell med gradert beloep naar helt uttak mangler`() {
        val afp = requireNotNull(Fixtures.createBrevDtoMedAfpOffentligLivsvarig().pesysData.simulering.afpOffentligLivsvarig)
        val blocks = render(afp.copy(vedHeltUttak = null, beskrivelse = beskrivelse))
        val afpTabell = tables(blocks).single { it.header.colSpec.first().headerContent.text.text() == "AFP i offentlig sektor" }

        assertEquals("3000", afpTabell.rows[0].cells[1].text.text().filter(Char::isDigit))
        assertEquals(1, blocks.filterIsInstance<Block.Title3>().count { it.content.text() == tittel })
        assertEquals(1, blocks.filterIsInstance<Block.Paragraph>().count {
            it.content.filterIsInstance<ParagraphContent.Text>().text() == beskrivelse
        })
    }

    @Test
    fun `viser en AFP-tabell naar eksisterende klient ikke sender beskrivelse`() {
        val afp = requireNotNull(Fixtures.createBrevDtoMedAfpOffentligLivsvarig().pesysData.simulering.afpOffentligLivsvarig)
        val blocks = render(afp)

        assertEquals(1, tables(blocks).count { it.header.colSpec.first().headerContent.text.text() == "AFP i offentlig sektor" })
        assertTrue(blocks.filterIsInstance<Block.Paragraph>().none {
            it.content.filterIsInstance<ParagraphContent.Text>().text() == beskrivelse
        })
    }

    @Test
    fun `viser ikke seksjonen uten livsvarig AFP`() {
        val blocks = render(null)

        assertTrue(blocks.filterIsInstance<Block.Title3>().none { it.content.text() == tittel })
        assertTrue(tables(blocks).none { it.header.colSpec.any { column -> "AFP" in column.headerContent.text.text() } })
    }

    private fun render(afp: AfpOffentligLivsvarigSimulering?, medApTabeller: Boolean = true): List<Block> {
        FeatureToggleSingleton.init(FeatureToggleDummy)
        val brev = Fixtures.createBrevDtoMedAfpOffentligLivsvarig()
        val data = brev.pesysData.copy(
            simulering = brev.pesysData.simulering.copy(
                afpOffentligLivsvarig = afp,
                maanedligAlderspensjonForKnekkpunkter =
                    if (medApTabeller) brev.pesysData.simulering.maanedligAlderspensjonForKnekkpunkter else null,
            ),
        )
        return LetterTestRenderer.render(
            LetterTestImpl(ApSimuleringBrev.template, brev.copy(pesysData = data), Language.Bokmal, Fixtures.felles),
        ).attachments.first().blocks
    }

    private fun tables(blocks: List<Block>): List<ParagraphContent.Table> =
        blocks.filterIsInstance<Block.Paragraph>().flatMap { it.content }.filterIsInstance<ParagraphContent.Table>()

    private fun List<ParagraphContent.Text>.text(): String = joinToString("") { it.text }
}
