package no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon

import no.nav.pensjon.brev.template.AttachmentTemplate
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.etterlatte.maler.barnepensjon.opphoer.BarnepensjonOpphoerDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.opphoer.BarnepensjonOpphoerDataSelectors.innholdForhaandsvarsel
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingDataSelectors.innholdForhaandsvarsel
import no.nav.pensjon.etterlatte.maler.fraser.common.Felles
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.barnepensjon.opphoer.BarnepensjonOpphoerDTOSelectors.data as dataOpphoer
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingDTOSelectors.data as dataRevurdering


@TemplateModelHelpers
val forhaandsvarselFeilutbetalingBarnepensjonRevurdering: AttachmentTemplate<LangBokmalNynorskEnglish, BarnepensjonRevurderingDTO> =
    createAttachment(
        title = {
            text(
                bokmal { +"Forhåndsvarsel - vi vurderer om du må betale tilbake barnepensjon" },
                nynorsk { +"Førehandsvarsel – vi vurderer om du må betale tilbake barnepensjon" },
                english { +"Advance notice – we are assessing whether you must repay children’s pension" },
            )
        },
        includeSakspart = false
    ) {

        konverterElementerTilBrevbakerformat(dataRevurdering.innholdForhaandsvarsel)

        includePhrase(Felles.SlikUttalerDuDegBarnepensjon)
        includePhrase(Felles.HvaSkjerVidereIDinSak)
    }

@TemplateModelHelpers
val forhaandsvarselFeilutbetalingBarnepensjonOpphoer: AttachmentTemplate<LangBokmalNynorskEnglish, BarnepensjonOpphoerDTO> =
    createAttachment(
        title = {
            text(
                bokmal { +"Forhåndsvarsel - vi vurderer om du må betale tilbake barnepensjon" },
                nynorsk { +"Førehandsvarsel – vi vurderer om du må betale tilbake barnepensjon" },
                english { +"Advance notice – we are assessing whether you must repay children’s pension" },
            )
        },
        includeSakspart = false
    ) {

        konverterElementerTilBrevbakerformat(dataOpphoer.innholdForhaandsvarsel)

        includePhrase(Felles.SlikUttalerDuDegBarnepensjon)
        includePhrase(Felles.HvaSkjerVidereIDinSak)
    }