package no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.LanguageSupport
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants


@TemplateModelHelpers
val informasjonOmOmstillingsstoenad = createAttachment(
    title = newText(
        Bokmal to "Informasjon til deg som mottar omstillingsstønad",
        Nynorsk to "",
        English to "",
    ),
    includeSakspart = false,
) {
    aktivitet()
    hvisDuIkkeFyllerAktivitetsplikten()
    inntektOgOmstillingsstoenad()
    endretInntekt()
    hvilkenInntektReduseresEtter()
    hvordanMeldeEndringer()
    utbetalingTilKontonummer()
    skatt()
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, Any>.aktivitet() {
    title2 {
        text(
            Bokmal to "Du må være i aktivitet når du mottar omstillingsstønad",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        text(
            Bokmal to "Når det er gått seks måneder etter dødsfallet er det et krav for å motta " +
                    "omstillingsstønad at du er i minst 50 prosent arbeid eller annen aktivitet med sikte på " +
                    "å komme i arbeid. Etter et år kan det forventes at du er i 100 prosent aktivitet. Dette " +
                    "kalles for aktivitetsplikt. ",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        text(
            Bokmal to "Du fyller aktivitetsplikten hvis du",
            Nynorsk to "",
            English to "",
        )
        list {
            item {
                text(
                    Bokmal to "jobber",
                    Nynorsk to "",
                    English to "",
                )
            }
            item {
                text(
                    Bokmal to "er selvstendig næringsdrivende",
                    Nynorsk to "",
                    English to "",
                )
            }
            item {
                text(
                    Bokmal to "etablerer egen virksomhet",
                    Nynorsk to "",
                    English to "",
                )
            }
            item {
                text(
                    Bokmal to "tar utdanning som er nødvendig og hensiktsmessig",
                    Nynorsk to "",
                    English to "",
                )
            }
            item {
                text(
                    Bokmal to "er reell arbeidssøker",
                    Nynorsk to "",
                    English to "",
                )
            }
            item {
                text(
                    Bokmal to "har fått tilbud om jobb",
                    Nynorsk to "",
                    English to "",
                )
            }
        }
    }
    paragraph {
        text(
            Bokmal to "Det er unntak fra aktivitetsplikten som gir rett til omstillingsstønad. Dette gjelder " +
                    "blant annet hvis du har omsorgen for barn under ett år, om du har dokumentert sykdom som " +
                    "forhindrer deg i å være i aktivitet, eller om du er innvilget etter unntaksregelen for de " +
                    "født i 1963 eller tidligere med lav inntekt.",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        text(
            Bokmal to "Du kan lese mer om aktivitetsplikten og unntakene på " + Constants.OMS_AKTIVITET_URL + ".",
            Nynorsk to "",
            English to "",
        )
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, Any>.hvisDuIkkeFyllerAktivitetsplikten() {
    title2 {
        text(
            Bokmal to "Hva skjer hvis du ikke fyller aktivitetsplikten?",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        text(
            Bokmal to "Hvis du ikke fyller aktivitetsplikten, kan utbetalingen av stønaden stoppe inntil " +
                    "vilkårene er oppfylt igjen. Det blir midlertidig stans av utbetalingene dine hvis du",
            Nynorsk to "",
            English to "",
        )
        list {
            item {
                text(
                    Bokmal to "sier nei til jobb",
                    Nynorsk to "",
                    English to "",
                )
            }
            item {
                text(
                    Bokmal to "sier nei til å delta i, eller slutter i et arbeidsmarkedstiltak",
                    Nynorsk to "",
                    English to "",
                )
            }
            item {
                text(
                    Bokmal to "sier opp, eller på andre måter slutter i jobben din",
                    Nynorsk to "",
                    English to "",
                )
            }
            item {
                text(
                    Bokmal to "blir avskjediget eller sagt opp på grunn av forhold som du selv er skyld i",
                    Nynorsk to "",
                    English to "",
                )
            }
        }
    }
    paragraph {
        text(
            Bokmal to "NAV må kunne komme i kontakt med deg for å følge deg opp ved behov. Får vi ikke " +
                    "kontakt med deg, kan vi stoppe stønaden din.",
            Nynorsk to "",
            English to "",
        )
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, Any>.inntektOgOmstillingsstoenad() {
    title2 {
        text(
            Bokmal to "Inntekt og omstillingsstønad",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        text(
            Bokmal to "Omstillingsstønaden skal reduseres med 45 prosent av inntekten din som er over " +
                    "halvparten av grunnbeløpet i folketrygden (G). Stønaden blir redusert ut fra hva du oppgir " +
                    "som forventet inntekt for gjeldende år.  ",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        text(
            Bokmal to "Om du ikke oppgir annet, vil inntekten bli oppjustert ved årsskiftet for å ta hensyn " +
                    "til generell lønnsøkning. Dette gjøres ved hjelp av faktor som er brukt i den årlige " +
                    "oppjusteringen av grunnbeløpet.",
            Nynorsk to "",
            English to "",
        )
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, Any>.endretInntekt() {
    title2 {
        text(
            Bokmal to "Får du endret inntekt i løpet av året?",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        text(
            Bokmal to "Om du melder fra om endring av inntekt i løpet av året, vil vi justere " +
                    "omstillingsstønaden fra måneden etter du har gitt beskjed. Inntekten din beregnes ut fra " +
                    "det du har tjent så langt i år, lagt sammen med det du forventer å tjene resten av året. " +
                    "Inntekt som er opptjent før mottak av omstillingsstønaden tas ikke med i beregningen.",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        text(
            Bokmal to "Utbetalingen for resten av året vil justeres utfra det du har fått utbetalt så langt " +
                    "det gjeldende året. Dette gjøres for å redusere et eventuelt etteroppgjør, som kan medføre " +
                    "tilbakekreving av tidligere utbetalt omstillingsstønad.",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        text(
            Bokmal to "Om inntekten din endres, må du melde fra til oss snarest mulig for å unngå etteroppgjør.",
            Nynorsk to "",
            English to "",
        )
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, Any>.hvilkenInntektReduseresEtter() {
    title2 {
        text(
            Bokmal to "Hvilken inntekt skal omstillingsstønaden reduseres etter?",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        text(
            Bokmal to "Omstillingsstønaden skal reduseres etter arbeidsinntekt og annen inntekt som er " +
                    "likestilt med arbeidsinntekt. Dette er blant annet:",
            Nynorsk to "",
            English to "",
        )
        list {
            item {
                text(
                    Bokmal to "arbeidsinntekt fra alle arbeidsgivere, inkludert feriepenger",
                    Nynorsk to "",
                    English to "",
                )
            }
            item {
                text(
                    Bokmal to "næringsinntekt, og inntekt fra salg av næringsvirksomhet",
                    Nynorsk to "",
                    English to "",
                )
            }
            item {
                text(
                    Bokmal to "styregodtgjørelse og andre godtgjørelser",
                    Nynorsk to "",
                    English to "",
                )
            }
            item {
                text(
                    Bokmal to "royalties",
                    Nynorsk to "",
                    English to "",
                )
            }
            item {
                text(
                    Bokmal to "dagpenger, sykepenger og arbeidsavklaringspenger",
                    Nynorsk to "",
                    English to "",
                )
            }
            item {
                text(
                    Bokmal to "svangerskapspenger og foreldrepenger",
                    Nynorsk to "",
                    English to "",
                )
            }
            item {
                text(
                    Bokmal to "omsorgsstønad",
                    Nynorsk to "",
                    English to "",
                )
            }
        }
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, Any>.hvordanMeldeEndringer() {
    title2 {
        text(
            Bokmal to "Hvordan melder du fra om endringer?",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        text(
            Bokmal to "Du kan gi beskjed om endringer i inntekten din ved å sende",
            Nynorsk to "",
            English to "",
        )
        list {
            item {
                text(
                    Bokmal to "en melding på " + Constants.SKRIVTILOSS_URL,
                    Nynorsk to "",
                    English to "",
                )
            }
            item {
                text(
                    Bokmal to "brev til " + Constants.POSTADRESSE,
                    Nynorsk to "",
                    English to "",
                )
            }
        }
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, Any>.utbetalingTilKontonummer() {
    title2 {
        text(
            Bokmal to "Utbetaling til kontonummer",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        text(
            Bokmal to "Du kan bare ha ett kontonummer registrert hos oss. Du kan endre kontonummeret i " +
                    "«Personopplysninger» ved å logge på nav.no. Du kan også sende endring per post. " +
                    "Du finner skjema og riktig adresse på " + Constants.ENDRING_KONTONUMMER_URL + ".",
            Nynorsk to "",
            English to "",
        )
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, Any>.skatt() {
    title2 {
        text(
            Bokmal to "Skatt",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        text(
            Bokmal to "Omstillingsstønaden er skattepliktig. Du trenger ikke levere skattekortet til NAV " +
                    "fordi skatteopplysningene dine sendes elektronisk fra Skatteetaten.",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        text(
            Bokmal to "Endring av skattekort gjøres enklest på Skatteetatens nettsider www.skatteetaten.no. " +
                    "Har du spørsmål kan du ringe Skatteetaten på telefon " + Constants.KONTAKTTELEFON_SKATT + ". " +
                    "Fra utlandet ringer du " + Constants.Utland.KONTAKTTELEFON_SKATT + ".",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        text(
            Bokmal to "Omstillingsstønaden er pensjonsgivende inntekt. Den gir ikke opptjening av feriepenger.",
            Nynorsk to "",
            English to "",
        )
    }
}
