package no.nav.pensjon.brev.maler.alder.avslag.gradsendring

import brev.avslag.gradsendring.InnholdSoeknadFoerEttAar
import brev.felles.HarDuSpoersmaalAlder
import brev.felles.RettTilAAKlage
import brev.felles.RettTilInnsyn
import dineRettigheterOgMulighetTilAaKlagePensjonStatisk
import no.nav.pensjon.brev.model.alder.AlderspensjonRegelverkType
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.text

data class AvslagGradsendringFoerNormertPensjonsalderFoerEttAarFelles(
    val regelverkType: Expression<AlderspensjonRegelverkType>,
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
            InnholdSoeknadFoerEttAar(
                regelverkType = regelverkType
            )
        )

        includePhrase(RettTilAAKlage)
        includePhrase(RettTilInnsyn(dineRettigheterOgMulighetTilAaKlagePensjonStatisk))
        includePhrase(HarDuSpoersmaalAlder)
    }
}