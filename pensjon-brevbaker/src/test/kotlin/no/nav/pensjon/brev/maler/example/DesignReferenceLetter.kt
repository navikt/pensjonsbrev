package no.nav.pensjon.brev.maler.example

import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

object DesignReferenceLetter : AutobrevTemplate<LetterExampleDto> {

    override val kode = Brevkode.AutoBrev.PE_OMSORG_EGEN_AUTO

    override val template = createTemplate(
        name = "EKSEMPEL_BREV", //Letter ID
        letterDataType = LetterExampleDto::class, // Data class containing the required data of this letter
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Dette er ett eksempel-brev", // Display title for external systems
            isSensitiv = false, // If this letter contains sensitive information requiring level 4 log-in
            distribusjonstype = LetterMetadata.Distribusjonstype.ANNET, // Brukes ved distribusjon av brevet
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title {
            text(
                Bokmal to "Brevtittel"
            )
        }

        outline {
            repeat(100){
                title1 {
                    text(
                        Bokmal to "Overskrift",
                    )
                }
                paragraph {
                    repeat((Math.random()*4).toInt() + 1) {
                        text(
                            Bokmal to "Aliquam lectus nulla, condimentum vel est vitae, imperdiet viverra tortor. "
                        )
                    }

                    list {
                        repeat((Math.random()*9).toInt() + 1){
                            item {
                                text(
                                    Bokmal to "Nummerert list",
                                )
                            }
                        }
                    }
                }
            }
/*
            // section title
            title1 {
                text(Bokmal to "Overskrift")
            }

            paragraph {
                text(
                    Bokmal to "Aliquam lectus nulla, condimentum vel est vitae, imperdiet viverra tortor. Mauris non lorem eget diam posuere porta nec eu elit. Integer nec vestibulum leo. Aliquam lectus nulla, condimentum vel est vitae, imperdiet viverra tortor. Mauris non lorem eget diam posuere porta nec eu elit. Integer nec vestibulum leo."
                )
            }
            title1 {
                text(
                    Bokmal to "Underoverskrift nivå en",
                )
            }
            paragraph {
                text(
                    Bokmal to "Aliquam lectus nulla, condimentum vel est vitae, imperdiet viverra tortor. Mauris non lorem eget diam posuere porta nec eu elit. Integer nec vestibulum leo. Aliquam lectus nulla, condimentum vel est vitae, imperdiet viverra tortor.",
                )
            }
            paragraph {
                text(
                    Bokmal to "Mauris non lorem eget diam posuere porta nec eu elit. Integer nec vestibulum leo.",
                )
            }
            title1 {
                text(Bokmal to "Utbetalingsoversikt")
            }

            paragraph {
                table(
                    header = {
                        column(1) { text(Bokmal to "Måned", FontType.BOLD) }
                        column(1, RIGHT) { text(Bokmal to "Stønad", FontType.BOLD) }
                        column(1, RIGHT) { text(Bokmal to "Pensjon", FontType.BOLD) }
                        column(1, RIGHT) { text(Bokmal to "Totalt", FontType.BOLD) }
                    }
                ) {
                    listOf(
                        "Januar",
                        "Februar",
                        "Mars",
                        "April",
                        "Mai",
                        "Juni",
                        "Juli",
                        "August",
                        "September",
                        "Oktober",
                        "November",
                        "Desember",
                        "Januar",
                        "Februar",
                        "Mars",
                    ).forEach {
                        row {
                            cell { text(Bokmal to it) }
                            cell { text(Bokmal to "1 kr") }
                            cell { text(Bokmal to "1 kr") }
                            cell { text(Bokmal to "2 kr") }
                        }
                    }
                }
            }

            title2 {
                text(Bokmal to "Tittel 2")
            }

            paragraph {
                text(
                    Bokmal to "Du har fått innvilget pensjon.",
                )

                text(
                    Bokmal to "Du har fått innvilget pensjon. Dette er grunnen til det. Dette er grunnen til det. Dette er grunnen til" +
                            "det. Dette er grunnen til det. Dette er grunnen til det. Dette er grunnen til det. Du får 16 000 kroner i" +
                            "måneden fra januar 2022.")
            }

            paragraph {
                text(
                    Bokmal to "Du har fått innvilget pensjon. Dette er grunnen til det. Dette er grunnen til det. Dette er grunnen til" +
                            "det. Dette er grunnen til det. Dette er grunnen til det. Dette er grunnen til det. Du får 16 000 kroner i" +
                            "måneden fra januar 2022.")
            }

            title1 {
                text(Bokmal to "Tittel")
            }

            paragraph {
                text(
                    Bokmal to "Du har fått innvilget pensjon. Dette er grunnen til det. Dette er grunnen til det. Dette er grunnen til" +
                            "det. Dette er grunnen til det. Dette er grunnen til det. Dette er grunnen til det. Du får 16 000 kroner i" +
                            "måneden fra januar 2022.")
                list {
                    item {
                        text(
                            Bokmal to "Kulepunkt",
                        )
                    }
                    item {
                        text(
                            Bokmal to "Kulepunkt",
                        )
                    }
                    item {
                        text(
                            Bokmal to "Kulepunkt",
                        )
                    }
                    item {
                        text(
                            Bokmal to "Kulepunkt",
                        )
                    }
                }
            }

            title1 {
                text(Bokmal to "Tittel")
            }

            paragraph {
                text(
                    Bokmal to "Du har fått innvilget pensjon. Dette er grunnen til det. Dette er grunnen til det. Dette er grunnen til" +
                            "det. Dette er grunnen til det. Dette er grunnen til det. Dette er grunnen til det. Du får 16 000 kroner i" +
                            "måneden fra januar 2022.")


                list {
                    item {
                        text(
                            Bokmal to "Nummerert list",
                        )
                    }
                    item {
                        text(
                            Bokmal to "Nummerert list",
                        )
                    }
                    item {
                        text(
                            Bokmal to "Nummerert list",
                        )
                    }
                    item {
                        text(
                            Bokmal to "Nummerert list",
                        )
                    }
                    item {
                        text(
                            Bokmal to "Nummerert list",
                        )
                    }
                }
            }

            title1 {
                text(Bokmal to "Samme innhold med annen størrelse")
            }

            title2 {
                text(Bokmal to "Samme innhold med annen størrelse")
            }
*/

        }
    }
}