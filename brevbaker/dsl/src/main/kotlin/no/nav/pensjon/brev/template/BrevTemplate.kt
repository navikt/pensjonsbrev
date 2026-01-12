package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.api.model.FeatureToggle
import no.nav.pensjon.brev.api.model.ISakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.AutobrevData
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.api.model.maler.SaksbehandlerValgBrevdata
import no.nav.pensjon.brev.template.Expression.Literal
import no.nav.pensjon.brev.template.dsl.TemplateGlobalScope
import no.nav.pensjon.brev.template.dsl.TemplateRootScope
import no.nav.pensjon.brevbaker.api.model.ElementTags
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import kotlin.reflect.KClass

sealed interface BrevTemplate<out LetterData : BrevbakerBrevdata, Kode : Brevkode<Kode>> : HasModel<LetterData> {
    val template: LetterTemplate<*, LetterData>
    val kode: Kode
    fun description(): TemplateDescription
    val featureToggle: FeatureToggle?
        get() = null

    fun <Lang : LanguageSupport, LetterData : BrevbakerBrevdata> createTemplate(
        letterDataType: KClass<LetterData>,
        languages: Lang,
        letterMetadata: LetterMetadata,
        init: TemplateRootScope<Lang, LetterData>.() -> Unit
    ): LetterTemplate<Lang, LetterData> =
        with(TemplateRootScope<Lang, LetterData>().apply(init)) {
            return LetterTemplate(title, letterDataType, languages, outline, attachments, pdfAttachments, letterMetadata)
        }
}

inline fun <Kode : Brevkode<Kode>, Lang : LanguageSupport, reified LetterData : BrevbakerBrevdata> BrevTemplate<LetterData, Kode>.createTemplate(
    languages: Lang,
    letterMetadata: LetterMetadata,
    noinline init: TemplateRootScope<Lang, LetterData>.() -> Unit
): LetterTemplate<Lang, LetterData> = createTemplate(LetterData::class, languages, letterMetadata, init)

interface RedigerbarTemplate<LetterData : RedigerbarBrevdata<out SaksbehandlerValgBrevdata, out FagsystemBrevdata>> : BrevTemplate<LetterData, Brevkode.Redigerbart> {
    val kategori: TemplateDescription.Brevkategori
    val brevkontekst: TemplateDescription.Brevkontekst
    val sakstyper: Set<ISakstype>

    override fun description(): TemplateDescription.Redigerbar =
        TemplateDescription.Redigerbar(
            name = kode.kode(),
            letterDataClass = template.letterDataType.java.name,
            languages = template.language.all().map { it.toCode() },
            metadata = template.letterMetadata,
            kategori = kategori,
            brevkontekst = brevkontekst,
            sakstyper = sakstyper,
        )

    fun TemplateGlobalScope<LetterData>.fritekst(beskrivelse: String): Expression<String> =
        beskrivelse.takeIf { it.trim().isNotEmpty() }
            ?.let { Literal(it, setOf(ElementTags.FRITEKST)) }
            ?: throw IllegalArgumentException("Fritekstfelt må ha initiell tekst for at vi ikke skal lure bruker.")
}

interface AutobrevTemplate<out LetterData : AutobrevData> : BrevTemplate<LetterData, Brevkode.Automatisk> {
    override fun description(): TemplateDescription.Autobrev =
        TemplateDescription.Autobrev(
            name = kode.kode(),
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