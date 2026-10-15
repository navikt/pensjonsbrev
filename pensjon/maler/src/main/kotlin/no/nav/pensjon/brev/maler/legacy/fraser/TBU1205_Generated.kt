package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.maler.legacy.ut_bunnfradrag_faktisk
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.dsl.*


data class TBU1205_Generated(
    val pe: Expression<PEgruppe10>
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        //[TBU1205NN, TBU1205, TBU1205EN]

        paragraph {
            text(
                bokmal { +"Du kan tjene " + pe.ut_bunnfradrag_faktisk().format() + " uten at uføretrygden din blir redusert. Dette er bunnfradraget ditt. " },
                nynorsk { +"Du kan tene " + pe.ut_bunnfradrag_faktisk().format() + " utan at uføretrygda di blir redusert. Dette er botnfrådraget ditt. " },
                english { +"You may earn " + pe.ut_bunnfradrag_faktisk().format() + ", without your disability benefit being reduced, which is your income limit." },
            )
        }
    }
}