package no.nav.pensjon.brev.maler.ufore.extensions

import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.Reduksjonsgrunnlag
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.reduksjonsgrunnlag.sumBruttoEtterReduksjonBt
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.reduksjonsgrunnlag.sumBruttoFoerReduksjonBt
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.isNull
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.safe
import no.nav.pensjon.brevbaker.api.model.BrevbakerType.Kroner


fun Expression<Reduksjonsgrunnlag?>.barnetilleggRedusertTil0() = not(this.isNull()) and this.safe { sumBruttoFoerReduksjonBt }.ifNull(Kroner(0)).greaterThan(0) and this.safe { sumBruttoEtterReduksjonBt }.ifNull(Kroner(0)).equalTo(0)
