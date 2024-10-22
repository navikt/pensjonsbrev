package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.maler.fraser.common.Constants.GRUNNBELOEP_URL
import no.nav.pensjon.brev.maler.legacy.FUNKSJON_Month
import no.nav.pensjon.brev.maler.legacy.FUNKSJON_Year
import no.nav.pensjon.brev.maler.legacy.pebrevkode
import no.nav.pensjon.brev.maler.legacy.ut_forstegangstjenesteikkenull
import no.nav.pensjon.brev.maler.legacy.ut_sisteopptjeningarlikuforetidspunkt
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_belopsgrense
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagordinar_opptjeningutliste_opptjeningut_forstegansgstjeneste
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagordinar_opptjeningutliste_opptjeningut_omsorgsaar
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_kravhode_kravarsaktype
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforetidspunkt
import no.nav.pensjon.brev.maler.legacy.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningstidpunkt
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.lessThan
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.text

data class TBU011V_TBU016V(val pe: Expression<PE>): OutlinePhrase<LangBokmalNynorskEnglish>(){
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {

        //IF(PE_pebrevkode <> "PE_UT_07_100" AND PE_pebrevkode <> "PE_UT_05_100" AND PE_pebrevkode <> "PE_UT_04_300" AND PE_pebrevkode <> "PE_UT_14_300" AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense <> 60000 AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_pebrevkode <> "PE_UT_04_108" AND PE_pebrevkode <> "PE_UT_04_109" AND PE_pebrevkode <> "PE_UT_07_200" AND PE_pebrevkode <> "PE_UT_06_300") THEN      INCLUDE ENDIF
        showIf((pe.pebrevkode().notEqualTo("PE_UT_07_100") and pe.pebrevkode().notEqualTo("PE_UT_05_100") and pe.pebrevkode().notEqualTo("PE_UT_04_300") and pe.pebrevkode().notEqualTo("PE_UT_14_300") and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_belopsgrense().notEqualTo(60000) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.pebrevkode().notEqualTo("PE_UT_04_108") and pe.pebrevkode().notEqualTo("PE_UT_04_109") and pe.pebrevkode().notEqualTo("PE_UT_07_200") and pe.pebrevkode().notEqualTo("PE_UT_06_300"))){
            //[TBU011V-TBU016V]

            title1 {
                text (
                    Bokmal to "Slik beregner vi uføretrygden din",
                    Nynorsk to "Slik bereknar vi uføretrygda di",
                    English to "This is how your disability benefit is calculated",
                )
            }

            //IF( ( Year(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforetidspunkt(1))<>Year(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Virkningstidpunkt(1))  OR Month(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforetidspunkt(1)) > 06 OR Month(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Virkningstidpunkt(1)) > 06 ) AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Forstegansgstjeneste(1) = 0 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Omsorgsaar(1) = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode = "folketrygd" )THEN INCLUDE ENDIF
            showIf(((FUNKSJON_Year(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforetidspunkt()).notEqualTo(FUNKSJON_Year(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningstidpunkt())) or FUNKSJON_Month(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforetidspunkt()).greaterThan(6) or FUNKSJON_Month(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningstidpunkt()).greaterThan(6)) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagordinar_opptjeningutliste_opptjeningut_forstegansgstjeneste().equalTo(0) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagordinar_opptjeningutliste_opptjeningut_omsorgsaar()) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("folketrygd"))){
                //[TBU011V-TBU016V]

                paragraph {
                    text (
                        Bokmal to "Når vi beregner uføretrygden din, bruker vi gjennomsnittsinntekten i de tre beste av de fem siste årene før du ble ufør. Inntekt opptil seks ganger folketrygdens grunnbeløp (G) blir tatt med i beregningen. Uføretrygden utgjør 66 prosent av beregningsgrunnlaget. Du finner størrelsen på grunnbeløpet på $GRUNNBELOEP_URL.",
                        Nynorsk to "Når vi bereknar uføretrygda di, bruker vi gjennomsnittsinntekta i dei tre beste av dei fem siste åra før du blei ufør. Inntekt opptil seks gonger grunnbeløpet (G) i folketrygda blir teken med i berekninga. Uføretrygda utgjer 66 prosent av berekningsgrunnlaget. Du finn storleiken på grunnbeløpet på $GRUNNBELOEP_URL.",
                        English to "In calculating your disability benefit, we base calculations on your average income for the best three of the last five years prior to the onset of your disability. Income up to six times the National Insurance basic amount (G) is included in the calculation. The disability benefit equals 66 percent of the basis for calculation. You can find out how much the basic amount is at $GRUNNBELOEP_URL.",
                    )
                }
            }

            //IF( ( Year(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforetidspunkt(1))=Year(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Virkningstidpunkt(1))  AND Month(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforetidspunkt(1)) < 06 AND Month(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Virkningstidpunkt(1)) < 06 ) AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode = "folketrygd" )THEN INCLUDE ENDIF
            showIf(((FUNKSJON_Year(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforetidspunkt()).equalTo(FUNKSJON_Year(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningstidpunkt())) and FUNKSJON_Month(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforetidspunkt()).lessThan(6) and FUNKSJON_Month(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningstidpunkt()).lessThan(6)) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("folketrygd"))){
                //[TBU011V-TBU016V]

                paragraph {
                    text (
                        Bokmal to "Når vi beregner uføretrygden din, bruker vi som hovedregel gjennomsnittsinntekten i de tre beste av de fem siste årene før du ble ufør. Fordi ligningen din for året før du ble ufør ikke er ferdig, bruker vi her gjennomsnittsinntekten i de tre beste av de fire siste årene før du ble ufør.",
                        Nynorsk to "Når vi bereknar uføretrygda di, bruker vi som hovudregel gjennomsnittsinntekta i dei tre beste av dei fem siste åra før du blei ufør. Fordi likninga di for året før du blei ufør, ikkje er ferdig, bruker vi her gjennomsnittsinntekta i dei tre beste av dei fire siste åra før du blei ufør.",
                        English to "In calculating your disability benefit, calculations are, as a main rule, based on your average income for the best three of the last five years prior to the onset of your disability. Because the tax assessment for the year of the onset of your disability is not yet completed, we have based these calculations on the best three of the last four years prior to the onset of your disability.",
                    )
                }
            }

            //IF( ( Year(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforetidspunkt(1))=Year(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Virkningstidpunkt(1))  AND Month(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforetidspunkt(1)) < 06 AND Month(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Virkningstidpunkt(1)) < 06 ) AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode = "folketrygd" )THEN INCLUDE ENDIF
            showIf(((FUNKSJON_Year(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforetidspunkt()).equalTo(FUNKSJON_Year(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningstidpunkt())) and FUNKSJON_Month(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforetidspunkt()).lessThan(6) and FUNKSJON_Month(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningstidpunkt()).lessThan(6)) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("folketrygd"))){
                //[TBU011V-TBU016V]

                paragraph {
                    text (
                        Bokmal to "Bare inntekt opptil seks ganger folketrygdens grunnbeløp (G) regnes med. Uføretrygden utgjør 66 prosent av beregningsgrunnlaget. Du finner størrelsen på grunnbeløpet på $GRUNNBELOEP_URL.",
                        Nynorsk to "Berre inntekt opptil seks gonger grunnbeløpet (G) i folketrygda blir rekna med. Uføretrygda utgjer 66 prosent av berekningsgrunnlaget. Du finn storleiken på grunnbeløpet på $GRUNNBELOEP_URL.",
                        English to "The calculations only include income up to six times the National Insurance basic amount (G). The disability benefit equals 66 percent of the basis for calculation. You can find out how much the basic amount is at $GRUNNBELOEP_URL.",
                    )
                }
            }

            //IF( ( Year(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforetidspunkt(1))=Year(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Virkningstidpunkt(1))  AND Month(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforetidspunkt(1)) < 06 AND Month(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Virkningstidpunkt(1)) < 06 ) AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode = "folketrygd" )THEN INCLUDE ENDIF
            showIf(((FUNKSJON_Year(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforetidspunkt()).equalTo(FUNKSJON_Year(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningstidpunkt())) and FUNKSJON_Month(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforetidspunkt()).lessThan(6) and FUNKSJON_Month(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningstidpunkt()).lessThan(6)) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("folketrygd"))){
                //[TBU011V-TBU016V]

                paragraph {
                    text (
                        Bokmal to "Når ligningen er ferdig, vil uføretrygden din bli beregnet på nytt. Du vil få et nytt vedtaksbrev om dette.",
                        Nynorsk to "Når likninga er ferdig, blir uføretrygda di berekna på nytt. Du får eit nytt vedtaksbrev om dette.",
                        English to "Once the tax assessment is completed, your disability benefit will be recalculated. You will receive a new letter informing you of any new developments.",
                    )
                }
            }

            //IF( PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode = "folketrygd"  AND  PE_UT_SisteOpptjeningArLikUforetidspunkt() AND PE_UT_ForstegangstjenesteIkkeNull()   ) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("folketrygd") and pe.ut_sisteopptjeningarlikuforetidspunkt() and pe.ut_forstegangstjenesteikkenull())){
                //[TBU011V-TBU016V]

                paragraph {
                    text (
                        Bokmal to "Når vi beregner uføretrygden din, bruker vi som hovedregel gjennomsnittsinntekten i de tre beste av de fem siste årene før du ble ufør. Du har vært i militæret, eller hatt sivil førstegangstjeneste. Inntekt i denne perioden skal utgjøre minst tre ganger gjennomsnittlig G (folketrygdens grunnbeløp). ",
                        Nynorsk to "Når vi bereknar uføretrygda di, bruker vi som hovudregel gjennomsnittsinntekta i dei tre beste av dei fem siste åra før du blei ufør. Du har vore i militæret, eller hatt sivil førstegongsteneste. Inntekt i denne perioden skal utgjere minst tre gonger gjennomsnittleg G (grunnbeløpet i folketrygda). ",
                        English to "In calculating your disability benefit, calculations are, as a main rule, based on your average income for the best three of the last five years prior to the onset of your disability. You have served in the military, or completed your initial service as a civilian. Income during this period must total no less than three times the average G (National Insurance basic amount). ",
                    )
                    //Failed to convert with error: Unexpected character: - at line 6 :

                    //Integer i FOR i = 2 TO Count(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Ar) 	IF( 		FF_GetArrayElement_Float(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Forstegansgstjeneste) <> 0 		AND 		FF_GetArrayElement_Boolean(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Brukt,i-1) = true  	) THEN      		INCLUDE 	ENDIF NEXT

                    text (
                        Bokmal to "Du hadde en høyere inntekt i året før du avtjente førstegangstjeneste, og vi bruker derfor denne inntekten i beregningen.",
                        Nynorsk to "Du hadde ei høgare inntekt i året før du avtente førstegongstenesta, og vi bruker derfor denne inntekta i berekninga.",
                        English to "Your income was higher in the year prior to your initial service, and we have thus applied this incomed in the calculations.",
                    )
                }
            }

            //IF( PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode = "folketrygd"  AND  PE_UT_SisteOpptjeningArLikUforetidspunkt() AND PE_UT_ForstegangstjenesteIkkeNull()   ) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("folketrygd") and pe.ut_sisteopptjeningarlikuforetidspunkt() and pe.ut_forstegangstjenesteikkenull())){
                //[TBU011V-TBU016V]

                paragraph {
                    text (
                        Bokmal to "Bare inntekt opptil seks ganger folketrygdens grunnbeløp (G) regnes med. Uføretrygden utgjør 66 prosent av beregningsgrunnlaget. Du finner størrelsen på grunnbeløpet på $GRUNNBELOEP_URL.",
                        Nynorsk to "Berre inntekt opptil seks gonger grunnbeløpet (G) i folketrygda blir rekna med. Uføretrygda utgjer 66 prosent av berekningsgrunnlaget. Du finn storleiken på grunnbeløpet på $GRUNNBELOEP_URL.",
                        English to "The calculations only include income up to six times the National Insurance basic amount (G). The disability benefit equals 66 percent of the basis for calculation. You can find out how much the basic amount is at $GRUNNBELOEP_URL.",
                    )
                }
            }
            //Failed to convert with error: Exstream logikk har innhold før if. Tolkes ikke.

            //Integer i  IF( 	PE_UT_SisteOpptjeningArLikUforetidspunkt() 	AND 	PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode = "folketrygd") 	THEN 	 	FOR i = 1 TO Count(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Ar) 		IF(                                                 FF_GetArrayElement_Boolean(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Omsorgsaar, i) = TRUE                                 ) 		THEN 			INCLUDE 		ENDIF 	NEXT ENDIF

            //[TBU011V-TBU016V]

            paragraph {
                text (
                    Bokmal to "Når vi beregner uføretrygden din, bruker vi som hovedregel gjennomsnittsinntekten i de tre beste av de fem siste årene før du ble ufør. Du har hatt pensjonsopptjening på grunnlag av omsorgsarbeid i ett eller flere av disse årene. Vi bruker disse årene i beregningen, hvis dette er en fordel for deg.",
                    Nynorsk to "Når vi bereknar uføretrygda di, bruker vi som hovudregel gjennomsnittsinntekta i dei tre beste av dei fem siste åra før du blei ufør. Du har hatt pensjonsopptening på grunnlag av omsorgsarbeid i eitt eller fleire av desse åra. Vi bruker desse åra i berekninga dersom dette er ein fordel for deg.",
                    English to "In calculating your disability benefit, calculations are, as a main rule, based on your average income for the best three of the last five years prior to the onset of your disability. You have earned pension points due to care work during one or more of these years. We will include these years in the calculation, if this is to your advantage.",
                )
            }
            //Failed to convert with error: Exstream logikk har innhold før if. Tolkes ikke.

            //Integer i  IF( 	PE_UT_SisteOpptjeningArLikUforetidspunkt() 	AND 	PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode = "folketrygd") 	THEN 	 	FOR i = 1 TO Count(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Ar) 		IF(                                                 FF_GetArrayElement_Boolean(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Omsorgsaar, i) = TRUE                                 ) 		THEN 			INCLUDE 		ENDIF 	NEXT ENDIF

            //[TBU011V-TBU016V]

            paragraph {
                text (
                    Bokmal to "Bare inntekt opptil seks ganger folketrygdens grunnbeløp (G) regnes med. Uføretrygden utgjør 66 prosent av beregningsgrunnlaget. Du finner størrelsen på grunnbeløpet på $GRUNNBELOEP_URL.",
                    Nynorsk to "Berre inntekt opptil seks gonger grunnbeløpet (G) i folketrygda blir rekna med. Uføretrygda utgjer 66 prosent av berekningsgrunnlaget. Du finn storleiken på grunnbeløpet på $GRUNNBELOEP_URL.",
                    English to "The calculations only include income up to six times the National Insurance basic amount (G). The disability benefit equals 66 percent of the basis for calculation. You can find out how much the basic amount is at $GRUNNBELOEP_URL.",
                )
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode = "eos") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("eos"))){
                //[TBU011V-TBU016V]

                paragraph {
                    text (
                        Bokmal to "Når vi beregner uføretrygden din, bruker vi gjennomsnittsinntekten i de tre beste av de fem siste årene før du ble ufør. Inntekt opptil seks ganger folketrygdens grunnbeløp (G) blir tatt med i beregningen. Uføretrygden utgjør 66 prosent av beregningsgrunnlaget. Du finner størrelsen på grunnbeløpet på $GRUNNBELOEP_URL.",
                        Nynorsk to "Når vi bereknar uføretrygda di, bruker vi gjennomsnittsinntekta i dei tre beste av dei fem siste åra før du blei ufør. Inntekt opptil seks gonger grunnbeløpet i folketrygda (G) blir teke med i berekninga. Uføretrygda utgjer 66 prosent av berekningsgrunnlaget. Du finn storleiken på grunnbeløpet på $GRUNNBELOEP_URL.",
                        English to "In calculating your disability benefit, we base calculations on your average income for the best three of the last five years prior to the onset of your disability. Income up to six times the National Insurance basic amount (G) is included in the calculation. The disability benefit equals 66 percent of the basis for calculation. You can find out how much the basic amount is at $GRUNNBELOEP_URL.",
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode = "eos") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("eos"))){
                //[TBU011V-TBU016V]

                paragraph {
                    text (
                        Bokmal to "Du hadde inntekt i utlandet i minst ett av de fem siste årene før du ble ufør. Vi bruker ikke denne inntekten når vi beregner uføretrygden din. For å kompensere for dette, erstatter vi disse årene med et gjennomsnitt av årene du har hatt inntekt i Norge i denne femårsperioden. Du kan se hvilke år vi har brukt i tabellen «Inntekt lagt til grunn for beregning av uføretrygden din».",
                        Nynorsk to "Du hadde inntekt i utlandet i minst eitt av dei fem siste åra før du blei ufør. Vi bruker ikkje denne inntekta når vi bereknar uføretrygda di. For å kompensere for dette erstattar vi desse åra med eit gjennomsnitt av åra du har hatt inntekt i Noreg i denne femårsperioden. Du kan sjå kva år vi har brukt, i tabellen «Inntekt lagd til grunn for berekning av uføretrygda di».",
                        English to "You had income abroad for at least one of the last five years prior to the onset of your disability. This income will not be included when we calculate your disability benefit. To compensate, we will instead apply an average of your income from Norway during this five-year period. You can see which years we have applied in the table called \"Income included in the basis for calculation of your disability benefit\".",
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode = "eos") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("eos"))){
                //[TBU011V-TBU016V]

                paragraph {
                    text (
                        Bokmal to "Når vi beregner gjennomsnittet bruker vi bare de årene du hadde inntekt i Norge i femårsperioden. Hvis du hadde inntekt i Norge og i utlandet samme år, bruker vi den inntekten som er best for deg.",
                        Nynorsk to "Når vi bereknar gjennomsnittet, bruker vi berre dei åra du hadde inntekt i Noreg i femårsperioden. Dersom du hadde inntekt i Noreg og i utlandet same året, bruker vi den inntekta som er best for deg.",
                        English to "When we calculate the average income, we only include the years you had income in Norway during this five-year period. If you had income in Norway and abroad during the same year, we apply the income that will benefit you most.",
                    )
                }
            }
        }
    }

}
