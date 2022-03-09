package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.api.model.maler.EksempelBrevDto
import no.nav.pensjon.brev.template.Element.Text.FontType
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.StaticTemplate
import no.nav.pensjon.brev.template.base.PensjonLatex
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr

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
                list {
                    showIf(true.expr()) {
                        item {
                            text(Bokmal to "Test1")
                        }
                    }
                    showIf(false.expr()) {
                        item {
                            text(Bokmal to "Test2")
                        }
                    }
                    showIf(true.expr()) {
                        item {
                            text(Bokmal to "Test3")
                        }
                    }
                    item{
                        textExpr(Bokmal to "Hello".expr() + "world".expr())
                    }
                }
            }

            title1{
                Bokmal to "Utbetalingsoversikt"
            }
            paragraph {
                text(Bokmal to "test")
                table {
                    title {
                        text(Bokmal to "Eksempeltabell")
                    }
                    columnHeaderRow {
                        cell(1) {text(Bokmal to "Kolonne 1",FontType.BOLD)}
                        cell(1) {text(Bokmal to "Kolonne 2",FontType.BOLD)}
                        cell(1) {text(Bokmal to "Kolonne 3",FontType.BOLD)}
                        cell(1) {text(Bokmal to "Kolonne 4",FontType.BOLD)}
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