package no.nav.pensjon.brev.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringGjenlevendepensjonBosattUtlandDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringGjenlevendepensjonBosattUtlandDto.AarsakEndring
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringGjenlevendepensjonBosattUtlandDto.ForventetInntektNivaa
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringGjenlevendepensjonBosattUtlandDto.SkattAlternativ
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringGjenlevendepensjonBosattUtlandDto.Sivilstand
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringGjenlevendepensjonBosattUtlandDto.UtbetalingAlternativ
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringGjenlevendepensjonBosattUtlandDto.UtbetalingAlternativ.*
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringGjenlevendepensjonBosattUtlandDtoSelectors.BeregningSelectors.brutto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringGjenlevendepensjonBosattUtlandDtoSelectors.BeregningSelectors.familietillegg
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringGjenlevendepensjonBosattUtlandDtoSelectors.BeregningSelectors.fasteUtgifter
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringGjenlevendepensjonBosattUtlandDtoSelectors.BeregningSelectors.framtidigAarligInntekt
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringGjenlevendepensjonBosattUtlandDtoSelectors.BeregningSelectors.grunnbeloep
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringGjenlevendepensjonBosattUtlandDtoSelectors.BeregningSelectors.grunnpensjon
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringGjenlevendepensjonBosattUtlandDtoSelectors.BeregningSelectors.harYrkesskadegradFraAvdoed
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringGjenlevendepensjonBosattUtlandDtoSelectors.BeregningSelectors.netto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringGjenlevendepensjonBosattUtlandDtoSelectors.BeregningSelectors.saertillegg
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringGjenlevendepensjonBosattUtlandDtoSelectors.BeregningSelectors.tilleggspensjon
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringGjenlevendepensjonBosattUtlandDtoSelectors.BeregningSelectors.virkDatoFom
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringGjenlevendepensjonBosattUtlandDtoSelectors.AvdoedSelectors.doedsfallSkyldesYrkesskade
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringGjenlevendepensjonBosattUtlandDtoSelectors.AvdoedSelectors.flyktning
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringGjenlevendepensjonBosattUtlandDtoSelectors.AvdoedSelectors.ungUfoerFodtEtter1940
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringGjenlevendepensjonBosattUtlandDtoSelectors.AvdoedSelectors.ungUfoerFodtFor1941
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringGjenlevendepensjonBosattUtlandDtoSelectors.EktefelleDataSelectors.inntektOver2g
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringGjenlevendepensjonBosattUtlandDtoSelectors.EktefelleDataSelectors.mottarPensjon
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringGjenlevendepensjonBosattUtlandDtoSelectors.KomponentSelectors.brutto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringGjenlevendepensjonBosattUtlandDtoSelectors.KomponentSelectors.netto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringGjenlevendepensjonBosattUtlandDtoSelectors.PesysDataSelectors.avdoed
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringGjenlevendepensjonBosattUtlandDtoSelectors.PesysDataSelectors.beregning
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringGjenlevendepensjonBosattUtlandDtoSelectors.PesysDataSelectors.ektefelle
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringGjenlevendepensjonBosattUtlandDtoSelectors.PesysDataSelectors.opplysningerOmBeregningen
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringGjenlevendepensjonBosattUtlandDtoSelectors.PesysDataSelectors.oversiktOverPensjonensStoerrelse
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringGjenlevendepensjonBosattUtlandDtoSelectors.PesysDataSelectors.sivilstand
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringGjenlevendepensjonBosattUtlandDtoSelectors.PesysDataSelectors.virkningFom
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringGjenlevendepensjonBosattUtlandDtoSelectors.SaksbehandlervalgSelectors.aarsakEndring
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringGjenlevendepensjonBosattUtlandDtoSelectors.SaksbehandlervalgSelectors.forventetInntektNivaa
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringGjenlevendepensjonBosattUtlandDtoSelectors.SaksbehandlervalgSelectors.harBehovForOppfoelging
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringGjenlevendepensjonBosattUtlandDtoSelectors.SaksbehandlervalgSelectors.skattAlternativ
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringGjenlevendepensjonBosattUtlandDtoSelectors.SaksbehandlervalgSelectors.utbetalingAlternativ
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringGjenlevendepensjonBosattUtlandDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringGjenlevendepensjonBosattUtlandDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.maler.FeatureToggles
import no.nav.pensjon.brev.maler.fraser.common.Constants.NAV_URL
import no.nav.pensjon.brev.maler.fraser.gjenlevende.bruttoNettoRad
import no.nav.pensjon.brev.maler.fraser.gjenlevende.gjenlevendepensjonBeregningTabellBruttoNetto
import no.nav.pensjon.brev.maler.fraser.gjenlevende.gjenlevendepensjonBeregningTabellKunNetto
import no.nav.pensjon.brev.maler.fraser.gjenlevende.nettoRad
import no.nav.pensjon.brev.maler.legacy.vedlegg.vedleggFolketrygdenBokmalEnglish
import no.nav.pensjon.brev.maler.legacy.vedlegg.vedleggOpplysningerOmBeregningenGPUtlandLegacy
import no.nav.pensjon.brev.maler.legacy.vedlegg.vedleggOversiktOverPensjonensStoerrelseGjenlevendepensjonLegacy
import no.nav.pensjon.brev.model.Brevkategori
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.greaterThanOrEqual
import no.nav.pensjon.brev.template.dsl.expression.isNotAnyOf
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

// PE_GP_04_029 Vedtak endring av gjenlevendepensjon (bosatt utland)
// Brevgruppe 2

@TemplateModelHelpers
object VedtakEndringGjenlevendepensjonBosattUtland : RedigerbarTemplate<VedtakEndringGjenlevendepensjonBosattUtlandDto> {

    override val featureToggle = FeatureToggles.vedtakEndringGjenlevendepensjonBosattUtland.toggle

    override val kode = Pesysbrevkoder.Redigerbar.GP_VEDTAK_ENDRING_BOSATT_UTLAND
    override val kategori = Brevkategori.VEDTAK_ENDRING_OG_REVURDERING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper = setOf(Sakstype.GJENLEV)

    override val template = createTemplate(
        languages = languages(Bokmal, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - endring av ytelse til gjenlevende (bosatt utland)",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title {
            text(
                bokmal { +"Gjenlevendepensjon - melding om vedtak" },
                english { +"Survivor's pension - notification of decision" },
            )
        }
        outline {
            // ---- PE_GP_04_029_tekst1: åpningstekst ----
            paragraph {
                text(
                    bokmal {
                        +"Pensjonen din endres fra " + pesysData.virkningFom.format() +
                                ". Du får " + pesysData.beregning.netto.format() +
                                " i pensjon hver måned før skatt."
                    },
                    english {
                        +"Your pension will be changed as of " + pesysData.virkningFom.format() +
                                ". You will receive " + pesysData.beregning.netto.format() +
                                " each month, before tax."
                    },
                )
            }

            // ---- Begrunnelse for endringen (Saksbehandlervalg-fork) ----
            // Erstatter <FRITEKST: VELG ETT AV ALTERNATIVENE UNDER, ELLER FYLL INN EGEN TEKST …>
            // og de tre alternativene som fulgte i Exstream-kilden.
            showIf(saksbehandlerValg.aarsakEndring.equalTo(AarsakEndring.OEKNING_AV_INNTEKT)) {
                paragraph {
                    text(
                        bokmal {
                            +"Pensjonen din blir regulert i forhold til den arbeidsinntekten du har eller forventes å ha." +
                                    " Pensjonen din vil reduseres fordi du har hatt en økning i forventet arbeidsinntekt." +
                                    " Reduksjonen av pensjonen trer i kraft fra og med måneden etter at arbeidsinntekten ble endret."
                        },
                        english {
                            +"Your pension will be regulated in relation to the earned income you have or is expected to have." +
                                    " Your pension will be reduced due to an increase in your expected earned income." +
                                    " Your pension will be changed as of the month from which your earned income has changed."
                        },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter bestemmelsene i folketrygdloven kapittel 17." },
                        english { +"This decision has been made in accordance with Chapter 17 of the National Insurance Act." },
                    )
                }
            }.orShowIf(saksbehandlerValg.aarsakEndring.equalTo(AarsakEndring.REDUKSJON_AV_INNTEKT)) {
                paragraph {
                    text(
                        bokmal {
                            +"Pensjonen din blir regulert i forhold til ƒden arbeidsinntekten du har eller forventes å ha." +
                                    " Pensjonen din vil øke fordi du har hatt en reduksjon i forventet arbeidsinntekt." +
                                    " Økning av pensjonen trer i kraft fra og med den måneden arbeidsinntekten din ble endret."
                        },
                        english {
                            +"Your pension will be regulated in relation to the earned income you have or is expected to have." +
                                    " Your pension will be increased due to a change in your expected earned income." +
                                    " Your pension will be changed as of the month from which your earned income has changed."
                        },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter bestemmelsene i folketrygdloven kapittel 17." },
                        english { +"This decision has been made in accordance with Chapter 17 of the National Insurance Act." },
                    )
                }
            }.orShowIf(saksbehandlerValg.aarsakEndring.equalTo(AarsakEndring.SAMBOER_12_AV_18_MAANEDER)) {
                paragraph {
                    text(
                        bokmal {
                            +"Pensjonen din er endret fordi du har vært samboer i 12 av de siste 18 månedene," +
                                    " og fordi samboeren din har en inntekt som overstiger to ganger grunnbeløpet" +
                                    " eller mottar pensjon eller uføretrygd fra folketrygden."
                        },
                        english {
                            +"Your pension has been changed because you have been cohabiting for 12 of the past 18 months," +
                                    " and because your cohabitant has an income that exceeds twice the national insurance basic amount" +
                                    " or receives a pension from the National Insurance Scheme."
                        },
                    )
                }
                paragraph {
                    text(
                        bokmal {
                            +"Pensjonen vil bli redusert fra måneden etter at dere har vært samboere i 12 av de 18 siste månedene."
                        },
                        english {
                            +"Your pension will be reduced as of the month after you have been cohabiting for 12 of the past 18 months."
                        },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Vedtaket er gjort etter bestemmelsene i folketrygdloven kapittel 3." },
                        english { +"This decision has been made in accordance with Chapter 3 of the National Insurance Act." },
                    )
                }
            }.orShowIf(saksbehandlerValg.aarsakEndring.equalTo(AarsakEndring.FRITEKST)) {
                paragraph {
                    text(bokmal { +fritekst("Egen tekst") }, english { +fritekst("Egen tekst") })
                }
            }

            // ---- PE_GP_tabellA1_utland (brutto != netto) ----
            // Tre kolonner: komponent, brutto, netto.
            showIf(pesysData.beregning.brutto.notEqualTo(pesysData.beregning.netto)) {
                gjenlevendepensjonBeregningTabellBruttoNetto(
                    virkDatoFom = pesysData.beregning.virkDatoFom,
                    grunnbeloep = pesysData.beregning.grunnbeloep,
                    framtidigAarligInntekt = pesysData.beregning.framtidigAarligInntekt,
                    sumBrutto = pesysData.beregning.brutto,
                    sumNetto = pesysData.beregning.netto,
                ) {
                    bruttoNettoRad(
                        "Grunnpensjon", "Basic pension",
                        pesysData.beregning.grunnpensjon.brutto, pesysData.beregning.grunnpensjon.netto,
                    )
                    ifNotNull(pesysData.beregning.tilleggspensjon) { tp ->
                        bruttoNettoRad("Tilleggspensjon", "Supplementary pension", tp.brutto, tp.netto)
                    }
                    ifNotNull(pesysData.beregning.saertillegg) { st ->
                        bruttoNettoRad("Særtillegg", "Special supplement", st.brutto, st.netto)
                    }
                    ifNotNull(pesysData.beregning.fasteUtgifter) { fu ->
                        bruttoNettoRad(
                            "Faste utgifter ved institusjonsopphold", "Fixed costs when institutionalised",
                            fu.brutto, fu.netto,
                        )
                    }
                    ifNotNull(pesysData.beregning.familietillegg) { ft ->
                        bruttoNettoRad("Familietillegg", "Family supplement", ft.brutto, ft.netto)
                    }
                }
            }

            // ---- PE_GP_tabellA2_utland (brutto = netto) ----
            // To kolonner: komponent, netto.
            showIf(pesysData.beregning.brutto.equalTo(pesysData.beregning.netto)) {
                gjenlevendepensjonBeregningTabellKunNetto(
                    virkDatoFom = pesysData.beregning.virkDatoFom,
                    grunnbeloep = pesysData.beregning.grunnbeloep,
                    framtidigAarligInntekt = pesysData.beregning.framtidigAarligInntekt,
                    sumNetto = pesysData.beregning.netto,
                ) {
                    nettoRad("Grunnpensjon", "Basic pension", pesysData.beregning.grunnpensjon.netto)
                    ifNotNull(pesysData.beregning.tilleggspensjon) { tp ->
                        nettoRad("Tilleggspensjon", "Supplementary pension", tp.netto)
                    }
                    ifNotNull(pesysData.beregning.saertillegg) { st ->
                        nettoRad("Særtillegg", "Special supplement", st.netto)
                    }
                    ifNotNull(pesysData.beregning.fasteUtgifter) { fu ->
                        nettoRad(
                            "Faste utgifter ved institusjonsopphold", "Fixed costs when institutionalised",
                            fu.netto,
                        )
                    }
                    ifNotNull(pesysData.beregning.familietillegg) { ft ->
                        nettoRad("Familietillegg", "Family supplement", ft.netto)
                    }
                }
            }

            // ---- PE_GP_04_028_tekst2: trygdetid og avhengige tilleggsavsnitt ----
            paragraph {
                text(bokmal { +"Grunnpensjon" }, english { +"The basic pension" }, BOLD)
                text(
                    bokmal {
                        +" fastsettes med utgangspunkt i folketrygdens grunnbeløp, som for tiden er " +
                                pesysData.beregning.grunnbeloep.format() +
                                ". For at du skal få full grunnpensjon må avdødes trygdetid være minst 40 år." +
                                " Trygdetiden tilsvarer det antall år den avdøde har vært medlem i folketrygden etter fylte 16 år." +
                                " Dersom avdøde var under 67 år på tidspunktet for dødsfallet, blir det beregnet framtidig trygdetid." +
                                " Den regnes vanligvis fram til og med det året avdøde ville ha fylt 66 år." +
                                " Ved mindre enn 40 års trygdetid blir grunnpensjonen tilsvarende redusert." +
                                " Oversikt over trygdetiden er gitt i vedlegg til dette vedtaket."
                    },
                    english {
                        +" is calculated on the basis of the national insurance basic amount, which currently is " +
                                pesysData.beregning.grunnbeloep.format() +
                                ". In order for you to receive a full basic pension, the deceased's period of national insurance cover" +
                                " must be at least 40 years. The period of national insurance cover is equivalent to the years the deceased" +
                                " had been a member of the National Insurance Scheme since turning 16. If the deceased was under 67 years of" +
                                " age at the time of death, credit is also given for their future period of national insurance cover." +
                                " This period is usually calculated to include the years up to and including the year the deceased would have" +
                                " turned 66. If the deceased had less than 40 years of national insurance cover, the basic pension is reduced" +
                                " proportionately. An overview of the period of national insurance cover is enclosed with this decision."
                    },
                )
            }

            showIf(pesysData.avdoed.flyktning) {
                paragraph {
                    text(
                        bokmal { +"Pensjonen er beregnet etter full trygdetid (40 år) fordi avdøde var flyktning." },
                        english {
                            +"The pension has been calculated based on a full period of national insurance cover (40 years)" +
                                    " because the deceased was a refugee."
                        },
                    )
                }
            }

            showIf(pesysData.avdoed.doedsfallSkyldesYrkesskade) {
                paragraph {
                    text(
                        bokmal {
                            +"Når dødsfallet skyldes en yrkesskade blir ikke grunnpensjonen avkortet på grunn av manglende trygdetid."
                        },
                        english {
                            +"If the death occurred as the result of a workplace injury, the basic pension is not reduced even if" +
                                    " the deceased's period of national insurance cover was less than 40 years."
                        },
                    )
                }
            }

            showIf(pesysData.beregning.harYrkesskadegradFraAvdoed and not(pesysData.avdoed.doedsfallSkyldesYrkesskade)) {
                paragraph {
                    text(
                        bokmal {
                            +"Når avdøde mottok en pensjon beregnet helt eller delvis etter regler som gjelder for yrkesskade," +
                                    " blir ikke denne delen av grunnpensjonen avkortet på grunn av manglende trygdetid."
                        },
                        english {
                            +"If the deceased received a pension based entirely or in part on the regulations for workplace injuries," +
                                    " this part of the basic pension is not reduced even if the deceased's period of national insurance cover" +
                                    " was less than 40 years."
                        },
                    )
                }
            }

            showIf(pesysData.ektefelle.mottarPensjon and not(pesysData.ektefelle.inntektOver2g)) {
                paragraph {
                    text(
                        bokmal {
                            +"Grunnpensjonen er justert til 90 prosent av beløpet fordi din samboer mottar pensjon" +
                                    " eller uføretrygd fra folketrygden."
                        },
                        english {
                            +"The basic pension has been adjusted to 90 per cent of the total amount, because your cohabitant" +
                                    " receives a national insurance pension."
                        },
                    )
                }
            }

            showIf(pesysData.ektefelle.inntektOver2g) {
                paragraph {
                    text(
                        bokmal {
                            +"Grunnpensjonen er justert til 90 prosent av beløpet fordi din samboer har inntekt over to ganger grunnbeløpet."
                        },
                        english {
                            +"The basic pension has been adjusted to 90 per cent of the total amount, because your cohabitant has" +
                                    " an income that exceeds twice the national insurance basic amount."
                        },
                    )
                }
            }

            ifNotNull(pesysData.beregning.tilleggspensjon) {
                paragraph {
                    text(
                        bokmal { +"Tilleggspensjonen" }, english { +"Your supplementary pension" }, BOLD
                    )
                    text(
                        bokmal {
                            +" avhenger av antall år med pensjonspoeng avdøde har opptjent og størrelsen på pensjonspoengene." +
                                    " Det gis pensjonspoeng for år med inntekt over folketrygdens grunnbeløp." +
                                    " Det kreves 40 år med pensjonspoeng for å få full tilleggspensjon." +
                                    " På visse vilkår kan det medregnes framtidige poengår fra dødsåret til og med det året avdøde ville ha fylt 66 år." +
                                    " Tilleggspensjonen din utgjør 55 prosent av den tilleggspensjonen avdøde hadde opptjent rett til." +
                                    " Oversikt over poengopptjeningen er gitt i vedlegg til dette vedtaket."
                        },
                        english {
                            +" depends on the number of years the deceased earned pension points and on how" +
                                    " many pension points were earned. You receive pension points for years in which you have an income greater" +
                                    " than the national insurance basic amount. 40 years of pension points are required to receive a full" +
                                    " supplementary pension. In some cases credit may be given for future years of earning pension points." +
                                    " The future years of earning pension points usually include the years between death and up to and including" +
                                    " the year the deceased would have turned 66. Your supplementary pension is 55 per cent of the supplementary" +
                                    " pension the deceased had accumulated. An overview of the accumulated points is enclosed with this decision."
                        },
                    )
                }
                showIf(pesysData.avdoed.doedsfallSkyldesYrkesskade) {
                    paragraph {
                        text(
                            bokmal {
                                +"Når dødsfallet skyldes en yrkesskade gjelder det spesielle regler for beregning av tilleggspensjon."
                            },
                            english {
                                +"When the death is caused by a workplace injury, particular regulations for the calculation of" +
                                        " supplementary pensions apply."
                            },
                        )
                    }
                }
                showIf(pesysData.beregning.harYrkesskadegradFraAvdoed) {
                    paragraph {
                        text(
                            bokmal {
                                +"Når avdøde mottok en pensjon beregnet helt eller delvis etter regler som gjelder for yrkesskade," +
                                        " gjelder det spesielle regler for beregning av tilleggspensjon."
                            },
                            english {
                                +"If the deceased received a pension calculated in whole or in part in accordance with the regulations" +
                                        " for workplace injuries, particular regulations for the calculation of a supplementary pension apply."
                            },
                        )
                    }
                }
                showIf(pesysData.avdoed.ungUfoerFodtEtter1940 or pesysData.avdoed.ungUfoerFodtFor1941) {
                    paragraph {
                        text(
                            bokmal {
                                +"Tilleggspensjonen er beregnet med utgangspunkt i at avdøde tidligere har mottatt pensjon beregnet" +
                                        " etter særbestemmelsene for unge uføre. Dette grunnlaget er fortsatt benyttet, ettersom dette gir den" +
                                        " høyeste tilleggspensjonen. Vær oppmerksom på at tilleggspensjonen etter særbestemmelsene for unge" +
                                        " uføre vil falle bort dersom du velger å flytte fra Norge."
                            },
                            english {
                                +"Supplementary pension has been calculated on the basis that the deceased previously received a" +
                                        " pension based on the particular regulations for young disabled people. This basis has still been used," +
                                        " as it gives the highest supplementary pension. Please note that your supplementary pension based on" +
                                        " the regulations for young disabled people will be terminated if you choose to move away from Norway."
                            },
                        )
                    }
                }
            }

            ifNotNull(pesysData.beregning.saertillegg) {
                paragraph {
                    text(bokmal { +"Særtillegget" }, english { +"A pension supplement" }, BOLD)
                    text(
                        bokmal {
                            +" er gitt for å sikre et minste pensjonsnivå til de som har liten eller ingen opptjening av tilleggspensjon."
                        },
                        english {
                            +" is granted to ensure a minimum pension level for people who are not eligible for a" +
                                    " supplementary pension or only qualify for a small supplementary pension."
                        },
                    )
                }
            }

            // ---- PE_GP_04_028_tekst4: forventet inntekt ----
            title1 { text(bokmal { +"Forventet inntekt" }, english { +"Expected income" }) }
            paragraph {
                text(
                    bokmal {
                        +"Størrelsen på pensjonen er avhengig av hvilken arbeidsinntekt du har eller kan forventes å få ved siden av pensjonen." +
                                " Det er arbeidsinntekt før skatt som legges til grunn."
                    },
                    english {
                        +"The size of your pension depends on your earned income level or the income level you are expected to receive in" +
                                " addition to your pension. Your pre-tax earned income level forms the basis for the calculation of your pension."
                    },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Med arbeidsinntekt menes personinntekt fra arbeid eller næringsvirksomhet som er pensjonsgivende, eller andre" +
                                " ytelser fra folketrygden som likestilles med arbeidsinntekt. Slike ytelser er dagpenger under arbeidsledighet," +
                                " sykepenger, pleiepenger, svangerskapspenger, foreldrepenger og arbeidsavklaringspenger."
                    },
                    english {
                        +"\"Earned income\" means personal income from pensionable work or business activities, or other benefits from the" +
                                " National Insurance Scheme that are considered equivalent to earned income. Such benefits include unemployment" +
                                " benefit (dagpenger), sickness benefit (sykepenger), attendance allowance (pleiepenger), pregnancy allowance" +
                                " (svangerskapspenger), parental benefit (foreldrepenger) and work assessment allowance (arbeidsavklaringspenger)."
                    },
                )
            }

            // Forventet-inntekt-fork (Saksbehandlervalg.forventetInntektNivaa)
            // Erstatter <FRITEKST: VELG ET ALTERNATIV …> + Alt 1/2/3 fra Exstream-kilden.
            showIf(saksbehandlerValg.forventetInntektNivaa.equalTo(ForventetInntektNivaa.UNDER_HALV_G)) {
                paragraph {
                    text(
                        bokmal {
                            +"Vi har lagt til grunn at du har en inntekt tilsvarende " +
                                    pesysData.beregning.framtidigAarligInntekt.format() +
                                    ". Pensjonen din er beregnet uten reduksjon for arbeidsinntekt, siden inntekten din er under halvparten" +
                                    " av folketrygdens grunnbeløp. Hvis inntekten din overstiger halvparten av grunnbeløpet er det viktig at du" +
                                    " varsler Nav om dette."
                        },
                        english {
                            +"The pension calculation has been adjusted to your reported income being " +
                                    pesysData.beregning.framtidigAarligInntekt.format() +
                                    ". Your pension has not been reduced due to registered employment/taxable income, because your income" +
                                    " is less than half of the national insurance basic amount. It is important that you notify Nav if your" +
                                    " income increases to more than half the national insurance basic amount."
                        },
                    )
                }
            }.orShowIf(saksbehandlerValg.forventetInntektNivaa.equalTo(ForventetInntektNivaa.OVER_HALV_G)) {
                paragraph {
                    text(
                        bokmal {
                            +"Pensjonen er redusert etter en forventet inntekt tilsvarende " +
                                    pesysData.beregning.framtidigAarligInntekt.format() +
                                    ". Hvis inntekten din endrer seg er det viktig at du varsler Nav om dette."
                        },
                        english {
                            +"Your pension has been reduced because you have an expected income of " +
                                    pesysData.beregning.framtidigAarligInntekt.format() +
                                    ". It is important that you notify Nav if your income changes."
                        },
                    )
                }
            }.orShowIf(saksbehandlerValg.forventetInntektNivaa.equalTo(ForventetInntektNivaa.REDUSERT_TIL_NULL)) {
                paragraph {
                    text(
                        bokmal {
                            +"Pensjonen er redusert etter en forventet inntekt tilsvarende " +
                                    pesysData.beregning.framtidigAarligInntekt.format() +
                                    ". Du får derfor ikke utbetalt noen pensjon per i dag. Det er imidlertid viktig at du melder fra til" +
                                    " Nav om endringer av inntekten din, slik at vi kan vurdere om endringen kan medføre at du likevel vil få" +
                                    " utbetalt pensjon."
                        },
                        english {
                            +"Your pension has been reduced because you have an expected income of " +
                                    pesysData.beregning.framtidigAarligInntekt.format() +
                                    ". You have consequently not been granted a pension. However, it is important that you notify Nav if" +
                                    " your income changes, so that we can assess whether the changes mean that you will receive a pension."
                        },
                    )
                }
            }.orShowIf(saksbehandlerValg.forventetInntektNivaa.equalTo(ForventetInntektNivaa.FRITEKST)) {
                paragraph { text(bokmal { +fritekst("Fritekst") }, english { +fritekst("Fritekst") }) }
            }

            // Behov for oppfølging — valgfritt avsnitt (Saksbehandlervalg.harBehovForOppfoelging)
            showIf(saksbehandlerValg.harBehovForOppfoelging) {
                paragraph {
                    text(
                        bokmal {
                            +"Nav har som en av sine hovedoppgaver å bistå mottakere av trygdeytelser til å komme i arbeid og aktivitet." +
                                    " Har du behov for hjelp til å komme i arbeid, forbedre din kompetanse, eller øke din stillingsprosent," +
                                    " kan du ta kontakt med ditt lokale Nav-kontor."
                        },
                        english {
                            +"One of the main tasks of Nav is to assist recipients of national insurance benefits in getting work or" +
                                    " starting work-related activities. Contact your local Nav office if you need help in finding work," +
                                    " improving your skills or increasing your labour market participation."
                        },
                    )
                }
            }

            // ---- PE_GP_04_028_tekst5: utbetaling og skatt (kun når netto > 0) ----
            showIf(pesysData.beregning.netto.greaterThan(0)) {
                title1 {
                    text(
                        bokmal { +"Utbetaling og skatt" },
                        english { +"Payment and tax" },
                    )
                }
                paragraph {
                    text(
                        bokmal {
                            +"Gjenlevendepensjonen din blir vanligvis utbetalt den 20. hver måned. Når den 20. er en lørdag eller offentlig" +
                                    " fridag blir pensjonen utbetalt senest siste virkedag før den 20. Oversikt over utbetalingsdatoer finner du" +
                                    " på $NAV_URL."
                        },
                        english {
                            +"Your survivor's pension will normally be paid on the 20th of each month. When the 20th is a Saturday or public" +
                                    " holiday, your pension will be paid at the latest on the last business day before the 20th. You can find a" +
                                    " list of payment dates on $NAV_URL."
                        },
                    )
                }
                paragraph {
                    text(
                        bokmal {
                            +"Ved overføring av pensjon til utenlandsk konto vil overføringstiden medføre noen dagers forsinkelse i utbetalingen."
                        },
                        english {
                            +"On transfer to a foreign account, there will be a few days delay in payment, due to the transfer time."
                        },
                    )
                }

                // Skatt-fork (Saksbehandlervalg.skattAlternativ)
                showIf(saksbehandlerValg.skattAlternativ.equalTo(SkattAlternativ.INFORMASJON_OM_SKATT)) {
                    paragraph {
                        text(
                            bokmal {
                                +"Pensjonen er skattepliktig til Norge og vil bli utbetalt etter fradrag for skatt. Trekk for skatt og eventuelt" +
                                        " andre trekk framkommer på den månedlige utbetalingsmeldingen. Ønsker du endring i skattetrekket, må du" +
                                        " henvende deg til Skatteetaten. Spørsmål om skatteplikt til Norge etter flytting til utlandet må også" +
                                        " rettes til Skatteetaten. Spørsmål om skatteplikt til det landet du har flyttet til, må du selv avklare" +
                                        " med skattemyndighetene der."
                            },
                            english {
                                +"The pension is taxable in Norway and tax will be deducted before the pension is paid to you. Tax deductions" +
                                        " and any other deductions will be specified in the monthly notification of payment. Contact the Norwegian" +
                                        " Tax Administration if you want to have your tax rate altered. Any questions regarding tax liability in" +
                                        " Norway after you have moved abroad must also be addressed to the Norwegian Tax Administration. You must" +
                                        " clarify issues regarding your tax liability in the country you have moved to with that country's tax" +
                                        " authorities."
                            },
                        )
                    }
                }.orShowIf(saksbehandlerValg.skattAlternativ.equalTo(SkattAlternativ.KILDESKATT)) {
                    paragraph {
                        text(
                            bokmal {
                                +"Pensjonen vil bli beskattet i Norge etter reglene om kildeskatt. Reglene innebærer at man i utgangspunktet" +
                                        " er skattepliktig i Norge for norsk pensjon selv om man er skattemessig emigrert fra Norge eller aldri" +
                                        " har bodd i Norge. Det innebærer et forskuddstrekk på 15 prosent av brutto pensjon. Du kan søke om helt" +
                                        " eller delvis skattefritak, som vil avhenge av din skatteplikt til bostedslandet og eventuell skatteavtale" +
                                        " mellom Norge og bostedslandet ditt."
                            },
                            english {
                                +"Your pension will be taxed in Norway pursuant to the rules on withholding tax. The rules mean that you are" +
                                        " in principle liable to tax in Norway for Norwegian pensions even if you have moved from Norway for tax" +
                                        " purposes or have never been resident in Norway. The tax is 15 per cent of the gross pension amount." +
                                        " You can apply for full or partial exemption from this tax liability; whether you qualify for an exemption" +
                                        " will depend on your tax liability in your country of residence and any tax treaties between Norway and" +
                                        " your country of residence."
                            },
                        )
                    }
                    paragraph {
                        text(
                            bokmal {
                                +"Alle spørsmål om kildeskatt og skattefritak skal rettes til Skatt nord, Postboks 6310, NO - 9293 Tromsø." +
                                        " Tlf.: +47 22 07 70 00. Spørsmål om skatteplikt til det landet du har flyttet til, må du selv avklare" +
                                        " med skattemyndighetene der."
                            },
                            english {
                                +"All questions about withholding tax and tax exemptions must be directed to Skatt nord, Postboks 6310," +
                                        " NO - 9293 Tromsø. Tel.: +47 22 07 70 00. Questions related to tax obligations in your current country" +
                                        " of residence must be clarified with local tax authorities."
                            },
                        )
                    }
                    //Jeg har sjekket kontakt informasjon ut hos Skatteetaten. Telefonummer er riktig - +47 22 07 70 00.
                    // Postadressen er gammel og bør være Skatteetaten, Postboks 9200 Grønland, 0134 Oslo.
                    // Ellers kan brukere skrive til Skatteetaten ved å logge inn på www.skatteetaten.no/min-side
                }
            }

            // ---- PE_GP_04_028_tekst6: etterbetaling / feilutbetaling (kun når netto >= 0) ----
            showIf(pesysData.beregning.netto.greaterThanOrEqual(0)) {
                showIf(saksbehandlerValg.utbetalingAlternativ.isNotAnyOf(ETTERBETALING, INGEN_AVSNITT_OM_ETTERBETALING_FEILUTBETALING)) {
                    title1 {
                        text(
                            bokmal { +"Etterbetaling" },
                            english { +"Back pay" },
                        )
                    }
                    paragraph {
                        text(
                            bokmal {
                                +"Du får etterbetalt pensjon fra " + pesysData.virkningFom.format() +
                                        ". Etterbetalingen vil vanligvis bli utbetalt i løpet av sju virkedager. Det kan bli beregnet fradrag i" +
                                        " etterbetalingen for skatt, ytelser du har mottatt fra Nav eller andre, som for eksempel" +
                                        " tjenestepensjonsordninger. Hvis skattekontor eller andre ordninger har krav i etterbetalingen kan denne" +
                                        " bli forsinket. Fradrag i etterbetalingen vil gå fram av utbetalingsmeldingen."
                            },
                            english {
                                +"You will receive a back pay pension from " + pesysData.virkningFom.format() +
                                        ". The back pay will normally be paid within seven business days. Your back pay may be reduced due to" +
                                        " taxes, benefits you have received from the Nav or others (such as from an occupational pension)." +
                                        " If the tax office or other offices have demands on your back pay, your payment may be delayed." +
                                        " Any reduction of your back pay will be noted in your payment notification."
                            },
                        )
                    }
                    paragraph {
                        text(
                            bokmal {
                                +"Ved etterbetalinger som gjelder tidligere år, vil Nav trekke 30 prosent som en standardsats." +
                                        " Dersom du krever at skatteetaten forut for utbetalingen reberegner skatten for de tidligere årene," +
                                        " så må du gi beskjed om dette til Nav innen sju dager etter dato for dette brevet."
                            },
                            english {
                                +"For refunds for previous years, Nav will deduct 30 percent as a standard rate. If you demand in advance" +
                                        " of the payment that the Norwegian Tax Administration recalculate taxes for the previous years," +
                                        " you must notify Nav of this within seven days after the date of this letter."
                            },
                        )
                    }
                }.orShowIf(saksbehandlerValg.utbetalingAlternativ.isNotAnyOf(FEILUTBETALING, INGEN_AVSNITT_OM_ETTERBETALING_FEILUTBETALING)) {
                    // FEILUTBETALING
                    title1 {
                        text(
                            bokmal { +"Feilutbetaling" },
                            english { +"Overpayment" },
                        )
                    }
                    paragraph {
                        text(
                            bokmal {
                                +"Siden pensjonen din er redusert tilbake i tid, medfører dette at du har fått utbetalt for mye i pensjon" +
                                        " i denne perioden. Vi vil sende deg eget forhåndsvarsel om eventuell tilbakekreving av det" +
                                        " feilutbetalte beløpet."
                            },
                            english {
                                +"As your pension reduction has been made retroactive, this means that your pension payments have been too" +
                                        " high in this period. We will send you a separate notification of any demands for the return of the" +
                                        " payments made in error."
                            },
                        )
                    }
                }
            }

            // ---- PE_GP_04_028_tekst7: dine rettigheter / dine plikter ----
            title1 {
                text(
                    bokmal { +"Dine rettigheter" },
                    english { +"Your rights" },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Du har som hovedregel rett til å se sakens dokumenter etter bestemmelsene i forvaltningsloven paragraf 18."
                    },
                    english {
                        +"As a main rule you have the right to see the case documents, in accordance with paragraph 18 of the Public" +
                                " Administration Act."
                    },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Hvis du ikke er enig i vedtaket kan du klage. Fristen for å klage er seks uker fra du mottar dette brevet."
                    },
                    english {
                        +"If you disagree with the decision, you have the right to appeal. The time limit for filing an appeal is six weeks" +
                                " from the date you receive this letter."
                    },
                )
            }

            title1 {
                text(
                    bokmal { +"Dine plikter" },
                    english { +"Your obligations" },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Vi gjør oppmerksom på at du har plikt til å melde fra til Nav om endringer som har betydning for størrelsen på" +
                                " pensjonen din, eller for retten til pensjon. Du må alltid melde fra dersom"
                    },
                    english {
                        +"We wish to remind you that you have a duty to notify Nav of any changes that will have an impact on the amount" +
                                " of pension you receive, or on your rights to a pension. You must always notify Nav if:"
                    },
                )
            }
            paragraph {
                list {
                    item {
                        text(
                            bokmal { +"arbeidsinntekten din endrer seg" },
                            english { +"your earned income changes" },
                        )
                    }
                    showIf(pesysData.sivilstand.equalTo(Sivilstand.ENSLIG)) {
                        item {
                            text(
                                bokmal { +"du gifter deg, inngår partnerskap eller samboerskap" },
                                english { +"you marry, enter a partnership, or begin cohabiting" },
                            )
                        }
                    }
                    showIf(pesysData.sivilstand.equalTo(Sivilstand.SAMBOER_3_2)) {
                        item {
                            text(
                                bokmal {
                                    +"samboerens inntektsforhold endrer seg. Dette gjelder både endringer i arbeidsinntekt," +
                                            " pensjonsinntekter og kapitalinntekt"
                                },
                                english {
                                    +"there are changes to your cohabitant's income. This regards any changes in earned income," +
                                            " pension, or capital income"
                                },
                            )
                        }
                        item {
                            text(
                                bokmal { +"du gifter deg eller inngår partnerskap" },
                                english { +"you marry or enter a partnership" },
                            )
                        }
                        item {
                            text(
                                bokmal { +"du får barn med samboer" },
                                english { +"you have children with your cohabitant" },
                            )
                        }
                        item {
                            text(
                                bokmal { +"en av dere får et varig opphold i institusjon" },
                                english { +"you or your cohabitant are permanently institutionalised" },
                            )
                        }
                        item {
                            text(
                                bokmal { +"du og samboeren din flytter fra hverandre" },
                                english { +"you and your cohabitant move apart" },
                            )
                        }
                        item {
                            text(
                                bokmal { +"samboeren din dør" },
                                english { +"your cohabitant dies" },
                            )
                        }
                    }
                    item {
                        text(
                            bokmal { +"du flytter til et annet land eller tilbake til Norge" },
                            english { +"you move to another country or back to Norway" },
                        )
                    }
                }
            }
            paragraph {
                text(
                    bokmal {
                        +"Hvis du ikke melder fra om endringer og får utbetalt for mye pensjon, kan pensjon som er utbetalt feil kreves tilbake."
                    },
                    english {
                        +"If you do not notify us of changes to your situation and your pension payments subsequently are too high," +
                                " you may have to repay the pension that has been paid to you in error."
                    },
                )
            }
            paragraph {
                text(
                    bokmal { +"Se vår nettside $NAV_URL, eller ta kontakt med Nav dersom du ønsker mer informasjon." },
                    english {
                        +"See our website $NAV_URL, or contact Nav if you would like more information."
                    },
                )
            }
        }
        includeAttachmentIfNotNull(vedleggOpplysningerOmBeregningenGPUtlandLegacy, pesysData.opplysningerOmBeregningen)
        includeAttachmentIfNotNull(vedleggOversiktOverPensjonensStoerrelseGjenlevendepensjonLegacy, pesysData.oversiktOverPensjonensStoerrelse)
        includeAttachment(vedleggFolketrygdenBokmalEnglish)
    }
}






