@file:Suppress("LocalVariableName")

package no.nav.pensjon.brev.maler.legacy.vedlegg


import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10Selectors.vedtaksbrev
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.grunnlag.GrunnlagSelectors.persongrunnlagsliste
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.grunnlag.PersongrunnlagSelectors.trygdetidsgrunnlaglistebilateral
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.grunnlag.PersongrunnlagSelectors.trygdetidsgrunnlaglisteeos
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.grunnlag.PersongrunnlagSelectors.trygdetidsgrunnlaglistenor
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.grunnlag.trygdetidsgrunnlagbilateral.TrygdetidsgrunnlagListeBilateralSelectors.trygdetidsgrunnlagbilateral
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.grunnlag.trygdetidsgrunnlageos.TrygdetidsgrunnlagListeEOSSelectors.trygdetidsgrunnlageos
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.grunnlag.trygdetidsgrunnlagnorge.TrygdetidsgrunnlagListeNorSelectors.trygdetidsgrunnlag
import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.vedtaksbrev.VedtaksbrevSelectors.grunnlag
import no.nav.pensjon.brev.maler.legacy.*
import no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere.*
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.text

@TemplateModelHelpers
val vedleggOpplysningerBruktIBeregningUTLegacy =
    createAttachment<LangBokmalNynorsk, PEgruppe10>(
        title = {
            text(
                bokmal { +"Opplysninger om beregningen" },
                nynorsk { +"Opplysningar om utrekninga" },
            )
        },
        includeSakspart = false,
    ) {
        val pe = argument

        title2 {
            text(
                bokmal { + "Opplysninger vi har brukt i beregningen fra " },
                nynorsk { + "Opplysningar vi har brukt i berekninga frå " },
            )
            ifNotNull(pe.vedtaksdata_beregningsdata_beregningufore_beregningvirkningdatofom()) { beregningVirkFom ->
                text(
                    bokmal { + beregningVirkFom.format() },
                    nynorsk { + beregningVirkFom.format() },
                )
            }
        }
        paragraph {
            text(
                bokmal { + " Folketrygdens grunnbeløp (G) benyttet i beregningen er " +
                        pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop().format() + "." },
                nynorsk { + " Folketrygdas grunnbeløp (G) nytta i berekninga er " +
                        pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop().format() + "." },
            )
        }

        includePhrase(TBU010V(pe))
        includePhrase(TBUxx1V(pe))
        includePhrase(TBU011V_TBU016V(pe))
        includePhrase(TBUxx2V(pe))
        includePhrase(TBU080V_TBU027V(pe))

        // TODO TBU028V-TBU020V trengs for brev PE_UT_04_300 og PE_UT_14_300

        showIf(
            pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt")
                    and pe.pebrevkode().isNotAnyOf("PE_UT_04_108", "PE_UT_04_109", "PE_UT_07_200", "PE_UT_06_300")) {
            includePhrase(TBU034V_036V(pe))
        }

        showIf(!pe.ut_uforetidspunkt_foer_17()
                        and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt")
                        and pe.pebrevkode().isNotAnyOf("PE_UT_04_108", "PE_UT_04_109", "PE_UT_07_200", "PE_UT_06_300", "PE_UT_07_100", "PE_UT_05_100", "PE_UT_04_300", "PE_UT_14_300", "PE_UT_04_500")
                        and (pe.pebrevkode().notEqualTo("PE_UT_04_102") or pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("tilst_dod"))) {
            title1 {
                text(
                    bokmal { +"Dette er inntektene vi har brukt i beregningen din" },
                    nynorsk { +"Dette er inntektene vi har brukt i berekninga di" },
                )
            }
            includePhrase(TBU037V_1(pe))
            includePhrase(TBU037V_2(pe))

            includePhrase(TBU038V_1(pe))
            includePhrase(TBU038V_2(pe))

            includePhrase(TBU037V_3(pe))
            includePhrase(TBU037V_4(pe))

            includePhrase(TBU038V_3(pe))
            includePhrase(TBU038V_4(pe))
        }

        showIf(pe.ut_trygdetid()) {
            includePhrase(TBU039V_TBU044V_1(pe))
        }

        //IF( PE_UT_Trygdetid() = true  AND ((((FF_GetArrayElement_Integer(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FaTTNorge) +  FF_GetArrayElement_Integer(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FramtidigTTNorsk)) / 12) < 40 AND FF_GetArrayElement_Boolean(PE_Grunnlag_Persongrunnlagsliste_BrukerFlyktning) = false )   OR (PE_Vedtaksdata_Kravhode_BoddArbeidUtland = true  AND  FF_GetArrayElement_Boolean(PE_Grunnlag_Persongrunnlagsliste_BrukerFlyktning) = false)   AND  FF_GetArrayElement_Date_Boolean(PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeNor_Trygdetidsgrunnlag_TrygdetidFom) = true  )  ) THEN      INCLUDE ENDIF
        // TB1187 2
        showIf(
            pe.ut_trygdetid()
                    and ((pe.ut_sum_fattnorge_framtidigttnorge_div_12().lessThan(40)
                    and not(pe.grunnlag_persongrunnlagsliste_brukerflyktning()))
                    or (pe.vedtaksdata_kravhode_boddarbeidutland()
                    and not(pe.grunnlag_persongrunnlagsliste_brukerflyktning()))
                    and pe.grunnlag_persongrunnlagsliste_trygdetidsgrunnlaglistenor_trygdetidsgrunnlag_trygdetidfom().notNull())
        )
        {
            ifNotNull(pe.safe { vedtaksbrev }.safe { grunnlag }.safe { persongrunnlagsliste }.getOrNull().safe { trygdetidsgrunnlaglistenor }.safe { trygdetidsgrunnlag }) { trygdetidsliste ->
                includePhrase(TrygdetidListeNorTabell(trygdetidsliste))
            }
        }

        showIf(pe.ut_trygdetid() and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_fatteos().greaterThan(0)){
            ifNotNull(pe.safe { vedtaksbrev }.safe { grunnlag }.safe { persongrunnlagsliste }.getOrNull().safe { trygdetidsgrunnlaglisteeos }.safe { trygdetidsgrunnlageos }){
                includePhrase(TBU045V_1)
                includePhrase(TrygdetidsListeEOSTabell(it))
            }
        }

        //[TBU046V_1]
        showIf(
            pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt")
                    and ((pe.pebrevkode().equalTo("PE_UT_04_101") or pe.pebrevkode().equalTo("PE_UT_04_114"))
                    or (pe.pebrevkode().notEqualTo("PE_UT_05_100")
                    and pe.pebrevkode().notEqualTo("PE_UT_07_100")
                    and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_anvendttrygdetid().lessThan(40)))
                    and pe.pebrevkode().notEqualTo("PE_UT_04_108")
                    and pe.pebrevkode().notEqualTo("PE_UT_04_109")
                    and pe.grunnlag_persongrunnlagsliste_trygdetidsgrunnlaglistebilateral_trygdetidsgrunnlagbilateral_trygdetidfombilateral()
                .notNull()){

            ifNotNull(pe.safe { vedtaksbrev }.safe { grunnlag }.safe { persongrunnlagsliste }.getOrNull().safe { trygdetidsgrunnlaglistebilateral }.safe { trygdetidsgrunnlagbilateral }){
                includePhrase(TBU046V_1)
                includePhrase(TrygdetidsListeBilateralTabell(it))
            }
        }

        showIf(pe.ut_trygdetid()
                and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_redusertframtidigtrygdetid()
                and not(pe.grunnlag_persongrunnlagsliste_brukerflyktning())
                and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_beregningsmetode().equalTo("folketrygd")) {
            includePhrase(TBU047V)
        }

        includePhrase(TBU1187(pe))
        includePhrase(TBU1382(pe))
        includePhrase(TBU1384(pe))

        showIf(pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endring_ifu")){
            includePhrase(TBU500v)
        }

        showIf((pe.pebrevkode().notEqualTo("PE_UT_07_100") and pe.pebrevkode().notEqualTo("PE_UT_05_100") and pe.pebrevkode().notEqualTo("PE_UT_04_115") and pe.pebrevkode().notEqualTo("PE_UT_04_103") and pe.pebrevkode().notEqualTo("PE_UT_06_100") and pe.pebrevkode().notEqualTo("PE_UT_04_300") and pe.pebrevkode().notEqualTo("PE_UT_14_300") and pe.pebrevkode().notEqualTo("PE_UT_07_200") and pe.pebrevkode().notEqualTo("PE_UT_06_300") and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse().notEqualTo("") or pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieubegrunnelse().notEqualTo(""))) or pe.pebrevkode().equalTo("PE_UT_04_500") or (pe.vedtaksdata_kravhode_kravarsaktype().equalTo("sivilstandsendring") and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse()) or (pe.pebrevkode().equalTo("PE_UT_04_108") or pe.pebrevkode().equalTo("PE_UT_04_109") and pe.vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_andelytelseavoifu().greaterThan(95.0)) and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("endring_ifu")) {
            includePhrase(TBUxx4v_og_TBU048V_TBU055V(pe))
        }

        // TODO vises kun om brevkode er PE_UT_14_300 or PE_UT_04_300
        //includePhrase(TBU052V_TBU073V_Del_1_InntektenDinFoerDuBleUfoer())

        includePhrase(TBU052V_TBU073V_SlikHarViFastsattKompensasjonsgradenDin(pe))

        includePhrase(TBU052V_TBU073V_SlikBeregnerViUtbetalingAvUforetrygdenNaarInntektenDinEndres(pe))

        includePhrase(TBU052V_TBU073V_SlikBeregnerViReduksjonenAvUfoeretrygden(pe))

        includePhrase(TBU052V_TBU073V_SlikBlirDinUtbetalingFoerSkatt(pe))

        showIf(pe.pe_ut_tbu601v_tbu604v()) {
            includePhrase(TBU052V_TBU073V_SlikRedusererViBarnetilleggetUtFraInntekt(pe))
        }
        includePhrase(TBU052V_TBU073V_ForDegSomHarRettTilBarnetillegg(pe))

        includePhrase(TBU052V_TBU073V_SlikBeregnerViStoerrelsenPaaBarnetilleggetDitt(pe))

        includePhrase(TBU052V_TBU073V_SlikBeregnerViGjenlevendetilleggetDitt(pe))

        includePhrase(TBU052V_TBU073V_ForDegSomMottarEktefelletillegg(pe))

        showIf((pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt") and pe.pebrevkode().notEqualTo("PE_UT_04_108") and pe.pebrevkode().notEqualTo("PE_UT_04_109") and pe.pebrevkode().notEqualTo("PE_UT_04_500") and pe.pebrevkode().notEqualTo("PE_UT_07_200") and (pe.pebrevkode().notEqualTo("PE_UT_04_102") or (pe.pebrevkode().equalTo("PE_UT_04_102") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("tilst_dod")))) or pe.pebrevkode().equalTo("PE_UT_06_300")) {
            includePhrase(TBU052V_TBU073V_EtteroppgjoerAvUforetrygdOgBarnetillegg(pe))
        }
    }

