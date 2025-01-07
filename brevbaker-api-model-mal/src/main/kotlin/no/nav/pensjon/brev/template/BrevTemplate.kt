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

fun Language.toCode(): LanguageCode =
    when (this) {
        Language.Bokmal -> LanguageCode.BOKMAL
        Language.Nynorsk -> LanguageCode.NYNORSK
        Language.English -> LanguageCode.ENGLISH
    }