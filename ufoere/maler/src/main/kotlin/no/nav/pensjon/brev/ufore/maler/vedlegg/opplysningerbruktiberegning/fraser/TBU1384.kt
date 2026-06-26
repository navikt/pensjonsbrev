package no.nav.pensjon.brev.ufore.maler.vedlegg.opplysningerbruktiberegning.fraser

import no.nav.pensjon.brev.ufore.api.model.vedlegg.OpplysningerBruktIBeregningUTLegacyDto.Trygdetid.PeriodeMedLand
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

/** Portert fra legacy TBU1384. Avdoedes trygdetid i bilaterale avtaleland. Gating skjer i vedlegget via flagg. */
data class TBU1384(
    val perioder: Expression<List<PeriodeMedLand>>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        paragraph {
            text(
                bokmal { + "Trygdetiden til avdøde i land som Norge har trygdeavtale med, er fastsatt ut fra følgende perioder:" },
                nynorsk { + "Trygdetida til den avdøde i land som Noreg har trygdeavtale med, er fastsett ut frå følgjande periodar:" },
            )
        }
        includePhrase(TrygdetidsListeBilateralTabell(perioder))
    }
}
