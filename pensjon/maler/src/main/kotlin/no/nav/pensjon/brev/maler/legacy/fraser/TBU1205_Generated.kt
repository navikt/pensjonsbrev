package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.maler.legacy.ut_bunnfradrag_faktisk
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.equalTo


data class TBU1205_Generated(
    val pe: Expression<PEgruppe10>,
    val vektetFribelop: Expression<Double>
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
		//[TBU1205NN, TBU1205, TBU1205EN]

        paragraph {
            showIf(vektetFribelop.equalTo(1.0)) {
                text(
                    bokmal { +"Du kan ha en årlig inntekt på folketrygdens grunnbeløp, uten at uføretrygden din blir redusert. I dag er dette " + pe.ut_bunnfradrag_faktisk().format() + ". Dette er bunnfradraget ditt." },
                    nynorsk { +"Du kan ha ei årleg inntekt på grunnbeløpet i folketrygda utan at uføretrygda di blir redusert. I dag er dette " + pe.ut_bunnfradrag_faktisk().format() + ". Dette er botnfrådraget ditt." },
                    english { +"You may have an annual income up to the National Insurance basic amount, without your disability benefit being reduced. This is currently " + pe.ut_bunnfradrag_faktisk().format() + ", which is your income limit." },
                )
            }.orShow {
                text(
                    bokmal { +"Du kan tjene " + pe.ut_bunnfradrag_faktisk().format() + " uten at uføretrygden din blir redusert. Dette er bunnfradraget ditt. " },
                    nynorsk { +"Du kan tene " + pe.ut_bunnfradrag_faktisk().format() + " utan at uføretrygda di blir redusert. Dette er botnfrådraget ditt. " },
                    english { +"You may earn " + pe.ut_bunnfradrag_faktisk().format() + ", without your disability benefit being reduced, which is your income limit." },
                )
            }
        }
    }
}
        