package no.nav.pensjon.brev.ufore.maler.hvilenderett

import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_S_HVILENDE_RETT_OPPHOER
import no.nav.pensjon.brev.ufore.api.model.maler.Sakstype
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brev.ufore.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object HvilendeRettOppHoer : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    override val kode = UT_S_HVILENDE_RETT_OPPHOER
    override val kategori = TemplateDescription.Brevkategori.ETTEROPPGJOER
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)

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
        outline {
            paragraph {
                text(
                    bokmal { + "Vi opphører uføretrygden din fra " + fritekst("dato") + " fordi du har hatt sammenhengende 10 år med hvilende rett. I tidligere brev har vi opplyst at uføretrygden din stanser fra " + fritekst("dato") + " hvis du ikke har fått utbetaling av uføretrygd i " + fritekst("år") + " og " + fritekst("år") + "." },
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
                    bokmal { + "Du har ikke hatt utbetaling av uføretrygd siden " + fritekst("år") + " og du har hatt 10 år sammenhengende med hvilende rett. Derfor opphører uføretrygden din." },
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

            includePhrase(Felles.RettTilAKlageLang)
            includePhrase(Felles.RettTilInnsynRefVedlegg)
            includePhrase(Felles.HarDuSporsmal)

        }
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk)
    }
}