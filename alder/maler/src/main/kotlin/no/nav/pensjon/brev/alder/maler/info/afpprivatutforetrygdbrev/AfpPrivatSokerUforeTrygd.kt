package no.nav.pensjon.brev.alder.maler.info.afpprivatutforetrygdbrev

import no.nav.pensjon.brev.alder.maler.Brevkategori
import no.nav.pensjon.brev.alder.maler.brev.FeatureToggles
import no.nav.pensjon.brev.alder.maler.vedlegg.forbeholdTilBeregningeneAfpTIlUforeTrygd
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.Sakstype
import no.nav.pensjon.brev.alder.model.info.afpprivatutforetrygdbrev.AfpPrivatSokerUforeTrygdDto
import no.nav.pensjon.brev.alder.model.info.afpprivatutforetrygdbrev.AfpPrivatSokerUforeTrygdDtoSelectors.SaksBehandlerValgSelectors.harSoktUforeTrygd
import no.nav.pensjon.brev.alder.model.info.afpprivatutforetrygdbrev.AfpPrivatSokerUforeTrygdDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.api.model.FeatureToggle
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.TemplateDescription.ISakstype
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata


@TemplateModelHelpers
object AfpPrivatSokerUforeTrygd : RedigerbarTemplate<AfpPrivatSokerUforeTrygdDto> {

    override val featureToggle: FeatureToggle = FeatureToggles.afpPrivatUfore.toggle

    override val kode = Aldersbrevkoder.Redigerbar.INFO_BRUKER_AFP_PRIVAT_SOKER_UFORETRYGD


    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Afp Privat til Uføre",
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
                        +"Du har i dag AFP i privat sektor, og har søkt om uføretrygd fra Nav. "

                    },
                    nynorsk {
                        +"Du har i dag AFP i privat sektor, og har søkt om uføretrygd frå Nav. "
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
                            nynorsk { +"Uføretrygd frå Nav og eventuelt alderspensjon" },
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
                    nynorsk { +"Valet mellom uføretrygd og AFP påverkar kor stor pensjonen din blir no og ved 67 år." }
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
                    nynorsk { +"Du får ikkje uføretrygd viss du vel å behalde AFP." }
                )
            }
            paragraph {
                text(
                    bokmal { +"Her er en beregning som viser hva du kan få i pensjon om du velger AFP og alderspensjon:" },
                    nynorsk { +"Her er ei berekning som viser kva du kan få i pensjon om du vel AFP og alderspensjon:" }
                )
            }
            title3 {
                text(
                    bokmal { +"AFP og alderspensjon" },
                    nynorsk { +"AFP og alderspensjon " }
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
                                bokmal { +"Alderspensjon 100%" },
                                nynorsk { +"Alderspensjon 100%" }
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
                                bokmal { +"Dagens utbetaling" },
                                nynorsk { +"Dagens utbetaling" }
                            )
                        }
                        cell {
                            text(
                                bokmal { +fritekst("beløp") + " kr" },
                                nynorsk { +fritekst("beløp") + " kr" }
                            )
                        }
                        cell {
                            text(
                                bokmal { +fritekst("beløp") + " kr" },
                                nynorsk { +fritekst("beløp") + " kr" }
                            )
                        }
                        cell {
                            text(
                                bokmal { +fritekst("beløp") + " kr" },
                                nynorsk { +fritekst("beløp") + " kr" }
                            )
                        }
                    }
                    row {
                        cell {
                            text(
                                bokmal { +"67 år" },
                                nynorsk { +"67 år" }
                            )
                            newline()
                            text(
                                bokmal { +"Utbetalingen av kronetillegg i AFP stanser ved fylte 67 år " },
                                nynorsk { +"Utbetalinga av kronetillegg i AFP stanser ved fylte 67 år " })
                        }
                        cell {
                            text(
                                bokmal { +fritekst("beløp") + " kr" },
                                nynorsk { +fritekst("beløp") + " kr" }
                            )
                        }
                        cell {
                            text(
                                bokmal { +fritekst("beløp") + " kr" },
                                nynorsk { +fritekst("beløp") + " kr" }
                            )
                        }
                        cell {
                            text(
                                bokmal { +fritekst("beløp") + " kr" },
                                nynorsk { +fritekst("beløp") + " kr" }
                            )
                        }
                    }
                }
            }
            title2 {
                text(
                    bokmal { +"Uføretrygd fra Nav og alderspensjon" },
                    nynorsk { +"Uføretrygd frå Nav og alderspensjon" }
                )
            }
            paragraph {
                text(
                    bokmal { +"Om du ønsker å ta ut uføretrygd må du si fra deg retten til AFP i privat sektor. Du vil ikke få tilbake AFP når du har mottatt uføretrygd etter fylte 62 år." },
                    nynorsk { +"Om du ønskjer å ta ut uføretrygd må du seie frå deg retten til AFP i privat sektor. Du vil ikkje få tilbake AFP når du har mottatt uføretrygd etter fylte 62 år." }
                )
            }
            paragraph {
                text(
                    bokmal { +"Du kan ha alderspensjon fra Nav ved siden av uføretrygden. Alderspensjonen din kan du ta ut delvis i gradene 20, 40, 50, 60, 80 og 100 prosent, og summen av uføretrygd og alderspensjon kan ikke være over 100 prosent til sammen." },
                    nynorsk { +"Du kan ha alderspensjon frå Nav saman med uføretrygda du har i dag. Alderspensjonen din kan du ta ut delvis i gradane 20, 40, 50, 60, 80 og 100 prosent. Summen av uføretrygd og alderspensjon kan ikkje vere over 100 prosent til saman." }
                )
            }
            paragraph {
                text(
                    bokmal { +"Får du 100% uføretrygd kan du ikke få utbetalt alderspensjon samtidig. Den delen av alderspensjonen du ikke tar ut før 67 år, blir spart opp og gir høyere utbetaling senere." },
                    nynorsk { +"Får du 100 % uføretrygd kan du ikkje få utbetalt alderspensjon samtidig. Den delen av alderspensjonen du ikkje tar ut før 67 år, blir spart opp og gir høgare utbetaling seinare." }
                )
            }
            paragraph {
                text(
                    bokmal { +"Her er en beregning som viser hva du kan få i pensjon om du velger uføretrygd og alderspensjon:" },
                    nynorsk { +"Her er ei berekning som viser kva du kan få i pensjon om du vel uføretrygd og alderspensjon:" }
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
                                bokmal { +"Uføretrygd " + fritekst("0") + "%" },
                                nynorsk { +"Uføretrygd " + fritekst("0") + "%" }
                            )
                        }
                        column(alignment = RIGHT) {
                            text(
                                bokmal { +"Alderspensjon 100%" },
                                nynorsk { +"Alderspensjon 100%" }
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
                        val dato = fritekst("dd.mm.åå")
                        cell {
                            text(
                                bokmal {
                                    +"Per " + dato
                                },
                                nynorsk {
                                    +"Per " + dato
                                }
                            )
                        }
                        cell {
                            text(
                                bokmal { +fritekst("beløp") + " kr" },
                                nynorsk { +fritekst("beløp") + " kr" }
                            )
                        }
                        cell {
                            text(
                                bokmal { +fritekst("beløp") + " kr" },
                                nynorsk { +fritekst("beløp") + " kr" }
                            )
                        }
                        cell {
                            text(
                                bokmal { +fritekst("beløp") + " kr" },
                                nynorsk { +fritekst("beløp") + " kr" }
                            )
                        }
                    }
                    row {
                        cell {
                            text(
                                bokmal { +"67 år" },
                                nynorsk { +"67 år " }
                            )
                        }
                        cell {
                            text(
                                bokmal { +fritekst("beløp") + " kr" },
                                nynorsk { +fritekst("beløp") + " kr" }
                            )
                        }
                        cell {
                            text(
                                bokmal { +fritekst("beløp") + " kr" },
                                nynorsk { +fritekst("beløp") + " kr" }
                            )
                        }
                        cell {
                            text(
                                bokmal { +fritekst("beløp") + " kr" },
                                nynorsk { +fritekst("beløp") + " kr" }
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
                list {""
                    item {
                        text(
                            bokmal { +"AFP, uføretrygd og alderspensjon gir forskjellige muligheter til å ha inntekt ved siden av. Du finner informasjon på www.nav.no" },
                            nynorsk { +"AFP, uføretrygd og alderspensjon gir forskjellige moglegheiter til å ha inntekt ved sidan av. Du finn informasjon på www.nav.no." }
                        )
                    }
                    item {
                        text(
                            bokmal { +"AFP, uføretrygd og alderspensjon skattes forskjellig. Du kan kontakte Skatteetaten på www.skatteetaten.no for spørsmål om skattetrekket" },
                            nynorsk { +"AFP, uføretrygd og alderspensjon skattast forskjellig. Du kan kontakte Skatteetaten på www.skatteetaten.no for spørsmål om skattetrekket." }
                        )
                    }
                    item {
                        text(
                            bokmal { +"Uføretrygd fra Nav kan ha innvirkning på rettigheter til uføreforsikringer. Ta kontakt med dine tjenestepensjonsordninger eller forsikringsselskap for avklaring." },
                            nynorsk { +"Uføretrygd frå Nav kan ha innverknad på rettar til uføreforsikringar. Ta kontakt med dine tenestepensjonsordningar eller forsikringsselskap for avklaring." }
                        )
                    }
                }
            }
            showIf(saksbehandlerValg.harSoktUforeTrygd.not()) {
                title2 {
                    text(
                        bokmal { +"Dette må du gjøre" },
                        nynorsk { +"Dette må du gjere" }
                    )
                }
                paragraph {
                    val fristOgFormulering = fritekst("Frist og formulering")
                    text(
                        bokmal { +"Du må sende oss en skriftlig tilbakemelding på om du ønsker å ta ut uføretrygd eller om du ønsker å beholde AFP. Vi må ha tilbakemelding fra deg innen " + fristOgFormulering },
                        nynorsk { +"Du må sende oss ei skriftleg tilbakemelding på om du ønskjer å ta ut uføretrygd eller om du ønskjer å behalde AFP. Vi må ha tilbakemelding frå deg innan " + fristOgFormulering }
                    )
                    list {
                        item {
                            text(
                                bokmal { +"Send beskjed til Nav på https://www.nav.no/skriv-til-oss. Velg tema pensjon og alderspensjon." },
                                nynorsk { +"Send beskjed til Nav på https://www.nav.no/skriv-til-oss. Vel tema pensjon og alderspensjon." }
                            )
                        }
                        item {
                            text(
                                bokmal { +"Du kan også sende brev merket med fødselsnummeret ditt til:" },
                                nynorsk { +"Du kan også sende brev merket med fødselsnummeret ditt til:" }
                            )
                            newline()
                            text(
                                bokmal { +"Nav familie- og pensjonsytelser" },
                                nynorsk { +"Nav familie- og pensjonsytelser, Postboks 6600 Etterstad 0607 OSLO" }
                            )
                            newline()
                            text(
                                bokmal { +"Postboks 6600 Etterstad 0607 OSLO" },
                                nynorsk { +"Postboks 6600 Etterstad 0607 OSLO" }
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
                }
            }

        }
        includeAttachment(forbeholdTilBeregningeneAfpTIlUforeTrygd)
    }

    override val kategori = Brevkategori.INFORMASJONSBREV

    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.ALLE

    override val sakstyper: Set<ISakstype> = setOf(Sakstype.ALDER)
}