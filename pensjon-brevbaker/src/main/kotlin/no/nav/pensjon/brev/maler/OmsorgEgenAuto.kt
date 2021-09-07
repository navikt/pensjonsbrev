package no.nav.pensjon.brev.maler

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.base.PensjonLatex
import no.nav.pensjon.brev.template.dsl.argument
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
        }

        attachment(
            title = title(Language.Bokmal to "Egenerklæring om pleie- og omsorgsarbeid"),
            includeSakspart = true,
        ) {
            paragraph {
                text(Language.Bokmal to "Jeg viser til brev av 2020")
//                eval(argument(Felles).str())
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
        }
    }

}