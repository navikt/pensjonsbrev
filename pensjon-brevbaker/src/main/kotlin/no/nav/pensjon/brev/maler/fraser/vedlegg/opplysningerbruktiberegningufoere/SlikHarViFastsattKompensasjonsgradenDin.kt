package no.nav.pensjon.brev.maler.fraser.vedlegg.opplysningerbruktiberegningufoere

import no.nav.pensjon.brev.api.model.KravAarsakType
import no.nav.pensjon.brev.api.model.Kroner
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr

data class SlikHarViFastsattKompensasjonsgradenDin(
val kravAarsakType: Expression<KravAarsakType>,
    val harUlikBeloepUT: Expression<Boolean>,
    val ifuInntekt: Expression<Kroner>,
    val harFullUfoeregrad: Expression<Boolean>,
    val harDelvisUfoeregrad: Expression<Boolean>,
    val oifuInntekt: Expression<Boolean>,
    val ugradertBruttoPerAar: Expression<Boolean>,
    val ufoeregrad: Expression<Int>,

)


/* TBU056V
IF (brevkode = PE_UT_04_102, PE_UT_04_116, PE_UT_04_101, PE_UT_04_114, PE_UT_04_300, PE_UT_14_300, PE_UT_04_500)
OR (KravArsakType = <ENDRET_INNTEKT>
AND <BelopGammelUT> <> <BelopNyUT>)
AND <KravArsakType> <> <SOKNAD_BT> */