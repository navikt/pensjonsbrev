package no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.etterlatte.maler.OmstillingsstoenadEtterbetaling
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants


@TemplateModelHelpers
val forhaandsvarselFeilutbetaling = createAttachment<LangBokmalNynorskEnglish, Any>(
    title = newText(
        Bokmal to "Forhåndsvarsel - vi vurderer om du må betale tilbake omstillingsstønad",
        Nynorsk to "",
        English to "",
    ),
    includeSakspart = false
) {

    paragraph {
        text(
            Bokmal to "Vi viser til vedtaket vårt av <dato>. Du har fått <beløp> kroner for mye utbetalt i " +
                    "omstillingsstønad fra og med <dato> til og med <dato>.",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        text(
            Bokmal to "Før vi avgjør om du skal betale tilbake, har du rett til å uttale deg. Dette må du " +
                    "gjøre innen 14 dager etter at du har fått dette varselet.",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        text(
            Bokmal to "Dette er kun et varsel om at vi vurderer å kreve tilbake det feilutbetalte beløpet. " +
                    "Det er ikke et vedtak om tilbakekreving.",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        text(
            Bokmal to "Dersom vi vedtar at du må betale tilbake hele eller deler av det feilutbetalte beløpet, " +
                    "trekker vi fra skatten på beløpet vi krever tilbake.",
            Nynorsk to "",
            English to "",
        )
    }

    title2 {
        text(
            Bokmal to "Dette har skjedd",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        text(
            Bokmal to "Som mottaker av en ytelse fra NAV er du pliktig til å opplyse om endringer som kan ha " +
                    "betydning for retten til, eller størrelsen på ytelsen, jf. folketrygdloven § 21-3. Videre " +
                    "følger det av folketrygdloven § 22-12 at ytelsen blir satt ned eller opphørt fra og med " +
                    "måneden etter den måneden vilkårene for dette er oppfylt. ",
            Nynorsk to "",
            English to "",
        )
    }

    // TODO ytelse inn som parameter her
    paragraph {
        text(
            Bokmal to "Omstillingsstønaden din skulle vært endret fra måneden etter endringen skjedde, men " +
                    "NAV Familie- og pensjonsytelser kan ikke se å ha mottatt opplysningene i tide til å endre " +
                    "stønaden fra riktig tidspunkt. Du har dermed fått for mye utbetalt stønad i ovennevnte periode.",
            Nynorsk to "",
            English to "",
        )
    }

    title2 {
        text(
            Bokmal to "Dette legger vi vekt på i vurderingen vår",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        text(
            Bokmal to "For å avgjøre om vi kan kreve tilbake, tar vi først stilling til:",
            Nynorsk to "",
            English to "",
        )
        list {
            item {
                text(
                    Bokmal to "om du forstod eller burde forstått at beløpet du fikk utbetalt var feil",
                    Nynorsk to "",
                    English to "",
                )
            }
            item {
                text(
                    Bokmal to "om du har gitt riktig informasjon til NAV",
                    Nynorsk to "",
                    English to "",
                )
            }
            item {
                text(
                    Bokmal to "om du har gitt all nødvendig informasjon til NAV i rett tid",
                    Nynorsk to "",
                    English to "",
                )
            }
        }
    }
    paragraph {
        text(
            Bokmal to "Selv om det er NAV som er skyld i feilutbetalingen, kan vi kreve at du betaler " +
                    "tilbake pengene. Dette går fram av folketrygdloven § 22-15.",
            Nynorsk to "",
            English to "",
        )
    }

    title2 {
        text(
            Bokmal to "Foreløpig vurdering",
            Nynorsk to "",
            English to "",
        )
    }

    paragraph {
        text(
            Bokmal to "En foreløpig vurdering viser at du ikke har meldt fra om <skriv inn en kort vurdering>.",
            Nynorsk to "",
            English to "",
        )
    }

    title2 {
        text(
            Bokmal to "Slik uttaler du deg",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        text(
            Bokmal to "Du kan sende uttalelsen din ved å logge deg inn på ${Constants.BESKJED_TIL_NAV_URL} og " +
                    "velge «Send beskjed til NAV». Du kan også sende uttalelsen din til oss i posten. Adressen finner " +
                    "du på ${Constants.ETTERSENDELSE_URL}.",
            Nynorsk to "",
            English to "",
        )
    }

    title2 {
        text(
            Bokmal to "Hva skjer videre i din sak",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        text(
            Bokmal to "Vi vil vurdere saken og sende deg et vedtak. Dersom du må betale hele eller deler av " +
                    "beløpet, vil du få beskjed om hvordan du betaler tilbake i vedtaket. ",
            Nynorsk to "",
            English to "",
        )
    }

    title2 {
        text(
            Bokmal to "Du har rett til innsyn",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        text(
            Bokmal to "På ${Constants.DITT_NAV} kan du se dokumentene i saken din.",
            Nynorsk to "",
            English to "",
        )
    }

    title2 {
        text(
            Bokmal to "Har du spørsmål?",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        text(
            Bokmal to "Kontakt oss gjerne på nav.no eller på telefon +47 ${Constants.KONTAKTTELEFON_PENSJON}. " +
                    "Hvis du oppgir fødselsnummeret ditt når du tar kontakt med NAV, kan vi lettere gi deg rask " +
                    "og god hjelp.",
            Nynorsk to "",
            English to "",
        )
    }

}
