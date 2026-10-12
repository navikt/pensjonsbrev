package no.nav.pensjon.brev.ufore.maler.svartid

import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.ufore.maler.fraser.Constants.UFOERETRYGD_ENDRING_URL

class OrienteringOmSaksbehandlingstidFraser {

    object MeldeFraOmEndringer : OutlinePhrase<LangBokmalNynorsk>() {
        override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
            title1 {
                text(
                    bokmal { +"Du må melde fra om endringer" },
                    nynorsk { +"Du må melde frå om endringar" },
                )
            }
            paragraph {
                text(
                    bokmal { +"Du må melde fra om endringer som kan påvirke søknaden din. Det kan være endringer som gjelder helse, arbeidssituasjon, inntekt, sivilstatus eller at du flytter til et annet land." },
                    nynorsk { +"Du må melde frå om endringar som kan påverke søknaden din. Det kan vere endringar som gjeld helse, arbeidssituasjon, inntekt, sivilstatus eller at du flyttar til eit anna land." }
                )
            }

            paragraph {
                text(
                    bokmal { +"For informasjon om hvordan du melder fra om endringer se: $UFOERETRYGD_ENDRING_URL" },
                    nynorsk { +"For informasjon om korleis du melder frå om endringar, sjå: $UFOERETRYGD_ENDRING_URL" }
                )
            }
        }
    }
}