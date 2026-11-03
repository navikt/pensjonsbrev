package no.nav.pensjon.brev.maler.ufore.extensions

import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.Grunnlag
import no.nav.pensjon.brev.api.model.maler.ufoerApi.felles.selectors.grunnlag.epsBrukerBorMed
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.safe

//TODO usikker på om disse bør finnes eller behandles bedre i brevet
fun Expression<Grunnlag>.epsBorMedNnEntall() = this.epsBrukerBorMed.safe { nynorskBestemtEntall() }.ifNull("")
fun Expression<Grunnlag>.epsBorMedBmEntall() = this.epsBrukerBorMed.safe { bokmalUbestemtEntall() }.ifNull("")