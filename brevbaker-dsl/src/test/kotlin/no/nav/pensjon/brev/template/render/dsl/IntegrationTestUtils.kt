package no.nav.pensjon.brev.template.render.dsl

import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.OutlineElement
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.languages

inline fun <reified LetterData : Any> outlineTestTemplate(
    noinline function: OutlineOnlyScope<LangBokmal, LetterData>.() -> Unit,
): LetterTemplate<LangBokmal, LetterData> =
    createTemplate(
        name = "test",
        letterDataType = LetterData::class,
        languages = languages(Bokmal),
        letterMetadata = testLetterMetadata,
    ) {
        title.add(bokmalTittel)
        outline(function)
    }

internal fun outlineTestLetter(vararg elements: OutlineElement<LangBokmal>) = LetterTemplate(
    name = "test",
    title = listOf(bokmalTittel),
    letterDataType = Unit::class,
    language = languages(Bokmal),
    outline = elements.asList(),
    letterMetadata = testLetterMetadata
)