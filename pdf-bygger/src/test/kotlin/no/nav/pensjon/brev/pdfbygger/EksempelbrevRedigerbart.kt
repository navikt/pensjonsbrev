package no.nav.pensjon.brev.pdfbygger

import no.nav.pensjon.brevbaker.api.model.LetterMarkup.ParagraphContent.Table.ColumnAlignment.RIGHT

object EksempelbrevRedigerbart {

    val brev = letterMarkup {
        title {
            text(
                "Redigerbart eksempelbrev",
            )
        }

        // Main letter content
        outline {
            // section title
            title1 {
                text("Du har fått innvilget pensjon")
            }

            paragraph {
                text(
                    "Du kan klage på vedtaket innen seks uker fra du mottok det. Kontoret som har fattet vedtaket, vil da vurdere saken din på nytt.",
                )
            }

            paragraph {
                text("Hei Alexander. ")
                text("Hei Håkon. ")
                text("Håper dere har en fin dag!")
            }

            paragraph {
                list {
                    item {
                        text("Test1")
                    }

                    item {
                        text("Test2")
                    }

                    item {
                        text("Test3")
                    }
                }
                text(lipsums[0])
            }

            title1 {
                text("Utbetalingsoversikt")
            }

            paragraph {
                text("Dette er din inntekt fra 01.01.2020 til 01.05.2020")
                table(
                    header = {
                        column(3) { text("Kolonne 1") }
                        column(1, RIGHT) { text("Kolonne 2") }
                        column(1, RIGHT) { text("Kolonne 3") }
                        column(1, RIGHT) { text("Kolonne 4") }
                    }
                ) {
                    row {
                        cell {
                            text("Din inntekt før skatt i måned 1")
                        }
                        cell { text("100 Kr") }
                        cell { text("200 Kr") }
                        cell { text("300 Kr") }
                    }
                    row {
                        cell {
                            text(
                                "Din inntekt før skatt i måned 1",
                            )
                        }
                        cell { text("400 Kr") }
                        cell { text("500 Kr") }
                        cell { text("600 Kr") }
                    }
                }
            }

            //Print some lipsum paragraphs.
            for (lipsum in lipsums) {
                paragraph { text(lipsum) }
            }
        }
    }

    val vedlegg = attachment {
        title { text("Test vedlegg") }
        outline {
            paragraph {
                text("Dette er et test vedlegg.")
            }
        }
    }
}
