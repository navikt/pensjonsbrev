package no.nav.pensjon.brev.alder.maler.info.afpprivatutforetrygdbrev

import no.nav.pensjon.brev.alder.maler.Brevkategori
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.TemplateDescription.ISakstype
import no.nav.pensjon.brev.api.model.maler.EmptyRedigerbarBrevdata
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata


@TemplateModelHelpers
object UforetrygdSokerAfpPrivat : RedigerbarTemplate<EmptyRedigerbarBrevdata> {
    override val kode = Aldersbrevkoder.Redigerbar.INFO_BRUKER_UFORETRYGD_SOKER_AFP_PRIVAT

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Du må velge mellom uføretrygd og AFP i privat sektor",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        )
    ) {
        title {
            text(
                bokmal { +"Du må velge mellom uføretrygd og AFP i privat sektor" },
                nynorsk { +"Du må velge mellom uføretrygd og AFP i privat sektor" },
            )
        }

        outline {
            paragraph {
                text(
                    bokmal {
                        +"Du har i dag uføretrygd fra Nav. For å ha rett til AFP i privat sektor, kan du ikke ha fått utbetalt uføretrygd etter den måneden du fyller 62 år. Dette går fram av AFP-tilskottsloven § 8. Hvis du ønsker å ta ut AFP i privat sektor må du si fra deg retten til uføretrygd senest fra TODO 01.mm.20åå. Du må derfor velge mellom:"

                    },
                    nynorsk {
                        +"Du har i dag uføretrygd fra Nav. For å ha rett til AFP i privat sektor, kan du ikke ha fått utbetalt uføretrygd etter den måneden du fyller 62 år. Dette går fram av AFP-tilskottsloven § 8. Hvis du ønsker å ta ut AFP i privat sektor må du si fra deg retten til uføretrygd senest fra TODO 01.mm.20åå. Du må derfor velge mellom:"
                    },
                )
                list {
                    item {
                        text(
                            bokmal { +"AFP og alderspensjon" },
                            nynorsk { +"AFP og alderspensjon" },
                        )

                    }
                    item {
                        text(
                            bokmal { +"Uføretrygd fra Nav og eventuelt alderspensjon" },
                            nynorsk { +"Uføretrygd fra Nav og eventuelt alderspensjon" },
                        )
                    }
                }
            }
            title2 {
                text(
                    bokmal { +"Hvor mye kan du få?" },
                    nynorsk { +"Hvor mye kan du få?" }
                )
            }
            paragraph {
                text(
                    bokmal { +"Valget mellom uføretrygd og AFP påvirker hvor stor pensjonen din blir nå og ved 67 år." },
                    nynorsk { +"Valget mellom uføretrygd og AFP påvirker hvor stor pensjonen din blir nå og ved 67 år." }
                )
            }
            title2 {
                text(
                    bokmal { +"Uføretrygd og alderspensjon" },
                    nynorsk { +"Uføretrygd og alderspensjon" }
                )
            }
            paragraph {
                text(
                    bokmal { +"Du kan ha alderspensjon fra Nav ved siden av uføretrygden du har i dag. Alderspensjonen din kan du ta ut delvis i gradene 20, 40, 50, 60, 80 og 100 prosent, og summen av uføretrygd og alderspensjon kan ikke være over 100 prosent til sammen. Den delen av alderspensjonen du ikke tar ut før 67 år, blir spart opp og gir høyere utbetaling senere." },
                    nynorsk { +"Du kan ha alderspensjon fra Nav ved siden av uføretrygden du har i dag. Alderspensjonen din kan du ta ut delvis i gradene 20, 40, 50, 60, 80 og 100 prosent, og summen av uføretrygd og alderspensjon kan ikke være over 100 prosent til sammen. Den delen av alderspensjonen du ikke tar ut før 67 år, blir spart opp og gir høyere utbetaling senere." }
                )
            }
            paragraph {
                text(
                    bokmal { +"Her er en beregning som viser hva du kan få i pensjon om du velger uføretrygd og alderspensjon:" },
                    nynorsk { +"Her er en beregning som viser hva du kan få i pensjon om du velger uføretrygd og alderspensjon:" }
                )
            }
            //TODO table
            title2 {
                text(
                    bokmal { +"AFP i privat sektor og alderspensjon" },
                    nynorsk { +"AFP i privat sektor og alderspensjon" }
                )
            }
            paragraph {
                text(
                    bokmal { +"AFP i privat sektor kommer i tillegg til alderspensjon fra Nav. Du kan velge å ta ut kun AFP i privat sektor, men da må du først ta ut AFP sammen med minst 20 prosent alderspensjon i minst 1 måned." },
                    nynorsk { +"AFP i privat sektor kommer i tillegg til alderspensjon fra Nav. Du kan velge å ta ut kun AFP i privat sektor, men da må du først ta ut AFP sammen med minst 20 prosent alderspensjon i minst 1 måned." }
                )
            }
            paragraph { text(
                bokmal { +"Her er en beregning som viser hva du kan få i pensjon om du velger AFP og alderspensjon:" },
                nynorsk { +"Her er en beregning som viser hva du kan få i pensjon om du velger AFP og alderspensjon:" }
            ) }
            //TODO Table

            paragraph {
                text(
                    bokmal { +"Se vedlegg om forbehold.:" },
                    nynorsk { +"Se vedlegg om forbehold.:" }
                )
            }
            //TODO Table
            paragraph {
                text(
                    bokmal { +"Det er også viktig å vite:" },
                    nynorsk { +"Det er også viktig å vite:" }
                )
                list {
                    item {
                        text(
                            bokmal { +"AFP, uføretrygd og alderspensjon gir forskjellige muligheter til å ha inntekt ved siden av. Du finner informasjon på www.nav.no" },
                            nynorsk { +"AFP, uføretrygd og alderspensjon gir forskjellige muligheter til å ha inntekt ved siden av. Du finner informasjon på www.nav.no" }
                        )
                    }
                    item {
                        text(
                            bokmal { +"AFP, uføretrygd og alderspensjon skattes forskjellig. Du kan kontakte Skatteetaten på www.skatteetaten.no for spørsmål om skattetrekket" },
                            nynorsk { +"AFP, uføretrygd og alderspensjon skattes forskjellig. Du kan kontakte Skatteetaten på www.skatteetaten.no for spørsmål om skattetrekket" }
                        )
                    }
                    item {
                        text(
                            bokmal { +"Uføretrygd fra Nav kan ha innvirkning på rettigheter til uføreforsikringer. Ta kontakt med dine tjenestepensjonsordninger eller forsikringsselskap for avklaring." },
                            nynorsk { +"Uføretrygd fra Nav kan ha innvirkning på rettigheter til uføreforsikringer. Ta kontakt med dine tjenestepensjonsordninger eller forsikringsselskap for avklaring." }
                        )
                    }
                }
            }
            title2 {
                text(
                    bokmal { +"Dette må du gjøre" },
                    nynorsk { +"Dette må du gjøre" }
                )
            }
            //TODO DENNE VELGER OM SØKT
            paragraph {
                text(
                    bokmal { +"Du må sende oss en skriftlig tilbakemelding på om du ønsker å ta ut AFP eller om du ønsker å beholde uføretrygden. Vi må ha tilbakemelding fra deg innen dd.mm.åå. (Fristen og formuleringen må tilpasses den enkelte situasjon og hvor lang tid det er til bruker fyller 62 år)." },
                    nynorsk { +"Du må sende oss en skriftlig tilbakemelding på om du ønsker å ta ut AFP eller om du ønsker å beholde uføretrygden. Vi må ha tilbakemelding fra deg innen dd.mm.åå. (Fristen og formuleringen må tilpasses den enkelte situasjon og hvor lang tid det er til bruker fyller 62 år)." }
                )
            }
            //TODO Denne velger om bruker IKKE har søkt

            paragraph {
                text(
                    bokmal { +"Dersom du ønsker AFP må du:" },
                    nynorsk { +"Dersom du ønsker AFP må du:" }
                )
                list {
                    item { text(
                        bokmal { +"Søke om alderspensjon og AFP i privat sektor. Du må søke senest måneden før du ønsker å ta ut alderspensjon og AFP." },
                        nynorsk { +"Søke om alderspensjon og AFP i privat sektor. Du må søke senest måneden før du ønsker å ta ut alderspensjon og AFP." }
                    ) }
                    item { text(
                        bokmal { +"Si ifra deg uføretrygden senest den måneden du fyller 62 år. Send beskjed til Nav på https://www.nav.no/skriv-til-oss. Velg tema pensjon og uføretrygd." },
                        nynorsk { +"Si ifra deg uføretrygden senest den måneden du fyller 62 år. Send beskjed til Nav på https://www.nav.no/skriv-til-oss. Velg tema pensjon og uføretrygd." }
                    ) }
                    item { text(
                        bokmal { +"Du kan også sende brev merket med fødselsnummeret ditt til: Nav familie- og pensjonsytelser Postboks 6600 Etterstad 0607 OSLO" },
                        nynorsk { +"Du kan også sende brev merket med fødselsnummeret ditt til: Nav familie- og pensjonsytelser Postboks 6600 Etterstad 0607 OSLO" }
                    ) }
                }
            }
            //TODO En felles veiledning





            //Veiledning

        }
    }

    override val kategori = Brevkategori.INFORMASJONSBREV

    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.ALLE

    override val sakstyper: Set<ISakstype> = setOf(Sakstype.ALDER)
}