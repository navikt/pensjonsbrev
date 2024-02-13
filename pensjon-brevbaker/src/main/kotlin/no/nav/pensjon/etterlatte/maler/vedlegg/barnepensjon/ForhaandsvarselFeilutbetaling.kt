package no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.etterlatte.maler.barnepensjon.opphoer.BarnepensjonOpphoerDTOSelectors.innholdForhaandsvarsel
import no.nav.pensjon.etterlatte.maler.fraser.common.Felles
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat


@TemplateModelHelpers
val forhaandsvarselFeilutbetalingBarnepensjonOpphoer = createAttachment(
    title = newText(
        Bokmal to "Forhåndsvarsel - vi vurderer om du må betale tilbake pensjon",
        Nynorsk to "Førehandsvarsel - vi vurderer om du må betale tilbake pensjon",
        English to "Advance Notice of Possible Repayment of Incorrectly Paid Pension",
    ),
    includeSakspart = false
) {

    konverterElementerTilBrevbakerformat(innholdForhaandsvarsel)

    includePhrase(Felles.SlikUttalerDuDeg)
    includePhrase(Felles.HvaSkjerVidereIDinSak)
}
