package no.nav.pensjon.brev.maler.alder.vedlegg

import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenSelectors.delingstallVedNormertPensjonsalder
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenSelectors.delingstallVedUttak
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenSelectors.pensjonsbeholdning
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenSelectors.redusertTrygdetid
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenSelectors.sisteOpptjeningsAar
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenSelectors.trygdeperioderNorge
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenSelectors.trygdeperioderUtland
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenSelectors.trygdetid
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenSelectors.uttaksgrad
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenSelectors.virkFom
import no.nav.pensjon.brev.api.model.maler.alderApi.TrygdeperiodeNorgeSelectors.fom
import no.nav.pensjon.brev.api.model.maler.alderApi.TrygdeperiodeNorgeSelectors.tom
import no.nav.pensjon.brev.api.model.maler.alderApi.TrygdeperiodeUtlandSelectors.fom
import no.nav.pensjon.brev.api.model.maler.alderApi.TrygdeperiodeUtlandSelectors.land
import no.nav.pensjon.brev.api.model.maler.alderApi.TrygdeperiodeUtlandSelectors.tom
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.*
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
                column(alignment = RIGHT) {}
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

        showIf(redusertTrygdetid) {
            paragraph {
                text(
                    Bokmal to "Trygdetid baserer seg på perioder du har bodd og/eller arbeidet i Norge, og har betydning for beregning avpensjonen din. Full trygdetid er 40 år.",
                    Nynorsk to "Trygdetid baserer seg på periodar du har budd og/eller arbeidd i Noreg, og har betydning for berekning avpensjonen din. Full trygdetid er 40 år.",
                    English to "The period of national insurance coverage is based on periods you have lived and/or worked in Norway, and these years affect pension eligibility. Full pension eligibility is 40 years."
                )
            }

            paragraph {
                text(
                    Bokmal to "Tabellen nedenfor viser perioder vi har registrert at du har bodd og/eller arbeidet i Norge. Disse opplysningene er brukt for å fastsette din norske trygdetid.",
                    Nynorsk to "Tabellen nedanfor viser periodar vi har registrertat du har budd og/eller arbeidd i Noreg. Desse opplysningane er brukte for å fastsetje den norske trygdetida di.",
                    English to "The table below shows the time periods when you have been registered as living and/or working in Norway. This information has been used to establish your Norwegian national insurance coverage."
                )
            }
            paragraph {
                table(header = {
                    column {
                        text(
                            Bokmal to "Fra og med",
                            Nynorsk to "Frå og med",
                            English to "Start date",
                        )
                    }
                    column(alignment = RIGHT) {
                        text(
                            Bokmal to "Til og med",
                            Nynorsk to "Til og med",
                            English to "End date"
                        )
                    }
                }) {
                    forEach(trygdeperioderNorge) { periode ->
                        row {
                            cell {
                                textExpr(
                                    Bokmal to periode.fom.format(short = true),
                                    Nynorsk to periode.fom.format(short = true),
                                    English to periode.fom.format(short = true)
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to periode.tom.format(short = true),
                                    Nynorsk to periode.tom.format(short = true),
                                    English to periode.tom.format(short = true)
                                )
                            }
                        }
                    }
                }
            }

            showIf(trygdeperioderUtland.isNotEmpty()) {
                paragraph {
                    text(
                        Bokmal to "Tabellen nedenfor viser perioder du har bodd og/eller arbeidet i land som Norge har trygdeavtale med. Disse periodene er brukt i vurderingen av retten til alderspensjon før fylte 67 år.",
                        Nynorsk to "Tabellen nedanfor viser periodar du har budd og/eller arbeidd i land som Noreg har trygdeavtale med. Desse periodane er brukt i vurderinga av retten til alderspensjon før fylte 67 år.",
                        English to "The table below shows your insurance coverage in countries with which Norway has a social security agreement. These periods has been used to assess whether you are eligible for retirement pension before the age of 67.",
                    )
                }

                paragraph {
                    table(header = {
                        column {
                            text(
                                Bokmal to "Land",
                                Nynorsk to "Land",
                                English to "Country"
                            )
                        }
                        column(alignment = RIGHT) {
                            text(
                                Bokmal to "Fra og med",
                                Nynorsk to "Frå og med",
                                English to "Start date",
                            )
                        }
                        column(alignment = RIGHT) {
                            text(
                                Bokmal to "Til og med",
                                Nynorsk to "Til og med",
                                English to "End date"
                            )
                        }
                    }) {
                        forEach(trygdeperioderUtland) { utlandPeriode ->
                            row {
                                cell {
                                    textExpr(
                                        Bokmal to utlandPeriode.land,
                                        Nynorsk to utlandPeriode.land,
                                        English to utlandPeriode.land
                                    )
                                }
                                cell {
                                    textExpr(
                                        Bokmal to utlandPeriode.fom.format(short = true),
                                        Nynorsk to utlandPeriode.fom.format(short = true),
                                        English to utlandPeriode.fom.format(short = true)
                                    )
                                }
                                cell {
                                    textExpr(
                                        Bokmal to utlandPeriode.tom.format(short = true),
                                        Nynorsk to utlandPeriode.tom.format(short = true),
                                        English to utlandPeriode.tom.format(short = true)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }