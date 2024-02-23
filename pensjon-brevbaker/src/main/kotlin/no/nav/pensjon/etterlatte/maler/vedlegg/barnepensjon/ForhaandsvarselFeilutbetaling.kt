package no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon

import no.nav.pensjon.etterlatte.maler.barnepensjon.opphoer.BarnepensjonOpphoerDTOSelectors.innholdForhaandsvarsel as innholdForhaansvarselOpphoer
import no.nav.pensjon.etterlatte.maler.barnepensjon.revurdering.BarnepensjonRevurderingDTOSelectors.innholdForhaandsvarsel as innholdForhaansvarselRevurdering
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.etterlatte.maler.fraser.common.Felles
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat


@TemplateModelHelpers
val forhaandsvarselFeilutbetalingBarnepensjonRevurdering = createAttachment(
    title = newText(
        Bokmal to "Forhåndsvarsel - vi vurderer om du må betale tilbake barnepensjon",
        Nynorsk to "Førehandsvarsel – vi vurderer om du må betale tilbake barnepensjon",
        English to "Advance notice – we are assessing whether you must repay children’s pension",
    ),
    includeSakspart = false
) {

    konverterElementerTilBrevbakerformat(innholdForhaansvarselRevurdering)

    includePhrase(Felles.SlikUttalerDuDeg)
    includePhrase(Felles.HvaSkjerVidereIDinSak)
}

@TemplateModelHelpers
val forhaandsvarselFeilutbetalingBarnepensjonOpphoer = createAttachment(
    title = newText(
        Bokmal to "Forhåndsvarsel - vi vurderer om du må betale tilbake barnepensjon",
        Nynorsk to "Førehandsvarsel – vi vurderer om du må betale tilbake barnepensjon",
        English to "Advance notice – we are assessing whether you must repay children’s pension",
    ),
    includeSakspart = false
) {

    konverterElementerTilBrevbakerformat(innholdForhaansvarselOpphoer)

    includePhrase(Felles.SlikUttalerDuDeg)
    includePhrase(Felles.HvaSkjerVidereIDinSak)
}