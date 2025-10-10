package no.nav.pensjon.brev.maler.fraser.alderspensjon

import brev.felles.Constants.DIN_PENSJON_URL
import brev.felles.Constants.SKATTEETATEN_PENSJONIST_URL
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

object VedtakAlderspensjon {
    object EndringKanHaBetydningForSkatt : OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            // skattAPendring_001
            title1 {
                text(
                    bokmal { + "Endring av alderspensjon kan ha betydning for skatt" },
                    nynorsk { + "Endring av alderspensjon kan ha betyding for skatt" },
                    english { + "The change in your retirement pension may affect how much tax you pay" }
                )
            }
            paragraph {
                text(
                    bokmal { + "Du bør kontrollere om skattekortet ditt er riktig når alderspensjonen blir endret. Dette kan du gjøre selv på $SKATTEETATEN_PENSJONIST_URL. Der får du også mer informasjon om skattekort for pensjonister." },
                    nynorsk { + "Du bør kontrollere om skattekortet ditt er riktig når alderspensjonen blir endra. Dette kan du gjere sjølv på $SKATTEETATEN_PENSJONIST_URL. Der får du også meir informasjon om skattekort for pensjonistar." },
                    english { + "When your retirement pension has been changed, you should check if your tax deduction card is correctly calculated. You can change your tax card by logging on to $SKATTEETATEN_PENSJONIST_URL. There you will find more information regarding tax deduction card for pensioners." }
                )
            }
            paragraph {
                text(
                    bokmal { + "På $DIN_PENSJON_URL får du vite hva du betaler i skatt. Her kan du også legge inn ekstra skattetrekk om du ønsker det. Dersom du endrer skattetrekket vil dette gjelde fra måneden etter at vi har fått beskjed." },
                    nynorsk { + "På $DIN_PENSJON_URL får du vite kva du betaler i skatt. Her kan du også leggje inn tilleggsskatt om du ønskjer det. Dersom du endrar skattetrekket, vil dette gjelde frå månaden etter at vi har fått beskjed." },
                    english { + "At $DIN_PENSJON_URL you can see how much tax you are paying. Here you can also add surtax, if you want. If you change your income tax rate, this will be applied from the month after we have been notified of the change." }
                )
            }
        }
    }
}