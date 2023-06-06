package no.nav.pensjon.brev.maler.fraser.vedlegg.etteroppgjoer

import no.nav.pensjon.brev.model.*
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*

object VedleggPraktiskInformasjonOmTilbakebetalingEtterOppgjoer : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title1 {
            text(
                Bokmal to "Praktisk informasjon om tilbakebetaling av etteroppgjør",
                Nynorsk to "Praktisk informasjon om tilbakebetaling av etteroppgjør",
                English to "Praktisk informasjon om tilbakebetaling av etteroppgjør"
            )
        }
                paragraph {
                    text(
                        Bokmal to "Du vil motta betalingsinformasjon fra NAV Innkreving på én av følgende måter:",
                        Nynorsk to "Du vil motta betalingsinformasjon fra NAV Innkreving på én av følgende måter:",
                        English to "Du vil motta betalingsinformasjon fra NAV Innkreving på én av følgende måter:"
                    )
                }
            }



    }