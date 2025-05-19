package no.nav.pensjon.brev.maler.fraser.alderspensjon

import no.nav.pensjon.brev.maler.fraser.common.Constants.ALDERSPENSJON
import no.nav.pensjon.brev.maler.fraser.common.Constants.DIN_PENSJON_URL
import no.nav.pensjon.brev.maler.fraser.common.Constants.DITT_NAV
import no.nav.pensjon.brev.maler.fraser.common.Constants.SKATTEETATEN_PENSJONIST_URL
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

object VedtakAlderspensjon {
    object EndringKanHaBetydningForSkatt : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            // skattAPendring_001
            title1 {
                text(
                    Bokmal to "Endring av alderspensjon kan ha betydning for skatt",
                    Nynorsk to "Endring av alderspensjon kan ha betyding for skatt",
                    English to "The change in your retirement pension may affect how much tax you pay"
                )
            }
            paragraph {
                text(
                    Bokmal to "Du bør kontrollere om skattekortet ditt er riktig når alderspensjonen blir endret. Dette kan du gjøre selv på $SKATTEETATEN_PENSJONIST_URL. Der får du også mer informasjon om skattekort for pensjonister.",
                    Nynorsk to "Du bør kontrollere om skattekortet ditt er riktig når alderspensjonen blir endra. Dette kan du gjere sjølv på $SKATTEETATEN_PENSJONIST_URL. Der får du også meir informasjon om skattekort for pensjonistar.",
                    English to "When your retirement pension has been changed, you should check if your tax deduction card is correctly calculated. You can change your tax card by logging on to $SKATTEETATEN_PENSJONIST_URL. There you will find more information regarding tax deduction card for pensioners."
                )
            }
            paragraph {
                text(
                    Bokmal to "På $DIN_PENSJON_URL får du vite hva du betaler i skatt. Her kan du også legge inn ekstra skattetrekk om du ønsker det. Dersom du endrer skattetrekket vil dette gjelde fra måneden etter at vi har fått beskjed.",
                    Nynorsk to "På $DIN_PENSJON_URL får du vite kva du betaler i skatt. Her kan du også leggje inn tilleggsskatt om du ønskjer det. Dersom du endrar skattetrekket, vil dette gjelde frå månaden etter at vi har fått beskjed.",
                    English to "At $DIN_PENSJON_URL you can see how much tax you are paying. Here you can also add surtax, if you want. If you change your income tax rate, this will be applied from the month after we have been notified of the change."
                )
            }
        }
    }


    object ArbeidsinntektOgAlderspensjon : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            // arbInntektAPOverskrift_001
            title1 {
                text(
                    Bokmal to "Arbeidsinntekt og alderspensjon",
                    Nynorsk to "Arbeidsinntekt og alderspensjon",
                    English to "Earned income and retirement pension"
                )
            }

            // arbInntektAP_001
            paragraph {
                text(
                    Bokmal to "Du kan arbeide så mye du vil uten at alderspensjonen din blir redusert. Det kan føre til at pensjonen din øker.",
                    Nynorsk to "Du kan arbeide så mykje du vil utan at alderspensjonen din blir redusert. Det kan føre til at pensjonen din aukar.",
                    English to "You can work as much as you want without your retirement pension being reduced. This may lead to an increase in your pension."
                )
            }
        }
    }

    object HvorKanDuFaaViteMerOmAlderspensjonenDin : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        // infoAPOverskrift_001
            title1 {
                text(
                    Bokmal to "Hvor kan du få vite mer om alderspensjonen din?",
                    Nynorsk to "Kvar kan du få vite meir om alderspensjonen din?",
                    English to "Where can you find out more about your retirement pension?"
                )
            }
            // infoAP_001
            paragraph {
                text(
                    Bokmal to "Du finner mer informasjon om hvordan alderspensjon er satt sammen og oversikter over grunnbeløp og aktuelle satser på $ALDERSPENSJON.",
                    Nynorsk to "Du finn meir informasjon om korleis alderspensjonen er sett saman, og oversikter over grunnbeløp og aktuelle satsar på $ALDERSPENSJON.",
                    English to "There is more information on how retirement pension is calculated, with overviews of basic amounts and relevant rates, at $ALDERSPENSJON."
                )
            }
            paragraph {
                text(
                    Bokmal to "Informasjon om utbetalingene dine finner du på $DITT_NAV. Her kan du også endre kontonummeret ditt.",
                    Nynorsk to "Informasjon om utbetalingane dine finn du på $DITT_NAV. Her kan du også endre kontonummeret ditt.",
                    English to "You can find more detailed information on what you will receive at $DITT_NAV. Here you can also change your bank account number."
                )
            }
        }
    }


}