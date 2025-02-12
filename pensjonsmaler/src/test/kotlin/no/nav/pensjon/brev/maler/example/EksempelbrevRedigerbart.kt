package no.nav.pensjon.brev.maler.example

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
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
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.BrukerSelectors.fornavn
import no.nav.pensjon.brevbaker.api.model.FellesSelectors.bruker
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import java.time.LocalDate

enum class EksempelbrevRedigerbartBrevkode : Brevkode.Redigerbart {
    TESTBREV,
    ;

    override fun kode() = name
}

@TemplateModelHelpers
object EksempelbrevRedigerbart : RedigerbarTemplate<EksempelRedigerbartDto> {
    override val kode: Brevkode.Redigerbart = EksempelbrevRedigerbartBrevkode.TESTBREV
    override val kategori = TemplateDescription.Brevkategori.INNHENTE_OPPLYSNINGER
    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper: Set<Sakstype> = Sakstype.all

    override val template =
        createTemplate(
            name = "EKSEMPEL_REDIGERBART_BREV", // Letter ID
            letterDataType = EksempelRedigerbartDto::class, // Data class containing the required data of this letter
            languages = languages(Bokmal, Nynorsk),
            letterMetadata =
                LetterMetadata(
                    displayTitle = "Dette er et redigerbart eksempel-brev", // Display title for external systems
                    isSensitiv = false, // If this letter contains sensitive information requiring level 4 log-in
                    distribusjonstype = LetterMetadata.Distribusjonstype.ANNET, // Brukes ved distribusjon av brevet
                    brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
                ),
        ) {
            title {
                text(
                    Bokmal to "Redigerbart eksempelbrev",
                    Nynorsk to "Redigerbart eksempelbrev",
                )
            }

            // Main letter content
            outline {
                // Select boolean expression from this letters argument

                // Data from the felles(common) argument can also be used. Both felles and argument supports map and select.
                val firstName = felles.bruker.fornavn

                // section title
                title1 {
                    text(Bokmal to "Du har fått innvilget pensjon", Nynorsk to "Du har fått innvilget pensjon")
                }

                paragraph {
                    text(
                        Bokmal to "Du kan klage på vedtaket innen seks uker fra du mottok det. Kontoret som har fattet vedtaket, vil da vurdere saken din på nytt.",
                        Nynorsk to "Du kan klage på vedtaket innen seks uker fra du mottok det. Kontoret som har fattet vedtaket, vil da vurdere saken din på nytt.",
                    )
                }

                paragraph {
                    showIf(firstName.equalTo("Alexander")) {
                        text(
                            Bokmal to "Hei Alexander",
                            Nynorsk to "Hei Alexander",
                        )
                    }.orShowIf(firstName.equalTo("Håkon")) {
                        text(
                            Bokmal to "Hei Håkon",
                            Nynorsk to "Hei Håkon",
                        )
                    }.orShow {
                        textExpr(
                            Bokmal to "Hei ".expr() + firstName,
                            Nynorsk to "Hei ".expr() + firstName,
                        )
                    }
                    text(
                        Bokmal to ", håper du har en fin dag!",
                        Nynorsk to ", håper du har en fin dag!",
                    )
                }

                paragraph {
                    forEach(pesysData.tilleggEksempel) {
                        textExpr(
                            Bokmal to "Heisann ".expr() + it.navn + " håper du har en fin dag!",
                            Nynorsk to "Heisann ".expr() + it.navn + " håper du har en fin dag!",
                        )
                    }
                }

                // Fetch a value from the letter arguments
                paragraph {
                    // ShowIf shows the content of the block if the boolean expression resolves to true
                    showIf(pesysData.pensjonInnvilget) {
                        textExpr(
                            // Text expressions can use variables as expressions, but the text literals also need to be expressions
                            Bokmal to "Hei ".expr() + firstName + ". Du har fått innvilget pensjon.".expr(),
                            Nynorsk to "Hei ".expr() + firstName + ". Du har fått innvilget pensjon.".expr(),
                        )
                    }

                    list {
                        forEach(pesysData.tilleggEksempel) { tillegg ->
                            ifNotNull(tillegg.tillegg1) {
                                item {
                                    textExpr(
                                        Bokmal to "Du har fått tilleg1 for ".expr() + tillegg.navn + " på ".expr() + it.format() + " Kr",
                                        Nynorsk to "Du har fått tilleg1 for ".expr() + tillegg.navn + " på ".expr() + it.format() + " Kr",
                                    )
                                }
                            }
                            item { text(Bokmal to "Joda", Nynorsk to "Jauda") }
                        }
                        item {
                            text(Bokmal to "Test1", Nynorsk to "Test1")
                        }
                        ifNotNull(pesysData.datoAvslaatt) { dato ->
                            item {
                                textExpr(
                                    Bokmal to "Du har fått avslag på noe ".expr() + dato.format(),
                                    Nynorsk to "Du har fått avslag på noe ".expr() + dato.format(),
                                )
                            }
                        }

                        item {
                            text(Bokmal to "Test2", Nynorsk to "Test2")
                        }

                        item {
                            text(Bokmal to "Test3", Nynorsk to "Test3")
                        }

                        item {
                            // textOnlyPhrase can be included anywhere you write text.
                            includePhrase(TextOnlyPhraseTest)
                        }
                        item {
                            // Any type of phrase can also require data
                            includePhrase(TextOnlyPhraseTestWithParams(pesysData.datoInnvilget))
                        }
                    }
                    text(Bokmal to lipsums[0], Nynorsk to lipsums[0])
                }

                title1 {
                    text(Bokmal to "Utbetalingsoversikt", Nynorsk to "Utbetalingsoversikt")
                }

                paragraph {
                    text(
                        Bokmal to "Dette er din inntekt fra 01.01.2020 til 01.05.2020",
                        Nynorsk to "Dette er din inntekt fra 01.01.2020 til 01.05.2020",
                    )
                    table(
                        header = {
                            column(3) { text(Bokmal to "Kolonne 1", Nynorsk to "Kolonne 1") }
                            column(1, RIGHT) { text(Bokmal to "Kolonne 2", Nynorsk to "Kolonne 2") }
                            column(1, RIGHT) { text(Bokmal to "Kolonne 3", Nynorsk to "Kolonne 3") }
                            column(1, RIGHT) { text(Bokmal to "Kolonne 4", Nynorsk to "Kolonne 4") }
                        },
                    ) {
                        forEach(pesysData.tilleggEksempel) { tillegg ->
                            val navn = tillegg.navn
                            val tillegg1 = tillegg.tillegg1
                            val tillegg2 = tillegg.tillegg2
                            val tillegg3 = tillegg.tillegg3
                            row {
                                cell {
                                    textExpr(
                                        Bokmal to "Du får tillegg for ".expr() + navn,
                                        Nynorsk to "Du får tillegg for ".expr() + navn,
                                    )
                                }
                                cell {
                                    ifNotNull(tillegg1) { tillegg ->
                                        includePhrase(KronerText(tillegg))
                                    }
                                }
                                cell {
                                    ifNotNull(tillegg2) { tillegg ->
                                        textExpr(
                                            Bokmal to tillegg.format() + " Kr".expr(),
                                            Nynorsk to tillegg.format() + " Kr".expr(),
                                        )
                                    }
                                }
                                cell {
                                    ifNotNull(tillegg3) { tillegg ->
                                        textExpr(
                                            Bokmal to tillegg.format() + " Kr".expr(),
                                            Nynorsk to tillegg.format() + " Kr".expr(),
                                        )
                                    }
                                }
                            }
                        }
                        row {
                            cell {
                                text(
                                    Bokmal to "Din inntekt før skatt i måned 1",
                                    Nynorsk to "Din inntekt før skatt i måned 1",
                                )
                            }
                            cell { text(Bokmal to "100 Kr", Nynorsk to "100 Kr") }
                            cell { text(Bokmal to "200 Kr", Nynorsk to "200 Kr") }
                            cell { text(Bokmal to "300 Kr", Nynorsk to "300 Kr") }
                        }
                        row {
                            cell {
                                text(
                                    Bokmal to "Din inntekt før skatt i måned 1",
                                    Nynorsk to "Din inntekt før skatt i måned 1",
                                )
                            }
                            cell { text(Bokmal to "400 Kr", Nynorsk to "400 Kr") }
                            cell { text(Bokmal to "500 Kr", Nynorsk to "500 Kr") }
                            cell { text(Bokmal to "600 Kr", Nynorsk to "600 Kr") }
                        }
                        ifNotNull(pesysData.datoAvslaatt) { dato ->
                            row {
                                cell {
                                    textExpr(
                                        Bokmal to "Du har en dato satt! ".expr() + dato.format(),
                                        Nynorsk to "Du har en dato satt! ".expr() + dato.format(),
                                    )
                                }
                                cell { text(Bokmal to "400 Kr", Nynorsk to "400 Kr") }
                                cell { text(Bokmal to "500 Kr", Nynorsk to "500 Kr") }
                                cell { text(Bokmal to "600 Kr", Nynorsk to "600 Kr") }
                            }
                        }
                    }
                }
                // Repeat content for each element in list
                forEach(pesysData.navneliste) {
                    title1 {
                        textExpr(Bokmal to it, Nynorsk to it)
                    }
                    paragraph {
                        textExpr(
                            Bokmal to "En liste med navn har elementet: ".expr() + it,
                            Nynorsk to "En liste med navn har elementet: ".expr() + it,
                        )
                    }
                }

                // Include outline phrase
                includePhrase(OutlinePhraseTest(pesysData.datoInnvilget, pesysData.pensjonInnvilget))

                // Print some lipsum paragraphs.
                for (lipsum in lipsums) {
                    paragraph { text(Bokmal to lipsum, Nynorsk to lipsum) }
                }
            }
            includeAttachment(testVedlegg, TestVedleggDto("test1", "test2").expr())
        }
}

// This data class should normally be in the api-model. Placed here for test-purposes.
data class EksempelRedigerbartDto(
    override val saksbehandlerValg: EmptyBrevdata,
    override val pesysData: PesysData,
) : RedigerbarBrevdata<EmptyBrevdata, EksempelRedigerbartDto.PesysData> {
    data class PesysData(
        val pensjonInnvilget: Boolean,
        val datoInnvilget: LocalDate,
        val navneliste: List<String>,
        val tilleggEksempel: List<ExampleTilleggDto>,
        val datoAvslaatt: LocalDate?,
        val pensjonBeloep: Int?,
    ) : BrevbakerBrevdata
}
