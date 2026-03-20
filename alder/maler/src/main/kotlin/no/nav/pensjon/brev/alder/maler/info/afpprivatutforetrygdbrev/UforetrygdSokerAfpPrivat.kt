package no.nav.pensjon.brev.alder.maler.info.afpprivatutforetrygdbrev

import no.nav.pensjon.brev.alder.maler.Brevkategori
import no.nav.pensjon.brev.alder.maler.vedlegg.forbeholdTilBeregningeneUforeTrygdSokerAfpPrivat
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.Sakstype
import no.nav.pensjon.brev.alder.model.info.afpprivatutforetrygdbrev.UforeTrygdSokerAfpPrivatDto
import no.nav.pensjon.brev.alder.model.info.afpprivatutforetrygdbrev.UforeTrygdSokerAfpPrivatDtoSelectors.SaksBehandlerValgSelectors.brukerHarSoktAfpPrivat
import no.nav.pensjon.brev.alder.model.info.afpprivatutforetrygdbrev.UforeTrygdSokerAfpPrivatDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.TemplateDescription.ISakstype
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.includeAttachment
import no.nav.pensjon.brevbaker.api.model.LetterMetadata


@TemplateModelHelpers
object UforetrygdSokerAfpPrivat : RedigerbarTemplate<UforeTrygdSokerAfpPrivatDto> {
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
                nynorsk { +"Du må vel mellom uføretrygd og AFP i privat sektor" },
            )
        }

        outline {
            paragraph {
                val dato = fritekst("dato: 01.mm.20åå")
                text(
                    bokmal {
                        +"Du har i dag uføretrygd fra Nav. For å ha rett til AFP i privat sektor, kan du ikke ha fått utbetalt uføretrygd etter den måneden du fyller 62 år. Dette går fram av AFP-tilskottsloven § 8. Hvis du ønsker å ta ut AFP i privat sektor må du si fra deg retten til uføretrygd senest fra " + dato + ". Du må derfor velge mellom:"

                    },
                    nynorsk {
                        +"Du har i dag uføretrygd frå Nav. For å ha rett til AFP i privat sektor, kan du ikkje ha fått utbetalt uføretrygd etter den månaden du fyller 62 år. Dette går fram av AFP-tilskottsloven § 8. Om du ønskjer å ta ut AFP i privat sektor må du seie frå deg retten til uføretrygd seinast frå " + dato + ". Du må derfor vel mellom:"
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
                    nynorsk { +"Kor mykje kan du få?" }
                )
            }
            paragraph {
                text(
                    bokmal { +"Valget mellom uføretrygd og AFP påvirker hvor stor pensjonen din blir nå og ved 67 år." },
                    nynorsk { +"Valet mellom uføretrygd og AFP påverkar kor stor pensjonen din blir ved 62 og 67 år." }
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
                    nynorsk { +"Du kan ha alderspensjon frå Nav saman med uføretrygda du har i dag. Alderspensjonen din kan du ta ut delvis i gradane 20, 40, 50, 60, 80 og 100 prosent. Summen av uføretrygd og alderspensjon kan ikkje vere over 100 prosent til saman. Den delen av alderspensjonen du ikkje tar ut før 67 år, blir spart opp og gjev høgare utbetaling seinare." }
                )
            }
            paragraph {
                text(
                    bokmal { +"Her er en beregning som viser hva du kan få i pensjon om du velger uføretrygd og alderspensjon:" },
                    nynorsk { +"Her er ei utrekning som viser kva du kan få i pensjon om du vel uføretrygd og alderspensjon:" }
                )
            }

            title3 {
                text(
                    bokmal { +"Uføretrygd og alderspensjon" },
                    nynorsk { +"Uføretrygd og alderspensjon " }
                )
            }

            paragraph {
                val `fritekstUføre` = fritekst("0")
                val fritekstAlderspensjon = fritekst("0")
                table(
                    header = {
                        column(alignment = RIGHT) {
                            text(
                                bokmal { +"" },
                                nynorsk { +"" }
                            )
                        }
                        column(alignment = RIGHT) {
                            text(
                                bokmal { +"Uføretrygd " + `fritekstUføre` + "%" },
                                nynorsk { +"Uføretrygd " + `fritekstUføre` + "%" }
                            )
                        }
                        column(alignment = RIGHT) {
                            text(
                                bokmal { +"Alderspensjon " + fritekstAlderspensjon + "% ved 62 år 100% ved 67 år " },
                                nynorsk { +"Alderspensjon " + fritekstAlderspensjon + " % ved 62 år 100% ved 67 år " }
                            )
                        }
                        column(alignment = RIGHT) {
                            text(
                                bokmal { +"Sum pensjon" },
                                nynorsk { +"Sum pensjon" }
                            )
                        }
                    }
                ) {
                    row {
                        cell {
                            text(
                                bokmal { +"62 år" },
                                nynorsk { +"62 år" }
                            )
                        }
                        cell {
                            text(
                                bokmal { +" kr" },
                                nynorsk { +" kr" }
                            )
                        }
                        cell {
                            text(
                                bokmal { +" kr" },
                                nynorsk { +" kr" }
                            )
                        }
                        cell {
                            text(
                                bokmal { +" kr" },
                                nynorsk { +" kr" }
                            )
                        }
                    }
                    row {
                        cell {
                            text(
                                bokmal { +"67 år" },
                                nynorsk { +"67 år" }
                            )
                        }
                        cell {
                            text(
                                bokmal { +" kr" },
                                nynorsk { +" kr" }
                            )
                        }
                        cell {
                            text(
                                bokmal { +" kr" },
                                nynorsk { +" kr" }
                            )
                        }
                        cell {
                            text(
                                bokmal { +" kr" },
                                nynorsk { +" kr" }
                            )
                        }
                    }
                }
            }
            title2 {
                text(
                    bokmal { +"AFP i privat sektor og alderspensjon" },
                    nynorsk { +"AFP i privat sektor og alderspensjon" }
                )
            }
            paragraph {
                text(
                    bokmal { +"AFP i privat sektor kommer i tillegg til alderspensjon fra Nav. Du kan velge å ta ut kun AFP i privat sektor, men da må du først ta ut AFP sammen med minst 20 prosent alderspensjon i minst 1 måned." },
                    nynorsk { +"AFP i privat sektor kjem i tillegg til alderspensjon frå Nav. Du kan velje å ta ut berre AFP i privat sektor, men da må du først ta ut AFP saman med minst 20 prosent alderspensjon i minst 1 månad." }
                )
            }
            paragraph {
                text(
                    bokmal { +"Her er en beregning som viser hva du kan få i pensjon om du velger AFP og alderspensjon:" },
                    nynorsk { +"Her er ei utrekning som viser kva du kan få i pensjon om du vel AFP og alderspensjon:" }
                )
            }

            title3 {
                text(
                    bokmal { +"Uføretrygd og alderspensjon" },
                    nynorsk { +"Uføretrygd og alderspensjon " }
                )
            }

            paragraph {
                table(
                    header = {
                        column(alignment = RIGHT) {
                            text(
                                bokmal { +"" },
                                nynorsk { +"" }
                            )
                        }
                        column(alignment = RIGHT) {
                            text(
                                bokmal { +"AFP" },
                                nynorsk { +"AFP" }
                            )
                        }
                        column(alignment = RIGHT) {
                            text(
                                bokmal { +"Alderspensjon " + fritekst("100" + "%") },
                                nynorsk { +"Alderspensjon " + fritekst("100" + "%") }
                            )
                        }
                        column(alignment = RIGHT) {
                            text(
                                bokmal { +"Sum pensjon" },
                                nynorsk { +"Sum pensjon" }
                            )
                        }
                    }
                ) {
                    row {
                        cell {
                            text(
                                bokmal { +"62 år" },
                                nynorsk { +"62 år" }
                            )
                        }
                        cell {
                            text(
                                bokmal { +" kr" },
                                nynorsk { +" kr" }
                            )
                        }
                        cell {
                            text(
                                bokmal { +" kr" },
                                nynorsk { +" kr" }
                            )
                        }
                        cell {
                            text(
                                bokmal { +" kr" },
                                nynorsk { +" kr" }
                            )
                        }
                    }
                    row {
                        cell {
                            text(
                                bokmal { +"67 år" },
                                nynorsk { +"67 år " }
                            )
                            text(
                                bokmal { +"Utbetalingen av kronetillegg i AFP stanser ved fylte 67 år " },
                                nynorsk { +"Utbetalinga av kronetillegg i AFP stanser ved fylte 67 år " })
                        }
                        cell {
                            text(
                                bokmal { +" kr" },
                                nynorsk { +" kr" }
                            )
                        }
                        cell {
                            text(
                                bokmal { +" kr" },
                                nynorsk { +" kr" }
                            )
                        }
                        cell {
                            text(
                                bokmal { +" kr" },
                                nynorsk { +" kr" }
                            )
                        }
                    }
                }
            }


            paragraph {
                text(
                    bokmal { +"Se vedlegg om forbehold.:" },
                    nynorsk { +"Sjå vedlegg om atterhald." }
                )
            }
            paragraph {
                text(
                    bokmal { +"Det er også viktig å vite:" },
                    nynorsk { +"Det er også viktig å vite:" }
                )
                list {
                    item {
                        text(
                            bokmal { +"AFP, uføretrygd og alderspensjon gir forskjellige muligheter til å ha inntekt ved siden av. Du finner informasjon på www.nav.no" },
                            nynorsk { +"AFP, uføretrygd og alderspensjon gir forskjellige moglegheiter til å ha inntekt saman med pensjonen. Du finner informasjon på www.nav.no" }
                        )
                    }
                    item {
                        text(
                            bokmal { +"AFP, uføretrygd og alderspensjon skattes forskjellig. Du kan kontakte Skatteetaten på www.skatteetaten.no for spørsmål om skattetrekket" },
                            nynorsk { +"AFP, uføretrygd og alderspensjon skattleggjast forskjellig. Du kan kontakte Skatteetaten på www.skatteetaten.no for spørsmål om skattetrekket." }
                        )
                    }
                    item {
                        text(
                            bokmal { +"Uføretrygd fra Nav kan ha innvirkning på rettigheter til uføreforsikringer. Ta kontakt med dine tjenestepensjonsordninger eller forsikringsselskap for avklaring." },
                            nynorsk { +"Uføretrygd frå Nav kan ha verknad på rettigheiter til uføreforsikringar. Ta kontakt med dine tenestepensjonsordningar eller forsikringsselskap for avklaring." }
                        )
                    }
                }
            }
            title2 {
                text(
                    bokmal { +"Dette må du gjøre" },
                    nynorsk { +"Dette må du gjere" }
                )
            }
            showIf(saksbehandlerValg.brukerHarSoktAfpPrivat) {
                paragraph {
                    text(
                        bokmal {
                            +"Du må sende oss en skriftlig tilbakemelding på om du ønsker å ta ut AFP eller om du ønsker å beholde uføretrygden. Vi må ha tilbakemelding fra deg innen " + fritekst(
                                "(Fristen og formuleringen må tilpasses den enkelte situasjon og hvor lang tid det er til bruker fyller 62 år)."
                            )
                        },
                        nynorsk {
                            +"Du må sende oss en skriftlig tilbakemelding på om du ønsker å ta ut AFP eller om du ønsker å beholde uføretrygden. Vi må ha tilbakemelding fra deg innen " + fritekst(
                                "(Fristen og formuleringen må tilpasses den enkelte situasjon og hvor lang tid det er til bruker fyller 62 år)."
                            )
                        }
                    )
                }

            }.orShow {

                paragraph {
                    text(
                        bokmal { +"Dersom du ønsker AFP må du:" },
                        nynorsk { +"Dersom du ønskjer AFP må du: " }
                    )
                    list {
                        item {
                            text(
                                bokmal { +"Søke om alderspensjon og AFP i privat sektor. Du må søke senest måneden før du ønsker å ta ut alderspensjon og AFP." },
                                nynorsk { +"Søke om alderspensjon og AFP i privat sektor. Du må søke seinast månaden før du ønskjer å ta ut alderspensjon og AFP." }
                            )
                        }
                        item {
                            text(
                                bokmal { +"Si ifra deg uføretrygden senest den måneden du fyller 62 år. Send beskjed til Nav på https://www.nav.no/skriv-til-oss. Velg tema pensjon og uføretrygd." },
                                nynorsk { +"Seie frå deg uføretrygda seinast den månaden du fyller 62 år. Send melding til Nav på https://www.nav.no/skriv-til-oss. Vel tema pensjon og uføretrygd." }
                            )
                        }
                        item {
                            text(
                                bokmal { +"Du kan også sende brev merket med fødselsnummeret ditt til: Nav familie- og pensjonsytelser Postboks 6600 Etterstad 0607 OSLO" },
                                nynorsk { +"Du kan også sende brev merket med fødselsnummeret ditt til: Nav familie- og pensjonsytelser , Postboks 6600 Etterstad  0607 OSLO " }
                            )
                        }
                    }
                }
            }

            title2 {
                text(
                    bokmal { +"Trenger du veiledning?" },
                    nynorsk { +"Treng du rettleiing? " }
                )
            }
            paragraph {
                list {
                    item {
                        text(
                            bokmal { +"Du kan ringe oss på telefon 55 55 33 34 dersom du ønsker mer informasjon om reglene for alderspensjon og AFP. " },
                            nynorsk { +"Du kan ringe oss på telefon 55 55 33 34 om du ønskjer meir informasjon om reglane for alderspensjon og AFP." }
                        )
                    }
                    item {
                        text(
                            bokmal { +"Har du spørsmål om uføretrygd må du kontakte Nav kontaktsenter uføretrygd på telefon 55 55 33 33." },
                            nynorsk { +"Har du spørsmål om uføretrygd må du kontakte Nav kontaktsenter uføretrygd på telefon 55 55 33 33." }
                        )
                    }
                    item {
                        text(
                            bokmal { +"Fellesordningen for AFP gir informasjon om vilkårene for å få AFP på 22 98 98 00 og på www.afp.no." },
                            nynorsk { +"Fellesordningen for AFP gir informasjon om vilkåra for å få AFP på 22 98 98 00 og på www.afp.no." }

                        )
                    }
                }
            }
        }
        includeAttachment(forbeholdTilBeregningeneUforeTrygdSokerAfpPrivat)
    }

    override val kategori = Brevkategori.INFORMASJONSBREV

    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.ALLE

    override val sakstyper: Set<ISakstype> = setOf(Sakstype.ALDER)
}