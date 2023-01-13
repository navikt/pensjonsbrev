package no.nav.pensjon.brev.maler.fraser.common

import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr

object HjemlerFolketrygdloven {

    data class Folketrygdloven(
        val innvilgetEktefelleTillegg: Expression<Boolean>,
        val innvilgetGjenlevendeTillegg: Expression<Boolean>,
        val innvilgetTilleggFellesbarn: Expression<Boolean>,
        val innvilgetTilleggSaerkullsbarn: Expression<Boolean>,
        val yrkesskadeGrad: Expression<Int>,


        )
}