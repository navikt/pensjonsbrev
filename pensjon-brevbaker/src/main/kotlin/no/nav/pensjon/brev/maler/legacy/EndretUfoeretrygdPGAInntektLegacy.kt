package no.nav.pensjon.brev.maler.legacy

import no.nav.pensjon.brev.FeatureToggles
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.legacy.EndretUfoeretrygdPGAInntektDto
import no.nav.pensjon.brev.api.model.maler.legacy.EndretUfoeretrygdPGAInntektDtoSelectors.maanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.api.model.maler.legacy.EndretUfoeretrygdPGAInntektDtoSelectors.orienteringOmRettigheterUfoere
import no.nav.pensjon.brev.api.model.maler.legacy.EndretUfoeretrygdPGAInntektDtoSelectors.pe
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.maler.fraser.ufoer.Ufoeretrygd
import no.nav.pensjon.brev.maler.legacy.fraser.*
import no.nav.pensjon.brev.maler.legacy.vedlegg.vedleggOpplysningerBruktIBeregningUTLegacy
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgPlikterUfoere
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligUfoeretrygdFoerSkatt
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VEDTAK
import java.time.LocalDate

@TemplateModelHelpers
object EndretUfoeretrygdPGAInntektLegacy : AutobrevTemplate<EndretUfoeretrygdPGAInntektDto> {

    // PE_UT_05_100
    override val kode = Pesysbrevkoder.AutoBrev.UT_ENDRET_PGA_INNTEKT

    override val template = createTemplate(
        name = kode.name,
        letterDataType = EndretUfoeretrygdPGAInntektDto::class,
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - endring av uføretrygd på grunn av inntekt (automatisk)",
            isSensitiv = false,
            distribusjonstype = VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        // Språklige greier
        val epsTypeBokmal = pe.sivilstand_ektefelle_partner_samboer_bormed_ut()
        val epsTypeNynorsk = pe.sivilstand_ektefelle_partner_samboer_bormed_ut_nn_entall()
        val barnetEllerBarnaFelles = pe.ut_barnet_barna_felles()
        val barnetEllerBarnaSaerkull = pe.ut_barnet_barna_serkull()
        val barnetBarnaFellesSerkull = pe.pe_ut_barnet_barna_felles_serkull()

        val gammeltBelop = pe.vedtaksdata_beregningsdata_beregningufore_belopsendring_uforetrygdordineryk_belopgammelut()
        val nyttBelop = pe.vedtaksdata_beregningsdata_beregningufore_belopsendring_uforetrygdordineryk_belopnyut()
        val virkningFom = pe.vedtaksdata_virkningfom()
        val virkningFomAr = pe.ut_virkningfomar()
        val forventetInntekt = pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_forventetinntekt()
        val utbetalingsgrad = pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_utbetalingsgrad()
        val uforegrad = pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_uforegrad()
        val ettArForVirkningstidspunkt = pe.ut_virkningstidpunktarminus1ar()
        val vilFylle67IlaVirkningFomAr = pe.ut_vilfylle67ivirkningfomar()
        val virkningFomErForsteDagIAaret = FUNKSJON_FF_CheckIfFirstDayAndMonthOfYear(pe.vedtaksdata_virkningfom())
        val beregningvirkningDatoFomErForsteDagIAaret = FUNKSJON_FF_CheckIfFirstDayAndMonthOfYear(pe.vedtaksdata_beregningsdata_beregningufore_beregningvirkningdatofom())
        val ektefelletilleggInnvilget = pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_ektefelletillegg_etinnvilget()
        val gjenlevendetilleggInnvilget = pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_gjenlevendetillegg_gtinnvilget()
        val instoppholdType = pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_instoppholdtype()
        val beregningUforeTotalNetto = pe.vedtaksdata_beregningsdata_beregningufore_totalnetto()
        val avkortningInntektsgrense = pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektsgrense()
        val avkortningInntektstak = pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortningsinformasjon_inntektstak()
        val beregningBelopRedusert = pe.vedtaksdata_beregningsdata_beregningufore_belopredusert()
        val avkortningsbelopPerAr = pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_avkortingsbelopperar()
        val beregningGrunnbelop = pe.vedtaksdata_beregningsdata_beregningufore_uforetrygdberegning_grunnbelop()
        val kravarsaksType = pe.vedtaksdata_kravhode_kravarsaktype()
        val nettoRestAr = pe.nettoakk_pluss_nettorestar()
        val nettoAkk = pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_nettoakk()
        val uforetrygdNetto = pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_uforetrygdordiner_netto()
        val beregningBelopOkt = pe.vedtaksdata_beregningsdata_beregningufore_belopokt()
        val barnetilleggRegelverkType = pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_reduksjonsgrunnlag_barnetilleggregelverktype()

        // Barnetillegg fellesbarn
        val gammeltBelopBTFB = pe.vedtaksdata_beregningsdata_beregningufore_belopsendring_barnetilleggfellesyk_belopgammelbtfb()
        val nyttBelopBTFB = pe.vedtaksdata_beregningsdata_beregningufore_belopsendring_barnetilleggfellesyk_belopnybtfb()
        val innvilgetBTFB = pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinnvilget()
        val inntektBruktIAvkortningAvBTFB = pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbinntektbruktiavkortning()
        val periodisertInntektBTFB = pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektperiodisert()
        val periodisertFribelopBTFB = pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_fribelopperiodisert()
        val brukersInntektBruktIAvkortningAvBTFB = pe.vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbrukersinntekttilavkortning()
        val inntektAnnenForelderBTFB = pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbinntektannenforelder()
        val belopFratrukketAnnenForeldersInntektBTFB = pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_btfbbelopfratrukketannenforeldersinntekt()
        val justeringsbelopPerArBTFB = pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_justeringsbelopperar()
        val nettoBTFB = pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbnetto()
        val fribelopBTFB = pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggfelles_btfbfribelop()
        val avkortningInntektstakBTFB = pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_inntektstak()
        val arligAvkortningsbelopBTFB = pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggfelles_avkortningsinformasjon_avkortingsbelopperar()
        val justeringsbelopPerArUtenMinusBTFB = pe.barnetilleggfelles_justeringsbelopperarutenminus()

        // Barnetillegg saerkullsbarn
        val gammeltBelopBTSB = pe.vedtaksdata_beregningsdata_beregningufore_belopsendring_barnetilleggserkullyk_belopgammelbtsb()
        val nyttBelopBTSB = pe.vedtaksdata_beregningsdata_beregningufore_belopsendring_barnetilleggserkullyk_belopnybtsb()
        val innvilgetBTSB = pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinnvilget()
        val inntektBruktIAvkortningAvBTSB = pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbinntektbruktiavkortning()
        val periodisertInntektBTSB = pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektperiodisert()
        val periodisertFribelopBTSB = pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_fribelopperiodisert()
        val nettoBTSB = pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbnetto()
        val justeringsbelopPerArBTSB = pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_justeringsbelopperar()
        val fribelopBTSB = pe.vedtaksdata_beregningsdata_beregning_beregningytelsekomp_barnetilleggserkull_btsbfribelop()
        val avkortningInntektstakBTSB = pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_inntektstak()
        val arligAvkortningsbelopBTSB = pe.vedtaksbrev_vedtaksdata_beregningsdata_beregningufore_beregningytelseskomp_barnetilleggserkull_avkortningsinformasjon_avkortingsbelopperar()
        val justeringsbelopPerArUtenMinusBTSB = pe.barnetilleggserkull_justeringsbelopperarutenminus()

        title {
                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB = PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB = PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB) THEN      INCLUDE ENDIF
                showIf(
                    (gammeltBelop.notEqualTo(nyttBelop)
                            and gammeltBelopBTFB.equalTo(nyttBelopBTFB)
                            and gammeltBelopBTSB.equalTo(nyttBelopBTSB))
                ) {
                    text (
                        Bokmal to "Nav har endret utbetalingen av uføretrygden din",
                        Nynorsk to "Nav har endra utbetalinga av uføretrygda di",
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT  AND (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB  OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB)) THEN      INCLUDE ENDIF
                showIf(
                    (gammeltBelop.notEqualTo(nyttBelop)
                            and (gammeltBelopBTFB.notEqualTo(nyttBelopBTFB)
                            or gammeltBelopBTSB.notEqualTo(nyttBelopBTSB)))
                ) {
                    text (
                        Bokmal to "Nav har endret utbetalingen av uføretrygden og ",
                        Nynorsk to "Nav har endra utbetalinga av uføretrygda di og ",
                    )
                }

                //IF((PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT  AND (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB  OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB)) AND ((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false) OR (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true))) THEN INCLUDE ENDIF
            showIf(
                (
                        (
                                gammeltBelop.notEqualTo(nyttBelop)
                                        and (gammeltBelopBTFB.notEqualTo(nyttBelopBTFB)
                                        or gammeltBelopBTSB.notEqualTo(nyttBelopBTSB))
                                )
                                and (
                                (innvilgetBTSB and not(innvilgetBTFB))
                                        or (not(innvilgetBTSB)
                                        and innvilgetBTFB))
                        )
            ) {
                    text (
                        Bokmal to "barnetillegget ditt",
                        Nynorsk to "barnetillegget ditt",
                    )
                }

                //IF((PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT  AND (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB  OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB)) AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true)) THEN      INCLUDE ENDIF
            showIf(
                (
                        (
                                gammeltBelop.notEqualTo(nyttBelop)
                                        and (gammeltBelopBTFB.notEqualTo(nyttBelopBTFB)
                                        or gammeltBelopBTSB.notEqualTo(nyttBelopBTSB)
                                        )
                                )
                                and (innvilgetBTSB and innvilgetBTFB)
                        )
            ) {
                    text (
                        Bokmal to "barnetilleggene dine",
                        Nynorsk to "barnetillegga di",
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT = PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT  AND (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB  OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB)) THEN      INCLUDE ENDIF
                showIf(
                    (gammeltBelop.equalTo(nyttBelop)
                            and (gammeltBelopBTFB.notEqualTo(nyttBelopBTFB)
                            or gammeltBelopBTSB.notEqualTo(nyttBelopBTSB)))
                ) {
                    text (
                        Bokmal to "Nav har endret utbetalingen av ",
                        Nynorsk to "Nav har endra utbetalinga av ",
                    )
                }

                //IF((PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT = PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT  AND (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB  OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB)) AND ((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false) OR (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true)))  THEN      INCLUDE ENDIF
            showIf(
                (
                        (
                                gammeltBelop.equalTo(nyttBelop)
                                        and (gammeltBelopBTFB.notEqualTo(nyttBelopBTFB)
                                        or gammeltBelopBTSB.notEqualTo(nyttBelopBTSB))
                                )
                                and (
                                (innvilgetBTSB and not(innvilgetBTFB))
                                        or (not(innvilgetBTSB)
                                        and innvilgetBTFB)
                                )
                        )
            ) {
                    text (
                        Bokmal to "barnetillegget ",
                        Nynorsk to "barnetillegget ",
                    )
                }

                //IF((PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT = PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT  AND (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB  OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB)) AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true)) THEN      INCLUDE ENDIF
            showIf(
                (
                        (
                                gammeltBelop.equalTo(nyttBelop)
                                        and (gammeltBelopBTFB.notEqualTo(nyttBelopBTFB)
                                        or gammeltBelopBTSB.notEqualTo(nyttBelopBTSB)
                                        )
                                ) and (innvilgetBTSB and innvilgetBTFB)
                        )
            ) {
                    text (
                        Bokmal to "barnetilleggene ",
                        Nynorsk to "barnetillegga ",

                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT = PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT  AND (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB  OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB)) THEN      INCLUDE ENDIF
                showIf(
                    (gammeltBelop.equalTo(nyttBelop)
                            and (gammeltBelopBTFB.notEqualTo(nyttBelopBTFB)
                            or gammeltBelopBTSB.notEqualTo(nyttBelopBTSB)))
                ) {
                    text (
                        Bokmal to "i uføretrygden din",
                        Nynorsk to "i uføretrygda di",
                    )
                }
        }
        outline {

            //IF(FF_CheckIfFirstDayAndMonthOfYear(PE_VedtaksData_VirkningFOM) = false) THEN      INCLUDE ENDIF
            showIf(not(virkningFomErForsteDagIAaret)) {
                //[TBU2249NN, TBU2249]

                paragraph {
                    text (
                        Bokmal to "Vi har mottatt nye opplysninger om inntekten",
                        Nynorsk to "Vi har fått nye opplysningar om inntekta",
                    )

                    //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false
                    showIf(not(innvilgetBTFB)) {
                        text (
                            Bokmal to " din",
                            Nynorsk to " di",
                        )
                    }

                    //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
                    showIf(innvilgetBTFB) {
                        textExpr(
                            Bokmal to " til deg eller din ".expr() + epsTypeBokmal + ". Inntekten til din " + epsTypeBokmal + " har kun betydning for størrelsen på barnetillegget ",
                            Nynorsk to " til deg eller ".expr() + epsTypeNynorsk + " din. Inntekta til " + epsTypeNynorsk + " din har berre betydning for storleiken på barnetillegget ",
                        )
                        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true) THEN      INCLUDE ENDIF
                        showIf(innvilgetBTSB) {
                            textExpr(
                                Bokmal to "for ".expr() + barnetEllerBarnaFelles + " som bor med begge sine foreldre",
                                Nynorsk to "for ".expr() + barnetEllerBarnaFelles + " som bur saman med begge foreldra sine",
                            )
                        }.orShow {
                            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false) THEN      INCLUDE ENDIF
                            text(
                                Bokmal to "ditt",
                                Nynorsk to "ditt",
                            )
                        }
                    }

                    text (
                        Bokmal to ". Utbetalingen av ",
                        Nynorsk to ". Utbetalinga av ",
                    )

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT) THEN      INCLUDE ENDIF
                    showIf(
                        gammeltBelop
                            .notEqualTo(nyttBelop)
                    ){
                        text (
                            Bokmal to "uføretrygden din ",
                            Nynorsk to "uføretrygda di ",
                        )
                        //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT  AND (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB  OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB)) THEN      INCLUDE ENDIF
                        showIf(
                            gammeltBelopBTFB.notEqualTo(nyttBelopBTFB)
                                    or gammeltBelopBTSB.notEqualTo(nyttBelopBTSB)
                        ) {
                            text (
                                Bokmal to "og ",
                                Nynorsk to "og ",
                            )
                        }
                    }

                    //IF(((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false)  OR (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true))) AND (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB  OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB)  THEN      INCLUDE ENDIF
                    showIf(
                        (
                                (
                                        innvilgetBTSB
                                                and not(innvilgetBTFB)
                                        )
                                        or (not(innvilgetBTSB)
                                        and innvilgetBTFB)
                                )
                                and (
                                gammeltBelopBTFB.notEqualTo(nyttBelopBTSB)
                                        or gammeltBelopBTSB.notEqualTo(nyttBelopBTSB)
                                )
                    ) {
                        text (
                            Bokmal to "barnetillegget ditt ",
                            Nynorsk to "barnetillegget ditt ",
                        )
                    }

                    //IF((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)) AND (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB  OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB)  THEN      INCLUDE ENDIF
                    showIf(
                        innvilgetBTFB
                                and innvilgetBTSB
                                and (
                                gammeltBelopBTFB.notEqualTo(nyttBelopBTSB)
                                        or gammeltBelopBTSB.notEqualTo(nyttBelopBTSB)
                                )
                    ) {
                        text (
                            Bokmal to "barnetilleggene dine ",
                            Nynorsk to "barnetillegga dine ",
                        )
                    }
                    text (
                        Bokmal to "er derfor endret fra ",
                        Nynorsk to "er derfor endra frå ",
                    )
                    ifNotNull(virkningFom) {
                        textExpr(
                            Bokmal to it.format(),
                            Nynorsk to it.format(),
                        )
                    }
                    text(
                        Bokmal to ".",
                        Nynorsk to ".",
                    )
                }
            }

            //Integer iYear  iYear = FF_getYear(PE_VedtaksData_VirkningFOM)  IF(  PE_VedtaksData_VirkningFOM = FF_getFirstDayOfYear(iYear) AND (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT)  ) THEN      INCLUDE ENDIF
            //[TBU3403NN, TBU3403]
            showIf(virkningFomErForsteDagIAaret and gammeltBelop.notEqualTo(nyttBelop)) {
                paragraph {
                    textExpr(
                        Bokmal to "Vi vil bruke en inntekt på ".expr() + forventetInntekt.format() + " kroner når vi reduserer uføretrygden din for " + virkningFomAr.format() + ". Har du ikke meldt inn ny inntekt for " + virkningFomAr.format() + ", er inntekten justert opp til dagens verdi.",
                        Nynorsk to "Vi vil bruke ei inntekt på ".expr() + forventetInntekt.format() + " kroner når vi reduserer uføretrygda di for " + virkningFomAr.format() + ". Har du ikkje meldt inn ny inntekt for " + virkningFomAr.format() + ", er inntekta justert opp til dagens verdi.",
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad) THEN      INCLUDE ENDIF
                showIf(utbetalingsgrad.notEqualTo(uforegrad)) {
                    paragraph {
                        textExpr(
                            Bokmal to "Fikk du innvilget uføretrygd etter januar ".expr() + ettArForVirkningstidspunkt.format() + ", er inntekten justert opp slik at den gjelder for hele " + virkningFomAr.format() + ".",
                            Nynorsk to "Fekk du innvilga uføretrygd etter januar ".expr() + ettArForVirkningstidspunkt.format() + ", er inntekta også justert opp slik at den gjeld for heile " + virkningFomAr.format() + ".",
                        )
                    }
                }

                //PE_UT_VilFylle67iVirkningFomAr = true
                showIf(vilFylle67IlaVirkningFomAr) {
                    paragraph {
                        textExpr(
                            Bokmal to "Fordi du fyller 67 år i ".expr() + virkningFomAr.format() + ", er inntekten justert i forhold til antall måneder du mottar uføretrygd.",
                            Nynorsk to "Fordi du fyljer 67 år i ".expr() + virkningFomAr.format() + ", er inntekta justert ut frå talet på månadar du får uføretrygd.",
                        )
                    }
                }
            }
            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT  AND  (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB  OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB)) AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget= true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false) AND FF_CheckIfFirstDayAndMonthOfYear(PE_VedtaksData_VirkningFOM) = true THEN  INCLUDE ENDIF
            showIf(
                gammeltBelop.notEqualTo(nyttBelop)
                        and (gammeltBelopBTFB.notEqualTo(nyttBelopBTFB)
                        or gammeltBelopBTSB.notEqualTo(nyttBelopBTSB)
                        )
                        and innvilgetBTSB
                        and not(innvilgetBTFB)
                        and virkningFomErForsteDagIAaret
            ) {
                //[TBU4016_NN, TBU4016]

                paragraph {
                    textExpr (
                        Bokmal to "I reduksjonen av barnetillegget ditt vil vi bruke en inntekt på ".expr() + inntektBruktIAvkortningAvBTSB.format() + " kroner. ",
                        Nynorsk to "I reduksjonen av barnetillegget ditt vil vi bruke ei inntekt på ".expr() + inntektBruktIAvkortningAvBTSB.format() + " kroner.",
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT  AND (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB  OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB)) AND (FF_CheckIfFirstDayAndMonthOfYear(PE_VedtaksData_VirkningFOM) = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true )     THEN  INCLUDE ENDIF
            showIf(
                gammeltBelop
                    .notEqualTo(nyttBelop) and (gammeltBelopBTFB
                    .notEqualTo(nyttBelopBTFB) or gammeltBelopBTSB
                    .notEqualTo(
                        nyttBelopBTSB
                    )) and virkningFomErForsteDagIAaret and innvilgetBTFB
            ){
                //[TBU4001]
                paragraph {

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT  AND (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB  OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB)) THEN      INCLUDE ENDIF
                    showIf(
                        gammeltBelop
                            .notEqualTo(nyttBelop) and (gammeltBelopBTFB
                            .notEqualTo(nyttBelopBTFB) or gammeltBelopBTSB
                            .notEqualTo(
                                nyttBelopBTSB
                            ))){
                        text (
                            Bokmal to "I reduksjonen av barnetillegg",
                            Nynorsk to "I reduksjonen av barnetillegg",
                        )
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT  AND (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB  OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB)) AND FF_CheckIfFirstDayAndMonthOfYear(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningVirkningDatoFom) AND ((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true))     THEN  INCLUDE ENDIF
                    showIf(
                        gammeltBelop
                            .notEqualTo(nyttBelop) and (gammeltBelopBTFB
                            .notEqualTo(nyttBelopBTFB) or gammeltBelopBTSB
                            .notEqualTo(
                                nyttBelopBTSB
                            )) and beregningvirkningDatoFomErForsteDagIAaret
                                and (innvilgetBTFB or innvilgetBTSB)
                    ) {
                        text (
                            Bokmal to "et ditt ",
                            Nynorsk to "et ditt ",
                        )
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT  AND (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB  OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB)) THEN      INCLUDE ENDIF
                    showIf(
                        gammeltBelop.notEqualTo(nyttBelop)
                                and (gammeltBelopBTFB.notEqualTo(nyttBelopBTFB)
                                or gammeltBelopBTSB.notEqualTo(nyttBelopBTSB))
                    ) {
                        textExpr (
                            Bokmal to "vil vi bruke en inntekt på ".expr() + inntektBruktIAvkortningAvBTFB
                                .format() + " kroner ",
                            Nynorsk to "vil vi bruke ei inntekt på ".expr() + inntektBruktIAvkortningAvBTFB
                                .format() + " kroner ",
                        )
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true) THEN      INCLUDE ENDIF
                    showIf(innvilgetBTFB and innvilgetBTSB) {
                        textExpr (
                            Bokmal to "for ".expr() + barnetEllerBarnaFelles + " som bor med begge sine foreldre. For " + barnetEllerBarnaSaerkull + " som ikke bor sammen med begge foreldrene vil vi bruke en inntekt på " + inntektBruktIAvkortningAvBTSB
                                .format() + " kroner",
                            Nynorsk to "for ".expr() + barnetEllerBarnaFelles + " som bur med begge sine foreldra. For " + barnetEllerBarnaSaerkull + " som ikkje bur saman med begge foreldrea vil vi bruke ei inntekt på " + inntektBruktIAvkortningAvBTSB
                                .format() + " kroner",
                        )
                    }
                    text (
                        Bokmal to ".",
                        Nynorsk to ".",
                    )
                }
            }


            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT = PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB  AND  (FF_CheckIfFirstDayAndMonthOfYear(PE_VedtaksData_VirkningFOM) = true) )     THEN  INCLUDE ENDIF
            showIf(
                gammeltBelop.equalTo(nyttBelop)
                        and gammeltBelopBTFB.notEqualTo(nyttBelopBTFB)
                        and virkningFomErForsteDagIAaret
            ) {
                //[TBU4002_NN, TBU4002]

                paragraph {
                    textExpr (
                        Bokmal to "Vi vil bruke en inntekt på ".expr() + inntektBruktIAvkortningAvBTFB
                            .format() + " kroner når vi reduserer barnetillegget ",
                        Nynorsk to "Vi vil bruke ei inntekt på ".expr() + inntektBruktIAvkortningAvBTFB
                            .format() + " kroner når vi reduserer barnetillegget ",
                    )

                    //IF((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false)  OR (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true)) THEN      INCLUDE ENDIF
                    showIf(
                        (
                                innvilgetBTSB
                                        and not(innvilgetBTFB)
                                )
                                or (not(innvilgetBTFB) and innvilgetBTFB)
                    ) {
                        text (
							Bokmal to "ditt ",
							Nynorsk to "ditt ",
                        )
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true) THEN      INCLUDE ENDIF
                    showIf(innvilgetBTFB and innvilgetBTSB) {
                        textExpr (
                            Bokmal to "for ".expr() + barnetEllerBarnaFelles + " som bor med begge sine foreldre for " + virkningFomAr.format() + ". For " + barnetEllerBarnaSaerkull + " som ikke bor sammen med begge foreldrene vil vi bruke en inntekt på " + inntektBruktIAvkortningAvBTSB.format() + " kroner. ",
                            Nynorsk to "for ".expr() + barnetEllerBarnaFelles + " som bur saman med begge foreldra sine for " + virkningFomAr.format() + ". For " + barnetEllerBarnaSaerkull + " som ikkje bur saman med begge foreldra vil vi bruke ei inntekt på " + inntektBruktIAvkortningAvBTSB.format() + " kroner. ",
                        )
                    }
                    text (
                        Bokmal to "Har du ikke meldt inn ny",
                        Nynorsk to "Har du ikkje meldt inn ",
                    )

                    //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
                    showIf(innvilgetBTFB) {
                        text(
                            Bokmal to "e",
                            Nynorsk to "ei",
                        )
                    }
                    text (
                        Bokmal to " inntekt",
                        Nynorsk to " ny",
                    )

                    //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
                    showIf(innvilgetBTFB) {
                        text(
                            Bokmal to "er",
                            Nynorsk to "e",
                        )
                    }
                    textExpr (
                        Bokmal to " for ".expr() + virkningFomAr.format() + ", er inntekten",
                        Nynorsk to " inntekt".expr(),
                    )

                    //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
                    showIf(innvilgetBTFB) {
                        text(
                            Bokmal to "e",
                            Nynorsk to "er",
                        )
                    }
                    textExpr (
                        Bokmal to " justert opp til dagens verdi.".expr(),
                        Nynorsk to " for ".expr() + virkningFomAr.format() + ", er ",
                    )

                    //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false
                    showIf(not(innvilgetBTFB)) {
                        text (
                            Bokmal to "",
                            Nynorsk to "inntekta",
                        )
                    }

                    //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
                    showIf(innvilgetBTFB) {
                        text(
                            Bokmal to "",
                            Nynorsk to "inntektene ",
                        )
                    }
                    text (
                        Bokmal to "",
                        Nynorsk to "justert opp til dagens verdi.",
                    )
                }
            }

            //IF (FF_CheckIfFirstDayAndMonthOfYear(PE_VedtaksData_VirkningFOM) = true AND  (   PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB = PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB    AND    PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB ) AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT = PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT) THEN  INCLUDE ENDIF
            showIf(
                virkningFomErForsteDagIAaret
                        and gammeltBelopBTFB.equalTo(nyttBelopBTFB)
                        and gammeltBelopBTSB.notEqualTo(nyttBelopBTSB)
                        and gammeltBelop.equalTo(nyttBelop)
            ) {
                //[TBU4017_NN, TBU4017]

                paragraph {
                    textExpr (
                        Bokmal to "Vi vil bruke en inntekt på ".expr() + inntektBruktIAvkortningAvBTSB
                            .format() + " kroner når vi reduserer barnetillegget ditt. Har du ikke meldt inn ny inntekt for " + virkningFomAr
                            .format() + ", er inntekten justert opp til dagens verdi.",
                        Nynorsk to "Vi vil bruke ei inntekt på ".expr() + inntektBruktIAvkortningAvBTSB
                            .format() + " kroner når vi reduserer barnetillegget ditt. Har du ikkje meldt inn ny inntekt for " + virkningFomAr
                            .format() + ", er inntekta justert opp til dagens verdi.",
                    )
                }
            }

            //IF( (PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad) AND  ((PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB  <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB) OR (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB  <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB)) AND FF_CheckIfFirstDayAndMonthOfYear(PE_VedtaksData_VirkningFOM) = true ) THEN      INCLUDE ENDIF
            showIf(
                utbetalingsgrad.equalTo(uforegrad)
                        and (
                        (
                                gammeltBelopBTSB.notEqualTo(nyttBelopBTSB)
                                )
                                or (gammeltBelopBTFB.notEqualTo(nyttBelopBTFB))
                        ) and virkningFomErForsteDagIAaret
            ) {
                //[TBU4013_NN, TBU4013]

                paragraph {
                    textExpr (
                        Bokmal to "Fikk du innvilget uføretrygd etter januar ".expr() + ettArForVirkningstidspunkt
                            .format() + ", er inntekten justert opp slik at den gjelder for hele " + virkningFomAr
                            .format() + ".",
                        Nynorsk to "Fekk du innvilga uføretrygd etter januar ".expr() + ettArForVirkningstidspunkt
                            .format() + ", er inntekta justert opp slik at den gjeld for heile " + virkningFomAr
                            .format() + ".",
                    )
                }
            }

            //IF ( FF_CheckIfFirstDayAndMonthOfYear(PE_VedtaksData_VirkningFOM) = true AND  ( PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB > PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB  OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB > PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB ) AND  ( PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB > 0 OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB > 0 ) AND ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert= true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert= true ) )THEN  INCLUDE ENDIF
            showIf(
                virkningFomErForsteDagIAaret
                        and (gammeltBelopBTFB.greaterThan(nyttBelopBTFB)
                        or gammeltBelopBTSB.greaterThan(nyttBelopBTSB)
                        ) and (nyttBelopBTFB.greaterThan(0)
                        or nyttBelopBTSB.greaterThan(0)
                        ) and (
                        periodisertInntektBTFB
                                or periodisertInntektBTSB
                                or periodisertFribelopBTFB
                                or periodisertFribelopBTSB
                        )
            ) {
                //[TBU4003_NN, TBU4003]

                paragraph {
                    text (
                        Bokmal to "Fordi du ikke har barnetillegg ",
                        Nynorsk to "Fordi du ikkje har barnetillegg ",
                    )

                    //IF( ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = true AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert= false ) OR ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert= false ) AND ( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true ) ) THEN      INCLUDE ENDIF
                    showIf(
                        (
                                (
                                        periodisertInntektBTFB
                                                and not(periodisertInntektBTSB)
                                        )
                                        or (periodisertFribelopBTFB
                                        and not(periodisertFribelopBTSB)
                                        )
                                        and (innvilgetBTFB and innvilgetBTSB))
                    ) {
                        textExpr (
                            Bokmal to "for ".expr() + barnetEllerBarnaFelles + " som bor med begge sine foreldre ",
                            Nynorsk to "for ".expr() + barnetEllerBarnaFelles + " som bur saman med begge foreldra ",
                        )
                    }

                    //IF( ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert= true ) OR ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert= true ) AND ( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true ) )THEN   INCLUDE ENDIF
                    showIf(
                        (
                                not(periodisertInntektBTFB)
                                        and periodisertInntektBTSB)
                                or (not(periodisertFribelopBTFB)
                                and periodisertFribelopBTSB
                                )
                                and (innvilgetBTFB and innvilgetBTSB)
                    ) {
                        textExpr (
                            Bokmal to "for ".expr() + barnetEllerBarnaSaerkull + " som ikke bor sammen med begge foreldrene ",
                            Nynorsk to "for ".expr() + barnetEllerBarnaSaerkull + " som ikkje bur saman med begge foreldra ",
                        )
                    }
                    text (
                        Bokmal to "hele året er ",
                        Nynorsk to "heile året er ",
                    )

                    //IF(( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert= true )) THEN   INCLUDE ENDIF
                    showIf((periodisertInntektBTFB or periodisertInntektBTSB)) {
                        text (
                            Bokmal to "inntektene ",
                            Nynorsk to "inntektene ",
                        )
                    }

                    //IF( ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert= true ) AND ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert= true ) )THEN   INCLUDE ENDIF
                    showIf((periodisertInntektBTFB or periodisertInntektBTSB) and (periodisertFribelopBTFB or periodisertFribelopBTSB)) {
                        text (
                            Bokmal to "og ",
                            Nynorsk to "og ",
                        )
                    }

                    //IF( ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert= true ) )THEN   INCLUDE ENDIF
                    showIf((periodisertFribelopBTFB or periodisertFribelopBTSB)) {
                        text (
                            Bokmal to "fribeløpet ",
                            Nynorsk to "fribeløpet ",
                        )
                    }
                    text (
                        Bokmal to "justert slik at de kun gjelder for den perioden du mottar barnetillegg.",
                        Nynorsk to "justert slik at dei berre gjelde for den perioden du får barnetillegg.",
                    )
                }
            }

            //IF(FF_CheckIfFirstDayAndMonthOfYear(PE_VedtaksData_VirkningFOM) = true) THEN      INCLUDE ENDIF
            showIf(virkningFomErForsteDagIAaret) {
                //[TBU4004_NN, TBU4004]

                paragraph {
                    text (
                        Bokmal to "Forventer du ",
                        Nynorsk to "Forventar du ",
                    )

                    //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
                    showIf(innvilgetBTFB) {
                        textExpr(
                            Bokmal to "og din ".expr() + epsTypeBokmal + " ",
                            Nynorsk to "og ".expr() + epsTypeNynorsk + " din ",
                        )
                    }

                    textExpr (
                        Bokmal to "å tjene noe annet i ".expr() + virkningFomAr
                            .format() + " er det viktig at du melder inn ny forventet inntekt. Dette kan du gjøre på nav.no.",
                        Nynorsk to "å tene noko anna i ".expr() + virkningFomAr
                            .format() + " er det viktig at du melder inn ei ny forventa inntekt. Dette kan du gjere på nav.no.",
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "") THEN      INCLUDE ENDIF
            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "") THEN      INCLUDE ENDIF
            showIf(
                (not(ektefelletilleggInnvilget) and not(
                    innvilgetBTSB
                ) and not(innvilgetBTFB)
                        and not(gjenlevendetilleggInnvilget)
                        and instoppholdType.equalTo(""))
            ) {
                includePhrase(TBU1120_Generated(pe))
            }

            //IF(  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = ""  ) THEN      INCLUDE ENDIF
            showIf(
                (
                        (innvilgetBTSB or innvilgetBTFB)
                                and not(ektefelletilleggInnvilget)
                                and not(gjenlevendetilleggInnvilget)
                                and instoppholdType.equalTo(""))
            ) {
                includePhrase(TBU1121_Generated(pe))
            }

            //IF(  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true  ) THEN      INCLUDE ENDIF
            showIf(
                (
                        (innvilgetBTSB or innvilgetBTFB)
                                and not(gjenlevendetilleggInnvilget)
                                and ektefelletilleggInnvilget)
            ) {
                includePhrase(TBU1254_Generated(pe))
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true) THEN      INCLUDE ENDIF
            showIf(
                (
                        not(innvilgetBTSB)
                                and not(innvilgetBTFB)
                                and not(gjenlevendetilleggInnvilget)
                                and ektefelletilleggInnvilget)
            ) {
                includePhrase(TBU1253_Generated(pe))
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "") THEN      INCLUDE ENDIF
            showIf(
                (
                        not(innvilgetBTSB)
                                and not(innvilgetBTFB)
                                and gjenlevendetilleggInnvilget
                                and not(ektefelletilleggInnvilget)
                                and instoppholdType.equalTo("")
                        )
            ) {
                includePhrase(TBU1122_Generated(pe))
            }

            //IF(  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false  ) THEN      INCLUDE ENDIF
            showIf(
                (
                        (innvilgetBTSB or innvilgetBTFB)
                                and gjenlevendetilleggInnvilget
                                and not(ektefelletilleggInnvilget)
                        )
            ) {
                includePhrase(TBU1123_Generated(pe))
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_TotalNetto > 0) THEN      INCLUDE ENDIF
            showIf((beregningUforeTotalNetto.greaterThan(0))) {
                includePhrase(Ufoeretrygd.UtbetalingsdatoUfoeretrygd(beregningUforeTotalNetto.greaterThan(0)))
            }
            includePhrase(Ufoeretrygd.ViktigAALeseHeleBrevet)
            //[TBU1029]

            includePhrase(Vedtak.BegrunnelseOverskrift)

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT) THEN      INCLUDE ENDIF
            showIf(gammeltBelop.notEqualTo(nyttBelop)) {

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense < PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak) THEN      INCLUDE ENDIF
                showIf(avkortningInntektsgrense.lessThan(avkortningInntektstak)) {
                    //[TBU2253NN, TBU2253]

                    paragraph {
                        text(
                            Bokmal to "Når vi endrer utbetalingen av uføretrygden din, tar vi utgangspunkt i inntekten du har ved siden av uføretrygden. Det er bare den delen av inntekten din som overstiger inntektsgrensen som vil gi en reduksjon av uføretrygden.",
                            Nynorsk to "Når vi endrar utbetalinga av uføretrygda di, tek vi utgangspunkt i inntekta du har ved sida av uføretrygda. Det er berre den delen av inntekta di som kjem over inntektsgrensa som gir ein reduksjon av uføretrygda di.",
                        )
                    }

                    paragraph {
                        //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt > PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT > PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT > 0) THEN      INCLUDE ENDIF
                        showIf(
                            forventetInntekt.greaterThan(avkortningInntektsgrense)
                                    and gammeltBelop.greaterThan(nyttBelop)
                                    and nyttBelop.greaterThan(0)
                        ) {
                            text (
                                Bokmal to "Uføretrygden din reduseres fordi du tjener over inntektsgrensen din. Selv om du får en reduksjon lønner det seg likevel å jobbe ved siden av uføretrygden. ",
                                Nynorsk to "Uføretrygda di blir redusert fordi du tener over inntektsgrensa di. Sjølv om du får ein reduksjon, lønner det seg likevel å jobbe ved sida av uføretrygda. ",
                            )
                        }

                        //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt > PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT < PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT > 0) THEN      INCLUDE ENDIF
                        showIf(
                            forventetInntekt.greaterThan(avkortningInntektsgrense)
                                    and utbetalingsgrad.equalTo(uforegrad)
                                    and gammeltBelop.lessThan(nyttBelop)
                                    and nyttBelop.greaterThan(0)
                        ) {
                            text (
                                Bokmal to "Endringen i inntekten din gjør at uføretrygden ikke lenger er redusert. ",
                                Nynorsk to "Endringa i inntekta di gjer at uføretrygda ikkje lenger er redusert. ",
                            )
                        }

                        //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt < PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT > 0) THEN      INCLUDE ENDIF
                        showIf(
                            forventetInntekt.lessThan(avkortningInntektsgrense)
                                    and utbetalingsgrad.equalTo(uforegrad)
                                    and nyttBelop.greaterThan(0)
                        ) {
                            text (
                                Bokmal to "Utbetalingen av uføretrygden din økes fordi du tjener under inntektsgrensen din. Det betyr at uføretrygden ikke lenger er redusert. ",
                                Nynorsk to "Utbetalinga av uføretrygda aukar fordi du tener under inntektsgrensa di. Det betyr at uføretrygda ikkje lenger er redusert. ",
                            )
                        }

                        //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT = 0 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt <= PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak) THEN      INCLUDE ENDIF
                        showIf(
                            nyttBelop.equalTo(0)
                                    and forventetInntekt.lessThanOrEqual(avkortningInntektstak)
                        ) {
                            text (
                                Bokmal to "Endring i inntekten din gjør at du ikke får utbetalt uføretrygd for resten av året. ",
                                Nynorsk to "Endring i inntekta di gjer at du ikkje får utbetalt uføretrygd for resten av året. ",
                            )
                        }
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense >= PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak) THEN      INCLUDE ENDIF
                showIf(avkortningInntektsgrense.greaterThanOrEqual(avkortningInntektstak)) {
                    //[TBU3734NN, TBU3734]

                    paragraph {
                        text (
                            Bokmal to "Når vi endrer utbetalingen av uføretrygden din, tar vi utgangspunkt i inntekten du har ved siden av uføretrygden.",
                            Nynorsk to "Når vi endrar utbetalinga av uføretrygda di, tek vi utgangspunkt i inntekta du har ved sida av uføretrygda.",
                        )

                        //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopRedusert = true AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt > PE_Vedtaksdata_BeregningsData_BeregningUforePeriode_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense) THEN      INCLUDE ENDIF
                        showIf(beregningBelopRedusert and forventetInntekt.greaterThan(avkortningInntektsgrense)) {
                            text (
                                Bokmal to " Det lønner seg likevel å jobbe, fordi inntekt og uføretrygd alltid vil være høyere enn uføretrygd alene.",
                                Nynorsk to " Det lønner seg likevel å jobbe fordi inntekt og uføretrygd vil alltid vere høgare enn uføretrygd åleine.",
                            )
                        }
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt > PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT > 0) THEN      INCLUDE ENDIF
                showIf(forventetInntekt.greaterThan(avkortningInntektsgrense) and nyttBelop.greaterThan(0)) {
                    //[TBU4005_NN, TBU4005]

                    paragraph {
                        textExpr (
                            Bokmal to "Siden du har en inntekt på ".expr() + forventetInntekt
                                .format() + " kroner trekker vi " + avkortningsbelopPerAr
                                .format() + " kroner fra uføretrygden ",
                            Nynorsk to "Fordi du har ei inntekt på ".expr() + forventetInntekt
                                .format() + " kroner trekkjer vi " + avkortningsbelopPerAr
                                .format() + " kroner frå uføretrygda ",
                        )

                        //IF(FF_CheckIfFirstDayAndMonthOfYear(PE_VedtaksData_VirkningFOM) = false) THEN      INCLUDE ENDIF
                        showIf(not(virkningFomErForsteDagIAaret)) {
                            text (
                                Bokmal to "i ",
                                Nynorsk to "i ",
                            )
                        }

                        //IF(FF_CheckIfFirstDayAndMonthOfYear(PE_VedtaksData_VirkningFOM) = true) THEN      INCLUDE ENDIF
                        showIf(virkningFomErForsteDagIAaret) {
                            text (
                                Bokmal to "for neste ",
                                Nynorsk to "for neste ",
                            )
                        }
                        text (
                            Bokmal to "år.",
                            Nynorsk to "år.",
                        )
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt > PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense <> PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT = 0) THEN      INCLUDE ENDIF
                showIf(
                    forventetInntekt.greaterThan(avkortningInntektstak)
                            and avkortningInntektsgrense.notEqualTo(avkortningInntektstak)
                            and nyttBelop.equalTo(0)
                ) {
                    //[TBU2258NN, TBU2258]

                    paragraph {
                        textExpr (
                            Bokmal to "Du får ikke utbetalt uføretrygd siden inntekten din er høyere enn 80 prosent av inntekten du hadde før du ble ufør, det vil si ".expr() + avkortningInntektstak
                                .format() + " kroner. Inntekten vi har brukt er " + forventetInntekt
                                .format() + " kroner og du vil derfor ikke få utbetalt uføretrygd resten av året.",
                            Nynorsk to "Du får ikkje utbetalt uføretrygd fordi inntekta di er høgare enn 80 prosent av inntekta du hadde før du blei ufør, det vil seie ".expr() + avkortningInntektstak
                                .format() + " kroner. Inntekta vi har brukt er " + forventetInntekt
                                .format() + " kroner og du vil ikkje få utbetalt uføretrygd resten av året.",
                        )
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt > PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense = PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT = 0) THEN      INCLUDE ENDIF
                showIf(
                    forventetInntekt.greaterThan(avkortningInntektsgrense)
                            and avkortningInntektsgrense.equalTo(avkortningInntektstak)
                            and nyttBelop.equalTo(0)
                ) {
                    //[TBU3735NN, TBU3735]

                    paragraph {
                        textExpr (
                            Bokmal to "Det utbetales ikke uføretrygd når inntekten din utgjør mer enn inntektsgrensen, det vil si ".expr() + avkortningInntektsgrense
                                .format() + " kroner. Inntekten vi har brukt er " + forventetInntekt
                                .format() + " kroner og du vil derfor ikke få utbetalt uføretrygd resten av året.",
                            Nynorsk to "Det blir ikkje utbetalt uføretrygd når inntekta di utgjer meir enn inntektsgrensa di, det vil seie ".expr() + avkortningInntektsgrense
                                .format() + " kroner. Inntekta vi har brukt er " + forventetInntekt
                                .format() + " kroner og du vil derfor ikkje få utbetalt uføretrygd resten av året.",
                        )
                    }
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB) THEN      INCLUDE ENDIF
            showIf(
                gammeltBelopBTFB.notEqualTo(nyttBelopBTFB)
                        or gammeltBelopBTSB.notEqualTo(nyttBelopBTSB)
            ) {

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true) THEN      INCLUDE ENDIF
                showIf(innvilgetBTFB or innvilgetBTSB) {
                    //[TBU4006_NN, TBU4006]

                    paragraph {
                        text (
                            Bokmal to "Inntekten din har ",
                            Nynorsk to "Inntekta di har ",
                        )

                        //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT  AND (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB  OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB)) THEN      INCLUDE ENDIF
                        showIf(
                            gammeltBelop.notEqualTo(nyttBelop)
                                    and (
                                    gammeltBelopBTFB.notEqualTo(nyttBelopBTFB)
                                            or gammeltBelopBTSB.notEqualTo(nyttBelopBTSB)
                                    )
                        ) {
                            text (
                                Bokmal to "også ",
                                Nynorsk to "også ",
                            )
                        }
                        text (
                            Bokmal to "betydning for hva du får utbetalt i barnetillegg. ",
                            Nynorsk to "betydning for kva du får utbetalt i barnetillegg. ",
                        )

                        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) THEN      INCLUDE ENDIF
                        showIf(not(innvilgetBTSB) and innvilgetBTFB) {
                            text (
                                Bokmal to "Fordi",
                                Nynorsk to "Fordi",
                            )
                        }

                        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) THEN      INCLUDE ENDIF
                        showIf(innvilgetBTSB and innvilgetBTFB) {
                            text (
                                Bokmal to "For",
                                Nynorsk to "For",
                            )
                        }

                        //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
                        showIf(innvilgetBTFB) {
                            textExpr(
                                Bokmal to " ".expr() + barnetEllerBarnaFelles + " ",
                                Nynorsk to " ".expr() + barnetEllerBarnaFelles + " ",
                            )
                        }

                        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) THEN      INCLUDE ENDIF
                        showIf(innvilgetBTSB and innvilgetBTFB) {
                            text (
                                Bokmal to "som ",
                                Nynorsk to "som ",
                            )
                        }

                        //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
                        showIf(innvilgetBTFB) {
                            textExpr(
                                Bokmal to "bor med begge sine foreldre, bruker vi i tillegg din ".expr() + epsTypeBokmal + "s inntekt når vi fastsetter størrelsen på barnetillegget ditt. ",
                                Nynorsk to "bur saman med begge foreldra sine, bruker vi i tillegg ".expr() + epsTypeNynorsk + " din si inntekt når vi fastset storleiken på barnetillegget ditt. ",
                            )
                        }

                        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) THEN      INCLUDE ENDIF
                        showIf(innvilgetBTSB and innvilgetBTFB) {
                            textExpr (
                                Bokmal to "For ".expr() + barnetEllerBarnaSaerkull + " som ikke bor sammen med begge foreldrene bruker vi kun inntekten din. ",
                                Nynorsk to "For ".expr() + barnetEllerBarnaSaerkull + " som ikkje bur saman med begge foreldra bruker vi berre inntekta di. ",
                            )
                        }
                        text (
                            Bokmal to "Uføretrygden ",
                            Nynorsk to "Uføretrygda ",
                        )

                        //PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false
                        showIf(not(gjenlevendetilleggInnvilget)) {
                            text (
                                Bokmal to "din ",
                                Nynorsk to "di ",
                            )
                        }

                        //PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true
                        showIf(gjenlevendetilleggInnvilget) {
                            text(
                                Bokmal to "og gjenlevendetillegget ditt ",
                                Nynorsk to "og attlevandetillegget ditt ",
                            )
                        }
                        text (
                            Bokmal to "regnes med som inntekt.",
                            Nynorsk to "er rekna med som inntekt.",
                        )
                    }
                }

                //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
                showIf(innvilgetBTFB) {
                    //[TBU4007_NN, TBU4007]

                    paragraph {
                        textExpr(
                            Bokmal to "Vi har beregnet barnetillegget på nytt ut fra inntekten din på ".expr() + brukersInntektBruktIAvkortningAvBTFB
                                .format() + " kroner og inntekten til din " + epsTypeBokmal + " på " + inntektAnnenForelderBTFB.format() + " kroner. ",
                            Nynorsk to "Vi har berekna barnetillegget på nytt ut frå inntekta di på ".expr() + brukersInntektBruktIAvkortningAvBTFB
                                .format() + " kroner og inntekta til " + epsTypeNynorsk + " din på " + inntektAnnenForelderBTFB.format() + " kroner. ",
                        )

                        //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBbelopFratrukketAnnenForeldersInntekt > 0) THEN      INCLUDE ENDIF
                        showIf((belopFratrukketAnnenForeldersInntektBTFB.greaterThan(0))) {
                            textExpr(
                                Bokmal to "Folketrygdens grunnbeløp på inntil ".expr() + beregningGrunnbelop.format() + " kroner er holdt utenfor din " + epsTypeBokmal + "s inntekt. ",
                                Nynorsk to "Grunnbeløpet i folketrygda på inntil ".expr() + beregningGrunnbelop.format() + " kroner er held utanfor inntekta til " + epsTypeNynorsk + " din. ",
                            )
                        }
                        textExpr(
                            Bokmal to "Til sammen utgjør disse inntektene ".expr() + inntektBruktIAvkortningAvBTFB.format() + " kroner. ",
                            Nynorsk to "Til saman utgjer desse inntektene ".expr() + inntektBruktIAvkortningAvBTFB.format() + " kroner. ",
                        )

                        showIf(justeringsbelopPerArBTFB.equalTo(0) and nettoBTFB.greaterThan(0)) {
                            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto > 0) THEN      INCLUDE ENDIF
                            text(
                                Bokmal to "Dette beløpet er ",
                                Nynorsk to "Dette beløpet er ",
                            )

                            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBInntektBruktiAvkortning > PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBfribelop AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto > 0) THEN      INCLUDE ENDIF
                            showIf(inntektBruktIAvkortningAvBTFB.greaterThan(fribelopBTFB)) {
                                text(
                                    Bokmal to "høyere",
                                    Nynorsk to "høgare",
                                )
                            }

                            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBInntektBruktiAvkortning <= PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBfribelop AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto > 0) THEN      INCLUDE ENDIF
                            showIf(inntektBruktIAvkortningAvBTFB.lessThanOrEqual(fribelopBTFB)) {
                                text(
                                    Bokmal to "lavere",
                                    Nynorsk to "lågare",
                                )
                            }

                            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto > 0) THEN      INCLUDE ENDIF
                            textExpr(
                                Bokmal to " enn fribeløpsgrensen på ".expr() + fribelopBTFB.format() + " kroner. Derfor er barnetillegget ",
                                Nynorsk to " enn fribeløpet på ".expr() + fribelopBTFB.format() + " kroner. Derfor er barnetillegget ",
                            )

                            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto > 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0) THEN      INCLUDE ENDIF
                            showIf(innvilgetBTFB and innvilgetBTSB and nettoBTSB.greaterThan(0)) {
                                textExpr(
                                    Bokmal to "for ".expr() + barnetEllerBarnaFelles + " som bor med begge sine foreldre ",
                                    Nynorsk to "for ".expr() + barnetEllerBarnaFelles + " som bur saman med begge sine foreldra ",
                                )
                            }

                            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBInntektBruktiAvkortning <= PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBfribelop AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto > 0) THEN      INCLUDE ENDIF
                            showIf(inntektBruktIAvkortningAvBTFB.lessThanOrEqual(fribelopBTFB)) {
                                text(
                                    Bokmal to "ikke lenger ",
                                    Nynorsk to "ikkje lenger ",
                                )
                            }

                            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto > 0) THEN      INCLUDE ENDIF
                            text(
                                Bokmal to "redusert. ",
                                Nynorsk to "redusert. ",
                            )
                        }


                        //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr <> 0) THEN      INCLUDE ENDIF
                        showIf(justeringsbelopPerArBTFB.notEqualTo(0)) {
                            text(
                                Bokmal to "Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget",
                                Nynorsk to "Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget",
                            )
                            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr <> 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) THEN      INCLUDE ENDIF
                            showIf(innvilgetBTSB and nettoBTSB.greaterThan(0)) {
                                textExpr(
                                    Bokmal to " for ".expr() + barnetEllerBarnaFelles + " som bor med begge sine foreldre",
                                    Nynorsk to " for ".expr() + barnetEllerBarnaFelles + " som bur saman med begge foreldra sine",
                                )
                            }

                            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr <> 0) THEN      INCLUDE ENDIF
                            text(
                                Bokmal to ". ",
                                Nynorsk to ". ",
                            )

                            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr <> 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0) THEN      INCLUDE ENDIF
                            showIf(nettoBTFB.equalTo(0)) {
                                text(
                                    Bokmal to "Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året.",
                                    Nynorsk to "Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året.",
                                )
                            }
                        }
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) THEN      INCLUDE ENDIF
                showIf(innvilgetBTSB and innvilgetBTFB) {
                    //[TBU4008_NN, TBU4008]

                    paragraph {
                        textExpr (
                            Bokmal to "Barnetillegget for ".expr() + barnetEllerBarnaSaerkull + " som ikke bor sammen med begge foreldrene er beregnet ut fra inntekten din på " + inntektBruktIAvkortningAvBTSB.format() + " kroner. ",
                            Nynorsk to "Barnetillegget for ".expr() + barnetEllerBarnaSaerkull + " som ikkje bur saman med begge foreldra er berekna ut frå inntekta di på " + inntektBruktIAvkortningAvBTSB.format() + " kroner. ",
                        )

                        //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) THEN      INCLUDE ENDIF
                        showIf(justeringsbelopPerArBTSB.equalTo(0) and nettoBTSB.greaterThan(0)) {
                            text (
                                Bokmal to "Dette er ",
                                Nynorsk to "Dette er ",
                            )
                            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning > PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBfribelop AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) THEN      INCLUDE ENDIF
                            showIf(inntektBruktIAvkortningAvBTSB.greaterThan(fribelopBTSB)) {
                                text (
                                    Bokmal to "høyere",
                                    Nynorsk to "høgare",
                                )
                            }

                            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning <= PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBfribelop AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) THEN      INCLUDE ENDIF
                            showIf(inntektBruktIAvkortningAvBTSB.lessThanOrEqual(fribelopBTSB)) {
                                text (
                                    Bokmal to "lavere",
                                    Nynorsk to "lågere",
                                )
                            }

                            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) THEN      INCLUDE ENDIF
                            textExpr (
                                Bokmal to " enn fribeløpsgrensen på ".expr() + fribelopBTSB.format() + " kroner. Dette barnetillegget er derfor ",
                                Nynorsk to " enn fribeløpet på ".expr() + fribelopBTSB.format() + " kroner. Derfor er barnetillegget ",
                            )

                            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning <= PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBfribelop AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) THEN      INCLUDE ENDIF
                            showIf(inntektBruktIAvkortningAvBTSB.lessThanOrEqual(fribelopBTSB)) {
                                text (
                                    Bokmal to "ikke lenger ",
                                    Nynorsk to "ikkje lenger ",
                                )
                            }

                            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB > PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB > PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) THEN      INCLUDE ENDIF
                            showIf(
                                gammeltBelopBTFB.greaterThan(nyttBelopBTFB)
                                        and gammeltBelopBTSB.greaterThan(nyttBelopBTSB)
                            ) {
                                text (
                                    Bokmal to "også ",
                                    Nynorsk to "også ",
                                )
                            }

                            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) THEN      INCLUDE ENDIF
                            text (
                                Bokmal to "redusert. ",
                                Nynorsk to "redusert. ",
                            )
                        }

                        //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr <> 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) THEN      INCLUDE ENDIF
                        showIf(
                            justeringsbelopPerArBTSB.notEqualTo(0)
                                    and nettoBTSB.greaterThan(0)
                        ) {
                            textExpr (
                                Bokmal to "Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget for ".expr() + barnetEllerBarnaSaerkull + " som ikke bor sammen med begge foreldrene. ",
                                Nynorsk to "Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget for ".expr() + barnetEllerBarnaSaerkull + " som ikkje bur saman med begge foreldra. ",
                            )
                        }

                        //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr <> 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0) THEN      INCLUDE ENDIF
                        showIf(justeringsbelopPerArBTSB.notEqualTo(0) and nettoBTSB.equalTo(0)) {
                            text (
                                Bokmal to "Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året.",
                                Nynorsk to "Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året.",
                            )
                        }
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false) THEN      INCLUDE ENDIF
                showIf(innvilgetBTSB and not(innvilgetBTFB)) {
                    //[TBU4009_NN, TBU4009]

                    paragraph {
                        textExpr (
                            Bokmal to "Vi har beregnet barnetillegget på nytt ut fra inntekten din på ".expr() + inntektBruktIAvkortningAvBTSB.format() + " kroner. ",
                            Nynorsk to "Vi har berekna barnetillegget på nytt ut frå inntekta di på ".expr() + inntektBruktIAvkortningAvBTSB.format() + " kroner. ",
                        )

                        //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) THEN      INCLUDE ENDIF
                        showIf(justeringsbelopPerArBTSB.equalTo(0) and nettoBTSB.greaterThan(0)) {
                            text (
                                Bokmal to "Dette er ",
                                Nynorsk to "Dette beløpet er ",
                            )
                        }

                        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning > PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBfribelop AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) THEN      INCLUDE ENDIF
                        showIf(
                            inntektBruktIAvkortningAvBTSB.greaterThan(fribelopBTSB)
                                    and justeringsbelopPerArBTSB.equalTo(0)
                                    and nettoBTSB.greaterThan(0)
                        ) {
                            text (
                                Bokmal to "høyere",
                                Nynorsk to "høgare",
                            )
                        }

                        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning <= PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBfribelop AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) THEN      INCLUDE ENDIF
                        showIf(
                            inntektBruktIAvkortningAvBTSB.lessThanOrEqual(fribelopBTSB)
                                    and justeringsbelopPerArBTSB.equalTo(0)
                                    and nettoBTSB.greaterThan(0)
                        ) {
                            text (
                                Bokmal to "lavere",
                                Nynorsk to "lågare",
                            )
                        }

                        //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) THEN      INCLUDE ENDIF
                        showIf(
                            justeringsbelopPerArBTSB.equalTo(0)
                                    and nettoBTSB.greaterThan(0)
                        ) {
                            textExpr (
                                Bokmal to " enn fribeløpsgrensen på ".expr() + fribelopBTSB.format() + " kroner. Barnetillegget er derfor ",
                                Nynorsk to " enn fribeløpet på ".expr() + fribelopBTSB.format() + " kroner. Derfor er barnetillegget ",
                            )
                        }

                        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning <= PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBfribelop AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) THEN      INCLUDE ENDIF
                        showIf(
                            inntektBruktIAvkortningAvBTSB.lessThanOrEqual(fribelopBTSB)
                                    and justeringsbelopPerArBTSB.equalTo(0)
                                    and nettoBTSB.greaterThan(0)
                        ) {
                            text (
                                Bokmal to "ikke lenger ",
                                Nynorsk to "ikkje lenger ",
                            )
                        }

                        //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) THEN      INCLUDE ENDIF
                        showIf(
                            justeringsbelopPerArBTSB.equalTo(0)
                                    and nettoBTSB.greaterThan(0)
                        ) {
                            text (
                                Bokmal to "redusert. ",
                                Nynorsk to "redusert. ",
                            )
                        }

                        //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr <> 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) THEN      INCLUDE ENDIF
                        showIf(
                            justeringsbelopPerArBTSB.notEqualTo(0)
                                    and nettoBTSB.greaterThan(0)
                        ) {
                            text (
                                Bokmal to "Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget. ",
                                Nynorsk to "Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget. ",
                            )
                        }

                        //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr <> 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0) THEN      INCLUDE ENDIF
                        showIf(
                            justeringsbelopPerArBTSB.notEqualTo(0)
                                    and nettoBTSB.equalTo(0)
                        ) {
                            text (
                                Bokmal to "Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året.",
                                Nynorsk to "Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året.",
                            )
                        }
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0) THEN      INCLUDE ENDIF
                showIf(
                    nettoBTFB.equalTo(0)
                            and innvilgetBTFB
                            and justeringsbelopPerArBTFB.equalTo(0)
                ) {
                    //[TBU4010_NN, TBU4010]

                    paragraph {
                        text (
                            Bokmal to "Barnetillegget ",
                            Nynorsk to "Barnetillegget ",
                        )

                        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) THEN      INCLUDE ENDIF
                        showIf(innvilgetBTSB and innvilgetBTFB) {
                            textExpr (
                                Bokmal to "for ".expr() + barnetEllerBarnaFelles + " som bor med begge sine foreldre ",
                                Nynorsk to "for ".expr() + barnetEllerBarnaFelles + " som bur saman med begge forelda sine ",
                            )
                        }
                        textExpr (
                            Bokmal to "blir ikke utbetalt fordi den samlede inntekten til deg og din ".expr() + epsTypeBokmal + " er høyere enn " + avkortningInntektstakBTFB.format() + " kroner.",
                            Nynorsk to "blir ikkje utbetalt fordi den samla inntekta til deg og ".expr() + epsTypeNynorsk + " din er høgare enn " + avkortningInntektstakBTFB.format() + " kroner.",
                        )
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0) THEN      INCLUDE ENDIF
                showIf(nettoBTSB.equalTo(0) and innvilgetBTSB and justeringsbelopPerArBTSB.equalTo(0)) {
                    //[TBU4011_NN, TBU4011]
                    paragraph {
                        text (
                            Bokmal to "Barnetillegget ",
                            Nynorsk to "Barnetillegget ",
                        )

                        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) THEN      INCLUDE ENDIF
                        showIf(innvilgetBTSB and innvilgetBTFB) {
                            textExpr (
                                Bokmal to "for ".expr() + barnetEllerBarnaSaerkull + " som ikke bor sammen med begge foreldrene ",
                                Nynorsk to "for ".expr() + barnetEllerBarnaSaerkull + " som ikkje bur saman med begge foreldra ",
                            )
                        }
                        textExpr (
                            Bokmal to "blir ikke utbetalt fordi inntekten din er høyere enn ".expr() + avkortningInntektstakBTSB.format() + " kroner.",
                            Nynorsk to "blir ikkje utbetalt fordi inntekta di er høgare enn ".expr() + avkortningInntektstakBTSB.format() + " kroner.",
                        )
                    }
                }

                //IF( FF_CheckIfFirstDayAndMonthOfYear(PE_VedtaksData_VirkningFOM) = false AND ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert= true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert= true ) ) THEN      INCLUDE ENDIF
                showIf(
                    not(virkningFomErForsteDagIAaret)
                            and (
                            periodisertInntektBTFB
                                    or periodisertInntektBTSB
                                    or periodisertFribelopBTFB
                                    or periodisertFribelopBTSB
                            )
                ) {
                    //[TBU4012]
                    paragraph {
                        text (
                            Bokmal to "Fordi du ikke har barnetillegg ",
                            Nynorsk to "Fordi du ikkje har barnetillegg ",
                        )

                        showIf(innvilgetBTFB and innvilgetBTSB) {
                            showIf(
                                (periodisertInntektBTFB
                                        and not(periodisertInntektBTSB)
                                        ) or periodisertFribelopBTFB and not(periodisertFribelopBTSB)
                            ) {
                                textExpr (
                                    Bokmal to "for ".expr() + barnetEllerBarnaFelles + " som bor med begge sine foreldre ",
                                    Nynorsk to "for ".expr() + barnetEllerBarnaFelles + " som bur saman med begge foreldra sine ",

                                    )
                            }

                            //IF( ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert= true ) OR ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert= true ) AND ( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true ) )THEN   INCLUDE ENDIF
                            showIf(
                                (
                                        not(periodisertInntektBTFB)
                                                and periodisertInntektBTSB)
                                        or not(periodisertFribelopBTFB)
                                        and periodisertFribelopBTSB
                            ){
                                textExpr (
                                    Bokmal to "for ".expr() + barnetEllerBarnaSaerkull + " som ikke bor sammen med begge foreldrene ",
                                    Nynorsk to "for ".expr() + barnetEllerBarnaSaerkull + " som ikkje bur saman med begge foreldra ",
                                )
                            }
                        }
                        //IF( ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = true AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert= false ) OR ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert= false ) AND ( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true ) ) THEN      INCLUDE ENDIF

                        text (
                            Bokmal to "hele året er ",
                            Nynorsk to "heile året, er ",
                        )

                        //IF(( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert= true )) THEN   INCLUDE ENDIF
                        showIf((periodisertInntektBTFB or periodisertInntektBTSB)) {
                            text (
                                Bokmal to "inntektene ",
                                Nynorsk to "inntektene ",
                            )
                        }

                        showIf(periodisertFribelopBTFB or periodisertFribelopBTSB) {
                            //IF( ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert= true ) AND ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert= true ) )THEN   INCLUDE ENDIF
                            showIf(periodisertInntektBTFB or periodisertInntektBTSB) {
                                text (
                                    Bokmal to "og ",
                                    Nynorsk to "og ",
                                )
                            }

                            //IF( ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert= true ) )THEN   INCLUDE ENDIF
                            text (
                                Bokmal to "fribeløpet ",
                                Nynorsk to "fribeløpet ",
                            )

                        }

                        text (
                            Bokmal to "justert slik at ",
                            Nynorsk to "justert slik at ",
                        )

                        //IF( (PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = false  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert = false) AND (PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true OR  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert = true) ) THEN      INCLUDE ENDIF
                        showIf(
                            not(periodisertInntektBTFB)
                                    and not(periodisertInntektBTSB)
                                    and (periodisertFribelopBTFB or periodisertFribelopBTSB)
                        ) {
                            text (
                                Bokmal to "det",
                                Nynorsk to "det",
                            )
                        }.orShow{
                            text(
                                Bokmal to "de",
                                Nynorsk to "dei",
                            )
                        }
                        text (
                            Bokmal to " kun gjelder for den perioden du mottar barnetillegg. ",
                            Nynorsk to " berre gjeld for den perioden du får barnetillegg. ",
                        )

                        //PE_Vedtaksdata_Kravhode_KravArsakType = "sivilstandsendring"
                        showIf(kravarsaksType.equalTo("sivilstandsendring")) {
                            text (
                                Bokmal to "Fordi sivilstanden din har endret seg er ",
                                Nynorsk to "Fordi sivilstanden din har endra seg, er ",
                            )
                        }

                        //IF( (PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert= true) AND PE_Vedtaksdata_Kravhode_KravArsakType = "sivilstandsendring" ) THEN   INCLUDE ENDIF
                        showIf((periodisertInntektBTFB or periodisertInntektBTSB) and kravarsaksType.equalTo("sivilstandsendring")) {
                            text (
                                Bokmal to "inntektene ",
                                Nynorsk to "inntektene ",
                            )
                        }

                        //IF( ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert= true ) AND ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert= true ) AND ( PE_Vedtaksdata_Kravhode_KravArsakType = "sivilstandsendring" ) )THEN   INCLUDE ENDIF
                        showIf(
                            (periodisertInntektBTFB or periodisertInntektBTSB)
                                    and (periodisertFribelopBTFB or periodisertFribelopBTSB)
                                    and kravarsaksType.equalTo("sivilstandsendring")
                        ) {
                            text (
                                Bokmal to "og ",
                                Nynorsk to "og ",
                            )
                        }

                        //IF( (PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert= true) AND PE_Vedtaksdata_Kravhode_KravArsakType = "sivilstandsendring"  )THEN   INCLUDE ENDIF
                        showIf(
                            (periodisertFribelopBTFB or periodisertFribelopBTSB)
                                    and kravarsaksType.equalTo("sivilstandsendring")
                        ) {
                            text (
                                Bokmal to "fribeløpet ",
                                Nynorsk to "fribeløpet ",
                            )
                        }

                        //PE_Vedtaksdata_Kravhode_KravArsakType = "sivilstandsendring"
                        showIf(kravarsaksType.equalTo("sivilstandsendring")) {
                            text (
                                Bokmal to "justert slik at de kun gjelder for den framtidige perioden du mottar barnetillegg. ",
                                Nynorsk to "justert slik at dei berre gjeld for den framtidige perioden du får barnetillegg. ",
                            )
                        }
                    }
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense < PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak) THEN      INCLUDE ENDIF
            showIf(avkortningInntektsgrense.lessThan(avkortningInntektstak)) {
                includePhrase(TBU1133_Generated)
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false) THEN      INCLUDE ENDIF
            showIf(
                not(innvilgetBTSB)
                        and not(innvilgetBTFB)
                        and not(gjenlevendetilleggInnvilget)
                        and not(ektefelletilleggInnvilget)
            ) {
                //[TBU2263NN, TBU2263]

                paragraph {
                    text (
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14 og 22-12.",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14 og 22-12.",
                    )
                }
            }

            //IF(  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT  ) THEN      INCLUDE ENDIF
            showIf(
                (innvilgetBTSB or innvilgetBTFB)
                        and not(gjenlevendetilleggInnvilget)
                        and not(ektefelletilleggInnvilget)
                        and gammeltBelop.notEqualTo(nyttBelop)
            ) {
                //[TBU2264NN, TBU2264]

                paragraph {
                    text (
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14 til 12-16 og 22-12",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14 til 12-16 og 22-12",
                    )

                    //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                    showIf(barnetilleggRegelverkType.equalTo("overgangsregler_2016")) {
                        text (
                            Bokmal to " og forskrift om overgangsregler for barnetillegg i uføretrygden",
                            Nynorsk to " og forskrift om overgangsregler for barnetillegg i uføretrygda",
                        )
                    }
                    text (
                        Bokmal to ".",
                        Nynorsk to ".",
                    )
                }
            }

            //IF(  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true  ) THEN      INCLUDE ENDIF
            showIf(
                (innvilgetBTSB or innvilgetBTFB)
                        and not(gjenlevendetilleggInnvilget)
                        and ektefelletilleggInnvilget
            ){
                //[TBU2265NN, TBU2265]

                paragraph {
                    text (
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14 til 12-16 og 22-12 og overgangsforskriften §8",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14 til 12-16 og 22-12 og overgangsforskrifta § 8",
                    )

                    //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                    showIf(barnetilleggRegelverkType.equalTo("overgangsregler_2016")) {
                        text (
                            Bokmal to " og forskrift om overgangsregler for barnetillegg i uføretrygden",
                            Nynorsk to " og forskrift om overgangsregler for barnetillegg i uføretrygda",
                        )
                    }
                    text (
                        Bokmal to ".",
                        Nynorsk to ".",
                    )
                }
            }

            //IF(  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false  ) THEN      INCLUDE ENDIF
            showIf((innvilgetBTSB or innvilgetBTFB) and gjenlevendetilleggInnvilget and not(ektefelletilleggInnvilget)) {
                //[TBU2266NN, TBU2266]

                paragraph {
                    text (
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14 til 12-16, 12-18 og 22-12",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14 til 12-16, 12-18 og 22-12",
                    )

                    //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                    showIf(
                        barnetilleggRegelverkType
                            .equalTo("overgangsregler_2016")
                    ){
                        text (
                            Bokmal to " og forskrift om overgangsregler for barnetillegg i uføretrygden",
                            Nynorsk to " og forskrift om overgangsreglar for barnetillegg i uføretrygda",
                        )
                    }
                    text (
                        Bokmal to ".",
                        Nynorsk to ".",
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false) THEN      INCLUDE ENDIF
            showIf(not(innvilgetBTSB) and not(innvilgetBTFB) and gjenlevendetilleggInnvilget and not(ektefelletilleggInnvilget)) {
                //[TBU2267NN, TBU2267]

                paragraph {
                    text (
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14, 12- 18 og 22-12.",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14, 12-18 og 22-12.",
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true) THEN      INCLUDE ENDIF
            showIf(not(innvilgetBTSB) and not(innvilgetBTFB) and not(gjenlevendetilleggInnvilget) and ektefelletilleggInnvilget) {
                //[TBU2268NN, TBU2268]

                paragraph {
                    text (
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14 og 22-12 og overgangsforskriften § 8.",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14 og 22-12 og overgangsforskrifta § 8.",
                    )
                }
            }

            //IF((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT = PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT)  THEN      INCLUDE ENDIF
            showIf(
                (innvilgetBTSB or innvilgetBTFB)
                        and not(gjenlevendetilleggInnvilget)
                        and not(ektefelletilleggInnvilget)
                        and gammeltBelop.equalTo(nyttBelop)
            ) {
                //[TBU4014_NN, TBU4014]

                paragraph {
                    text (
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-15, 12-16 og 22-12",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-15, 12-16 og 22-12",
                    )

                    //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                    showIf(
                        barnetilleggRegelverkType
                            .equalTo("overgangsregler_2016")
                    ){
                        text (
                            Bokmal to " og forskrift om overgangsregler for barnetillegg i uføretrygden",
                            Nynorsk to " og forskrift om overgangsreglar for barnetillegg i uføretrygda",
                        )
                    }
                    text (
                        Bokmal to ".",
                        Nynorsk to ".",
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT > 0) THEN      INCLUDE ENDIF
            showIf(gammeltBelop.notEqualTo(nyttBelop) and nyttBelop.greaterThan(0)) {
                //[TBU4015_NN, TBU4015]

                title1 {
                    text (
                        Bokmal to "Hva får du i uføretrygd framover?",
                        Nynorsk to "Kva får du i uføretrygd framover?",
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak > PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense) THEN      INCLUDE ENDIF
                showIf(utbetalingsgrad.lessThan(uforegrad) and avkortningInntektstak.greaterThan(avkortningInntektsgrense)) {
                    //[TBU4044_NN, TBU4044]

                    paragraph {
                        textExpr (
                            Bokmal to "Ut fra den årlige inntekten din vil uføretrygden utgjøre ".expr() + nettoRestAr.format() + " kroner. ",
                            Nynorsk to "Ut frå den årlege inntekta di vil uføretrygda utgjere ".expr() + nettoRestAr.format() + " kroner. ",
                        )

                        //IF(FF_CheckIfFirstDayAndMonthOfYear(PE_VedtaksData_VirkningFOM) = false) THEN      INCLUDE ENDIF
                        showIf(not(virkningFomErForsteDagIAaret)) {
                            textExpr (
                                Bokmal to "Hittil i år har du fått utbetalt ".expr() + nettoAkk.format() + " kroner. ",
                                Nynorsk to "Hittil i år har du fått utbetalt ".expr() + nettoAkk.format() + " kroner. ",
                            )
                        }
                        textExpr (
                            Bokmal to "Du har derfor rett til en utbetaling av uføretrygd på ".expr() + uforetrygdNetto.format() + " kroner per måned for resten av året.",
                            Nynorsk to "Du har derfor rett til ei utbetaling av uføretrygd på ".expr() + uforetrygdNetto.format() + " kroner per månad for resten av året.",
                        )
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt > PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense) THEN      INCLUDE ENDIF
                showIf(forventetInntekt.greaterThan(avkortningInntektsgrense)) {
                    //[TBU4045_NN, TBU4045]

                    paragraph {
                        textExpr (
                            Bokmal to "Selv om uføretrygden din er redusert på grunn av inntekt beholder du likevel uføregraden din på ".expr() + uforegrad
                                .format() + " prosent. Du får utbetalt hele uføretrygden igjen dersom du tjener mindre enn inntektsgrensen din.",
                            Nynorsk to "Sjølv om uføretrygda di er redusert på grunn av inntekt beheld du likevel uføregraden din på ".expr() + uforegrad
                                .format() + " prosent. Du får utbetalt heile uføretrygda att dersom du tener mindre enn inntektsgrensa di.",
                        )
                    }
                }
            }

            //IF( (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB > 0) OR (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB > 0) ) THEN   INCLUDE ENDIF
            showIf(
                (gammeltBelopBTFB.notEqualTo(nyttBelopBTFB) and nyttBelopBTFB.greaterThan(0))
                        or (gammeltBelopBTSB.notEqualTo(nyttBelopBTSB) and nyttBelopBTSB.greaterThan(0))
            ) {
                //[TBU4046_NN, TBU4046]

                title1 {
                    text (
                        Bokmal to "Hva får du i barnetillegg framover?",
                        Nynorsk to "Kva får du i barnetillegg framover?",
                    )
                }

                //IF( (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB) AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB <> 0 ) THEN   INCLUDE ENDIF
                showIf(gammeltBelopBTFB.notEqualTo(nyttBelopBTFB) and nyttBelopBTFB.notEqualTo(0)) {
                    //[TBU4047_NN, TBU4047]

                    paragraph {
                        textExpr (
                            Bokmal to "Ut fra den samlede inntekten til deg og din ".expr() + epsTypeBokmal + " er barnetillegget vurdert på nytt. Barnetillegget er redusert for hele året ut fra den inntekten som overstiger fribeløpet. Den årlige reduksjonen av barnetillegget er " + arligAvkortningsbelopBTFB
                                .format() + " kroner.",
                            Nynorsk to "Ut frå dei samla inntektene til deg og ".expr() + epsTypeNynorsk + " din er barnetillegget blitt vurdert på nytt. Barnetillegget er redusert for heile året ut frå inntekta som overstig fribeløpet. Den årlege reduksjonen av barnetillegget er " + arligAvkortningsbelopBTFB
                                .format() + " kroner.",
                        )

                        //IF(( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr <> 0 )) THEN   INCLUDE ENDIF
                        showIf(justeringsbelopPerArBTFB.notEqualTo(0)) {
                            text (
                                Bokmal to " Vi har tatt hensyn til hvordan barnetillegget eventuelt har vært redusert i starten av året, og vi har derfor ",
                                Nynorsk to " Vi har teke omsyn til korleis barnetillegget eventuelt har vore redusert i starten av året, og vi har derfor ",
                            )
                        }

                        //IF(( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr <> 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr > 0 )) THEN   INCLUDE ENDIF
                        showIf(justeringsbelopPerArBTFB.greaterThan(0)) {
                            text (
                                Bokmal to "lagt til",
                                Nynorsk to "lagt til",
                            )
                        }

                        //IF(( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr <> 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr < 0 )) THEN   INCLUDE ENDIF
                        showIf(justeringsbelopPerArBTFB.lessThan(0)) {
                            text (
                                Bokmal to "trukket fra",
                                Nynorsk to "trekt frå",
                            )
                        }

                        //IF(( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr <> 0 )) THEN   INCLUDE ENDIF
                        showIf(justeringsbelopPerArBTFB.notEqualTo(0)) {
                            textExpr (
                                Bokmal to " ".expr() + justeringsbelopPerArUtenMinusBTFB.format() + " kroner i det vi reduserer barnetillegget med for resten av året.",
                                Nynorsk to " ".expr() + justeringsbelopPerArUtenMinusBTFB.format() + " kroner i det vi har redusert barnetillegget med for resten av året.",
                            )
                        }
                        textExpr (
                            Bokmal to " Du har derfor rett til en utbetaling av barnetillegg på ".expr() + nettoBTFB.format() + " kroner per måned for resten av året. ",
                            Nynorsk to " Du har derfor rett til ei utbetaling av barnetillegg på ".expr() + nettoBTFB.format() + " kroner per månad for resten av året.",
                        )
                    }
                }

                //IF( (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB) AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB <> 0 ) THEN   INCLUDE ENDIF
                showIf(gammeltBelopBTSB.notEqualTo(nyttBelopBTSB) and nyttBelopBTSB.notEqualTo(0)) {
                    //[TBU4048_NN, TBU4048]

                    paragraph {
                        textExpr (
                            Bokmal to "Ut fra den samlede inntekten din er barnetillegget vurdert på nytt. Barnetillegget er redusert for hele året ut fra den inntekten som overstiger fribeløpet. Den årlige reduksjonen av barnetillegget er ".expr() + arligAvkortningsbelopBTSB.format() + " kroner.",
                            Nynorsk to "Ut frå den samla inntekta di er barnetillegget blitt vurdert på nytt. Barnetillegget er redusert for heile året ut frå inntekta som overstig fribeløpet. Den årlege reduksjonen av barnetillegget er ".expr() + arligAvkortningsbelopBTSB.format() + " kroner.",
                        )

                        //IF(( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr <> 0 )) THEN   INCLUDE ENDIF
                        showIf(justeringsbelopPerArBTSB.notEqualTo(0)) {
                            text (
                                Bokmal to " Vi har tatt hensyn til hvordan barnetillegget eventuelt har vært redusert i starten av året, og vi har derfor ",
                                Nynorsk to " Vi har teke omsyn til korleis barnetillegget eventuelt har vore redusert i starten av året, og vi har derfor ",
                            )
                        }

                        //IF(( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr > 0 )) THEN   INCLUDE ENDIF
                        showIf(justeringsbelopPerArBTSB.greaterThan(0)) {
                            text (
                                Bokmal to "lagt til",
                                Nynorsk to "lagt til",
                            )
                        }

                        //IF(( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr < 0 )) THEN   INCLUDE ENDIF
                        showIf(justeringsbelopPerArBTSB.lessThan(0)) {
                            text (
                                Bokmal to "trukket fra",
                                Nynorsk to "trekt frå",
                            )
                        }

                        //IF(( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr <> 0 )) THEN   INCLUDE ENDIF
                        showIf(justeringsbelopPerArBTSB.notEqualTo(0)) {
                            textExpr (
                                Bokmal to " ".expr() + justeringsbelopPerArUtenMinusBTSB.format() + " kroner i det vi reduserer barnetillegget med for resten av året.",
                                Nynorsk to " ".expr() + justeringsbelopPerArUtenMinusBTSB.format() + " kroner i det vi reduserte barnetillegget med for resten av året.",
                            )
                        }
                        textExpr (
                            Bokmal to " Du har derfor rett til en utbetaling av barnetillegg på ".expr() + nettoBTSB.format() + " kroner per måned for resten av året.",
                            Nynorsk to " Du har derfor rett til ei utbetaling av barnetillegg på ".expr() + nettoBTSB.format() + " kroner per månad for resten av året.",
                        )
                    }
                }

                //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true) OR (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) ) THEN   INCLUDE ENDIF
                showIf(
                    (nettoBTSB.equalTo(0) and innvilgetBTSB)
                            or (nettoBTFB.equalTo(0) and innvilgetBTFB)
                ) {
                    //[TBU4049_NN, TBU4049]

                    paragraph {
                        textExpr (
                            Bokmal to "Du får ikke utbetalt barnetillegget for ".expr() + barnetBarnaFellesSerkull + " som ",
                            Nynorsk to "Du får ikkje utbetalt barnetillegget for ".expr() + barnetBarnaFellesSerkull + " som ",
                        )

                        //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) AND (   PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false   OR   (     PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true     AND     PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto <> 0) ) )THEN   INCLUDE ENDIF
                        showIf(
                            nettoBTFB.equalTo(0)
                                    and innvilgetBTFB
                                    and (
                                    not(innvilgetBTSB)
                                            or (innvilgetBTSB and nettoBTSB.notEqualTo(0))
                                    )
                        ) {
                            text (
                                Bokmal to "bor med begge sine foreldre",
                                Nynorsk to "bur med begge foreldra",
                            )
                        }

                        //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true) AND (   PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false   OR   (     PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true     AND     PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0) ) )THEN   INCLUDE ENDIF
                        showIf(
                            nettoBTSB.equalTo(0)
                                    and innvilgetBTSB
                                    and (
                                    not(innvilgetBTFB)
                                            or (innvilgetBTFB and nettoBTFB.notEqualTo(0))
                                    )
                        ) {
                            text (
                                Bokmal to "ikke bor sammen med begge foreldrene",
                                Nynorsk to "ikkje bur saman med begge foreldra",
                            )
                        }
                        text (
                            Bokmal to ".",
                            Nynorsk to ".",
                        )
                    }
                }
            }

            //PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true
            showIf(gjenlevendetilleggInnvilget) {
                includePhrase(TBU1214_Generated)
                //[TBU2272NN, TBU2272]

                paragraph {
                    text(
                        Bokmal to "Du mottar gjenlevendetillegg i uføretrygden din. Tjener du mer enn inntektsgrensen din, reduserer vi gjenlevendetillegget ditt med samme prosent som vi reduserer uføretrygden din med.",
                        Nynorsk to "Du får attlevandetillegg i uføretrygda di. Tener du meir enn inntektsgrensa di, reduserer vi attlevandetillegget ditt med same prosenten som vi reduserer uføretrygda di med.",
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopRedusert = true AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true) THEN      INCLUDE ENDIF
                showIf(beregningBelopRedusert and gjenlevendetilleggInnvilget) {
                    //[TBU2273NN, TBU2273]

                    paragraph {
                        text(
                            Bokmal to "Gjenlevendetillegget er redusert ut fra den innmeldte inntekten.",
                            Nynorsk to "Attlevandetillegget er redusert ut frå den innmelde inntekta.",
                        )
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopOkt = true AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true) THEN      INCLUDE ENDIF
                showIf(beregningBelopOkt and gjenlevendetilleggInnvilget) {
                    //[TBU2274NN, TBU2274]

                    paragraph {
                        text(
                            Bokmal to "Gjenlevendetillegget er økt ut fra den innmeldte inntekten.",
                            Nynorsk to "Attlevandetillegget er auka ut frå den innmelde inntekta.",
                        )
                    }
                }
                includePhrase(TBU1133_Generated)
            }

            //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true
            showIf(ektefelletilleggInnvilget) {
                includePhrase(TBU2275_Generated)
            }

            //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true
            showIf(ektefelletilleggInnvilget) {
                //[TBU2276NN, TBU2276]

                paragraph {
                    text(
                        Bokmal to "Du mottar ektefelletillegg i uføretrygden din. Dette tillegget blir ikke endret som følge av inntektsendringer.",
                        Nynorsk to "Du får ektefelletillegg i uføretrygda di. Dette tillegget blir ikkje endra som følgje av inntektsendringar.",
                    )
                }
            }
            //[TBU4022_NN, TBU4022]

            title1 {
                text (
                    Bokmal to "Du må melde fra om endringer i inntekt",
                    Nynorsk to "Du må melde frå om endringar i inntekt",
                )
            }
            includePhrase(TBU2278_Generated(pe))

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt < PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak > PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense) THEN      INCLUDE ENDIF
            showIf(forventetInntekt.lessThan(avkortningInntektstak) and avkortningInntektstak.greaterThan(avkortningInntektsgrense)) {
                includePhrase(TBU2279_Generated(pe))
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt < PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak <= PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense) THEN      INCLUDE ENDIF
            showIf(
                forventetInntekt
                    .lessThan(avkortningInntektstak) and avkortningInntektstak
                    .lessThanOrEqual(
                        avkortningInntektsgrense
                    )){
                includePhrase(TBU3740_Generated(pe))
            }
            includePhrase(TBU2280_Generated(pe))
            //[TBU2282NN, TBU2282]

            title1 {
                text (
                    Bokmal to "Inntekter som ikke skal føre til reduksjon av uføretrygden",
                    Nynorsk to "Inntekter som ikkje skal føra til reduksjon av uføretrygda",
                )
            }
            paragraph {
                text(
                    Bokmal to "Det kan gjøres unntak for enkelte inntektstyper som ikke skal føre til reduksjon av uføretrygden. Dette kan gjelde følgende:",
                    Nynorsk to "Det kan gjerast unntak for enkelte inntektstypar som ikkje skal føra til reduksjon av uføretrygda. Dette kan gjelda følgjande:",
                )
            }
            paragraph {
                text (
                    Bokmal to "Erstatning for inntektstap ved erstatningsoppgjør etter",
                    Nynorsk to "Erstatning for inntektstap ved erstatningsoppgjer etter",
                )
                list {
                    item {
                        text(
                            Bokmal to "Skadeerstatningsloven § 3-1",
                            Nynorsk to "Skadeerstatningsloven § 3-1",
                        )
                    }
                    item {
                        text(
                            Bokmal to "Yrkesskadeforsikringsloven § 13",
                            Nynorsk to "Yrkesskadeforsikringsloven § 13",
                        )
                    }
                    item {
                        text(
                            Bokmal to "Pasientskadeloven § 4 første ledd",
                            Nynorsk to "Pasientskadeloven § 4 første ledd",
                        )
                    }
                }
                text (
                    Bokmal to "Inntekt fra arbeid eller virksomhet som ble helt avsluttet før du fikk innvilget uføretrygd, for eksempel:",
                    Nynorsk to "Inntekt frå arbeid eller verksemd som vart heilt avslutta før du fekk innvilga uføretrygd, til dømes:",
                )

                list {
                    item {
                        text(
                            Bokmal to "Utbetalte feriepenger for et arbeidsforhold som er avsluttet",
                            Nynorsk to "Utbetalte feriepengar for eit arbeidsforhold som er avslutta",
                        )
                    }
                    item {
                        text(
                            Bokmal to "Inntekter fra salg av produksjonsmidler i forbindelse med opphør av virksomheten",
                            Nynorsk to "Inntekter frå sal av produksjonsmiddel i samband med opphøyr av verksemda",
                        )
                    }
                    item {
                        text(
                            Bokmal to "Produksjonstillegg og andre overføringer til gårdbrukere",
                            Nynorsk to "Produksjonstillegg og andre overføringar til gardbrukarar",
                        )
                    }
                }

                // Når feature toggelen slettes skal resten av if-en bli igjen, også det i orShow
                showIf(FeatureToggles.pl7822EndretInntekt.expr().enabled() and virkningFom.legacyGreaterThanOrEqual(LocalDate.of(2023, 1, 1)) and innvilgetBTFB) {
                    text(
                        Bokmal to "Hva kan holdes utenfor personinntekten til den andre forelderen?",
                        Nynorsk to "Kva kan haldast utanfor personinntekta til den andre forelderen?",
                    )

                    list {
                        item {
                            text(
                                Bokmal to "Erstatningsoppgjør for inntektstap dersom den andre forelderen mottar uføretrygd eller alderspensjon fra Nav",
                                Nynorsk to "Erstatningsoppgjer for inntektstap dersom den andre forelderen mottar uføretrygd eller alderspensjon frå Nav",
                            )
                        }
                    }
                    text(
                        Bokmal to "Dersom vi mottar dokumentasjon fra deg som bekrefter slik inntekt, kan vi gjøre en ny beregning.",
                        Nynorsk to "Dersom vi mottar dokumentasjon frå deg som stadfestar slik inntekt, kan vi gjera ei ny berekning.",
                    )
                }.orShow {
                    text(
                        Bokmal to "Dersom vi mottar dokumentasjon fra deg som bekrefter at du har slik inntekt, kan vi gjøre en ny beregning av uføretrygden din.",
                        Nynorsk to "Dersom vi mottar dokumentasjon frå deg som stadfestar at du har slik inntekt, kan vi gjera ei ny berekning av uføretrygda di.",
                    )
                }
            }

            includePhrase(Ufoeretrygd.MeldeFraOmEndringer)
            includePhrase(Felles.RettTilAAKlage(vedleggDineRettigheterOgPlikterUfoere))
            includePhrase(Felles.RettTilInnsyn(vedleggDineRettigheterOgPlikterUfoere))
            includePhrase(Ufoeretrygd.SjekkUtbetalingene)
            includePhrase(Ufoeretrygd.Skattekort)
            includePhrase(Ufoeretrygd.SkattForDegSomBorIUtlandet(pe.grunnlag_persongrunnlagsliste_personbostedsland().equalTo("nor") or pe.grunnlag_persongrunnlagsliste_personbostedsland().equalTo("")))
            includePhrase(Felles.HarDuSpoersmaal.ufoeretrygd)
        }

        includeAttachmentIfNotNull(vedleggMaanedligUfoeretrygdFoerSkatt, maanedligUfoeretrygdFoerSkatt)
        includeAttachment(vedleggOpplysningerBruktIBeregningUTLegacy, pe, pe.inkluderopplysningerbruktiberegningen())
        includeAttachment(vedleggDineRettigheterOgPlikterUfoere, orienteringOmRettigheterUfoere)
    }
}