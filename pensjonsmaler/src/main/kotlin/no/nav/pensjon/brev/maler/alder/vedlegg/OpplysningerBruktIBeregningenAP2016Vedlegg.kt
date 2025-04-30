package no.nav.pensjon.brev.maler.alder.vedlegg


import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenKap19Selectors.andelGammeltRegelverk
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenKap19Selectors.andelNyttRegelverk
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenKap19Selectors.avslattKap19
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenKap19Selectors.fodselsAar
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenKap19Selectors.forholdstall
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenKap19Selectors.forholdstallVed67
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenKap19Selectors.innvilgetTillegspensjon
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenKap19Selectors.poengAar
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenKap19Selectors.poengAarE91
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenKap19Selectors.poengAarF92
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenKap19Selectors.redusertTrygdetidKap19
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenKap19Selectors.sluttpoengTall
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenKap19Selectors.trygdetidKap19
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenKap20Selectors.redusertTrygdetidKap20
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenSelectors.delingstallVedNormertPensjonsalder
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenSelectors.delingstallVedUttak
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenSelectors.kravAarsak
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenSelectors.normertPensjonsalder
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenSelectors.opplysningerKap19
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenSelectors.opplysningerKap20
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenSelectors.pensjonsbeholdning
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
import no.nav.pensjon.brev.maler.fraser.alderspensjon.aarOgMaanederFormattert
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.LocalizedFormatter
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr

val opplysningerBruktIBeregningenAP2016Vedlegg =
    createAttachment(
        title = newText(
            Bokmal to "Opplysninger brukt i beregningen",
            Nynorsk to "Opplysningar brukt i berekninga",
            English to "Information used in the calculation"
        ),
    ) {
        ifNotNull(opplysningerKap19) { opplysningerKap19 ->
            showIf(opplysningerKap19.avslattKap19.not()) {
                paragraph {
                    textExpr(
                        Bokmal to "Du som er født i perioden 1954 - 1962 får en kombinasjon av alderspensjon etter gamle ".expr() +
                                "og nye regler i folketrygdloven (kapittel 19 og 20). Fordi du er født i " +
                                opplysningerKap19.fodselsAar.format() + " får du " + "beregnet " +
                                opplysningerKap19.andelGammeltRegelverk.format() + "/10 av pensjonen etter gamle regler, og " +
                                opplysningerKap19.andelNyttRegelverk.format() + "/10 etter nye regler.",
                        Nynorsk to "Du som er fødd i perioden 1954-1962 får ein kombinasjon av alderspensjon etter gamle ".expr() +
                                "og nye reglar i folketrygdlova (kapittel 19 og 20). Fordi du er fødd i " +
                                opplysningerKap19.fodselsAar.format() + ", får du berekna " +
                                opplysningerKap19.andelGammeltRegelverk.format() + "/10 av pensjonen etter gamle reglar, og " +
                                opplysningerKap19.andelNyttRegelverk.format() + "/10 etter nye reglar.",
                        English to "Individuals born between 1954 and 1962 will receive a combination of retirement pension calculated on the basis of both old ".expr() +
                                "and new provisions in the National Insurance Act (Chapters 19 and 20). Because you are born in " + opplysningerKap19.fodselsAar.format() +
                                ", " + opplysningerKap19.andelGammeltRegelverk.format() + "/10 of your pension is calculated on the basis of the old provisions, " +
                                "and " + opplysningerKap19.andelNyttRegelverk.format() + "/10 is calculated on the basis of new provisions.",
                    )
                }

                title2 {
                    textExpr(
                        Bokmal to "Opplysninger til grunn for beregning etter gamle regler (kapittel 19) per ".expr() + virkFom.format(),
                        Nynorsk to "Opplysningar til grunn for berekning etter gamle reglar (kapittel 19) per ".expr() + virkFom.format(),
                        English to "Information used in the calculation on the basis of the old provisions (Chapter 19) as of ".expr() + virkFom.format()
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
                                    Bokmal to "Trygdetid etter kapittel 19",
                                    Nynorsk to "Trygdetid etter kapittel 19",
                                    English to "National insurance coverage pursuant to Chapter 19"
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to opplysningerKap19.trygdetidKap19.format() + " år".expr(),
                                    Nynorsk to opplysningerKap19.trygdetidKap19.format() + " år".expr(),
                                    English to opplysningerKap19.trygdetidKap19.format() + " years".expr()
                                )
                            }
                        }
                        showIf(opplysningerKap19.innvilgetTillegspensjon.equalTo(true)) {
                            ifNotNull(opplysningerKap19.sluttpoengTall) { sluttpoeng ->
                                row {
                                    cell {
                                        text(
                                            Bokmal to "Sluttpoengtall",
                                            Nynorsk to "Sluttpoengtal",
                                            English to "Final pension point score"
                                        )
                                    }
                                    cell {
                                        textExpr(
                                            Bokmal to sluttpoeng.format(),
                                            Nynorsk to sluttpoeng.format(),
                                            English to sluttpoeng.format()
                                        )
                                    }
                                }
                            }
                            ifNotNull(opplysningerKap19.poengAar) { antallPoengAar ->
                                row {
                                    cell {
                                        text(
                                            Bokmal to "Antall poengår",
                                            Nynorsk to "Talet på poengår",
                                            English to "Number of pension point earning years"
                                        )
                                    }
                                    cell {
                                        textExpr(
                                            Bokmal to antallPoengAar.format() + " år".expr(),
                                            Nynorsk to antallPoengAar.format() + " år".expr(),
                                            English to antallPoengAar.format() + " years".expr()
                                        )
                                    }
                                }
                            }
                            ifNotNull(opplysningerKap19.poengAarF92) { antallPoengAar ->
                                row {
                                    cell {
                                        text(
                                            Bokmal to "Antall år med pensjonsprosent 45",
                                            Nynorsk to "Talet på år med pensjonsprosent 45",
                                            English to "Number of years calculated with pension percentage 45"
                                        )
                                    }
                                    cell {
                                        textExpr(
                                            Bokmal to antallPoengAar.format() + " år".expr(),
                                            Nynorsk to antallPoengAar.format() + " år".expr(),
                                            English to antallPoengAar.format() + " years".expr()
                                        )
                                    }
                                }
                            }
                            ifNotNull(opplysningerKap19.poengAarE91) { antallPoengAar ->
                                row {
                                    cell {
                                        text(
                                            Bokmal to "Antall år med pensjonsprosent 42",
                                            Nynorsk to "Talet på år med pensjonsprosent 42",
                                            English to "Number of years calculated with pension percentage 42"
                                        )
                                    }
                                    cell {
                                        textExpr(
                                            Bokmal to antallPoengAar.format() + " år".expr(),
                                            Nynorsk to antallPoengAar.format() + " år".expr(),
                                            English to antallPoengAar.format() + " years".expr()
                                        )
                                    }
                                }
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
                                    Bokmal to "Forholdstall ved uttak",
                                    Nynorsk to "Forholdstal ved uttak",
                                    English to "Ratio at withdrawal"
                                )
                            }
                            cell {
                                textExpr(
                                    Bokmal to opplysningerKap19.forholdstall.format(ForholdstallFormat),
                                    Nynorsk to opplysningerKap19.forholdstall.format(ForholdstallFormat),
                                    English to opplysningerKap19.forholdstall.format(ForholdstallFormat),
                                )
                            }
                        }
                        showIf(uttaksgrad.lessThan(100)) {
                            row {
                                cell {
                                    text(
                                        Bokmal to "Forholdstall ved 67 år",
                                        Nynorsk to "Forholdstal ved 67 år",
                                        English to "Ratio adjustment at age 67"
                                    )
                                }
                                cell {
                                    textExpr(
                                        Bokmal to opplysningerKap19.forholdstallVed67.format(ForholdstallFormat),
                                        Nynorsk to opplysningerKap19.forholdstallVed67.format(ForholdstallFormat),
                                        English to opplysningerKap19.forholdstallVed67.format(ForholdstallFormat)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            title2 {
                textExpr(
                    Bokmal to "Opplysninger til grunn for beregning etter nye regler (kapittel 20) per ".expr() + virkFom.format(),
                    Nynorsk to "Opplysningar til grunn for berekning etter nye reglar (kapittel 20) per ".expr() + virkFom.format(),
                    English to "Information used in the calculation on the basis of the new provisions (Chapter 20) as of ".expr() + virkFom.format()
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
                    showIf(opplysningerKap19.avslattKap19.not()) {
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
                    }
                    row {
                        cell {
                            text(
                                Bokmal to "Pensjonsbeholdning",
                                Nynorsk to "Pensjonsbehaldning",
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
                                Nynorsk to "Delingstal ved uttak",
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
                    showIf(uttaksgrad.lessThan(100)) {
                        ifNotNull(delingstallVedNormertPensjonsalder) { delingstall ->
                            row {
                                cell {
                                    text(
                                        Bokmal to "Delingstall ved 67 år",
                                        Nynorsk to "Delingstal ved 67 år",
                                        English to "Life expectancy adjustment divisor at 67 years"
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

            showIf(kravAarsak.notEqualTo("UTTAKSGRAD")) {
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
            }

            showIf(kravAarsak.equalTo("UTTAKSGRAD")) {
                ifNotNull(sisteOpptjeningsAar) { sisteAar ->
                    paragraph {
                        text(
                            Bokmal to "Pensjonsbeholdningen i tabellen er beregnet ut fra den pensjonen du har tatt ut og pensjonsbeholdningen din.",
                            Nynorsk to "Pensjonsbehaldninga i tabellen er berekna ut frå den pensjonen du har tatt ut og pensjonsbehaldninga di.",
                            English to "The accumulated pension capital is calculated from your current pension and your remaining pension capital."
                        )
                    }
                }
            }

            paragraph {
                text(
                    Bokmal to "På ${Constants.PENSJON_URL} kan du lese mer om regelverket for alderspensjon og hvordan disse tallene har betydning for beregningen. Logg inn på ${Constants.DIN_PENSJON_URL} for å se hvilke inntekter og opplysninger om opptjening vi har registrert.",
                    Nynorsk to "På ${Constants.PENSJON_URL} kan du lese meir om regelverket for alderspensjon og kva desse tala har å seie for berekninga. Logg inn på ${Constants.DIN_PENSJON_URL} for å sjå kva inntekter og opplysningar om opptening vi har registrert.",
                    English to "Go to ${Constants.PENSJON_URL} to read more about these regulations that apply to retirement pension and how these affect your calculation. Log in to ${Constants.DIN_PENSJON_URL} to see your income and accumulated pension capital."
                )
            }

            showIf((opplysningerKap19.redusertTrygdetidKap19 or opplysningerKap20.redusertTrygdetidKap20) and opplysningerKap19.avslattKap19.not()) {
                paragraph {
                    text(
                        Bokmal to "Trygdetid baserer seg på perioder du har bodd og/eller arbeidet i Norge, og har betydning for beregning avpensjonen din. Full trygdetid er 40 år." +
                                " Unntaket kan være hvis du har pensjonsopptjening fra et land Norge har trygdeavtale med.",
                        Nynorsk to "Trygdetid baserer seg på periodar du har budd og/eller arbeidd i Noreg, og har betydning for berekning avpensjonen din. Full trygdetid er 40 år." +
                                " Unntaket kan vere om du har pensjonsopptening frå eit land Noreg har trygdeavtale med.",
                        English to "The period of national insurance coverage is based on periods you have lived and/or worked in Norway, and these years affect pension eligibility. Full pension eligibility is 40 years." +
                                " The exception may be if you have pension accrual from a country that Norway has a social security agreement with."
                    )
                }

                paragraph {
                    text(
                        Bokmal to "Reglene for fastsetting av trygdetid er litt ulike i kapittel 19 og kapittel 20 i folketrygdloven. " +
                                "Derfor kan trygdetid etter kapittel 19 i enkelte tilfeller være høyere enn trygdetid etter kapittel 20.",
                        Nynorsk to "Reglane for fastsetjing av trygdetid er ulike i kapittel 19 og kapittel 20 i folketrygdlova. " +
                                "Derfor kan trygdetid etter kapittel 19 i enkelte tilfelle vere høgare enn trygdetid etter kapittel 20.",
                        English to "The provisions pertaining to accumulated pension rights differ in Chapters 19 and 20 in the National Insurance Act. " +
                                "Consequently, national insurance coverage pursuant to Chapter 19 may, in some cases, be higher than years pursuant to Chapter 20.",
                    )
                }

                paragraph {
                    text(
                        Bokmal to "Tabellen nedenfor viser perioder vi har brukt for å fastsette din norske trygdetid.",
                        Nynorsk to "Tabellen nedanfor viser periodar vi har brukt for å fastsetje den norske trygdetida di.",
                        English to "The table below shows the time periods used to establish your Norwegian national insurance coverage."
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
                        textExpr(
                            Bokmal to ("Tabellen nedenfor viser perioder du har bodd og/eller arbeidet i land som Norge har" +
                                    " trygdeavtale med. Disse periodene er brukt i vurderingen av retten til alderspensjon før" +
                                    " du blir ").expr() + normertPensjonsalder.aarOgMaanederFormattert() + ".",
                            Nynorsk to ("Tabellen nedanfor viser periodar du har budd og/eller arbeidd i land som Noreg" +
                                    " har trygdeavtale med. Desse periodane er brukt i vurderinga av retten til alderspensjon" +
                                    " før du blir ").expr() + normertPensjonsalder.aarOgMaanederFormattert() + ".",
                            English to ("The table below shows your insurance coverage in countries with which Norway" +
                                    " has a social security agreement. These periods has been used to assess whether" +
                                    " you are eligible for retirement pension before the age of ").expr() + normertPensjonsalder.aarOgMaanederFormattert() + ".",
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
    }

private object ForholdstallFormat : LocalizedFormatter<Double>() {
    override fun stableHashCode(): Int = "ForholdstallFormat".hashCode()
    override fun apply(first: Double, second: Language): String =
        String.format(second.locale(), "%.3f", first)
}