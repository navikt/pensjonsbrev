package no.nav.pensjon.brev.maler.ufore.uforegrad

import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.OkningUforegradV2Dto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.selectors.okningUforegradV2Dto.pesysData.beregning
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.selectors.okningUforegradV2Dto.pesysData.vedtak
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.beregning.bunnfradrag
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.beregning.bunnfradragNesteAr
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.vedtak.virkFom
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.expression.localDateNow
import no.nav.pensjon.brev.template.dsl.expression.month
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.expression.year
import java.time.Month

internal fun Expression<OkningUforegradV2Dto.PesysData>.faktiskBunnfradrag() =
    ifElse(
        not(localDateNow.year.equalTo(vedtak.virkFom.year) and vedtak.virkFom.month.equalTo(Month.DECEMBER))
                or beregning.bunnfradragNesteAr.equalTo(0),
        beregning.bunnfradrag,
        beregning.bunnfradragNesteAr
    )