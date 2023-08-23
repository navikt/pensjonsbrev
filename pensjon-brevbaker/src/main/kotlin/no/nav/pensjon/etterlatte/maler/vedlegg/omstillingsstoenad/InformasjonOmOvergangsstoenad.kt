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
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType


@TemplateModelHelpers
val informasjonOmOvergangsstoenad = createAttachment(
    title = newText(
        Bokmal to "Informasjon til deg som mottar overgangsstønad",
        Nynorsk to "",
        English to "",
    ),
    includeSakspart = false,
) {
    utbetalingOgRegulering()
    inntektVedSidenAvOmstillingsstoenad()
    hvaRegnesSomInntektNaarDuHarOmstillingsstoenad()
    hvordanMelderDuFraOmEndringer()
    utbetalingTilKontonummer()
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, Any>.utbetalingOgRegulering() {
    title2 {
        text(
            Bokmal to "Utbetaling og regulering",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        text(
            Bokmal to "Omstillingsstønad blir utbetalt innen den 20. i hver måned. " +
                    "Du finner utbetalingsdatoer på ${Constants.UTBETALING_URL}.",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        text(
            Bokmal to "Grunnbeløpet blir regulert 1. mai hvert år. Økningen blir vanligvis etterbetalt i juni.",
            Nynorsk to "",
            English to "",
        )
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, Any>.inntektVedSidenAvOmstillingsstoenad() {
    title2 {
        text(
            Bokmal to "Inntekt ved siden av omstillingsstønad",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        text(
            Bokmal to "Når du søker omstillingsstønad, oppgir du hvor mye du forventer å tjene samtidig " +
                    "som du mottar stønaden. Omstillingsstønaden blir redusert ut fra dette. Om inntekten din " +
                    "endrer seg, må du melde fra til oss om den nye inntekten hvis du vil unngå etteroppgjør.",
            Nynorsk to "",
            English to "",
        )
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, Any>.hvaRegnesSomInntektNaarDuHarOmstillingsstoenad() {
    title2 {
        text(
            Bokmal to "Hva regnes som inntekt når du har omstillingsstønad?",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        text(
            Bokmal to "Som pensjonsgivende inntekt regnes blant annet:",
            Nynorsk to "",
            English to "",
        )
        list {
            item {
                text(
                    Bokmal to "arbeidsinntekt fra alle arbeidsgivere, også feriepenger.",
                    Nynorsk to "",
                    English to "",
                )
            }
            item {
                text(
                    Bokmal to "næringsinntekt",
                    Nynorsk to "",
                    English to "",
                )
            }
            item {
                text(
                    Bokmal to "salg av næringsvirksomhet",
                    Nynorsk to "",
                    English to "",
                )
            }
            item {
                text(
                    Bokmal to "sykepenger og dagpenger",
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
                    Bokmal to "omsorgsstønad",
                    Nynorsk to "",
                    English to "",
                )
            }
        }
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, Any>.hvordanMelderDuFraOmEndringer() {
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
                    Bokmal to "en melding på ${Constants.SKRIVTILOSS_URL}",
                    Nynorsk to "",
                    English to "",
                )
            }
            item {
                text(
                    Bokmal to "brev til ${Constants.POSTADRESSE}.",
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
                    "«Personopplysninger» ved å logge på ${Constants.NAV_URL}. Du kan også sende endring per post. " +
                    "Du finner skjema og riktig adresse på ${Constants.ENDRING_KONTONUMMER_URL}.",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        text(
            Bokmal to "Skatt",
            Nynorsk to "",
            English to "",
            FontType.BOLD
        )
    }
    paragraph {
        text(
            Bokmal to "Vanlig informasjon om skatt eller ha med noe om at OMS er pensjonsgivende og evt " +
                    "ikke har feriepengeutbetaling?",
            Nynorsk to "",
            English to "",
        )
    }
    paragraph {
        text(
            Bokmal to "Brudd på aktivitetsplikt/Sanksjon",
            Nynorsk to "",
            English to "",
            FontType.BOLD
        )
    }
    paragraph {
        text(
            Bokmal to "Et avsnitt om dette, evt henvise til nav.no? TODO: SKRIV OM DENNE",
            Nynorsk to "",
            English to "",
        )
    }
}
