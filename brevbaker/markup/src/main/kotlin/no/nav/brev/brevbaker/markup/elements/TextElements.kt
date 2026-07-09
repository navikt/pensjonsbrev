package no.nav.brev.brevbaker.markup.elements

import no.nav.brev.brevbaker.markup.ElementTags
import no.nav.brev.brevbaker.markup.outline.Text
import no.nav.brev.brevbaker.markup.outline.Text.FontType

/** Bygg en frittstående [Text.Literal]. */
fun literalElement(
    text: String,
    id: Int = 0,
    fontType: FontType = FontType.PLAIN,
    tags: Set<ElementTags> = emptySet(),
): Text.Literal = Text.Literal(id, text, fontType, tags)

/** Bygg en frittstående [Text.Variable]. */
fun variableElement(
    text: String,
    id: Int = 0,
    fontType: FontType = FontType.PLAIN,
    tags: Set<ElementTags> = emptySet(),
): Text.Variable = Text.Variable(id, text, fontType, tags)

/** Bygg en frittstående [Text.NewLine]. */
fun newLineElement(id: Int = 0): Text.NewLine = Text.NewLine(id)
