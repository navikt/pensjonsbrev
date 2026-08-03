package no.nav.pensjon.brev.alder.maler.vedlegg

import no.nav.pensjon.brev.alder.model.vedlegg.HvordanPensjonenErBeregnetAfpOffentligDto
import no.nav.pensjon.brev.alder.model.vedlegg.selectors.hvordanPensjonenErBeregnetAfpOffentligDto.ektefelletillegg.*
import no.nav.pensjon.brev.alder.model.vedlegg.selectors.hvordanPensjonenErBeregnetAfpOffentligDto.tilleggspensjon.*
import no.nav.pensjon.brev.alder.model.vedlegg.selectors.hvordanPensjonenErBeregnetAfpOffentligDto.*
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.lessThan
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.expression.notNull
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.text

/**
 * Vedlegg «Hvordan pensjonen er beregnet» for AFP i offentlig sektor.
 *
 * Konvertert fra Exstream-malen `PE_AF_hvordan_pensjonen_beregnes`. Vedlegget
 * brukes av flere AFP-brev (PE_AF_04_001 m.fl.).
 */
@TemplateModelHelpers
val vedleggHvordanPensjonenErBeregnetAfpOffentlig =
    createAttachment<LangBokmalNynorsk, HvordanPensjonenErBeregnetAfpOffentligDto>(
        title = {
            text(
                bokmal { +"Hvordan pensjonen er beregnet" },
                nynorsk { +"Korleis pensjonen er berekna" },
            )
        },
    ) {
        // Innledning om grunnbeløpet.
        paragraph {
            text(
                bokmal {
                    +"Folketrygdens grunnbeløp (G) har betydning for størrelsen på pensjonen fra folketrygden. " +
                        "Pensjonen reguleres i takt med økningen av G hvert år."
                },
                nynorsk {
                    +"Grunnbeløpet (G) i folketrygda er avgjerande for kor stor pensjonen frå folketrygda er. " +
                        "Pensjonen blir regulert i takt med auken av G kvart år."
                },
            )
        }
        paragraph {
            text(
                bokmal {
                    +"Både grunnpensjon, særtillegg og ektefelletillegg blir beregnet som prosentsatser av G. " +
                        "For tilleggspensjon legges G til grunn som en faktor for beregning av de årlige " +
                        "pensjonspoengene og ved beregning av selve pensjonen."
                },
                nynorsk {
                    +"Både grunnpensjon, særtillegg og forsørgingstillegg er berekna som prosentsatsar av G. " +
                        "For tilleggspensjon blir G lagd til grunn som ein faktor for berekning av dei årlege " +
                        "pensjonspoenga og ved berekning av sjølve pensjonen."
                },
            )
        }

        // Grunnpensjon.
        title1 {
            text(
                bokmal { +"Grunnpensjon" },
                nynorsk { +"Grunnpensjon" },
            )
        }
        paragraph {
            text(
                bokmal {
                    +"Grunnpensjonen beregnes uavhengig av tidligere arbeidsinntekt. For å få full grunnpensjon må " +
                        "trygdetiden tilsvare minst 40 år."
                },
                nynorsk {
                    +"Grunnpensjonen blir berekna uavhengig av tidlegare arbeidsinntekt. For å få full grunnpensjon " +
                        "må trygdetida tilsvare minst 40 år."
                },
            )
            showIf(brukerErFlyktning.not()) {
                text(
                    bokmal { +" Trygdetiden som er brukt i beregning er " + trygdetid.format() + " år." },
                    nynorsk { +" Trygdetida som er brukt i berekninga, er " + trygdetid.format() + " år." },
                )
            }
        }
        showIf(brukerErFlyktning) {
            paragraph {
                text(
                    bokmal {
                        +"Det er i beregningen tatt hensyn til registrert flyktningstatus etter folketrygdlovens " +
                            "paragraf 1-7. Så lenge du er bosatt i Norge blir grunnpensjonen ikke avkortet på grunn av " +
                            "manglende trygdetid."
                    },
                    nynorsk {
                        +"Det er i berekninga teke omsyn til registrert flyktningstatus etter folketrygdlova " +
                            "paragraf 1-7. Så lenge du er busett i Noreg, blir grunnpensjonen ikkje avkorta på grunn av " +
                            "manglande trygdetid."
                    },
                )
            }
        }
        showIf(not(ektefelleInntektOver2G or ektefelleMottarPensjon)) {
            paragraph {
                text(
                    bokmal { +"Full grunnpensjon tilsvarer 1 G." },
                    nynorsk { +"Full grunnpensjon tilsvarer 1 G." },
                )
            }
        }
        showIf(ektefelleInntektOver2G) {
            paragraph {
                text(
                    bokmal { +"Full grunnpensjon tilsvarer 90 prosent av 1 G fordi ektefellen din har inntekt over 2 G." },
                    nynorsk { +"Full grunnpensjon tilsvarer 90 prosent av 1 G fordi ektefellen din har inntekt over 2 G." },
                )
            }
        }
        showIf(ektefelleMottarPensjon) {
            paragraph {
                text(
                    bokmal {
                        +"Full grunnpensjon tilsvarer 90 prosent av 1 G fordi ektefellen din også har pensjon eller " +
                            "uføretrygd fra folketrygden eller AFP."
                    },
                    nynorsk {
                        +"Full grunnpensjon tilsvarer 90 prosent av 1 G fordi ektefellen din òg har pensjon frå " +
                            "folketrygda eller AFP."
                    },
                )
            }
        }
        showIf(ektefelleInntektOver2G or ektefelleMottarPensjon) {
            paragraph {
                text(
                    bokmal { +"Partnere og samboere er likestilt med ektefeller." },
                    nynorsk { +"Partnarar og sambuarar er likestilte med ektefellar." },
                )
            }
        }
        showIf(trygdetid.lessThan(40) and trygdetid.greaterThan(0)) {
            paragraph {
                text(
                    bokmal { +"Grunnpensjonen din reduseres til " + trygdetid.format() + "/40 av full grunnpensjon." },
                    nynorsk { +"Grunnpensjonen din reduseres til " + trygdetid.format() + "/40 av full grunnpensjon." },
                )
            }
        }

        // Tilleggspensjon.
        ifNotNull(tilleggspensjon) { tp ->
            title1 {
                text(
                    bokmal { +"Tilleggspensjon" },
                    nynorsk { +"Tilleggspensjon" },
                )
            }
            showIf(tp.poengaarUtenOkf92.notEqualTo(0) and tp.poengaarUtenOke91.notEqualTo(0)) {
                paragraph {
                    text(
                        bokmal {
                            +"Tilleggspensjonen beregnes på grunnlag av tidligere inntekt, uttrykt gjennom " +
                                "sluttpoengtallet, og en prosentandel av G. Prosentandelen er 45 for år med pensjonspoeng " +
                                "(poengår) til og med 1991 og 42 for poengår fra og med 1992."
                        },
                        nynorsk {
                            +"Tilleggspensjonen blir berekna ut frå tidlegare inntekt, uttrykt gjennom sluttpoengtalet, " +
                                "og ein prosentdel av G. Prosentdelen er 45 for år med pensjonspoeng (poengår) til og med " +
                                "1991, og 42 for poengår frå og med 1992."
                        },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Slik blir den månedlige tilleggspensjonen din beregnet:" },
                        nynorsk { +"Slik blir den månadlege tilleggspensjonen din berekna:" },
                    )
                    newline()
                    text(
                        bokmal {
                            +grunnbeloep.format(denominator = false) + " kr (G) × " +
                                tp.sluttpoengtallUtenOk.format() + " (sluttpoengtall) × 0,45 (pensjonsprosent) × " +
                                tp.poengaarUtenOke91.format() + " år (poengår til og med 1991) / (40 år × 12 måneder)"
                        },
                        nynorsk {
                            +grunnbeloep.format(denominator = false) + " kr (G) × " +
                                tp.sluttpoengtallUtenOk.format() + " (sluttpoengtal) × 0,45 (pensjonsprosent) × " +
                                tp.poengaarUtenOke91.format() + " år (poengår til og med 1991) / (40 år × 12 månader)"
                        },
                        BOLD,
                    )
                    newline()
                    text(
                        bokmal { +"+" },
                        nynorsk { +"+" },
                        BOLD,
                    )
                    newline()
                    text(
                        bokmal {
                            +grunnbeloep.format(denominator = false) + " kr (G) × " +
                                tp.sluttpoengtallUtenOk.format() + " (sluttpoengtall) × 0,42 (pensjonsprosent) × " +
                                tp.poengaarUtenOkf92.format() + " år (poengår fra og med 1992) / (40 år × 12 måneder)"
                        },
                        nynorsk {
                            +grunnbeloep.format(denominator = false) + " kr (G) × " +
                                tp.sluttpoengtallUtenOk.format() + " (sluttpoengtal) × 0,42 (pensjonsprosent) × " +
                                tp.poengaarUtenOkf92.format() + " år (poengår frå og med 1992) / (40 år × 12 månader)"
                        },
                        BOLD,
                    )
                }
            }
            showIf(tp.poengaarUtenOkf92.equalTo(0) and tp.poengaarUtenOke91.notEqualTo(0)) {
                paragraph {
                    text(
                        bokmal {
                            +"Tilleggspensjonen beregnes på grunnlag av tidligere inntekt uttrykt gjennom " +
                                "sluttpoengtallet, og en prosentandel på 42 av G for år med pensjonspoeng (poengår)."
                        },
                        nynorsk {
                            +"Tilleggspensjonen blir berekna ut frå tidlegare inntekt, uttrykt gjennom sluttpoengtalet, " +
                                "og ein prosentdel på 42 av G for år med pensjonspoeng (poengår)."
                        },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Slik blir den månedlige tilleggspensjonen din beregnet:" },
                        nynorsk { +"Slik blir den månadlege tilleggspensjonen din berekna:" },
                    )
                    newline()
                    text(
                        bokmal {
                            +grunnbeloep.format(denominator = false) + " kr (G) × " +
                                tp.sluttpoengtallUtenOk.format() + " (sluttpoengtall) × 0,42 (pensjonsprosent) × " +
                                tp.poengaarUtenOk.format() + " år (poengår) / (40 år × 12 måneder)"
                        },
                        nynorsk {
                            +grunnbeloep.format(denominator = false) + " kr (G) × " +
                                tp.sluttpoengtallUtenOk.format() + " (sluttpoengtal) × 0,42 (pensjonsprosent) × " +
                                tp.poengaarUtenOk.format() + " år (poengår) / (40 år × 12 månader)"
                        },
                        BOLD,
                    )
                }
            }
        }

        // Særtillegg.
        showIf(saertilleggInnvilget) {
            title1 {
                text(
                    bokmal { +"Særtillegg" },
                    nynorsk { +"Særtillegg" },
                )
            }
            paragraph {
                text(
                    bokmal {
                        +"Formålet med særtillegget er å sikre et visst minstenivå på pensjonen (minstepensjon). " +
                            "Størrelsen på særtillegget varierer ut fra om du bor sammen med ektefelle som har " +
                            "tilleggspensjon eller ikke. Partnere og samboere som har barn sammen eller tidligere " +
                            "vært gift med hverandre, jf. folketrygdlovens paragraf 1-5 likestilles ektefeller."
                    },
                    nynorsk {
                        +"Formålet med særtillegget er å sikre eit visst minstenivå på pensjonen (minstepensjon). " +
                            "Storleiken på særtillegget varierer ut frå om du bur saman med ektefelle som har " +
                            "tilleggspensjon, eller ikkje. Partnarar og sambuarar som har barn saman, eller som " +
                            "tidlegare har vore gifte med kvarandre, jf. folketrygdlova paragraf 1-5, blir likestilte " +
                            "med ektefellar."
                    },
                )
                showIf(tilleggspensjon.notNull()) {
                    text(
                        bokmal { +" Tilleggspensjonen går til fradrag i særtillegget, slik at du får utbetalt differansen." },
                        nynorsk { +" Tilleggspensjonen går til frådrag i særtillegget, slik at du får utbetalt differansen." },
                    )
                }
            }
            showIf(trygdetid.lessThan(40) and trygdetid.greaterThan(0)) {
                paragraph {
                    text(
                        bokmal { +"Særtillegget er beregnet etter den samme trygdetiden som grunnpensjonen." },
                        nynorsk { +"Særtillegget er berekna etter den same trygdetida som grunnpensjonen." },
                    )
                }
            }
        }

        // Ektefelletillegg.
        ifNotNull(ektefelletillegg) { et ->
            title1 {
                text(
                    bokmal { +"Ektefelletillegg" },
                    nynorsk { +"Ektefelletillegg" },
                )
            }
            showIf(trygdetid.equalTo(40)) {
                paragraph {
                    text(
                        bokmal { +"Fullt ektefelletillegg tilsvarer 50 prosent av folketrygdens grunnbeløp." },
                        nynorsk { +"Fullt ektefelletillegg tilsvarer 50 prosent av grunnbeløpet i folketrygda." },
                    )
                }
            }
            showIf(trygdetid.lessThan(40) and trygdetid.greaterThan(0)) {
                paragraph {
                    text(
                        bokmal {
                            +"Fullt ektefelletillegg tilsvarer 50 prosent av folketrygdens grunnbeløp hvis du har " +
                                "full trygdetid. Siden trygdetiden din er redusert, vil ektefelletillegget før " +
                                "inntektsavkorting reduseres med " + trygdetid.format() + "/40."
                        },
                        nynorsk {
                            +"Fullt ektefelletillegg tilsvarer 50 prosent av grunnbeløpet i folketrygda dersom du har " +
                                "full trygdetid. Sidan trygdetida di er redusert, blir ektefelletillegget før " +
                                "inntektsavkorting redusert med " + trygdetid.format() + "/40."
                        },
                    )
                }
            }
            paragraph {
                text(
                    bokmal {
                        +"Tillegget blir redusert med 50 prosent av den samlede inntekten din som overstiger et " +
                            "fribeløp. Fribeløpet er satt til " + et.fribeloep.format() + "."
                    },
                    nynorsk {
                        +"Tillegget blir redusert med 50 prosent av den samla inntekta di som overstig eit " +
                            "fribeløp. Fribeløpet er sett til " + et.fribeloep.format() + "."
                    },
                )
            }
            paragraph {
                text(
                    bokmal { +"Ektefelletillegget blir beregnet på følgende måte:" },
                    nynorsk { +"Ektefelletillegget blir berekna slik:" },
                )
                newline()
                showIf(trygdetid.lessThan(40) and trygdetid.notEqualTo(0)) {
                    text(
                        bokmal {
                            +"Fullt ektefelletillegg: (50 prosent av grunnbeløpet) × " + trygdetid.format() + "/40"
                        },
                        nynorsk {
                            +"Fullt ektefelletillegg: (50 prosent av grunnbeløpet) × " + trygdetid.format() + "/40"
                        },
                    )
                }.orShow {
                    text(
                        bokmal { +"Fullt ektefelletillegg: (50 prosent av grunnbeløpet)" },
                        nynorsk { +"Fullt ektefelletillegg: (50 prosent av grunnbeløpet)" },
                    )
                }
                newline()
                text(
                    bokmal { +"÷ Beregnet årlig fradrag for inntekt: (samlet årlig inntekt ÷ fribeløp) × 0,5" },
                    nynorsk { +"÷ Berekna årleg frådrag for inntekt: (samla årleg inntekt ÷ fribeløp) × 0,5" },
                )
                newline()
                text(
                    bokmal { +"= Ektefelletillegg per år (før skatt) / 12 mnd" },
                    nynorsk { +"= Ektefelletillegg per år (før skatt) / 12 månader" },
                )
            }
            paragraph {
                text(
                    bokmal { +"= Ektefelletillegg per måned (før skatt)" },
                    nynorsk { +"= Ektefelletillegg per månad (før skatt)" },
                    BOLD,
                )
            }

            // Fritekst: inntekter som ligger til grunn for inntektsprøving.
            // TODO vi har ikke støtte for redigering i vedlegg!
/*
            paragraph {
                text(
                    bokmal {
                        +"I tillegg til pensjon fra folketrygden har vi lagt følgende inntekt til grunn for " +
                            "inntektsprøving av forsørgingstillegg:"
                    },
                    nynorsk {
                        +"I tillegg til pensjon frå folketrygda har vi lagt følgjande inntekt til grunn for " +
                            "inntektsprøving av forsørgingstillegg:"
                    },
                )
                list {
                    item {
                        text(
                            bokmal { +"Pensjon/inntekt fra andre: … kroner per år." },
                            nynorsk { +"Pensjon/inntekt frå andre: … kroner per år." },
                        )
                    }
                    item {
                        text(
                            bokmal { +"Arbeidsinntekt/pensjonsgivende inntekt: … kroner per år." },
                            nynorsk { +"Arbeidsinntekt / pensjonsgivande inntekt: … kroner per år." },
                        )
                    }
                }
            }
*/
        }
    }
