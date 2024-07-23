package no.nav.pensjon.brev.maler
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.maler.fraser.generated.*
import no.nav.pensjon.brev.maler.fraser.ufoer.Ufoeretrygd
import no.nav.pensjon.brev.maler.fraser.ufoer.Ufoeretrygd.SkattForDegSomBorIUtlandet
import no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere.legacy.FUNKSJON_FF_CheckIfFirstDayAndMonthOfYear
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
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
        val PE_BarnetilleggFelles_JusteringsbelopPerArUtenMinus: Expression<Kroner> = Kroner(4741).expr()
        val PE_BarnetilleggSerkull_JusteringsbelopPerArUtenMinus: Expression<Kroner> = Kroner(1433).expr()
        val PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT: Expression<String> = "<TEXTVARIABEL:2527>".expr()
        val PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_NN_entall: Expression<String> = "<TEXTVARIABEL:7856>".expr()
        val PE_UT_Barnet_Barna_Felles: Expression<String> = "<TEXTVARIABEL:8092>".expr()
        val PE_UT_Barnet_Barna_Felles_serkull: Expression<String> = "<TEXTVARIABEL:7252>".expr()
        val PE_UT_Barnet_Barna_Serkull: Expression<String> = "<TEXTVARIABEL:3572>".expr()
        val PE_UT_NettoAkk_pluss_NettoRestAr: Expression<Kroner> = Kroner(3579).expr()
        val PE_UT_VilFylle67iVirkningFomAr: Expression<Boolean> = true.expr()
        val PE_UT_VirkningFomAr: Expression<String> = "<TEXTVARIABEL:9790>".expr()
        val PE_UT_VirkningstidpunktArMinus1Ar: Expression<Int> = 9413.expr()
        val PE_VedtaksData_VirkningFOM: Expression<LocalDate> = LocalDate.of(2020,5, 4).expr()
        val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_AvkortingsbelopPerAr: Expression<Kroner> = Kroner(6996).expr()
        val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert: Expression<Boolean> = true.expr()
        val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert: Expression<Boolean> = true.expr()
        val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_Inntektstak: Expression<Kroner> = Kroner(4526).expr()
        val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr: Expression<Kroner> = Kroner(8997).expr()
        val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBbelopFratrukketAnnenForeldersInntekt: Expression<Kroner> = Kroner(5671).expr()
        val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBinntektAnnenForelder: Expression<Kroner> = Kroner(4891).expr()
        val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_AvkortingsbelopPerAr: Expression<Kroner> = Kroner(9204).expr()
        val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert: Expression<Boolean> = true.expr()
        val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert: Expression<Boolean> = true.expr()
        val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_Inntektstak: Expression<Kroner> = Kroner(4190).expr()
        val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr: Expression<Kroner> = Kroner(4238).expr()
        val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType: Expression<String> = "<TEXTVARIABEL:3301>".expr()
        val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType: Expression<String> = "<TEXTVARIABEL:1357>".expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopOkt: Expression<Boolean> = true.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopRedusert: Expression<Boolean> = true.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB: Expression<Kroner> = Kroner(6999).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB: Expression<Kroner> = Kroner(6472).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB: Expression<Kroner> = Kroner(791).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB: Expression<Kroner> = Kroner(6171).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT: Expression<Kroner> = Kroner(9076).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT: Expression<Kroner> = Kroner(8780).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningVirkningDatoFom: Expression<LocalDate> = LocalDate.of(2020,2, 22).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBBrukersInntektTilAvkortning: Expression<Kroner> = Kroner(1146).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget: Expression<Boolean> = true.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortingsbelopPerAr: Expression<Double> = 44.0.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt: Expression<Kroner> = Kroner(4219).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense: Expression<Kroner> = Kroner(5362).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak: Expression<Kroner> = Kroner(1745).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad: Expression<Int> = 7824.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Netto: Expression<Double> = 20.0.expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_NettoAkk: Expression<Kroner> = Kroner(2888).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_TotalNetto: Expression<Kroner> = Kroner(4796).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Grunnbelop: Expression<Kroner> = Kroner(9171).expr()
        val PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad: Expression<Int> = 2410.expr()
        val PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBInntektBruktiAvkortning: Expression<Kroner> = Kroner(3663).expr()
        val PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBfribelop: Expression<Kroner> = Kroner(5263).expr()
        val PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget: Expression<Boolean> = true.expr()
        val PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto: Expression<Kroner> = Kroner(1844).expr()
        val PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning: Expression<Kroner> = Kroner(2891).expr()
        val PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBfribelop: Expression<Kroner> = Kroner(261).expr()
        val PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget: Expression<Boolean> = true.expr()
        val PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto: Expression<Kroner> = Kroner(9072).expr()
        val PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget: Expression<Boolean> = true.expr()
        val PE_Vedtaksdata_Kravhode_KravArsakType: Expression<String> = "<TEXTVARIABEL:806>".expr()
        val brukerBorINorge = true.expr()

        title {
            showIf(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT.notEqualTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT)) {
                showIf(
                    PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB.equalTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB)
                            and PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB.equalTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB)
                ) {
                    text(
                        Bokmal to "NAV har endret utbetalingen av uføretrygden din",
                        Nynorsk to "NAV har endra utbetalinga av uføretrygda di",
                    )
                }.orShow {
                    text(
                        Bokmal to "NAV har endret utbetalingen av uføretrygden og ",
                        Nynorsk to "NAV har endra utbetalinga av uføretrygda di og ",
                    )
                    showIf(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget) {
                        text(
                            Bokmal to "barnetilleggene dine",
                            Nynorsk to "barnetillegga di",
                        )
                    }.orShow {
                        text(
                            Bokmal to "barnetillegget ditt",
                            Nynorsk to "barnetillegget ditt",
                        )
                    }
                }
            }.orShowIf(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB.equalTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB)
                    and PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB.equalTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB)){
                text(
                    Bokmal to "NAV har endret utbetalingen av ",
                    Nynorsk to "NAV har endra utbetalinga av ",
                )
                showIf(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget) {
                    text(
                        Bokmal to "barnetilleggene",
                        Nynorsk to "barnetillegga",
                    )
                }.orShow{
                    text(
                        Bokmal to "barnetillegget",
                        Nynorsk to "barnetillegget",
                    )
                }
                text(
                    Bokmal to " i uføretrygden din",
                    Nynorsk to " i uføretrygda di",
                )

            }
        }
        outline {

            //IF(FF_CheckIfFirstDayAndMonthOfYear(PE_VedtaksData_VirkningFOM) = false) THEN      INCLUDE ENDIF
            showIf(not(FUNKSJON_FF_CheckIfFirstDayAndMonthOfYear(PE_VedtaksData_VirkningFOM))){
                //[TBU2249NN, TBU2249]

                paragraph {
                    text (
                        Bokmal to "Vi har mottatt nye opplysninger om inntekten",
                        Nynorsk to "Vi har fått nye opplysningar om inntekta",
                    )

                    //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false
                    showIf(not(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget)){
                        text (
                            Bokmal to " din",
                            Nynorsk to " di",
                        )
                    }

                    //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
                    showIf(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget){
                        textExpr (
                            Bokmal to " til deg eller din ".expr() + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT + ". Inntekten til din " + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT + " har kun betydning for størrelsen på barnetillegget ",
                            Nynorsk to " til deg eller ".expr() + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_NN_entall + " din. Inntekta til " + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_NN_entall + " din har berre betydning for storleiken på barnetillegget ",
                        )
                        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true) THEN      INCLUDE ENDIF
                        showIf(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget){
                            textExpr (
                                Bokmal to "for ".expr() + PE_UT_Barnet_Barna_Felles + " som bor med begge sine foreldre",
                                Nynorsk to "for ".expr() + PE_UT_Barnet_Barna_Felles + " som bur saman med begge foreldra sine",
                            )
                        }.orShow{
                            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false) THEN      INCLUDE ENDIF
                            text (
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
                    showIf(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT.notEqualTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT)){
                        text (
                            Bokmal to "uføretrygden din ",
                            Nynorsk to "uføretrygda di ",
                        )
                        //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT  AND (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB  OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB)) THEN      INCLUDE ENDIF
                        showIf(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB.notEqualTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB) or PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB.notEqualTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB)){
                            text (
                                Bokmal to "og ",
                                Nynorsk to "og ",
                            )
                        }
                    }

                    //IF(((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false)  OR (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true))) AND (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB  OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB)  THEN      INCLUDE ENDIF
                    showIf(((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget and not(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget)) or (not(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget) and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget)) and (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB.notEqualTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB) or PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB.notEqualTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB))){
                        text (
                            Bokmal to "barnetillegget ditt ",
                            Nynorsk to "barnetillegget ditt ",
                        )
                    }

                    //IF((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)) AND (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB  OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB)  THEN      INCLUDE ENDIF
                    showIf(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget and (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB.notEqualTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB) or PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB.notEqualTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB))){
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
            showIf(FUNKSJON_FF_CheckIfFirstDayAndMonthOfYear(PE_VedtaksData_VirkningFOM) and PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT.notEqualTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT)){
                paragraph {
                    textExpr(
                        Bokmal to "Vi vil bruke en inntekt på ".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt.format() + " kroner når vi reduserer uføretrygden din for " + PE_UT_VirkningFomAr + ". Har du ikke meldt inn ny inntekt for " + PE_UT_VirkningFomAr + ", er inntekten justert opp til dagens verdi.",
                        Nynorsk to "Vi vil bruke ei inntekt på ".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt.format() + " kroner når vi reduserer uføretrygda di for " + PE_UT_VirkningFomAr + ". Har du ikkje meldt inn ny inntekt for " + PE_UT_VirkningFomAr + ", er inntekta justert opp til dagens verdi.",
                    )

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad) THEN      INCLUDE ENDIF
                    showIf(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad.notEqualTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad)) {
                        textExpr(
                            Bokmal to "Fikk du innvilget uføretrygd etter januar ".expr() + PE_UT_VirkningstidpunktArMinus1Ar.format() + ", er inntekten justert opp slik at den gjelder for hele " + PE_UT_VirkningFomAr + ".",
                            Nynorsk to "Fekk du innvilga uføretrygd etter januar ".expr() + PE_UT_VirkningstidpunktArMinus1Ar.format() + ", er inntekta også justert opp slik at den gjeld for heile " + PE_UT_VirkningFomAr + ".",
                        )
                    }

                    //PE_UT_VilFylle67iVirkningFomAr = true
                    showIf(PE_UT_VilFylle67iVirkningFomAr) {
                        textExpr(
                            Bokmal to " Fordi du fyller 67 år i ".expr() + PE_UT_VirkningFomAr + ", er inntekten justert i forhold til antall måneder du mottar uføretrygd.",
                            Nynorsk to " Fordi du fyljer 67 år i ".expr() + PE_UT_VirkningFomAr + ", er inntekta justert ut frå talet på månadar du får uføretrygd.",
                        )
                    }
                }
            }
            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT  AND  (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB  OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB)) AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget= true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false) AND FF_CheckIfFirstDayAndMonthOfYear(PE_VedtaksData_VirkningFOM) = true THEN  INCLUDE ENDIF
            showIf(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT.notEqualTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT) and (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB.notEqualTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB) or PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB.notEqualTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB)) and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget and not(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget) and FUNKSJON_FF_CheckIfFirstDayAndMonthOfYear(PE_VedtaksData_VirkningFOM)){
                //[TBU4016_NN, TBU4016]

                paragraph {
                    textExpr (
                        Bokmal to "I reduksjonen av barnetillegget ditt vil vi bruke en inntekt på ".expr() + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning.format() + " kroner. ",
                        Nynorsk to "I reduksjonen av barnetillegget ditt vil vi bruke ei inntekt på ".expr() + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning.format() + " kroner.",
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT  AND (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB  OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB)) AND (FF_CheckIfFirstDayAndMonthOfYear(PE_VedtaksData_VirkningFOM) = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true )     THEN  INCLUDE ENDIF
            showIf(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT.notEqualTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT) and (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB.notEqualTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB) or PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB.notEqualTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB)) and FUNKSJON_FF_CheckIfFirstDayAndMonthOfYear(PE_VedtaksData_VirkningFOM) and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget){
                //[TBU4001]
                paragraph {

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT  AND (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB  OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB)) THEN      INCLUDE ENDIF
                    showIf(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT.notEqualTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT) and (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB.notEqualTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB) or PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB.notEqualTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB))){
                        text (
                            Bokmal to "I reduksjonen av barnetillegg",
                            Nynorsk to "I reduksjonen av barnetillegg",
                        )
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT  AND (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB  OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB)) AND FF_CheckIfFirstDayAndMonthOfYear(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningVirkningDatoFom) AND ((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true))     THEN  INCLUDE ENDIF
                    showIf(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT.notEqualTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT) and (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB.notEqualTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB) or PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB.notEqualTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB)) and FUNKSJON_FF_CheckIfFirstDayAndMonthOfYear(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningVirkningDatoFom) and (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget or PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget)){
                        text (
                            Bokmal to "et ditt ",
                            Nynorsk to "et ditt ",
                        )
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT  AND (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB  OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB)) THEN      INCLUDE ENDIF
                    showIf(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT.notEqualTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT) and (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB.notEqualTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB) or PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB.notEqualTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB))){
                        textExpr (
                            Bokmal to "vil vi bruke en inntekt på ".expr() + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBInntektBruktiAvkortning.format() + " kroner ",
                            Nynorsk to "vil vi bruke ei inntekt på ".expr() + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBInntektBruktiAvkortning.format() + " kroner ",
                        )
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true) THEN      INCLUDE ENDIF
                    showIf(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget){
                        textExpr (
                            Bokmal to "for ".expr() + PE_UT_Barnet_Barna_Felles + " som bor med begge sine foreldre. For " + PE_UT_Barnet_Barna_Serkull + " som ikke bor sammen med begge foreldrene vil vi bruke en inntekt på " + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning.format() + " kroner",
                            Nynorsk to "for ".expr() + PE_UT_Barnet_Barna_Felles + " som bur med begge sine foreldra. For " + PE_UT_Barnet_Barna_Serkull + " som ikkje bur saman med begge foreldrea vil vi bruke ei inntekt på " + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning.format() + " kroner",
                        )
                    }
                    text (
                        Bokmal to ".",
                        Nynorsk to ".",
                    )
                }
            }


            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT = PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB  AND  (FF_CheckIfFirstDayAndMonthOfYear(PE_VedtaksData_VirkningFOM) = true) )     THEN  INCLUDE ENDIF
            showIf(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT.equalTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT) and PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB.notEqualTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB) and FUNKSJON_FF_CheckIfFirstDayAndMonthOfYear(PE_VedtaksData_VirkningFOM)){
                //[TBU4002_NN, TBU4002]

                paragraph {
                    textExpr (
                        Bokmal to "Vi vil bruke en inntekt på ".expr() + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBInntektBruktiAvkortning.format() + " kroner når vi reduserer barnetillegget ",
                        Nynorsk to "Vi vil bruke ei inntekt på ".expr() + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBInntektBruktiAvkortning.format() + " kroner når vi reduserer barnetillegget ",
                    )

                    //IF((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false)  OR (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true)) THEN      INCLUDE ENDIF
                    showIf((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget and not(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget)) or (not(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget) and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget)){
                        text (
							Bokmal to "ditt ",
							Nynorsk to "ditt ",
                        )
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true) THEN      INCLUDE ENDIF
                    showIf(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget){
                        textExpr (
                            Bokmal to "for ".expr() + PE_UT_Barnet_Barna_Felles + " som bor med begge sine foreldre for " + PE_UT_VirkningFomAr + ". For " + PE_UT_Barnet_Barna_Serkull + " som ikke bor sammen med begge foreldrene vil vi bruke en inntekt på " + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning.format() + " kroner. ",
                            Nynorsk to "for ".expr() + PE_UT_Barnet_Barna_Felles + " som bur saman med begge foreldra sine for " + PE_UT_VirkningFomAr + ". For " + PE_UT_Barnet_Barna_Serkull + " som ikkje bur saman med begge foreldra vil vi bruke ei inntekt på " + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning.format() + " kroner. ",
                        )
                    }
                    text (
                        Bokmal to "Har du ikke meldt inn ny",
                        Nynorsk to "Har du ikkje meldt inn ",
                    )

                    //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
                    showIf(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget){
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
                    showIf(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget){
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
                    showIf(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget){
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
                    showIf(not(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget)){
                        text (
                            Bokmal to "",
                            Nynorsk to "inntekta",
                        )
                    }

                    //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
                    showIf(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget){
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
            showIf(FUNKSJON_FF_CheckIfFirstDayAndMonthOfYear(PE_VedtaksData_VirkningFOM) and PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB.equalTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB) and PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB.notEqualTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB) and PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT.equalTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT)){
                //[TBU4017_NN, TBU4017]

                paragraph {
                    textExpr (
                        Bokmal to "Vi vil bruke en inntekt på ".expr() + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning.format() + " kroner når vi reduserer barnetillegget ditt. Har du ikke meldt inn ny inntekt for " + PE_UT_VirkningFomAr + ", er inntekten justert opp til dagens verdi.",
                        Nynorsk to "Vi vil bruke ei inntekt på ".expr() + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning.format() + " kroner når vi reduserer barnetillegget ditt. Har du ikkje meldt inn ny inntekt for " + PE_UT_VirkningFomAr + ", er inntekta justert opp til dagens verdi.",
                    )
                }
            }

            //IF( (PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad) AND  ((PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB  <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB) OR (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB  <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB)) AND FF_CheckIfFirstDayAndMonthOfYear(PE_VedtaksData_VirkningFOM) = true ) THEN      INCLUDE ENDIF
            showIf(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad.equalTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad) and ((PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB.notEqualTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB)) or (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB.notEqualTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB))) and FUNKSJON_FF_CheckIfFirstDayAndMonthOfYear(PE_VedtaksData_VirkningFOM)){
                //[TBU4013_NN, TBU4013]

                paragraph {
                    textExpr (
                        Bokmal to "Fikk du innvilget uføretrygd etter januar ".expr() + PE_UT_VirkningstidpunktArMinus1Ar.format() + ", er inntekten justert opp slik at den gjelder for hele " + PE_UT_VirkningFomAr + ".",
                        Nynorsk to "Fekk du innvilga uføretrygd etter januar ".expr() + PE_UT_VirkningstidpunktArMinus1Ar.format() + ", er inntekta justert opp slik at den gjeld for heile " + PE_UT_VirkningFomAr + ".",
                    )
                }
            }

            //IF ( FF_CheckIfFirstDayAndMonthOfYear(PE_VedtaksData_VirkningFOM) = true AND  ( PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB > PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB  OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB > PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB ) AND  ( PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB > 0 OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB > 0 ) AND ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert= true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert= true ) )THEN  INCLUDE ENDIF
            showIf(FUNKSJON_FF_CheckIfFirstDayAndMonthOfYear(PE_VedtaksData_VirkningFOM) and (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB.greaterThan(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB) or PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB.greaterThan(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB)) and (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB.greaterThan(0) or PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB.greaterThan(0)) and (PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert or PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert or PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert or PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert)){
                //[TBU4003_NN, TBU4003]

                paragraph {
                    text (
                        Bokmal to "Fordi du ikke har barnetillegg ",
                        Nynorsk to "Fordi du ikkje har barnetillegg ",
                    )

                    //IF( ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = true AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert= false ) OR ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert= false ) AND ( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true ) ) THEN      INCLUDE ENDIF
                    showIf(((PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert and not(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert)) or (PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert and not(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert)) and (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget))){
                        textExpr (
                            Bokmal to "for ".expr() + PE_UT_Barnet_Barna_Felles + " som bor med begge sine foreldre ",
                            Nynorsk to "for ".expr() + PE_UT_Barnet_Barna_Felles + " som bur saman med begge foreldra ",
                        )
                    }

                    //IF( ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert= true ) OR ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert= true ) AND ( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true ) )THEN   INCLUDE ENDIF
                    showIf((not(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert) and PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert) or (not(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert) and PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert) and (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget)){
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
                    showIf((PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert or PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert)){
                        text (
                            Bokmal to "inntektene ",
                            Nynorsk to "inntektene ",
                        )
                    }

                    //IF( ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert= true ) AND ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert= true ) )THEN   INCLUDE ENDIF
                    showIf((PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert or PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert) and (PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert or PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert)){
                        text (
                            Bokmal to "og ",
                            Nynorsk to "og ",
                        )
                    }

                    //IF( ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert= true ) )THEN   INCLUDE ENDIF
                    showIf((PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert or PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert)){
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
            showIf(FUNKSJON_FF_CheckIfFirstDayAndMonthOfYear(PE_VedtaksData_VirkningFOM)){
                //[TBU4004_NN, TBU4004]

                paragraph {
                    text (
                        Bokmal to "Forventer du ",
                        Nynorsk to "Forventar du ",
                    )

                    //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
                    showIf(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget){
                        textExpr (
                            Bokmal to "og din ".expr() + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT + " ",
                            Nynorsk to "og ".expr() + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_NN_entall + " din ",
                        )
                    }

                    textExpr (
                        Bokmal to "å tjene noe annet i ".expr() + PE_UT_VirkningFomAr + " er det viktig at du melder inn ny forventet inntekt. Dette kan du gjøre på nav.no.",
                        Nynorsk to "å tene noko anna i ".expr() + PE_UT_VirkningFomAr + " er det viktig at du melder inn ei ny forventa inntekt. Dette kan du gjere på nav.no.",
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "") THEN      INCLUDE ENDIF
            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "") THEN      INCLUDE ENDIF
            showIf((not(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget) and not(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget) and not(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget) and not(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget) and PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType.equalTo(""))){
                includePhrase(TBU1120_Generated(PE_Vedtaksdata_BeregningsData_BeregningUfore_TotalNetto))
            }

            //IF(  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = ""  ) THEN      INCLUDE ENDIF
            showIf(((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget or PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget) and not(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget) and not(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget) and PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType.equalTo(""))){
                includePhrase(TBU1121_Generated(PE_Vedtaksdata_BeregningsData_BeregningUfore_TotalNetto))
            }

            //IF(  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true  ) THEN      INCLUDE ENDIF
            showIf(((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget or PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget) and not(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget) and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget)){
                includePhrase(TBU1254_Generated(PE_Vedtaksdata_BeregningsData_BeregningUfore_TotalNetto))
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true) THEN      INCLUDE ENDIF
            showIf((not(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget) and not(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget) and not(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget) and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget)){
                includePhrase(TBU1253_Generated(PE_Vedtaksdata_BeregningsData_BeregningUfore_TotalNetto))
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "") THEN      INCLUDE ENDIF
            showIf((not(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget) and not(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget) and PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget and not(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget) and PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType.equalTo(""))){
                includePhrase(TBU1122_Generated(PE_Vedtaksdata_BeregningsData_BeregningUfore_TotalNetto))
            }

            //IF(  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false  ) THEN      INCLUDE ENDIF
            showIf(((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget or PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget) and PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget and not(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget))){
                includePhrase(TBU1123_Generated(PE_Vedtaksdata_BeregningsData_BeregningUfore_TotalNetto))
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_TotalNetto > 0) THEN      INCLUDE ENDIF
            showIf((PE_Vedtaksdata_BeregningsData_BeregningUfore_TotalNetto.greaterThan(0))){
                includePhrase(Ufoeretrygd.UtbetalingsdatoUfoeretrygd(PE_Vedtaksdata_BeregningsData_BeregningUfore_TotalNetto.greaterThan(0)))
            }
            includePhrase(Ufoeretrygd.ViktigAALeseHeleBrevet)
            //[TBU1029]

            includePhrase(Vedtak.BegrunnelseOverskrift)

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT) THEN      INCLUDE ENDIF
            showIf(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT.notEqualTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT)){

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense < PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak) THEN      INCLUDE ENDIF
                showIf(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense.lessThan(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak)){
                    //[TBU2253NN, TBU2253]

                    paragraph {
                        text (
                            Bokmal to "Når vi endrer utbetalingen av uføretrygden din, tar vi utgangspunkt i inntekten du har ved siden av uføretrygden. Det er bare den delen av inntekten din som overstiger inntektsgrensen som vil gi en reduksjon av uføretrygden.",
                            Nynorsk to "Når vi endrar utbetalinga av uføretrygda di, tek vi utgangspunkt i inntekta du har ved sida av uføretrygda. Det er berre den delen av inntekta di som kjem over inntektsgrensa som gir ein reduksjon av uføretrygda di.",
                        )

                        //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt > PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT > PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT > 0) THEN      INCLUDE ENDIF
                        showIf(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt.greaterThan(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense) and PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT.greaterThan(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT) and PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT.greaterThan(0)){
                            text (
                                Bokmal to "Uføretrygden din reduseres fordi du tjener over inntektsgrensen din. Selv om du får en reduksjon lønner det seg likevel å jobbe ved siden av uføretrygden. ",
                                Nynorsk to "Uføretrygda di blir redusert fordi du tener over inntektsgrensa di. Sjølv om du får ein reduksjon, lønner det seg likevel å jobbe ved sida av uføretrygda. ",
                            )
                        }

                        //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt > PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT < PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT > 0) THEN      INCLUDE ENDIF
                        showIf(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt.greaterThan(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense) and PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad.equalTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad) and PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT.lessThan(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT) and PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT.greaterThan(0)){
                            text (
                                Bokmal to "Endringen i inntekten din gjør at uføretrygden ikke lenger er redusert. ",
                                Nynorsk to "Endringa i inntekta di gjer at uføretrygda ikkje lenger er redusert. ",
                            )
                        }

                        //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt < PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT > 0) THEN      INCLUDE ENDIF
                        showIf(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt.lessThan(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense) and PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad.equalTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad) and PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT.greaterThan(0)){
                            text (
                                Bokmal to "Utbetalingen av uføretrygden din økes fordi du tjener under inntektsgrensen din. Det betyr at uføretrygden ikke lenger er redusert. ",
                                Nynorsk to "Utbetalinga av uføretrygda aukar fordi du tener under inntektsgrensa di. Det betyr at uføretrygda ikkje lenger er redusert. ",
                            )
                        }

                        //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT = 0 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt <= PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak) THEN      INCLUDE ENDIF
                        showIf(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT.equalTo(0) and PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt.lessThanOrEqual(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak)){
                            text (
                                Bokmal to "Endring i inntekten din gjør at du ikke får utbetalt uføretrygd for resten av året. ",
                                Nynorsk to "Endring i inntekta di gjer at du ikkje får utbetalt uføretrygd for resten av året. ",
                            )
                        }
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense >= PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak) THEN      INCLUDE ENDIF
                showIf(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense.greaterThanOrEqual(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak)){
                    //[TBU3734NN, TBU3734]

                    paragraph {
                        text (
                            Bokmal to "Når vi endrer utbetalingen av uføretrygden din, tar vi utgangspunkt i inntekten du har ved siden av uføretrygden.",
                            Nynorsk to "Når vi endrar utbetalinga av uføretrygda di, tek vi utgangspunkt i inntekta du har ved sida av uføretrygda.",
                        )

                        //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopRedusert = true AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt > PE_Vedtaksdata_BeregningsData_BeregningUforePeriode_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense) THEN      INCLUDE ENDIF
                        showIf(PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopRedusert and PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt.greaterThan(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense)){
                            text (
                                Bokmal to " Det lønner seg likevel å jobbe, fordi inntekt og uføretrygd alltid vil være høyere enn uføretrygd alene.",
                                Nynorsk to " Det lønner seg likevel å jobbe fordi inntekt og uføretrygd vil alltid vere høgare enn uføretrygd åleine.",
                            )
                        }
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt > PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT > 0) THEN      INCLUDE ENDIF
                showIf(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt.greaterThan(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense) and PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT.greaterThan(0)){
                    //[TBU4005_NN, TBU4005]

                    paragraph {
                        textExpr (
                            Bokmal to "Siden du har en inntekt på ".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt.format() + " kroner trekker vi " + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortingsbelopPerAr.format() + " kroner fra uføretrygden ",
                            Nynorsk to "Fordi du har ei inntekt på ".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt.format() + " kroner trekkjer vi " + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortingsbelopPerAr.format() + " kroner frå uføretrygda ",
                        )

                        //IF(FF_CheckIfFirstDayAndMonthOfYear(PE_VedtaksData_VirkningFOM) = false) THEN      INCLUDE ENDIF
                        showIf(not(FUNKSJON_FF_CheckIfFirstDayAndMonthOfYear(PE_VedtaksData_VirkningFOM))){
                            text (
                                Bokmal to "i ",
                                Nynorsk to "i ",
                            )
                        }

                        //IF(FF_CheckIfFirstDayAndMonthOfYear(PE_VedtaksData_VirkningFOM) = true) THEN      INCLUDE ENDIF
                        showIf(FUNKSJON_FF_CheckIfFirstDayAndMonthOfYear(PE_VedtaksData_VirkningFOM)){
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
                showIf(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt.greaterThan(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak) and PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense.notEqualTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak) and PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT.equalTo(0)){
                    //[TBU2258NN, TBU2258]

                    paragraph {
                        textExpr (
                            Bokmal to "Du får ikke utbetalt uføretrygd siden inntekten din er høyere enn 80 prosent av inntekten du hadde før du ble ufør, det vil si ".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak.format() + " kroner. Inntekten vi har brukt er " + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt.format() + " kroner og du vil derfor ikke få utbetalt uføretrygd resten av året.",
                            Nynorsk to "Du får ikkje utbetalt uføretrygd fordi inntekta di er høgare enn 80 prosent av inntekta du hadde før du blei ufør, det vil seie ".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak.format() + " kroner. Inntekta vi har brukt er " + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt.format() + " kroner og du vil ikkje få utbetalt uføretrygd resten av året.",
                        )
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt > PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense = PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT = 0) THEN      INCLUDE ENDIF
                showIf(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt.greaterThan(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense) and PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense.equalTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak) and PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT.equalTo(0)){
                    //[TBU3735NN, TBU3735]

                    paragraph {
                        textExpr (
                            Bokmal to "Det utbetales ikke uføretrygd når inntekten din utgjør mer enn inntektsgrensen, det vil si ".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense.format() + " kroner. Inntekten vi har brukt er " + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt.format() + " kroner og du vil derfor ikke få utbetalt uføretrygd resten av året.",
                            Nynorsk to "Det blir ikkje utbetalt uføretrygd når inntekta di utgjer meir enn inntektsgrensa di, det vil seie ".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense.format() + " kroner. Inntekta vi har brukt er " + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt.format() + " kroner og du vil derfor ikkje få utbetalt uføretrygd resten av året.",
                        )
                    }
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB) THEN      INCLUDE ENDIF
            showIf(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB.notEqualTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB) or PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB.notEqualTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB)){

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true) THEN      INCLUDE ENDIF
                showIf(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget or PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget){
                    //[TBU4006_NN, TBU4006]

                    paragraph {
                        text (
                            Bokmal to "Inntekten din har ",
                            Nynorsk to "Inntekta di har ",
                        )

                        //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT  AND (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB  OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB)) THEN      INCLUDE ENDIF
                        showIf(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT.notEqualTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT) and (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB.notEqualTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB) or PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB.notEqualTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB))){
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
                        showIf(not(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget) and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget){
                            text (
                                Bokmal to "Fordi",
                                Nynorsk to "Fordi",
                            )
                        }

                        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) THEN      INCLUDE ENDIF
                        showIf(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget){
                            text (
                                Bokmal to "For",
                                Nynorsk to "For",
                            )
                        }

                        //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
                        showIf(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget){
                            textExpr (
                                Bokmal to " ".expr() + PE_UT_Barnet_Barna_Felles + " ",
                                Nynorsk to " ".expr() + PE_UT_Barnet_Barna_Felles + " ",
                            )
                        }

                        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) THEN      INCLUDE ENDIF
                        showIf(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget){
                            text (
                                Bokmal to "som ",
                                Nynorsk to "som ",
                            )
                        }

                        //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
                        showIf(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget){
                            textExpr (
                                Bokmal to "bor med begge sine foreldre, bruker vi i tillegg din ".expr() + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT + "s inntekt når vi fastsetter størrelsen på barnetillegget ditt. ",
                                Nynorsk to "bur saman med begge foreldra sine, bruker vi i tillegg ".expr() + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_NN_entall + " din si inntekt når vi fastset storleiken på barnetillegget ditt. ",
                            )
                        }

                        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) THEN      INCLUDE ENDIF
                        showIf(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget){
                            textExpr (
                                Bokmal to "For ".expr() + PE_UT_Barnet_Barna_Serkull + " som ikke bor sammen med begge foreldrene bruker vi kun inntekten din. ",
                                Nynorsk to "For ".expr() + PE_UT_Barnet_Barna_Serkull + " som ikkje bur saman med begge foreldra bruker vi berre inntekta di. ",
                            )
                        }
                        text (
                            Bokmal to "Uføretrygden ",
                            Nynorsk to "Uføretrygda ",
                        )

                        //PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false
                        showIf(not(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget)){
                            text (
                                Bokmal to "din ",
                                Nynorsk to "di ",
                            )
                        }

                        //PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true
                        showIf(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget){
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
                showIf(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget){
                    //[TBU4007_NN, TBU4007]

                    paragraph {
                        textExpr (
                            Bokmal to "Vi har beregnet barnetillegget på nytt ut fra inntekten din på ".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBBrukersInntektTilAvkortning.format() + " kroner og inntekten til din " + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT + " på " + PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBinntektAnnenForelder.format() + " kroner. ",
                            Nynorsk to "Vi har berekna barnetillegget på nytt ut frå inntekta di på ".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBBrukersInntektTilAvkortning.format() + " kroner og inntekta til " + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_NN_entall + " din på " + PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBinntektAnnenForelder.format() + " kroner. ",
                        )

                        //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBbelopFratrukketAnnenForeldersInntekt > 0) THEN      INCLUDE ENDIF
                        showIf((PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBbelopFratrukketAnnenForeldersInntekt.greaterThan(0))){
                            textExpr (
                                Bokmal to "Folketrygdens grunnbeløp på inntil ".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Grunnbelop.format() + " kroner er holdt utenfor din " + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT + "s inntekt. ",
                                Nynorsk to "Grunnbeløpet i folketrygda på inntil ".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Grunnbelop.format() + " kroner er held utanfor inntekta til " + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_NN_entall + " din. ",
                            )
                        }
                        textExpr (
                            Bokmal to "Til sammen utgjør disse inntektene ".expr() + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBInntektBruktiAvkortning.format() + " kroner. ",
                            Nynorsk to "Til saman utgjer desse inntektene ".expr() + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBInntektBruktiAvkortning.format() + " kroner. ",
                        )

                        showIf(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr.equalTo(0) and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto.greaterThan(0)){
                            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto > 0) THEN      INCLUDE ENDIF
                            text (
                                Bokmal to "Dette beløpet er ",
                                Nynorsk to "Dette beløpet er ",
                            )

                            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBInntektBruktiAvkortning > PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBfribelop AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto > 0) THEN      INCLUDE ENDIF
                            showIf(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBInntektBruktiAvkortning.greaterThan(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBfribelop)){
                                text (
                                    Bokmal to "høyere",
                                    Nynorsk to "høgare",
                                )
                            }

                            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBInntektBruktiAvkortning <= PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBfribelop AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto > 0) THEN      INCLUDE ENDIF
                            showIf(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBInntektBruktiAvkortning.lessThanOrEqual(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBfribelop)){
                                text (
                                    Bokmal to "lavere",
                                    Nynorsk to "lågare",
                                )
                            }

                            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto > 0) THEN      INCLUDE ENDIF
                            textExpr (
                                Bokmal to " enn fribeløpsgrensen på ".expr() + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBfribelop.format() + " kroner. Derfor er barnetillegget ",
                                Nynorsk to " enn fribeløpet på ".expr() + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBfribelop.format() + " kroner. Derfor er barnetillegget ",
                            )

                            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto > 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0) THEN      INCLUDE ENDIF
                            showIf(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget and  PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto.greaterThan(0) ){
                                textExpr (
                                    Bokmal to "for ".expr() + PE_UT_Barnet_Barna_Felles + " som bor med begge sine foreldre ",
                                    Nynorsk to "for ".expr() + PE_UT_Barnet_Barna_Felles + " som bur saman med begge sine foreldra ",
                                )
                            }

                            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBInntektBruktiAvkortning <= PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBfribelop AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto > 0) THEN      INCLUDE ENDIF
                            showIf(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBInntektBruktiAvkortning.lessThanOrEqual(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBfribelop) ){
                                text (
                                    Bokmal to "ikke lenger ",
                                    Nynorsk to "ikkje lenger ",
                                )
                            }

                            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto > 0) THEN      INCLUDE ENDIF
                            text (
                                Bokmal to "redusert. ",
                                Nynorsk to "redusert. ",
                            )
                        }


                        //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr <> 0) THEN      INCLUDE ENDIF
                        showIf(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr.notEqualTo(0)){
                            text (
                                Bokmal to "Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget",
                                Nynorsk to "Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget",
                            )
                            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr <> 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) THEN      INCLUDE ENDIF
                            showIf( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto.greaterThan(0)){
                                textExpr (
                                    Bokmal to " for ".expr() + PE_UT_Barnet_Barna_Felles + " som bor med begge sine foreldre",
                                    Nynorsk to " for ".expr() + PE_UT_Barnet_Barna_Felles + " som bur saman med begge foreldra sine",
                                )
                            }

                            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr <> 0) THEN      INCLUDE ENDIF
                            text (
                                Bokmal to ". ",
                                Nynorsk to ". ",
                            )

                            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr <> 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0) THEN      INCLUDE ENDIF
                            showIf( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto.equalTo(0)){
                                text (
                                    Bokmal to "Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året.",
                                    Nynorsk to "Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året.",
                                )
                            }
                        }
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) THEN      INCLUDE ENDIF
                showIf(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget){
                    //[TBU4008_NN, TBU4008]

                    paragraph {
                        textExpr (
                            Bokmal to "Barnetillegget for ".expr() + PE_UT_Barnet_Barna_Serkull + " som ikke bor sammen med begge foreldrene er beregnet ut fra inntekten din på " + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning.format() + " kroner. ",
                            Nynorsk to "Barnetillegget for ".expr() + PE_UT_Barnet_Barna_Serkull + " som ikkje bur saman med begge foreldra er berekna ut frå inntekta di på " + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning.format() + " kroner. ",
                        )

                        //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) THEN      INCLUDE ENDIF
                        showIf(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr.equalTo(0) and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto.greaterThan(0)){
                            text (
                                Bokmal to "Dette er ",
                                Nynorsk to "Dette er ",
                            )
                            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning > PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBfribelop AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) THEN      INCLUDE ENDIF
                            showIf(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning.greaterThan(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBfribelop)){
                                text (
                                    Bokmal to "høyere",
                                    Nynorsk to "høgare",
                                )
                            }

                            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning <= PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBfribelop AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) THEN      INCLUDE ENDIF
                            showIf(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning.lessThanOrEqual(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBfribelop) ){
                                text (
                                    Bokmal to "lavere",
                                    Nynorsk to "lågere",
                                )
                            }

                            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) THEN      INCLUDE ENDIF
                            textExpr (
                                Bokmal to " enn fribeløpsgrensen på ".expr() + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBfribelop.format() + " kroner. Dette barnetillegget er derfor ",
                                Nynorsk to " enn fribeløpet på ".expr() + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBfribelop.format() + " kroner. Derfor er barnetillegget ",
                            )

                            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning <= PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBfribelop AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) THEN      INCLUDE ENDIF
                            showIf(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning.lessThanOrEqual(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBfribelop)){
                                text (
                                    Bokmal to "ikke lenger ",
                                    Nynorsk to "ikkje lenger ",
                                )
                            }

                            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB > PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB > PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) THEN      INCLUDE ENDIF
                            showIf(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB.greaterThan(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB) and PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB.greaterThan(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB)){
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
                        showIf(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr.notEqualTo(0) and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto.greaterThan(0)){
                            textExpr (
                                Bokmal to "Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget for ".expr() + PE_UT_Barnet_Barna_Serkull + " som ikke bor sammen med begge foreldrene. ",
                                Nynorsk to "Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget for ".expr() + PE_UT_Barnet_Barna_Serkull + " som ikkje bur saman med begge foreldra. ",
                            )
                        }

                        //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr <> 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0) THEN      INCLUDE ENDIF
                        showIf(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr.notEqualTo(0) and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto.equalTo(0)){
                            text (
                                Bokmal to "Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året.",
                                Nynorsk to "Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året.",
                            )
                        }
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false) THEN      INCLUDE ENDIF
                showIf(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget and not(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget)){
                    //[TBU4009_NN, TBU4009]

                    paragraph {
                        textExpr (
                            Bokmal to "Vi har beregnet barnetillegget på nytt ut fra inntekten din på ".expr() + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning.format() + " kroner. ",
                            Nynorsk to "Vi har berekna barnetillegget på nytt ut frå inntekta di på ".expr() + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning.format() + " kroner. ",
                        )

                        //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) THEN      INCLUDE ENDIF
                        showIf(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr.equalTo(0) and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto.greaterThan(0)){
                            text (
                                Bokmal to "Dette er ",
                                Nynorsk to "Dette beløpet er ",
                            )
                        }

                        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning > PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBfribelop AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) THEN      INCLUDE ENDIF
                        showIf(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning.greaterThan(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBfribelop) and PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr.equalTo(0) and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto.greaterThan(0)){
                            text (
                                Bokmal to "høyere",
                                Nynorsk to "høgare",
                            )
                        }

                        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning <= PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBfribelop AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) THEN      INCLUDE ENDIF
                        showIf(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning.lessThanOrEqual(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBfribelop) and PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr.equalTo(0) and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto.greaterThan(0)){
                            text (
                                Bokmal to "lavere",
                                Nynorsk to "lågare",
                            )
                        }

                        //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) THEN      INCLUDE ENDIF
                        showIf(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr.equalTo(0) and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto.greaterThan(0)){
                            textExpr (
                                Bokmal to " enn fribeløpsgrensen på ".expr() + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBfribelop.format() + " kroner. Barnetillegget er derfor ",
                                Nynorsk to " enn fribeløpet på ".expr() + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBfribelop.format() + " kroner. Derfor er barnetillegget ",
                            )
                        }

                        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning <= PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBfribelop AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) THEN      INCLUDE ENDIF
                        showIf(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning.lessThanOrEqual(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBfribelop) and PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr.equalTo(0) and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto.greaterThan(0)){
                            text (
                                Bokmal to "ikke lenger ",
                                Nynorsk to "ikkje lenger ",
                            )
                        }

                        //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) THEN      INCLUDE ENDIF
                        showIf(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr.equalTo(0) and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto.greaterThan(0)){
                            text (
                                Bokmal to "redusert. ",
                                Nynorsk to "redusert. ",
                            )
                        }

                        //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr <> 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) THEN      INCLUDE ENDIF
                        showIf(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr.notEqualTo(0) and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto.greaterThan(0)){
                            text (
                                Bokmal to "Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget. ",
                                Nynorsk to "Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget. ",
                            )
                        }

                        //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr <> 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0) THEN      INCLUDE ENDIF
                        showIf(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr.notEqualTo(0) and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto.equalTo(0)){
                            text (
                                Bokmal to "Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året.",
                                Nynorsk to "Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året.",
                            )
                        }
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0) THEN      INCLUDE ENDIF
                showIf(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto.equalTo(0) and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget and PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr.equalTo(0)){
                    //[TBU4010_NN, TBU4010]

                    paragraph {
                        text (
                            Bokmal to "Barnetillegget ",
                            Nynorsk to "Barnetillegget ",
                        )

                        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) THEN      INCLUDE ENDIF
                        showIf(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget){
                            textExpr (
                                Bokmal to "for ".expr() + PE_UT_Barnet_Barna_Felles + " som bor med begge sine foreldre ",
                                Nynorsk to "for ".expr() + PE_UT_Barnet_Barna_Felles + " som bur saman med begge forelda sine ",
                            )
                        }
                        textExpr (
                            Bokmal to "blir ikke utbetalt fordi den samlede inntekten til deg og din ".expr() + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT + " er høyere enn " + PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_Inntektstak.format() + " kroner.",
                            Nynorsk to "blir ikkje utbetalt fordi den samla inntekta til deg og ".expr() + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_NN_entall + " din er høgare enn " + PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_Inntektstak.format() + " kroner.",
                        )
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0) THEN      INCLUDE ENDIF
                showIf(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto.equalTo(0) and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget and PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr.equalTo(0)){
                    //[TBU4011_NN, TBU4011]

                    paragraph {
                        text (
                            Bokmal to "Barnetillegget ",
                            Nynorsk to "Barnetillegget ",
                        )

                        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) THEN      INCLUDE ENDIF
                        showIf(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget){
                            textExpr (
                                Bokmal to "for ".expr() + PE_UT_Barnet_Barna_Serkull + " som ikke bor sammen med begge foreldrene ",
                                Nynorsk to "for ".expr() + PE_UT_Barnet_Barna_Serkull + " som ikkje bur saman med begge foreldra ",
                            )
                        }
                        textExpr (
                            Bokmal to "blir ikke utbetalt fordi inntekten din er høyere enn ".expr() + PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_Inntektstak.format() + " kroner.",
                            Nynorsk to "blir ikkje utbetalt fordi inntekta di er høgare enn ".expr() + PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_Inntektstak.format() + " kroner.",
                        )
                    }
                }

                //IF( FF_CheckIfFirstDayAndMonthOfYear(PE_VedtaksData_VirkningFOM) = false AND ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert= true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert= true ) ) THEN      INCLUDE ENDIF
                showIf(not(FUNKSJON_FF_CheckIfFirstDayAndMonthOfYear(PE_VedtaksData_VirkningFOM)) and (PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert or PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert or PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert or PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert)){
                    //[TBU4012]
                    paragraph {
                        text (
                            Bokmal to "Fordi du ikke har barnetillegg ",
                            Nynorsk to "Fordi du ikkje har barnetillegg ",
                        )

                        showIf(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget){
                            showIf((PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert and not(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert)) or PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert and not(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert)){
                                textExpr (
                                    Bokmal to "for ".expr() + PE_UT_Barnet_Barna_Felles + " som bor med begge sine foreldre ",
                                    Nynorsk to "for ".expr() + PE_UT_Barnet_Barna_Felles + " som bur saman med begge foreldra sine ",

                                    )
                            }

                            //IF( ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert= true ) OR ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert= true ) AND ( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true ) )THEN   INCLUDE ENDIF
                            showIf((not(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert) and PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert) or not(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert) and PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert){
                                textExpr (
                                    Bokmal to "for ".expr() + PE_UT_Barnet_Barna_Serkull + " som ikke bor sammen med begge foreldrene ",
                                    Nynorsk to "for ".expr() + PE_UT_Barnet_Barna_Serkull + " som ikkje bur saman med begge foreldra ",
                                )
                            }
                        }
                        //IF( ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = true AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert= false ) OR ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert= false ) AND ( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true ) ) THEN      INCLUDE ENDIF

                        text (
                            Bokmal to "hele året er ",
                            Nynorsk to "heile året, er ",
                        )

                        //IF(( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert= true )) THEN   INCLUDE ENDIF
                        showIf((PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert or PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert)){
                            text (
                                Bokmal to "inntektene ",
                                Nynorsk to "inntektene ",
                            )
                        }

                        showIf(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert or PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert) {
                            //IF( ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert= true ) AND ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert= true ) )THEN   INCLUDE ENDIF
                            showIf(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert or PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert){
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
                        showIf(not(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert) and not(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert) and (PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert or PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert)){
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
                        showIf(PE_Vedtaksdata_Kravhode_KravArsakType.equalTo("sivilstandsendring")){
                            text (
                                Bokmal to "Fordi sivilstanden din har endret seg er ",
                                Nynorsk to "Fordi sivilstanden din har endra seg, er ",
                            )
                        }

                        //IF( (PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert= true) AND PE_Vedtaksdata_Kravhode_KravArsakType = "sivilstandsendring" ) THEN   INCLUDE ENDIF
                        showIf((PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert or PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert) and PE_Vedtaksdata_Kravhode_KravArsakType.equalTo("sivilstandsendring")){
                            text (
                                Bokmal to "inntektene ",
                                Nynorsk to "inntektene ",
                            )
                        }

                        //IF( ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert= true ) AND ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert= true ) AND ( PE_Vedtaksdata_Kravhode_KravArsakType = "sivilstandsendring" ) )THEN   INCLUDE ENDIF
                        showIf((PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert or PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert) and (PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert or PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert) and PE_Vedtaksdata_Kravhode_KravArsakType.equalTo("sivilstandsendring")){
                            text (
                                Bokmal to "og ",
                                Nynorsk to "og ",
                            )
                        }

                        //IF( (PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert= true) AND PE_Vedtaksdata_Kravhode_KravArsakType = "sivilstandsendring"  )THEN   INCLUDE ENDIF
                        showIf((PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert or PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert) and PE_Vedtaksdata_Kravhode_KravArsakType.equalTo("sivilstandsendring")){
                            text (
                                Bokmal to "fribeløpet ",
                                Nynorsk to "fribeløpet ",
                            )
                        }

                        //PE_Vedtaksdata_Kravhode_KravArsakType = "sivilstandsendring"
                        showIf(PE_Vedtaksdata_Kravhode_KravArsakType.equalTo("sivilstandsendring")){
                            text (
                                Bokmal to "justert slik at de kun gjelder for den framtidige perioden du mottar barnetillegg. ",
                                Nynorsk to "justert slik at dei berre gjeld for den framtidige perioden du får barnetillegg. ",
                            )
                        }
                    }
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense < PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak) THEN      INCLUDE ENDIF
            showIf(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense.lessThan(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak)){
                includePhrase(TBU1133_Generated)
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false) THEN      INCLUDE ENDIF
            showIf(not(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget) and not(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget) and not(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget) and not(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget)){
                //[TBU2263NN, TBU2263]

                paragraph {
                    text (
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14 og 22-12.",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14 og 22-12.",
                    )
                }
            }

            //IF(  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT  ) THEN      INCLUDE ENDIF
            showIf((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget or PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget) and not(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget) and not(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget) and PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT.notEqualTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT)){
                //[TBU2264NN, TBU2264]

                paragraph {
                    text (
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14 til 12-16 og 22-12",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14 til 12-16 og 22-12",
                    )

                    //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                    showIf(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType.equalTo("overgangsregler_2016")){
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
            showIf((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget or PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget) and not(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget) and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget){
                //[TBU2265NN, TBU2265]

                paragraph {
                    text (
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14 til 12-16 og 22-12 og overgangsforskriften §8",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14 til 12-16 og 22-12 og overgangsforskrifta § 8",
                    )

                    //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                    showIf(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType.equalTo("overgangsregler_2016")){
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
            showIf((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget or PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget) and PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget and not(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget)){
                //[TBU2266NN, TBU2266]

                paragraph {
                    text (
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14 til 12-16, 12-18 og 22-12",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14 til 12-16, 12-18 og 22-12",
                    )

                    //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                    showIf(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType.equalTo("overgangsregler_2016")){
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
            showIf(not(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget) and not(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget) and PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget and not(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget)){
                //[TBU2267NN, TBU2267]

                paragraph {
                    text (
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14, 12- 18 og 22-12.",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14, 12-18 og 22-12.",
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true) THEN      INCLUDE ENDIF
            showIf(not(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget) and not(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget) and not(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget) and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget){
                //[TBU2268NN, TBU2268]

                paragraph {
                    text (
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14 og 22-12 og overgangsforskriften § 8.",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14 og 22-12 og overgangsforskrifta § 8.",
                    )
                }
            }

            //IF((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT = PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT)  THEN      INCLUDE ENDIF
            showIf((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget or PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget) and not(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget) and not(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget) and PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT.equalTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT)){
                //[TBU4014_NN, TBU4014]

                paragraph {
                    text (
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-15, 12-16 og 22-12",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-15, 12-16 og 22-12",
                    )

                    //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                    showIf(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType.equalTo("overgangsregler_2016")){
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
            showIf(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT.notEqualTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT) and PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT.greaterThan(0)){
                //[TBU4015_NN, TBU4015]

                title1 {
                    text (
                        Bokmal to "Hva får du i uføretrygd framover?",
                        Nynorsk to "Kva får du i uføretrygd framover?",
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak > PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense) THEN      INCLUDE ENDIF
                showIf(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad.lessThan(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad) and PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak.greaterThan(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense)){
                    //[TBU4044_NN, TBU4044]

                    paragraph {
                        textExpr (
                            Bokmal to "Ut fra den årlige inntekten din vil uføretrygden utgjøre ".expr() + PE_UT_NettoAkk_pluss_NettoRestAr.format() + " kroner. ",
                            Nynorsk to "Ut frå den årlege inntekta di vil uføretrygda utgjere ".expr() + PE_UT_NettoAkk_pluss_NettoRestAr.format() + " kroner. ",
                        )

                        //IF(FF_CheckIfFirstDayAndMonthOfYear(PE_VedtaksData_VirkningFOM) = false) THEN      INCLUDE ENDIF
                        showIf(not(FUNKSJON_FF_CheckIfFirstDayAndMonthOfYear(PE_VedtaksData_VirkningFOM))){
                            textExpr (
                                Bokmal to "Hittil i år har du fått utbetalt ".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_NettoAkk.format() + " kroner. ",
                                Nynorsk to "Hittil i år har du fått utbetalt ".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_NettoAkk.format() + " kroner. ",
                            )
                        }
                        textExpr (
                            Bokmal to "Du har derfor rett til en utbetaling av uføretrygd på ".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Netto.format() + " kroner per måned for resten av året.",
                            Nynorsk to "Du har derfor rett til ei utbetaling av uføretrygd på ".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Netto.format() + " kroner per månad for resten av året.",
                        )
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt > PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense) THEN      INCLUDE ENDIF
                showIf(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt.greaterThan(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense)){
                    //[TBU4045_NN, TBU4045]

                    paragraph {
                        textExpr (
                            Bokmal to "Selv om uføretrygden din er redusert på grunn av inntekt beholder du likevel uføregraden din på ".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad.format() + " prosent. Du får utbetalt hele uføretrygden igjen dersom du tjener mindre enn inntektsgrensen din.",
                            Nynorsk to "Sjølv om uføretrygda di er redusert på grunn av inntekt beheld du likevel uføregraden din på ".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad.format() + " prosent. Du får utbetalt heile uføretrygda att dersom du tener mindre enn inntektsgrensa di.",
                        )
                    }
                }
            }

            //IF( (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB > 0) OR (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB > 0) ) THEN   INCLUDE ENDIF
            showIf((PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB.notEqualTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB) and PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB.greaterThan(0)) or (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB.notEqualTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB) and PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB.greaterThan(0))){
                //[TBU4046_NN, TBU4046]

                title1 {
                    text (
                        Bokmal to "Hva får du i barnetillegg framover?",
                        Nynorsk to "Kva får du i barnetillegg framover?",
                    )
                }

                //IF( (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB) AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB <> 0 ) THEN   INCLUDE ENDIF
                showIf(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB.notEqualTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB) and PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB.notEqualTo(0)){
                    //[TBU4047_NN, TBU4047]

                    paragraph {
                        textExpr (
                            Bokmal to "Ut fra den samlede inntekten til deg og din ".expr() + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT + " er barnetillegget vurdert på nytt. Barnetillegget er redusert for hele året ut fra den inntekten som overstiger fribeløpet. Den årlige reduksjonen av barnetillegget er " + PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_AvkortingsbelopPerAr.format() + " kroner.",
                            Nynorsk to "Ut frå dei samla inntektene til deg og ".expr() + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_NN_entall + " din er barnetillegget blitt vurdert på nytt. Barnetillegget er redusert for heile året ut frå inntekta som overstig fribeløpet. Den årlege reduksjonen av barnetillegget er " + PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_AvkortingsbelopPerAr.format() + " kroner.",
                        )

                        //IF(( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr <> 0 )) THEN   INCLUDE ENDIF
                        showIf(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr.notEqualTo(0)){
                            text (
                                Bokmal to " Vi har tatt hensyn til hvordan barnetillegget eventuelt har vært redusert i starten av året, og vi har derfor ",
                                Nynorsk to " Vi har teke omsyn til korleis barnetillegget eventuelt har vore redusert i starten av året, og vi har derfor ",
                            )
                        }

                        //IF(( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr <> 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr > 0 )) THEN   INCLUDE ENDIF
                        showIf(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr.greaterThan(0)){
                            text (
                                Bokmal to "lagt til",
                                Nynorsk to "lagt til",
                            )
                        }

                        //IF(( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr <> 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr < 0 )) THEN   INCLUDE ENDIF
                        showIf(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr.lessThan(0)){
                            text (
                                Bokmal to "trukket fra",
                                Nynorsk to "trekt frå",
                            )
                        }

                        //IF(( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr <> 0 )) THEN   INCLUDE ENDIF
                        showIf(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr.notEqualTo(0)){
                            textExpr (
                                Bokmal to PE_BarnetilleggFelles_JusteringsbelopPerArUtenMinus.format() + " kroner i det vi reduserer barnetillegget med for resten av året.",
                                Nynorsk to PE_BarnetilleggFelles_JusteringsbelopPerArUtenMinus.format() + " kroner i det vi har redusert barnetillegget med for resten av året.",
                            )
                        }
                        textExpr (
                            Bokmal to " Du har derfor rett til en utbetaling av barnetillegg på ".expr() + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto.format() + " kroner per måned for resten av året. ",
                            Nynorsk to " Du har derfor rett til ei utbetaling av barnetillegg på ".expr() + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto.format() + " kroner per månad for resten av året.",
                        )
                    }
                }

                //IF( (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB) AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB <> 0 ) THEN   INCLUDE ENDIF
                showIf(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB.notEqualTo(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB) and PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB.notEqualTo(0)){
                    //[TBU4048_NN, TBU4048]

                    paragraph {
                        textExpr (
                            Bokmal to "Ut fra den samlede inntekten din er barnetillegget vurdert på nytt. Barnetillegget er redusert for hele året ut fra den inntekten som overstiger fribeløpet. Den årlige reduksjonen av barnetillegget er ".expr() + PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_AvkortingsbelopPerAr.format() + " kroner.",
                            Nynorsk to "Ut frå den samla inntekta di er barnetillegget blitt vurdert på nytt. Barnetillegget er redusert for heile året ut frå inntekta som overstig fribeløpet. Den årlege reduksjonen av barnetillegget er ".expr() + PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_AvkortingsbelopPerAr.format() + " kroner.",
                        )

                        //IF(( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr <> 0 )) THEN   INCLUDE ENDIF
                        showIf(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr.notEqualTo(0)){
                            text (
                                Bokmal to " Vi har tatt hensyn til hvordan barnetillegget eventuelt har vært redusert i starten av året, og vi har derfor ",
                                Nynorsk to " Vi har teke omsyn til korleis barnetillegget eventuelt har vore redusert i starten av året, og vi har derfor ",
                            )
                        }

                        //IF(( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr > 0 )) THEN   INCLUDE ENDIF
                        showIf(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr.greaterThan(0)){
                            text (
                                Bokmal to "lagt til",
                                Nynorsk to "lagt til",
                            )
                        }

                        //IF(( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr < 0 )) THEN   INCLUDE ENDIF
                        showIf(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr.lessThan(0)){
                            text (
                                Bokmal to "trukket fra",
                                Nynorsk to "trekt frå",
                            )
                        }

                        //IF(( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr <> 0 )) THEN   INCLUDE ENDIF
                        showIf(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr.notEqualTo(0)){
                            textExpr (
								Bokmal to " ".expr() + PE_BarnetilleggSerkull_JusteringsbelopPerArUtenMinus.format() + " kroner i det vi reduserer barnetillegget med for resten av året.",
								Nynorsk to " ".expr() + PE_BarnetilleggSerkull_JusteringsbelopPerArUtenMinus.format() + " kroner i det vi reduserte barnetillegget med for resten av året.",
                            )
                        }
                        textExpr (
                            Bokmal to " Du har derfor rett til en utbetaling av barnetillegg på ".expr() + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto.format() + " kroner per måned for resten av året.",
                            Nynorsk to " Du har derfor rett til ei utbetaling av barnetillegg på ".expr() + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto.format() + " kroner per månad for resten av året.",
                        )
                    }
                }

                //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true) OR (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) ) THEN   INCLUDE ENDIF
                showIf((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto.equalTo(0) and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget) or (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto.equalTo(0) and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget)){
                    //[TBU4049_NN, TBU4049]

                    paragraph {
                        textExpr (
                            Bokmal to "Du får ikke utbetalt barnetillegget for ".expr() + PE_UT_Barnet_Barna_Felles_serkull + " som ",
                            Nynorsk to "Du får ikkje utbetalt barnetillegget for ".expr() + PE_UT_Barnet_Barna_Felles_serkull + " som ",
                        )

                        //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) AND (   PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false   OR   (     PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true     AND     PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto <> 0) ) )THEN   INCLUDE ENDIF
                        showIf(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto.equalTo(0) and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget and (not(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget) or (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto.notEqualTo(0)))){
                            text (
                                Bokmal to "bor med begge sine foreldre",
                                Nynorsk to "bur med begge foreldra",
                            )
                        }

                        //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true) AND (   PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false   OR   (     PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true     AND     PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0) ) )THEN   INCLUDE ENDIF
                        showIf(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto.equalTo(0) and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget and (not(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget) or (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget and PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto.notEqualTo(0)))){
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
            showIf(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget){
                includePhrase(TBU1214_Generated)
                //[TBU2272NN, TBU2272]

                paragraph {
                    text (
                        Bokmal to "Du mottar gjenlevendetillegg i uføretrygden din. Tjener du mer enn inntektsgrensen din, reduserer vi gjenlevendetillegget ditt med samme prosent som vi reduserer uføretrygden din med.",
                        Nynorsk to "Du får attlevandetillegg i uføretrygda di. Tener du meir enn inntektsgrensa di, reduserer vi attlevandetillegget ditt med same prosenten som vi reduserer uføretrygda di med.",
                    )
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopRedusert = true AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true) THEN      INCLUDE ENDIF
                showIf(PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopRedusert and PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget){
                    //[TBU2273NN, TBU2273]

                    paragraph {
                        text (
                            Bokmal to "Gjenlevendetillegget er redusert ut fra den innmeldte inntekten.",
                            Nynorsk to "Attlevandetillegget er redusert ut frå den innmelde inntekta.",
                        )
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopOkt = true AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true) THEN      INCLUDE ENDIF
                showIf(PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopOkt and PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget){
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
            showIf(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget){
                includePhrase(TBU2275_Generated)
            }

            //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true
            showIf(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget){
                //[TBU2276NN, TBU2276]

                paragraph {
                    text (
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
            includePhrase(TBU2278_Generated(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget, PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget))

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt < PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak > PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense) THEN      INCLUDE ENDIF
            showIf(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt.lessThan(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak) and PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak.greaterThan(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense)){
                includePhrase(TBU2279_Generated(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak))
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt < PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak <= PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense) THEN      INCLUDE ENDIF
            showIf(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt.lessThan(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak) and PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak.lessThanOrEqual(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense)){
                includePhrase(TBU3740_Generated(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense))
            }
            includePhrase(TBU2280_Generated(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget, PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget))
            //[TBU2282NN, TBU2282]

            title1{
                text (
                    Bokmal to "Inntekter som ikke skal føre til reduksjon av uføretrygden",
                    Nynorsk to "Inntekter som ikkje skal føra til reduksjon av uføretrygda",
                )
            }
            paragraph {
                text (
                    Bokmal to "Det kan gjøres unntak for enkelte inntektstyper som ikke skal føre til reduksjon av uføretrygden. Dette kan gjelde følgende:",
                    Nynorsk to "Det kan gjerast unntak for enkelte inntektstypar som ikkje skal føra til reduksjon av uføretrygda. Dette kan gjelda følgjande:",
                )
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
                text (
                    Bokmal to "Dersom vi mottar dokumentasjon fra deg som bekrefter at du har slik inntekt, kan vi gjøre en ny beregning av uføretrygden din.",
                    Nynorsk to "Dersom vi mottek dokumentasjon frå deg som stadfestar at du har slik inntekt, kan vi gjera ei ny berekning av uføretrygda di.",
                )
            }
            includePhrase(Ufoeretrygd.MeldeFraOmEndringer)
            includePhrase(Ufoeretrygd.RettTilAAKlage)
            includePhrase(Felles.RettTilInnsynPesys)
            includePhrase(Ufoeretrygd.SjekkUtbetalingene)
            includePhrase(Ufoeretrygd.Skattekort)
            includePhrase(SkattForDegSomBorIUtlandet(brukerBorINorge))
            includePhrase(Ufoeretrygd.HarDuSpoersmaalUfoeretrygd)
        }
    }
}