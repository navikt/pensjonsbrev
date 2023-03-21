package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.KravAarsakType
import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr




data class SlikBlirDinUtbetalingFoerSkatt(
    val ufoeregrad: Expression<Int>,
    val harutbetalingsgradLessThanUfoeregrad: Expression<Boolean>,
    val nettoAkkumulertePlussNettoRestAar: Expression<Kroner>,
    val nettoAkkumulerteBeloepUtbetalt: Expression<Kroner>,
    val nettoTilUtbetalingRestenAvAaret: Expression<Kroner>,
    val harNyUTBeloep: Expression<Boolean>,
    val harInntektsgrenseLessThanInntektstak: Expression<Boolean>,
    val forventetInntektAar: Expression<Kroner>,
    val harTotalNetto: Expression<Kroner>, // har fra før?
    val harInntektsgrenseLargerThanOrEqualToInntektstak: Expression<Boolean>,
    val harForventetInntektLargerThanInntektstak: Expression<Boolean>,

)
