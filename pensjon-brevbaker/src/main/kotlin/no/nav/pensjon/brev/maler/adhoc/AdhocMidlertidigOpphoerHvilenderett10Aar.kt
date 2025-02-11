package no.nav.pensjon.brev.maler.adhoc

import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.maler.adhoc.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

object AdhocMidlertidigOpphoerHvilenderett10Aar : AutobrevTemplate<EmptyBrevdata> {
    override val kode = Pesysbrevkoder.AutoBrev.PE_UT_ADHOC_2024_MIDL_OPPHOER_HVILENDE_RETT_10_AAR
    override val template = createTemplate(
        name = kode.name,
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
                Bokmal to "Vedtak om midlertidig stans av uføretrygd etter 10 år med hvilende rett",
            )
        }
        outline {
            paragraph {
                text(
                    Bokmal to "Vi stanser uføretrygden din midlertidig fordi du har hatt 10 år med hvilende rett.",
                )
            }
            paragraph {
                text(
                    Bokmal to "Du har tidligere fått tilsendt brev der vi opplyser at uføretrygden din stanser fra 1.1.2025 hvis du ikke har fått utbetaling av uføretrygd i 2023 og 2024. Det utbetales ikke uføretrygd når den pensjonsgivende inntekten utgjør mer enn 80 prosent av inntekten du hadde før du ble ufør. Dette står i folketrygdloven § 12-14 tredje ledd.",
                )
            }
            paragraph {
                text(
                    Bokmal to "Du har ikke fått utbetaling av uføretrygd i 2023 og 2024 og har hatt 10 sammenhengende år uten utbetaling av uføretrygd. Du har dermed nådd den maksimale grensen for hvor lenge du kan ha hvilende rett til uføretrygd.",
                )
            }
            paragraph {
                text(
                    Bokmal to "Vi stanser derfor uføretrygden din midlertidig. Dette fremkommer av folketrygdloven §12-10 tredje ledd.",
                )
            }
            paragraph {
                text(
                    Bokmal to "Når etteroppgjøret for 2024 er klart må vi vurdere om du skal få et endelig vedtak om stans av uføretrygden din. Vi stanser uføretrygden din midlertidig allerede nå for å unngå feilutbetaling av uføretrygd, og for å være sikker på at det stemmer at du ikke har rett til uføretrygd i 2024.",
                )
            }
            paragraph {
                text(
                    Bokmal to "Dersom du ikke ønsker å beholde retten til uføretrygd, trenger du ikke foreta deg noe.",
                )
            }

            title1 {
                text(
                    Bokmal to "Honnørkort"
                )
            }
            paragraph {
                text(
                    Bokmal to "Du har ikke lenger rett på uføretrygd, og må derfor levere tilbake honnørkortet til Nav-kontoret ditt."
                )
            }

            includePhrase(Felles.RettTilAAKlage(vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk))
            includePhrase(Felles.RettTilInnsyn(vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk))
            includePhrase(Felles.HarDuSpoersmaal.ufoeretrygd)

        }
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlageUfoereStatisk)
    }
}