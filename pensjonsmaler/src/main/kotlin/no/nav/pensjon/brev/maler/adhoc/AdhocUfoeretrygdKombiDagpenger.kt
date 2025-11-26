package no.nav.pensjon.brev.maler.adhoc

import no.nav.pensjon.brev.api.model.maler.EmptyAutobrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata


object AdhocUfoeretrygdKombiDagpenger : AutobrevTemplate<EmptyAutobrevdata> {
    override val kode = Pesysbrevkoder.AutoBrev.UT_ADHOC_UFOERETRYGD_KOMBI_DAGPENGER
    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Informasjon om etterbetaling av dagpenger og betydningen for uføretrygd",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        )
    ) {
        title {
            text(
                bokmal { + "Informasjon om etterbetaling av dagpenger" },
            )
        }
        outline {
            paragraph {
                text(
                    bokmal { + "Nav etterbetaler dagpenger til medlemmer som fikk feil beregningsgrunnlag for dagpenger i perioden 2019 til og med desember 2021. "
                            + "Du har mottatt eget brev om dette, og vi viser til det brevet for nærmere informasjon om etterbetalingen av dagpenger." },
                )
            }
            paragraph {
                text(
                    bokmal { + "I dette brevet gir vi deg informasjon om hvordan etterbetalingen av dagpenger påvirker din uføretrygd." },
                )
            }
            title1 {
                text(
                    bokmal { + "Hvilken betydning har etterbetalingen av dagpenger for din uføretrygd?" },
                )
            }
            paragraph {
                text(
                    bokmal { + "Du har blitt innvilget uføretrygd etter desember 2021. Pensjonsgivende inntekt som stammer fra perioden før man blir innvilget uføretrygd skal ikke føre til avkortning av uføretrygden. "
                            + "Etterbetalingen av dagpenger skal derfor ikke påvirke størrelsen på din uføretrygd, og du trenger ikke foreta deg noe. Dersom du mottar barnetillegg i din uføretrygd, "
                            + "fører etterbetalingen av dagpenger heller ikke til reduksjon av barnetillegget. Dette følger av folketrygdloven §§ 12-14 og 12-16." }
                )
            }
            paragraph {
                text(
                    bokmal { + "Har du annen pensjonsgivende inntekt må denne legges inn i inntektsplanleggeren på Dine sider på $NAV_URL på vanlig måte." }
                )
            }
            includePhrase(Felles.HarDuSpoersmaal(NAV_URL, Constants.NAV_KONTAKTSENTER_TELEFON))
        }
    }
}