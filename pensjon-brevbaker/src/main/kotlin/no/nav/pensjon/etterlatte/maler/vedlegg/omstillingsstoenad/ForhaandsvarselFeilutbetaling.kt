package no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadFellesFraser
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadForhaandsvarselFraser
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.revurdering.OmstillingsstoenadRevurderingDTOSelectors.innholdForhaandsvarsel


@TemplateModelHelpers
val forhaandsvarselFeilutbetaling = createAttachment(
    title = newText(
        Bokmal to "Forhåndsvarsel - vi vurderer om du må betale tilbake omstillingsstønad",
        Nynorsk to "",
        English to "",
    ),
    includeSakspart = false
) {

    konverterElementerTilBrevbakerformat(innholdForhaandsvarsel)

    includePhrase(OmstillingsstoenadForhaandsvarselFraser.SlikUttalerDuDeg)
    includePhrase(OmstillingsstoenadForhaandsvarselFraser.HvaSkjerVidereIDinSak)
    includePhrase(OmstillingsstoenadFellesFraser.DuHarRettTilInnsyn)
    includePhrase(OmstillingsstoenadFellesFraser.HarDuSpoersmaal)

}
