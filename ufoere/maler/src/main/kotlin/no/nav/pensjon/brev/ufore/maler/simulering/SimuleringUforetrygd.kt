package no.nav.pensjon.brev.ufore.maler.simulering

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment.RIGHT
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.Ufoerebrevkoder
import no.nav.pensjon.brev.ufore.api.model.maler.simulering.SimuleringUforetrygdDto
import no.nav.pensjon.brev.ufore.api.model.maler.simulering.selectors.simuleringUforetrygdDto.*
import no.nav.pensjon.brev.ufore.api.model.maler.simulering.selectors.simuleringUforetrygdDto.inntektsaar.*
import no.nav.pensjon.brev.ufore.api.model.maler.simulering.selectors.simuleringUforetrygdDto.yrkesskade.*
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object SimuleringUforetrygd : AutobrevTemplate<SimuleringUforetrygdDto> {
    override val kode = Ufoerebrevkoder.AutoBrev.UT_SIMULERING

    override val template = createTemplate(
        letterDataType = SimuleringUforetrygdDto::class,
        languages = languages(Language.Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Simulering av uføretrygd",
            distribusjonstype = LetterMetadata.Distribusjonstype.ANNET,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        ),
    ) {
        title {
            text(bokmal { +"Simulering av uføretrygd" })
        }

        outline {
            paragraph {
                text(bokmal {
                    +"Dette er en foreløpig beregning av hvor mye du kan forvente å få utbetalt i uføretrygd før skatt. Det er ikke et endelig resultat på hva utbetalingen kan bli dersom vilkårene for uføretrygd er oppfylt. Beregningen er ikke juridisk bindende."
                })
            }

            title1 { text(bokmal { +"Beregnet fremtidig uføretrygd" }) }
            paragraph {
                table(
                    header = {
                        column(columnSpan = 3) {}
                        column(columnSpan = 1, alignment = RIGHT) {}
                    },
                ) {
                    row {
                        cell {
                            text(
                                bokmal { +"Årsbeløp fra " + virkningstidspunkt.format() + " (før skatt):" },
                                fontType = FontType.BOLD,
                            )
                        }
                        cell {}
                    }
                    row {
                        cell { text(bokmal { +"Ordinær uføretrygd" }) }
                        cell { text(bokmal { +aarligBeloep.format() }) }
                    }
                    row {
                        cell {
                            text(
                                bokmal { +"Månedsbeløp fra " + virkningstidspunkt.format() + " (før skatt):" },
                                fontType = FontType.BOLD,
                            )
                        }
                        cell {}
                    }
                    row {
                        cell { text(bokmal { +"Ordinær uføretrygd" }) }
                        cell { text(bokmal { +maanedligBeloep.format() }) }
                    }
                }
            }

            title1 { text(bokmal { +"Beregningsparametere" }) }
            paragraph {
                table(
                    header = {
                        column(columnSpan = 3) {}
                        column(columnSpan = 1, alignment = RIGHT) {}
                    },
                ) {
                    row {
                        cell { text(bokmal { +"Grunnbeløp (G)" }) }
                        cell { text(bokmal { +grunnbeloep.format() }) }
                    }
                    row {
                        cell { text(bokmal { +"Trygdetid" }) }
                        cell { text(bokmal { +trygdetidAar.format() + " år" }) }
                    }
                    row {
                        cell { text(bokmal { +"Virkningstidspunkt" }) }
                        cell { text(bokmal { +virkningstidspunkt.format() }) }
                    }
                    row {
                        cell { text(bokmal { +"Uføretidspunkt" }) }
                        cell { text(bokmal { +uforetidspunkt.format() }) }
                    }
                    row {
                        cell { text(bokmal { +"Uføregrad" }) }
                        cell { text(bokmal { +uforegrad.format() + " %" }) }
                    }
                    row {
                        cell { text(bokmal { +"Snittet av de tre beste av de siste fem årene med pensjonsgivende inntekt" }) }
                        cell { text(bokmal { +snittInntektTreBesteAvFem.format() }) }
                    }
                    ifNotNull(yrkesskade) { ysk ->
                        row {
                            cell { text(bokmal { +"Yrkesskade" }) }
                            cell { text(bokmal { +"Ja" }) }
                        }
                        row {
                            cell { text(bokmal { +"Yrkesskadegrad" }) }
                            cell { text(bokmal { +ysk.yrkesskadegrad.format() + " %" }) }
                        }
                        row {
                            cell { text(bokmal { +"Inntekt på skadetidspunkt" }) }
                            cell { text(bokmal { +ysk.inntektPaaSkadetidspunkt.format() }) }
                        }
                    }.orShow {
                        row {
                            cell { text(bokmal { +"Yrkesskade" }) }
                            cell { text(bokmal { +"Nei" }) }
                        }
                    }
                }
            }

            title1 { text(bokmal { +"Inntekt benyttet i beregningen" }) }
            paragraph {
                table(
                    header = {
                        column(columnSpan = 1) { text(bokmal { +"År" }) }
                        column(columnSpan = 2, alignment = RIGHT) { text(bokmal { +"Pensjonsgivende inntekt" }) }
                        column(columnSpan = 2, alignment = RIGHT) { text(bokmal { +"Inntekt justert med grunnbeløp" }) }
                        column(columnSpan = 2) { text(bokmal { +"Merknad" }) }
                    },
                ) {
                    forEach(inntektsgrunnlag) { rad ->
                        row {
                            cell { text(bokmal { +rad.aar.format() + ifElse(rad.benyttetIBeregningen, "*", "") }) }
                            cell { text(bokmal { +rad.pensjonsgivendeInntekt.format() }) }
                            cell { text(bokmal { +rad.inntektJustertMedGrunnbeloep.format() }) }
                            cell { ifNotNull(rad.merknad) { m -> text(bokmal { +m }) } }
                        }
                    }
                }
            }
            paragraph {
                text(bokmal { +"* år brukt i beregningen" })
            }
            paragraph {
                text(bokmal {
                    +"Når vi beregner uføretrygden, tar vi utgangspunkt i et valgt uføretidspunkt. Uføretrygden utgjør 66 prosent av gjennomsnittet av de tre beste av de fem siste årene før uføretidspunktet. Inntekt som overstiger seks ganger folketrygdens grunnbeløp blir ikke tatt med i beregningen."
                })
            }

            title1 { text(bokmal { +"Forbehold" }) }
            paragraph {
                text(bokmal { +"Beregning av uføretrygd er gjort på bakgrunn av disse opplysningene:" })
                list {
                    item { text(bokmal { +"Pensjonsgivende inntekt" }) }
                    item { text(bokmal { +"Uføretidspunkt" }) }
                    item { text(bokmal { +"Uføregrad" }) }
                    item { text(bokmal { +"Sivilstand" }) }
                    item { text(bokmal { +"Antall år i utlandet" }) }
                    item { text(bokmal { +"Flyktningstatus" }) }
                    item { text(bokmal { +"Yrkesskadegrad" }) }
                    item { text(bokmal { +"Inntekt på skadetidspunktet" }) }
                }
            }
            paragraph {
                text(bokmal { +"Sivilstand har kun betydning dersom du får beregnet minsteytelse." })
            }
            paragraph {
                text(bokmal { +"Yrkesskadegrad og inntekt på skadetidspunkt har kun betydning dersom deler av eller hele uførheten skyldes yrkesskade." })
            }
            paragraph {
                text(bokmal { +"Hvis du har rettigheter fra andre land enn Norge, rettigheter som ung ufør eller barnetillegg er dette ikke tatt med i beregningen." })
            }
            paragraph {
                text(bokmal { +"For mer informasjon se nav.no/uføretrygd." })
            }
        }
    }
}
