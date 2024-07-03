package no.nav.pensjon.brev.maler
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.maler.fraser.generated.*
import no.nav.pensjon.brev.maler.fraser.ufoer.Ufoeretrygd
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VEDTAK
import java.time.LocalDate


@TemplateModelHelpers
object EndretUfoeretrygdPGAInntekt : AutobrevTemplate<EmptyBrevdata> {

    override val kode = Brevkode.AutoBrev.UT_ENDRET_PGA_INNTEKT

    override val template = createTemplate(
        name = kode.name,
        letterDataType = EmptyBrevdata::class,
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - endring av uføretrygd på grunn av inntekt (automatisk)",
            isSensitiv = false,
            distribusjonstype = VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        title {
            text(
                Bokmal to "",
                Nynorsk to "",
            )
        }

        outline {
            val PE_BarnetilleggFelles_JusteringsbelopPerArUtenMinus: Expression<Double> = (-9187.0).expr()
            val PE_BarnetilleggSerkull_JusteringsbelopPerArUtenMinus: Expression<Double> = (-6466.0).expr()
            val PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT: Expression<String> = "<TEXTVARIABEL:2586>".expr()
            val PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_NN_entall: Expression<String> = "<TEXTVARIABEL:130>".expr()
            val PE_UT_Barnet_Barna_Felles: Expression<String> = "<TEXTVARIABEL:900>".expr()
            val PE_UT_Barnet_Barna_Felles_serkull: Expression<String> = "<TEXTVARIABEL:3296>".expr()
            val PE_UT_Barnet_Barna_Serkull: Expression<String> = "<TEXTVARIABEL:1264>".expr()
            val PE_UT_NettoAkk_pluss_NettoRestAr: Expression<Double> = (-6930.0).expr()
            val PE_UT_VirkningFomAr: Expression<String> = "<TEXTVARIABEL:9178>".expr()
            val PE_UT_VirkningFomArMinusEttAr: Expression<String> = "<TEXTVARIABEL:1298>".expr()
            val PE_UT_VirkningstidpunktArMinus1Ar: Expression<Int> = 8533.expr()
            val PE_VedtaksData_VirkningFOM: Expression<LocalDate> = LocalDate.of(2020, 8, 16).expr()
            val antallBeregningsperioder: Expression<Int> = 3807.expr()
            val barnetilleggFelles_BTFBinnvilget: Expression<Boolean> = true.expr()
            val barnetilleggFelles_InntektBruktIAvkortning: Expression<Double> = 8532.0.expr()
            val barnetilleggFelles_avkortningsbeloepPerAar: Expression<Double> = (-7012.0).expr()
            val barnetilleggFelles_belopFratrukketAnnenForeldersInntekt: Expression<Kroner> = Kroner(9303).expr()
            val barnetilleggFelles_brukersInntektTilAvkortning: Expression<Double> = (-4395.0).expr()
            val barnetilleggFelles_fribeloep: Expression<Double> = (-1557.0).expr()
            val barnetilleggFelles_fribelopPeriodisert: Expression<Boolean> = true.expr()
            val barnetilleggFelles_inntektAnnenForelder: Expression<Double> = (-2088.0).expr()
            val barnetilleggFelles_inntektPeriodisert: Expression<Boolean> = true.expr()
            val barnetilleggFelles_inntektstak: Expression<Double> = 4395.0.expr()
            val barnetilleggFelles_justeringsbelopPerAr: Expression<Kroner> = Kroner(8071).expr()
            val barnetilleggFelles_netto: Expression<Double> = (-2061.0).expr()
            val barnetilleggSerkull_avkortingsbelopPerAr: Expression<Double> = (-6905.0).expr()
            val barnetilleggSerkull_fribeloep: Expression<Double> = (-5024.0).expr()
            val barnetilleggSerkull_fribelopPeriodisert: Expression<Boolean> = true.expr()
            val barnetilleggSerkull_inntektBruktIAvkortning: Expression<Double> = (-4399.0).expr()
            val barnetilleggSerkull_inntektPeriodisert: Expression<Boolean> = true.expr()
            val barnetilleggSerkull_inntektstak: Expression<Double> = 8497.0.expr()
            val barnetilleggSerkull_innvilget: Expression<Boolean> = true.expr()
            val barnetilleggSerkull_justeringsbelopPerAr: Expression<Kroner> = Kroner(1667).expr()
            val barnetilleggSerkull_netto: Expression<Double> = (-3073.0).expr()
            val beregningUfore_BelopGammelBTFB: Expression<Kroner> = Kroner(3932).expr()
            val beregningUfore_BelopGammelBTSB: Expression<Kroner> = Kroner(8387).expr()
            val beregningUfore_BelopGammelUT: Expression<Kroner> = Kroner(2423).expr()
            val beregningUfore_BelopNyBTFB: Expression<Kroner> = Kroner(8366).expr()
            val beregningUfore_BelopNyBTSB: Expression<Kroner> = Kroner(1505).expr()
            val beregningUfore_BelopNyUT: Expression<Kroner> = Kroner(5845).expr()
            val beregningUfore_BelopOkt: Expression<Boolean> = true.expr()
            val beregningUfore_BelopRedusert: Expression<Boolean> = true.expr()
            val beregningUfore_BeregningVirkningDatoFom: Expression<LocalDate> = LocalDate.of(2020, 8, 7).expr()
            val beregningUfore_grunnbelop: Expression<Double> = (-7736.0).expr()
            val beregningUfore_total: Expression<Double> = (-7051.0).expr()
            val beregningUfore_totalNetto: Expression<Double> = (-7055.0).expr()
            val beregningUfore_uforegrad: Expression<Int> = 904.expr()
            val ektefelletillegg_ETinnvilget: Expression<Boolean> = true.expr()
            val erFaktaomregnet: Expression<Boolean> = true.expr()
            val fyller67iAar: Expression<Boolean> = true.expr()
            val gjenlevendetillegg_GTinnvilget: Expression<Boolean> = true.expr()
            val kravArsakType: Expression<String> = "<TEXTVARIABEL:3567>".expr()
            val ufoeretrygd_instOppholdType: Expression<String> = "<TEXTVARIABEL:2269>".expr()
            val ufoeretrygd_reduksjonsgrunnlag_barnetilleggRegelverkType: Expression<String> = "<TEXTVARIABEL:3107>".expr()
            val uforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad: Expression<Int> = 7119.expr()
            val uforetrygdOrdiner_Brutto: Expression<Kroner> = Kroner(483).expr()
            val uforetrygdOrdiner_avkortingsbelopPerAr: Expression<Double> = 2480.0.expr()
            val uforetrygdOrdiner_forventetInntekt: Expression<Double> = (-2570.0).expr()
            val uforetrygdOrdiner_inntektsgrense: Expression<Double> = 762.0.expr()
            val uforetrygdOrdiner_inntektstak: Expression<Double> = (-7398.0).expr()
            val uforetrygdOrdiner_netto: Expression<Double> = 2623.0.expr()
            val uforetrygdOrdiner_nettoAkk: Expression<Double> = 3931.0.expr()

            val virkFomErFoersteJanuar = false.expr()  // FUNKSJON_FF_CheckIfFirstDayAndMonthOfYear(beregningUfore_BeregningVirkningDatoFom)
            val personBostedsland = "nor".expr()//FUNKSJON_PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland(1)

            // TODO er det forskjell på denne og vedtaksdata virkfom?
            val beregningVirkFomErFoersteJanuar = false.expr() // beregningUfore_BeregningVirkningDatoFom

            //IF(FF_CheckIfFirstDayAndMonthOfYear(PE_VedtaksData_VirkningFOM) = false) THEN      INCLUDE ENDIF
            showIf(not(virkFomErFoersteJanuar)){
                //[TBU2249NN, TBU2249]

                paragraph {
                    text (
                        Bokmal to "Vi har mottatt nye opplysninger om inntekten",
                        Nynorsk to "Vi har fått nye opplysningar om inntekta",
                    )

                    //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false
                    showIf(not(barnetilleggFelles_BTFBinnvilget)){
                        text (
                            Bokmal to " din",
                            Nynorsk to " di",
                        )
                    }

                    //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
                    showIf(barnetilleggFelles_BTFBinnvilget){
                        textExpr (
                            Bokmal to "til deg eller din ".expr() + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT + ". Inntekten til din " + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT + " har kun betydning for størrelsen på barnetillegget ",
                            Nynorsk to "til deg eller ".expr() + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_NN_entall + " din. Inntekta til " + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_NN_entall + " din har berre betydning for storleiken på barnetillegget ",
                        )
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true) THEN      INCLUDE ENDIF
                    showIf(barnetilleggFelles_BTFBinnvilget and barnetilleggSerkull_innvilget){
                        textExpr (
                            Bokmal to "for ".expr() + PE_UT_Barnet_Barna_Felles + " som bor med begge sine foreldre",
                            Nynorsk to "for ".expr() + PE_UT_Barnet_Barna_Felles + " som bur saman med begge foreldra sine",
                        )
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false) THEN      INCLUDE ENDIF
                    showIf(barnetilleggFelles_BTFBinnvilget and not(barnetilleggSerkull_innvilget)){
                        text (
                            Bokmal to "ditt",
                            Nynorsk to "ditt",
                        )
                    }
                    text (
                        Bokmal to ". Utbetalingen av ",
                        Nynorsk to ". Utbetalinga av ",
                    )

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT) THEN      INCLUDE ENDIF
                    showIf(beregningUfore_BelopGammelUT.notEqualTo(beregningUfore_BelopNyUT)){
                        text (
                            Bokmal to "uføretrygden din ",
                            Nynorsk to "uføretrygda di ",
                        )
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT  AND (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB  OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB)) THEN      INCLUDE ENDIF
                    showIf(beregningUfore_BelopGammelUT.notEqualTo(beregningUfore_BelopNyUT) and (beregningUfore_BelopGammelBTFB.notEqualTo(beregningUfore_BelopNyBTFB) or beregningUfore_BelopGammelBTSB.notEqualTo(beregningUfore_BelopNyBTSB))){
                        text (
                            Bokmal to "og ",
                            Nynorsk to "og ",
                        )
                    }

                    //IF(((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false)  OR (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true))) AND (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB  OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB)  THEN      INCLUDE ENDIF
                    showIf(((barnetilleggSerkull_innvilget and not(barnetilleggFelles_BTFBinnvilget)) or (not(barnetilleggSerkull_innvilget) and barnetilleggFelles_BTFBinnvilget)) and (beregningUfore_BelopGammelBTFB.notEqualTo(beregningUfore_BelopNyBTSB) or beregningUfore_BelopGammelBTSB.notEqualTo(beregningUfore_BelopNyBTSB))){
                        text (
                            Bokmal to "barnetillegget ditt ",
                            Nynorsk to "barnetillegget ditt ",
                        )
                    }

                    //IF((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)) AND (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB  OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB)  THEN      INCLUDE ENDIF
                    showIf(barnetilleggFelles_BTFBinnvilget and barnetilleggSerkull_innvilget and (beregningUfore_BelopGammelBTFB.notEqualTo(beregningUfore_BelopNyBTSB) or beregningUfore_BelopGammelBTSB.notEqualTo(beregningUfore_BelopNyBTSB))){
                        text (
                            Bokmal to "barnetilleggene dine ",
                            Nynorsk to "barnetillegga dine ",
                        )
                    }
                    textExpr (
                        Bokmal to "er derfor endret fra ".expr() + PE_VedtaksData_VirkningFOM.format() + ".",
                        Nynorsk to "er derfor endra frå ".expr() + PE_VedtaksData_VirkningFOM.format() + ".",
                    )
                }
            }

            //Failed to convert with error: Exstream logikk har innhold før if. Tolkes ikke.

            //Integer iYear  iYear = FF_getYear(PE_VedtaksData_VirkningFOM)  IF(  PE_VedtaksData_VirkningFOM = FF_getFirstDayOfYear(iYear) AND (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT)  ) THEN      INCLUDE ENDIF

            //[TBU3403NN, TBU3403]
            showIf(virkFomErFoersteJanuar and beregningUfore_BelopGammelUT.notEqualTo(beregningUfore_BelopNyUT)){
                paragraph {
                    textExpr(
                        Bokmal to "Vi vil bruke en inntekt på ".expr() + uforetrygdOrdiner_forventetInntekt.format() + " kroner når vi reduserer uføretrygden din for " + PE_UT_VirkningFomAr + ". Har du ikke meldt inn ny inntekt for " + PE_UT_VirkningFomAr + ", er inntekten justert opp til dagens verdi.",
                        Nynorsk to "Vi vil bruke ei inntekt på ".expr() + uforetrygdOrdiner_forventetInntekt.format() + "kroner når vi reduserer uføretrygda di for " + PE_UT_VirkningFomAr + ". Har du ikkje meldt inn ny inntekt for " + PE_UT_VirkningFomAr + ", er inntekta justert opp til dagens verdi.",
                    )

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad) THEN      INCLUDE ENDIF
                    showIf(uforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad.notEqualTo(beregningUfore_uforegrad)) {
                        textExpr(
                            Bokmal to "Fikk du innvilget uføretrygd etter januar ".expr() + PE_UT_VirkningFomArMinusEttAr + ", er inntekten justert opp slik at den gjelder for hele " + PE_UT_VirkningFomAr + ".",
                            Nynorsk to "Fekk du innvilga uføretrygd etter januar ".expr() + PE_UT_VirkningFomArMinusEttAr + ", er inntekta også justert opp slik at den gjeld for heile " + PE_UT_VirkningFomAr + ".",
                        )
                    }

                    //PE_UT_VilFylle67iVirkningFomAr = true
                    showIf(fyller67iAar) {
                        textExpr(
                            Bokmal to "Fordi du fyller 67 år i ".expr() + PE_UT_VirkningFomAr + ", er inntekten justert i forhold til antall måneder du mottar uføretrygd.",
                            Nynorsk to "Fordi du fyljer 67 år i ".expr() + PE_UT_VirkningFomAr + ", er inntekta justert ut frå talet på månadar du får uføretrygd.",
                        )
                    }
                }
            }
            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT  AND  (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB  OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB)) AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget= true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false) AND FF_CheckIfFirstDayAndMonthOfYear(PE_VedtaksData_VirkningFOM) = true THEN  INCLUDE ENDIF
            showIf(beregningUfore_BelopGammelUT.notEqualTo(beregningUfore_BelopNyUT) and (beregningUfore_BelopGammelBTFB.notEqualTo(beregningUfore_BelopNyBTFB) or beregningUfore_BelopGammelBTSB.notEqualTo(beregningUfore_BelopNyBTSB)) and barnetilleggSerkull_innvilget and not(barnetilleggFelles_BTFBinnvilget) and virkFomErFoersteJanuar){
                //[TBU4016_NN, TBU4016]

                paragraph {
                    textExpr (
                        Bokmal to "I reduksjonen av barnetillegget ditt vil vi bruke en inntekt på ".expr() + barnetilleggSerkull_inntektBruktIAvkortning.format() + " kroner. ",
                        Nynorsk to "I reduksjonen av barnetillegget ditt vil vi bruke ei inntekt på ".expr() + barnetilleggSerkull_inntektBruktIAvkortning.format() + " kroner.",
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT  AND (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB  OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB)) AND (FF_CheckIfFirstDayAndMonthOfYear(PE_VedtaksData_VirkningFOM) = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true )     THEN  INCLUDE ENDIF
            showIf(beregningUfore_BelopGammelUT.notEqualTo(beregningUfore_BelopNyUT) and (beregningUfore_BelopGammelBTFB.notEqualTo(beregningUfore_BelopNyBTFB) or beregningUfore_BelopGammelBTSB.notEqualTo(beregningUfore_BelopNyBTSB)) and virkFomErFoersteJanuar and barnetilleggFelles_BTFBinnvilget){
                //[TBU4001]
                // TODO Logic was different for different language layers.
                paragraph {

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT  AND (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB  OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB)) THEN      INCLUDE ENDIF
                    showIf(beregningUfore_BelopGammelUT.notEqualTo(beregningUfore_BelopNyUT) and (beregningUfore_BelopGammelBTFB.notEqualTo(beregningUfore_BelopNyBTFB) or beregningUfore_BelopGammelBTSB.notEqualTo(beregningUfore_BelopNyBTSB))){
                        text (
                            Bokmal to "I reduksjonen av barnetillegg",
                            Nynorsk to "I reduksjonen av barnetillegg",
                        )
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT  AND (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB  OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB)) AND FF_CheckIfFirstDayAndMonthOfYear(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningVirkningDatoFom) AND ((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true))     THEN  INCLUDE ENDIF
                    showIf(beregningUfore_BelopGammelUT.notEqualTo(beregningUfore_BelopNyUT) and (beregningUfore_BelopGammelBTFB.notEqualTo(beregningUfore_BelopNyBTFB) or beregningUfore_BelopGammelBTSB.notEqualTo(beregningUfore_BelopNyBTSB)) and beregningVirkFomErFoersteJanuar and (barnetilleggFelles_BTFBinnvilget or barnetilleggSerkull_innvilget)){
                        text (
                            Bokmal to "et ditt ",
                            Nynorsk to "et ditt ",
                        )
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT  AND (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB  OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB)) THEN      INCLUDE ENDIF
                    showIf(beregningUfore_BelopGammelUT.notEqualTo(beregningUfore_BelopNyUT) and (beregningUfore_BelopGammelBTFB.notEqualTo(beregningUfore_BelopNyBTFB) or beregningUfore_BelopGammelBTSB.notEqualTo(beregningUfore_BelopNyBTSB))){
                        textExpr (
                            Bokmal to "vil vi bruke en inntekt på ".expr() + barnetilleggFelles_InntektBruktIAvkortning.format() + "kroner ",
                            Nynorsk to "vil vi bruke ei inntekt på ".expr() + barnetilleggFelles_InntektBruktIAvkortning.format() + "kroner ",
                        )
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true) THEN      INCLUDE ENDIF
                    showIf(barnetilleggFelles_BTFBinnvilget and barnetilleggSerkull_innvilget){
                        textExpr (
                            Bokmal to "for ".expr() + PE_UT_Barnet_Barna_Felles + " som bor med begge sine foreldre. For " + PE_UT_Barnet_Barna_Serkull + " som ikke bor sammen med begge foreldrene vil vi bruke en inntekt på " + barnetilleggSerkull_inntektBruktIAvkortning.format() + "kroner",
                            Nynorsk to "for ".expr() + PE_UT_Barnet_Barna_Felles + " som bur med begge sine foreldra. For " + PE_UT_Barnet_Barna_Serkull + " som ikkje bur saman med begge foreldrea vil vi bruke ei inntekt på " + barnetilleggSerkull_inntektBruktIAvkortning.format() + "kroner",
                        )
                    }
                    text (
                        Bokmal to ".",
                        Nynorsk to ".",
                    )
                }
            }


            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT = PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB  AND  (FF_CheckIfFirstDayAndMonthOfYear(PE_VedtaksData_VirkningFOM) = true) )     THEN  INCLUDE ENDIF
            showIf(beregningUfore_BelopGammelUT.equalTo(beregningUfore_BelopNyUT) and beregningUfore_BelopGammelBTFB.notEqualTo(beregningUfore_BelopNyBTFB) and virkFomErFoersteJanuar){
                //[TBU4002_NN, TBU4002]

                paragraph {
                    textExpr (
                        Bokmal to "Vi vil bruke en inntekt på ".expr() + barnetilleggFelles_InntektBruktIAvkortning.format() + "kroner når vi reduserer barnetillegget ",
                        Nynorsk to "Vi vil bruke ei inntekt på ".expr() + barnetilleggFelles_InntektBruktIAvkortning.format() + "kroner når vi reduserer barnetillegget ",
                    )

                    //IF((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false)  OR (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true)) THEN      INCLUDE ENDIF
                    showIf((barnetilleggSerkull_innvilget and not(barnetilleggFelles_BTFBinnvilget)) or (not(barnetilleggFelles_BTFBinnvilget) and barnetilleggFelles_BTFBinnvilget)){
                        text (
                            Bokmal to "ditt",
                            Nynorsk to "ditt",
                        )
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true) THEN      INCLUDE ENDIF
                    showIf(barnetilleggFelles_BTFBinnvilget and barnetilleggSerkull_innvilget){
                        textExpr (
                            Bokmal to "for ".expr() + PE_UT_Barnet_Barna_Felles + " som bor med begge sine foreldre for " + PE_UT_VirkningFomAr + ". For " + PE_UT_Barnet_Barna_Serkull + " som ikke bor sammen med begge foreldrene vil vi bruke en inntekt på " + barnetilleggSerkull_inntektBruktIAvkortning.format() + "kroner. ",
                            Nynorsk to "for ".expr() + PE_UT_Barnet_Barna_Felles + " som bur saman med begge foreldra sine for " + PE_UT_VirkningFomAr + ". For " + PE_UT_Barnet_Barna_Serkull + " som ikkje bur saman med begge foreldra vil vi bruke ei inntekt på " + barnetilleggSerkull_inntektBruktIAvkortning.format() + "kroner. ",
                        )
                    }
                    text (
                        Bokmal to "Har du ikke meldt inn ny",
                        Nynorsk to "Har du ikkje meldt inn ",
                    )

                    //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
                    showIf(barnetilleggFelles_BTFBinnvilget){
                        text (
                            Bokmal to "e",
                            Nynorsk to "ei",
                        )
                    }
                    text (
                        Bokmal to " inntekt",
                        Nynorsk to " ny",
                    )

                    //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
                    showIf(barnetilleggFelles_BTFBinnvilget){
                        text (
                            Bokmal to "er",
                            Nynorsk to "e",
                        )
                    }
                    textExpr (
                        Bokmal to " for ".expr() + PE_UT_VirkningFomAr + ", er inntekten",
                        Nynorsk to " inntekt".expr(),
                    )

                    //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
                    showIf(barnetilleggFelles_BTFBinnvilget){
                        text (
                            Bokmal to "e",
                            Nynorsk to "er",
                        )
                    }
                    textExpr (
                        Bokmal to " justert opp til dagens verdi.".expr(),
                        Nynorsk to " for ".expr() + PE_UT_VirkningFomAr + ", er ",
                    )

                    //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false
                    showIf(not(barnetilleggFelles_BTFBinnvilget)){
                        text (
                            Bokmal to "",
                            Nynorsk to "inntekta",
                        )
                    }

                    //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
                    showIf(barnetilleggFelles_BTFBinnvilget){
                        text (
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
            showIf(virkFomErFoersteJanuar and beregningUfore_BelopGammelBTFB.equalTo(beregningUfore_BelopNyBTFB) and beregningUfore_BelopGammelBTSB.notEqualTo(beregningUfore_BelopNyBTSB) and beregningUfore_BelopGammelUT.equalTo(beregningUfore_BelopNyUT)){
                //[TBU4017_NN, TBU4017]

                paragraph {
                    textExpr (
                        Bokmal to "Vi vil bruke en inntekt på ".expr() + barnetilleggSerkull_inntektBruktIAvkortning.format() + "kroner når vi reduserer barnetillegget ditt. Har du ikke meldt inn ny inntekt for " + PE_UT_VirkningFomAr + ", er inntekten justert opp til dagens verdi.",
                        Nynorsk to "Vi vil bruke ei inntekt på".expr() + barnetilleggSerkull_inntektBruktIAvkortning.format() + " kroner når vi reduserer barnetillegget ditt. Har du ikkje meldt inn ny inntekt for " + PE_UT_VirkningFomAr + ", er inntekta justert opp til dagens verdi.",
                    )
                }
            }

            //IF( (PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad) AND  ((PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB  <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB) OR (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB  <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB)) AND FF_CheckIfFirstDayAndMonthOfYear(PE_VedtaksData_VirkningFOM) = true ) THEN      INCLUDE ENDIF
            showIf(uforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad.equalTo(beregningUfore_uforegrad) and ((beregningUfore_BelopGammelBTSB.notEqualTo(beregningUfore_BelopNyBTSB)) or (beregningUfore_BelopGammelBTFB.notEqualTo(beregningUfore_BelopNyBTFB))) and virkFomErFoersteJanuar){
                //[TBU4013_NN, TBU4013]

                paragraph {
                    textExpr (
                        Bokmal to "Fikk du innvilget uføretrygd etter januar ".expr() + PE_UT_VirkningstidpunktArMinus1Ar.format() + ", er inntekten justert opp slik at den gjelder for hele " + PE_UT_VirkningFomAr + ".",
                        Nynorsk to "Fekk du innvilga uføretrygd etter januar ".expr() + PE_UT_VirkningstidpunktArMinus1Ar.format() + ", er inntekta justert opp slik at den gjeld for heile " + PE_UT_VirkningFomAr + ".",
                    )
                }
            }

            //IF ( FF_CheckIfFirstDayAndMonthOfYear(PE_VedtaksData_VirkningFOM) = true AND  ( PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB > PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB  OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB > PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB ) AND  ( PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB > 0 OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB > 0 ) AND ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert= true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert= true ) )THEN  INCLUDE ENDIF
            showIf(virkFomErFoersteJanuar and (beregningUfore_BelopGammelBTFB.greaterThan(beregningUfore_BelopNyBTFB) or beregningUfore_BelopGammelBTSB.greaterThan(beregningUfore_BelopNyBTSB)) and (beregningUfore_BelopNyBTFB.greaterThan(0) or beregningUfore_BelopNyBTSB.greaterThan(0)) and (barnetilleggFelles_inntektPeriodisert or barnetilleggSerkull_inntektPeriodisert or barnetilleggFelles_fribelopPeriodisert or barnetilleggSerkull_fribelopPeriodisert)){
                //[TBU4003_NN, TBU4003]

                paragraph {
                    text (
                        Bokmal to "Fordi du ikke har barnetillegg ",
                        Nynorsk to "Fordi du ikkje har barnetillegg ",
                    )

                    //IF( ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = true AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert= false ) OR ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert= false ) AND ( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true ) ) THEN      INCLUDE ENDIF
                    showIf((barnetilleggFelles_inntektPeriodisert and not(barnetilleggSerkull_inntektPeriodisert)) or barnetilleggFelles_fribelopPeriodisert and not(barnetilleggSerkull_fribelopPeriodisert) and barnetilleggFelles_BTFBinnvilget and barnetilleggSerkull_innvilget){
                        textExpr (
                            Bokmal to "for ".expr() + PE_UT_Barnet_Barna_Felles + " som bor med begge sine foreldre ",
                            Nynorsk to "for ".expr() + PE_UT_Barnet_Barna_Felles + " som bur saman med begge foreldra ",
                        )
                    }

                    //IF( ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert= true ) OR ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert= true ) AND ( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true ) )THEN   INCLUDE ENDIF
                    showIf((not(barnetilleggFelles_inntektPeriodisert) and barnetilleggSerkull_inntektPeriodisert) or not(barnetilleggFelles_fribelopPeriodisert) and barnetilleggSerkull_fribelopPeriodisert and barnetilleggFelles_BTFBinnvilget and barnetilleggSerkull_innvilget){
                        textExpr (
                            Bokmal to "for ".expr() + PE_UT_Barnet_Barna_Serkull + " som ikke bor sammen med begge foreldrene ",
                            Nynorsk to "for ".expr() + PE_UT_Barnet_Barna_Serkull + " som ikkje bur saman med begge foreldra ",
                        )
                    }
                    text (
                        Bokmal to "hele året er ",
                        Nynorsk to "heile året er ",
                    )

                    //IF(( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert= true )) THEN   INCLUDE ENDIF
                    showIf((barnetilleggFelles_inntektPeriodisert or barnetilleggSerkull_inntektPeriodisert)){
                        text (
                            Bokmal to "inntektene ",
                            Nynorsk to "inntektene ",
                        )
                    }

                    //IF( ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert= true ) AND ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert= true ) )THEN   INCLUDE ENDIF
                    showIf((barnetilleggFelles_inntektPeriodisert or barnetilleggSerkull_inntektPeriodisert) and (barnetilleggFelles_fribelopPeriodisert or barnetilleggSerkull_fribelopPeriodisert)){
                        text (
                            Bokmal to "og ",
                            Nynorsk to "og ",
                        )
                    }

                    //IF( ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert= true ) )THEN   INCLUDE ENDIF
                    showIf((barnetilleggFelles_fribelopPeriodisert or barnetilleggSerkull_fribelopPeriodisert)){
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
            showIf(virkFomErFoersteJanuar){
                //[TBU4004_NN, TBU4004]

                paragraph {
                    text (
                        Bokmal to "Forventer du ",
                        Nynorsk to "Forventar du ",
                    )

                    //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
                    showIf(barnetilleggFelles_BTFBinnvilget){
                        textExpr (
                            Bokmal to "og din ".expr() + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT,
                            Nynorsk to "og ".expr() + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_NN_entall + " din ",
                        )
                    }
                    textExpr (
                        Bokmal to "å tjene noe annet i ".expr() + PE_UT_VirkningFomAr + "er det viktig at du melder inn ny forventet inntekt. Dette kan du gjøre på nav.no.",
                        Nynorsk to "å tene noko anna i ".expr() + PE_UT_VirkningFomAr + "er det viktig at du melder inn ei ny forventa inntekt. Dette kan du gjere på nav.no.",
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "") THEN      INCLUDE ENDIF
            showIf(not(ektefelletillegg_ETinnvilget) and not(barnetilleggSerkull_innvilget) and not(barnetilleggFelles_BTFBinnvilget) and not(gjenlevendetillegg_GTinnvilget) and ufoeretrygd_instOppholdType.equalTo("")){
                includePhrase(TBU1120_Generated(beregningUfore_totalNetto))
            }

            //IF(  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = ""  ) THEN      INCLUDE ENDIF
            showIf((barnetilleggSerkull_innvilget or barnetilleggFelles_BTFBinnvilget) and not(ektefelletillegg_ETinnvilget) and not(gjenlevendetillegg_GTinnvilget) and ufoeretrygd_instOppholdType.equalTo("")){
                includePhrase(TBU1121_Generated(beregningUfore_totalNetto))
            }

            //IF(  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true  ) THEN      INCLUDE ENDIF
            showIf((barnetilleggSerkull_innvilget or barnetilleggFelles_BTFBinnvilget) and not(gjenlevendetillegg_GTinnvilget) and ektefelletillegg_ETinnvilget){
                includePhrase(TBU1254_Generated(beregningUfore_totalNetto))
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true) THEN      INCLUDE ENDIF
            showIf(not(barnetilleggSerkull_innvilget) and not(barnetilleggFelles_BTFBinnvilget) and not(gjenlevendetillegg_GTinnvilget) and ektefelletillegg_ETinnvilget){
                includePhrase(TBU1253_Generated(beregningUfore_totalNetto))
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "") THEN      INCLUDE ENDIF
            showIf(not(barnetilleggSerkull_innvilget) and not(barnetilleggFelles_BTFBinnvilget) and gjenlevendetillegg_GTinnvilget and not(ektefelletillegg_ETinnvilget) and ufoeretrygd_instOppholdType.equalTo("")){
                includePhrase(TBU1122_Generated(beregningUfore_totalNetto))
            }

            //IF(  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false  ) THEN      INCLUDE ENDIF
            showIf((barnetilleggSerkull_innvilget or barnetilleggFelles_BTFBinnvilget) and gjenlevendetillegg_GTinnvilget and not(ektefelletillegg_ETinnvilget)){
                includePhrase(TBU1123_Generated(beregningUfore_totalNetto))
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_TotalNetto > 0) THEN      INCLUDE ENDIF
            showIf(beregningUfore_totalNetto.greaterThan(0)){
                includePhrase(TBU2223_Generated)
            }
            includePhrase(TBU1128_Generated)
            //[TBU1029]

            paragraph {
                text (
                    Bokmal to "Begrunnelse for vedtaket",
                )
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT) THEN      INCLUDE ENDIF
            showIf(beregningUfore_BelopGammelUT.notEqualTo(beregningUfore_BelopNyUT)){

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense < PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak) THEN      INCLUDE ENDIF
                showIf(uforetrygdOrdiner_inntektsgrense.lessThan(uforetrygdOrdiner_inntektstak)){
                    //[TBU2253NN, TBU2253]

                    paragraph {
                        text (
                            Bokmal to "Når vi endrer utbetalingen av uføretrygden din, tar vi utgangspunkt i inntekten du har ved siden av uføretrygden. Det er bare den delen av inntekten din som overstiger inntektsgrensen som vil gi en reduksjon av uføretrygden.",
                            Nynorsk to "Når vi endrar utbetalinga av uføretrygda di, tek vi utgangspunkt i inntekta du har ved sida av uføretrygda. Det er berre den delen av inntekta di som kjem over inntektsgrensa som gir ein reduksjon av uføretrygda di.",
                        )

                        //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt > PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT > PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT > 0) THEN      INCLUDE ENDIF
                        showIf(uforetrygdOrdiner_forventetInntekt.greaterThan(uforetrygdOrdiner_inntektsgrense) and beregningUfore_BelopGammelUT.greaterThan(beregningUfore_BelopNyUT) and beregningUfore_BelopNyUT.greaterThan(0)){
                            text (
                                Bokmal to "Uføretrygden din reduseres fordi du tjener over inntektsgrensen din. Selv om du får en reduksjon lønner det seg likevel å jobbe ved siden av uføretrygden. ",
                                Nynorsk to "Uføretrygda di blir redusert fordi du tener over inntektsgrensa di. Sjølv om du får ein reduksjon, lønner det seg likevel å jobbe ved sida av uføretrygda. ",
                            )
                        }

                        //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt > PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT < PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT > 0) THEN      INCLUDE ENDIF
                        showIf(uforetrygdOrdiner_forventetInntekt.greaterThan(uforetrygdOrdiner_inntektsgrense) and uforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad.equalTo(beregningUfore_uforegrad) and beregningUfore_BelopGammelUT.lessThan(beregningUfore_BelopNyUT) and beregningUfore_BelopNyUT.greaterThan(0)){
                            text (
                                Bokmal to "Endringen i inntekten din gjør at uføretrygden ikke lenger er redusert.",
                                Nynorsk to "Endringa i inntekta di gjer at uføretrygda ikkje lenger er redusert. ",
                            )
                        }

                        //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt < PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT > 0) THEN      INCLUDE ENDIF
                        showIf(uforetrygdOrdiner_forventetInntekt.lessThan(uforetrygdOrdiner_inntektsgrense) and uforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad.equalTo(beregningUfore_uforegrad) and beregningUfore_BelopNyUT.greaterThan(0)){
                            text (
                                Bokmal to "Utbetalingen av uføretrygden din økes fordi du tjener under inntektsgrensen din. Det betyr at uføretrygden ikke lenger er redusert. ",
                                Nynorsk to "Utbetalinga av uføretrygda aukar fordi du tener under inntektsgrensa di. Det betyr at uføretrygda ikkje lenger er redusert. ",
                            )
                        }

                        //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT = 0 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt <= PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak) THEN      INCLUDE ENDIF
                        showIf(beregningUfore_BelopNyUT.equalTo(0) and uforetrygdOrdiner_forventetInntekt.lessOrEqual(uforetrygdOrdiner_inntektstak)){
                            text (
                                Bokmal to "Endring i inntekten din gjør at du ikke får utbetalt uføretrygd for resten av året. ",
                                Nynorsk to "Endring i inntekta di gjer at du ikkje får utbetalt uføretrygd for resten av året. ",
                            )
                        }
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense >= PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak) THEN      INCLUDE ENDIF
                showIf(uforetrygdOrdiner_inntektsgrense.greaterOrEqual(uforetrygdOrdiner_inntektstak)){
                    //[TBU3734NN, TBU3734]

                    paragraph {
                        text (
                            Bokmal to "Når vi endrer utbetalingen av uføretrygden din, tar vi utgangspunkt i inntekten du har ved siden av uføretrygden.",
                            Nynorsk to "Når vi endrar utbetalinga av uføretrygda di, tek vi utgangspunkt i inntekta du har ved sida av uføretrygda.",
                        )

                        //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopRedusert = true AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt > PE_Vedtaksdata_BeregningsData_BeregningUforePeriode_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense) THEN      INCLUDE ENDIF
                        showIf(beregningUfore_BelopRedusert and uforetrygdOrdiner_forventetInntekt.greaterThan(uforetrygdOrdiner_inntektsgrense)){
                            text (
                                Bokmal to " Det lønner seg likevel å jobbe, fordi inntekt og uføretrygd alltid vil være høyere enn uføretrygd alene.",
                                Nynorsk to " Det lønner seg likevel å jobbe fordi inntekt og uføretrygd vil alltid vere høgare enn uføretrygd åleine.",
                            )
                        }
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt > PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT > 0) THEN      INCLUDE ENDIF
                showIf(uforetrygdOrdiner_forventetInntekt.greaterThan(uforetrygdOrdiner_inntektsgrense) and beregningUfore_BelopNyUT.greaterThan(0)){
                    //[TBU4005_NN, TBU4005]

                    paragraph {
                        textExpr (
                            Bokmal to "Siden du har en inntekt på ".expr() + uforetrygdOrdiner_forventetInntekt.format() + " kroner trekker vi " + uforetrygdOrdiner_avkortingsbelopPerAr.format() + " kroner fra uføretrygden ",
                            Nynorsk to "Fordi du har ei inntekt på ".expr() + uforetrygdOrdiner_forventetInntekt.format() + " kroner trekkjer vi " + uforetrygdOrdiner_avkortingsbelopPerAr.format() + " kroner frå uføretrygda ",
                        )

                        //IF(FF_CheckIfFirstDayAndMonthOfYear(PE_VedtaksData_VirkningFOM) = false) THEN      INCLUDE ENDIF
                        showIf(not(virkFomErFoersteJanuar)){
                            text (
                                Bokmal to "i ",
                                Nynorsk to "i ",
                            )
                        }

                        //IF(FF_CheckIfFirstDayAndMonthOfYear(PE_VedtaksData_VirkningFOM) = true) THEN      INCLUDE ENDIF
                        showIf(virkFomErFoersteJanuar){
                            text (
                                Bokmal to "for neste ",
                                Nynorsk to "for " + "neste ",
                            )
                        }
                        text (
                            Bokmal to "år.",
                            Nynorsk to "år.",
                        )
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt > PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense <> PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT = 0) THEN      INCLUDE ENDIF
                showIf(uforetrygdOrdiner_forventetInntekt.greaterThan(uforetrygdOrdiner_inntektstak) and uforetrygdOrdiner_inntektsgrense.notEqualTo(uforetrygdOrdiner_inntektstak) and beregningUfore_BelopNyUT.equalTo(0)){
                    //[TBU2258NN, TBU2258]

                    paragraph {
                        textExpr (
                            Bokmal to "Du får ikke utbetalt uføretrygd siden inntekten din er høyere enn 80 prosent av inntekten du hadde før du ble ufør, det vil si ".expr() + uforetrygdOrdiner_inntektstak.format() + " kroner. Inntekten vi har brukt er " + uforetrygdOrdiner_forventetInntekt.format() + " kroner og du vil derfor ikke få utbetalt uføretrygd resten av året.",
                            Nynorsk to "Du får ikkje utbetalt uføretrygd fordi inntekta di er høgare enn 80 prosent av inntekta du hadde før du blei ufør, det vil seie ".expr() + uforetrygdOrdiner_inntektstak.format() + " kroner. Inntekta vi har brukt er " + uforetrygdOrdiner_forventetInntekt.format() + " kroner og du vil ikkje få utbetalt uføretrygd resten av året.",
                        )
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt > PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense = PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT = 0) THEN      INCLUDE ENDIF
                showIf(uforetrygdOrdiner_forventetInntekt.greaterThan(uforetrygdOrdiner_inntektsgrense) and uforetrygdOrdiner_inntektsgrense.equalTo(uforetrygdOrdiner_inntektstak) and beregningUfore_BelopNyUT.equalTo(0)){
                    //[TBU3735NN, TBU3735]

                    paragraph {
                        textExpr (
                            Bokmal to "Det utbetales ikke uføretrygd når inntekten din utgjør mer enn inntektsgrensen, det vil si ".expr() + uforetrygdOrdiner_inntektsgrense.format() + " kroner. Inntekten vi har brukt er " + uforetrygdOrdiner_forventetInntekt.format() + " kroner og du vil derfor ikke få utbetalt uføretrygd resten av året.",
                            Nynorsk to "Det blir ikkje utbetalt uføretrygd når inntekta di utgjer meir enn inntektsgrensa di, det vil seie ".expr() + uforetrygdOrdiner_inntektsgrense.format() + " kroner. Inntekta vi har brukt er " + uforetrygdOrdiner_forventetInntekt.format() + " kroner og du vil derfor ikkje få utbetalt uføretrygd resten av året.",
                        )
                    }
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB) THEN      INCLUDE ENDIF
            showIf(beregningUfore_BelopGammelBTFB.notEqualTo(beregningUfore_BelopNyBTFB) or beregningUfore_BelopGammelBTSB.notEqualTo(beregningUfore_BelopNyBTSB)){

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true) THEN      INCLUDE ENDIF
                showIf(barnetilleggFelles_BTFBinnvilget or barnetilleggSerkull_innvilget){
                    //[TBU4006_NN, TBU4006]

                    paragraph {
                        text (
                            Bokmal to "Inntekten din har ",
                            Nynorsk to "Inntekta di har ",
                        )

                        //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT  AND (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB  OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB)) THEN      INCLUDE ENDIF
                        showIf(beregningUfore_BelopGammelUT.notEqualTo(beregningUfore_BelopNyUT) and (beregningUfore_BelopGammelBTFB.notEqualTo(beregningUfore_BelopNyBTFB) or beregningUfore_BelopGammelBTSB.notEqualTo(beregningUfore_BelopNyBTSB))){
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
                        showIf(not(barnetilleggSerkull_innvilget) and barnetilleggFelles_BTFBinnvilget){
                            text (
                                Bokmal to "Fordi",
                                Nynorsk to "Fordi",
                            )
                        }

                        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) THEN      INCLUDE ENDIF
                        showIf(barnetilleggSerkull_innvilget and barnetilleggFelles_BTFBinnvilget){
                            text (
                                Bokmal to "For",
                                Nynorsk to "For",
                            )
                        }

                        //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
                        showIf(barnetilleggFelles_BTFBinnvilget){
                            textExpr (
                                Bokmal to PE_UT_Barnet_Barna_Felles,
                                Nynorsk to PE_UT_Barnet_Barna_Felles,
                            )
                        }

                        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) THEN      INCLUDE ENDIF
                        showIf(barnetilleggSerkull_innvilget and barnetilleggFelles_BTFBinnvilget){
                            text (
                                Bokmal to "som ",
                                Nynorsk to "som ",
                            )
                        }

                        //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
                        showIf(barnetilleggFelles_BTFBinnvilget){
                            textExpr (
                                Bokmal to "bor med begge sine foreldre, bruker vi i tillegg din ".expr() + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT + "s inntekt når vi fastsetter størrelsen på barnetillegget ditt. ",
                                Nynorsk to "bur saman med begge foreldra sine, bruker vi i tillegg ".expr() + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_NN_entall + " din si inntekt når vi fastset storleiken på barnetillegget ditt. ",
                            )
                        }

                        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) THEN      INCLUDE ENDIF
                        showIf(barnetilleggSerkull_innvilget and barnetilleggFelles_BTFBinnvilget){
                            textExpr (
                                Bokmal to "For ".expr() + PE_UT_Barnet_Barna_Serkull + " som ikke bor sammen med begge foreldrene bruker vi kun inntekten din.",
                                Nynorsk to "For ".expr() + PE_UT_Barnet_Barna_Serkull + " som ikkje bur saman med begge foreldra bruker vi berre inntekta di. ",
                            )
                        }
                        text (
                            Bokmal to "Uføretrygden ",
                            Nynorsk to "Uføretrygda ",
                        )

                        //PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false
                        showIf(not(gjenlevendetillegg_GTinnvilget)){
                            text (
                                Bokmal to "din ",
                                Nynorsk to "di ",
                            )
                        }

                        //PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true
                        showIf(gjenlevendetillegg_GTinnvilget){
                            text (
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
                showIf(barnetilleggFelles_BTFBinnvilget){
                    //[TBU4007_NN, TBU4007]

                    paragraph {
                        textExpr (
                            Bokmal to "Vi har beregnet barnetillegget på nytt ut fra inntekten din på ".expr() + barnetilleggFelles_brukersInntektTilAvkortning.format() + " kroner og inntekten til din " + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT + " på " + barnetilleggFelles_inntektAnnenForelder.format() + " kroner. ",
                            Nynorsk to "Vi har berekna barnetillegget på nytt ut frå inntekta di på ".expr() + barnetilleggFelles_brukersInntektTilAvkortning.format() + " kroner og inntekta til " + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_NN_entall + " din på " + barnetilleggFelles_inntektAnnenForelder.format() + " kroner. ",
                        )

                        //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBbelopFratrukketAnnenForeldersInntekt > 0) THEN      INCLUDE ENDIF
                        showIf(barnetilleggFelles_belopFratrukketAnnenForeldersInntekt.greaterThan(0)){
                            textExpr (
                                Bokmal to "Folketrygdens grunnbeløp på inntil ".expr() + beregningUfore_grunnbelop.format() + " kroner er holdt utenfor din " + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT + "s inntekt. ",
                                Nynorsk to "Grunnbeløpet i folketrygda på inntil ".expr() + beregningUfore_grunnbelop.format() + " kroner er held utanfor inntekta til " + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_NN_entall + " din. ",
                            )
                        }
                        textExpr (
                            Bokmal to "Til sammen utgjør disse inntektene ".expr() + barnetilleggFelles_InntektBruktIAvkortning.format() + " kroner. ",
                            Nynorsk to "Til saman utgjer desse inntektene ".expr() + barnetilleggFelles_InntektBruktIAvkortning.format() + " kroner. ",
                        )

                        //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto > 0) THEN      INCLUDE ENDIF
                        showIf(barnetilleggFelles_justeringsbelopPerAr.equalTo(0) and barnetilleggFelles_netto.greaterThan(0)){
                            text (
                                Bokmal to "Dette beløpet er ",
                                Nynorsk to "Dette beløpet er ",
                            )
                        }

                        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBInntektBruktiAvkortning > PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBfribelop AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto > 0) THEN      INCLUDE ENDIF
                        showIf(barnetilleggFelles_InntektBruktIAvkortning.greaterThan(barnetilleggFelles_fribeloep) and barnetilleggFelles_justeringsbelopPerAr.equalTo(0) and barnetilleggFelles_netto.greaterThan(0)){
                            text (
                                Bokmal to "høyere",
                                Nynorsk to "høgare",
                            )
                        }

                        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBInntektBruktiAvkortning <= PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBfribelop AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto > 0) THEN      INCLUDE ENDIF
                        showIf(barnetilleggFelles_InntektBruktIAvkortning.lessOrEqual(barnetilleggFelles_fribeloep) and barnetilleggFelles_justeringsbelopPerAr.equalTo(0) and barnetilleggFelles_netto.greaterThan(0)){
                            text (
                                Bokmal to "lavere",
                                Nynorsk to "lågare",
                            )
                        }

                        //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto > 0) THEN      INCLUDE ENDIF
                        showIf(barnetilleggFelles_justeringsbelopPerAr.equalTo(0) and barnetilleggFelles_netto.greaterThan(0)){
                            textExpr (
                                Bokmal to " enn fribeløpsgrensen på ".expr() + barnetilleggFelles_fribeloep.format() + "kroner. Derfor er barnetillegget ",
                                Nynorsk to " enn fribeløpet på ".expr() + barnetilleggFelles_fribeloep.format() + " kroner. Derfor er barnetillegget ",
                            )
                        }

                        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto > 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0) THEN      INCLUDE ENDIF
                        showIf(barnetilleggFelles_BTFBinnvilget and barnetilleggFelles_netto.greaterThan(0) and barnetilleggSerkull_innvilget and barnetilleggSerkull_netto.greaterThan(0) and barnetilleggFelles_justeringsbelopPerAr.equalTo(0)){
                            textExpr (
                                Bokmal to "for ".expr() + PE_UT_Barnet_Barna_Felles + " som bor med begge sine foreldre ",
                                Nynorsk to "for ".expr() + PE_UT_Barnet_Barna_Felles + " som bur saman med begge sine foreldra ",
                            )
                        }

                        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBInntektBruktiAvkortning <= PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBfribelop AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto > 0) THEN      INCLUDE ENDIF
                        showIf(barnetilleggFelles_InntektBruktIAvkortning.lessOrEqual(barnetilleggFelles_fribeloep) and barnetilleggFelles_justeringsbelopPerAr.equalTo(0) and barnetilleggFelles_netto.greaterThan(0)){
                            text (
                                Bokmal to "ikke lenger ",
                                Nynorsk to "ikkje lenger ",
                            )
                        }

                        //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto > 0) THEN      INCLUDE ENDIF
                        showIf(barnetilleggFelles_justeringsbelopPerAr.equalTo(0) and barnetilleggFelles_netto.greaterThan(0)){
                            text (
                                Bokmal to "redusert. ",
                                Nynorsk to "redusert. ",
                            )
                        }

                        //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr <> 0) THEN      INCLUDE ENDIF
                        showIf(barnetilleggFelles_justeringsbelopPerAr.notEqualTo(0)){
                            text (
                                Bokmal to "Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget",
                                Nynorsk to "Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget",
                            )
                        }

                        //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr <> 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) THEN      INCLUDE ENDIF
                        showIf(barnetilleggFelles_justeringsbelopPerAr.notEqualTo(0) and barnetilleggSerkull_innvilget and barnetilleggSerkull_netto.greaterThan(0)){
                            textExpr (
                                Bokmal to " for ".expr() + PE_UT_Barnet_Barna_Felles + " som bor med begge sine foreldre",
                                Nynorsk to " for ".expr() + PE_UT_Barnet_Barna_Felles + " som bur saman med begge foreldra sine",
                            )
                        }

                        //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr <> 0) THEN      INCLUDE ENDIF
                        showIf(barnetilleggFelles_justeringsbelopPerAr.notEqualTo(0)){
                            text (
                                Bokmal to ". ",
                                Nynorsk to ". ",
                            )
                        }

                        //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr <> 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0) THEN      INCLUDE ENDIF
                        showIf(barnetilleggFelles_justeringsbelopPerAr.notEqualTo(0) and barnetilleggFelles_netto.equalTo(0)){
                            text (
                                Bokmal to "Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året.",
                                Nynorsk to "Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året.",
                            )
                        }
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) THEN      INCLUDE ENDIF
                showIf(barnetilleggSerkull_innvilget and barnetilleggFelles_BTFBinnvilget){
                    //[TBU4008_NN, TBU4008]

                    paragraph {
                        textExpr (
                            Bokmal to "Barnetillegget for ".expr() + PE_UT_Barnet_Barna_Serkull + " som ikke bor sammen med begge foreldrene er beregnet ut fra inntekten din på " + barnetilleggSerkull_inntektBruktIAvkortning.format() + " kroner. ",
                            Nynorsk to "Barnetillegget for ".expr() + PE_UT_Barnet_Barna_Serkull + " som ikkje bur saman med begge foreldra er berekna ut frå inntekta di på " + barnetilleggSerkull_inntektBruktIAvkortning.format() + " kroner. ",
                        )

                        //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) THEN      INCLUDE ENDIF
                        showIf(barnetilleggSerkull_justeringsbelopPerAr.equalTo(0) and barnetilleggSerkull_netto.greaterThan(0)){
                            text (
                                Bokmal to "Dette er ",
                                Nynorsk to "Dette er ",
                            )
                        }

                        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning > PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBfribelop AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) THEN      INCLUDE ENDIF
                        showIf(barnetilleggSerkull_inntektBruktIAvkortning.greaterThan(barnetilleggSerkull_fribeloep) and barnetilleggSerkull_justeringsbelopPerAr.equalTo(0) and barnetilleggSerkull_netto.greaterThan(0)){
                            text (
                                Bokmal to "høyere",
                                Nynorsk to "høgare",
                            )
                        }

                        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning <= PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBfribelop AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) THEN      INCLUDE ENDIF
                        showIf(barnetilleggSerkull_inntektBruktIAvkortning.lessOrEqual(barnetilleggSerkull_fribeloep) and barnetilleggSerkull_justeringsbelopPerAr.equalTo(0) and barnetilleggSerkull_netto.greaterThan(0)){
                            text (
                                Bokmal to "lavere",
                                Nynorsk to "lågere",
                            )
                        }

                        //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) THEN      INCLUDE ENDIF
                        showIf(barnetilleggSerkull_justeringsbelopPerAr.equalTo(0) and barnetilleggSerkull_netto.greaterThan(0)){
                            textExpr (
                                Bokmal to " enn fribeløpsgrensen på ".expr() + barnetilleggSerkull_fribeloep.format() + " kroner. Dette barnetillegget er derfor ",
                                Nynorsk to " enn fribeløpet på ".expr() + barnetilleggSerkull_fribeloep.format() + " kroner. Derfor er barnetillegget ",
                            )
                        }

                        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning <= PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBfribelop AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) THEN      INCLUDE ENDIF
                        showIf(barnetilleggSerkull_inntektBruktIAvkortning.lessOrEqual(barnetilleggSerkull_fribeloep) and barnetilleggSerkull_justeringsbelopPerAr.equalTo(0) and barnetilleggSerkull_netto.greaterThan(0)){
                            text (
                                Bokmal to "ikke lenger ",
                                Nynorsk to "ikkje lenger ",
                            )
                        }

                        //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB > PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB > PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) THEN      INCLUDE ENDIF
                        showIf(beregningUfore_BelopGammelBTFB.greaterThan(beregningUfore_BelopNyBTFB) and beregningUfore_BelopGammelBTSB.greaterThan(beregningUfore_BelopNyBTSB) and barnetilleggSerkull_justeringsbelopPerAr.equalTo(0) and barnetilleggSerkull_netto.greaterThan(0)){
                            text (
                                Bokmal to "også ",
                                Nynorsk to "også ",
                            )
                        }

                        //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) THEN      INCLUDE ENDIF
                        showIf(barnetilleggSerkull_justeringsbelopPerAr.equalTo(0) and barnetilleggSerkull_netto.greaterThan(0)){
                            text (
                                Bokmal to "redusert. ",
                                Nynorsk to "redusert. ",
                            )
                        }

                        //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr <> 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) THEN      INCLUDE ENDIF
                        showIf(barnetilleggSerkull_justeringsbelopPerAr.notEqualTo(0) and barnetilleggSerkull_netto.greaterThan(0)){
                            textExpr (
                                Bokmal to "Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget for ".expr() + PE_UT_Barnet_Barna_Serkull + " som ikke bor sammen med begge foreldrene. ",
                                Nynorsk to "Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget for ".expr() + PE_UT_Barnet_Barna_Serkull + " som ikkje bur saman med begge foreldra. ",
                            )
                        }

                        //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr <> 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0) THEN      INCLUDE ENDIF
                        showIf(barnetilleggSerkull_justeringsbelopPerAr.notEqualTo(0) and barnetilleggSerkull_netto.equalTo(0)){
                            text (
                                Bokmal to "Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året.",
                                Nynorsk to "Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året.",
                            )
                        }
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false) THEN      INCLUDE ENDIF
                showIf(barnetilleggSerkull_innvilget and not(barnetilleggFelles_BTFBinnvilget)){
                    //[TBU4009_NN, TBU4009]

                    paragraph {
                        textExpr (
                            Bokmal to "Vi har beregnet barnetillegget på nytt ut fra inntekten din på ".expr() + barnetilleggSerkull_inntektBruktIAvkortning.format() + " kroner. ",
                            Nynorsk to "Vi har berekna barnetillegget på nytt ut frå inntekta di på ".expr() + barnetilleggSerkull_inntektBruktIAvkortning.format() + " kroner. ",
                        )

                        //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) THEN      INCLUDE ENDIF
                        showIf(barnetilleggSerkull_justeringsbelopPerAr.equalTo(0) and barnetilleggSerkull_netto.greaterThan(0)){
                            text (
                                Bokmal to "Dette er ",
                                Nynorsk to "Dette beløpet er ",
                            )
                        }

                        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning > PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBfribelop AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) THEN      INCLUDE ENDIF
                        showIf(barnetilleggSerkull_inntektBruktIAvkortning.greaterThan(barnetilleggSerkull_fribeloep) and barnetilleggSerkull_justeringsbelopPerAr.equalTo(0) and barnetilleggSerkull_netto.greaterThan(0)){
                            text (
                                Bokmal to "høyere",
                                Nynorsk to "høgare",
                            )
                        }

                        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning <= PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBfribelop AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) THEN      INCLUDE ENDIF
                        showIf(barnetilleggSerkull_inntektBruktIAvkortning.lessOrEqual(barnetilleggSerkull_fribeloep) and barnetilleggSerkull_justeringsbelopPerAr.equalTo(0) and barnetilleggSerkull_netto.greaterThan(0)){
                            text (
                                Bokmal to "lavere",
                                Nynorsk to "lågare",
                            )
                        }

                        //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) THEN      INCLUDE ENDIF
                        showIf(barnetilleggSerkull_justeringsbelopPerAr.equalTo(0) and barnetilleggSerkull_netto.greaterThan(0)){
                            textExpr (
                                Bokmal to " enn fribeløpsgrensen på ".expr() + barnetilleggSerkull_fribeloep.format() + "kroner. Barnetillegget er derfor ",
                                Nynorsk to " enn fribeløpet på ".expr() + barnetilleggSerkull_fribeloep.format() + "kroner. Derfor er barnetillegget ",
                            )
                        }

                        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning <= PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBfribelop AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) THEN      INCLUDE ENDIF
                        showIf(barnetilleggSerkull_inntektBruktIAvkortning.lessOrEqual(barnetilleggSerkull_fribeloep) and barnetilleggSerkull_justeringsbelopPerAr.equalTo(0) and barnetilleggSerkull_netto.greaterThan(0)){
                            text (
                                Bokmal to "ikke lenger ",
                                Nynorsk to "ikkje lenger ",
                            )
                        }

                        //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) THEN      INCLUDE ENDIF
                        showIf(barnetilleggSerkull_justeringsbelopPerAr.equalTo(0) and barnetilleggSerkull_netto.greaterThan(0)){
                            text (
                                Bokmal to "redusert. ",
                                Nynorsk to "redusert. ",
                            )
                        }

                        //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr <> 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) THEN      INCLUDE ENDIF
                        showIf(barnetilleggSerkull_justeringsbelopPerAr.notEqualTo(0) and barnetilleggSerkull_netto.greaterThan(0)){
                            text (
                                Bokmal to "Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endretbarnetillegget. ",
                                Nynorsk to "Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget. ",
                            )
                        }

                        //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr <> 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0) THEN      INCLUDE ENDIF
                        showIf(barnetilleggSerkull_justeringsbelopPerAr.notEqualTo(0) and barnetilleggSerkull_netto.equalTo(0)){
                            text (
                                Bokmal to "Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året.",
                                Nynorsk to "Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året.",
                            )
                        }
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0) THEN      INCLUDE ENDIF
                showIf(barnetilleggFelles_netto.equalTo(0) and barnetilleggFelles_BTFBinnvilget and barnetilleggFelles_justeringsbelopPerAr.equalTo(0)){
                    //[TBU4010_NN, TBU4010]

                    paragraph {
                        text (
                            Bokmal to "Barnetillegget ",
                            Nynorsk to "Barnetillegget ",
                        )

                        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) THEN      INCLUDE ENDIF
                        showIf(barnetilleggSerkull_innvilget and barnetilleggFelles_BTFBinnvilget){
                            textExpr (
                                Bokmal to "for ".expr() + PE_UT_Barnet_Barna_Felles + " som bor med begge sine foreldre ",
                                Nynorsk to "for ".expr() + PE_UT_Barnet_Barna_Felles + " som bur saman med begge forelda sine ",
                            )
                        }
                        textExpr (
                            Bokmal to "blir ikke utbetalt fordi den samlede inntekten til deg og din ".expr() + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT + " er høyere enn " + barnetilleggFelles_inntektstak.format() + " kroner.",
                            Nynorsk to "blir ikkje utbetalt fordi den samla inntekta til deg og ".expr() + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_NN_entall + " din er høgare enn " + barnetilleggFelles_inntektstak.format() + "kroner.",
                        )
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0) THEN      INCLUDE ENDIF
                showIf(barnetilleggSerkull_netto.equalTo(0) and barnetilleggSerkull_innvilget and barnetilleggSerkull_justeringsbelopPerAr.equalTo(0)){
                    //[TBU4011_NN, TBU4011]

                    paragraph {
                        text (
                            Bokmal to "Barnetillegget ",
                            Nynorsk to "Barnetillegget ",
                        )

                        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) THEN      INCLUDE ENDIF
                        showIf(barnetilleggSerkull_innvilget and barnetilleggFelles_BTFBinnvilget){
                            textExpr (
                                Bokmal to "for ".expr() + PE_UT_Barnet_Barna_Serkull + " som ikke bor sammen med begge foreldrene ",
                                Nynorsk to "for ".expr() + PE_UT_Barnet_Barna_Serkull + " som ikkje bur saman med begge foreldra ",
                            )
                        }
                        textExpr (
                            Bokmal to "blir ikke utbetalt fordi inntekten din er høyere enn ".expr() + barnetilleggSerkull_inntektstak.format() + " kroner.",
                            Nynorsk to "blir ikkje utbetalt fordi inntekta di er høgare enn ".expr() + barnetilleggSerkull_inntektstak.format() + "kroner.",
                        )
                    }
                }

                //IF( FF_CheckIfFirstDayAndMonthOfYear(PE_VedtaksData_VirkningFOM) = false AND ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert= true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert= true ) ) THEN      INCLUDE ENDIF
                showIf(not(virkFomErFoersteJanuar) and (barnetilleggFelles_inntektPeriodisert or barnetilleggSerkull_inntektPeriodisert or barnetilleggFelles_fribelopPeriodisert or barnetilleggSerkull_fribelopPeriodisert)){
                    //[TBU4012]
                    // TODO Logic was different for different language layers.
                    paragraph {
                        text (
                            Bokmal to "Fordi du ikke har barnetillegg ",
                        )

                        //IF( ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = true AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert= false ) OR ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert= false ) AND ( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true ) ) THEN      INCLUDE ENDIF
                        showIf((barnetilleggFelles_inntektPeriodisert and not(barnetilleggSerkull_inntektPeriodisert)) or barnetilleggFelles_fribelopPeriodisert and not(barnetilleggSerkull_fribelopPeriodisert) and barnetilleggFelles_BTFBinnvilget and barnetilleggSerkull_innvilget){
                            textExpr (
                                Bokmal to "for ".expr() + PE_UT_Barnet_Barna_Felles + " som bor med begge sine foreldre ",
                            )
                        }

                        //IF( ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert= true ) OR ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert= true ) AND ( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true ) )THEN   INCLUDE ENDIF
                        showIf((not(barnetilleggFelles_inntektPeriodisert) and barnetilleggSerkull_inntektPeriodisert) or not(barnetilleggFelles_fribelopPeriodisert) and barnetilleggSerkull_fribelopPeriodisert and barnetilleggFelles_BTFBinnvilget and barnetilleggSerkull_innvilget){
                            textExpr (
                                Bokmal to "for ".expr() + PE_UT_Barnet_Barna_Serkull + " som ikke bor sammen med begge foreldrene ",
                            )
                        }
                        text (
                            Bokmal to "hele året er ",
                        )

                        //IF(( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert= true )) THEN   INCLUDE ENDIF
                        showIf((barnetilleggFelles_inntektPeriodisert or barnetilleggSerkull_inntektPeriodisert)){
                            text (
                                Bokmal to "inntektene ",
                            )
                        }

                        //IF( ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert= true ) AND ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert= true ) )THEN   INCLUDE ENDIF
                        showIf((barnetilleggFelles_inntektPeriodisert or barnetilleggSerkull_inntektPeriodisert) and (barnetilleggFelles_fribelopPeriodisert or barnetilleggSerkull_fribelopPeriodisert)){
                            text (
                                Bokmal to "og ",
                            )
                        }

                        //IF( ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert= true ) )THEN   INCLUDE ENDIF
                        showIf((barnetilleggFelles_fribelopPeriodisert or barnetilleggSerkull_fribelopPeriodisert)){
                            text (
                                Bokmal to "fribeløpet ",
                            )
                        }
                        text (
                            Bokmal to "justert slik at de",
                        )

                        //IF( (PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = false  AND  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert = false) AND (PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true OR  PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert = true) ) THEN      INCLUDE ENDIF
                        showIf(not(barnetilleggFelles_inntektPeriodisert) and not(barnetilleggSerkull_inntektPeriodisert) and (barnetilleggFelles_fribelopPeriodisert or barnetilleggSerkull_fribelopPeriodisert)){
                            text (
                                Bokmal to "t",
                            )
                        }
                        text (
                            Bokmal to " kun gjelder for den perioden du mottar barnetillegg. ",
                        )

                        //PE_Vedtaksdata_Kravhode_KravArsakType = "sivilstandsendring"
                        showIf(kravArsakType.equalTo("sivilstandsendring")){
                            text (
                                Bokmal to "Fordi sivilstanden din har endret seg er ",
                            )
                        }

                        //IF( (PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert= true) AND PE_Vedtaksdata_Kravhode_KravArsakType = "sivilstandsendring" ) THEN   INCLUDE ENDIF
                        showIf((barnetilleggFelles_inntektPeriodisert or barnetilleggSerkull_inntektPeriodisert) and kravArsakType.equalTo("sivilstandsendring")){
                            text (
                                Bokmal to "inntektene ",
                            )
                        }

                        //IF( ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert= true ) AND ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert= true ) AND ( PE_Vedtaksdata_Kravhode_KravArsakType = "sivilstandsendring" ) )THEN   INCLUDE ENDIF
                        showIf((barnetilleggFelles_inntektPeriodisert or barnetilleggSerkull_inntektPeriodisert) and (barnetilleggFelles_fribelopPeriodisert or barnetilleggSerkull_fribelopPeriodisert) and kravArsakType.equalTo("sivilstandsendring")){
                            text (
                                Bokmal to "og ",
                            )
                        }

                        //IF( (PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert= true) AND PE_Vedtaksdata_Kravhode_KravArsakType = "sivilstandsendring"  )THEN   INCLUDE ENDIF
                        showIf((barnetilleggFelles_fribelopPeriodisert or barnetilleggSerkull_fribelopPeriodisert) and kravArsakType.equalTo("sivilstandsendring")){
                            text (
                                Bokmal to "fribeløpet ",
                            )
                        }

                        //PE_Vedtaksdata_Kravhode_KravArsakType = "sivilstandsendring"
                        showIf(kravArsakType.equalTo("sivilstandsendring")){
                            text (
                                Bokmal to "justert slik at de kun gjelder for den framtidige perioden du mottar barnetillegg. ",
                            )
                        }
                    }
                    //[TBU4012]
                    // TODO Logic was different for different language layers.
                    paragraph {
                        text (
                            Nynorsk to "Fordi du ikkje har barnetillegg ",
                        )

                        //IF( ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = true AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert= false ) OR ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert= false ) AND ( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true ) ) THEN      INCLUDE ENDIF
                        showIf((barnetilleggFelles_inntektPeriodisert and not(barnetilleggSerkull_inntektPeriodisert)) or barnetilleggFelles_fribelopPeriodisert and not(barnetilleggSerkull_fribelopPeriodisert) and barnetilleggFelles_BTFBinnvilget and barnetilleggSerkull_innvilget){
                            textExpr (
                                Nynorsk to "for ".expr() + PE_UT_Barnet_Barna_Felles + " som bur saman med begge foreldra sine ",
                            )
                        }

                        //IF( ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert= true ) OR ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert= true ) AND ( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true ) )THEN   INCLUDE ENDIF
                        showIf((not(barnetilleggFelles_inntektPeriodisert) and barnetilleggSerkull_inntektPeriodisert) or not(barnetilleggFelles_fribelopPeriodisert) and barnetilleggSerkull_fribelopPeriodisert and barnetilleggFelles_BTFBinnvilget and barnetilleggSerkull_innvilget){
                            textExpr (
                                Nynorsk to "for ".expr() + PE_UT_Barnet_Barna_Serkull + " som ikkje bur saman med begge foreldra ",
                            )
                        }
                        text (
                            Nynorsk to "heile året, er ",
                        )

                        //IF(( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert= true )) THEN   INCLUDE ENDIF
                        showIf((barnetilleggFelles_inntektPeriodisert or barnetilleggSerkull_inntektPeriodisert)){
                            text (
                                Nynorsk to "inntektene ",
                            )
                        }

                        //IF( ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert= true ) AND ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert= true ) )THEN   INCLUDE ENDIF
                        showIf((barnetilleggFelles_inntektPeriodisert or barnetilleggSerkull_inntektPeriodisert) and (barnetilleggFelles_fribelopPeriodisert or barnetilleggSerkull_fribelopPeriodisert)){
                            text (
                                Nynorsk to "og ",
                            )
                        }

                        //IF( ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert= true ) )THEN   INCLUDE ENDIF
                        showIf((barnetilleggFelles_fribelopPeriodisert or barnetilleggSerkull_fribelopPeriodisert)){
                            text (
                                Nynorsk to "fribeløpet ",
                            )
                        }
                        text (
                            Nynorsk to "justert slik at ",
                        )

                        //IF( NOT ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert= true ) )THEN   INCLUDE ENDIF
                        showIf(FUNKSJON_NOT(barnetilleggFelles_inntektPeriodisert or barnetilleggSerkull_inntektPeriodisert)){
                            text (
                                Nynorsk to "det",
                            )
                        }

                        //IF( ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert= true ) )THEN   INCLUDE ENDIF
                        showIf((barnetilleggFelles_inntektPeriodisert or barnetilleggSerkull_inntektPeriodisert)){
                            text (
                                Nynorsk to "dei",
                            )
                        }
                        text (
                            Nynorsk to " berre gjeld for den perioden du får barnetillegg. ",
                        )

                        //PE_Vedtaksdata_Kravhode_KravArsakType = "sivilstandsendring"
                        showIf(kravArsakType.equalTo("sivilstandsendring")){
                            text (
                                Nynorsk to "Fordi sivilstanden din har endra seg, er ",
                            )
                        }

                        //IF( (PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert= true) AND PE_Vedtaksdata_Kravhode_KravArsakType = "sivilstandsendring" ) THEN   INCLUDE ENDIF
                        showIf((barnetilleggFelles_inntektPeriodisert or barnetilleggSerkull_inntektPeriodisert) and kravArsakType.equalTo("sivilstandsendring")){
                            text (
                                Nynorsk to "inntektene ",
                            )
                        }

                        //IF( ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert= true ) AND ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert= true ) AND ( PE_Vedtaksdata_Kravhode_KravArsakType = "sivilstandsendring" ) )THEN   INCLUDE ENDIF
                        showIf((barnetilleggFelles_inntektPeriodisert or barnetilleggSerkull_inntektPeriodisert) and (barnetilleggFelles_fribelopPeriodisert or barnetilleggSerkull_fribelopPeriodisert) and kravArsakType.equalTo("sivilstandsendring")){
                            text (
                                Nynorsk to "og ",
                            )
                        }

                        //IF( ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert= true) AND PE_Vedtaksdata_Kravhode_KravArsakType = "sivilstandsendring"  )THEN   INCLUDE ENDIF
                        showIf((barnetilleggFelles_fribelopPeriodisert or barnetilleggSerkull_fribelopPeriodisert) and kravArsakType.equalTo("sivilstandsendring")){
                            text (
                                Nynorsk to "fribeløpet ",
                            )
                        }

                        //PE_Vedtaksdata_Kravhode_KravArsakType = "sivilstandsendring"
                        showIf(kravArsakType.equalTo("sivilstandsendring")){
                            text (
                                Nynorsk to "justert slik at dei berre gjeld for den framtidige perioden du får barnetillegg. ",
                            )
                        }
                    }
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense < PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak) THEN      INCLUDE ENDIF
            showIf(uforetrygdOrdiner_inntektsgrense.lessThan(uforetrygdOrdiner_inntektstak)){
                includePhrase(TBU1133_Generated)
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false) THEN      INCLUDE ENDIF
            showIf(not(barnetilleggSerkull_innvilget) and not(barnetilleggFelles_BTFBinnvilget) and not(gjenlevendetillegg_GTinnvilget) and not(ektefelletillegg_ETinnvilget)){
                //[TBU2263NN, TBU2263]

                paragraph {
                    text (
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14 og 22-12.",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14 og 22-12.",
                    )
                }
            }

            //IF(  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT  ) THEN      INCLUDE ENDIF
            showIf((barnetilleggSerkull_innvilget or barnetilleggFelles_BTFBinnvilget) and not(gjenlevendetillegg_GTinnvilget) and not(ektefelletillegg_ETinnvilget) and beregningUfore_BelopGammelUT.notEqualTo(beregningUfore_BelopNyUT)){
                //[TBU2264NN, TBU2264]

                paragraph {
                    text (
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14 til 12-16 og 22-12",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14 til 12-16 og 22-12",
                    )

                    //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                    showIf(ufoeretrygd_reduksjonsgrunnlag_barnetilleggRegelverkType.equalTo("overgangsregler_2016")){
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
            showIf((barnetilleggSerkull_innvilget or barnetilleggFelles_BTFBinnvilget) and not(gjenlevendetillegg_GTinnvilget) and ektefelletillegg_ETinnvilget){
                //[TBU2265NN, TBU2265]

                paragraph {
                    text (
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14 til 12-16 og 22-12 og overgangsforskriften §8",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14 til 12-16 og 22-12 og overgangsforskrifta § 8",
                    )

                    //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                    showIf(ufoeretrygd_reduksjonsgrunnlag_barnetilleggRegelverkType.equalTo("overgangsregler_2016")){
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
            showIf((barnetilleggSerkull_innvilget or barnetilleggFelles_BTFBinnvilget) and gjenlevendetillegg_GTinnvilget and not(ektefelletillegg_ETinnvilget)){
                //[TBU2266NN, TBU2266]

                paragraph {
                    text (
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14 til 12-16, 12-18 og 22-12",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14 til 12-16, 12-18 og 22-12",
                    )

                    //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                    showIf(ufoeretrygd_reduksjonsgrunnlag_barnetilleggRegelverkType.equalTo("overgangsregler_2016")){
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
            showIf(not(barnetilleggSerkull_innvilget) and not(barnetilleggFelles_BTFBinnvilget) and gjenlevendetillegg_GTinnvilget and not(ektefelletillegg_ETinnvilget)){
                //[TBU2267NN, TBU2267]

                paragraph {
                    text (
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14, 12- 18 og 22-12.",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14, 12-18 og 22-12.",
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true) THEN      INCLUDE ENDIF
            showIf(not(barnetilleggSerkull_innvilget) and not(barnetilleggFelles_BTFBinnvilget) and not(gjenlevendetillegg_GTinnvilget) and ektefelletillegg_ETinnvilget){
                //[TBU2268NN, TBU2268]

                paragraph {
                    text (
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14 og 22-12 og overgangsforskriften § 8.",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14 og 22-12 og overgangsforskrifta § 8.",
                    )
                }
            }

            //IF((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT = PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT)  THEN      INCLUDE ENDIF
            showIf((barnetilleggSerkull_innvilget or barnetilleggFelles_BTFBinnvilget) and not(gjenlevendetillegg_GTinnvilget) and not(ektefelletillegg_ETinnvilget) and beregningUfore_BelopGammelUT.equalTo(beregningUfore_BelopNyUT)){
                //[TBU4014_NN, TBU4014]

                paragraph {
                    text (
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-15, 12-16 og 22-12",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-15, 12-16 og 22-12",
                    )

                    //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                    showIf(ufoeretrygd_reduksjonsgrunnlag_barnetilleggRegelverkType.equalTo("overgangsregler_2016")){
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
            showIf(beregningUfore_BelopGammelUT.notEqualTo(beregningUfore_BelopNyUT) and beregningUfore_BelopNyUT.greaterThan(0)){
                //[TBU4015_NN, TBU4015]

                paragraph {
                    text (
                        Bokmal to "Hva får du i uføretrygd framover?",
                        Nynorsk to "Kva får du i uføretrygd framover?",
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak > PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense) THEN      INCLUDE ENDIF
                showIf(uforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad.lessThan(beregningUfore_uforegrad) and uforetrygdOrdiner_inntektstak.greaterThan(uforetrygdOrdiner_inntektsgrense)){
                    //[TBU4044_NN, TBU4044]

                    paragraph {
                        textExpr (
                            Bokmal to "Ut fra den årlige inntekten din vil uføretrygden utgjøre ".expr() + PE_UT_NettoAkk_pluss_NettoRestAr.format() + " kroner. ",
                            Nynorsk to "Ut frå den årlege inntekta di vil uføretrygda utgjere ".expr() + PE_UT_NettoAkk_pluss_NettoRestAr.format() + " kroner. ",
                        )

                        //IF(FF_CheckIfFirstDayAndMonthOfYear(PE_VedtaksData_VirkningFOM) = false) THEN      INCLUDE ENDIF
                        showIf(not(virkFomErFoersteJanuar)){
                            textExpr (
                                Bokmal to "Hittil i år har du fått utbetalt ".expr() + uforetrygdOrdiner_nettoAkk.format() + " kroner. ",
                                Nynorsk to "Hittil i år har du fått utbetalt ".expr() + uforetrygdOrdiner_nettoAkk.format() + " kroner. ",
                            )
                        }
                        textExpr (
                            Bokmal to "Du har derfor rett til en utbetaling av uføretrygd på ".expr() + uforetrygdOrdiner_netto.format() + " kroner per måned for resten av året.",
                            Nynorsk to "Du har derfor rett til ei utbetaling av uføretrygd på ".expr() + uforetrygdOrdiner_netto.format() + " kroner per månad for resten av året.",
                        )
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt > PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense) THEN      INCLUDE ENDIF
                showIf(uforetrygdOrdiner_forventetInntekt.greaterThan(uforetrygdOrdiner_inntektsgrense)){
                    //[TBU4045_NN, TBU4045]

                    paragraph {
                        textExpr (
                            Bokmal to "Selv om uføretrygden din er redusert på grunn av inntekt beholder du likevel uføregraden din på ".expr() + beregningUfore_uforegrad.format() + " prosent. Du får utbetalt hele uføretrygden igjen dersom du tjener mindre enn inntektsgrensen din.",
                            Nynorsk to "Sjølv om uføretrygda di er redusert på grunn av inntekt beheld du likevel uføregraden din på ".expr() + beregningUfore_uforegrad.format() + " prosent. Du får utbetalt heile uføretrygda att dersom du tener mindre enn inntektsgrensa di.",
                        )
                    }
                }
            }

            //IF( (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB > 0) OR (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB > 0) ) THEN   INCLUDE ENDIF
            showIf((beregningUfore_BelopGammelBTFB.notEqualTo(beregningUfore_BelopNyBTFB) and beregningUfore_BelopNyBTFB.greaterThan(0)) or (beregningUfore_BelopGammelBTSB.notEqualTo(beregningUfore_BelopNyBTSB) and beregningUfore_BelopNyBTSB.greaterThan(0))){
                //[TBU4046_NN, TBU4046]

                paragraph {
                    text (
                        Bokmal to "Hva får du i barnetillegg framover?",
                        Nynorsk to "Kva får du i barnetillegg framover?",
                    )
                }

                //IF( (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB) AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB <> 0 ) THEN   INCLUDE ENDIF
                showIf(beregningUfore_BelopGammelBTFB.notEqualTo(beregningUfore_BelopNyBTFB) and beregningUfore_BelopNyBTFB.notEqualTo(0)){
                    //[TBU4047_NN, TBU4047]

                    paragraph {
                        textExpr (
                            Bokmal to "Ut fra den samlede inntekten til deg og din ".expr() + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT + " er barnetillegget vurdert på nytt. Barnetillegget er redusert for hele året ut fra den inntekten som overstiger fribeløpet. Den årlige reduksjonen av barnetillegget er " + barnetilleggFelles_avkortningsbeloepPerAar.format() + " kroner.",
                            Nynorsk to "Ut frå dei samla inntektene til deg og ".expr() + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_NN_entall + " din er barnetillegget blitt vurdert på nytt. Barnetillegget er redusert for heile året ut frå inntekta som overstig fribeløpet. Den årlege reduksjonen av barnetillegget er " + barnetilleggFelles_avkortningsbeloepPerAar.format() + " kroner.",
                        )

                        //IF(( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr <> 0 )) THEN   INCLUDE ENDIF
                        showIf(barnetilleggFelles_justeringsbelopPerAr.notEqualTo(0)){
                            text (
                                Bokmal to " Vi har tatt hensyn til hvordan barnetillegget eventuelt har vært redusert i starten av året, og vi har derfor ",
                                Nynorsk to " Vi har teke omsyn til korleis barnetillegget eventuelt har vore redusert i starten av året, og vi har derfor ",
                            )
                        }

                        //IF(( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr <> 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr > 0 )) THEN   INCLUDE ENDIF
                        showIf(barnetilleggFelles_justeringsbelopPerAr.notEqualTo(0) and barnetilleggFelles_justeringsbelopPerAr.greaterThan(0)){
                            text (
                                Bokmal to "lagt til",
                                Nynorsk to "lagt til",
                            )
                        }

                        //IF(( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr <> 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr < 0 )) THEN   INCLUDE ENDIF
                        showIf(barnetilleggFelles_justeringsbelopPerAr.notEqualTo(0) and barnetilleggFelles_justeringsbelopPerAr.lessThan(0)){
                            text (
                                Bokmal to "t" + "rukket fra",
                                Nynorsk to "trekt frå",
                            )
                        }

                        //IF(( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr <> 0 )) THEN   INCLUDE ENDIF
                        showIf(barnetilleggFelles_justeringsbelopPerAr.notEqualTo(0)){
                            textExpr (
                                Bokmal to PE_BarnetilleggFelles_JusteringsbelopPerArUtenMinus.format() + " kroner i det vi reduserer barnetillegget med for resten av året.",
                                Nynorsk to PE_BarnetilleggFelles_JusteringsbelopPerArUtenMinus.format() + " kroner i det vi har redusert barnetillegget med for resten av året.",
                            )
                        }
                        textExpr (
                            Bokmal to " Du har derfor rett til en utbetaling av barnetillegg på ".expr() + barnetilleggFelles_netto.format() + " kroner per måned for resten av året. ",
                            Nynorsk to " Du har derfor rett til ei utbetaling av barnetillegg på ".expr() + barnetilleggFelles_netto.format() + " kroner per månad for resten av året.",
                        )
                    }
                }

                //IF( (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB) AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB <> 0 ) THEN   INCLUDE ENDIF
                showIf(beregningUfore_BelopGammelBTSB.notEqualTo(beregningUfore_BelopNyBTSB) and beregningUfore_BelopNyBTSB.notEqualTo(0)){
                    //[TBU4048_NN, TBU4048]

                    paragraph {
                        textExpr (
                            Bokmal to "Ut fra den samlede inntekten din er barnetillegget vurdert på nytt. Barnetillegget er redusert for hele året ut fra den inntekten som overstiger fribeløpet. Den årlige reduksjonen av barnetillegget er ".expr() + barnetilleggSerkull_avkortingsbelopPerAr.format() + " kroner.",
                            Nynorsk to "Ut frå den samla inntekta di er barnetillegget blitt vurdert på nytt. Barnetillegget er redusert for heile året ut frå inntekta som overstig fribeløpet. Den årlege reduksjonen av barnetillegget er ".expr() + barnetilleggSerkull_avkortingsbelopPerAr.format() + " kroner.",
                        )

                        //IF(( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr <> 0 )) THEN   INCLUDE ENDIF
                        showIf(barnetilleggSerkull_justeringsbelopPerAr.notEqualTo(0)){
                            text (
                                Bokmal to " Vi har tatt hensyn til hvordan barnetillegget eventuelt har vært redusert i starten av året, og vi har derfor ",
                                Nynorsk to " Vi har teke omsyn til korleis barnetillegget eventuelt har vore redusert i starten av året, og vi har derfor ",
                            )
                        }

                        //IF(( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr > 0 )) THEN   INCLUDE ENDIF
                        showIf(barnetilleggSerkull_justeringsbelopPerAr.greaterThan(0)){
                            text (
                                Bokmal to "lagt til",
                                Nynorsk to "lagt til",
                            )
                        }

                        //IF(( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr < 0 )) THEN   INCLUDE ENDIF
                        showIf(barnetilleggSerkull_justeringsbelopPerAr.lessThan(0)){
                            text (
                                Bokmal to "trukket fra",
                                Nynorsk to "trekt frå",
                            )
                        }

                        //IF(( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr <> 0 )) THEN   INCLUDE ENDIF
                        showIf(barnetilleggSerkull_justeringsbelopPerAr.notEqualTo(0)){
                            textExpr (
                                Bokmal to PE_BarnetilleggSerkull_JusteringsbelopPerArUtenMinus.format() + "kroner i det vi reduserer barnetillegget med for resten av året.",
                                Nynorsk to PE_BarnetilleggSerkull_JusteringsbelopPerArUtenMinus.format() + "kroner i det vi reduserte barnetillegget med for resten av året.",
                            )
                        }
                        textExpr (
                            Bokmal to " Du har derfor rett til en utbetaling av barnetillegg på ".expr() + barnetilleggSerkull_netto.format() + " kroner per måned for resten av året.",
                            Nynorsk to " Du har derfor rett til ei utbetaling av barnetillegg på ".expr() + barnetilleggSerkull_netto.format() + " kroner per månad for resten av året.",
                        )
                    }
                }

                //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true) OR (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) ) THEN   INCLUDE ENDIF
                showIf((barnetilleggSerkull_netto.equalTo(0) and barnetilleggSerkull_innvilget) or (barnetilleggFelles_netto.equalTo(0) and barnetilleggFelles_BTFBinnvilget)){
                    //[TBU4049_NN, TBU4049]

                    paragraph {
                        textExpr (
                            Bokmal to "Du får ikke utbetalt barnetillegget for ".expr() + PE_UT_Barnet_Barna_Felles_serkull + " som ",
                            Nynorsk to "Du får ikkje utbetalt barnetillegget for ".expr() + PE_UT_Barnet_Barna_Felles_serkull + " som ",
                        )

                        //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) AND (   PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false   OR   (     PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true     AND     PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto <> 0) ) )THEN   INCLUDE ENDIF
                        showIf(barnetilleggFelles_netto.equalTo(0) and barnetilleggFelles_BTFBinnvilget and (not(barnetilleggSerkull_innvilget) or (barnetilleggSerkull_innvilget and barnetilleggSerkull_netto.notEqualTo(0)))){
                            text (
                                Bokmal to "bor med begge sine foreldre",
                                Nynorsk to "bur med begge foreldra",
                            )
                        }

                        //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true) AND (   PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false   OR   (     PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true     AND     PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0) ) )THEN   INCLUDE ENDIF
                        showIf(barnetilleggSerkull_netto.equalTo(0) and barnetilleggSerkull_innvilget and (not(barnetilleggFelles_BTFBinnvilget) or (barnetilleggFelles_BTFBinnvilget and barnetilleggFelles_netto.notEqualTo(0)))){
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
            showIf(gjenlevendetillegg_GTinnvilget){
                includePhrase(TBU1214_Generated)
                //[TBU2272NN, TBU2272]

                paragraph {
                    text (
                        Bokmal to "Du mottar gjenlevendetillegg i uføretrygden din. Tjener du mer enn inntektsgrensen din, reduserer vi gjenlevendetillegget ditt med samme prosent som vi reduserer uføretrygden din med.",
                        Nynorsk to "Du får attlevandetillegg i uføretrygda di. Tener du meir enn inntektsgrensa di, reduserer vi attlevandetillegget ditt med same prosenten som vi reduserer uføretrygda di med.",
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopRedusert = true AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true) THEN      INCLUDE ENDIF
                showIf(beregningUfore_BelopRedusert and gjenlevendetillegg_GTinnvilget){
                    //[TBU2273NN, TBU2273]

                    paragraph {
                        text (
                            Bokmal to "Gjenlevendetillegget er redusert ut fra den innmeldte inntekten.",
                            Nynorsk to "Attlevandetillegget er redusert ut frå den innmelde inntekta.",
                        )
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopOkt = true AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true) THEN      INCLUDE ENDIF
                showIf(beregningUfore_BelopOkt and gjenlevendetillegg_GTinnvilget){
                    //[TBU2274NN, TBU2274]

                    paragraph {
                        text (
                            Bokmal to "Gjenlevendetillegget er økt ut fra den innmeldte inntekten.",
                            Nynorsk to "Attlevandetillegget er auka ut frå den innmelde inntekta.",
                        )
                    }
                }
                includePhrase(TBU1133_Generated)
            }

            //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true
            showIf(ektefelletillegg_ETinnvilget){
                includePhrase(TBU2275_Generated)
            }

            //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true
            showIf(ektefelletillegg_ETinnvilget){
                //[TBU2276NN, TBU2276]

                paragraph {
                    text (
                        Bokmal to "Du mottar ektefelletillegg i uføretrygden din. Dette tillegget blir ikke endret som følge av inntektsendringer.",
                        Nynorsk to "Du får ektefelletillegg i uføretrygda di. Dette tillegget blir ikkje endra som følgje av inntektsendringar.",
                    )
                }
            }
            //[TBU4022_NN, TBU4022]

            paragraph {
                text (
                    Bokmal to "Du må melde fra om endringer i inntekt",
                    Nynorsk to "Du må melde frå om endringar i inntekt",
                )
            }
            includePhrase(TBU2278_Generated(barnetilleggSerkull_innvilget, barnetilleggFelles_BTFBinnvilget))

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt < PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak > PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense) THEN      INCLUDE ENDIF
            showIf(uforetrygdOrdiner_forventetInntekt.lessThan(uforetrygdOrdiner_inntektstak) and uforetrygdOrdiner_inntektstak.greaterThan(uforetrygdOrdiner_inntektsgrense)){
                includePhrase(TBU2279_Generated(uforetrygdOrdiner_inntektstak, uforetrygdOrdiner_inntektstak))
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt < PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak <= PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense) THEN      INCLUDE ENDIF
            showIf(uforetrygdOrdiner_forventetInntekt.lessThan(uforetrygdOrdiner_inntektstak) and uforetrygdOrdiner_inntektstak.lessThanOrEqual(uforetrygdOrdiner_inntektsgrense)){
                includePhrase(TBU3740_Generated(uforetrygdOrdiner_inntektsgrense))
            }
            includePhrase(TBU2280_Generated(barnetilleggSerkull_innvilget, barnetilleggFelles_BTFBinnvilget))
            //[TBU2282NN, TBU2282]

            paragraph {
                text (
                    Bokmal to "Inntekter som ikke skal føre til reduksjon av uføretrygden",
                    Nynorsk to "Inntekter som ikkje skal føra til reduksjon av uføretrygda",
                )
                text (
                    Bokmal to "Det kan gjøres unntak for enkelte inntektstyper som ikke skal føre til reduksjon av uføretrygden. Dette kan gjelde følgende:",
                    Nynorsk to "Det kan gjerast unntak for enkelte inntektstypar som ikkje skal føra til reduksjon av uføretrygda. Dette kan gjelda følgjande:",
                )
                text (
                    Bokmal to "Erstatning for inntektstap ved erstatningsoppgjør etter",
                    Nynorsk to "Erstatning for inntektstap ved erstatningsoppgjer etter",
                )
                text (
                    Bokmal to "Skadeerstatningsloven § 3-1",
                    Nynorsk to "Skadeerstatningsloven § 3-1",
                )
                text (
                    Bokmal to "Yrkesskadeforsikringsloven § 13",
                    Nynorsk to "Yrkesskadeforsikringsloven § 13",
                )
                text (
                    Bokmal to "Pasientskadeloven § 4 første ledd",
                    Nynorsk to "Pasientskadeloven § 4 første ledd",
                )
                text (
                    Bokmal to "Inntekt fra arbeid eller virksomhet som ble helt avsluttet før du fikk innvilget uføretrygd, for eksempel:",
                    Nynorsk to "Inntekt frå arbeid eller verksemd som vart heilt avslutta før du fekk innvilga uføretrygd, til dømes:",
                )
                text (
                    Bokmal to "Utbetalte feriepenger for et arbeidsforhold som er avsluttet",
                    Nynorsk to "Utbetalte feriepengar for eit arbeidsforhold som er avslutta",
                )
                text (
                    Bokmal to "Inntekter fra salg av produksjonsmidler i forbindelse med opphør av virksomheten",
                    Nynorsk to "Inntekter frå sal av produksjonsmiddel i samband med opphøyr av verksemda",
                )
                text (
                    Bokmal to "Produksjonstillegg og andre overføringer til gårdbrukere",
                    Nynorsk to "Produksjonstillegg og andre overføringar til gardbrukarar",
                )
                text (
                    Bokmal to "Dersom vi mottar dokumentasjon fra deg som bekrefter at du har slik inntekt, kan vi gjøre en ny beregning av uføretrygden din.",
                    Nynorsk to "Dersom vi mottek dokumentasjon frå deg som stadfestar at du har slik inntekt, kan vi gjera ei ny berekning av uføretrygda di.",
                )
            }
            includePhrase(TBU2212_Generated)
            includePhrase(TBU2213_Generated)
            includePhrase(TBU1074_MedMargin_Generated)
            includePhrase(TBU2242_Generated)
            includePhrase(TBU1227_Generated)
            includePhrase(TBU1228_Generated)

            //IF(PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland(1) <> "nor" AND PE_Grunnlag_Persongrunnlagsliste_PersonBostedsland(1) <> "") THEN      INCLUDE ENDIF
            showIf(personBostedsland.notEqualTo("nor") and personBostedsland.notEqualTo("")){
                includePhrase(Ufoeretrygd.SkattForDegSomBorIUtlandet)
            }

            // TODO bruk HarDuSpoersmaalUfoeretrygd
            includePhrase(Ufoeretrygd.HarDuSpoersmaalUfoeretrygd)

            //TBU1074_MedMargin_Generated
            //TBU1076_MedMargin_Generated
            //TBU1077_Generated
            //TBU1120_Generated
            //TBU1121_Generated
            //TBU1122_Generated
            //TBU1123_Generated
            //TBU1128_Generated
            //TBU1133_Generated
            //TBU1214_Generated
            //TBU1227_Generated
            //TBU1228_Generated
            //TBU1253_Generated
            //TBU1254_Generated
            //TBU2212_Generated
            //TBU2213_Generated
            //TBU2223_Generated
            //TBU2242_Generated
            //TBU2245_Generated
            //TBU2247_Generated
            //TBU2275_Generated
            //TBU2278_Generated
            //TBU2279_Generated
            //TBU2280_Generated
            //TBU2283_Generated
            //TBU3730_Generated
            //TBU3740_Generated
        }
    }
}