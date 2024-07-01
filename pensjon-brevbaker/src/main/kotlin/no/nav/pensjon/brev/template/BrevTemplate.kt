package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.toCode

interface BrevTemplate<out LetterData : BrevbakerBrevdata, Kode : Enum<Kode>> : HasModel<LetterData> {
    val template: LetterTemplate<*, LetterData>
    val kode: Kode
    fun description(): TemplateDescription
}

interface RedigerbarTemplate<LetterData : RedigerbarBrevdata<out BrevbakerBrevdata, out BrevbakerBrevdata>> :
    BrevTemplate<LetterData, Brevkode.Redigerbar> {
        val kategori: TemplateDescription.Brevkategori

        override fun description(): TemplateDescription =
            TemplateDescription(
                name = template.name,
                letterDataClass = template.letterDataType.java.name,
                languages = template.language.all().map { it.toCode() },
                metadata = template.letterMetadata,
                kategori = kategori,
            )
    }

interface AutobrevTemplate<out LetterData : BrevbakerBrevdata> : BrevTemplate<LetterData, Brevkode.AutoBrev> {
    override fun description(): TemplateDescription =
        TemplateDescription(
            name = template.name,
            letterDataClass = template.letterDataType.java.name,
            languages = template.language.all().map { it.toCode() },
            metadata = template.letterMetadata,
            kategori = null,
        )
}