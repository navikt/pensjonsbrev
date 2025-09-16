package no.nav.pensjon.brev.maler.ufoereBrev.hvilenderett

import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.maler.adhoc.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

object HvilendeRettOppHoer : AutobrevTemplate<EmptyBrevdata> {
    override val kode = Pesysbrevkoder.AutoBrev.UT_HVILENDE_RETT_OPPHOER
    override val template: LetterTemplate<*, EmptyBrevdata> = createTemplate(
        letterDataType = EmptyBrevdata::class,
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak midlertidig opphør hvilende rett 10 år",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title {
            text(
                bokmal { + "Uføretrygden din opphører" },
            )
        }
        outline {
            paragraph {
                text(
                    bokmal { + "Vi opphører uføretrygden din fra 1.1.2025 fordi du har hatt sammenhengende 10 år med hvilende rett. I tidligere brev har vi opplyst at uføretrygden din stanser fra 1.1.2025 hvis du ikke har fått utbetaling av uføretrygd i 2023 og 2024." },
                )
            }
            title1 {
                text(
                    bokmal { + "Derfor opphører uføretrygden din" }
                )
            }
            paragraph {
                text(
                    bokmal { + "Uføretrygden utbetales ikke når pensjonsgivende inntekt er høyere enn 80 prosent av inntekten du hadde før du ble ufør. Retten til uføretrygd kan likevel beholdes i inntil 10 år, og dette kalles hvilende rett." },
                )
            }
            paragraph {
                text(
                    bokmal { + "Du har ikke hatt utbetaling av uføretrygd siden 2015 og du har hatt 10 år sammenhengende med hvilende rett. Derfor opphører uføretrygden din." },
                )
            }
            paragraph {
                text(
                    bokmal { + "Dersom du på et senere tidspunkt mener du igjen har rett til uføretrygd, må du søke om uføretrygd på nytt." },
                )
            }
            paragraph {
                text(
                    bokmal { + "Vedtaket har vi gjort etter folketrygdlovens § 12-14 tredje ledd og § 12-10 tredje ledd." },
                )
            }
            title1 {
                text(
                    bokmal { + "Honnørkort" }
                )
            }
            paragraph {
                text(
                    bokmal { + "Du har ikke lenger rett på uføretrygd, og må derfor levere tilbake honnørkortet til Nav-kontoret ditt." }
                )
            }

            includePhrase(Felles.RettTilAAKlage(vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk))
            includePhrase(Felles.RettTilInnsyn(vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk))
            includePhrase(Felles.HarDuSpoersmaal.ufoeretrygd)

        }
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk)
    }
}