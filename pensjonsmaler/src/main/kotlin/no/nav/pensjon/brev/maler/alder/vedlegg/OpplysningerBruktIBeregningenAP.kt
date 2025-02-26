package no.nav.pensjon.brev.maler.alder.vedlegg

import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenAP
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text

@TemplateModelHelpers
val opplysningerBruktIBeregningenAP =
    createAttachment<LangBokmalNynorskEnglish, OpplysningerBruktIBeregningenAP>(
        title = newText(
            Bokmal to "Opplysninger brukt i beregningen",
            Nynorsk to "",
            English to ""
        ),
        includeSakspart = false,
    ) {
        title2 {
            text(
                Bokmal to "Opplysninger som ligger til grunn for beregningen per <krav.virkDatoFom>",
                Nynorsk to "",
                English to ""
            )
        }
        paragraph {
            table(header = {
                column {}
                column {}
            }) {
                row {
                    cell {
                        text(
                            Bokmal to "Trygdetid",
                            Nynorsk to "",
                            English to ""
                        )
                    }
                    cell {
                        text(
                            Bokmal to "<simulertBeregningKap20VedVirk.trygdetid> år",
                            Nynorsk to "",
                            English to ""
                        )
                    }
                }
                row {
                    cell {
                        text(
                            Bokmal to "Pensjonsbeholdning",
                            Nynorsk to "",
                            English to ""
                        )
                    }
                    cell {
                        text(
                            Bokmal to "<simulertBeregningKap20VedVirk.beholdning> kr",
                            Nynorsk to "",
                            English to ""
                        )
                    }
                }
                row {
                    cell {
                        text(
                            Bokmal to "Ønsket uttaksgrad",
                            Nynorsk to "",
                            English to ""
                        )
                    }
                    cell {
                        text(
                            Bokmal to "<simulertAlderspensjonVedVirk.uttaksgrad> %",
                            Nynorsk to "",
                            English to ""
                        )
                    }
                }
                row {
                    cell {
                        text(
                            Bokmal to "Delingstall ved uttak",
                            Nynorsk to "",
                            English to ""
                        )
                    }
                    cell {
                        text(
                            Bokmal to "<simulertBeregningKap20VedVirk.delingstallLevealder>",
                            Nynorsk to "",
                            English to ""
                        )
                    }
                }
                showIf(uttaksgrad.notEqualTo(100)) {
                    row {
                        cell {
                            text(
                                Bokmal to "Delingstall ved 67 år",
                                Nynorsk to "",
                                English to ""
                            )
                        }
                        cell {
                            text(
                                Bokmal to "<simulertBeregningKap20VedVirk.delingstallVed67>",
                                Nynorsk to "",
                                English to ""
                            )
                        }
                    }
                }
            }
        }

        paragraph {
            text(
                Bokmal to "Pensjonsopptjening og trygdetid tas med i beregningen av alderspensjon fra og med året etter at skatteoppgjøret er klart. Dette gjelder selv om skatteoppgjøret ditt er klart tidligere. I beregningen er det derfor brukt pensjonsopptjening til og med <vedtak.sisteOpptjeningsAr >.",
                Nynorsk to "",
                English to ""
            )
        }

        paragraph {
            text(
                Bokmal to "På nav.no/pensjon kan du lese mer om regelverket for alderspensjon og hvordan disse tallene har betydning for beregningen. I nettjenesten Din pensjon på nav.no/dinpensjon kan du se hvilke inntekter og opplysninger om opptjening som vi har registrert.<",
                Nynorsk to "",
                English to ""
            )
        }
    }