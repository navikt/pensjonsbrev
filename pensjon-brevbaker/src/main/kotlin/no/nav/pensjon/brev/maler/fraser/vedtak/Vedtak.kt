package no.nav.pensjon.brev.maler.fraser.vedtak

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.text

object Vedtak {

    // vedtakOverskriftPesys_001
    val overskrift = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
        title1 {
            text(
                Bokmal to "Vedtak",
                Nynorsk to "Vedtak",
                English to "Decision",
            )
        }
    }

    /**
     * TBU1092
     */
    val begrunnelseOverskrift = OutlinePhrase<LangBokmalNynorskEnglish, Unit> {
        title1 {
            text(
                Bokmal to "Begrunnelse for vedtaket",
                Nynorsk to "Grunngiving for vedtaket",
                English to "Grounds for the decision",
            )
        }
    }
}