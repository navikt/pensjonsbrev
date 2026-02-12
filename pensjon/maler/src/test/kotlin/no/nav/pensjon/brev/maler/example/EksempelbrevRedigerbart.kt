package no.nav.pensjon.brev.maler.example

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.FagsystemBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.maler.example.EksempelRedigerbartDtoSelectors.PesysDataSelectors.datoAvslaatt
import no.nav.pensjon.brev.maler.example.EksempelRedigerbartDtoSelectors.PesysDataSelectors.datoInnvilget
import no.nav.pensjon.brev.maler.example.EksempelRedigerbartDtoSelectors.PesysDataSelectors.navneliste
import no.nav.pensjon.brev.maler.example.EksempelRedigerbartDtoSelectors.PesysDataSelectors.pensjonInnvilget
import no.nav.pensjon.brev.maler.example.EksempelRedigerbartDtoSelectors.PesysDataSelectors.tilleggEksempel
import no.nav.pensjon.brev.maler.example.EksempelRedigerbartDtoSelectors.pesysData
import no.nav.pensjon.brev.maler.example.ExampleTilleggDtoSelectors.navn
import no.nav.pensjon.brev.maler.example.ExampleTilleggDtoSelectors.tillegg1
import no.nav.pensjon.brev.maler.example.ExampleTilleggDtoSelectors.tillegg2
import no.nav.pensjon.brev.maler.example.ExampleTilleggDtoSelectors.tillegg3
import no.nav.pensjon.brev.maler.fraser.common.KronerText
import no.nav.pensjon.brev.model.Brevkategori
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.BrukerSelectors.fornavn
import no.nav.pensjon.brevbaker.api.model.FellesSelectors.bruker
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import java.time.LocalDate

enum class EksempelbrevRedigerbartBrevkode : Brevkode.Redigerbart {
    TESTBREV_REDIGERBART;

    override fun kode() = name
}

@TemplateModelHelpers
object EksempelbrevRedigerbart : RedigerbarTemplate<EksempelRedigerbartDto> {

    override val kode: Brevkode.Redigerbart = EksempelbrevRedigerbartBrevkode.TESTBREV_REDIGERBART
    override val kategori = Brevkategori.INNHENTE_OPPLYSNINGER
    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper: Set<Sakstype> = Sakstype.all

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Dette er et redigerbart eksempel-brev", // Display title for external systems
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK, // Brukes ved distribusjon av brevet
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title {
            text(
                bokmal { + "Redigerbart eksempelbrev" },
                nynorsk { + "Redigerbart eksempelbrev" }
            )
        }

        // Main letter content
        outline {
            //Select boolean expression from this letters argument


            // Data from the felles(common) argument can also be used. Both felles and argument supports map and select.
            val firstName = felles.bruker.fornavn

            // section title
            title1 {
                text(bokmal { + "Du har fått innvilget pensjon" }, nynorsk { + "Du har fått innvilget pensjon" })
            }

            paragraph {
                text(
                    bokmal { + "Du kan klage på vedtaket innen seks uker fra du mottok det. Kontoret som har fattet vedtaket, vil da vurdere saken din på nytt." },
                    nynorsk { + "Du kan klage på vedtaket innen seks uker fra du mottok det. Kontoret som har fattet vedtaket, vil da vurdere saken din på nytt." }
                )
            }

            paragraph {
                showIf(firstName.equalTo("Alexander")) {
                    text(
                        bokmal { + "Hei Alexander" },
                        nynorsk { + "Hei Alexander" },
                    )
                }.orShowIf(firstName.equalTo("Håkon")) {
                    text(
                        bokmal { + "Hei Håkon" },
                        nynorsk { + "Hei Håkon" }
                    )
                }.orShow {
                    text(
                        bokmal { + "Hei " + firstName },
                        nynorsk { + "Hei " + firstName },
                    )
                }
                text(
                    bokmal { + ", håper du har en fin dag!" },
                    nynorsk { + ", håper du har en fin dag!" }
                )
            }

            paragraph {
                forEach(pesysData.tilleggEksempel) {
                    text(
                        bokmal { + "Heisann " + it.navn + " håper du har en fin dag!" },
                        nynorsk { + "Heisann " + it.navn + " håper du har en fin dag!" },
                    )
                }
            }

            // Fetch a value from the letter arguments
            paragraph {
                //ShowIf shows the content of the block if the boolean expression resolves to true
                showIf(pesysData.pensjonInnvilget) {
                    text(
                        // Text expressions can use variables as expressions, but the text literals also need to be expressions
                        bokmal { + "Hei " + firstName + ". Du har fått innvilget pensjon." },
                        nynorsk { + "Hei " + firstName + ". Du har fått innvilget pensjon." },
                    )
                }

                list {
                    forEach(pesysData.tilleggEksempel) { tillegg ->
                        ifNotNull(tillegg.tillegg1) {
                            item {
                                text(
                                    bokmal { + "Du har fått tilleg1 for " + tillegg.navn + " på " + it.format() },
                                    nynorsk { + "Du har fått tilleg1 for " + tillegg.navn + " på " + it.format() },
                                )
                            }
                        }
                        item { text(bokmal { + "Joda" }, nynorsk { + "Jauda" }) }
                    }
                    item {
                        text(bokmal { + "Test1" }, nynorsk { + "Test1" })
                    }
                    ifNotNull(pesysData.datoAvslaatt) { dato ->
                        item {
                            text(
                                bokmal { + "Du har fått avslag på noe " + dato.format() },
                                nynorsk { + "Du har fått avslag på noe " + dato.format() }
                            )
                        }
                    }

                    item {
                        text(bokmal { + "Test2" }, nynorsk { + "Test2" })
                    }

                    item {
                        text(bokmal { + "Test3" }, nynorsk { + "Test3" })
                    }

                    item {
                        //textOnlyPhrase can be included anywhere you write text.
                        includePhrase(TextOnlyPhraseTest)
                    }
                    item {
                        //Any type of phrase can also require data
                        includePhrase(TextOnlyPhraseTestWithParams(pesysData.datoInnvilget))
                    }
                }
                text(bokmal { + lipsums[0] }, nynorsk { + lipsums[0] })
            }

            title1 {
                text(bokmal { + "Utbetalingsoversikt" }, nynorsk { + "Utbetalingsoversikt" })
            }

            paragraph {
                text(
                    bokmal { + "Dette er din inntekt fra 01.01.2020 til 01.05.2020" },
                    nynorsk { + "Dette er din inntekt fra 01.01.2020 til 01.05.2020" }
                )
                table(
                    header = {
                        column(3) { text(bokmal { + "Kolonne 1" }, nynorsk { + "Kolonne 1" }) }
                        column(1, RIGHT) { text(bokmal { + "Kolonne 2" }, nynorsk { + "Kolonne 2" }) }
                        column(1, RIGHT) { text(bokmal { + "Kolonne 3" }, nynorsk { + "Kolonne 3" }) }
                        column(1, RIGHT) { text(bokmal { + "Kolonne 4" }, nynorsk { + "Kolonne 4" }) }
                    }
                ) {
                    forEach(pesysData.tilleggEksempel) { tillegg ->
                        val navn = tillegg.navn
                        val tillegg1 = tillegg.tillegg1
                        val tillegg2 = tillegg.tillegg2
                        val tillegg3 = tillegg.tillegg3
                        row {
                            cell {
                                text(
                                    bokmal { + "Du får tillegg for " + navn },
                                    nynorsk { + "Du får tillegg for " + navn }
                                )
                            }
                            cell {
                                ifNotNull(tillegg1) { tillegg ->
                                    includePhrase(KronerText(tillegg))
                                }
                            }
                            cell {
                                ifNotNull(tillegg2) { tillegg ->
                                    text(
                                        bokmal { + tillegg.format() },
                                        nynorsk { + tillegg.format() }
                                    )
                                }
                            }
                            cell {
                                ifNotNull(tillegg3) { tillegg ->
                                    text(
                                        bokmal { + tillegg.format() },
                                        nynorsk { + tillegg.format() }
                                    )
                                }
                            }
                        }
                    }
                    row {
                        cell {
                            text(
                                bokmal { + "Din inntekt før skatt i måned 1" },
                                nynorsk { + "Din inntekt før skatt i måned 1" }
                            )
                        }
                        cell { text(bokmal { + "100 Kr" }, nynorsk { + "100 Kr" }) }
                        cell { text(bokmal { + "200 Kr" }, nynorsk { + "200 Kr" }) }
                        cell { text(bokmal { + "300 Kr" }, nynorsk { + "300 Kr" }) }
                    }
                    row {
                        cell {
                            text(
                                bokmal { + "Din inntekt før skatt i måned 1" },
                                nynorsk { + "Din inntekt før skatt i måned 1" }
                            )
                        }
                        cell { text(bokmal { + "400 Kr" }, nynorsk { + "400 Kr" }) }
                        cell { text(bokmal { + "500 Kr" }, nynorsk { + "500 Kr" }) }
                        cell { text(bokmal { + "600 Kr" }, nynorsk { + "600 Kr" }) }
                    }
                    ifNotNull(pesysData.datoAvslaatt) { dato ->
                        row {
                            cell {
                                text(
                                    bokmal { + "Du har en dato satt! " + dato.format() },
                                    nynorsk { + "Du har en dato satt! " + dato.format() }
                                )
                            }
                            cell { text(bokmal { + "400 Kr" }, nynorsk { + "400 Kr" }) }
                            cell { text(bokmal { + "500 Kr" }, nynorsk { + "500 Kr" }) }
                            cell { text(bokmal { + "600 Kr" }, nynorsk { + "600 Kr" }) }
                        }
                    }
                }
            }
            // Repeat content for each element in list
            forEach(pesysData.navneliste) {
                title1 {
                    text(bokmal { + it }, nynorsk { + it })
                }
                paragraph {
                    text(
                        bokmal { + "En liste med navn har elementet: " + it },
                        nynorsk { + "En liste med navn har elementet: " + it }
                    )
                }
            }

            //Include outline phrase
            includePhrase(OutlinePhraseTest(pesysData.datoInnvilget, pesysData.pensjonInnvilget))

            //Print some lipsum paragraphs.
            for (lipsum in lipsums) {
                paragraph { text(bokmal { + lipsum }, nynorsk { + lipsum }) }
            }
        }
        includeAttachment(testVedlegg, TestVedleggDto("test1", "test2").expr())
    }
}

// This data class should normally be in the api-model. Placed here for test-purposes.
data class EksempelRedigerbartDto(
    override val saksbehandlerValg: EmptySaksbehandlerValg,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<EmptySaksbehandlerValg, EksempelRedigerbartDto.PesysData> {
    data class PesysData(
        val pensjonInnvilget: Boolean,
        val datoInnvilget: LocalDate,
        val navneliste: List<String>,
        val tilleggEksempel: List<ExampleTilleggDto>,
        val datoAvslaatt: LocalDate?,
        val pensjonBeloep: Int?,
    ) : FagsystemBrevdata
}
