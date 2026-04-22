package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.maler.fraser.common.Constants.GRUNNBELOEP_URL
import no.nav.pensjon.brev.maler.legacy.*
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text

data class TBU011V_TBU016V(val pe: Expression<PEgruppe10>): OutlinePhrase<LangBokmalNynorsk>(){
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {

        //IF(PE_pebrevkode <> "PE_UT_07_100" AND PE_pebrevkode <> "PE_UT_05_100" AND PE_pebrevkode <> "PE_UT_04_300" AND PE_pebrevkode <> "PE_UT_14_300" AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense <> 60000 AND PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_pebrevkode <> "PE_UT_04_108" AND PE_pebrevkode <> "PE_UT_04_109" AND PE_pebrevkode <> "PE_UT_07_200" AND PE_pebrevkode <> "PE_UT_06_300") THEN      INCLUDE ENDIF
        showIf((pe.pebrevkode().notEqualTo("PE_UT_07_100") and pe.pebrevkode().notEqualTo("PE_UT_05_100") and pe.pebrevkode().notEqualTo("PE_UT_04_300") and pe.pebrevkode().notEqualTo("PE_UT_14_300") and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_belopsgrense().notEqualTo(60000) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.pebrevkode().notEqualTo("PE_UT_04_108") and pe.pebrevkode().notEqualTo("PE_UT_04_109") and pe.pebrevkode().notEqualTo("PE_UT_07_200") and pe.pebrevkode().notEqualTo("PE_UT_06_300"))){
            //[TBU011V-TBU016V]

            title1 {
                text (
                    bokmal { + "Slik beregner vi uføretrygden din" },
                    nynorsk { + "Slik bereknar vi uføretrygda di" },
                )
            }

            //IF( ( Year(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforetidspunkt(1))<>Year(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Virkningstidpunkt(1))  OR Month(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforetidspunkt(1)) > 06 OR Month(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Virkningstidpunkt(1)) > 06 ) AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Forstegansgstjeneste(1) = 0 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Omsorgsaar(1) = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode = "folketrygd" )THEN INCLUDE ENDIF
            showIf(((FUNKSJON_Year(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforetidspunkt()).notEqualTo(FUNKSJON_Year(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningstidpunkt())) or FUNKSJON_Month(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforetidspunkt()).greaterThan(6) or FUNKSJON_Month(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningstidpunkt()).greaterThan(6)) and pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagordinar_opptjeningutliste_opptjeningut_forstegansgstjeneste().equalTo(0) and not(pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_ytelsesgrunnlag_beregningsgrunnlagordinar_opptjeningutliste_opptjeningut_omsorgsaar()) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("folketrygd"))){
                //[TBU011V-TBU016V]

                paragraph {
                    text (
                        bokmal { + "Når vi beregner uføretrygden din, bruker vi gjennomsnittsinntekten i de tre beste av de fem siste årene før du ble ufør. Inntekt opptil seks ganger folketrygdens grunnbeløp (G) blir tatt med i beregningen. Uføretrygden utgjør 66 prosent av beregningsgrunnlaget. Du finner størrelsen på grunnbeløpet på $GRUNNBELOEP_URL." },
                        nynorsk { + "Når vi bereknar uføretrygda di, bruker vi gjennomsnittsinntekta i dei tre beste av dei fem siste åra før du blei ufør. Inntekt opptil seks gonger grunnbeløpet (G) i folketrygda blir teken med i berekninga. Uføretrygda utgjer 66 prosent av berekningsgrunnlaget. Du finn storleiken på grunnbeløpet på $GRUNNBELOEP_URL." },
                    )
                }
            }

            //IF( ( Year(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforetidspunkt(1))=Year(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Virkningstidpunkt(1))  AND Month(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforetidspunkt(1)) < 06 AND Month(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Virkningstidpunkt(1)) < 06 ) AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode = "folketrygd" )THEN INCLUDE ENDIF
            showIf(((FUNKSJON_Year(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforetidspunkt()).equalTo(FUNKSJON_Year(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningstidpunkt()))
                    and FUNKSJON_Month(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforetidspunkt()).lessThan(6) and FUNKSJON_Month(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningstidpunkt()).lessThan(6))
                    and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("folketrygd"))){
                //[TBU011V-TBU016V]

                paragraph {
                    text (
                        bokmal { + "Når vi beregner uføretrygden din, bruker vi som hovedregel gjennomsnittsinntekten i de tre beste av de fem siste årene før du ble ufør. Fordi ligningen din for året før du ble ufør ikke er ferdig, bruker vi her gjennomsnittsinntekten i de tre beste av de fire siste årene før du ble ufør." },
                        nynorsk { + "Når vi bereknar uføretrygda di, bruker vi som hovudregel gjennomsnittsinntekta i dei tre beste av dei fem siste åra før du blei ufør. Fordi likninga di for året før du blei ufør, ikkje er ferdig, bruker vi her gjennomsnittsinntekta i dei tre beste av dei fire siste åra før du blei ufør." },
                    )
                }
            }

            //IF( ( Year(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforetidspunkt(1))=Year(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Virkningstidpunkt(1))  AND Month(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforetidspunkt(1)) < 06 AND Month(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Virkningstidpunkt(1)) < 06 ) AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode = "folketrygd" )THEN INCLUDE ENDIF
            showIf(((FUNKSJON_Year(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforetidspunkt()).equalTo(FUNKSJON_Year(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningstidpunkt())) and FUNKSJON_Month(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforetidspunkt()).lessThan(6) and FUNKSJON_Month(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningstidpunkt()).lessThan(6)) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("folketrygd"))){
                //[TBU011V-TBU016V]

                paragraph {
                    text (
                        bokmal { + "Bare inntekt opptil seks ganger folketrygdens grunnbeløp (G) regnes med. Uføretrygden utgjør 66 prosent av beregningsgrunnlaget. Du finner størrelsen på grunnbeløpet på $GRUNNBELOEP_URL." },
                        nynorsk { + "Berre inntekt opptil seks gonger grunnbeløpet (G) i folketrygda blir rekna med. Uføretrygda utgjer 66 prosent av berekningsgrunnlaget. Du finn storleiken på grunnbeløpet på $GRUNNBELOEP_URL." },
                    )
                }
            }

            //IF( ( Year(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforetidspunkt(1))=Year(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Virkningstidpunkt(1))  AND Month(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforetidspunkt(1)) < 06 AND Month(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Virkningstidpunkt(1)) < 06 ) AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode = "folketrygd" )THEN INCLUDE ENDIF
            showIf(((FUNKSJON_Year(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforetidspunkt()).equalTo(FUNKSJON_Year(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningstidpunkt())) and FUNKSJON_Month(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_uforetidspunkt()).lessThan(6) and FUNKSJON_Month(pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_virkningstidpunkt()).lessThan(6)) and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("folketrygd"))){
                //[TBU011V-TBU016V]

                paragraph {
                    text (
                        bokmal { + "Når ligningen er ferdig, vil uføretrygden din bli beregnet på nytt. Du vil få et nytt vedtaksbrev om dette." },
                        nynorsk { + "Når likninga er ferdig, blir uføretrygda di berekna på nytt. Du får eit nytt vedtaksbrev om dette." },
                    )
                }
            }

            //IF( PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode = "folketrygd"  AND  PE_UT_SisteOpptjeningArLikUforetidspunkt() AND PE_UT_ForstegangstjenesteIkkeNull()   ) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("folketrygd") and pe.ut_sisteopptjeningarlikuforetidspunkt() and pe.ut_forstegangstjenesteikkenull())){
                //[TBU011V-TBU016V]

                paragraph {
                    text (
                        bokmal { + "Når vi beregner uføretrygden din, bruker vi som hovedregel gjennomsnittsinntekten i de tre beste av de fem siste årene før du ble ufør. Du har vært i militæret, eller hatt sivil førstegangstjeneste. Inntekt i denne perioden skal utgjøre minst tre ganger gjennomsnittlig G (folketrygdens grunnbeløp). " },
                        nynorsk { + "Når vi bereknar uføretrygda di, bruker vi som hovudregel gjennomsnittsinntekta i dei tre beste av dei fem siste åra før du blei ufør. Du har vore i militæret, eller hatt sivil førstegongsteneste. Inntekt i denne perioden skal utgjere minst tre gonger gjennomsnittleg G (grunnbeløpet i folketrygda). " },
                    )

                    //Integer i
                    // FOR i = 2 TO Count(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Ar)
                        // IF( 		FF_GetArrayElement_Float(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Forstegansgstjeneste) <> 0
                            // AND 		FF_GetArrayElement_Boolean(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Brukt,i-1) = true  	)
                                // THEN INCLUDE
                            // ENDIF
                    // NEXT

                    // Manuellt konvertert
                    showIf(pe.harOpptjeningUTMedOpptjeningBruktAaretFoerOgFoerstegangstjeneste()){
                        text (
                            bokmal { + "Du hadde en høyere inntekt i året før du avtjente førstegangstjeneste, og vi bruker derfor denne inntekten i beregningen." },
                            nynorsk { + "Du hadde ei høgare inntekt i året før du avtente førstegongstenesta, og vi bruker derfor denne inntekta i berekninga." },
                        )
                    }
                }
            }

            //IF( PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode = "folketrygd"  AND  PE_UT_SisteOpptjeningArLikUforetidspunkt() AND PE_UT_ForstegangstjenesteIkkeNull()   ) THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("folketrygd") and pe.ut_sisteopptjeningarlikuforetidspunkt() and pe.ut_forstegangstjenesteikkenull())){
                //[TBU011V-TBU016V]

                paragraph {
                    text (
                        bokmal { + "Bare inntekt opptil seks ganger folketrygdens grunnbeløp (G) regnes med. Uføretrygden utgjør 66 prosent av beregningsgrunnlaget. Du finner størrelsen på grunnbeløpet på $GRUNNBELOEP_URL." },
                        nynorsk { + "Berre inntekt opptil seks gonger grunnbeløpet (G) i folketrygda blir rekna med. Uføretrygda utgjer 66 prosent av berekningsgrunnlaget. Du finn storleiken på grunnbeløpet på $GRUNNBELOEP_URL." },
                    )
                }
            }

            //Integer i  IF( 	PE_UT_SisteOpptjeningArLikUforetidspunkt() 	AND 	PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode = "folketrygd") 	THEN 	 	FOR i = 1 TO Count(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Ar) 		IF(                                                 FF_GetArrayElement_Boolean(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Omsorgsaar, i) = TRUE                                 ) 		THEN 			INCLUDE 		ENDIF 	NEXT ENDIF
            //[TBU011V-TBU016V]

            // Manuellt konvertert
            showIf(pe.ut_sisteopptjeningarlikuforetidspunkt()
                    and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("folketrygd")
                    and pe.harOpptjeningUTMedOmsorg()
            ){
                paragraph {
                    text (
                        bokmal { + "Når vi beregner uføretrygden din, bruker vi som hovedregel gjennomsnittsinntekten i de tre beste av de fem siste årene før du ble ufør. Du har hatt pensjonsopptjening på grunnlag av omsorgsarbeid i ett eller flere av disse årene. Vi bruker disse årene i beregningen, hvis dette er en fordel for deg." },
                        nynorsk { + "Når vi bereknar uføretrygda di, bruker vi som hovudregel gjennomsnittsinntekta i dei tre beste av dei fem siste åra før du blei ufør. Du har hatt pensjonsopptening på grunnlag av omsorgsarbeid i eitt eller fleire av desse åra. Vi bruker desse åra i berekninga dersom dette er ein fordel for deg." },
                    )
                }

                paragraph {
                    text (
                        bokmal { + "Bare inntekt opptil seks ganger folketrygdens grunnbeløp (G) regnes med. Uføretrygden utgjør 66 prosent av beregningsgrunnlaget. Du finner størrelsen på grunnbeløpet på $GRUNNBELOEP_URL." },
                        nynorsk { + "Berre inntekt opptil seks gonger grunnbeløpet (G) i folketrygda blir rekna med. Uføretrygda utgjer 66 prosent av berekningsgrunnlaget. Du finn storleiken på grunnbeløpet på $GRUNNBELOEP_URL." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode = "eos") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("eos"))){
                //[TBU011V-TBU016V]

                paragraph {
                    text (
                        bokmal { + "Når vi beregner uføretrygden din, bruker vi gjennomsnittsinntekten i de tre beste av de fem siste årene før du ble ufør. Inntekt opptil seks ganger folketrygdens grunnbeløp (G) blir tatt med i beregningen. Uføretrygden utgjør 66 prosent av beregningsgrunnlaget. Du finner størrelsen på grunnbeløpet på $GRUNNBELOEP_URL." },
                        nynorsk { + "Når vi bereknar uføretrygda di, bruker vi gjennomsnittsinntekta i dei tre beste av dei fem siste åra før du blei ufør. Inntekt opptil seks gonger grunnbeløpet i folketrygda (G) blir teke med i berekninga. Uføretrygda utgjer 66 prosent av berekningsgrunnlaget. Du finn storleiken på grunnbeløpet på $GRUNNBELOEP_URL." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode = "eos") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("eos"))){
                //[TBU011V-TBU016V]

                paragraph {
                    text (
                        bokmal { + "Du hadde inntekt i utlandet i minst ett av de fem siste årene før du ble ufør. Vi bruker ikke denne inntekten når vi beregner uføretrygden din. For å kompensere for dette, erstatter vi disse årene med et gjennomsnitt av årene du har hatt inntekt i Norge i denne femårsperioden. Du kan se hvilke år vi har brukt i tabellen " + quoted("Inntekt lagt til grunn for beregning av uføretrygden din") +"." },
                        nynorsk { + "Du hadde inntekt i utlandet i minst eitt av dei fem siste åra før du blei ufør. Vi bruker ikkje denne inntekta når vi bereknar uføretrygda di. For å kompensere for dette erstattar vi desse åra med eit gjennomsnitt av åra du har hatt inntekt i Noreg i denne femårsperioden. Du kan sjå kva år vi har brukt, i tabellen " + quoted("Inntekt lagd til grunn for berekning av uføretrygda di") +"." },
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode = "eos") THEN      INCLUDE ENDIF
            showIf((pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("eos"))){
                //[TBU011V-TBU016V]

                paragraph {
                    text (
                        bokmal { + "Når vi beregner gjennomsnittet bruker vi bare de årene du hadde inntekt i Norge i femårsperioden. Hvis du hadde inntekt i Norge og i utlandet samme år, bruker vi den inntekten som er best for deg." },
                        nynorsk { + "Når vi bereknar gjennomsnittet, bruker vi berre dei åra du hadde inntekt i Noreg i femårsperioden. Dersom du hadde inntekt i Noreg og i utlandet same året, bruker vi den inntekta som er best for deg." },
                    )
                }
            }
        }
    }

}
