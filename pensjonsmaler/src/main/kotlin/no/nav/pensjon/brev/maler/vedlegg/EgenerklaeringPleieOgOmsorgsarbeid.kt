package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.maler.redigerbar.OmsorgEgenManuellDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.OmsorgEgenManuellDtoSelectors.PesysDataSelectors.returadresse
import no.nav.pensjon.brev.api.model.maler.redigerbar.OmsorgEgenManuellDtoSelectors.SaksbehandlerValgSelectors.aarEgenerklaringOmsorgspoeng
import no.nav.pensjon.brev.api.model.maler.redigerbar.OmsorgEgenManuellDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.maler.redigerbar.OmsorgEgenManuellDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.api.model.vedlegg.EgenerklaeringOmsorgsarbeidDto
import no.nav.pensjon.brev.api.model.vedlegg.EgenerklaeringOmsorgsarbeidDtoSelectors.aarEgenerklaringOmsorgspoeng
import no.nav.pensjon.brev.api.model.vedlegg.EgenerklaeringOmsorgsarbeidDtoSelectors.returadresse
import no.nav.pensjon.brev.api.model.vedlegg.ReturAdresse
import no.nav.pensjon.brev.api.model.vedlegg.ReturAdresseSelectors.adresseLinje1
import no.nav.pensjon.brev.api.model.vedlegg.ReturAdresseSelectors.postNr
import no.nav.pensjon.brev.api.model.vedlegg.ReturAdresseSelectors.postSted
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Form.Text.Size
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brevbaker.api.model.FellesSelectors.avsenderEnhet
import no.nav.pensjon.brevbaker.api.model.FellesSelectors.dokumentDato
import no.nav.pensjon.brevbaker.api.model.NAVEnhetSelectors.navn


@TemplateModelHelpers
val egenerklaeringPleieOgOmsorgsarbeid = createAttachment<LangBokmalNynorskEnglish, EgenerklaeringOmsorgsarbeidDto>(
    title = newText(
        Bokmal to "Egenerklæring om pleie- og omsorgsarbeid",
        Nynorsk to "Eigenmelding om pleie- og omsorgsarbeid",
        English to "Personal declaration that nursing and care work has been provided",
    ),
    includeSakspart = true
) {
    vedlegg(returadresse) { aarEgenerklaringOmsorgspoeng.format() }
}



@TemplateModelHelpers
val egenerklaeringPleieOgOmsorgsarbeidManuell = createAttachment<LangBokmalNynorskEnglish, OmsorgEgenManuellDto>(
    title = newText(
        Bokmal to "Egenerklæring om pleie- og omsorgsarbeid",
        Nynorsk to "Eigenmelding om pleie- og omsorgsarbeid",
        English to "Personal declaration that nursing and care work has been provided",
    ),
    includeSakspart = true
) {
    vedlegg(pesysData.returadresse) { saksbehandlerValg.aarEgenerklaringOmsorgspoeng.format() }
}

private fun OutlineOnlyScope<LangBokmalNynorskEnglish, *>.vedlegg(returadresse: Expression<ReturAdresse>, aarEgenerklaering: (TextScope<*,*>) -> Expression<String>) {
    paragraph {
        val dokDato = felles.dokumentDato.format()
        textExpr(
            Bokmal to "Jeg viser til brev av ".expr() + dokDato + ".",
            Nynorsk to "Eg viser til brev datert ".expr() + dokDato + ".",
            English to "I refer to your letter dated ".expr() + dokDato + ".",
        )
    }

    paragraph {
        textExpr(
            Bokmal to "I ".expr() + aarEgenerklaering(this) + " år har jeg utført pleie og omsorgsarbeid på minst 22 timer i uken. (Inkludert opptil en halv time reisetid per besøk.)",
            Nynorsk to "I ".expr() + aarEgenerklaering(this) + " år har eg utført pleie- og omsorgsarbeid på minst 22 timar i veka. (Inkludert opptil ein halv time reisetid per besøk.)",
            English to "In ".expr() + aarEgenerklaering(this) + " years I have provided care work that has amounted to at least 22 hours per week. (Travelling time up to 30 minutes per visit may be included.)",
        )
    }

    paragraph {
        formText(
            Size.LONG, newText(
                Bokmal to "Navn på pleietrengende:",
                Nynorsk to "Navn på pleietrengende:",
                English to "I have provided care work for:",
            )
        )

        formChoice(
            newText(
                Bokmal to "Arbeidet har vart i:",
                Nynorsk to "Arbeidet har vart i:",
                English to "The work has lasted for:"
            )
        ) {
            choice(
                Bokmal to "minst seks måneder",
                Nynorsk to "minst seks månader",
                English to "at least six months"
            )
            choice(
                Bokmal to "under seks måneder",
                Nynorsk to "under seks månader",
                English to "less than six months"
            )
        }
    }

    title2 {
        text(
            Bokmal to "Hvis omsorgsforholdet har opphørt i løpet av året:",
            Nynorsk to "Om omsorgsforholdet er blitt avslutta under året:",
            English to "If care work has ceased during the year:",
        )
    }
    paragraph {
        formText(
            size = Size.SHORT,
            vspace = false,
            prompt = newText(
                Bokmal to "Oppgi dato for opphøret:",
                Nynorsk to "Dato for opphøyr:",
                English to "State date if ceased:"
            )
        )
        formText(
            size = Size.LONG,
            vspace = false,
            prompt = newText(
                Bokmal to "Oppgi årsaken til opphøret:",
                Nynorsk to "Grunnen til opphøyr: ",
                English to "State reason if ceased"
            )
        )

        formText(size = Size.SHORT, prompt = newText(Bokmal to "Dato:", Nynorsk to "Dato:", English to "Date"))
        formText(
            size = Size.LONG,
            vspace = false,
            prompt = newText(Bokmal to "Underskrift:", Nynorsk to "Underskrift:", English to "Signature:")
        )
    }

    paragraph {
        text(
            Bokmal to "Du må sende denne egenerklæringen til:",
            Nynorsk to "Du må sende denne eigenmeldinga til:",
            English to "Please return the form to:"
        )
        newline()

        eval(felles.avsenderEnhet.navn)
        newline()

        with(returadresse) {
            eval(adresseLinje1)
            newline()
            eval(postNr + " " + postSted)
        }
    }
}
