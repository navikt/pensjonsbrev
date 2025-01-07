package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.template.dsl.TextScope
import no.nav.pensjon.brevbaker.api.model.ElementTags

interface RedigerbarTemplate<LetterData : RedigerbarBrevdata<out BrevbakerBrevdata, out BrevbakerBrevdata>> : BrevTemplate<LetterData, Brevkode.Redigerbart> {
    val kategori: TemplateDescription.Brevkategori
    val brevkontekst: TemplateDescription.Brevkontekst
    val sakstyper: Set<Sakstype>

    override fun description(): TemplateDescription.Redigerbar =
        TemplateDescription.Redigerbar(
            name = template.name,
            letterDataClass = template.letterDataType.java.name,
            languages = template.language.all().map { it.toCode() },
            metadata = template.letterMetadata,
            kategori = kategori,
            brevkontekst = brevkontekst,
            sakstyper = sakstyper,
        )

    fun TextScope<*, *>.fritekst(beskrivelse: String): Expression<String> =
        Expression.Literal(beskrivelse, setOf(ElementTags.FRITEKST))
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