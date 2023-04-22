package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.vedlegg.NorskTrygdetidPeriodeSelectors.trygdetidFom
import no.nav.pensjon.brev.api.model.vedlegg.NorskTrygdetidPeriodeSelectors.trygdetidTom
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.api.model.vedlegg.UtenlandskTrygdetidBilateralPeriodeSelectors.trygdetidBilateralLand
import no.nav.pensjon.brev.api.model.vedlegg.UtenlandskTrygdetidBilateralPeriodeSelectors.trygdetidFom
import no.nav.pensjon.brev.api.model.vedlegg.UtenlandskTrygdetidBilateralPeriodeSelectors.trygdetidTom
import no.nav.pensjon.brev.api.model.vedlegg.UtenlandskTrygdetidEOSPeriodeSelectors.trygdetidEOSLand
import no.nav.pensjon.brev.api.model.vedlegg.UtenlandskTrygdetidEOSPeriodeSelectors.trygdetidFom
import no.nav.pensjon.brev.api.model.vedlegg.UtenlandskTrygdetidEOSPeriodeSelectors.trygdetidTom
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brev.template.dsl.expression.*

data class TabellTrygdetiden(
    val norskTrygdetidPerioder: Expression<List<OpplysningerBruktIBeregningUTDto.NorskTrygdetidPeriode>>,
    val utenlandskTrygdetidBilateralPerioder: Expression<List<OpplysningerBruktIBeregningUTDto.UtenlandskTrygdetidBilateralPeriode>>,
    val utenlandskTrygdetidEOSPerioder: Expression<List<OpplysningerBruktIBeregningUTDto.UtenlandskTrygdetidEOSPeriode>>,

    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {

        // TBU044V
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
                    utenlandskTrygdetidEOSPerioder
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
                    utenlandskTrygdetidBilateralPerioder
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



