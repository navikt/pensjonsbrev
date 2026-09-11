package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere.inntektsgrense

import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

object HvaErBunnfradragFribelopOgVenteperiode : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        title1 {
            text (
                bokmal { + "Hva er bunnfradrag, fribeløp og venteperiode" },
                nynorsk { + "Kva er bunnfrådrag, fribeløp og venteperiode" }
            )
        }

        paragraph {
            text (
                bokmal { + "Bunnfradrag er hvor mye du kan ha i inntekt før uføretrygden din blir redusert. " },
                nynorsk { + "Bunnfrådrag er kor mykje du kan ha i inntekt før uføretrygda di blir redusert. " }
            )
        }
        paragraph {
            text (
                bokmal { + "Bunnfradrag = Fribeløp + Oppjustert inntekt etter uførhet " },
                nynorsk { + "Bunnfrådrag = Fribeløp + Oppjustert inntekt etter uførleik " }
            )
        }
        paragraph {
            text (
                bokmal { + "Inntekt etter uførhet: Inntekten som er lagt til grunn for gradering av uføretrygden. Inntekten oppjusteres etter gjeldende grunnbeløp. " },
                nynorsk { + "Inntekt etter uførleik: Inntekta som er lagt til grunn for gradering av uføretrygda. Inntekta oppjusterast etter gjeldande grunnbeløp. " }
            )
        }
        paragraph {
            text (
                bokmal { + "Dersom du ikke har inntekt etter uførhet, vil bunnfradraget være lik fribeløpet. " },
                nynorsk { + "Dersom du ikkje har inntekt etter uførleik, vil bunnfrådraget vere lik fribeløpet. " }
            )
        }

        paragraph {
            text (
                bokmal { + "Fribeløp: " },
                nynorsk { + "Fribeløp: " }
            )
            list {
                item {
                    text (
                        bokmal { + "0,4 G: De første 2 årene (24 mnd) du har uføretrygd. " },
                        nynorsk { + "0,4 G: Dei første 2 åra (24 md.) du har uføretrygd. " }
                    )
                }
                item {
                    text (
                        bokmal { + "1 G: Etter at du har hatt uføretrygd i 2 år (24 måneder), forutsatt at uføregraden ikke har blitt endret til en høyere grad. " },
                        nynorsk { + "1 G: Etter at du har hatt uføretrygd i 2 år (24 månader), føresett at uføregraden ikkje har blitt endra til ein høgare grad. " }
                    )
                }
            }
        }

        paragraph {
            text (
                bokmal { + "Venteperiode:" },
                nynorsk { + "Venteperiode:" }
            )
            list {
                item {
                    text (
                        bokmal { + "Ved innvilgelse av uføretrygd vil fribeløpet, eller økt uføregrad være 0,4 G. Etter 24 mnd økes det til 1G." },
                        nynorsk { + "Ved innvilging av uføretrygd vil fribeløpet, eller auka uføregrad vere 0,4 G. Etter 24 md. blir det auka til 1G." }
                    )
                }
                item {
                    text (
                        bokmal { + "Dersom uføregraden øker, vil det bli venteperiode på 2 år med fribeløp på 0,4G" },
                        nynorsk { + "Dersom uføregraden aukar, vil det bli venteperiode på 2 år med fribeløp på 0,4G" }
                    )
                }
            }
        }

        paragraph {
            text (
                bokmal { + "Grunnbeløpet justeres i mai hvert år." },
                nynorsk { + "Grunnbeløpet blir justert i mai kvart år." }
            )
        }

        title1 {
            text (
                bokmal { + "Når fribeløpet endres i løpet av kalenderåret" },
                nynorsk { + "Når fribeløpet blir endra i løpet av kalenderåret" }
            )
        }

        paragraph {
            text (
                bokmal { + "Hvis du blir ferdig med venteperioden i løpet av et kalenderår, vil du få to ulike fribeløp for det aktuelle året." },
                nynorsk { + "Dersom du blir ferdig med venteperioden i løpet av eit kalenderår, vil du få to ulike fribeløp for det aktuelle året." }
            )
        }

        paragraph {
            text (
                bokmal { + "Når du har to ulike fribeløp i løpet av et kalenderår, regner vi et gjennomsnitt av disse to. Dette blir ditt fribeløp for året. Gjennomsnittet beregnes ut fra hvor mange måneder du har hatt de ulike fribeløpene. Dette kaller vi et vektet fribeløp." },
                nynorsk { + "Når du har to ulike fribeløp i løpet av eit kalenderår, reknar vi eit gjennomsnitt av desse to. Dette blir fribeløpet ditt for året. Gjennomsnittet blir rekna ut frå kor mange månader du har hatt dei ulike fribeløpa. Dette kallar vi eit vekta fribeløp." }
            )
        }
    }
}
