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
                text(Bokmal to "Visning av feil beløp i tidligere brev")
            }

            outline {
                paragraph {
                    text(Bokmal to "Du har nylig fått et vedtaksbrev fra oss, hvor vi viser feil beløp for årlig utbetaling av uføretrygd. Vi beklager dette!")
                }

                paragraph {
                    text(Bokmal to "Tallet påvirker ikke beregningen eller utbetalingen av din uføretrygd. Du får fortsatt utbetalt riktig uføretrygd framover, og du trenger ikke å gjøre noe.")
                }

                title1 {
                    text(Bokmal to "Detaljer om feilen")
                }

                paragraph {
                    text(Bokmal to "Feilen er i brevet ")
                    text(Bokmal to "\"Vi endrer utbetalingen av uføretrygden/barnetillegget du får\".", FontType.ITALIC)
                    text(Bokmal to "Under overskriften ")
                    text(Bokmal to "\"Endring i utbetaling av uføretrygd\"", FontType.ITALIC)
                    text(Bokmal to ", står denne teksten:")
                }

                paragraph {
                    text(Bokmal to "\"Det gir deg rett til en årlig utbetaling av uføretrygd på XXX XXX kroner.\"",
                        FontType.ITALIC)
                }

                paragraph {
                    text(Bokmal to "Dette beløpet er feil.")
                }
            }
        }
    }
}