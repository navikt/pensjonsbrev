package no.nav.pensjon.brev.maler.alder

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.maler.alderApi.NormertPensjonsalder
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningen
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenSelectors.prorataBruktIBeregningen
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningenSelectors.uttaksgrad
import no.nav.pensjon.brev.maler.alder.vedlegg.opplysningerBruktIBeregningenAP2016Vedlegg
import no.nav.pensjon.brev.maler.alder.vedlegg.opplysningerBruktIBeregningenAP2025Vedlegg
import no.nav.pensjon.brev.maler.fraser.alderspensjon.aarOgMaanederFormattert
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brev.template.namedReference
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

data class AvslagUttakFoerNormertPensjonsalderFelles(
    val afpBruktIBeregning: Expression<Boolean>,
    val normertPensjonsalder: Expression<NormertPensjonsalder>,
    val uttaksgrad: Expression<Int>,
    val prorataBruktIBeregningen: Expression<Boolean>,
    val virkFom: Expression<LocalDate>,
    val minstePensjonssats: Expression<Kroner>,
    val totalPensjon: Expression<Kroner>,
    val borINorge: Expression<Boolean>,
    val regelverkType: Expression<AlderspensjonRegelverkType>,
    val harEOSLand: Expression<Boolean>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title2 {
            text(
                Bokmal to "Vedtak",
                Nynorsk to "Vedtak",
                English to "Decision"
            )
        }
        paragraph {
            textExpr(
                Bokmal to
                        "For å ta ut alderspensjon før du er ".expr() + normertPensjonsalder.aarOgMaanederFormattert() + ", må du ha høy nok pensjonsopptjening. " +
                        "Du har for lav pensjonsopptjening til at du kan ta ut ".expr() + uttaksgrad.format() +
                        " prosent pensjon fra " + virkFom.format() + ". Derfor har vi avslått søknaden din.",
                Nynorsk to
                        "For å ta ut alderspensjon før du er ".expr() + normertPensjonsalder.aarOgMaanederFormattert() + ", må du ha høg nok pensjonsopptjening. " +
                        "Du har for låg pensjonsopptening til at du kan ta ut ".expr() + uttaksgrad.format() +
                        " prosent pensjon frå " + virkFom.format() + ". Derfor har vi avslått søknaden din.",
                English to
                        "Your pension accrual must be sufficient to start drawing retirement pension before you turn ".expr() + normertPensjonsalder.aarOgMaanederFormattert() + ". " +
                        "Your accumulated pension capital is not sufficient for you to draw a retirement pension at ".expr() + uttaksgrad.format() +
                        " percent from " + virkFom.format() + ". Therefore, we have declined your application.",
            )
        }

        showIf(regelverkType.isOneOf(AlderspensjonRegelverkType.AP2025)) {
            paragraph {
                text(
                    Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 20-15 og 22-13.",
                    Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 20-15 og 22-13.",
                    English to "This decision was made pursuant to the provisions of §§ 20-15 and 22-13 of the National Insurance Act."
                )
            }
        }.orShowIf(regelverkType.isOneOf(AlderspensjonRegelverkType.AP2016)) {
            paragraph {
                text(
                    Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 19-11, 19-15, 20-15 og 20-19.",
                    Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 19-11, 19-15, 20-15 og 20-19.",
                    English to "This decision was made pursuant to the provisions of §§ 19-11, 19-15, 20-15 and 20-19 of the National Insurance Act."
                )
            }
        }

        showIf(harEOSLand and prorataBruktIBeregningen) {
            paragraph {
                text(
                    Bokmal to "Vedtaket er også gjort etter EØS-avtalens regler i forordning 883/2004, artikkel 6.",
                    Nynorsk to "Vedtaket er også gjort etter reglane i EØS-avtalen i forordning 883/2004, artikkel 6.",
                    English to "This decision was also made pursuant to the provisions of Regulation (EC) 883/2004, article 6.",
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
            showIf(uttaksgrad.equalTo(100)) {
                list {
                    item {
                        textExpr(
                            Bokmal to "For å kunne ta ut alderspensjon før du fyller ".expr() + normertPensjonsalder.aarOgMaanederFormattert() +
                                    ", må pensjonen din minst være ".expr() + minstePensjonssats.format() + " kroner i året.",
                            Nynorsk to "For å kunne ta ut alderspensjon før du fyller ".expr() + normertPensjonsalder.aarOgMaanederFormattert() +
                                    ", må pensjonen din minst vere ".expr() + minstePensjonssats.format() + " kroner i året.",
                            English to "For you to be eligible for retirement pension before the age of ".expr() + normertPensjonsalder.aarOgMaanederFormattert() +
                                    ", your retirement pension must be, at minimum, NOK ".expr() + minstePensjonssats.format() + " a year.",
                        )

                        showIf(prorataBruktIBeregningen) {
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
                            Bokmal to "Hvis du hadde tatt ut ".expr() + uttaksgrad.format() + " prosent alderspensjon fra "
                                    + virkFom.format() + ", ville du fått ".expr() + totalPensjon.format() + " kroner årlig i pensjon. ",
                            Nynorsk to "Om du hadde tatt ut ".expr() + uttaksgrad.format() + " prosent alderspensjon frå "
                                    + virkFom.format() + ", ville du fått ".expr() + totalPensjon.format() + " kroner årleg i pensjon. ",
                            English to "If you draw a retirement pension of ".expr() + uttaksgrad.format() + " percent from "
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
            }.orShow {
                list {
                    item {
                        textExpr(
                            Bokmal to "For å kunne ta ut alderspensjon før du fyller ".expr() + normertPensjonsalder.aarOgMaanederFormattert() +
                                    ", må pensjonen din minst være ".expr() + minstePensjonssats.format() + " kroner i året. " +
                                    "Vi beregner den delen du ønsker å ta ut nå og hva du ville ha fått hvis du tar resten av pensjonen ved ".expr() +
                                    normertPensjonsalder.aarOgMaanederFormattert() + ".",
                            Nynorsk to "For å kunne ta ut alderspensjon før du fyller ".expr() + normertPensjonsalder.aarOgMaanederFormattert() +
                                    ", må pensjonen din minst vere ".expr() + minstePensjonssats.format() + " kroner i året. " +
                                    "Vi reknar ut den delen du ønsker å ta ut nå og kva du ville ha fått om du tar resten av pensjonen ved ".expr() +
                                    normertPensjonsalder.aarOgMaanederFormattert() + ".",
                            English to "For you to be eligible for retirement pension before the age of ".expr() + normertPensjonsalder.aarOgMaanederFormattert() +
                                    ", your retirement pension must be, at minimum, NOK ".expr() + minstePensjonssats.format() + " a year. " +
                                    "We calculate the part you wish to withdraw now and what you would have received if you take the rest of the pension at age ".expr() +
                                    normertPensjonsalder.aarOgMaanederFormattert() + ".",
                        )

                        showIf(prorataBruktIBeregningen) {
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
                            Bokmal to "Hvis du hadde tatt ut ".expr() + uttaksgrad.format() + " prosent alderspensjon fra " + virkFom.format() +
                                    ", ville du fått ".expr() + totalPensjon.format() + " kroner årlig i full pensjon når du blir ".expr() +
                                    normertPensjonsalder.aarOgMaanederFormattert() + ". ",
                            Nynorsk to "Om du hadde tatt ut ".expr() + uttaksgrad.format() + " prosent alderspensjon frå " + virkFom.format() +
                                    ", ville du fått ".expr() + totalPensjon.format() + " kroner årleg i full pensjon når du blir ".expr() +
                                    normertPensjonsalder.aarOgMaanederFormattert() + ". ",
                            English to "If you draw a retirement pension of ".expr() + uttaksgrad.format() + " percent from the date you requested, " +
                                    "your full retirement pension is calculated to be NOK ".expr() + totalPensjon.format() + " a year at age ".expr() +
                                    normertPensjonsalder.aarOgMaanederFormattert() + ". ",
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
                Bokmal to "I vedlegget ",
                Nynorsk to "I vedlegget ",
                English to "In the appendix "
            )
            showIf(regelverkType.isOneOf(AlderspensjonRegelverkType.AP2025)) {
                namedReference(opplysningerBruktIBeregningenAP2025Vedlegg)
            }
            showIf(regelverkType.isOneOf(AlderspensjonRegelverkType.AP2016)) {
                namedReference(opplysningerBruktIBeregningenAP2016Vedlegg)
            }
            text(
                Bokmal to " finner du en tabell som viser hvilke opplysninger vi har brukt.",
                Nynorsk to " finn du ein tabell som viser kva opplysningar vi har brukt.",
                English to " you will find a table showing the data we have used."
            )
        }
        title2 {
            text(
                Bokmal to "Se når du kan ta ut alderspensjon",
                Nynorsk to "Sjå når du kan ta ut alderspensjon",
                English to "Find out when you can start drawing retirement pension"
            )
        }
        paragraph {
            showIf(borINorge) {
                textExpr(
                    Bokmal to "Selv om vi har avslått denne søknaden, kan du likevel ha rett til å ta ut alderspensjon før du fyller ".expr() +
                            normertPensjonsalder.aarOgMaanederFormattert() + ". " +
                            "Da må du kunne velge en lavere uttaksgrad eller ta ut pensjonen senere. " +
                            "På ${Constants.DIN_PENSJON_URL} kan du sjekke når du tidligst kan ta ut alderspensjon. " +
                            "Du kan også se hva pensjonen din blir, avhengig av når og hvor mye du tar ut.",
                    Nynorsk to "Sjølv om vi har avslått denne søknaden, kan du likevel ha rett til å ta ut alderspensjon før du fyller ".expr() +
                            normertPensjonsalder.aarOgMaanederFormattert() + ". " +
                            "Då må du kunne velje ein lågare uttaksgrad eller ta ut pensjonen seinare. " +
                            "På ${Constants.DIN_PENSJON_URL} kan du sjekke når du tidlegast kan ta ut alderspensjon. " +
                            "Du kan også sjå kva pensjonen din blir, avhengig av når og kor mykje du tar ut.",
                    English to "Even though we have rejected this application, you may still be eligible to withdraw your retirement pension before you turn ".expr() +
                            normertPensjonsalder.aarOgMaanederFormattert() + " old. " +
                            "You must then be able to choose a lower withdrawal rate or take out the pension later. " +
                            "In \"Din Pensjon\" at ${Constants.DIN_PENSJON_URL}, you can check when you can earliest withdraw your retirement pension. " +
                            "You can also see what your pension will be, depending on when and how much you withdraw.",
                )
            }.orShow {
                textExpr(
                    Bokmal to "I Din pensjon på ${Constants.DIN_PENSJON_URL} kan du sjekke når du tidligst kan ta ut alderspensjon. ".expr() +
                            "Der kan du også se hva pensjonen din blir avhengig av når og hvor mye du tar ut. Du kan " +
                            "logge inn med BankID, Buypass eller Commfides. Kontakt oss gjerne på telefon" +
                            " ${Constants.NAV_KONTAKTSENTER_TELEFON} hvis du trenger hjelp til dette. ",
                    Nynorsk to "I Din pensjon på ${Constants.DIN_PENSJON_URL} kan du sjekke når du tidlegast kan ta ut ".expr() +
                            "alderspensjon. Der kan du også sjå kva pensjonen din blir, avhengig av når og kor mykje du " +
                            "tek ut. Du kan logge inn med BankID, Buypass eller Commfides. Kontakt oss gjerne på telefon " +
                            "${Constants.NAV_KONTAKTSENTER_TELEFON} hvis du treng hjelp til dette. ",
                    English to "You may still be eligible for retirement pension before you turn ".expr() +
                            normertPensjonsalder.aarOgMaanederFormattert() + " old, provided your accumulated pension capital is sufficiently high. " +
                            "Log on to \"Din Pensjon\" at ${Constants.DIN_PENSJON_URL} to find out more about your pension payments and how it changes " +
                            "depending on when you start drawing a retirement pension and what retirement percentage you choose. The service also offers " +
                            "information about when you become eligible for retirement pension.",
                )
                textExpr(
                    Bokmal to "Selv om vi har avslått denne søknaden, kan du likevel ha rett til å ta ut alderspensjon før ".expr() +
                            "du fyller " + normertPensjonsalder.aarOgMaanederFormattert() + ". Da må du velge en lavere " +
                            "uttaksgrad eller ta ut pensjonen på et senere tidspunkt.",
                    Nynorsk to "Sjølv om vi har avslått denne søknaden, kan du likevel ha rett til å ta ut alderspensjon ".expr() +
                            "før du fyller " + normertPensjonsalder.aarOgMaanederFormattert() + ". Da må du velje ei lågare " +
                            "uttaksgrad eller ta ut pensjonen på eit seinare tidspunkt.",
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

        title2 {
            text(
                Bokmal to "Du har rett til å klage",
                Nynorsk to "Du har rett til å klage",
                English to "You have the right to appeal"
            )
        }
        paragraph {
            text(
                Bokmal to "Hvis du mener vedtaket er feil, kan du klage innen seks uker fra den datoen du mottok vedtaket. " +
                        "Klagen skal være skriftlig. Du finner skjema og informasjon på ${Constants.KLAGE_URL}.",
                Nynorsk to "Om du meiner vedtaket er feil, kan du klage innan seks veker frå den datoen du fekk vedtaket. " +
                        "Klagen skal vere skriftleg. Du finn skjema og informasjon på ${Constants.KLAGE_URL}.",
                English to "If you think the decision is wrong, you may appeal the decision within six weeks of the date on which " +
                        "you received notice of the decision. Your appeal must be made in writing. You will find a form you can use and more " +
                        "information about appeals at ${Constants.KLAGE_URL}."
            )
        }

        paragraph {
            text(
                Bokmal to "I vedlegget får du vite mer om hvordan du går fram.",
                Nynorsk to "I vedlegget får du vite meir om korleis du går fram.",
                English to "The appendix includes information on how to proceed."
            )
        }

        title2 {
            text(
                Bokmal to "Du har rett til innsyn",
                Nynorsk to "Du har rett til innsyn",
                English to "You have the right to access your file"
            )
        }
        paragraph {
            text(
                Bokmal to "Du har rett til å se dokumentene i saken din. I vedlegget får du vite hvordan du går fram.",
                Nynorsk to "Du har rett til å sjå dokumenta i saka di. I vedlegget får du vite korleis du går fram.",
                English to "You have the right to access all documents pertaining to your case. The attachment includes information on how to proceed."
            )
        }

        includePhrase(Felles.HarDuSpoersmaal.alder)

    }
}

