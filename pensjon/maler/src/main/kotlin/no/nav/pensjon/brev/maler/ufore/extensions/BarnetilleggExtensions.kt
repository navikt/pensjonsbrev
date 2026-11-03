package no.nav.pensjon.brev.maler.ufore.extensions

import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.Barnetillegg
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.barnetillegg.barnetilleggFellesbarn.innvilget
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.barnetillegg.barnetilleggFellesbarn.netto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.barnetillegg.barnetilleggSerkullsbarn.innvilget
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.barnetillegg.barnetilleggSerkullsbarn.netto
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.barnetillegg.btFellesbarn
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.barnetillegg.btSerkullsbarn
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.expression.safe


fun Expression<Barnetillegg>.btFellesInnvilgetMenIkkeUtbetalt(): Expression<Boolean> = this.btFellesbarn.safe { innvilget and netto.equalTo(0) }.ifNull(false)
fun Expression<Barnetillegg>.btSerkullInnvilgetMenIkkeUtbetalt(): Expression<Boolean> = this.btSerkullsbarn.safe { innvilget and netto.equalTo(0) }.ifNull(false)
fun Expression<Barnetillegg>.erInnvilget() = this.fellesbarnInnvilget() or this.serkullsbarnInnvilget()
fun Expression<Barnetillegg>.fellesbarnInnvilget() = this.btFellesbarn.safe { innvilget }.ifNull(false)
fun Expression<Barnetillegg>.serkullsbarnInnvilget() = this.btSerkullsbarn.safe { innvilget }.ifNull(false)