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
            English to "Information used in the calculation"
        ),
    ) {
        title2 {
            textExpr(
                Bokmal to "Opplysninger som ligger til grunn for beregningen per ".expr() + virkFom.format(),
                Nynorsk to "Opplysningar som ligg til grunn for berekninga per ".expr() + virkFom.format(),
                English to "Information that provides the basis for the calculation as of ".expr() + virkFom.format()
            )
        }

        paragraph {
            table(header = {
                column {
                    text(
                        Bokmal to "Opplysning",
                        Nynorsk to "Opplysning",
                        English to "Information",
                    )
                }
                column {}
            }) {
                row {
                    cell {
                        text(
                            Bokmal to "Trygdetid",
                            Nynorsk to "Trygdetid",
                            English to "National insurance coverage"
                        )
                    }
                    cell {
                        textExpr(
                            Bokmal to trygdetid.format() + " år".expr(),
                            Nynorsk to trygdetid.format() + " år".expr(),
                            English to trygdetid.format() + " years".expr()
                        )
                    }
                }
                row {
                    cell {
                        text(
                            Bokmal to "Pensjonsbeholdning",
                            Nynorsk to "Pensjonsbeholdning",
                            English to "Accumulated pension capital"
                        )
                    }
                    cell {
                        textExpr(
                            Bokmal to pensjonsbeholdning.format() + " kr".expr(),
                            Nynorsk to pensjonsbeholdning.format() + " kr".expr(),
                            English to pensjonsbeholdning.format() + " NOK".expr()
                        )
                    }
                }
                row {
                    cell {
                        text(
                            Bokmal to "Ønsket uttaksgrad",
                            Nynorsk to "Ønska uttaksgrad",
                            English to "Pension level applied for"
                        )
                    }
                    cell {
                        textExpr(
                            Bokmal to uttaksgrad.format() + " %",
                            Nynorsk to uttaksgrad.format() + " %",
                            English to uttaksgrad.format() + " %"
                        )
                    }
                }
                row {
                    cell {
                        text(
                            Bokmal to "Delingstall ved uttak",
                            Nynorsk to "Delingstal ved levealdersjustering",
                            English to "Ratio for life expectancy adjustment"
                        )
                    }
                    cell {
                        textExpr(
                            Bokmal to delingstallVedUttak.format(),
                            Nynorsk to delingstallVedUttak.format(),
                            English to delingstallVedUttak.format()
                        )
                    }
                }
                showIf(uttaksgrad.notEqualTo(100)) {
                    ifNotNull(delingstallVedNormertPensjonsalder) { delingstall ->
                        row {
                            cell {
                                text(
                                    Bokmal to "Delingstall ved 67 år",
                                    Nynorsk to "Delingstal ved 67 år",
                                    English to "Ratio for life expectancy adjustment at age 67"
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to delingstall.format(),
                                    Nynorsk to delingstall.format(),
                                    English to delingstall.format()
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
                    Nynorsk to "Pensjonsopptening og trygdetid blir tatt med i berekninga av alderspensjon frå og med året etter at skatteoppgjeret er klart. ".expr() +
                            "Dette gjeld sjølv om skatteoppgjeret ditt er klart tidlegare. " +
                            "I berekninga er det derfor brukt pensjonsopptening til og med " + sisteAar.format() + ".",
                    English to "Pension accrual and periods of National Insurance Scheme coverage are included in the calculation of retirement pension from the year after the tax settlement is ready. ".expr() +
                            "This applies even if your tax settlement is ready earlier. " +
                            "Therefore, the calculation considers pension accrual up to and including " + sisteAar.format() + "."
                )
            }
        }

        paragraph {
            text(
                Bokmal to "På ${Constants.PENSJON_URL} kan du lese mer om regelverket for alderspensjon og hvordan disse tallene har betydning for beregningen. I nettjenesten Din pensjon på ${Constants.DIN_PENSJON_URL} kan du se hvilke inntekter og opplysninger om opptjening som vi har registrert.",
                Nynorsk to "På ${Constants.PENSJON_URL} kan du lese meir om regelverket for alderspensjon og kva desse tala har å seie for berekninga. I nettenesta Din pensjon på ${Constants.DIN_PENSJON_URL} kan du sjå kva inntekter og opplysningar om opptening som vi har registrert.",
                English to "Go to ${Constants.PENSJON_URL} to read more about these regulations that apply to retirement pension and how these affect your calculation. Logon to our online service \"Din pensjon\" at ${Constants.DIN_PENSJON_URL} to see your income and accumulated pension capital."
            )
        }
    }