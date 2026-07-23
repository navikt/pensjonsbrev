package no.nav.brev.brevbaker.markup

import kotlinx.serialization.Serializable
import no.nav.brev.brevbaker.markup.outline.Text

/**
 * Pakker inn felles enum- og value-typer i markup-API-et for å unngå navnekollisjoner hos konsumenter.
 */
object Markup {
    /** Et markup-element med en stabil identifikator. */
    interface Identifiable {
        val id: Int
    }

    /** Et markup-element som inneholder en sekvens av tekst-elementer. */
    interface TextContainer {
        val content: List<Text>
    }

    /** Språket brevet er skrevet på. Styrer bl.a. datoformatering og språkoppsett i pdf-bygger. */
    enum class Spraak {
        BOKMAL,
        NYNORSK,
        ENGLISH,
    }

    /** Hva slags brev markup-en representerer. */
    enum class Brevtype {
        VEDTAKSBREV,
        INFORMASJONSBREV,
    }

    @Serializable
    @JvmInline
    value class Personidentifikator internal constructor(val value: String) {
        override fun toString() = value
    }

    @Serializable
    @JvmInline
    value class Saksnummer internal constructor(val value: String) {
        override fun toString() = value
    }
}
