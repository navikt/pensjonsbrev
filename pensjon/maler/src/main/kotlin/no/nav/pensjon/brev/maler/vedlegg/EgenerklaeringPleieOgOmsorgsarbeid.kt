package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.maler.redigerbar.OmsorgEgenManuellDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.selectors.omsorgEgenManuellDto.pesysData.*
import no.nav.pensjon.brev.api.model.maler.redigerbar.selectors.omsorgEgenManuellDto.saksbehandlerValg.*
import no.nav.pensjon.brev.api.model.maler.redigerbar.selectors.omsorgEgenManuellDto.*
import no.nav.pensjon.brev.api.model.vedlegg.EgenerklaeringOmsorgsarbeidDto
import no.nav.pensjon.brev.api.model.vedlegg.selectors.egenerklaeringOmsorgsarbeidDto.*
import no.nav.pensjon.brev.api.model.vedlegg.ReturAdresse
import no.nav.pensjon.brev.api.model.vedlegg.selectors.returAdresse.*
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.selectors.brevbakerFelles.*
import no.nav.pensjon.brevbaker.api.model.selectors.brevbakerFelles.navEnhet.*


@TemplateModelHelpers
val egenerklaeringPleieOgOmsorgsarbeid = createAttachment<LangBokmalNynorskEnglish, EgenerklaeringOmsorgsarbeidDto>(
    title = {
        text(
            bokmal { +"Egenerklæring om pleie- og omsorgsarbeid" },
            nynorsk { +"Eigenmelding om pleie- og omsorgsarbeid" },
            english { +"Personal declaration that nursing and care work has been provided" },
        )
    },
    includeSakspart = true
) {
    vedlegg(returadresse, aarEgenerklaringOmsorgspoeng.format())
}



@TemplateModelHelpers
val egenerklaeringPleieOgOmsorgsarbeidManuell = createAttachment<LangBokmalNynorskEnglish, OmsorgEgenManuellDto>(
    title = {
        text(
            bokmal { +"Egenerklæring om pleie- og omsorgsarbeid" },
            nynorsk { +"Eigenmelding om pleie- og omsorgsarbeid" },
            english { +"Personal declaration that nursing and care work has been provided" },
        )
    },
    includeSakspart = true
) {
    vedlegg(pesysData.returadresse, saksbehandlerValg.aarEgenerklaringOmsorgspoeng.format())
}

private fun OutlineOnlyScope<LangBokmalNynorskEnglish, *>.vedlegg(returadresse: Expression<ReturAdresse>, aarEgenerklaering: Expression<String>) {
    paragraph {
        val dokDato = felles.dokumentDato.format()
        text(
            bokmal { + "Jeg viser til brev av " + dokDato + "." },
            nynorsk { + "Eg viser til brev datert " + dokDato + "." },
            english { + "I refer to your letter dated " + dokDato + "." },
        )
    }

    paragraph {
        text(
            bokmal { + "I " + aarEgenerklaering + " har jeg utført pleie og omsorgsarbeid på minst 22 timer i uken. (Inkludert opptil en halv time reisetid per besøk.)" },
            nynorsk { + "I " + aarEgenerklaering + " har eg utført pleie- og omsorgsarbeid på minst 22 timar i veka. (Inkludert opptil ein halv time reisetid per besøk.)" },
            english { + "In " + aarEgenerklaering + " I have provided care work that has amounted to at least 22 hours per week. (Travelling time up to 30 minutes per visit may be included.)" },
        )
    }

    paragraph {
        text(
            bokmal { +"Navn på pleietrengende: ____________________________________________________________" },
            nynorsk { +"Namn på pleietrengande: ___________________________________________________________" },
            english { +"I have provided care work for: ___________________________________________________" },
        )
        newline()
        text(
            bokmal { +"Arbeidet har vart i:" },
            nynorsk { +"Arbeidet har vart i:" },
            english { +"The work has lasted for:" },
        )
        newline()
        text(bokmal { +"☐ minst seks måneder" }, nynorsk { +"☐ minst seks månader" }, english { +"☐ at least six months" })
        newline()
        text(bokmal { +"☐ under seks måneder" }, nynorsk { +"☐ under seks månader" }, english { +"☐ less than six months" })
        newline()
    }

    title2 {
        text(
            bokmal { + "Hvis omsorgsforholdet har opphørt i løpet av året:" },
            nynorsk { + "Om omsorgsforholdet er blitt avslutta under året:" },
            english { + "If care work has ceased during the year:" },
        )
    }
    paragraph {
        text(
            bokmal { +"Oppgi dato for opphøret: _________________________" },
            nynorsk { +"Dato for opphøyr: ________________________________" },
            english { +"State date if ceased: ____________________________" },
        )
        newline()
        text(
            bokmal { +"Oppgi årsaken til opphøret: ____________________________________________________________" },
            nynorsk { +"Grunnen til opphøyr: ___________________________________________________________________" },
            english { +"State reason if ceased: ________________________________________________________________" },
        )
        newline()
        text(bokmal { +"Dato: _________________________" }, nynorsk { +"Dato: _________________________" }, english { +"Date: _________________________" })
        newline()
        text(
            bokmal { +"Underskrift: ____________________________________________________________" },
            nynorsk { +"Underskrift: ____________________________________________________________" },
            english { +"Signature: ______________________________________________________________" },
        )
        newline()
    }

    paragraph {
        text(
            bokmal { + "Du må sende denne egenerklæringen til:" },
            nynorsk { + "Du må sende denne eigenmeldinga til:" },
            english { + "Please return the form to:" }
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
