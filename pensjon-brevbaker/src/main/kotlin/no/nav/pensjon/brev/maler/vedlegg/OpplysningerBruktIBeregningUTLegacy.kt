package no.nav.pensjon.brev.maler.vedlegg


import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
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
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.PE_UT_NettoAkk_pluss_NettoRestAr_pluss_ForventetInntekt
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
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Fradrag
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
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

@TemplateModelHelpers
val vedleggOpplysningerBruktIBeregningUTLegacy =
    createAttachment<LangBokmalNynorskEnglish, EmptyBrevdata>(
        title = newText(
            Bokmal to "Opplysninger om beregningen",
            Nynorsk to "Opplysningar om utrekninga",
            English to "Information about calculations"
        ),
        includeSakspart = false,
    ) {

        val FF01_webadresse_grunnbelop: Expression<String> = "<TEXTVARIABEL:869>".expr()
        val PE_BarnetilleggFelles_JusteringsbelopPerArUtenMinus: Expression<Kroner> = Kroner(2032).expr()
        val PE_BarnetilleggSerkull_JusteringsbelopPerArUtenMinus: Expression<Kroner> = Kroner(8689).expr()
        val PE_Grunnlag_PersongrunnlagAvdod_BrukerFlyktning: Expression<Boolean> = true.expr()
        val PE_Grunnlag_PersongrunnlagAvdod_Fodselsnummer: Expression<String> = "<TEXTVARIABEL:6110>".expr()
        val PE_Grunnlag_PersongrunnlagAvdod_TrygdetidsgrunnlagEOS_TrygdetidEOSLand: Expression<String> = "<TEXTVARIABEL:9866>".expr()
        val PE_Grunnlag_PersongrunnlagAvdod_TrygdetidsgrunnlagEOS_TrygdetidFomEOS: Expression<LocalDate> = LocalDate.of(2020,2, 14).expr()
        val PE_Grunnlag_PersongrunnlagAvdod_TrygdetidsgrunnlagEOS_TrygdetidTomEOS: Expression<LocalDate> = LocalDate.of(2020,10, 27).expr()
        val PE_Grunnlag_PersongrunnlagAvdod_TrygdetidsgrunnlagListeBilateral_TrygdetidsgrunnlagBilateral_TrygdetidBilateralLand: Expression<String> = "<TEXTVARIABEL:3026>".expr()
        val PE_Grunnlag_PersongrunnlagAvdod_TrygdetidsgrunnlagListeBilateral_TrygdetidsgrunnlagBilateral_TrygdetidFomBilateral: Expression<LocalDate> = LocalDate.of(2020,3, 8).expr()
        val PE_Grunnlag_PersongrunnlagAvdod_TrygdetidsgrunnlagListeBilateral_TrygdetidsgrunnlagBilateral_TrygdetidTomBilateral: Expression<LocalDate> = LocalDate.of(2020,3, 21).expr()
        val PE_Grunnlag_PersongrunnlagAvdod_TrygdetidsgrunnlagListeNor_Trygdetidsgrunnlag_TrygdetidFom: Expression<LocalDate> = LocalDate.of(2020,5, 11).expr()
        val PE_Grunnlag_PersongrunnlagAvdod_TrygdetidsgrunnlagListeNor_Trygdetidsgrunnlag_TrygdetidTom: Expression<LocalDate> = LocalDate.of(2020,1, 18).expr()
        val PE_Grunnlag_Persongrunnlagsliste_BrukerFlyktning: Expression<Boolean> = true.expr()
        val PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland: Expression<String> = "<TEXTVARIABEL:1578>".expr()
        val PE_Grunnlag_Persongrunnlagsliste_Trygdeavtaler_Avtaleland: Expression<String> = "<TEXTVARIABEL:2324>".expr()
        val PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeBilateral_TrygdetidsgrunnlagBilateral_TrygdetidBilateralLand: Expression<String> = "<TEXTVARIABEL:2134>".expr()
        val PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeBilateral_TrygdetidsgrunnlagBilateral_TrygdetidFomBilateral: Expression<LocalDate> = LocalDate.of(2020,11, 2).expr()
        val PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeBilateral_TrygdetidsgrunnlagBilateral_TrygdetidTomBilateral: Expression<LocalDate> = LocalDate.of(2020,11, 4).expr()
        val PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeEOS_TrygdetidsgrunnlagEOS_TrygdetidEOSLand: Expression<String> = "<TEXTVARIABEL:8961>".expr()
        val PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeEOS_TrygdetidsgrunnlagEOS_TrygdetidFomEOS: Expression<LocalDate> = LocalDate.of(2020,8, 14).expr()
        val PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeEOS_TrygdetidsgrunnlagEOS_TrygdetidTomEOS: Expression<LocalDate> = LocalDate.of(2020,4, 24).expr()
        val PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeNor_Trygdetidsgrunnlag_TrygdetidFom: Expression<LocalDate> = LocalDate.of(2020,9, 12).expr()
        val PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeNor_Trygdetidsgrunnlag_TrygdetidTom: Expression<LocalDate> = LocalDate.of(2020,7, 23).expr()
        val PE_SivilstandAnvendt_bormed_1Bindestrek5: Expression<String> = "<TEXTVARIABEL:659>".expr()
        val PE_SivilstandAnvendt_bormed_3_2: Expression<String> = "<TEXTVARIABEL:7837>".expr()
        val PE_SivilstandAnvendt_bormed_ektefelle: Expression<String> = "<TEXTVARIABEL:1496>".expr()
        val PE_SivilstandAnvendt_bormed_registrert_partner: Expression<String> = "<TEXTVARIABEL:6413>".expr()
        val PE_SivilstandAnvendt_gift_men_lever_adskilt: Expression<String> = "<TEXTVARIABEL:545>".expr()
        val PE_SivilstandAnvendt_registrert_partner_men_lever_adskilt: Expression<String> = "<TEXTVARIABEL:3410>".expr()
        val PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT: Expression<String> = "<TEXTVARIABEL:78>".expr()
        val PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_EN: Expression<String> = "<TEXTVARIABEL:7477>".expr()
        val PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_EN_cohabiting_partner: Expression<String> = "<TEXTVARIABEL:847>".expr()
        val PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_NN_entall: Expression<String> = "<TEXTVARIABEL:2617>".expr()
        val PE_UT_AntallBarnSerkullogFelles: Expression<Int> = 4601.expr()
        val PE_UT_BTFBInntektBruktiAvkortningMinusBTFBfribelop: Expression<Double> = 1056.0.expr()
        val PE_UT_BTSBinntektBruktiAvkortningMinusBTSBfribelop: Expression<Double> = 1211.0.expr()
        val PE_UT_Barnet_Barna_Felles: Expression<String> = "<TEXTVARIABEL:1346>".expr()
        val PE_UT_Barnet_Barna_Innvilget: Expression<String> = "<TEXTVARIABEL:3498>".expr()
        val PE_UT_Barnet_Barna_Serkull: Expression<String> = "<TEXTVARIABEL:538>".expr()
        val PE_UT_Inntektsgrense_faktisk: Expression<Double> = 5071.0.expr()
        val PE_UT_Kompensasjonsgrad_EN: Expression<Double> = 5639.0.expr()
        val PE_UT_Minsteytelse_Sats_EN: Expression<Double> = 9021.0.expr()
        val PE_UT_NettoAkk_pluss_NettoRestAr: Expression<Kroner> = Kroner(8620).expr()
        val PE_UT_NettoAkk_pluss_NettoRestAr_pluss_ForventetInntekt: Expression<Double> = 9874.0.expr()
        val PE_UT_OpplyningerOmBergeningen_NettoPerAr: Expression<Double> = 8210.0.expr()
        val PE_UT_Overskytende: Expression<Double> = 8144.0.expr()
        val PE_UT_SUM_FaTTNorge_FramtidigTTNorge_DIV_12: Expression<Double> = 6636.0.expr()
        val PE_UT_SUM_FaTTNorge_FramtidigTTNorge_DIV_12_avdod: Expression<Double> = 6132.0.expr()
        val PE_UT_VilkarGjelderPersonAlder: Expression<Int> = 908.expr()
        val PE_UT_VilkarGjelderPerson_fodselsdato: Expression<LocalDate> = LocalDate.of(2020,3, 24).expr()
        val PE_UT_fattNorgePlusfattEOS_Avdød: Expression<Int> = 1450.expr()
        val PE_UT_fattnorgePlusfatta10netto_Avdød: Expression<Int> = 3985.expr()
        val PE_UT_fattnorgePlusfattbilateral_Avdød: Expression<Int> = 9775.expr()
        val PE_UT_sum_FaTTNorge_FaTTBilateral: Expression<Int> = 7288.expr()
        val PE_UT_sum_FaTTNorge_FaTTEOS: Expression<Int> = 6657.expr()
        val PE_UT_sum_FaTTNorge_FaTT_A10_netto: Expression<Int> = 8657.expr()
        val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_AvkortingsbelopPerAr: Expression<Kroner> = Kroner(3483).expr()
        val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert: Expression<Boolean> = true.expr()
        val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert: Expression<Boolean> = true.expr()
        val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_Inntektstak: Expression<Double> = 7204.0.expr()
        val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr: Expression<Kroner> = Kroner(3823).expr()
        val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr_UtenMinus: Expression<Double> = 1744.0.expr()
        val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBbelopFratrukketAnnenForeldersInntekt: Expression<Kroner> = Kroner(6077).expr()
        val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBinntektAnnenForelder: Expression<Double> = 3595.0.expr()
        val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_AvkortingsbelopPerAr: Expression<Double> = 4648.0.expr()
        val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert: Expression<Boolean> = true.expr()
        val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert: Expression<Boolean> = true.expr()
        val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_Inntektstak: Expression<Double> = 7929.0.expr()
        val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr: Expression<Kroner> = Kroner(3554).expr()
        val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr_UtenMinus: Expression<Double> = 7396.0.expr()
        val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType: Expression<String> = "<TEXTVARIABEL:3511>".expr()
        val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_GradertOppjustertIFU: Expression<Double> = 3340.0.expr()
        val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_ProsentsatsOIFUForTak: Expression<Double> = 5426.0.expr()
        val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoEtterReduksjonBT: Expression<Double> = 9687.0.expr()
        val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoForReduksjonBT: Expression<Kroner> = Kroner(3078).expr()
        val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_TotaltAntallBarn: Expression<Int> = 82.expr()
        val PE_Vedtaksbrev_Vedtaksdata_Kravhode_BrukerKonvertertUP: Expression<Boolean> = true.expr()
        val PE_Vedtaksbrev_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_FortsattMedlemskap_InngangUnntak: Expression<String> = "<TEXTVARIABEL:2107>".expr()
        val PE_Vedtaksbrev_Vedtaksdata_VilkarsVedtak_Vilkar_MedlemskapForUTEtterTrygdeavtaler_OppfyltVedSammenlegging: Expression<Boolean> = true.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopRedusert: Expression<Boolean> = true.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT: Expression<Kroner> = Kroner(2801).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT: Expression<Kroner> = Kroner(474).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningVirkningDatoFom: Expression<LocalDate> = LocalDate.of(2020,2, 1).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBBrukersInntektTilAvkortning: Expression<Kroner> = Kroner(6559).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBbruttoPerAr: Expression<Double> = 1493.0.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBnettoPerAr: Expression<Double> = 6163.0.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBbruttoPerAr: Expression<Double> = 9499.0.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBnettoPerAr: Expression<Double> = 2946.0.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget: Expression<Boolean> = true.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GjenlevendetilleggInformasjon_AnvendtTrygdetid: Expression<Int> = 8418.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GjenlevendetilleggInformasjon_BeregningsgrunnlagAvdodOrdiner_Arsbelop: Expression<Double> = 1206.0.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GjenlevendetilleggInformasjon_BeregningsgrunnlagAvdodOrdiner_OpptjeningUTListe_OpptjeningUT_Ar: Expression<Double> = 9723.0.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GjenlevendetilleggInformasjon_BeregningsgrunnlagAvdodOrdiner_OpptjeningUTListe_OpptjeningUT_AvkortetBelop: Expression<Double> = 5831.0.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GjenlevendetilleggInformasjon_BeregningsgrunnlagAvdodOrdiner_OpptjeningUTListe_OpptjeningUT_Brukt: Expression<Boolean> = true.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GjenlevendetilleggInformasjon_BeregningsgrunnlagAvdodOrdiner_OpptjeningUTListe_OpptjeningUT_Forstegansgstjeneste: Expression<Boolean> = true.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GjenlevendetilleggInformasjon_BeregningsgrunnlagAvdodOrdiner_OpptjeningUTListe_OpptjeningUT_InntektIAvtaleLand: Expression<Boolean> = true.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GjenlevendetilleggInformasjon_BeregningsgrunnlagAvdodOrdiner_OpptjeningUTListe_OpptjeningUT_JustertBelop: Expression<Double> = 392.0.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GjenlevendetilleggInformasjon_BeregningsgrunnlagAvdodOrdiner_OpptjeningUTListe_OpptjeningUT_Omsorgsaar: Expression<Boolean> = true.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GjenlevendetilleggInformasjon_BeregningsgrunnlagAvdodOrdiner_OpptjeningUTListe_OpptjeningUT_Pgi: Expression<Double> = 1835.0.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GjenlevendetilleggInformasjon_BeregningsgrunnlagAvdodYrkesskadeArsbelop: Expression<Double> = 3158.0.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GjenlevendetilleggInformasjon_InntektVedSkadetidspunktet: Expression<Double> = 4470.0.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GjenlevendetilleggInformasjon_MinsteYtelseBenyttetUngUfor: Expression<Boolean> = true.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GjenlevendetilleggInformasjon_Uforetidspunkt: Expression<LocalDate> = LocalDate.of(2020,3, 26).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GjenlevendetilleggInformasjon_Yrkesskadegrad: Expression<Double> = 2598.0.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_NyttGjenlevendetillegg: Expression<Kroner> = Kroner(9483).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense: Expression<Kroner> = Kroner(9744).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt: Expression<Double> = 1677.0.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense: Expression<Double> = 2033.0.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak: Expression<Double> = 7630.0.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Kompensasjonsgrad: Expression<Double> = 3084.0.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Oifu: Expression<Kroner> = Kroner(3295).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_UgradertBruttoPerAr: Expression<Double> = 6416.0.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad: Expression<Int> = 743.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Fradrag: Expression<Double> = 7687.0.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Minsteytelse_OppfyltUngUfor: Expression<Boolean> = true.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Minsteytelse_Sats: Expression<Double> = 4656.0.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_NettoAkk: Expression<Double> = 9955.0.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_NettoRestAr: Expression<Double> = 1908.0.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_AntallArInntektIAvtaleland: Expression<Int> = 9404.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_AntallArOver1G: Expression<Int> = 6034.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_BeregningsgrunnlagOrdinerArsbelop: Expression<Double> = 1008.0.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Ar: Expression<Int> = 4361.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_AvkortetBelop: Expression<Double> = 4908.0.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Brukt: Expression<Boolean> = true.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Forstegansgstjeneste: Expression<Boolean> = true.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_InntektIAvtaleLand: Expression<Boolean> = true.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_JustertBelop: Expression<Double> = 7346.0.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Omsorgsaar: Expression<Boolean> = true.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Pgi: Expression<Double> = 9157.0.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagYrkesskadeArsbelop: Expression<Double> = 4485.0.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_InntektVedSkadetidspunktet: Expression<Double> = 8819.0.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_AndelYtelseAvOIFU: Expression<Double> = 4905.0.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_TotalNetto: Expression<Double> = 4096.0.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_AnvendtTrygdetid: Expression<Int> = 6441.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode: Expression<String> = "<TEXTVARIABEL:4259>".expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Grunnbelop: Expression<Double> = 1446.0.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse: Expression<Boolean> = true.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_ProratabrokNevner: Expression<Int> = 5346.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_ProratabrokTeller: Expression<Int> = 4778.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad: Expression<Int> = 7029.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforetidspunkt: Expression<LocalDate> = LocalDate.of(2020,7, 24).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad: Expression<Int> = 1729.expr()
        val PE_Vedtaksdata_BeregningsData_Beregning_BeregningBrukerSivilstand: Expression<String> = "<TEXTVARIABEL:430>".expr()
        val PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt: Expression<String> = "<TEXTVARIABEL:9308>".expr()
        val PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_AntallBarnFelles: Expression<Int> = 8217.expr()
        val PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBInntektBruktiAvkortning: Expression<Kroner> = Kroner(4606).expr()
        val PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBfribelop: Expression<Kroner> = Kroner(8151).expr()
        val PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget: Expression<Boolean> = true.expr()
        val PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto: Expression<Kroner> = Kroner(2468).expr()
        val PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_AntallBarnSerkull: Expression<Int> = 7592.expr()
        val PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning: Expression<Double> = 7056.0.expr()
        val PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBfribelop: Expression<Kroner> = Kroner(8049).expr()
        val PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget: Expression<Boolean> = true.expr()
        val PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto: Expression<Kroner> = Kroner(9374).expr()
        val PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget: Expression<Boolean> = true.expr()
        val PE_Vedtaksdata_Kravhode_BoddArbeidUtlandAvdod: Expression<Boolean> = true.expr()
        val PE_Vedtaksdata_Kravhode_KravArsakType: Expression<String> = "<TEXTVARIABEL:1950>".expr()
        val PE_Vedtaksdata_Kravhode_KravGjelder: Expression<String> = "<TEXTVARIABEL:8767>".expr()
        val PE_Vedtaksdata_Kravhode_VurdereTrygdeavtale: Expression<Boolean> = true.expr()
        val PE_Vedtaksdata_TrygdetidAvdod_FaTTEOS: Expression<Double> = 7278.0.expr()
        val PE_Vedtaksdata_TrygdetidAvdod_FaTTNorge: Expression<Double> = 9917.0.expr()
        val PE_Vedtaksdata_TrygdetidAvdod_FaTT_A10_netto: Expression<Double> = 1818.0.expr()
        val PE_Vedtaksdata_TrygdetidAvdod_FramtidigTTEOS: Expression<Double> = 1982.0.expr()
        val PE_Vedtaksdata_TrygdetidAvdod_FramtidigTTNorsk: Expression<Double> = 6566.0.expr()
        val PE_Vedtaksdata_TrygdetidAvdod_TTNevnerEOS: Expression<Double> = 8917.0.expr()
        val PE_Vedtaksdata_TrygdetidAvdod_TTNevnerNordisk: Expression<Double> = 4984.0.expr()
        val PE_Vedtaksdata_TrygdetidAvdod_TTNordisk: Expression<Double> = 2587.0.expr()
        val PE_Vedtaksdata_TrygdetidAvdod_TTTellerEOS: Expression<Double> = 2180.0.expr()
        val PE_Vedtaksdata_TrygdetidAvdod_TTTellerNordisk: Expression<Double> = 4892.0.expr()
        val PE_Vedtaksdata_TrygdetidAvdod_TTUtlandTrygdeAvtale_FaTTBilateral: Expression<Double> = 6504.0.expr()
        val PE_Vedtaksdata_TrygdetidAvdod_TTUtlandTrygdeAvtale_FramtidigTTAvtaleland: Expression<Double> = 1683.0.expr()
        val PE_Vedtaksdata_TrygdetidAvdod_TTUtlandTrygdeAvtale_TTNevnerBilateral: Expression<Double> = 3003.0.expr()
        val PE_Vedtaksdata_TrygdetidAvdod_TTUtlandTrygdeAvtale_TTTellerBilateral: Expression<Double> = 7270.0.expr()
        val PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUBegrunnelse: Expression<String> = "<TEXTVARIABEL:5759>".expr()
        val PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUInntekt: Expression<Kroner> = Kroner(6136).expr()
        val PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse: Expression<String> = "<TEXTVARIABEL:8264>".expr()
        val PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUInntekt: Expression<Kroner> = Kroner(3562).expr()
        val PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Skadetidspunkt: Expression<LocalDate> = LocalDate.of(2020,10, 23).expr()
        val PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_TTUtlandTrygdeAvtaleListe_TTUtlandTrygdeAvtale_FaTTBilateral: Expression<Int> = 8356.expr()
        val PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FaTTEOS: Expression<Int> = 2191.expr()
        val PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FaTTNorge: Expression<Int> = 5145.expr()
        val PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FramtidigTTEOS: Expression<Int> = 898.expr()
        val PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FramtidigTTNorsk: Expression<Int> = 3932.expr()
        val PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_RedusertFramtidigTrygdetid: Expression<Boolean> = true.expr()
        val PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_TTNevnerEOS: Expression<Int> = 7820.expr()
        val PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_TTNevnerNordisk: Expression<Int> = 5452.expr()
        val PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_TTNordisk: Expression<Int> = 6532.expr()
        val PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_TTTellerEOS: Expression<Int> = 8744.expr()
        val PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_TTTellerNordisk: Expression<Int> = 3064.expr()
        val PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforegrad: Expression<Double> = 6926.0.expr()
        val PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_UngUforResultat: Expression<String> = "<TEXTVARIABEL:1230>".expr()
        val PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat: Expression<String> = "<TEXTVARIABEL:821>".expr()
        val PE_pebrevkode: Expression<String> = "<TEXTVARIABEL:9253>".expr()

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
            PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Fradrag = PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Fradrag,
            PE_UT_NettoAkk_pluss_NettoRestAr_pluss_ForventetInntekt = PE_UT_NettoAkk_pluss_NettoRestAr_pluss_ForventetInntekt,
            PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt = PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt,
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

