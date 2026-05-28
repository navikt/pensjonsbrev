package no.nav.pensjon.brev.alder.maler.afpprivat

import no.nav.pensjon.brev.alder.maler.Brevkategori
import no.nav.pensjon.brev.alder.maler.brev.FeatureToggles
import no.nav.pensjon.brev.alder.maler.felles.Constants
import no.nav.pensjon.brev.alder.maler.felles.HarDuSpoersmaal
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.Sakstype
import no.nav.pensjon.brev.alder.model.afpprivat.AfpPrivatBeregningEndringSelectors.kompensasjonstillegg
import no.nav.pensjon.brev.alder.model.afpprivat.AfpPrivatBeregningEndringSelectors.kronetillegg
import no.nav.pensjon.brev.alder.model.afpprivat.AfpPrivatBeregningEndringSelectors.livsvarig
import no.nav.pensjon.brev.alder.model.afpprivat.AfpPrivatBeregningEndringSelectors.sumAfpFoerSkatt
import no.nav.pensjon.brev.alder.model.afpprivat.VedtakAfpPrivatEndringDto
import no.nav.pensjon.brev.alder.model.afpprivat.VedtakAfpPrivatEndringDtoSelectors.PesysDataSelectors.beregning
import no.nav.pensjon.brev.alder.model.afpprivat.VedtakAfpPrivatEndringDtoSelectors.PesysDataSelectors.borIForNorge
import no.nav.pensjon.brev.alder.model.afpprivat.VedtakAfpPrivatEndringDtoSelectors.PesysDataSelectors.brukerAlder
import no.nav.pensjon.brev.alder.model.afpprivat.VedtakAfpPrivatEndringDtoSelectors.PesysDataSelectors.virkningFom
import no.nav.pensjon.brev.alder.model.afpprivat.VedtakAfpPrivatEndringDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.TemplateDescription.ISakstype
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.lessThan
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.notNull
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

/**
 * Redigerbart vedtak — endring av AFP i privat sektor.
 *
 * Konvertert fra Exstream-malen `PE_AF_04_114`. Brevet har bokmål og nynorsk
 * i originalen. Auto-varianten er [VedtakAfpPrivatEndringOpptjeningAuto]
 * (`PE_AF_04_113`).
 */
@TemplateModelHelpers
object VedtakAfpPrivatEndring : RedigerbarTemplate<VedtakAfpPrivatEndringDto> {

    override val kode = Aldersbrevkoder.Redigerbar.PE_AFP_PRIVAT_ENDRING

    override val featureToggle = FeatureToggles.vedtakAfpPrivatEndring.toggle

    override val kategori = Brevkategori.VEDTAK_ENDRING_OG_REVURDERING

    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.VEDTAK

    override val sakstyper: Set<ISakstype> = setOf(Sakstype.AFP_PRIVAT)

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - endring av AFP i privat sektor",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                bokmal { +"Avtalefestet pensjon (AFP) i privat sektor - melding om endring" },
                nynorsk { +"Avtalefesta pensjon (AFP) i privat sektor - melding om endring" },
            )
        }

        outline {
            paragraph {
                text(
                    bokmal {
                        +"Nav har bestemt at din AFP i privat sektor skal endres fra " +
                            pesysData.virkningFom.format() + " fordi opptjeningsgrunnlaget er endret."
                    },
                    nynorsk {
                        +"Nav har bestemt at din AFP i privat sektor skal endrast frå " +
                            pesysData.virkningFom.format() + ", fordi oppteningsgrunnlaget er endra."
                    },
                )
            }

            paragraph {
                text(
                    bokmal {
                        +"Vedtaket er gjort etter bestemmelsene i lov om statstilskott til arbeidstakere som tar ut " +
                            "avtalefestet pensjon i privat sektor (AFP-tilskottsloven)."
                    },
                    nynorsk {
                        +"Vedtaket er gjort etter føresegnene i lov om statstilskott til arbeidstakarar som tek ut " +
                            "avtalefesta pensjon i privat sektor (AFP-tilskotslova)."
                    },
                )
            }

            paragraph {
                text(
                    bokmal { +"Din AFP per måned blir slik:" },
                    nynorsk { +"Din AFP per månad blir slik:" },
                )
            }

            paragraph {
                table(
                    header = {
                        column { text(bokmal { +"" }, nynorsk { +"" }) }
                        column {
                            text(
                                bokmal { +"Beløp per måned" },
                                nynorsk { +"Beløp per månad" },
                            )
                        }
                    },
                ) {
                    ifNotNull(pesysData.beregning.livsvarig) { livsvarigBeloep ->
                        row {
                            cell {
                                text(
                                    bokmal { +"AFP livsvarig del" },
                                    nynorsk { +"AFP livsvarig del" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +livsvarigBeloep.format(false) + " kroner" },
                                    nynorsk { +livsvarigBeloep.format(false) + " kroner" },
                                )
                            }
                        }
                    }
                    ifNotNull(pesysData.beregning.kronetillegg) { kroneBeloep ->
                        row {
                            cell {
                                text(
                                    bokmal { +"AFP kronetillegg" },
                                    nynorsk { +"AFP-kronetillegg" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +kroneBeloep.format(false) + " kroner" },
                                    nynorsk { +kroneBeloep.format(false) + " kroner" },
                                )
                            }
                        }
                    }
                    ifNotNull(pesysData.beregning.kompensasjonstillegg) { kompBeloep ->
                        row {
                            cell {
                                text(
                                    bokmal { +"AFP kompensasjonstillegg (skattefritt)" },
                                    nynorsk { +"AFP-kompensasjonstillegg (skattefritt)" },
                                )
                            }
                            cell {
                                text(
                                    bokmal { +kompBeloep.format(false) + " kroner" },
                                    nynorsk { +kompBeloep.format(false) + " kroner" },
                                )
                            }
                        }
                    }
                    row {
                        cell {
                            text(
                                bokmal { +"Sum AFP før skatt" },
                                nynorsk { +"Sum AFP før skatt" },
                            )
                        }
                        cell {
                            text(
                                bokmal { +pesysData.beregning.sumAfpFoerSkatt.format(false) + " kroner" },
                                nynorsk { +pesysData.beregning.sumAfpFoerSkatt.format(false) + " kroner" },
                            )
                        }
                    }
                }
            }

            paragraph {
                text(
                    bokmal {
                        +"AFP justeres i forhold til forventet levealder ved tidspunkt for uttak. Levealdersjustering er " +
                            "en mekanisme som tar høyde for økt levealder i befolkningen og er innført for å sikre at " +
                            "pensjonssystemet forblir bærekraftig. Du kan lese mer om levealdersjustering på " +
                            Constants.NAV_URL + "."
                    },
                    nynorsk {
                        +"AFP blir justert i forhold til forventa levealder ved tidspunktet for uttak. Levealdersjustering " +
                            "er ein mekanisme som tek høgd for auka levealder i befolkninga, og er innført for å sikre at " +
                            "pensjonssystemet held seg berekraftig. Du kan lese meir om levealdersjustering på " +
                            Constants.NAV_URL + "."
                    },
                )
            }

            ifNotNull(pesysData.beregning.livsvarig) {
                paragraph {
                    text(
                        bokmal {
                            +"AFP livsvarig del er beregnet ut fra opptjeningen din. Grunnlaget er opptjening du har " +
                                "hatt til og med det året du fylte 61 år."
                        },
                        nynorsk {
                            +"AFP livsvarig del er berekna ut frå oppteninga di. Grunnlaget er opptening du har hatt " +
                                "til og med det året du fylte 61 år."
                        },
                    )
                }
            }

            ifNotNull(pesysData.beregning.kronetillegg) {
                paragraph {
                    text(
                        bokmal {
                            +"AFP kronetillegg utbetales bare til og med den måneden du fyller 67 år. Opphør av " +
                                "kronetillegget har sammenheng med at obligatorisk tjenestepensjon i private " +
                                "arbeidsforhold blir utbetalt fra fylte 67 år."
                        },
                        nynorsk {
                            +"AFP-kronetillegg blir berre utbetalt til og med den månaden du fyller 67 år. Opphøyr av " +
                                "kronetillegget har samanheng med at obligatorisk tenestepensjon i private " +
                                "arbeidsforhold blir utbetalt frå fylte 67 år."
                        },
                    )
                }
            }

            ifNotNull(pesysData.beregning.kompensasjonstillegg) {
                paragraph {
                    text(
                        bokmal {
                            +"AFP kompensasjonstillegg får du fordi du er født før 1963. Personer født før 1963 har " +
                                "ved fortsatt arbeid ikke samme mulighet til å øke grunnlaget for sin alderspensjon " +
                                "fra folketrygden som de som er født senere."
                        },
                        nynorsk {
                            +"AFP-kompensasjonstillegg får du fordi du er fødd før 1963. Personar som er fødde før " +
                                "1963, har ved vidare arbeid ikkje det same høvet til å auke grunnlaget for " +
                                "alderspensjonen sin frå folketrygda som dei som er fødde seinare."
                        },
                    )
                }
            }

            title1 {
                text(
                    bokmal { +"AFP i privat sektor og alderspensjon fra folketrygden" },
                    nynorsk { +"AFP i privat sektor og alderspensjon frå folketrygda" },
                )
            }

            paragraph {
                text(
                    bokmal {
                        +"AFP i privat sektor gis sammen med alderspensjonen din. Størrelsen på din AFP er uavhengig av " +
                            "uttaksgraden på alderspensjonen. Utbetaling av AFP vil fortsette også dersom du velger å " +
                            "sette uttaksgraden av alderspensjonen din til null."
                    },
                    nynorsk {
                        +"AFP i privat sektor blir gitt saman med alderspensjonen din. Storleiken på AFP-en din er " +
                            "uavhengig av uttaksgraden på alderspensjonen. Utbetaling av AFP vil halde fram også " +
                            "dersom du vel å setje uttaksgraden av alderspensjonen din til null."
                    },
                )
            }

            showIf(pesysData.brukerAlder.lessThan(70)) {
                paragraph {
                    text(
                        bokmal {
                            +"Din AFP er fastsatt på grunnlag av opptjening som du har hatt til og med det året du fylte " +
                                "61 år. Senere opptjening vil ikke ha noen innvirkning på størrelsen på din AFP. Dersom " +
                                "du har inntekt i årene etter uttak av alderspensjon kan alderspensjonen fra folketrygden " +
                                "øke - dette gjelder til og med det året du fyller 75."
                        },
                        nynorsk {
                            +"AFP-en din er fastsett på grunnlag av opptening som du har hatt til og med det året du " +
                                "fylte 61 år. Seinare opptening vil ikkje ha nokon innverknad på storleiken på AFP-en din. " +
                                "Dersom du har inntekt i åra etter uttak av alderspensjon, kan alderspensjonen frå " +
                                "folketrygda auke - det gjeld til og med det året du fyller 75."
                        },
                    )
                }
            }

            paragraph {
                text(
                    bokmal {
                        +"Som ved alderspensjon fra folketrygden kan du arbeide så mye du vil uten at din AFP blir redusert."
                    },
                    nynorsk {
                        +"Som ved alderspensjon frå folketrygda kan du arbeide så mykje du vil, utan at AFP-en din blir redusert."
                    },
                )
            }

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
                            "utbetalingsdatoer finner du på " + Constants.NAV_URL + "."
                    },
                    nynorsk {
                        +"AFP-en din blir vanlegvis utbetalt den 20. kvar månad. Når den 20. er ein laurdag eller " +
                            "offentleg fridag, blir pensjonen utbetalt seinast siste yrkedagen før den 20. Oversikt " +
                            "over utbetalingsdatoar finn du på " + Constants.NAV_URL + "."
                    },
                )
            }

            showIf(pesysData.borIForNorge) {
                showIf(pesysData.beregning.kompensasjonstillegg.notNull()) {
                    paragraph {
                        text(
                            bokmal {
                                +"Med unntak av kompensasjonstillegget er din AFP skattepliktig. Du trenger ikke levere " +
                                    "skattekort da skatteopplysningene dine sendes elektronisk fra skatteetaten til Nav. " +
                                    "Ta kontakt med skattekontoret ditt hvis du har spørsmål om skatt og skattekort."
                            },
                            nynorsk {
                                +"Med unntak av kompensasjonstillegget er AFP-en din skattepliktig. Du treng ikkje levere " +
                                    "skattekort då skatteopplysningane dine blir sende elektronisk frå skatteetaten til Nav. " +
                                    "Ta kontakt med skattekontoret ditt dersom du har spørsmål om skatt og skattekort."
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
                            nynorsk {
                                +"AFP-en din er skattepliktig. Du treng ikkje levere skattekort då skatteopplysningane " +
                                    "dine blir sende elektronisk frå skatteetaten til Nav. Ta kontakt med skattekontoret " +
                                    "ditt dersom du har spørsmål om skatt og skattekort."
                            },
                        )
                    }
                }
            }

            showIf(pesysData.borIForNorge.not()) {
                showIf(pesysData.beregning.kompensasjonstillegg.notNull()) {
                    paragraph {
                        text(
                            bokmal {
                                +"Med unntak av kompensasjonstillegget er din AFP skattepliktig til Norge. Spørsmål om " +
                                    "skatteplikt til Norge etter flytting til utlandet må rettes til skattekontoret i den " +
                                    "kommunen du har flyttet fra. Spørsmål om skatteplikt til det landet du er bosatt i " +
                                    "må du selv avklare med skattemyndighetene der."
                            },
                            nynorsk {
                                +"Med unntak av kompensasjonstillegget er AFP-en din skattepliktig til Noreg. Spørsmål om " +
                                    "skatteplikt til Noreg etter flytting til utlandet må rettast til skattekontoret i den " +
                                    "kommunen du har flytta frå. Spørsmål om skatteplikt til det landet du er busett i, må " +
                                    "du sjølv avklare med skattemyndigheitene der."
                            },
                        )
                    }
                }.orShow {
                    paragraph {
                        text(
                            bokmal {
                                +"Din AFP er skattepliktig til Norge. Spørsmål om skatteplikt til Norge etter flytting til " +
                                    "utlandet må rettes til skattekontoret i den kommunen du har flyttet fra. Spørsmål om " +
                                    "skatteplikt til det landet du er bosatt i må du selv avklare med skattemyndighetene der."
                            },
                            nynorsk {
                                +"AFP-en din er skattepliktig til Noreg. Spørsmål om skatteplikt til Noreg etter flytting til " +
                                    "utlandet må rettast til skattekontoret i den kommunen du har flytta frå. Spørsmål om " +
                                    "skatteplikt til det landet du er busett i, må du sjølv avklare med skattemyndigheitene der."
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
                    nynorsk {
                        +"På nettenesta Din pensjon på " + Constants.NAV_URL + " kan du sjå kva skattetrekk som er " +
                            "registrert hos Nav, og leggje inn eventuelt tilleggstrekk om du ønskjer det."
                    },
                )
            }

            title1 {
                text(
                    bokmal { +"Din rett til innsyn og klage" },
                    nynorsk { +"Retten din til innsyn og klage" },
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

            paragraph {
                text(
                    bokmal {
                        +"Hvis du mener at vedtaket ikke er i samsvar med det du har søkt om, kan du klage på vedtaket " +
                            "etter bestemmelsene i folketrygdloven paragraf 21-12. Fristen for å klage er seks uker fra " +
                            "du mottar dette brevet."
                    },
                    nynorsk {
                        +"Dersom du meiner at vedtaket ikkje er i samsvar med det du har søkt om, kan du klage på " +
                            "vedtaket etter føresegnene i folketrygdlova paragraf 21-12. Fristen for å klage er seks " +
                            "veker frå du får dette brevet."
                    },
                )
            }

            paragraph {
                text(
                    bokmal { +"Ta kontakt med Nav dersom du ønsker mer informasjon." },
                    nynorsk { +"Ta kontakt med Nav dersom du ønskjer meir informasjon." },
                )
            }

            includePhrase(HarDuSpoersmaal.afpPrivat)
        }
    }
}
