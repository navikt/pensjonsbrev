package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.pebrevkode
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_belopsgrense
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_kravhode_kravarsaktype
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text


data class TBU034V_036V(
    val pe: Expression<PE>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        //IF(PE_pebrevkode <> "PE_UT_05_100" AND PE_pebrevkode <> "PE_UT_07_100" AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense <> 60000 AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_pebrevkode <> "PE_UT_04_108" AND PE_pebrevkode <> "PE_UT_04_109" AND PE_pebrevkode <> "PE_UT_07_200" AND PE_pebrevkode <> "PE_UT_06_300") THEN      INCLUDE ENDIF
        showIf(
            pe.pebrevkode().notEqualTo("PE_UT_05_100")
                    and pe.pebrevkode().notEqualTo("PE_UT_07_100")
                    and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_belopsgrense()
                .notEqualTo(
                    60000
                )

        ) {
            //[TBU034V-TBU36V]

            paragraph {
                text(
                    Bokmal to "Folketrygdens grunnbeløp endres hvert år, og uføretrygden din blir justert ut fra dette.",
                    Nynorsk to "Grunnbeløpet i folketrygda blir endra kvart år, og uføretrygda di blir justert ut frå dette.",
                    English to "The National Insurance basic amount changes every year, and your disability benefit will be adjusted in accordance with this."
                )
            }
        }

        //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad > 0 AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_pebrevkode <> "PE_UT_04_108" AND PE_pebrevkode <> "PE_UT_04_109" AND PE_pebrevkode <> "PE_UT_07_200" AND PE_pebrevkode <> "PE_UT_06_300") THEN      INCLUDE ENDIF
        showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_yrkesskadegrad().greaterThan(0) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.pebrevkode().notEqualTo("PE_UT_04_108") and pe.pebrevkode().notEqualTo("PE_UT_04_109") and pe.pebrevkode().notEqualTo("PE_UT_07_200") and pe.pebrevkode().notEqualTo("PE_UT_06_300"))) {
            //[TBU034V-TBU36V]

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
                    English to "We will determine a degree of occupational injury for the part of your disability caused by a certified occupational injury or occupational illness. This degree of occupational injury will determine how much of your disability benefit will be calculated on the basis of special rules. We will base our calculations on your income at the time of injury. The time of injury is determined to the first of the month of your occupational injury or occupational illness.",
                )
            }
        }

        //IF(PE_pebrevkode <> "PE_UT_07_100"  AND PE_pebrevkode <> "PE_UT_05_100"  AND PE_pebrevkode <> "PE_UT_04_300"  AND PE_pebrevkode <> "PE_UT_14_300"  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense <> 60000  AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt"  AND PE_pebrevkode <> "PE_UT_04_108"  AND PE_pebrevkode <> "PE_UT_04_109"  AND PE_pebrevkode <> "PE_UT_07_200"   AND PE_pebrevkode <> "PE_UT_06_300" AND (PE_pebrevkode <> "PE_UT_04_500"  AND (PE_pebrevkode <> "PE_UT_04_102"      OR (PE_pebrevkode = "PE_UT_04_102"     AND PE_Vedtaksdata_Kravhode_KravArsakType <> "tilst_dod")))) THEN      INCLUDE ENDIF
        showIf(
            (pe.pebrevkode().notEqualTo("PE_UT_07_100")
                    and pe.pebrevkode().notEqualTo("PE_UT_05_100")
                    and pe.pebrevkode().notEqualTo("PE_UT_04_300")
                    and pe.pebrevkode().notEqualTo("PE_UT_14_300")
                    and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_belopsgrense()
                .notEqualTo(60000)
                    and (pe.pebrevkode()
                .notEqualTo("PE_UT_04_500") and (pe.pebrevkode().notEqualTo("PE_UT_04_102") or (pe.pebrevkode().equalTo("PE_UT_04_102")
                    and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("tilst_dod")))))
        ) {
            //[TBU034V-TBU36V]

            title1 {
                text(
                    Bokmal to "Dette er inntektene vi har brukt i beregningen din",
                    Nynorsk to "Dette er inntektene vi har brukt i berekninga di",
                    English to "This is the income on which we have based your calculations",
                )
            }
        }
    }
}