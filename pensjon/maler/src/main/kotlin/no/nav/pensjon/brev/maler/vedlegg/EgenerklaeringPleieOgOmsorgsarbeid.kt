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
import no.nav.pensjon.brev.template.Element.OutlineContent.ParagraphContent.Form.Text.Size
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.choice
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.BrevFellesSelectors.FellesSelectors.avsenderEnhet
import no.nav.pensjon.brevbaker.api.model.BrevFellesSelectors.FellesSelectors.dokumentDato
import no.nav.pensjon.brevbaker.api.model.BrevFellesSelectors.NavEnhetSelectors.navn


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
        formText(
            Size.LONG, { text(
                bokmal { +"Navn på pleietrengende:" },
                nynorsk { +"Namn på pleietrengande:" },
                english { +"I have provided care work for:" }
            ) }
        )

        formChoice(
            { text(
                bokmal { +"Arbeidet har vart i:" },
                nynorsk { +"Arbeidet har vart i:" },
                english { +"The work has lasted for:" }
            ) }
        ) {
            choice(
                bokmal { +"minst seks måneder" },
                nynorsk { +"minst seks månader" },
                english { +"at least six months" }
            )
            choice(
                bokmal { +"under seks måneder" },
                nynorsk { +"under seks månader" },
                english { +"less than six months" }
            )
        }
    }

    title2 {
        text(
            bokmal { + "Hvis omsorgsforholdet har opphørt i løpet av året:" },
            nynorsk { + "Om omsorgsforholdet er blitt avslutta under året:" },
            english { + "If care work has ceased during the year:" },
        )
    }
    paragraph {
        formText(
            size = Size.SHORT,
            vspace = false,
            prompt = {
                text (
                    bokmal { +"Oppgi dato for opphøret:" },
                    nynorsk { +"Dato for opphøyr:" },
                    english { +"State date if ceased:" }
                )
            }
        )
        formText(
            size = Size.LONG,
            vspace = false,
            prompt = { text(
                bokmal { +"Oppgi årsaken til opphøret:" },
                nynorsk { +"Grunnen til opphøyr: " },
                english { +"State reason if ceased" }
            ) }
        )

        formText(size = Size.SHORT, prompt = { text(bokmal { +"Dato:" }, nynorsk { +"Dato:" }, english { +"Date" }) })
        formText(
            size = Size.LONG,
            vspace = false,
            prompt = { text(bokmal { +"Underskrift:" }, nynorsk { +"Underskrift:" }, english { +"Signature:" }) }
        )
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
