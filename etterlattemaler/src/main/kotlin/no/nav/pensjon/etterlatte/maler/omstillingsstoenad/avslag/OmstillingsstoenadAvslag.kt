package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.avslag

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.Element
import no.nav.pensjon.etterlatte.maler.FerdigstillingBrevDTO
import no.nav.pensjon.etterlatte.maler.Hovedmal
import no.nav.pensjon.etterlatte.maler.fraser.common.Felles
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadFellesFraser
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.avslag.selectors.omstillingstoenadAvslagDTO.*

data class OmstillingstoenadAvslagData(
    val bosattUtland: Boolean,
)

data class OmstillingstoenadAvslagDTO(
    override val innhold: List<Element>,
    override val data: OmstillingstoenadAvslagData,
) : FerdigstillingBrevDTO

@TemplateModelHelpers
object OmstillingsstoenadAvslag : EtterlatteTemplate<OmstillingstoenadAvslagDTO>, Hovedmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMSTILLINGSSTOENAD_AVSLAG

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - avslag",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                bokmal { +"Vi har avslått søknaden din om omstillingsstønad" },
                nynorsk { +"Vi har avslått søknaden din om omstillingsstønad" },
                english { +"We have rejected your application for adjustment allowance" },
            )
        }

        outline {
            konverterElementerTilBrevbakerformat(innhold)

            includePhrase(Felles.DuHarRettTilAaKlage)
            includePhrase(Felles.DuHarRettTilInnsynISakenDin)
            includePhrase(Felles.DuHarRettigheterKnyttetTilPersonopplysninger)
            includePhrase(Felles.DuHarRettTilHjelpFraAndre)
            includePhrase(Felles.DuHarRettTilVeiledning)
            includePhrase(OmstillingsstoenadFellesFraser.HarDuSpoersmaal)
        }
    }
}
