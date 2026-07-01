package no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Table.ColumnAlignment
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.ParagraphOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.api.model.vedlegg.OpplysningerBruktIBeregningUTLegacyDto.Barnetillegg
import no.nav.pensjon.brev.ufore.api.model.vedlegg.OpplysningerBruktIBeregningUTLegacyDto.Person
import no.nav.pensjon.brev.ufore.api.model.vedlegg.OpplysningerBruktIBeregningUTLegacyDto.Visningsflagg
import no.nav.pensjon.brev.ufore.api.model.vedlegg.SivilstatusVisning
import no.nav.pensjon.brev.ufore.api.model.vedlegg.selectors.opplysningerBruktIBeregningUTLegacyDto.barnetillegg.*
import no.nav.pensjon.brev.ufore.api.model.vedlegg.selectors.opplysningerBruktIBeregningUTLegacyDto.person.*
import no.nav.pensjon.brev.ufore.api.model.vedlegg.selectors.opplysningerBruktIBeregningUTLegacyDto.visningsflagg.*

data class TBU052V_TBU073V_SlikBeregnerViStoerrelsenPaaBarnetilleggetDitt(
    val flagg: Expression<Visningsflagg>,
    val barnetilleggFelles: Expression<Barnetillegg?>,
    val barnetilleggSaerkull: Expression<Barnetillegg?>,
    val person: Expression<Person>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        showIf(flagg.tbu069v) {
            title1 {
                text(
                    bokmal { + "Slik beregner vi størrelsen på barnetillegget" },
                    nynorsk { + "Slik bereknar vi storleiken på barnetillegget" },
                )
            }
            paragraph {
                text(
                    bokmal { + "Størrelsen på barnetillegget er avhengig av samlet inntekt. " },
                    nynorsk { + "Storleiken på barnetillegget er avhengig av samla inntekt." },
                )
            }
            paragraph {
                text(
                    bokmal { + "Barnetillegget kan bli redusert ut fra:" },
                    nynorsk { + "Barnetillegget kan bli redusert ut frå:" },
                )
                list {
                    item { text(bokmal { + "uføretrygd" }, nynorsk { + "uføretrygd" }) }
                    item { text(bokmal { + "arbeidsinntekt" }, nynorsk { + "arbeidsinntekt" }) }
                    item { text(bokmal { + "næringsinntekt" }, nynorsk { + "næringsinntekt" }) }
                    item { text(bokmal { + "inntekt fra utlandet" }, nynorsk { + "inntekt frå utlandet" }) }
                    item { text(bokmal { + "ytelser/pensjon fra Norge" }, nynorsk { + "ytingar/pensjon frå Noreg" }) }
                    item { text(bokmal { + "pensjon fra utlandet" }, nynorsk { + "pensjon frå utlandet" }) }
                }
            }

            showIf(flagg.barnetilleggFellesInnvilget and not(flagg.barnetilleggSaerkullInnvilget)) {
                paragraph {
                    text(
                        bokmal { + "Vi fastsetter størrelsen på barnetillegget ut fra den samlede inntekten til begge foreldrene." },
                        nynorsk { + "Vi fastset storleiken på barnetillegget ut frå den samla inntekta til begge foreldra." },
                    )
                }
                paragraph {
                    text(
                        bokmal { + "Barnetillegget blir redusert dersom den samlede inntekten er høyere enn fribeløpet. Fribeløpet for et barn er 4,6 ganger folketrygdens grunnbeløp og det øker med 40 prosent av folketrygdens grunnbeløp for hvert ekstra barn. " },
                        nynorsk { + "Barnetillegget blir redusert dersom den samla inntekta er høgare enn fribeløpet. Fribeløpet for eit barn er 4,6 gonger grunnbeløpet i folketrygda, og det aukar med 40 prosent av grunnbeløpet i folketrygda for kvart ekstra barn. " },
                    )
                    showIf(flagg.visBarnetilleggRedusertKortTrygdetid) {
                        text(
                            bokmal { + "Siden trygdetiden din er kortere enn 40 år, blir fribeløpet redusert ut fra den trygdetiden du har." },
                            nynorsk { + "Sidan trygdetida di er kortare enn 40 år, blir fribeløpet redusert ut frå den trygdetida du har." },
                        )
                    }
                }
                paragraph {
                    text(
                        bokmal { + "Dersom begge foreldrene mottar uføretrygd blir barnetillegget gitt til den som har rett til det høyeste tillegget. " },
                        nynorsk { + "Dersom begge foreldra får uføretrygd, blir barnetillegget gitt til den som har rett til det høgaste tillegget. " },
                    )
                }
            }

            showIf(not(flagg.barnetilleggFellesInnvilget) and flagg.barnetilleggSaerkullInnvilget) {
                paragraph {
                    text(
                        bokmal { + "Vi fastsetter størrelsen på barnetillegget ut fra inntekten din. Inntekt til en ektefelle/partner/samboer som ikke er forelder til barnet, har ikke betydning for størrelsen på barnetillegget. " },
                        nynorsk { + "Vi fastset storleiken på barnetillegget ut frå inntekta di. Inntekt til ein ektefelle/partnar/sambuar som ikkje er forelder til barnet, har ikkje betydning for storleiken på barnetillegget." },
                    )
                }
                paragraph {
                    text(
                        bokmal { + "Barnetillegget blir redusert dersom den samlede inntekten din er høyere enn fribeløpet. Fribeløpet for et barn er 3,1 ganger folketrygdens grunnbeløp og det øker med 40 prosent av folketrygdens grunnbeløp for hvert ekstra barn. " },
                        nynorsk { + "Barnetillegget blir redusert dersom den samla inntekta di er høgare enn fribeløpet. Fribeløpet for eit barn er 3,1 gonger grunnbeløpet i folketrygda, og det aukar med 40 prosent av grunnbeløpet i folketrygda for kvart ekstra barn." },
                    )
                    showIf(flagg.visBarnetilleggRedusertKortTrygdetid) {
                        text(
                            bokmal { + "Siden trygdetiden din er kortere enn 40 år, blir fribeløpet redusert ut fra den trygdetiden du har." },
                            nynorsk { + "Sidan trygdetida di er kortare enn 40 år, blir fribeløpet redusert ut frå den trygdetida du har." },
                        )
                    }
                }
                paragraph {
                    text(
                        bokmal { + "Dersom begge foreldrene mottar uføretrygd blir barnetillegget gitt til den som har den daglige omsorgen for barnet. Har foreldrene delt omsorg for barnet, blir barnetillegget gitt til den forelderen som bor på samme folkeregistrerte adresse som barnet. " },
                        nynorsk { + "Dersom begge foreldra får uføretrygd, blir barnetillegget gitt til den som har den daglege omsorga for barnet. Har foreldra delt omsorg for barnet, blir barnetillegget gitt til den forelderen som bur på same folkeregistrerte adresse som barnet." },
                    )
                }
            }

            showIf(flagg.barnetilleggFellesInnvilget and flagg.barnetilleggSaerkullInnvilget) {
                ifNotNull(barnetilleggFelles) { felles ->
                    paragraph {
                        text(bokmal { + "Vi fastsetter størrelsen på barnetillegget ut fra inntekten til deg og din " }, nynorsk { + "Vi fastset storleiken på barnetillegget ut frå inntekta til deg og " })
                        partnerSubstantiv(person)
                        text(bokmal { + " for " }, nynorsk { + " din for " })
                        barnetBarna(felles)
                        text(
                            bokmal { + " som bor med begge sine foreldre. Barnetillegget blir redusert dersom den samlede inntekten er høyere enn fribeløpet. Fribeløpet for et barn som bor med begge foreldrene er 4,6 ganger folketrygdens grunnbeløp, og øker med 40 prosent av folketrygdens grunnbeløp for hvert ekstra barn. " },
                            nynorsk { + " som bur med begge foreldra sine. Barnetillegget blir redusert dersom den samla inntekta er høgare enn fribeløpet. Fribeløpet for eit barn som bur med begge foreldra, er 4,6 gonger grunnbeløpet i folketrygda, og aukar med 40 prosent av grunnbeløpet i folketrygda for kvart ekstra barn. " },
                        )
                    }
                }
                ifNotNull(barnetilleggSaerkull) { saerkull ->
                    paragraph {
                        text(bokmal { + "For " }, nynorsk { + "For " })
                        barnetBarna(saerkull)
                        text(
                            bokmal { + " som ikke bor sammen med begge foreldre, fastsetter vi størrelsen på barnetillegget ut fra inntekten din. Inntekt til en ektefelle/partner/samboer som ikke er forelder til barnet, har ikke betydning for størrelsen på barnetillegget. Barnetillegget blir redusert dersom den samlede inntekten din er høyere enn fribeløpet. Fribeløpet for et barn som ikke bor sammen med begge foreldrene er 3,1 ganger folketrygdens grunnbeløp, og øker med 40 prosent av folketrygdens grunnbeløp for hvert ekstra barn. " },
                            nynorsk { + " som ikkje bur saman med begge foreldra, fastset vi storleiken på barnetillegget ut frå inntekta di. Inntekt til ein ektefelle/partnar/sambuar som ikkje er forelder til barnet, har ikkje betydning for storleiken på barnetillegget. Barnetillegget blir redusert dersom den samla inntekta di er høgare enn fribeløpet. Fribeløpet for eit barn som ikkje bur saman med begge foreldra, er 3,1 gonger grunnbeløpet i folketrygda, og aukar med 40 prosent av grunnbeløpet i folketrygda for kvart ekstra barn. " },
                        )
                    }
                }
            }

            showIf(flagg.visBarnetilleggBeggeTyperKortTrygdetidEtter2016) {
                paragraph {
                    text(
                        bokmal { + "Siden trygdetiden din er kortere enn 40 år, blir fribeløpene redusert ut fra den trygdetiden du har." },
                        nynorsk { + "Sidan trygdetida di er kortare enn 40 år, blir fribeløpa reduserte ut frå den trygdetida du har." },
                    )
                }
            }

            showIf(flagg.tbu605) {
                paragraph {
                    showIf(flagg.tbu605vEllerTilDin) {
                        text(bokmal { + "Har det vært en endring i inntekten din" }, nynorsk { + "Har det vore ei endring i inntekta " })
                    }
                    showIf(flagg.visBarnetilleggTbu605EllerTilDinFellesMedPartner) {
                        text(bokmal { + " eller til din " }, nynorsk { + "til deg eller " })
                        partnerSubstantiv(person)
                        text(bokmal { + "," }, nynorsk { + " " })
                    }
                    showIf(flagg.tbu605vEllerTilDin and not(flagg.visBarnetilleggTbu605EllerTilDinFellesMedPartner)) {
                        text(bokmal { + "" }, nynorsk { + "di," })
                    }
                    showIf(flagg.visBarnetilleggEndretInntekt) {
                        text(bokmal { + "Når inntekten din " }, nynorsk { + "Når inntekta di " })
                    }
                    showIf(flagg.visBarnetilleggEndretInntektFellesMedPartner) {
                        text(bokmal { + "eller til din " }, nynorsk { + "eller til " })
                        partnerSubstantiv(person)
                        text(bokmal { + " " }, nynorsk { + " din " })
                    }
                    showIf(flagg.visBarnetilleggEndretInntekt) {
                        text(bokmal { + "endrer seg," }, nynorsk { + "endrar seg," })
                    }
                    text(
                        bokmal { + " blir reduksjonen av barnetillegget vurdert på nytt. 50 prosent av den inntekten som overstiger fribeløpet " },
                        nynorsk { + " blir reduksjonen av barnetillegget vurdert på nytt. 50 prosent av inntekta som overstig fribeløpet " },
                    )
                    showIf(flagg.visBarnetilleggFribeloepEllerInntektPeriodisert) {
                        text(
                            bokmal { + "blir omregnet til et årlig beløp som tilsvarer " },
                            nynorsk { + "blir rekna om til et årleg beløp som svarer til " },
                        )
                    }
                    showIf(flagg.visBarnetilleggIngenPeriodisering) {
                        text(bokmal { + "er " }, nynorsk { + "er " })
                    }
                    avkortingsbeloepForInnvilgetBarnetillegg(barnetilleggFelles, barnetilleggSaerkull, flagg)
                    text(bokmal { + " kroner. " }, nynorsk { + " kroner. " })
                    showIf(flagg.visBarnetilleggIngenJustering) {
                        text(
                            bokmal { + "Dette beløpet bruker vi til å redusere barnetillegget ditt for hele året. " },
                            nynorsk { + "Dette beløpet bruker vi til å redusere barnetillegget ditt for heile året. " },
                        )
                    }
                }
            }

            showIf(flagg.tbu605 and flagg.visBarnetilleggHarJustering) {
                paragraph {
                    text(
                        bokmal { + "Vi tar hensyn til hvordan barnetillegget eventuelt har vært redusert tidligere, og vi har derfor " },
                        nynorsk { + "Vi tek omsyn til korleis eit barnetillegg eventuelt har vore redusert tidlegare, og har derfor  " },
                    )
                    showIf(flagg.visBarnetilleggJusteringPositiv) { text(bokmal { + "lagt til" }, nynorsk { + "lagt til" }) }
                    showIf(flagg.visBarnetilleggJusteringNegativ) { text(bokmal { + "redusert" }, nynorsk { + "trekt frå" }) }
                    text(bokmal { + " " }, nynorsk { + " " })
                    justeringsbeloepForInnvilgetBarnetillegg(barnetilleggFelles, barnetilleggSaerkull, flagg)
                    text(
                        bokmal { + " i beløpet vi reduserer barnetillegget med for resten av året. " },
                        nynorsk { + " i beløpet vi reduserer barnetillegget med for resten av året. " },
                    )
                }
            }

            showIf(flagg.tbu613v and flagg.tbu613v1_3) {
                paragraph {
                    showIf(flagg.visBarnetilleggIkkeEndretInntekt) {
                        text(bokmal { + "Har det vært en endring i inntekten din" }, nynorsk { + "Har det vore ei endring i inntekta " })
                    }
                    showIf(flagg.visBarnetilleggIkkeEndretInntektMedPartner) {
                        text(bokmal { + " eller til din " }, nynorsk { + "til deg eller " })
                        partnerSubstantiv(person)
                        text(bokmal { + "," }, nynorsk { + " din," })
                    }
                    showIf(flagg.visBarnetilleggIkkeEndretInntekt and not(flagg.visBarnetilleggIkkeEndretInntektMedPartner)) {
                        text(bokmal { + "" }, nynorsk { + "di," })
                    }
                    showIf(flagg.visBarnetilleggEndretInntekt) {
                        text(bokmal { + "Når inntekten din " }, nynorsk { + "Når inntekta di " })
                    }
                    showIf(flagg.visBarnetilleggEndretInntektMedPartner) {
                        text(bokmal { + "eller til din " }, nynorsk { + "eller til " })
                        partnerSubstantiv(person)
                        text(bokmal { + " " }, nynorsk { + " din " })
                    }
                    showIf(flagg.visBarnetilleggEndretInntekt) {
                        text(bokmal { + "endrer seg," }, nynorsk { + "endrar seg," })
                    }
                    text(
                        bokmal { + " blir reduksjonen av barnetilleggene vurdert på nytt. " },
                        nynorsk { + " blir reduksjonen av barnetilleggene vurdert på nytt." },
                    )
                }
                ifNotNull(barnetilleggFelles) { felles ->
                    paragraph {
                        text(bokmal { + "50 prosent av den inntekten som overstiger fribeløpet for " }, nynorsk { + "50 prosent av inntekta som overstig fribeløpet for " })
                        barnetBarna(felles)
                        text(bokmal { + " som bor med begge sine foreldre " }, nynorsk { + " som bur med begge foreldra " })
                        showIf(felles.fribeloepErPeriodisert) {
                            text(
                                bokmal { + "blir omregnet til et årlig beløp som tilsvarer " },
                                nynorsk { + "sine blir rekna om til et årleg beløp som svarer til " },
                            )
                        }.orShow {
                            text(bokmal { + "er " }, nynorsk { + "er " })
                        }
                        ifNotNull(felles.avkortingsbeloepPerAar) { avkorting ->
                            text(bokmal { + avkorting.format() + ". " }, nynorsk { + avkorting.format() + ". " })
                        }
                        showIf(flagg.visBarnetilleggSaerkullVisInntektstak or flagg.visBarnetilleggSaerkullNettoNull) {
                            text(
                                bokmal { + "Dette beløpet bruker vi til å redusere dette barnetillegget for hele året." },
                                nynorsk { + "Dette beløpet bruker vi til å redusere dette barnetillegget for hele året." },
                            )
                        }
                    }
                    justeringParagrafForBarnetillegg(
                        barn = felles,
                        vis = flagg.visBarnetilleggFellesHarJustering,
                        positiv = flagg.visBarnetilleggFellesJusteringPositiv,
                    )
                }
            }

            showIf(flagg.tbu613v and flagg.tbu613v4_5) {
                ifNotNull(barnetilleggSaerkull) { saerkull ->
                    paragraph {
                        text(bokmal { + "For " }, nynorsk { + "For " })
                        barnetBarna(saerkull)
                        text(bokmal { + " som ikke bor sammen med begge foreldrene " }, nynorsk { + " som ikkje bur med begge foreldra " })
                        showIf(saerkull.fribeloepErPeriodisert) {
                            text(
                                bokmal { + "blir 50 prosent av den inntekten som overstiger fribeløpet omregnet til et årlig beløp som tilsvarer " },
                                nynorsk { + "blir 50 prosent av den inntekta som overstig fribeløpet regna om til et årleg beløp som svarer til " },
                            )
                        }.orShow {
                            text(
                                bokmal { + "er 50 prosent av den inntekten som overstiger fribeløpet " },
                                nynorsk { + "er 50 prosent av den inntekta som overstig fribeløpet " },
                            )
                        }
                        ifNotNull(saerkull.avkortingsbeloepPerAar) { avkorting ->
                            text(bokmal { + avkorting.format() + ". " }, nynorsk { + avkorting.format() + ". " })
                        }
                        showIf(flagg.visBarnetilleggFellesVisInntektstak or flagg.visBarnetilleggFellesNettoNull) {
                            text(
                                bokmal { + "Dette beløpet bruker vi til å redusere dette barnetillegget for hele året. " },
                                nynorsk { + "Dette beløpet bruker vi til å redusere dette barnetillegget for heile året. " },
                            )
                        }
                    }
                    justeringParagrafForBarnetillegg(
                        barn = saerkull,
                        vis = flagg.visBarnetilleggSaerkullHarJustering,
                        positiv = flagg.visBarnetilleggSaerkullJusteringPositiv,
                    )
                }
            }

            ifNotNull(barnetilleggFelles) { felles ->
                showIf(flagg.tbu606v611v and flagg.tbu606v608v) {
                    barnetilleggTabell(
                        barn = felles,
                        tittelBokmal = "Reduksjon av barnetillegg for fellesbarn før skatt ",
                        tittelNynorsk = "Reduksjon av barnetillegg for fellesbarn før skatt ",
                        visReduksjonsrader = flagg.visBarnetilleggFellesVisReduksjonsrader,
                        visAvkortingsbeloep = flagg.visBarnetilleggFellesVisAvkortingsbeloep,
                        visJustering = flagg.visBarnetilleggFellesHarJustering,
                        justeringPositiv = flagg.visBarnetilleggFellesJusteringPositiv,
                        visInntektstak = flagg.visBarnetilleggFellesVisInntektstak,
                    )
                }
                showIf(flagg.visBarnetilleggFellesNettoPositiv) {
                    paragraph {
                        text(
                            bokmal { + "Du vil få utbetalt " + felles.netto.format() + " i måneden før skatt i barnetillegg" },
                            nynorsk { + "Du vil få utbetalt " + felles.netto.format() + " i månaden før skatt i barnetillegg" },
                        )
                        showIf(flagg.barnetilleggFellesInnvilget and flagg.barnetilleggSaerkullInnvilget and flagg.etteroppgjoerBtUtbetalt) {
                            text(bokmal { + " for " }, nynorsk { + " for " })
                            barnetBarna(felles)
                            text(
                                bokmal { + " som bor med begge sine foreldre" },
                                nynorsk { + " som bur saman med begge foreldra sine" },
                            )
                        }
                        text(bokmal { + ". " }, nynorsk { + "." })
                    }
                }
                showIf(flagg.visBarnetilleggFellesNettoNull) {
                    paragraph {
                        showIf(flagg.tbu608FaarIkke) {
                            text(bokmal { + "Du får ikke utbetalt barnetillegget " }, nynorsk { + "Du får ikkje utbetalt barnetillegget " })
                        }
                        showIf(flagg.tbu608FaarIkke and flagg.barnetilleggFellesInnvilget and flagg.barnetilleggSaerkullInnvilget) {
                            text(bokmal { + "for " }, nynorsk { + "for " })
                            barnetBarna(felles)
                            text(
                                bokmal { + " som bor med begge sine foreldre " },
                                nynorsk { + " som bur saman med begge foreldra sine " },
                            )
                        }
                        showIf(flagg.tbu608FaarIkke) {
                            text(
                                bokmal { + "fordi samlet inntekt er over grensen for å få utbetalt barnetillegg. " },
                                nynorsk { + "fordi samla inntekt er over grensa for å få utbetalt barnetillegg. " },
                            )
                        }
                        showIf(flagg.visBarnetilleggFellesNettoNullMedJustering) {
                            text(
                                bokmal { + "Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året. " },
                                nynorsk { + "Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året. " },
                            )
                        }
                    }
                }
            }

            showIf(flagg.tbu606v611v and flagg.tbu606v608v) {
                periodiseringParagraf()
            }

            ifNotNull(barnetilleggSaerkull) { saerkull ->
                showIf(flagg.tbu606v611v and flagg.tbu609v611v) {
                    barnetilleggTabell(
                        barn = saerkull,
                        tittelBokmal = "Reduksjon av barnetillegg for særkullsbarn før skatt ",
                        tittelNynorsk = "Reduksjon av barnetillegg for særkullsbarn før skatt ",
                        visReduksjonsrader = flagg.visBarnetilleggSaerkullVisReduksjonsrader,
                        visAvkortingsbeloep = flagg.visBarnetilleggSaerkullVisAvkortingsbeloep,
                        visJustering = flagg.visBarnetilleggSaerkullHarJustering,
                        justeringPositiv = flagg.visBarnetilleggSaerkullJusteringPositiv,
                        visInntektstak = flagg.visBarnetilleggSaerkullVisInntektstak,
                    )
                }
                showIf(flagg.visBarnetilleggSaerkullNettoPositiv) {
                    paragraph {
                        text(
                            bokmal { + "Du vil få utbetalt " + saerkull.netto.format() + " i måneden før skatt i barnetillegg" },
                            nynorsk { + "Du vil få utbetalt " + saerkull.netto.format() + " i månaden før skatt i barnetillegg" },
                        )
                        showIf(flagg.barnetilleggFellesInnvilget and flagg.barnetilleggSaerkullInnvilget and flagg.etteroppgjoerBtUtbetalt) {
                            text(bokmal { + " for " }, nynorsk { + " for " })
                            barnetBarna(saerkull)
                            text(
                                bokmal { + " som ikke bor med begge sine foreldre" },
                                nynorsk { + " som ikkje bur saman med begge foreldra" },
                            )
                        }
                        text(bokmal { + ". " }, nynorsk { + ". " })
                    }
                }
                showIf(flagg.visBarnetilleggSaerkullNettoNull) {
                    paragraph {
                        showIf(flagg.tbu611FaarIkke) {
                            text(bokmal { + "Du får ikke utbetalt barnetillegget " }, nynorsk { + "Du får ikkje utbetalt barnetillegget " })
                        }
                        showIf(flagg.tbu611FaarIkke and flagg.barnetilleggFellesInnvilget and flagg.barnetilleggSaerkullInnvilget) {
                            text(bokmal { + "for " }, nynorsk { + "for " })
                            barnetBarna(saerkull)
                            text(
                                bokmal { + " som ikke bor med begge sine foreldre " },
                                nynorsk { + " som ikkje bur saman med begge foreldra " },
                            )
                        }
                        showIf(flagg.tbu611FaarIkke) {
                            text(
                                bokmal { + "fordi samlet inntekt er over grensen for å få utbetalt barnetillegg. " },
                                nynorsk { + "fordi samla inntekt er over grensa for å få utbetalt barnetillegg. " },
                            )
                        }
                        showIf(flagg.visBarnetilleggSaerkullNettoNullMedJustering) {
                            text(
                                bokmal { + "Du har allerede fått utbetalt det du har rett til i år, og får derfor ikke utbetalt barnetillegg for resten av året." },
                                nynorsk { + "Du har allereie fått utbetalt det du har rett til i år, og får derfor ikkje utbetalt barnetillegg for resten av året. " },
                            )
                        }
                    }
                }
            }

            showIf(flagg.tbu606v611v and flagg.tbu609v611v) {
                periodiseringParagraf()
            }
        }
    }

    private fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.barnetilleggTabell(
        barn: Expression<Barnetillegg>,
        tittelBokmal: String,
        tittelNynorsk: String,
        visReduksjonsrader: Expression<Boolean>,
        visAvkortingsbeloep: Expression<Boolean>,
        visJustering: Expression<Boolean>,
        justeringPositiv: Expression<Boolean>,
        visInntektstak: Expression<Boolean>,
    ) {
        paragraph {
            table(
                header = {
                    column(columnSpan = 4) {
                        text(bokmal { + tittelBokmal }, nynorsk { + tittelNynorsk })
                        showIf(flagg.visBarnetilleggVirkningFomIkkeFoersteJanuar) {
                            text(bokmal { + "i år" }, nynorsk { + "i år" })
                        }
                        showIf(flagg.visBarnetilleggVirkningFomFoersteJanuar) {
                            text(bokmal { + "for neste år" }, nynorsk { + "for neste år" })
                        }
                    }
                    column(columnSpan = 1, ColumnAlignment.RIGHT) {}
                }
            ) {
                row {
                    cell {
                        text(
                            bokmal { + "Årlig barnetillegg før reduksjon ut fra inntekt" },
                            nynorsk { + "Årleg barnetillegg før reduksjon ut frå inntekt" },
                            FontType.BOLD,
                        )
                    }
                    cell {
                        ifNotNull(barn.bruttoPerAar) { brutto ->
                            text(bokmal { + brutto.format(false) + " kr" }, nynorsk { + brutto.format(false) + " kr" }, FontType.BOLD)
                        }
                    }
                }
                row {
                    cell {
                        ifNotNull(barn.inntektBruktIAvkortning) { inntekt ->
                            text(
                                bokmal { + "Samlet inntekt brukt i fastsettelse av barnetillegget er " + inntekt.format(false) + " kr" },
                                nynorsk { + "Samla inntekt brukt i fastsetting av barnetillegget er " + inntekt.format(false) + " kr" },
                            )
                        }
                    }
                    cell {}
                }
                showIf(visReduksjonsrader) {
                    row {
                        cell {
                            ifNotNull(barn.fribeloep) { fribeloep ->
                                text(
                                    bokmal { + "Fribeløp brukt i fastsettelsen av barnetillegget er " + fribeloep.format(false) + " kr" },
                                    nynorsk { + "Fribeløp brukt i fastsetting av barnetillegget er " + fribeloep.format(false) + " kr" },
                                )
                            }
                        }
                        cell {}
                    }
                    row {
                        cell {
                            ifNotNull(barn.inntektBruktIAvkortningMinusFribeloep) { inntektOver ->
                                text(
                                    bokmal { + "Inntekt over fribeløpet er " + inntektOver.format(false) + " kr" },
                                    nynorsk { + "Inntekt over fribeløpet er " + inntektOver.format(false) + " kr" },
                                )
                            }
                        }
                        cell {}
                    }
                }
                showIf(visAvkortingsbeloep) {
                    row {
                        cell {
                            text(
                                bokmal { + "- 50 prosent av inntekt som overstiger fribeløpet" },
                                nynorsk { + "- 50 prosent av inntekta som overstig fribeløpet" },
                                FontType.BOLD,
                            )
                            showIf(barn.fribeloepErPeriodisert) {
                                text(
                                    bokmal { + "(oppgitt som et årlig beløp)" },
                                    nynorsk { + "(oppgitt som eit årleg beløp)" },
                                    FontType.BOLD,
                                )
                            }
                        }
                        cell {
                            ifNotNull(barn.avkortingsbeloepPerAar) { avkorting ->
                                text(bokmal { + avkorting.format(false) + " kr" }, nynorsk { + avkorting.format(false) + " kr" }, FontType.BOLD)
                            }
                        }
                    }
                }
                showIf(visJustering) {
                    row {
                        cell {
                            showIf(justeringPositiv) {
                                text(bokmal { + "-" }, nynorsk { + "-" })
                            }.orShow {
                                text(bokmal { + "+" }, nynorsk { + "+" })
                            }
                            text(
                                bokmal { + " Beløp som er brukt for å justere reduksjonen av barnetillegget" },
                                nynorsk { + " Beløp som er brukt for å justere reduksjonen av barnetillegget" },
                            )
                        }
                        cell {
                            ifNotNull(barn.justeringsbeloepPerAarUtenMinus) { justering ->
                                text(bokmal { + justering.format(false) + " kr" }, nynorsk { + justering.format(false) + " kr" })
                            }
                        }
                    }
                }
                showIf(visReduksjonsrader) {
                    row {
                        cell {
                            text(
                                bokmal { + "= Årlig barnetillegg etter reduksjon ut fra inntekt" },
                                nynorsk { + "= Årleg barnetillegg etter reduksjon ut frå inntekt" },
                                FontType.BOLD,
                            )
                        }
                        cell {
                            ifNotNull(barn.nettoPerAar) { netto ->
                                text(bokmal { + netto.format(false) + " kr" }, nynorsk { + netto.format(false) + " kr" }, FontType.BOLD)
                            }
                        }
                    }
                    row {
                        cell { text(bokmal { + "Utbetaling av barnetillegg per måned " }, nynorsk { + "Utbetaling av barnetillegg per månad " }) }
                        cell { text(bokmal { + barn.netto.format(false) + " kr" }, nynorsk { + barn.netto.format(false) + " kr" }) }
                    }
                }
                showIf(visInntektstak) {
                    row {
                        cell {
                            text(
                                bokmal { + "Grensen for å få utbetalt barnetillegg" },
                                nynorsk { + "Grensa for å få utbetalt barnetillegg" },
                                FontType.BOLD,
                            )
                        }
                        cell {
                            ifNotNull(barn.inntektstak) { tak ->
                                text(bokmal { + tak.format(false) + " kr" }, nynorsk { + tak.format(false) + " kr" }, FontType.BOLD)
                            }
                        }
                    }
                }
            }
        }
    }
}

private fun ParagraphOnlyScope<LangBokmalNynorsk, Unit>.barnetBarna(barn: Expression<Barnetillegg>) {
    showIf(barn.antallBarn.greaterThan(1)) {
        text(bokmal { + "barna" }, nynorsk { + "barna" })
    }.orShow {
        text(bokmal { + "barnet" }, nynorsk { + "barnet" })
    }
}

private fun ParagraphOnlyScope<LangBokmalNynorsk, Unit>.partnerSubstantiv(person: Expression<Person>) {
    ifNotNull(person.sivilstatusVisning) { sivilstatus ->
        showIf(sivilstatus.equalTo(SivilstatusVisning.GIFT)) {
            text(bokmal { + "ektefelle" }, nynorsk { + "ektefellen" })
        }
        showIf(sivilstatus.equalTo(SivilstatusVisning.PARTNER)) {
            text(bokmal { + "partner" }, nynorsk { + "partnaren" })
        }
        showIf(sivilstatus.equalTo(SivilstatusVisning.SAMBOER_12_13) or sivilstatus.equalTo(SivilstatusVisning.SAMBOER_1_5)) {
            text(bokmal { + "samboer" }, nynorsk { + "sambuaren" })
        }
    }
}

private fun ParagraphOnlyScope<LangBokmalNynorsk, Unit>.avkortingsbeloepForInnvilgetBarnetillegg(
    barnetilleggFelles: Expression<Barnetillegg?>,
    barnetilleggSaerkull: Expression<Barnetillegg?>,
    flagg: Expression<Visningsflagg>,
) {
    showIf(flagg.barnetilleggFellesInnvilget) {
        ifNotNull(barnetilleggFelles) { felles ->
            ifNotNull(felles.avkortingsbeloepPerAar) { avkorting ->
                text(bokmal { + avkorting.format(false) }, nynorsk { + avkorting.format(false) })
            }
        }
    }
    showIf(flagg.barnetilleggSaerkullInnvilget and not(flagg.barnetilleggFellesInnvilget)) {
        ifNotNull(barnetilleggSaerkull) { saerkull ->
            ifNotNull(saerkull.avkortingsbeloepPerAar) { avkorting ->
                text(bokmal { + avkorting.format(false) }, nynorsk { + avkorting.format(false) })
            }
        }
    }
}

private fun ParagraphOnlyScope<LangBokmalNynorsk, Unit>.justeringsbeloepForInnvilgetBarnetillegg(
    barnetilleggFelles: Expression<Barnetillegg?>,
    barnetilleggSaerkull: Expression<Barnetillegg?>,
    flagg: Expression<Visningsflagg>,
) {
    showIf(flagg.barnetilleggFellesInnvilget) {
        ifNotNull(barnetilleggFelles) { felles ->
            ifNotNull(felles.justeringsbeloepPerAarUtenMinus) { justering ->
                text(bokmal { + justering.format() }, nynorsk { + justering.format() })
            }
        }
    }
    showIf(flagg.barnetilleggSaerkullInnvilget) {
        ifNotNull(barnetilleggSaerkull) { saerkull ->
            ifNotNull(saerkull.justeringsbeloepPerAarUtenMinus) { justering ->
                text(bokmal { + justering.format() }, nynorsk { + justering.format() })
            }
        }
    }
}

private fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.justeringParagrafForBarnetillegg(
    barn: Expression<Barnetillegg>,
    vis: Expression<Boolean>,
    positiv: Expression<Boolean>,
) {
    showIf(vis) {
        paragraph {
            text(
                bokmal { + "Vi tar hensyn til hvordan barnetillegget eventuelt har vært redusert tidligere, og vi har derfor " },
                nynorsk { + "Vi tek omsyn til korleis eit barnetillegg eventuelt har vore redusert tidlegare, og har derfor " },
            )
            showIf(positiv) {
                text(bokmal { + "lagt til" }, nynorsk { + "lagt til" })
            }.orShow {
                text(bokmal { + "trukket fra" }, nynorsk { + "trekt frå" })
            }
            ifNotNull(barn.justeringsbeloepPerAarUtenMinus) { justering ->
                text(
                    bokmal { + " " + justering.format() + " i beløpet vi reduserer barnetillegget med for resten av året." },
                    nynorsk { + " " + justering.format() + " i beløpet vi reduserer barnetillegget med for resten av året." },
                )
            }
        }
    }
}

private fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.periodiseringParagraf() {
    paragraph {
        text(
            bokmal {
                + "Hvis du har barnetillegg bare deler av året, er inntektene og fribeløpet for beregning av barnetillegg justert slik at det kun gjelder for den perioden du mottar barnetillegg. " +
                    "Får du barnetillegg for hele året, beregnes dette ut fra hele årets inntekter og fradrag."
            },
            nynorsk {
                + "Hvis du har barnetillegg berre delar av året, er inntektene og fribeløpet for berekning av barnetillegg justert slik at det kun gjeld for den perioden du mottar barnetillegg. " +
                    "Får du barnetillegg for heile året, bereknas dette ut fra heile årets inntekter og frådrag."
            },
        )
    }
}
