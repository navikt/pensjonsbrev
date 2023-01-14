package no.nav.pensjon.brev.maler.fraser.common

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr


object HjemlerFolketrygdloven {

    // § 12-11.Grunnlaget for beregning av uføretrygd
    // § 12-12.Trygdetid
    // § 12-13.Uføretrygdens størrelse
    // § 12-14.Reduksjon av uføretrygd på grunn av inntekt
    // § 12-15.Barnetillegg
    // § 12-16.Reduksjon av barnetillegg på grunn av inntekt
    // § 12-17.Uføretrygd ved yrkesskade
    // § 12-18.Tillegg til uføretrygd for gjenlevende ektefelle (gjenlevendetillegg)
    // § 22-12.Tidspunkt for utbetaling når rett til en ytelse oppstår eller opphører

    data class Folketrygdloven(
        val innvilgetEktefelleTillegg: Expression<Boolean>,
        val innvilgetGjenlevendeTillegg: Expression<Boolean>,
        val innvilgetTilleggFellesbarn: Expression<Boolean>,
        val innvilgetTilleggSaerkullsbarn: Expression<Boolean>,
        val yrkesskadeGrad: Expression<Int>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {

            // TBU2228 - Uten tillegg
            // TBU2230 - med gjenlevendetillegg
            // TBU2233 - med ektefelletillegg
            // TBU2234 - ved yrkesskade, uten tillegg
            // TBU2236 - ved yrkesskade, med gjenlevendetillegg
            // TBU2239 - ved yrkesskade, med ektefelletillegg
            showIf(not(innvilgetTilleggFellesbarn) and not(innvilgetTilleggSaerkullsbarn)) {
                paragraph {
                    val harYrkesskade = yrkesskadeGrad.greaterThan(0)
                    textExpr(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-11 til 12-14".expr() +
                                ifElse(harYrkesskade, ifTrue = ", 22-17,", ifFalse = "") + "".expr(),
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-11 til 12-14".expr() +
                                ifElse(harYrkesskade, ifTrue = ", 22-17,", ifFalse = "") + "".expr(),
                        English to "This decision was made pursuant to the provisions of §§ 12-11 to 12-14".expr() +
                                ifElse(harYrkesskade, ifTrue = ", 22-17,", ifFalse = "") + "".expr()
                    )
                    showIf(not(innvilgetEktefelleTillegg) and not(innvilgetGjenlevendeTillegg)) {
                        text(Bokmal to " og 22-12.", Nynorsk to " og 22-12.", English to " and 22-12 of the National Insurance Act.")
                    }.orShowIf(not(innvilgetEktefelleTillegg) and innvilgetGjenlevendeTillegg) {
                        text(Bokmal to ", 12-18 og 22-12.", Nynorsk to ", 12-18 og 22-12.", English to ", 12-18 and 22-12 of the National Insurance Act.")
                    }.orShowIf(innvilgetEktefelleTillegg and not(innvilgetEktefelleTillegg)) {
                        text(Bokmal to ", 22-12 og overgangsforskriften § 8.", Nynorsk to ", 22-12 og overgangsforskriften § 8.", English to ", 22-12 and overgangsforskriften § 8 of the National Insurance Act.")
                    }
                }
            }

            // TBU2229 - med barnetillegg
            // TBU2231 - med barnetillegg og gjenlevendetillegg
            // TBU2232 - med barnetillegg og ektefelletillegg
            // TBU2235 - ved yrkesskade, med barnetillegg
            // TBU2237 - ved yrkesskade, med barnetillegg og gjenlevendetillegg
            // TBU2238 - ved yrkesskade, med barnetillegg og ektefelletillegg
            showIf(innvilgetTilleggFellesbarn and innvilgetTilleggSaerkullsbarn and yrkesskadeGrad.equalTo(0)) {
                val harYrkesskade = yrkesskadeGrad.greaterThan(0)
                textExpr(
                    Bokmal to "Vedtaket er gjort etter folketrygdloven §§ ".expr() +
                            ifElse(harYrkesskade, ifTrue = "12-11 til 12-17", ifFalse = "12-11 til 12-16") + "".expr(),
                    Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ ".expr() +
                            ifElse(harYrkesskade, ifTrue = "12-11 til 12-17", ifFalse = "12-11 til 12-16") + "".expr(),
                    English to "his decision was made pursuant to the provisions of §§ ".expr() +
                            ifElse(harYrkesskade, ifTrue = "12-11 to 12-17", ifFalse = "12-11 to 12-16") + "".expr()
                )
                showIf(not(innvilgetEktefelleTillegg) and not(innvilgetGjenlevendeTillegg)) {
                    text(Bokmal to " og 22-12.", Nynorsk to " og 22-12.", English to " and 22-12 of the National Insurance Act.")
                }.orShowIf(not(innvilgetEktefelleTillegg) and innvilgetGjenlevendeTillegg) {
                    text(Bokmal to ", 12-18 og 22-12.", Nynorsk to ", 12-18 og 22-12.", English to ", 12-18 og 22-12 of the National Insurance Act.")
                }.orShowIf(innvilgetEktefelleTillegg and not(innvilgetGjenlevendeTillegg)) {
                    text(Bokmal to ", 22-12 og overgangsforskriften § 8.", Nynorsk to ", 22-12 og overgangsforskriften § 8.", English to ", 22-12 and overgangsforskriften § 8 of the National Insurance Act.")
                }
            }

        }
    }
}