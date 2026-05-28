package no.nav.pensjon.brev.alder.maler.afpprivat

import no.nav.pensjon.brev.alder.maler.felles.Constants
import no.nav.pensjon.brev.alder.maler.felles.HarDuSpoersmaal
import no.nav.pensjon.brev.alder.maler.felles.KronerText
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.afpprivat.AfpPrivatBeregningEndringSelectors.kompensasjonstillegg
import no.nav.pensjon.brev.alder.model.afpprivat.AfpPrivatBeregningEndringSelectors.kronetillegg
import no.nav.pensjon.brev.alder.model.afpprivat.AfpPrivatBeregningEndringSelectors.livsvarig
import no.nav.pensjon.brev.alder.model.afpprivat.AfpPrivatBeregningEndringSelectors.sumAfpFoerSkatt
import no.nav.pensjon.brev.alder.model.afpprivat.VedtakAfpPrivatEndringOpptjeningAutoDto
import no.nav.pensjon.brev.alder.model.afpprivat.VedtakAfpPrivatEndringOpptjeningAutoDtoSelectors.beregning
import no.nav.pensjon.brev.alder.model.afpprivat.VedtakAfpPrivatEndringOpptjeningAutoDtoSelectors.borIForNorge
import no.nav.pensjon.brev.alder.model.afpprivat.VedtakAfpPrivatEndringOpptjeningAutoDtoSelectors.brukerAlder
import no.nav.pensjon.brev.alder.model.afpprivat.VedtakAfpPrivatEndringOpptjeningAutoDtoSelectors.virkningFom
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.lessThan
import no.nav.pensjon.brev.template.dsl.expression.notNull
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

/**
 * Auto-vedtak — endring av opptjening for AFP i privat sektor.
 *
 * Konvertert fra Exstream-malen `PE_AF_04_113`. Brevet har kun bokmål.
 * Den redigerbare varianten av samme situasjon er
 * [VedtakAfpPrivatEndring] (`PE_AF_04_114`).
 */
@TemplateModelHelpers
object VedtakAfpPrivatEndringOpptjeningAuto : AutobrevTemplate<VedtakAfpPrivatEndringOpptjeningAutoDto> {

    override val kode = Aldersbrevkoder.AutoBrev.PE_AFP_PRIVAT_ENDRING_OPPTJENING_AUTO

    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - endring av opptjening - AFP i privat sektor",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                bokmal { +"Avtalefestet pensjon (AFP) i privat sektor - melding om endring" },
            )
        }

        outline {
            paragraph {
                text(
                    bokmal {
                        +"Nav har omregnet pensjonen din og utbetalingen er endret med virkning fra " +
                            virkningFom.format() + " fordi opptjeningsgrunnlaget er endret."
                    },
                )
            }

            paragraph {
                text(
                    bokmal {
                        +"AFP i privat sektor gis etter bestemmelsene i lov om statstilskott til arbeidstakere som tar ut " +
                            "avtalefestet pensjon i privat sektor (AFP-tilskottsloven). Fellesordningen for AFP har funnet " +
                            "at du oppfyller de avtalemessige vilkårene for rett til AFP. Nav har avgjort andre spørsmål " +
                            "om retten til pensjon, blant annet beregningen. Beregningsreglene står i paragrafene 6 til 11 " +
                            "i AFP-tilskottsloven."
                    },
                )
            }

            paragraph {
                text(
                    bokmal { +"Din AFP per måned blir slik:" },
                )
            }

            paragraph {
                table(
                    header = {
                        column { text(bokmal { +"" }) }
                        column { text(bokmal { +"Beløp per måned" }) }
                    },
                ) {
                    ifNotNull(beregning.livsvarig) { livsvarigBeloep ->
                        row {
                            cell { text(bokmal { +"AFP livsvarig del" }) }
                            cell { includePhrase(KronerText(livsvarigBeloep)) }
                        }
                    }
                    ifNotNull(beregning.kronetillegg) { kroneBeloep ->
                        row {
                            cell { text(bokmal { +"AFP kronetillegg" }) }
                            cell { includePhrase(KronerText(kroneBeloep)) }
                        }
                    }
                    ifNotNull(beregning.kompensasjonstillegg) { kompBeloep ->
                        row {
                            cell { text(bokmal { +"AFP kompensasjonstillegg (skattefritt)" }) }
                            cell { includePhrase(KronerText(kompBeloep)) }
                        }
                    }
                    row {
                        cell { text(bokmal { +"Sum AFP før skatt" }, BOLD) }
                        cell { includePhrase(KronerText(beregning.sumAfpFoerSkatt, BOLD)) }
                    }
                }
            }

            ifNotNull(beregning.livsvarig) {
                paragraph {
                    text(
                        bokmal {
                            +"AFP livsvarig del er beregnet ut fra opptjeningen din. Grunnlaget er opptjening du har " +
                                "hatt til og med det året du fylte 61 år."
                        },
                    )
                }
            }

            ifNotNull(beregning.kronetillegg) {
                paragraph {
                    text(
                        bokmal {
                            +"AFP kronetillegg utbetales bare til og med den måneden du fyller 67 år. Opphør av " +
                                "kronetillegget har sammenheng med at obligatorisk tjenestepensjon i private " +
                                "arbeidsforhold blir utbetalt fra fylte 67 år."
                        },
                    )
                }
            }

            ifNotNull(beregning.kompensasjonstillegg) {
                paragraph {
                    text(
                        bokmal {
                            +"AFP kompensasjonstillegg får du fordi du er født før 1963. Personer født før 1963 har " +
                                "ved fortsatt arbeid ikke samme mulighet til å øke grunnlaget for sin alderspensjon " +
                                "fra folketrygden som de som er født senere."
                        },
                    )
                }
            }

            title1 {
                text(
                    bokmal { +"Begrunnelse for vedtaket" },
                )
            }

            paragraph {
                text(
                    bokmal {
                        +"Pensjonen din er omregnet fordi det er endringer i opptjeningsgrunnlaget. Opptjening kan være " +
                            "pensjonsgivende inntekt og/eller omsorgsopptjening. Ny opptjening for det året du fylte " +
                            "61 år kan tas med i beregningen av pensjonen fra og med det året du fyller 63 år. Ved " +
                            "korrigeringer i opptjening for ferdiglignede år vil Nav gjøre følgende:"
                    },
                )
                list {
                    item {
                        text(
                            bokmal {
                                +"Hvis endringen medfører en økning i pensjonen, får du en etterbetaling fra det " +
                                    "tidspunktet opptjeningen kan bli medregnet."
                            },
                        )
                    }
                    item {
                        text(
                            bokmal {
                                +"Hvis endringen medfører en reduksjon i pensjonen, blir utbetalingen satt ned med " +
                                    "virkning fra første mulige måned etter at Nav fikk melding om endringen."
                            },
                        )
                    }
                }
            }

            title1 {
                text(
                    bokmal { +"Din AFP blir levealdersjustert" },
                )
            }

            paragraph {
                text(
                    bokmal {
                        +"AFP justeres i forhold til forventet levealder ved tidspunkt for uttak. Levealdersjustering er " +
                            "en mekanisme som tar høyde for økt levealder i befolkningen og er innført for å sikre at " +
                            "pensjonssystemet forblir bærekraftig. Du kan lese mer om levealdersjustering på " +
                            Constants.NAV_URL + "."
                    },
                )
            }

            title1 {
                text(
                    bokmal { +"AFP i privat sektor og alderspensjon fra folketrygden" },
                )
            }

            paragraph {
                text(
                    bokmal {
                        +"AFP i privat sektor gis sammen med alderspensjonen din. Størrelsen på din AFP er uavhengig av " +
                            "uttaksgraden på alderspensjonen. Utbetaling av AFP vil fortsette også dersom du velger å " +
                            "sette uttaksgraden av alderspensjonen din til null."
                    },
                )
            }

            showIf(brukerAlder.lessThan(70)) {
                paragraph {
                    text(
                        bokmal {
                            +"Din AFP er fastsatt på grunnlag av opptjening som du har hatt til og med det året du fylte " +
                                "61 år. Senere opptjening vil ikke ha noen innvirkning på størrelsen på din AFP. Dersom " +
                                "du har inntekt i årene etter uttak av alderspensjon kan alderspensjonen fra folketrygden " +
                                "øke - dette gjelder til og med det året du fyller 75."
                        },
                    )
                }
            }

            paragraph {
                text(
                    bokmal { +"Som ved alderspensjon fra folketrygden kan du arbeide så mye du vil uten at din AFP blir redusert." },
                )
            }

            title1 {
                text(
                    bokmal { +"Din AFP utbetales månedlig" },
                )
            }

            paragraph {
                text(
                    bokmal {
                        +"Din AFP blir vanligvis utbetalt den 20. hver måned. Når den 20. er en lørdag eller offentlig " +
                            "fridag blir pensjonen utbetalt senest siste virkedag før den 20. Oversikt over " +
                            "utbetalingsdatoer finner du på " + Constants.NAV_URL + "."
                    },
                )
            }

            showIf(borIForNorge) {
                showIf(beregning.kompensasjonstillegg.notNull()) {
                    paragraph {
                        text(
                            bokmal {
                                +"Med unntak av kompensasjonstillegget er din AFP skattepliktig. Du trenger ikke levere " +
                                    "skattekort da skatteopplysningene dine sendes elektronisk fra skatteetaten til Nav. " +
                                    "Ta kontakt med skattekontoret ditt hvis du har spørsmål om skatt og skattekort."
                            },
                        )
                    }
                }.orShow {
                    paragraph {
                        text(
                            bokmal {
                                +"Din AFP er skattepliktig. Du trenger ikke levere skattekort da skatteopplysningene dine " +
                                    "sendes elektronisk fra skatteetaten til Nav. Ta kontakt med skattekontoret ditt hvis " +
                                    "du har spørsmål om skatt og skattekort."
                            },
                        )
                    }
                }
            }

            paragraph {
                text(
                    bokmal {
                        +"På nettjenesten Din pensjon på " + Constants.NAV_URL + " kan du se hvilket skattetrekk som er " +
                            "registrert hos Nav og legge inn eventuelt tilleggstrekk om du ønsker det."
                    },
                )
            }

            title1 {
                text(
                    bokmal { +"Din rett til innsyn og klage" },
                )
            }

            paragraph {
                text(
                    bokmal {
                        +"Du har som hovedregel rett til å se sakens dokumenter etter bestemmelsene i forvaltningsloven " +
                            "paragraf 18."
                    },
                )
            }

            paragraph {
                text(
                    bokmal {
                        +"Hvis du mener at vedtaket ikke er i samsvar med det du har søkt om, kan du klage på vedtaket " +
                            "etter bestemmelsene i folketrygdloven paragraf 21-12. Fristen for å klage er seks uker fra " +
                            "du mottar dette brevet."
                    },
                )
            }

            paragraph {
                text(
                    bokmal { +"Ta kontakt med Nav dersom du ønsker mer informasjon." },
                )
            }

            includePhrase(HarDuSpoersmaal.afpPrivat)
        }
    }
}
