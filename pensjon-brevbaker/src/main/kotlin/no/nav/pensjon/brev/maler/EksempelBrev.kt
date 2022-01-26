package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.api.model.maler.EksempelBrevDto
import no.nav.pensjon.brev.template.Element.Table.RowColour.GRAY
import no.nav.pensjon.brev.template.Element.Text.FontType.BOLD
import no.nav.pensjon.brev.template.Element.Text.FontType.ITALIC
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.StaticTemplate
import no.nav.pensjon.brev.template.base.PensjonLatex
import no.nav.pensjon.brev.template.dsl.*

object EksempelBrev : StaticTemplate {
    override val template = createTemplate(
        //ID på brevet
        name = "EKSEMPEL_BREV",

        //Master-mal for brevet.
        base = PensjonLatex,

        // Unik datagrunnlag/DTO for brevet
        letterDataType = EksempelBrevDto::class,

        // Hvilke språk brevet støtter
        lang = languages(Bokmal),

        // Hovedtittel inne i brevet
        title = newText(Bokmal to "Eksempelbrev"),

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
                text(Bokmal to "Du har fått innvilget pensjon")
            }

            // Inkluder data fra datagrunnlaget til malen inn i brevet som tekst
            eval { argument().select(EksempelBrevDto::pensjonInnvilget).str() }
            text(Bokmal to "test")
            table {
                row(GRAY) {
                    cell(4) {
                        text(Bokmal to "1", BOLD)
                        text(Bokmal to "2", ITALIC)
                    }
                }
                row(GRAY) {
                    cell(3) {
                        text(Bokmal to "3", BOLD)
                        text(Bokmal to "4", ITALIC)
                    }
                    cell {
                        text(Bokmal to "555555555555", BOLD)
                        text(Bokmal to "666666666", ITALIC)
                    }
                }
                row(GRAY) {
                    cell {
                        text(Bokmal to "1111111111111", BOLD)
                        text(Bokmal to "1234567890", ITALIC)
                    }
                    cell(2) {
                        text(Bokmal to "2222222222222", BOLD)
                        text(Bokmal to "1234567890", ITALIC)
                    }
                    cell {
                        text(Bokmal to "2222222222222", BOLD)
                        text(Bokmal to "1234567890", ITALIC)
                    }
                }



                row {
                    cell { text(Bokmal to "Dette er en eksempeltekst eller noe sånnt", ITALIC) }
                    cell { text(Bokmal to "2", BOLD) }
                    cell { text(Bokmal to "3") }
                    cell { text(Bokmal to "4") }
                }
                row {
                    cell { text(Bokmal to "Dette er noe", ITALIC) }
                    cell { text(Bokmal to "535135135351", BOLD) }
                    cell { text(Bokmal to "5") }
                    cell { text(Bokmal to "4gdagdagad gdagdagda") }
                }
            }
        }
    }
}