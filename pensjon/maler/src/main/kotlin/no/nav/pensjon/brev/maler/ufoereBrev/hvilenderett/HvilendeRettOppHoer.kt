package no.nav.pensjon.brev.maler.ufoereBrev.hvilenderett

import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.ufoerApi.HvilendeRettUforetrygdDto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.HvilendeRettUforetrygdDtoSelectors.senesteHvilendeAr
import no.nav.pensjon.brev.maler.adhoc.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.minus
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object HvilendeRettOppHoer : AutobrevTemplate<HvilendeRettUforetrygdDto> {
    override val kode = Pesysbrevkoder.AutoBrev.UT_HVILENDE_RETT_OPPHOER
    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak om opphør uføretrygd hvilende rett",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title {
            text(
                bokmal { + "Uføretrygden din opphører" },
            )
        }
        val forsteHvilendeAr = senesteHvilendeAr.minus(9).format()
        val nestSisteHvilendeAr = senesteHvilendeAr.minus(1).format()
        val sisteHvilendeAr = senesteHvilendeAr.format()
        val opphorAr = senesteHvilendeAr.plus(1).format()
        outline {
            paragraph {
                text(
                    bokmal { + "Vi opphører uføretrygden din fra 1.1." + opphorAr + " fordi du har hatt sammenhengende 10 år med hvilende rett. I tidligere brev har vi opplyst at uføretrygden din stanser fra 1.1." + opphorAr + " hvis du ikke har fått utbetaling av uføretrygd i " + nestSisteHvilendeAr + " og " + sisteHvilendeAr + "." },
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
                    bokmal { + "Du har ikke hatt utbetaling av uføretrygd siden " + forsteHvilendeAr + " og du har hatt 10 år sammenhengende med hvilende rett. Derfor opphører uføretrygden din." },
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

            includePhrase(Felles.RettTilAAKlage)
            includePhrase(Felles.RettTilInnsyn(vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk))
            includePhrase(Felles.HarDuSpoersmaal.ufoeretrygd)

        }
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk)
    }
}