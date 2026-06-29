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
import no.nav.pensjon.etterlatte.maler.Delmal
import no.nav.pensjon.etterlatte.maler.RedigerbartUtfallBrevDTO
import no.nav.pensjon.etterlatte.maler.fraser.common.Vedtak
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadAvslagFraser
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadFellesFraser
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.avslag.selectors.omstillingstoenadAvslagRedigerbartUtfallDTO.*
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.avslag.selectors.omstillingstoenadAvslagRedigerbartUtfallData.*

data class OmstillingstoenadAvslagRedigerbartUtfallData(
    val avdoedNavn: String,
    val erSluttbehandling: Boolean = false,
    val tidligereFamiliepleier: Boolean = false,
)

data class OmstillingstoenadAvslagRedigerbartUtfallDTO(
    override val data: OmstillingstoenadAvslagRedigerbartUtfallData,
) : RedigerbartUtfallBrevDTO

@TemplateModelHelpers
object OmstillingsstoenadAvslagRedigerbartUtfall :
    EtterlatteTemplate<OmstillingstoenadAvslagRedigerbartUtfallDTO>,
    Delmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMSTILLINGSSTOENAD_AVSLAG_UTFALL

    override val template =
        createTemplate(
            languages = languages(Bokmal, Nynorsk, English),
            letterMetadata =
            LetterMetadata(
                displayTitle = "Vedtak - begrunnelse for avslag",
                distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
                brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
            ),
        ) {
            title {
                text(
                    bokmal { +"" },
                    nynorsk { +"" },
                    english { +"" },
                )
            }

            outline {
                includePhrase(
                    OmstillingsstoenadAvslagFraser.Vedtak(
                        data.erSluttbehandling,
                        data.tidligereFamiliepleier,
                        data.avdoedNavn
                    )
                )
                includePhrase(Vedtak.BegrunnelseForVedtaket)
                includePhrase(OmstillingsstoenadFellesFraser.FyllInn)
            }
        }
}
