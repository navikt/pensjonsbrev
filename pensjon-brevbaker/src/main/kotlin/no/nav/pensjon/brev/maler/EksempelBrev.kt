package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.base.PensjonLatex
import no.nav.pensjon.brev.template.dsl.format
import no.nav.pensjon.brev.template.dsl.str
import java.time.LocalDate

data class EksempelBrevDto(val pensjonInnvilget: Boolean, val datoInnvilget: LocalDate)

object EksempelBrev : StaticTemplate {
    override val template = createTemplate(
        name = "eksempelBrev",
        title = newText(Language.Bokmal to "Eksempelbrev"),
        base = PensjonLatex,
        lang = languages(Language.Bokmal),
        parameterType = EksempelBrevDto::class
    ) {

        outline {
            title1 {
                text(Language.Bokmal to "Heisann, ")
                text(Language.Bokmal to "Du har fått innvilget pensjon")
            }
            selectField(EksempelBrevDto::pensjonInnvilget) {
                it.str()
            }

            selectField(EksempelBrevDto::datoInnvilget) {
                it.format()
            }
        }
    }
}