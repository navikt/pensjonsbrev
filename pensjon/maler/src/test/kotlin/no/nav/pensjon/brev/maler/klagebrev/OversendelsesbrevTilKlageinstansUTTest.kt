package no.nav.pensjon.brev.maler.klagebrev

import no.nav.brev.brevbaker.LetterTestImpl
import no.nav.brev.brevbaker.LetterTestRenderer
import no.nav.brev.brevbaker.lagSaksbehandlervalg
import no.nav.brev.brevbaker.markup.LetterMarkup
import no.nav.brev.brevbaker.markup.Markup.TextContainer
import no.nav.brev.brevbaker.markup.outline.Block
import no.nav.pensjon.brev.Fixtures
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.maler.klagebrev.OversendelsesbrevTilKlageinstansUT.Forskriften
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.RedigerbarTemplate
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource

class OversendelsesbrevTilKlageinstansUTTest {

    @ParameterizedTest
    @MethodSource("forskrifter")
    fun `valgt forskrift renderer innhold uten tomme listepunkter`(forskrift: Forskriften) {
        val foelgebrevForskrift = OversendelseOgFoelgebrevKlageinstansUT.Forskriften.valueOf(forskrift.name)
        assertEquals(
            Forskriften.entries.map { it.name }.toSet(),
            OversendelseOgFoelgebrevKlageinstansUT.Forskriften.entries.map { it.name }.toSet(),
        )

        listOf(
            render(OversendelsesbrevTilKlageinstansUT, forskrift.name),
            render(OversendelseOgFoelgebrevKlageinstansUT, foelgebrevForskrift.name),
        ).forEach { markup ->
            assertSelectedBranchHasContent(markup, forskrift)
            assertNoEmptyListItems(markup, forskrift)
        }
    }

    private fun render(template: RedigerbarTemplate<EmptyRedigerbarBrevdata>, forskrift: String): LetterMarkup =
        LetterTestRenderer.renderLetterOnlyV2(
            LetterTestImpl(
                template.template,
                EmptyRedigerbarBrevdata(saksbehandlerValg = lagSaksbehandlervalg("forskrift" to forskrift)),
                Language.Bokmal,
                Fixtures.felles,
            )
        )

    private fun assertSelectedBranchHasContent(markup: LetterMarkup, forskrift: Forskriften) {
        val sectionStart = markup.blocks.indexOfFirst {
            it is Block.Title2 && it.content.joinToString("") { text -> text.text } == "Hva klagesaken gjelder"
        }
        val sectionEnd = markup.blocks.indexOfFirst {
            it is Block.Paragraph && it.content.joinToString("") { text -> text.text }
                .startsWith("Vedtaket opprettholdes")
        }

        assertTrue(sectionStart >= 0, "Section heading not rendered for $forskrift")
        assertTrue(sectionEnd > sectionStart, "Selected branch not rendered for $forskrift")
        assertTrue(
            markup.blocks.subList(sectionStart + 1, sectionEnd).any { block ->
                (block as? TextContainer)?.content?.any { it.text.isNotBlank() } == true
            },
            "Selected branch has no text content for $forskrift",
        )
    }

    private fun assertNoEmptyListItems(markup: LetterMarkup, forskrift: Forskriften) {
        val items = markup.blocks.flatMap { block ->
            when (block) {
                is Block.ItemList -> block.items
                is Block.NumberedList -> block.items
                else -> emptyList()
            }
        }

        assertTrue(
            items.all { item -> item.content.joinToString("") { it.text }.isNotBlank() },
            "Empty list item rendered for $forskrift",
        )
    }

    companion object {
        @JvmStatic
        fun forskrifter() = Forskriften.entries.map { Arguments.of(it) }
    }
}
