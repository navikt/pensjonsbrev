package no.nav.pensjon.brev.maler.example

import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.template.Element.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Element.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.VedtaksbrevTemplate
import no.nav.pensjon.brev.template.base.PensjonLatex
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text

object DesignReferenceLetter : VedtaksbrevTemplate<LetterExampleDto> {

    override val kode: Brevkode.Vedtak = Brevkode.Vedtak.OMSORG_EGEN_AUTO

    override val template = createTemplate(
        name = "EKSEMPEL_BREV", //Letter ID
        base = PensjonLatex, //Master-template
        letterDataType = LetterExampleDto::class, // Data class containing the required data of this letter
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Dette er ett eksempel-brev", // Display title for external systems
            isSensitiv = false, // If this letter contains sensitive information requiring level 4 log-in
            distribusjonstype = LetterMetadata.Distribusjonstype.ANNET, // Brukes ved distribusjon av brevet
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
                    Bokmal to "Du har fått innvilget pensjon. Dette er grunnen til det. Dette er grunnen til det. Dette er grunnen til\n" +
                            "det. Dette er grunnen til det. Dette er grunnen til det. Dette er grunnen til det. Du får 16 000 kroner i\n" +
                            "måneden fra januar 2022.",
                    Nynorsk to "Du har fått innvilget pensjon. Dette er grunnen til det. Dette er grunnen til det. Dette er grunnen til\n" +
                            "det. Dette er grunnen til det. Dette er grunnen til det. Dette er grunnen til det. Du får 16 000 kroner i\n" +
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
                    Bokmal to "Du har fått innvilget pensjon. Dette er grunnen til det. Dette er grunnen til det. Dette er grunnen til\n" +
                            "det. Dette er grunnen til det. Dette er grunnen til det. Dette er grunnen til det. Du får 16 000 kroner i\n" +
                            "måneden fra januar 2022.",
                    Nynorsk to "Du har fått innvilget pensjon. Dette er grunnen til det. Dette er grunnen til det. Dette er grunnen til\n" +
                            "det. Dette er grunnen til det. Dette er grunnen til det. Dette er grunnen til det. Du får 16 000 kroner i\n" +
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
                    Bokmal to "Du har fått innvilget pensjon. Dette er grunnen til det. Dette er grunnen til det. Dette er grunnen til\n" +
                            "det. Dette er grunnen til det. Dette er grunnen til det. Dette er grunnen til det. Du får 16 000 kroner i\n" +
                            "måneden fra januar 2022.",
                    Nynorsk to "Du har fått innvilget pensjon. Dette er grunnen til det. Dette er grunnen til det. Dette er grunnen til\n" +
                            "det. Dette er grunnen til det. Dette er grunnen til det. Dette er grunnen til det. Du får 16 000 kroner i\n" +
                            "måneden fra januar 2022."
                )
            }

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
    }
}