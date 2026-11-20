package no.nav.pensjon.brev.maler.ufore.endring.bunnfradragNesteAr

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner

data class BegrensetFribelop(
    val bunnfradrag: Expression<Kroner>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        title1 {
            text(
                bokmal { +"Begrensning i fribeløpet og bunnfradraget ditt" },
                nynorsk { +"Begrensing i fribeløpet og bunnfrådraget ditt" }
            )
        }
        paragraph {
            text(
                bokmal { +"Du får ikke utbetalt uføretrygd dersom arbeidsinntekten din overstiger 80 prosent av inntekten du hadde før du ble ufør. Dette kaller vi inntektstak. " },
                nynorsk { +"Du får ikkje utbetalt uføretrygd dersom arbeidsinntekta di overstig 80 prosent av inntekta du hadde før du blei ufør. Dette kallar vi inntektstak. " }
            )
        }
        paragraph {
            text(
                bokmal { +"Ifølge reglene kan ikke bunnfradraget være høyere enn inntektstaket. Ditt bunnfradrag er høyere enn inntektstaket, og derfor begrenser vi bunnfradraget ditt til inntektstaket på " + bunnfradrag.format() + ". " },
                nynorsk { +"Ifølgje reglane kan ikkje bunnfrådraget vere høgare enn inntektstaket. Ditt bunnfrådrag er høgare enn inntektstaket, og derfor avgrensar vi bunnfrådraget ditt til inntektstaket på " + bunnfradrag.format() + ". " }
            )
        }
        paragraph {
            text(
                bokmal { +"Hvis du et kalenderår har fått utbetalt uføretrygd og samtidig hatt inntekt over inntektstaket, vil vi kreve tilbake uføretrygden din for det aktuelle året. " },
                nynorsk { +"Hvis du eit kalenderår har fått utbetalt uføretrygd og samtidig hatt inntekt over inntektstaket, vil vi krevje tilbake uføretrygda di for det aktuelle året. " }
            )
        }
    }
}