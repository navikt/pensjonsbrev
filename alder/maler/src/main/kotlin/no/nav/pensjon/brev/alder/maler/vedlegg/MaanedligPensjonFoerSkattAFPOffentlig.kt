package no.nav.pensjon.brev.alder.maler.vedlegg

import no.nav.pensjon.brev.alder.maler.vedlegg.maanedligPensjonFoerSkattAFP.TabellDinMaanedligAFPOffentlig
import no.nav.pensjon.brev.alder.maler.vedlegg.maanedligPensjonFoerSkattAFP.TabellMaanedligPensjonAFPOffentlig
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPOffentligDto
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPOffentligDtoSelectors.AFPStatBeregningListeSelectors.antallBeregningsperioder
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPOffentligDtoSelectors.AFPStatBeregningSelectors.afpTillegg
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPOffentligDtoSelectors.AFPStatBeregningSelectors.brukerErFlyktning
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPOffentligDtoSelectors.AFPStatBeregningSelectors.fullTrygdetid
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPOffentligDtoSelectors.AFPStatBeregningSelectors.grunnbelop
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPOffentligDtoSelectors.AFPStatBeregningSelectors.minstenivaIndividuell
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPOffentligDtoSelectors.AFPStatBeregningSelectors.sartillegg
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPOffentligDtoSelectors.AFPStatBeregningSelectors.tilleggspensjon
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPOffentligDtoSelectors.AFPStatGjeldendeSelectors.er70ProsentRegelAvkortet
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPOffentligDtoSelectors.AFPStatGjeldendeSelectors.erInntektsavkortet
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPOffentligDtoSelectors.TilleggspensjonAFPStatGjeldendeSelectors.erRedusert
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPOffentligDtoSelectors.afpStatGjeldende
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPOffentligDtoSelectors.beregnetPensjonPerManed
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPOffentligDtoSelectors.beregnetPensjonPerManedGjeldende
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPOffentligDtoSelectors.kravVirkDatoFom
import no.nav.pensjon.brev.alder.model.vedlegg.MaanedligPensjonFoerSkattAFPOffentligDtoSelectors.tilleggspensjonAFPStatGjeldende
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.text

@TemplateModelHelpers
val vedleggMaanedligPensjonFoerSkattAFPOffentlig =
    createAttachment<LangBokmalNynorsk, MaanedligPensjonFoerSkattAFPOffentligDto>(
        title = {
            text(
                bokmal { +"Dette er din månedlige pensjon før skatt" },
                nynorsk { +"Dette er den månadlege pensjonen din før skatt" },
            )
        },
    ) {
        val er70ProsentRegelAvkortet = afpStatGjeldende.er70ProsentRegelAvkortet
        val fullTrygdetid = beregnetPensjonPerManedGjeldende.fullTrygdetid
        val grunnbelop = beregnetPensjonPerManedGjeldende.grunnbelop
        var tilleggspensjon = beregnetPensjonPerManedGjeldende.tilleggspensjon
        val sartillegg = beregnetPensjonPerManedGjeldende.sartillegg

        includePhrase(
            TabellMaanedligPensjonAFPOffentlig(
                beregnetPensjonPerManedGjeldende,
            ),
        )

        title1 {
            text(
                bokmal { +"Slik er pensjonen din satt sammen" },
                nynorsk { +"Slik er pensjonen din sett saman" }
            )
        }

        showIf(afpStatGjeldende.erInntektsavkortet) {
            showIf(er70ProsentRegelAvkortet) {
                paragraph {
                    text(
                        bokmal {
                            +"Pensjonen er redusert fordi AFP ikke kan utgjøre mer enn 70 prosent av full AFP. Pensjonen reduseres også fordi du har en forventet årlig inntekt over toleransebeløpet. Nedenfor beskrives komponentene som inngår i AFP før reduksjonen."
                        },
                        nynorsk {
                            +"Pensjonen er redusert fordi AFP ikkje kan utgjere meir enn 70 prosent av full AFP. Pensjonen vert òg redusert fordi du har ei forventa årleg inntekt over toleransebeløpet. Nedanfor vert komponentane som inngår i AFP før reduksjonen forklara."
                        }
                    )
                }
            }.orShow {
                paragraph {
                    text(
                        bokmal {
                            +"Pensjonen din er redusert fordi du har en forventet årlig inntekt over toleransebeløpet. Nedenfor beskrives komponentene som inngår i AFP før reduksjonen."
                        },
                        nynorsk {
                            +"Pensjonen din er redusert fordi du har ei forventa årleg inntekt over toleransebeløpet. Nedanfor vert komponentane som inngår i AFP før reduksjonen forklara."
                        }
                    )
                }
            }
        }.orShow {
            showIf(er70ProsentRegelAvkortet) {
                paragraph {
                    text(
                        bokmal {
                            +"Pensjonen din er redusert fordi du har en forventet årlig inntekt over toleransebeløpet. Nedenfor beskrives komponentene som inngår i AFP før reduksjonen."
                        },
                        nynorsk {
                            +"Pensjonen din er redusert fordi du har ei forventa årleg inntekt over toleransebeløpet. Nedanfor vert komponentane som inngår i AFP før reduksjonen forklara."
                        }
                    )
                }
            }
        }

        showIf(fullTrygdetid) {
            paragraph {
                text(
                    bokmal { +"Grunnpensjon" },
                    nynorsk { +"Grunnpensjon" },
                    Element.OutlineContent.ParagraphContent.Text.FontType.BOLD,
                )

                text(
                    bokmal {
                        +" fastsettes med utgangspunkt i folketrygdens grunnbeløp, som for tiden er " + grunnbelop.format() +
                            ". Full grunnpensjon for enslige pensjonister utgjør 100 prosent av grunnbeløpet."
                    },
                    nynorsk {
                        +" blir fastsett med utgangspunkt i grunnbeløpet i folketrygda, som for tida er " + grunnbelop.format() +
                            ". Full grunnpensjon for einslege pensjonistar utgjer 100 prosent av grunnbeløpet."
                    },
                )
            }

            showIf(beregnetPensjonPerManedGjeldende.brukerErFlyktning) {
                paragraph {
                    text(
                        bokmal {
                            +"Som flyktning får du grunnpensjonen din beregnet som om du har full trygdetid i Norge. Vær oppmerksom på at alderspensjonen din vil bli omregnet etter faktisk trygdetid dersom du flytter til et land utenfor EØS-området."
                        },
                        nynorsk {
                            +"Som flyktning får du grunnpensjonen din rekna ut som om du har full trygdetid i Noreg. Ver merksam på at alderspensjonen din vil bli omrekna etter faktisk trygdetid dersom du flyttar til eit land utanfor EØS-området."
                        },
                    )
                }
            }
        }.orShow {
            paragraph {
                text(
                    bokmal { +"Grunnpensjon" },
                    nynorsk { +"Grunnpensjon" },
                    Element.OutlineContent.ParagraphContent.Text.FontType.BOLD,
                )

                text(
                    bokmal {
                        +" fastsettes med utgangspunkt i folketrygdens grunnbeløp, som for tiden er " + grunnbelop.format() +
                            ". Full grunnpensjon for enslige pensjonister utgjør 100 prosent av grunnbeløpet før reduksjon på grunn av trygdetid. Du får trygdetid for de årene du har bodd og/eller arbeidet i Norge etter fylte 16 år. Fordi du har mindre enn 40 års trygdetid, vil grunnpensjonen bli redusert tilsvarende."
                    },
                    nynorsk {
                        +" blir fastsett med utgangspunkt i grunnbeløpet i folketrygda, som for tida er " + grunnbelop.format() +
                            ".  Full grunnpensjon for einslege pensjonistar utgjer 100 prosent av grunnbeløpet før reduksjon på grunn av trygdetid. Du får trygdetid for dei åra du har budd og/eller arbeidd i Noreg etter fylte 16 år. Fordi du har mindre enn 40 års trygdetid, vil grunnpensjonen bli redusert tilsvarande."
                    },
                )
            }
        }

        ifNotNull(tilleggspensjon) {
            showIf(tilleggspensjonAFPStatGjeldende.erRedusert) {
                paragraph {
                    text(
                        bokmal { +"Tilleggspensjonen" },
                        nynorsk { +"Tilleggspensjonen" },
                        Element.OutlineContent.ParagraphContent.Text.FontType.BOLD,
                    )

                    text(
                        bokmal {
                            +" din avhenger av antall år med pensjonspoeng og størrelsen på pensjonspoengene. Du får pensjonspoeng for år med inntekt over folketrygdens grunnbeløp. Du får ikke full tilleggspensjon fordi du har opptjent pensjonspoeng i mindre enn 40 år."
                        },
                        nynorsk {
                            +" din er avhengig av kor mange år du har hatt med pensjonspoeng, og storleiken på pensjonspoenga. Du får pensjonspoeng for år med inntekt over grunnbeløpet i folketrygda. Du får ikkje full tilleggspensjon fordi du har tent opp pensjonspoeng i mindre enn 40 år."
                        },
                    )
                }
            }.orShow {
                paragraph {
                    text(
                        bokmal { +"Tilleggspensjonen" },
                        nynorsk { +"Tilleggspensjonen" },
                        Element.OutlineContent.ParagraphContent.Text.FontType.BOLD,
                    )

                    text(
                        bokmal {
                            +" din avhenger av antall år med pensjonspoeng og størrelsen på pensjonspoengene. Du får pensjonspoeng for år med inntekt over folketrygdens grunnbeløp."
                        },
                        nynorsk {
                            +" din er avhengig av kor mange år du har hatt med pensjonspoeng, og storleiken på pensjonspoenga. Du får pensjonspoeng for år med inntekt over grunnbeløpet i folketrygda."
                        },
                    )
                }
            }
        }

        ifNotNull(sartillegg) {
            paragraph {
                text(
                    bokmal { +"Særtillegget" },
                    nynorsk { +"Særtillegget" },
                    Element.OutlineContent.ParagraphContent.Text.FontType.BOLD,
                )

                text(
                    bokmal {
                        +" skal sikre et visst minstenivå på pensjonen. Du får særtillegg etter ordinær sats som utgjør 100 prosent av grunnbeløpet. Denne gis til enslige pensjonister som ikke har rett til tilleggspensjon eller som har en tilleggspensjon som er mindre enn særtillegget."
                    },
                    nynorsk {
                        +" skal sikre eit visst minstenivå på pensjonen. Du får særtillegg etter ordinær sats som utgjer 100 prosent av grunnbeløpet. Denne gis til einslege pensjonistar som ikkje har rett til tilleggspensjon eller som har ein tilleggspensjon som er mindre enn særtillegget."
                    },
                )
            }

            ifNotNull(tilleggspensjon) {
                showIf(fullTrygdetid) {
                    paragraph {
                        text(
                            bokmal {
                                +"Vi har avkortet særtillegget ditt mot tilleggspensjonen, slik at du får utbetalt differansen."
                            },
                            nynorsk {
                                +"Vi har avkorta særtillegget mot din tilleggspensjon, slik at du får utbetalt differansen."
                            },
                        )
                    }
                }.orShow {
                    paragraph {
                        text(
                            bokmal {
                                +"Vi har avkortet særtillegget ditt mot tilleggspensjonen, slik at du får utbetalt differansen. Det avkortes også mot trygdetid på samme måte som for grunnpensjonen."
                            },
                            nynorsk {
                                +"]Vi har avkorta særtillegget mot din tilleggspensjon, slik at du får utbetalt differansen. Det blir også avkorta mot trygdetid på same måte som for grunnpensjonen."
                            },
                        )
                    }
                }
            }
        }

        ifNotNull(beregnetPensjonPerManedGjeldende.afpTillegg) {
            paragraph {
                text(
                    bokmal { +"AFP-tillegg" },
                    nynorsk { +"AFP-tillegg" },
                    Element.OutlineContent.ParagraphContent.Text.FontType.BOLD,
                )

                text(
                    bokmal {
                        +" er et tillegg som gis til alle som mottar AFP, når dette er avtalt i tariffavtale."
                    },
                    nynorsk {
                        +" er eit tillegg som blir gitt til alle som mottek AFP, når dette er avtalt i tariffavtale."
                    },
                )
            }
        }

        ifNotNull(beregnetPensjonPerManedGjeldende.minstenivaIndividuell) {
            paragraph {
                text(
                    bokmal { +"Minstenivåtillegg individuelt" },
                    nynorsk { +"Minstenivåtillegg individuelt" },
                    Element.OutlineContent.ParagraphContent.Text.FontType.BOLD,
                )

                text(
                    bokmal {
                        +" er gitt fordi pensjonen ellers ville ha vært lavere enn minste pensjonsnivå."
                    },
                    nynorsk {
                        +" er gitt fordi pensjonen elles ville ha vore lågare enn minste pensjonsnivå."
                    },
                )
            }
        }

        title2 {
            text(
                bokmal { +"Regulering av AFP under utbetaling" },
                nynorsk { +"Regulering av AFP under utbetaling" },
            )
        }

        paragraph {
            text(
                bokmal {
                    +"Pensjonen, unntatt AFP-tillegget, reguleres årlig. Reguleringen skjer med virkning fra 1. mai og selve økningen blir vanligvis etterbetalt i juni. Du får informasjon om dette på utbetalingsmeldingen din. På nav.no kan du lese mer om hvordan pensjonene blir regulert."
                },
                nynorsk {
                    +"Pensjonen, bortsett frå AFP-tillegget, blir årleg regulert. Reguleringa skjer med verknad frå 1. mai, og sjølve auken blir vanlegvis etterbetalt i juni. Du får informasjon om dette på utbetalingsmeldinga di. På nav.no kan du lese meir om korleis pensjonane blir regulerte."
                },
            )
        }

        showIf(beregnetPensjonPerManed.antallBeregningsperioder.greaterThan(1)) {
            title1 {
                text(
                    bokmal { +"Oversikt over pensjonen fra " + kravVirkDatoFom.format() },
                    nynorsk { +"Oversikt over pensjonen frå " + kravVirkDatoFom.format() },
                )
            }

            paragraph {
                text(
                    bokmal {
                        +"Hvis det har vært endringer i noen av opplysningene som ligger til grunn for beregningen eller pensjonen har vært regulert, kan dette føre til en endring i hvor mye du får utbetalt. Nedenfor følger en oversikt over de månedlige pensjonsbeløpene dine."
                    },
                    nynorsk {
                        +"Dersom det har vore endringar i nokre av opplysningane som ligg til grunn for utrekninga eller pensjonen har vore regulert, kan det føre til ei endring i kor mykje du får utbetalt. Nedanfor fylgjer ei oversikt over dei månadlege pensjonsbeløpa dine."
                    },
                )
            }

            includePhrase(
                TabellDinMaanedligAFPOffentlig(
                    afpStatBeregning = beregnetPensjonPerManed,
                ),
            )
        }
    }
