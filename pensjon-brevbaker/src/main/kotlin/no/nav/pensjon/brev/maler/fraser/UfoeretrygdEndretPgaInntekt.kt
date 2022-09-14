package no.nav.pensjon.brev.maler.fraser

import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.api.model.Sivilstand
import no.nav.pensjon.brev.api.model.Year
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.TextOnlyPhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.TextOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import java.time.LocalDate


object UfoeretrygdEndretPgaInntekt {
    data class Tittel(
        val harEndretUfoeretrygd: Expression<Boolean>,
        val harEndretBarnetillegg: Expression<Boolean>,
        val harFlereBarnetillegg: Expression<Boolean>,
    ) : TextOnlyPhrase<LangBokmal>() {
        override fun TextOnlyScope<LangBokmal, Unit>.template() {
            showIf(harEndretUfoeretrygd and not(harEndretBarnetillegg)) {
                text(
                    Bokmal to "NAV har endret utbetalingen av uføretrygden din",
                )
            }

            showIf(harEndretUfoeretrygd and harEndretBarnetillegg) {
                textExpr(
                    Bokmal to "NAV har endret utbetalingen av uføretrygden og ".expr()
                            + ifElse(harFlereBarnetillegg, "barnetilleggene dine", "barnetillegget ditt"),
                )
            }

            showIf(not(harEndretUfoeretrygd) and harEndretBarnetillegg) {
                textExpr(
                    Bokmal to "NAV har endret utbetalingen av ".expr() + ifElse(
                        harFlereBarnetillegg,
                        "barnetilleggene",
                        "barnetillegget"
                    ) + " i uføretrygden din".expr()
                )
            }
        }
    }

    // TBU2249
    data class InnledningInntektsendring(
        val harFlereFellesBarn: Expression<Boolean>,
        val harEndretBarnetillegg: Expression<Boolean>,
        val harEndretUfoeretrygd: Expression<Boolean>,
        val harFlereBarnetillegg: Expression<Boolean>,
        val harInnvilgetBarnetilleggFellesBarn: Expression<Boolean>,
        val brukersSivilstandUfoeretrygd: Expression<Sivilstand>,
        val virkningFraOgMed: Expression<LocalDate>,
    ) : OutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
            paragraph {
                text(
                    Bokmal to "Vi har mottatt nye opplysninger om inntekten ",
                )
                showIf(not(harInnvilgetBarnetilleggFellesBarn)) {
                    text(
                        Bokmal to "din.",
                    )
                }.orShow {
                    text(Bokmal to "til deg eller din ")
                    includePhrase(Felles.SivilstandEPSUbestemtForm(brukersSivilstandUfoeretrygd))
                    text(Bokmal to ". Inntekten til din ")
                    includePhrase(Felles.SivilstandEPSUbestemtForm(brukersSivilstandUfoeretrygd))
                    text(
                        Bokmal to " har kun betydning for størrelsen på barnetillegget "
                    )
                    showIf(harFlereBarnetillegg) {
                        textExpr(
                            Bokmal to "for ".expr() + ifElse(harFlereFellesBarn, "barna", "barnet")
                                    + " som bor med begge foreldre.",
                        )
                    }.orShow {
                        text(
                            Bokmal to "ditt."
                        )
                    }
                }
                text(
                    Bokmal to " Utbetalingen av "
                )

                showIf(harEndretUfoeretrygd) {
                    text(
                        Bokmal to "uføretrygden din"
                    )
                    showIf(harEndretBarnetillegg) {
                        text(
                            Bokmal to " og "
                        )
                    }
                }
                showIf(harEndretBarnetillegg) {
                    showIf(harFlereBarnetillegg) {
                        text(
                            Bokmal to "barnetilleggene dine"
                        )
                    }.orShow {
                        text(
                            Bokmal to "barnetillegget ditt"
                        )
                    }
                }
                textExpr(
                    Bokmal to " er derfor endret fra ".expr() + virkningFraOgMed.format() + "."
                )
            }
        }
    }

    // TBU3403
    data class InnledningReduksjon(
        val forventetInntektAvkoret: Expression<Kroner>,
        val virkningFraOgMedAar: Expression<Int>,
        val aarFoerVirkningsAar: Expression<Year>,
        val ufoeregrad: Expression<Double>,
        val utbetalingsgrad: Expression<Double>,
        val fyller67IVirkningsAar: Expression<Boolean>
    ) : OutlinePhrase<LangBokmal>() {
        override fun OutlineOnlyScope<LangBokmal, Unit>.template() {
            paragraph {
                textExpr(
                    Bokmal to "Vi vil bruke en inntekt på ".expr() + forventetInntektAvkoret.format() +
                            " kroner når vi reduserer uføretrygden din for " + virkningFraOgMedAar.format() +
                            ". Har du ikke meldt inn ny inntekt for " + virkningFraOgMedAar.format() +
                            ", er inntekten justert opp til dagens verdi."
                )
            }
            showIf(utbetalingsgrad.notEqualTo(ufoeregrad)) {
                paragraph {
                    textExpr(
                        Bokmal to "Fikk du innvilget uføretrygd etter januar ".expr() + aarFoerVirkningsAar.format() + ", er inntekten justert opp slik at den gjelder for hele " + virkningFraOgMedAar.format() + "."
                    )
                }
            }

            showIf(fyller67IVirkningsAar) {
                paragraph {
                    textExpr(
                        Bokmal to "Fordi du fyller 67 år i ".expr() + virkningFraOgMedAar.format() + ", er inntekten justert i forhold til antall måneder du mottar uføretrygd."
                    )
                }
            }
        }
    }
}