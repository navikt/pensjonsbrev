package no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad

import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.aktivitetsplikt.NasjonalEllerUtland

class OmstillingsstoenadAktivitetspliktFraser {

    data class DuMaaMeldeFraOmEndringer(
        val nasjonalEllerUtland: Expression<NasjonalEllerUtland>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Du må melde fra om endringer",
                    Nynorsk to "Du må melde frå om endringar",
                    English to "You must report any changes",
                )
            }

            paragraph {
                text(
                    Bokmal to "Vi minner om plikten din til å melde fra om endringer som kan ha betydning for " +
                            "stønaden du får fra oss. Du må melde fra hvis",
                    Nynorsk to "Vi minner om at du pliktar å melde frå om endringar som kan ha betydning for " +
                            "stønaden du får frå oss. Du må melde frå dersom",
                    English to "Please remember that you are obligated to notify us about changes that may have significance " +
                            "to the benefits or allowances you receive from NAV. You must report any changes if",
                )

                list {
                    item {
                        text(
                            Bokmal to "arbeidsinntekten din endrer seg",
                            Nynorsk to "arbeidsinntekta di endrar seg",
                            English to "your income from work changes",
                        )
                    }
                    item {
                        text(
                            Bokmal to "arbeidssituasjonen din endrer seg",
                            Nynorsk to "arbeidssituasjonen din endrar seg",
                            English to "your working situation changes",
                        )
                    }
                    item {
                        text(
                            Bokmal to "du får innvilget andre stønader fra NAV",
                            Nynorsk to "du får innvilga andre stønader frå NAV",
                            English to "you are granted other benefits from NAV",
                        )
                    }
                    item {
                        text(
                            Bokmal to "du ikke lenger er arbeidssøker",
                            Nynorsk to "du er ikkje lenger arbeidssøkjar",
                            English to "you are no longer a job seeker",
                        )
                    }
                    item {
                        text(
                            Bokmal to "du endrer, avbryter eller reduserer omfanget av utdanningen din",
                            Nynorsk to "du endrar, avbryt eller reduserer omfanget av utdanninga di",
                            English to "you change, discontinue or reduce the scope of your education/training",
                        )
                    }
                    item {
                        text(
                            Bokmal to "du gifter deg",
                            Nynorsk to "du giftar deg",
                            English to "you get married",
                        )
                    }
                    item {
                        text(
                            Bokmal to "du blir samboer med en du har felles barn med eller tidligere har vært gift med",
                            Nynorsk to "du blir sambuar med nokon du har felles barn med eller tidlegare har vore gift med",
                            English to "you become a cohabiting partner with someone you have common children with or were previously married to",
                        )
                    }
                    item {
                        text(
                            Bokmal to "du får felles barn med ny samboer",
                            Nynorsk to "du får barn med ny sambuar",
                            English to "you are expecting a child with a new cohabiting partner",
                        )
                    }
                    item {
                        text(
                            Bokmal to "du skal oppholde deg utenfor Norge i en periode på mer enn seks måneder eller skal flytte til et annet land",
                            Nynorsk to "du skal opphalde deg utanfor Noreg i meir enn seks månader, eller du skal flytte til eit anna land",
                            English to "you intend to stay outside of Norway for a period of more than six months or intend to move to another country",
                        )
                    }
                    item {
                        text(
                            Bokmal to "du får opphold i institusjon i minst tre måneder, eller fengsel i minst to måneder",
                            Nynorsk to "du skal vere på ein institusjon i minst tre månader, eller i fengsel i minst to månader",
                            English to "you are placed in an institution for at least three months, or jail for at least two months",
                        )
                    }
                }
            }

            paragraph {
                text(
                    Bokmal to "Du kan melde fra om endringer på følgende måter:",
                    Nynorsk to "Du kan melde frå om endringar på følgjande måtar:",
                    English to "You can report changes in the following ways:",
                )

                val postadresse = ifElse(nasjonalEllerUtland.equalTo(NasjonalEllerUtland.UTLAND), Constants.POSTADRESSE, Constants.Utland.POSTADRESSE)

                list {
                    item {
                        text(
                            Bokmal to "sende en melding på ${Constants.SKRIVTILOSS_URL} (her får du ikke lagt ved dokumentasjon)",
                            Nynorsk to "Send ei melding på ${Constants.SKRIVTILOSS_URL} (her får du ikkje lagt ved dokumentasjon).",
                            English to "send us a message online: ${Constants.SKRIVTILOSS_URL} (you cannot attach documentation from this page)",
                        )
                    }
                    item {
                        text(
                            Bokmal to "ettersende dokumentasjon fra søknad om omstillingsstønad. " +
                                    "Dette gjør du ved å gå inn på ${Constants.ETTERSENDE_OMS_URL}",
                            Nynorsk to "Ettersend dokumentasjon frå søknad om omstillingsstønad. " +
                                    "Dette gjer du ved å gå inn på ${Constants.ETTERSENDE_OMS_URL}.",
                            English to "send more documentation later from your adjustment allowance application. " +
                                    "Do this online here: ${Constants.ETTERSENDE_OMS_URL}.",
                        )
                    }
                    item {
                        textExpr(
                            Bokmal to "sende brev til ".expr() + postadresse,
                            Nynorsk to "Send brev til ".expr() + postadresse,
                            English to "send a letter to ".expr() + postadresse,
                        )
                    }
                }
            }

            showIf(nasjonalEllerUtland.equalTo(NasjonalEllerUtland.UTLAND)) {
                paragraph {
                    text(
                        Bokmal to "Har du ikke BankID eller annen innloggingsmulighet til vår hjemmeside ${Constants.NAV_URL}, må du sende dokumentasjonen i posten.",
                        Nynorsk to "Har du ikkje BankID eller andre moglegheiter til å logge på heimesida vår ${Constants.NAV_URL}, må du sende dokumentasjon per post.",
                        English to "Please send documentation as normal post if you do not use BankID or another login option.",
                    )
                }
            }

            paragraph {
                text(
                    Bokmal to "Hvis du ikke melder fra om endringer og får utbetalt for mye stønad, kan stønad som er utbetalt feil kreves tilbake.",
                    Nynorsk to "Dersom du får utbetalt for mykje stønad fordi du ikkje har meldt frå om endringar, kan vi krevje at du betaler tilbake det du ikkje hadde rett på.",
                    English to "If you fail to report changes and/or are paid too much benefits/allowance, NAV has the right to collect the incorrect amount.",
                )
            }

        }
    }

    object HarDuHelseutfordringer : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Har du helseutfordringer?",
                    Nynorsk to "Har du helseutfordringar?",
                    English to "Do you have health problems?",
                )
            }

            paragraph {
                text(
                    Bokmal to "Hvis du har helseutfordringer, kan du undersøke mulighetene for andre ytelser " +
                            "eller støtteordninger ved ditt lokale NAV-kontor og på ${Constants.HELSE_URL}.",
                    Nynorsk to "Viss du har helseutfordringar, kan du undersøkje moglegheitene for andre ytingar " +
                            "eller støtteordningar ved ditt lokale NAV-kontor og på ${Constants.HELSE_URL}.",
                    English to "If you are faced with difficult health challenges, you can investigate the possibilities " +
                            "for other benefits or support schemes through your local NAV office, or check out various " +
                            "opportunities online: ${Constants.HELSE_URL}.",
                )
            }
        }
    }

    object TrengerDuHjelpTilAaFaaNyJobb : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Trenger du hjelp til å få ny jobb eller jobbe mer?",
                    Nynorsk to "Treng du hjelp med å få ny jobb eller jobbe meir?",
                    English to "Do you need help getting a new job or more work?",
                )
            }

            paragraph {
                text(
                    Bokmal to "NAV tilbyr ulike tjenester og støtteordninger for deg som trenger hjelp til å få jobb. " +
                            "Du kan finne flere jobbsøkertips og informasjon på ${Constants.ARBEID_URL}.",
                    Nynorsk to "NAV tilbyr ulike tenester og støtteordningar for deg som treng hjelp med å kome i " +
                            "arbeid. Du finn fleire jobbsøkjartips og informasjon på ${Constants.ARBEID_URL}.",
                    English to "The Norwegian Labour and Welfare Administration (NAV) offers various services and " +
                            "support schemes for those who need help finding a job. You can find more job seeker's tips " +
                            "and information online: ${Constants.ARBEID_URL}.",
                )
            }

            paragraph {
                text(
                    Bokmal to "På nettsiden arbeidsplassen.no kan du søke etter ledige stillinger i ditt område " +
                            "eller andre steder i landet. Her kan du også opprette varsler på e-post for å få " +
                            "meldinger om jobber som passer deg. Du finner også mange gode råd og tips for " +
                            "jobbsøking på ${Constants.FINN_JOBBENE_URL}.",
                    Nynorsk to "På nettsida arbeidsplassen.no kan du søkje etter ledige stillingar i nærområdet " +
                            "ditt eller andre stader i landet. Her kan du også opprette varsel på e-post for å få " +
                            "meldingar om jobbar som passar deg. Du finn i tillegg mange gode råd og tips for " +
                            "jobbsøking på ${Constants.FINN_JOBBENE_URL}.",
                    English to "To apply for vacant positions in your area or other place around the country, you " +
                            "can see this webpage: arbeidsplassen.no. You can also set up e-mail notifications to receive " +
                            "information and offers about jobs that are suited to your situation. There are also many " +
                            "good tips on looking for work here: ${Constants.FINN_JOBBENE_URL}.",
                )
            }

            paragraph {
                text(
                    Bokmal to "Trenger du hjelp til å komme i jobb eller ønsker å øke arbeidsinnsatsen din, " +
                            "kan du få veiledning og støtte fra oss. Vi kan fortelle deg om ulike muligheter i " +
                            "arbeidsmarkedet eller snakke med deg om nødvendig utdanning eller andre tiltak som " +
                            "kan hjelpe deg med å komme i arbeid.",
                    Nynorsk to "Viss du treng hjelp med å kome i jobb, eller ønskjer å auke arbeidsinnsatsen din, " +
                            "kan du få rettleiing og støtte frå oss. Vi kan informere deg om ulike moglegheiter i " +
                            "arbeidsmarknaden, eller snakke med deg om nødvendig utdanning eller andre tiltak som kan " +
                            "hjelpe deg med å kome i arbeid.",
                    English to "If you need help joining the work force or want to increase your work hours, you " +
                            "can get guidance and support from us. We can tell you about various opportunities in the " +
                            "labour market or talk to you about necessary education or other measures that can help you find work.",
                )
            }
        }
    }

    data class FellesInfoOmInntektsendring(
        val redusertEtterInntekt: Expression<Boolean>,
    ) : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Bokmal to "Du må melde fra hvis inntekten din endrer seg",
                    Nynorsk to "Meld frå dersom inntekta di endrar seg",
                    English to "You must report any changes to your income",
                )
            }

            showIf(redusertEtterInntekt) {
                paragraph {
                    text(
                        Bokmal to "For at du skal motta korrekt utbetaling, er det viktig at du informerer oss hvis inntekten din endrer seg.",
                        Nynorsk to "For at du skal få rett utbetaling, er det viktig at du gir oss beskjed viss inntekta di endrar seg.",
                        English to "To receive the correct amount, you are obligated to inform us about any changes to your income.",
                    )
                }
            } orShow {
                paragraph {
                    text(
                        Bokmal to "For at du skal motta korrekt utbetaling, er det viktig at du informerer oss hvis " +
                                "du får en forventet årsinntekt som vil overstige et halvt grunnbeløp. Dette er per i dag 62 014 kroner. " +
                                "Grunnbeløpet blir justert hvert år fra 1. mai.",
                        Nynorsk to "For at du få rett utbetaling, er det viktig at du gir oss beskjed viss du får ei " +
                                "forventa årsinntekt som vil overstige eit halvt grunnbeløp. Dette er per i dag 62 014 kroner. " +
                                "Grunnbeløpet blir justert kvart år frå 1. mai.",
                        English to "To receive the correct amount, you are obligated to inform us about any changes to " +
                                "your anticipated annual income that exceeds one half of the basic amount. This is currently " +
                                "NOK 62 014. The basic amount is adjusted on 1 May each year.",
                    )
                }
            }

            paragraph {
                text(
                    Bokmal to "Hvis inntekten din har endret seg, må du oppgi ny forventet brutto inntekt for " +
                            "inneværende år. Gjelder inntektsendringen fra før du ble innvilget stønaden, skal du også " +
                            "oppgi inntekt fra januar til og med måneden før du fikk innvilget stønaden.",
                    Nynorsk to "Dersom inntekta di har endra seg, må du oppgi ny forventa brutto inntekt for " +
                            "inneverande år. Viss inntektsendringa gjeld frå før du blei innvilga stønaden, oppgir du i " +
                            "tillegg inntekt frå januar til og med månaden før du fekk innvilga stønaden.",
                    English to "If your income has changed, you must state the new anticipated gross income for the " +
                            "current year. If the change in income applies from before you were granted the allowance, " +
                            "you must also state your income from January up to and including the month before you were " +
                            "granted the allowance.",
                )
            }

            paragraph {
                text(
                    Bokmal to "Vi vil justere stønaden fra måneden etter du gir beskjed. Meld derfor fra snarest " +
                            "mulig for å få mest mulig riktig utbetalt omstillingsstønad, slik at etteroppgjøret blir " +
                            "så riktig som mulig. Du kan finne mer informasjon om etteroppgjør på ${Constants.OMS_ETTEROPPGJOER_URL}.",
                    Nynorsk to "Vi vil justere stønaden frå månaden etter du gir beskjed. Meld difor frå snarast " +
                            "råd, slik at du får utbetalt omstillingsstønaden du har krav på, og etteroppgjeret blir " +
                            "mest mogleg rett. Du finn meir informasjon om etteroppgjer på ${Constants.OMS_ETTEROPPGJOER_URL}.",
                    English to "We will then adjust your allowance starting in the month after you inform us of the change. " +
                            "It is therefore important to notify us as soon as possible to receive the correct amount of adjustment " +
                            "allowance, so that any final settlement will be as correct as possible. You can find more information " +
                            "about final settlements online: ${Constants.OMS_ETTEROPPGJOER_URL}.",
                )
            }
        }
    }

}