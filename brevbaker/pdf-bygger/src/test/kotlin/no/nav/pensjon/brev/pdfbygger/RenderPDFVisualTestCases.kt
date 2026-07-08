@file:OptIn(InterneDataklasser::class)

package no.nav.pensjon.brev.pdfbygger

import no.nav.brev.InterneDataklasser
import no.nav.brev.brevbaker.copy
import no.nav.brev.brevbaker.createTemplate
import no.nav.brev.brevbaker.FellesFactory
import no.nav.pensjon.brev.api.model.maler.EmptyAutobrevdata
import no.nav.pensjon.brev.api.model.maler.EmptyVedleggData
import no.nav.pensjon.brev.template.AttachmentTemplate
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Letter
import no.nav.pensjon.brev.template.LetterImpl
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.ParagraphOnlyScope
import no.nav.pensjon.brev.template.dsl.choice
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.BrevbakerFelles
import no.nav.pensjon.brevbaker.api.model.BrevbakerFelles.SignerendeSaksbehandlere
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Form.Text.Size
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType

data class RenderPDFVisualTestCase(
    val testName: String,
    private val title: String = testName,
    private val felles: BrevbakerFelles = FellesFactory.fellesAuto,
    private val brevtype: Brevtype = Brevtype.VEDTAKSBREV,
    private val attachments: List<AttachmentTemplate<LangBokmal, EmptyVedleggData>> = emptyList(),
    private val outlineInit: OutlineOnlyScope<LangBokmal, EmptyAutobrevdata>.() -> Unit,
) {
    fun letter(): Letter<EmptyAutobrevdata> =
        LetterImpl(template(), EmptyAutobrevdata, Bokmal, felles)

    private fun template(): LetterTemplate<LangBokmal, EmptyAutobrevdata> =
        createTemplate(
            EmptyAutobrevdata::class,
            languages(Bokmal),
            LetterMetadata(
                displayTitle = testName,
                distribusjonstype = Distribusjonstype.VEDTAK,
                brevtype = brevtype
            )
        ) {
            title {
                text(bokmal { +title })
            }
            outline {
                outlineInit()
            }
            attachments.forEach { includeAttachment(it) }
        }

    override fun toString(): String = testName
}

internal object RenderPDFVisualTestCases {
    val allCases: List<RenderPDFVisualTestCase> =
        buildList {
            add(
                case("two paragraphs in a row") {
                    paragraph { ipsumText() }
                    paragraph { ipsumText() }
                }
            )
            add(
                case("fonts") {
                    paragraph {
                        text(bokmal { +"The quick brown fox jumps over the lazy dog. " }, FontType.PLAIN)
                        text(bokmal { +"The quick brown fox jumps over the lazy dog. " }, FontType.ITALIC)
                        text(bokmal { +"The quick brown fox jumps over the lazy dog. " }, FontType.BOLD)
                    }
                }
            )
            add(
                case("bindestrek") {
                    paragraph {
                        text(bokmal { +"https://www.nav.no/ufore-ettersende-post" }, FontType.PLAIN)
                    }
                    val testString = (50..200).joinToString(" ") {
                        Char(it) + "-" + Char(it + 1)
                    }
                    paragraph {
                        text(bokmal { +testString }, FontType.PLAIN)
                    }
                }
            )
            add(
                case(
                    testName = "verge foersteside",
                    felles = FellesFactory.felles.copy(annenMottaker = "Verge vergeson"),
                ) {
                    testTitle1()
                    paragraph { ipsumText() }
                }
            )
            add(
                case("form choice med vspace") {
                    title1 { text(bokmal { +"Form choice" }) }
                    paragraph {
                        formChoice(prompt = { text(bokmal { +"Hvor lenge har du jobba?" }) }, true) {
                            choice(bokmal { +"0 år" })
                            choice(bokmal { +"1 år" })
                            choice(bokmal { +"2 til 5 år" })
                            choice(bokmal { +"6 år eller mer" })
                        }
                    }
                }
            )
            add(
                case("title should not be put on a separate page") {
                    repeat(14) {
                        paragraph { text(bokmal { +"Padding" }) }
                    }
                    title1 { text(bokmal { +"Title 1" }) }
                    title2 { text(bokmal { +"Title 2" }) }
                    title3 { text(bokmal { +"Title 3" }) }
                }
            )
            add(
                case("form choice uten vspace") {
                    title1 { text(bokmal { +"Form choice uten vspace" }) }
                    paragraph {
                        formChoice(prompt = { text(bokmal { +"Hvor lenge vil du jobbe?" }) }, false) {
                            choice(bokmal { +"0 år" })
                            choice(bokmal { +"1 år" })
                            choice(bokmal { +"2 til 5 år" })
                            choice(bokmal { +"6 år eller mer" })
                        }
                    }
                }
            )
            add(
                case("short form text med vspace") {
                    title1 { text(bokmal { +"Form text short med vspace" }) }
                    paragraph { formText(Size.SHORT, { text(bokmal { +"test" }) }, true) }
                }
            )
            add(
                case("long form text med vspace") {
                    title1 { text(bokmal { +"Form text long med vspace" }) }
                    paragraph { formText(Size.LONG, { text(bokmal { +"test" }) }, true) }
                }
            )
            add(
                case("short form text uten vspace") {
                    title1 { text(bokmal { +"Form text short uten vspace" }) }
                    paragraph { formText(Size.SHORT, { text(bokmal { +"test" }) }, false) }
                }
            )
            add(
                case("long form text uten vspace") {
                    title1 { text(bokmal { +"Form text long uten vspace" }) }
                    paragraph { formText(Size.LONG, { text(bokmal { +"test" }) }, false) }
                }
            )
            add(
                vedleggCase(
                    testName = "verge vedlegg med saksinfo",
                    includeSakspart = true,
                    felles = FellesFactory.felles.copy(annenMottaker = "Verge vergeson"),
                ) {
                    testTitle1()
                    paragraph { ipsumText() }
                }
            )
            add(
                vedleggCase(
                    testName = "vedlegg med saksinfo",
                    includeSakspart = true,
                ) {
                    testTitle1()
                    paragraph { ipsumText() }
                }
            )
            add(
                vedleggCase(
                    testName = "vedlegg uten saksinfo",
                    includeSakspart = false,
                ) {
                    testTitle1()
                    paragraph { ipsumText() }
                }
            )
            add(
                case(
                    testName = "brev med saksbehandler underskrift",
                    felles = FellesFactory.felles.medSignerendeSaksbehandlere(
                        signerendeSaksbehandlere = SignerendeSaksbehandlere(
                            saksbehandler = "Ole Saksbehandler"
                        )
                    ),
                ) {
                    paragraph { ipsumText() }
                }
            )
            add(
                case(
                    testName = "brev med saksbehandler og attestant underskrift",
                    felles = FellesFactory.felles.medSignerendeSaksbehandlere(
                        signerendeSaksbehandlere = SignerendeSaksbehandlere(
                            saksbehandler = "Ole Saksbehandler",
                            attesterendeSaksbehandler = "Per Saksbehandler"
                        )
                    ),
                ) {
                    paragraph { ipsumText() }
                }
            )
            add(
                case(
                    testName = "test av ulike ",
                    felles = FellesFactory.felles.copy(
                        signerendeSaksbehandlere = SignerendeSaksbehandlere(
                            saksbehandler = "Ole Saksbehandler",
                            attesterendeSaksbehandler = "Per Saksbehandler"
                        )
                    ),
                ) {
                    paragraph { ipsumText() }
                }
            )
            add(
                case(
                    testName = "vedtaksbrev med saksbehandler underskrift",
                    felles = FellesFactory.felles.copy(
                        signerendeSaksbehandlere = SignerendeSaksbehandlere(
                            saksbehandler = "Ole Saksbehandler",
                            attesterendeSaksbehandler = "Per Attesterende"
                        )
                    ),
                ) {
                    paragraph { ipsumText() }
                }
            )
            add(
                case("should not add extra line change when nearing end of line before shipping content to new line") {
                    paragraph {
                        text(bokmal { +"Denne testen skal vise at det ikke fremstår en bug hvor det kommer ekstra linjeskift under innhold som tar opp hele bredden av linjen." })
                    }
                    repeat(30) {
                        paragraph {
                            text(
                                bokmal {
                                    +"Denne linjen skal ikke ha større mellomrom til neste setning enn de andre. Dette er littegranne filler ˌˌˌˌˌˌ"
                                    +"ˌ".repeat(it)
                                }
                            )
                        }
                    }
                }
            )
            add(
                case("Table across multiple pages") {
                    paragraph {
                        table(header = {
                            column(columnSpan = 2) { text(bokmal { +"Tekst" }) }
                            column(alignment = RIGHT) { text(bokmal { +"Kroner" }) }
                        }) {
                            for (i in 1..100) {
                                row {
                                    cell { text(bokmal { +"Rad $i" }) }
                                    cell { text(bokmal { +"$i Kroner" }) }
                                }
                            }
                        }
                    }
                }
            )
            add(
                case("Table title should have the correct spacing when sticking to a table that was shipped to the next page") {
                    repeat(5) {
                        paragraph {
                            text(
                                bokmal { +"Padding text bla bla. ".repeat(18) },
                            )
                        }
                    }
                    paragraph {
                        text(bokmal { +"Padding text bla bla. ".repeat(14) })
                    }
                    title1 {
                        text(bokmal { +"Test-tittel" })
                    }
                    paragraph { testTable(5) }
                }
            )
            add(
                case("table title and other content") {
                    paragraph { text(bokmal { +"asdf" }) }
                    title1 { text(bokmal { +"Test-tittel" }) }
                    paragraph { testTable(5) }
                }
            )
            add(
                case("table title and other title") {
                    title1 { text(bokmal { +"Test-tittel" }) }
                    title1 { text(bokmal { +"Test-tittel" }) }
                    paragraph { testTable(5) }
                }
            )
            add(
                case("small text then bullet list") {
                    paragraph {
                        text(
                            bokmal { +"Dette er en liste:" }
                        )
                        testList()
                    }
                }
            )
            ElementType.entries.forEach { elementA ->
                ElementType.entries.forEach { elementB ->
                    add(
                        case("${elementA.description} then ${elementB.description}") {
                            renderOutlineElementOfType(elementA)
                            renderOutlineElementOfType(elementB)
                        }
                    )
                }
            }
        }

    private fun case(
        testName: String,
        title: String = testName,
        felles: BrevbakerFelles = FellesFactory.fellesAuto,
        brevtype: Brevtype = Brevtype.VEDTAKSBREV,
        outlineInit: OutlineOnlyScope<LangBokmal, EmptyAutobrevdata>.() -> Unit,
    ) = RenderPDFVisualTestCase(
        testName = testName,
        title = title,
        felles = felles,
        brevtype = brevtype,
        outlineInit = outlineInit,
    )

    private fun vedleggCase(
        testName: String,
        includeSakspart: Boolean,
        title: String = testName,
        felles: BrevbakerFelles = FellesFactory.fellesAuto,
        vedleggOutlineInit: OutlineOnlyScope<LangBokmal, EmptyVedleggData>.() -> Unit,
    ): RenderPDFVisualTestCase {
        val vedlegg = createAttachment(
            title = { text(bokmal { +title }) },
            includeSakspart = includeSakspart,
        ) {
            vedleggOutlineInit()
        }

        return RenderPDFVisualTestCase(
            testName = testName,
            title = title,
            felles = felles,
            attachments = listOf(vedlegg),
            outlineInit = {},
        )
    }

    private fun ParagraphOnlyScope<LangBokmal, *>.ipsumText() = text(bokmal { +lipsums[1] })

    private fun OutlineOnlyScope<LangBokmal, *>.renderOutlineElementOfType(elementA: ElementType) {
        when (elementA) {
            ElementType.T1 -> testTitle1()
            ElementType.T2 -> testTitle2()
            ElementType.T3 -> testTitle3()
            ElementType.PAR -> paragraph { ipsumText() }
            ElementType.TABLE -> paragraph { testTable() }
            ElementType.LIST -> paragraph { testList() }
            ElementType.NUMBERED_LIST -> paragraph { testNumberedList() }
            ElementType.FORM -> paragraph {
                formText(Size.FILL, {
                    text(bokmal { +"Underskrift:" })
                }, false)
            }
        }
    }

    private fun ParagraphOnlyScope<LangBokmal, *>.testList() {
        list {
            item {
                text(bokmal { +"Bullet point 1" })
            }
            item {
                text(bokmal { +"Bullet point 2" })
            }
        }
    }

    private fun ParagraphOnlyScope<LangBokmal, *>.testNumberedList() {
        numberedList {
            item {
                text(bokmal { +"Numbered point 1" })
            }
            item {
                text(bokmal { +"Numbered point 2" })
            }
        }
    }

    private fun ParagraphOnlyScope<LangBokmal, *>.testTable(numRows: Int = 1) {
        table(
            header = {
                column { text(bokmal { +"Column A" }) }
                column { text(bokmal { +"Column B" }) }
            }
        ) {
            for (i in 1..numRows) {
                row {
                    cell { text(bokmal { +"Cell A-$i" }) }
                    cell { text(bokmal { +"Cell B-$i" }) }
                }
            }
        }
    }

    private fun OutlineOnlyScope<LangBokmal, *>.testTitle1() {
        title1 { text(bokmal { +"First title" }) }
    }

    private fun OutlineOnlyScope<LangBokmal, *>.testTitle2() {
        title2 { text(bokmal { +"Second title" }) }
    }

    private fun OutlineOnlyScope<LangBokmal, *>.testTitle3() {
        title3 { text(bokmal { +"Third title" }) }
    }

    private enum class ElementType(val description: String) {
        T1("Title 1"),
        T2("Title 2"),
        T3("Title 3"),
        PAR("Paragraph"),
        TABLE("Table"),
        LIST("Item list"),
        FORM("Form"),
        NUMBERED_LIST("Numbered list")
    }
}
