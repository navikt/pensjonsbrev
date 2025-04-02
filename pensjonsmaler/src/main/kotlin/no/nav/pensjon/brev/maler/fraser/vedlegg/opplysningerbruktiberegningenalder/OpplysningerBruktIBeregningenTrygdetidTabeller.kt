package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningenalder

import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.TrygdetidSelectors.fom
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.TrygdetidSelectors.land
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerBruktIBeregningenAlderDtoSelectors.TrygdetidSelectors.tom
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.size
import no.nav.pensjon.brev.template.dsl.text

object OpplysningerBruktIBeregningenTrygdetidTabeller{
    data class NorskTrygdetid(
        val trygdetidNorge: Expression<List<OpplysningerBruktIBeregningenAlderDto.Trygdetid>>,
    ): OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            showIf(trygdetidNorge.size().greaterThan(0)) {
                paragraph {
                    text(
                        Bokmal to "Tabellen nedenfor viser perioder vi har registrert at du har bodd og/eller arbeidet i Norge. Disse opplysningene er brukt for å fastsette din norske trygdetid.",
                        Nynorsk to "Tabellen nedanfor viser periodar vi har registrert at du har budd og/eller arbeidd i Noreg. Desse opplysningane er brukte for å fastsetje den norske trygdetida di.",
                        English to "The table below shows the time periods when you have been registered as living and/or working in Norway. This information has been used to establish your Norwegian national insurance coverage.",
                    )
                    table(
                        {
                            column {
                                text(
                                    Bokmal to "Fra og med",
                                    Nynorsk to "Frå og med",
                                    English to "Start date"
                                )
                            }
                            column {
                                text(
                                    Bokmal to "Til og med",
                                    Nynorsk to "Til og med",
                                    English to "End date",
                                )
                            }
                        }
                    ) {
                        forEach(trygdetidNorge) { trygedtid ->
                            row {
                                cell {
                                    ifNotNull(trygedtid.fom) {
                                        eval(it.format(short = true))
                                    }
                                }
                                cell {
                                    ifNotNull(trygedtid.tom) {
                                        eval(it.format(short = true))
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
    }

    data class UtenlandskTrygdetid(
        val trygdetid: Expression<List<OpplysningerBruktIBeregningenAlderDto.Trygdetid>>,
    ): OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                table({
                    column { text(Bokmal to "Land", Nynorsk to "Land", English to "Country") }
                    column {
                        text(
                            Bokmal to "Fra og med",
                            Nynorsk to "Frå og med",
                            English to "Start date"
                        )
                    }
                    column {
                        text(
                            Bokmal to "Til og med",
                            Nynorsk to "Til og med",
                            English to "End date",
                        )
                    }
                }) {
                    forEach(trygdetid) { trygdetid ->
                        row {
                            cell {
                                ifNotNull(trygdetid.land) {
                                    eval(it)
                                }
                            }
                            cell {
                                ifNotNull(trygdetid.fom) {
                                    eval(it.format(short = true))
                                }
                            }
                            cell {
                                ifNotNull(trygdetid.tom) {
                                    eval(it.format(short = true))
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
