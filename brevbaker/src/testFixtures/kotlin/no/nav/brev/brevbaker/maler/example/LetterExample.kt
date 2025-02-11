package no.nav.pensjon.brev.maler.example

import no.nav.pensjon.brev.api.model.FeatureToggle
import no.nav.pensjon.brev.api.model.ToggleImpl
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.maler.example.ExampleTilleggDtoSelectors.navn
import no.nav.pensjon.brev.maler.example.ExampleTilleggDtoSelectors.tillegg1
import no.nav.pensjon.brev.maler.example.ExampleTilleggDtoSelectors.tillegg2
import no.nav.pensjon.brev.maler.example.ExampleTilleggDtoSelectors.tillegg3
import no.nav.pensjon.brev.maler.example.LetterExampleDtoSelectors.datoAvslaatt
import no.nav.pensjon.brev.maler.example.LetterExampleDtoSelectors.datoInnvilget
import no.nav.pensjon.brev.maler.example.LetterExampleDtoSelectors.navneliste
import no.nav.pensjon.brev.maler.example.LetterExampleDtoSelectors.pensjonInnvilget
import no.nav.pensjon.brev.maler.example.LetterExampleDtoSelectors.tilleggEksempel
import no.nav.pensjon.brev.maler.example.TestVedleggDtoSelectors.testVerdi1
import no.nav.pensjon.brev.maler.example.TestVedleggDtoSelectors.testVerdi2
import no.nav.pensjon.brev.maler.fraser.common.KronerText
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.ParagraphPhrase
import no.nav.pensjon.brev.template.StableHash
import no.nav.pensjon.brev.template.TextOnlyPhrase
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.ParagraphOnlyScope
import no.nav.pensjon.brev.template.dsl.TextOnlyScope
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.enabled
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.BrukerSelectors.fornavn
import no.nav.pensjon.brevbaker.api.model.FellesSelectors.bruker
import no.nav.pensjon.brevbaker.api.model.FellesSelectors.dokumentDato
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import java.time.LocalDate

enum class LetterExampleBrevkode : Brevkode.Automatisk {
    TESTBREV,
    ;

    override fun kode() = name
}

object TestToggle : FeatureToggle, StableHash by StableHash.of("TestToggleKey") {
    override fun key() = "TestToggleKey"
}

@TemplateModelHelpers
object LetterExample : AutobrevTemplate<LetterExampleDto> {
    override val kode: Brevkode.Automatisk = LetterExampleBrevkode.TESTBREV

    override val template =
        createTemplate(
            // Letter ID
            name = "EKSEMPEL_BREV",
            // Data class containing the required data of this letter
            letterDataType = LetterExampleDto::class,
            languages = languages(Bokmal, Nynorsk),
            letterMetadata =
                LetterMetadata(
                    // Display title for external systems
                    displayTitle = "Dette er ett eksempel-brev",
                    // If this letter contains sensitive information requiring level 4 log-in
                    isSensitiv = false,
                    // Brukes ved distribusjon av brevet
                    distribusjonstype = LetterMetadata.Distribusjonstype.ANNET,
                    brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
                ),
        ) {
            title {
                text(
                    Bokmal to "Eksempelbrev",
                    Nynorsk to "Eksempelbrev",
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
                    forEach(tilleggEksempel) {
                        textExpr(
                            Bokmal to "Heisann ".expr() + it.navn + " håper du har en fin dag!",
                            Nynorsk to "Heisann ".expr() + it.navn + " håper du har en fin dag!",
                        )
                    }
                }

                paragraph {
                    showIf(TestToggle.expr().enabled() and true.expr()) {
                        text(
                            Bokmal to "a",
                            Nynorsk to "b",
                        )
                    }.orShow {
                        text(
                            Bokmal to "nei",
                            Nynorsk to "kanskje",
                        )
                    }
                }

                showIf(ToggleImpl("" + System.currentTimeMillis()).expr().enabled()) {
                    paragraph {
                        text(
                            Bokmal to "Tekst styrt av funksjonsbryter",
                            Nynorsk to "Tekst styrt av funksjonsbrytar",
                        )
                    }
                }

                // Fetch a value from the letter arguments
                paragraph {
                    // ShowIf shows the content of the block if the boolean expression resolves to true
                    showIf(pensjonInnvilget) {
                        textExpr(
                            // Text expressions can use variables as expressions, but the text literals also need to be expressions
                            Bokmal to "Hei ".expr() + firstName + ". Du har fått innvilget pensjon.".expr(),
                            Nynorsk to "Hei ".expr() + firstName + ". Du har fått innvilget pensjon.".expr(),
                        )
                    }

                    list {
                        forEach(tilleggEksempel) { tillegg ->
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
                        ifNotNull(datoAvslaatt) { dato ->
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
                            includePhrase(TextOnlyPhraseTestWithParams(datoInnvilget))
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
                        forEach(tilleggEksempel) { tillegg ->
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
                        ifNotNull(datoAvslaatt) { dato ->
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
                forEach(navneliste) {
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
                includePhrase(OutlinePhraseTest(datoInnvilget, pensjonInnvilget))

                // Print some lipsum paragraphs.
                for (lipsum in lipsums) {
                    paragraph { text(Bokmal to lipsum, Nynorsk to lipsum) }
                }
            }
            includeAttachment(testVedlegg, TestVedleggDto("test1", "test2").expr())
        }
}

// This data class should normally be in the api-model. Placed here for test-purposes.
data class LetterExampleDto(
    val pensjonInnvilget: Boolean,
    val datoInnvilget: LocalDate,
    val navneliste: List<String>,
    val tilleggEksempel: List<ExampleTilleggDto>,
    val datoAvslaatt: LocalDate?,
    val pensjonBeloep: Int?,
) : BrevbakerBrevdata

data class ExampleTilleggDto(
    val navn: String,
    val tillegg1: Kroner? = null,
    val tillegg2: Kroner? = null,
    val tillegg3: Kroner? = null,
) {
    @Suppress("unused")
    constructor() : this(
        navn = "Navn",
        tillegg1 = Kroner(1234),
        tillegg2 = Kroner(1234),
        tillegg3 = Kroner(1234),
    )
}

data class OutlinePhraseTest(val datoInnvilget: Expression<LocalDate>, val pensjonInnvilget: Expression<Boolean>) : OutlinePhrase<LangBokmalNynorsk>() {
    // The elements used in outline can also be used in outline phrases.
    // This is intended for use in the top-level outline scope

    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() =
        paragraph {
            showIf(pensjonInnvilget) {
                val dato = datoInnvilget.format()
                textExpr(
                    Bokmal to "Du har fått innvilget pensjon fra ".expr() + dato + ".",
                    Nynorsk to "Du har fått innvilget pensjon fra ".expr() + dato + ".",
                )
            }
        }
}

@Suppress("unused")
object ParagraphPhraseTest : ParagraphPhrase<LangBokmalNynorsk>() {
    override fun ParagraphOnlyScope<LangBokmalNynorsk, Unit>.template() =
        list {
            item {
                text(Bokmal to "Test 1", Nynorsk to "Test 1")
            }

            item {
                text(Bokmal to "Test 2", Nynorsk to "Test 2")
            }

            item {
                text(Bokmal to "Test 3", Nynorsk to "Test 3")
            }
        }
}

object TextOnlyPhraseTest : TextOnlyPhrase<LangBokmalNynorsk>() {
    override fun TextOnlyScope<LangBokmalNynorsk, Unit>.template() =
        text(Bokmal to "Dette er en tekstfrase", Nynorsk to "Dette er en tekstfrase")
}

data class TextOnlyPhraseTestWithParams(val dato: Expression<LocalDate>) : TextOnlyPhrase<LangBokmalNynorsk>() {
    override fun TextOnlyScope<LangBokmalNynorsk, Unit>.template() =
        textExpr(
            Bokmal to "Dette er en tekstfrase med datoen: ".expr() + dato.format(),
            Nynorsk to "Dette er en tekstfrase med datoen: ".expr() + dato.format(),
        )
}

data class TestVedleggDto(val testVerdi1: String, val testVerdi2: String)

@TemplateModelHelpers
val testVedlegg =
    createAttachment<LangBokmalNynorsk, TestVedleggDto>(
        title =
            newText(
                Bokmal to "Test vedlegg",
                Nynorsk to "Test vedlegg",
            ),
        includeSakspart = true,
    ) {
        paragraph {
            // felles can also be used in phrases and attachment even if it wasn't explicitly sent in
            val dokDato = felles.dokumentDato.format()
            textExpr(
                Bokmal to "Test verdi 1: ".expr() + testVerdi1 + " Test verdi 2: " + testVerdi2 + " dokument dato: " + dokDato,
                Nynorsk to "Test verdi 1: ".expr() + testVerdi1 + " Test verdi 2: " + testVerdi2 + " dokument dato: " + dokDato,
            )
        }
    }

val lipsums =
    listOf(
        "Etiam porta turpis et eros ullamcorper sodales. Cras et eleifend leo. Aenean vehicula nunc sit amet quam tincidunt, id aliquam arcu cursus. Morbi non imperdiet augue, nec placerat tellus. Aenean imperdiet auctor porta. Morbi in lacus nec purus commodo sodales non in ligula. Praesent euismod mollis elit, mollis finibus massa pretium eget. Fusce mollis tempus nisl vitae suscipit. Morbi in elementum tortor. Aenean varius odio non sem convallis, at venenatis arcu ullamcorper. Duis porttitor nulla facilisis mattis porttitor. Quisque pharetra hendrerit tellus, id consequat sapien maximus sit amet. Vestibulum vehicula pellentesque nulla, sit amet egestas felis pellentesque ac. Ut viverra vel magna eget mollis. Aliquam dictum aliquet tortor vitae efficitur. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus.",
        "Fusce sed diam ac dui luctus venenatis sit amet sit amet sapien. Praesent non congue metus. Morbi rutrum pellentesque rhoncus. Duis semper dictum rutrum. Curabitur iaculis, magna sit amet varius dignissim, sapien augue pellentesque mi, id malesuada arcu risus et justo. Vivamus fermentum neque ac purus faucibus, non viverra massa pulvinar. Suspendisse ornare erat hendrerit, condimentum lorem vel, fringilla dui. Donec non tortor dignissim, ornare metus nec, malesuada nulla. Nulla convallis arcu ultricies augue consectetur, eu mattis neque tristique. Sed suscipit lacus vel risus lobortis, sed dignissim orci posuere. Aenean ut magna eget tellus viverra tincidunt non quis lectus. Donec elementum molestie tellus, tincidunt tincidunt urna tincidunt in. Nunc eget lorem non enim rhoncus consequat. Vivamus laoreet semper facilisis.",
        "Donec consequat nibh vitae faucibus blandit. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Ut vel fermentum urna. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Nulla dictum justo in egestas venenatis. Sed vel eros quis turpis blandit accumsan. Nulla sed velit euismod, aliquet nibh eu, finibus mauris.",
        "Cras efficitur tincidunt eleifend. Vestibulum auctor diam in tortor tincidunt dapibus. Nulla id nunc luctus, sodales tellus sed, pulvinar turpis. Etiam vel vulputate ex, nec efficitur nunc. Morbi vel maximus quam. Pellentesque id iaculis ipsum. Sed facilisis dui et arcu aliquam rhoncus. Nullam id ex dictum, porttitor elit sed, hendrerit risus. Duis convallis sed magna sit amet porttitor. Phasellus neque ligula, cursus id tristique et, facilisis in magna. Sed bibendum tempus neque.",
        "Suspendisse faucibus lorem ante, vel gravida enim dignissim sit amet. Pellentesque a tempor ligula. Nunc eu nisl massa. Nullam ut semper magna. Aliquam condimentum massa dui. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas ac ex pretium, pretium mi ut, congue nulla. Integer eget neque id orci tristique hendrerit quis quis quam. Praesent quis nunc nunc. Cras luctus maximus mi quis dapibus. Nullam ultricies bibendum velit quis pulvinar. Cras a turpis elit. Mauris placerat rhoncus metus ac varius. Etiam rhoncus mi sit amet sollicitudin posuere. Vivamus scelerisque, ex vitae imperdiet luctus, metus tortor fringilla lacus, non facilisis dui tortor vel ante. Cras efficitur lacus felis, in imperdiet enim feugiat a.",
        "In suscipit, velit convallis feugiat semper, dolor urna vehicula arcu, ornare hendrerit quam ex non dui. Donec consectetur, urna et faucibus viverra, mi est consectetur enim, ac luctus lectus eros nec mauris. Fusce quam sapien, elementum ut neque a, tempus ultrices nunc. Integer id aliquet lacus. Nam rhoncus ligula et diam gravida, eget auctor urna vehicula. Phasellus vel dignissim metus, sed consectetur risus. Proin in diam in sapien condimentum tristique. Duis ultrices est eu nisl scelerisque porta. Sed vulputate quis felis id semper. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Etiam eleifend, ante in porta aliquam, velit sem faucibus libero, quis rhoncus elit libero nec diam. Suspendisse vel faucibus orci.",
        "Etiam luctus, velit eu semper cursus, orci justo porttitor leo, sed tincidunt purus nunc commodo quam. Integer sed varius sapien. Vestibulum felis dolor, tristique eget mauris id, semper fermentum tortor. Ut imperdiet auctor sodales. Proin ut tincidunt nulla, at lobortis nulla. Vestibulum quis justo vitae mi tempor ultricies a sit amet dolor. Praesent vitae metus viverra, consectetur purus ut, consequat mi. Integer vitae tempus eros. Curabitur orci nisi, varius eget orci quis, pulvinar rhoncus quam. Fusce non dui id augue semper vulputate. Morbi aliquet blandit ullamcorper. Interdum et malesuada fames ac ante ipsum primis in faucibus. Curabitur tempus massa dui, quis malesuada enim suscipit sed.",
        "Aenean vel mauris massa. Aliquam vel tortor in mi posuere suscipit et vitae mauris. Aenean tortor nulla, tempus quis suscipit vel, ornare non orci. Phasellus non semper tellus. Fusce finibus neque eu accumsan efficitur. Vestibulum ante ipsum primis in faucibus orci luctus et ultrices posuere cubilia curae; Cras dictum euismod volutpat. Praesent quis rutrum massa. Nullam iaculis, erat vel sodales fringilla, leo tellus ullamcorper risus, a bibendum nulla nisl in dui. Duis ac nisi felis.",
        "Ut congue ac orci a porttitor. Nullam ut ex erat. Nullam at venenatis augue. Morbi luctus fringilla ex, ut ultrices enim auctor ac. Maecenas eget blandit justo, quis euismod nunc. Cras venenatis nunc non euismod hendrerit. Suspendisse facilisis mi dui, in ultricies tellus dictum ut. Nulla id elit eu arcu viverra iaculis non at est. Phasellus leo nunc, posuere vitae est vel, sagittis lobortis tellus. Vivamus viverra eu tortor at placerat. Sed vehicula congue magna vitae ultrices. Nulla facilisi. Vivamus bibendum metus vitae lacus consequat, eget ullamcorper sem blandit.",
        "Fusce tincidunt porta orci, ac pretium erat mattis vel. Suspendisse quis turpis sit amet enim lacinia interdum at et dui. Nulla fringilla bibendum magna sed interdum. Donec congue orci non malesuada dignissim. Mauris pulvinar nibh et varius fermentum. Proin efficitur bibendum viverra. Nam sollicitudin euismod tempor. Sed dapibus ac dolor ut dignissim. Quisque sagittis, sem eget suscipit venenatis, odio elit finibus eros, vel faucibus nibh libero vitae nisi.",
        "Nulla in bibendum tellus. Praesent sit amet luctus ligula. Nulla facilisi. Ut laoreet arcu dignissim arcu dignissim, efficitur porta risus lobortis. Suspendisse elit nunc, tempus id maximus nec, faucibus non ipsum. Cras semper neque quis est facilisis, sed egestas neque accumsan. Nulla facilisi.",
        "Aliquam erat volutpat. Integer ac euismod augue. Phasellus venenatis lorem in ipsum vestibulum volutpat. Proin eu sapien id lectus egestas placerat vel quis dui. Fusce dapibus risus urna, eu feugiat orci auctor id. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Pellentesque porttitor nisl non mi finibus, ut malesuada enim congue. Nullam auctor enim sollicitudin orci viverra tempor. Etiam placerat volutpat nisl vitae vehicula. Donec quis rutrum dui. Nunc congue, sapien quis pharetra gravida, nisl tortor vehicula arcu, a mattis leo turpis ac massa. Duis sagittis augue in arcu sollicitudin, sit amet dapibus turpis dignissim. Sed id efficitur risus. Pellentesque habitant morbi tristique senectus et netus et malesuada fames ac turpis egestas. Ut cursus eget augue vitae faucibus.",
        "Nullam rhoncus faucibus nisi, at tristique ante fermentum et. Donec hendrerit, sem eu euismod convallis, nisi risus dapibus quam, id egestas metus justo ac tortor. Nulla sagittis mauris eleifend odio efficitur, gravida rutrum turpis aliquet. Sed a turpis sed ex molestie viverra. Quisque maximus tincidunt dui, ut faucibus quam sollicitudin ut. Mauris nec rhoncus ipsum. Fusce in enim ac risus tincidunt interdum et vitae mauris. Aliquam non sodales elit, sit amet pharetra lectus. Nulla facilisi. Quisque vitae nisl eu nulla eleifend sagittis. Praesent rutrum elit nec est viverra, a feugiat nibh dignissim. Donec sed scelerisque leo. Donec sed pulvinar eros, nec mollis nunc. Curabitur ac erat et justo porttitor auctor. Donec sed condimentum dui, at sodales magna. Vivamus quis lacinia risus.",
        "Sed sed nunc vitae nunc convallis placerat quis non quam. Sed vulputate augue nulla, in finibus tortor consectetur in. Vestibulum mollis sagittis feugiat. Donec a interdum diam, nec pretium justo. Fusce congue lacus ac euismod rutrum. Vivamus nulla nisl, ornare vel tempor eget, porta eget dui. Praesent vulputate dui vitae venenatis porttitor. Sed aliquam ligula libero, id rhoncus mauris pharetra vel. Donec et pulvinar ante. Curabitur id malesuada leo. Vestibulum vel augue vehicula, rutrum magna id, consectetur erat. Maecenas condimentum felis varius, eleifend ex ac, interdum augue. Nam quis ex sed urna ornare suscipit sed quis ante. Curabitur vitae est pharetra, condimentum purus et, mollis ante. Morbi id feugiat dui, ullamcorper fermentum ligula.",
        "Etiam dictum molestie nunc eget vehicula. Suspendisse et interdum est. Ut sed luctus massa. Phasellus aliquet, felis vel tempus aliquet, velit est vestibulum orci, nec hendrerit nunc odio vitae magna. In mi est, lobortis nec suscipit in, auctor vel nisi. Duis ante neque, dictum non consequat non, ornare sed mauris. Quisque eu lorem convallis, sodales magna eget, dictum ante. Aliquam ac pharetra tortor. Mauris dictum gravida vestibulum. Duis lacinia lacinia sem a ultrices. Phasellus posuere fermentum ex, vel iaculis diam lobortis eget. Integer et efficitur ipsum, id hendrerit nulla. Phasellus dapibus, lacus et euismod rutrum, mi ante convallis massa, sit amet fermentum ante ante sed orci. Aliquam sapien dolor, imperdiet blandit odio nec, laoreet euismod nisl.",
        "Pellentesque at est tempor, maximus velit vitae, ullamcorper felis. Quisque non sodales turpis. Vestibulum tincidunt et orci vel auctor. Pellentesque eu consequat est. Nulla condimentum venenatis ex, non vehicula sem pulvinar et. Quisque aliquet semper eros. Donec dictum nibh in libero molestie ultrices. Sed quis libero neque. Integer vehicula quam at sollicitudin dignissim. Pellentesque maximus arcu dolor, id gravida elit hendrerit quis.",
        "Donec sed rutrum metus. Sed at est a orci iaculis tristique. Ut non bibendum nisl. Integer ante eros, suscipit sit amet nisl ut, ultricies faucibus ante. Vivamus hendrerit felis arcu, et convallis velit mattis porta. Curabitur varius, leo et lacinia pellentesque, eros massa tempor felis, at gravida leo augue et nulla. Donec efficitur quam ac elit sagittis maximus. Duis in rhoncus nunc, eu aliquet risus. Curabitur posuere mattis mi, sed placerat justo pellentesque vel. Suspendisse sed nulla sapien. Praesent feugiat neque in diam pharetra mattis. Nullam laoreet finibus arcu sit amet eleifend. In mollis dictum ultricies. Donec id justo a sapien tempor scelerisque. Ut varius, sapien vitae semper commodo, velit leo mollis orci, id efficitur velit augue vel neque. In rutrum laoreet semper.",
        "Nulla dignissim non ante sit amet mattis. Sed condimentum tincidunt ligula sed sodales. Quisque volutpat et nisl at molestie. Curabitur consectetur, massa efficitur tempus euismod, enim velit lacinia nulla, eu vehicula purus justo et mauris. Curabitur in dui tempor, porttitor lorem vitae, tincidunt ligula. Maecenas ultrices quam nec mattis hendrerit. Aenean pretium aliquet ipsum, sed iaculis felis tristique dapibus.",
        "Ut aliquam magna non elit molestie feugiat. Orci varius natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Ut blandit, augue in tristique accumsan, arcu felis ornare elit, et luctus nulla odio in libero. Vestibulum commodo eleifend sapien. Vivamus tempor ac nulla non pulvinar. Nulla pulvinar at odio ultrices semper. Donec tempor mauris eu tortor rhoncus lobortis. Fusce egestas velit congue massa eleifend blandit. Maecenas egestas fringilla urna a dapibus. Nulla id leo a diam rhoncus tempus.",
    )
