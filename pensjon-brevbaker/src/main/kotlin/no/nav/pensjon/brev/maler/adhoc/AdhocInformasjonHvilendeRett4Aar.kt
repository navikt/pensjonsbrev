package no.nav.pensjon.brev.maler.adhoc

import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

object AdhocInformasjonHvilendeRett4Aar : AutobrevTemplate<EmptyBrevdata> {
    override val kode = Brevkode.AutoBrev.PE_UT_ADHOC_2024_INFO_HVILENDE_RETT_4_AAR
    override val template: LetterTemplate<*, EmptyBrevdata> = createTemplate(
        name = kode.name,
        letterDataType = EmptyBrevdata::class,
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Informasjon om hvilende rett til uføretrygd",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            text(
                Bokmal to "Informasjon om hvilende rett til uføretrygd",
            )
        }
        outline {
            paragraph {
                text(
                    Bokmal to "Du har siden 2020 ikke fått utbetaling av uføretrygd fordi inntekten din har vært over 80 prosent av oppjustert inntekt før du ble ufør. Du har derfor fått innvilget en hvilende rett.",
                )
            }
            paragraph {
                text(
                    Bokmal to "Uføretrygden utbetales ikke når den pensjonsgivende inntekten utgjør mer enn 80 prosent av inntekten før du ble ufør. Dette står i folketrygdloven § 12-14 tredje ledd.",
                )
            }
            paragraph {
                text(
                    Bokmal to "Når uføretrygden faller bort etter reglene i folketrygdloven § 12-14 tredje ledd, kan en beholde retten til uføretrygd med opprinnelig uføregrad i inntil ti år ved å melde fra til Arbeids- og velferdsetaten. Dette kalles hvilende rett og står i folketrygdloven § 12-10 tredje ledd.",
                )
            }
            paragraph {
                text(
                    Bokmal to "Du har ikke hatt utbetaling av uføretrygd siden 2020. Du kan derfor ha en hvilende rett til uføretrygd i ytterligere fem år før uføretrygden opphøres. Ved opphør av uføretrygden vil du få et eget varsel og vedtak om dette.",
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du ønsker en ny periode på 5 år, trenger du ikke å kontakte oss om dette. Vi forutsetter da at du ønsker å beholde retten til uføretrygd i ytterligere fem år.",
                )
            }
            paragraph {
                text(
                    Bokmal to "Om du senere har en årlig inntekt under 80 prosent av oppjustert inntekt før uførhet, vil du igjen ha rett til utbetaling av uføretrygd. Du vil da opparbeide deg rett til en ny periode med hvilende rett.",
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du ønsker å motta uføretrygd igjen eller forventer lavere inntekt fremover gjelder følgende:",
                )
                list { 
                    item {
                        text(
                            Bokmal to "Du korrigerer selv forventet inntekt i Inntektsplanleggeren din. Denne finner du på Ditt NAV // Din Uføretrygd // Inntektsplanlegger.",
                        )
                    }
                    item {
                        text(
                            Bokmal to "Når du registrerer annen forventet inntekt via Inntektsplanleggeren, som gir ny beregning, vil det dannes et nytt brev i innboksen din.",
                        )
                    }
                    item {
                        text(
                            Bokmal to "Endringen vil gjelde fra kalendermåneden etter endringen av inntekt.",
                        )
                    }
                }
            }
            includePhrase(Felles.HarDuSpoersmaal.ufoeretrygd)
        }
    }
}