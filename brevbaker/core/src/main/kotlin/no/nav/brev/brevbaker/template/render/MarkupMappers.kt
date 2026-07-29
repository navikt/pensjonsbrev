package no.nav.brev.brevbaker.template.render

import no.nav.brev.brevbaker.markup.Markup
import no.nav.brev.brevbaker.markup.outline.Block
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Text.FontType
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

internal fun LanguageCode.toMarkup(): Markup.Spraak = when (this) {
    LanguageCode.BOKMAL -> Markup.Spraak.BOKMAL
    LanguageCode.NYNORSK -> Markup.Spraak.NYNORSK
    LanguageCode.ENGLISH -> Markup.Spraak.ENGLISH
}

internal fun LetterMetadata.Brevtype.toMarkup(): Markup.Brevtype = when (this) {
    LetterMetadata.Brevtype.VEDTAKSBREV -> Markup.Brevtype.VEDTAKSBREV
    LetterMetadata.Brevtype.INFORMASJONSBREV -> Markup.Brevtype.INFORMASJONSBREV
}

internal fun ParagraphContent.Text.FontType.toMarkup(): FontType =
    when (this) {
        ParagraphContent.Text.FontType.PLAIN -> FontType.PLAIN
        ParagraphContent.Text.FontType.BOLD -> FontType.BOLD
        ParagraphContent.Text.FontType.ITALIC -> FontType.ITALIC
    }

internal fun ParagraphContent.Form.Text.Size.toMarkup(): Block.FormText.Size =
    when (this) {
        ParagraphContent.Form.Text.Size.NONE -> Block.FormText.Size.NONE
        ParagraphContent.Form.Text.Size.SHORT -> Block.FormText.Size.SHORT
        ParagraphContent.Form.Text.Size.LONG -> Block.FormText.Size.LONG
        ParagraphContent.Form.Text.Size.FILL -> Block.FormText.Size.FILL
    }

internal fun ParagraphContent.Table.ColumnAlignment.toMarkup(): Block.Table.ColumnAlignment =
    when (this) {
        ParagraphContent.Table.ColumnAlignment.LEFT -> Block.Table.ColumnAlignment.LEFT
        ParagraphContent.Table.ColumnAlignment.RIGHT -> Block.Table.ColumnAlignment.RIGHT
    }
