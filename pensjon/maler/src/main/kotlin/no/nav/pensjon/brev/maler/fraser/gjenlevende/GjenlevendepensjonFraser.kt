package no.nav.pensjon.brev.maler.fraser.gjenlevende

import no.nav.pensjon.brev.maler.SamletMeldingOmPensjonsvedtak.fritekst
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish

import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text


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