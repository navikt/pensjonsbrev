package no.nav.pensjon.brev.maler.alder.vedlegg

import brev.felles.Constants
import brev.felles.aarOgMaanederFormattert
import brev.vedlegg.opplysningerbruktiberegningen.DelingstallVed67Aar
import no.nav.pensjon.brev.model.alder.avslag.OpplysningerBruktIBeregningenKap20Selectors.redusertTrygdetidKap20
import no.nav.pensjon.brev.model.alder.avslag.OpplysningerBruktIBeregningenSelectors.delingstallVedNormertPensjonsalder
import no.nav.pensjon.brev.model.alder.avslag.OpplysningerBruktIBeregningenSelectors.delingstallVedUttak
import no.nav.pensjon.brev.model.alder.avslag.OpplysningerBruktIBeregningenSelectors.kravAarsak
import no.nav.pensjon.brev.model.alder.avslag.OpplysningerBruktIBeregningenSelectors.normertPensjonsalder
import no.nav.pensjon.brev.model.alder.avslag.OpplysningerBruktIBeregningenSelectors.opplysningerKap20
import no.nav.pensjon.brev.model.alder.avslag.OpplysningerBruktIBeregningenSelectors.pensjonsbeholdning
import no.nav.pensjon.brev.model.alder.avslag.OpplysningerBruktIBeregningenSelectors.sisteOpptjeningsAar
import no.nav.pensjon.brev.model.alder.avslag.OpplysningerBruktIBeregningenSelectors.trygdeperioderNorge
import no.nav.pensjon.brev.model.alder.avslag.OpplysningerBruktIBeregningenSelectors.trygdeperioderUtland
import no.nav.pensjon.brev.model.alder.avslag.OpplysningerBruktIBeregningenSelectors.trygdetid
import no.nav.pensjon.brev.model.alder.avslag.OpplysningerBruktIBeregningenSelectors.uttaksgrad
import no.nav.pensjon.brev.model.alder.avslag.OpplysningerBruktIBeregningenSelectors.virkFom
import no.nav.pensjon.brev.model.alder.avslag.TrygdeperiodeNorgeSelectors.fom
import no.nav.pensjon.brev.model.alder.avslag.TrygdeperiodeNorgeSelectors.tom
import no.nav.pensjon.brev.model.alder.avslag.TrygdeperiodeUtlandSelectors.fom
import no.nav.pensjon.brev.model.alder.avslag.TrygdeperiodeUtlandSelectors.land
import no.nav.pensjon.brev.model.alder.avslag.TrygdeperiodeUtlandSelectors.tom
import no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningenalder.OpplysningerBruktIBeregningenTrygdetidTabeller
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text

val opplysningerBruktIBeregningenAP2025Vedlegg =
    createAttachment(
        title = newText(
            Bokmal to "Opplysninger brukt i beregningen",
            Nynorsk to "Opplysningar brukt i berekninga",
            English to "Information used in the calculation"
        ),
    ) {
        title2 {
            text(
                bokmal { + "Opplysninger som ligger til grunn for beregningen per " + virkFom.format() },
                nynorsk { + "Opplysningar som ligg til grunn for berekninga per " + virkFom.format() },
                english { + "Information that provides the basis for the calculation as of " + virkFom.format() }
            )
        }

        paragraph {
            table(header = {
                column {
                    text(
                        bokmal { + "Opplysning" },
                        nynorsk { + "Opplysning" },
                        english { + "Information" },
                    )
                }
                column(alignment = RIGHT) {}
            }) {
                row {
                    cell {
                        text(
                            bokmal { + "Trygdetid" },
                            nynorsk { + "Trygdetid" },
                            english { + "National insurance coverage" }
                        )
                    }
                    cell {
                        text(
                            bokmal { + trygdetid.format() + " år" },
                            nynorsk { + trygdetid.format() + " år" },
                            english { + trygdetid.format() + " years" }
                        )
                    }
                }
                row {
                    cell {
                        text(
                            bokmal { + "Pensjonsbeholdning" },
                            nynorsk { + "Pensjonsbehaldning" },
                            english { + "Accumulated pension capital" }
                        )
                    }
                    cell {
                        text(
                            bokmal { + pensjonsbeholdning.format(false) + " kr" },
                            nynorsk { + pensjonsbeholdning.format(false) + " kr" },
                            english { + pensjonsbeholdning.format(false) + " NOK" }
                        )
                    }
                }
                row {
                    cell {
                        text(
                            bokmal { + "Ønsket uttaksgrad" },
                            nynorsk { + "Ønska uttaksgrad" },
                            english { + "Pension level applied for" }
                        )
                    }
                    cell {
                        text(
                            bokmal { + uttaksgrad.format() + " %" },
                            nynorsk { + uttaksgrad.format() + " %" },
                            english { + uttaksgrad.format() + " %" }
                        )
                    }
                }
                row {
                    cell {
                        text(
                            bokmal { + "Delingstall ved uttak" },
                            nynorsk { + "Delingstal ved uttak" },
                            english { + "Ratio for life expectancy adjustment" }
                        )
                    }
                    cell {
                        text(
                            bokmal { + delingstallVedUttak.format() },
                            nynorsk { + delingstallVedUttak.format() },
                            english { + delingstallVedUttak.format() }
                        )
                    }
                }
                showIf(uttaksgrad.notEqualTo(100)) {
                    ifNotNull(delingstallVedNormertPensjonsalder) { delingstall ->
                        row {
                            cell {includePhrase(DelingstallVed67Aar)}
                            cell {
                                text(
                                    bokmal { + delingstall.format() },
                                    nynorsk { + delingstall.format() },
                                    english { + delingstall.format() }
                                )
                            }
                        }
                    }
                }
            }
        }

        showIf(kravAarsak.notEqualTo("UTTAKSGRAD")) {
            ifNotNull(sisteOpptjeningsAar) { sisteAar ->
                paragraph {
                    text(
                        bokmal { + "Pensjonsopptjening og trygdetid tas med i beregningen av alderspensjon fra og med året etter at skatteoppgjøret er klart. " +
                                "Dette gjelder selv om skatteoppgjøret ditt er klart tidligere. " +
                                "I beregningen er det derfor brukt pensjonsopptjening til og med " + sisteAar.format() + "." },
                        nynorsk { + "Pensjonsopptening og trygdetid blir tatt med i berekninga av alderspensjon frå og med året etter at skatteoppgjeret er klart. " +
                                "Dette gjeld sjølv om skatteoppgjeret ditt er klart tidlegare. " +
                                "I berekninga er det derfor brukt pensjonsopptening til og med " + sisteAar.format() + "." },
                        english { + "Pension accrual and periods of National Insurance Scheme coverage are included in the calculation of retirement pension from the year after the tax settlement is ready. " +
                                "This applies even if your tax settlement is ready earlier. " +
                                "Therefore, the calculation considers pension accrual up to and including " + sisteAar.format() + "." }
                    )
                }
            }
        }

        showIf(kravAarsak.equalTo("UTTAKSGRAD")) {
            ifNotNull(sisteOpptjeningsAar) { sisteAar ->
                paragraph {
                    text(
                        bokmal { + "Pensjonsbeholdningen i tabellen er beregnet ut fra den pensjonen du har tatt ut og pensjonsbeholdningen din." },
                        nynorsk { + "Pensjonsbehaldninga i tabellen er berekna ut frå den pensjonen du har tatt ut og pensjonsbehaldninga di." },
                        english { + "The accumulated pension capital is calculated from your current pension and your remaining pension capital." }
                    )
                }
            }
        }

        paragraph {
            text(
                bokmal { + "På ${Constants.PENSJON_URL} kan du lese mer om regelverket for alderspensjon og hvordan disse tallene har betydning for beregningen. Logg inn på ${Constants.DIN_PENSJON_URL} for å se hvilke inntekter og opplysninger om opptjening vi har registrert." },
                nynorsk { + "På ${Constants.PENSJON_URL} kan du lese meir om regelverket for alderspensjon og kva desse tala har å seie for berekninga. Logg inn på ${Constants.DIN_PENSJON_URL} for å sjå kva inntekter og opplysningar om opptening vi har registrert." },
                english { + "Go to ${Constants.PENSJON_URL} to read more about these regulations that apply to retirement pension and how these affect your calculation. Log in to ${Constants.DIN_PENSJON_URL} to see your income and accumulated pension capital." }
            )
        }

        showIf(opplysningerKap20.redusertTrygdetidKap20) {
            paragraph {
                text(
                    bokmal { + "Trygdetid baserer seg på perioder du har bodd og/eller arbeidet i Norge, og har betydning for beregning av pensjonen din. Full trygdetid er 40 år." },
                    nynorsk { + "Trygdetid baserer seg på periodar du har budd og/eller arbeidd i Noreg, og har betydning for berekning av pensjonen din. Full trygdetid er 40 år." },
                    english { + "The period of national insurance coverage is based on periods you have lived and/or worked in Norway, and these years affect pension eligibility. Full pension eligibility is 40 years." }
                )
            }

            includePhrase(OpplysningerBruktIBeregningenTrygdetidTabeller.NorskTrygdetidInnledning)

            paragraph {
                table(header = {
                    column {
                        text(
                            bokmal { + "Fra og med" },
                            nynorsk { + "Frå og med" },
                            english { + "Start date" },
                        )
                    }
                    column(alignment = RIGHT) {
                        text(
                            bokmal { + "Til og med" },
                            nynorsk { + "Til og med" },
                            english { + "End date" }
                        )
                    }
                }) {
                    forEach(trygdeperioderNorge) { periode ->
                        row {
                            cell {
                                text(
                                    bokmal { + periode.fom.format(short = true) },
                                    nynorsk { + periode.fom.format(short = true) },
                                    english { + periode.fom.format(short = true) }
                                )
                            }
                            cell {
                                text(
                                    bokmal { + periode.tom.format(short = true) },
                                    nynorsk { + periode.tom.format(short = true) },
                                    english { + periode.tom.format(short = true) }
                                )
                            }
                        }
                    }
                }
            }

            showIf(trygdeperioderUtland.isNotEmpty()) {
                paragraph {
                    text(
                        bokmal { + "Tabellen nedenfor viser perioder du har bodd og/eller arbeidet i land som Norge har trygdeavtale med. Disse periodene er brukt i vurderingen av retten til alderspensjon før du blir " + normertPensjonsalder.aarOgMaanederFormattert() + "." },
                        nynorsk { + "Tabellen nedanfor viser periodar du har budd og/eller arbeidd i land som Noreg har trygdeavtale med. Desse periodane er brukt i vurderinga av retten til alderspensjon før du blir " + normertPensjonsalder.aarOgMaanederFormattert() + "." },
                        english { + "The table below shows your insurance coverage in countries with which Norway has a social security agreement. These periods has been used to assess whether you are eligible for retirement pension before the age of " + normertPensjonsalder.aarOgMaanederFormattert() + "." },
                    )
                }

                paragraph {
                    table(header = {
                        column {
                            text(
                                bokmal { + "Land" },
                                nynorsk { + "Land" },
                                english { + "Country" }
                            )
                        }
                        column(alignment = RIGHT) {
                            text(
                                bokmal { + "Fra og med" },
                                nynorsk { + "Frå og med" },
                                english { + "Start date" },
                            )
                        }
                        column(alignment = RIGHT) {
                            text(
                                bokmal { + "Til og med" },
                                nynorsk { + "Til og med" },
                                english { + "End date" }
                            )
                        }
                    }) {
                        forEach(trygdeperioderUtland) { utlandPeriode ->
                            row {
                                cell {
                                    text(
                                        bokmal { + utlandPeriode.land },
                                        nynorsk { + utlandPeriode.land },
                                        english { + utlandPeriode.land }
                                    )
                                }
                                cell {
                                    text(
                                        bokmal { + utlandPeriode.fom.format(short = true) },
                                        nynorsk { + utlandPeriode.fom.format(short = true) },
                                        english { + utlandPeriode.fom.format(short = true) }
                                    )
                                }
                                cell {
                                    text(
                                        bokmal { + utlandPeriode.tom.format(short = true) },
                                        nynorsk { + utlandPeriode.tom.format(short = true) },
                                        english { + utlandPeriode.tom.format(short = true) }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }