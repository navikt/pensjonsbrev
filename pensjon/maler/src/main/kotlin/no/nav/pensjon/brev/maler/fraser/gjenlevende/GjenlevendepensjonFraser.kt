package no.nav.pensjon.brev.maler.fraser.gjenlevende

import no.nav.pensjon.brev.template.LangBokmalEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text


// ---- Begrunnelse for endringen (Saksbehandlervalg-fork) - PE_GP_04_020, -028, -029
object Inntektsoekning : OutlinePhrase<LangBokmalEnglish>() {
    override fun OutlineOnlyScope<LangBokmalEnglish, Unit>.template() {
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
    }
}

object Inntektsreduksjon : OutlinePhrase<LangBokmalEnglish>() {
    override fun OutlineOnlyScope<LangBokmalEnglish, Unit>.template() {
        paragraph {
            text(
                bokmal {
                    +"Pensjonen din blir regulert i forhold til den arbeidsinntekten du har eller forventes å ha." +
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
    }
}

object Samboer12av18Maaneder : OutlinePhrase<LangBokmalEnglish>() {
    override fun OutlineOnlyScope<LangBokmalEnglish, Unit>.template() {
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
    }
}

object AarsakTilEndringFritekster : OutlinePhrase<LangBokmalEnglish>() {
    override fun OutlineOnlyScope<LangBokmalEnglish, Unit>.template() {

    }

}