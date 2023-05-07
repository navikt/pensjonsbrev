package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text


// <> brevkode PE_UT_04_101, PE_UT_04_102, PE_UT_04_102, PE_UT_04_108, PE_UT_04_109, PE_UT_04_500, PE_UT_06_300, PE_UT_07_200,

data class ForDegSomMottarEktefelletillegg(
    val harEktefelletilleggInnvilget: Expression<Boolean>,

) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        // TBU071V
        showIf(harEktefelletilleggInnvilget) {
            title1 {
                text(
                    Bokmal to "Ektefelletillegget blir utbetalt som et fast tillegg ved siden av uføretrygden. Tillegget blir ikke endret i perioden ektefelletillegget er innvilget.",
                    Nynorsk to "Ektefelletillegget blir utbetalt som eit fast tillegg ved sida av uføretrygda. Tillegget blir ikkje endra i den perioden ektefelletillegget er innvilga for.",
                    English to "The spouse supplement is paid as a fixed supplement in addition to your disability benefit. The supplement will not be amended during the period for which the spouse supplement has been granted."
                )
            }
            // TBU072V
            paragraph {
                text(
                    Bokmal to "Du kan beholde ektefelletillegget ut vedtaksperioden, men det opphører senest 31. desember 2024.",
                    Nynorsk to "Du kan behalde ektefelletillegget ut vedtaksperioden, men det tek slutt seinast 31. desember 2024.",
                    English to "You may retain the spouse supplement until the end of the period, but it will expire on 31 December 2024 at the latest."
                )
            }
            // TBU073V
            paragraph {
                text(
                    Bokmal to "Ektefelletillegget vil falle bort hvis du skiller deg, uføretrygden opphører eller hvis ektefellen din dør.",
                    Nynorsk to "Ektefelletillegget fell bort dersom du skil deg, uføretrygda tek slutt eller dersom ektefellen din døyr.",
                    English to "The spouse supplement will terminate if you get divorced, your disability benefit ceases, or your spouse dies."
                )
            }
        }
    }
}

