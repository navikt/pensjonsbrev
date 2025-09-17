package no.nav.brev.brevbaker

import no.nav.pensjon.brev.template.LanguageSupport
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.dsl.TemplateRootScope
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import kotlin.reflect.KClass

fun <Lang : LanguageSupport, LetterData : Any> createTemplate(
    letterDataType: KClass<LetterData>,
    languages: Lang,
    letterMetadata: LetterMetadata,
    init: TemplateRootScope<Lang, LetterData>.() -> Unit,
): LetterTemplate<Lang, LetterData> =
    with(TemplateRootScope<Lang, LetterData>().apply(init)) {
        return LetterTemplate(title, letterDataType, languages, outline, attachments, letterMetadata)
    }