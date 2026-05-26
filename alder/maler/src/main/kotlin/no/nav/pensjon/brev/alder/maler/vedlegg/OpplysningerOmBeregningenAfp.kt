package no.nav.pensjon.brev.alder.maler.vedlegg

import no.nav.pensjon.brev.alder.maler.felles.AntallAarText
import no.nav.pensjon.brev.alder.maler.felles.Ja
import no.nav.pensjon.brev.alder.maler.felles.KronerText
import no.nav.pensjon.brev.alder.maler.felles.Nei
import no.nav.pensjon.brev.alder.maler.vedlegg.opplysningerbruktiberegningen.TabellPoengrekke
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmBeregningenAfpDto
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmBeregningenAfpDto.AfpOrdning
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmBeregningenAfpDto.SivilstandKategori
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmBeregningenAfpDtoSelectors.TilleggspensjonSelectors.poengaarUtenOk
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmBeregningenAfpDtoSelectors.TilleggspensjonSelectors.poengaarUtenOke91
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmBeregningenAfpDtoSelectors.TilleggspensjonSelectors.poengaarUtenOkf92
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmBeregningenAfpDtoSelectors.TilleggspensjonSelectors.sluttpoengtallUtenOk
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmBeregningenAfpDtoSelectors.afpOrdning
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmBeregningenAfpDtoSelectors.afpPensjonsgrad
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmBeregningenAfpDtoSelectors.beregningVirkDatoFom
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmBeregningenAfpDtoSelectors.brukerErFlyktning
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmBeregningenAfpDtoSelectors.ektefelleEllerPartnerInntektOver2G
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmBeregningenAfpDtoSelectors.ektefelleEllerPartnerMottarPensjon
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmBeregningenAfpDtoSelectors.ektefelletilleggInntektBruktIAvkortning
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmBeregningenAfpDtoSelectors.framtidigArligInntekt
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmBeregningenAfpDtoSelectors.harOmsorgspoeng
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmBeregningenAfpDtoSelectors.poengrekke
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmBeregningenAfpDtoSelectors.sivilstand
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmBeregningenAfpDtoSelectors.tidligereArbeidsinntekt
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmBeregningenAfpDtoSelectors.tilleggspensjon
import no.nav.pensjon.brev.alder.model.vedlegg.OpplysningerOmBeregningenAfpDtoSelectors.trygdetid
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.isNotEmpty
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.expression.notNull
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.text

/**
 * Vedlegg «Opplysninger om beregningen» for AFP-brev.
 *
 * Konvertert fra Exstream-malen `PE_AF_opplysninger_om_beregningen_MR71`. Brukes
 * av PE_AF_04_001 (innvilgelse AFP offentlig sektor) og er tenkt gjenbrukt av
 * andre AFP-brev.
 *
 * Strukturen er en to-kolonne «label → verdi» tabell etterfulgt av poengrekken
 * (gjenbruker [TabellPoengrekke]).
 */
@TemplateModelHelpers
val vedleggOpplysningerOmBeregningenAfp =
    createAttachment<LangBokmalNynorsk, OpplysningerOmBeregningenAfpDto>(
        title = {
            text(
                bokmal { +"Opplysninger om beregningen" },
                nynorsk { +"Opplysningar om berekninga" },
            )
        },
        includeSakspart = false,
    ) {
        paragraph {
            text(
                bokmal {
                    +"Hvis du mener at opplysningene vi har lagt til grunn ved beregningen inneholder feil, " +
                        "ber vi deg ta kontakt med Nav."
                },
                nynorsk {
                    +"Dersom du meiner at opplysningane vi har lagt til grunn ved berekninga inneheld feil, " +
                        "ber vi deg om å ta kontakt med Nav."
                },
            )
        }

        // Hoved-info-tabell: «label : verdi» med beregningsvirkdato i headeren.
        paragraph {
            table(
                header = {
                    column(columnSpan = 3) {
                        text(
                            bokmal {
                                +"Opplysninger som ligger til grunn for beregningen fra " +
                                    beregningVirkDatoFom.format()
                            },
                            nynorsk {
                                +"Opplysningar som ligg til grunn for berekninga frå " +
                                    beregningVirkDatoFom.format()
                            },
                        )
                    }
                    column(columnSpan = 2, alignment = ColumnAlignment.RIGHT) { }
                },
            ) {
                row {
                    cell {
                        text(
                            bokmal { +"Grad av full AFP som tas ut" },
                            nynorsk { +"Grad av full AFP som blir teken ut" },
                        )
                    }
                    cell {
                        text(
                            bokmal { +afpPensjonsgrad.format() + " %" },
                            nynorsk { +afpPensjonsgrad.format() + " %" },
                        )
                    }
                }
                row {
                    cell {
                        text(
                            bokmal { +"Beregnet tidligere arbeidsinntekt" },
                            nynorsk { +"Berekna tidlegare arbeidsinntekt" },
                        )
                    }
                    cell { includePhrase(KronerText(tidligereArbeidsinntekt)) }
                }
                row {
                    cell {
                        text(
                            bokmal { +"Anvendt framtidig arbeidsinntekt" },
                            nynorsk { +"Nytta framtidig arbeidsinntekt" },
                        )
                    }
                    cell { includePhrase(KronerText(framtidigArligInntekt)) }
                }
                ifNotNull(ektefelletilleggInntektBruktIAvkortning) { inntekt ->
                    row {
                        cell {
                            text(
                                bokmal {
                                    +"Samlet inntekt som ligger til grunn for inntektsavkorting av ektefelletillegg"
                                },
                                nynorsk {
                                    +"Samla inntekt som ligg til grunn for inntektsavkorting av ektefelletillegg"
                                },
                            )
                        }
                        cell { includePhrase(KronerText(inntekt)) }
                    }
                }
                showIf(afpOrdning.equalTo(AfpOrdning.LO_NHO)
                    .or(afpOrdning.equalTo(AfpOrdning.SPEKTER))
                    .or(afpOrdning.equalTo(AfpOrdning.FINANS))
                    .or(afpOrdning.equalTo(AfpOrdning.STATLIG))) {
                    row {
                        cell {
                            text(
                                bokmal { +"AFP-ordning" },
                                nynorsk { +"AFP-ordning" },
                            )
                        }
                        cell {
                            showIf(afpOrdning.equalTo(AfpOrdning.LO_NHO)) {
                                text(bokmal { +"LO/NHO" }, nynorsk { +"LO/NHO" })
                            }.orShowIf(afpOrdning.equalTo(AfpOrdning.SPEKTER)) {
                                text(bokmal { +"Spekter" }, nynorsk { +"Spekter" })
                            }.orShowIf(afpOrdning.equalTo(AfpOrdning.FINANS)) {
                                text(bokmal { +"Finansnæringen" }, nynorsk { +"Finansnæringa" })
                            }.orShow {
                                text(bokmal { +"Statlig AFP" }, nynorsk { +"Statleg AFP" })
                            }
                        }
                    }
                }
                showIf(sivilstand.notEqualTo(SivilstandKategori.ENSLIG)) {
                    row {
                        cell {
                            text(
                                bokmal { +"Sivilstatus lagt til grunn ved beregningen" },
                                nynorsk { +"Sivilstatus lagd til grunn ved berekninga" },
                            )
                        }
                        cell {
                            showIf(sivilstand.isOneOf(SivilstandKategori.GIFT, SivilstandKategori.GIFT_SEPARERT)) {
                                text(bokmal { +"Gift" }, nynorsk { +"Gift" })
                            }.orShowIf(sivilstand.isOneOf(SivilstandKategori.PARTNER, SivilstandKategori.PARTNER_SEPARERT)) {
                                text(bokmal { +"Partner" }, nynorsk { +"Partnar" })
                            }.orShowIf(sivilstand.equalTo(SivilstandKategori.SAMBOER_FOLKETRYGD_3_2_5LEDD)) {
                                text(
                                    bokmal { +"Samboer (jf. folketrygdloven § 3-2, 5. ledd)" },
                                    nynorsk { +"Sambuar (jf. folketrygdlova § 3-2, 5. ledd)" },
                                )
                            }.orShow {
                                text(
                                    bokmal { +"Samboer (jf. folketrygdloven § 1-5)" },
                                    nynorsk { +"Sambuar (jf. folketrygdlova § 1-5)" },
                                )
                            }
                        }
                    }
                }
                showIf(sivilstand.equalTo(SivilstandKategori.PARTNER_SEPARERT)) {
                    row {
                        cell {
                            text(
                                bokmal { +"Du eller partneren er registrert med annet bosted, eller er på institusjon" },
                                nynorsk { +"Du eller partnaren er registrert med annan bustad eller er på institusjon" },
                            )
                        }
                        cell { includePhrase(Ja) }
                    }
                }
                showIf(sivilstand.equalTo(SivilstandKategori.GIFT_SEPARERT)) {
                    row {
                        cell {
                            text(
                                bokmal { +"Du eller ektefellen er registrert med annet bosted, eller er på institusjon" },
                                nynorsk { +"Du eller ektefellen er registrert med annan bustad eller er på institusjon" },
                            )
                        }
                        cell { includePhrase(Ja) }
                    }
                }
                showIf(sivilstand.isOneOf(SivilstandKategori.GIFT, SivilstandKategori.GIFT_SEPARERT)) {
                    epsMottarPensjonRow(
                        bokmal = "Ektefelle mottar pensjon eller uføretrygd fra folketrygden, eller AFP som det godskrives pensjonspoeng for",
                        nynorsk = "Ektefellen får pensjon frå folketrygda eller AFP som det blir godskrive pensjonspoeng for",
                    )
                }
                showIf(sivilstand.isOneOf(SivilstandKategori.PARTNER, SivilstandKategori.PARTNER_SEPARERT)) {
                    epsMottarPensjonRow(
                        bokmal = "Partner mottar pensjon eller uføretrygd fra folketrygden, eller AFP som det godskrives pensjonspoeng for",
                        nynorsk = "Partnaren får pensjon frå folketrygda eller AFP som det blir godskrive pensjonspoeng for",
                    )
                }
                showIf(sivilstand.isOneOf(SivilstandKategori.SAMBOER_FOLKETRYGD_1_5, SivilstandKategori.SAMBOER_FOLKETRYGD_3_2_5LEDD)) {
                    epsMottarPensjonRow(
                        bokmal = "Samboer mottar pensjon eller uføretrygd fra folketrygden, eller AFP som det godskrives pensjonspoeng for",
                        nynorsk = "Sambuaren får pensjon frå folketrygda eller AFP som det blir godskrive pensjonspoeng for",
                    )
                }
                showIf(sivilstand.isOneOf(SivilstandKategori.GIFT, SivilstandKategori.GIFT_SEPARERT)) {
                    epsInntektOver2GRow(
                        bokmal = "Ektefelle har inntekt over 2 G",
                        nynorsk = "Ektefellen har inntekt over 2 G",
                    )
                }
                showIf(sivilstand.isOneOf(SivilstandKategori.PARTNER, SivilstandKategori.PARTNER_SEPARERT)) {
                    epsInntektOver2GRow(
                        bokmal = "Partner har inntekt over 2 G",
                        nynorsk = "Partnaren har inntekt over 2 G",
                    )
                }
                showIf(sivilstand.isOneOf(SivilstandKategori.SAMBOER_FOLKETRYGD_1_5, SivilstandKategori.SAMBOER_FOLKETRYGD_3_2_5LEDD)) {
                    epsInntektOver2GRow(
                        bokmal = "Samboer har inntekt over 2 G",
                        nynorsk = "Sambuaren har inntekt over 2 G",
                    )
                }
                showIf(brukerErFlyktning) {
                    row {
                        cell {
                            text(
                                bokmal { +"Du er registrert med flyktningstatus" },
                                nynorsk { +"Du er registrert med flyktningstatus" },
                            )
                        }
                        cell { includePhrase(Ja) }
                    }
                }
                showIf(not(brukerErFlyktning)) {
                    row {
                        cell {
                            text(
                                bokmal { +"Trygdetid anvendt i beregningen" },
                                nynorsk { +"Trygdetid nytta i berekninga" },
                            )
                        }
                        cell { includePhrase(AntallAarText(trygdetid)) }
                    }
                }
                ifNotNull(tilleggspensjon) { tp ->
                    row {
                        cell {
                            text(
                                bokmal { +"Sluttpoengtall" },
                                nynorsk { +"Sluttpoengtal" },
                            )
                        }
                        cell {
                            text(
                                bokmal { +tp.sluttpoengtallUtenOk.format() },
                                nynorsk { +tp.sluttpoengtallUtenOk.format() },
                            )
                        }
                    }
                    row {
                        cell {
                            text(
                                bokmal { +"Antall poengår" },
                                nynorsk { +"Talet på poengår" },
                            )
                        }
                        cell { includePhrase(AntallAarText(tp.poengaarUtenOk)) }
                    }
                    showIf(tp.poengaarUtenOkf92.notEqualTo(0)) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Antall år med pensjonsprosent 45" },
                                    nynorsk { +"Talet på år med pensjonsprosent 45" },
                                )
                            }
                            cell { includePhrase(AntallAarText(tp.poengaarUtenOkf92)) }
                        }
                    }
                    showIf(tp.poengaarUtenOke91.notEqualTo(0)) {
                        row {
                            cell {
                                text(
                                    bokmal { +"Antall år med pensjonsprosent 42" },
                                    nynorsk { +"Talet på år med pensjonsprosent 42" },
                                )
                            }
                            cell { includePhrase(AntallAarText(tp.poengaarUtenOke91)) }
                        }
                    }
                }
            }
        }

        // Poengrekken.
        showIf(poengrekke.isNotEmpty()) {
            title1 {
                text(
                    bokmal { +"Poengrekken" },
                    nynorsk { +"Poengrekkja" },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Nedenfor følger oversikt over pensjonsgivende inntekt og poengtall for de enkelte år. " +
                            "Pensjonsgivende inntekt og pensjonspoeng blir fastsatt fra 1967 da folketrygden ble " +
                            "innført. Det kreves minst 40 poengår for full opptjening av tilleggspensjon. Nav " +
                            "mottar opplysninger om pensjonsgivende inntekt fra Skattedirektoratet. Hvis du mener " +
                            "at denne inntekten er feil, må du ta kontakt med skattekontoret."
                    },
                    nynorsk {
                        +"Nedanfor følgjer ei oversikt over pensjonsgivande inntekt og poengtal for dei enkelte åra. " +
                            "Pensjonsgivande inntekt og pensjonspoeng blir fastsette frå 1967, då folketrygda blei " +
                            "innført. Full opptening av tilleggspensjon krev minst 40 poengår. Nav får opplysningar " +
                            "om pensjonsgivande inntekt frå Skattedirektoratet. Dersom du meiner at denne inntekta " +
                            "er feil, må du ta kontakt med skattekontoret."
                    },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Sluttpoengtallet er beregnet som gjennomsnittet av de 20 beste poengårene, eller eventuelt " +
                            "alle poengår hvis det er færre enn 20 år. Det er bare de årene da poengtallene er " +
                            "større enn null som regnes som poengår. Årene som er tatt med ved beregningen av " +
                            "sluttpoengtallet er uthevet."
                    },
                    nynorsk {
                        +"Sluttpoengtalet er berekna som gjennomsnittet av dei 20 beste poengåra, eller alle " +
                            "poengåra dersom det er færre enn 20. Det er berre år med poengtal større enn null som " +
                            "blir rekna som poengår. Åra som er tekne med ved berekninga av sluttpoengtalet er utheva."
                    },
                )
            }
            showIf(harOmsorgspoeng) {
                paragraph {
                    text(
                        bokmal {
                            +"For år der egen opptjening er høyere enn omsorgspoengene, vil egen opptjening være " +
                                "gjeldende i beregningen av pensjonen."
                        },
                        nynorsk {
                            +"For år der eiga opptening er høgare enn omsorgspoenga, vil eiga opptening vere " +
                                "gjeldande i berekninga av pensjonen."
                        },
                    )
                }
            }
            includePhrase(TabellPoengrekke(poengrekke))
        }
    }

private fun no.nav.pensjon.brev.template.dsl.TableScope<LangBokmalNynorsk, OpplysningerOmBeregningenAfpDto>.epsMottarPensjonRow(
    bokmal: String,
    nynorsk: String,
) {
    row {
        cell {
            text(
                bokmal { +bokmal },
                nynorsk { +nynorsk },
            )
        }
        cell {
            showIf(ektefelleEllerPartnerMottarPensjon) {
                includePhrase(Ja)
            }.orShow {
                includePhrase(Nei)
            }
        }
    }
}

private fun no.nav.pensjon.brev.template.dsl.TableScope<LangBokmalNynorsk, OpplysningerOmBeregningenAfpDto>.epsInntektOver2GRow(
    bokmal: String,
    nynorsk: String,
) {
    row {
        cell {
            text(
                bokmal { +bokmal },
                nynorsk { +nynorsk },
            )
        }
        cell {
            showIf(ektefelleEllerPartnerInntektOver2G) {
                includePhrase(Ja)
            }.orShow {
                includePhrase(Nei)
            }
        }
    }
}
