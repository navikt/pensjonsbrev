package no.nav.pensjon.brev.planleggepensjon.simulering.vedlegg

import no.nav.pensjon.brev.api.model.maler.EmptyVedleggData
import no.nav.pensjon.brev.planleggepensjon.simulering.Simuleringsinformasjon
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.text

val forbeholdVedlegg = createAttachment<LangBokmal, EmptyVedleggData>(
    title = {
        text(bokmal { +"Forbehold" })
    },
    includeSakspart = false,
) {
    paragraph {
        text(
            bokmal {
                +"Pensjonen er beregnet med de opplysningene vi har om deg, i tillegg til de opplysningene du har oppgitt, på tidspunktet for beregningen. Dette er derfor et foreløpig estimat på hva du kan forvente deg i pensjon. Pensjonsberegningen er vist i dagens kroneverdi før skatt. Vi har benyttet dagens satser for beregning av minstepensjon. Satsene reguleres hvert år og blir ikke fastsatt før de skal brukes. Fremtidige reguleringer kan ha betydning for når du tidligst kan starte uttak av alderspensjon."
            }
        )
    }
    paragraph {
        text(
            bokmal {
                +"Vi anbefaler at du får en ny beregning når du nærmer deg ønsket pensjonsalder hvis det er lenge til du skal ta ut pensjon. Det vil blant annet kunne skje endringer i din opptjening og endringer i regelverket."
            }
        )
    }
    title1 {
        text(bokmal { +"Inntekt og godskriving av pensjonsopptjening" })
    }
    paragraph {
        text(
            bokmal {
                +"I beregningen benytter vi din siste registrerte pensjonsgivende årsinntekt som Nav har mottatt fra Skatteetaten. Den blir brukt som din fremtidige inntekt frem til du starter uttak av alderspensjon, med mindre du har oppgitt annen inntekt."
            })
    }
    paragraph {
        text(
            bokmal {
                +"Pensjonsgivende inntekt blir først gjeldende i pensjon fra januar året etter den er mottatt fra Skatteetaten. Alderspensjonen vil derfor normalt øke som følge av ny opptjening de to første årene etter uttak. Dette vil fremgå i tabell med årlig inntekt og pensjon."
            }
        )
    }
    paragraph {
        text(
            bokmal {
                +"I enkelte tilfeller kan man få avslag på en søknad om alderspensjon selv om man har fått beregnet alderspensjon fra samme tidspunkt. Det kan skje hvis du søker om uttak av pensjon fra neste år. I disse tilfellene må man vente med å søke til midten av desember før man søker for å få med siste gjeldende år med pensjonsgivende inntekt."
            }
        )
    }
    title1 {
        text(bokmal { +"Opphold utenfor Norge" })
    }
    paragraph {
        text(
            bokmal {
                +"Når du ikke har opplyst om utenlandsopphold, forutsetter beregningen at du har bodd i Norge fra fylte 16 år og frem til du tar ut pensjon."
            }
        )
    }
    paragraph {
        text(
            bokmal {
                +"Vi bruker opplysningene du har gitt om botid og arbeid i andre land i beregningen. Beregningen og tidspunktet du tidligst kan ta ut den norske pensjonen din kan være unøyaktig hvis du har oppgitt opphold utenfor Norge."
            }
        )
        list {
            item {
                text(
                    bokmal { +"Det er opptjeningen din i Norge som brukes når Nav beregner alderspensjonen din. Har du oppgitt fremtidige perioder med opphold utenfor Norge, settes inntekt i Norge til null kroner i disse periodene. " }
                )
            }
            item {
                text(
                    bokmal { +"Medlemstid i land Norge har trygdeavtale med kan også påvirke om du kan starte uttaket før du er 67 år. Beregningen er derfor gjort med forbehold om at de opplysningene vi bruker er riktige. Merk at det kan komme endringer i trygdeavtaler med andre land." }
                )
            }
        }
    }

}

