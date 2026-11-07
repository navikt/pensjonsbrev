package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere.inntektsgrense

import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

object HvaErBunnfradragFribelopOgVenteperiode : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        title1 {
            text (
                bokmal { + "Slik fastsetter vi bunnfradraget ditt" },
                nynorsk { + "Slik fastset vi botnfrådraget ditt" }
            )
        }

        paragraph {
            text (
                bokmal { + "Uføretrygd beregnes ut fra grunnbeløpet (G). G justeres hvert år i mai, med utgangspunkt i lønnsutviklingen i samfunnet. Bunnfradraget fastsettes også ut fra G. " },
                nynorsk { + "Uføretrygd blir rekna ut frå grunnbeløpet (G). G blir justert kvart år i mai, med utgangspunkt i lønnsutviklinga i samfunnet. Botnfrådraget blir også fastsett ut frå G. " }
            )
        }

        paragraph {
            text (
                bokmal { + "Bunnfradrag = Fribeløp + inntekt etter uførhet. " },
                nynorsk { + "Botnfrådrag = Fribeløp + inntekt etter uførleik. " }
            )
        }
        paragraph {
            text (
                bokmal { + "Fribeløpet er 0,4 G de første 24 månedene du har uføretrygd, disse 24 månedene kalles venteperiode. " },
                nynorsk { + "Fribeløpet er 0,4 G dei første 24 månadene du har uføretrygd, desse 24 månadene kalles venteperiode. " }
            )
        }
        paragraph {
            text (
                bokmal { + "Fribeløpet er 1 G når du har hatt uføretrygd i 24 måneder. " },
                nynorsk { + "Fribeløpet er 1 G når du har hatt uføretrygd i 24 månader. " }
            )
        }
        paragraph {
            text (
                bokmal { + "Dersom uføregraden øker, vil det bli en ny venteperiode på 24 måneder med fribeløp på 0,4 G. " },
                nynorsk { + "Dersom uføregraden din aukar, vil det bli ein ny venteperiode på 24 månader med fribeløp på 0,4 G. " }
            )
        }
        paragraph {
            text (
                bokmal { + "Hvis fribeløpet endrer seg i løpet av kalenderåret, får du et vektet fribeløp. Fribeløpet vektes ut fra periodene med 0,4 G og 1 G. " },
                nynorsk { + "Hvis fribeløpet endrar seg i løpet av kalenderåret, får du eit vektet fribeløp. Fribeløpet vektast ut frå periodane med 0,4 G og 1 G. " }
            )
        }

        paragraph {
            text (
                bokmal { + "Når vi beregner bunnfradraget, skal vi bruke en vektet G. Det betyr at når grunnbeløpet endrer seg i mai hvert år skal vi bruke grunnbeløpet som gjelder for de aktuelle månedene. Altså 4 måneder med grunnbeløp som gjelder frem til mai og 8 måneder med grunnbeløpet som gjelder etter mai. På nav.no finner du dette tallet som gjennomsnitt per år her: nav.no/grunnbelopet" },
                nynorsk { + "Når vi beregner botnfrådraget, skal vi bruke ein vektet G. Det betyr at når grunnbeløpet endrar seg i mai kvart år skal vi bruke grunnbeløpet som gjeld for dei aktuelle månadene. Altså 4 månader med grunnbeløp som gjeld fram til mai og 8 månader med grunnbeløpet som gjeld etter mai. På nav.no finn du dette talet som gjennomsnitt per år her: nav.no/grunnbelopet" }
            )
        }

        paragraph {
            text (
                bokmal { + "Dersom uføretrygden din opphører, eller du går av med alderspensjon, vil du få ett annet bunnfradrag det aktuelle året. Da vil du få ett bunnfradrag som er lavere, fordi du ikke skal ha uføretrygd hele året. " },
                nynorsk { + "Dersom uføretrygda di opphøyrer, eller du går av med alderspensjon, vil du få eit anna botnfrådrag det aktuelle året. Då vil du få eit botnfrådrag som er lågare, fordi du ikkje skal ha uføretrygd heile året. " }
            )
        }

        title2 {
            text(
                bokmal { + "Eksempel" },
                nynorsk { + "Eksempel" }
            )
        }
        paragraph {
            text (
                bokmal { + "Kim har 100 prosent uføretrygd og fyller 67 år i august. Fra første september skal Kim få utbetalt alderspensjon i stedet for uføretrygd. Kim får et lavere bunnfradrag dette året, fordi Kim bare skal ha uføretrygd i 8 måneder. " },
                nynorsk { + "Kim har 100 prosent uføretrygd og fyller 67 år i august. Frå første september skal Kim få utbetalt alderspensjon i staden for uføretrygd. Kim får eit lågare botnfrådrag dette året, fordi Kim berre skal ha uføretrygd i 8 månader. " }
            )
        }
        paragraph {
            text (
                bokmal { + "Bunnfradraget til Kim blir derfor: 1G /12 måneder x de 8 månedene Kim har uføretrygd. " },
                nynorsk { + "Botnfrådraget til Kim blir derfor: 1G /12 månader x dei 8 månadene Kim har uføretrygd. " }
            )
        }
        paragraph {
            text (
                bokmal { + "(132 000 / 12) x 8 = 88 000 " },
                nynorsk { + "(132 000 / 12) x 8 = 88 000 " }
            )
        }
        paragraph {
            text (
                bokmal { + "Bunnfradraget til Kim dette året er 88 000 kroner. Det betyr at Kim kan tjene 88 000 kroner fra januar til august før vi begynner å redusere uføretrygden. Det er egne regler for inntekt og alderspensjon. " },
                nynorsk { + "Botnfrådraget til Kim dette året er 88 000 kroner. Det betyr at Kim kan tene 88 000 kroner frå januar til august før vi begynner å redusere uføretrygda. Det er eigne reglar for inntekt og alderspensjon. " }
            )
        }
        paragraph {
            text (
                bokmal { + "Dette eksemplet vil også gjelde hvis uføretrygden opphører i løpet av året. " },
                nynorsk { + "Dette eksempelet vil også gjelde dersom uføretrygda opphøyrer i løpet av året. " }
            )
        }
    }
}
