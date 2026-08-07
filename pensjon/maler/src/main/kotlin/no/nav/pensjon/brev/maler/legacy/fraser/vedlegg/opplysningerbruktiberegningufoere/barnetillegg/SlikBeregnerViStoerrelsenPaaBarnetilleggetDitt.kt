package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere.barnetillegg

import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope

/**
 * TBU052V-TBU073V "Slik beregner vi størrelsen på barnetillegget ditt".
 *
 * Orkestrerer delfrasene i rekkefølge. Innholdet er delt opp i tematiske delfraser
 * (innledning, ett barnekull, TBU613, særkull, fellesbarn) for lesbarhet — rendret
 * output er identisk med den opprinnelige samlede fila.
 */
/** Exstream: TBU052V–TBU073V. */
data class SlikBeregnerViStoerrelsenPaaBarnetilleggetDitt(
    val pe: Expression<PEgruppe10>
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        includePhrase(StoerrelsePaaBarnetillegg_Innledning(pe))
        includePhrase(StoerrelsePaaBarnetillegg_EttBarnekull(pe))
        includePhrase(StoerrelsePaaBarnetillegg_EndringIInntekt(pe))
        includePhrase(StoerrelsePaaBarnetillegg_Serkull(pe))
        includePhrase(StoerrelsePaaBarnetillegg_Fellesbarn(pe))
    }
}
