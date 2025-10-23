package no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.ParagraphOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.etterlatte.maler.*
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregningSelectors.antallBarn
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregningSelectors.beregningsperioder
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregningSelectors.bruktTrygdetid
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregningSelectors.erForeldreloes
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregningSelectors.erYrkesskade
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregningSelectors.forskjelligTrygdetid
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregningSelectors.grunnbeloep
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregningSelectors.harForskjelligMetode
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregningSelectors.innhold
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregningSelectors.sisteBeregningsperiode
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregningSelectors.trygdetid
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregningsperiodeSelectors.harForeldreloessats
import no.nav.pensjon.etterlatte.maler.ForskjelligTrygdetidSelectors.erForskjellig
import no.nav.pensjon.etterlatte.maler.ForskjelligTrygdetidSelectors.foersteTrygdetid
import no.nav.pensjon.etterlatte.maler.ForskjelligTrygdetidSelectors.foersteVirkningsdato
import no.nav.pensjon.etterlatte.maler.ForskjelligTrygdetidSelectors.senereVirkningsdato
import no.nav.pensjon.etterlatte.maler.ForskjelligTrygdetidSelectors.tilOgIkkeMedSenereVirkningsdato
import no.nav.pensjon.etterlatte.maler.TrygdetidSelectors.beregnetTrygdetidAar
import no.nav.pensjon.etterlatte.maler.TrygdetidSelectors.beregningsMetodeAnvendt
import no.nav.pensjon.etterlatte.maler.TrygdetidSelectors.beregningsMetodeFraGrunnlag
import no.nav.pensjon.etterlatte.maler.TrygdetidSelectors.mindreEnnFireFemtedelerAvOpptjeningstiden
import no.nav.pensjon.etterlatte.maler.TrygdetidSelectors.navnAvdoed
import no.nav.pensjon.etterlatte.maler.TrygdetidSelectors.prorataBroek
import no.nav.pensjon.etterlatte.maler.TrygdetidSelectors.trygdetidsperioder
import no.nav.pensjon.etterlatte.maler.vedlegg.Trygdetidstabell


@TemplateModelHelpers
val beregningAvBarnepensjonGammeltOgNyttRegelverk: AttachmentTemplate<LangBokmalNynorskEnglish, BarnepensjonBeregning> = createAttachment(
    title = newText(
        Bokmal to "Beregning av barnepensjon",
        Nynorsk to "Utrekning av barnepensjon",
        English to "Calculation of Children’s Pension",
    ),
    includeSakspart = false
) {
    paragraph {
        text(
            bokmal { +"Barnepensjonen din blir beregnet ut fra grunnbeløpet i folketrygden og avdødes trygdetid." },
            nynorsk { +"Barnepensjonen din blir rekna ut med utgangspunkt i grunnbeløpet i folketrygda og trygdetida til avdøde." },
            english { +"Your children's pension is calculated according to the National Insurance Scheme basic amount (G) and the deceased's contribution time in the Norwegian National Insurance Scheme." },
        )
    }
    grunnbeloepetGammeltOgNyttRegelverk(grunnbeloep)
    with(bruktTrygdetid) {
        trygdetid(
            navnAvdoed,
            beregnetTrygdetidAar,
            prorataBroek,
            beregningsMetodeFraGrunnlag,
            beregningsMetodeAnvendt,
            mindreEnnFireFemtedelerAvOpptjeningstiden,
            forskjelligTrygdetid,
            harForskjelligMetode,
            erYrkesskade,
        )
        beregnetBarnepensjonGammeltOgNyttRegelverk(beregnetTrygdetidAar, prorataBroek, beregningsMetodeAnvendt, beregningsperioder)
    }
    forEach(trygdetid) {
        showIf(it.trygdetidsperioder.isNotEmpty()) {
            ifNotNull(it.navnAvdoed) { navnAvdoed ->
                perioderMedRegistrertTrygdetid(navnAvdoed, it.trygdetidsperioder, it.beregningsMetodeAnvendt)
            }
        }
    }
    meldFraTilNav()
}

@TemplateModelHelpers
val beregningAvBarnepensjonNyttRegelverk: AttachmentTemplate<LangBokmalNynorskEnglish, BarnepensjonBeregning> = createAttachment(
    title = newText(
        Bokmal to "Beregning av barnepensjon",
        Nynorsk to "Utrekning av barnepensjon",
        English to "Calculation of Children’s Pension",
    ),
    includeSakspart = false
) {
    paragraph {
        text(
            bokmal { +"Barnepensjonen din blir beregnet ut fra grunnbeløpet i folketrygden og avdødes trygdetid." },
            nynorsk { +"Barnepensjonen din blir rekna ut med utgangspunkt i grunnbeløpet i folketrygda og trygdetida til avdøde." },
            english { +"Your children's pension is calculated according to the National Insurance Scheme basic amount (G) and the deceased's contribution time in the Norwegian National Insurance Scheme." },
        )
    }
    showIf(erForeldreloes) {
         paragraph {
             text(
                 bokmal { +"Når begge foreldrene er døde, er det den forelderen med lengst trygdetid som legges " +
                         "til grunn i beregningen. Hvis begge foreldrene hadde full trygdetid, er det tilfeldig hvilken " +
                         "som blir lagt til grunn. Hvis ingen av foreldrene hadde full trygdetid, reduseres barnepensjonen " +
                         "forholdsmessig tilsvarende trygdetiden til den av foreldrene som hadde lengst trygdetid." },
                 nynorsk { +"Når begge foreldra er døde, vil forelderen med lengst trygdetid bli lagd til grunn " +
                         "i utrekninga. Dersom begge foreldra hadde full trygdetid, er det tilfeldig kva for ei trygdetid " +
                         "som blir lagt til grunn. Dersom ingen av foreldra hadde full trygdetid, blir barnepensjonen " +
                         "redusert forholdsmessig tilsvarande trygdetida til forelderen med lengst trygdetid." },
                 english { +"When both parents are deceased, the parent with the longest contribution time " +
                         "will be used as the basis for calculation. If both parents had full contribution time, " +
                         "it is random which one is used as a basis. If neither of the parents reached full " +
                         "contribution time, the children’s pension will be reduced proportionately based on " +
                         "the contribution time of the parent that had the longest contribution time." },
             )
         }
    }
    grunnbeloepetNyttRegelverk()
    with(bruktTrygdetid) {
        trygdetid(
            navnAvdoed,
            beregnetTrygdetidAar,
            prorataBroek,
            beregningsMetodeFraGrunnlag,
            beregningsMetodeAnvendt,
            mindreEnnFireFemtedelerAvOpptjeningstiden,
            forskjelligTrygdetid,
            harForskjelligMetode,
            erYrkesskade,
        )
        beregnetBarnepensjonNyttRegelverk(beregnetTrygdetidAar, prorataBroek, beregningsMetodeAnvendt, beregningsperioder, forskjelligTrygdetid)
    }
    forEach(trygdetid){
	    showIf(it.trygdetidsperioder.isNotEmpty()) {
            ifNotNull(it.navnAvdoed) { navnAvdoed ->
		        perioderMedRegistrertTrygdetid(navnAvdoed, it.trygdetidsperioder, it.beregningsMetodeAnvendt)
            }
	    }
    }
    meldFraTilNav()
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, BarnepensjonBeregning>.grunnbeloepetGammeltOgNyttRegelverk(
    grunnbeloep: Expression<Kroner>,
) {

    title2 {
        text(
            bokmal { +"Grunnbeløpet" },
            nynorsk { +"Grunnbeløpet" },
            english { +"Basic amount (G)" },
        )
    }

    showIf(antallBarn.greaterThan(1)) {
        paragraph {
            text(
                bokmal { +"Før 1. januar 2024" },
                nynorsk { +"Før 1. januar 2024" },
                english { +"Before 1 January 2024" },
                FontType.BOLD
            )
        }
        paragraph {
            text(
                bokmal { +"Nav gjør en samlet beregning av pensjon for barn som oppdras sammen. For denne beregningen har vi lagt til grunn at dere er " + antallBarn.format() + " barn som oppdras sammen." },
                nynorsk { +"Nav gjer ei samla utrekning av pensjon for barn som blir oppdregne saman. For denne utrekninga har vi lagt til grunn at de er " + antallBarn.format() + " barn som blir oppdregne saman." },
                english { +"Nav makes a combined pension calculation for children who are raised together. For this calculation, we have determined that your family has " + antallBarn.format() + " being raised together." },
            )
        }
        paragraph {
            text(
                bokmal { +"Barnepensjon per år utgjør 40 prosent av folketrygdens grunnbeløp (G) for det første barnet i søskenflokken. For hvert av de andre barna legges det til 25 prosent av G. Summen deles på antall barn, og pensjonen utbetales med likt beløp til hvert av barna." },
                nynorsk { +"Barnepensjonen per år utgjer 40 prosent av grunnbeløpet i folketrygda (G) for det første barnet i syskenflokken. For kvart av dei andre barna blir det lagt til 25 prosent av G. Summen blir delt på talet på barn, og pensjonen blir utbetalt med likt beløp til kvart av barna. " },
                english { +"The children’s pension amounts to 40 percent of the national insurance basic amount (G) for the first child in the sibling group. 25 percent of G is added for each of the other children. This sum is divided by the total number of children, and the pension is paid in equal amounts to each of the children." },
            )
        }
        paragraph {
            text(
                bokmal { +"Fra 1. januar 2024 (nye regler)" },
                nynorsk { +"Frå og med 1. januar 2024 (nye reglar)" },
                english { +"From 1 January 2024 (new rules)" },
                FontType.BOLD
            )
        }
        paragraph {
            text(
                bokmal { +"Barnepensjonen per år utgjør en ganger folketrygdens grunnbeløp (G)." },
                nynorsk { +"Barnepensjonen per år utgjer éin gong grunnbeløpet i folketrygda (G)." },
                english { +"The children's pension per year amounts to 1 x the national insurance basic amount (G)" },
            )
        }
    }.orShow {
        paragraph {
            text(
                bokmal { +"Før 1. januar 2024" },
                nynorsk { +"Før 1. januar 2024" },
                english { +"Before 1 January 2024" },
                FontType.BOLD
            )
        }
        paragraph {
            text(
                bokmal { +"Barnepensjonen per år utgjør 40 prosent av folketrygdens grunnbeløp (G)." },
                nynorsk { +"Barnepensjonen per år utgjer 40 prosent av grunnbeløpet i folketrygda (G)." },
                english { +"The children's pension per year amounts to 40 percent of the national insurance basic amount (G)." },
            )
        }
        paragraph {
            text(
                bokmal { +"Fra 1. januar 2024 (nye regler)" },
                nynorsk { +"Frå og med 1. januar 2024 (nye reglar)" },
                english { +"From 1 January 2024 (new rules)" },
                FontType.BOLD
            )
        }
        paragraph {
            text(
                bokmal { +"Barnepensjonen per år utgjør en ganger folketrygdens grunnbeløp (G)." },
                nynorsk { +"Barnepensjonen per år utgjer éin gong grunnbeløpet i folketrygda (G)." },
                english { +"The children's pension per year amounts to 1 x the national insurance basic amount (G)" },
            )
        }
    }

    paragraph {
        text(
            bokmal { +"Folketrygdens grunnbeløp er per i dag "  + grunnbeloep.format() + ". Grunnbeløpet blir regulert 1. mai hvert år. Økningen etterbetales vanligvis i juni hvert år." },
            nynorsk { +"Grunnbeløpet i folketrygda er per i dag "  + grunnbeloep.format() + ". Grunnbeløpet blir regulert 1. mai kvart år. Auken blir vanlegvis etterbetalt i juni kvart år." },
            english { +"The national insurance basic amount currently amounts to "  + grunnbeloep.format() + ". The basic amount is adjusted on 1 May each year. You will receive payment of any increase in June of each year." },
        )
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, BarnepensjonBeregning>.grunnbeloepetNyttRegelverk() {

    title2 {
        text(
            bokmal { +"Grunnbeløpet" },
            nynorsk { +"Grunnbeløpet" },
            english { +"Basic amount (G)" },
        )
    }
    paragraph {
        showIf(erForeldreloes) {
            ifNotNull(forskjelligTrygdetid) {
                text(
                    bokmal { +"Barnepensjonen per år utgjør en ganger folketrygdens grunnbeløp (G) når en forelder er død. " },
                    nynorsk { +"Barnepensjonen per år utgjer éin gong grunnbeløpet i folketrygda (G) når ein forelder er død." },
                    english { +"The children's pension per year amounts to 1 x the national insurance basic amount (G)." }
                )
            }
            text(
                bokmal { +"Når begge foreldrene er døde, blir den årlige pensjonen lik 2,25 ganger grunnbeløpet. " +
                        "Folketrygdens grunnbeløp per i dag er " + grunnbeloep.format() + ". " +
                        "Dette deles på 12 måneder. Grunnbeløpet blir regulert 1. mai hvert år. " +
                        "Økningen etterbetales vanligvis i juni hvert år." },
                nynorsk { +"Barnepensjonen per år utgjer 2,25 gongar grunnbeløpet i folketrygda (G) når begge " +
                        "foreldra er døde.  Grunnbeløpet i folketrygda er per i dag " + grunnbeloep.format() + ". " +
                        "Beløpet blir fordelt på 12 utbetalingar i året.  Grunnbeløpet blir regulert 1. mai kvart år. " +
                        "Auken blir vanlegvis etterbetalt i juni kvart år." },
                english { +"When both parents are deceased, the children's pension per year amounts to 2,25 x the " +
                        "national insurance basic amount (G). The national insurance basic amount currently amounts " +
                        "to " + grunnbeloep.format() + ". This amount is distributed in 12 payments a year. The basic amount is " +
                        "adjusted on 1 May each year. You will receive payment of any increase in June of each year. " }
            )
        }.orShow {
            text(
                bokmal { +"Barnepensjonen utgjør en ganger folketrygdens grunnbeløp (G). " +
                        "Folketrygdens grunnbeløp er per i dag " + grunnbeloep.format() + ". " +
                        "Grunnbeløpet blir regulert 1. mai hvert år. Økningen etterbetales vanligvis i juni hvert år. " },
                nynorsk { +"Barnepensjonen per år utgjer éin gong grunnbeløpet i folketrygda (G). " +
                        "Grunnbeløpet i folketrygda er per i dag  " + grunnbeloep.format() + ". " +
                        "Grunnbeløpet blir regulert 1. mai kvart år. Auken blir vanlegvis etterbetalt i juni kvart år." },
                english { +"The children's pension per year amounts to 1 x the national insurance basic " +
                        "amount (G). The national insurance basic amount currently amounts to " +
                        grunnbeloep.format() + ".  The basic amount is adjusted on 1 May each year. " +
                        "You will receive payment of any increase in June of each year. " },
            )
        }
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, BarnepensjonBeregning>.trygdetid(
    navnAvdoed: Expression<String?>,
    aarTrygdetid: Expression<Int>,
    prorataBroek: Expression<IntBroek?>,
    beregningsMetodeFraGrunnlag: Expression<BeregningsMetode>,
    beregningsMetodeAnvendt: Expression<BeregningsMetode>,
    mindreEnnFireFemtedelerAvOpptjeningstiden: Expression<Boolean>,
    forskjelligTrygdetid: Expression<ForskjelligTrygdetid?>,
    harForskjelligMetode: Expression<Boolean>,
    erYrkesskade: Expression<Boolean>,
) {
    title2 {
        text(
            bokmal { +"Trygdetid" },
            nynorsk { +"Trygdetid" },
            english { +"Period of national insurance coverage" },
        )
    }

    showIf(beregningsMetodeFraGrunnlag.equalTo(BeregningsMetode.NASJONAL).and(harForskjelligMetode.not())) {
        paragraph {
            text(
                bokmal { +"Trygdetiden tilsvarer det antall år avdøde har vært medlem i folketrygden etter " +
                        "fylte 16 år. Når avdøde var under 67 år ved dødsfallet blir det vanligvis beregnet " +
                        "framtidig trygdetid fram til og med det året avdøde ville ha fylt 66 år." },
                nynorsk { +"Trygdetida svarer til talet på år avdøde var medlem i folketrygda etter fylte " +
                        "16 år. Dersom personen døydde før fylte 67 år, blir det vanlegvis rekna ut framtidig " +
                        "trygdetid fram til og med det året avdøde ville ha fylt 66 år. " },
                english { +"Contribution time is the number of years the deceased has been a member of the " +
                        "Norwegian National Insurance Scheme after reaching the age of 16. For deceased persons under " +
                        "67 years of age at the time of death, the general rule is to calculate future contribution " +
                        "time up to and including the year the deceased would have turned 66." },
            )
        }
        paragraph {
            showIf(erForeldreloes.not()) {
                text(
                    bokmal { +"For å få full pensjon må avdødes trygdetid være beregnet til minst 40 år. " +
                            "Trygdetid over 40 år blir ikke tatt med i beregningen. Avdødes samlede trygdetid er " +
                            "beregnet til " + aarTrygdetid.format() + " år." },
                    nynorsk { +"For å få full pensjon må den utrekna trygdetida til avdøde vere minst 40 år. " +
                            "Trygdetid over 40 år blir ikkje teken med i utrekninga. Den utrekna trygdetida til avdøde " +
                            "er totalt " + aarTrygdetid.format() + " år." },
                    english { +"To be entitled to a full pension, the deceased must have " +
                            "accumulated at least 40 years of contribution time. Contribution time " +
                            "above 40 years of coverage is not included in the calculation. " +
                            "The deceased's total calculated contribution time is " + aarTrygdetid.format() + " years." },
                )
            }.orIfNotNull(forskjelligTrygdetid) { tidligTrygdetidPeriode ->
                ifNotNull(tidligTrygdetidPeriode.foersteTrygdetid.navnAvdoed, navnAvdoed) { foersteNavn, nesteNavn ->
                    val foersteVirkningsdato = tidligTrygdetidPeriode.foersteVirkningsdato.format()
                    val senereVirkningsdato = tidligTrygdetidPeriode.senereVirkningsdato.format()

                    text(
                        bokmal { +"For å få full pensjon må avdødes trygdetid være beregnet til minst 40 år. " +
                                "Når begge foreldrene er døde, må minst en av foreldrene ha minst 40 års trygdetid. " +
                                "Trygdetid over 40 år blir ikke tatt med i beregningen. " +
                                "Pensjonen din er beregnet etter " + foersteNavn + " fra " +
                                foersteVirkningsdato + ". Fra " + senereVirkningsdato + " er pensjonen din beregnet " +
                                "etter trygdetiden til " + nesteNavn + ". Avdødes samlede " +
                                "trygdetid er beregnet til " + aarTrygdetid.format() +  " år. " },
                        nynorsk { +"For å få full pensjon må den utrekna trygdetida til avdøde vere minst 40 år. " +
                                "For at det skal kunne betalast ut full pensjon når begge foreldra er død, må minst éin " +
                                "av foreldra ha hatt minimum 40 års trygdetid. " +
                                "Pensjonen din er rekna ut etter trygdetida til " + foersteNavn  + " frå " +
                                foersteVirkningsdato + ". Frå " + senereVirkningsdato + " er pensjonen rekna ut etter " +
                                "trygdetida til " + nesteNavn + ". " +
                                "Avdøde hadde ei samla trygdetid på " + aarTrygdetid.format() + " år." },
                        english { +"To be entitled to a full pension, the deceased must have accumulated at " +
                                "least 40 years of contribution time. When both parents are dead, at least one of the " +
                                "parents must have at least 40 years of National Insurance coverage. Contribution time " +
                                "above 40 years of coverage is not included in the calculation." +
                                "Your pension has been calculated based on the contribution time of " + foersteNavn +
                                " from " + foersteVirkningsdato + ". From the " + senereVirkningsdato + " your pension " +
                                "is calculated based on the contribution time of " + nesteNavn +
                                ". The deceased's total contribution time amounts to " + aarTrygdetid.format() + " years." },
                    )
                }.orShow {
                    // TODO støtter ikke to trygdetider med en ukjent og en kjent avdød. To ukjente skal ikke støttes i Gjenny
                }

            }.orShow {
                ifNotNull(navnAvdoed) { navnAvdoed ->
                    text(
                        bokmal { +"For å få full pensjon må minst en av foreldrene ha hatt minst 40 års trygdetid. " +
                                "Trygdetid over 40 år blir ikke tatt med i beregningen. Pensjonen din er beregnet " +
                                "etter trygdetiden til " + navnAvdoed + ". Avdødes samlede trygdetid er " +
                                "beregnet til " + aarTrygdetid.format() + " år." },
                        nynorsk { +"For at det skal kunne betalast ut full pensjon, må minst éin av foreldra " +
                                "ha hatt minimum 40 års trygdetid. Trygdetid over 40 år blir ikkje teken med i utrekninga. " +
                                "Pensjonen din er rekna ut etter trygdetida til " + navnAvdoed  + ". " +
                                "Avdøde hadde ei samla trygdetid på " + aarTrygdetid.format() + " år" },
                        english { +"To achieve a full pension, at least one of the parents must have accumulated 40 years " +
                                "of contribution time. Contribution time above 40 years of coverage is not included in the calculation. " +
                                "Your pension has been calculated based on the contribution time of " + navnAvdoed + ". " +
                                "The deceased's total contribution time amounts to " + aarTrygdetid.format() + " years." },
                    )
                }.orShow {
                    text(
                        bokmal { +"For å få full pensjon må minst en av foreldrene ha hatt minst 40 års trygdetid. " +
                                "Trygdetid over 40 år blir ikke tatt med i beregningen. Avdødes samlede trygdetid er " +
                                "beregnet til " + aarTrygdetid.format() + " år." },
                        nynorsk { +"For at det skal kunne betalast ut full pensjon, må minst éin av foreldra " +
                                "ha hatt minimum 40 års trygdetid. Trygdetid over 40 år blir ikkje teken med i utrekninga. " +
                                "Avdøde hadde ei samla trygdetid på " + aarTrygdetid.format() + " år" },
                        english { +"To achieve a full pension, at least one of the parents must have accumulated 40 years " +
                                "of contribution time. Contribution time above 40 years of coverage is not included in the calculation. " +
                                "The deceased's total contribution time amounts to " + aarTrygdetid.format() + " years." },
                    )
                }
            }
        }

        showIf(erYrkesskade) {
            paragraph {
                text(
                    bokmal { +"Det er bekreftet at dødsfallet skyldes en godkjent yrkesskade eller yrkessykdom. " +
                            "Det gis derfor barnepensjon etter egne særbestemmelser. Selv om den avdøde hadde mindre enn " +
                            "40 års trygdetid i Norge, er barnepensjonen beregnet med full trygdetid. Dette framkommer " +
                            "ikke i tabellen nedenfor." },
                    nynorsk { +"Det er stadfesta at dødsfallet kjem av ein godkjend yrkesskade eller yrkessjukdom. " +
                            "Det blir derfor gitt barnepensjon etter eigne særreglar. Sjølv om den avdøde hadde mindre " +
                            "enn 40 års trygdetid i Noreg, er barnepensjonen berekna med full trygdetid. " +
                            "Dette kjem ikkje fram i tabellen nedanfor." },
                    english { +"It has been confirmed that the death was caused by an approved occupational injury " +
                            "or disease. The children's pension is granted under special regulations. Although the " +
                            "deceased had less than 40 years of social security coverage in Norway, the children's pension " +
                            "is calculated based on a full social security period. This is not reflected in the table below." },
                )
            }
        }.orShowIf(mindreEnnFireFemtedelerAvOpptjeningstiden) {
            paragraph {
                text(
                    bokmal { +"Tabellen under «Perioder med registrert trygdetid» viser full framtidig trygdetid. " +
                            "Siden avdøde har vært medlem i folketrygden i mindre enn 4/5 av tiden mellom fylte 16 år og " +
                            "dødsfallstidspunktet (opptjeningstiden), blir framtidig trygdetid redusert til 40 år " +
                            "minus 4/5 av opptjeningstiden. Dette er mindre enn det tabellen viser." },
                    nynorsk { +"Tabellen under «Periodar med registrert trygdetid» viser full framtidig trygdetid. " +
                            "Ettersom avdøde var medlem av folketrygda i mindre enn 4/5 av tida mellom fylte 16 år og " +
                            "fram til sin død (oppteningstid), blir framtidig trygdetid redusert til 40 år minus 4/5 av " +
                            "oppteningstida. Dette er mindre enn det tabellen viser." },
                    english { +"The table in “Periods with Registered Contribution Time” shows the entire " +
                            "future contribution time. Since the deceased has been a member of the National Insurance " +
                            "Scheme for less than 4/5 of the time between turning 16 and the date of the death " +
                            "(qualifying period), the future contribution time is reduced to 40 years minus 4/5 of " +
                            "the qualifying period. This is less than what the table show." },
                )
            }
        }
    }
    showIf(beregningsMetodeFraGrunnlag.equalTo(BeregningsMetode.PRORATA).and(harForskjelligMetode.not())) {
        paragraph {
            showIf(erForeldreloes.not()) {
                text(
                    bokmal { +"For å få full pensjon må avdødes trygdetid være beregnet til minst 40 år. Trygdetid " +
                            "over 40 år blir ikke tatt med i beregningen." },
                    nynorsk { +"For å få full pensjon må den utrekna trygdetida til avdøde vere minst 40 år. " +
                            "Trygdetid over 40 år blir ikkje teken med i utrekninga." },
                    english { +"To be entitled to a full pension, the deceased must have accumulated at least 40 years " +
                            "of contribution time. Contribution time above 40 years of coverage is not included in the calculation." },
                )
            }.orIfNotNull(forskjelligTrygdetid) {
                text(
                    bokmal { +"For å få full pensjon må avdødes trygdetid være beregnet til minst 40 år. Når " +
                            "begge foreldrene er døde, må minst en av foreldrene ha minst 40 års trygdetid. Trygdetid " +
                            "over 40 år blir ikke tatt med i beregningen." },
                    nynorsk { +"For å få full pensjon må den utrekna trygdetida til avdøde vere minst 40 år. " +
                            "For at det skal kunne betalast ut full pensjon når begge foreldra er døde, må minst éin " +
                            "av foreldra ha hatt minimum 40 års trygdetid. Trygdetid over 40 år blir ikkje teken med " +
                            "i utrekninga." },
                    english { +"To be entitled to a full pension, the deceased must have accumulated at least " +
                            "40 years of contribution time. When both parents are dead, at least one of the parents " +
                            "must have at least 40 years of National Insurance coverage. Contribution time above 40 " +
                            "years of coverage is not included in the calculation." },
                )
            }.orShow {
                ifNotNull(navnAvdoed) { navnAvdoed ->
                    text(
                        bokmal { +"For å få full pensjon må minst en av foreldrene ha hatt minst 40 års trygdetid. " +
                                "Trygdetid over 40 år blir ikke tatt med i beregningen. Pensjonen din er beregnet etter " +
                                "trygdetiden til " + navnAvdoed + ". Avdødes samlede trygdetid er beregnet " +
                                "til " + aarTrygdetid.format() + " år." },
                        nynorsk { +"For at det skal kunne betalast ut full pensjon, må minst éin av foreldra ha hatt " +
                                "minimum 40 års trygdetid. Trygdetid over 40 år blir ikkje teken med i utrekninga. " +
                                "Pensjonen din er rekna ut etter trygdetida til " + navnAvdoed + ". " +
                                "Avdøde hadde ei samla trygdetid på " + aarTrygdetid.format() + " år." },
                        english { +"To achieve a full pension, at least one of the parents must have accumulated " +
                                "40 years of contribution time. Contribution time above 40 years of coverage is not included " +
                                "in the calculation. Your pension has been calculated based on the contribution time " +
                                "of " + navnAvdoed + ". The deceased total contribution time amounts to " + aarTrygdetid.format() + " years." },
                    )
                }.orShow {
                    text(
                        bokmal { +"For å få full pensjon må minst en av foreldrene ha hatt minst 40 års trygdetid. " +
                                "Trygdetid over 40 år blir ikke tatt med i beregningen. Avdødes samlede trygdetid er beregnet " +
                                "til " + aarTrygdetid.format() + " år." },
                        nynorsk { +"For at det skal kunne betalast ut full pensjon, må minst éin av foreldra ha hatt " +
                                "minimum 40 års trygdetid. Trygdetid over 40 år blir ikkje teken med i utrekninga. " +
                                "Avdøde hadde ei samla trygdetid på " + aarTrygdetid.format() + " år." },
                        english { +"To achieve a full pension, at least one of the parents must have accumulated " +
                                "40 years of contribution time. Contribution time above 40 years of coverage is not included " +
                                "in the calculation. The deceased total contribution time amounts to " + aarTrygdetid.format() + " years." },
                    )
                }

            }
        }
        ifNotNull(forskjelligTrygdetid) { forskjelligTrygdetid ->
            val foersteAarTrygdetid = forskjelligTrygdetid.foersteTrygdetid.beregnetTrygdetidAar.format()
            val foersteProrataBroek = forskjelligTrygdetid.foersteTrygdetid.prorataBroek.formatBroek()
            val foersteVirk = forskjelligTrygdetid.foersteVirkningsdato.format()
            val senereVirk = forskjelligTrygdetid.senereVirkningsdato.format()

            paragraph {
                text(
                    bokmal { +"Pensjonen din er beregnet etter bestemmelsene i EØS-avtalen fordi vilkårene " +
                            "for rett til pensjon er oppfylt ved sammenlegging av avdødes opptjeningstid i Norge og " +
                            "andre EØS- eller avtaleland. Trygdetiden er beregnet etter avdødes samlede opptjeningstid i " +
                            "disse landene. For å beregne norsk del av denne trygdetiden ganges avdødes samlede " +
                            "opptjeningstid med et forholdstall, som angir forholdet mellom faktisk opptjeningstid i " +
                            "Norge og samlet faktisk opptjeningstid i Norge og andre EØS- eller avtaleland. " },
                    nynorsk { +"Pensjonen din er rekna ut etter føresegnene i EØS-avtalen, då vilkåra for rett til " +
                            "pensjon er oppfylte ved samanlegging av oppteningstida til avdøde i Noreg og andre EØS- eller " +
                            "avtaleland. Trygdetida er rekna ut etter den samla oppteningstida til avdøde i desse landa. " +
                            "For å rekne ut den norske delen av denne trygdetida blir den samla oppteningstida til avdøde " +
                            "gonga med eit forholdstal som angir forholdet mellom faktisk oppteningstid i Noreg og samla " +
                            "faktisk opptening i Noreg og andre EØS- eller avtaleland." },
                    english { +"Your pension is calculated according to the provisions in the EEA Agreement because " +
                            "the conditions that entitle you to a pension have been met, by compiling the deceased's " +
                            "contribution time in Norway and other EEA countries or other countries with which Norway has " +
                            "an agreement. Contribution time is calculated according to the deceased's total contribution " +
                            "time in these countries. To calculate the Norwegian part of this contribution time, the " +
                            "deceased's total contribution time is multiplied with a proportional fraction that provides " +
                            "a ratio between the actual contribution time in Norway and the total actual contribution " +
                            "time in Norway and any other EEA or agreement country." }
                )
            }
            paragraph {
                text(
                    bokmal { +"Avdødes samlede trygdetid er beregnet til " + foersteAarTrygdetid +
                            " år, og forholdstallet til " + foersteProrataBroek +
                            " for perioden når en forelder er død. For perioden fra begge foreldrene er døde er " +
                            "avdødes samlede trygdetid er beregnet til " + aarTrygdetid.format() +
                            " år, og forholdstallet til " + prorataBroek.formatBroek() + "." },
                    nynorsk { +"Den utrekna trygdetida til avdøde er totalt " + foersteAarTrygdetid +
                            " år, og forholdstalet er " + foersteProrataBroek + " frå " + foersteVirk + " til " +
                            forskjelligTrygdetid.tilOgIkkeMedSenereVirkningsdato.format() + ". Frå " + senereVirk +
                            " er den utrekna trygdetida til avdøde totalt " + aarTrygdetid.format() +
                            " år, og forholdstalet er " + prorataBroek.formatBroek() + "." },
                    english { +"The deceased's total contribution time amounts to " + foersteAarTrygdetid +
                            " years, and the proportional fraction of " + foersteProrataBroek + " for the period when " +
                            "a parent has died. For the period from the death of both parents, the deceased's total " +
                            "contribution time amounts to " + aarTrygdetid.format() +
                            " years, and the proportional fraction of " + prorataBroek.formatBroek() + "." }
                )
            }

        } orShow {
            paragraph {
                text(
                    bokmal { +"Pensjonen din er beregnet etter bestemmelsene i EØS-avtalen fordi vilkårene for rett til pensjon er oppfylt ved sammenlegging av avdødes opptjeningstid i Norge og andre EØS- eller avtaleland. Trygdetiden er beregnet etter avdødes samlede opptjeningstid i disse landene. For å beregne norsk del av denne trygdetiden ganges avdødes samlede opptjeningstid med et forholdstall, som angir forholdet mellom faktisk opptjeningstid i Norge og samlet faktisk opptjeningstid i Norge og andre EØS- eller avtaleland. Avdødes samlede trygdetid er beregnet til " + aarTrygdetid.format() + " år, og forholdstallet til " + prorataBroek.formatBroek() + "." },
                    nynorsk { +"Pensjonen din er rekna ut etter føresegnene i EØS-avtalen, då vilkåra for rett til pensjon er oppfylte ved samanlegging av oppteningstida til avdøde i Noreg og andre EØS- eller avtaleland. Trygdetida er rekna ut etter den samla oppteningstida til avdøde i desse landa. For å rekne ut den norske delen av denne trygdetida blir den samla oppteningstida til avdøde gonga med eit forholdstal som angir forholdet mellom faktisk oppteningstid i Noreg og samla faktisk opptening i Noreg og andre EØS- eller avtaleland. Den utrekna trygdetida til avdøde er totalt " + aarTrygdetid.format() + " år, og forholdstalet er " + prorataBroek.formatBroek() + "." },
                    english { +"Your pension is calculated according to the provisions in the EEA Agreement because the conditions that entitle you to a pension have been met, by compiling the deceased's contribution time in Norway and other EEA countries or other countries with which Norway has an agreement. Contribution time is calculated according to the deceased's total contribution time in these the countries. To calculate the Norwegian part of this contribution time, the deceased's total contribution time is multiplied with a proportional fraction that provides a ratio between the actual contribution time in Norway and the total actual contribution time in Norway and any other EEA or agreement country. The deceased's total contribution time amounts to " + aarTrygdetid.format() + " years, and the proportional fraction of " + prorataBroek.formatBroek() + "." },
                )
            }
        }

        showIf(mindreEnnFireFemtedelerAvOpptjeningstiden) {
            paragraph {
                text(
                    bokmal { +"Siden avdøde har vært medlem i et EØS-land i mindre enn 4/5 av tiden mellom fylte " +
                            "16 år og dødsfallstidspunktet (opptjeningstiden), blir den framtidige trygdetiden redusert. " +
                            "Denne reduksjonen skjer før prorata-beregningen utføres. " },
                    nynorsk { +"Sidan avdøde har vært medlem i eit EØS-land i mindre enn 4/5 av tiden mellom fylte " +
                            "16 år og dødsfallstidspunktet (oppteningstida), blir den framtidige trygdetida redusert. " +
                            "Reduksjonen skjer før utrekning av prorata. " },
                    english { +"Since the deceased has been a member of the National Insurance Scheme for less " +
                            "than 4/5 of the period between the age of 16 and the time of death (qualifying period), " +
                            "the future social contribution time is reduced to 40 years minus 4/5 of the qualifying " +
                            "period. This reduction takes place before the pro rata calculation is performed. " },
                )
            }
        }
    }
    showIf(beregningsMetodeFraGrunnlag.equalTo(BeregningsMetode.BEST).or(harForskjelligMetode)) {
        showIf(erForeldreloes.not()) {
            paragraph {
                text(
                    bokmal { +"For å få full pensjon må avdødes trygdetid være beregnet til minst 40 år. " +
                            "Trygdetid over 40 år blir ikke tatt med i beregningen. Når grunnlag for pensjon er " +
                            "oppfylt etter nasjonale regler, og avdøde også har opptjening av medlemsperioder i land " +
                            "som Norge har trygdeavtale med, skal trygdetid gis etter den beste beregningen av kun " +
                            "nasjonal opptjening og av sammenlagt opptjening i Norge og avtaleland." },
                    nynorsk { +"For å få full pensjon må den utrekna trygdetida til avdøde vere minst 40 år. " +
                            "Trygdetid over 40 år blir ikkje teken med i utrekninga. Når grunnlaget for pensjon er " +
                            "oppfylt etter nasjonale reglar, og avdøde også har opptening av medlemsperiodar i land " +
                            "som Noreg har trygdeavtale med, skal det bli gitt trygdetid etter den utrekninga som er " +
                            "best av berre nasjonal opptening og samanlagd opptening i Noreg og avtaleland." },
                    english { +"To be entitled to a full pension, the deceased must have accumulated at least " +
                            "40 years of contribution time. Contribution time above 40 years of coverage is not " +
                            "included in the calculation. When the basis for the pension is met according to national " +
                            "rules, and the deceased has also accrued membership periods in countries with which " +
                            "Norway has a national insurance agreement, the contribution time must be stated according " +
                            "to the best calculation of (only) national contribution and of the combined " +
                            "contribution time in Norway and the agreement country(ies)." },
                )
            }
        }.orShow {
            ifNotNull(forskjelligTrygdetid) {
                paragraph {
                    text(
                        bokmal { +"For å få full pensjon må avdødes trygdetid være beregnet til minst 40 år. Når " +
                                "begge foreldrene er døde, må minst en av foreldrene ha minst 40 års trygdetid. Trygdetid " +
                                "over 40 år blir ikke tatt med i beregningen. Når grunnlag for pensjon er oppfylt etter " +
                                "nasjonale regler, og avdøde også har opptjening av medlemsperioder i land som Norge har " +
                                "trygdeavtale med, skal trygdetid gis etter den beste beregningen av kun nasjonal " +
                                "opptjening og av sammenlagt opptjening i Norge og avtaleland." },
                        nynorsk { +"For å få full pensjon må den utrekna trygdetida til avdøde vere minst 40 år. " +
                                "For at det skal kunne betalast ut full pensjon når begge foreldra er død, må minst éin " +
                                "av foreldra ha hatt minimum 40 års trygdetid. Trygdetid over 40 år blir ikkje teken med " +
                                "i utrekninga." },
                        english { +"To be entitled to a full pension, the deceased must have accumulated at least " +
                                "40 years of contribution time. When both parents are dead, at least one of the parents " +
                                "must have at least 40 years of National Insurance coverage. Contribution time above 40 " +
                                "years of coverage is not included in the calculation." }
                    )
                }
            }.orShow {
                ifNotNull(navnAvdoed) { navnAvdoed ->
                    paragraph {
                        text(
                            bokmal { +"For å få full pensjon må avdødes trygdetid være beregnet til minst 40 år. " +
                                    "Trygdetid over 40 år blir ikke tatt med i beregningen. Pensjonen din er beregnet etter " +
                                    "trygdetiden til " + navnAvdoed + ". Avdødes samlede trygdetid er beregnet " +
                                    "til " + aarTrygdetid.format() + " år." },
                            nynorsk { +"For at det skal kunne betalast ut full pensjon, må minst éin av foreldra ha hatt " +
                                    "minimum 40 års trygdetid. Trygdetid over 40 år blir ikkje teken med i utrekninga. " +
                                    "Pensjonen din er rekna ut etter trygdetida til " + navnAvdoed + ". " +
                                    "Avdøde hadde ei samla trygdetid på " + aarTrygdetid.format() + " år." },
                            english { +"To achieve a full pension, at least one of the parents must have accumulated " +
                                    "40 years of contribution time. Contribution time above 40 years of coverage is not " +
                                    "included in the calculation. Your pension has been calculated based on the contribution " +
                                    "time of " + navnAvdoed + ". The deceased total contribution time amounts to " +
                                    aarTrygdetid.format() + " years. " },
                        )
                    }
                }.orShow {
                    paragraph {
                        text(
                            bokmal { +"For å få full pensjon må avdødes trygdetid være beregnet til minst 40 år. " +
                                    "Trygdetid over 40 år blir ikke tatt med i beregningen. Avdødes samlede trygdetid er beregnet " +
                                    "til " + aarTrygdetid.format() + " år." },
                            nynorsk { +"For at det skal kunne betalast ut full pensjon, må minst éin av foreldra ha hatt " +
                                    "minimum 40 års trygdetid. Trygdetid over 40 år blir ikkje teken med i utrekninga. " +
                                    "Avdøde hadde ei samla trygdetid på " + aarTrygdetid.format() + " år." },
                            english { +"To achieve a full pension, at least one of the parents must have accumulated " +
                                    "40 years of contribution time. Contribution time above 40 years of coverage is not " +
                                    "included in the calculation. The deceased total contribution time amounts to " +
                                    aarTrygdetid.format() + " years. " },
                        )
                    }
                }
            }
            paragraph {
                text(
                    bokmal { +"Når grunnlag for pensjon er oppfylt etter nasjonale regler, og avdøde også har opptjening av medlemsperioder i land som Norge har trygdeavtale med, skal trygdetid gis etter den beste beregningen av kun nasjonal opptjening og av sammenlagt opptjening i Norge og avtaleland." },
                    nynorsk { +"Når grunnlaget for pensjon er oppfylt etter nasjonale reglar, og avdøde også har opptening av medlemsperiodar i land som Noreg har trygdeavtale med, skal det bli gitt trygdetid etter den utrekninga som er best av berre nasjonal opptening og samanlagd opptening i Noreg og avtaleland." },
                    english { +"When the basis for the pension is met according to national rules, and the deceased has also accrued membership periods in countries with which Norway has a national insurance agreement, the contribution time must be stated according to the best calculation of (only) national contribution and of the combined contribution time in Norway and the agreement country(ies)." },
                )
            }
        }
        paragraph {
            text(
                bokmal { +"Ved nasjonal beregning av trygdetid tilsvarer denne det antall år avdøde har vært medlem i folketrygden etter fylte 16 år. Når avdøde var under 67 år ved dødsfallet blir det vanligvis beregnet framtidig trygdetid fram til og med det året avdøde ville ha fylt 66 år." },
                nynorsk { +"Ved nasjonal utrekning av trygdetida svarer denne til talet på år avdøde var medlem i folketrygda etter fylte 16 år. Dersom personen døydde før fylte 67 år, blir det vanlegvis rekna ut framtidig trygdetid fram til og med det året avdøde ville ha fylt 66 år." },
                english { +"For calculating national contribution time, this equals the number of years the deceased has been a member of the Norwegian National Insurance Scheme after reaching the age of 16. For deceased persons under 67 years of age at the time of death, the general rule is to calculate future contribution time up to and including the year the deceased would have turned 66." },
            )
        }
        paragraph {
            text(
                bokmal { +"Ved sammenlegging av avdødes opptjeningstid i Norge og andre EØS/avtale-land er trygdetiden beregnet etter avdødes samlede opptjeningstid i disse landene. For å beregne norsk del av denne trygdetiden ganges avdødes samlede opptjeningstid med et forholdstall, som angir forholdet mellom faktisk opptjeningstid i Norge og samlet faktisk opptjeningstid i Norge og andre EØS-land." },
                nynorsk { +"Ved samanlegging av oppteningstida til avdøde i Noreg og andre EØS-/avtaleland blir trygdetida rekna ut etter den samla oppteningstida til avdøde i desse landa. For å rekne ut den norske delen av denne trygdetida blir den samla oppteningstida til avdøde gonga med eit forholdstal som angir forholdet mellom faktisk oppteningstid i Noreg og samla faktisk opptening i Noreg og andre EØS-land." },
                english { +"For comparing the deceased's contribution time in Norway with other EEA/agreement countries, the contribution time is calculated according to the deceased's total contribution time in these the countries. To calculate the Norwegian part of this contribution time, the deceased's total contribution time is multiplied with a proportional fraction that provides the ratio between the actual contribution time in Norway and the total actual contribution time in Norway and any other EEA or agreement country." },
            )
        }

        ifNotNull(forskjelligTrygdetid) {
            val anvendt = it.foersteTrygdetid.beregningsMetodeAnvendt
            val aar = it.foersteTrygdetid.beregnetTrygdetidAar
            val broek = it.foersteTrygdetid.prorataBroek
            val andreVirk = it.senereVirkningsdato

            showIf(anvendt.equalTo(BeregningsMetode.NASJONAL)) {
                bestBeregnetNasjonal(aar)
            }.orShowIf(anvendt.equalTo(BeregningsMetode.PRORATA)) {
                bestBeregnetProrata(aar, broek)
            }
            showIf(it.erForskjellig) {
                showIf(beregningsMetodeAnvendt.equalTo(BeregningsMetode.NASJONAL)) {
                    paragraph {
                        text(
                            bokmal { +"Fra " + andreVirk.format() + " er det den andre forelderen sin " +
                                    "trygdetid som er brukt i beregningen. Avdødes samlede trygdetid er beregnet til " +
                                    aarTrygdetid.format() + " år ved nasjonal opptjening. Dette gir den beste " +
                                    "beregningen av trygdetid." },
                            nynorsk { +"Frå " + andreVirk.format() + " er det den andre forelderen sin " +
                                    "trygdetid som er brukt i beregningen. Avdødes samlede trygdetid er beregnet til " +
                                    aarTrygdetid.format() + " år ved nasjonal opptjening. Dette fordi den gir beste " +
                                    "utrekning av trygdetid." },
                            english { +"From " + andreVirk.format() + " it is the other parent's total " +
                                    "contribution time that has been used in the calculation. The deceased's total " +
                                    "calculated contribution time is " + aarTrygdetid.format() + " years of national " +
                                    "contributions. This provides the best calculation for total contribution time." },
                        )
                    }
                }.orShowIf(beregningsMetodeAnvendt.equalTo(BeregningsMetode.PRORATA)) {
                    paragraph {
                        text(
                            bokmal { +"Fra " + andreVirk.format() + " er det den andre forelderen sin " +
                                    "trygdetid som er brukt i beregingen. Avdødes samlede trygdetid fra avtaleland " +
                                    "er beregnet til " + aarTrygdetid.format() + " år, og forholdstallet til " +
                                    prorataBroek.formatBroek() + ". Dette gir den beste beregningen av trygdetid." },
                            nynorsk { +"Pensjonen din er rekna ut etter trygdetida til " +
                                    " frå " + andreVirk.format() + ". Den utrekna trygdetida til avdøde frå " +
                                    "avtaleland er totalt " + aarTrygdetid.format() + " år, og forholdstalet er " +
                                    prorataBroek.formatBroek() + ". Dette gir den beste utrekninga av trygdetid." },
                            english { +"From " + andreVirk.format() + " it is the other parent's total " +
                                    "contribution time that has been used in the calculation. The deceased's total " +
                                    "contribution time from agreement countries amounts to " + aarTrygdetid.format() +
                                    " years, and the proportional fraction of " + prorataBroek.formatBroek() +
                                    ". This provides the best calculation for total contribution time." },
                        )
                    }
                }
            }
        }.orShow {
            showIf(beregningsMetodeAnvendt.equalTo(BeregningsMetode.PRORATA)) {
                bestBeregnetProrata(aarTrygdetid, prorataBroek)
            }.orShowIf(beregningsMetodeAnvendt.equalTo(BeregningsMetode.NASJONAL)) {
                bestBeregnetNasjonal(aarTrygdetid)
            }
        }

    }

    konverterElementerTilBrevbakerformat(innhold)
}

private fun OutlineOnlyScope<LangBokmalNynorskEnglish, BarnepensjonBeregning>.bestBeregnetNasjonal(aarTrygdetid: Expression<Int>) {
    paragraph {
        text(
            bokmal { +"Avdødes samlede trygdetid er beregnet til " + aarTrygdetid.format() + " år ved nasjonal opptjening. Dette gir den beste beregningen av trygdetid." },
            nynorsk { +"Den utrekna trygdetida til avdøde er totalt " + aarTrygdetid.format() + " år ved nasjonal opptening. Dette gir den beste utrekninga av trygdetid." },
            english { +"The deceased's total calculated contribution time is " + aarTrygdetid.format() + " years in national contributions. This provides the best calculation for total contribution time." },
        )
    }
}


private fun OutlineOnlyScope<LangBokmalNynorskEnglish, BarnepensjonBeregning>.bestBeregnetProrata(aarTrygdetid: Expression<Int>, prorataBroek: Expression<IntBroek?>) {
    paragraph {
        text(
            bokmal { +"Avdødes samlede trygdetid fra avtaleland er beregnet til " + aarTrygdetid.format() + " år, og forholdstallet til " + prorataBroek.formatBroek() + ". Dette gir den beste beregningen av trygdetid." },
            nynorsk { +"Den utrekna trygdetida til avdøde frå avtaleland er totalt " + aarTrygdetid.format() + " år, og forholdstalet er " + prorataBroek.formatBroek() + ". Dette gir den beste utrekninga av trygdetid." },
            english { +"The deceased's total contribution time from agreement countries amounts to " + aarTrygdetid.format() + " years, and the proportional fraction of " + prorataBroek.formatBroek() + ". This provides the best calculation for total contribution time." },
        )
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, BarnepensjonBeregning>.beregnetBarnepensjonNyttRegelverk(
    aarTrygdetid: Expression<Int>,
    prorataBroek: Expression<IntBroek?>,
    beregningsMetodeAnvendt: Expression<BeregningsMetode>,
    beregningsperioder: Expression<List<BarnepensjonBeregningsperiode>>,
    forskjelligTrygdetid: Expression<ForskjelligTrygdetid?>,
) {
    title2 {
        text(
            bokmal { +"Beregnet barnepensjon" },
            nynorsk { +"Utrekna barnepensjon" },
            english { +"Calculation of Children’s Pension" },
        )
    }
    ifNotNull(forskjelligTrygdetid) {
        showIf(it.foersteTrygdetid.beregningsMetodeAnvendt.equalTo(BeregningsMetode.NASJONAL)) {
            paragraph {
                text(
                    bokmal { +"Barnepensjonen per år er beregnet til 1 G ganget med " +
                            it.foersteTrygdetid.beregnetTrygdetidAar.format() + "/40 trygdetid for perioden " +
                            it.foersteVirkningsdato.format() + "-" + it.tilOgIkkeMedSenereVirkningsdato.format() +
                            ". Beløpet fordeles på 12 utbetalinger i året." },
                    nynorsk { +"Barnepensjonen per år er rekna ut til 1 G gonga med " +
                            it.foersteTrygdetid.beregnetTrygdetidAar.format() +
                            "/40 trygdetid i perioda " +
                            it.foersteVirkningsdato.format() + "-" + it.tilOgIkkeMedSenereVirkningsdato.format() +
                            ". Beløpet blir fordelt på 12 utbetalingar i året." },
                    english { +"The children's pension paid per year is calculated to 1G x " +
                            it.foersteTrygdetid.beregnetTrygdetidAar.format() +
                            "/40 contribution time for the period " +
                            it.foersteVirkningsdato.format() + "-" + it.tilOgIkkeMedSenereVirkningsdato.format() +
                            ". This amount is distributed in 12 payments a year." },
                )
            }
        }.orShow {
            paragraph {
                text(
                    bokmal { +"Barnepensjonen per år er beregnet til 1 G ganget med " +
                            it.foersteTrygdetid.beregnetTrygdetidAar.format() + "/40 trygdetid, ganget med " +
                            "forholdstallet " + it.foersteTrygdetid.prorataBroek.formatBroek() + " for perioden " +
                            it.foersteVirkningsdato.format() + "-" + it.tilOgIkkeMedSenereVirkningsdato.format() +
                            ". Beløpet fordeles på 12 utbetalinger i året." },
                    nynorsk { +"Barnepensjonen per år er rekna ut til 1 G gonga med " +
                            it.foersteTrygdetid.beregnetTrygdetidAar.format() +
                            "/40 trygdetid, gonga med forholdstalet " + it.foersteTrygdetid.prorataBroek.formatBroek() +
                            " i perioda " +
                            it.foersteVirkningsdato.format() + "-" + it.tilOgIkkeMedSenereVirkningsdato.format() +
                            ". Beløpet blir fordelt på 12 utbetalingar i året." },
                    english { +"The children's pension paid per year is calculated to 1G x " +
                            it.foersteTrygdetid.beregnetTrygdetidAar.format() +
                            "/40 contribution time, multiplied by the proportional fraction " +
                            it.foersteTrygdetid.prorataBroek.formatBroek() +
                            " for the period " +
                            it.foersteVirkningsdato.format() + "-" + it.tilOgIkkeMedSenereVirkningsdato.format() +
                            ". This amount is distributed in 12 payments a year." },
                )
            }
        }
        showIf(beregningsMetodeAnvendt.equalTo(BeregningsMetode.PRORATA)) {
            paragraph {
                text(
                    bokmal { +"Barnepensjonen per år er beregnet til 2,25 G ganget med " +
                            aarTrygdetid.format() + "/40 år trygdetid, ganget med forholdstallet " +
                            prorataBroek.formatBroek() + " fra " + it.senereVirkningsdato.format() +
                            ". Beløpet fordeles på 12 utbetalinger i året." },
                    nynorsk { +"Barnepensjonen per år er rekna ut til 2,25 G gonga med " +
                            aarTrygdetid.format() + "/40 trygdetid, gonga med forholdstalet " +
                            prorataBroek.formatBroek() + " frå " + it.senereVirkningsdato.format() +
                            ". Beløpet blir fordelt på 12 utbetalingar i året." },
                    english { +"The children's pension per year is calculated at 2,25 G x " +
                            aarTrygdetid.format() + "/40 contribution time, multiplied by the proportional fraction " +
                            prorataBroek.formatBroek() + " from " + it.senereVirkningsdato.format() +
                            ". This amount is distributed in 12 payments a year." },
                )
            }
        }.orShowIf(beregningsMetodeAnvendt.equalTo(BeregningsMetode.NASJONAL)) {
            paragraph {
                text(
                    bokmal { +"Barnepensjonen er beregnet til 2,25 G ganget med " +
                            aarTrygdetid.format() + "/40 år trygdetid fra " + it.senereVirkningsdato.format() +
                            ". Beløpet fordeles på 12 utbetalinger i året." },
                    nynorsk { +"Barnepensjonen per år er rekna ut til 2,25 G gonga med " +
                            aarTrygdetid.format() + "/40 trygdetid frå " + it.senereVirkningsdato.format() +
                            ". Beløpet blir fordelt på 12 utbetalingar i året." },
                    english { +"The children's pension paid per year is calculated to 2,25 G x " +
                            aarTrygdetid.format() + "/40 contribution time from " + it.senereVirkningsdato.format() +
                            ". This amount is distributed in 12 payments a year." },
                )
            }
        }
    }.orShow {
        showIf(beregningsMetodeAnvendt.equalTo(BeregningsMetode.PRORATA)) {
            paragraph {
                text(
                    bokmal { +"Barnepensjonen per år er beregnet til " + barnepensjonssats() + " G ganget med " + aarTrygdetid.format() + "/40 år trygdetid, ganget med forholdstallet " + prorataBroek.formatBroek() + ". Beløpet fordeles på 12 utbetalinger i året." },
                    nynorsk { +"Barnepensjonen per år er rekna ut til " + barnepensjonssats() + " G gonga med " + aarTrygdetid.format() + "/40 trygdetid, gonga med forholdstalet " + prorataBroek.formatBroek() + ". Beløpet blir fordelt på 12 utbetalingar i året." },
                    english { +"The children's pension per year is calculated at " + barnepensjonssats() + "G x " + aarTrygdetid.format() + "/40 contribution time, multiplied by the proportional fraction " + prorataBroek.formatBroek() + ". This amount is distributed in 12 payments a year." },
                )
            }
        }.orShowIf(beregningsMetodeAnvendt.equalTo(BeregningsMetode.NASJONAL)) {
            paragraph {
                text(
                    bokmal { +"Barnepensjonen er beregnet til " + barnepensjonssats() + " G ganget med " + aarTrygdetid.format() + "/40 år trygdetid. Beløpet fordeles på 12 utbetalinger i året." },
                    nynorsk { +"Barnepensjonen per år er rekna ut til " + barnepensjonssats() + " G gonga med " + aarTrygdetid.format() + "/40 trygdetid. Beløpet blir fordelt på 12 utbetalingar i året." },
                    english { +"The children's pension paid per year is calculated to " + barnepensjonssats() + "G x " + aarTrygdetid.format() + "/40 contribution time. This amount is distributed in 12 payments a year." },
                )
            }
        }
    }

    includePhrase(Beregningsperiodetabell(beregningsperioder))
}

private fun ParagraphOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, BarnepensjonBeregning>.barnepensjonssats(): StringExpression {
    return ifElse(sisteBeregningsperiode.harForeldreloessats, 2.25, 1).formatTall()
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, BarnepensjonBeregning>.beregnetBarnepensjonGammeltOgNyttRegelverk(
    aarTrygdetid: Expression<Int>,
    prorataBroek: Expression<IntBroek?>,
    beregningsMetodeAnvendt: Expression<BeregningsMetode>,
    beregningsperioder: Expression<List<BarnepensjonBeregningsperiode>>
) {
    title2 {
        text(
            bokmal { +"Beregnet barnepensjon" },
            nynorsk { +"Utrekna barnepensjon" },
            english { +"Calculation of Children’s Pension" },
        )
    }
    showIf(beregningsMetodeAnvendt.equalTo(BeregningsMetode.PRORATA)) {
        showIf(antallBarn.greaterThan(1)) {
            paragraph {
                text(
                    bokmal { +"Før 1. januar 2024" },
                    nynorsk { +"Før 1. januar 2024" },
                    english { +"Before 1 January 2024" },
                    FontType.BOLD
                )
            }
            paragraph {
                text(
                    bokmal { +"Barnepensjonen per år er beregnet ut fra at det er " + antallBarn.format() + " søsken som oppdras sammen, med 0,4 G til første barn, og 0,25 G til påfølgende barn. Beløpet fordeles likt på hvert barn, og blir ganget med " + aarTrygdetid.format() + "/40 trygdetid, ganget med forholdstallet " + prorataBroek.formatBroek() + ". Beløpet fordeles på 12 utbetalinger i året." },
                    nynorsk { +"Utrekninga av barnepensjonen tek utgangspunkt i at det er " + antallBarn.format() + " søsken som blir oppdregne saman, med 0,4 G til første barn, og 0,25 G til påfølgjande barn. Beløpet blir fordelt likt på kvart barn, og blir gonga med " + aarTrygdetid.format() + "/40 trygdetid, gonga med forholdstalet " + prorataBroek.formatBroek() + ". Beløpet blir fordelt på 12 utbetalingar i året." },
                    english { +"The children's pension per year is calculated according to the assumption that there are " + antallBarn.format() + " siblings being raised together, with 0,4G going to the first child, and 0,25G to subsequent children. This amount is distributed equally to each child and multiplied by " + aarTrygdetid.format() + "/40 contribution time, multiplied by the proportional fraction " + prorataBroek.formatBroek() + ". This amount is distributed in 12 payments a year." },
                )
            }
            paragraph {
                text(
                    bokmal { +"Fra 1. januar 2024" },
                    nynorsk { +"Frå og med 1. januar 2024" },
                    english { +"From 1 January 2024" },
                    FontType.BOLD
                )
            }
            paragraph {
                text(
                    bokmal { +"Barnepensjonen per år er beregnet til " + barnepensjonssats() + " G ganget med " + aarTrygdetid.format() + "/40 trygdetid, ganget med forholdstallet " + prorataBroek.formatBroek() + ". Beløpet fordeles på 12 utbetalinger i året." },
                    nynorsk { +"Barnepensjonen per år er rekna ut til " + barnepensjonssats() + " G gonga med " + aarTrygdetid.format() + "/40 trygdetid, gonga med forholdstalet " + prorataBroek.formatBroek() + ". Beløpet blir fordelt på 12 utbetalingar i året." },
                    english { +"The children's pension per year is calculated at " + barnepensjonssats() + "G x " + aarTrygdetid.format() + "/40 contribution time, multiplied by the proportional fraction " + prorataBroek.formatBroek() + ". This amount is distributed in 12 payments a year." },
                )
            }
        }.orShow {
            paragraph {
                text(
                    bokmal { +"Før 1. januar 2024" },
                    nynorsk { +"Før 1. januar 2024" },
                    english { +"Before 1 January 2024" },
                    FontType.BOLD
                )
            }
            paragraph {
                text(
                    bokmal { +"Barnepensjonen per år er beregnet til 0,4 G ganget med " + aarTrygdetid.format() + "/40 trygdetid, ganget med forholdstallet " + prorataBroek.formatBroek() + ". Beløpet fordeles på 12 utbetalinger i året." },
                    nynorsk { +"Barnepensjonen per år er rekna ut til 0,4 G gonga med " + aarTrygdetid.format() + "/40 trygdetid, gonga med forholdstalet " + prorataBroek.formatBroek() + ". Beløpet blir fordelt på 12 utbetalingar i året." },
                    english { +"The children's pension per year is calculated at 0,4 G x " + aarTrygdetid.format() + "/40 contribution time, multiplied by the proportional fraction " + prorataBroek.formatBroek() + ". This amount is distributed in 12 payments a year." },
                )
            }
            paragraph {
                text(
                    bokmal { +"Fra 1. januar 2024" },
                    nynorsk { +"Frå og med 1. januar 2024" },
                    english { +"From 1 January 2024" },
                    FontType.BOLD
                )
            }
            paragraph {
                text(
                    bokmal { +"Barnepensjonen per år er beregnet til " + barnepensjonssats() + " G ganget med " + aarTrygdetid.format() + "/40 trygdetid, ganget med forholdstallet " + prorataBroek.formatBroek() + ". Beløpet fordeles på 12 utbetalinger i året." },
                    nynorsk { +"Barnepensjonen per år er rekna ut til " + barnepensjonssats() + " G gonga med " + aarTrygdetid.format() + "/40 trygdetid, gonga med forholdstalet " + prorataBroek.formatBroek() + ". Beløpet blir fordelt på 12 utbetalingar i året." },
                    english { +"The children's pension per year is calculated at " + barnepensjonssats() + "G x " + aarTrygdetid.format() + "/40 contribution time, multiplied by the proportional fraction " + prorataBroek.formatBroek() + ". This amount is distributed in 12 payments a year." },
                )
            }
        }
    }.orShowIf(beregningsMetodeAnvendt.equalTo(BeregningsMetode.NASJONAL)) {
        showIf(antallBarn.greaterThan(1)) {
            paragraph {
                text(
                    bokmal { +"Før 1. januar 2024" },
                    nynorsk { +"Før 1. januar 2024" },
                    english { +"Before 1 January 2024" },
                    FontType.BOLD
                )
            }
            paragraph {
                text(
                    bokmal { +"Barnepensjonen per år er beregnet ut fra at det er "  + antallBarn.format() + " søsken som oppdras sammen, med 0,4 G til første barn, og 0,25 G til påfølgende barn. Beløpet fordeles likt på hvert barn, og blir ganget med " + aarTrygdetid.format() + "/40 trygdetid.  Beløpet fordeles på 12 utbetalinger i året." },
                    nynorsk { +"Utrekninga av barnepensjonen tek utgangspunkt i at det er "  + antallBarn.format() + " søsken som blir oppdregne saman, med 0,4 G til første barn, og 0,25 G til påfølgjande barn. Beløpet blir fordelt likt på kvart barn, og blir gonga med " + aarTrygdetid.format() + "/40 trygdetid.  Beløpet blir fordelt på 12 utbetalingar i året." },
                    english { +"The children's pension per year is calculated according to the assumption that there are "  + antallBarn.format() + " siblings being raised together, with 0,4G going to the first child, and 0,25G to subsequent children. This amount is distributed equally to each child and multiplied by " + aarTrygdetid.format() + "/40 contribution time.  This amount is distributed in 12 payments a year. " },
                )
            }
            paragraph {
                text(
                    bokmal { +"Fra 1. januar 2024" },
                    nynorsk { +"Frå og med 1. januar 2024" },
                    english { +"From 1 January 2024" },
                    FontType.BOLD
                )
            }
            paragraph {
                text(
                    bokmal { +"Barnepensjonen per år er beregnet til " + barnepensjonssats() + " G ganget med "  + aarTrygdetid.format() + "/40 trygdetid. Beløpet fordeles på 12 utbetalinger i året." },
                    nynorsk { +"Barnepensjonen per år er rekna ut til " + barnepensjonssats() + " G gonga med " + aarTrygdetid.format() + "/40 trygdetid. Beløpet blir fordelt på 12 utbetalingar i året." },
                    english { +"The children's pension per year is calculated at " + barnepensjonssats() + "G x " + aarTrygdetid.format() + "/40 contribution time. This amount is distributed in 12 payments a year." },
                )
            }
        }.orShow {
            paragraph {
                text(
                    bokmal { +"Før 1. januar 2024" },
                    nynorsk { +"Før 1. januar 2024" },
                    english { +"Before 1 January 2024" },
                    FontType.BOLD
                )
            }
            paragraph {
                text(
                    bokmal { +"Barnepensjonen per år er beregnet til 0,4 G ganget med "  + aarTrygdetid.format() + "/40 trygdetid. Beløpet fordeles på 12 utbetalinger i året." },
                    nynorsk { +"Barnepensjonen per år er rekna ut til 0,4 G gonga med " + aarTrygdetid.format() + "/40 trygdetid. Beløpet blir fordelt på 12 utbetalingar i året." },
                    english { +"The children's pension per year is calculated at 0,4 G x " + aarTrygdetid.format() + "/40 contribution time. This amount is distributed in 12 payments a year." },
                )
            }
            paragraph {
                text(
                    bokmal { +"Fra 1. januar 2024" },
                    nynorsk { +"Frå og med 1. januar 2024" },
                    english { +"From 1 January 2024" },
                    FontType.BOLD
                )
            }
            paragraph {
                text(
                    bokmal { +"Barnepensjonen per år er beregnet til " + barnepensjonssats() + " G ganget med "  + aarTrygdetid.format() + "/40 trygdetid. Beløpet fordeles på 12 utbetalinger i året." },
                    nynorsk { +"Barnepensjonen per år er rekna ut til " + barnepensjonssats() + " G gonga med " + aarTrygdetid.format() + "/40 trygdetid. Beløpet blir fordelt på 12 utbetalingar i året." },
                    english { +"The children's pension per year is calculated at " + barnepensjonssats() + "G x " + aarTrygdetid.format() + "/40 contribution time. This amount is distributed in 12 payments a year." },
                )
            }
        }
    }

    includePhrase(Beregningsperiodetabell(beregningsperioder))
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, BarnepensjonBeregning>.perioderMedRegistrertTrygdetid(
    navnAvdoed: Expression<String>,
    trygdetidsperioder: Expression<List<Trygdetidsperiode>>,
    beregningsMetodeAnvendt: Expression<BeregningsMetode>
) {
    title2 {
        text(
            bokmal { +"Perioder med registrert trygdetid for " + navnAvdoed },
            nynorsk { +"Periodar med registrert trygdetid for " + navnAvdoed },
            english { +"Periods of registered contribution time for " + navnAvdoed },
        )
    }

    showIf(beregningsMetodeAnvendt.equalTo(BeregningsMetode.NASJONAL)){
        paragraph {
            text(
                bokmal { +"Tabellen viser perioder avdøde har vært medlem av folketrygden, og registrert fremtidig trygdetid." },
                nynorsk { +"Tabellen viser periodar avdøde har vore medlem av folketrygda, og registrert framtidig trygdetid." },
                english { +"The table shows the periods in which the deceased was a member of the National Insurance Scheme, and registered future contribution time." },
            )
        }
        includePhrase(Trygdetidstabell(trygdetidsperioder))
    }.orShowIf(beregningsMetodeAnvendt.equalTo(BeregningsMetode.PRORATA)) {
        paragraph {
            text(
                bokmal { +"Tabellen viser perioder avdøde har vært medlem av folketrygden og medlemsperioder avdøde har hatt i land som Norge har trygdeavtale med, som er tatt med i beregningen." },
                nynorsk { +"Tabellen viser periodar avdøde har vore medlem av folketrygda, og medlemsperioder avdøde har hatt i land som Noreg har trygdeavtale med, som er tekne med i utrekninga." },
                english { +"The table shows periods in which the deceased was a member of the National Insurance Scheme and member periods which the deceased contributed in countries which Norway had a national insurance agreement which are included in the calculation." },
            )
        }
        includePhrase(Trygdetidstabell(trygdetidsperioder))
    }
}

private fun OutlineOnlyScope<LanguageSupport.Triple<Bokmal, Nynorsk, English>, BarnepensjonBeregning>.meldFraTilNav() {
    paragraph {
        text(
            bokmal { +"Hvis du mener at opplysningene brukt i beregningen er feil, må du melde fra til Nav. Det kan ha betydning for størrelsen på pensjonen din." },
            nynorsk { +"Sei frå til Nav dersom du meiner at det er brukt feil opplysningar i utrekninga. Det kan ha betydning for kor mykje pensjon du får." },
            english { +"If you believe the information applied in the calculation is incorrect, you must notify Nav. Errors may affect your pension amount." }
        )
    }
}