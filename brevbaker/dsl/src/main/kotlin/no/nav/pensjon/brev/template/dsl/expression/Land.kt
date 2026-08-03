package no.nav.pensjon.brev.template.dsl.expression

import no.nav.brev.BrevLandmodell
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LocalizedFormatter

fun Expression<BrevLandmodell.Landkode>.format(): Expression<String> = LocalizedFormatter.LandnavnFormat(this, Expression.FromScope.Language)

@JvmName("formatNullable")
fun Expression<BrevLandmodell.Landkode?>.format(): Expression<String?> = safe { this.format() }