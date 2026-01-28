package no.nav.pensjon.brev.ufore.maler.hvilenderett

import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder.Redigerbar.UT_HVILENDE_RETT_MIDL_OPPHOER
import no.nav.pensjon.brev.ufore.api.model.maler.Sakstype
import no.nav.pensjon.brev.ufore.maler.fraser.Felles
import no.nav.pensjon.brev.ufore.maler.hvilenderett.HvilendeRettInfo4Aar.fritekst
import no.nav.pensjon.brev.ufore.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object HvilendeRettMidlertidigOppHoer : RedigerbarTemplate<EmptyRedigerbarBrevdata> {

    override val kode = UT_HVILENDE_RETT_MIDL_OPPHOER
    override val kategori = TemplateDescription.Brevkategori.ETTEROPPGJOER
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.UFOREP)

    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak midlertidig opphør hvilende rett 10 år",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title {
            text(
                bokmal { + "Vedtak om midlertidig stans av uføretrygd etter 10 år med hvilende rett" },
            )
        }
        outline {
            paragraph {
                text(
                    bokmal { + "Vi stanser uføretrygden din midlertidig fordi du har hatt 10 år med hvilende rett." },
                )
            }
            paragraph {
                text(
                    bokmal { + "Du har tidligere fått tilsendt brev der vi opplyser at uføretrygden din stanser fra " + fritekst("dato") + " hvis du ikke har fått utbetaling av uføretrygd i " + fritekst("år") + " og " + fritekst("år") + ". Det utbetales ikke uføretrygd når den pensjonsgivende inntekten utgjør mer enn 80 prosent av inntekten du hadde før du ble ufør. Dette står i folketrygdloven § 12-14 tredje ledd." },
                )
            }
            paragraph {
                text(
                    bokmal { + "Du har ikke fått utbetaling av uføretrygd i " + fritekst("år") + " og " + fritekst("år") + " og har hatt 10 sammenhengende år uten utbetaling av uføretrygd. Du har dermed nådd den maksimale grensen for hvor lenge du kan ha hvilende rett til uføretrygd." },
                )
            }
            paragraph {
                text(
                    bokmal { + "Vi stanser derfor uføretrygden din midlertidig. Dette fremkommer av folketrygdloven §12-10 tredje ledd." },
                )
            }
            paragraph {
                text(
                    bokmal { + "Når etteroppgjøret for " + fritekst("år") + " er klart må vi vurdere om du skal få et endelig vedtak om stans av uføretrygden din. Vi stanser uføretrygden din midlertidig allerede nå for å unngå feilutbetaling av uføretrygd, og for å være sikker på at det stemmer at du ikke har rett til uføretrygd i " + fritekst("år") + "." },
                )
            }
            paragraph {
                text(
                    bokmal { + "Dersom du ikke ønsker å beholde retten til uføretrygd, trenger du ikke foreta deg noe." },
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