package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brev.template.dsl.expression.*

data class Trygdetiden(
    val fastsattTrygdetid: Expression<Int>,  // (<FaTTNorge> + <FramtidigTTNorge>)/12
    val har40AarFastsattTrygdetid: Expression<Boolean>,
    val harFlyktningstatus: Expression<Boolean>,  // brukerFlyktning = true
    val harFramtidigTrygdetidEOS: Expression<Boolean>,
    val harFramtidigTrygdetidNorsk: Expression<Boolean>,
    val harLikUfoeregradOgYrkesskadegrad: Expression<Boolean>,
    val harTrygdetidsgrunnlag: Expression<Boolean>,  // PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeNor_Trygdetidsgrunnlag_TrygdetidFom
    val harYrkesskadeOppfylt: Expression<Boolean>,  // YrkesskadeResultat = Oppfylt / Finnes andre YrkesskadeResultat koder? Se TBU043V
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        val harFramtidigTrygdetid = harFramtidigTrygdetidNorsk or harFramtidigTrygdetidEOS

        // TBU039V
        title1 {
            text(
                Bokmal to "Dette er trygdetiden din",
                Nynorsk to "Dette er trygdetida di",
                English to "This is your period of national insurance coverage"
            )
        }
        paragraph {
            textExpr(
                Bokmal to "Vi fastsetter trygdetiden din ut fra faktisk ".expr() +
                        ifElse(harFramtidigTrygdetid, ifTrue = "og framtidig ", ifFalse = "") + "trygdetid.".expr(),
                Nynorsk to "Vi fastset trygdetida di ut frå faktisk trygdetid ".expr() +
                        ifElse(harFramtidigTrygdetid, ifTrue = "og framtidig ", ifFalse = "") + "trydetid.".expr(),
                English to "Your period of national insurance coverage has been established on the basis of your actual ".expr() +
                        ifElse(
                            harFramtidigTrygdetid,
                            ifTrue = "and future ",
                            ifFalse = ""
                        ) + "periods of national insurance coverage.".expr()
            )
            text(
                Bokmal to " Den faktiske trygdetiden din er perioder du har vært medlem av folketrygden fra fylte 16 år og fram til uføretidspunktet.",
                Nynorsk to " Den faktiske trygdetida di er periodar du har vore medlem av folketrygda frå fylte 16 år og fram til uføretidspunktet.",
                English to " Your actual period of national insurance coverage is the period of coverage by the National Insurance scheme, from the time you turned 16 years old to the date of your disability."
            )
            showIf(harFramtidigTrygdetid) {
                text(
                    Bokmal to " Den framtidige trygdetiden er perioden fra uføretidspunktet ditt og fram til og med det året du fyller 66 år.",
                    Nynorsk to " Den framtidige trygdetida er perioden frå uføretidspunktet ditt og fram til og med det året du fyller 66 år.",
                    English to " Your future period of national insurance coverage is the time from the date of your disability up to and including the year you turn 66 years old."
                )
            }
        }

        paragraph {
            text(
                Bokmal to "Størrelsen på uføretrygden din er avhengig av hvor lenge du har vært medlem av folketrygden. Full trygdetid er 40 år. Dersom trygdetiden er kortere enn 40 år, blir uføretrygden redusert.",
                Nynorsk to "Storleiken på uføretrygda di er avhengig av kor lenge du har vore medlem av folketrygda. Full trygdetid er 40 år. Dersom trygdetida er kortare enn 40 år, blir uføretrygda redusert.",
                English to "The size of your disability benefit is dependent on how long you have had national insurance coverage. The full period of national insurance coverage is 40 years. If your period of national insurance coverage is shorter than 40 years, your disability benefit will be reduced."
            )
        }

        // TBU040V
        showIf(har40AarFastsattTrygdetid) {
            paragraph {
                text(
                    Bokmal to "Trygdetiden din er fastsatt til 40 år.",
                    Nynorsk to "Trygdetida di er fastsett til 40 år.",
                    English to "Your period of national insurance coverage has been set to 40 years."
                )
            }
        }

        // TBU041V
        showIf(harFlyktningstatus) {
            paragraph {
                text(
                    Bokmal to "Trygdetiden din er fastsatt til 40 år, fordi du er innvilget flyktningstatus fra Utlendingsdirektoratet. Du beholder uføretrygden beregnet med 40 års trygdetid så lenge du er bosatt i Norge.",
                    Nynorsk to "Trygdetida di er fastsett til 40 år fordi du er innvilga flyktningstatus frå Utlendingsdirektoratet. Du beheld uføretrygda berekna med 40 års trygdetid så lenge du er busett i Noreg.",
                    English to "Your period of national insurance coverage has been set to 40 years, because you have been granted refugee status by the Directorate of Immigration. You will retain a period of national insurance coverage of 40 years for as long as you live in Norway."
                )
            }
        }
        // TBU075V
        showIf(
            harYrkesskadeOppfylt and not(har40AarFastsattTrygdetid) and not(harFlyktningstatus) and not(
                harLikUfoeregradOgYrkesskadegrad
            )
        ) {
            paragraph {
                text(
                    Bokmal to "Trygdetiden din er fastsatt til 40 år, for den delen du er innvilget uføretrygd etter særbestemmelser for yrkesskade eller yrkessykdom.",
                    Nynorsk to "Trygdetida di er fastsett til 40 år for den delen du er innvilga uføretrygd etter særreglar for yrkesskade eller yrkessjukdom.",
                    English to "Your period of national insurance coverage has been set to 40 years, for the part you have been granted disability benefit pursuant to special rules relating to occupational injury or occupational illness."
                )
            }
            // TBU076V
            paragraph {
                textExpr(
                    Bokmal to "Trygdetiden i folketrygden er fastsatt til ".expr() + fastsattTrygdetid.format() + " år for den delen av uførheten din som ikke skyldes en godkjent yrkesskade eller yrkessykdom.".expr(),
                    Nynorsk to "Trygdetida i folketrygda er fastsett til ".expr() + fastsattTrygdetid.format() + " år for den delen av uføretrygda di som ikkje skuldas ein godkjend yrkesskade eller yrkessjukdom.".expr(),
                    English to "The period of national insurance coverage has been set to ".expr() + fastsattTrygdetid.format() + " years for the part of your disability that is not caused by an approved occupational injury or occupational illness.".expr(),
                )
                showIf(harTrygdetidsgrunnlag) {
                    text(
                        Bokmal to " Den faktiske trygdetiden din i denne perioden er fastsatt på grunnlag av følgende perioder:",
                        Nynorsk to " Den faktiske trygdetida di i denne perioden er fastsett på grunnlag av følgjande periodar:",
                        English to " Your actual period of national insurance coverage in this period has been determined on the basis of the following periods of coverage:"
                    )
                }
            }
        }
        // TBU042V
        showIf(harYrkesskadeOppfylt and harLikUfoeregradOgYrkesskadegrad and not(harFlyktningstatus)) {
            paragraph {
                text(
                    Bokmal to "Trygdetiden din er fastsatt til 40 år, fordi du er innvilget uføretrygd etter særbestemmelser for yrkesskade eller yrkessykdom.",
                    Nynorsk to "Trygdetida di er fastsett til 40 år fordi du er innvilga uføretrygd etter særreglar for yrkesskade eller yrkessjukdom.",
                    English to "Your period of national insurance coverage has been set to 40 years, because you have been granted disability benefit pursuant to special rules relating to occupational injury or occupational illness."
                )
            }
        }
        // TBU043V
        showIf(not(harLikUfoeregradOgYrkesskadegrad) and not(harFlyktningstatus) and not(harYrkesskadeOppfylt)) {
            paragraph {
                textExpr(
                    Bokmal to "Trygdetiden din i folketrygden er fastsatt til ".expr() + fastsattTrygdetid.format() + " år.".expr(),
                    Nynorsk to "Trygdetida di i folketrygda er fastsett til ".expr() + fastsattTrygdetid.format() + " år.".expr(),
                    English to "Your period of national insurance coverage has been set to ".expr() + fastsattTrygdetid.format() + " years.".expr()
                )
                showIf(harTrygdetidsgrunnlag) {
                    text(
                        Bokmal to " Den faktiske trygdetiden din er fastsatt på grunnlag av følgende perioder:",
                        Nynorsk to " Den faktiske trygdetida di er fastsett på grunnlag av følgjande periodar:",
                        English to " Your actual period of national insurance coverage has been determined on the basis of the following periods of coverage:"
                    )
                }
            }
        }
    }
}
