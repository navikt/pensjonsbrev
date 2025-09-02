package no.nav.pensjon.brev.maler.legacy.fraser

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

object TBU5005_Generated : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        //[TBU5005_NN, TBU5005, TBU5005_EN]

        title1 {
            text(
                bokmal { + "Barnetillegget påvirkes av utlandsopphold" },
                nynorsk { + "Barnetillegget vert påverka av utanlandsopphald" },
                english { + "The dependant supplement is affected by stay abroad" },
            )
        }
        paragraph {
            text(
                bokmal { + "For å ha rett til barnetillegg fra 1. juli 2020" },
                nynorsk { + "For å ha rett til barnetillegg frå 1. juli 2020" },
                english { + "In order to be entitled to child supplement from 1 July 2020" },
            )
            list {
                item {
                    text(
                        bokmal { + "må du enten bo i Norge, innenfor EØS-området eller i et annet land Norge har trygdeavtale med" },
                        nynorsk { + "må du enten bu i Noreg, innanfor EØS-området eller i eit anna land Noreg har trygdeavtale med" },
                        english { + "you must live either in Norway, within the EEA, or in another country that Norway has a social security agreement with" },
                    )
                }
                item {
                    text(
                        bokmal { + "må også barnet være bosatt og oppholde seg i Norge, innenfor EØS-området eller et annet land Norge har trygdeavtale med" },
                        nynorsk { + "må også barn vere busett og opphalde seg i Noreg, innanfor EØS-området eller eit anna land Noreg har trygdeavtale med" },
                        english { + "your child must also be a resident of and currently reside in Norway, within the EEA, or in another country that Norway has a social security agreement with" },
                    )
                }
            }
            text(
                bokmal { + "Dette går frem av folketrygdloven §12-15 som gjelder fra 1. juli 2020." },
                nynorsk { + "Dette går fram av folketrygdlova §12-15 som gjeld frå 1. juli 2020." },
                english { + "This is in accordance with the regulations of § 12-15 of the National Insurance Act, which apply from 1 July 2020." },
            )
        }
    }
}
