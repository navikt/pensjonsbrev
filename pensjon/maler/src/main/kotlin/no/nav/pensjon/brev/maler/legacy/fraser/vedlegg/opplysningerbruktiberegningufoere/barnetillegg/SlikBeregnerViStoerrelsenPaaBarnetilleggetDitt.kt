package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere.barnetillegg

import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope

data class SlikBeregnerViStoerrelsenPaaBarnetilleggetDitt(
    val pe: Expression<PEgruppe10>,
    val visStoerrelsesforklaring: Expression<Boolean>,
    val utelatVedDoedsfall: Expression<Boolean>,
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {
        includePhrase(
            StoerrelsePaaBarnetillegg_Innledning(
                pe = pe,
                visStoerrelsesforklaring = visStoerrelsesforklaring,
                utelatVedDoedsfall = utelatVedDoedsfall,
            )
        )
        includePhrase(
            StoerrelsePaaBarnetillegg_EttBarnekull(
                pe = pe,
                visStoerrelsesforklaring = visStoerrelsesforklaring,
                utelatVedDoedsfall = utelatVedDoedsfall,
            )
        )
        includePhrase(StoerrelsePaaBarnetillegg_EndringIInntekt(pe))
        includePhrase(StoerrelsePaaBarnetillegg_Fellesbarn(pe))
        includePhrase(StoerrelsePaaBarnetillegg_Serkull(pe))
    }
}
