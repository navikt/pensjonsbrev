package no.nav.pensjon.brev.alder.maler.afp

import no.nav.pensjon.brev.alder.maler.felles.Constants.NAV_URL
import no.nav.pensjon.brev.alder.maler.felles.HarDuSpoersmaal
import no.nav.pensjon.brev.alder.maler.felles.KronerText
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpAutoDto
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpAutoDtoSelectors.AfpBeregningSelectors.kompensasjonstilleggBrutto
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpAutoDtoSelectors.AfpBeregningSelectors.kronetilleggBrutto
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpAutoDtoSelectors.AfpBeregningSelectors.livsvarigBrutto
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpAutoDtoSelectors.AfpBeregningSelectors.totalPensjon
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpAutoDtoSelectors.afpBeregning
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpAutoDtoSelectors.bosattINorge
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpAutoDtoSelectors.brukerUnder70Aar
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpAutoDtoSelectors.kravMottattDato
import no.nav.pensjon.brev.alder.model.afp.InnvilgelseAvAfpAutoDtoSelectors.virkningFom
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

/**
 * Vedtak — innvilgelse av AFP i privat sektor (autobrev).
 *
 * Konvertert fra Exstream-malen `PE_AF_04_115`
 */
@TemplateModelHelpers
object InnvilgelseAvAfpAuto : AutobrevTemplate<InnvilgelseAvAfpAutoDto> {

    override val kode = Aldersbrevkoder.AutoBrev.PE_AFP_INNVILGELSE_AUTO

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - innvilgelse av AFP i privat sektor",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                bokmal { +"Søknaden din om avtalefestet pensjon (AFP) i privat sektor er innvilget – melding om vedtak" },
                nynorsk { +"Søknaden din om avtalefestet pensjon (AFP) i privat sektor er innvilga – melding om vedtak" },
            )
        }

        outline {
            paragraph {
                text(
                    bokmal {
                        +"Nav viser til søknaden din om AFP i privat sektor mottatt " +
                            kravMottattDato.format() +
                            ". Du har valgt å ta ut AFP fra " + virkningFom.format() +
                            ". Du får " + afpBeregning.totalPensjon.format() +
                            " hver måned før skatt."
                    },
                    nynorsk {
                        +"Nav viser til søknaden din om AFP i privat sektor motteken " +
                            kravMottattDato.format() +
                            ". Du har valt å ta ut AFP frå " + virkningFom.format() +
                            ". Du får " + afpBeregning.totalPensjon.format() +
                            " kvar månad før skatt."
                    },
                )
            }

            paragraph {
                text(
                    bokmal {
                        +"AFP i privat sektor gis etter bestemmelsene i lov om statstilskott til arbeidstakere som tar " +
                            "ut avtalefestet pensjon i privat sektor (AFP-tilskottsloven). Fellesordningen for AFP har " +
                            "funnet at du oppfyller de avtalemessige vilkårene for rett til AFP. Nav har avgjort andre " +
                            "spørsmål om retten til pensjon, blant annet beregningen. Beregningsreglene står i " +
                            "paragrafene 6 til 11 i AFP-tilskottsloven."
                    },
                    nynorsk {
                        +"AFP i privat sektor blir gitt etter føresegnene i lov om statstilskot til arbeidstakarar som " +
                            "tek ut avtalefesta pensjon i privat sektor (AFP-tilskotslova). Fellesordninga for AFP har " +
                            "funne at du oppfyller dei avtalemessige vilkåra for rett til AFP. Nav har avgjort andre " +
                            "spørsmål om retten til pensjon, mellom anna berekninga. Berekningsreglane står i " +
                            "paragrafane 6 til 11 i AFP-tilskotslova."
                    },
                )
            }

            // Tabell: "Beløp per måned" — restaurert fra de flate text(...)-blokkene i konverteren
            // (Step 5 i convert-exstream-letter). Konvertert til ekte table med betinget rad per komponent.
            paragraph {
                text(
                    bokmal { +"Din AFP per måned blir slik:" },
                    nynorsk { +"AFP-en din per månad blir slik:" },
                )
                table(
                    header = {
                        column { text(bokmal { +"Beløp per måned" }, nynorsk { +"Beløp per månad" }) }
                        column(alignment = ColumnAlignment.RIGHT) {
                            text(bokmal { +"" }, nynorsk { +"" })
                        }
                    },
                ) {
                    ifNotNull(afpBeregning.livsvarigBrutto) { brutto ->
                        row {
                            cell { text(bokmal { +"AFP livsvarig del" }, nynorsk { +"AFP livsvarig del" }) }
                            cell { includePhrase(KronerText(brutto)) }
                        }
                    }
                    ifNotNull(afpBeregning.kronetilleggBrutto) { brutto ->
                        row {
                            cell { text(bokmal { +"AFP kronetillegg" }, nynorsk { +"AFP-kronetillegg" }) }
                            cell { includePhrase(KronerText(brutto)) }
                        }
                    }
                    ifNotNull(afpBeregning.kompensasjonstilleggBrutto) { brutto ->
                        row {
                            cell {
                                text(
                                    bokmal { +"AFP kompensasjonstillegg (skattefritt)" },
                                    nynorsk { +"AFP-kompensasjonstillegg (skattefritt)" },
                                )
                            }
                            cell { includePhrase(KronerText(brutto)) }
                        }
                    }
                    row {
                        cell { text(bokmal { +"Sum AFP før skatt" }, nynorsk { +"Sum AFP før skatt" }, BOLD) }
                        cell { includePhrase(KronerText(afpBeregning.totalPensjon, BOLD)) }
                    }
                }
            }

            // Levealdersjustering — felles tekst for alle.
            paragraph {
                text(
                    bokmal {
                        +"AFP justeres i forhold til forventet levealder ved tidspunkt for uttak. Levealdersjustering " +
                            "er en mekanisme som tar høyde for økt levealder i befolkningen og er innført for å sikre " +
                            "at pensjonssystemet forblir bærekraftig. Du kan lese mer om levealdersjustering på $NAV_URL."
                    },
                    nynorsk {
                        +"AFP blir justert i forhold til forventa levealder ved tidspunktet for uttak. Levealdersjustering " +
                            "er ein mekanisme som tek høgd for auka levealder i befolkninga, og er innført for å sikre " +
                            "at pensjonssystemet held seg berekraftig. Du kan lese meir om levealdersjustering på $NAV_URL."
                    },
                )
            }

            // Per-komponent forklaring (én paragraph per innvilget komponent — null = ikke innvilget).
            ifNotNull(afpBeregning.livsvarigBrutto) {
                paragraph {
                    text(
                        bokmal { +"AFP livsvarig del" },
                        nynorsk { +"AFP livsvarig del" },
                        fontType = BOLD,
                    )
                    text(
                        bokmal {
                            +" er beregnet ut fra opptjeningen din. Grunnlaget er opptjening du har " +
                                "hatt til og med det året du fylte 61 år."
                        },
                        nynorsk {
                            +" er berekna ut frå oppteninga di. Grunnlaget er opptening du har hatt " +
                                "til og med det året du fylte 61 år."
                        },
                    )
                }
            }
            ifNotNull(afpBeregning.kronetilleggBrutto) {
                paragraph {
                    text(
                        bokmal { +"AFP kronetillegg" },
                        nynorsk { +"AFP-kronetillegg" },
                        fontType = BOLD,
                    )
                    text(
                        bokmal {
                            +" utbetales bare til og med den måneden du fyller 67 år. Opphør av " +
                                "kronetillegget har sammenheng med at obligatorisk tjenestepensjon i private " +
                                "arbeidsforhold blir utbetalt fra fylte 67 år."
                        },
                        nynorsk {
                            +" blir berre utbetalt til og med den månaden du fyller 67 år. Opphøyr " +
                                "av kronetillegget har samanheng med at obligatorisk tenestepensjon i private " +
                                "arbeidsforhold blir utbetalt frå fylte 67 år."
                        },
                    )
                }
            }
            ifNotNull(afpBeregning.kompensasjonstilleggBrutto) {
                paragraph {
                    text(
                        bokmal { +"AFP kompensasjonstillegg" },
                        nynorsk { +"AFP-kompensasjonstillegg" },
                        fontType = BOLD,
                    )
                    text(
                        bokmal {
                            +" får du fordi du er født før 1963. Personer født før 1963 har " +
                                "ved fortsatt arbeid ikke samme mulighet til å øke grunnlaget for sin alderspensjon " +
                                "fra folketrygden som de som er født senere."
                        },
                        nynorsk {
                            +" får du fordi du er fødd før 1963. Personar som er fødde før " +
                                "1963, har ved vidare arbeid ikkje det same høvet til å auke grunnlaget for " +
                                "alderspensjonen sin frå folketrygda som dei som er fødde seinare."
                        },
                    )
                }
            }

            // AFP og alderspensjon fra folketrygden.
            title1 {
                text(
                    bokmal { +"AFP i privat sektor og alderspensjon fra folketrygden" },
                    nynorsk { +"AFP i privat sektor og alderspensjon frå folketrygda" },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"AFP i privat sektor gis sammen med alderspensjonen din. Størrelsen på din AFP er uavhengig " +
                            "av uttaksgraden på alderspensjonen. Utbetaling av AFP vil fortsette også dersom du velger " +
                            "å sette uttaksgraden av alderspensjonen din til null."
                    },
                    nynorsk {
                        +"AFP i privat sektor blir gitt saman med alderspensjonen din. Storleiken på AFP-en din er " +
                            "uavhengig av uttaksgraden på alderspensjonen. Utbetaling av AFP vil halde fram også " +
                            "dersom du vel å setje uttaksgraden av alderspensjonen din til null."
                    },
                )
            }

            // Opptjening etter 61 år (kun for brukere under 70 år).
            showIf(brukerUnder70Aar) {
                paragraph {
                    text(
                        bokmal {
                            +"Din AFP er fastsatt på grunnlag av opptjening som du har hatt til og med det året du " +
                                "fylte 61 år. Senere opptjening vil ikke ha noen innvirkning på størrelsen på din AFP. " +
                                "Dersom du har inntekt i årene etter uttak av alderspensjon kan alderspensjonen fra " +
                                "folketrygden øke - dette gjelder til og med det året du fyller 75."
                        },
                        nynorsk {
                            +"AFP-en din er fastsett på grunnlag av opptening som du har hatt til og med det året du " +
                                "fylte 61 år. Seinare opptening vil ikkje ha nokon innverknad på storleiken på AFP-en " +
                                "din. Dersom du har inntekt i åra etter uttak av alderspensjon, kan alderspensjonen " +
                                "frå folketrygda auke - det gjeld til og med det året du fyller 75."
                        },
                    )
                }
            }

            paragraph {
                text(
                    bokmal {
                        +"Som ved alderspensjon fra folketrygden kan du arbeide så mye du vil uten at din AFP blir " +
                            "redusert."
                    },
                    nynorsk {
                        +"Som ved alderspensjon frå folketrygda kan du arbeide så mykje du vil, utan at AFP-en din " +
                            "blir redusert."
                    },
                )
            }

            // Utbetaling — overskrift + tekst.
            title1 {
                text(
                    bokmal { +"Din AFP utbetales månedlig" },
                    nynorsk { +"AFP-en din blir utbetalt månadleg" },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Din AFP blir vanligvis utbetalt den 20. hver måned. Når den 20. er en lørdag eller offentlig " +
                            "fridag blir pensjonen utbetalt senest siste virkedag før den 20. Oversikt over " +
                            "utbetalingsdatoer finner du på $NAV_URL."
                    },
                    nynorsk {
                        +"AFP-en din blir vanlegvis utbetalt den 20. kvar månad. Når den 20. er ein laurdag eller " +
                            "offentleg fridag, blir pensjonen utbetalt seinast siste yrkedagen før den 20. Oversikt " +
                            "over utbetalingsdatoar finn du på $NAV_URL."
                    },
                )
            }

            // Skattepliktig — bosatt i Norge.
            showIf(bosattINorge) {
                paragraph {
                    ifNotNull(afpBeregning.kompensasjonstilleggBrutto) {
                        text(
                            bokmal { +"Med unntak av kompensasjonstillegget er din AFP skattepliktig. " },
                            nynorsk { +"Med unntak av kompensasjonstillegget er AFP-en din skattepliktig. " },
                        )
                    }.orShow {
                        text(
                            bokmal { +"Din AFP er skattepliktig. " },
                            nynorsk { +"AFP-en din er skattepliktig. " },
                        )
                    }
                    text(
                        bokmal {
                            +"Du trenger ikke levere skattekort da skatteopplysningene dine sendes elektronisk fra " +
                                "skatteetaten til Nav. Ta kontakt med skattekontoret ditt hvis du har spørsmål om " +
                                "skatt og skattekort."
                        },
                        nynorsk {
                            +"Du treng ikkje levere skattekort då skatteopplysningane dine blir sende elektronisk frå " +
                                "skatteetaten til Nav. Ta kontakt med skattekontoret ditt dersom du har spørsmål om " +
                                "skatt og skattekort."
                        },
                    )
                }
            }

            // Skattepliktig — bosatt i utlandet.
            showIf(not(bosattINorge)) {
                paragraph {
                    ifNotNull(afpBeregning.kompensasjonstilleggBrutto) {
                        text(
                            bokmal { +"Med unntak av kompensasjonstillegget er din AFP skattepliktig til Norge. " },
                            nynorsk { +"Med unntak av kompensasjonstillegget er AFP-en din skattepliktig til Noreg. " },
                        )
                    }.orShow {
                        text(
                            bokmal { +"Din AFP er skattepliktig til Norge. " },
                            nynorsk { +"AFP-en din er skattepliktig til Noreg. " },
                        )
                    }
                    text(
                        bokmal {
                            +"Spørsmål om skatteplikt til Norge etter flytting til utlandet må rettes til " +
                                "skattekontoret i den kommunen du har flyttet fra. Spørsmål om skatteplikt til det " +
                                "landet du er bosatt i må du selv avklare med skattemyndighetene der."
                        },
                        nynorsk {
                            +"Spørsmål om skatteplikt til Noreg etter flytting til utlandet må rettast til " +
                                "skattekontoret i den kommunen du har flytta frå. Spørsmål om skatteplikt til det " +
                                "landet du er busett i, må du sjølv avklare med skattemyndigheitene der."
                        },
                    )
                }
            }

            paragraph {
                text(
                    bokmal {
                        +"På nettjenesten Din pensjon på $NAV_URL kan du se hvilket skattetrekk som er registrert " +
                            "hos Nav og legge inn eventuelt tilleggstrekk om du ønsker det."
                    },
                    nynorsk {
                        +"På nettenesta Din pensjon på $NAV_URL kan du sjå kva skattetrekk som er registrert hos Nav " +
                            "og leggje inn eventuelt tilleggstrekk om du ønskjer det."
                    },
                )
            }

            // Dine rettigheter — innsyn.
            title1 {
                text(
                    bokmal { +"Dine rettigheter" },
                    nynorsk { +"Dine rettar" },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Du har som hovedregel rett til å se sakens dokumenter etter bestemmelsene i forvaltningsloven " +
                            "paragraf 18."
                    },
                    nynorsk {
                        +"Du har som hovudregel rett til å sjå saksdokumenta etter føresegnene i forvaltingslova " +
                            "paragraf 18."
                    },
                )
            }

            // Klagerett.
            paragraph {
                text(
                    bokmal {
                        +"Hvis du mener at vedtaket ikke er i samsvar med det du har søkt om, kan du klage på vedtaket " +
                            "etter bestemmelsene i folketrygdloven paragraf 21-12. Fristen for å klage er seks uker " +
                            "fra du mottar dette brevet."
                    },
                    nynorsk {
                        +"Dersom du meiner at vedtaket ikkje er i samsvar med det du har søkt om, kan du klage på " +
                            "vedtaket etter føresegnene i folketrygdlova paragraf 21-12. Fristen for å klage er seks " +
                            "veker frå du får dette brevet."
                    },
                )
            }

            includePhrase(HarDuSpoersmaal.alder)
        }
    }
}

