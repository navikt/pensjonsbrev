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
    }
}
