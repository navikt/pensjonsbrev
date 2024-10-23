package no.nav.pensjon.brev.maler.adhoc

import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

object AdhocVarselOpphoerMedHvilendeRett : AutobrevTemplate<EmptyBrevdata> {
    override val kode: Brevkode.AutoBrev = Brevkode.AutoBrev.UT_ADHOC_VARSEL_OPPHOER_MED_HVILENDE_RETT
    override val template: LetterTemplate<*, EmptyBrevdata> = createTemplate(
        name = kode.name,
        letterDataType = EmptyBrevdata::class,
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Varsel om opphør av uføretrygden etter 10 år med hvilende rett",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            text(
                Bokmal to "Varsel om opphør av uføretrygden etter 10 år med hvilende rett",
            )
        }
        outline {
            paragraph {
                text(
                    Bokmal to "Dette er et varsel om at uføretrygden din blir opphørt fra 1.1.2025 hvis du ikke har rett " +
                            "til utbetaling av uføretrygd i 2023 og 2024."
                )
            }
            paragraph {
                text(
                    Bokmal to "Du har siden 2015 ikke fått utbetaling av uføretrygd fordi din inntekt har vært over 80 prosent " +
                            "av oppjustert inntekt før uførhet.  Du har derfor hatt innvilget en hvilende rett."
                )
            }
            paragraph {
                text(
                    Bokmal to "Det utbetales ikke uføretrygd når den pensjonsgivende inntekten utgjør mer enn 80 prosent av inntekt før uførhet. " +
                            "Dette står i folketrygdloven § 12-14 tredje ledd."
                )
            }
            paragraph {
                text(
                    Bokmal to "Dersom uføretrygden faller bort etter reglene i folketrygdloven § 12-14 tredje ledd, " +
                            "kan en beholde retten til uføretrygd med opprinnelig uføregrad i inntil ti år (hvilende rett) " +
                            "ved å melde fra til Arbeids- og velferdsetaten. Dette står i folketrygdloven § 12-10 tredje ledd."
                )
            }
            paragraph {
                text(
                    Bokmal to "Du har ikke hatt utbetaling av uføretrygd siden 2015. Har du heller ikke rett til utbetaling " +
                            "av uføretrygd i 2023 og 2024, vil retten til uføretrygd opphøre fra 1.1.2025. " +
                            "Ved opphør av uføretrygden, vil du få eget vedtak om dette."
                )
            }
            paragraph {
                text(
                    Bokmal to "Har du en årlig inntekt under 80 prosent av oppjustert inntekt før uførhet i 2023 eller 2024, " +
                            "vil du igjen ha rett til utbetaling av uføretrygd. Du vil da opparbeide deg retten til en ny periode med hvilende rett."
                )
            }
            includePhrase(Felles.HarDuSpoersmaal(Constants.UFOERETRYGD_URL, Constants.NAV_KONTAKTSENTER_TELEFON))
        }
    }
}