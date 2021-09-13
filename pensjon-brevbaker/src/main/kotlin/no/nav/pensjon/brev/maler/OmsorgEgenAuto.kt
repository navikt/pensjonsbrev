package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.something.Fagdelen
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.base.PensjonLatex
import no.nav.pensjon.brev.template.dsl.argument
import no.nav.pensjon.brev.template.dsl.format
import no.nav.pensjon.brev.template.dsl.select
import no.nav.pensjon.brev.template.dsl.str

object OmsorgEgenAuto : StaticTemplate {

    override val template: LetterTemplate<*> = createTemplate(
        name = "OMSORG_EGEN_AUTO",
        title = title(Language.Bokmal to "Du må sende oss egenerklæring om pleie- og omsorgsarbeid"),
        base = PensjonLatex,
        lang = languages(Language.Bokmal)
    ) {
        parameters {
            required { ArEgenerklaringOmsorgspoeng }
            required { Felles }
        }

        outline {
            paragraph {
                text(Language.Bokmal to "Vi trenger en bekreftelse på at du har utført pleie- og omsorgsarbeid i ")
                eval(argument(ArEgenerklaringOmsorgspoeng).str())
                text(Language.Bokmal to ". Derfor må du fylle ut det vedlagte skjemaet og sende det til oss innen fire uker.")
            }

            paragraph {
                text(Language.Bokmal to "Du har fått godkjent pensjonsopptjening for ")
                eval(argument(ArEgenerklaringOmsorgspoeng).str())
                text(Language.Bokmal to ".")
            }

//            paragraph {
//                text(Language.Bokmal to "Vi trenger en bekreftelse på at du har utført pleie- og omsorgsarbeid i ")
//                eval(argument(ArEgenerklaringOmsorgspoeng).str())
//                text(Language.Bokmal to ". Derfor må du fylle ut det vedlagte skjemaet og sende det til oss innen fire uker.")
//                text(Language.Bokmal to ". Derfor må du fylle ut det vedlagte skjemaet og sende det til oss innen fire uker.")
//                text(Language.Bokmal to ". Derfor må du fylle ut det vedlagte skjemaet og sende det til oss innen fire uker.")
//                text(Language.Bokmal to ". Derfor må du fylle ut det vedlagte skjemaet og sende det til oss innen fire uker.")
//                text(Language.Bokmal to ". Derfor må du fylle ut det vedlagte skjemaet og sende det til oss innen fire uker.")
//                text(Language.Bokmal to ". Derfor må du fylle ut det vedlagte skjemaet og sende det til oss innen fire uker.")
//                text(Language.Bokmal to ". Derfor må du fylle ut det vedlagte skjemaet og sende det til oss innen fire uker.")
//                text(Language.Bokmal to ". Derfor må du fylle ut det vedlagte skjemaet og sende det til oss innen fire uker.")
//                text(Language.Bokmal to ". Derfor må du fylle ut det vedlagte skjemaet og sende det til oss innen fire uker.")
//                text(Language.Bokmal to ". Derfor må du fylle ut det vedlagte skjemaet og sende det til oss innen fire uker.")
//                text(Language.Bokmal to ". Derfor må du fylle ut det vedlagte skjemaet og sende det til oss innen fire uker.")
//                text(Language.Bokmal to ". Derfor må du fylle ut det vedlagte skjemaet og sende det til oss innen fire uker.")
//                text(Language.Bokmal to ". Derfor må du fylle ut det vedlagte skjemaet og sende det til oss innen fire uker.")
//            }
//
//            paragraph {
//                text(Language.Bokmal to "Vi trenger en bekreftelse på at du har utført pleie- og omsorgsarbeid i ")
//                eval(argument(ArEgenerklaringOmsorgspoeng).str())
//                text(Language.Bokmal to ". Derfor må du fylle ut det vedlagte skjemaet og sende det til oss innen fire uker.")
//                text(Language.Bokmal to ". Derfor må du fylle ut det vedlagte skjemaet og sende det til oss innen fire uker.")
//                text(Language.Bokmal to ". Derfor må du fylle ut det vedlagte skjemaet og sende det til oss innen fire uker.")
//                text(Language.Bokmal to ". Derfor må du fylle ut det vedlagte skjemaet og sende det til oss innen fire uker.")
//                text(Language.Bokmal to ". Derfor må du fylle ut det vedlagte skjemaet og sende det til oss innen fire uker.")
//                text(Language.Bokmal to ". Derfor må du fylle ut det vedlagte skjemaet og sende det til oss innen fire uker.")
//                text(Language.Bokmal to ". Derfor må du fylle ut det vedlagte skjemaet og sende det til oss innen fire uker.")
//                text(Language.Bokmal to ". Derfor må du fylle ut det vedlagte skjemaet og sende det til oss innen fire uker.")
//                text(Language.Bokmal to ". Derfor må du fylle ut det vedlagte skjemaet og sende det til oss innen fire uker.")
//                text(Language.Bokmal to ". Derfor må du fylle ut det vedlagte skjemaet og sende det til oss innen fire uker.")
//                text(Language.Bokmal to ". Derfor må du fylle ut det vedlagte skjemaet og sende det til oss innen fire uker.")
//                text(Language.Bokmal to ". Derfor må du fylle ut det vedlagte skjemaet og sende det til oss innen fire uker.")
//                text(Language.Bokmal to ". Derfor må du fylle ut det vedlagte skjemaet og sende det til oss innen fire uker.")
//            }


        }

        attachment(
            title = title(Language.Bokmal to "Egenerklæring om pleie- og omsorgsarbeid"),
            includeSakspart = true,
        ) {
            paragraph {
                text(Language.Bokmal to "Jeg viser til brev av ")
                eval(argument(Felles).select(Fagdelen.Felles::dokumentDato).format())
                text(Language.Bokmal to ".")
            }

            paragraph {
                text(Language.Bokmal to "I ")
                eval(argument(ArEgenerklaringOmsorgspoeng).str())
                text(Language.Bokmal to " har jeg utført pleie og omsorgsarbeid på minst 22 timer i uken. (Inkludert opptil en halv time reisetid per besøk.)")
            }

            paragraph {
                text(Language.Bokmal to "Navn på pleietrengende: ............................................................")
            }

            paragraph {
                text(Language.Bokmal to "Arbeidet har vart i (sett kryss):")
            }

            paragraph {
                text(Language.Bokmal to "Hvis omsorgsforholdet har opphørt i løpet av året:")
            }

            paragraph {
                text(Language.Bokmal to "Oppgi dato for opphøret: _________________________")
            }

            paragraph {
                text(Language.Bokmal to "Oppgi årsaken til opphøret: ______________________________________________________")
            }

            paragraph {
                text(Language.Bokmal to "Dato: _________________________")
            }

            paragraph {
                text(Language.Bokmal to "Underskrift: _________________________________________")
            }

            paragraph {
                text(Language.Bokmal to "Du må sende denne egenerklæringen til:")
            }

            val returAdresse = argument(Felles).select(Fagdelen.Felles::returAdresse)
            paragraph {
                eval(returAdresse.select(Fagdelen.ReturAdresse::navEnhetsNavn))
            }
            paragraph {
                eval(returAdresse.select(Fagdelen.ReturAdresse::adresseLinje1))
            }
            paragraph {
                eval(returAdresse.select(Fagdelen.ReturAdresse::postNr))
                text(Language.Bokmal to " ")
                eval(returAdresse.select(Fagdelen.ReturAdresse::postSted))

            }
        }

    }

}