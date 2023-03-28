package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

/* TBU035V
IF Yrkesskadegrad > 0
AND KravArsakType <> soknad_bt
AND not(brevkode PE_UT_04_108, PE_UT_04_109_ PE_UT_07_200, PE_UT_06_300)
INCLUDE
*/


object BeregningAvUfoeretrygdSomSkyldesYrkesskadeEllerYrkessykdom {
    object YrkesskadeEllerYrkessykdom : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title1 {
                text(
                    Bokmal to "Beregning av uføretrygd som skyldes yrkesskade eller yrkessykdom",
                    Nynorsk to "Berekning av uføretrygd som kjem av yrkesskade eller yrkessjukdom",
                    English to "Calculation of disability benefit due to occupational injury or occupational illness",
                )
            }
            paragraph {
                text(
                    Bokmal to "For den delen av uførheten din som skyldes en godkjent yrkesskade eller yrkessykdom, fastsetter vi en yrkesskadegrad. Det er yrkesskadegraden som bestemmer hvor mye av uføretrygden din som skal beregnes etter særbestemmelser. Vi tar utgangspunkt i inntekten på skadetidspunktet ditt, når vi gjør beregningen. Skadetidspunktet blir alltid satt til den første i måneden på det tidspunktet du får en yrkesskade eller yrkessykdom.",
                    Nynorsk to "For den delen av uførleiken din som kjem av ein godkjend yrkesskade eller yrkessjukdom, fastset vi ein yrkesskadegrad. Det er yrkesskadegraden som bestemmer kor mykje av uføretrygda di som skal bereknast etter særreglar. Når vi bereknar, tek vi utgangspunkt i inntekta på skadetidspunktet ditt. Skadetidspunktet blir alltid satt til den første i månaden på det tidspunktet du får ein yrkesskade eller yrkessjukdom.",
                    English to "We will determine a degree of occupational injury for the part of your disability caused by a certified occupational injury or occupational illness. This degree of occupational injury will determine how much of your disability benefit will be calculated on the basis of special rules. We will base our calculations on your income at the time of injury. The time of injury is determined to the first of the month of your occupational injury or occupational illness."
                )
            }
        }
    }
}


