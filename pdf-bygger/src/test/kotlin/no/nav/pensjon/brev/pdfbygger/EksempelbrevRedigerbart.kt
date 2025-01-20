package no.nav.pensjon.brev.pdfbygger

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.BrukerSelectors.fornavn
import no.nav.pensjon.brevbaker.api.model.FellesSelectors.bruker
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import java.time.LocalDate

enum class EksempelbrevRedigerbartBrevkode : Brevkode.Redigerbart {
    TESTBREV;

    override fun kode() = name
}

@TemplateModelHelpers
object EksempelbrevRedigerbart : RedigerbarTemplate<EksempelRedigerbartDto> {

    override val kode: Brevkode.Redigerbart = EksempelbrevRedigerbartBrevkode.TESTBREV
    override val kategori = TemplateDescription.Brevkategori.INNHENTE_OPPLYSNINGER
    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper: Set<Sakstype> = Sakstype.all

    override val template = createTemplate(
        name = "EKSEMPEL_REDIGERBART_BREV", //Letter ID
        letterDataType = EksempelRedigerbartDto::class, // Data class containing the required data of this letter
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Dette er et redigerbart eksempel-brev", // Display title for external systems
            isSensitiv = false, // If this letter contains sensitive information requiring level 4 log-in
            distribusjonstype = LetterMetadata.Distribusjonstype.ANNET, // Brukes ved distribusjon av brevet
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title {
            text(
                Bokmal to "Redigerbart eksempelbrev",
                Nynorsk to "Redigerbart eksempelbrev"
            )
        }

        // Main letter content
        outline {
            //Select boolean expression from this letters argument


            // Data from the felles(common) argument can also be used. Both felles and argument supports map and select.
            val firstName = felles.bruker.fornavn

            // section title
            title1 {
                text(Bokmal to "Du har fått innvilget pensjon", Nynorsk to "Du har fått innvilget pensjon")
            }

            paragraph {
                text(
                    Bokmal to "Du kan klage på vedtaket innen seks uker fra du mottok det. Kontoret som har fattet vedtaket, vil da vurdere saken din på nytt.",
                    Nynorsk to "Du kan klage på vedtaket innen seks uker fra du mottok det. Kontoret som har fattet vedtaket, vil da vurdere saken din på nytt."
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
                        Nynorsk to "Hei Håkon"
                    )
                }.orShow {
                    textExpr(
                        Bokmal to "Hei ".expr() + firstName,
                        Nynorsk to "Hei ".expr() + firstName,
                    )
                }
                text(
                    Bokmal to ", håper du har en fin dag!",
                    Nynorsk to ", håper du har en fin dag!"
                )
            }

            // Fetch a value from the letter arguments
            paragraph {

                list {
                    item {
                        text(Bokmal to "Test1", Nynorsk to "Test1")
                    }

                    item {
                        text(Bokmal to "Test2", Nynorsk to "Test2")
                    }

                    item {
                        text(Bokmal to "Test3", Nynorsk to "Test3")
                    }

                    item {
                        //textOnlyPhrase can be included anywhere you write text.
                        includePhrase(TextOnlyPhraseTest)
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
                    Nynorsk to "Dette er din inntekt fra 01.01.2020 til 01.05.2020"
                )
                table(
                    header = {
                        column(3) { text(Bokmal to "Kolonne 1", Nynorsk to "Kolonne 1") }
                        column(1, RIGHT) { text(Bokmal to "Kolonne 2", Nynorsk to "Kolonne 2") }
                        column(1, RIGHT) { text(Bokmal to "Kolonne 3", Nynorsk to "Kolonne 3") }
                        column(1, RIGHT) { text(Bokmal to "Kolonne 4", Nynorsk to "Kolonne 4") }
                    }
                ) {
                    row {
                        cell {
                            text(
                                Bokmal to "Din inntekt før skatt i måned 1",
                                Nynorsk to "Din inntekt før skatt i måned 1"
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
                                Nynorsk to "Din inntekt før skatt i måned 1"
                            )
                        }
                        cell { text(Bokmal to "400 Kr", Nynorsk to "400 Kr") }
                        cell { text(Bokmal to "500 Kr", Nynorsk to "500 Kr") }
                        cell { text(Bokmal to "600 Kr", Nynorsk to "600 Kr") }
                    }
                }
            }

            //Print some lipsum paragraphs.
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


fun createEksempelbrevRedigerbartDto() =
    EksempelRedigerbartDto(
        saksbehandlerValg = EmptyBrevdata,
        pesysData = EksempelRedigerbartDto.PesysData(
            pensjonInnvilget = true,
            datoInnvilget = LocalDate.of(2020, 1, 1),
            navneliste = listOf("test testerson1", "test testerson2", "test testerson3"),
            tilleggEksempel = listOf(
                ExampleTilleggDto(
                    navn = "Test testerson 1",
                    tillegg1 = Kroner(300),
                    tillegg3 = Kroner(500),
                ), ExampleTilleggDto(
                    navn = "Test testerson 2",
                    tillegg1 = Kroner(100),
                    tillegg2 = Kroner(600),
                ), ExampleTilleggDto(
                    navn = "Test testerson 3",
                    tillegg2 = Kroner(300),
                )
            ), datoAvslaatt = LocalDate.of(2020, 1, 1),
            pensjonBeloep = 100
        )
    )