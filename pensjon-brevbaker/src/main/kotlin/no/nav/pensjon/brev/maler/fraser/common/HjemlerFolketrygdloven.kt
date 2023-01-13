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
            showIf(not(innvilgetTilleggFellesbarn) and not(innvilgetTilleggSaerkullsbarn) and yrkesskadeGrad.equalTo(0)) {
                showIf(not(innvilgetEktefelleTillegg) and not(innvilgetGjenlevendeTillegg)) {
                    paragraph {
                        text(
                            Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-11 til 12-14 og 22-12.",
                            Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-11 til 12-14 og 22-12.",
                            English to ""
                        )
                    }
                }.orShowIf(not(innvilgetEktefelleTillegg) and innvilgetGjenlevendeTillegg) {
                    paragraph {
                        text(
                            Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12- 11 til 12-14, 12-18 og 22-12.",
                            Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12- 11 til 12-14, 12-18 og 22-12.",
                            English to ""
                        )
                    }
                }.orShowIf(innvilgetEktefelleTillegg and not(innvilgetEktefelleTillegg)) {
                    paragraph {
                        text(
                            Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-11 til 12-14, 22-12 og overgangsforskriften § 8",
                            Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-11 til 12-14, 22-12 og overgangsforskriften § 8",
                            English to ""
                        )
                    }
                }
            }
            // TBU2234 - ved yrkesskade, uten tillegg
            // TBU2236 - ved yrkesskade, med gjenlevendetillegg
            // TBU2239 - ved yrkesskade, med ektefelletillegg
            showIf(not(innvilgetTilleggFellesbarn) and not(innvilgetTilleggSaerkullsbarn) and yrkesskadeGrad.greaterThan(0)) {
                showIf(not(innvilgetEktefelleTillegg) and not(innvilgetGjenlevendeTillegg)) {
                    paragraph {
                        text(
                            Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-11 til 12-14, 12-17 og 22-12.",
                            Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-11 til 12-14, 12-17 og 22-12.",
                            English to ""
                        )
                    }
                }.orShowIf(not(innvilgetEktefelleTillegg) and innvilgetGjenlevendeTillegg) {
                    paragraph {
                        text(
                            Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12- 11 til 12-14, 12-17, 12-18 og 22-12.",
                            Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12- 11 til 12-14, 12-17, 12-18 og 22-12.",
                            English to ""
                        )
                    }
                }.orShowIf(innvilgetEktefelleTillegg and not(innvilgetGjenlevendeTillegg)) {
                    paragraph {
                        text(
                            Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-11 til 12-14, 12- 17, 22-12 og overgangsforskriften § 8.",
                            Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-11 til 12-14, 12- 17, 22-12 og overgangsforskriften § 8.",
                            English to ""
                        )
                    }
                }
            }
        }
    }
}