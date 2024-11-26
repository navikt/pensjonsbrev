package no.nav.pensjon.brev.maler.example

import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

object DesignReferenceLetter : AutobrevTemplate<LetterExampleDto> {

    override val kode = Pesysbrevkoder.AutoBrev.PE_OMSORG_EGEN_AUTO

    override val template = createTemplate(
        name = "EKSEMPEL_BREV", //Letter ID
        letterDataType = LetterExampleDto::class, // Data class containing the required data of this letter
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Dette er ett eksempel-brev", // Display title for external systems
            isSensitiv = false, // If this letter contains sensitive information requiring level 4 log-in
            distribusjonstype = LetterMetadata.Distribusjonstype.ANNET, // Brukes ved distribusjon av brevet
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title {
            text(
                Bokmal to "Du har fått innvilget søknad om pensjon",
                Nynorsk to "Du har fått innvilget søknad om pensjon"
            )
        }

        outline {
            // section title
            title1 {
                text(Bokmal to "Hvorfor skriver vi til deg?", Nynorsk to "Hvorfor skriver vi til deg?")
            }

            paragraph {
                text(
                    Bokmal to "Du har fått innvilget pensjon. Dette er grunnen til det. Dette er grunnen til det. Dette er grunnen til" +
                            "det. Dette er grunnen til det. Dette er grunnen til det. Dette er grunnen til det. Du får 16 000 kroner i" +
                            "måneden fra januar 2022.",
                    Nynorsk to "Du har fått innvilget pensjon. Dette er grunnen til det. Dette er grunnen til det. Dette er grunnen til" +
                            "det. Dette er grunnen til det. Dette er grunnen til det. Dette er grunnen til det. Du får 16 000 kroner i" +
                            "måneden fra januar 2022."
                )
            }


            title1 {
                text(Bokmal to "Utbetalingsoversikt", Nynorsk to "Utbetalingsoversikt")
            }

            paragraph {
                text(
                    Bokmal to "Dette er din månedlige pensjonsutbetaling",
                    Nynorsk to "Dette er din månedlige pensjonsutbetaling"
                )
                table(
                    header = {
                        column(1) { text(Bokmal to "Måned", Nynorsk to "Måned", FontType.BOLD) }
                        column(1, RIGHT) { text(Bokmal to "Stønad", Nynorsk to "Stønad", FontType.BOLD) }
                        column(1, RIGHT) { text(Bokmal to "Pensjon", Nynorsk to "Pensjon", FontType.BOLD) }
                        column(1, RIGHT) { text(Bokmal to "Totalt", Nynorsk to "Totalt", FontType.BOLD) }
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
                            cell { text(Bokmal to it, Nynorsk to it) }
                            cell { text(Bokmal to "1 kr", Nynorsk to "1 kr") }
                            cell { text(Bokmal to "1 kr", Nynorsk to "1 kr") }
                            cell { text(Bokmal to "2 kr", Nynorsk to "2 kr") }
                        }
                    }
                }
            }

            title2 {
                text(Bokmal to "Tittel 2", Nynorsk to "Tittel 2")
            }

            paragraph {
                text(
                    Bokmal to "Du har fått innvilget pensjon.",
                    Nynorsk to "Du har fått innvilget pensjon.",
                    FontType.BOLD
                )

                text(
                    Bokmal to "Du har fått innvilget pensjon. Dette er grunnen til det. Dette er grunnen til det. Dette er grunnen til" +
                            "det. Dette er grunnen til det. Dette er grunnen til det. Dette er grunnen til det. Du får 16 000 kroner i" +
                            "måneden fra januar 2022.",
                    Nynorsk to "Du har fått innvilget pensjon. Dette er grunnen til det. Dette er grunnen til det. Dette er grunnen til" +
                            "det. Dette er grunnen til det. Dette er grunnen til det. Dette er grunnen til det. Du får 16 000 kroner i" +
                            "måneden fra januar 2022."
                )
            }

            paragraph {
                text(
                    Bokmal to "Du har fått innvilget pensjon. Dette er grunnen til det. Dette er grunnen til det. Dette er grunnen til" +
                            "det. Dette er grunnen til det. Dette er grunnen til det. Dette er grunnen til det. Du får 16 000 kroner i" +
                            "måneden fra januar 2022.",
                    Nynorsk to "Du har fått innvilget pensjon. Dette er grunnen til det. Dette er grunnen til det. Dette er grunnen til" +
                            "det. Dette er grunnen til det. Dette er grunnen til det. Dette er grunnen til det. Du får 16 000 kroner i" +
                            "måneden fra januar 2022."
                )
            }

            title1 {
                text(Bokmal to "Tittel", Nynorsk to "Tittel")
            }

            paragraph {
                text(
                    Bokmal to "Du har fått innvilget pensjon. Dette er grunnen til det. Dette er grunnen til det. Dette er grunnen til" +
                            "det. Dette er grunnen til det. Dette er grunnen til det. Dette er grunnen til det. Du får 16 000 kroner i" +
                            "måneden fra januar 2022.",
                    Nynorsk to "Du har fått innvilget pensjon. Dette er grunnen til det. Dette er grunnen til det. Dette er grunnen til" +
                            "det. Dette er grunnen til det. Dette er grunnen til det. Dette er grunnen til det. Du får 16 000 kroner i" +
                            "måneden fra januar 2022."
                )
                list {
                    item {
                        text(
                            Bokmal to "Kulepunkt",
                            Nynorsk to "Kulepunkt",
                        )
                    }
                    item {
                        text(
                            Bokmal to "Kulepunkt",
                            Nynorsk to "Kulepunkt",
                        )
                    }
                    item {
                        text(
                            Bokmal to "Kulepunkt",
                            Nynorsk to "Kulepunkt",
                        )
                    }
                    item {
                        text(
                            Bokmal to "Kulepunkt",
                            Nynorsk to "Kulepunkt",
                        )
                    }
                }
            }

            title1 {
                text(Bokmal to "Tittel", Nynorsk to "Tittel")
            }

            paragraph {
                text(
                    Bokmal to "Du har fått innvilget pensjon. Dette er grunnen til det. Dette er grunnen til det. Dette er grunnen til" +
                            "det. Dette er grunnen til det. Dette er grunnen til det. Dette er grunnen til det. Du får 16 000 kroner i" +
                            "måneden fra januar 2022.",
                    Nynorsk to "Du har fått innvilget pensjon. Dette er grunnen til det. Dette er grunnen til det. Dette er grunnen til" +
                            "det. Dette er grunnen til det. Dette er grunnen til det. Dette er grunnen til det. Du får 16 000 kroner i" +
                            "måneden fra januar 2022."
                )


                list {
                    item {
                        text(
                            Bokmal to "Nummerert list",
                            Nynorsk to "Nummerert list",
                        )
                    }
                    item {
                        text(
                            Bokmal to "Nummerert list",
                            Nynorsk to "Nummerert list",
                        )
                    }
                    item {
                        text(
                            Bokmal to "Nummerert list",
                            Nynorsk to "Nummerert list",
                        )
                    }
                    item {
                        text(
                            Bokmal to "Nummerert list",
                            Nynorsk to "Nummerert list",
                        )
                    }
                    item {
                        text(
                            Bokmal to "Nummerert list",
                            Nynorsk to "Nummerert list",
                        )
                    }
                }
            }

            title1 {
                text(Bokmal to "Tittel 1", Nynorsk to "Tittel 1")
            }

            title2 {
                text(Bokmal to "Tittel 2", Nynorsk to "Tittel 2")
            }

        }
    }
}