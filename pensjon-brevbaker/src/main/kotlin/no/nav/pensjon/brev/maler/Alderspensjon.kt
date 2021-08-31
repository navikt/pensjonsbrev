package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.something.PensjonLatex
import no.nav.pensjon.brev.template.*

object Alderspensjon : StaticTemplate {

    override val template = createTemplate("alderspensjon", PensjonLatex, languages(Language.Bokmal)) {
        parameters {
            required { PensjonInnvilget }
        }

        outline {
            title1 { text(Language.Bokmal to "Vedtak") }
            paragraph { text(Language.Bokmal to "Du får 20 513 kroner før skatt fra 1. juni 2021.") }
            paragraph { text(Language.Bokmal to "Alderspensjonen din utbetales innen den 20. hver måned. Du finner oversikt over utbetalingene dine på nav.no/utbetalinger") }
            paragraph { text(Language.Bokmal to "Du har også søkt om avtalefestet pensjon (AFP) i privat sektor, og du vil få et eget vedtak om dette.") }
            paragraph { text(Language.Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 19-2 til 19-8, 19-10, 19-15, 20-2, 20-3, 20-9 til 20-14, 20-19 og 22-12.") }

            title1 { text(Language.Bokmal to "Det er egne skatteregler for pensjon") }
            paragraph { text(Language.Bokmal to "Du bør endre skattekortet når du begynner å ta ut alderspensjon. Dette kan du gjøre selv på skatteetaten.no/pensjonist. Der får du også mer informasjon om skattekort for pensjonister. Vi får skattekortet elektronisk. Du skal derfor ikke sende det til oss.") }
            paragraph { text(Language.Bokmal to "På nav.no/dinpensjon kan du se hva du betaler i skatt. Her kan du også legge inn ekstra skattetrekk om du ønsker det. Dersom du endrer skattetrekket, vil dette gjelde fra måneden etter at vi har fått beskjed.") }

            title1 { text(Language.Bokmal to "Alderspensjonen din reguleres årlig") }
            paragraph { text(Language.Bokmal to "Reguleringen skjer med virkning fra 1. mai og selve økningen blir vanligvis etterbetalt i juni. Du får informasjon om dette på utbetalingsmeldingen din. På nav.no kan du lese mer om hvordan pensjonene reguleres.") }

            title1 { text(Language.Bokmal to "Du kan søke om å endre pensjonen din") }
            paragraph { text(Language.Bokmal to "Du kan ha mulighet til å ta ut 20, 40, 50, 60, 80 eller 100 prosent alderspensjon. Etter at du har begynt å ta ut alderspensjon, kan du gjøre endringer med 12 måneders mellomrom. Hvis du har høy nok opptjening, kan du ta ut100 prosent alderspensjon når du selv ønsker det. Du kan alltid stanse pensjonen.") }
        }
    }

}