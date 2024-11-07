package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.legacy.*
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import kotlin.text.format

data class TBU052V_TBU073V_ForDegSomHarRettTilBarnetillegg(
    val pe: Expression<PE>
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        //IF(PE_UT_TBU501V() = true) THEN      INCLUDE ENDIF
        showIf((pe.ut_tbu501v())){
            //[TBU052V-TBU073V]

            paragraph {
                text (
                    Bokmal to "For deg som har rett til barnetillegg",
                    Nynorsk to "For deg som har rett til barnetillegg",
                    English to "For you who are eligible for child supplement",
                )
            }
        }

        //IF(PE_UT_TBU501V() = true) THEN      INCLUDE ENDIF
        showIf((pe.ut_tbu501v())){
            //[TBU052V-TBU073V]

            paragraph {
                textExpr (
                    Bokmal to "Du har rett til barnetillegg for barn født: ".expr(),
                    Nynorsk to "Du har rett til barnetillegg for barn fødd:".expr(),
                    English to "You are entitled to child supplement for the ".expr() + pe.ut_barnet_barna_innvilget() + " born:",
                )
            }
        }

        //IF( PE_UT_TBU501V() = true AND (PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_VilkarKravlinjeKode(SYS_TableRow) = "bt" AND PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_VilkarVedtakResultat(SYS_TableRow) = "innv")  ) THEN      INCLUDE ENDIF
        showIf((pe.ut_tbu501v() and (FUNKSJON_PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_VilkarKravlinjeKode(sys_tablerow()).equalTo("bt") and FUNKSJON_PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_VilkarVedtakResultat(sys_tablerow()).equalTo("innv")))){
            //[TBU052V-TBU073V]

            paragraph {
                textExpr (
                    Bokmal to pe.ut_vilkargjelderperson_fodselsdato().format(),
                    Nynorsk to pe.ut_vilkargjelderperson_fodselsdato().format(),
                    English to pe.ut_vilkargjelderperson_fodselsdato().format(),
                )
            }
        }


        //IF(PE_UT_TBU501V() = true) THEN      INCLUDE ENDIF
        showIf((pe.ut_tbu501v())){
            //[TBU052V-TBU073V]

            paragraph {
                text (
                    Bokmal to "Barnetillegget kan utgjøre opptil 40 prosent av folketrygdens grunnbeløp for hvert barn du forsørger. Du har rett til barnetillegg så lenge du forsørger barn som er under 18 år. Barnetillegget opphører når barnet fyller 18 år. ",
                    Nynorsk to "Barnetillegget kan utgjere opptil 40 prosent av grunnbeløpet i folketrygda for kvart barn du forsørgjer. Du har rett til barnetillegg så lenge du forsørgjer barn som er under 18 år. Barnetillegget opphøyrer når barnet fyller 18 år.",
                    English to "The child supplement may be up to 40 percent of the national insurance basic amount for each child you support. You are entitled to child supplement as long as you support children under 18 years of age. Payment of child supplement stops when the child turns 18. ",
                )

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_AnvendtTrygdetid < 40 AND PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat(1) <> "oppfylt") THEN      INCLUDE ENDIF
                showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_anvendttrygdetid().lessThan(40) and FUNKSJON_PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat(1).notEqualTo("oppfylt"))){
                    text (
                        Bokmal to "Hvor mye du får i barnetillegg er også avhengig av trygdetiden din. Fordi trygdetiden din er kortere enn 40 år, blir barnetillegget ditt redusert. ",
                        Nynorsk to " Kor mykje du får i barnetillegg, er også avhengig av trygdetida di. Fordi trygdetida di er kortare enn 40 år, blir barnetillegget ditt redusert.",
                        English to "How much child supplement you receive depends on your period of national insurance cover. As your period of national insurance cover is less than 40 years, your child supplement will be reduced.",
                    )
                }
            }
        }
    }
}