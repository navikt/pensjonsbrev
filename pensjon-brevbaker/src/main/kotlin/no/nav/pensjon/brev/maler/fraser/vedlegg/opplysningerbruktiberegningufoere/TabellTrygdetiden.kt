package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.KravAarsakType
import no.nav.pensjon.brev.api.model.vedlegg.BeregnetUTPerManedSelectors.brukerErFlyktning
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
import no.nav.pensjon.brev.template.dsl.expression.lessThan
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.greaterThan


data class TabellTrygdetiden(
    val anvendtTT: Expression<Int>,
    val brukerErFlyktning: Expression<Boolean>,
    val bilateralTrygdetidPerioder: Expression<List<OpplysningerBruktIBeregningUTDto.BilateralTrygdetidPeriode>>,
    val eosTrygdetidPerioder: Expression<List<OpplysningerBruktIBeregningUTDto.EOSTrygdetidPeriode>>,
    val faktiskTTEOS: Expression<Int?>,
    val fastsattNorskTrygdetid: Expression<Int?>,
    val harBoddArbeidUtland: Expression<Boolean>,
    val kravAarsakType: Expression<KravAarsakType>,
    val norskTrygdetidPerioder: Expression<List<OpplysningerBruktIBeregningUTDto.NorskTrygdetidPeriode>>,

    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        ifNotNull(fastsattNorskTrygdetid) { fastsattNorskTrygdetid ->
            // TBU044V
            showIf(
                (fastsattNorskTrygdetid.lessThan(40) and
                        not(brukerErFlyktning) and norskTrygdetidPerioder.notNull()) or
                        (harBoddArbeidUtland and not(brukerErFlyktning) and norskTrygdetidPerioder.notNull())
            ) {
                paragraph {
                    text(
                        Bokmal to "Trygdetiden din i Norge:",
                        Nynorsk to "Trygdetida di i Noreg:",
                        English to "Period of national insurance coverage in Norway:"
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
            }
        }
        ifNotNull(faktiskTTEOS) { faktiskTTEOS ->
            showIf(faktiskTTEOS.greaterThan(0) and eosTrygdetidPerioder.notNull() and anvendtTT.lessThan(40)) {
                paragraph {
                    text(
                        Bokmal to "Trygdetiden din i andre EØS-land er fastsatt på grunnlag av følgende perioder:",
                        Nynorsk to "Trygdetida di i andre EØS-land er fastsett på grunnlag av følgjande periodar:",
                        English to "Your period of national insurance coverage in other EEA countries has been determined on the basis of the following periods of coverage:"
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
        }
        showIf(bilateralTrygdetidPerioder.notNull() and anvendtTT.lessThan(40)) {
            paragraph {
                text(
                    Bokmal to "Trygdetiden din i land som Norge har trygdeavtale med, er fastsatt på grunnlag av følgende perioder:",
                    Nynorsk to "Trygdetida di i land som Noreg har trygdeavtale med, er fastsett på grunnlag av følgjande periodar:",
                    English to "Your period of national insurance coverage in countries with which Norway has a national insurance agreement, has been determined on the basis of the following periods of coverage:"
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



