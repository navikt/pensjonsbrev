package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.model.Felles
import no.nav.pensjon.brev.api.model.NAVEnhet
import no.nav.pensjon.brev.api.model.ReturAdresse
import no.nav.pensjon.brev.api.model.maler.OmsorgEgenAutoDto
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.base.PensjonLatex
import no.nav.pensjon.brev.template.dsl.*

object OmsorgEgenAuto : StaticTemplate {

    override val template: LetterTemplate<*, *> = createTemplate(
        name = "OMSORG_EGEN_AUTO",
        base = PensjonLatex,
        letterDataType = OmsorgEgenAutoDto::class,
        lang = languages(Bokmal, Nynorsk, English),
        title = newText(
            Bokmal to "Du må sende oss egenerklæring om pleie- og omsorgsarbeid",
            Nynorsk to "Du må sende oss eigenmelding om pleie- og omsorgsarbeid",
            English to "Personal declaration about the circumstances of care",
        )
    ) {

        val arEgenerklaring = argument().select(OmsorgEgenAutoDto::arEgenerklaringOmsorgspoeng).str()

        outline {
            paragraph {
                textExpr(
                    Bokmal to
                            "Vi trenger en bekreftelse på at du har utført pleie- og omsorgsarbeid i ".expr()
                            + arEgenerklaring
                            + ". Derfor må du fylle ut det vedlagte skjemaet og sende det til oss innen fire uker.",

                    Nynorsk to
                            "Vi treng ei stadfesting på at du har utført pleie- og omsorgsarbeid i ".expr()
                            + arEgenerklaring
                            + ". Du må difor nytte det vedlagde skjemaet og sende til oss innan fire veker.",

                    English to
                            "We need you to confirm that you have provided nursing and care work in ".expr()
                            + arEgenerklaring
                            + ". Therefore, it is required that you complete the enclosed form and return it to NAV within four weeks."
                )
            }

            paragraph {
                val arInnvilget = argument().select(OmsorgEgenAutoDto::arInnvilgetOmsorgspoeng).str()
                textExpr(
                    Bokmal to "Du har fått godkjent pensjonsopptjening for ".expr() + arInnvilget + ".",
                    Nynorsk to "Du har fått godkjend pensjonsopptening for ".expr() + arInnvilget + ".",
                    English to "You have accumulated pensionable earnings for ".expr() + arInnvilget + "."
                )
            }

        }

        attachment(
            title = newText(
                Bokmal to "Egenerklæring om pleie- og omsorgsarbeid",
                Nynorsk to "Eigenmelding om pleie- og omsorgsarbeid",
                English to "Personal declaration that nursing and care work has been provided",
            ),
            includeSakspart = true,
        ) {
            paragraph {
                val dokDato = felles().select(Felles::dokumentDato).format()
                textExpr(
                    Bokmal to "Jeg viser til brev av ".expr() + dokDato + ".",
                    Nynorsk to "Eg viser til brev datert ".expr() + dokDato + ".",
                    English to "I refer to your letter dated ".expr() + dokDato + ".",
                )
            }

            paragraph {
                textExpr(
                    Bokmal to "I ".expr() + arEgenerklaring + " har jeg utført pleie og omsorgsarbeid på minst 22 timer i uken. (Inkludert opptil en halv time reisetid per besøk.)",
                    Nynorsk to "I ".expr() + arEgenerklaring + " har eg utført pleie- og omsorgsarbeid på minst 22 timar i veka. (Inkludert opptil ein halv time reisetid per besøk.)",
                    English to "In ".expr() + arEgenerklaring + " I have provided care work that has amounted to at least 22 hours per week. (Travelling time up to 30 minutes per visit may be included.)",
                )
            }

            formText(60, newText(
                Bokmal to "Navn på pleietrengende:",
                Nynorsk to "Navn på pleietrengende:",
                English to "I have provided care work for:",
            ))

            formChoice(newText(
                Bokmal to "Arbeidet har vart i (sett kryss):",
                Nynorsk to "Arbeidet har vart i (set kryss):",
                English to "The work has lasted for (insert X):"
            )) {
                choice(Bokmal to "minst seks måneder", Nynorsk to "minst seks månader", English to "at least six months")
                choice(Bokmal to "under seks måneder", Nynorsk to "under seks månader", English to "less than six months")
            }

            formText(size = 0, prompt = newText(
                Bokmal to "Hvis omsorgsforholdet har opphørt i løpet av året:",
                Nynorsk to "Om omsorgsforholdet er blitt avslutta under året:",
                English to "If care work has ceased during the year:",
            ))
            formText(size = 25, vspace = false, prompt = newText(Bokmal to "Oppgi dato for opphøret:", Nynorsk to "Dato for opphøyr:", English to "State date if ceased:"))
            formText(size = 55, vspace = false, prompt = newText(Bokmal to "Oppgi årsaken til opphøret:", Nynorsk to "Grunnen til opphøyr: ", English to "State reason if ceased"))

            repeat(4) { newline() }

            formText(size = 25, prompt = newText(Bokmal to "Dato:", Nynorsk to "Dato:", English to "Date"))
            formText(size = 55, vspace = false, prompt = newText(Bokmal to "Underskrift:", Nynorsk to "Underskrift:", English to "Signature:"))

            repeat(3) { newline() }

            paragraph {
                text(Bokmal to "Du må sende denne egenerklæringen til:", Nynorsk to "Du må sende denne eigenmeldinga til:", English to "Please return the form to:")
                newline()

                val avsender = felles().select(Felles::avsenderEnhet)
                eval { avsender.select(NAVEnhet::navn) }
                newline()

                val returAdresse = avsender.select(NAVEnhet::returAdresse)
                eval { returAdresse.select(ReturAdresse::adresseLinje1) }
                newline()

                eval(returAdresse.select(ReturAdresse::postNr) + " " + returAdresse.select(ReturAdresse::postSted))
            }
        }

    }

}