package no.nav.pensjon.brev.api.model

import no.nav.brev.InterneDataklasser
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkategori
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkontekst
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

object TemplateDescriptionImpl {

    @InterneDataklasser
    data class AutobrevImpl(
        override val name: String,
        override val letterDataClass: String,
        override val languages: List<LanguageCode>,
        override val metadata: LetterMetadata
    ): TemplateDescription.Autobrev

    @InterneDataklasser
    data class RedigerbarImpl(
        override val name: String,
        override val letterDataClass: String,
        override val languages: List<LanguageCode>,
        override val metadata: LetterMetadata,
        override val kategori: Brevkategori,
        override val brevkontekst: Brevkontekst,
        override val sakstyper: Set<Sakstype>,
    ): TemplateDescription.Redigerbar
}