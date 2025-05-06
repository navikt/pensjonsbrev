package no.nav.pensjon.brev.template.render.dsl

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.dsl.expression.select
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brev.template.render.dsl.ForEachViewTestSelectors.ListArgumentSelectors.liste
import no.nav.pensjon.brev.template.render.dsl.ForEachViewTestSelectors.ListArgumentSelectors.listeSelector
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class ForEachViewTest {

    data class ListArgument(val liste: List<String>)

    @Test
    fun `ForEach uses stableHashCode of items to assign id of Assigned-Expression`() {
        val template = outlineTestTemplate<ListArgument> {
            paragraph {
                forEach(liste) {
                    textExpr(Language.Bokmal to it)
                }
            }
        }

        val itemsExpr = Expression.FromScope.Argument<ListArgument>().select(listeSelector)
        val expectedNext = Expression.FromScope.Assigned<String>(itemsExpr.stableHashCode())

        val expected = ContentOrControlStructure.Content(
            Element.OutlineContent.Paragraph(
                listOf(
                    ContentOrControlStructure.ForEach(
                        itemsExpr,
                        listOf(
                            ContentOrControlStructure.Content(
                                Element.OutlineContent.ParagraphContent.Text.Expression.ByLanguage.create(Language.Bokmal to expectedNext)
                            ),
                        ),
                        expectedNext
                    )
                )
            )
        )

        assertEquals(
            expected,
            template.outline.first()
        )
    }

}