package no.nav.pensjon.brev.maler.fraser.ufoer

import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.expression.*
import java.time.LocalDate
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.textExpr

// TBU3020
data class InnledningVarselSaksbehandlingstid(
    val mottattDato: Expression<LocalDate>
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            textExpr(
                Bokmal to "Vi viser til søknaden din om uføretrygd som vi mottok ".expr() + mottattDato.format() + ".",
                Nynorsk to "Vi viser til søknaden din om uføretrygd som vi tok imot ".expr() + mottattDato.format() + ".",
                English to "We refer to your application for disability benefit that we received ".expr() + mottattDato.format() + "."
            )
        }
    }
}

// TBU3015
data class SaksbehandlingstidUfore(
    val utvidetBehandlingstid: Expression<Boolean>
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        paragraph {
            textExpr(
                Bokmal to "Søknaden din blir behandlet så snart som mulig, og senest innen ".expr()
                        + ifElse(utvidetBehandlingstid, ifFalse = "4", ifTrue = "12") + " måneder. "
                        + "Blir ikke saken din ferdigbehandlet innen denne fristen, vil vi gi deg beskjed om ny svartid.",
                Nynorsk to "Søknaden din vert handsama så snart som mogleg, og seinast innan ".expr()
                        + ifElse(utvidetBehandlingstid, ifFalse = "4", ifTrue = "12") + " 4 månader. "
                        + "Vert ikkje saka di handsama innan denne fristen, vil vi gje deg melding om ny svarfrist.",
                        English to "Your application will be processed as soon as possible, and no later than within ".expr()
                + ifElse(utvidetBehandlingstid, ifFalse = "4", ifTrue = "12") + " months . "
                + "If your case is not processed within this deadline, we will notify you of a new response time."
            )
        }
    }
}