package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.template.base.PensjonLatex
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.bokmalTittel
import no.nav.pensjon.brev.template.dsl.testLetterMetadata

internal inline fun <reified LetterData : Any> outlineTestTemplate(noinline function: OutlineScope<BokmalLang, LetterData>.() -> Unit) =
    createTemplate(
        name = "test",
        base = PensjonLatex,
        letterDataType = LetterData::class,
        title = bokmalTittel,
        letterMetadata = testLetterMetadata,
    ) {
        outline(function)
    }

fun outlineTestLetter(vararg elements: Element<BokmalLang>) = LetterTemplate(
    name = "test",
    title = bokmalTittel,
    base = PensjonLatex,
    letterDataType = Unit::class,
    language = languages(Language.Bokmal),
    letterMetadata = testLetterMetadata,
    outline = elements.asList())