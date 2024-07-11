package no.nav.pensjon.brev.maler.vedlegg


import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.OpplysningerOmBarnetilleggDtoSelectors.anvendtTrygdetid
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.OpplysningerOmBarnetilleggDtoSelectors.barnetillegg
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.OpplysningerOmBarnetilleggDtoSelectors.fraOgMedDatoErNesteAar
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.OpplysningerOmBarnetilleggDtoSelectors.harKravaarsakEndringInntekt
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.OpplysningerOmBarnetilleggDtoSelectors.harYrkesskade
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.OpplysningerOmMinstetilleggDtoSelectors.inntektFoerUfoereGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.OpplysningerOmMinstetilleggDtoSelectors.inntektsgrenseErUnderTak
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.OpplysningerOmMinstetilleggDtoSelectors.minsteytelseGjeldendeSats
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.OpplysningerOmMinstetilleggDtoSelectors.ufoeretrygdGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.OpplysningerOmMinstetilleggDtoSelectors.ungUfoerGjeldende_erUnder20Aar
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.PE_Grunnlag_Persongrunnlagsliste_BrukerFlyktning
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeBilateral
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeBilateral_TrygdetidsgrunnlagBilateral_TrygdetidFomBilateral
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeEOS
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeNor
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeNor_Trygdetidsgrunnlag_TrygdetidFom
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.PE_UT_NettoAkk_pluss_NettoRestAr
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.PE_UT_OpplyningerOmBergeningen_NettoPerAr
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.PE_UT_Overskytende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.PE_UT_SUM_FaTTNorge_FramtidigTTNorge_DIV_12
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.PE_Vedtaksbrev_Vedtaksdata_Kravhode_BrukerKonvertertUP
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopRedusert
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Kompensasjonsgrad
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Oifu
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_UgradertBruttoPerAr
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_NettoAkk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_NettoRestAr
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.PE_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_AndelYtelseAvOIFU
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.PE_Vedtaksdata_BeregningsData_BeregningUfore_TotalNetto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_AnvendtTrygdetid
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.PE_Vedtaksdata_Kravhode_BoddArbeidUtland
import no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere.legacy.functions.FUNKSJON_FF_GetArrayElement_Date_Boolean
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.PE_Vedtaksdata_Kravhode_KravArsakType
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUBegrunnelse
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUInntekt
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FaTTEOS
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FramtidigTTEOS
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FramtidigTTNorsk
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_RedusertFramtidigTrygdetid
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforegrad
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.PE_pebrevkode
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.TabellufoereOpplysningerDtoSelectors.antallAarOver1G
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.TabellufoereOpplysningerDtoSelectors.antallAarOverInntektIAvtaleland
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.TabellufoereOpplysningerDtoSelectors.barnetilleggGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.TabellufoereOpplysningerDtoSelectors.beregnetUTPerManedGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.TabellufoereOpplysningerDtoSelectors.beregningUfore_BeregningVirkningDatoFom
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.TabellufoereOpplysningerDtoSelectors.beregningUfore_andelYtelseAvOIFU
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.TabellufoereOpplysningerDtoSelectors.beregningUfore_prosentsatsOIFUForTak
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.TabellufoereOpplysningerDtoSelectors.borMedSivilstand
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.TabellufoereOpplysningerDtoSelectors.brukersSivilstand
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.TabellufoereOpplysningerDtoSelectors.erUngUfoer
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.TabellufoereOpplysningerDtoSelectors.harMinsteytelse
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.TabellufoereOpplysningerDtoSelectors.inntektEtterUfoereGjeldendeBeloep
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.TabellufoereOpplysningerDtoSelectors.inntektFoerUfoereGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.TabellufoereOpplysningerDtoSelectors.inntektsAvkortingGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.TabellufoereOpplysningerDtoSelectors.inntektsgrenseErUnderTak
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.TabellufoereOpplysningerDtoSelectors.kravGjelderFoerstegangsbehandlingBosattUtland
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.TabellufoereOpplysningerDtoSelectors.trygdetidsdetaljerGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.TabellufoereOpplysningerDtoSelectors.ufoeretrygdGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.TabellufoereOpplysningerDtoSelectors.ufoeretrygd_reduksjonsgrunnlag_gradertOppjustertIFU
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.TabellufoereOpplysningerDtoSelectors.yrkesskadeGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.beregnetUTPerMaanedGjeldendeGrunnbeloep
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.beregnetUTPerMaanedGjeldendeVirkFom
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.inntektsgrenseAar
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.inntektstak
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.opplysningerOmBarnetillegg
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.opplysningerOmMinstetillegg
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.tabellUfoereOpplysninger
import no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere.*
import no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere.legacy.*
import no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere.legacy.functions.FUNKSJON_PE_UT_TBU056V
import no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere.legacy.functions.FUNKSJON_PE_UT_TBU056V_51
import no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere.legacy.functions.FUNKSJON_PE_UT_Trygdetid
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers

@TemplateModelHelpers
val vedleggOpplysningerBruktIBeregningUTLegacy =
    createAttachment<LangBokmalNynorskEnglish, OpplysningerBruktIBeregningenLegacyDto>(
        title = newText(
            Bokmal to "Opplysninger om beregningen",
            Nynorsk to "Opplysningar om utrekninga",
            English to "Information about calculations"
        ),
        includeSakspart = false,
    ) {
        val inntektsgrenseErUnderTak = inntektsgrenseAar.lessThan(inntektstak)

        val FUNKSJON_PE_UT_Trygdetid = FUNKSJON_PE_UT_Trygdetid(PE_pebrevkode, PE_Vedtaksdata_Kravhode_KravArsakType, PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_AnvendtTrygdetid)

        val FUNKSJON_PE_UT_TBU056V = FUNKSJON_PE_UT_TBU056V(
            PE_pebrevkode = PE_pebrevkode,
            PE_Vedtaksdata_Kravhode_KravArsakType = PE_Vedtaksdata_Kravhode_KravArsakType,
            PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT = PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT,
            PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT = PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT,
            PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense = PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense,
            PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak = PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak,
        )

        val FUNKSJON_PE_UT_TBU056V_51 = FUNKSJON_PE_UT_TBU056V_51(
            PE_pebrevkode = PE_pebrevkode,
            PE_Vedtaksdata_Kravhode_KravArsakType = PE_Vedtaksdata_Kravhode_KravArsakType,
            PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT = PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT,
            PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT = PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT,
            PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense = PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense,
            PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak = PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak,
            PE_Vedtaksbrev_Vedtaksdata_Kravhode_BrukerKonvertertUP = PE_Vedtaksbrev_Vedtaksdata_Kravhode_BrukerKonvertertUP,
        )

        paragraph {
            textExpr(
                Bokmal to "Opplysninger vi har brukt i beregningen fra ".expr()
                        + beregnetUTPerMaanedGjeldendeVirkFom.format() + ". Folketrygdens grunnbeløp (G) benyttet i beregningen er ".expr()
                        + beregnetUTPerMaanedGjeldendeGrunnbeloep.format() + " kroner.",

                Nynorsk to "Opplysningar vi har brukt i utrekninga frå ".expr()
                        + beregnetUTPerMaanedGjeldendeVirkFom.format() + ". Grunnbeløpet i folketrygda (G) nytta i utrekninga er ".expr()
                        + beregnetUTPerMaanedGjeldendeGrunnbeloep.format() + " kroner.",

                English to "Data we have used in the calculations as of ".expr()
                        + beregnetUTPerMaanedGjeldendeVirkFom.format() + ". The National Insurance basic amount (G) used in the calculation is NOK ".expr()
                        + beregnetUTPerMaanedGjeldendeGrunnbeloep.format() + ".",
            )
        }

        //TBU010V
        includePhrase(
            TabellUfoereOpplysningerV2(
                ufoeretrygdGjeldende = tabellUfoereOpplysninger.ufoeretrygdGjeldende,
                yrkesskadeGjeldende = tabellUfoereOpplysninger.yrkesskadeGjeldende,
                inntektFoerUfoereGjeldende = tabellUfoereOpplysninger.inntektFoerUfoereGjeldende,
                inntektsAvkortingGjeldende = tabellUfoereOpplysninger.inntektsAvkortingGjeldende,
                inntektsgrenseErUnderTak = tabellUfoereOpplysninger.inntektsgrenseErUnderTak,
                beregnetUTPerManedGjeldende = tabellUfoereOpplysninger.beregnetUTPerManedGjeldende,
                inntektEtterUfoereGjeldendeBeloep = tabellUfoereOpplysninger.inntektEtterUfoereGjeldendeBeloep,
                erUngUfoer = tabellUfoereOpplysninger.erUngUfoer,
                trygdetidsdetaljerGjeldende = tabellUfoereOpplysninger.trygdetidsdetaljerGjeldende,
                barnetilleggGjeldende = tabellUfoereOpplysninger.barnetilleggGjeldende,
                harMinsteytelse = tabellUfoereOpplysninger.harMinsteytelse,
                borMedSivilstand = tabellUfoereOpplysninger.borMedSivilstand,
                brukersSivilstand = tabellUfoereOpplysninger.brukersSivilstand,
                kravGjelderFoerstegangsbehandlingBosattUtland = tabellUfoereOpplysninger.kravGjelderFoerstegangsbehandlingBosattUtland,
                antallAarOver1G = tabellUfoereOpplysninger.antallAarOver1G,
                antallAarOverInntektIAvtaleland = tabellUfoereOpplysninger.antallAarOverInntektIAvtaleland,
                ufoeretrygd_reduksjonsgrunnlag_gradertOppjustertIFU = tabellUfoereOpplysninger.ufoeretrygd_reduksjonsgrunnlag_gradertOppjustertIFU,
                beregningUfore_andelYtelseAvOIFU = tabellUfoereOpplysninger.beregningUfore_andelYtelseAvOIFU,
                beregningUfore_BeregningVirkningDatoFom = tabellUfoereOpplysninger.beregningUfore_BeregningVirkningDatoFom,
                beregningUfore_prosentsatsOIFUForTak = tabellUfoereOpplysninger.beregningUfore_prosentsatsOIFUForTak,
            )
        )

        // TODO TBUxx1V trengs for brev som ikke er:
        //  PE_UT_07_100, PE_UT_05_100, PE_UT_04_108, PE_UT_04_109, PE_UT_04_500, PE_UT_07_200, PE_UT_06_300

        // TODO TBU011V-TBU016V trengs for brev som ikke er:
        //  PE_UT_07_100 , PE_UT_05_100 , PE_UT_04_300 , PE_UT_14_300 , PE_UT_04_108 , PE_UT_04_109 , PE_UT_07_200 , PE_UT_06_300

        // TODO TBUxx2V trengs for brev som ikke er:
        //  PE_UT_04_300, PE_UT_14_300, PE_UT_05_100, PE_UT_07_100, PE_UT_04_108, PE_UT_04_109, PE_UT_07_100, PE_UT_07_200, PE_UT_06_300

        // TODO TBU080V-TBU027V trengs for brev som ikke er:
        //  PE_UT_07_100, PE_UT_05_100, PE_UT_04_300, PE_UT_14_300, PE_UT_04_103, PE_UT_04_108, PE_UT_04_109, PE_UT_07_200, PE_UT_06_300

        // TODO TBU028V-TBU020V trengs for brev PE_UT_04_300 og PE_UT_14_300



        showIf(
            PE_Vedtaksdata_Kravhode_KravArsakType.notEqualTo("soknad_bt")
                    and PE_pebrevkode.notEqualTo("PE_UT_04_108")
                    and PE_pebrevkode.notEqualTo("PE_UT_04_109")
                    and PE_pebrevkode.notEqualTo("PE_UT_07_200")
                    and PE_pebrevkode.notEqualTo("PE_UT_06_300")
        ) {
            includePhrase(
                TBU034V_036V(
                    PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense = PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense,
                    PE_Vedtaksdata_Kravhode_KravArsakType = PE_Vedtaksdata_Kravhode_KravArsakType,
                    PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad,
                    PE_pebrevkode = PE_pebrevkode,
                )
            )
        }
        // TODO TBU037V_1 trengs for brev som ikke er:
        //  PE_UT_05_100, PE_UT_14_300, PE_UT_07_100, PE_UT_04_300, PE_UT_04_108, PE_UT_04_109, PE_UT_07_200, PE_UT_06_300, PE_UT_04_500

        // TODO TBU037V_2 trengs for brev som ikke er:
        //  PE_UT_05_100, PE_UT_14_300, PE_UT_07_100, PE_UT_04_300, PE_UT_04_108, PE_UT_04_109, PE_UT_07_200, PE_UT_06_300, PE_UT_04_500

        // TODO TBU038V_1 trengs for brev som ikke er:
        //  PE_UT_07_100, PE_UT_05_100, PE_UT_14_300, PE_UT_04_300, PE_UT_04_108, PE_UT_04_109, PE_UT_07_200, PE_UT_06_300, PE_UT_04_500

        // TODO TBU038V_2 trengs for brev som ikke er:
        //  PE_UT_07_100, PE_UT_05_100, PE_UT_14_300, PE_UT_04_300, PE_UT_04_108, PE_UT_04_109, PE_UT_07_200, PE_UT_06_300, PE_UT_04_500

        // TODO TVU037V_3 trengs for brev som ikke er:
        //  PE_UT_05_100, PE_UT_04_300, PE_UT_07_100, PE_UT_14_300, PE_UT_04_500, PE_UT_06_300

        // TODO TVU037V_4 trengs for brev som ikke er:
        //  PE_UT_05_100, PE_UT_04_300, PE_UT_07_100, PE_UT_14_300, PE_UT_04_500, PE_UT_06_300

        // TODO TBU038V_3 trengs for brev som ikke er:
        //  PE_UT_05_100, PE_UT_07_100, PE_UT_14_300, PE_UT_04_300, PE_UT_04_500, PE_UT_06_300
        //
        // TODO TBU038V_4 trengs for brev som ikke er:
        //  PE_UT_05_100, PE_UT_07_100, PE_UT_14_300, PE_UT_04_300, PE_UT_04_500, PE_UT_06_300

        showIf(FUNKSJON_PE_UT_Trygdetid) {
            includePhrase(
                TBU039V_TBU044V_1(
                    PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FramtidigTTNorsk = PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FramtidigTTNorsk,
                    PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FramtidigTTEOS = PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FramtidigTTEOS,
                    PE_UT_SUM_FaTTNorge_FramtidigTTNorge_DIV_12 = PE_UT_SUM_FaTTNorge_FramtidigTTNorge_DIV_12,
                    PE_Grunnlag_Persongrunnlagsliste_BrukerFlyktning = PE_Grunnlag_Persongrunnlagsliste_BrukerFlyktning,
                    PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeNor_Trygdetidsgrunnlag_TrygdetidFom = PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeNor_Trygdetidsgrunnlag_TrygdetidFom,
                    PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad,
                    PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat = PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat,
                    PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad,
                )
            )
        }


        //IF( PE_UT_Trygdetid() = true  AND ((((FF_GetArrayElement_Integer(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FaTTNorge) +  FF_GetArrayElement_Integer(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FramtidigTTNorsk)) / 12) < 40 AND FF_GetArrayElement_Boolean(PE_Grunnlag_Persongrunnlagsliste_BrukerFlyktning) = false )   OR (PE_Vedtaksdata_Kravhode_BoddArbeidUtland = true  AND  FF_GetArrayElement_Boolean(PE_Grunnlag_Persongrunnlagsliste_BrukerFlyktning) = false)   AND  FF_GetArrayElement_Date_Boolean(PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeNor_Trygdetidsgrunnlag_TrygdetidFom) = true  )  ) THEN      INCLUDE ENDIF

        // TB1187 2
        showIf( FUNKSJON_PE_UT_Trygdetid
                and not(PE_Grunnlag_Persongrunnlagsliste_BrukerFlyktning)
                and PE_UT_SUM_FaTTNorge_FramtidigTTNorge_DIV_12.lessThan(40) or
                (PE_Vedtaksdata_Kravhode_BoddArbeidUtland
                        and FUNKSJON_FF_GetArrayElement_Date_Boolean(PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeNor_Trygdetidsgrunnlag_TrygdetidFom)  ))
        {
            includePhrase(TBU1187_2(PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeNor))
        }
        // TBU1187_F 2 er bare whitespace under tabellen

        showIf(FUNKSJON_PE_UT_Trygdetid and PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FaTTEOS.greaterThan(0)){
            includePhrase(TBU045V_1)
            includePhrase(TBU045V_2(PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeEOS))
        }

        //[TBU046V_1]
        showIf(PE_Vedtaksdata_Kravhode_KravArsakType.notEqualTo("soknad_bt")
                and ((PE_pebrevkode.equalTo("PE_UT_04_101") or PE_pebrevkode.equalTo("PE_UT_04_114"))
                or (PE_pebrevkode.notEqualTo("PE_UT_05_100")
                and PE_pebrevkode.notEqualTo("PE_UT_07_100")
                and PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_AnvendtTrygdetid.lessThan(40)))
                and PE_pebrevkode.notEqualTo("PE_UT_04_108")
                and PE_pebrevkode.notEqualTo("PE_UT_04_109")
                and FUNKSJON_FF_GetArrayElement_Date_Boolean(PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeBilateral_TrygdetidsgrunnlagBilateral_TrygdetidFomBilateral)){
            includePhrase(TBU046V_1)
            includePhrase(TBU046V_2(PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeBilateral))
        }

        showIf(FUNKSJON_PE_UT_Trygdetid
                    and PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_RedusertFramtidigTrygdetid
                    and not(PE_Grunnlag_Persongrunnlagsliste_BrukerFlyktning)
        ) {
            includePhrase(TBU047V)
        }

        // TODO TBU1187_H, TBU1187_H, TBU1382, TBU1384_h, TBU1384 trengs ikke for brev:
        //  PE_UT_07_100, PE_UT_05_100, PE_UT_04_108, PE_UT_04_109, PE_UT_04_500, PE_UT_07_200, PE_UT_06_300

        showIf(PE_Vedtaksdata_Kravhode_KravArsakType.equalTo("endring_ifu")){
            includePhrase(TBU500v)
        }

        showIf(
            (
                        PE_pebrevkode.notEqualTo("PE_UT_07_100")
                        and PE_pebrevkode.notEqualTo("PE_UT_05_100")
                        and PE_pebrevkode.notEqualTo("PE_UT_04_115")
                        and PE_pebrevkode.notEqualTo("PE_UT_04_103")
                        and PE_pebrevkode.notEqualTo("PE_UT_06_100")
                        and PE_pebrevkode.notEqualTo("PE_UT_04_300")
                        and PE_pebrevkode.notEqualTo("PE_UT_14_300")
                        and PE_pebrevkode.notEqualTo("PE_UT_07_200")
                        and PE_pebrevkode.notEqualTo("PE_UT_06_300")
                        and (PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse.notEqualTo("")
                            or PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUBegrunnelse.notEqualTo(""))
                    )

                    or PE_pebrevkode.equalTo("PE_UT_04_500")
                    or (PE_Vedtaksdata_Kravhode_KravArsakType.equalTo("sivilstandsendring") and PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse)
                    or (
                        PE_pebrevkode.equalTo("PE_UT_04_108") or PE_pebrevkode.equalTo("PE_UT_04_109")
                            and PE_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_AndelYtelseAvOIFU.greaterThan(95.0)
                    )
                    and PE_Vedtaksdata_Kravhode_KravArsakType.notEqualTo("soknad_bt")
                    and PE_Vedtaksdata_Kravhode_KravArsakType.notEqualTo("endring_ifu")) {

            includePhrase(TBUxx4v_og_TBU048V_TBU055V(
                PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad,
                PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse = PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse,
                PE_pebrevkode = PE_pebrevkode,
            ))
        }

        // TODO vises kun om brevkode er PE_UT_14_300 or PE_UT_04_300
        //includePhrase(TBU052V_TBU073V_Del_1_InntektenDinFoerDuBleUfoer())

        includePhrase(
            TBU052V_TBU073V_Del_2_SlikHarViFastsattKompensasjonsgradenDin(
                FUNKSJON_PE_UT_TBU056V = FUNKSJON_PE_UT_TBU056V,
                FUNKSJON_PE_UT_TBU056V_51 = FUNKSJON_PE_UT_TBU056V_51,
                PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad,
                PE_pebrevkode = PE_pebrevkode,
                PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUInntekt = PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUInntekt,
                PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Oifu = PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Oifu,
                PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_UgradertBruttoPerAr = PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_UgradertBruttoPerAr,
                PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Kompensasjonsgrad = PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Kompensasjonsgrad
            )
        )

        includePhrase(TBU052V_TBU073V_Del_3_SlikBeregnerViUtbetalingAvUforetrygdenNaarInntektenDinEndres(
            PE_Vedtaksdata_Kravhode_KravArsakType = PE_Vedtaksdata_Kravhode_KravArsakType,
            PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT = PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT,
            PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT = PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT,
            PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense = PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense,
            PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak = PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak,
            PE_pebrevkode = PE_pebrevkode,
            PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt = PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt,
            PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Kompensasjonsgrad = PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Kompensasjonsgrad,
            PE_UT_Overskytende = PE_UT_Overskytende,
        ))

        includePhrase(TBU052V_TBU073V_Del_4_SlikBeregnerViReduksjonenAvUfoeretrygden(
            PE_Vedtaksdata_Kravhode_KravArsakType = PE_Vedtaksdata_Kravhode_KravArsakType,
            PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT = PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT,
            PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT = PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT,
            PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt = PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt,
            PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense = PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense,
            PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak = PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak,
            PE_UT_Overskytende = PE_UT_Overskytende,
            PE_pebrevkode = PE_pebrevkode,
            PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Kompensasjonsgrad = PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Kompensasjonsgrad,
            PE_UT_OpplyningerOmBergeningen_NettoPerAr = PE_UT_OpplyningerOmBergeningen_NettoPerAr,
        ))

        includePhrase(TBU052V_TBU073V_Del_5_SlikBlirDinUtbetalingFoerSkatt(
            PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad = PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad,
            PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforegrad = PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforegrad,
            PE_Vedtaksdata_Kravhode_KravArsakType = PE_Vedtaksdata_Kravhode_KravArsakType,
            PE_pebrevkode = PE_pebrevkode,
            PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad,
            PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense = PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense,
            PE_UT_NettoAkk_pluss_NettoRestAr = PE_UT_NettoAkk_pluss_NettoRestAr,
            PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_NettoAkk = PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_NettoAkk,
            PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak = PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak,
            PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT = PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT,
            PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_NettoRestAr = PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_NettoRestAr,
            PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopRedusert = PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopRedusert,
            PE_Vedtaksdata_BeregningsData_BeregningUfore_TotalNetto = PE_Vedtaksdata_BeregningsData_BeregningUfore_TotalNetto,
        ))




        ifNotNull(opplysningerOmMinstetillegg) { opplysningerOmMinstetillegg ->
            val harMinsteytelseSats = opplysningerOmMinstetillegg.minsteytelseGjeldendeSats.ifNull(0.0).greaterThan(0.0)
            showIf(harMinsteytelseSats) {
                includePhrase(
                    OpplysningerOmMinstetillegg(
                        minsteytelseGjeldendeSats = opplysningerOmMinstetillegg.minsteytelseGjeldendeSats,
                        ungUfoerGjeldende_erUnder20Aar = opplysningerOmMinstetillegg.ungUfoerGjeldende_erUnder20Aar,
                        ufoeretrygdGjeldende = opplysningerOmMinstetillegg.ufoeretrygdGjeldende,
                        inntektFoerUfoereGjeldende = opplysningerOmMinstetillegg.inntektFoerUfoereGjeldende,
                        inntektsgrenseErUnderTak = opplysningerOmMinstetillegg.inntektsgrenseErUnderTak,
                    )
                )
            }
        }

        ifNotNull(opplysningerOmBarnetillegg) { opplysningerOmBarnetillegg ->
            includePhrase(
                OpplysningerOmBarnetillegg(
                    barnetillegg = opplysningerOmBarnetillegg.barnetillegg,
                    anvendtTrygdetid = opplysningerOmBarnetillegg.anvendtTrygdetid,
                    harYrkesskade = opplysningerOmBarnetillegg.harYrkesskade,
                    harKravaarsakEndringInntekt = opplysningerOmBarnetillegg.harKravaarsakEndringInntekt,
                    fraOgMedDatoErNesteAar = opplysningerOmBarnetillegg.fraOgMedDatoErNesteAar,
                )
            )
        }
    }

