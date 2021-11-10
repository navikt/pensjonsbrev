package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.api.model.maler.EksempelBrevDto
import no.nav.pensjon.brev.maler.fraser.TestFraseDto
import no.nav.pensjon.brev.maler.fraser.TestFrase
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.StaticTemplate
import no.nav.pensjon.brev.template.base.PensjonLatex
import no.nav.pensjon.brev.template.dsl.*

object EksempelBrev : StaticTemplate {
    override val template = createTemplate(
        name = "eksempelBrev",
        base = PensjonLatex,
        letterDataType = EksempelBrevDto::class,
        lang = languages(Language.Bokmal),
        title = newText(Language.Bokmal to "Eksempelbrev"),
        letterMetadata = LetterMetadata(
            "Dette er ett eksempel-brev"
        )
    ) {

        outline {
            includePhrase(argument().select(EksempelBrevDto::pensjonInnvilget).map { TestFraseDto(it.toString()) }, TestFrase)

            title1 {
                text(Language.Bokmal to "Heisann, ")
                text(Language.Bokmal to "Du har fått innvilget pensjon")
            }
            eval { argument().select(EksempelBrevDto::pensjonInnvilget).str() }

            eval { argument().select(EksempelBrevDto::datoInnvilget).format() }
        }
    }
}