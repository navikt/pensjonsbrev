package no.nav.pensjon.brev.maler.adhoc

import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata


object AdhocUfoeretrygdEtterbetalingDagpenger : AutobrevTemplate<EmptyBrevdata> {
    override val kode = Pesysbrevkoder.AutoBrev.UT_ADHOC_UFOERETRYGD_ETTERBETALING_DAGPENGER
    override val template: LetterTemplate<*, EmptyBrevdata> = createTemplate(
        name = kode.name,
        letterDataType = EmptyBrevdata::class,
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
                Bokmal to "Ny informasjon om etterbetaling av dagpenger"
            )
        }
        outline {
            paragraph {
                text(
                    Bokmal to "Du har tidligere fått et brev fra oss med informasjon om hvordan etterbetaling av dagpenger påvirker din uføretrygd. "
                            + "I brevet fikk du informasjon om at etterbetaling av dagpenger er pensjonsgivende inntekt som kan føre til reduksjon av uføretrygden, "
                            + "dersom den overstiger inntektsgrensen."
                )
            }
            paragraph {
                text(
                    Bokmal to "Etter at vi sendte brevet, har reglene endret seg til fordel for deg. Det er Arbeids- og inkluderingsdepartementet som har bestemt de nye reglene."
                )
            }
            paragraph {
                text(
                    Bokmal to "Regelendringen gjør at etterbetaling av trygdeytelser ikke lenger skal medføre reduksjon i uføretrygden."
                )
            }
            paragraph {
                text(
                    Bokmal to "Etterbetalingen av dagpenger skal derfor ikke påvirke størrelsen på uføretrygden din fremover, og du trenger ikke å gjøre noe i denne saken."
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du mottar barnetillegg i uføretrygden din, fører etterbetalingen av dagpenger heller ikke til reduksjon av barnetillegget fremover."
                )
            }
            paragraph {
                text(
                    Bokmal to "Etterbetalingen av dagpenger "
                )
                text(
                    Bokmal to "kan",
                            FontType.ITALIC
                )
                text(
                    Bokmal to " likevel medføre reduksjon av barnetillegg for felles barn hvis det er den andre forelderen får utbetalt barnetillegget."
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis din uføretrygd eller barnetillegg allerede har blitt redusert som følge av etterbetalingen av dagpenger, vil dette bli rettet opp i etteroppgjøret for 2024."
                )
            }
            paragraph {
                text(
                    Bokmal to "Har du annen pensjonsgivende inntekt må denne legges inn i inntektsplanleggeren på Dine side på nav.no på vanlig måte."
                )
            }
            includePhrase(Felles.HarDuSpoersmaal(Constants.NAV_URL, Constants.NAV_KONTAKTSENTER_TELEFON))
        }
    }
}