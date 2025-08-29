package no.nav.pensjon.brev.maler.ufoereBrev.adhoc

import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.ufoerApi.VarselSaksbehandlingstidAutoDto
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

class FeilBelopInntekstendringsbrev_AvkortetTil0 {
    @TemplateModelHelpers
    object FeilBelopInntekstendringsbrev_AvkortetTil0 : AutobrevTemplate<EmptyBrevdata> {

        override val kode = Pesysbrevkoder.AutoBrev.UT_ADHOC_FEIL_BELOP_INNT_ENDR_AVKORTET_TIL_0

        override val template = createTemplate(
            name = kode.name,
            letterDataType = EmptyBrevdata::class,
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
                    text(bokmal { + "\"Det gir deg rett til en årlig utbetaling av uføretrygd på XXX XXX kroner.\" Dette beløpet er feil." },
                        FontType.ITALIC)
                }

                paragraph {
                    text(bokmal { + "Tallet påvirker ikke din utbetaling og du trenger ikke å gjøre noe. Ønsker du mer informasjon eller har spørsmål, kan du kontakte Nav på nav.no/kontakt. Du kan også ringe oss på 55 55 33 33, alle hverdager mellom kl. 09.00 og 15.00" })
                }
            }
        }
    }
}