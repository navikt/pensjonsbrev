package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.api.model.maler.EksempelBrevDto
import no.nav.pensjon.brev.template.Element.Text.FontType
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.StaticTemplate
import no.nav.pensjon.brev.template.base.PensjonLatex
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.select
import no.nav.pensjon.brev.template.dsl.expression.str
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

            // Avsnitt
            paragraph {
                text(Bokmal to "Test dette er en test")
            }

            // Inkluder data fra datagrunnlaget til malen inn i brevet som tekst
            eval { argument().select(EksempelBrevDto::pensjonInnvilget).str() }
            text(Bokmal to "test")
            paragraph {
                table {
                    title{
                        text(Bokmal to "Eksempeltabell")
                    }
                    columnHeaderRow {
                        cell(4) {
                            text(
                                Bokmal to "Dette er en lang lang tittel. Dette er en lang lang tittel. Dette er en lang lang tittel. Dette er en lang lang tittel.",
                                FontType.BOLD
                            )
                        }
                    }
                    columnHeaderRow {
                        cell(2) {
                            text(Bokmal to "Blabla 2 kolonner brei")
                        }
                        cell(2) {
                            text(Bokmal to "Blabla 2 kolonner brei")
                        }
                    }
                    columnHeaderRow {
                        cell(1) {
                            text(Bokmal to "Blabla 1 kolonne brei")
                        }
                        cell(3) {
                            text(Bokmal to "Blabla 3 kolonner breiiiiiiiiiiiiiiiiiiiiiiiiiiiiiiii")
                        }
                    }
                    for (i in 1..50) {
                        row {
                            cell {
                                text(Bokmal to "$i Kr")
                            }
                            cell {
                                text(Bokmal to "$i Kr")
                            }
                            cell {
                                text(Bokmal to "$i Kr")
                            }
                            cell {
                                text(Bokmal to "$i Kr")
                            }
                        }
                    }
                }
            }
        }
    }
}