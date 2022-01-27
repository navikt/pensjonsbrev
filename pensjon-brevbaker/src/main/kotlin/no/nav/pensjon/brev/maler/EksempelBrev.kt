package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.api.model.maler.EksempelBrevDto
import no.nav.pensjon.brev.maler.fraser.TestFraseDto
import no.nav.pensjon.brev.maler.fraser.testFrase
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.StaticTemplate
import no.nav.pensjon.brev.template.base.PensjonLatex
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.map
import no.nav.pensjon.brev.template.dsl.expression.select
import no.nav.pensjon.brev.template.dsl.expression.str
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text

object EksempelBrev : StaticTemplate {
    override val template = createTemplate(
        //ID på brevet
        name = "EKSEMPEL_BREV",

        //Master-mal for brevet.
        base = PensjonLatex,

        // Unik datagrunnlag/DTO for brevet
        letterDataType = EksempelBrevDto::class,

        // Hvilke språk brevet støtter
        lang = languages(Language.Bokmal),

        // Hovedtittel inne i brevet
        title = newText(Language.Bokmal to "Eksempelbrev"),

        // Metadata knyttet til en brevmal som ikke påvirker innholdet
        letterMetadata = LetterMetadata(

            // Visningstittel for ulike systemer.
            // F.eks saksbehandling brev oversikt, brukerens brev oversikt osv
            displayTitle = "Dette er ett eksempel-brev",

            // Krever brevet bankid/ nivå 4 pålogging for å vises(og ikke brukernavn/passord nivå 3)
            isSensitiv = false,
        )
    ) {
        // Tekst-innholdet i malen
        outline {

            // Undertittel
            title1 {

                // Tekst
                text(Language.Bokmal to "Du har fått innvilget pensjon")
            }

            // Inkluder data fra datagrunnlaget til malen inn i brevet som tekst
            eval { argument().select(EksempelBrevDto::pensjonInnvilget).str() }

            // Inkludering av eksisterende frase/mini-mal for gjenbruk av elementer
            includePhrase(
                argument().select(EksempelBrevDto::pensjonInnvilget)
                    .map { TestFraseDto(it.toString()) },
                testFrase
            )
        }
    }
}