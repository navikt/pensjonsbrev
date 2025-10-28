package no.nav.pensjon.brev.maler.alder.avslag.gradsendring

import brev.avslag.gradsendring.InnholdLavOpptjening
import brev.felles.HarDuSpoersmaalAlder
import brev.felles.RettTilAAKlage
import brev.felles.RettTilInnsyn
import dineRettigheterOgMulighetTilAaKlagePensjonStatisk
import no.nav.pensjon.brev.model.alder.AlderspensjonRegelverkType
import no.nav.pensjon.brev.model.alder.avslag.NormertPensjonsalder
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.Kroner
import java.time.LocalDate

data class AvslagGradsendringFoerNormertPensjonsalderFelles(
    val afpBruktIBeregning: Expression<Boolean>,
    val normertPensjonsalder: Expression<NormertPensjonsalder>,
    val virkFom: Expression<LocalDate>,
    val minstePensjonssats: Expression<Kroner>,
    val totalPensjon: Expression<Kroner>,
    val borINorge: Expression<Boolean>,
    val harEOSLand: Expression<Boolean>,
    val regelverkType: Expression<AlderspensjonRegelverkType>,
    val uttaksgrad: Expression<Int>,
    val prorataBruktIBeregningen: Expression<Boolean>,
    val avtaleland: Expression<String?>,
) : OutlinePhrase<LangBokmalNynorskEnglish>() {
    override fun OutlineOnlyScope<LangBokmalNynorskEnglish, Unit>.template() {
        title2 {
            text(
                bokmal { + "Vedtak" },
                nynorsk { + "Vedtak" },
                english { + "Decision" }
            )
        }

        includePhrase(
            InnholdLavOpptjening(
                afpBruktIBeregning = afpBruktIBeregning,
                normertPensjonsalder = normertPensjonsalder,
                uttaksgrad = uttaksgrad,
                prorataBruktIBeregningen = prorataBruktIBeregningen,
                virkFom = virkFom,
                minstePensjonssats = minstePensjonssats,
                totalPensjon = totalPensjon,
                borINorge = borINorge,
                harEOSLand = harEOSLand,
                regelverkType = regelverkType,
                avtaleland = avtaleland,
            )
        )

        includePhrase(RettTilAAKlage)
        includePhrase(RettTilInnsyn(dineRettigheterOgMulighetTilAaKlagePensjonStatisk))
        includePhrase(HarDuSpoersmaalAlder)
    }
}