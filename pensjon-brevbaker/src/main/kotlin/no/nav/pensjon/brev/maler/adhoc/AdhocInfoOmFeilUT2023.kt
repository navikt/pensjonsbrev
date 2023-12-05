package no.nav.pensjon.brev.maler.adhoc

import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.maler.fraser.ufoer.Ufoeretrygd
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

object AdhocInfoOmFeilUT2023 : AutobrevTemplate<EmptyBrevdata> {

    override val kode: Brevkode.AutoBrev = Brevkode.AutoBrev.UT_ADHOC_2023_INFORMASJON_OM_FEIL
    override val template: LetterTemplate<*, EmptyBrevdata> = createTemplate(
        name = kode.name,
        letterDataType = EmptyBrevdata::class,
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Inntektsgrensen i uføretrygdsaken din blir satt ned",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            text(
                Bokmal to "Inntektsgrensen i uføretrygdsaken din blir satt ned",
            )
        }
        outline {
            paragraph {
                text(
                    Bokmal to "Fra 1. januar 2024 vil NAV sette ned grensen for hva du kan tjene før utbetalingen av uføretrygden din blir redusert. " +
                            "Du vil få et eget vedtak med informasjon om den nye inntektsgrensen din fra 1. januar 2024. " +
                            "Du vil få dette brevet før utbetalingen i januar måned.",
                )
            }
            title1 {
                text(
                    Bokmal to "Hvorfor setter vi ned inntektsgrensen?",
                )
            }
            paragraph {
                text(
                    Bokmal to "Ved en feil ble ikke inntektsgrensen din endret ved overgang fra uførepensjon til uføretrygd fra 2015. " +
                            "Da du mottok uførepensjon hadde du rett til en høyere inntektsgrense fordi du hadde rett til et tillegg på 10 prosent av grunnbeløpet.",
                )
            }
            paragraph {
                text(
                    Bokmal to "Fra 1. januar 2015 skulle dette tillegget i inntektsgrensen ikke vært videreført i uføretrygden. " +
                            "Fra denne datoen ble forskriften som ga rett på dette tillegget opphevet.",
                )
            }
            paragraph {
                text(
                    Bokmal to "NAV reduserte ikke inntektsgrensen din etter at forskriften om økt inntektsgrense ble opphevet." +
                            " Vi vil sette inntektsgrensen riktig framover i tid.",
                )
            }
            title1 {
                text(
                    Bokmal to "Hvis du vil vite mer om regelverket",
                )
            }
            paragraph {
                text(
                    Bokmal to "Før 1. januar 2015 var grensen for hva du kunne tjene før uførepensjonen ble avkortet, økt med 10 prosent av grunnbeløpet. " +
                            "Dette fulgte av en forskrift til folketrygdloven § 25-13.",
                )
            }
            paragraph {
                text(
                    Bokmal to "Forskriften ble opphevet fra 1. januar 2015, samtidig som uførepensjon ble omregnet til uføretrygd. " +
                            "Store deler av regelverket for uføreytelser ble da endret.",
                )
                list {
                    item {
                        text(
                            Bokmal to "Folketrygdloven § 12-14 - Reduksjon av uføretrygd på grunn av inntekt",
                        )
                    }
                    item {
                        text(
                            Bokmal to "Folketrygdloven § 25-13 – Forsøksvirksomhet – avvik ved loven",
                        )
                    }
                    item {
                        text(
                            Bokmal to "Forskrift om forsøksvirksomhet for reaktivisering av uførepensjonister (gjaldt i perioden 1. september 2001 til 31. desember 2014)",
                        )
                    }
                }
            }
            includePhrase(Ufoeretrygd.HarDuSpoersmaalUfoeretrygd)
        }
    }
}