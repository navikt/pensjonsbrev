package no.nav.pensjon.brev.maler.vedlegg

import kotlinx.coroutines.awaitCancellation
import no.nav.pensjon.brev.api.model.BeregningsMetode
import no.nav.pensjon.brev.maler.fraser.*
import no.nav.pensjon.brev.maler.fraser.common.Kroner
import no.nav.pensjon.brev.template.Element.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import java.lang.Boolean.FALSE
import java.time.LocalDate
import kotlin.contracts.ReturnsNotNull

// TODO: Lag gjenbrukbar formattering av Kroner(1000) med språkstøtte og enhet (kr/NOK)

data class OpplysningerBruktIBeregningUTDto(
    val anvendtTT_trygdetidsdetaljerGjeldende: Int,
    val avkortningsbelopAr_barnetilleggSBGjeldende: Int,
    val belop_barnetilleggSBGjeldende: Int,
    val belopAr_barnetilleggSBGjeldende: Int,
    val belopArForAvkort_barnetilleggSBGjeldende: Int,
    val belopIEU_inntektEtterUforeGjeldende: Int,
    val belopsgrense_uforetrygdGjeldende: Int,
    val beregningsgrunnlagBelopAr_uforetrygdGjeldende: Int,
    val beregningsgrunnlagBelopAr_yrkesskadeGjeldende: Int,
    val beregningsMetode_trygdetidsdetaljergjeldende: BeregningsMetode,
    val brukerErFlyktning_gjeldendeBeregnetUTPerManed: Boolean,
    val brukersSivilstand_gjeldendeBeregnetUTPerManed: String,
    val erEndret_barnetilleggSBGjeldende: Boolean,
    val erIkkeUtbetaltpgaTak_barnetilleggGrunnlagGjeldende: Boolean,
    val erKonvertert_uforetrygdGjeldende: Boolean,
    val erUnder20Ar_ungUforGjeldende: Boolean,
    val erRedusertMotinntekt_barnetilleggSBGjeldende: Boolean,
    val erRedusertMotTak_barnetilleggGrunnlagGjeldende: Boolean,
    val erSannsynligEndret_inntektForUforeGjeldende: Boolean,
    val erSannsynligEndret_inntektForUforeVedVirk: Boolean,
    val faktiskTTBilateral_trygdetidsdetaljerGjeldende: Int,
    val faktiskTTEOS_trygdetidsdetaljerGjeldende: Int,
    val faktiskTTNordiskKonv_trygdetidsdetaljerGjeldende: Int,
    val faktiskTTNorge_trygdetidsdetaljerGjeldende: Int,
    val forventetInntektAr_inntektsAvkortingGjeldende: Int,
    val framtidigTTNorsk_trygdetidsdetaljerGjeldende: Int,
    val fribelop_barnetilleggSBGjeldende: Int,
    val fribelopEllerInntektErPeriodisert_barnetilleggSBGjeldende: Boolean,
    val gradertOIFU_barnetilleggGrunnlagGjeldende: Int,
    val grunnbelop_gjeldendeBeregnetUTPerManed: Int,
    val ifuInntekt_inntektForUforeGjeldende: Int,
    val inntektBruktIAvkortning_barnetilleggSBGjeldende: Int,
    val inntektOverFribelop_barnetilleggSBGjeldende: Int,
    val inntektstak_barnetilleggSBGjeldende: Int,
    val inntektsgrenseAr_inntektsAvkortingGjeldende: Int,
    val inntektstak_inntektsAvkortingGjeldende: Int,
    val inntektVedSkadetidspunkt_yrkesskadeGjeldende: Int,
    val justeringsbelopAr_barnetilleggSBGjeldende: Int,
    val kompensasjonsgrad_uforetrygdGjeldende: Double,
    val nevnerProRata_trygdetidsdetaljerGjeldende: Int,
    val nevnerTTEOS_trygdetidsdetaljerGjeldende: Int,
    val nevnerTTNordiskKonv_trygdetidsdetaljerGjeldende: Int,
    val prosentsatsGradertOIFU_barnetilleggGrunnlagGjeldende: Int,
    val samletTTNordiskKonv_trygdetidsdetaljerGjeldende: Int,
    val sats_minsteytelseGjeldende: Double,
    val skadetidspunkt_yrkesskadeGjeldende: LocalDate,
    val tellerProRata_trygdetidsdetaljerGjeldende: Int,
    val tellerTTEOS_trygdetidsdetaljerGjeldende: Int,
    val tellerTTNordiskKonv_trygdetidsdetaljerGjeldende: Int,
    val totaltAntallBarn_barnetilleggGrunnlagGjeldende: Int,
    val uforegrad_uforetrygdGjeldende: Int,
    val uforetidspunkt_uforetrygdGjeldende: LocalDate,
    val virkDatoFom_gjeldendeBeregnetUTPerManed: LocalDate,
    val yrkesskadegrad_yrkesskadeGjeldende: Int,
) {
    constructor() : this(
        anvendtTT_trygdetidsdetaljerGjeldende = 0,
        avkortningsbelopAr_barnetilleggSBGjeldende = 0,
        belop_barnetilleggSBGjeldende = 0,
        belopAr_barnetilleggSBGjeldende = 0,
        belopArForAvkort_barnetilleggSBGjeldende = 0,
        belopIEU_inntektEtterUforeGjeldende = 0,
        belopsgrense_uforetrygdGjeldende = 0,
        beregningsgrunnlagBelopAr_uforetrygdGjeldende = 0,
        beregningsgrunnlagBelopAr_yrkesskadeGjeldende = 0,
        beregningsMetode_trygdetidsdetaljergjeldende = BeregningsMetode.FOLKETRYGD,
        brukerErFlyktning_gjeldendeBeregnetUTPerManed = false,
        brukersSivilstand_gjeldendeBeregnetUTPerManed = "",
        erEndret_barnetilleggSBGjeldende = false,
        erIkkeUtbetaltpgaTak_barnetilleggGrunnlagGjeldende = false,
        erRedusertMotinntekt_barnetilleggSBGjeldende = false,
        erRedusertMotTak_barnetilleggGrunnlagGjeldende = false,
        erSannsynligEndret_inntektForUforeGjeldende = false,
        erSannsynligEndret_inntektForUforeVedVirk = false,
        faktiskTTBilateral_trygdetidsdetaljerGjeldende = 0,
        faktiskTTEOS_trygdetidsdetaljerGjeldende = 0,
        faktiskTTNordiskKonv_trygdetidsdetaljerGjeldende = 0,
        faktiskTTNorge_trygdetidsdetaljerGjeldende = 0,
        forventetInntektAr_inntektsAvkortingGjeldende = 0,
        framtidigTTNorsk_trygdetidsdetaljerGjeldende = 0,
        fribelop_barnetilleggSBGjeldende = 0,
        fribelopEllerInntektErPeriodisert_barnetilleggSBGjeldende = false,
        gradertOIFU_barnetilleggGrunnlagGjeldende = 0,
        grunnbelop_gjeldendeBeregnetUTPerManed = 0,
        ifuInntekt_inntektForUforeGjeldende = 0,
        inntektBruktIAvkortning_barnetilleggSBGjeldende = 0,
        inntektOverFribelop_barnetilleggSBGjeldende = 0,
        inntektstak_barnetilleggSBGjeldende = 0,
        inntektsgrenseAr_inntektsAvkortingGjeldende = 0,
        inntektstak_inntektsAvkortingGjeldende = 0,
        inntektVedSkadetidspunkt_yrkesskadeGjeldende = 0,
        justeringsbelopAr_barnetilleggSBGjeldende = 0,
        kompensasjonsgrad_uforetrygdGjeldende = 0.0,
        nevnerProRata_trygdetidsdetaljerGjeldende = 0,
        nevnerTTEOS_trygdetidsdetaljerGjeldende = 0,
        nevnerTTNordiskKonv_trygdetidsdetaljerGjeldende = 0,
        prosentsatsGradertOIFU_barnetilleggGrunnlagGjeldende = 0,
        samletTTNordiskKonv_trygdetidsdetaljerGjeldende = 0,
        sats_minsteytelseGjeldende = 0.0,
        skadetidspunkt_yrkesskadeGjeldende = LocalDate.of(2020, 1, 1),
        tellerProRata_trygdetidsdetaljerGjeldende = 0,
        tellerTTEOS_trygdetidsdetaljerGjeldende = 0,
        tellerTTNordiskKonv_trygdetidsdetaljerGjeldende = 0,
        totaltAntallBarn_barnetilleggGrunnlagGjeldende = 0,
        uforegrad_uforetrygdGjeldende = 0,
        uforetidspunkt_uforetrygdGjeldende = LocalDate.of(2020, 1, 1),
        erKonvertert_uforetrygdGjeldende = false,
        erUnder20Ar_ungUforGjeldende = false,
        virkDatoFom_gjeldendeBeregnetUTPerManed = LocalDate.of(2020, 1, 1),
        yrkesskadegrad_yrkesskadeGjeldende = 0,
    )
}
/*
The condition for the showing of the attachement is:
var erRedusertMotInntekt= GetValue("fag=barnetilleggSBGjeldende=erRedusertMotInntekt").toLowerCase();
var erSannsynligEndret = GetValue("fag=inntektForUforeVedVirk=erSannsynligEndret").toLowerCase();
if(	g.finnesNode("fag=minsteytelseVedVirk")
||	erSannsynligEndret == "true"
||	erRedusertMotInntekt == "true")
 */
val opplysningerBruktIBeregningUT = createAttachment<LangBokmalNynorskEnglish, OpplysningerBruktIBeregningUTDto>(
    title = newText(
        Bokmal to "Opplysninger om beregningen",
        Nynorsk to "Opplysningar om utrekninga",
        English to "Information about calculations"
    ),
    includeSakspart = false,
) {
    paragraph {
        val virkDatoFom =
            argument().map { it.virkDatoFom_gjeldendeBeregnetUTPerManed }.format()
        val grunnbelop = argument().map { Kroner(it.grunnbelop_gjeldendeBeregnetUTPerManed) }.format()
        textExpr(
            Bokmal to "Opplysninger vi har brukt i beregningen fra ".expr() + virkDatoFom.str() + " Folketrygdens grunnbeløp (G) benyttet i beregningen er ".expr() + grunnbelop.str() + " kroner",
            Nynorsk to "Opplysningar vi har brukt i utrekninga frå ".expr() + virkDatoFom.str() + " Grunnbeløpet i folketrygda (G) nytta i utrekninga er ".expr() + grunnbelop.str() + " kroner",
            English to "Data we have used in the calculations as of ".expr() + virkDatoFom.str() + " The National Insurance basic amount (G) used in the calculation is NOK ".expr() + grunnbelop.str() + "."
        )
    }

    table(
        header = {
            column(3) {
                text(
                    Bokmal to "Opplysning",
                    Nynorsk to "Opplysning",
                    English to "Information"
                )
            }
            column(alignment = RIGHT) {
                text(
                    Bokmal to "Verdi",
                    Nynorsk to "Verdi",
                    English to "Value"
                )
            }
        }
    ) {
        // Hvis uforetidspunkt_uforetrygdGjeldende != tom, include
        row {
            cell {
                text(
                    Bokmal to "Uføretidspunkt",
                    Nynorsk to "Uføretidspunkt",
                    English to "Date of disability"
                )
            }
            cell {
                val uforetidspunkt =
                    argument().map { it.uforetidspunkt_uforetrygdGjeldende }.format()
                textExpr(
                    Bokmal to uforetidspunkt,
                    Nynorsk to uforetidspunkt,
                    English to uforetidspunkt
                )
            }
        }
        showIf(argument().map { it.beregningsgrunnlagBelopAr_uforetrygdGjeldende > 0 })
        {
            row {
                cell {
                    text(
                        Bokmal to "Beregningsgrunnlag",
                        Nynorsk to "Utrekningsgrunnlag",
                        English to "Basis for calculation"
                    )
                }
                cell {
                    val beregningsgrunnlagBelopAr =
                        argument().map { Kroner(it.beregningsgrunnlagBelopAr_uforetrygdGjeldende) }.format()
                    textExpr(
                        Bokmal to beregningsgrunnlagBelopAr + " kr",
                        Nynorsk to beregningsgrunnlagBelopAr + " kr",
                        English to beregningsgrunnlagBelopAr + " NOK"
                    )
                }
            }
        }
        showIf(argument().map { it.beregningsgrunnlagBelopAr_yrkesskadeGjeldende > 0 })
        {
            row {
                cell {
                    text(
                        Bokmal to "Beregningsgrunnlag yrkesskade",
                        Nynorsk to "Utrekningsgrunnlag yrkesskade",
                        English to "Basis for calculation due to occupational injury"
                    )
                }
                cell {
                    val beregningsgrunnlagBelopAr =
                        argument().map { Kroner(it.beregningsgrunnlagBelopAr_yrkesskadeGjeldende) }.format()
                    textExpr(
                        Bokmal to beregningsgrunnlagBelopAr + " kr",
                        Nynorsk to beregningsgrunnlagBelopAr + " kr",
                        English to beregningsgrunnlagBelopAr + " NOK"
                    )
                }
            }
        }
        showIf(argument().map { it.ifuInntekt_inntektForUforeGjeldende > 0 })
        {
            row {
                cell {
                    text(
                        Bokmal to "Inntekt før uførhet",
                        Nynorsk to "Inntekt før uførleik",
                        English to "Income prior to disability"
                    )
                }
                cell {
                    val ifuInntekt =
                        argument().map { Kroner(it.ifuInntekt_inntektForUforeGjeldende) }.format()
                    textExpr(
                        Bokmal to ifuInntekt + " kr",
                        Nynorsk to ifuInntekt + " kr",
                        English to ifuInntekt + " NOK"
                    )
                }
            }
        }
        showIf(argument().map { it.belopIEU_inntektEtterUforeGjeldende > 0 })
        {
            row {
                cell {
                    text(
                        Bokmal to "Inntekt etter uførhet",
                        Nynorsk to "Inntekt etter uførleik",
                        English to "Income after disability"
                    )
                }
                cell {
                    val belopIEU =
                        argument().map { Kroner(it.belopIEU_inntektEtterUforeGjeldende) }.format()
                    textExpr(
                        Bokmal to belopIEU + " kr",
                        Nynorsk to belopIEU + " kr",
                        English to belopIEU + " NOK"
                    )
                }
            }
        }
        // Mandatory
        row {
            cell {
                text(
                    Bokmal to "Uføregrad",
                    Nynorsk to "Uføregrad",
                    English to "Degree of disability"
                )
            }
            cell {
                val uforegrad = argument().map { it.uforegrad_uforetrygdGjeldende }.str()
                textExpr(
                    Bokmal to uforegrad + " %",
                    Nynorsk to uforegrad + " %",
                    English to uforegrad + " %"
                )
            }
        }
        // Mandatory
        showIf(argument().map { it.belopsgrense_uforetrygdGjeldende > 0 })
        {
            row {
                cell {
                    text(
                        Bokmal to "Inntektsgrense",
                        Nynorsk to "Inntektsgrense",
                        English to "Income cap"
                    )
                }
                cell {
                    val belopsgrense =
                        argument().map { Kroner(it.belopsgrense_uforetrygdGjeldende) }.format()
                    textExpr(
                        Bokmal to belopsgrense + " kr",
                        Nynorsk to belopsgrense + " kr",
                        English to belopsgrense + " NOK"
                    )
                }
            }
        }
        showIf(argument().map { it.inntektsgrenseAr_inntektsAvkortingGjeldende > 0 })
        {
            row {
                cell {
                    text(
                        Bokmal to "Inntektsgrense",
                        Nynorsk to "Inntektsgrense",
                        English to "Income cap"
                    )
                }
                cell {
                    val inntektsgrenseAr =
                        argument().map { Kroner(it.inntektsgrenseAr_inntektsAvkortingGjeldende) }
                            .format()
                    textExpr(
                        Bokmal to inntektsgrenseAr + " kr",
                        Nynorsk to inntektsgrenseAr + " kr",
                        English to inntektsgrenseAr + " NOK"
                    )
                }
            }
        }
        showIf(argument().map { it.forventetInntektAr_inntektsAvkortingGjeldende > 0 })
        {
            row {
                cell {
                    text(
                        Bokmal to "Forventet inntekt",
                        Nynorsk to "Forventa inntekt",
                        English to "Expected income"
                    )
                }
                cell {
                    val forventetInntektAr =
                        argument().map { Kroner(it.forventetInntektAr_inntektsAvkortingGjeldende) }
                            .format()
                    textExpr(
                        Bokmal to forventetInntektAr + " kr",
                        Nynorsk to forventetInntektAr + " kr",
                        English to forventetInntektAr + " NOK"
                    )
                }
            }
        }
        showIf(argument().map {
            it.kompensasjonsgrad_uforetrygdGjeldende > 0 &&
                (it.inntektsgrenseAr_inntektsAvkortingGjeldende < it.inntektstak_inntektsAvkortingGjeldende)
        }
        ) {
            row {
                cell {
                    text(
                        Bokmal to "Kompensasjonsgrad",
                        Nynorsk to "Kompensasjonsgrad",
                        English to "Percentage of compensation"
                    )
                }
                cell {
                    val kompensasjonsgrad =
                        argument().map { it.kompensasjonsgrad_uforetrygdGjeldende }.format()
                    textExpr(
                        Bokmal to kompensasjonsgrad + " %",
                        Nynorsk to kompensasjonsgrad + " %",
                        English to kompensasjonsgrad + " %"
                    )
                }
            }
        }
        showIf(argument().map {
            it.inntektsgrenseAr_inntektsAvkortingGjeldende < it.inntektstak_inntektsAvkortingGjeldende
        }
        ) {
            row {
                cell {
                    text(
                        Bokmal to "Inntekt som medfører at uføretrygden ikke blir utbetalt",
                        Nynorsk to "Inntekt som fører til at uføretrygda ikkje blir utbetalt",
                        English to "Income that will lead to no payment of your disability benefit"
                    )
                }
                cell {
                    val inntektstak =
                        argument().map { Kroner(it.inntektstak_inntektsAvkortingGjeldende) }.format()
                    textExpr(
                        Bokmal to inntektstak + " kr",
                        Nynorsk to inntektstak + " kr",
                        English to inntektstak + " NOK"
                    )
                }
            }
        }
        showIf(argument().map {
            it.inntektsgrenseAr_inntektsAvkortingGjeldende > it.inntektstak_inntektsAvkortingGjeldende
        }
        ) {
            row {
                cell {
                    text(
                        Bokmal to "Inntekt som medfører at uføretrygden ikke blir utbetalt",
                        Nynorsk to "Inntekt som fører til at uføretrygda ikkje blir utbetalt",
                        English to "Income that will lead to no payment of your disability benefit"
                    )
                }
                cell {
                    val inntektsgrenseAr =
                        argument().map { Kroner(it.inntektsgrenseAr_inntektsAvkortingGjeldende) }
                            .format()
                    textExpr(
                        Bokmal to inntektsgrenseAr + " kr",
                        Nynorsk to inntektsgrenseAr + " kr",
                        English to inntektsgrenseAr + " NOK"
                    )
                }
            }
        }
        // Mandatory
        row {
            cell {
                text(
                    Bokmal to "Sivilstatus lagt til grunn i beregningen",
                    Nynorsk to "Sivilstatus lagt til grunn i utrekninga",
                    English to "Marital status applied to calculation"
                )
            }
            cell {
                val brukersSivilstand =
                    argument().map { it.brukersSivilstand_gjeldendeBeregnetUTPerManed }
                        .str()
                textExpr(
                    Bokmal to brukersSivilstand,
                    Nynorsk to brukersSivilstand,
                    English to brukersSivilstand
                )
            }
        }
        /* showIf(argument().map {
            it.erUnder20Ar_ungUforGjeldende = true
        }
        ) {
            row {
                cell {
                    text(
                        Bokmal to "Ung ufør",
                        Nynorsk to "Ung ufør",
                        English to "Young disabled"
                    )
                }
                cell {
                    text(
                        Bokmal to "Ja",
                        Nynorsk to "Ja",
                        English to "Yes"
                    )
                }
            }
        }
        */
        showIf(argument().map { it.yrkesskadegrad_yrkesskadeGjeldende > 0 })
        {
            row {
                cell {
                    text(
                        Bokmal to "Yrkesskadegrad",
                        Nynorsk to "Yrkesskadegrad",
                        English to "Degree of disability due to occupational injury"
                    )
                }
                cell {
                    val yrkesskadegrad =
                        argument().map { it.yrkesskadegrad_yrkesskadeGjeldende }.str()
                    textExpr(
                        Bokmal to yrkesskadegrad + " %",
                        Nynorsk to yrkesskadegrad + " %",
                        English to yrkesskadegrad + " %"
                    )
                }
            }
        }
        // Trenger støtte for hvis ikke tom
        // Hvis skadetidspunkt_yrkesskadeGjeldende != tom, include
        //showIf(argument().map { it.skadetidspunkt_yrkesskadeGjeldende})
        row {
            cell {
                text(
                    Bokmal to "Skadetidspunktet for yrkesskaden",
                    Nynorsk to "Skadetidspunktet for yrkesskaden",
                    English to "Date of injury"
                )
            }
            cell {
                val skadetidspunkt =
                    argument().map { it.skadetidspunkt_yrkesskadeGjeldende }.format()
                textExpr(
                    Bokmal to skadetidspunkt,
                    Nynorsk to skadetidspunkt,
                    English to skadetidspunkt
                )
            }
        }
        showIf(argument().map { it.inntektVedSkadetidspunkt_yrkesskadeGjeldende > 0 })
        {
            row {
                cell {
                    text(
                        Bokmal to "Årlig arbeidsinntekt på skadetidspunktet",
                        Nynorsk to "Årleg arbeidsinntekt på skadetidspunktet",
                        English to "Annual income at the date of injury"
                    )
                }
                cell {
                    val inntektVedSkadetidspunkt =
                        argument().map { Kroner(it.inntektVedSkadetidspunkt_yrkesskadeGjeldende) }
                            .format()
                    textExpr(
                        Bokmal to inntektVedSkadetidspunkt + " kr",
                        Nynorsk to inntektVedSkadetidspunkt + " kr",
                        English to inntektVedSkadetidspunkt + " NOK"
                    )
                }
            }
        }
        showIf(argument().map {
            (it.brukerErFlyktning_gjeldendeBeregnetUTPerManed && (it.beregningsMetode_trygdetidsdetaljergjeldende == BeregningsMetode.FOLKETRYGD))
        })
        {
            row {
                cell {
                    text(
                        Bokmal to "Du er innvilget flyktningstatus fra UDI",
                        Nynorsk to "Du er innvilga flyktningstatus frå UDI",
                        English to "You have been granted status as a refugee by the Norwegian Directorate of Immigration (UDI)"
                    )
                }
                cell {
                    text(
                        Bokmal to "Ja",
                        Nynorsk to "Ja",
                        English to "Yes"
                    )
                }
            }
        }
        showIf(argument().map {
            (it.brukerErFlyktning_gjeldendeBeregnetUTPerManed == FALSE && (it.beregningsMetode_trygdetidsdetaljergjeldende == BeregningsMetode.FOLKETRYGD))
        })
        {
            row {
                cell {
                    text(
                        Bokmal to "Trygdetid (maksimalt 40 år)",
                        Nynorsk to "Trygdetid (maksimalt 40 år)",
                        English to "Insurance period (maximum 40 years)"
                    )
                }
                cell {
                    val anvendtTT =
                        argument().map { it.anvendtTT_trygdetidsdetaljerGjeldende }.str()
                    textExpr(
                        Bokmal to anvendtTT + " år",
                        Nynorsk to anvendtTT + " år",
                        English to anvendtTT + " years"
                    )
                }
            }
        }
        showIf(argument().map {
            it.beregningsMetode_trygdetidsdetaljergjeldende == BeregningsMetode.EOS || it.beregningsMetode_trygdetidsdetaljergjeldende == BeregningsMetode.NORDISK
        })
        {
            row {
                cell {
                    text(
                        Bokmal to "Teoretisk trygdetid i Norge og andre EØS-land som er brukt i beregningen (maksimalt 40 år)",
                        Nynorsk to "Teoretisk trygdetid i Noreg og andre EØS-land som er brukt i utrekninga (maksimalt 40 år)",
                        English to "Theoretical insurance period in Norway and other EEA countries used in the calculation (maximum 40 years)"
                    )
                }
                // Implement logic for year/years
                cell {
                    val anvendtTT =
                        argument().map { it.anvendtTT_trygdetidsdetaljerGjeldende }.str()
                    textExpr(
                        Bokmal to anvendtTT + " år",
                        Nynorsk to anvendtTT + " år",
                        English to anvendtTT + " years"
                    )
                }
            }
        }
        showIf(argument().map {
            it.beregningsMetode_trygdetidsdetaljergjeldende != BeregningsMetode.FOLKETRYGD &&
                it.beregningsMetode_trygdetidsdetaljergjeldende != BeregningsMetode.NORDISK &&
                it.beregningsMetode_trygdetidsdetaljergjeldende != BeregningsMetode.EOS
        })
        {
            row {
                cell {
                    text(
                        Bokmal to "Teoretisk trygdetid i Norge og andre avtaleland som er brukt i beregningen (maksimalt 40 år)",
                        Nynorsk to "Teoretisk trygdetid i Noreg og andre avtaleland som er brukt i utrekninga (maksimalt 40 år)",
                        English to "Theoretical insurance period in Norway and other partner countries used in the calculation (maximum 40 years)"
                    )
                }
                cell {
                    val anvendtTT =
                        argument().map { it.anvendtTT_trygdetidsdetaljerGjeldende }.str()
                    textExpr(
                        Bokmal to anvendtTT + " år",
                        Nynorsk to anvendtTT + " år",
                        English to anvendtTT + " years"
                    )
                }
            }
        }
        showIf(argument().map {
            it.beregningsMetode_trygdetidsdetaljergjeldende != BeregningsMetode.FOLKETRYGD
        })
        {
            row {
                cell {
                    text(
                        Bokmal to "Faktisk trygdetid i Norge",
                        Nynorsk to "Faktisk trygdetid i Noreg",
                        English to "Actual insurance period in Norway"
                    )
                }
                cell {
                    val faktiskTTNorge =
                        argument().map { it.faktiskTTNorge_trygdetidsdetaljerGjeldende }
                            .str()
                    textExpr(
                        Bokmal to faktiskTTNorge + " måneder",
                        Nynorsk to faktiskTTNorge + " måneder",
                        English to faktiskTTNorge + " months"
                    )
                }
            }
        }
        showIf(argument().map {
            it.beregningsMetode_trygdetidsdetaljergjeldende == BeregningsMetode.EOS
        })
        {
            row {
                cell {
                    text(
                        Bokmal to "Faktisk trygdetid i andre EØS-land",
                        Nynorsk to "Faktisk trygdetid i andre EØS-land",
                        English to "Actual insurance period(s) in other EEA countries"
                    )
                }
                cell {
                    val faktiskTTEOS =
                        argument().map { it.faktiskTTEOS_trygdetidsdetaljerGjeldende }.str()
                    textExpr(
                        Bokmal to faktiskTTEOS + " måneder",
                        Nynorsk to faktiskTTEOS + " måneder",
                        English to faktiskTTEOS + " months"
                    )
                }
            }
        }
        showIf(argument().map {
            it.beregningsMetode_trygdetidsdetaljergjeldende == BeregningsMetode.EOS
        })
        {
            row {
                cell {
                    text(
                        Bokmal to "Faktisk trygdetid i Norge og EØS-land (maksimalt 40 år)",
                        Nynorsk to "Faktisk trygdetid i Noreg og EØS-land (maksimalt 40 år)",
                        English to "Actual insurance period in Norway and EEA countries (maximum 40 years)"
                    )
                }
                cell {
                    val nevnerTTEOS =
                        argument().map { it.nevnerTTEOS_trygdetidsdetaljerGjeldende }.str()
                    textExpr(
                        Bokmal to nevnerTTEOS + " måneder",
                        Nynorsk to nevnerTTEOS + " måneder",
                        English to nevnerTTEOS + " months"
                    )
                }
            }
        }
        showIf(argument().map {
            it.beregningsMetode_trygdetidsdetaljergjeldende == BeregningsMetode.EOS
        })
        {
            row {
                cell {
                    text(
                        Bokmal to "Forholdstallet brukt i beregning av trygdetid",
                        Nynorsk to "Forholdstalet brukt i utrekning av trygdetid",
                        English to "Ratio applied in calculation of insurance period"
                    )
                }
                cell {
                    val tellerTTEOS =
                        argument().map { it.tellerTTEOS_trygdetidsdetaljerGjeldende }.str()
                    val nevnerTTEOS =
                        argument().map { it.nevnerTTEOS_trygdetidsdetaljerGjeldende }.str()
                    textExpr(
                        Bokmal to tellerTTEOS + " / " + nevnerTTEOS,
                        Nynorsk to tellerTTEOS + " / " + nevnerTTEOS,
                        English to tellerTTEOS + " / " + nevnerTTEOS
                    )
                }
            }
        }
        showIf(argument().map {
            it.beregningsMetode_trygdetidsdetaljergjeldende == BeregningsMetode.NORDISK
        })
        {
            row {
                cell {
                    text(
                        Bokmal to "Faktisk trygdetid i annet nordisk land som brukes i beregning av framtidig trygdetid",
                        Nynorsk to "Faktisk trygdetid i anna nordisk land som blir brukt i utrekning av framtidig trygdetid",
                        English to "Actual insurance period in another Nordic country, applied in calculation of future insurance period(s)"
                    )
                }
                cell {
                    val faktiskTTNordiskKonv =
                        argument().map { it.faktiskTTNordiskKonv_trygdetidsdetaljerGjeldende }
                            .str()
                    textExpr(
                        Bokmal to faktiskTTNordiskKonv + " måneder",
                        Nynorsk to faktiskTTNordiskKonv + " måneder",
                        English to faktiskTTNordiskKonv + " months"
                    )
                }
            }
        }
        showIf(argument().map {
            ((it.beregningsMetode_trygdetidsdetaljergjeldende == BeregningsMetode.FOLKETRYGD || it.beregningsMetode_trygdetidsdetaljergjeldende == BeregningsMetode.NORDISK)
                && it.framtidigTTNorsk_trygdetidsdetaljerGjeldende < 40)
        })
        {
            row {
                cell {
                    text(
                        Bokmal to "Norsk framtidig trygdetid",
                        Nynorsk to "Norsk framtidig trygdetid",
                        English to "Future insurance period in Norway"
                    )
                }
                cell {
                    val framtidigTTNorsk =
                        argument().map { it.framtidigTTNorsk_trygdetidsdetaljerGjeldende }
                            .str()
                    textExpr(
                        Bokmal to framtidigTTNorsk + " måneder",
                        Nynorsk to framtidigTTNorsk + " måneder",
                        English to framtidigTTNorsk + " months"
                    )
                }
            }
        }
        showIf(argument().map {
            it.beregningsMetode_trygdetidsdetaljergjeldende == BeregningsMetode.NORDISK
        })
        {
            row {
                cell {
                    text(
                        Bokmal to "Forholdstallet brukt i reduksjon av norsk framtidig trygdetid",
                        Nynorsk to "Forholdstalet brukt i reduksjon av norsk framtidig trygdetid",
                        English to "Ratio applied in reduction of future Norwegian insurance period"
                    )
                }
                cell {
                    val tellerTTNordiskKonv =
                        argument().map { it.tellerTTNordiskKonv_trygdetidsdetaljerGjeldende }
                            .str()
                    val nevnerTTNordiskKonv_trygdetidsdetaljerGjeldende =
                        argument().map { it.nevnerTTNordiskKonv_trygdetidsdetaljerGjeldende }
                            .str()
                    textExpr(
                        Bokmal to tellerTTNordiskKonv + " / " + nevnerTTNordiskKonv_trygdetidsdetaljerGjeldende,
                        Nynorsk to tellerTTNordiskKonv + " / " + nevnerTTNordiskKonv_trygdetidsdetaljerGjeldende,
                        English to tellerTTNordiskKonv + " / " + nevnerTTNordiskKonv_trygdetidsdetaljerGjeldende
                    )
                }
            }
        }
        showIf(argument().map {
            it.beregningsMetode_trygdetidsdetaljergjeldende == BeregningsMetode.NORDISK
        })
        {
            row {
                cell {
                    text(
                        Bokmal to "Samlet trygdetid brukt i beregning av uføretrygd etter reduksjon av framtidig trygdetid",
                        Nynorsk to "Samla trygdetid brukt i utrekning av uføretrygd etter reduksjon av framtidig trygdetid",
                        English to "Total insurance period applied in calculating disability benefit after reduction of future insurance period(s"
                    )
                }
                cell {
                    val samletTTNordiskKonv =
                        argument().map { it.samletTTNordiskKonv_trygdetidsdetaljerGjeldende }
                            .str()
                    textExpr(
                        Bokmal to samletTTNordiskKonv + " måneder",
                        Nynorsk to samletTTNordiskKonv + " måneder",
                        English to samletTTNordiskKonv + " months"
                    )
                }
            }
        }
        showIf(argument().map {
            it.beregningsMetode_trygdetidsdetaljergjeldende != BeregningsMetode.FOLKETRYGD &&
                it.beregningsMetode_trygdetidsdetaljergjeldende != BeregningsMetode.NORDISK &&
                it.beregningsMetode_trygdetidsdetaljergjeldende != BeregningsMetode.EOS
        })
        {
            row {
                cell {
                    text(
                        Bokmal to "Faktisk trygdetid i annet avtaleland",
                        Nynorsk to "Faktisk trygdetid i anna avtaleland",
                        English to "Actual insurance period(s) in another partner country"
                    )
                }
                cell {
                    val faktiskTTBilateral =
                        argument().map { it.faktiskTTBilateral_trygdetidsdetaljerGjeldende }
                            .str()
                    textExpr(
                        Bokmal to faktiskTTBilateral + " måneder",
                        Nynorsk to faktiskTTBilateral + " måneder",
                        English to faktiskTTBilateral + " months"
                    )
                }
            }
        }
        showIf(argument().map {
            it.beregningsMetode_trygdetidsdetaljergjeldende != BeregningsMetode.FOLKETRYGD &&
                it.beregningsMetode_trygdetidsdetaljergjeldende != BeregningsMetode.NORDISK &&
                it.beregningsMetode_trygdetidsdetaljergjeldende != BeregningsMetode.EOS
        })
        {
            row {
                cell {
                    text(
                        Bokmal to "Faktisk trygdetid i Norge og avtaleland (maksimalt 40 år)",
                        Nynorsk to "Faktisk trygdetid i Noreg og avtaleland (maksimalt 40 år)",
                        English to "Actual insurance period in Norway and partner countries (maximum 40 years)"
                    )
                }
                cell {
                    val nevnerProRata =
                        argument().map { it.nevnerProRata_trygdetidsdetaljerGjeldende }.str()
                    textExpr(
                        Bokmal to nevnerProRata + " måneder",
                        Nynorsk to nevnerProRata + " måneder",
                        English to nevnerProRata + " months"
                    )
                }
            }
        }
        showIf(argument().map {
            it.beregningsMetode_trygdetidsdetaljergjeldende != BeregningsMetode.FOLKETRYGD &&
                it.beregningsMetode_trygdetidsdetaljergjeldende != BeregningsMetode.NORDISK &&
                it.beregningsMetode_trygdetidsdetaljergjeldende != BeregningsMetode.EOS
        })
        {
            row {
                cell {
                    text(
                        Bokmal to "Forholdstallet brukt i beregning av uføretrygd",
                        Nynorsk to "Forholdstalet brukt i utrekning av uføretrygd",
                        English to "Ratio applied in calculation of insurance period"
                    )
                }
                cell {
                    val tellerProRata =
                        argument().map { it.tellerProRata_trygdetidsdetaljerGjeldende }.str()
                    val nevnerProRata =
                        argument().map { it.nevnerProRata_trygdetidsdetaljerGjeldende }.str()
                    textExpr(
                        Bokmal to tellerProRata + " / " + nevnerProRata,
                        Nynorsk to tellerProRata + " / " + nevnerProRata,
                        English to tellerProRata + " / " + nevnerProRata
                    )
                }
            }
        }
        showIf(argument().map {
            it.belop_barnetilleggSBGjeldende > 0
        })
        {
            row {
                cell {
                    text(
                        Bokmal to "Totalt antall barn du har barnetillegg for",
                        Nynorsk to "Totalt tal barn du har barnetillegg for",
                        English to "Total number of children for whom you receive child supplement"
                    )
                }
                cell {
                    val totaltAntallBarn =
                        argument().map { it.totaltAntallBarn_barnetilleggGrunnlagGjeldende }
                            .str()
                    textExpr(
                        Bokmal to totaltAntallBarn,
                        Nynorsk to totaltAntallBarn,
                        English to totaltAntallBarn
                    )
                }
            }
        }
        showIf(argument().map {
            it.erRedusertMotTak_barnetilleggGrunnlagGjeldende
        })
        {
            row {
                cell {
                    val prosentsatsGradertOIFU =
                        argument().map { it.prosentsatsGradertOIFU_barnetilleggGrunnlagGjeldende }
                            .str()
                    textExpr(
                        Bokmal to prosentsatsGradertOIFU + " % av inntekt før uførhet (justert for endringer i grunnbeløpet)",
                        Nynorsk to prosentsatsGradertOIFU + " % av inntekt før uførleik (justert for endringar i grunnbeløpet)",
                        English to prosentsatsGradertOIFU + " % of income before disability, adjusted for changes in the basic amount"
                    )
                }
                cell {
                    val gradertOIFU =
                        argument().map { Kroner(it.gradertOIFU_barnetilleggGrunnlagGjeldende) }
                            .format()
                    textExpr(
                        Bokmal to gradertOIFU + " kr",
                        Nynorsk to gradertOIFU + " kr",
                        English to gradertOIFU + " NOK"
                    )
                }
            }
        }
        showIf(argument().map {
            it.belop_barnetilleggSBGjeldende > 0
        })
        {
            row {
                cell {
                    text(
                        Bokmal to "Fribeløp for særkullsbarn",
                        Nynorsk to "Fribeløp for særkullsbarn",
                        English to "Exemption amount for children from a previous relationship"
                    )
                }
                cell {
                    val fribelop =
                        argument().map { Kroner(it.fribelop_barnetilleggSBGjeldende) }.format()
                    textExpr(
                        Bokmal to fribelop + " kr",
                        Nynorsk to fribelop + " kr",
                        English to fribelop + " NOK"
                    )
                }
            }
        }
        showIf(argument().map {
            it.belop_barnetilleggSBGjeldende > 0
        })
        {
            row {
                cell {
                    text(
                        Bokmal to "Samlet inntekt som er brukt i fastsettelse av barnetillegg",
                        Nynorsk to "Samla inntekt som er brukt i fastsetjinga av barnetillegg",
                        English to "Your income, which is used to calculate child supplement"
                    )
                }
                cell {
                    val inntektBruktIAvkortning =
                        argument().map { Kroner(it.inntektBruktIAvkortning_barnetilleggSBGjeldende) }
                            .format()
                    textExpr(
                        Bokmal to inntektBruktIAvkortning + " kr",
                        Nynorsk to inntektBruktIAvkortning + " kr",
                        English to inntektBruktIAvkortning + " NOK"
                    )
                }
            }
        }
        showIf(argument().map {
            it.belop_barnetilleggSBGjeldende > 0
        })
        {
            row {
                cell {
                    text(
                        Bokmal to "Samlet inntekt for deg som gjør at barnetillegget ikke blir utbetalt",
                        Nynorsk to "Samla inntekt for deg som gjer at barnetillegget ikkje blir utbetalt",
                        English to "Your income which means that no child supplement is received"
                    )
                }
                cell {
                    val inntektstak =
                        argument().map { Kroner(it.inntektstak_barnetilleggSBGjeldende) }.format()
                    textExpr(
                        Bokmal to inntektstak + " kr",
                        Nynorsk to inntektstak + " kr",
                        English to inntektstak + " NOK"
                    )
                }
            }
        }
    }
    //End of table
    // Start of phrases
    showIf(argument().map {
        it.sats_minsteytelseGjeldende > 0
    }) {
        includePhrase(rettTilMYOverskrift_001)
    }
    showIf(argument().map {
        it.sats_minsteytelseGjeldende > 0 && !it.erKonvertert_uforetrygdGjeldende
    }) {
        includePhrase(vedleggBeregnUTInfoMY_001)
    }
    showIf(argument().map {
        it.sats_minsteytelseGjeldende > 0 && it.erKonvertert_uforetrygdGjeldende
    }) {
        includePhrase(vedleggBeregnUTInfoMY2_001)
    }
    showIf(argument().map {
        it.sats_minsteytelseGjeldende > 0 && !it.erUnder20Ar_ungUforGjeldende
    }) {
        includePhrase(vedleggBeregnUTInfoMYUngUfor_001)
    }
    showIf(argument().map {
        it.sats_minsteytelseGjeldende > 0 && it.erUnder20Ar_ungUforGjeldende
    }) {
        includePhrase(vedleggBeregnUTInfoMYUngUforUnder20_001)
    }
    showIf(argument().map {
        it.sats_minsteytelseGjeldende > 0
    }) {
        includePhrase(vedleggBeregnUTDinMY_001, argument().map {
            VedleggBeregnUTDinMY_001Dto(it.sats_minsteytelseGjeldende)
        })
    }
    showIf(argument().map {
        it.erSannsynligEndret_inntektForUforeVedVirk
    }) {
        includePhrase(vedleggBeregnUTMinsteIFU_001)
    }
    showIf(argument().map {
        it.sats_minsteytelseGjeldende > 0 &&
            it.erSannsynligEndret_inntektForUforeVedVirk &&
            (it.inntektsgrenseAr_inntektsAvkortingGjeldende < it.inntektstak_inntektsAvkortingGjeldende)
    }) {
        includePhrase(slikFastsettesKompGradOverskrift_001)
    }
    showIf(argument().map {
        it.sats_minsteytelseGjeldende > 0 &&
            it.erSannsynligEndret_inntektForUforeGjeldende &&
            (it.inntektsgrenseAr_inntektsAvkortingGjeldende < it.inntektstak_inntektsAvkortingGjeldende)
    }) {
        includePhrase(vedleggBeregnUTKompGrad_001)
    }
    showIf(argument().map {
        it.sats_minsteytelseGjeldende > 0 &&
            it.erSannsynligEndret_inntektForUforeGjeldende &&
            (it.inntektsgrenseAr_inntektsAvkortingGjeldende < it.inntektstak_inntektsAvkortingGjeldende) &&
            !it.erKonvertert_uforetrygdGjeldende
    }) {
        includePhrase(vedleggBeregnUTKompGradGjsntt_001)
    }
    showIf(argument().map {
        it.sats_minsteytelseGjeldende > 0 &&
            it.erSannsynligEndret_inntektForUforeGjeldende &&
            (it.inntektsgrenseAr_inntektsAvkortingGjeldende < it.inntektstak_inntektsAvkortingGjeldende) &&
            it.erKonvertert_uforetrygdGjeldende
    }) {
        includePhrase(vedleggBeregnUTKompGradGjsnttKonvUT_001)
    }
    val erRedusertMotInntektOgUtbetalt = argument().map {
        it.erRedusertMotinntekt_barnetilleggSBGjeldende && !it.erIkkeUtbetaltpgaTak_barnetilleggGrunnlagGjeldende
    }
    showIf(erRedusertMotInntektOgUtbetalt) {
        includePhrase(slikBeregnBTOverskrift_001)
    }
    showIf(argument().map {
        !it.erIkkeUtbetaltpgaTak_barnetilleggGrunnlagGjeldende
            && it.erRedusertMotinntekt_barnetilleggSBGjeldende
    })  {
        includePhrase(vedleggBeregnUTInnlednBT_001)
    }
    showIf(erRedusertMotInntektOgUtbetalt) {
        includePhrase(vedleggBeregnUTInfoBTSB_001)
    }
    showIf(argument().map {
        it.erRedusertMotinntekt_barnetilleggSBGjeldende &&
            !it.erIkkeUtbetaltpgaTak_barnetilleggGrunnlagGjeldende &&
            it.anvendtTT_trygdetidsdetaljerGjeldende < 40 &&
            it.yrkesskadegrad_yrkesskadeGjeldende > 0
    }) {
        includePhrase(vedleggBeregnUTredusTTBTSB_001)
    }
    showIf(argument().map {
        it.erRedusertMotinntekt_barnetilleggSBGjeldende &&
            !it.erIkkeUtbetaltpgaTak_barnetilleggGrunnlagGjeldende &&
            !it.fribelopEllerInntektErPeriodisert_barnetilleggSBGjeldende &&
            it.justeringsbelopAr_barnetilleggSBGjeldende > 0
    }) {
        includePhrase(vedleggBeregnUTIkkePeriodisertFriBOgInntektBTSB_001, argument().map {
            VedleggBeregnUTIkkePeriodisertFriBOgInntektBTSB_001Dto(it.avkortningsbelopAr_barnetilleggSBGjeldende)
        })
    }
    showIf(argument().map {
        it.erRedusertMotinntekt_barnetilleggSBGjeldende &&
            !it.erIkkeUtbetaltpgaTak_barnetilleggGrunnlagGjeldende &&
            !it.fribelopEllerInntektErPeriodisert_barnetilleggSBGjeldende &&
            it.justeringsbelopAr_barnetilleggSBGjeldende > 0
    }) {
        includePhrase(vedleggBeregnUTIkkePeriodisertFriBOgInntektBTSBJusterBelop_001, argument().map {
            VedleggBeregnUTkkePeriodisertFriBOgInntektBTSBJusterBelop_001Dto(it.avkortningsbelopAr_barnetilleggSBGjeldende)
        })
    }
    showIf(argument().map {
        it.erRedusertMotinntekt_barnetilleggSBGjeldende &&
            !it.erIkkeUtbetaltpgaTak_barnetilleggGrunnlagGjeldende &&
            it.fribelopEllerInntektErPeriodisert_barnetilleggSBGjeldende &&
            it.justeringsbelopAr_barnetilleggSBGjeldende > 0
    }) {
        includePhrase(vedleggBeregnUTPeridisertFriBOgInntektBTSB_001, argument().map {
            VedleggBeregnUTPeriodisertFriBOgInntektBTSB_001Dto(it.avkortningsbelopAr_barnetilleggSBGjeldende)
        })
    }
    showIf(argument().map {
        it.erRedusertMotinntekt_barnetilleggSBGjeldende &&
            !it.erIkkeUtbetaltpgaTak_barnetilleggGrunnlagGjeldende &&
            it.fribelopEllerInntektErPeriodisert_barnetilleggSBGjeldende &&
            it.justeringsbelopAr_barnetilleggSBGjeldende == 0
    }) {
        includePhrase(vedleggBeregnUTPeriodisertFriBOgInntektBTSBJusterBelop_001, argument().map {
            VedleggBeregnUTPeriodisertFriBOgInntektBTSBJusterBelop_001Dto(it.avkortningsbelopAr_barnetilleggSBGjeldende)
        })
    }
    showIf(argument().map {
        it.erRedusertMotinntekt_barnetilleggSBGjeldende &&
            !it.erIkkeUtbetaltpgaTak_barnetilleggGrunnlagGjeldende &&
            it.justeringsbelopAr_barnetilleggSBGjeldende > 0
    }) {
        includePhrase(vedleggBeregnUTJusterBelopOver0BTSB_001, argument().map {
            VedleggBeregnUTJusterBelopOver0BTSB_001Dto(it.justeringsbelopAr_barnetilleggSBGjeldende)
        })
    }
    showIf(argument().map {
        it.erRedusertMotinntekt_barnetilleggSBGjeldende &&
            !it.erIkkeUtbetaltpgaTak_barnetilleggGrunnlagGjeldende &&
            it.justeringsbelopAr_barnetilleggSBGjeldende < 0
        // < 0? Is there a minus operator from Pesys?
    }) {
        includePhrase(vedleggBeregnUTJusterBelopUnder0BTSB_001, argument().map {
            VedleggBeregnUTJusterBelopUnder0BTSB_001Dto(it.justeringsbelopAr_barnetilleggSBGjeldende)
        })
    }
    // TABLE 2 - start
    showIf(erRedusertMotInntektOgUtbetalt) {
        title1 {
            text(
                Bokmal to "Reduksjon av barnetillegg for særkullsbarn før skatt",
                Nynorsk to "Reduksjon av barnetillegg for særkullsbarn før skatt",
                English to "Reduction of child supplement payment for children from a previous relationship before tax"
            )
        }
        table(
            header = {
                column(2) {
                    text(
                        Bokmal to "Beskrivelse",
                        Nynorsk to "Beskrivelse",
                        English to "Description"
                    )
                }
                column(alignment = RIGHT) {
                    text(
                        Bokmal to "Beløp",
                        Nynorsk to "Beløp",
                        English to "Amount"
                    )
                }
            }
        ) {
            showIf(argument().map { it.belop_barnetilleggSBGjeldende > 0 && it.justeringsbelopAr_barnetilleggSBGjeldende != 0 }) {
                row {
                    cell {
                        text(
                            Bokmal to "Årlig barnetillegg før reduksjon ut fra inntektbelopArForAvkort",
                            Nynorsk to "Årleg barnetillegg før reduksjon ut frå inntekt",
                            English to "Yearly child supplement before income reduction"
                        )
                    }
                    cell {
                        val belopArForAvkort = argument().map { Kroner(it.belopArForAvkort_barnetilleggSBGjeldende) }.format()
                        textExpr(
                            Bokmal to belopArForAvkort + " kr",
                            Nynorsk to belopArForAvkort + " kr",
                            English to belopArForAvkort + " NOK"
                        )
                    }
                }
            }
            showIf(erRedusertMotInntektOgUtbetalt) {
                row {
                    cell {
                        val inntektBruktIAvkortning =
                            argument().map { Kroner(it.inntektBruktIAvkortning_barnetilleggSBGjeldende) }
                                .format()
                        textExpr(
                            Bokmal to "Samlet inntekt brukt i fastsettelse av barnetillegget er\n".expr() + inntektBruktIAvkortning.str() + " kr",
                            Nynorsk to "Samla inntekt brukt i fastsetjinga av barnetillegget er\n".expr() + inntektBruktIAvkortning.str() + " kr",
                            English to "Total income applied in calculation of reduction in child supplement is\n".expr() + inntektBruktIAvkortning.str() + " NOK"
                        )
                    }
                    cell {
                        text(
                            Bokmal to "",
                            Nynorsk to "",
                            English to ""
                        )
                    }
                }
            }
            showIf(argument().map { (it.belop_barnetilleggSBGjeldende > 0 || (it.belop_barnetilleggSBGjeldende < 0 && it.justeringsbelopAr_barnetilleggSBGjeldende != 0)) }) {
                row {
                    cell {
                        val fribelop = argument().map { Kroner(it.fribelop_barnetilleggSBGjeldende) }.format()
                        textExpr(
                            Bokmal to "Fribeløp brukt i fastsettelsen av barnetillegget er\n".expr() + fribelop.str() + " kr",
                            Nynorsk to "Fribeløp brukt i fastsetjinga av barnetillegget er\n".expr() + fribelop.str() + " kr",
                            English to "Exemption amount applied in calculation of reduction in child supplement is\n".expr() + fribelop.str() + " NOK"
                        )
                    }
                    cell {
                        text(
                            Bokmal to "",
                            Nynorsk to "",
                            English to ""
                        )
                    }
                }
            }
            showIf(argument().map { (it.belop_barnetilleggSBGjeldende != 0 || (it.belop_barnetilleggSBGjeldende == 0 && it.justeringsbelopAr_barnetilleggSBGjeldende != 0)) }) {
                row {
                    cell {
                        val inntektOverFribelop = argument().map { Kroner(it.inntektOverFribelop_barnetilleggSBGjeldende) }.format()
                        textExpr(
                            Bokmal to "Inntekt over fribeløpet er\n".expr() + inntektOverFribelop.str() + " kr",
                            Nynorsk to "Inntekt over fribeløpet er\n".expr() + inntektOverFribelop.str() + " kr",
                            English to "Income exceeding the exemption amount is\n".expr() + inntektOverFribelop.str() + " NOK"
                        )
                    }
                    cell {
                        text(
                            Bokmal to "",
                            Nynorsk to "",
                            English to ""
                        )
                    }
                }
            }
            showIf(argument().map {
                !it.fribelopEllerInntektErPeriodisert_barnetilleggSBGjeldende
                    && (it.belop_barnetilleggSBGjeldende != 0 || (it.belop_barnetilleggSBGjeldende == 0 && it.belopAr_barnetilleggSBGjeldende != 0))
                    && it.avkortningsbelopAr_barnetilleggSBGjeldende > 0
            }) {
                row {
                    cell {
                        text(
                            Bokmal to "- 50 prosent av inntekt som overstiger fribeløpet",
                            Nynorsk to "- 50 prosent av inntekt som overstig fribeløpet",
                            English to "- 50 percent of income exceeding the allowance amount"
                        )
                    }
                    cell {
                        val avkortningsbelopAr = argument().map { Kroner(it.avkortningsbelopAr_barnetilleggSBGjeldende) }.format()
                        textExpr(
                            Bokmal to avkortningsbelopAr + " kr",
                            Nynorsk to avkortningsbelopAr + " kr",
                            English to avkortningsbelopAr + " NOK"
                        )
                    }
                }
            }
            showIf(argument().map {
                it.fribelopEllerInntektErPeriodisert_barnetilleggSBGjeldende
                    && (it.belop_barnetilleggSBGjeldende != 0 || (it.belop_barnetilleggSBGjeldende == 0 && it.belopAr_barnetilleggSBGjeldende != 0))
                    && it.avkortningsbelopAr_barnetilleggSBGjeldende > 0
            }) {
                row {
                    cell {
                        text(
                            Bokmal to "- 50 prosent av inntekt som overstiger fribeløpet (oppgitt som et årlig beløp)",
                            Nynorsk to "- 50 prosent av inntekt som overstig fribeløpet (oppgitt som eit årleg beløp)",
                            English to "- 50 percent of income exceeding the allowance amount (calculated to an annual amount)"
                        )
                    }
                    cell {
                        val avkortningsbelopAr = argument().map { Kroner(it.avkortningsbelopAr_barnetilleggSBGjeldende) }
                            .format()
                        textExpr(
                            Bokmal to avkortningsbelopAr + " kr",
                            Nynorsk to avkortningsbelopAr + " kr",
                            English to avkortningsbelopAr + " NOK"
                        )
                    }
                }
            }
            showIf(argument().map { it.justeringsbelopAr_barnetilleggSBGjeldende != 0 }) {
                row {
                    cell {
                        text(
                            Bokmal to "+ Beløp som er brukt for å justere reduksjonen av barnetillegget",
                            Nynorsk to "+ Beløp som er brukt for å justera reduksjonen av barnetillegget",
                            English to "+ Amount which is used to adjust the reduction of child supplement"
                        )
                    }
                    cell {
                        val justeringsbelopAr = argument().map { Kroner(it.justeringsbelopAr_barnetilleggSBGjeldende) }
                            .format()
                        textExpr(
                            Bokmal to justeringsbelopAr + " kr",
                            Nynorsk to justeringsbelopAr + " kr",
                            English to justeringsbelopAr + " NOK"
                        )
                    }
                }
            }
            showIf(argument().map { it.belop_barnetilleggSBGjeldende != 0 || (it.belop_barnetilleggSBGjeldende == 0 && it.belopAr_barnetilleggSBGjeldende != 0) }) {
                row {
                    cell {
                        text(
                            Bokmal to "= Årlig barnetillegg etter reduksjon ut fra inntekt",
                            Nynorsk to "Årleg barnetillegg etter reduksjon ut frå inntekt",
                            English to "Yearly child supplement after income reduction"
                        )
                    }
                    cell {
                        val belopAr =
                            argument().map { Kroner(it.belopAr_barnetilleggSBGjeldende) }.format()
                        textExpr(
                            Bokmal to belopAr + " kr",
                            Nynorsk to belopAr + " kr",
                            English to belopAr + " NOK"
                        )
                    }
                }
            }
            showIf(argument().map { (it.belop_barnetilleggSBGjeldende != 0 || (it.belop_barnetilleggSBGjeldende == 0 && it.justeringsbelopAr_barnetilleggSBGjeldende != 0)) }) {
                row {
                    cell {
                        text(
                            Bokmal to "Utbetaling av barnetillegg per måned",
                            Nynorsk to "Utbetaling av barnetillegg per månad",
                            English to "Child supplement payment for the remaining months of the year"
                        )
                    }
                    cell {
                        val belop = argument().map { Kroner(it.belop_barnetilleggSBGjeldende) }.format()
                        textExpr(
                            Bokmal to belop + " kr",
                            Nynorsk to belop + " kr",
                            English to belop + " NOK"
                        )
                    }
                }
            }
            showIf(argument().map { it.belop_barnetilleggSBGjeldende == 0 && it.belopAr_barnetilleggSBGjeldende == 0 }) {
                row {
                    cell {
                        text(
                            Bokmal to "Grensen for å få utbetalt barnetillegg",
                            Nynorsk to "Grensa for å få utbetalt barnetillegg",
                            English to "The income limit for receiving child supplement"
                        )
                    }
                    cell {
                        val inntektstak = argument().map { Kroner(it.inntektstak_barnetilleggSBGjeldende) }.format()
                        textExpr(
                            Bokmal to inntektstak + " kr",
                            Nynorsk to inntektstak + " kr",
                            English to inntektstak + " NOK"
                        )
                    }
                }
            }
        }
    }
    // TABLE 2 - end
    showIf(argument().map { it.belop_barnetilleggSBGjeldende > 0 }) {
        includePhrase(vedleggBeregnUTredusBTSBPgaInntekt_001, argument().map {
            VedleggBeregnUTredusBTSBPgaInntekt_001Dto(it.belop_barnetilleggSBGjeldende)
        })
    }
    showIf(argument().map { it.belop_barnetilleggSBGjeldende == 0 && it.justeringsbelopAr_barnetilleggSBGjeldende == 0 }) {
        includePhrase(vedleggBeregnUTIkkeUtbetaltBTSBPgaInntekt_001)
    }
    showIf(argument().map { it.belop_barnetilleggSBGjeldende == 0 && it.justeringsbelopAr_barnetilleggSBGjeldende != 0 }) {
        includePhrase(vedleggBeregnUTJusterBelopIkkeUtbetalt_001)
    }
}




