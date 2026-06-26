package no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser

import no.nav.pensjon.brev.ufore.api.model.vedlegg.OpplysningerBruktIBeregningUTLegacyDto.Trygdetid.Periode
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

/** Portert fra legacy TBU1187. Avdoedes faktiske norske trygdetid. Gating skjer i vedlegget via flagg. */
data class TBU1187(
    val perioder: Expression<List<Periode>>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        paragraph {
            text(
                bokmal { + "Den faktiske norske trygdetiden til avdøde er fastsatt ut fra følgende perioder:" },
                nynorsk { + "Den faktiske norske trygdetida til den avdøde er fastsett ut frå følgjande periodar:" },
            )
        }
        includePhrase(TrygdetidListeNorTabell(perioder))
    }
}
