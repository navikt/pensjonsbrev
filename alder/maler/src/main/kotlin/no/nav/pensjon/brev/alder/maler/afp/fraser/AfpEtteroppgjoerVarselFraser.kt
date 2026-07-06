package no.nav.pensjon.brev.alder.maler.afp.fraser

import no.nav.pensjon.brev.alder.maler.felles.Constants
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Year
import java.time.LocalDate

object AfpEtteroppgjoerVarselFraser {

    data class ForeloepigBeregningForMye(
        val erHelAfpHeleAaret: Expression<Boolean>,
        val erUttakIAaret: Expression<Boolean>,
        val erOpphoerIAaret: Expression<Boolean>,
        val erUttakOgOpphoerIAaret: Expression<Boolean>,
        val uttaksdato: Expression<LocalDate>,
        val opphorsdato: Expression<LocalDate?>,
        val oppgjoersAar: Expression<Year>,
        val formyebetalt: Expression<Kroner>,
    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            showIf(erHelAfpHeleAaret) {
                paragraph {
                    text(
                        bokmal {
                            +"Den foreløpige beregningen av pensjonen for " + oppgjoersAar.format() +
                                    " viser at du kan ha fått " + formyebetalt.format() +
                                    " for mye utbetalt i pensjon."
                        },
                        nynorsk {
                            +"Den førebelse berekninga av pensjonen for " + oppgjoersAar.format() +
                                    " viser at du kan ha fått " + formyebetalt.format() +
                                    " for mykje utbetalt i pensjon."
                        },
                    )
                }
            }
            showIf(erUttakIAaret) {
                paragraph {
                    text(
                        bokmal {
                            +"Den foreløpige beregningen av pensjonen for tidsrommet " + uttaksdato.format() +
                                    " til 31. desember " + oppgjoersAar.format() + " viser at du kan ha fått " +
                                    formyebetalt.format() + " for mye utbetalt i pensjon."
                        },
                        nynorsk {
                            +"Den førebelse berekninga av pensjonen for tidsrommet " + uttaksdato.format() +
                                    " til 31. desember " + oppgjoersAar.format() + " viser at du kan ha fått " +
                                    formyebetalt.format() + " for mykje utbetalt i pensjon."
                        },
                    )
                }
            }
            showIf(erOpphoerIAaret) {
                ifNotNull(opphorsdato) { opphor ->
                    paragraph {
                        text(
                            bokmal {
                                +"Den foreløpige beregningen av pensjonen for tidsrommet 1. januar " +
                                        oppgjoersAar.format() + " til " + opphor.format() + " viser at du kan ha fått " +
                                        formyebetalt.format() + " for mye utbetalt i pensjon."
                            },
                            nynorsk {
                                +"Den førebelse berekninga av pensjonen for tidsrommet 1. januar " +
                                        oppgjoersAar.format() + " til " + opphor.format() + " viser at du kan ha fått " +
                                        formyebetalt.format() + " for mykje utbetalt i pensjon."
                            },
                        )
                    }
                }
            }
            showIf(erUttakOgOpphoerIAaret) {
                ifNotNull(opphorsdato) { opphor ->
                    paragraph {
                        text(
                            bokmal {
                                +"Den foreløpige beregningen av pensjonen for tidsrommet " + uttaksdato.format() +
                                        " til " + opphor.format() + " viser at du kan ha fått " + formyebetalt.format() +
                                        " for mye utbetalt i pensjon."
                            },
                            nynorsk {
                                +"Den førebelse berekninga av pensjonen for tidsrommet " + uttaksdato.format() +
                                        " til " + opphor.format() + " viser at du kan ha fått " + formyebetalt.format() +
                                        " for mykje utbetalt i pensjon."
                            },
                        )
                    }
                }
            }
        }
    }

    object HjemmelForeloepig : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            title2 {
                text(
                    bokmal { +"Dette skjer videre i saken din" },
                    nynorsk { +"Dette skjer vidare i saka di" },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Hvis du ikke sender oss ny dokumentasjon, får du et vedtak og informasjon om hvordan du skal betale tilbake det du har fått for mye utbetalt."
                    },
                    nynorsk {
                        +"Om du ikkje sender oss ny dokumentasjon, får du eit vedtak og informasjon om korleis du skal betale tilbake det du har fått for mykje utbetalt."
                    },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Hvis du sender oss ny dokumentasjon på inntekt som skal holdes utenfor etteroppgjøret, får du et vedtak med ny beregning."
                    },
                    nynorsk {
                        +"Om du sender oss ny dokumentasjon på inntekt som skal haldast utanfor etteroppgjeret, får du eit vedtak med ny berekning."
                    },
                )
            }
        }
    }

    data class ToleransebeloepForeloepig(
        val forventetInntekt: Expression<Kroner>,
        val oppgjoersAar: Expression<Year>,
        val toleranseBeloep: Expression<Kroner>,
    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                text(
                    bokmal {
                        +"Ved beregning av pensjonen din la vi til grunn at du ville ha en forventet " +
                                "arbeidsinntekt på " + forventetInntekt.format() + ". Etter våre nye beregninger har " +
                                "du hatt en arbeidsinntekt i den perioden du har mottatt AFP som er høyere enn den " +
                                "arbeidsinntekten som ble lagt til grunn ved utbetalingen av pensjon for " +
                                oppgjoersAar.format() + ". Denne forskjellen er større enn toleransebeløpet som i " +
                                oppgjoersAar.format() + " var på " + toleranseBeloep.format() + ". Pensjonen din er derfor beregnet på nytt og avregnet " +
                                "mot den pensjonen du allerede har fått utbetalt i perioden."
                    },
                    nynorsk {
                        +"Ved berekninga av pensjonen din la vi til grunn at du ville ha ei forventa " +
                                "arbeidsinntekt på " + forventetInntekt.format() + ". Etter dei nye berekningane våre " +
                                "har du hatt ei arbeidsinntekt i den perioden du har fått AFP som er høgare enn " +
                                "den arbeidsinntekta som blei lagd til grunn ved utbetalinga av pensjon for " +
                                oppgjoersAar.format() + ". Denne forskjellen er større enn toleransebeløpet som i " +
                                oppgjoersAar.format() + " var på " + toleranseBeloep.format() + ". Pensjonen din er derfor berekna på nytt og avrekna mot " +
                                "den pensjonen du allereie har fått utbetalt i perioden."
                    },
                )
            }
        }
    }

    data class NaarFristenHarGaattUt(
        val oppgjoersAar: Expression<Year>,
    ) : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            paragraph {
                text(
                    bokmal {
                        +"Når fristen for å sende inn dokumentasjon har gått ut, gjør Nav et endelig " +
                                "vedtak om hvor høy AFP du hadde rett til i " + oppgjoersAar.format() + ". Hvis " +
                                "beregningen fortsatt viser at du har fått utbetalt for mye AFP, må du betale tilbake det som er " +
                                "utbetalt for mye. Du får nærmere informasjon om hvordan " +
                                "du betaler tilbake i det samme vedtaket. Innbetalt skatt vil bli trukket " +
                                "fra beløpet vi krever tilbake."
                    },
                    nynorsk {
                        +"Når fristen for å sende inn dokumentasjon har gått ut, gjer Nav eit endeleg " +
                                "vedtak om kor høg AFP du hadde rett til i " + oppgjoersAar.format() + ". Dersom " +
                                "berekninga framleis viser at du har fått utbetalt for mykje AFP, må du betale tilbake det som er " +
                                "utbetalt for mykje. Du får nærmare informasjon om korleis " +
                                "du betaler tilbake i det same vedtaket. Innbetalt skatt vil bli trekt " +
                                "frå beløpet vi krev tilbake."
                    },
                )
            }
        }
    }

    object DuHarRettTil : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            title2 {
                text(

                    bokmal { +"Du har rett til innsyn i saken din" },
                    nynorsk { +"Du har rett til innsyn i saka di" }
                )
            }

            paragraph {
                text(
                    bokmal {
                        +"Du har rett til å se dokumentene i saken din. Dette følger av forvaltningsloven § 18."
                    },
                    nynorsk {
                        +"Du har rett til å sjå dokumenta i saka di. Dette følgjer av forvaltningslova § 18."
                    },
                )
            }

            title2 {
                text(

                    bokmal { +"Du har rettigheter knyttet til personopplysningene dine" },
                    nynorsk { +"Du har rettar knytt til personopplysningane dine" }
                )
            }

            paragraph {
                text(
                    bokmal {
                        +"Du finner informasjon om hvordan Nav behandler personopplysningene dine, og hvilke " +
                                "rettigheter du har, på ${Constants.PERSONVERNERKLAERING_URL}."
                    },
                    nynorsk {
                        +"Du finn informasjon om korleis Nav behandlar personopplysningane dine og kva rettar du " +
                                "har på ${Constants.PERSONVERNERKLAERING_URL}."
                    },
                )
            }

            title2 {
                text(
                    bokmal { +"Du har rett til å få hjelp fra andre" },
                    nynorsk { +"Du har rett til å få hjelp frå andre" }
                )
            }

            paragraph {
                text(
                    bokmal {
                        +"Du kan be om hjelp fra andre under hele saksbehandlingen, for eksempel fra en advokat, rettshjelper, " +
                                "en organisasjon du er medlem av, eller en myndig person over 18 år. Dette følger av " +
                                "forvaltningsloven § 12. Hvis den som hjelper deg ikke er advokat, må du gi denne personen " +
                                "skriftlig fullmakt. Bruk skjemaet du finner på ${Constants.FULLMAKT_URL}. Ta kontakt på telefon " +
                                "${Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON} hvis du ikke kan bruke det digitale skjemaet."
                    },
                    nynorsk {
                        +"Du kan be om hjelp frå andre under heile saksbehandlinga, til dømes frå ein advokat, rettshjelpar, " +
                                "ein organisasjon du er medlem av eller ein myndig person over 18 år. Dette følgjer av " +
                                "forvaltningslova § 12. Om den som hjelper deg ikkje er advokat, må du gi denne personen " +
                                "skriftleg fullmakt. Bruk skjemaet du finn på ${Constants.FULLMAKT_URL}. Ta kontakt på telefon " +
                                "${Constants.NAV_KONTAKTSENTER_TELEFON_PENSJON} om du ikkje kan bruke det digitale skjemaet."
                    },
                )
            }
        }
    }
}
