package no.nav.pensjon.brev.maler.alder

import brev.avslag.gradsendring.fraser.AvslagHjemler
import brev.felles.Constants
import brev.felles.HarDuSpoersmaal
import brev.felles.aarOgMaanederFormattert
import no.nav.pensjon.brev.api.model.maler.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.maler.alderApi.NormertPensjonsalder
import no.nav.pensjon.brev.maler.alder.vedlegg.opplysningerBruktIBeregningenAP2016Vedlegg
import no.nav.pensjon.brev.maler.alder.vedlegg.opplysningerBruktIBeregningenAP2025Vedlegg
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
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
    val avtaleland: Expression<String?>,
    val visInfoOmUttakFoer67: Expression<Boolean>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title2 {
            text(
                bokmal { + "Vedtak" },
                nynorsk { + "Vedtak" },
                english { + "Decision" }
            )
        }
        paragraph {
            text(
                bokmal { +
                        "For å ta ut alderspensjon før du er " + normertPensjonsalder.aarOgMaanederFormattert() + ", må du ha høy nok pensjonsopptjening. " +
                        "Du har for lav pensjonsopptjening til at du kan ta ut " + uttaksgrad.format() +
                        " prosent pensjon fra " + virkFom.format() + ". Derfor har vi avslått søknaden din." },
                nynorsk { +
                        "For å ta ut alderspensjon før du er " + normertPensjonsalder.aarOgMaanederFormattert() + ", må du ha høg nok pensjonsopptjening. " +
                        "Du har for låg pensjonsopptening til at du kan ta ut " + uttaksgrad.format() +
                        " prosent pensjon frå " + virkFom.format() + ". Derfor har vi avslått søknaden din." },
                english { +
                        "Your pension accrual must be sufficient to start drawing retirement pension before you turn " + normertPensjonsalder.aarOgMaanederFormattert() + ". " +
                        "Your accumulated pension capital is not sufficient for you to draw a retirement pension at " + uttaksgrad.format() +
                        " percent from " + virkFom.format() + ". Therefore, we have declined your application." },
            )
        }

        showIf(visInfoOmUttakFoer67) {
            paragraph {
                text(
                    bokmal { +"Våre beregninger viser at du ikke har rett til å ta ut alderspensjonen din før du blir 67 år." },
                    nynorsk { +"Våre berekningar viser at du ikkje har rett til å ta ut alderspensjon før du blir 67 år." },
                    english { +"Our calculations show that you are not eligible for retirement pension before the age of 67." }
                )
                text(
                    bokmal { + " Du må sende oss en ny søknad når du ønsker å ta ut alderspensjon, omtrent fire måneder før du blir 67 år." },
                    nynorsk { + " Du må sende oss ein ny søknad når du ønskjer å ta ut alderspensjon, om lag fire månader før du blir 67 år." },
                    english { + " You must submit a new application when you want to start drawing your retirement pension, approximately 4 moths before you turn 67." }
                )
            }
        }

        includePhrase(AvslagHjemler(regelverkType, harEOSLand, prorataBruktIBeregningen, avtaleland))

        title2 {
            text(
                bokmal { + "Slik har vi beregnet" },
                nynorsk { + "Slik har vi berekna" },
                english { + "The calculations made are as follows" }
            )
        }
        paragraph {
            showIf(uttaksgrad.equalTo(100)) {
                list {
                    item {
                        text(
                            bokmal { + "For å kunne ta ut alderspensjon før du blir " + normertPensjonsalder.aarOgMaanederFormattert() +
                                    ", må pensjonen din minst være " + minstePensjonssats.format() + " i året." },
                            nynorsk { + "For å kunne ta ut alderspensjon før du blir " + normertPensjonsalder.aarOgMaanederFormattert() +
                                    ", må pensjonen din minst vere " + minstePensjonssats.format() + " i året." },
                            english { + "For you to be eligible for retirement pension before the age of " + normertPensjonsalder.aarOgMaanederFormattert() +
                                    ", your retirement pension must be, at minimum, " + minstePensjonssats.format() + " a year." },
                        )

                        showIf(prorataBruktIBeregningen) {
                            text(
                                bokmal { + " Vi har tatt hensyn til at du også har trygdetid fra land som Norge har trygdeavtale med." },
                                nynorsk { + " Vi har tatt omsyn til at du også har trygdetid frå land som Noreg har trygdeavtale med." },
                                english { + " We have taken into account any periods of national insurance coverage" +
                                        " that you may have in countries with which Norway has a social security agreement." }
                            )
                        }
                    }
                    item {
                        text(
                            bokmal { + "Hvis du hadde tatt ut " + uttaksgrad.format() + " prosent alderspensjon fra "
                                    + virkFom.format() + ", ville du fått " + totalPensjon.format() + " årlig i pensjon. " },
                            nynorsk { + "Om du hadde tatt ut " + uttaksgrad.format() + " prosent alderspensjon frå "
                                    + virkFom.format() + ", ville du fått " + totalPensjon.format() + " årleg i pensjon. " },
                            english { + "If you draw a retirement pension of " + uttaksgrad.format() + " percent from "
                                    + virkFom.format() + ", your retirement pension is calculated to be " + totalPensjon.format() + " a year. " },
                        )
                        showIf(afpBruktIBeregning) {
                            text(
                                bokmal { + "I denne beregningen har vi inkludert AFP." },
                                nynorsk { + "I denne berekninga har vi inkludert AFP." },
                                english { + "This amount includes contractual pension (AFP)." }
                            )
                        }
                    }
                }
            }.orShow {
                list {
                    item {
                        text(
                            bokmal { + "For å kunne ta ut alderspensjon før du blir " + normertPensjonsalder.aarOgMaanederFormattert() +
                                    ", må pensjonen din minst være " + minstePensjonssats.format() + " i året." },
                            nynorsk { + "For å kunne ta ut alderspensjon før du blir " + normertPensjonsalder.aarOgMaanederFormattert() +
                                    ", må pensjonen din minst vere " + minstePensjonssats.format() + " i året." },
                            english { + "For you to be eligible for retirement pension before the age of " + normertPensjonsalder.aarOgMaanederFormattert() +
                                    ", your retirement pension must be, at minimum, " + minstePensjonssats.format() + " a year." },
                        )

                        showIf(prorataBruktIBeregningen) {
                            text(
                                bokmal { + " Vi har tatt hensyn til at du også har trygdetid fra land som Norge har trygdeavtale med." },
                                nynorsk { + " Vi har tatt omsyn til at du også har trygdetid frå land som Noreg har trygdeavtale med." },
                                english { + " We have taken into account any periods of national insurance coverage" +
                                        " that you may have in countries with which Norway has a social security agreement." }
                            )
                        }
                    }

                    item {
                        text(
                            bokmal { + "Vi beregner den delen du ønsker å ta ut nå og hva du ville ha fått hvis du tar resten av pensjonen ved " + normertPensjonsalder.aarOgMaanederFormattert() + ". " +
                                    "Hvis du hadde tatt ut " + uttaksgrad.format() + " prosent alderspensjon fra " + virkFom.format() +
                                    ", ville du fått " + totalPensjon.format() + " årlig i full pensjon når du blir " +
                                    normertPensjonsalder.aarOgMaanederFormattert() + ". " },
                            nynorsk { + "Vi reknar ut den delen du ønsker å ta ut nå og kva du ville ha fått om du tar resten av pensjonen ved " + normertPensjonsalder.aarOgMaanederFormattert() + ". " +
                                    "Om du hadde tatt ut " + uttaksgrad.format() + " prosent alderspensjon frå " + virkFom.format() +
                                    ", ville du fått " + totalPensjon.format() + " årleg i full pensjon når du blir " +
                                    normertPensjonsalder.aarOgMaanederFormattert() + ". " },
                            english { + "We calculate the part you wish to withdraw now and what you would have received if you take the rest of the pension at age " + normertPensjonsalder.aarOgMaanederFormattert() + ". " +
                                    "If you draw a retirement pension of " + uttaksgrad.format() + " percent from the date you requested, " +
                                    "your full retirement pension is calculated to be " + totalPensjon.format() + " a year at age " +
                                    normertPensjonsalder.aarOgMaanederFormattert() + ". " },
                        )
                        showIf(afpBruktIBeregning) {
                            text(
                                bokmal { + "I denne beregningen har vi inkludert AFP." },
                                nynorsk { + "I denne berekninga har vi inkludert AFP." },
                                english { + "This amount includes contractual pension (AFP)." }
                            )
                        }
                    }
                }
            }
        }

        paragraph {
            text(
                bokmal { + "Beregningen er uavhengig av sivilstanden din." },
                nynorsk { + "Berekninga er uavhengig av sivilstanden din." },
                english { + "The calculation is independent of your marital status." }
            )
        }
        paragraph {
            text(
                bokmal { + "I vedlegget " },
                nynorsk { + "I vedlegget " },
                english { + "In the appendix " }
            )
            showIf(regelverkType.isOneOf(AlderspensjonRegelverkType.AP2025)) {
                namedReference(opplysningerBruktIBeregningenAP2025Vedlegg)
            }
            showIf(regelverkType.isOneOf(AlderspensjonRegelverkType.AP2016)) {
                namedReference(opplysningerBruktIBeregningenAP2016Vedlegg)
            }
            text(
                bokmal { + " finner du en tabell som viser hvilke opplysninger vi har brukt." },
                nynorsk { + " finn du ein tabell som viser kva opplysningar vi har brukt." },
                english { + " you will find a table showing the data we have used." }
            )
        }

        showIf(visInfoOmUttakFoer67.not()) {
            title2 {
                text(
                    bokmal { + "Se når du kan ta ut alderspensjon" },
                    nynorsk { + "Sjå når du kan ta ut alderspensjon" },
                    english { + "Find out when you can start drawing retirement pension" }
                )
            }
            paragraph {
                showIf(borINorge) {
                    text(
                        bokmal { + "Selv om vi har avslått denne søknaden, kan du likevel ha rett til å ta ut alderspensjon før du blir " +
                                normertPensjonsalder.aarOgMaanederFormattert() + ". " +
                                "Da må du kunne velge en lavere uttaksgrad eller ta ut pensjonen senere. " +
                                "Logg inn på ${Constants.DIN_PENSJON_URL} for å sjekke når du tidligst kan ta ut alderspensjon. " +
                                "Du kan også se hva pensjonen din blir, avhengig av når og hvor mye du tar ut." },
                        nynorsk { + "Sjølv om vi har avslått denne søknaden, kan du likevel ha rett til å ta ut alderspensjon før du blir " +
                                normertPensjonsalder.aarOgMaanederFormattert() + ". " +
                                "Då må du kunne velje ein lågare uttaksgrad eller ta ut pensjonen seinare. " +
                                "Logg inn på ${Constants.DIN_PENSJON_URL} for å sjekke når du tidlegast kan ta ut alderspensjon. " +
                                "Du kan også sjå kva pensjonen din blir, avhengig av når og kor mykje du tar ut." },
                        english { + "Even though we have rejected this application, you may still be eligible to withdraw your retirement pension before you turn " +
                                normertPensjonsalder.aarOgMaanederFormattert() + " old. " +
                                "You must then be able to choose a lower withdrawal rate or take out the pension later. " +
                                "Log in to ${Constants.DIN_PENSJON_URL} to check the earliest date you can withdraw your retirement pension. " +
                                "You can also see what your pension will be, depending on when and how much you withdraw." },
                    )
                }.orShow {
                    text(
                        bokmal { + "Logg inn på ${Constants.DIN_PENSJON_URL} for å sjekke når du tidligst kan ta ut alderspensjon. " +
                                "Der kan du også se hva pensjonen din blir avhengig av når og hvor mye du tar ut. Du kan " +
                                "logge inn med BankID, Buypass eller Commfides. Kontakt oss gjerne på telefon" +
                                " +47 ${Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON} hvis du trenger hjelp til dette. " },
                        nynorsk { + "Logg inn på ${Constants.DIN_PENSJON_URL} for å sjekke når du tidlegast kan ta ut " +
                                "alderspensjon. Der kan du også sjå kva pensjonen din blir, avhengig av når og kor mykje du " +
                                "tar ut. Du kan logge inn med BankID, Buypass eller Commfides. Kontakt oss gjerne på telefon" +
                                " +47 ${Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON} om du treng hjelp til dette. " },
                        english { + "Log in to ${Constants.DIN_PENSJON_URL} to check the earliest date you can withdraw your retirement pension. " +
                                "There, you can also see how your pension will vary depending on when and how much you choose to withdraw. " +
                                "You can log in using BankID, Buypass, or Commfides. If you need assistance, please contact us by phone at +47 ${Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON}. " +
                                "Even though we have rejected this application, you may still have the right to withdraw your retirement pension before turning " + normertPensjonsalder.aarOgMaanederFormattert() + ". " +
                                    "To do so, you would need to choose a lower withdrawal rate or postpone your pension withdrawal to a later date." },
                    )
                    text(
                        bokmal { + "Selv om vi har avslått denne søknaden, kan du likevel ha rett til å ta ut alderspensjon før " +
                                "du blir " + normertPensjonsalder.aarOgMaanederFormattert() + ". Da må du velge en lavere " +
                                "uttaksgrad eller ta ut pensjonen på et senere tidspunkt." },
                        nynorsk { + "Sjølv om vi har avslått denne søknaden, kan du likevel ha rett til å ta ut alderspensjon " +
                                "før du blir " + normertPensjonsalder.aarOgMaanederFormattert() + ". Da må du velje ei lågare " +
                                "uttaksgrad eller ta ut pensjonen på eit seinare tidspunkt." },
                        english { + "" },
                    )
                }
            }

            paragraph {
                text(
                    bokmal { + "Du må sende oss en ny søknad når du ønsker å ta ut alderspensjon. " +
                            "En eventuell endring kan tidligst skje måneden etter at vi har mottatt søknaden." },
                    nynorsk { + "Du må sende oss ein ny søknad når du ønskjer å ta ut alderspensjon. " +
                            "Ei eventuell endring kan tidlegast skje månaden etter at vi har fått søknaden." },
                    english { + "You must submit a new application when you want to start drawing your retirement pension. " +
                            "Any change will be implemented at the earliest the month after we have received the application." }
                )
            }
        }

        title2 {
            text(
                bokmal { + "Du har rett til å klage" },
                nynorsk { + "Du har rett til å klage" },
                english { + "You have the right to appeal" }
            )
        }
        paragraph {
            text(
                bokmal { + "Hvis du mener vedtaket er feil, kan du klage innen seks uker fra den datoen du mottok vedtaket. " +
                        "Klagen skal være skriftlig. Du finner skjema og informasjon på ${Constants.KLAGE_URL}." },
                nynorsk { + "Om du meiner vedtaket er feil, kan du klage innan seks veker frå den datoen du fekk vedtaket. " +
                        "Klagen skal vere skriftleg. Du finn skjema og informasjon på ${Constants.KLAGE_URL}." },
                english { + "If you think the decision is wrong, you may appeal the decision within six weeks of the date on which " +
                        "you received notice of the decision. Your appeal must be made in writing. You will find a form you can use and more " +
                        "information about appeals at ${Constants.KLAGE_URL}." }
            )
        }

        paragraph {
            text(
                bokmal { + "I vedlegget får du vite mer om hvordan du går fram." },
                nynorsk { + "I vedlegget får du vite meir om korleis du går fram." },
                english { + "The appendix includes information on how to proceed." }
            )
        }

        title2 {
            text(
                bokmal { + "Du har rett til innsyn" },
                nynorsk { + "Du har rett til innsyn" },
                english { + "You have the right to access your file" }
            )
        }
        paragraph {
            text(
                bokmal { + "Du har rett til å se dokumentene i saken din. I vedlegget får du vite hvordan du går fram." },
                nynorsk { + "Du har rett til å sjå dokumenta i saka di. I vedlegget får du vite korleis du går fram." },
                english { + "You have the right to access all documents pertaining to your case. The attachment includes information on how to proceed." }
            )
        }

        includePhrase(HarDuSpoersmaal.alder)

    }
}

