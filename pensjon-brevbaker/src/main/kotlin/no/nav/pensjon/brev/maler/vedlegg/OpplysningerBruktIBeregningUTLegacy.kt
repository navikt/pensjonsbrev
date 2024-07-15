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
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeBilateral
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeEOS
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenLegacyDtoSelectors.PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeNor
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
    createAttachment<LangBokmalNynorskEnglish, OpplysningerBruktIBeregningenLegacyDto>(
        title = newText(
            Bokmal to "Opplysninger om beregningen",
            Nynorsk to "Opplysningar om utrekninga",
            English to "Information about calculations"
        ),
        includeSakspart = false,
    ) {

        val PE_Grunnlag_Persongrunnlagsliste_BrukerFlyktning: Expression<Boolean> = true.expr()
        val PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland: Expression<String> = "<TEXTVARIABEL:4932>".expr()
        val PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeBilateral_TrygdetidsgrunnlagBilateral_TrygdetidFomBilateral: Expression<LocalDate> = LocalDate.of(2020,4, 11).expr()
        val PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeNor_Trygdetidsgrunnlag_TrygdetidFom: Expression<LocalDate> = LocalDate.of(2020,11, 11).expr()
        val PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT: Expression<String> = "<TEXTVARIABEL:1287>".expr()
        val PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_EN_cohabiting_partner: Expression<String> = "<TEXTVARIABEL:6599>".expr()
        val PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_NN_entall: Expression<String> = "<TEXTVARIABEL:8818>".expr()
        val PE_UT_NettoAkk_pluss_NettoRestAr: Expression<Kroner> = Kroner(496).expr()
        val PE_UT_NettoAkk_pluss_NettoRestAr_pluss_ForventetInntekt: Expression<Kroner> = Kroner(3391).expr()
        val PE_UT_OpplyningerOmBergeningen_NettoPerAr: Expression<Kroner> = Kroner(2683).expr()
        val PE_UT_Overskytende: Expression<Kroner> = Kroner(9111).expr()
        val PE_UT_SUM_FaTTNorge_FramtidigTTNorge_DIV_12: Expression<Int> = 6761.expr()
        val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr: Expression<Kroner> = Kroner(4846).expr()
        val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr: Expression<Kroner> = Kroner(2598).expr()
        val PE_Vedtaksbrev_Vedtaksdata_Kravhode_BrukerKonvertertUP: Expression<Boolean> = true.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopRedusert: Expression<Boolean> = true.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT: Expression<Kroner> = Kroner(6206).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT: Expression<Kroner> = Kroner(4537).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense: Expression<Kroner> = Kroner(8977).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt: Expression<Kroner> = Kroner(4946).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense: Expression<Kroner> = Kroner(1189).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak: Expression<Kroner> = Kroner(2427).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Kompensasjonsgrad: Expression<Int> = 4500.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Oifu: Expression<Kroner> = Kroner(2264).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_UgradertBruttoPerAr: Expression<Kroner> = Kroner(1975).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad: Expression<Int> = 9582.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Fradrag: Expression<Kroner> = Kroner(9978).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Minsteytelse_Sats: Expression<Double> = 99.0.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_NettoAkk: Expression<Kroner> = Kroner(3814).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_AndelYtelseAvOIFU: Expression<Double> = 29.0.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_TotalNetto: Expression<Kroner> = Kroner(9765).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_AnvendtTrygdetid: Expression<Int> = 3196.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Mottarminsteytelse: Expression<Boolean> = true.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad: Expression<Int> = 7514.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Yrkesskadegrad: Expression<Int> = 4987.expr()
        val PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt: Expression<String> = "<TEXTVARIABEL:8197>".expr()
        val PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget: Expression<Boolean> = true.expr()
        val PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto: Expression<Kroner> = Kroner(9126).expr()
        val PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget: Expression<Boolean> = true.expr()
        val PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto: Expression<Kroner> = Kroner(755).expr()
        val PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget: Expression<Boolean> = true.expr()
        val PE_Vedtaksdata_Kravhode_BoddArbeidUtland: Expression<Boolean> = true.expr()
        val PE_Vedtaksdata_Kravhode_KravArsakType: Expression<String> = "<TEXTVARIABEL:637>".expr()
        val PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUBegrunnelse: Expression<String> = "<TEXTVARIABEL:235>".expr()
        val PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUBegrunnelse: Expression<String> = "<TEXTVARIABEL:633>".expr()
        val PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IFUInntekt: Expression<Kroner> = Kroner(1472).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB: Expression<Kroner> = Kroner(14720).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB: Expression<Kroner> = Kroner(14721).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB: Expression<Kroner> = Kroner(14722).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB: Expression<Kroner> = Kroner(14723).expr()
        val PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FaTTEOS: Expression<Int> = 8112.expr()
        val PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FramtidigTTEOS: Expression<Int> = 8560.expr()
        val PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FramtidigTTNorsk: Expression<Int> = 5410.expr()
        val PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_RedusertFramtidigTrygdetid: Expression<Boolean> = true.expr()
        val PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforegrad: Expression<Int> = 3.expr()
        val PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_YrkesskadeResultat: Expression<String> = "<TEXTVARIABEL:7131>".expr()
        val PE_pebrevkode: Expression<String> = "<TEXTVARIABEL:3348>".expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_NettoRestAr: Expression<Kroner> = Kroner(4980).expr()


        // UNUSED VARIABLES (used in phrases that has not been written yet):
        /*
        val PE_BarnetilleggFelles_JusteringsbelopPerArUtenMinus: Expression<Kroner> = Kroner(8861).expr()
        val PE_BarnetilleggSerkull_JusteringsbelopPerArUtenMinus: Expression<Kroner> = Kroner(1296).expr()
        val PE_Grunnlag_PersongrunnlagAvdod_BrukerFlyktning: Expression<Boolean> = true.expr()
        val PE_Grunnlag_PersongrunnlagAvdod_Fodselsnummer: Expression<String> = "<TEXTVARIABEL:7782>".expr()
        val PE_Grunnlag_PersongrunnlagAvdod_TrygdetidsgrunnlagEOS_TrygdetidEOSLand: Expression<String> = "<TEXTVARIABEL:2565>".expr()
        val PE_Grunnlag_PersongrunnlagAvdod_TrygdetidsgrunnlagEOS_TrygdetidFomEOS: Expression<LocalDate> = LocalDate.of(2020,10, 3).expr()
        val PE_Grunnlag_PersongrunnlagAvdod_TrygdetidsgrunnlagEOS_TrygdetidTomEOS: Expression<LocalDate> = LocalDate.of(2020,3, 18).expr()
        val PE_Grunnlag_PersongrunnlagAvdod_TrygdetidsgrunnlagListeBilateral_TrygdetidsgrunnlagBilateral_TrygdetidBilateralLand: Expression<String> = "<TEXTVARIABEL:1402>".expr()
        val PE_Grunnlag_PersongrunnlagAvdod_TrygdetidsgrunnlagListeBilateral_TrygdetidsgrunnlagBilateral_TrygdetidFomBilateral: Expression<LocalDate> = LocalDate.of(2020,5, 6).expr()
        val PE_Grunnlag_PersongrunnlagAvdod_TrygdetidsgrunnlagListeBilateral_TrygdetidsgrunnlagBilateral_TrygdetidTomBilateral: Expression<LocalDate> = LocalDate.of(2020,9, 3).expr()
        val PE_Grunnlag_PersongrunnlagAvdod_TrygdetidsgrunnlagListeNor_Trygdetidsgrunnlag_TrygdetidFom: Expression<LocalDate> = LocalDate.of(2020,10, 22).expr()
        val PE_Grunnlag_PersongrunnlagAvdod_TrygdetidsgrunnlagListeNor_Trygdetidsgrunnlag_TrygdetidTom: Expression<LocalDate> = LocalDate.of(2020,5, 20).expr()
        val PE_Grunnlag_Persongrunnlagsliste_Trygdeavtaler_Avtaleland: Expression<String> = "<TEXTVARIABEL:4860>".expr()
        val PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeBilateral_TrygdetidsgrunnlagBilateral_TrygdetidBilateralLand: Expression<String> = "<TEXTVARIABEL:8973>".expr()
        val PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeBilateral_TrygdetidsgrunnlagBilateral_TrygdetidTomBilateral: Expression<LocalDate> = LocalDate.of(2020,1, 17).expr()
        val PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeEOS_TrygdetidsgrunnlagEOS_TrygdetidEOSLand: Expression<String> = "<TEXTVARIABEL:2365>".expr()
        val PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeEOS_TrygdetidsgrunnlagEOS_TrygdetidFomEOS: Expression<LocalDate> = LocalDate.of(2020,10, 24).expr()
        val PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeEOS_TrygdetidsgrunnlagEOS_TrygdetidTomEOS: Expression<LocalDate> = LocalDate.of(2020,7, 16).expr()
        val PE_Grunnlag_Persongrunnlagsliste_TrygdetidsgrunnlagListeNor_Trygdetidsgrunnlag_TrygdetidTom: Expression<LocalDate> = LocalDate.of(2020,3, 3).expr()
        val PE_SivilstandAnvendt_bormed_1Bindestrek5: Expression<String> = "<TEXTVARIABEL:6190>".expr()
        val PE_SivilstandAnvendt_bormed_3_2: Expression<String> = "<TEXTVARIABEL:4408>".expr()
        val PE_SivilstandAnvendt_bormed_ektefelle: Expression<String> = "<TEXTVARIABEL:4816>".expr()
        val PE_SivilstandAnvendt_bormed_registrert_partner: Expression<String> = "<TEXTVARIABEL:1152>".expr()
        val PE_SivilstandAnvendt_gift_men_lever_adskilt: Expression<String> = "<TEXTVARIABEL:5800>".expr()
        val PE_SivilstandAnvendt_registrert_partner_men_lever_adskilt: Expression<String> = "<TEXTVARIABEL:825>".expr()
        val PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_EN: Expression<String> = "<TEXTVARIABEL:8837>".expr()
        val PE_UT_AntallBarnSerkullogFelles: Expression<Int> = 5196.expr()
        val PE_UT_BTFBInntektBruktiAvkortningMinusBTFBfribelop: Expression<Kroner> = Kroner(6573).expr()
        val PE_UT_BTSBinntektBruktiAvkortningMinusBTSBfribelop: Expression<Kroner> = Kroner(5374).expr()
        val PE_UT_Barnet_Barna_Felles: Expression<String> = "<TEXTVARIABEL:6641>".expr()
        val PE_UT_Barnet_Barna_Innvilget: Expression<String> = "<TEXTVARIABEL:8578>".expr()
        val PE_UT_Barnet_Barna_Serkull: Expression<String> = "<TEXTVARIABEL:5628>".expr()
        val PE_UT_Inntektsgrense_faktisk: Expression<Kroner> = Kroner(4322).expr()
        val PE_UT_Kompensasjonsgrad_EN: Expression<Double> = 65.0.expr()
        val PE_UT_Minsteytelse_Sats_EN: Expression<Double> = 75.0.expr()
        val PE_UT_SUM_FaTTNorge_FramtidigTTNorge_DIV_12_avdod: Expression<Int> = 8172.expr()
        val PE_UT_VilkarGjelderPersonAlder: Expression<Int> = 7976.expr()
        val PE_UT_VilkarGjelderPerson_fodselsdato: Expression<LocalDate> = LocalDate.of(2020,8, 14).expr()
        val PE_UT_fattNorgePlusfattEOS_Avdød: Expression<Int> = 3040.expr()
        val PE_UT_fattnorgePlusfatta10netto_Avdød: Expression<Int> = 8422.expr()
        val PE_UT_fattnorgePlusfattbilateral_Avdød: Expression<Int> = 8269.expr()
        val PE_UT_sum_FaTTNorge_FaTTBilateral: Expression<Int> = 9169.expr()
        val PE_UT_sum_FaTTNorge_FaTTEOS: Expression<Int> = 5162.expr()
        val PE_UT_sum_FaTTNorge_FaTT_A10_netto: Expression<Int> = 5471.expr()
        val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_AvkortingsbelopPerAr: Expression<Kroner> = Kroner(5738).expr()
        val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert: Expression<Boolean> = true.expr()
        val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert: Expression<Boolean> = true.expr()
        val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_Inntektstak: Expression<Kroner> = Kroner(2779).expr()
        val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr_UtenMinus: Expression<Kroner> = Kroner(3649).expr()
        val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBbelopFratrukketAnnenForeldersInntekt: Expression<Kroner> = Kroner(4867).expr()
        val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBinntektAnnenForelder: Expression<Kroner> = Kroner(3455).expr()
        val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_AvkortingsbelopPerAr: Expression<Kroner> = Kroner(7197).expr()
        val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert: Expression<Boolean> = true.expr()
        val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert: Expression<Boolean> = true.expr()
        val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_Inntektstak: Expression<Kroner> = Kroner(5068).expr()
        val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr_UtenMinus: Expression<Kroner> = Kroner(1404).expr()
        val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType: Expression<String> = "<TEXTVARIABEL:3869>".expr()
        val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_GradertOppjustertIFU: Expression<Kroner> = Kroner(1959).expr()
        val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_ProsentsatsOIFUForTak: Expression<Double> = 8.0.expr()
        val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoEtterReduksjonBT: Expression<Kroner> = Kroner(4224).expr()
        val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_SumBruttoForReduksjonBT: Expression<Kroner> = Kroner(3233).expr()
        val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_TotaltAntallBarn: Expression<Int> = 9729.expr()
        val PE_Vedtaksbrev_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_FortsattMedlemskap_InngangUnntak: Expression<String> = "<TEXTVARIABEL:8813>".expr()
        val PE_Vedtaksbrev_Vedtaksdata_VilkarsVedtak_Vilkar_MedlemskapForUTEtterTrygdeavtaler_OppfyltVedSammenlegging: Expression<Boolean> = true.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningVirkningDatoFom: Expression<LocalDate> = LocalDate.of(2020,4, 20).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBBrukersInntektTilAvkortning: Expression<Kroner> = Kroner(3968).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBbruttoPerAr: Expression<Kroner> = Kroner(8678).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBnettoPerAr: Expression<Kroner> = Kroner(4267).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBbruttoPerAr: Expression<Kroner> = Kroner(1770).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_BTSBnettoPerAr: Expression<Kroner> = Kroner(4719).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget: Expression<Boolean> = true.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GjenlevendetilleggInformasjon_AnvendtTrygdetid: Expression<Int> = 4261.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GjenlevendetilleggInformasjon_BeregningsgrunnlagAvdodOrdiner_Arsbelop: Expression<Kroner> = Kroner(7769).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GjenlevendetilleggInformasjon_BeregningsgrunnlagAvdodOrdiner_OpptjeningUTListe_OpptjeningUT_Ar: Expression<Kroner> = Kroner(388).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GjenlevendetilleggInformasjon_BeregningsgrunnlagAvdodOrdiner_OpptjeningUTListe_OpptjeningUT_AvkortetBelop: Expression<Kroner> = Kroner(898).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GjenlevendetilleggInformasjon_BeregningsgrunnlagAvdodOrdiner_OpptjeningUTListe_OpptjeningUT_Brukt: Expression<Boolean> = true.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GjenlevendetilleggInformasjon_BeregningsgrunnlagAvdodOrdiner_OpptjeningUTListe_OpptjeningUT_Forstegansgstjeneste: Expression<Boolean> = true.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GjenlevendetilleggInformasjon_BeregningsgrunnlagAvdodOrdiner_OpptjeningUTListe_OpptjeningUT_InntektIAvtaleLand: Expression<Boolean> = true.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GjenlevendetilleggInformasjon_BeregningsgrunnlagAvdodOrdiner_OpptjeningUTListe_OpptjeningUT_JustertBelop: Expression<Kroner> = Kroner(3597).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GjenlevendetilleggInformasjon_BeregningsgrunnlagAvdodOrdiner_OpptjeningUTListe_OpptjeningUT_Omsorgsaar: Expression<Boolean> = true.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GjenlevendetilleggInformasjon_BeregningsgrunnlagAvdodOrdiner_OpptjeningUTListe_OpptjeningUT_Pgi: Expression<Kroner> = Kroner(1642).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GjenlevendetilleggInformasjon_BeregningsgrunnlagAvdodYrkesskadeArsbelop: Expression<Kroner> = Kroner(387).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GjenlevendetilleggInformasjon_InntektVedSkadetidspunktet: Expression<Kroner> = Kroner(9051).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GjenlevendetilleggInformasjon_MinsteYtelseBenyttetUngUfor: Expression<Kroner> = Kroner(3061).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GjenlevendetilleggInformasjon_Uforetidspunkt: Expression<LocalDate> = LocalDate.of(2020,9, 26).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GjenlevendetilleggInformasjon_Yrkesskadegrad: Expression<Double> = 5.0.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_NyttGjenlevendetillegg: Expression<Kroner> = Kroner(8192).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Minsteytelse_OppfyltUngUfor: Expression<Boolean> = true.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_AntallArInntektIAvtaleland: Expression<Int> = 9937.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_AntallArOver1G: Expression<Int> = 314.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_BeregningsgrunnlagOrdinerArsbelop: Expression<Kroner> = Kroner(8698).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Ar: Expression<Int> = 4393.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_AvkortetBelop: Expression<Kroner> = Kroner(6131).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Brukt: Expression<Boolean> = true.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Forstegansgstjeneste: Expression<Boolean> = true.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_InntektIAvtaleLand: Expression<Boolean> = true.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_JustertBelop: Expression<Kroner> = Kroner(8861).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Omsorgsaar: Expression<Boolean> = true.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagOrdinar_OpptjeningUTListe_OpptjeningUT_Pgi: Expression<Kroner> = Kroner(554).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagYrkesskadeArsbelop: Expression<Kroner> = Kroner(3212).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_InntektVedSkadetidspunktet: Expression<Kroner> = Kroner(1420).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_BeregningsMetode: Expression<String> = "<TEXTVARIABEL:2564>".expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Grunnbelop: Expression<Kroner> = Kroner(1103).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_ProratabrokNevner: Expression<Int> = 4798.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_ProratabrokTeller: Expression<Int> = 3561.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforetidspunkt: Expression<LocalDate> = LocalDate.of(2020,11, 26).expr()
        val PE_Vedtaksdata_BeregningsData_Beregning_BeregningBrukerSivilstand: Expression<String> = "<TEXTVARIABEL:6742>".expr()
        val PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_AntallBarnFelles: Expression<Int> = 8264.expr()
        val PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBInntektBruktiAvkortning: Expression<Kroner> = Kroner(500).expr()
        val PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBfribelop: Expression<Kroner> = Kroner(9550).expr()
        val PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_AntallBarnSerkull: Expression<Int> = 8432.expr()
        val PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning: Expression<Kroner> = Kroner(9058).expr()
        val PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBfribelop: Expression<Kroner> = Kroner(1303).expr()
        val PE_Vedtaksdata_Kravhode_BoddArbeidUtlandAvdod: Expression<Boolean> = true.expr()
        val PE_Vedtaksdata_Kravhode_KravGjelder: Expression<String> = "<TEXTVARIABEL:8734>".expr()
        val PE_Vedtaksdata_Kravhode_VurdereTrygdeavtale: Expression<Boolean> = true.expr()
        val PE_Vedtaksdata_TrygdetidAvdod_FaTTEOS: Expression<Int> = 5984.expr()
        val PE_Vedtaksdata_TrygdetidAvdod_FaTTNorge: Expression<Int> = 4901.expr()
        val PE_Vedtaksdata_TrygdetidAvdod_FaTT_A10_netto: Expression<Kroner> = Kroner(3914).expr()
        val PE_Vedtaksdata_TrygdetidAvdod_FramtidigTTEOS: Expression<Int> = 229.expr()
        val PE_Vedtaksdata_TrygdetidAvdod_FramtidigTTNorsk: Expression<Int> = 7815.expr()
        val PE_Vedtaksdata_TrygdetidAvdod_TTNevnerEOS: Expression<Int> = 8038.expr()
        val PE_Vedtaksdata_TrygdetidAvdod_TTNevnerNordisk: Expression<Int> = 6549.expr()
        val PE_Vedtaksdata_TrygdetidAvdod_TTNordisk: Expression<Int> = 5534.expr()
        val PE_Vedtaksdata_TrygdetidAvdod_TTTellerEOS: Expression<Int> = 7732.expr()
        val PE_Vedtaksdata_TrygdetidAvdod_TTTellerNordisk: Expression<Int> = 7590.expr()
        val PE_Vedtaksdata_TrygdetidAvdod_TTUtlandTrygdeAvtale_FaTTBilateral: Expression<Int> = 3839.expr()
        val PE_Vedtaksdata_TrygdetidAvdod_TTUtlandTrygdeAvtale_FramtidigTTAvtaleland: Expression<Int> = 9319.expr()
        val PE_Vedtaksdata_TrygdetidAvdod_TTUtlandTrygdeAvtale_TTNevnerBilateral: Expression<Int> = 3484.expr()
        val PE_Vedtaksdata_TrygdetidAvdod_TTUtlandTrygdeAvtale_TTTellerBilateral: Expression<Int> = 7933.expr()
        val PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUInntekt: Expression<Kroner> = Kroner(19).expr()
        val PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Skadetidspunkt: Expression<LocalDate> = LocalDate.of(2020,10, 26).expr()
        val PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_TTUtlandTrygdeAvtaleListe_TTUtlandTrygdeAvtale_FaTTBilateral: Expression<Int> = 9074.expr()
        val PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_FaTTNorge: Expression<Int> = 5021.expr()
        val PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_TTNevnerEOS: Expression<Int> = 9117.expr()
        val PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_TTNevnerNordisk: Expression<Int> = 644.expr()
        val PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_TTNordisk: Expression<Int> = 1575.expr()
        val PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_TTTellerEOS: Expression<Int> = 8620.expr()
        val PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Trygdetid_TTTellerNordisk: Expression<Int> = 3616.expr()
        val PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_Vilkar_UngUforResultat: Expression<String> = "<TEXTVARIABEL:5863>".expr()
        */

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

        val FUNKSJON_PE_UT_TBU601V_TBU604V = FUNKSJON_PE_UT_TBU601V_TBU604V(
            PE_Vedtaksdata_Kravhode_KravArsakType = PE_Vedtaksdata_Kravhode_KravArsakType,
            PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB = PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB,
            PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB = PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB,
            PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB = PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB,
            PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB = PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB,
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

        showIf(FUNKSJON_PE_UT_TBU601V_TBU604V) {
            includePhrase(
                TBU052V_TBU073V_Del_6_SlikRedusererViBarnetilleggetUtFraInntekt(
                    PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget,
                    PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget,
                    PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT = PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT,
                    PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_EN_cohabiting_partner = PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_EN_cohabiting_partner,
                    PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_NN_entall = PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_NN_entall,
                    PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr,
                    PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto,
                    PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr,
                    PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto,
                )
            )
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

        // TODO TBU052V-TBU073V_del7(slik beregner vi gjenlevendetillegget ditt til tittel "Slik beregner vi gjenlevendetillegget ditt")
        //  trengs kun for brev som ikke er:
        //  PE_UT_04_300, PE_UT_14_300, PE_UT_05_100, PE_UT_07_100, PE_UT_04_108, PE_UT_04_109, PE_UT_04_500, PE_UT_07_200
        //  eller brev som er PE_UT_04_300, PE_UT_14_300

        includePhrase(
            TBU052V_TBU073V_Del_8_ForDegSomMottarEktefelletillegg(
                PE_pebrevkode = PE_pebrevkode,
                PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget,
                PE_Vedtaksdata_Kravhode_KravArsakType = PE_Vedtaksdata_Kravhode_KravArsakType,
                PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Minsteytelse_Sats = PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Minsteytelse_Sats,
            )
        )

        showIf(
            (PE_Vedtaksdata_Kravhode_KravArsakType.notEqualTo("soknad_bt")
                        and PE_pebrevkode.notEqualTo("PE_UT_04_108")
                        and PE_pebrevkode.notEqualTo("PE_UT_04_109")
                        and PE_pebrevkode.notEqualTo("PE_UT_04_500")
                        and PE_pebrevkode.notEqualTo("PE_UT_07_200")
                        and (PE_pebrevkode.notEqualTo("PE_UT_04_102")
                            or (PE_pebrevkode.equalTo("PE_UT_04_102") and PE_Vedtaksdata_Kravhode_KravArsakType.notEqualTo("tilst_dod")))
                    ) or PE_pebrevkode.equalTo("PE_UT_06_300")) {
                includePhrase(
                    TBU052V_TBU073V_Del_9_EtteroppgjoerAvUforetrygdOgBarnetillegg(
                        PE_pebrevkode = PE_pebrevkode,
                        PE_Vedtaksdata_Kravhode_KravArsakType = PE_Vedtaksdata_Kravhode_KravArsakType,
                        PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget,
                        PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget,
                        PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt = PE_Vedtaksdata_BeregningsData_Beregning_BeregningSivilstandAnvendt,
                        PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland = PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland,
                    )
                )
            }
    }

