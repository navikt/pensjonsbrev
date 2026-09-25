package no.nav.brev.brevbaker.markup

import no.nav.brev.brevbaker.markup.outline.Block
import no.nav.brev.brevbaker.markup.outline.Text
import java.time.LocalDate
import java.util.Objects

class LetterMarkup internal constructor(
    val title1: List<Text>,
    val saksinformasjon: Saksinformasjon,
    val blocks: List<Block>,
    val signatur: Signatur,
    val version: Int = VERSION,
) {
    companion object {
        const val VERSION = 2
    }

    fun clean(): LetterMarkup = LetterMarkup(
        title1 = this.title1,
        saksinformasjon = this.saksinformasjon,
        blocks = this.blocks.cleanBlocks(),
        signatur = this.signatur,
        version = this.version
    )

    override fun equals(other: Any?) = other is LetterMarkup &&
            title1 == other.title1 &&
            saksinformasjon == other.saksinformasjon &&
            blocks == other.blocks &&
            signatur == other.signatur &&
            version == other.version

    override fun hashCode() = Objects.hash(title1, saksinformasjon, blocks, signatur, version)
    override fun toString(): String =
        "LetterMarkup(title1=$title1, saksinformasjon=$saksinformasjon, blocks=$blocks, signatur=$signatur, version=$version)"
}

class Attachment internal constructor(
    val title1: List<Text>,
    val blocks: List<Block>,
    val inkluderSaksinformasjon: Boolean,
) {
    fun clean() = Attachment(
        title1 = this.title1,
        blocks = this.blocks.cleanBlocks(),
        inkluderSaksinformasjon = this.inkluderSaksinformasjon
    )

    override fun equals(other: Any?): Boolean = other is Attachment && title1 == other.title1 &&
            blocks == other.blocks &&
            inkluderSaksinformasjon == other.inkluderSaksinformasjon

    override fun hashCode() = Objects.hash(title1, blocks, inkluderSaksinformasjon)
    override fun toString(): String =
        "Attachment(title1=$title1, blocks=$blocks, inkluderSaksinformasjon=$inkluderSaksinformasjon)"
}

class Saksinformasjon internal constructor(
    val gjelderNavn: String,
    val gjelderPersonidentifikator: Markup.Personidentifikator,
    val annenMottakerNavn: String?,
    val saksnummer: Markup.Saksnummer,
    val dokumentDato: LocalDate,
) {
    override fun equals(other: Any?): Boolean = other is Saksinformasjon && gjelderNavn == other.gjelderNavn &&
            gjelderPersonidentifikator == other.gjelderPersonidentifikator &&
            annenMottakerNavn == other.annenMottakerNavn &&
            saksnummer == other.saksnummer &&
            dokumentDato == other.dokumentDato

    override fun hashCode() =
        Objects.hash(gjelderNavn, gjelderPersonidentifikator, annenMottakerNavn, saksnummer, dokumentDato)

    override fun toString(): String =
        "Saksinformasjon(gjelderNavn=$gjelderNavn, gjelderPersonidentifikator=$gjelderPersonidentifikator, annenMottakerNavn=$annenMottakerNavn, saksnummer=$saksnummer, dokumentDato=$dokumentDato)"
}

class Signatur internal constructor(
    val saksbehandlerSignatur: SaksbehandlerSignatur?,
    val navAvsenderEnhet: String,
) {
    override fun equals(other: Any?): Boolean =
        other is Signatur && saksbehandlerSignatur == other.saksbehandlerSignatur &&
                navAvsenderEnhet == other.navAvsenderEnhet

    override fun hashCode() = Objects.hash(saksbehandlerSignatur, navAvsenderEnhet)
    override fun toString(): String =
        "Signatur(saksbehandlerSignatur=$saksbehandlerSignatur, navAvsenderEnhet=$navAvsenderEnhet)"
}

class SaksbehandlerSignatur internal constructor(
    val saksbehandlerNavn: String,
    val attesterendeSaksbehandlerNavn: String?,
) {
    override fun equals(other: Any?): Boolean =
        other is SaksbehandlerSignatur && saksbehandlerNavn == other.saksbehandlerNavn &&
                attesterendeSaksbehandlerNavn == other.attesterendeSaksbehandlerNavn

    override fun hashCode() = Objects.hash(saksbehandlerNavn, attesterendeSaksbehandlerNavn)
    override fun toString(): String =
        "SaksbehandlerSignatur(saksbehandlerNavn=$saksbehandlerNavn, attesterendeSaksbehandlerNavn=$attesterendeSaksbehandlerNavn)"
}

class PDFTittel internal constructor(
    val title1: List<Text>,
) {
    override fun equals(other: Any?): Boolean = other is PDFTittel && title1 == other.title1
    override fun hashCode() = Objects.hash(title1)
    override fun toString(): String = "PDFTittel(title1=$title1)"
}
