@file:Suppress("LocalVariableName")

package no.nav.pensjon.brev.maler.legacy.vedlegg


import no.nav.pensjon.brev.api.model.maler.legacy.PE
import no.nav.pensjon.brev.api.model.maler.legacy.PESelectors.ExstreamFunctionsSelectors.pe_ut_sum_fattnorge_framtidigttnorge_div_12
import no.nav.pensjon.brev.api.model.maler.legacy.PESelectors.functions
import no.nav.pensjon.brev.api.model.maler.legacy.PESelectors.vedtaksbrev_safe
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.GrunnlagSelectors.persongrunnlagsliste_safe
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.PersongrunnlagSelectors.trygdetidsgrunnlaglistebilateral_safe
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.PersongrunnlagSelectors.trygdetidsgrunnlaglisteeos_safe
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.trygdetidsgrunnlagbilateral.TrygdetidsgrunnlagListeBilateralSelectors.trygdetidsgrunnlagbilateral_safe
import no.nav.pensjon.brev.api.model.maler.legacy.vedtaksbrev.VedtaksbrevSelectors.grunnlag_safe
import no.nav.pensjon.brev.maler.legacy.*
import no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere.*
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr

@TemplateModelHelpers
val vedleggOpplysningerBruktIBeregningUTLegacy =
    createAttachment<LangBokmalNynorskEnglish, PE>(
        title = newText(
            Bokmal to "Opplysninger om beregningen",
            Nynorsk to "Opplysningar om utrekninga",
            English to "Information about calculations"
        ),
        includeSakspart = false,
    ) {
        val pe = argument

        title2 {
            text(
                Bokmal to "Opplysninger vi har brukt i beregningen fra ",
                Nynorsk to "Opplysningar vi har brukt i berekninga frå ",
                English to "Data we have used in the calculations of ",
            )
            ifNotNull(pe.vedtaksdata_beregningsdata_beregningufore_beregningvirkningdatofom()) { beregningVirkFom ->
                textExpr(
                    Bokmal to beregningVirkFom.format(),
                    Nynorsk to beregningVirkFom.format(),
                    English to beregningVirkFom.format(),
                )
            }
        }
        paragraph {
            textExpr(
                Bokmal to " Folketrygdens grunnbeløp (G) benyttet i beregningen er ".expr() +
                        pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop().format() + " kroner.",
                Nynorsk to " Folketrygdas grunnbeløp (G) nytta i berekninga er ".expr() +
                        pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop().format() + " kroner.",
                English to " The national insurance basic amount (G) used in the calculation is NOK ".expr()
                        + pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop().format() + ".",
            )
        }

        //TBU010V
        includePhrase(TBU010V(pe))
        includePhrase(TBUxx1V(pe))
        includePhrase(TBU011V_TBU016V(pe))
        includePhrase(TBUxx2V(pe))
        includePhrase(TBU080V_TBU027V(pe))

        // TODO TBU028V-TBU020V trengs for brev PE_UT_04_300 og PE_UT_14_300

        showIf(
            pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt")
                    and pe.pebrevkode().notEqualTo("PE_UT_04_108")
                    and pe.pebrevkode().notEqualTo("PE_UT_04_109")
                    and pe.pebrevkode().notEqualTo("PE_UT_07_200")
                    and pe.pebrevkode().notEqualTo("PE_UT_06_300")
        ) {
            includePhrase(
                TBU034V_036V(pe)
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

        showIf(pe.ut_trygdetid()) {
            includePhrase(TBU039V_TBU044V_1(pe))
        }


        //IF( PE_UT_Trygdetid() = true  AND ((((FF_GetArrayElement_Integer(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FaTTNorge) +  FF_GetArrayElement_Integer(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FramtidigTTNorsk)) / 12) < 40 AND FF_GetArrayElement_Boolean(PE_Grunnlag_Persongrunnlagsliste_BrukerFlyktning) = false )   OR (PE_Vedtaksdata_Kravhode_BoddArbeidUtland = true  AND  FF_GetArrayElement_Boolean(PE_Grunnlag_Persongrunnlagsliste_BrukerFlyktning) = false)   AND  FF_GetArrayElement_Date_Boolean(PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeNor_Trygdetidsgrunnlag_TrygdetidFom) = true  )  ) THEN      INCLUDE ENDIF

        // TB1187 2
        showIf(
            pe.ut_trygdetid()
                    and not(pe.grunnlag_persongrunnlagsliste_brukerflyktning())
                    and pe.functions.pe_ut_sum_fattnorge_framtidigttnorge_div_12.lessThan(40) or
                    (pe.vedtaksdata_kravhode_boddarbeidutland()
                            and pe.grunnlag_persongrunnlagsliste_trygdetidsgrunnlaglistenor_trygdetidsgrunnlag_trygdetidfom().notNull()))
        {
            includePhrase(TBU1187_2(pe))
        }
        // TBU1187_F 2 er bare whitespace under tabellen

        showIf(pe.ut_trygdetid() and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_fatteos().greaterThan(0)){
            ifNotNull(pe.vedtaksbrev_safe.grunnlag_safe.persongrunnlagsliste_safe.getOrNull().trygdetidsgrunnlaglisteeos_safe){
                includePhrase(TBU045V_1)
                includePhrase(TBU045V_2(it))
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

            ifNotNull(pe.vedtaksbrev_safe.grunnlag_safe.persongrunnlagsliste_safe.getOrNull().trygdetidsgrunnlaglistebilateral_safe.trygdetidsgrunnlagbilateral_safe){
                includePhrase(TBU046V_1)
                includePhrase(TBU046V_2(it))
            }
        }

        showIf(
            pe.ut_trygdetid()
                    and pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_trygdetid_redusertframtidigtrygdetid()
                    and not(pe.grunnlag_persongrunnlagsliste_brukerflyktning())
        ) {
            includePhrase(TBU047V)
        }

        includePhrase(TB1187(pe))

        // TODO TBU1187_H, TBU1382, TBU1384_h, TBU1384 trengs ikke for brev:
        //  PE_UT_07_100, PE_UT_05_100, PE_UT_04_108, PE_UT_04_109, PE_UT_04_500, PE_UT_07_200, PE_UT_06_300

        showIf(pe.vedtaksdata_kravhode_kravarsaktype().equalTo("endring_ifu")){
            includePhrase(TBU500v)
        }

        showIf(
            (
                    pe.pebrevkode().notEqualTo("PE_UT_07_100")
                            and pe.pebrevkode().notEqualTo("PE_UT_05_100")
                            and pe.pebrevkode().notEqualTo("PE_UT_04_115")
                            and pe.pebrevkode().notEqualTo("PE_UT_04_103")
                            and pe.pebrevkode().notEqualTo("PE_UT_06_100")
                            and pe.pebrevkode().notEqualTo("PE_UT_04_300")
                            and pe.pebrevkode().notEqualTo("PE_UT_14_300")
                            and pe.pebrevkode().notEqualTo("PE_UT_07_200")
                            and pe.pebrevkode().notEqualTo("PE_UT_06_300")
                            and (pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ifubegrunnelse().notEqualTo("")
                            or pe.vedtaksdata_vilkarsvedtaklist_vilkarsvedtak_beregningsvilkar_ieubegrunnelse().notEqualTo(""))
                    )

                    or pe.pebrevkode().equalTo("PE_UT_04_500")
                    or (pe.vedtaksdata_kravhode_kravarsaktype()
                .equalTo("sivilstandsendring") and pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_mottarminsteytelse())
                    or (
                    pe.pebrevkode().equalTo("PE_UT_04_108") or pe.pebrevkode().equalTo("PE_UT_04_109")
                            and pe.vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_andelytelseavoifu().greaterThan(95.0)
                    )
                    and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt")
                    and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("endring_ifu")) {

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
        includePhrase(TBU052V_TBU073V_SlikBeregnerViStoerrelsenPaaBarnetilleggetDitt(pe))

        // TODO TBU052V-TBU073V_del7("slik beregner vi gjenlevendetillegget ditt" til tittel "For deg som mottar ektefelletillegg")
        //  trengs kun for brev som ikke er:
        //  PE_UT_04_300, PE_UT_14_300, PE_UT_05_100, PE_UT_07_100, PE_UT_04_108, PE_UT_04_109, PE_UT_04_500, PE_UT_07_200

        includePhrase(TBU052V_TBU073V_ForDegSomMottarEktefelletillegg(pe))

        showIf(
            (pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("soknad_bt")
                    and pe.pebrevkode().notEqualTo("PE_UT_04_108")
                    and pe.pebrevkode().notEqualTo("PE_UT_04_109")
                    and pe.pebrevkode().notEqualTo("PE_UT_04_500")
                    and pe.pebrevkode().notEqualTo("PE_UT_07_200")
                    and (pe.pebrevkode().notEqualTo("PE_UT_04_102")
                    or (pe.pebrevkode().equalTo("PE_UT_04_102") and pe.vedtaksdata_kravhode_kravarsaktype().notEqualTo("tilst_dod")))
                    ) or pe.pebrevkode().equalTo("PE_UT_06_300")) {
                includePhrase(TBU052V_TBU073V_EtteroppgjoerAvUforetrygdOgBarnetillegg(pe))
            }
    }

