package no.nav.pensjon.brev.skribenten.brevredigering.domain

import no.nav.pensjon.brev.skribenten.letter.Edit
import no.nav.pensjon.brev.skribenten.letter.v2.EditV2

fun VersjonertBrev.asV1(): Edit.Letter = (this as VersjonertBrev.V1).brev

fun VersjonertBrev.asV2(): EditV2.Letter = (this as VersjonertBrev.V2).brev

fun VersjonertBrev.blocks(): List<Any> = when (this) {
    is VersjonertBrev.V1 -> brev.blocks
    is VersjonertBrev.V2 -> brev.blocks
}

fun VersjonertBrev.title(): Any = when (this) {
    is VersjonertBrev.V1 -> brev.title
    is VersjonertBrev.V2 -> brev.title
}

fun VersjonertBrev.gjelderNavn(): String = when (this) {
    is VersjonertBrev.V1 -> brev.sakspart.gjelderNavn
    is VersjonertBrev.V2 -> brev.saksinformasjon.gjelderNavn
}

fun VersjonertBrev.navAvsenderEnhet(): String? = when (this) {
    is VersjonertBrev.V1 -> brev.signatur.navAvsenderEnhet
    is VersjonertBrev.V2 -> brev.signatur.navAvsenderEnhet
}

fun VersjonertBrev.medEndretFoersteLiteral(nyTekst: String): VersjonertBrev = when (this) {
    is VersjonertBrev.V1 -> {
        val blokk = brev.blocks.first() as Edit.Block.Paragraph
        val literal = blokk.content.first() as Edit.ParagraphContent.Text.Literal
        VersjonertBrev.V1(brev.copy(blocks = listOf(blokk.copy(content = listOf(literal.copy(editedText = nyTekst)))) + brev.blocks.drop(1)))
    }
    is VersjonertBrev.V2 -> {
        val blokk = brev.blocks.first() as EditV2.Block.Paragraph
        val literal = blokk.content.first() as EditV2.Text.Literal
        VersjonertBrev.V2(brev.copy(blocks = listOf(blokk.copy(content = listOf(literal.copy(editedText = nyTekst)))) + brev.blocks.drop(1)))
    }
}

fun VersjonertVedlegg.blockIds(): List<Int?> = when (this) {
    is VersjonertVedlegg.V1 -> vedlegg.blocks.map { it.id }
    is VersjonertVedlegg.V2 -> vedlegg.blocks.map { it.id }
}

fun VersjonertVedlegg.foersteLiteralEditedText(): String? = when (this) {
    is VersjonertVedlegg.V1 -> ((vedlegg.blocks.first() as Edit.Block.Paragraph).content.first() as Edit.ParagraphContent.Text.Literal).editedText
    is VersjonertVedlegg.V2 -> ((vedlegg.blocks.first() as EditV2.Block.Paragraph).content.first() as EditV2.Text.Literal).editedText
}

fun VersjonertVedlegg.medRedigertTekst(tekst: String): VersjonertVedlegg = when (this) {
    is VersjonertVedlegg.V1 -> {
        val blokk = vedlegg.blocks.first() as Edit.Block.Paragraph
        val literal = blokk.content.first() as Edit.ParagraphContent.Text.Literal
        VersjonertVedlegg.V1(vedlegg.copy(blocks = listOf(blokk.copy(content = listOf(literal.copy(editedText = tekst)))) + vedlegg.blocks.drop(1)))
    }
    is VersjonertVedlegg.V2 -> {
        val blokk = vedlegg.blocks.first() as EditV2.Block.Paragraph
        val literal = blokk.content.first() as EditV2.Text.Literal
        VersjonertVedlegg.V2(vedlegg.copy(blocks = listOf(blokk.copy(content = listOf(literal.copy(editedText = tekst)))) + vedlegg.blocks.drop(1)))
    }
}
