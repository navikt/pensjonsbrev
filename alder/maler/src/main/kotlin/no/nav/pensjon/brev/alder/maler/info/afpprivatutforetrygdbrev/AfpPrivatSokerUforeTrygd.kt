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
object AfpPrivatSokerUforeTrygd : RedigerbarTemplate<EmptyRedigerbarBrevdata> {
    override val kode = Aldersbrevkoder.Redigerbar.INFO_BRUKER_AFP_PRIVAT_SOKER_UFORETRYGD

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Du må velge mellom Afp i privat sektor og uføretrygd fra Nav",
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV
        )
    ) {
        title {
            text(
                bokmal { +"Du må velge mellom Afp i privat sektor og uføretrygd fra Nav" },
                nynorsk { +"Du må velge mellom Afp i privat sektor og uføretrygd fra Nav" },
            )
        }

        outline {
            paragraph {
                text(
                    bokmal {
                        +"Du har i dag AFP i privat sektor, og har søkt om uføretrygd fra Nav. TODO: Du har i dag AFP i privat sektor og vurderer å søke om uføretrygd fra Nav. Du kan ikke ha AFP samtidig med uføretrygd, se AFP-tilskottsloven § 8. Du må derfor velge mellom:"

                    },
                    nynorsk {
                        +"Du har i dag AFP i privat sektor, og har søkt om uføretrygd fra Nav. TODO: Du har i dag AFP i privat sektor og vurderer å søke om uføretrygd fra Nav. Du kan ikke ha AFP samtidig med uføretrygd, se AFP-tilskottsloven § 8. Du må derfor velge mellom:"
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
                    bokmal { +"AFP i privat sektor og alderspensjon" },
                    nynorsk { +"AFP i privat sektor og alderspensjon" }
                )
            }
            paragraph {
                text(
                    bokmal { +"Du får ikke uføretrygd hvis du velger å fortsatt motta AFP." },
                    nynorsk { +"Du får ikke uføretrygd hvis du velger å fortsatt motta AFP." }
                )
            }
            paragraph {
                text(
                    bokmal { +"Her er en beregning som viser hva du kan få i pensjon om du velger AFP og alderspensjon:" },
                    nynorsk { +"Her er en beregning som viser hva du kan få i pensjon om du velger AFP og alderspensjon:" }
                )
            }
            //TODO Table
            title2 {
                text(
                    bokmal { +"Uføretrygd fra Nav og alderspensjon" },
                    nynorsk { +"Uføretrygd fra Nav og alderspensjon" }
                )
            }
            paragraph {
                text(
                    bokmal { +"Om du ønsker å ta ut uføretrygd må du si fra deg retten til AFP i privat sektor. Du vil ikke få tilbake AFP når du har mottatt uføretrygd etter fylte 62 år." },
                    nynorsk { +"Om du ønsker å ta ut uføretrygd må du si fra deg retten til AFP i privat sektor. Du vil ikke få tilbake AFP når du har mottatt uføretrygd etter fylte 62 år." }
                )
            }
            paragraph {
                text(
                    bokmal { +"Du kan ha alderspensjon fra Nav ved siden av uføretrygden. Alderspensjonen din kan du ta ut delvis i gradene 20, 40, 50, 60, 80 og 100 prosent, og summen av uføretrygd og alderspensjon kan ikke være over 100 prosent til sammen." },
                    nynorsk { +"Du kan ha alderspensjon fra Nav ved siden av uføretrygden. Alderspensjonen din kan du ta ut delvis i gradene 20, 40, 50, 60, 80 og 100 prosent, og summen av uføretrygd og alderspensjon kan ikke være over 100 prosent til sammen." }
                )
            }
            paragraph {
                text(
                    bokmal { +"Får du 100% uføretrygd kan du ikke få utbetalt alderspensjon samtidig. Den delen av alderspensjonen du ikke tar ut før 67 år, blir spart opp og gir høyere utbetaling senere." },
                    nynorsk { +"Får du 100% uføretrygd kan du ikke få utbetalt alderspensjon samtidig. Den delen av alderspensjonen du ikke tar ut før 67 år, blir spart opp og gir høyere utbetaling senere." }
                )
            }
            paragraph {
                text(
                    bokmal { +"Her er en beregning som viser hva du kan få i pensjon om du velger uføretrygd og alderspensjon:" },
                    nynorsk { +"Her er en beregning som viser hva du kan få i pensjon om du velger uføretrygd og alderspensjon:" }
                )
            }

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
            paragraph {
                //TODO fristen og formuleringen må tilpasses enkelstest situasjon det er til bruker kan få uføre
                text(
                    bokmal { +"Du må sende oss en skriftlig tilbakemelding på om du ønsker å ta ut uføretrygd eller om du ønsker å beholde AFP. Vi må ha tilbakemelding fra deg innen" },
                    nynorsk { +"Du må sende oss en skriftlig tilbakemelding på om du ønsker å ta ut uføretrygd eller om du ønsker å beholde AFP. Vi må ha tilbakemelding fra deg innen" }
                )
                list {
                    item {
                        text(
                            bokmal { +"Send beskjed til Nav på https://www.nav.no/skriv-til-oss. Velg tema pensjon og alderspensjon." },
                            nynorsk { +"Send beskjed til Nav på https://www.nav.no/skriv-til-oss. Velg tema pensjon og alderspensjon." }
                        )
                    }
                    //TODO Kan nok bli finere
                    item {
                        text(
                            bokmal { +"Du kan også sende brev merket med fødselsnummeret ditt til: Nav familie- og pensjonsytelser Postboks 6600 Etterstad 0607 OSLO" },
                            nynorsk { +"Du kan også sende brev merket med fødselsnummeret ditt til: Nav familie- og pensjonsytelser Postboks 6600 Etterstad 0607 OSLO" }
                        )
                    }
                    //TODO Veiledning?
                }
            }

            //TODO Table

        }
    }

    override val kategori = Brevkategori.INFORMASJONSBREV

    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.ALLE

    override val sakstyper: Set<ISakstype> = setOf(Sakstype.ALDER)
}