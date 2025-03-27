package no.nav.pensjon.brev.maler.fraser.vedlegg

import no.nav.pensjon.brev.api.model.maler.redigerbar.VarselTilbakekrevingAvFeilutbetaltBeloepDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.VarselTilbakekrevingAvFeilutbetaltBeloepDtoSelectors.SaksbehandlerValgSelectors.hvisGjenlevendepensjonOgForsoergningstillegg
import no.nav.pensjon.brev.api.model.maler.redigerbar.VarselTilbakekrevingAvFeilutbetaltBeloepDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.*

// Det ble aldri laget engelsk/nynorsk tekster for dette vedlegget.
// Bør gjøres ettersom brevet er tilgjengelig på engelsk og nynorsk
val vedleggVarselTilbakekreving =
    createAttachment<LangBokmalNynorskEnglish, VarselTilbakekrevingAvFeilutbetaltBeloepDto>(
        title = newText(
            Bokmal to "Rettslig grunnlag for tilbakekreving",
            Nynorsk to "Rettslig grunnlag for tilbakekreving",
            English to "Rettslig grunnlag for tilbakekreving",
        ),
        includeSakspart = false,
    ) {
        title1 {
            universalText("§ 22-15. Tilbakekreving etter feilaktig utbetaling")
        }

        paragraph {
            universalText("En utbetaling som Arbeids- og velferdsetaten, Helsedirektoratet eller organ underlagt Helsedirektoratet har foretatt til noen som ikke hadde krav på den, kan kreves tilbake dersom den som har fått utbetalingen (mottakeren) eller noen som opptrådte på vegne av mottakeren forsto eller burde ha forstått at utbetalingen skyldtes en feil. Det samme gjelder dersom vedkommende har forårsaket utbetalingen ved forsettlig eller uaktsomt å gi feilaktige eller mangelfulle opplysninger.")
        }

        paragraph {
            universalText("Krav etter første ledd skal rettes mot mottakeren av feilutbetalingen. Dersom kravet ikke dekkes hos mottakeren, kan det rettes mot noen som har opptrådt på vegne av mottakeren og som har utvist forsett eller uaktsomhet som angitt i første ledd. På tilsvarende vilkår kan krav om tilbakebetaling av en ytelse utbetalt til en arbeidsgiver etter § 22-3 eller etter særskilt avtale, rettes mot en arbeidstaker.")
        }

        paragraph {
            universalText("Dersom det etterbetales lønn, eller erstatning for lønn, for tidsrom som det allerede er utbetalt arbeidsavklaringspenger eller dagpenger under arbeidsløshet for, kan det for meget utbetalte kreves tilbake. Dersom ytelser etter kapitlene 4 og 11 er utbetalt som forskudd, kan det for meget utbetalte kreves tilbake.")
        }

        paragraph {
            universalText("Det skal settes fram krav om tilbakebetaling etter første til tredje ledd med mindre særlige grunner taler mot det. Det legges blant annet vekt på graden av uaktsomhet hos den som kravet retter seg mot, størrelsen av det feilutbetalte beløpet, hvor lang tid det er gått siden utbetalingen fant sted og om feilen helt eller delvis kan tilskrives Arbeids- og velferdsetaten, Helsedirektoratet eller organ underlagt Helsedirektoratet. Tilbakebetalingskravet kan herunder settes til en del av det feilutbetalte beløpet. Når den som kravet retter seg mot har opptrådt forsettlig, skal krav alltid fremmes, og beløpet kan ikke settes ned.")
        }

        paragraph {
            universalText("Feilutbetalte beløp som er mottatt i aktsom god tro, kan kreves tilbake, begrenset til det beløp som er i behold når vedkommende blir kjent med feilen. Ved vurderingen av om dette beløpet helt eller delvis skal kreves tilbake, legges det blant annet vekt på størrelsen av det feilutbetalte beløpet, hvor lang tid det er gått siden feilutbetalingen fant sted og om vedkommende har innrettet seg i tillit til den.")
        }

        paragraph {
            universalText("Tilbakekreving etter paragrafen her kan unnlates dersom det feilutbetalte beløpet utgjør mindre enn fire ganger rettsgebyret. Dette gjelder likevel ikke dersom den som har fått utbetalingen (mottakeren) eller noen som opptrådte på vegne av mottakeren, har opptrådt  forsettlig eller grovt uaktsomt.")
        }

        paragraph {
            universalText("Nasjonalt klageorgan for helsetjenesten er klageinstans for vedtak om tilbakekreving etter paragrafen her fattet av Helsedirektoratet eller det organ Helsedirektoratet bestemmer. Vedtak om tilbakekreving etter paragrafen her er tvangsgrunnlag for utlegg. Kravet kan innkreves ved trekk i framtidige trygdeytelser eller inndrives etter reglene i bidragsinnkrevingsloven av Innkrevingssentralen for bidrag og tilbakebetalingskrav eller, for så vidt gjelder ytelser etter kapittel 5, av det organ som Helsedirektoratet bestemmer.")
        }

        paragraph {
            universalText("Feilutbetalinger etter direkte oppgjørsordninger kreves tilbake etter reglene i § 22-15 a.")
        }

        showIf(saksbehandlerValg.hvisGjenlevendepensjonOgForsoergningstillegg) {
            title1 {
                universalText("§ 22-16. Avregning av feilutbetalinger som skyldes for høy inntekt")
            }

            paragraph {
                universalText("Når en ytelse som nevnt i andre ledd utbetales med for høyt beløp fordi mottakerens inntekt er høyere enn forutsatt ved fastsettingen av ytelsen, skal det som etter reglene om inntektsprøving, meldeplikt og nedsetting av ytelser på grunn av endrede forhold er utbetalt for mye, avregnes ved trekk i framtidige ytelser. Dersom feilutbetalingen helt eller delvis kan tilskrives Arbeids- og velferdsetaten, kan kravet settes ned.")
            }

            paragraph {
                universalText("Avregningsordningen gjelder ved feilutbetaling av:")

                list {
                    item {
                        universalText("ektefelletillegg og barnetillegg som kan reduseres etter § 3-26,")
                    }
                }

                list {
                    item {
                        universalText("etterlattepensjon og overgangsstønad etter kapitlene 15, 16 og 17,")
                    }
                }

                list {
                    item {
                        universalText("uføretrygd etter kapittel 12.")
                    }
                }
            }

            paragraph {
                universalText("Avregning etter første ledd skjer normalt med opptil 10 prosent av samlet månedlig ytelse, men kan settes til et høyere beløp. Trekk kan foretas i ytelser som nevnt i andre ledd, i alderspensjon og i krigspensjon og pensjon etter yrkesskadetrygdloven. Det kan foretas trekk også etter overgang fra en av disse ytelsene til en annen, og feilutbetalte forsørgingstillegg kan avregnes i pensjon mv. etter at tilleggene er falt bort.")
            }

            paragraph {
                universalText("Avregning etter paragrafen her gjøres av Innkrevingssentralen for bidrag og tilbakebetalingskrav.")
            }
        }

        title1 {
            universalText("§ 22-17 a. Renter og rentetillegg ved tilbakekreving av feilutbetalinger")
        }

        paragraph {
            universalText("Ved tilbakekreving etter § 22-15 første og andre ledd på grunnlag av forsett eller grov uaktsomhet hos den som kravet retter seg mot, beregnes et rentetillegg på 10 prosent av det beløp som kreves tilbake.")
        }

        paragraph {
            universalText("Ved tilbakekreving etter § 22-15 a beregnes et rentetillegg på 10 prosent av det beløp som kreves tilbake. Ved betaling etter fastsatt betalingsfrist kreves det renter fra fristens utløp inntil tilbakebetaling skjer, beregnet på grunnlag av tilbakebetalingsbeløpet tillagt rentetillegg etter første punktum. Rentesatsen etter forsinkelsesrenteloven1 legges til grunn.")
        }
    }

private fun TextOnlyScope<LangBokmalNynorskEnglish, VarselTilbakekrevingAvFeilutbetaltBeloepDto>.universalText(text: String) {
    text(Bokmal to text, Nynorsk to text, English to text)
}

private fun PlainTextOnlyScope<LangBokmalNynorskEnglish, VarselTilbakekrevingAvFeilutbetaltBeloepDto>.universalText(text: String) {
    text(Bokmal to text, Nynorsk to text, English to text)
}

private fun ParagraphOnlyScope<LangBokmalNynorskEnglish, VarselTilbakekrevingAvFeilutbetaltBeloepDto>.universalText(text: String) {
    text(Bokmal to text, Nynorsk to text, English to text)
}