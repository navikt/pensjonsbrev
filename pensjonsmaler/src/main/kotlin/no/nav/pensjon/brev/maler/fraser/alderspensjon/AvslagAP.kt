package no.nav.pensjon.brev.maler.fraser.alderspensjon

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate
import java.util.Date


data class AvslagAPTidligUttakInnledning(
    val kravDato: Expression<LocalDate>,
    val uttaksgrad: Expression<Int>
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            textExpr(
                Bokmal to "Du har for lav pensjonsopptjening til at du kan ta ut ".expr() +
                        uttaksgrad.format() + " prosent pensjon fra ".expr() +
                        kravDato.format() + ". Derfor har vi avslått søknaden din.",
                Nynorsk to "Du har for låg pensjonsopptening til at du kan ta ut ".expr() +
                        uttaksgrad.format() + " prosent pensjon frå ".expr() +
                        kravDato.format() + ". Derfor har vi avslått søknaden din.",
                English to "Your accumulated pension capital is not sufficient for you to draw a retirement pension at ".expr() +
                        uttaksgrad.format() + " percent from ".expr() +
                        kravDato.format() + ". Therefore, we have declined your application."
            )
        }
    }
}

object AvslagAPTidligUttak : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            text(
                Bokmal to "Våre beregninger viser at du ikke har rett til å ta ut alderspensjonen din før du blir 67 år.",
                Nynorsk to "Våre berekningar viser at du ikkje har rett til å ta ut alderspensjon før du blir 67 år.",
                English to "Our calculations shows that you are not eligible for retirement pension before the age of 67."
            )
        }
}

data class EuArt6Hjemmel(
    val proRataErBrukt: Expression<Boolean>,
    val avtalelandErEOSLand: Expression<Boolean>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            showIf(proRataErBrukt and avtalelandErEOSLand) {
                text(
                    Bokmal to "Vedtaket er også gjort etter EØS-avtalens regler i forordning 883/2004, artikkel 6.",
                    Nynorsk to "",
                    English to "Eu art6 hjemmel avslag."
                )
            }
        }
    }
}

data class AvtaleFritekstHjemmel(
    val proRataErBrukt: Expression<Boolean>,
    val avtalelandNavn: Expression<String>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            showIf(proRataErBrukt) {
                textExpr(
                    Bokmal to "Vedtaket er også gjort etter artikkel [FRITEKST; Legg inn aktuelle artikler om sammenlegging og eksport] i trygdeavtalen med ".expr() + avtalelandNavn + ".",
                    Nynorsk to "Vedtaket er også gjort etter artikkel [FRITEKST; Legg inn aktuelle artikler om sammenlegging og eksport] i trygdeavtale med ".expr() + avtalelandNavn + ".",
                    English to "This decision is also made pursuant to the provisions of Article [FRITEKST; Legg inn aktuelle artikler om sammenlegging og eksport] in the social security agreement with ".expr() + avtalelandNavn + "."
                )
            }
        }
    }
}

object AvslagAP2025TidligUttakHjemmel : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() =
        paragraph {
            text(
                Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 20-15 og 22-13.",
                Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 20-15 og 22-13.",
                English to "This decision is made pursuant to the provisions of the National Insurance Act.",
            )
        }
}

data class avslagAPTidligUttakBeregning1(
    val kravDato: Expression<LocalDate>,
    val minstePensjonsNivaa: Expression<Kroner>,
    val privatAFPErBrukt: Expression<Boolean>,
    val proRataErBrukt: Expression<Boolean>,
    val totalPensjonMedAFP: Expression<Kroner>,
    val uttaksgrad: Expression<Int>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {

        // norsk trygdetid og full uttak av alderspensjon
        paragraph {
            showIf(not(proRataErBrukt) and uttaksgrad.equalTo(100)) {
                list {
                    item {
                        textExpr(
                            Bokmal to "For å kunne ta ut alderspensjon før du fyller 67 år, må pensjonen din minst utgjøre ".expr() +
                                    minstePensjonsNivaa.format() + " kroner i året.",
                            Nynorsk to "For å kunne ta ut alderspensjon før du fyller 67 år, må pensjonen din minst utgjere ".expr() +
                                    minstePensjonsNivaa.format() + " kroner i året.",
                            English to "In order for you to be eligible for retirement pension before the age of 67, your retirement pension must be, at minimum, NOK ".expr() +
                                    minstePensjonsNivaa.format() + " a year."
                        )
                    }
                    showIf(not(privatAFPErBrukt)) {
                        item {
                            textExpr(
                                Bokmal to "Dersom du hadde tatt ut ".expr() + uttaksgrad.format() + " prosent alderspensjon fra ".expr() +
                                        kravDato.format() + ", ville du fått ".expr() + totalPensjonMedAFP.format() + " kroner årlig i pensjon.",
                                Nynorsk to "Dersom du hadde tatt ut ".expr() + uttaksgrad.format() + " prosent alderspensjon frå ".expr() +
                                        kravDato.format() + ", ville du fått ".expr() + totalPensjonMedAFP.format() + " kroner årleg i pensjon.",
                                English to "If you draw a retirement pension of ".expr() + uttaksgrad.format() + " percent from ".expr() +
                                        kravDato.format() + ", your retirement pension is calculated to be NOK ".expr() + totalPensjonMedAFP.format() + " a year."
                            )
                        }
                    }
                    showIf(privatAFPErBrukt) {
                        item {
                            textExpr(
                                Bokmal to "Dersom du hadde tatt ut ".expr() + uttaksgrad.format() + " prosent alderspensjon fra ".expr() +
                                        kravDato.format() + ", ville du fått ".expr() +
                                        totalPensjonMedAFP.format() + " kroner årlig i pensjon. I denne beregningen har vi inkludert AFP.",
                                Nynorsk to "Dersom du hadde tatt ut ".expr() + uttaksgrad.format() + " prosent alderspensjon frå ".expr() +
                                        kravDato.format() + ", ville du fått ".expr() +
                                        totalPensjonMedAFP.format() + " kroner årleg i pensjon. I denne berekninga har vi inkludert AFP.",
                                English to "If you draw a retirement pension of ".expr() + uttaksgrad.format() + " percent from ".expr() +
                                        kravDato.format() + ", your retirement pension is calculated to be NOK ".expr() +
                                        totalPensjonMedAFP.format() + " a year. This amount includes contractual early retirement pension.",
                            )
                        }
                    }
                }
            }
        }

        // norsk trygdetid og gradert uttak av alderspensjon     Check from here - Beregningen2Gradert
        paragraph {
            showIf(not(proRataErBrukt) and uttaksgrad.notEqualTo(100)) {
                list {
                    item {
                        textExpr(
                            Bokmal to "For å kunne ta ut alderspensjon før du fyller 67 år, må pensjonen din minst være ".expr() +
                                    minstePensjonsNivaa.format() + " kroner i året. Vi beregner den delen du ønsker å ta ut nå og hva du ville ha fått hvis du tar resten av pensjonen ved 67 år.",
                            Nynorsk to "For å kunne ta ut alderspensjon før du fyller 67 år, må pensjonen din minst utgjere ".expr() +
                                    minstePensjonsNivaa.format() + " kroner i året. Vi bereknar den delen du ynskjer å ta ut nå og kva du ville ha fått dersom du tar resten av pensjonen ved 67 år.",
                            English to "In order for you to be eligible for retirement pension before the age of 67, your retirement pension must be, at minimum, NOK ".expr() +
                                    minstePensjonsNivaa.format() + " a year. We calculate the part you wish to withdraw now and what you would have received if you take the rest of the pension at age 67."
                        )
                    }
                    showIf(not(privatAFPErBrukt)) {
                        item {
                            textExpr(
                                Bokmal to "Hvis du hadde tatt ut ".expr() + uttaksgrad.format() + " prosent alderspensjon fra ".expr() +
                                        kravDato.format() + ", ville du fått ".expr() + totalPensjonMedAFP.format() + " kroner årlig i full pensjon når du blir 67 år.",
                                Nynorsk to "Dersom du hadde tatt ut ".expr() + uttaksgrad.format() + " prosent alderspensjon frå ".expr() +
                                        kravDato.format() + ", ville du fått ".expr() + totalPensjonMedAFP.format() + " kroner årleg i full pensjon når du blir 67 år.",
                                English to "If you draw a retirement pension of ".expr() + uttaksgrad.format() + " percent from ".expr() +
                                        kravDato.format() + ", your full retirement pension is calculated to be NOK ".expr() + totalPensjonMedAFP.format() + " a year at age 67."
                            )
                        }
                    }
                    showIf(privatAFPErBrukt) {
                        item {
                            textExpr(
                                Bokmal to "Hvis du hadde tatt ut ".expr() + uttaksgrad.format() + " prosent alderspensjon fra ".expr() +
                                        kravDato.format() + ", ville du fått ".expr() +
                                        totalPensjonMedAFP.format() + " kroner årlig i full pensjon når du blir 67 år. I denne beregningen har vi inkludert AFP.",
                                Nynorsk to "Dersom du hadde tatt ut ".expr() + uttaksgrad.format() + " prosent alderspensjon frå ".expr() +
                                        kravDato.format() + ", ville du fått ".expr() +
                                        totalPensjonMedAFP.format() + " kroner årleg i full pensjon når du blir 67 år. I denne berekninga har vi inkludert AFP.",
                                English to "If you draw a retirement pension of ".expr() + uttaksgrad.format() + " percent from ".expr() +
                                        kravDato.format() + ", your full retirement pension is calculated to be NOK ".expr() +
                                        totalPensjonMedAFP.format() + " a year at age 67. This amount includes contractual early retirement pension.",
                            )
                        }
                    }
                }
            }
        }

        // med trydetid i utlandet og full uttak av alderspensjon
        paragraph {
            showIf(proRataErBrukt and uttaksgrad.equalTo(100)) {
                list {
                    item {
                        textExpr(
                            Bokmal to "For å kunne ta ut alderspensjon før du fyller 67 år, må pensjonen din minst utgjøre ".expr() +
                                    minstePensjonsNivaa.format() + " kroner i året. Vi har tatt hensyn til at du også har trygdetid fra land som Norge har trygdeavtale med.",
                            Nynorsk to "For å kunne ta ut alderspensjon før du fyller 67 år, må pensjonen din minst utgjere ".expr() +
                                    minstePensjonsNivaa.format() + " kroner i året. Vi har tatt omsyn til at du også har trygdetid frå land som Noreg har trygdeavtale med.",
                            English to "In order for you to be eligible for retirement pension before the age of 67, your retirement pension must be, at minimum, NOK ".expr() +
                                    minstePensjonsNivaa.format() + " a year. We have taken into account any periods of national insurance coverage that you may have in countries with which Norway has a social security agreement."
                        )
                    }
                    showIf(not(privatAFPErBrukt)) {
                        item {
                            textExpr(
                                Bokmal to "Dersom du hadde tatt ut ".expr() + uttaksgrad.format() + " percent from ".expr() +
                                        kravDato.format() + ", your retirement pension is calculated to be NOK ".expr() +
                                        totalPensjonMedAFP.format() + " a year.",
                                Nynorsk to "Dersom du hadde tatt ut ".expr() + uttaksgrad.format() + " prosent alderspensjon frå ".expr() +
                                        kravDato.format() + ", ville du fått ".expr() +
                                        totalPensjonMedAFP.format() + " kroner årleg i pensjon.",
                                English to "If you draw a retirement pension of ".expr() + uttaksgrad.format() + " percent from ".expr() +
                                        kravDato.format() + ", your retirement pension is calculated to be NOK ".expr() +
                                        totalPensjonMedAFP.format() + " a year."
                            )
                        }
                    }
                    showIf(privatAFPErBrukt) {
                        item {
                            textExpr(
                                Bokmal to "Dersom du hadde tatt ut ".expr() + uttaksgrad.format() + " prosent alderspensjon fra ".expr() +
                                        kravDato.format() + ", ville du fått ".expr() +
                                        totalPensjonMedAFP.format() + " kroner årlig i pensjon. I denne beregningen har vi inkludert AFP.",
                                Nynorsk to "Dersom du hadde tatt ut ".expr() + uttaksgrad.format() + " prosent alderspensjon frå ".expr() +
                                        kravDato.format() + ", ville du fått ".expr() +
                                        totalPensjonMedAFP.format() + " kroner årleg i pensjon . I denne berekninga har vi inkludert AFP.",
                                English to "If you draw a retirement pension of ".expr() + uttaksgrad.format() + " percent from ".expr() +
                                        kravDato.format() + ", your retirement pension is calculated to be NOK ".expr() +
                                        totalPensjonMedAFP.format() + " a year. This amount includes contractual early retirement pension."
                            )
                        }
                    }


                    // dligUttakBeregning3Gradert_001
                    paragraph { }
                    showIf(proRataErBrukt and not(privatAFPErBrukt) and uttaksgrad.notEqualTo(100)) {
                        list {
                            item {
                                textExpr(
                                    Bokmal to "For å kunne ta ut alderspensjon før du fyller 67 år, må pensjonen din minst utgjøre ".expr() +
                                            minstePensjonsNivaa.format() + " kroner i året. Vi har tatt hensyn til at du også har trygdetid fra land som Norge har trygdeavtale med.",
                                    Nynorsk to "For å kunne ta ut alderspensjon før du fyller 67 år, må pensjonen din minst utgjere ".expr() +
                                            minstePensjonsNivaa.format() + " kroner i året. Vi har tatt omsyn til at du også har trygdetid frå land som Noreg har trygdeavtale med.",
                                    English to "In order for you to be eligible for retirement pension before the age of 67, your retirement pension must be, at minimum, NOK ".expr() +
                                            minstePensjonsNivaa.format() + " a year. We have taken into account any periods of national insurance coverage that you may have in countries with which Norway has a social security agreement."
                                )
                            }
                            item {
                                textExpr(
                                    Bokmal to "Vi beregner den delen du ønsker å ta ut nå og hva du ville ha fått hvis du tar resten av pensjonen ved 67 år. Hvis du hadde tatt ut ".expr() +
                                            uttaksgrad.format() + " prosent alderspensjon fra ".expr() +
                                            kravDato.format() + ", ville du fått ".expr() +
                                            totalPensjonMedAFP.format() + " kroner årlig i full pensjon når du blir 67 år.",
                                    Nynorsk to "Vi bereknar den delen du ynskjer å ta ut nå og kva du ville ha fått dersom du tar resten av pensjonen ved 67 år. Dersom du hadde tatt ut ".expr() +
                                            uttaksgrad.format() + " prosent alderspensjon frå ".expr() +
                                            kravDato.format() + ", ville du fått ".expr() +
                                            totalPensjonMedAFP.format() + " kroner årleg i full pensjon når du blir 67 år.",
                                    English to "We calculate the part you wish to withdraw now and what you would have received if you take the rest of the pension at age 67. If you draw a retirement pension of ".expr() +
                                            uttaksgrad.format() + " percent from ".expr() +
                                            kravDato.format() + ", your full retirement pension is calculated to be NOK ".expr() +
                                            totalPensjonMedAFP.format() + " a year at age 67."
                                )
                            }
                        }
                    }
                }
                // avslagAPTidligUttakBeregning4 - med trygdetid fra avtaleland
                paragraph {
                    showIf(proRataErBrukt and privatAFPErBrukt) {
                        list {
                            item {
                                textExpr(
                                    Bokmal to "For å kunne ta ut alderspensjon før du fyller 67 år, må pensjonen din minst utgjøre ".expr() +
                                            minstePensjonsNivaa.format() + " kroner i året. Vi har tatt hensyn til at du også har trygdetid fra land som Norge har trygdeavtale med.",
                                    Nynorsk to "For å kunne ta ut alderspensjon før du fyller 67 år, må pensjonen din minst utgjere ".expr() +
                                            minstePensjonsNivaa.format() + " kroner i året. Vi har tatt omsyn til at du også har trygdetid frå land som Noreg har trygdeavtale med.",
                                    English to "In order for you to be eligible for retirement pension before the age of 67, your retirement pension must be, at minimum, NOK ".expr() +
                                            minstePensjonsNivaa.format() + " a year. We have taken into account any periods of national insurance coverage that you may have in countries with which Norway has a social security agreement."
                                )
                            }
                            showIf(uttaksgrad.equalTo(100)) {
                                item {
                                    textExpr(
                                        Bokmal to "Dersom du hadde tatt ut ".expr() + uttaksgrad.format() + " prosent alderspensjon fra ".expr() +
                                                kravDato.format() + ", ville du fått ".expr() +
                                                totalPensjonMedAFP.format() + " kroner årlig i pensjon. I denne beregningen har vi inkludert AFP.",
                                        Nynorsk to "Dersom du hadde tatt ut ".expr() + uttaksgrad.format() + " prosent alderspensjon frå ".expr() +
                                                kravDato.format() + ", ville du fått ".expr() +
                                                totalPensjonMedAFP.format() + " kroner årleg i pensjon . I denne berekninga har vi inkludert AFP.",
                                        English to "If you draw a retirement pension of ".expr() + uttaksgrad.format() + " percent from ".expr() +
                                                kravDato.format() + ", your retirement pension is calculated to be NOK ".expr() +
                                                totalPensjonMedAFP.format() + " a year. This amount includes contractual early retirement pension."
                                    )
                                }
                            }
                            showIf(uttaksgrad.notEqualTo(100)) {
                                item {
                                    textExpr(
                                        Bokmal to "Vi beregner den delen du ønsker å ta ut nå og hva du ville ha fått hvis du tar resten av pensjonen ved 67 år. Hvis du hadde tatt ut ".expr() +
                                                uttaksgrad.format() + " prosent alderspensjon fra ".expr() +
                                                kravDato.format() + ", ville du fått ".expr() +
                                                totalPensjonMedAFP.format() + " kroner årlig i full pensjon når du blir 67 år. I denne beregningen har vi inkludert AFP.",
                                        Nynorsk to "Vi bereknar den delen du ynskjer å ta ut nå og kva du ville ha fått dersom du tar resten av pensjonen ved 67 år. Dersom du hadde tatt ut ".expr() +
                                                uttaksgrad.format() + " prosent alderspensjon frå ".expr() +
                                                kravDato.format() + ", ville du fått ".expr() +
                                                totalPensjonMedAFP.format() + " kroner årleg i full pensjon når du blir 67 år. I denne berekninga har vi inkludert AFP. ",
                                        English to "We calculate the part you wish to withdraw now and what you would have received if you take the rest of the pension at age 67. If you had withdrawn ".expr() +
                                                uttaksgrad.format() + " percent from ".expr() +
                                                kravDato.format() + ", your full retirement pension is calculated to be NOK ".expr() +
                                                totalPensjonMedAFP.format() + " a year at age 67. This amount includes contractual early retirement pension."


                                    )
                                }
                            }
                        }
                    }
                }
            }
        }


