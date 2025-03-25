package no.nav.pensjon.brev.maler.alder.avslag.gradsendring


import no.nav.pensjon.brev.api.model.maler.alderApi.NormertPensjonsalder
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningen
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenSelectors.prorataBruktIBeregningen
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenSelectors.uttaksgrad
import no.nav.pensjon.brev.maler.alder.vedlegg.opplysningerBruktIBeregningenAP
import no.nav.pensjon.brev.maler.fraser.alderspensjon.aarOgMaanederFormattert
import no.nav.pensjon.brev.maler.fraser.common.Constants.DIN_PENSJON_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_KONTAKTSENTER_TELEFON
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brev.template.namedReference
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

data class InnholdLavOpptjening(
    val afpBruktIBeregning: Expression<Boolean>,
    val normertPensjonsalder: Expression<NormertPensjonsalder>,
    val opplysningerBruktIBeregningen: Expression<OpplysningerBruktIBeregningen>,
    val virkFom: Expression<LocalDate>,
    val minstePensjonssats: Expression<Kroner>,
    val totalPensjon: Expression<Kroner>,
    val borINorge: Expression<Boolean>,
    val harEOSLand: Expression<Boolean>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            textExpr(
                Bokmal to
                        "Du har for lav pensjonsopptjening til at du kan ta ut ".expr() + opplysningerBruktIBeregningen.uttaksgrad.format() +
                        " prosent fra " + virkFom.format() + ". Derfor har vi avslått søknaden din.",
                Nynorsk to
                        "For å ta ut alderspensjon før du er ".expr() + normertPensjonsalder.aarOgMaanederFormattert() + ", må du ha høg nok pensjonsopptjening. " +
                        "Du har for låg pensjonsopptening til at du kan ta ut ".expr() + opplysningerBruktIBeregningen.uttaksgrad.format() +
                        " prosent pensjon frå " + virkFom.format() + ". Derfor har vi avslått søknaden din.",
                English to
                        "Your pension accrual must be sufficient to start drawing retirement pension before you turn ".expr() + normertPensjonsalder.aarOgMaanederFormattert() + ". " +
                        "Your accumulated pension capital is not sufficient for you to draw a retirement pension at ".expr() + opplysningerBruktIBeregningen.uttaksgrad.format() +
                        " percent from " + virkFom.format() + ". Therefore, we have declined your application.",
            )
        }

        paragraph {
            text(
                Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 20-15 og 22-13.",
                Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 20-15 og 22-13.",
                English to "This decision was made pursuant to the provisions of §§ 20-15 and 22-13 of the National Insurance Act."
            )
        }

        showIf(harEOSLand and opplysningerBruktIBeregningen.prorataBruktIBeregningen) {
            paragraph {
                text(
                    Bokmal to "Vedtaket er også gjort etter EØS-avtalens regler i forordning 883/2004, artikkel 6.",
                    Nynorsk to "",
                    English to "",
                )
            }
        }

        showIf(harEOSLand.not() and opplysningerBruktIBeregningen.prorataBruktIBeregningen) {
            paragraph {
                text(
                    Bokmal to "Vedtaket er også gjort etter artikkel<FRITEKST; Legg inn aktuelle artikler om sammenlegging og eksport> itrygdeavtalen med <avtaleland.navn>.", //TODO
                    Nynorsk to "",
                    English to "",
                )
            }
        }

        title2 {
            text(
                Bokmal to "Slik har vi beregnet",
                Nynorsk to "Slik har vi berekna",
                English to "The calculations made are as follows"
            )
        }
        paragraph {
            list {
                item {
                    textExpr(
                        Bokmal to "For å kunne ta ut alderspensjon før du fyller ".expr() + normertPensjonsalder.aarOgMaanederFormattert() +
                                ", må pensjonen din minst utgjøre ".expr() + minstePensjonssats.format() + " kroner i året.",
                        Nynorsk to "For å kunne ta ut alderspensjon før du fyller ".expr() + normertPensjonsalder.aarOgMaanederFormattert() +
                                ", må pensjonen din minst vere ".expr() + minstePensjonssats.format() + " kroner i året.",
                        English to "For you to be eligible for retirement pension before the age of ".expr() + normertPensjonsalder.aarOgMaanederFormattert() +
                                ", your retirement pension must be, at minimum, NOK ".expr() + minstePensjonssats.format() + " a year.",
                    )

                    showIf(opplysningerBruktIBeregningen.prorataBruktIBeregningen) {
                        text(
                            Bokmal to " Vi har tatt hensyn til at du også har trygdetid fra land som Norge har trygdeavtale med.",
                            Nynorsk to " Vi har tatt omsyn til at du også har trygdetid frå land som Noreg har trygdeavtale med.",
                            English to " We have taken into account any periods of national insurance coverage" +
                                    " that you may have in countries with which Norway has a social security agreement."
                        )
                    }
                }
                item {
                    textExpr(
                        Bokmal to "Hvis du hadde tatt ut ".expr() + opplysningerBruktIBeregningen.uttaksgrad.format() + " prosent alderspensjon fra "
                                + virkFom.format() + ", ville du fått ".expr() + totalPensjon.format() + " kroner årlig i pensjon. ",
                        Nynorsk to "Om du hadde tatt ut ".expr() + opplysningerBruktIBeregningen.uttaksgrad.format() + " prosent alderspensjon frå "
                                + virkFom.format() + ", ville du fått ".expr() + totalPensjon.format() + " kroner årleg i pensjon. ",
                        English to "If you draw a retirement pension of ".expr() + opplysningerBruktIBeregningen.uttaksgrad.format() + " percent from "
                                + virkFom.format() + ", your retirement pension is calculated to be NOK " + totalPensjon.format() + " a year. ",
                    )
                    showIf(afpBruktIBeregning) {
                        text(
                            Bokmal to "I denne beregningen har vi inkludert AFP.",
                            Nynorsk to "I denne berekninga har vi inkludert AFP.",
                            English to "This amount includes contractual early retirement pension."
                        )
                    }
                }
            }
        }

        paragraph {
            text(
                Bokmal to "Beregningen er uavhengig av sivilstanden din.",
                Nynorsk to "Berekninga er uavhengig av sivilstanden din.",
                English to "The calculation is independent of your marital status."
            )
        }
        paragraph {
            text(
                Bokmal to "I vedlegg ",
                Nynorsk to "I vedlegg ",
                English to "Appendix "
            )
            namedReference(opplysningerBruktIBeregningenAP)
            text(
                Bokmal to " finner du en tabell som viser opplysninger brukt i beregningen.",
                Nynorsk to " finn du ein tabell som viser opplysningar brukt i berekninga.",
                English to " includes a table with information about how your pension is calculated."
            )
        }

        showIf(borINorge) {
            title2 {
                textExpr(
                    Bokmal to "Du kan fremdeles ha mulighet til å ta ut mer alderspensjon før du fyller + ".expr() + normertPensjonsalder.aarOgMaanederFormattert(),
                    Nynorsk to "".expr(),
                    English to "".expr()
                )
            }
            paragraph {
                textExpr(
                    Bokmal to "Selv om vi har avslått denne søknaden, kan du likevel ha rett til å ta ut alderspensjon før du fyller ".expr() +
                            normertPensjonsalder.aarOgMaanederFormattert() + ". " +
                            "Da må du kunne velge en lavere uttaksgrad eller ta ut pensjonen senere. I Din pensjon på $DIN_PENSJON_URL kan " +
                            "du sjekke når du tidligst kan ta ut alderspensjon. Du kan også se hva pensjonen din blir, avhengig av når og hvor mye du tar ut.",
                    Nynorsk to "".expr(),
                    English to "".expr(),
                )
            }
        }.orShow {
            title2 {
                text(
                    Bokmal to "Du kan selv sjekke når du kan ta ut alderspensjon",
                    Nynorsk to "",
                    English to ""
                )
            }
            paragraph {
                textExpr(
                    Bokmal to "I Din pensjon på $DIN_PENSJON_URL kan du sjekke når du tidligst kan ta ut alderspensjon. ".expr() +
                            "Der kan du også se hva pensjonen din blir avhengig av når og hvor mye du tar ut. " +
                            "Du kan logge inn med BankID, Buypass eller Commfides. Kontakt oss gjerne på telefon " +
                            " $NAV_KONTAKTSENTER_TELEFON hvis du trenger hjelp til dette. ",
                    Nynorsk to "".expr(),
                    English to "".expr(),
                )
                textExpr(
                    Bokmal to "Selv om vi har avslått denne søknaden, kan du likevel ha rett til å ta ut alderspensjon før ".expr() +
                            "du fyller " + normertPensjonsalder.aarOgMaanederFormattert() + ". Da må du kunne velge en lavere uttaksgrad eller ta ut pensjonen senere.",
                    Nynorsk to "".expr(),
                    English to "".expr(),
                )
            }
        }

        paragraph {
            text(
                Bokmal to "Du må sende oss en ny søknad når du ønsker å ta ut alderspensjon. " +
                        "En eventuell endring kan tidligst skje måneden etter at vi har mottatt søknaden.",
                Nynorsk to "Du må sende oss ein ny søknad når du ønskjer å ta ut alderspensjon. " +
                        "Ei eventuell endring kan tidlegast skje månaden etter at vi har fått søknaden.",
                English to "You must submit an application when you want to start drawing your retirement pension. " +
                        "Any change will be implemented at the earliest the month after we have received the application."
            )
        }
    }
}
