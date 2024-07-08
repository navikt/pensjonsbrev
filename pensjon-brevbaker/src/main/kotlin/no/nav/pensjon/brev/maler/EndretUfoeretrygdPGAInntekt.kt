package no.nav.pensjon.brev.maler
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDto
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.PE_BarnetilleggFelles_JusteringsbelopPerArUtenMinus
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.PE_BarnetilleggSerkull_JusteringsbelopPerArUtenMinus
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.PE_UT_NettoAkk_pluss_NettoRestAr
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.PE_UT_VirkningstidpunktArMinus1Ar
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.PE_VedtaksData_VirkningFOM
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.barnetilleggFelles_InntektBruktIAvkortning
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.barnetilleggFelles_avkortningsbeloepPerAar
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.barnetilleggFelles_belopFratrukketAnnenForeldersInntekt
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.barnetilleggFelles_brukersInntektTilAvkortning
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.barnetilleggFelles_fribeloep
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.barnetilleggFelles_fribelopPeriodisert
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.barnetilleggFelles_inntektAnnenForelder
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.barnetilleggFelles_inntektPeriodisert
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.barnetilleggFelles_inntektstak
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.barnetilleggFelles_innvilget
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.barnetilleggFelles_justeringsbelopPerAr
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.barnetilleggFelles_netto
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.barnetilleggReduksjonsgrunnlagHarRegelverkstypeOvergang2016
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.barnetilleggSerkull_avkortingsbelopPerAr
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.barnetilleggSerkull_fribeloep
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.barnetilleggSerkull_fribelopPeriodisert
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.barnetilleggSerkull_inntektBruktIAvkortning
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.barnetilleggSerkull_inntektPeriodisert
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.barnetilleggSerkull_inntektstak
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.barnetilleggSerkull_innvilget
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.barnetilleggSerkull_justeringsbelopPerAr
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.barnetilleggSerkull_netto
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.beregningUfore_BelopGammelBTFB
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.beregningUfore_BelopGammelBTSB
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.beregningUfore_BelopGammelUT
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.beregningUfore_BelopNyBTFB
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.beregningUfore_BelopNyBTSB
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.beregningUfore_BelopNyUT
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.beregningUfore_BelopOkt
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.beregningUfore_BelopRedusert
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.beregningUfore_grunnbelop
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.beregningUfore_totalNetto
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.beregningUfore_uforegrad
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.beregningVirkFomErFoersteJanuar
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.borMedSivilstand
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.brukerBorINorge
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.ektefelletillegg_ETinnvilget
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.fyller67iAar
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.gjenlevendetillegg_GTinnvilget
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.harFlereFellesBarn
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.harFlereSaerkullsbarn
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.harInstitusjonsopphold
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.harKravaarsakSivilstandsendring
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.harTilleggForFlereBarn
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.uforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.uforetrygdOrdiner_avkortingsbelopPerAr
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.uforetrygdOrdiner_forventetInntekt
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.uforetrygdOrdiner_inntektsgrense
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.uforetrygdOrdiner_inntektstak
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.uforetrygdOrdiner_netto
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.uforetrygdOrdiner_nettoAkk
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.virkFomAar
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.virkFomAarMinusEn
import no.nav.pensjon.brev.api.model.maler.EndretUfoeretrygdPGAInntektDtoSelectors.virkFomErFoersteJanuar
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.maler.fraser.generated.*
import no.nav.pensjon.brev.maler.fraser.ufoer.Ufoeretrygd
import no.nav.pensjon.brev.maler.fraser.ufoer.Ufoeretrygd.SkattForDegSomBorIUtlandet
import no.nav.pensjon.brev.model.bestemtForm
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.model.ubestemtForm
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

@TemplateModelHelpers
object EndretUfoeretrygdPGAInntekt : AutobrevTemplate<EndretUfoeretrygdPGAInntektDto> {

    override val kode = Brevkode.AutoBrev.UT_ENDRET_PGA_INNTEKT

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
        title {
            showIf(beregningUfore_BelopGammelUT.notEqualTo(beregningUfore_BelopNyUT)) {
                showIf(
                    beregningUfore_BelopGammelBTSB.equalTo(beregningUfore_BelopNyBTSB)
                            and beregningUfore_BelopGammelBTFB.equalTo(beregningUfore_BelopNyBTFB)
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
                    showIf(barnetilleggSerkull_innvilget and barnetilleggFelles_innvilget) {
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
            }.orShowIf(beregningUfore_BelopGammelBTSB.equalTo(beregningUfore_BelopNyBTSB)
                    and beregningUfore_BelopGammelBTFB.equalTo(beregningUfore_BelopNyBTFB)){
                text(
                    Bokmal to "NAV har endret utbetalingen av ",
                    Nynorsk to "NAV har endra utbetalinga av ",
                )
                showIf(barnetilleggSerkull_innvilget and barnetilleggFelles_innvilget) {
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
            showIf(not(virkFomErFoersteJanuar)){
                //[TBU2249NN, TBU2249]

                paragraph {
                    text (
                        Bokmal to "Vi har mottatt nye opplysninger om inntekten",
                        Nynorsk to "Vi har fått nye opplysningar om inntekta",
                    )

                    //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false
                    showIf(not(barnetilleggFelles_innvilget)){
                        text (
                            Bokmal to " din",
                            Nynorsk to " di",
                        )
                    }

                    //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
                    showIf(barnetilleggFelles_innvilget){
                        textExpr (
                            Bokmal to " til deg eller din ".expr() + borMedSivilstand.ubestemtForm() + ". Inntekten til din " + borMedSivilstand.ubestemtForm() + " har kun betydning for størrelsen på barnetillegget ",
                            Nynorsk to " til deg eller ".expr() + borMedSivilstand.bestemtForm() + " din. Inntekta til " + borMedSivilstand.bestemtForm() + " din har berre betydning for storleiken på barnetillegget ",
                        )
                        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true) THEN      INCLUDE ENDIF
                        showIf(barnetilleggSerkull_innvilget){
                            textExpr (
                                Bokmal to "for ".expr() + ifElse(harFlereFellesBarn, "barna", "barnet") + " som bor med begge sine foreldre",
                                Nynorsk to "for ".expr() + ifElse(harFlereFellesBarn, "barna", "barnet") + " som bur saman med begge foreldra sine",
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
                    showIf(beregningUfore_BelopGammelUT.notEqualTo(beregningUfore_BelopNyUT)){
                        text (
                            Bokmal to "uføretrygden din ",
                            Nynorsk to "uføretrygda di ",
                        )
                        //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT  AND (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB  OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB)) THEN      INCLUDE ENDIF
                        showIf(beregningUfore_BelopGammelBTFB.notEqualTo(beregningUfore_BelopNyBTFB) or beregningUfore_BelopGammelBTSB.notEqualTo(beregningUfore_BelopNyBTSB)){
                            text (
                                Bokmal to "og ",
                                Nynorsk to "og ",
                            )
                        }
                    }

                    //IF(((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false)  OR (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true))) AND (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB  OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB)  THEN      INCLUDE ENDIF
                    showIf(((barnetilleggSerkull_innvilget and not(barnetilleggFelles_innvilget)) or (not(barnetilleggSerkull_innvilget) and barnetilleggFelles_innvilget)) and (beregningUfore_BelopGammelBTFB.notEqualTo(beregningUfore_BelopNyBTSB) or beregningUfore_BelopGammelBTSB.notEqualTo(beregningUfore_BelopNyBTSB))){
                        text (
                            Bokmal to "barnetillegget ditt ",
                            Nynorsk to "barnetillegget ditt ",
                        )
                    }

                    //IF((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true)) AND (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB  OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB)  THEN      INCLUDE ENDIF
                    showIf(barnetilleggFelles_innvilget and barnetilleggSerkull_innvilget and (beregningUfore_BelopGammelBTFB.notEqualTo(beregningUfore_BelopNyBTSB) or beregningUfore_BelopGammelBTSB.notEqualTo(beregningUfore_BelopNyBTSB))){
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
                        Bokmal to "Vi vil bruke en inntekt på ".expr() + uforetrygdOrdiner_forventetInntekt.format() + " kroner når vi reduserer uføretrygden din for " + virkFomAar.format() + ". Har du ikke meldt inn ny inntekt for " + virkFomAar.format() + ", er inntekten justert opp til dagens verdi.",
                        Nynorsk to "Vi vil bruke ei inntekt på ".expr() + uforetrygdOrdiner_forventetInntekt.format() + " kroner når vi reduserer uføretrygda di for " + virkFomAar.format() + ". Har du ikkje meldt inn ny inntekt for " + virkFomAar.format() + ", er inntekta justert opp til dagens verdi.",
                    )

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad) THEN      INCLUDE ENDIF
                    showIf(uforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad.notEqualTo(beregningUfore_uforegrad)) {
                        textExpr(
                            Bokmal to "Fikk du innvilget uføretrygd etter januar ".expr() + virkFomAarMinusEn.format() + ", er inntekten justert opp slik at den gjelder for hele " + virkFomAar.format() + ".",
                            Nynorsk to "Fekk du innvilga uføretrygd etter januar ".expr() + virkFomAarMinusEn.format() + ", er inntekta også justert opp slik at den gjeld for heile " + virkFomAar.format() + ".",
                        )
                    }

                    //PE_UT_VilFylle67iVirkningFomAr = true
                    showIf(fyller67iAar) {
                        textExpr(
                            Bokmal to " Fordi du fyller 67 år i ".expr() + virkFomAar.format() + ", er inntekten justert i forhold til antall måneder du mottar uføretrygd.",
                            Nynorsk to " Fordi du fyljer 67 år i ".expr() + virkFomAar.format() + ", er inntekta justert ut frå talet på månadar du får uføretrygd.",
                        )
                    }
                }
            }
            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT  AND  (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB  OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB)) AND (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget= true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false) AND FF_CheckIfFirstDayAndMonthOfYear(PE_VedtaksData_VirkningFOM) = true THEN  INCLUDE ENDIF
            showIf(beregningUfore_BelopGammelUT.notEqualTo(beregningUfore_BelopNyUT) and (beregningUfore_BelopGammelBTFB.notEqualTo(beregningUfore_BelopNyBTFB) or beregningUfore_BelopGammelBTSB.notEqualTo(beregningUfore_BelopNyBTSB)) and barnetilleggSerkull_innvilget and not(barnetilleggFelles_innvilget) and virkFomErFoersteJanuar){
                //[TBU4016_NN, TBU4016]

                paragraph {
                    textExpr (
                        Bokmal to "I reduksjonen av barnetillegget ditt vil vi bruke en inntekt på ".expr() + barnetilleggSerkull_inntektBruktIAvkortning.format() + " kroner. ",
                        Nynorsk to "I reduksjonen av barnetillegget ditt vil vi bruke ei inntekt på ".expr() + barnetilleggSerkull_inntektBruktIAvkortning.format() + " kroner.",
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT  AND (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB  OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB)) AND (FF_CheckIfFirstDayAndMonthOfYear(PE_VedtaksData_VirkningFOM) = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true )     THEN  INCLUDE ENDIF
            showIf(beregningUfore_BelopGammelUT.notEqualTo(beregningUfore_BelopNyUT) and (beregningUfore_BelopGammelBTFB.notEqualTo(beregningUfore_BelopNyBTFB) or beregningUfore_BelopGammelBTSB.notEqualTo(beregningUfore_BelopNyBTSB)) and virkFomErFoersteJanuar and barnetilleggFelles_innvilget){
                //[TBU4001]
                paragraph {

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT  AND (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB  OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB)) THEN      INCLUDE ENDIF
                    showIf(beregningUfore_BelopGammelUT.notEqualTo(beregningUfore_BelopNyUT) and (beregningUfore_BelopGammelBTFB.notEqualTo(beregningUfore_BelopNyBTFB) or beregningUfore_BelopGammelBTSB.notEqualTo(beregningUfore_BelopNyBTSB))){
                        text (
                            Bokmal to "I reduksjonen av barnetillegg",
                            Nynorsk to "I reduksjonen av barnetillegg",
                        )
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT  AND (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB  OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB)) AND FF_CheckIfFirstDayAndMonthOfYear(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningVirkningDatoFom) AND ((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true))     THEN  INCLUDE ENDIF
                    showIf(beregningUfore_BelopGammelUT.notEqualTo(beregningUfore_BelopNyUT) and (beregningUfore_BelopGammelBTFB.notEqualTo(beregningUfore_BelopNyBTFB) or beregningUfore_BelopGammelBTSB.notEqualTo(beregningUfore_BelopNyBTSB)) and beregningVirkFomErFoersteJanuar and (barnetilleggFelles_innvilget or barnetilleggSerkull_innvilget)){
                        text (
                            Bokmal to "et ditt ",
                            Nynorsk to "et ditt ",
                        )
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT  AND (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB  OR PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB)) THEN      INCLUDE ENDIF
                    showIf(beregningUfore_BelopGammelUT.notEqualTo(beregningUfore_BelopNyUT) and (beregningUfore_BelopGammelBTFB.notEqualTo(beregningUfore_BelopNyBTFB) or beregningUfore_BelopGammelBTSB.notEqualTo(beregningUfore_BelopNyBTSB))){
                        textExpr (
                            Bokmal to "vil vi bruke en inntekt på ".expr() + barnetilleggFelles_InntektBruktIAvkortning.format() + " kroner ",
                            Nynorsk to "vil vi bruke ei inntekt på ".expr() + barnetilleggFelles_InntektBruktIAvkortning.format() + " kroner ",
                        )
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true) THEN      INCLUDE ENDIF
                    showIf(barnetilleggFelles_innvilget and barnetilleggSerkull_innvilget){
                        textExpr (
                            Bokmal to "for ".expr() + ifElse(
                                harFlereFellesBarn,
                                "barna",
                                "barnet"
                            ) + " som bor med begge sine foreldre. For " + ifElse(harFlereSaerkullsbarn, "barna", "barnet") + " som ikke bor sammen med begge foreldrene vil vi bruke en inntekt på " + barnetilleggSerkull_inntektBruktIAvkortning.format() + " kroner",
                            Nynorsk to "for ".expr() + ifElse(
                                harFlereFellesBarn,
                                "barna",
                                "barnet"
                            ) + " som bur med begge sine foreldra. For " + ifElse(harFlereSaerkullsbarn, "barna", "barnet") + " som ikkje bur saman med begge foreldrea vil vi bruke ei inntekt på " + barnetilleggSerkull_inntektBruktIAvkortning.format() + " kroner",
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
                        Bokmal to "Vi vil bruke en inntekt på ".expr() + barnetilleggFelles_InntektBruktIAvkortning.format() + " kroner når vi reduserer barnetillegget ",
                        Nynorsk to "Vi vil bruke ei inntekt på ".expr() + barnetilleggFelles_InntektBruktIAvkortning.format() + " kroner når vi reduserer barnetillegget ",
                    )

                    //IF((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false)  OR (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true)) THEN      INCLUDE ENDIF
                    showIf((barnetilleggSerkull_innvilget and not(barnetilleggFelles_innvilget)) or (not(barnetilleggFelles_innvilget) and barnetilleggFelles_innvilget)){
                        text (
							Bokmal to "ditt ",
							Nynorsk to "ditt ",
                        )
                    }

                    //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true) THEN      INCLUDE ENDIF
                    showIf(barnetilleggFelles_innvilget and barnetilleggSerkull_innvilget){
                        textExpr (
                            Bokmal to "for ".expr() + ifElse(
                                harFlereFellesBarn,
                                "barna",
                                "barnet"
                            ) + " som bor med begge sine foreldre for " + virkFomAar.format() + ". For " + ifElse(harFlereSaerkullsbarn, "barna", "barnet") + " som ikke bor sammen med begge foreldrene vil vi bruke en inntekt på " + barnetilleggSerkull_inntektBruktIAvkortning.format() + " kroner. ",
                            Nynorsk to "for ".expr() + ifElse(
                                harFlereFellesBarn,
                                "barna",
                                "barnet"
                            ) + " som bur saman med begge foreldra sine for " + virkFomAar.format() + ". For " + ifElse(harFlereSaerkullsbarn, "barna", "barnet") + " som ikkje bur saman med begge foreldra vil vi bruke ei inntekt på " + barnetilleggSerkull_inntektBruktIAvkortning.format() + " kroner. ",
                        )
                    }
                    text (
                        Bokmal to "Har du ikke meldt inn ny",
                        Nynorsk to "Har du ikkje meldt inn ",
                    )

                    //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
                    showIf(barnetilleggFelles_innvilget){
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
                    showIf(barnetilleggFelles_innvilget){
                        text (
                            Bokmal to "er",
                            Nynorsk to "e",
                        )
                    }
                    textExpr (
                        Bokmal to " for ".expr() + virkFomAar.format() + ", er inntekten",
                        Nynorsk to " inntekt".expr(),
                    )

                    //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
                    showIf(barnetilleggFelles_innvilget){
                        text (
                            Bokmal to "e",
                            Nynorsk to "er",
                        )
                    }
                    textExpr (
                        Bokmal to " justert opp til dagens verdi.".expr(),
                        Nynorsk to " for ".expr() + virkFomAar.format() + ", er ",
                    )

                    //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false
                    showIf(not(barnetilleggFelles_innvilget)){
                        text (
                            Bokmal to "",
                            Nynorsk to "inntekta",
                        )
                    }

                    //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
                    showIf(barnetilleggFelles_innvilget){
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
                        Bokmal to "Vi vil bruke en inntekt på ".expr() + barnetilleggSerkull_inntektBruktIAvkortning.format() + " kroner når vi reduserer barnetillegget ditt. Har du ikke meldt inn ny inntekt for " + virkFomAar.format() + ", er inntekten justert opp til dagens verdi.",
                        Nynorsk to "Vi vil bruke ei inntekt på ".expr() + barnetilleggSerkull_inntektBruktIAvkortning.format() + " kroner når vi reduserer barnetillegget ditt. Har du ikkje meldt inn ny inntekt for " + virkFomAar.format() + ", er inntekta justert opp til dagens verdi.",
                    )
                }
            }

            //IF( (PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad) AND  ((PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB  <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB) OR (PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB  <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB)) AND FF_CheckIfFirstDayAndMonthOfYear(PE_VedtaksData_VirkningFOM) = true ) THEN      INCLUDE ENDIF
            showIf(uforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad.equalTo(beregningUfore_uforegrad) and ((beregningUfore_BelopGammelBTSB.notEqualTo(beregningUfore_BelopNyBTSB)) or (beregningUfore_BelopGammelBTFB.notEqualTo(beregningUfore_BelopNyBTFB))) and virkFomErFoersteJanuar){
                //[TBU4013_NN, TBU4013]

                paragraph {
                    textExpr (
                        Bokmal to "Fikk du innvilget uføretrygd etter januar ".expr() + PE_UT_VirkningstidpunktArMinus1Ar.format() + ", er inntekten justert opp slik at den gjelder for hele " + virkFomAar.format() + ".",
                        Nynorsk to "Fekk du innvilga uføretrygd etter januar ".expr() + PE_UT_VirkningstidpunktArMinus1Ar.format() + ", er inntekta justert opp slik at den gjeld for heile " + virkFomAar.format() + ".",
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
                    showIf((barnetilleggFelles_inntektPeriodisert and not(barnetilleggSerkull_inntektPeriodisert)) or barnetilleggFelles_fribelopPeriodisert and not(barnetilleggSerkull_fribelopPeriodisert) and barnetilleggFelles_innvilget and barnetilleggSerkull_innvilget){
                        textExpr (
                            Bokmal to "for ".expr() + ifElse(harFlereFellesBarn, "barna", "barnet") + " som bor med begge sine foreldre ",
                            Nynorsk to "for ".expr() + ifElse(harFlereFellesBarn, "barna", "barnet") + " som bur saman med begge foreldra ",
                        )
                    }

                    //IF( ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert= true ) OR ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert= true ) AND ( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true ) )THEN   INCLUDE ENDIF
                    showIf((not(barnetilleggFelles_inntektPeriodisert) and barnetilleggSerkull_inntektPeriodisert) or not(barnetilleggFelles_fribelopPeriodisert) and barnetilleggSerkull_fribelopPeriodisert and barnetilleggFelles_innvilget and barnetilleggSerkull_innvilget){
                        textExpr (
                            Bokmal to "for ".expr() + ifElse(harFlereSaerkullsbarn, "barna", "barnet") + " som ikke bor sammen med begge foreldrene ",
                            Nynorsk to "for ".expr() + ifElse(harFlereSaerkullsbarn, "barna", "barnet") + " som ikkje bur saman med begge foreldra ",
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
                    showIf(barnetilleggFelles_innvilget){
                        textExpr (
                            Bokmal to "og din ".expr() + borMedSivilstand.ubestemtForm(),
                            Nynorsk to "og ".expr() + borMedSivilstand.bestemtForm() + " din ",
                        )
                    }
                    textExpr (
                        Bokmal to "å tjene noe annet i ".expr() + virkFomAar.format() + " er det viktig at du melder inn ny forventet inntekt. Dette kan du gjøre på nav.no.",
                        Nynorsk to "å tene noko anna i ".expr() + virkFomAar.format() + " er det viktig at du melder inn ei ny forventa inntekt. Dette kan du gjere på nav.no.",
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "") THEN      INCLUDE ENDIF
            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "") THEN      INCLUDE ENDIF
            showIf((not(ektefelletillegg_ETinnvilget) and not(barnetilleggSerkull_innvilget) and not(barnetilleggFelles_innvilget) and not(gjenlevendetillegg_GTinnvilget) and not(harInstitusjonsopphold))){
                includePhrase(TBU1120_Generated(beregningUfore_totalNetto))
            }

            //IF(  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = ""  ) THEN      INCLUDE ENDIF
            showIf(((barnetilleggSerkull_innvilget or barnetilleggFelles_innvilget) and not(ektefelletillegg_ETinnvilget) and not(gjenlevendetillegg_GTinnvilget) and not(harInstitusjonsopphold))){
                includePhrase(TBU1121_Generated(beregningUfore_totalNetto))
            }

            //IF(  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true  ) THEN      INCLUDE ENDIF
            showIf(((barnetilleggSerkull_innvilget or barnetilleggFelles_innvilget) and not(gjenlevendetillegg_GTinnvilget) and ektefelletillegg_ETinnvilget)){
                includePhrase(TBU1254_Generated(beregningUfore_totalNetto))
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true) THEN      INCLUDE ENDIF
            showIf((not(barnetilleggSerkull_innvilget) and not(barnetilleggFelles_innvilget) and not(gjenlevendetillegg_GTinnvilget) and ektefelletillegg_ETinnvilget)){
                includePhrase(TBU1253_Generated(beregningUfore_totalNetto))
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_InstOppholdType = "") THEN      INCLUDE ENDIF
            showIf((not(barnetilleggSerkull_innvilget) and not(barnetilleggFelles_innvilget) and gjenlevendetillegg_GTinnvilget and not(ektefelletillegg_ETinnvilget) and not(harInstitusjonsopphold))){
                includePhrase(TBU1122_Generated(beregningUfore_totalNetto))
            }

            //IF(  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false  ) THEN      INCLUDE ENDIF
            showIf(((barnetilleggSerkull_innvilget or barnetilleggFelles_innvilget) and gjenlevendetillegg_GTinnvilget and not(ektefelletillegg_ETinnvilget))){
                includePhrase(TBU1123_Generated(beregningUfore_totalNetto))
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_TotalNetto > 0) THEN      INCLUDE ENDIF
            showIf(beregningUfore_totalNetto.greaterThan(0)){
                includePhrase(Ufoeretrygd.UtbetalingsdatoUfoeretrygd(beregningUfore_totalNetto.greaterThan(0)))
            }
            includePhrase(Ufoeretrygd.ViktigAALeseHeleBrevet)
            //[TBU1029]

            includePhrase(Vedtak.BegrunnelseOverskrift)

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
                                Bokmal to "Endringen i inntekten din gjør at uføretrygden ikke lenger er redusert. ",
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
                        showIf(beregningUfore_BelopNyUT.equalTo(0) and uforetrygdOrdiner_forventetInntekt.lessThanOrEqual(uforetrygdOrdiner_inntektstak)){
                            text (
                                Bokmal to "Endring i inntekten din gjør at du ikke får utbetalt uføretrygd for resten av året. ",
                                Nynorsk to "Endring i inntekta di gjer at du ikkje får utbetalt uføretrygd for resten av året. ",
                            )
                        }
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense >= PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak) THEN      INCLUDE ENDIF
                showIf(uforetrygdOrdiner_inntektsgrense.greaterThanOrEqual(uforetrygdOrdiner_inntektstak)){
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
                showIf(barnetilleggFelles_innvilget or barnetilleggSerkull_innvilget){
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
                        showIf(not(barnetilleggSerkull_innvilget) and barnetilleggFelles_innvilget){
                            text (
                                Bokmal to "Fordi",
                                Nynorsk to "Fordi",
                            )
                        }

                        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) THEN      INCLUDE ENDIF
                        showIf(barnetilleggSerkull_innvilget and barnetilleggFelles_innvilget){
                            text (
                                Bokmal to "For",
                                Nynorsk to "For",
                            )
                        }

                        //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
                        showIf(barnetilleggFelles_innvilget){
                            textExpr (
                                Bokmal to " ".expr() + ifElse(harFlereFellesBarn, "barna", "barnet") + " ",
                                Nynorsk to " ".expr() + ifElse(harFlereFellesBarn, "barna", "barnet") + " ",
                            )
                        }

                        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) THEN      INCLUDE ENDIF
                        showIf(barnetilleggSerkull_innvilget and barnetilleggFelles_innvilget){
                            text (
                                Bokmal to "som ",
                                Nynorsk to "som ",
                            )
                        }

                        //PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true
                        showIf(barnetilleggFelles_innvilget){
                            textExpr (
                                Bokmal to "bor med begge sine foreldre, bruker vi i tillegg din ".expr() + borMedSivilstand.ubestemtForm() + "s inntekt når vi fastsetter størrelsen på barnetillegget ditt. ",
                                Nynorsk to "bur saman med begge foreldra sine, bruker vi i tillegg ".expr() + borMedSivilstand.bestemtForm() + " din si inntekt når vi fastset storleiken på barnetillegget ditt. ",
                            )
                        }

                        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) THEN      INCLUDE ENDIF
                        showIf(barnetilleggSerkull_innvilget and barnetilleggFelles_innvilget){
                            textExpr (
                                Bokmal to "For ".expr() + ifElse(harFlereSaerkullsbarn, "barna", "barnet") + " som ikke bor sammen med begge foreldrene bruker vi kun inntekten din. ",
                                Nynorsk to "For ".expr() + ifElse(harFlereSaerkullsbarn, "barna", "barnet") + " som ikkje bur saman med begge foreldra bruker vi berre inntekta di. ",
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
                showIf(barnetilleggFelles_innvilget){
                    //[TBU4007_NN, TBU4007]

                    paragraph {
                        textExpr (
                            Bokmal to "Vi har beregnet barnetillegget på nytt ut fra inntekten din på ".expr() + barnetilleggFelles_brukersInntektTilAvkortning.format() + " kroner og inntekten til din " + borMedSivilstand.ubestemtForm() + " på " + barnetilleggFelles_inntektAnnenForelder.format() + " kroner. ",
                            Nynorsk to "Vi har berekna barnetillegget på nytt ut frå inntekta di på ".expr() + barnetilleggFelles_brukersInntektTilAvkortning.format() + " kroner og inntekta til " + borMedSivilstand.bestemtForm() + " din på " + barnetilleggFelles_inntektAnnenForelder.format() + " kroner. ",
                        )

                        //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBbelopFratrukketAnnenForeldersInntekt > 0) THEN      INCLUDE ENDIF
                        showIf(barnetilleggFelles_belopFratrukketAnnenForeldersInntekt.greaterThan(0)){
                            textExpr (
                                Bokmal to "Folketrygdens grunnbeløp på inntil ".expr() + beregningUfore_grunnbelop.format() + " kroner er holdt utenfor din " + borMedSivilstand.ubestemtForm() + "s inntekt. ",
                                Nynorsk to "Grunnbeløpet i folketrygda på inntil ".expr() + beregningUfore_grunnbelop.format() + " kroner er held utanfor inntekta til " + borMedSivilstand.bestemtForm() + " din. ",
                            )
                        }
                        textExpr (
                            Bokmal to "Til sammen utgjør disse inntektene ".expr() + barnetilleggFelles_InntektBruktIAvkortning.format() + " kroner. ",
                            Nynorsk to "Til saman utgjer desse inntektene ".expr() + barnetilleggFelles_InntektBruktIAvkortning.format() + " kroner. ",
                        )

                        showIf(barnetilleggFelles_justeringsbelopPerAr.equalTo(0) and barnetilleggFelles_netto.greaterThan(0)){
                            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto > 0) THEN      INCLUDE ENDIF
                            text (
                                Bokmal to "Dette beløpet er ",
                                Nynorsk to "Dette beløpet er ",
                            )

                            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBInntektBruktiAvkortning > PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBfribelop AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto > 0) THEN      INCLUDE ENDIF
                            showIf(barnetilleggFelles_InntektBruktIAvkortning.greaterThan(barnetilleggFelles_fribeloep)){
                                text (
                                    Bokmal to "høyere",
                                    Nynorsk to "høgare",
                                )
                            }

                            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBInntektBruktiAvkortning <= PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBfribelop AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto > 0) THEN      INCLUDE ENDIF
                            showIf(barnetilleggFelles_InntektBruktIAvkortning.lessThanOrEqual(barnetilleggFelles_fribeloep)){
                                text (
                                    Bokmal to "lavere",
                                    Nynorsk to "lågare",
                                )
                            }

                            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto > 0) THEN      INCLUDE ENDIF
                            textExpr (
                                Bokmal to " enn fribeløpsgrensen på ".expr() + barnetilleggFelles_fribeloep.format() + " kroner. Derfor er barnetillegget ",
                                Nynorsk to " enn fribeløpet på ".expr() + barnetilleggFelles_fribeloep.format() + " kroner. Derfor er barnetillegget ",
                            )

                            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto > 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0) THEN      INCLUDE ENDIF
                            showIf(barnetilleggFelles_innvilget and  barnetilleggSerkull_innvilget and barnetilleggSerkull_netto.greaterThan(0) ){
                                textExpr (
                                    Bokmal to "for ".expr() + ifElse(harFlereFellesBarn, "barna", "barnet") + " som bor med begge sine foreldre ",
                                    Nynorsk to "for ".expr() + ifElse(harFlereFellesBarn, "barna", "barnet") + " som bur saman med begge sine foreldra ",
                                )
                            }

                            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBInntektBruktiAvkortning <= PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBfribelop AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto > 0) THEN      INCLUDE ENDIF
                            showIf(barnetilleggFelles_InntektBruktIAvkortning.lessThanOrEqual(barnetilleggFelles_fribeloep) ){
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
                        showIf(barnetilleggFelles_justeringsbelopPerAr.notEqualTo(0)){
                            text (
                                Bokmal to "Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget",
                                Nynorsk to "Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget",
                            )
                            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr <> 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) THEN      INCLUDE ENDIF
                            showIf( barnetilleggSerkull_innvilget and barnetilleggSerkull_netto.greaterThan(0)){
                                textExpr (
                                    Bokmal to " for ".expr() + ifElse(harFlereFellesBarn, "barna", "barnet") + " som bor med begge sine foreldre",
                                    Nynorsk to " for ".expr() + ifElse(harFlereFellesBarn, "barna", "barnet") + " som bur saman med begge foreldra sine",
                                )
                            }

                            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr <> 0) THEN      INCLUDE ENDIF
                            text (
                                Bokmal to ". ",
                                Nynorsk to ". ",
                            )

                            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr <> 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0) THEN      INCLUDE ENDIF
                            showIf( barnetilleggFelles_netto.equalTo(0)){
                                text (
                                    Bokmal to "Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året.",
                                    Nynorsk to "Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året.",
                                )
                            }
                        }
                    }
                }

                //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) THEN      INCLUDE ENDIF
                showIf(barnetilleggSerkull_innvilget and barnetilleggFelles_innvilget){
                    //[TBU4008_NN, TBU4008]

                    paragraph {
                        textExpr (
                            Bokmal to "Barnetillegget for ".expr() + ifElse(harFlereSaerkullsbarn, "barna", "barnet") + " som ikke bor sammen med begge foreldrene er beregnet ut fra inntekten din på " + barnetilleggSerkull_inntektBruktIAvkortning.format() + " kroner. ",
                            Nynorsk to "Barnetillegget for ".expr() + ifElse(harFlereSaerkullsbarn, "barna", "barnet") + " som ikkje bur saman med begge foreldra er berekna ut frå inntekta di på " + barnetilleggSerkull_inntektBruktIAvkortning.format() + " kroner. ",
                        )

                        //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) THEN      INCLUDE ENDIF
                        showIf(barnetilleggSerkull_justeringsbelopPerAr.equalTo(0) and barnetilleggSerkull_netto.greaterThan(0)){
                            text (
                                Bokmal to "Dette er ",
                                Nynorsk to "Dette er ",
                            )
                            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning > PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBfribelop AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) THEN      INCLUDE ENDIF
                            showIf(barnetilleggSerkull_inntektBruktIAvkortning.greaterThan(barnetilleggSerkull_fribeloep)){
                                text (
                                    Bokmal to "høyere",
                                    Nynorsk to "høgare",
                                )
                            }

                            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning <= PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBfribelop AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) THEN      INCLUDE ENDIF
                            showIf(barnetilleggSerkull_inntektBruktIAvkortning.lessThanOrEqual(barnetilleggSerkull_fribeloep) ){
                                text (
                                    Bokmal to "lavere",
                                    Nynorsk to "lågere",
                                )
                            }

                            //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) THEN      INCLUDE ENDIF
                            textExpr (
                                Bokmal to " enn fribeløpsgrensen på ".expr() + barnetilleggSerkull_fribeloep.format() + " kroner. Dette barnetillegget er derfor ",
                                Nynorsk to " enn fribeløpet på ".expr() + barnetilleggSerkull_fribeloep.format() + " kroner. Derfor er barnetillegget ",
                            )

                            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning <= PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBfribelop AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) THEN      INCLUDE ENDIF
                            showIf(barnetilleggSerkull_inntektBruktIAvkortning.lessThanOrEqual(barnetilleggSerkull_fribeloep)){
                                text (
                                    Bokmal to "ikke lenger ",
                                    Nynorsk to "ikkje lenger ",
                                )
                            }

                            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopGammelBTFB > PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggFellesYK_BelopNyBTFB AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopGammelBTSB > PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_BarnetilleggSerkullYK_BelopNyBTSB AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) THEN      INCLUDE ENDIF
                            showIf(beregningUfore_BelopGammelBTFB.greaterThan(beregningUfore_BelopNyBTFB) and beregningUfore_BelopGammelBTSB.greaterThan(beregningUfore_BelopNyBTSB)){
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
                        showIf(barnetilleggSerkull_justeringsbelopPerAr.notEqualTo(0) and barnetilleggSerkull_netto.greaterThan(0)){
                            textExpr (
                                Bokmal to "Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget for ".expr() + ifElse(harFlereSaerkullsbarn, "barna", "barnet") + " som ikke bor sammen med begge foreldrene. ",
                                Nynorsk to "Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget for ".expr() + ifElse(harFlereSaerkullsbarn, "barna", "barnet") + " som ikkje bur saman med begge foreldra. ",
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
                showIf(barnetilleggSerkull_innvilget and not(barnetilleggFelles_innvilget)){
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
                        showIf(barnetilleggSerkull_inntektBruktIAvkortning.lessThanOrEqual(barnetilleggSerkull_fribeloep) and barnetilleggSerkull_justeringsbelopPerAr.equalTo(0) and barnetilleggSerkull_netto.greaterThan(0)){
                            text (
                                Bokmal to "lavere",
                                Nynorsk to "lågare",
                            )
                        }

                        //IF(PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) THEN      INCLUDE ENDIF
                        showIf(barnetilleggSerkull_justeringsbelopPerAr.equalTo(0) and barnetilleggSerkull_netto.greaterThan(0)){
                            textExpr (
                                Bokmal to " enn fribeløpsgrensen på ".expr() + barnetilleggSerkull_fribeloep.format() + " kroner. Barnetillegget er derfor ",
                                Nynorsk to " enn fribeløpet på ".expr() + barnetilleggSerkull_fribeloep.format() + " kroner. Derfor er barnetillegget ",
                            )
                        }

                        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning <= PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBfribelop AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_JusteringsbelopPerAr = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto > 0) THEN      INCLUDE ENDIF
                        showIf(barnetilleggSerkull_inntektBruktIAvkortning.lessThanOrEqual(barnetilleggSerkull_fribeloep) and barnetilleggSerkull_justeringsbelopPerAr.equalTo(0) and barnetilleggSerkull_netto.greaterThan(0)){
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
                                Bokmal to "Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget. ",
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
                showIf(barnetilleggFelles_netto.equalTo(0) and barnetilleggFelles_innvilget and barnetilleggFelles_justeringsbelopPerAr.equalTo(0)){
                    //[TBU4010_NN, TBU4010]

                    paragraph {
                        text (
                            Bokmal to "Barnetillegget ",
                            Nynorsk to "Barnetillegget ",
                        )

                        //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) THEN      INCLUDE ENDIF
                        showIf(barnetilleggSerkull_innvilget and barnetilleggFelles_innvilget){
                            textExpr (
                                Bokmal to "for ".expr() + ifElse(harFlereFellesBarn, "barna", "barnet") + " som bor med begge sine foreldre ",
                                Nynorsk to "for ".expr() + ifElse(harFlereFellesBarn, "barna", "barnet") + " som bur saman med begge forelda sine ",
                            )
                        }
                        textExpr (
                            Bokmal to "blir ikke utbetalt fordi den samlede inntekten til deg og din ".expr() + borMedSivilstand.ubestemtForm() + " er høyere enn " + barnetilleggFelles_inntektstak.format() + " kroner.",
                            Nynorsk to "blir ikkje utbetalt fordi den samla inntekta til deg og ".expr() + borMedSivilstand.bestemtForm() + " din er høgare enn " + barnetilleggFelles_inntektstak.format() + " kroner.",
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
                        showIf(barnetilleggSerkull_innvilget and barnetilleggFelles_innvilget){
                            textExpr (
                                Bokmal to "for ".expr() + ifElse(harFlereSaerkullsbarn, "barna", "barnet") + " som ikke bor sammen med begge foreldrene ",
                                Nynorsk to "for ".expr() + ifElse(harFlereSaerkullsbarn, "barna", "barnet") + " som ikkje bur saman med begge foreldra ",
                            )
                        }
                        textExpr (
                            Bokmal to "blir ikke utbetalt fordi inntekten din er høyere enn ".expr() + barnetilleggSerkull_inntektstak.format() + " kroner.",
                            Nynorsk to "blir ikkje utbetalt fordi inntekta di er høgare enn ".expr() + barnetilleggSerkull_inntektstak.format() + " kroner.",
                        )
                    }
                }

                //IF( FF_CheckIfFirstDayAndMonthOfYear(PE_VedtaksData_VirkningFOM) = false AND ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert= true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert= true ) ) THEN      INCLUDE ENDIF
                showIf(not(virkFomErFoersteJanuar) and (barnetilleggFelles_inntektPeriodisert or barnetilleggSerkull_inntektPeriodisert or barnetilleggFelles_fribelopPeriodisert or barnetilleggSerkull_fribelopPeriodisert)){
                    //[TBU4012]
                    paragraph {
                        text (
                            Bokmal to "Fordi du ikke har barnetillegg ",
                            Nynorsk to "Fordi du ikkje har barnetillegg ",
                        )

                        showIf(barnetilleggFelles_innvilget and barnetilleggSerkull_innvilget){
                            showIf((barnetilleggFelles_inntektPeriodisert and not(barnetilleggSerkull_inntektPeriodisert)) or barnetilleggFelles_fribelopPeriodisert and not(barnetilleggSerkull_fribelopPeriodisert)){
                                textExpr (
                                    Bokmal to "for ".expr() + ifElse(harFlereFellesBarn, "barna", "barnet") + " som bor med begge sine foreldre ",
                                    Nynorsk to "for ".expr() + ifElse(harFlereFellesBarn, "barna", "barnet") + " som bur saman med begge foreldra sine ",

                                    )
                            }

                            //IF( ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert= true ) OR ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = false AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert= true ) AND ( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true ) )THEN   INCLUDE ENDIF
                            showIf((not(barnetilleggFelles_inntektPeriodisert) and barnetilleggSerkull_inntektPeriodisert) or not(barnetilleggFelles_fribelopPeriodisert) and barnetilleggSerkull_fribelopPeriodisert){
                                textExpr (
                                    Bokmal to "for ".expr() + ifElse(harFlereSaerkullsbarn, "barna", "barnet") + " som ikke bor sammen med begge foreldrene ",
                                    Nynorsk to "for ".expr() + ifElse(harFlereSaerkullsbarn, "barna", "barnet") + " som ikkje bur saman med begge foreldra ",
                                )
                            }
                        }
                        //IF( ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = true AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert= false ) OR ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert= false ) AND ( PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true ) ) THEN      INCLUDE ENDIF

                        text (
                            Bokmal to "hele året er ",
                            Nynorsk to "heile året, er ",
                        )

                        //IF(( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert= true )) THEN   INCLUDE ENDIF
                        showIf((barnetilleggFelles_inntektPeriodisert or barnetilleggSerkull_inntektPeriodisert)){
                            text (
                                Bokmal to "inntektene ",
                                Nynorsk to "inntektene ",
                            )
                        }

                        showIf(barnetilleggFelles_fribelopPeriodisert or barnetilleggSerkull_fribelopPeriodisert) {
                            //IF( ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert= true ) AND ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert= true ) )THEN   INCLUDE ENDIF
                            showIf(barnetilleggFelles_inntektPeriodisert or barnetilleggSerkull_inntektPeriodisert){
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
                        showIf(not(barnetilleggFelles_inntektPeriodisert) and not(barnetilleggSerkull_inntektPeriodisert) and (barnetilleggFelles_fribelopPeriodisert or barnetilleggSerkull_fribelopPeriodisert)){
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
                        showIf(harKravaarsakSivilstandsendring){
                            text (
                                Bokmal to "Fordi sivilstanden din har endret seg er ",
                                Nynorsk to "Fordi sivilstanden din har endra seg, er ",
                            )
                        }

                        //IF( (PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert= true) AND PE_Vedtaksdata_Kravhode_KravArsakType = "sivilstandsendring" ) THEN   INCLUDE ENDIF
                        showIf((barnetilleggFelles_inntektPeriodisert or barnetilleggSerkull_inntektPeriodisert) and harKravaarsakSivilstandsendring){
                            text (
                                Bokmal to "inntektene ",
                                Nynorsk to "inntektene ",
                            )
                        }

                        //IF( ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_InntektPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_InntektPeriodisert= true ) AND ( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert= true ) AND ( PE_Vedtaksdata_Kravhode_KravArsakType = "sivilstandsendring" ) )THEN   INCLUDE ENDIF
                        showIf((barnetilleggFelles_inntektPeriodisert or barnetilleggSerkull_inntektPeriodisert) and (barnetilleggFelles_fribelopPeriodisert or barnetilleggSerkull_fribelopPeriodisert) and harKravaarsakSivilstandsendring){
                            text (
                                Bokmal to "og ",
                                Nynorsk to "og ",
                            )
                        }

                        //IF( (PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_FribelopPeriodisert = true OR PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_FribelopPeriodisert= true) AND PE_Vedtaksdata_Kravhode_KravArsakType = "sivilstandsendring"  )THEN   INCLUDE ENDIF
                        showIf((barnetilleggFelles_fribelopPeriodisert or barnetilleggSerkull_fribelopPeriodisert) and harKravaarsakSivilstandsendring){
                            text (
                                Bokmal to "fribeløpet ",
                                Nynorsk to "fribeløpet ",
                            )
                        }

                        //PE_Vedtaksdata_Kravhode_KravArsakType = "sivilstandsendring"
                        showIf(harKravaarsakSivilstandsendring){
                            text (
                                Bokmal to "justert slik at de kun gjelder for den framtidige perioden du mottar barnetillegg. ",
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
            showIf(not(barnetilleggSerkull_innvilget) and not(barnetilleggFelles_innvilget) and not(gjenlevendetillegg_GTinnvilget) and not(ektefelletillegg_ETinnvilget)){
                //[TBU2263NN, TBU2263]

                paragraph {
                    text (
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14 og 22-12.",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14 og 22-12.",
                    )
                }
            }

            //IF(  (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT  ) THEN      INCLUDE ENDIF
            showIf((barnetilleggSerkull_innvilget or barnetilleggFelles_innvilget) and not(gjenlevendetillegg_GTinnvilget) and not(ektefelletillegg_ETinnvilget) and beregningUfore_BelopGammelUT.notEqualTo(beregningUfore_BelopNyUT)){
                //[TBU2264NN, TBU2264]

                paragraph {
                    text (
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14 til 12-16 og 22-12",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14 til 12-16 og 22-12",
                    )

                    //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                    showIf(barnetilleggReduksjonsgrunnlagHarRegelverkstypeOvergang2016){
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
            showIf((barnetilleggSerkull_innvilget or barnetilleggFelles_innvilget) and not(gjenlevendetillegg_GTinnvilget) and ektefelletillegg_ETinnvilget){
                //[TBU2265NN, TBU2265]

                paragraph {
                    text (
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14 til 12-16 og 22-12 og overgangsforskriften §8",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14 til 12-16 og 22-12 og overgangsforskrifta § 8",
                    )

                    //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                    showIf(barnetilleggReduksjonsgrunnlagHarRegelverkstypeOvergang2016){
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
            showIf((barnetilleggSerkull_innvilget or barnetilleggFelles_innvilget) and gjenlevendetillegg_GTinnvilget and not(ektefelletillegg_ETinnvilget)){
                //[TBU2266NN, TBU2266]

                paragraph {
                    text (
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14 til 12-16, 12-18 og 22-12",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14 til 12-16, 12-18 og 22-12",
                    )

                    //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                    showIf(barnetilleggReduksjonsgrunnlagHarRegelverkstypeOvergang2016){
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
            showIf(not(barnetilleggSerkull_innvilget) and not(barnetilleggFelles_innvilget) and gjenlevendetillegg_GTinnvilget and not(ektefelletillegg_ETinnvilget)){
                //[TBU2267NN, TBU2267]

                paragraph {
                    text (
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14, 12- 18 og 22-12.",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14, 12-18 og 22-12.",
                    )
                }
            }

            //IF(PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = true) THEN      INCLUDE ENDIF
            showIf(not(barnetilleggSerkull_innvilget) and not(barnetilleggFelles_innvilget) and not(gjenlevendetillegg_GTinnvilget) and ektefelletillegg_ETinnvilget){
                //[TBU2268NN, TBU2268]

                paragraph {
                    text (
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14 og 22-12 og overgangsforskriften § 8.",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14 og 22-12 og overgangsforskrifta § 8.",
                    )
                }
            }

            //IF((PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true  OR PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true)  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_Gjenlevendetillegg_GTinnvilget = false  AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_Ektefelletillegg_ETinnvilget = false  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopGammelUT = PE_Vedtaksdata_BeregningsData_BeregningUfore_Belopsendring_UforetrygdOrdinerYK_BelopNyUT)  THEN      INCLUDE ENDIF
            showIf((barnetilleggSerkull_innvilget or barnetilleggFelles_innvilget) and not(gjenlevendetillegg_GTinnvilget) and not(ektefelletillegg_ETinnvilget) and beregningUfore_BelopGammelUT.equalTo(beregningUfore_BelopNyUT)){
                //[TBU4014_NN, TBU4014]

                paragraph {
                    text (
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-15, 12-16 og 22-12",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-15, 12-16 og 22-12",
                    )

                    //PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_Reduksjonsgrunnlag_BarnetilleggRegelverkType = "overgangsregler_2016"
                    showIf(barnetilleggReduksjonsgrunnlagHarRegelverkstypeOvergang2016){
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

                title1 {
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

                title1 {
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
                            Bokmal to "Ut fra den samlede inntekten til deg og din ".expr() + borMedSivilstand.ubestemtForm() + " er barnetillegget vurdert på nytt. Barnetillegget er redusert for hele året ut fra den inntekten som overstiger fribeløpet. Den årlige reduksjonen av barnetillegget er " + barnetilleggFelles_avkortningsbeloepPerAar.format() + " kroner.",
                            Nynorsk to "Ut frå dei samla inntektene til deg og ".expr() + borMedSivilstand.bestemtForm() + " din er barnetillegget blitt vurdert på nytt. Barnetillegget er redusert for heile året ut frå inntekta som overstig fribeløpet. Den årlege reduksjonen av barnetillegget er " + barnetilleggFelles_avkortningsbeloepPerAar.format() + " kroner.",
                        )

                        //IF(( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr <> 0 )) THEN   INCLUDE ENDIF
                        showIf(barnetilleggFelles_justeringsbelopPerAr.notEqualTo(0)){
                            text (
                                Bokmal to " Vi har tatt hensyn til hvordan barnetillegget eventuelt har vært redusert i starten av året, og vi har derfor ",
                                Nynorsk to " Vi har teke omsyn til korleis barnetillegget eventuelt har vore redusert i starten av året, og vi har derfor ",
                            )
                        }

                        //IF(( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr <> 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr > 0 )) THEN   INCLUDE ENDIF
                        showIf(barnetilleggFelles_justeringsbelopPerAr.greaterThan(0)){
                            text (
                                Bokmal to "lagt til",
                                Nynorsk to "lagt til",
                            )
                        }

                        //IF(( PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr <> 0 AND PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_JusteringsbelopPerAr < 0 )) THEN   INCLUDE ENDIF
                        showIf(barnetilleggFelles_justeringsbelopPerAr.lessThan(0)){
                            text (
                                Bokmal to "trukket fra",
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
								Bokmal to " ".expr() + PE_BarnetilleggSerkull_JusteringsbelopPerArUtenMinus.format() + " kroner i det vi reduserer barnetillegget med for resten av året.",
								Nynorsk to " ".expr() + PE_BarnetilleggSerkull_JusteringsbelopPerArUtenMinus.format() + " kroner i det vi reduserte barnetillegget med for resten av året.",
                            )
                        }
                        textExpr (
                            Bokmal to " Du har derfor rett til en utbetaling av barnetillegg på ".expr() + barnetilleggSerkull_netto.format() + " kroner per måned for resten av året.",
                            Nynorsk to " Du har derfor rett til ei utbetaling av barnetillegg på ".expr() + barnetilleggSerkull_netto.format() + " kroner per månad for resten av året.",
                        )
                    }
                }

                //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true) OR (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) ) THEN   INCLUDE ENDIF
                showIf((barnetilleggSerkull_netto.equalTo(0) and barnetilleggSerkull_innvilget) or (barnetilleggFelles_netto.equalTo(0) and barnetilleggFelles_innvilget)){
                    //[TBU4049_NN, TBU4049]

                    paragraph {
                        textExpr (
                            Bokmal to "Du får ikke utbetalt barnetillegget for ".expr() + ifElse(harTilleggForFlereBarn, "barna", "barnet") + " som ",
                            Nynorsk to "Du får ikkje utbetalt barnetillegget for ".expr() + ifElse(harTilleggForFlereBarn, "barna", "barnet") + " som ",
                        )

                        //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true) AND (   PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = false   OR   (     PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true     AND     PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto <> 0) ) )THEN   INCLUDE ENDIF
                        showIf(barnetilleggFelles_netto.equalTo(0) and barnetilleggFelles_innvilget and (not(barnetilleggSerkull_innvilget) or (barnetilleggSerkull_innvilget and barnetilleggSerkull_netto.notEqualTo(0)))){
                            text (
                                Bokmal to "bor med begge sine foreldre",
                                Nynorsk to "bur med begge foreldra",
                            )
                        }

                        //IF( (PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto = 0 AND PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBinnvilget = true) AND (   PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = false   OR   (     PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBinnvilget = true     AND     PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto <> 0) ) )THEN   INCLUDE ENDIF
                        showIf(barnetilleggSerkull_netto.equalTo(0) and barnetilleggSerkull_innvilget and (not(barnetilleggFelles_innvilget) or (barnetilleggFelles_innvilget and barnetilleggFelles_netto.notEqualTo(0)))){
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

            title1 {
                text (
                    Bokmal to "Du må melde fra om endringer i inntekt",
                    Nynorsk to "Du må melde frå om endringar i inntekt",
                )
            }
            includePhrase(TBU2278_Generated(barnetilleggSerkull_innvilget, barnetilleggFelles_innvilget))

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt < PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak > PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense) THEN      INCLUDE ENDIF
            showIf(uforetrygdOrdiner_forventetInntekt.lessThan(uforetrygdOrdiner_inntektstak) and uforetrygdOrdiner_inntektstak.greaterThan(uforetrygdOrdiner_inntektsgrense)){
                includePhrase(TBU2279_Generated(uforetrygdOrdiner_inntektstak))
            }

            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt < PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak <= PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense) THEN      INCLUDE ENDIF
            showIf(uforetrygdOrdiner_forventetInntekt.lessThan(uforetrygdOrdiner_inntektstak) and uforetrygdOrdiner_inntektstak.lessThanOrEqual(uforetrygdOrdiner_inntektsgrense)){
                includePhrase(TBU3740_Generated(uforetrygdOrdiner_inntektsgrense))
            }
            includePhrase(TBU2280_Generated(barnetilleggSerkull_innvilget, barnetilleggFelles_innvilget))
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