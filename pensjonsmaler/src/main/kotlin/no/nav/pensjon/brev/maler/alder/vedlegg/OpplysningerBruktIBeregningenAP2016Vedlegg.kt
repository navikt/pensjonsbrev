package no.nav.pensjon.brev.maler.alder.vedlegg


import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenKap19Selectors.andelGammeltRegelverk
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenKap19Selectors.andelNyttRegelverk
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenKap19Selectors.avslattKap19
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenKap19Selectors.avslattKap19_safe
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenKap19Selectors.fodselsAar
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenKap19Selectors.forholdstall
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenKap19Selectors.forholdstallVed67
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenKap19Selectors.innvilgetTillegspensjon
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenKap19Selectors.poengAar
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenKap19Selectors.poengAarE91
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenKap19Selectors.poengAarF92
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenKap19Selectors.redusertTrygdetidKap19_safe
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
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningenalder.DelingstallVed67Aar
import no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningenalder.OpplysningerBruktIBeregningenTrygdetidTabeller
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.LocalizedFormatter
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.text


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
                    text(
                        bokmal { + "Du som er født i perioden 1954 - 1962 får en kombinasjon av alderspensjon etter gamle " +
                                "og nye regler i folketrygdloven (kapittel 19 og 20). Fordi du er født i " +
                                opplysningerKap19.fodselsAar.format() + " får du " + "beregnet " +
                                opplysningerKap19.andelGammeltRegelverk.format() + "/10 av pensjonen etter gamle regler, og " +
                                opplysningerKap19.andelNyttRegelverk.format() + "/10 etter nye regler." },
                        nynorsk { + "Du som er fødd i perioden 1954-1962 får ein kombinasjon av alderspensjon etter gamle " +
                                "og nye reglar i folketrygdlova (kapittel 19 og 20). Fordi du er fødd i " +
                                opplysningerKap19.fodselsAar.format() + ", får du berekna " +
                                opplysningerKap19.andelGammeltRegelverk.format() + "/10 av pensjonen etter gamle reglar, og " +
                                opplysningerKap19.andelNyttRegelverk.format() + "/10 etter nye reglar." },
                        english { + "Individuals born between 1954 and 1962 will receive a combination of retirement pension calculated on the basis of both old " +
                                "and new provisions in the National Insurance Act (Chapters 19 and 20). Because you are born in " + opplysningerKap19.fodselsAar.format() +
                                ", " + opplysningerKap19.andelGammeltRegelverk.format() + "/10 of your pension is calculated on the basis of the old provisions, " +
                                "and " + opplysningerKap19.andelNyttRegelverk.format() + "/10 is calculated on the basis of new provisions." },
                    )
                }

                title2 {
                    text(
                        bokmal { + "Opplysninger til grunn for beregning etter gamle regler (kapittel 19) per " + virkFom.format() },
                        nynorsk { + "Opplysningar til grunn for berekning etter gamle reglar (kapittel 19) per " + virkFom.format() },
                        english { + "Information used in the calculation on the basis of the old provisions (Chapter 19) as of " + virkFom.format() }
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
                            cell { includePhrase(Vedtak.TrygdetidText) }
                            cell {
                                text(
                                    bokmal { + opplysningerKap19.trygdetidKap19.format() + " år" },
                                    nynorsk { + opplysningerKap19.trygdetidKap19.format() + " år" },
                                    english { + opplysningerKap19.trygdetidKap19.format() + " years" }
                                )
                            }
                        }
                        showIf(opplysningerKap19.innvilgetTillegspensjon.equalTo(true)) {
                            ifNotNull(opplysningerKap19.sluttpoengTall) { sluttpoeng ->
                                showIf(sluttpoeng.greaterThan(0.0)) {
                                    row {
                                        cell {
                                            text(
                                                bokmal { + "Sluttpoengtall" },
                                                nynorsk { + "Sluttpoengtal" },
                                                english { + "Final pension point score" }
                                            )
                                        }
                                        cell {
                                            text(
                                                bokmal { + sluttpoeng.format() },
                                                nynorsk { + sluttpoeng.format() },
                                                english { + sluttpoeng.format() }
                                            )
                                        }
                                    }
                                }
                            }
                            ifNotNull(opplysningerKap19.poengAar) { antallPoengAar ->
                                showIf(antallPoengAar.greaterThan(0)) {
                                    row {
                                        cell {
                                            text(
                                                bokmal { + "Antall poengår" },
                                                nynorsk { + "Talet på poengår" },
                                                english { + "Number of pension point earning years" }
                                            )
                                        }
                                        cell {
                                            text(
                                                bokmal { + antallPoengAar.format() + " år" },
                                                nynorsk { + antallPoengAar.format() + " år" },
                                                english { + antallPoengAar.format() + " years" }
                                            )
                                        }
                                    }
                                }
                            }
                            ifNotNull(opplysningerKap19.poengAarF92) { antallPoengAar ->
                                showIf(antallPoengAar.greaterThan(0)) {
                                    row {
                                        cell {
                                            text(
                                                bokmal { + "Antall år med pensjonsprosent 45" },
                                                nynorsk { + "Talet på år med pensjonsprosent 45" },
                                                english { + "Number of years calculated with pension percentage 45" }
                                            )
                                        }
                                        cell {
                                            text(
                                                bokmal { + antallPoengAar.format() + " år" },
                                                nynorsk { + antallPoengAar.format() + " år" },
                                                english { + antallPoengAar.format() + " years" }
                                            )
                                        }
                                    }
                                }
                            }
                            ifNotNull(opplysningerKap19.poengAarE91) { antallPoengAar ->
                                showIf(antallPoengAar.greaterThan(0)) {
                                    row {
                                        cell {
                                            text(
                                                bokmal { + "Antall år med pensjonsprosent 42" },
                                                nynorsk { + "Talet på år med pensjonsprosent 42" },
                                                english { + "Number of years calculated with pension percentage 42" }
                                            )
                                        }
                                        cell {
                                            text(
                                                bokmal { + antallPoengAar.format() + " år" },
                                                nynorsk { + antallPoengAar.format() + " år" },
                                                english { + antallPoengAar.format() + " years" }
                                            )
                                        }
                                    }
                                }
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
                                    bokmal { + "Forholdstall ved uttak" },
                                    nynorsk { + "Forholdstal ved uttak" },
                                    english { + "Ratio at withdrawal" }
                                )
                            }
                            cell {
                                text(
                                    bokmal { + opplysningerKap19.forholdstall.format(ForholdstallFormat) },
                                    nynorsk { + opplysningerKap19.forholdstall.format(ForholdstallFormat) },
                                    english { + opplysningerKap19.forholdstall.format(ForholdstallFormat) },
                                )
                            }
                        }
                        showIf(uttaksgrad.lessThan(100)) {
                            row {
                                cell {
                                    text(
                                        bokmal { + "Forholdstall ved 67 år" },
                                        nynorsk { + "Forholdstal ved 67 år" },
                                        english { + "Ratio adjustment at age 67" }
                                    )
                                }
                                cell {
                                    text(
                                        bokmal { + opplysningerKap19.forholdstallVed67.format(ForholdstallFormat) },
                                        nynorsk { + opplysningerKap19.forholdstallVed67.format(ForholdstallFormat) },
                                        english { + opplysningerKap19.forholdstallVed67.format(ForholdstallFormat) }
                                    )
                                }
                            }
                        }
                    }
                }
            }

        }

        title2 {
            text(
                bokmal { + "Opplysninger til grunn for beregning etter nye regler (kapittel 20) per " + virkFom.format() },
                nynorsk { + "Opplysningar til grunn for berekning etter nye reglar (kapittel 20) per " + virkFom.format() },
                english { + "Information used in the calculation on the basis of the new provisions (Chapter 20) as of " + virkFom.format() }
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
                showIf(opplysningerKap19.avslattKap19_safe.equalTo(false) and trygdetid.greaterThan(0)) {
                    row {
                        cell { includePhrase(Vedtak.TrygdetidText) }
                        cell {
                            text(
                                bokmal { + trygdetid.format() + " år" },
                                nynorsk { + trygdetid.format() + " år" },
                                english { + trygdetid.format() + " years" }
                            )
                        }
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
                            bokmal { +  delingstallVedUttak.format() },
                            nynorsk { +  delingstallVedUttak.format() },
                            english { +  delingstallVedUttak.format() }
                        )
                    }
                }
                showIf(uttaksgrad.lessThan(100)) {
                    ifNotNull(delingstallVedNormertPensjonsalder) { delingstall ->
                        row {
                            cell { includePhrase(DelingstallVed67Aar) }
                            cell {
                                text(
                                    bokmal { +  delingstall.format() },
                                    nynorsk { +  delingstall.format() },
                                    english { +  delingstall.format() }
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
                        bokmal { +  "Pensjonsopptjening og trygdetid tas med i beregningen av alderspensjon fra og med året etter at skatteoppgjøret er klart. " +
                                "Dette gjelder selv om skatteoppgjøret ditt er klart tidligere. " +
                                "I beregningen er det derfor brukt pensjonsopptjening til og med " + sisteAar.format() + "." },
                        nynorsk { +  "Pensjonsopptening og trygdetid blir tatt med i berekninga av alderspensjon frå og med året etter at skatteoppgjeret er klart. " +
                                "Dette gjeld sjølv om skatteoppgjeret ditt er klart tidlegare. " +
                                "I berekninga er det derfor brukt pensjonsopptening til og med " + sisteAar.format() + "." },
                        english { +  "Pension accrual and periods of National Insurance Scheme coverage are included in the calculation of retirement pension from the year after the tax settlement is ready. " +
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
                        bokmal { +  "Pensjonsbeholdningen i tabellen er beregnet ut fra den pensjonen du har tatt ut og pensjonsbeholdningen din." },
                        nynorsk { +  "Pensjonsbehaldninga i tabellen er berekna ut frå den pensjonen du har tatt ut og pensjonsbehaldninga di." },
                        english { +  "The accumulated pension capital is calculated from your current pension and your remaining pension capital." }
                    )
                }
            }
        }

        paragraph {
            text(
                bokmal { +  "På ${Constants.PENSJON_URL} kan du lese mer om regelverket for alderspensjon og hvordan disse tallene har betydning for beregningen. Logg inn på ${Constants.DIN_PENSJON_URL} for å se hvilke inntekter og opplysninger om opptjening vi har registrert." },
                nynorsk { +  "På ${Constants.PENSJON_URL} kan du lese meir om regelverket for alderspensjon og kva desse tala har å seie for berekninga. Logg inn på ${Constants.DIN_PENSJON_URL} for å sjå kva inntekter og opplysningar om opptening vi har registrert." },
                english { +  "Go to ${Constants.PENSJON_URL} to read more about these regulations that apply to retirement pension and how these affect your calculation. Log in to ${Constants.DIN_PENSJON_URL} to see your income and accumulated pension capital." }
            )
        }

        showIf((opplysningerKap19.redusertTrygdetidKap19_safe.equalTo(true) or opplysningerKap20.redusertTrygdetidKap20) and opplysningerKap19.avslattKap19_safe.equalTo(false)) {
            paragraph {
                text(
                    bokmal { +  "Trygdetid baserer seg på perioder du har bodd og/eller arbeidet i Norge, og har betydning for beregning av pensjonen din. Full trygdetid er 40 år." +
                            " Unntaket kan være hvis du har pensjonsopptjening fra et land Norge har trygdeavtale med." },
                    nynorsk { +  "Trygdetid baserer seg på periodar du har budd og/eller arbeidd i Noreg, og har betydning for berekning av pensjonen din. Full trygdetid er 40 år." +
                            " Unntaket kan vere om du har pensjonsopptening frå eit land Noreg har trygdeavtale med." },
                    english { +  "The period of national insurance coverage is based on periods you have lived and/or worked in Norway, and these years affect pension eligibility. Full pension eligibility is 40 years." +
                            " The exception may be if you have pension accrual from a country that Norway has a social security agreement with." }
                )
            }

            paragraph {
                text(
                    bokmal { +  "Reglene for fastsetting av trygdetid er litt ulike i kapittel 19 og kapittel 20 i folketrygdloven. " +
                            "Derfor kan trygdetid etter kapittel 19 i enkelte tilfeller være høyere enn trygdetid etter kapittel 20." },
                    nynorsk { +  "Reglane for fastsetjing av trygdetid er ulike i kapittel 19 og kapittel 20 i folketrygdlova. " +
                            "Derfor kan trygdetid etter kapittel 19 i enkelte tilfelle vere høgare enn trygdetid etter kapittel 20." },
                    english { +  "The provisions pertaining to accumulated pension rights differ in Chapters 19 and 20 in the National Insurance Act. " +
                            "Consequently, national insurance coverage pursuant to Chapter 19 may, in some cases, be higher than years pursuant to Chapter 20." },
                )
            }

            includePhrase(OpplysningerBruktIBeregningenTrygdetidTabeller.NorskTrygdetidInnledning)

            paragraph {
                table(header = {
                    column {
                        text(
                            bokmal { +  "Fra og med" },
                            nynorsk { +  "Frå og med" },
                            english { +  "Start date" },
                        )
                    }
                    column(alignment = RIGHT) {
                        text(
                            bokmal { +  "Til og med" },
                            nynorsk { +  "Til og med" },
                            english { +  "End date" }
                        )
                    }
                }) {
                    forEach(trygdeperioderNorge) { periode ->
                        row {
                            cell {
                                text(
                                    bokmal { +  periode.fom.format(short = true) },
                                    nynorsk { +  periode.fom.format(short = true) },
                                    english { +  periode.fom.format(short = true) }
                                )
                            }
                            cell {
                                text(
                                    bokmal { +  periode.tom.format(short = true) },
                                    nynorsk { +  periode.tom.format(short = true) },
                                    english { +  periode.tom.format(short = true) }
                                )
                            }
                        }
                    }
                }
            }

            showIf(trygdeperioderUtland.isNotEmpty()) {
                paragraph {
                    text(
                        bokmal { +  ("Tabellen nedenfor viser perioder du har bodd og/eller arbeidet i land som Norge har" +
                                " trygdeavtale med. Disse periodene er brukt i vurderingen av retten til alderspensjon før" +
                                " du blir ").expr() + normertPensjonsalder.aarOgMaanederFormattert() + "." },
                        nynorsk { +  ("Tabellen nedanfor viser periodar du har budd og/eller arbeidd i land som Noreg" +
                                " har trygdeavtale med. Desse periodane er brukt i vurderinga av retten til alderspensjon" +
                                " før du blir ").expr() + normertPensjonsalder.aarOgMaanederFormattert() + "." },
                        english { +  ("The table below shows your insurance coverage in countries with which Norway" +
                                " has a social security agreement. These periods have been used to assess whether" +
                                " you are eligible for retirement pension before the age of ").expr() + normertPensjonsalder.aarOgMaanederFormattert() + "." },
                    )
                }

                paragraph {
                    table(header = {
                        column {
                            text(
                                bokmal { +  "Land" },
                                nynorsk { +  "Land" },
                                english { +  "Country" }
                            )
                        }
                        column(alignment = RIGHT) {
                            text(
                                bokmal { +  "Fra og med" },
                                nynorsk { +  "Frå og med" },
                                english { +  "Start date" },
                            )
                        }
                        column(alignment = RIGHT) {
                            text(
                                bokmal { +  "Til og med" },
                                nynorsk { +  "Til og med" },
                                english { +  "End date" }
                            )
                        }
                    }) {
                        forEach(trygdeperioderUtland) { utlandPeriode ->
                            row {
                                cell {
                                    text(
                                        bokmal { +  utlandPeriode.land },
                                        nynorsk { +  utlandPeriode.land },
                                        english { +  utlandPeriode.land }
                                    )
                                }
                                cell {
                                    text(
                                        bokmal { +  utlandPeriode.fom.format(short = true) },
                                        nynorsk { +  utlandPeriode.fom.format(short = true) },
                                        english { +  utlandPeriode.fom.format(short = true) }
                                    )
                                }
                                cell {
                                    text(
                                        bokmal { +  utlandPeriode.tom.format(short = true) },
                                        nynorsk { +  utlandPeriode.tom.format(short = true) },
                                        english { +  utlandPeriode.tom.format(short = true) }
                                    )
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