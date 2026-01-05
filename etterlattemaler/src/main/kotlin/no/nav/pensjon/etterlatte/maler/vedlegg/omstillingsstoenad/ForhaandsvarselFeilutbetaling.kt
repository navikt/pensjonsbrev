package no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad

import no.nav.pensjon.brev.template.AttachmentTemplate
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LanguageSupport
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.etterlatte.maler.fraser.common.Felles
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.opphoer.OmstillingsstoenadOpphoerDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurderingDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.opphoer.OmstillingsstoenadOpphoerDTOSelectors.innholdForhaandsvarsel as innholdForhaandsvarselOpphoer
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurderingDTOSelectors.innholdForhaandsvarsel as innholdForhaansvarselRevurdering


@TemplateModelHelpers
val forhaandsvarselFeilutbetalingOmstillingsstoenadRevurdering: AttachmentTemplate<LangBokmalNynorskEnglish, OmstillingsstoenadRevurderingDTO> =
    createAttachment(
        title = {
            text(
                bokmal { +"Forhåndsvarsel - vi vurderer om du må betale tilbake omstillingsstønad" },
                nynorsk { +"Førehandsvarsel – vi vurderer om du må betale tilbake omstillingsstønad" },
                english { +"Advance notice – we are assessing whether you must repay adjustment allowance" },
            )
        },
        includeSakspart = false
    ) {

        konverterElementerTilBrevbakerformat(innholdForhaansvarselRevurdering)

        includePhrase(Felles.SlikUttalerDuDegOmstillingsstoenad)
        includePhrase(Felles.HvaSkjerVidereIDinSak)
    }

@TemplateModelHelpers
val forhaandsvarselFeilutbetalingOmstillingsstoenadOpphoer: AttachmentTemplate<LangBokmalNynorskEnglish, OmstillingsstoenadOpphoerDTO> =
    createAttachment(
        title = {
            text(
                bokmal { +"Forhåndsvarsel - vi vurderer om du må betale tilbake omstillingsstønad" },
                nynorsk { +"Førehandsvarsel - vi vurderer om du må betale tilbake omstillingsstønad" },
                english { +"Advance notice – we are assessing whether you must repay adjustment allowance" },
            )
        },
        includeSakspart = false
    ) {

    konverterElementerTilBrevbakerformat(innholdForhaandsvarselOpphoer)

    includePhrase(Felles.SlikUttalerDuDegOmstillingsstoenad)
    includePhrase(Felles.HvaSkjerVidereIDinSak)
}
