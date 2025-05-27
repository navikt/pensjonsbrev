package no.nav.pensjon.brev.maler.adhoc.gjenlevenderett2027

import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.adhoc.gjenlevenderett2027.Gjenlevenderett2027Dto
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brev.api.model.maler.adhoc.gjenlevenderett2027.Gjenlevenderett2027DtoSelectors.inntekt2019
import no.nav.pensjon.brev.api.model.maler.adhoc.gjenlevenderett2027.Gjenlevenderett2027DtoSelectors.inntekt2020
import no.nav.pensjon.brev.api.model.maler.adhoc.gjenlevenderett2027.Gjenlevenderett2027DtoSelectors.inntekt2021
import no.nav.pensjon.brev.api.model.maler.adhoc.gjenlevenderett2027.Gjenlevenderett2027DtoSelectors.inntekt2022
import no.nav.pensjon.brev.api.model.maler.adhoc.gjenlevenderett2027.Gjenlevenderett2027DtoSelectors.inntekt2023
import no.nav.pensjon.brev.template.LocalizedFormatter.CurrencyFormat
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus

@TemplateModelHelpers
object VedtakForlengelse1962til70 : AutobrevTemplate<Gjenlevenderett2027Dto> {
    override val kode = Pesysbrevkoder.AutoBrev.GJP_VEDTAK_FORLENGELSE_62_70

    override val template: LetterTemplate<*, Gjenlevenderett2027Dto> = createTemplate(
        name = "GJP_VEDTAK_FORLENGELSE_62_70",
        letterDataType = Gjenlevenderett2027Dto::class,
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Forhåndsvarsel - Gjenlevendepensjonen din kan bli forlenget",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title {
            text(
                Bokmal to "Vedtak - Gjenlevendepensjonen din forlenges ",
            )
        }

        outline {
            paragraph {
                text (
                    Bokmal to "Vi viser til tidligere informasjon om at Stortinget har vedtatt endringer i folketrygdlovens regler om ytelser til etterlatte. Endringene gjelder fra 1. januar 2024. "
                )
            }
            paragraph {
                text (
                    Bokmal to "Du fyller vilkårene for rett til pengestøtte som gjenlevende frem til du fyller 67 år. "
                )
            }

            title1 {
                text (
                    Bokmal to "Begrunnelse for vedtaket "
                )
            }
            paragraph {
                text (
                    Bokmal to "Opplysninger om inntekten din i perioden 2019 – 2023, viser at du fyller vilkårene for rett til pengestøtte som gjenlevende frem til du fyller 67 år. "
                )
            }
            paragraph {
                text (
                    Bokmal to "Du beholder retten til gjenlevendepensjon til og med 31. desember 2028. Deretter vil pensjonen omregnes til omstillingsstønad fra 1. januar 2029. "
                )
            }
            paragraph {
                text (
                    Bokmal to "Omstillingsstønaden tilsvarer 2,25 ganger grunnbeløpet per 1. januar 2029. " +
                            "Dersom gjenlevendepensjonen din er beregnet med mindre enn 40 års trygdetid, vil omstillingsstønaden bli redusert tilsvarende. "
                )
            }
            paragraph {
                text (
                    Bokmal to "Vedtaket er gjort etter folketrygdloven § 17 A-3. "
                )
            }

            title1 {
                text (
                    Bokmal to "Hva er inntektsgrensene? "
                )
            }
            paragraph {
                text (
                    Bokmal to "Pensjonsgivende inntekt må ha vært under tre ganger gjennomsnittlig grunnbeløp i folketrygden (G) i både 2022 og 2023: "
                )
            }

            paragraph {
                table(
                    header = {
                        column(1) {
                            text(
                                Bokmal to "År ",
                            )
                        }
                        column(2) {
                            text(
                                Bokmal to "Gjennomsnittlig grunnbeløp (G) ganger 3 ",
                            )
                        }
                    },
                ) {
                    row {
                        cell {
                            text(
                                Bokmal to "2022",
                            )
                        }
                        cell {
                            text(
                                Bokmal to "329 352 kroner",
                            )
                        }
                    }
                    row {
                        cell {
                            text(
                                Bokmal to "2023",
                            )
                        }
                        cell {
                            text(
                                Bokmal to "348 717 kroner",
                            )
                        }
                    }
                }
            }

            paragraph {
                text (
                    Bokmal to "I tillegg må inntekten din i 2019 – 2023 ha vært under to ganger grunnbeløpet i folketrygden (G) i gjennomsnitt i disse fem årene. Det vil si at inntekten kan overstige to ganger grunnbeløpet i et enkelt år, så lenge gjennomsnittet av de fem årene er lavere. "
                )
            }

            paragraph {
                table(
                    header = {
                        column (1) {
                            text(
                                Bokmal to "År",
                            )
                        }
                        column (2){
                            text(
                                Bokmal to "Gjennomsnittlig grunnbeløp (G) ganger 2",
                            )
                        }
                    },
                ) {
                    row {
                        cell {
                            text(
                                Bokmal to "2019",
                            )
                        }
                        cell {
                            text(
                                Bokmal to "197 732 kroner",
                            )
                        }
                    }
                    row {
                        cell {
                            text(
                                Bokmal to "2020",
                            )
                        }
                        cell {
                            text(
                                Bokmal to "201 706 kroner",
                            )
                        }
                    }
                    row {
                        cell {
                            text(
                                Bokmal to "2021",
                            )
                        }
                        cell {
                            text(
                                Bokmal to "209 432 kroner",
                            )
                        }
                    }
                    row {
                        cell {
                            text(
                                Bokmal to "2022",
                            )
                        }
                        cell {
                            text(
                                Bokmal to "219 568 kroner",
                            )
                        }
                    }
                    row {
                        cell {
                            text(
                                Bokmal to "2023",
                            )
                        }
                        cell {
                            text(
                                Bokmal to "232 478 kroner",
                            )
                        }
                    }
                }
            }

            title1 {
                text (
                    Bokmal to "Hvilke opplysninger har vi om deg? "
                )
            }
            paragraph {
                text (
                    Bokmal to "Det er din inntekt i årene 2019 – 2023 som avgjør om du kan beholde pengestøtte som gjenlevende til du blir 67 år. "
                )
            }
            paragraph {
                text (
                    Bokmal to "Ifølge registeropplysninger vi har om deg fra Skatteetaten, har du i årene 2019 – 2023 hatt følgende inntekter: "
                )
            }

            paragraph {
                table(
                    header = {
                        column (1) {
                            text(
                                Bokmal to "År"
                            )
                        }
                        column (2) {
                            text(
                                Bokmal to "Din inntekt"
                            )
                        }
                    },
                ) {
                    row {
                        cell {
                            text(
                                Bokmal to "2019"
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to inntekt2019.format(CurrencyFormat) + " kroner"
                            )
                        }
                    }
                    row {
                        cell {
                            text(
                                Bokmal to "2020"
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to inntekt2020.format(CurrencyFormat) + " kroner"
                            )
                        }
                    }
                    row {
                        cell {
                            text(
                                Bokmal to "2021"
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to inntekt2021.format(CurrencyFormat) + " kroner"
                            )
                        }
                    }
                    row {
                        cell {
                            text(
                                Bokmal to "2022"
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to inntekt2022.format(CurrencyFormat) + " kroner"
                            )
                        }
                    }
                    row {
                        cell {
                            text(
                                Bokmal to "2023"
                            )
                        }
                        cell {
                            textExpr(
                                Bokmal to inntekt2023.format(CurrencyFormat) + " kroner"
                            )
                        }
                    }
                }
            }

            paragraph {
                text (
                    Bokmal to "Din inntekt har ifølge opplysninger fra Skatteetaten vært lavere enn inntektsgrensene i disse årene. "
                )
            }
            paragraph {
                text (
                    Bokmal to "Under forutsetning av at de øvrige vilkårene er oppfylt vil du ha rett til pengestøtte som gjenlevende frem til du fyller 67 år.  "
                )
            }

            title1 {
                text (
                    Bokmal to "Du har rett til å klage "
                )
            }
            paragraph {
                text (
                    Bokmal to "Hvis du mener vedtaket er feil, kan du klage. Fristen for å klage er seks uker fra den datoen du mottok vedtaket. I vedlegget «Dine rettigheter og plikter» får du vite mer om hvordan du går fram. Du finner skjema og informasjon på nav.no/klage. "
                )
            }

            title1 {
                text (
                    Bokmal to "Du har rett til innsyn "
                )
            }
            paragraph {
                text (
                    Bokmal to "Du har rett til å se dokumentene i saken din. Se vedlegg «Dine rettigheter og plikter» for informasjon om hvordan du går fram. "
                )
            }

            title1 {
                text (
                    Bokmal to "Meld fra om endringer "
                )
            }
            paragraph {
                text (
                    Bokmal to "Hvis du får endringer i inntekt, familiesituasjon, jobbsituasjon eller planlegger å flytte til et annet land, kan det påvirke gjenlevendepensjonen din. I slike tilfeller må du derfor straks melde fra til Nav. "
                )
            }

            title1 {
                text(
                    Bokmal to "Har du spørsmål?  "
                )
            }
            paragraph {
                text(
                    Bokmal to "Du finner mer informasjon på nav.no/gjenlevendepensjon. "
                )
            }
            paragraph {
                text(
                    Bokmal to "På nav.no/kontakt kan du chatte eller skrive til oss.  "
                )
            }
            paragraph {
                text(
                    Bokmal to "Hvis du ikke finner svar på nav.no, kan du ringe oss på telefon 55 55 33 34, hverdager 09:00-15:00.  "
                )
            }
        }
        includeAttachment(vedleggDineRettigheterOgPlikterGJP)
    }
}