package no.nav.pensjon.brev.maler.fraser.gjenlevende

import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringGjenlevendepensjonBosattUtlandDtoSelectors.pesysData
import no.nav.pensjon.brev.maler.SamletMeldingOmPensjonsvedtak.fritekst
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Text.FontType.BOLD
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalEnglish
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner


// ---- Begrunnelse for endringen (Saksbehandlervalg-fork) - PE_GP_04_020, -028, -029
object Inntektsoekning : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                bokmal {
                    +"Pensjonen din blir regulert i forhold til den arbeidsinntekten du har eller forventes å ha." +
                            " Pensjonen din vil reduseres fordi du har hatt en økning i forventet arbeidsinntekt." +
                            " Reduksjonen av pensjonen trer i kraft fra og med måneden etter at arbeidsinntekten ble endret."
                },
                nynorsk {
                    +"Pensjonen din blir regulert i forhold til arbeidsinntekta du har eller kan forventast å ha. "
                    +"Pensjonen din blir redusert fordi du har hatt ein auke i forventa arbeidsinntekt. "
                    +"Pensjonen din er redusert frå månaden etter at endringa skjedde. "
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
                nynorsk { +"Vedtaket er gjort etter føresegnene i folketrygdlova kapittel 17." },
                english { +"This decision has been made in accordance with Chapter 17 of the National Insurance Act." },
            )
        }
    }
}

object Inntektsreduksjon : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                bokmal {
                    +"Pensjonen din blir regulert i forhold til den arbeidsinntekten du har eller forventes å ha." +
                            " Pensjonen din vil øke fordi du har hatt en reduksjon i forventet arbeidsinntekt." +
                            " Økning av pensjonen trer i kraft fra og med den måneden arbeidsinntekten din ble endret."
                },
                nynorsk {
                    +"Pensjonen din blir regulert i forhold til arbeidsinntekta du har eller kan forventast å ha. "
                    +"Pensjonen din aukar fordi du har hatt ein reduksjon i forventa arbeidsinntekt. "
                    +"Pensjonen din aukar frå og med den månaden arbeidsinntekta di blei endra. "
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
                nynorsk { +"Vedtaket er gjort etter føresegnene i folketrygdlova kapittel 17." },
                english { +"This decision has been made in accordance with Chapter 17 of the National Insurance Act." },
            )
        }
    }
}

object Samboer12av18Maaneder : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                bokmal {
                    +"Pensjonen din er endret fordi du har vært samboer i 12 av de siste 18 månedene," +
                            " og fordi samboeren din har en inntekt som overstiger to ganger grunnbeløpet" +
                            " eller mottar pensjon eller uføretrygd fra folketrygden."
                },
                nynorsk {
                    +"Pensjonen din er endra fordi du har vore sambuar i 12 av dei siste 18 månadene, "
                    +"og fordi sambuaren din har ei inntekt som overstig to gonger grunnbeløpet eller mottek pensjon eller uføretrygd frå folketrygda. "
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
                bokmal { +"Pensjonen vil bli redusert fra måneden etter at dere har vært samboere i 12 av de 18 siste månedene." },
                nynorsk { +"Pensjonen blir redusert frå månaden etter at de har vore sambuarar i 12 av dei 18 siste månadene." },
                english { +"Your pension will be reduced as of the month after you have been cohabiting for 12 of the past 18 months." },
            )
        }
        paragraph {
            text(
                bokmal { +"Vedtaket er gjort etter bestemmelsene i folketrygdloven kapittel 3." },
                nynorsk { +"Vedtaket er gjort etter føresegnene i folketrygdlova kapittel 3." },
                english { +"This decision has been made in accordance with Chapter 3 of the National Insurance Act." },
            )
        }
    }
}

object AarsakTilEndringFritekst : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
// Fritekst – andre årsaker som ikke passer i de tre faste alternativene.
        paragraph {
            text(
                bokmal { +fritekst("Beskriv årsaken til at pensjonen endres, og hvilken bestemmelse i folketrygdloven vedtaket er gjort etter.") },
                nynorsk { +fritekst("Beskriv årsaken til at pensjonen endres, og hvilken bestemmelse i folketrygdloven vedtaket er gjort etter.") },
                english { +fritekst("Beskriv årsaken til at pensjonen endres, og hvilken bestemmelse i folketrygdloven vedtaket er gjort etter.") },
            )
        }
    }
}

data class GrunnpensjonGP(val grunnbeloep: Expression<Kroner>) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(bokmal { +"Grunnpensjon" }, nynorsk { +"Grunnpensjon" }, english { +"The basic pension" }, BOLD)
            text(
                bokmal {
                    +" fastsettes med utgangspunkt i folketrygdens grunnbeløp, som for tiden er " + grunnbeloep.format() + ". "
                    +"For at du skal få full grunnpensjon må avdødes trygdetid være minst 40 år. "
                    +"Trygdetiden tilsvarer det antall år den avdøde har vært medlem i folketrygden etter fylte 16 år. "
                    +"Dersom avdøde var under 67 år på tidspunktet for dødsfallet, blir det beregnet framtidig trygdetid. "
                    +"Den regnes vanligvis fram til og med det året avdøde ville ha fylt 66 år. "
                    +"Ved mindre enn 40 års trygdetid blir grunnpensjonen tilsvarende redusert. "
                    +"Oversikt over trygdetiden er gitt i vedlegg til dette vedtaket."
                },
                nynorsk {
                    +"Grunnpensjon blir fastsett med utgangspunkt i grunnbeløpet i folketrygda, som for tida er " + grunnbeloep.format() + ". "
                    +"For at du skal få full grunnpensjon må trygdetida til den avdøde vere minst 40 år. "
                    +"Trygdetida svarer til det talet på år den avdøde har vore medlem i folketrygda etter fylte 16 år. "
                    +"Dersom den avdøde var under 67 år då dødsfallet skjedde, blir det rekna framtidig trygdetid. "
                    +"Denne blir vanlegvis rekna fram til og med det året den avdøde ville ha fylt 66 år. "
                    +"Ved mindre enn 40 års trygdetid blir grunnpensjonen tilsvarande redusert. "
                    +"Oversikt over trygdetida er gitt i vedlegg til dette vedtaket."
                },
                english {
                    +"The basic pension is calculated on the basis of the national insurance basic amount, which currently is " + grunnbeloep.format() + ". "
                    +"In order for you to receive a full basic pension, the deceased's period of national insurance cover must be at least 40 years. "
                    +"The period of national insurance cover is equivalent to the years the deceased had been a member of the National Insurance Scheme since turning 16. "
                    +"If the deceased was under 67 years of age at the time of death, credit is also given for their future period of national insurance cover. "
                    +"This period is usually calculated to include the years up to and including the year the deceased would have turned 66. "
                    +"If the deceased had less than 40 years of national insurance cover, the basic pension is reduced proportionately. "
                    +"An overview of the period of national insurance cover is enclosed with this decision."
                },
            )
        }
    }
}

object AvdodFlyktning : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                bokmal { +"Pensjonen er beregnet etter full trygdetid (40 år) fordi avdøde var flyktning." },
                nynorsk { +"" },
                english {
                    +"The pension has been calculated based on a full period of national insurance cover (40 years)" +
                            " because the deceased was a refugee."
                },
            )
        }
    }
}

object AvdoedDoedsfallSkyldesYrkesskade : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                bokmal { +"Når dødsfallet skyldes en yrkesskade blir ikke grunnpensjonen avkortet på grunn av manglende trygdetid." },
                nynorsk { +"Når dødsfallet skuldast ein yrkesskade, blir ikkje grunnpensjonen avkorta på grunn av manglande trygdetid." },
                english { +"If the death occurred as the result of a workplace injury, the basic pension is not reduced even if the deceased's period of national insurance cover was less than 40 years." },
            )
        }
    }
}

object AvdoedDoedsfallNotSkyldesYrkesskade : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                bokmal {
                    +"Når avdøde mottok en pensjon beregnet helt eller delvis etter regler som gjelder for yrkesskade, "
                    +"blir ikke denne delen av grunnpensjonen avkortet på grunn av manglende trygdetid."
                },
                nynorsk {
                    +"Når den avdøde mottok ein pensjon rekna heilt eller delvis etter reglar som gjeld for yrkesskade, "
                    +"blir ikkje denne delen av grunnpensjonen avkorta på grunn av manglande trygdetid."
                },
                english {
                    +"If the deceased received a pension based entirely or in part on the regulations for workplace injuries, "
                    +"this part of the basic pension is not reduced even if the deceased's period of national insurance cover was less than 40 years."
                },
            )
        }
    }
}

object GrunnpensjonJustertTil90ProsentPgaEgenPensjon : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                bokmal { +"Grunnpensjonen er justert til 90 prosent av beløpet fordi din samboer mottar pensjon eller uføretrygd fra folketrygden." },
                nynorsk { +"Grunnpensjonen er justert til 90 prosent av beløpet fordi sambuaren din mottek pensjon eller uføretrygd frå folketrygda." },
                english { +"The basic pension has been adjusted to 90 per cent of the total amount, because your cohabitant receives a national insurance pension." },
            )
        }
    }
}

object GrunnpensjonJustertTil90ProsentPgaEktefelleInntekt : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                bokmal { +"Grunnpensjonen er justert til 90 prosent av beløpet fordi din samboer har en inntekt som overstiger to ganger grunnbeløpet." },
                nynorsk { +"Grunnpensjonen er justert til 90 prosent av beløpet fordi sambuaren din har ei inntekt som overstig to gonger grunnbeløpet." },
                english { +"The basic pension has been adjusted to 90 per cent of the total amount, because your cohabitant has an income that exceeds twice the national insurance basic amount." },
            )
        }
    }
}

object Tilleggspensjon : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                bokmal { +"Tilleggspensjonen" },
                nynorsk { +"Tilleggspensjon" },
                english { +"Your supplementary pension" },
                BOLD
            )
            text(
                bokmal {
                    +" avhenger av antall år med pensjonspoeng avdøde har opptjent og størrelsen på pensjonspoengene. "
                    +"Det gis pensjonspoeng for år med inntekt over folketrygdens grunnbeløp. "
                    +"Det kreves 40 år med pensjonspoeng for å få full tilleggspensjon. "
                    +"På visse vilkår kan det medregnes framtidige poengår fra dødsåret til og med det året avdøde ville ha fylt 66 år. "
                    +"Tilleggspensjonen din utgjør 55 prosent av den tilleggspensjonen avdøde hadde opptjent rett til. "
                    +"Oversikt over poengopptjeningen er gitt i vedlegg til dette vedtaket."
                },
                nynorsk {
                    +" er avhengig av talet på år med pensjonspoeng som den avdøde har opptent, og storleiken på pensjonspoenga. "
                    +"Det blir gitt pensjonspoeng for år med inntekt over grunnbeløpet i folketrygda. "
                    +"Det krevst 40 år med pensjonspoeng for å få full tilleggspensjon. "
                    +"På visse vilkår kan det medreknast framtidige poengår frå dødsåret til og med det året den avdøde ville ha fylt 66 år. "
                    +"Tilleggspensjonen din utgjer 55 prosent av den tilleggspensjonen den avdøde hadde opptent rett til. "
                    +"Oversikt over poengopptjeninga er gitt i vedlegg til dette vedtaket. "
                },
                english {
                    +" depends on the number of years the deceased earned pension points and on how many pension points were earned. "
                    +"You receive pension points for years in which you have an income greater than the national insurance basic amount. "
                    +"40 years of pension points are required to receive a full supplementary pension. "
                    +"In some cases credit may be given for future years of earning pension points. "
                    +"The future years of earning pension points usually include the years between death and up to and including the year the deceased would have turned 66. "
                    +"Your supplementary pension is 55 per cent of the supplementary pension the deceased had accumulated. "
                    +"An overview of the accumulated points is enclosed with this decision."
                },
            )
        }
    }
}

object AvdoedDoedsfallSkyldesYrkesskadeBeregningAvTilleggspensjon : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            text(
                bokmal { +"Når dødsfallet skyldes en yrkesskade gjelder det spesielle regler for beregning av tilleggspensjon." },
                nynorsk { +"Når dødsfallet skuldast ein yrkesskade, gjeld det spesielle reglar for berekning av tilleggspensjon." },
                english { +"When the death is caused by a workplace injury, particular regulations for the calculation of supplementary pensions apply." },
            )
        }
    }
}

