package no.nav.pensjon.brev.maler.example

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Form.Text.Size
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.choice
import no.nav.pensjon.brev.template.dsl.createTemplate
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
        name = "ENKELT_REDIGERTBART_TESTBREV",
        letterDataType = EmptyRedigerbarBrevdata::class,
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Displaytittelen",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.ANNET,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title {
            text(
                Bokmal to "Tittelen",
                Nynorsk to "Tittelen"
            )
        }

        outline {
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
                list {
                    item {
                        text(Bokmal to "Test1", Nynorsk to "Test1")
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
                        column(1) { text(Bokmal to "Kolonne 1", Nynorsk to "Kolonne 1") }
                    }
                ) {
                    row {
                        cell {
                            text(
                                Bokmal to "Din inntekt før skatt i måned 1",
                                Nynorsk to "Din inntekt før skatt i måned 1"
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