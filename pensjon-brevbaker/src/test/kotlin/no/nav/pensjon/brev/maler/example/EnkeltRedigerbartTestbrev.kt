package no.nav.pensjon.brev.maler.example

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Form.Text.Size
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.choice
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata


@TemplateModelHelpers
object EnkeltRedigerbartTestbrev : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    override val kode: Brevkode.Redigerbart = EnkeltRedigerbartTestbrevBrevkode.ENKELT_REDIGERBART_TESTBREV
    override val kategori = TemplateDescription.Brevkategori.INNHENTE_OPPLYSNINGER
    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.SAK
    override val sakstyper: Set<Sakstype> = Sakstype.all

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Displaytittelen",
            distribusjonstype = LetterMetadata.Distribusjonstype.ANNET,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title {
            text(
                bokmal { +"Tittelen" },
                nynorsk { +"Tittelen" }
            )
        }

        outline {
            title1 {
                text(bokmal { +"Du har fått innvilget pensjon" }, nynorsk { +"Du har fått innvilget pensjon" })
            }

            title2 {
                text(bokmal { +"Her er din title2" }, nynorsk { +"Her er din title2" })
            }

            title3 {
                text(bokmal { +"Her er din title3" }, nynorsk { +"Her er din title3" })
            }

            paragraph {
                text(
                    bokmal { +"Du kan klage på vedtaket innen seks uker fra du mottok det. Kontoret som har fattet vedtaket, vil da vurdere saken din på nytt." },
                    nynorsk { +"Du kan klage på vedtaket innen seks uker fra du mottok det. Kontoret som har fattet vedtaket, vil da vurdere saken din på nytt." }
                )
                text(bokmal { +" Kanskje." }, nynorsk { +" Kanskje." }, fontType = FontType.BOLD)
            }

            paragraph {
                list {
                    item {
                        text(bokmal { +"Test1" }, nynorsk { +"Test1" })
                    }
                }
                formText(size = Size.LONG, newText(Bokmal to "Formtittel1", Nynorsk to "Formtittel1"))
                formChoice(newText(Bokmal to "Formtittel2", Nynorsk to "Formtittel 2")) {
                    choice(Bokmal to "Valg1", Nynorsk to "Valg1")
                }
            }

            paragraph {
                table(
                    header = {
                        column(1) { text(bokmal { +"Kolonne 1" }, nynorsk { +"Kolonne 1" }) }
                    }
                ) {
                    row {
                        cell {
                            text(
                                bokmal { +"Din inntekt før skatt i måned 1" },
                                nynorsk { +"Din inntekt før skatt i måned 1" }
                            )
                        }
                    }
                }
            }
        }
        includeAttachment(testVedlegg, TestVedleggDto("test1", "test2").expr())
    }
}

enum class EnkeltRedigerbartTestbrevBrevkode : Brevkode.Redigerbart {
    ENKELT_REDIGERBART_TESTBREV;

    override fun kode() = name
}