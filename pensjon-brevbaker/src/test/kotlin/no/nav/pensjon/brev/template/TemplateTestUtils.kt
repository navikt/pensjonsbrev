package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.bokmalTittel
import no.nav.pensjon.brev.template.dsl.testLetterMetadata

internal inline fun <reified LetterData : Any> outlineTestTemplate(noinline function: OutlineOnlyScope<LangBokmal, LetterData>.() -> Unit) =
    createTemplate(
        name = "test",
        letterDataType = LetterData::class,
        languages = languages(Bokmal),
        letterMetadata = testLetterMetadata,
    ) {
        title.add(bokmalTittel)
        outline(function)
    }

fun outlineTestLetter(vararg elements: OutlineElement<LangBokmal>) = LetterTemplate(
    name = "test",
    title = listOf(bokmalTittel),
    letterDataType = Unit::class,
    language = languages(Bokmal),
    outline = elements.asList(),
    letterMetadata = testLetterMetadata
)