package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.vedlegg.NorskTrygdetidAvdoedSelectors.trygdetidFomAvdoed
import no.nav.pensjon.brev.api.model.vedlegg.NorskTrygdetidAvdoedSelectors.trygdetidTomAvdoed
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningUTDto
import no.nav.pensjon.brev.api.model.vedlegg.UtenlandskTrygdetidBilateralAvdoedSelectors.trygdetidBilateralLandAvdoed
import no.nav.pensjon.brev.api.model.vedlegg.UtenlandskTrygdetidBilateralAvdoedSelectors.trygdetidFomAvdoed
import no.nav.pensjon.brev.api.model.vedlegg.UtenlandskTrygdetidBilateralAvdoedSelectors.trygdetidTomAvdoed
import no.nav.pensjon.brev.api.model.vedlegg.UtenlandskTrygdetidEOSAvdoedSelectors.trygdetidEOSLandAvdoed
import no.nav.pensjon.brev.api.model.vedlegg.UtenlandskTrygdetidEOSAvdoedSelectors.trygdetidFomAvdoed
import no.nav.pensjon.brev.api.model.vedlegg.UtenlandskTrygdetidEOSAvdoedSelectors.trygdetidTomAvdoed
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brev.template.dsl.expression.*

data class TabellTrygdetidenAvdoed(
    val norskTrygdetidAvdoed: Expression<List<OpplysningerBruktIBeregningUTDto.NorskTrygdetidAvdoed>>,
    val utenlandskTrygdetidBilateralAvdoed: Expression<List<OpplysningerBruktIBeregningUTDto.UtenlandskTrygdetidBilateralAvdoed>>,
    val utenlandskTrygdetidEOSAvdoed: Expression<List<OpplysningerBruktIBeregningUTDto.UtenlandskTrygdetidEOSAvdoed>>,

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
                    norskTrygdetidAvdoed
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
                    utenlandskTrygdetidEOSAvdoed
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
                    utenlandskTrygdetidBilateralAvdoed
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



