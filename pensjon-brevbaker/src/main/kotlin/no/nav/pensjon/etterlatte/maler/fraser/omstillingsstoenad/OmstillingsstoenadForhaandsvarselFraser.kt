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
                            "Dette må du gjøre innen 14 dager etter at du har fått dette varselet. ",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Dette er kun et varsel om at vi vurderer å kreve tilbake det " +
                            "feilutbetalte beløpet. Det er ikke et vedtak om tilbakekreving. ",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Dersom vi vedtar at du må betale tilbake hele eller deler av det " +
                            "feilutbetalte beløpet, trekker vi fra skatten på beløpet vi krever tilbake. ",
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
                            "vilkårene ikke var oppfylt. Dette har medført en feilutbetaling til deg. >",
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
                    Language.Bokmal to "Selv om det er NAV som er skyld i feilutbetalingen, kan vi kreve at " +
                            "du betaler tilbake pengene. I et slikt tilfelle vil vi legge vekt på om du forstod " +
                            "eller burde forstått at beløpet du fikk utbetalt var feil.",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
            }

            paragraph {
                text(
                    Language.Bokmal to "Hvis vi mener at det er manglende eller feil opplysninger fra deg som " +
                            "har ført til feilutbetalingen vil vi legge vekt på",
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
                    Language.Bokmal to "Vi vil også se på",
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
                    Language.Bokmal to "Dette følger av folketrygdloven § 22-15 og rettspraksis.",
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
                    Language.Bokmal to "Vår foreløpige vurdering er at du " +
                            "<har gitt oss mangelfulle opplysninger // har gitt oss feil opplysninger // har gitt " +
                            "oss nødvendige opplysninger for sent ///, selv om du har fått god informasjon fra " +
                            "oss. //, fordi du ikke har fått god nok informasjon fra oss. // forstod eller burde " +
                            "forstått at beløpet du fikk, var feil.> ",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
            }
        }
    }

    object SlikUttalerDuDeg : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Language.Bokmal to "Slik uttaler du deg",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Du kan sende uttalelsen din ved å logge deg inn på " +
                            "${Constants.BESKJED_TIL_NAV_URL} og velge «Send beskjed til NAV». Du kan også sende " +
                            "uttalelsen din til oss i posten. Adressen finner du på ${Constants.ETTERSENDELSE_URL}.",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
            }
        }
    }

    object HvaSkjerVidereIDinSak : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            title2 {
                text(
                    Language.Bokmal to "Hva skjer videre i din sak",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Når fristen for uttale er gått ut, vil vi gjøre et vedtak og sende det " +
                            "til deg. Hvis du må betale tilbake hele eller deler av beløpet, gir vi beskjed i " +
                            "vedtaket om hvordan du betaler tilbake. ",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
            }
        }
    }

}