package no.nav.pensjon.brev.api.model

import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@Suppress("unused")
interface TemplateDescription {
    val name: String
    val letterDataClass: String
    val languages: List<LanguageCode>
    val metadata: LetterMetadata

    data class Autobrev(
        override val name: String,
        override val letterDataClass: String,
        override val languages: List<LanguageCode>,
        override val metadata: LetterMetadata
    ): TemplateDescription

    data class Redigerbar(
        override val name: String,
        override val letterDataClass: String,
        override val languages: List<LanguageCode>,
        override val metadata: LetterMetadata,
        val kategori: Brevkategori,
        val brevkontekst: Brevkontekst,
        val sakstyper: Set<Sakstype>,
    ): TemplateDescription

    enum class Brevkontekst { ALLE, SAK, VEDTAK }

    interface Brevkategori {
        fun kode(): String
    }
}
