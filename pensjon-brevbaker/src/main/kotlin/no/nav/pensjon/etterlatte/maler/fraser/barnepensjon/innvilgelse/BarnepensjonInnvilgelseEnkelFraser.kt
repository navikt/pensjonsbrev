package no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.innvilgelse

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

object BarnepensjonInnvilgelseEnkelFraser {

    data class Foerstegangsbehandlingsvedtak(
        val virkningsdato: Expression<LocalDate>,
        val avdoedNavn: Expression<String>,
        val doedsdato: Expression<LocalDate>,
        val beloep: Expression<Kroner>,
        val vedtaksdato: Expression<LocalDate>,
        val erEtterbetaling: Expression<Boolean>,
    ) :
        OutlinePhrase<LangBokmalNynorskEnglish>() {
        override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
            paragraph {
                val formatertVirkningsdato = virkningsdato.format()
                val formatertDoedsdato = doedsdato.format()
                val formatertVedtaksdato = vedtaksdato.format()
                textExpr(
                    Bokmal to "Du er innvilget barnepensjon fra ".expr() + formatertVirkningsdato +
                            " fordi " + avdoedNavn + " er registrert død " + formatertDoedsdato + ". " +
                            "Du får " + beloep.format() + " kroner hver måned før skatt fra " + formatertVedtaksdato + ". " +
                            "Barnepensjonen utbetales til og med den kalendermåneden du fyller 18 år.",
                    Nynorsk to "Du har fått innvilga barnepensjon frå ".expr() + formatertVirkningsdato +
                            "fordi " + avdoedNavn + " er registrert død " + formatertDoedsdato +". " +
                            "Frå og med " + formatertVedtaksdato + " får du " + beloep.format() + "kroner per månad før skatt.",
                    English to "You have been granted a children's pension ".expr() + formatertVirkningsdato +
                            " because " + avdoedNavn + " is registered as deceased on " + formatertDoedsdato + ". " +
                            "You will receive NOK " + beloep.format() + " each month before tax as of " + formatertVedtaksdato,
                )
            }
            paragraph {
                text(
                    Bokmal to "Barnepensjon gis når du er medlem i folketrygden " +
                            "og når avdøde i de siste fem årene før dødsfallet var medlem i folketrygden " +
                            "eller fikk pensjon fra folketrygden.",
                    Nynorsk to "Barnepensjonen blir utbetalt til og med den kalendermånaden du fyller 18 år." +
                            "Barnepensjon blir gitt når du er medlem i folketrygda og avdøde var medlem i eller fekk pensjon frå folketrygda dei siste fem åra før dødsfallet.",
                    English to "The children’s pension is paid up to and including the calendar month in which you turn 18." +
                            "You are eligible for a children's pension because you are a member of " +
                            "the Norwegian National Insurance Scheme, and the deceased has been a member of " +
                            "the National Insurance Scheme in the five years prior to the death or he/she " +
                            "has been receiving a pension from the Scheme. ",
                )
            }
            showIf(erEtterbetaling) {
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter bestemmelsene om barnepensjon i folketrygdloven §§ <sett inn paragrafer fra tekstbiblioteket her>.",
                        Nynorsk to "Vedtaket er gjort i tråd med føresegnene om barnepensjon i folketrygdlova §§ <sett inn paragrafer fra tekstbiblioteket her>.",
                        English to "The decision was made in accordance with the provisions for children's pensions as stated in the National Insurance Act, in sections <sett inn paragrafer fra tekstbiblioteket her>.",
                    )
                }
            }.orShow {
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter bestemmelsene om barnepensjon i folketrygdloven §§ 18-2, 18-3, 18-4, 18-5 og 22-12.",
                        Nynorsk to "Vedtaket er gjort i tråd med føresegnene om barnepensjon i folketrygdlova §§ 18-2, 18-3, 18-4, 18-5 og 22-12.",
                        English to "The decision was made in accordance with the provisions for children's pensions as stated in the National Insurance Act, in sections 18-2, 18-3, 18-4, 18-5 and 22-12.",
                    )
                }
            }
        }
    }
}
