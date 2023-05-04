package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.vedlegg.BilateralTrygdetidPeriodeSelectors.trygdetidBilateralLand
import no.nav.pensjon.brev.api.model.vedlegg.BilateralTrygdetidPeriodeSelectors.trygdetidFom
import no.nav.pensjon.brev.api.model.vedlegg.BilateralTrygdetidPeriodeSelectors.trygdetidTom
import no.nav.pensjon.brev.api.model.vedlegg.EOSTrygdetidPeriodeSelectors.trygdetidEOSLand
import no.nav.pensjon.brev.api.model.vedlegg.EOSTrygdetidPeriodeSelectors.trygdetidFom
import no.nav.pensjon.brev.api.model.vedlegg.EOSTrygdetidPeriodeSelectors.trygdetidTom
import no.nav.pensjon.brev.api.model.vedlegg.NorskTrygdetidPeriodeSelectors.trygdetidFom
import no.nav.pensjon.brev.api.model.vedlegg.NorskTrygdetidPeriodeSelectors.trygdetidTom
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brev.template.dsl.expression.*

data class TabellTrygdetidenAvdoed(
    val norskTrygdetidPerioder: Expression<List<OpplysningerBruktIBeregningUTDto.OpplysningerBruktIBeregningUTAvdoed>>,
    val bilateralTrygdetidPerioder: Expression<List<OpplysningerBruktIBeregningUTDto.OpplysningerBruktIBeregningUTAvdoed>>,
    val eosTrygdetidPerioder: Expression<List<OpplysningerBruktIBeregningUTDto.EOSTrygdetidPeriode>>,
    val fastsattNorskTrygdetid: Expression<Int?>,
    val harBoddArbeidUtland: Expression<Boolean>,
    val brukerErFlyktning: Expression<Boolean>,

    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {

        // TBU044V
        paragraph {
            text(
                Bokmal to "Den faktiske norske trygdetiden til avdøde er fastsatt ut fra følgende perioder:",
                Nynorsk to "Den faktiske norske trygdetida til den avdøde er fastsett ut frå følgjande periodar:",
                English to "The actual periodeof Norwegian national insurance coverage for the deceased has been established on the basis of the following periods:"
            )
        }

        paragraph {
            table(header = {
                column {
                    text(
                        Bokmal to "Fra og med", Nynorsk to "Frå og med", English to "From and including"
                    )
                }
                column {
                    text(
                        Bokmal to "Til og med", Nynorsk to "Til og med", English to "To and including"
                    )
                }
            }) {
                forEach(
                    norskTrygdetidPerioder
                ) { periode ->
                    row {
                        cell {
                            textExpr(
                                Bokmal to periode.trygdetidFom.format(),
                                Nynorsk to periode.trygdetidFom.format(),
                                English to periode.trygdetidFom.format()
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to periode.trygdetidTom.format(),
                                Nynorsk to periode.trygdetidTom.format(),
                                English to periode.trygdetidTom.format()
                            )
                        }
                    }
                }
            }
        }
        showIf(eosTrygdetidPerioder.isNotEmpty()) {
            paragraph {
                text(
                    Bokmal to "Trygdetiden til avdøde i andre EØS-land er fastsatt ut fra følgende perioder:",
                    Nynorsk to "Trygdetida til den avdøde i andre EØS-land er fastsett ut frå følgjande periodar:",
                    English to "The period of national insurance coverage for the deceased in other EEA countries has been establsihed on the basis of the following periods:"
                )
            }
            // TBU045V
            paragraph {
                table(header = {
                    column {
                        text(Bokmal to "Land", Nynorsk to "Land", English to "Country")
                    }
                    column {
                        text(Bokmal to "Fra og med", Nynorsk to "Frå og med", English to "From and including")
                    }
                    column {
                        text(Bokmal to "Til og med", Nynorsk to "Til og med", English to "To and including")
                    }
                }) {
                    forEach(
                        eosTrygdetidPerioder
                    ) { periode ->
                        row {
                            cell {
                                textExpr(
                                    Bokmal to periode.trygdetidEOSLand,
                                    Nynorsk to periode.trygdetidEOSLand,
                                    English to periode.trygdetidEOSLand,
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to periode.trygdetidFom.format(),
                                    Nynorsk to periode.trygdetidFom.format(),
                                    English to periode.trygdetidFom.format()
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to periode.trygdetidTom.format(),
                                    Nynorsk to periode.trygdetidTom.format(),
                                    English to periode.trygdetidTom.format()
                                )
                            }
                        }
                    }
                }
            }
        }

        showIf(bilateralTrygdetidPerioder.isNotEmpty()) {
            paragraph {
                text(
                    Bokmal to "Trygdetiden til avdøde i land som Norge har trygdeavtale med, er fastsatt ut fra følgende perioder:",
                    Nynorsk to "Trygdetida til den avdøde i land som Noreg har trygdeavtale med, er fastsett ut frå følgjande periodar:",
                    English to "The period of national insurance coverage for the deceased in countries with which Norway has a national insurance agreement, has been established on the basis of the following periods:"
                )
            }
            // TBU046V
            paragraph {
                table(header = {
                    column {
                        text(Bokmal to "Land", Nynorsk to "Land", English to "Country")
                    }
                    column {
                        text(Bokmal to "Fra og med", Nynorsk to "Frå og med", English to "From and including")
                    }
                    column {
                        text(Bokmal to "Til og med", Nynorsk to "Til og med", English to "To and including")
                    }
                }) {
                    forEach(
                        bilateralTrygdetidPerioder
                    ) { periode ->
                        row {
                            cell {
                                textExpr(
                                    Bokmal to periode.trygdetidBilateralLand,
                                    Nynorsk to periode.trygdetidBilateralLand,
                                    English to periode.trygdetidBilateralLand,
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to periode.trygdetidFom.format(),
                                    Nynorsk to periode.trygdetidFom.format(),
                                    English to periode.trygdetidFom.format()
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to periode.trygdetidTom.format(),
                                    Nynorsk to periode.trygdetidTom.format(),
                                    English to periode.trygdetidTom.format()
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}




