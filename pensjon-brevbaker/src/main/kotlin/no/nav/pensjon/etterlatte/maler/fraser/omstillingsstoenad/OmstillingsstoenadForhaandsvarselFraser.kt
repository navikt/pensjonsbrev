package no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants

class OmstillingsstoenadForhaandsvarselFraser {

    object ForhaandsvarselRedigerbart : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                text(
                    Language.Bokmal to "Som følge av vedtaket som er gjort har du fått <beløp> kroner for " +
                            "mye utbetalt i omstillingsstønad fra og med <dato> til og med <dato>.",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Før vi avgjør om du skal betale tilbake, har du rett til å uttale deg. " +
                            "Dette må du gjøre innen 14 dager etter at du har fått dette varselet.",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Dette er kun et varsel om at vi vurderer å kreve tilbake det " +
                            "feilutbetalte beløpet. Det er ikke et vedtak om tilbakekreving.",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Dersom vi vedtar at du må betale tilbake hele eller deler av det " +
                            "feilutbetalte beløpet, trekker vi fra skatten på beløpet vi krever tilbake.",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
            }

            title2 {
                text(
                    Language.Bokmal to "Dette har skjedd",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "<Fritekst. Eks.: Vi innvilget omstillingsstønad til deg selv om " +
                            "vilkårene ikke var oppfylt. Dette har medført en feilutbetaling til deg. > ",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
            }

            title2 {
                text(
                    Language.Bokmal to "Dette legger vi vekt på i vurderingen vår",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
            }

            paragraph {
                text(
                    Language.Bokmal to "Hvis NAV har forårsaket feilutbetalingen, kan vi kreve tilbake " +
                            "feilutbetalt beløp hvis du forstod eller burde forstått at utbetalingen du fikk, " +
                            "skyldtes en feil. Vi legger blant annet vekt på",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
                list {
                    item {
                        text(
                            Language.Bokmal to "hvilken informasjon du har fått fra oss, og kvaliteten på denne",
                            Language.Nynorsk to "",
                            Language.English to "",
                        )
                    }
                    item {
                        text(
                            Language.Bokmal to "hvordan du har opptrådt i forbindelse med saken din",
                            Language.Nynorsk to "",
                            Language.English to "",
                        )
                    }
                }
            }

            paragraph {
                text(
                    Language.Bokmal to "Vi kan også kreve tilbake feilutbetalt beløp hvis feilutbetalingen " +
                            "skyldes at du uaktsomt har gitt mangelfulle eller feil opplysninger. Vi legger blant " +
                            "annet vekt på ",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
                list {
                    item {
                        text(
                            Language.Bokmal to "om du har fått god informasjon fra oss om dine rettigheter og plikter",
                            Language.Nynorsk to "",
                            Language.English to "",
                        )
                    }
                    item {
                        text(
                            Language.Bokmal to "om du har gitt riktig informasjon til oss",
                            Language.Nynorsk to "",
                            Language.English to "",
                        )
                    }
                    item {
                        text(
                            Language.Bokmal to "om du har gitt all nødvendig informasjon til oss i rett tid",
                            Language.Nynorsk to "",
                            Language.English to "",
                        )
                    }
                }
            }

            paragraph {
                text(
                    Language.Bokmal to "Vi vil vurdere om det er særlige grunner til å redusere kravet. " +
                            "Da legger vi vekt på",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
                list {
                    item {
                        text(
                            Language.Bokmal to "hvor aktsom du har vært",
                            Language.Nynorsk to "",
                            Language.English to "",
                        )
                    }
                    item {
                        text(
                            Language.Bokmal to "hvor mye vi kan bebreides",
                            Language.Nynorsk to "",
                            Language.English to "",
                        )
                    }
                    item {
                        text(
                            Language.Bokmal to "hvor lenge det er siden feilutbetalingen skjedde",
                            Language.Nynorsk to "",
                            Language.English to "",
                        )
                    }
                    item {
                        text(
                            Language.Bokmal to "størrelsen på det feilutbetalte beløpet",
                            Language.Nynorsk to "",
                            Language.English to "",
                        )
                    }
                }
            }
            paragraph {
                text(
                    Language.Bokmal to "Dette går fram av folketrygdloven § 22-15 og rettspraksis.",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
            }

            title2 {
                text(
                    Language.Bokmal to "Foreløpig vurdering",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
            }

            paragraph {
                text(
                    Language.Bokmal to "Vår foreløpige vurdering er at du <har gitt oss mangelfulle " +
                            "opplysninger // har gitt oss feil opplysninger // har gitt oss nødvendige opplysninger " +
                            "for sent ///, selv om du har fått god informasjon fra oss. //, fordi du ikke har fått " +
                            "god nok informasjon fra oss. // forstod eller burde forstått at beløpet du fikk, " +
                            "skyldtes en feil // ikke forstod eller burde forstått at beløpet du fikk, " +
                            "skyldtes en feil.> ",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
            }
        }
    }

}