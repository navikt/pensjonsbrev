package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brevbaker.api.model.LanguageCode

interface BrevTemplate<out LetterData : BrevbakerBrevdata, Kode : Brevkode<Kode>> : HasModel<LetterData> {
    val template: LetterTemplate<*, LetterData>
    val kode: Kode
    fun description(): TemplateDescription
}

interface AutobrevTemplate<out LetterData : BrevbakerBrevdata> : BrevTemplate<LetterData, Brevkode.Automatisk> {
    override fun description(): TemplateDescription.Autobrev =
        TemplateDescription.Autobrev(
            name = template.name,
            letterDataClass = template.letterDataType.java.name,
            languages = template.language.all().map { it.toCode() },
            metadata = template.letterMetadata,
        )
}

fun Language.toCode(): LanguageCode =
    when (this) {
        Language.Bokmal -> LanguageCode.BOKMAL
        Language.Nynorsk -> LanguageCode.NYNORSK
        Language.English -> LanguageCode.ENGLISH
    }