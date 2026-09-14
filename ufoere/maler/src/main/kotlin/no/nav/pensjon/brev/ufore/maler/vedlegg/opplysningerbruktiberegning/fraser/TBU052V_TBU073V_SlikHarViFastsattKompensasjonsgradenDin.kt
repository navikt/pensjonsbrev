package no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser

import no.nav.pensjon.brev.ufore.api.model.vedlegg.OpplysningerBruktIBeregningUTLegacyDto.Avkortning
import no.nav.pensjon.brev.ufore.api.model.vedlegg.OpplysningerBruktIBeregningUTLegacyDto.Beregning
import no.nav.pensjon.brev.ufore.api.model.vedlegg.OpplysningerBruktIBeregningUTLegacyDto.InntektFoerUfoere
import no.nav.pensjon.brev.ufore.api.model.vedlegg.OpplysningerBruktIBeregningUTLegacyDto.Visningsflagg
import no.nav.pensjon.brev.ufore.api.model.vedlegg.selectors.opplysningerBruktIBeregningUTLegacyDto.avkortning.*
import no.nav.pensjon.brev.ufore.api.model.vedlegg.selectors.opplysningerBruktIBeregningUTLegacyDto.beregning.*
import no.nav.pensjon.brev.ufore.api.model.vedlegg.selectors.opplysningerBruktIBeregningUTLegacyDto.inntektFoerUfoere.*
import no.nav.pensjon.brev.ufore.api.model.vedlegg.selectors.opplysningerBruktIBeregningUTLegacyDto.visningsflagg.*
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.lessThan
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text

/**
 * Portert fra legacy TBU052V_TBU073V_SlikHarViFastsattKompensasjonsgradenDin. Forklarer
 * reduksjonsprosenten. Ytre blokker gates av [Visningsflagg.tbu056v]; detaljblokken med
 * oppjustert inntekt gates av [Visningsflagg.visKompensasjonOppjustertInntekt].
 */
data class TBU052V_TBU073V_SlikHarViFastsattKompensasjonsgradenDin(
    val flagg: Expression<Visningsflagg>,
    val beregning: Expression<Beregning>,
    val inntektFoerUfoere: Expression<InntektFoerUfoere>,
    val avkortning: Expression<Avkortning>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        showIf(flagg.tbu056v) {
            title1 {
                text(
                    bokmal { + "Slik har vi fastsatt reduksjonsprosenten din" },
                    nynorsk { + "Slik har vi fastsett reduksjonsprosenten din" },
                )
            }
            paragraph {
                text(
                    bokmal { + "Vi fastsetter reduksjonsprosenten ved å sammenligne det du " },
                    nynorsk { + "Vi fastset reduksjonsprosenten ved å samanlikne det du " },
                )
                showIf(beregning.ufoeregrad.equalTo(100)) {
                    text(bokmal { + "har rett til i" }, nynorsk { + "har rett til i" })
                }.orShow {
                    text(bokmal { + "ville hatt rett til i" }, nynorsk { + "ville hatt rett til i" })
                }
                text(
                    bokmal { + " 100 prosent uføretrygd med din oppjusterte inntekt før du ble ufør. Reduksjonsprosenten brukes til å beregne hvor mye vi reduserer uføretrygden din, hvis du har inntekt som er høyere enn inntektsgrensen. Reduksjonsprosenten kan ikke være høyere enn 70 prosent." },
                    nynorsk { + " 100 prosent uføretrygd, med den oppjusterte inntekta di før du blei ufør. Reduksjonsprosenten blir brukt til å berekne kor mykje vi reduserer uføretrygda di, dersom du har inntekt som er høgare enn inntektsgrensa. Reduksjonsprosenten kan ikkje vere høgare enn 70 prosent." },
                )
            }
        }

        showIf(flagg.visKompensasjonOppjustertInntekt) {
            ifNotNull(avkortning.oifu, avkortning.ugradertBruttoPerAar) { oifu, ugradertBrutto ->
                paragraph {
                    text(
                        bokmal { + "Inntekten din før du ble ufør er fastsatt til " + inntektFoerUfoere.ifuInntekt.format() + ". For å kunne fastsette reduksjonsprosenten din, må denne inntekten oppjusteres til dagens verdi. Oppjustert til dagens verdi tilsvarer dette en inntekt på " + oifu.format() + "." },
                        nynorsk { + "Inntekta di før du blei ufør er fastsett til " + inntektFoerUfoere.ifuInntekt.format() + ". For å kunne fastsetje reduksjonsprosenten din, må inntekta oppjusterast til dagens verdi. Oppjustert til dagens verdi utgjer dette ei inntekt på " + oifu.format() + "." },
                    )
                }
                showIf(beregning.ufoeregrad.equalTo(100)) {
                    paragraph {
                        text(
                            bokmal { + "Du har rett til 100 prosent uføretrygd, som utgjør " + ugradertBrutto.format() + " per år." },
                            nynorsk { + "Du har rett til 100 prosent uføretrygd, som utgjer " + ugradertBrutto.format() + " per år." },
                        )
                    }
                }.orShow {
                    paragraph {
                        text(
                            bokmal { + "Du har rett til " + beregning.ufoeregrad.format() + " prosent uføretrygd. Regnet om til 100 prosent uføretrygd, utgjør dette " + ugradertBrutto.format() + " per år." },
                            nynorsk { + "Du har rett til " + beregning.ufoeregrad.format() + " prosent uføretrygd. Rekna om til 100 prosent uføretrygd, utgjer dette " + ugradertBrutto.format() + " per år." },
                        )
                    }
                }
                paragraph {
                    text(
                        bokmal { + "Vi beregner reduksjonsprosenten din slik: (" + ugradertBrutto.format(false) + " / " + oifu.format(false) + ") * 100 = " + avkortning.kompensasjonsgrad.format() + " prosent." },
                        nynorsk { + "Vi bereknar reduksjonsprosenten din slik: (" + ugradertBrutto.format(false) + " / " + oifu.format(false) + ") * 100 = " + avkortning.kompensasjonsgrad.format() + " prosent." },
                    )
                }
            }
        }

        showIf(flagg.tbu056v) {
            paragraph {
                text(
                    bokmal { + "Hvis uføretrygden din i løpet av et kalenderår endres, bruker vi en gjennomsnittlig reduksjonsprosent i beregningen." },
                    nynorsk { + "Dersom uføretrygda di blir endra i løpet av eit kalenderår, vil vi bruke ein gjennomsnittleg reduksjonsprosent i berekninga." },
                )
            }
        }
    }
}
