package no.nav.pensjon.brev.maler.ufoereBrev.adhoc

import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

class FeilBelopInntekstendringsbrev {
    @TemplateModelHelpers
    object FeilBelopInntekstendringsbrev : AutobrevTemplate<EmptyBrevdata> {

        override val kode = Pesysbrevkoder.AutoBrev.UT_ADHOC_FEIL_BELOP_INNT_ENDR

        override val template = createTemplate(
            languages = languages(Bokmal),
            letterMetadata = LetterMetadata(
                displayTitle = "Visning av feil beløp i tidligere inntektsendringsbrev",
                isSensitiv = false,
                distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
                brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
            )
        ) {
            title {
                text(bokmal { + "Visning av feil beløp i tidligere brev" })
            }

            outline {
                paragraph {
                    text(bokmal { + "Du har nylig fått et vedtaksbrev fra oss, hvor vi viser feil beløp for årlig utbetaling av uføretrygd. Vi beklager dette!" })
                }

                paragraph {
                    text(bokmal { + "Tallet påvirker ikke beregningen eller utbetalingen av din uføretrygd. Du får fortsatt utbetalt riktig uføretrygd framover, og du trenger ikke å gjøre noe." })
                }

                title1 {
                    text(bokmal { + "Detaljer om feilen" })
                }

                paragraph {
                    text(bokmal { + "Feilen er i brevet " })
                    text(bokmal { + "\"Vi endrer utbetalingen av uføretrygden/barnetillegget du får\"." }, FontType.ITALIC)
                    text(bokmal { + "Under overskriften " })
                    text(bokmal { + "\"Endring i utbetaling av uføretrygd\"" }, FontType.ITALIC)
                    text(bokmal { + ", står denne teksten:" })
                }

                paragraph {
                    text(bokmal { + "\"Det gir deg rett til en årlig utbetaling av uføretrygd på XXX XXX kroner.\"" },
                        FontType.ITALIC)
                }

                paragraph {
                    text(bokmal { + "Dette beløpet er feil. Du finner det riktige beløpet i vedlegget " })
                    text(bokmal { + "\"Opplysninger om beregningen\"" }, FontType.ITALIC)
                    text(bokmal { + ", under avsnittet " })
                    text(bokmal { + "\"Slik beregner vi reduksjonen av uføretrygden\"" }, FontType.ITALIC)
                    text(bokmal { + ". Beløpet står der som " })
                    text(bokmal { + "\"Brutto beregnet uføretrygd som følge av innmeldt inntekt.\"" }, FontType.ITALIC)
                }
            }
        }
    }
}