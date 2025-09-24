package no.nav.pensjon.brev.maler.adhoc

import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata


object AdhocUfoeretrygdEtterbetalingDagpenger : AutobrevTemplate<EmptyBrevdata> {
    override val kode = Pesysbrevkoder.AutoBrev.UT_ADHOC_UFOERETRYGD_ETTERBETALING_DAGPENGER
    override val template: LetterTemplate<*, EmptyBrevdata> = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Ny informasjon om etterbetaling av dagpenger og betydningen for uføretrygd",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            text(
                bokmal { + "Ny informasjon om etterbetaling av dagpenger" }
            )
        }
        outline {
            paragraph {
                text(
                    bokmal { + "Du har tidligere fått et brev fra oss med informasjon om hvordan etterbetaling av dagpenger påvirker din uføretrygd. "
                            + "I brevet fikk du informasjon om at etterbetaling av dagpenger er pensjonsgivende inntekt som kan føre til reduksjon av uføretrygden, "
                            + "dersom den overstiger inntektsgrensen." }
                )
            }
            paragraph {
                text(
                    bokmal { + "Etter at vi sendte brevet, har reglene endret seg til fordel for deg. Det er Arbeids- og inkluderingsdepartementet som har bestemt de nye reglene." }
                )
            }
            paragraph {
                text(
                    bokmal { + "Regelendringen gjør at etterbetaling av trygdeytelser ikke lenger skal medføre reduksjon i uføretrygden." }
                )
            }
            paragraph {
                text(
                    bokmal { + "Etterbetalingen av dagpenger skal derfor ikke påvirke størrelsen på uføretrygden din fremover, og du trenger ikke å gjøre noe i denne saken." }
                )
            }
            paragraph {
                text(
                    bokmal { + "Hvis du mottar barnetillegg i uføretrygden din, fører etterbetalingen av dagpenger heller ikke til reduksjon av barnetillegget fremover." }
                )
            }
            paragraph {
                text(
                    bokmal { + "Etterbetalingen av dagpenger " }
                )
                text(
                    bokmal { + "kan" },
                            FontType.ITALIC
                )
                text(
                    bokmal { + " likevel medføre reduksjon av barnetillegg for felles barn hvis det er den andre forelderen får utbetalt barnetillegget." }
                )
            }
            paragraph {
                text(
                    bokmal { + "Hvis din uføretrygd eller barnetillegg allerede har blitt redusert som følge av etterbetalingen av dagpenger, vil dette bli rettet opp i etteroppgjøret for 2024." }
                )
            }
            paragraph {
                text(
                    bokmal { + "Har du annen pensjonsgivende inntekt må denne legges inn i inntektsplanleggeren på Dine side på $NAV_URL på vanlig måte." }
                )
            }
            includePhrase(Felles.HarDuSpoersmaal(NAV_URL, Constants.NAV_KONTAKTSENTER_TELEFON))
        }
    }
}