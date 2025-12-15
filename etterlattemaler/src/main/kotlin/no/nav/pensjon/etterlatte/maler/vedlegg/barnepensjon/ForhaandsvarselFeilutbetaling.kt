package no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon

import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.etterlatte.maler.fraser.common.Felles
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.barnepensjon.opphoer.BarnepensjonOpphoerDTOSelectors.innholdForhaandsvarsel as innholdForhaansvarselOpphoer
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingDTOSelectors.innholdForhaandsvarsel as innholdForhaansvarselRevurdering


@TemplateModelHelpers
val forhaandsvarselFeilutbetalingBarnepensjonRevurdering = createAttachment(
    title = {
        text(
            bokmal { +"Forhåndsvarsel - vi vurderer om du må betale tilbake barnepensjon" },
            nynorsk { +"Førehandsvarsel – vi vurderer om du må betale tilbake barnepensjon" },
            english { +"Advance notice – we are assessing whether you must repay children’s pension" },
        )
    },
    includeSakspart = false
) {

    konverterElementerTilBrevbakerformat(innholdForhaansvarselRevurdering)

    includePhrase(Felles.SlikUttalerDuDegBarnepensjon)
    includePhrase(Felles.HvaSkjerVidereIDinSak)
}

@TemplateModelHelpers
val forhaandsvarselFeilutbetalingBarnepensjonOpphoer = createAttachment(
    title = {
        text(
            bokmal { +"Forhåndsvarsel - vi vurderer om du må betale tilbake barnepensjon" },
            nynorsk { +"Førehandsvarsel – vi vurderer om du må betale tilbake barnepensjon" },
            english { +"Advance notice – we are assessing whether you must repay children’s pension" },
        )
    },
    includeSakspart = false
) {

    konverterElementerTilBrevbakerformat(innholdForhaansvarselOpphoer)

    includePhrase(Felles.SlikUttalerDuDegBarnepensjon)
    includePhrase(Felles.HvaSkjerVidereIDinSak)
}