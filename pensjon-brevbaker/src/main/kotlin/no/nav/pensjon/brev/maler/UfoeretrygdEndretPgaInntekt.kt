package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.Year
import no.nav.pensjon.brev.api.model.YearSelectors.value
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.maler.BarnetilleggFellesbarnSelectors.fribeloepErPeriodisert
import no.nav.pensjon.brev.maler.BarnetilleggFellesbarnSelectors.gammeltBeloep
import no.nav.pensjon.brev.maler.BarnetilleggFellesbarnSelectors.gjelderFlereBarn
import no.nav.pensjon.brev.maler.BarnetilleggFellesbarnSelectors.harBarnetillegg
import no.nav.pensjon.brev.maler.BarnetilleggFellesbarnSelectors.inntektBruktIAvkortning
import no.nav.pensjon.brev.maler.BarnetilleggFellesbarnSelectors.inntektErPeriodisert
import no.nav.pensjon.brev.maler.BarnetilleggFellesbarnSelectors.nyttBeloep
import no.nav.pensjon.brev.maler.BarnetilleggSaerkullsbarnSelectors.fribeloepErPeriodisert
import no.nav.pensjon.brev.maler.BarnetilleggSaerkullsbarnSelectors.gammeltBeloep
import no.nav.pensjon.brev.maler.BarnetilleggSaerkullsbarnSelectors.gjelderFlereBarn
import no.nav.pensjon.brev.maler.BarnetilleggSaerkullsbarnSelectors.harBarnetillegg
import no.nav.pensjon.brev.maler.BarnetilleggSaerkullsbarnSelectors.inntektBruktIAvkortning
import no.nav.pensjon.brev.maler.BarnetilleggSaerkullsbarnSelectors.inntektErPeriodisert
import no.nav.pensjon.brev.maler.BarnetilleggSaerkullsbarnSelectors.nyttBeloep
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDtoSelectors.aarFoerVirkningsAar
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDtoSelectors.barnetilleggFellesbarn
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDtoSelectors.barnetilleggSaerkullsbarn
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDtoSelectors.fyller67IVirkningsAar
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDtoSelectors.harEktefelletillegg
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDtoSelectors.harGjenlevendetillegg
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDtoSelectors.opplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDtoSelectors.sivilstand
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDtoSelectors.ufoeretrygd
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDtoSelectors.virkningsdatoFraOgMed
import no.nav.pensjon.brev.maler.UfoeretrygdSelectors.avkortningsbeloepPerAar
import no.nav.pensjon.brev.maler.UfoeretrygdSelectors.beloepErRedusert
import no.nav.pensjon.brev.maler.UfoeretrygdSelectors.beloepNetto
import no.nav.pensjon.brev.maler.UfoeretrygdSelectors.forventetInntekt
import no.nav.pensjon.brev.maler.UfoeretrygdSelectors.gammeltBeloep
import no.nav.pensjon.brev.maler.UfoeretrygdSelectors.inntektsgrense
import no.nav.pensjon.brev.maler.UfoeretrygdSelectors.inntektstak
import no.nav.pensjon.brev.maler.UfoeretrygdSelectors.nyttBeloep
import no.nav.pensjon.brev.maler.UfoeretrygdSelectors.ufoeregrad
import no.nav.pensjon.brev.maler.UfoeretrygdSelectors.utbetalingsgrad
import no.nav.pensjon.brev.maler.fraser.UfoeretrygdEndretPgaInntekt
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.maler.fraser.ufoer.Ufoeretrygd
import no.nav.pensjon.brev.maler.vedlegg.createVedleggOpplysningerBruktIBeregningUT
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.VedtaksbrevTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import java.time.LocalDate
import java.time.Month

data class UfoeretrygdEndretPgaInntektDto(
    val barnetilleggFellesbarn: BarnetilleggFellesbarn,
    val barnetilleggSaerkullsbarn: BarnetilleggSaerkullsbarn,
    val sivilstand: Sivilstand,
    val ufoeretrygd: Ufoeretrygd,
    val virkningsdatoFraOgMed: LocalDate,
    val aarFoerVirkningsAar: Year,
    val fyller67IVirkningsAar: Boolean,
    val harEktefelletillegg: Boolean,
    val harGjenlevendetillegg: Boolean,
    val opplysningerBruktIBeregningUTDto: OpplysningerBruktIBeregningUTDto,


    //Under konvertering:
    //val FF01_NAV: PlaceholderForType,
    //val FF01_webadresse: PlaceholderForType,
    //val FF01_webadresse_Skatteetaten_no: PlaceholderForType, //TODO
    //val FF01_webadresse_nav: PlaceholderForType, // TODO bruk felles
    //val FF01_webadresse_navKlage: PlaceholderForType,//TODO phrase
    //val FF01_webadresse_uforetrygd: String,
    //val PE_BarnetilleggFelles_JusteringsbelopPerArUtenMinus: Kroner,
    //val PE_BarnetilleggSerkull_JusteringsbelopPerArUtenMinus: Kroner,
    //val PE_Kontaktinformasjon_NavnAvsenderEnhet: PlaceholderForType, //TODO phrase eller felles
    //val PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT: PlaceholderForType,//TODO phrase
    //val PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_NN_entall: PlaceholderForType,//TODO phrase
    //val PE_Skatteetaten: PlaceholderForType,
    //val PE_UT_NettoAkk_pluss_NettoRestAr: Kroner,
    //val PE_UT_OrienteringOmRettighetOgPlikt_eller_RettigheterOgMulighetForKlage: PlaceholderForType,//TODO phrase
    //val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_AvkortingsbelopPerAr: Kroner,
    //val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_Inntektstak: Kroner,
    //val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBinntektAnnenForelder: Kroner,
    //val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_AvkortingsbelopPerAr: Kroner,
    //val PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_Inntektstak: Kroner,
    //val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBBrukersInntektTilAvkortning: Kroner,
    //val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense: Kroner,
    //val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Netto: Kroner,
    //val PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_NettoAkk: Kroner,
    //val PE_Vedtaksdata_BeregningsData_BeregningUfore_TotalNetto: Kroner,
    //val PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Grunnbelop: Kroner,
    //val PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad: Double,
    //val inntektBruktIAvkortningAvBarnetilleggForFellesbarn: Kroner,
    //val PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBfribelop: Kroner,
    //val PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto: Kroner,
    //val inntektBruktIAvkortningAvBarnetilleggForSaerkullsbarn: Kroner,
    //val PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBfribelop: Kroner,
    //val PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto: Kroner,
) {
    //val ufoeretrygdAvkortningsbeloepPerAar: Kroner,
    //val forventetInntektUfoeretrygd: Kroner,
    data class Ufoeretrygd(
        val nyttBeloep: Kroner,
        val gammeltBeloep: Kroner,
        val beloepNetto: Kroner,
        val ufoeregrad: Double,
        val utbetalingsgrad: Double,
        val inntektsgrense: Kroner,
        val forventetInntekt: Kroner,
        val inntektstak: Kroner,
        val beloepErRedusert: Boolean,
        val avkortningsbeloepPerAar: Kroner,
        )

    data class BarnetilleggSaerkullsbarn(
        val gammeltBeloep: Kroner,
        val harBarnetillegg: Boolean,
        val inntektErPeriodisert: Boolean,
        val fribeloepErPeriodisert: Boolean,
        val nyttBeloep: Kroner,
        val inntektBruktIAvkortning: Kroner,
        val gjelderFlereBarn: Boolean,
    )

    data class BarnetilleggFellesbarn(
        val gammeltBeloep: Kroner,
        val harBarnetillegg: Boolean,
        val inntektErPeriodisert: Boolean,
        val fribeloepErPeriodisert: Boolean,
        val nyttBeloep: Kroner,
        val inntektBruktIAvkortning: Kroner,
        val gjelderFlereBarn: Boolean,
        )
}
@TemplateModelHelpers
object UfoeretrygdEndretPgaInntekt : VedtaksbrevTemplate<UfoeretrygdEndretPgaInntektDto> {
    override val kode = Brevkode.Vedtak.UFOER_ENDRET_PGA_INNTEKT

    override val template = createTemplate(
        kode.name,
        letterDataType = UfoeretrygdEndretPgaInntektDto::class,
        languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            "Vedtak – endring av uføretrygd på grunn av inntekt (automatisk)",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK
        )
    ) {

        val harBarnetilleggForSaerkullOgFellesbarn =
            barnetilleggFellesbarn.harBarnetillegg and barnetilleggSaerkullsbarn.harBarnetillegg
        val harEndretUfoeretrygd = ufoeretrygd.gammeltBeloep.notEqualTo(ufoeretrygd.nyttBeloep)
        val harEndretBarnetilleggFellesbarn =
            barnetilleggFellesbarn.nyttBeloep.notEqualTo(barnetilleggFellesbarn.gammeltBeloep)
        val harEndretBarnetilleggSaerkullsbarn =
            barnetilleggSaerkullsbarn.nyttBeloep.notEqualTo(barnetilleggSaerkullsbarn.gammeltBeloep)
        val harEndretBarnetillegg = harEndretBarnetilleggFellesbarn or harEndretBarnetilleggSaerkullsbarn

        val virkningsdatoErFoersteJanuar =
            virkningsdatoFraOgMed.day.equalTo(1) and virkningsdatoFraOgMed.month.equalTo(Month.JANUARY)
        val virkningsaar = virkningsdatoFraOgMed.year

        title {
            includePhrase(
                UfoeretrygdEndretPgaInntekt.Tittel(
                    harEndretUfoeretrygd = harEndretUfoeretrygd,
                    harFlereBarnetillegg = harBarnetilleggForSaerkullOgFellesbarn,
                    harEndretBarnetillegg = harEndretBarnetillegg,
                )
            )
        }
        outline {
            showIf(virkningsdatoErFoersteJanuar) {
                includePhrase(
                    UfoeretrygdEndretPgaInntekt.InnledningReduksjonUfoeretrygd(
                        forventetInntektUfoeretrygd = ufoeretrygd.forventetInntekt,
                        harEndretUfoeretrygd = harEndretUfoeretrygd,
                        virkningsaar = virkningsaar,
                        aarFoerVirkningsAar = aarFoerVirkningsAar,
                        ufoeregrad = ufoeretrygd.ufoeregrad,
                        utbetalingsgrad = ufoeretrygd.utbetalingsgrad,
                        fyller67IVirkningsAar = fyller67IVirkningsAar,
                    )
                )

                includePhrase(
                    UfoeretrygdEndretPgaInntekt.InnledningReduksjonBarnetillegg(
                        harEndretUfoeretrygd = harEndretUfoeretrygd,
                        harEndretBarnetillegg = harEndretBarnetillegg,
                        harBarnetilleggSaerkullsbarn = barnetilleggSaerkullsbarn.harBarnetillegg,
                        harBarnetilleggFellesbarn = barnetilleggFellesbarn.harBarnetillegg,
                        harFlereFellesbarn = barnetilleggFellesbarn.gjelderFlereBarn,
                        harFlereSaerkullsbarn = barnetilleggSaerkullsbarn.gjelderFlereBarn,
                        harEndretBarnetilleggFellesbarn = harEndretBarnetilleggFellesbarn,
                        harEndretBarnetilleggSaerkullsbarn = harEndretBarnetilleggSaerkullsbarn,
                        inntektBruktIAvkortningAvBarnetilleggForSaerkullsbarn = barnetilleggSaerkullsbarn.inntektBruktIAvkortning,
                        inntektBruktIAvkortningAvBarnetilleggForFellesbarn = barnetilleggFellesbarn.inntektBruktIAvkortning,
                        virkningsaar = virkningsaar,
                    )
                )

                showIf(ufoeretrygd.ufoeregrad.notEqualTo(ufoeretrygd.utbetalingsgrad) and harEndretBarnetillegg) {
                    includePhrase(
                        UfoeretrygdEndretPgaInntekt.InnledningInntektsjusteringUfoeretrygd(
                            aarFoerVirkningsAar = aarFoerVirkningsAar.value,
                            virkningsaar = virkningsaar,
                        )
                    )
                }

                showIf(
                    (barnetilleggFellesbarn.gammeltBeloep.greaterThan(barnetilleggFellesbarn.nyttBeloep)
                            or barnetilleggSaerkullsbarn.gammeltBeloep.greaterThan(barnetilleggSaerkullsbarn.nyttBeloep))
                            and (barnetilleggFellesbarn.nyttBeloep.greaterThan(0)
                            or barnetilleggSaerkullsbarn.nyttBeloep.greaterThan(0))
                            and (
                            barnetilleggFellesbarn.inntektErPeriodisert
                                    or barnetilleggFellesbarn.fribeloepErPeriodisert
                                    or barnetilleggSaerkullsbarn.inntektErPeriodisert
                                    or barnetilleggSaerkullsbarn.fribeloepErPeriodisert
                            )
                ) {
                    includePhrase(
                        UfoeretrygdEndretPgaInntekt.BarnetilleggInntektEllerFribeloepPeriodisert(
                            barnetilleggFellesbarn_fribeloepErPeriodisert = barnetilleggFellesbarn.fribeloepErPeriodisert,
                            barnetilleggFellesbarn_harBarnetillegg = barnetilleggFellesbarn.harBarnetillegg,
                            barnetilleggFellesbarn_inntektErPeriodisert = barnetilleggFellesbarn.inntektErPeriodisert,
                            barnetilleggSaerkullsbarn_fribeloepErPeriodisert = barnetilleggSaerkullsbarn.fribeloepErPeriodisert,
                            barnetilleggSaerkullsbarn_harBarnetillegg = barnetilleggSaerkullsbarn.harBarnetillegg,
                            barnetilleggSaerkullsbarn_inntektErPeriodisert = barnetilleggSaerkullsbarn.inntektErPeriodisert,
                            harFlereFellesbarn = barnetilleggFellesbarn.gjelderFlereBarn,
                            harFlereSaerkullsbarn = barnetilleggSaerkullsbarn.gjelderFlereBarn,
                        )
                    )
                }


                includePhrase(
                    UfoeretrygdEndretPgaInntekt.MeldFraOmInntektsEndringerUfoer(
                        barnetilleggFellesbarn_harBarnetillegg = barnetilleggFellesbarn.harBarnetillegg,
                        virkningsaar = virkningsaar,
                        brukersSivilstand = sivilstand,
                    )
                )
            }.orShow {
                includePhrase(
                    UfoeretrygdEndretPgaInntekt.InnledningInntektsendring(
                        harFlereFellesbarn = barnetilleggFellesbarn.gjelderFlereBarn,
                        harEndretBarnetillegg = harEndretBarnetillegg,
                        harEndretUfoeretrygd = harEndretUfoeretrygd,
                        harBarnetilleggForSaerkullOgFellesbarn = harBarnetilleggForSaerkullOgFellesbarn,
                        harBarnetilleggFellesbarn = barnetilleggFellesbarn.harBarnetillegg,
                        brukersSivilstand = sivilstand,
                        virkningsdatoFraOgMed = virkningsdatoFraOgMed,
                    )
                )
            }
            includePhrase(Ufoeretrygd.Beloep(
                perMaaned = ufoeretrygd.beloepNetto,
                ektefelle = harEktefelletillegg,
                ufoeretrygd = true.expr(),
                gjenlevende = harGjenlevendetillegg,
                saerkullsbarn = barnetilleggSaerkullsbarn.notNull(),
                fellesbarn = barnetilleggFellesbarn.notNull(),
            ))
            includePhrase(Ufoeretrygd.UtbetalingsdatoUfoeretrygd)
            includePhrase(Ufoeretrygd.ViktigAALeseHeleBrevet)


            includePhrase(Vedtak.BegrunnelseOverskrift)

            includePhrase(
                UfoeretrygdEndretPgaInntekt.EndringUfoeretrygdBegrunnelseInntektstak(
                    inntektsgrenseUfoeretrygd = ufoeretrygd.inntektsgrense,
                    inntektstakUfoeretrygd = ufoeretrygd.inntektstak,
                    nyttUfoeretrygdBeloep = ufoeretrygd.nyttBeloep,
                    forventetInntektUfoeretrygd = ufoeretrygd.forventetInntekt,
                    gammeltUfoeretrygdBeloep = ufoeretrygd.gammeltBeloep,
                    utbetalingsgrad = ufoeretrygd.utbetalingsgrad,
                    ufoeregrad = ufoeretrygd.ufoeregrad,
                )
            )

            includePhrase(
                UfoeretrygdEndretPgaInntekt.InntektVedSidenAvUfoeretrygd(
                    inntektsgrenseUfoeretrygd = ufoeretrygd.inntektsgrense,
                    ufoeretrygdBeloepErRedusert = ufoeretrygd.beloepErRedusert,
                    forventetInntektUfoeretrygd = ufoeretrygd.forventetInntekt,
                )
            )

            includePhrase(
                UfoeretrygdEndretPgaInntekt.UfoeretrygdTrekkPgaInntekt(
                    forventetInntektUfoeretrygd = ufoeretrygd.forventetInntekt,
                    inntektsgrenseUfoeretrygd = ufoeretrygd.inntektsgrense,
                    nyttUfoeretrygdBeloep = ufoeretrygd.nyttBeloep,
                    ufoeretrygdAvkortningsbeloepPerAar = ufoeretrygd.avkortningsbeloepPerAar,
                    virkningsdatoErFoersteJanuar = virkningsdatoErFoersteJanuar,
                )
            )

            includePhrase(
                UfoeretrygdEndretPgaInntekt.IkkeUtbetaltUfoeretrygdPaaGrunnAvInntekt(
                    forventetInntektUfoeretrygd = ufoeretrygd.forventetInntekt,
                    inntektstakUfoeretrygd = ufoeretrygd.inntektstak,
                    nyttUfoeretrygdBeloep = ufoeretrygd.nyttBeloep,
                    inntektsgrenseUfoeretrygd = ufoeretrygd.inntektsgrense,
                )
            )

            //textExpr(
            //    Bokmal to "Det utbetales ikke uføretrygd når inntekten din utgjør mer enn inntektsgrensen, det vil si ".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense + " kroner. Inntekten vi har brukt er ".expr() + forventetInntektUfoeretrygd + " kroner og du vil derfor ikke få utbetalt uføretrygd resten av året.",
            //    Nynorsk to "Det blir ikkje utbetalt uføretrygd når inntekta di utgjer meir enn inntektsgrensa di, det vil seie ".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense + " kroner. Inntekta vi har brukt er ".expr() + forventetInntektUfoeretrygd + " kroner og du vil derfor ikkje få utbetalt uføretrygd resten av året.",
            //)
            //textExpr(
            //    Bokmal to "Inntekten din har også betydning for hva du får utbetalt i barnetillegg. FordiFor ".expr() + ifElse(harFlereFellesbarn, "barna", "barnet") + " som bor med begge sine foreldre, bruker vi i tillegg din ".expr() + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT + "s inntekt når vi fastsetter størrelsen på barnetillegget ditt. For ".expr() + ifElse(harFlereSaerkullsbarn, "barna", "barnet") + " som ikke bor sammen med begge foreldrene bruker vi kun inntekten din. Uføretrygden din og gjenlevendetillegget ditt regnes med som inntekt.",
            //    Nynorsk to "Inntekta di har også betydning for kva du får utbetalt i barnetillegg. FordiFor ".expr() + ifElse(harFlereFellesbarn, "barna", "barnet") + " som bur saman med begge foreldra sine, bruker vi i tillegg ".expr() + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_NN_entall + " din si inntekt når vi fastset storleiken på barnetillegget ditt. For ".expr() + ifElse(harFlereSaerkullsbarn, "barna", "barnet") + " som ikkje bur saman med begge foreldra bruker vi berre inntekta di. Uføretrygda di og attlevandetillegget ditt er rekna med som inntekt.",
            //)
            //textExpr(
            //    Bokmal to "Vi har beregnet barnetillegget på nytt ut fra inntekten din på ".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBBrukersInntektTilAvkortning + " kroner og inntekten til din ".expr() + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT + " på ".expr() + PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBinntektAnnenForelder + " kroner. Folketrygdens grunnbeløp på inntil ".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Grunnbelop + " kroner er holdt utenfor din ".expr() + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT + "s inntekt. Til sammen utgjør disse inntektene ".expr() + inntektBruktIAvkortningAvBarnetilleggForFellesbarn + " kroner. Dette beløpet er høyerelavere enn fribeløpsgrensen på ".expr() + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBfribelop + " kroner. Derfor er barnetillegget for ".expr() + ifElse(harFlereFellesbarn, "barna", "barnet") + " som bor med begge sine foreldre ikke lenger redusert. Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget for ".expr() + ifElse(harFlereFellesbarn, "barna", "barnet") + " som bor med begge sine foreldre. Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året.",
            //    Nynorsk to "Vi har berekna barnetillegget på nytt ut frå inntekta di på ".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBBrukersInntektTilAvkortning + " kroner og inntekta til ".expr() + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_NN_entall + " din på ".expr() + PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_BTFBinntektAnnenForelder + " kroner. Grunnbeløpet i folketrygda på inntil ".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Grunnbelop + " kroner er held utanfor inntekta til ".expr() + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_NN_entall + " din. Til saman utgjer desse inntektene ".expr() + inntektBruktIAvkortningAvBarnetilleggForFellesbarn + " kroner. Dette beløpet er høgarelågare enn fribeløpet på ".expr() + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBfribelop + " kroner. Derfor er barnetillegget for ".expr() + ifElse(harFlereFellesbarn, "barna", "barnet") + " som bur saman med begge sine foreldra ikkje lenger redusert. Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget for ".expr() + ifElse(harFlereFellesbarn, "barna", "barnet") + " som bur saman med begge foreldra sine. Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året.",
            //)
            //textExpr(
            //    Bokmal to "Barnetillegget for ".expr() + ifElse(harFlereSaerkullsbarn, "barna", "barnet") + " som ikke bor sammen med begge foreldrene er beregnet ut fra inntekten din på ".expr() + inntektBruktIAvkortningAvBarnetilleggForSaerkullsbarn + " kroner. Dette er høyerelavere enn fribeløpsgrensen på ".expr() + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBfribelop + " kroner. Dette barnetillegget er derfor ikke lenger også redusert. Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget for ".expr() + ifElse(harFlereSaerkullsbarn, "barna", "barnet") + " som ikke bor sammen med begge foreldrene. Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året.",
            //    Nynorsk to "Barnetillegget for ".expr() + ifElse(harFlereSaerkullsbarn, "barna", "barnet") + " som ikkje bur saman med begge foreldra er berekna ut frå inntekta di på ".expr() + inntektBruktIAvkortningAvBarnetilleggForSaerkullsbarn + " kroner. Dette er høgarelågere enn fribeløpet på ".expr() + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBfribelop + " kroner. Derfor er barnetillegget ikkje lenger også redusert. Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget for ".expr() + ifElse(harFlereSaerkullsbarn, "barna", "barnet") + " som ikkje bur saman med begge foreldra. Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året.",
            //)
            //textExpr(
            //    Bokmal to "Vi har beregnet barnetillegget på nytt ut fra inntekten din på ".expr() + inntektBruktIAvkortningAvBarnetilleggForSaerkullsbarn + " kroner. Dette er høyerelavere enn fribeløpsgrensen på ".expr() + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBfribelop + " kroner. Barnetillegget er derfor ikke lenger redusert. Det du har fått utbetalt i barnetillegg tidligere i år har også betydning for hva du får i barnetillegg framover. Dette ble tatt hensyn til da vi endret barnetillegget. Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året.",
            //    Nynorsk to "Vi har berekna barnetillegget på nytt ut frå inntekta di på ".expr() + inntektBruktIAvkortningAvBarnetilleggForSaerkullsbarn + " kroner. Dette beløpet er høgarelågare enn fribeløpet på ".expr() + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBfribelop + " kroner. Derfor er barnetillegget ikkje lenger redusert. Det du har fått utbetalt i barnetillegg tidlegare i år har også noko å seie for kva du får i barnetillegg framover. Dette har vi teke omsyn til når vi endra barnetillegget. Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året.",
            //)
            //textExpr(
            //    Bokmal to "Barnetillegget for ".expr() + ifElse(harFlereFellesbarn, "barna", "barnet") + " som bor med begge sine foreldre blir ikke utbetalt fordi den samlede inntekten til deg og din ".expr() + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT + " er høyere enn ".expr() + PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_Inntektstak + " kroner.",
            //    Nynorsk to "Barnetillegget for ".expr() + ifElse(harFlereFellesbarn, "barna", "barnet") + " som bur saman med begge forelda sine blir ikkje utbetalt fordi den samla inntekta til deg og ".expr() + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_NN_entall + " din er høgare enn ".expr() + PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_Inntektstak + " kroner.",
            //)
            //textExpr(
            //    Bokmal to "Barnetillegget for ".expr() + ifElse(harFlereSaerkullsbarn, "barna", "barnet") + " som ikke bor sammen med begge foreldrene blir ikke utbetalt fordi inntekten din er høyere enn ".expr() + PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_Inntektstak + " kroner.",
            //    Nynorsk to "Barnetillegget for ".expr() + ifElse(harFlereSaerkullsbarn, "barna", "barnet") + " som ikkje bur saman med begge foreldra blir ikkje utbetalt fordi inntekta di er høgare enn ".expr() + PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_Inntektstak + " kroner.",
            //)
            //textExpr(
            //    Bokmal to "Fordi du ikke har barnetillegg for ".expr() + ifElse(harFlereFellesbarn, "barna", "barnet") + " som bor med begge sine foreldre for ".expr() + ifElse(harFlereSaerkullsbarn, "barna", "barnet") + " som ikke bor sammen med begge foreldrene hele året er inntektene og fribeløpet justert slik at det kun gjelder for den perioden du mottar barnetillegg. Fordi sivilstanden din har endret seg er inntektene og fribeløpet justert slik at de kun gjelder for den framtidige perioden du mottar barnetillegg.",
            //    Nynorsk to "Fordi du ikkje har barnetillegg for ".expr() + ifElse(harFlereFellesbarn, "barna", "barnet") + " som bur saman med begge foreldra sine for ".expr() + ifElse(harFlereSaerkullsbarn, "barna", "barnet") + " som ikkje bur saman med begge foreldra heile året, er inntektene og fribeløpet justert slik at detdei berre gjeld for den perioden du får barnetillegg. Fordi sivilstanden din har endra seg, er inntektene og fribeløpet justert slik at dei berre gjeld for den framtidige perioden du får barnetillegg.",
            //)
            //text(
            //    Bokmal to "Du kan lese mer om dette i vedlegget «Opplysninger om beregningen».",
            //    Nynorsk to "Du kan lese meir om dette i vedlegget «Opplysningar om berekninga».",
            //)
            //text(
            //    Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14 og 22-12.",
            //    Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14 og 22-12.",
            //)
            //text(
            //    Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14 til 12-16 og 22-12 og forskrift om overgangsregler for barnetillegg i uføretrygden.",
            //    Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14 til 12-16 og 22-12 og forskrift om overgangsregler for barnetillegg i uføretrygda.",
            //)
            //text(
            //    Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14 til 12-16 og 22-12 og overgangsforskriften §8 og forskrift om overgangsregler for barnetillegg i uføretrygden.",
            //    Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14 til 12-16 og 22-12 og overgangsforskrifta § 8 og forskrift om overgangsregler for barnetillegg i uføretrygda.",
            //)
            //text(
            //    Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14 til 12-16, 12-18 og 22-12 og forskrift om overgangsregler for barnetillegg i uføretrygden.",
            //    Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14 til 12-16, 12-18 og 22-12 og forskrift om overgangsreglar for barnetillegg i uføretrygda.",
            //)
            //text(
            //    Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14, 12- 18 og 22-12.",
            //    Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14, 12-18 og 22-12.",
            //)
            //text(
            //    Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-14 og 22-12 og overgangsforskriften § 8.",
            //    Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-14 og 22-12 og overgangsforskrifta § 8.",
            //)
            //text(
            //    Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 12-15, 12-16 og 22-12 og forskrift om overgangsregler for barnetillegg i uføretrygden.",
            //    Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 12-15, 12-16 og 22-12 og forskrift om overgangsreglar for barnetillegg i uføretrygda.",
            //)
            //text(
            //    Bokmal to "Hva får du i uføretrygd framover?",
            //    Nynorsk to "Kva får du i uføretrygd framover?",
            //)
            //textExpr(
            //    Bokmal to "Ut fra den årlige inntekten din vil uføretrygden utgjøre ".expr() + PE_UT_NettoAkk_pluss_NettoRestAr + " kroner. Hittil i år har du fått utbetalt ".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_NettoAkk + " kroner. Du har derfor rett til en utbetaling av uføretrygd på ".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Netto + " kroner per måned for resten av året.",
            //    Nynorsk to "Ut frå den årlege inntekta di vil uføretrygda utgjere ".expr() + PE_UT_NettoAkk_pluss_NettoRestAr + " kroner. Hittil i år har du fått utbetalt ".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_NettoAkk + " kroner. Du har derfor rett til ei utbetaling av uføretrygd på ".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_Netto + " kroner per månad for resten av året.",
            //)
            //textExpr(
            //    Bokmal to "Selv om uføretrygden din er redusert på grunn av inntekt beholder du likevel uføregraden din på ".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad + " prosent. Du får utbetalt hele uføretrygden igjen dersom du tjener mindre enn inntektsgrensen din.",
            //    Nynorsk to "Sjølv om uføretrygda di er redusert på grunn av inntekt beheld du likevel uføregraden din på ".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_Uforetrygdberegning_Uforegrad + " prosent. Du får utbetalt heile uføretrygda att dersom du tener mindre enn inntektsgrensa di.",
            //)
            //text(
            //    Bokmal to "Hva får du i barnetillegg framover?",
            //    Nynorsk to "Kva får du i barnetillegg framover?",
            //)
            //textExpr(
            //    Bokmal to "Ut fra den samlede inntekten til deg og din ".expr() + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT + " er barnetillegget vurdert på nytt. Barnetillegget er redusert for hele året ut fra den inntekten som overstiger fribeløpet. Den årlige reduksjonen av barnetillegget er ".expr() + PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_AvkortingsbelopPerAr + " kroner. Vi har tatt hensyn til hvordan barnetillegget eventuelt har vært redusert i starten av året, og vi har derfor lagt tiltrukket fra ".expr() + PE_BarnetilleggFelles_JusteringsbelopPerArUtenMinus + " kroner i det vi reduserer barnetillegget med for resten av året. Du har derfor rett til en utbetaling av barnetillegg på ".expr() + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto + " kroner per måned for resten av året.",
            //    Nynorsk to "Ut frå dei samla inntektene til deg og ".expr() + PE_Sivilstand_Ektefelle_Partner_Samboer_Bormed_UT_NN_entall + " din er barnetillegget blitt vurdert på nytt. Barnetillegget er redusert for heile året ut frå inntekta som overstig fribeløpet. Den årlege reduksjonen av barnetillegget er ".expr() + PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggFelles_AvkortningsInformasjon_AvkortingsbelopPerAr + " kroner. Vi har teke omsyn til korleis barnetillegget eventuelt har vore redusert i starten av året, og vi har derfor lagt tiltrekt frå ".expr() + PE_BarnetilleggFelles_JusteringsbelopPerArUtenMinus + " kroner i det vi har redusert barnetillegget med for resten av året. Du har derfor rett til ei utbetaling av barnetillegg på ".expr() + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBnetto + " kroner per månad for resten av året.",
            //)
            //textExpr(
            //    Bokmal to "Ut fra den samlede inntekten din er barnetillegget vurdert på nytt. Barnetillegget er redusert for hele året ut fra den inntekten som overstiger fribeløpet. Den årlige reduksjonen av barnetillegget er ".expr() + PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_AvkortingsbelopPerAr + " kroner. Vi har tatt hensyn til hvordan barnetillegget eventuelt har vært redusert i starten av året, og vi har derfor lagt tiltrukket fra ".expr() + PE_BarnetilleggSerkull_JusteringsbelopPerArUtenMinus + " kroner i det vi reduserer barnetillegget med for resten av året. Du har derfor rett til en utbetaling av barnetillegg på ".expr() + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto + " kroner per måned for resten av året.",
            //    Nynorsk to "Ut frå den samla inntekta di er barnetillegget blitt vurdert på nytt. Barnetillegget er redusert for heile året ut frå inntekta som overstig fribeløpet. Den årlege reduksjonen av barnetillegget er ".expr() + PE_Vedtaksbrev_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_BarnetilleggSerkull_AvkortningsInformasjon_AvkortingsbelopPerAr + " kroner. Vi har teke omsyn til korleis barnetillegget eventuelt har vore redusert i starten av året, og vi har derfor lagt tiltrekt frå ".expr() + PE_BarnetilleggSerkull_JusteringsbelopPerArUtenMinus + " kroner i det vi reduserte barnetillegget med for resten av året. Du har derfor rett til ei utbetaling av barnetillegg på ".expr() + PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBnetto + " kroner per månad for resten av året.",
            //)
            //textExpr(
            //    Bokmal to "Du får ikke utbetalt barnetillegget for ".expr() + PE_UT_Barnet_Barna_Felles_serkull + " som bor med begge sine foreldreikke bor sammen med begge foreldrene.",
            //    Nynorsk to "Du får ikkje utbetalt barnetillegget for ".expr() + PE_UT_Barnet_Barna_Felles_serkull + " som bur med begge foreldraikkje bur saman med begge foreldra.",
            //)
            //text(
            //    Bokmal to "For deg som mottar gjenlevendetillegg",
            //    Nynorsk to "For deg som mottar tillegg for attlevande ektefelle",
            //)
            //text(
            //    Bokmal to "Du mottar gjenlevendetillegg i uføretrygden din. Tjener du mer enn inntektsgrensen din, reduserer vi gjenlevendetillegget ditt med samme prosent som vi reduserer uføretrygden din med.",
            //    Nynorsk to "Du får attlevandetillegg i uføretrygda di. Tener du meir enn inntektsgrensa di, reduserer vi attlevandetillegget ditt med same prosenten som vi reduserer uføretrygda di med.",
            //)
            //text(
            //    Bokmal to "Gjenlevendetillegget er redusert ut fra den innmeldte inntekten.",
            //    Nynorsk to "Attlevandetillegget er redusert ut frå den innmelde inntekta.",
            //)
            //text(
            //    Bokmal to "Gjenlevendetillegget er økt ut fra den innmeldte inntekten.",
            //    Nynorsk to "Attlevandetillegget er auka ut frå den innmelde inntekta.",
            //)
            //text(
            //    Bokmal to "Du kan lese mer om dette i vedlegget «Opplysninger om beregningen».",
            //    Nynorsk to "Du kan lese meir om dette i vedlegget «Opplysningar om berekninga».",
            //)
            //text(
            //    Bokmal to "For deg som mottar ektefelletillegg",
            //    Nynorsk to "For deg som får ektefelletillegg",
            //)
            //text(
            //    Bokmal to "Du mottar ektefelletillegg i uføretrygden din. Dette tillegget blir ikke endret som følge av inntektsendringer.",
            //    Nynorsk to "Du får ektefelletillegg i uføretrygda di. Dette tillegget blir ikkje endra som følgje av inntektsendringar.",
            //)
            //text(
            //    Bokmal to "Du må melde fra om endringer i inntekt",
            //    Nynorsk to "Du må melde frå om endringar i inntekt",
            //)
            //textExpr(
            //    Bokmal to "Det er viktig at du melder fra om inntektsendringer slik at uføretrygden og barnetillegget blir så riktig som mulig. Du kan enkelt melde fra om inntektsendringer under menyvalget «uføretrygd» når du logger deg inn på ".expr() + FF01_webadresse_nav + ".",
            //    Nynorsk to "Det er viktig at du melder frå om inntektsendringar, slik at uføretrygda og barnetillegget blir så riktig som mogleg. Du kan enkelt melde frå om inntektsendringar under menyvalet «uføretrygd» når du loggar deg inn på ".expr() + FF01_webadresse_nav + ".",
            //)
            //textExpr(
            //    Bokmal to "Vi gjør oppmerksom på at det ikke utbetales uføretrygd når inntekten din utgjør mer enn 80 prosent av inntekten du hadde før du ble ufør, det vil si ".expr() + inntektstakUfoeretrygd + " kroner per år. Inntekten er justert opp til dagens verdi.",
            //    Nynorsk to "Vi gjer merksam på at det ikkje blir utbetalt uføretrygd når inntekta di utgjer meir enn 80 prosent av inntekta du hadde før du blei ufør, det vil seie ".expr() + inntektstakUfoeretrygd + " kroner per år. Inntekta er justert opp til dagens verdi.",
            //)
            //textExpr(
            //    Bokmal to "Vi gjør oppmerksom på at det ikke utbetales uføretrygd når inntekten din utgjør mer enn inntektsgrensen din, det vil si ".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense + " kroner per år.",
            //    Nynorsk to "Vi gjer merksam på at det ikkje blir utbetalt uføretrygd når inntekta di utgjer meir enn inntektsgrensa di, det vil seie ".expr() + PE_Vedtaksdata_BeregningsData_BeregningUfore_BeregningYtelsesKomp_UforetrygdOrdiner_AvkortningsInformasjon_Inntektsgrense + " kroner per år.",
            //)
            //textExpr(
            //    Bokmal to "Vi vil foreta et etteroppgjør hvis du har fått utbetalt for mye eller for lite uføretrygd og barnetillegg i løpet av året. Dette gjør vi når likningen fra ".expr() + PE_Skatteetaten + " er klar. Har du fått utbetalt for lite i uføretrygd og barnetillegg, vil vi utbetale dette beløpet til deg. Hvis du har fått utbetalt for mye i uføretrygd og barnetillegg, må du betale dette tilbake.",
            //    Nynorsk to "Vi gjer eit etteroppgjer dersom du har fått utbetalt for mykje eller for lite uføretrygd og barnetillegg i løpet av året. Dette gjer vi når likninga frå Skatteetaten er klar. Har du fått utbetalt for lite i uføretrygd og barnetillegg, vil vi betale deg dette beløpet. Dersom du har fått utbetalt for mykje i uføretrygd og barnetillegg, må du betale dette tilbake.",
            //)
            //text(
            //    Bokmal to "Inntekter som ikke skal føre til reduksjon av uføretrygden",
            //    Nynorsk to "Inntekter som ikkje skal føra til reduksjon av uføretrygda",
            //)
            //text(
            //    Bokmal to "Det kan gjøres unntak for enkelte inntektstyper som ikke skal føre til reduksjon av uføretrygden. Dette kan gjelde følgende:",
            //    Nynorsk to "Det kan gjerast unntak for enkelte inntektstypar som ikkje skal føra til reduksjon av uføretrygda. Dette kan gjelda følgjande:",
            //)
            //text(
            //    Bokmal to "Erstatning for inntektstap ved erstatningsoppgjør etter",
            //    Nynorsk to "Erstatning for inntektstap ved erstatningsoppgjer etter",
            //)
            //text(
            //    Bokmal to "•	Skadeerstatningsloven § 3-1",
            //    Nynorsk to "•	Skadeerstatningsloven § 3-1",
            //)
            //text(
            //    Bokmal to "•	Yrkesskadeforsikringsloven § 13",
            //    Nynorsk to "•	Yrkesskadeforsikringsloven § 13",
            //)
            //text(
            //    Bokmal to "•	Pasientskadeloven § 4 første ledd",
            //    Nynorsk to "•	Pasientskadeloven § 4 første ledd",
            //)
            //text(
            //    Bokmal to "Inntekt fra arbeid eller virksomhet som ble helt avsluttet før du fikk innvilget uføretrygd, for eksempel:",
            //    Nynorsk to "Inntekt frå arbeid eller verksemd som vart heilt avslutta før du fekk innvilga uføretrygd, til dømes:",
            //)
            //text(
            //    Bokmal to "•	Utbetalte feriepenger for et arbeidsforhold som er avsluttet",
            //    Nynorsk to "•	Utbetalte feriepengar for eit arbeidsforhold som er avslutta",
            //)
            //text(
            //    Bokmal to "•	Inntekter fra salg av produksjonsmidler i forbindelse med opphør av virksomheten",
            //    Nynorsk to "•	Inntekter frå sal av produksjonsmiddel i samband med opphøyr av verksemda",
            //)
            //text(
            //    Bokmal to "•	Produksjonstillegg og andre overføringer til gårdbrukere",
            //    Nynorsk to "•	Produksjonstillegg og andre overføringar til gardbrukarar",
            //)
            //text(
            //    Bokmal to "Dersom vi mottar dokumentasjon fra deg som bekrefter at du har slik inntekt, kan vi gjøre en ny beregning av uføretrygden din.",
            //    Nynorsk to "Dersom vi mottek dokumentasjon frå deg som stadfestar at du har slik inntekt, kan vi gjera ei ny berekning av uføretrygda di.",
            //)
            //text(
            //    Bokmal to "Du må melde fra om endringer",
            //    Nynorsk to "Du må melde frå om endringar",
            //)
            //text(
            //    Bokmal to "Skjer det endringer, må du melde fra til oss med en gang. I vedlegget «Orientering om rettigheter og plikter» ser du hvilke endringer du må si fra om.",
            //    Nynorsk to "Skjer det endringar, må du melde frå til oss med ein gong. I vedlegget «Orientering om rettar og plikter» ser du kva endringar du må seie frå om.",
            //)
            //text(
            //    Bokmal to "Du har rett til å klage",
            //    Nynorsk to "Du har rett til å klage",
            //)
            //textExpr(
            //    Bokmal to "Hvis du mener vedtaket er feil, kan du klage. Fristen for å klage er seks uker fra den datoen du mottok vedtaket. I vedlegget «Orientering om rettigheter og plikter» får du vite mer om hvordan du går fram. Du finner skjema og informasjon på ".expr() + FF01_webadresse_navKlage + ".",
            //    Nynorsk to "Viss du meiner vedtaket er feil, kan du klage. Fristen for å klage er seks veker frå den datoen du fekk vedtaket. I vedlegget «".expr() + PE_UT_OrienteringOmRettighetOgPlikt_eller_RettigheterOgMulighetForKlage + "» får du vite meir om korleis du går fram. Du finn skjema og informasjon på ".expr() + FF01_webadresse_navKlage + ".",
            //)
            //text(
            //    Bokmal to "Du har rett til innsyn",
            //    Nynorsk to "Du har rett til innsyn",
            //)
            //textExpr(
            //    Bokmal to "Du har rett til å se dokumentene i saken din. Se vedlegg «Orientering om rettigheter og plikter» for informasjon om hvordan du går fram.",
            //    Nynorsk to "Du har rett til å sjå dokumenta i saka di. Sjå vedlegg «".expr() + PE_UT_OrienteringOmRettighetOgPlikt_eller_RettigheterOgMulighetForKlage + "» for informasjon om korleis du går fram.",
            //)
            //text(
            //    Bokmal to "Sjekk utbetalingene dine",
            //    Nynorsk to "Sjekk utbetalingane dine",
            //)
            //textExpr(
            //    Bokmal to "Du får uføretrygd utbetalt den 20. hver måned, eller senest siste virkedag før denne datoen. Se alle utbetalinger du har mottatt: ".expr() + FF01_webadresse_uforetrygd + ". Her kan du også endre kontonummer.",
            //    Nynorsk to "Du får uføretrygd betalt ut den 20. kvar månad eller seinast siste vyrkedag før denne datoen. Sjå alle utbetalingar du har fått: ".expr() + FF01_webadresse_uforetrygd + ". Her kan du også endre kontonummer.",
            //)
            //text(
            //    Bokmal to "Skattekort",
            //    Nynorsk to "Skattekort",
            //)
            //textExpr(
            //    Bokmal to "Uføretrygd skattlegges som lønnsinntekt. Du trenger ikke levere skattekortet ditt til ".expr() + FF01_NAV + " fordi skatteopplysningene dine sendes elektronisk fra ".expr() + PE_Skatteetaten + ". Du bør likevel sjekke at du har riktig skattekort. Skattekortet kan du endre på ".expr() + FF01_webadresse_Skatteetaten_no + ". Under menyvalget «uføretrygd» når du logger deg inn på ".expr() + FF01_webadresse + ", kan du se hvilket skattetrekk som er registrert hos ".expr() + FF01_NAV + ".",
            //    Nynorsk to "Uføretrygd blir skattlagd som lønsinntekt. Du treng ikkje levere skattekortet ditt til ".expr() + FF01_NAV + ", fordi skatteopplysningane dine blir sende elektronisk frå ".expr() + PE_Skatteetaten + ". Du bør likevel sjekke at du har rett skattekort. Skattekortet kan du endre på ".expr() + FF01_webadresse_Skatteetaten_no + ". Under menyvalet «uføretrygd» når du logger deg inn på ".expr() + FF01_webadresse + ", kan du sjå kva skattetrekk som er registrert hos ".expr() + FF01_NAV + ".",
            //)
            //text(
            //    Bokmal to "Skatt for deg som bor i utlandet",
            //    Nynorsk to "Skatt for deg som bur i utlandet",
            //)
            //textExpr(
            //    Bokmal to "Bor du i utlandet og betaler kildeskatt, finner du mer informasjon om kildeskatt på ".expr() + FF01_webadresse_Skatteetaten_no + ". Hvis du er bosatt i utlandet og betaler skatt i annet land enn Norge, kan du kontakte skattemyndighetene der du bor.",
            //    Nynorsk to "Bur du i utlandet og betaler kjeldeskatt, finn du meir informasjon om kjeldeskatt på ".expr() + FF01_webadresse_Skatteetaten_no + ". Viss du er busett i utlandet og betaler skatt i eit anna land enn Noreg, kan du kontakte skattemyndigheitene der du bur.",
            //)
            //text(
            //    Bokmal to "Har du spørsmål?",
            //    Nynorsk to "Har du spørsmål?",
            //)
            //text(
            //    Bokmal to "Du finner mer informasjon på nav.no. Hvis du ikke finner svar på spørsmålet ditt, kontakt oss på nav.no/kontakt",
            //    Nynorsk to "Du finn meir informasjon på nav.no. Om du ikkje finn svar på spørsmålet ditt, kontakt oss på nav.no/kontakt.",
            //)
            //text(
            //    Bokmal to "Med vennlig hilsen",
            //    Nynorsk to "Med vennleg helsing",
            //)
            //textExpr(
            //    Bokmal to PE_Kontaktinformasjon_NavnAvsenderEnhet ,
            //    Nynorsk to  PE_Kontaktinformasjon_NavnAvsenderEnhet ,
            //)
            //text(
            //    Bokmal to "Saken har blitt automatisk saksbehandlet av vårt fagsystem. Vedtaksbrevet er derfor ikke underskrevet av saksbehandler.",
            //    Nynorsk to "Saka er automatisk saksbehandla av fagsystemet vårt. Vedtaksbrevet er derfor ikkje skrive under av saksbehandlar.",
            //)
            //text(
            //    Bokmal to "Vedlegg:",
            //    Nynorsk to "Vedlegg:",
            //)
            //text(
            //    Bokmal to "Dette er din månedlige uføretrygd før skatt",
            //    Nynorsk to "Dette er den månadlege uføretrygda di før skatt",
            //)
            //text(
            //    Bokmal to "Opplysninger om beregningen",
            //    Nynorsk to "Opplysningar om berekninga",
            //)
            //text(
            //    Bokmal to "Orientering om rettigheter og plikter",
            //    Nynorsk to "Orientering om rettar og plikter",
            //)
        }
        includeAttachment(
            createVedleggOpplysningerBruktIBeregningUT(skalViseMinsteytelse = true, skalViseBarnetillegg = true),
            opplysningerBruktIBeregningUTDto
            )
    }
}