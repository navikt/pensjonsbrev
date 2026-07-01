package no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser

import no.nav.pensjon.brev.ufore.api.model.vedlegg.OpplysningerBruktIBeregningUTLegacyDto.Trygdetid.PeriodeMedLand
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

/** Portert fra legacy TBU1382. Avdoedes trygdetid i andre EOS-land. Gating skjer i vedlegget via flagg. */
data class TBU1382(
    val perioder: Expression<List<PeriodeMedLand>>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        paragraph {
            text(
                bokmal { + "Trygdetiden til avdøde i andre EØS-land er fastsatt ut fra følgende perioder:" },
                nynorsk { + "Trygdetida til den avdøde i andre EØS-land er fastsett ut frå følgjande periodar:" },
            )
        }
        includePhrase(TrygdetidsListeEOSTabell(perioder))
    }
}
