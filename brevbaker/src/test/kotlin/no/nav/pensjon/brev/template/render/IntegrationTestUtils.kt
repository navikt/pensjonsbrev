package no.nav.pensjon.brev.template.render

import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.OutlineElement
import no.nav.pensjon.brev.template.dsl.languages

fun outlineTestLetter(vararg elements: OutlineElement<LangBokmal>) =
    LetterTemplate(
        name = "test",
        title = listOf(bokmalTittel),
        letterDataType = Unit::class,
        language = languages(Bokmal),
        outline = elements.asList(),
        letterMetadata = testLetterMetadata,
    )
