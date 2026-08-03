package no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad

import no.nav.pensjon.brev.template.AttachmentTemplate
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.etterlatte.maler.fraser.common.Felles
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.opphoer.OmstillingsstoenadOpphoerDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.opphoer.selectors.omstillingsstoenadOpphoerData.innholdForhaandsvarsel as innholdForhaandsvarselOpphoer
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurderingDTO
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.selectors.omstillingsstoenadRevurderingData.innholdForhaandsvarsel as innholdForhaandsvarselRevurdering
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.opphoer.selectors.omstillingsstoenadOpphoerDTO.data as dataOpphoer
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.selectors.omstillingsstoenadRevurderingDTO.data as dataRevurdering


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

        konverterElementerTilBrevbakerformat(dataRevurdering.innholdForhaandsvarselRevurdering)

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

    konverterElementerTilBrevbakerformat(dataOpphoer.innholdForhaandsvarselOpphoer)

    includePhrase(Felles.SlikUttalerDuDegOmstillingsstoenad)
    includePhrase(Felles.HvaSkjerVidereIDinSak)
}
