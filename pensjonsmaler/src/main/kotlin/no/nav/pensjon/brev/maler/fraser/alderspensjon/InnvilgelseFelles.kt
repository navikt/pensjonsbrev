package no.nav.pensjon.brev.maler.fraser.alderspensjon

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.text

// hvisFlyttetBosattEØS / hvisFlyttetBosattAvtaleland
data class HvisFlytetFaktiskBostedsland(
    val eksportTrygdeavtaleAvtaleland: Expression<Boolean>,
    val eksportTrygdeavtaleEOS: Expression<Boolean>,
    val faktiskBostedsland: Expression<String>
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        showIf(eksportTrygdeavtaleEOS or eksportTrygdeavtaleAvtaleland) {
            // hvisFlyttetBosattEØS / hvisFlyttetBosattAvtaleland
            paragraph {
                text(
                    bokmal { +"Vi forutsetter at du bor i " + faktiskBostedsland + ". Hvis du skal flytte til et " + ifElse(
                        eksportTrygdeavtaleEOS,
                        ifTrue = "land utenfor EØS-området",
                        ifFalse = "annet land"
                    ) + ", må du kontakte oss slik at vi kan vurdere om du fortsatt har rett til alderspensjon." },
                    nynorsk { +"Vi føreset at du bur i " + faktiskBostedsland + ". Dersom du skal flytte til eit " + ifElse(
                        eksportTrygdeavtaleEOS,
                        ifTrue = "land utanfor EØS-området",
                        ifFalse = "anna land"
                    ) + ", må du kontakte oss slik at vi kan vurdere om du framleis har rett til alderspensjon." },
                    english { +"We presume that you live in " + faktiskBostedsland + ". If you are moving to " + ifElse(
                        eksportTrygdeavtaleEOS,
                        ifTrue = "a country outside the EEA region",
                        ifFalse = "another country"
                    ) + ", it is important that you contact Nav. We will then reassess your eligibility for retirement pension." }
                )
            }
        }
    }
}