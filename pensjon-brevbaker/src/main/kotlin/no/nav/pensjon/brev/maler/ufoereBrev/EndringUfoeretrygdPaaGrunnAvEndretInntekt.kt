package no.nav.pensjon.brev.maler.ufoereBrev

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.maler.fraser.ufoer.Ufoeretrygd
import no.nav.pensjon.brev.maler.ufoereBrev.EndringUfoeretrygdPaaGrunnAvEndretInntektDto.KravAarsak.INSTOPPHOLD
import no.nav.pensjon.brev.maler.ufoereBrev.EndringUfoeretrygdPaaGrunnAvEndretInntektDto.KravAarsak.SOKNAD_BT
import no.nav.pensjon.brev.maler.ufoereBrev.EndringUfoeretrygdPaaGrunnAvEndretInntektDtoSelectors.beloepOekt
import no.nav.pensjon.brev.maler.ufoereBrev.EndringUfoeretrygdPaaGrunnAvEndretInntektDtoSelectors.beloepRedusert
import no.nav.pensjon.brev.maler.ufoereBrev.EndringUfoeretrygdPaaGrunnAvEndretInntektDtoSelectors.beloepsgrense
import no.nav.pensjon.brev.maler.ufoereBrev.EndringUfoeretrygdPaaGrunnAvEndretInntektDtoSelectors.beregningsgrunnlagYrkesskadeBest
import no.nav.pensjon.brev.maler.ufoereBrev.EndringUfoeretrygdPaaGrunnAvEndretInntektDtoSelectors.faktiskInntektsgrense
import no.nav.pensjon.brev.maler.ufoereBrev.EndringUfoeretrygdPaaGrunnAvEndretInntektDtoSelectors.faktiskInntektsgrenseMinus60000
import no.nav.pensjon.brev.maler.ufoereBrev.EndringUfoeretrygdPaaGrunnAvEndretInntektDtoSelectors.forventetInntekt
import no.nav.pensjon.brev.maler.ufoereBrev.EndringUfoeretrygdPaaGrunnAvEndretInntektDtoSelectors.grunnbeloep
import no.nav.pensjon.brev.maler.ufoereBrev.EndringUfoeretrygdPaaGrunnAvEndretInntektDtoSelectors.inntektEtterUfoerhet
import no.nav.pensjon.brev.maler.ufoereBrev.EndringUfoeretrygdPaaGrunnAvEndretInntektDtoSelectors.inntektsgrense
import no.nav.pensjon.brev.maler.ufoereBrev.EndringUfoeretrygdPaaGrunnAvEndretInntektDtoSelectors.kompensasjonsgrad
import no.nav.pensjon.brev.maler.ufoereBrev.EndringUfoeretrygdPaaGrunnAvEndretInntektDtoSelectors.kravAarsak
import no.nav.pensjon.brev.maler.ufoereBrev.EndringUfoeretrygdPaaGrunnAvEndretInntektDtoSelectors.nettoAkk
import no.nav.pensjon.brev.maler.ufoereBrev.EndringUfoeretrygdPaaGrunnAvEndretInntektDtoSelectors.nettoRestAar
import no.nav.pensjon.brev.maler.ufoereBrev.EndringUfoeretrygdPaaGrunnAvEndretInntektDtoSelectors.ordinaerNetto
import no.nav.pensjon.brev.maler.ufoereBrev.EndringUfoeretrygdPaaGrunnAvEndretInntektDtoSelectors.overInntektEtterUfoerhet
import no.nav.pensjon.brev.maler.ufoereBrev.EndringUfoeretrygdPaaGrunnAvEndretInntektDtoSelectors.overInntektEtterUfoerhet80prosent
import no.nav.pensjon.brev.maler.ufoereBrev.EndringUfoeretrygdPaaGrunnAvEndretInntektDtoSelectors.totaltUfoerePerMnd
import no.nav.pensjon.brev.maler.ufoereBrev.EndringUfoeretrygdPaaGrunnAvEndretInntektDtoSelectors.ufoeregrad
import no.nav.pensjon.brev.maler.ufoereBrev.EndringUfoeretrygdPaaGrunnAvEndretInntektDtoSelectors.utbetalingsgrad
import no.nav.pensjon.brev.maler.ufoereBrev.EndringUfoeretrygdPaaGrunnAvEndretInntektDtoSelectors.virkFom
import no.nav.pensjon.brev.maler.ufoereBrev.EndringUfoeretrygdPaaGrunnAvEndretInntektDtoSelectors.virkFomErFoersteJanuar
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import java.time.LocalDate


data class EndringUfoeretrygdPaaGrunnAvEndretInntektDto(
    val virkFom: LocalDate, //PE_VedtaksData_VirkningFOM
    val beloepOekt: Boolean,
    val beloepRedusert: Boolean,
    val totaltUfoerePerMnd: Kroner,
    val beregningsgrunnlagYrkesskadeBest: Boolean, //PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Ytelsesgrunnlag_BeregningsgrunnlagYrkesskadeBest
    val inntektstakUfoeretrygdOrdinaer: Kroner, //PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektstak

    val ufoeregrad: Int, //PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad

    val utbetalingsgrad: Int, //PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad
    val beloepsgrense: Kroner, // PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense
    val grunnbeloep: Kroner, // PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Grunnbelop
    val inntektEtterUfoerhet: Kroner, //PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUInntekt
    val overInntektEtterUfoerhet: Kroner, //PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Oieu
    val overInntektEtterUfoerhet80prosent: Kroner, //PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Oieu

    //Fra logic designer:
    //Om PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_InntektsgrenseNesteAar ikke er 0, bruk den.
    // Ellers bruk PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense
    val faktiskInntektsgrense: Kroner,
    val faktiskInntektsgrenseMinus60000: Kroner,
    val inntektsgrense: Kroner, // PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense
    val kravAarsak: KravAarsak, //PE_Vedtaksdata_Kravhode_KravArsakType
    val kompensasjonsgrad: Double, //PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Kompensasjonsgrad
    val forventetInntekt: Kroner, //PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt
    val redusertBeloep: Boolean,
    val oektBeloep: Boolean,
    //PE_UT_NettoAkk_pluss_NettoRestAr = PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdinaer_NettoAkk + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdinaer_NettoRestAr
    val nettoRestAar: Kroner, //PE_UT_NettoAkk_pluss_NettoRestAr
    val nettoAkk: Kroner, //PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_NettoAkk
    val ordinaerNetto: Kroner, //PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Netto
    val virkFomErFoersteJanuar: Boolean, //(FF_CheckIfFirstDayAndMonthOfYear(PE_VedtaksData_VirkningFOM) = false)

) : BrevbakerBrevdata {
    enum class KravAarsak {
        SOKNAD_BT,
        INSTOPPHOLD,
        ANNET,
    }
}

@TemplateModelHelpers
object EndringUfoeretrygdPaaGrunnAvEndretInntekt : AutobrevTemplate<EndringUfoeretrygdPaaGrunnAvEndretInntektDto> {
    override val kode: Brevkode.AutoBrev = Brevkode.AutoBrev.UT_ENDRING_UFOERETRYGD_PGA_INNTEKT
    override val template: LetterTemplate<*, EndringUfoeretrygdPaaGrunnAvEndretInntektDto> = createTemplate(
        name = kode.name,
        letterDataType = EndringUfoeretrygdPaaGrunnAvEndretInntektDto::class,
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - ny beregning av uføretrygd på grunn av endring i opptjening (automatisk)",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title {
            text(
                Bokmal to "NAV har beregnet uføretrygden din på nytt",
            )
        }
        outline {
            val harEktefelletillegg = true.expr() // TODO
            val harGjenlevendetillegg = true.expr() // TODO
            val harBarnetilleggFellesbarn = true.expr() // TODO
            val harBarnetilleggSaerkullsbarn = true.expr() // TODO
            val harBarnetillegg = harBarnetilleggFellesbarn or harBarnetilleggSaerkullsbarn
            val harGradertUfoeretrygd = ufoeregrad.greaterThan(0) and ufoeregrad.lessThan(100)
            val harFullUfoeregrad = ufoeregrad.equalTo(100)
            val inntektsgrenseUnder80ProsentAvInntektFoerUfoerhet = inntektsgrense.lessThan(overInntektEtterUfoerhet80prosent)

            paragraph {
                text(
                    Bokmal to "Vi har mottatt nye opplysninger fra Skatteetaten, og har derfor beregnet uføretrygden din på nytt.",
                )
            }
            paragraph {
                showIf(beloepOekt or beloepRedusert) {
                    textExpr(
                        Bokmal to "Utbetalingen er endret med virkning fra ".expr() + virkFom.format() + ".",
                    )
                } orShow {
                    text(
                        Bokmal to "Dette får ikke betydning for uføretrygden din, og du vil få utbetalt det samme som før.",
                    )
                }
            }

            includePhrase(
                Ufoeretrygd.Beloep(
                    perMaaned = totaltUfoerePerMnd,
                    ektefelle = harEktefelletillegg,
                    ufoeretrygd = true.expr(), // TODO dette er vel alltid sant i dette brevet?
                    gjenlevende = harGjenlevendetillegg,
                    fellesbarn = harBarnetilleggFellesbarn,
                    saerkullsbarn = harBarnetilleggSaerkullsbarn,
                )
            )
            includePhrase(Ufoeretrygd.UtbetalingsdatoUfoeretrygd(true.expr()))
            includePhrase(Ufoeretrygd.ViktigAALeseHeleBrevet)
            includePhrase(Vedtak.BegrunnelseOverskrift)

            paragraph {
                text(
                    Bokmal to "Uføretrygden blir beregnet ut fra inntektsårene før du ble ufør. Vi beregner uføretrygden på nytt dersom du får endringer i:",
                )
                list {
                    item {
                        text(
                            Bokmal to "Pensjonsgivende inntekt",
                        )
                    }
                    item {
                        text(
                            Bokmal to "Omsorgspoeng",
                        )
                    }
                    item {
                        text(
                            Bokmal to "År med inntekt i utlandet",
                        )
                    }
                }
            }
            showIf(beloepOekt) {
                paragraph {
                    textExpr(
                        Bokmal to "Du har fått en endring i opptjeningen din før du ble ufør. Dette gir deg en økt uføretrygd fra".expr() + virkFom.format() + ". Du vil derfor motta en etterbetaling fra NAV",
                    )
                }
            }
            showIf(beloepRedusert) {
                paragraph {
                    textExpr(
                        Bokmal to "Du har fått en endring i opptjeningen din før du ble ufør. Dette gir deg en redusert uføretrygd fra ".expr() + virkFom.format() + ".",
                    )
                }
            }
            paragraph {
                text(
                    Bokmal to "I vedlegget «Opplysninger om beregningen» kan du lese mer om beregningen av uføretrygden din.",
                )
            }

            paragraph {
                showIf(beregningsgrunnlagYrkesskadeBest) {
                    showIf(not(harBarnetillegg) and not(harGjenlevendetillegg) and not(harEktefelletillegg)) {
                        text(
                            Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-11 til 12-14, 12-17 og 22-12.",
                        )
                    }.orShowIf(harBarnetillegg and not(harGjenlevendetillegg) and not(harEktefelletillegg)) {
                        text(
                            Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-11 til 12-17 og 22-12.",
                        )
                    }.orShowIf(not(harBarnetillegg) and harGjenlevendetillegg and not(harEktefelletillegg)) {
                        text(
                            Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-11 til 12-14, 12-17, 12-18 og 22-12.",
                        )
                    }.orShowIf(harBarnetillegg and harGjenlevendetillegg and not(harEktefelletillegg)) {
                        text(
                            Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-11 til 12-18 og 22-12.",
                        )
                    }.orShowIf(harBarnetillegg and not(harGjenlevendetillegg) and harEktefelletillegg) {
                        text(
                            Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-11 til 12-17, 22-12 og overgangsforskriften § 8.",
                        )
                    }
                } orShow {
                    showIf(not(harBarnetillegg) and not(harGjenlevendetillegg) and not(harEktefelletillegg)) {
                        text(
                            Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-11 til 12-14 og 22-12.",
                        )
                    }.orShowIf(harBarnetillegg and not(harGjenlevendetillegg) and not(harEktefelletillegg)) {
                        text(
                            Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-11 til 12-16 og 22-12.",
                        )
                    }.orShowIf(not(harBarnetillegg) and harGjenlevendetillegg and not(harEktefelletillegg)) {
                        text(
                            Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12- 11 til 12-14, 12-18 og 22-12.",
                        )
                    }.orShowIf(harBarnetillegg and harGjenlevendetillegg and not(harEktefelletillegg)) {
                        text(
                            Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-11 til 12-16, 12-18 og 22-12.",
                        )
                    }.orShowIf(harBarnetillegg and not(harGjenlevendetillegg) and harEktefelletillegg) {
                        text(
                            Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-11 til 12-16, 22-12 og overgangsforskriften § 8.",
                        )
                    }.orShowIf(not(harBarnetillegg) and not(harGjenlevendetillegg) and harEktefelletillegg) {
                        text(
                            Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-11 til 12-14, 22-12 og overgangsforskriften § 8.",
                        )
                    }
                }
            }


            //TBU1201
            //(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad < 100 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad > 0)
            //OR
            //(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad)
            showIf(harFullUfoeregrad and utbetalingsgrad.equalTo(100)) {
                title1 {
                    text(
                        Bokmal to "Skal du kombinere uføretrygd og inntekt?",
                    )
                }

            }

            //TBU1203
            //IF(
            //
            //(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad < 100 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad > 0)
            //OR
            //(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad)
            //
            //) THEN
            //     INCLUDE
            //ENDIF
            val utbetalingsgradUnderUfoeregrad = utbetalingsgrad.lessThan(ufoeregrad)
            showIf(harGradertUfoeretrygd or utbetalingsgradUnderUfoeregrad) {
                title1 {
                    text(
                        Bokmal to "For deg som kombinerer uføretrygd og inntekt",
                    )
                }
            }

            //TBU1204
            //PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad
            showIf(utbetalingsgrad.equalTo(ufoeregrad)) {
                paragraph {
                    text(
                        Bokmal to "Du har mulighet til å ha inntekt ved siden av uføretrygden din. Det lønner seg å jobbe, fordi inntekt og uføretrygd alltid vil være høyere enn uføretrygd alene.",
                    )
                }
            }

            //TBU2251
            //(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad
            showIf(utbetalingsgradUnderUfoeregrad) {
                paragraph {
                    text(
                        Bokmal to "Utbetalingen av uføretrygden din er redusert fordi du har inntekt utover inntektsgrensen. Det lønner seg likevel å jobbe, fordi inntekt og uføretrygd alltid vil være høyere enn uføretrygd alene.",
                    )
                }
            }

            //TBU1205
            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad = 100
            //  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Grunnbelop
            //  AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense <> 60000
            //  AND PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUInntekt(1) = 0) THEN
            //     INCLUDE
            //ENDIF
            // TODO går det greit å ikke hente dette fra beregningsvilkår, men å bare hente det fra
            showIf(
                harFullUfoeregrad
                        and beloepsgrense.notEqualTo(grunnbeloep)
                        and beloepsgrense.notEqualTo(60000)
                        and inntektEtterUfoerhet.equalTo(0)
            ) {
                paragraph {
                    textExpr(
                        Bokmal to "Du kan ha en årlig inntekt på 40 prosent av folketrygdens grunnbeløp, uten at uføretrygden din blir redusert. I dag er dette ".expr() + faktiskInntektsgrense.format() + " kroner. Dette er inntektsgrensen din.",
                    )
                }
            }
            //TBU1296
            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad = 100 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense = 60000) THEN
            //     INCLUDE
            //ENDIF
            showIf(harFullUfoeregrad and beloepsgrense.equalTo(60000)) {
                paragraph {
                    text(
                        Bokmal to "Du kan ha en årlig inntekt på 60 000 kroner uten at uføretrygden din blir redusert. Dette er inntektsgrensen din.",
                    )
                }
            }

            // TBU1206
            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Grunnbelop = PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense) THEN
            //     INCLUDE
            //ENDIF
            showIf(grunnbeloep.equalTo(beloepsgrense)) {
                paragraph {
                    textExpr(
                        Bokmal to "Du kan ha en årlig inntekt på folketrygdens grunnbeløp fordi du er i varig tilrettelagt arbeid, uten at uføretrygden din blir redusert. I dag er dette ".expr() + inntektsgrense.format() + " kroner. Dette er inntektsgrensen din.",
                    )
                }
            }

            // TBU1207
            //IF(
            //
            //PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense <> 60000
            //AND
            //PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense <> PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Grunnbelop
            //AND
            //(FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforegrad) < 100
            //AND FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforegrad) > 0)
            //OR

            //(FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_IEUInntekt ) > 0
            //AND
            //FF_GetArrayElement_Float(PE_Vedtaksdata_VilkarsVedtakList_VilkarsVedtak_BeregningsVilkar_Uforegrad) = 100)
            //AND
            //PE_Vedtaksdata_Kravhode_KravArsakType <> "soknad_bt" AND PE_Vedtaksdata_Kravhode_KravArsakType <> "instopphold"
            //) THEN
            //     INCLUDE
            //ENDIF
            showIf(
                (beloepsgrense.notEqualTo(60000)
                        and beloepsgrense.notEqualTo(grunnbeloep)
                        and harGradertUfoeretrygd)
                        or (inntektEtterUfoerhet.greaterThan(0)
                        and harFullUfoeregrad
                        and kravAarsak.isNotAnyOf(SOKNAD_BT, INSTOPPHOLD))
            ) {
                paragraph {
                    textExpr(
                        Bokmal to "Vi har lagt til grunn at du framover skal ha en inntekt på ".expr() + overInntektEtterUfoerhet.format()
                                + " kroner per år. Du kan i tillegg ha en årlig inntekt på 40 prosent av folketrygdens grunnbeløp, uten at uføretrygden din blir redusert. Inntektsgrensen din blir derfor "
                                + faktiskInntektsgrense.format() + " kroner.",
                    )
                }
            }


            //TBU2357
            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Belopsgrense = 60000 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad > 0 AND PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad < 100) THEN
            //     INCLUDE
            //ENDIF
            showIf(beloepsgrense.equalTo(60000) and harGradertUfoeretrygd) {
                paragraph {
                    textExpr(
                        Bokmal to "Vi har lagt til grunn at du framover skal ha en inntekt på ".expr() + faktiskInntektsgrenseMinus60000.format()
                                + " kroner per år. Du kan i tillegg ha en årlig inntekt på 60 000 kroner, uten at uføretrygden din blir redusert. Inntektsgrensen din blir derfor " + faktiskInntektsgrense.format() + " kroner.",
                    )
                }
            }

            //TBU1208
            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense < PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Oifu_x_08) THEN
            //     INCLUDE
            //ENDIF
            showIf(inntektsgrenseUnder80ProsentAvInntektFoerUfoerhet) {
                paragraph {
                    text(
                        Bokmal to "Vi bruker en fastsatt prosentandel når vi justerer uføretrygden din ut fra inntekt. Denne prosentandelen kaller vi kompensasjonsgrad.",
                    )
                }
                paragraph {
                    textExpr(
                        Bokmal to "For deg utgjør kompensasjonsgraden ".expr()
                                + kompensasjonsgrad.format() + " prosent. Det er bare den delen av inntekten din som overstiger "
                                + faktiskInntektsgrense.format() + " kroner, som vi justerer uføretrygden din ut fra. Det betyr at et beløp som tilsvarer "
                                + kompensasjonsgrad.format() + " prosent av den inntekten du har over "
                                + faktiskInntektsgrense.format() + " kroner trekkes fra uføretrygden din.",
                    )
                }
            }

            //TBU2361
            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad
            // AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_ForventetInntekt > PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense
            // AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense < PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Oifu_x_08) THEN
            //     INCLUDE
            //ENDIF
            showIf(utbetalingsgradUnderUfoeregrad and forventetInntekt.greaterThan(inntektsgrense) and inntektsgrenseUnder80ProsentAvInntektFoerUfoerhet) {
                paragraph {
                    textExpr(
                        Bokmal to "Du har tidligere meldt fra om en inntekt på ".expr() + forventetInntekt.format() + " kroner i år.",
                    )
                }
            }

            //TBU2362
            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad
            // AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopRedusert = true
            // AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense < PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Oifu_x_08) THEN
            //     INCLUDE
            //ENDIF
            showIf(utbetalingsgradUnderUfoeregrad and beloepRedusert and inntektsgrenseUnder80ProsentAvInntektFoerUfoerhet) {
                paragraph {
                    text(
                        Bokmal to "Vi har derfor redusert utbetalingen av uføretrygden din for resten av kalenderåret.",
                    )
                }
            }

            //TBU2363
            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BelopOkt = true AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense < PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Oifu_x_08) THEN
            //     INCLUDE
            //ENDIF

            showIf(utbetalingsgradUnderUfoeregrad and beloepOekt and inntektsgrenseUnder80ProsentAvInntektFoerUfoerhet) {
                paragraph {
                    text(
                        Bokmal to "Vi har derfor økt utbetalingen av uføretrygden din for resten av kalenderåret.",
                    )
                }
            }

            //TBU2261
            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad
            // AND PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense < PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Oifu_x_08) THEN
            //     INCLUDE
            //ENDIF
            showIf(utbetalingsgradUnderUfoeregrad and inntektsgrenseUnder80ProsentAvInntektFoerUfoerhet) {
                paragraph {
                    textExpr(
                        Bokmal to "Ut fra den årlige inntekten din vil uføretrygden utgjøre ".expr()
                                + nettoRestAar.format() + " kroner. ",
                    )
                    //IF(FF_CheckIfFirstDayAndMonthOfYear(PE_VedtaksData_VirkningFOM) = false) THEN
                    //     INCLUDE
                    //ENDIF
                    showIf(not(virkFomErFoersteJanuar)) {
                        textExpr(
                            Bokmal to "Hittil i år har du fått utbetalt ".expr() + nettoAkk.format() + " kroner. ",
                        )
                    }
                    textExpr(
                        Bokmal to "Du har derfor rett til en utbetaling av uføretrygd på ".expr() + ordinaerNetto.format() + " kroner per måned for resten av året.",
                    )
                }
            }

            //TBU1210

            paragraph {
                textExpr(
                    Bokmal to "Blir uføretrygden din redusert på grunn av inntekt beholder du likevel uføregraden din på ".expr() + ufoeregrad.format()
                            + " prosent. Du får utbetalt hele uføretrygden igjen dersom du tjener mindre enn inntektsgrensen din.",
                )
            }

            //TBU2364
            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad = PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad) THEN
            //     INCLUDE
            //ENDIF
            showIf(utbetalingsgrad.equalTo(ufoeregrad)){
                //TBU2364
                title1 {
                    text(
                        Bokmal to "Du må melde fra om eventuell inntekt",
                    )
                }
                //TBU2365
                paragraph {
                    text(
                        Bokmal to "Dersom du er i jobb eller har planer om å jobbe, må du melde fra om eventuelle endringer i inntekten din. Det er viktig at du melder fra så tidlig som mulig, slik at du får riktig utbetaling av uføretrygd. Dette kan du gjøre under menyvalget «uføretrygd» når du logger deg inn på $NAV_URL. Her kan du legge inn hvor mye du forventer å tjene i løpet av året. Du vil da kunne se hvor mye du vil få utbetalt i uføretrygd ved siden av inntekten din.",
                    )
                }
            }

            //TBU2366
            //IF(PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Utbetalingsgrad < PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad) THEN
            //     INCLUDE
            //ENDIF
            showIf(utbetalingsgradUnderUfoeregrad) {
                title1 {
                    text(
                        Bokmal to "Du må melde fra om endringer i inntekten",
                    )
                }
                //TBU2367
                paragraph {
                    text(
                        Bokmal to "Du kan melde fra om inntektsendringer under menyvalget «uføretrygd» når du logger deg inn på $NAV_URL. Her kan du legge inn endringer i den forventede årlige inntekten, og se hva dette betyr for utbetalingen av uføretrygden din. For at du skal få en jevn utbetaling av uføretrygden, er det viktig at du melder fra om inntektsendringer så tidlig som mulig.",
                    )
                }
            }
            //

        }
    }
}