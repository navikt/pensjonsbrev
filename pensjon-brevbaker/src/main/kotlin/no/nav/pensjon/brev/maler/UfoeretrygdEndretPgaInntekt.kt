package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.LetterMetadata
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.Year
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDtoSelectors.aarFoerVirkningsAar
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDtoSelectors.antallFellesBarn
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDtoSelectors.antallSaerkullsbarn
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDtoSelectors.barnetilleggFellesbarnInntektBruktIAvkortning
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDtoSelectors.barnetilleggSaerkullsbarnInntektBruktIAvkortning
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDtoSelectors.beloepGammelBarnetillegFellesBarn
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDtoSelectors.beloepGammelBarnetillegSaerkullsbarn
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDtoSelectors.beloepGammelUfoeretrygd
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDtoSelectors.beloepNyBarnetillegFellesBarn
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDtoSelectors.beloepNyBarnetillegSaerkullsbarn
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDtoSelectors.beloepNyUfoeretrygd
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDtoSelectors.brukersSivilstandUfoeretrygd
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDtoSelectors.forventetInntektAvkoret
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDtoSelectors.fyller67IVirkningsAar
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDtoSelectors.harInnvilgetBarnetilleggFellesBarn
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDtoSelectors.harInnvilgetBarnetilleggSaerkullsBarn
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDtoSelectors.ufoeregrad
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDtoSelectors.utbetalingsgrad
import no.nav.pensjon.brev.maler.UfoeretrygdEndretPgaInntektDtoSelectors.virkningFraOgMed
import no.nav.pensjon.brev.maler.fraser.UfoeretrygdEndretPgaInntekt
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.VedtaksbrevTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import java.time.LocalDate
import java.time.Month

data class UfoeretrygdEndretPgaInntektDto(
    val beloepGammelUfoeretrygd: Kroner,
    val beloepNyUfoeretrygd: Kroner,
    val beloepGammelBarnetillegSaerkullsbarn: Kroner,
    val beloepNyBarnetillegSaerkullsbarn: Kroner,
    val beloepGammelBarnetillegFellesBarn: Kroner,
    val beloepNyBarnetillegFellesBarn: Kroner,
    val harInnvilgetBarnetilleggFellesBarn: Boolean,
    val harInnvilgetBarnetilleggSaerkullsBarn: Boolean,
    val brukersSivilstandUfoeretrygd: Sivilstand,
    val barnetilleggSaerkullsbarnInntektBruktIAvkortning: Kroner,
    val barnetilleggFellesbarnInntektBruktIAvkortning: Kroner,
    val virkningFraOgMed: LocalDate,
    val antallFellesBarn: Int,
    val forventetInntektAvkortet: Kroner,
    val forventetInntektAvkoret: Kroner,
    val aarFoerVirkningsAar: Year,
    val ufoeregrad: Double,
    val utbetalingsgrad: Double,
    val fyller67IVirkningsAar: Boolean,
    val antallSaerkullsbarn: Int,
)

@TemplateModelHelpers
object UfoeretrygdEndretPgaInntekt : VedtaksbrevTemplate<UfoeretrygdEndretPgaInntektDto> {
    override val kode = Brevkode.Vedtak.UFOER_ENDRET_PGA_INNTEKT

    override val template = createTemplate(
        kode.name,
        letterDataType = UfoeretrygdEndretPgaInntektDto::class,
        languages(Bokmal),
        letterMetadata = LetterMetadata(
            "Vedtak – endring av uføretrygd på grunn av inntekt (automatisk)",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK
        )
    ) {
        val harFlereBarnetillegg = harInnvilgetBarnetilleggFellesBarn and harInnvilgetBarnetilleggSaerkullsBarn
        val harEndretBarnetillegSaerkullsbarn =
            beloepGammelBarnetillegSaerkullsbarn.notEqualTo(beloepNyBarnetillegSaerkullsbarn)
        val harEndretBarnetilleggFellesbarn =
            beloepGammelBarnetillegFellesBarn.notEqualTo(beloepNyBarnetillegFellesBarn)
        val harEndretBarnetillegg = (harEndretBarnetillegSaerkullsbarn or harEndretBarnetilleggFellesbarn)
        val harEndretUfoeretrygd = beloepGammelUfoeretrygd.notEqualTo(beloepNyUfoeretrygd)
        val harFlereFellesBarn = antallFellesBarn.greaterThan(1)
        val harFlereSaerkullbarn = antallSaerkullsbarn.greaterThan(1)



        title {
            includePhrase(
                UfoeretrygdEndretPgaInntekt.Tittel(
                    harEndretUfoeretrygd = harEndretUfoeretrygd,
                    harFlereBarnetillegg = harFlereBarnetillegg,
                    harEndretBarnetillegg = harEndretBarnetillegg,
                )
            )
        }

        outline {
            val virkningsdatoErFoersteJanuar =
                virkningFraOgMed.month.equalTo(Month.JANUARY) and virkningFraOgMed.day.equalTo(1)
            showIf(virkningsdatoErFoersteJanuar) {
                includePhrase(
                    UfoeretrygdEndretPgaInntekt.InnledningReduksjon(
                        forventetInntektAvkoret = forventetInntektAvkoret,
                        virkningFraOgMedAar = virkningFraOgMed.year,
                        aarFoerVirkningsAar = aarFoerVirkningsAar,
                        ufoeregrad = ufoeregrad,
                        utbetalingsgrad = utbetalingsgrad,
                        fyller67IVirkningsAar = fyller67IVirkningsAar,
                    )
                )
            }.orShow {
                includePhrase(
                    UfoeretrygdEndretPgaInntekt.InnledningInntektsendring(
                        harFlereFellesBarn = harFlereFellesBarn,
                        harEndretBarnetillegg = harEndretBarnetillegg,
                        harEndretUfoeretrygd = harEndretUfoeretrygd,
                        harFlereBarnetillegg = harFlereBarnetillegg,
                        harInnvilgetBarnetilleggFellesBarn = harInnvilgetBarnetilleggFellesBarn,
                        brukersSivilstandUfoeretrygd = brukersSivilstandUfoeretrygd,
                        virkningFraOgMed = virkningFraOgMed,
                    )
                )
            }

            showIf(
                harEndretUfoeretrygd
                        and harEndretBarnetillegg
                        and virkningsdatoErFoersteJanuar
            ) {
                showIf(
                    harInnvilgetBarnetilleggSaerkullsBarn
                            and not(harInnvilgetBarnetilleggFellesBarn)
                ) {
                    paragraph {
                        textExpr(
                            Bokmal to "I reduksjonen av barnetillegget ditt vil vi bruke en inntekt på ".expr() + barnetilleggSaerkullsbarnInntektBruktIAvkortning.format() + " kroner. "
                        )
                    }
                }

                showIf(harInnvilgetBarnetilleggFellesBarn) {
                    paragraph {
                        textExpr(
                            Bokmal to "I reduksjonen av barnetillegget ditt vil vi bruke en inntekt på ".expr() +
                                    barnetilleggFellesbarnInntektBruktIAvkortning.format() +
                                    " kroner for " + ifElse(harFlereFellesBarn, "barna", "barnet") +
                                    " som bor med begge sine foreldre.",
                        )
                        showIf(harInnvilgetBarnetilleggSaerkullsBarn) {
                            textExpr(
                                Bokmal to " For ".expr() + ifElse(harFlereSaerkullbarn, "barna", "barnet") +
                                        " som ikke bor sammen med begge foreldrene vil vi bruke en inntekt på "
                                        + barnetilleggSaerkullsbarnInntektBruktIAvkortning.format() + " kroner.",
                            )
                        }
                    }
                }
            }

            showIf(
                not(harEndretUfoeretrygd)
                        and virkningsdatoErFoersteJanuar
                        and harEndretBarnetilleggFellesbarn
            ) {
                textExpr(
                    Bokmal to "Vi vil bruke en inntekt på ".expr()
                            + barnetilleggFellesbarnInntektBruktIAvkortning.format() +
                            " kroner når vi reduserer barnetillegget " + ". For ".expr()
                            + ifElse(harFlereSaerkullbarn, "barna", "barnet") +
                            " som ikke bor sammen med begge foreldrene vil vi bruke en inntekt på "
                            + barnetilleggSaerkullsbarnInntektBruktIAvkortning.format() +
                            " kroner"
                )

                showIf(harFlereBarnetillegg) {
                    textExpr(
                        Bokmal to "for ".expr()
                                + ifElse(harFlereFellesBarn, "barna", "barnet")
                                + " som bor med begge sine foreldre for " + virkningFraOgMed.year.format()
                    )
                }.orShow {
                    text(
                        Bokmal to "ditt"
                    )
                }

                textExpr(
                    Bokmal to ". Har du ikke meldt inn nye inntekter for ".expr() + virkningFraOgMed.year.format()
                            + ", er inntektene justert opp til dagens verdi."
                )
            }

            //I reduksjonen av barnetillegget ditt vil vi bruke en inntekt på <PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggFelles_BTFBInntektBruktiAvkortning> kroner for <PE_UT_Barnet_Barna_Felles> som bor med begge sine foreldre. For <PE_UT_Barnet_Barna_Serkull> som ikke bor sammen med begge foreldrene vil vi bruke en inntekt på <PE_Vedtaksdata_BeregningsData_Beregning_BeregningYtelseKomp_BarnetilleggSerkull_BTSBInntektBruktiAvkortning> kroner.
            //IF(endret UT
            //AND endret barnetilleg
            //AND virkdatoFoersteJanuar
            //AND BTFB innvilget
            //)
            //THEN
            // INCLUDE
            //ENDIF
        }
    }
}