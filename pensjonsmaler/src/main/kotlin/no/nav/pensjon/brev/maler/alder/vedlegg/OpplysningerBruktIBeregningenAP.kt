package no.nav.pensjon.brev.maler.alder.vedlegg

import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenSelectors.delingstallVedNormertPensjonsalder
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenSelectors.delingstallVedUttak
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenSelectors.pensjonsbeholdning
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenSelectors.sisteOpptjeningsAar
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenSelectors.trygdetid
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenSelectors.uttaksgrad
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenSelectors.virkFom
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr

@TemplateModelHelpers
val opplysningerBruktIBeregningenAP =
    createAttachment(
        title = newText(
            Bokmal to "Opplysninger brukt i beregningen",
            Nynorsk to "Opplysningar brukt i berekninga",
            English to "Information about your calculation"
        ),
    ) {
        title2 {
            textExpr(
                Bokmal to "Opplysninger som ligger til grunn for beregningen per ".expr() + virkFom.format(),
                Nynorsk to "".expr(),
                English to "".expr()
            )
        }

        paragraph {
            table(header = {
                column {
                    text(
                        Bokmal to "Opplysning",
                        Nynorsk to "",
                        English to "",
                    )
                }
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
                        textExpr(
                            Bokmal to trygdetid.format() + " år".expr(),
                            Nynorsk to "".expr(),
                            English to "".expr()
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
                        textExpr(
                            Bokmal to pensjonsbeholdning.format() + " kr".expr(),
                            Nynorsk to "".expr(),
                            English to "".expr()
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
                        textExpr(
                            Bokmal to uttaksgrad.format() + " %",
                            Nynorsk to "".expr(),
                            English to "".expr()
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
                        textExpr(
                            Bokmal to delingstallVedUttak.format(),
                            Nynorsk to "".expr(),
                            English to "".expr()
                        )
                    }
                }
                showIf(uttaksgrad.notEqualTo(100)) {
                    ifNotNull(delingstallVedNormertPensjonsalder) { delingstall ->
                        row {
                            cell {
                                text(
                                    Bokmal to "Delingstall ved 67 år",
                                    Nynorsk to "",
                                    English to ""
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to delingstall.format(),
                                    Nynorsk to "".expr(),
                                    English to "".expr()
                                )
                            }
                        }
                    }
                }
            }
        }

        ifNotNull(sisteOpptjeningsAar) { sisteAar ->
            paragraph {
                textExpr(
                    Bokmal to "Pensjonsopptjening og trygdetid tas med i beregningen av alderspensjon fra og med året etter at skatteoppgjøret er klart. ".expr() +
                            "Dette gjelder selv om skatteoppgjøret ditt er klart tidligere. " +
                            "I beregningen er det derfor brukt pensjonsopptjening til og med " + sisteAar.format() + ".",
                    Nynorsk to "".expr(),
                    English to "".expr()
                )
            }
        }

        paragraph {
            text(
                Bokmal to "På ${Constants.PENSJON_URL} kan du lese mer om regelverket for alderspensjon og hvordan disse tallene har betydning for beregningen. I nettjenesten Din pensjon på ${Constants.DIN_PENSJON_URL} kan du se hvilke inntekter og opplysninger om opptjening som vi har registrert.",
                Nynorsk to "",
                English to ""
            )
        }
    }