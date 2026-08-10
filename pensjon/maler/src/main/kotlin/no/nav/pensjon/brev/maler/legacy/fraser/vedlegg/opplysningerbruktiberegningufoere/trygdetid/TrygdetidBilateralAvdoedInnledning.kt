package no.nav.pensjon.brev.maler.legacy.fraser.vedlegg.opplysningerbruktiberegningufoere.trygdetid

import no.nav.pensjon.brev.api.model.maler.legacy.pegruppe10.PEgruppe10
import no.nav.pensjon.brev.maler.legacy.grunnlag_persongrunnlagavdod_trygdetidsgrunnlaglistebilateral_trygdetidsgrunnlagbilateral
import no.nav.pensjon.brev.maler.legacy.grunnlag_persongrunnlagavdod_trygdetidsgrunnlaglistebilateral_trygdetidsgrunnlagbilateral_trygdetidfombilateral
import no.nav.pensjon.brev.maler.legacy.ut_trygdetid_avdod
import no.nav.pensjon.brev.template.Expression
import no.nav.pensjon.brev.template.LangBokmalNynorsk
import no.nav.pensjon.brev.template.OutlinePhrase
import no.nav.pensjon.brev.template.dsl.OutlineOnlyScope
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.notNull
import no.nav.pensjon.brev.template.dsl.text

data class TrygdetidBilateralAvdoedInnledning(
    val pe: Expression<PEgruppe10>
) : OutlinePhrase<LangBokmalNynorsk>() {
    override fun OutlineOnlyScope<LangBokmalNynorsk, Unit>.template() {

        showIf((pe.ut_trygdetid_avdod() and (pe.grunnlag_persongrunnlagavdod_trygdetidsgrunnlaglistebilateral_trygdetidsgrunnlagbilateral_trygdetidfombilateral().notNull()))){
            paragraph {
                text (
                    bokmal { + "Trygdetiden til avdøde i land som Norge har trygdeavtale med, er fastsatt ut fra følgende perioder:" },
                    nynorsk { + "Trygdetida til den avdøde i land som Noreg har trygdeavtale med, er fastsett ut frå følgjande periodar:" },
                )
            }

            includePhrase(TrygdetidsListeBilateralTabell(pe.grunnlag_persongrunnlagavdod_trygdetidsgrunnlaglistebilateral_trygdetidsgrunnlagbilateral()))
        }

    }
}