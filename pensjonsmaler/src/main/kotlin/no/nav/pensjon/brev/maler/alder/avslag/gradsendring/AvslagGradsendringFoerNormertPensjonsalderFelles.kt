package no.nav.pensjon.brev.maler.alder.avslag.gradsendring

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.maler.alderApi.NormertPensjonsalder
import no.nav.pensjon.brev.api.model.maler.alderApi.OpplysningerBruktIBeregningen
import no.nav.pensjon.brev.maler.adhoc.vedlegg.dineRettigheterOgMulighetTilAaKlagePensjonStatisk
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.common.Felles.HarDuSpoersmaal.Companion.alder
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

data class AvslagGradsendringFoerNormertPensjonsalderFelles(
    val afpBruktIBeregning: Expression<Boolean>,
    val normertPensjonsalder: Expression<NormertPensjonsalder>,
    val opplysningerBruktIBeregningen: Expression<OpplysningerBruktIBeregningen>,
    val virkFom: Expression<LocalDate>,
    val minstePensjonssats: Expression<Kroner>,
    val totalPensjon: Expression<Kroner>,
    val borINorge: Expression<Boolean>,
    val harEOSLand: Expression<Boolean>,
    val vedtakBegrunnelseLavOpptjening: Expression<Boolean>,
    val regelverkType: Expression<AlderspensjonRegelverkType>
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title2 {
            text(
                Bokmal to "Vedtak",
                Nynorsk to "Vedtak",
                English to "Decision"
            )
        }
        showIf(vedtakBegrunnelseLavOpptjening) {
            includePhrase(
                InnholdLavOpptjening(
                    afpBruktIBeregning = afpBruktIBeregning,
                    normertPensjonsalder = normertPensjonsalder,
                    opplysningerBruktIBeregningen = opplysningerBruktIBeregningen,
                    virkFom = virkFom,
                    minstePensjonssats = minstePensjonssats,
                    totalPensjon = totalPensjon,
                    borINorge = borINorge,
                    harEOSLand = harEOSLand,
                    regelverkType = regelverkType
                )
            )
        }.orShow {
            includePhrase(
                InnholdSoeknadFoerEttAar(
                    regelverkType = regelverkType
                )
            )
        }

        includePhrase(Felles.RettTilAAKlage(dineRettigheterOgMulighetTilAaKlagePensjonStatisk))
        includePhrase(Felles.RettTilInnsyn(dineRettigheterOgMulighetTilAaKlagePensjonStatisk))
        includePhrase(alder)
    }
}