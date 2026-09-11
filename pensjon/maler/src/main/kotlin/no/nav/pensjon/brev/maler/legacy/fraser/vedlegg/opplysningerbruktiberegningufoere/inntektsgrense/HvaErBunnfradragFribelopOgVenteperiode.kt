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

        title2 {
            text (
                bokmal { + "Bunnfradrag" },
                nynorsk { + "Bunnfrådrag" }
            )
        }
        paragraph {
            text (
                bokmal { + "Bunnfradrag er hvor mye du kan tjene før uføretrygden din blir redusert. Bunnfradraget består av to deler:" },
                nynorsk { + "Bunnfrådrag er kor mykje du kan tene før uføretrygda di blir redusert. Bunnfrådraget består av to delar:" }
            )
        }
        paragraph {
            text (
                bokmal { + "Bunnfradrag = fribeløp + oppjustert inntekt etter uførhet" },
                nynorsk { + "Bunnfrådrag = fribeløp + oppjustert inntekt etter uførleik" }
            )
        }
        paragraph {
            text (
                bokmal { + "Med inntekt etter uførhet mener vi inntekten vi har lagt til grunn for uføregraden din. Denne inntekten justerer vi opp etter dagens grunnbeløp." },
                nynorsk { + "Med inntekt etter uførleik meiner vi inntekta vi har lagt til grunn for uføregraden din. Denne inntekta justerer vi opp etter grunnbeløpet som gjeld i dag." }
            )
        }
        paragraph {
            text (
                bokmal { + "Hvis du ikke har inntekt etter uførhet, er bunnfradraget det samme som fribeløpet." },
                nynorsk { + "Dersom du ikkje har inntekt etter uførleik, er bunnfrådraget det same som fribeløpet." }
            )
        }

        title2 {
            text (
                bokmal { + "Fribeløp" },
                nynorsk { + "Fribeløp" }
            )
        }
        paragraph {
            text (
                bokmal { + "Fribeløpet avhenger av hvor lenge du har hatt uføretrygd:" },
                nynorsk { + "Fribeløpet er avhengig av kor lenge du har hatt uføretrygd:" }
            )
            list {
                item {
                    text (
                        bokmal { + "0,4 G de første 2 årene (24 måneder) du har uføretrygd." },
                        nynorsk { + "0,4 G dei første 2 åra (24 månader) du har uføretrygd." }
                    )
                }
                item {
                    text (
                        bokmal { + "1 G når du har hatt uføretrygd i mer enn 2 år (24 måneder), så lenge uføregraden din ikke har økt." },
                        nynorsk { + "1 G når du har hatt uføretrygd i meir enn 2 år (24 månader), så lenge uføregraden din ikkje har auka." }
                    )
                }
            }
        }

        title2 {
            text (
                bokmal { + "Venteperiode" },
                nynorsk { + "Venteperiode" }
            )
        }
        paragraph {
            text (
                bokmal { + "Når du får innvilget uføretrygd eller høyere uføregrad, er fribeløpet 0,4 G. Etter 24 måneder øker det til 1 G. " },
                nynorsk { + "Når du får innvilga uføretrygd eller høgare uføregrad, er fribeløpet 0,4 G. Etter 24 månader aukar det til 1 G. " }
            )
            text (
                bokmal { + "Hvis uføregraden din øker, får du en ny venteperiode på 2 år med et fribeløp på 0,4 G." },
                nynorsk { + "Dersom uføregraden din aukar, får du ein ny venteperiode på 2 år med eit fribeløp på 0,4 G." }
            )
        }

        paragraph {
            text (
                bokmal { + "Grunnbeløpet (G) justeres i mai hvert år." },
                nynorsk { + "Grunnbeløpet (G) blir justert i mai kvart år." }
            )
        }

        title2 {
            text (
                bokmal { + "Når fribeløpet endres i løpet av året" },
                nynorsk { + "Når fribeløpet blir endra i løpet av året" }
            )
        }
        paragraph {
            text (
                bokmal { + "Hvis venteperioden din tar slutt i løpet av et kalenderår, får du to ulike fribeløp det året." },
                nynorsk { + "Dersom venteperioden din tek slutt i løpet av eit kalenderår, får du to ulike fribeløp det året." }
            )
        }
        paragraph {
            text (
                bokmal { + "Da regner vi ut et gjennomsnitt av de to fribeløpene, og gjennomsnittet blir fribeløpet ditt for året. Vi vekter gjennomsnittet ut fra hvor mange måneder du har hatt hvert av fribeløpene. Dette kaller vi et vektet fribeløp." },
                nynorsk { + "Då reknar vi ut eit gjennomsnitt av dei to fribeløpa, og gjennomsnittet blir fribeløpet ditt for året. Vi vektar gjennomsnittet ut frå kor mange månader du har hatt kvart av fribeløpa. Dette kallar vi eit vekta fribeløp." }
            )
        }
    }
}
