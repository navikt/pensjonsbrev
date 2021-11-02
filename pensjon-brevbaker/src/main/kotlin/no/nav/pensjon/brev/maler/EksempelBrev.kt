package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.maler.EksempelBrevDto
import no.nav.pensjon.brev.maler.fraser.PhraseInputDto
import no.nav.pensjon.brev.maler.fraser.testFrase
import no.nav.pensjon.brev.template.Expression
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
        title = newText(Language.Bokmal to "Eksempelbrev")
    ) {

        outline {
            usePhrase(Expression.Literal(PhraseInputDto("test")), testFrase)
            title1 {
                text(Language.Bokmal to "Heisann, ")
                text(Language.Bokmal to "Du har fått innvilget pensjon")
            }
            eval { argument().select(EksempelBrevDto::pensjonInnvilget).str() }

            eval { argument().select(EksempelBrevDto::datoInnvilget).format() }
        }
    }
}