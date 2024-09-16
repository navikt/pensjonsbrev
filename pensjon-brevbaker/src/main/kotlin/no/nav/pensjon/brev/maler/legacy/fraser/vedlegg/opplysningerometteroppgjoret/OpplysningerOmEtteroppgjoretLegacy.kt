package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerometteroppgjoret

import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.uforetrygdetteroppgjor.Inntektsgrunnlag
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.uforetrygdetteroppgjor.InntektsgrunnlagSelectors.inntekttype_safe
import no.nav.pensjon.brev.api.model.maler.legacy.grunnlag.uforetrygdetteroppgjor.InntektsgrunnlagSelectors.registerkilde_safe
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.TextOnlyPhrase
import no.nav.pensjon.brev.template.dsl.TextOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.text

object OpplysningerOmEtteroppgjoretLegacy {
    data class PE_UT_Etteroppgjor_DetaljBruker_InntektListe_InntektTypeKode(
        val inntektTypeKode: Expression<String?>
    ): TextOnlyPhrase<LangBokmalNynorsk>() {
        override fun TextOnlyScope<LangBokmalNynorsk, Unit>.template() {
            showIf(inntektTypeKode.equalTo("rap_arb")) {
                text(
                    Bokmal to "Arbeidsinntekt",
                    Nynorsk to "Arbeidsinntekt",
                )
            }.orShowIf(inntektTypeKode.equalTo("forintutl")){
                text(
                    Bokmal to "Utlandsinntekt",
                    Nynorsk to "Utanlandsinntekt",
                )
            }.orShowIf(inntektTypeKode.equalTo("rap_nar")){
                text(
                    Bokmal to "Næringsinntekt",
                    Nynorsk to "Næringsinntekt",
                )
            }.orShowIf(inntektTypeKode.equalTo("rap_and")){
                text(
                    Bokmal to "Pensjoner fra andre enn NAV",
                    Nynorsk to "Pensjon frå andre enn NAV",
                )
            }.orShowIf(inntektTypeKode.equalTo("forpenutl")){
                text(
                    Bokmal to "Pensjon fra utlandet",
                    Nynorsk to "Pensjon frå utlandet",
                )
            }
        }
    }

    data class PE_UT_Etteroppgjor_DetaljBruker_InntektListe_RegisterKildeKode(
        val registerkilde: Expression<String?>
    ): TextOnlyPhrase<LangBokmalNynorsk>() {
        override fun TextOnlyScope<LangBokmalNynorsk, Unit>.template() {
            showIf(registerkilde.equalTo("a_ordning")) {
                text(
                    Bokmal to "Elektronisk innmeldt fra arbeidsgiver",
                    Nynorsk to "Elektronisk innmeld frå arbeidsgivar",
                )
            }.orShowIf(registerkilde.equalTo("skd")) {
                text(
                    Bokmal to "Oppgitt av Skatteetaten",
                    Nynorsk to "Opplyst av Skatteetaten",
                )
            }.orShow {
                text(
                    Bokmal to "Oppgitt av deg",
                    Nynorsk to "Opplyst av deg",
                )
            }
        }
    }

}
