package no.nav.pensjon.brev.maler.fraser.alderspensjon

import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

// pensjonFraAndreInfoAP_001
object InfoPensjonFraAndreAP : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                bokmal { + "Andre pensjonsordninger" },
                nynorsk { + "Andre pensjonsordningar" },
                english { + "Other pension schemes" },
            )
        }
        paragraph {
            text(
                bokmal { + 
                    "Mange er tilknyttet en eller flere offentlige eller private pensjonsordninger som de har pensjonsrettigheter fra. " +
                    "Du bør kontakte dem du har slike ordninger med for å undersøke hvilke rettigheter du kan ha. Du kan også undersøke med siste arbeidsgiver. " },
                nynorsk { + 
                    "Mange er knytte til ei eller fleire offentlege eller private pensjonsordningar som de har pensjonsrettar frå. " +
                    "Du bør kontakte dei du har slike ordningar med for å undersøke kva for rettar du har. Du kan også undersøkje med siste arbeidsgivar." },
                english { + 
                    "Many people are also members of one or more public or private pension schemes where they also have pension rights. " +
                    "You must contact the company/ies you have pension arrangements with, if you have any questions about this. You can also contact your most recent employer." },
            )
        }
    }
}

