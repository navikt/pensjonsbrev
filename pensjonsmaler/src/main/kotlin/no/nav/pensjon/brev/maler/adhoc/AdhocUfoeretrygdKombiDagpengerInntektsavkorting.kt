package no.nav.pensjon.brev.maler.adhoc

import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata


object AdhocUfoeretrygdKombiDagpengerInntektsavkorting : AutobrevTemplate<EmptyBrevdata> {
    override val kode = Pesysbrevkoder.AutoBrev.UT_ADHOC_UFOERETRYGD_KOMBI_DAGPENGER_AVKORTNING
    override val template: LetterTemplate<*, EmptyBrevdata> = createTemplate(
        name = kode.name,
        letterDataType = EmptyBrevdata::class,
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
                    bokmal { + "Nav etterbetaler dagpenger til personer som fikk feil beregningsgrunnlag for dagpenger i perioden 2019 til og med desember 2021. "
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
                    bokmal { + "Du kombinerte uføretrygd og dagpenger i hele eller deler av perioden 2019 til og med desember 2021." }
                )
            }
            paragraph {
                text(
                    bokmal { + "Etterbetalingen av dagpenger er pensjonsgivende inntekt som kan føre til reduksjon av uføretrygden, dersom inntekten overstiger inntektsgrensen. "
                            + "Eventuell reduksjon av uføretrygden og barnetillegget avhenger av størrelsen på etterbetalingen av dagpenger. "
                            + "Dette gjelder selv om etterbetalingen stammer fra en tidligere periode, da det er tidspunktet for utbetaling som er avgjørende. "
                            + "Du vil få eget vedtak dersom utbetalingen av uføretrygden og barnetillegget reduseres. Dette følger av folketrygdloven §§ 12-14 og 12-16." }
                )
            }
            paragraph {
                text(
                    bokmal { + "Det er derfor viktig at du legger inn etterbetalingen av dagpenger i inntektsplanleggeren på Dine sider på $NAV_URL. Dette vil gi riktig utbetaling av uføretrygd." }
                )
            }
            paragraph {
                text(
                    bokmal { + "Annen pensjonsgivende inntekt vil som vanlig kunne føre til reduksjon av uføretrygden dersom inntektsgrensen overstiges." }
                )
            }
            includePhrase(Felles.HarDuSpoersmaal(Constants.NAV_URL, Constants.NAV_KONTAKTSENTER_TELEFON))
        }
    }
}