package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.vedlegg.NorskTrygdetidAvdoedPeriodeSelectors.trygdetidFomAvdoed
import no.nav.pensjon.brev.api.model.vedlegg.NorskTrygdetidAvdoedPeriodeSelectors.trygdetidTomAvdoed
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.api.model.vedlegg.UtenlandskTrygdetidBilateralAvdoedPeriodeSelectors.trygdetidBilateralLandAvdoed
import no.nav.pensjon.brev.api.model.vedlegg.UtenlandskTrygdetidBilateralAvdoedPeriodeSelectors.trygdetidFomAvdoed
import no.nav.pensjon.brev.api.model.vedlegg.UtenlandskTrygdetidBilateralAvdoedPeriodeSelectors.trygdetidTomAvdoed
import no.nav.pensjon.brev.api.model.vedlegg.UtenlandskTrygdetidEOSAvdoedPeriodeSelectors.trygdetidEOSLandAvdoed
import no.nav.pensjon.brev.api.model.vedlegg.UtenlandskTrygdetidEOSAvdoedPeriodeSelectors.trygdetidFomAvdoed
import no.nav.pensjon.brev.api.model.vedlegg.UtenlandskTrygdetidEOSAvdoedPeriodeSelectors.trygdetidTomAvdoed
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brev.template.dsl.expression.*

data class TabellTrygdetidenAvdoed(
    val norskTrygdetidAvdoedPeriode: Expression<List<OpplysningerBruktIBeregningUTDto.NorskTrygdetidAvdoedPeriode>>,
    val utenlandskTrygdetidBilateralAvdoedPeriode: Expression<List<OpplysningerBruktIBeregningUTDto.UtenlandskTrygdetidBilateralAvdoedPeriode>>,
    val utenlandskTrygdetidEOSAvdoedPeriode: Expression<List<OpplysningerBruktIBeregningUTDto.UtenlandskTrygdetidEOSAvdoedPeriode>>,

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
                    norskTrygdetidAvdoedPeriode
                ) { periode ->
                    row {
                        cell {
                            textExpr(
                                Bokmal to periode.trygdetidFomAvdoed.format(),
                                Nynorsk to periode.trygdetidFomAvdoed.format(),
                                English to periode.trygdetidFomAvdoed.format()
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to periode.trygdetidTomAvdoed.format(),
                                Nynorsk to periode.trygdetidTomAvdoed.format(),
                                English to periode.trygdetidTomAvdoed.format()
                            )
                        }
                    }
                }
            }
        }

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
                    utenlandskTrygdetidEOSAvdoedPeriode
                ) { periode ->
                    row {
                        cell {
                            textExpr(
                                Bokmal to periode.trygdetidEOSLandAvdoed,
                                Nynorsk to periode.trygdetidEOSLandAvdoed,
                                English to periode.trygdetidEOSLandAvdoed,
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to periode.trygdetidFomAvdoed.format(),
                                Nynorsk to periode.trygdetidFomAvdoed.format(),
                                English to periode.trygdetidFomAvdoed.format()
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to periode.trygdetidTomAvdoed.format(),
                                Nynorsk to periode.trygdetidTomAvdoed.format(),
                                English to periode.trygdetidTomAvdoed.format()
                            )
                        }
                    }
                }
            }
        }

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
                    utenlandskTrygdetidBilateralAvdoedPeriode
                ) { periode ->
                    row {
                        cell {
                            textExpr(
                                Bokmal to periode.trygdetidBilateralLandAvdoed,
                                Nynorsk to periode.trygdetidBilateralLandAvdoed,
                                English to periode.trygdetidBilateralLandAvdoed,
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to periode.trygdetidFomAvdoed.format(),
                                Nynorsk to periode.trygdetidFomAvdoed.format(),
                                English to periode.trygdetidFomAvdoed.format()
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to periode.trygdetidTomAvdoed.format(),
                                Nynorsk to periode.trygdetidTomAvdoed.format(),
                                English to periode.trygdetidTomAvdoed.format()
                            )
                        }
                    }
                }
            }
        }
    }
}



