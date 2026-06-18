package no.nav.pensjon.brev.template.dsl.expression

import no.nav.brev.BrevLandmodell
import no.nav.pensjon.brev.template.BinaryOperation
import no.nav.pensjon.brev.template.Expression

fun Expression<BrevLandmodell.Landkode>.format(): Expression<String> = BinaryOperation.Landnavn(this, Expression.FromScope.Language)