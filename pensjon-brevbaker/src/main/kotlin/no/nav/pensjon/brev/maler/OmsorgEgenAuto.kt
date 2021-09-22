package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.api.dto.Felles
import no.nav.pensjon.brev.api.dto.NAVEnhet
import no.nav.pensjon.brev.api.dto.ReturAdresse
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.base.PensjonLatex
import no.nav.pensjon.brev.template.dsl.*

data class OmsorgEgenAutoDto(val arEgenerklaringOmsorgspoeng: Number, val arInnvilgetOmsorgspoeng: Number)

object OmsorgEgenAuto : StaticTemplate {

    override val template: LetterTemplate<*, *> = createTemplate(
        name = "OMSORG_EGEN_AUTO",
        base = PensjonLatex,
        parameterType = OmsorgEgenAutoDto::class,
        lang = languages(Language.Bokmal),
        title = newText(Language.Bokmal to "Du må sende oss egenerklæring om pleie- og omsorgsarbeid")
    ) {

        val arEgenerklaring = argument().select(OmsorgEgenAutoDto::arEgenerklaringOmsorgspoeng).str()

        outline {
            paragraph {
                textExpr(
                    Language.Bokmal to
                            "Vi trenger en bekreftelse på at du har utført pleie- og omsorgsarbeid i ".expr()
                            + arEgenerklaring
                            + ". Derfor må du fylle ut det vedlagte skjemaet og sende det til oss innen fire uker."
                )
            }

            paragraph {
                textExpr(
                    Language.Bokmal to
                            "Du har fått godkjent pensjonsopptjening for ".expr()
                            + argument().select(OmsorgEgenAutoDto::arInnvilgetOmsorgspoeng).str()
                            + "."
                )
            }

        }

        attachment(
            title = newText(Language.Bokmal to "Egenerklæring om pleie- og omsorgsarbeid"),
            includeSakspart = true,
        ) {
            paragraph {
                val dokDato = felles().select(Felles::dokumentDato).format()
                textExpr(
                    Language.Bokmal to "Jeg viser til brev av ".expr() + dokDato + "."
                )
            }

            paragraph {
                textExpr(
                    Language.Bokmal to
                            "I ".expr() + arEgenerklaring + " har jeg utført pleie og omsorgsarbeid på minst 22 timer i uken. (Inkludert opptil en halv time reisetid per besøk.)"
                )
            }

            formText(60, newText(Language.Bokmal to "Navn på pleietrengende:"))

            formChoice(newText(Language.Bokmal to "Arbeidet har vart i (sett kryss):")) {
                choice(Language.Bokmal to "minst seks måneder")
                choice(Language.Bokmal to "under seks måneder")
            }

            formText(size = 0, prompt = newText(Language.Bokmal to "Hvis omsorgsforholdet har opphørt i løpet av året:"))
            formText(size = 25, vspace = false, prompt = newText(Language.Bokmal to "Oppgi dato for opphøret:"))
            formText(size = 55, vspace = false, prompt = newText(Language.Bokmal to "Oppgi årsaken til opphøret:"))

            repeat(4) { newline() }

            formText(size = 25, prompt = newText(Language.Bokmal to "Dato:"))
            formText(size = 55, vspace = false, prompt = newText(Language.Bokmal to "Underskrift:"))

            repeat(3) { newline() }

            paragraph {
                text(Language.Bokmal to "Du må sende denne egenerklæringen til:")
                newline()

                val avsender = felles().select(Felles::avsenderEnhet)
                eval { avsender.select(NAVEnhet::navn) }
                newline()

                val returAdresse = avsender.select(NAVEnhet::returAdresse)
                eval { returAdresse.select(ReturAdresse::adresseLinje1) }
                newline()
                textExpr(
                    Language.Bokmal to returAdresse.select(ReturAdresse::postNr) + " " + returAdresse.select(ReturAdresse::postSted)
                )
            }
        }

    }

}