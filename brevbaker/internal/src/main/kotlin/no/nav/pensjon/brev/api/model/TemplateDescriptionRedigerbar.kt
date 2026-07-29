package no.nav.pensjon.brev.api.model

import no.nav.brev.InternKonstruktoer
import no.nav.pensjon.brev.api.model.TemplateDescription.IBrevkategori
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import java.util.Objects

class RedigerbarTemplateDescription(
    override val name: String,
    override val letterDataClass: String,
    override val languages: List<LanguageCode>,
    override val metadata: LetterMetadata,
    val kategori: Brevkategori,
    val brevkontekst: TemplateDescription.Brevkontekst,
    val sakstyper: Set<Sakstype>,
): TemplateDescription {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is RedigerbarTemplateDescription) return false
        return name == other.name && letterDataClass == other.letterDataClass && languages == other.languages && metadata == other.metadata && kategori == other.kategori && brevkontekst == other.brevkontekst && sakstyper == other.sakstyper
    }

    override fun hashCode() = Objects.hash(name, letterDataClass, languages, metadata, kategori, brevkontekst, sakstyper)
    override fun toString(): String =
        "RedigerbarTemplateDescription(name='$name', letterDataClass='$letterDataClass', languages=$languages, metadata=$metadata, kategori=$kategori, brevkontekst=$brevkontekst, sakstyper=$sakstyper)"

    @JvmInline
    value class Brevkategori @InternKonstruktoer constructor(override val kode: String) : IBrevkategori {
        override fun toString() = kode
    }

    @JvmInline
    value class Sakstype @InternKonstruktoer constructor(val kode: String) {
        override fun toString() = kode
    }
}
