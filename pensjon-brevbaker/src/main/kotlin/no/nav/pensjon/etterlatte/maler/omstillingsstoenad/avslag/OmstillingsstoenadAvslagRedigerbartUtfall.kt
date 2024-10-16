package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.avslag

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.createTemplate
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
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.avslag.OmstillingstoenadAvslagRedigerbartUtfallDTOSelectors.avdoedNavn
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.avslag.OmstillingstoenadAvslagRedigerbartUtfallDTOSelectors.erSluttbehandling
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.avslag.OmstillingstoenadAvslagRedigerbartUtfallDTOSelectors.tidligereFamiliepleier

data class OmstillingstoenadAvslagRedigerbartUtfallDTO(
    val avdoedNavn: String,
    val erSluttbehandling: Boolean = false,
    val tidligereFamiliepleier: Boolean? = false
) : RedigerbartUtfallBrevDTO

@TemplateModelHelpers
object OmstillingsstoenadAvslagRedigerbartUtfall :
    EtterlatteTemplate<OmstillingstoenadAvslagRedigerbartUtfallDTO>,
    Delmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMSTILLINGSSTOENAD_AVSLAG_UTFALL

    override val template =
        createTemplate(
            name = kode.name,
            letterDataType = OmstillingstoenadAvslagRedigerbartUtfallDTO::class,
            languages = languages(Bokmal, Nynorsk, English),
            letterMetadata =
                LetterMetadata(
                    displayTitle = "Vedtak - begrunnelse for avslag",
                    isSensitiv = true,
                    distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
                    brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
                ),
        ) {
            title {
                text(
                    Bokmal to "",
                    Nynorsk to "",
                    English to "",
                )
            }

            outline {
                includePhrase(
                    OmstillingsstoenadAvslagFraser.Vedtak(
                        erSluttbehandling,
                        tidligereFamiliepleier,
                        avdoedNavn
                    )
                )
                includePhrase(Vedtak.BegrunnelseForVedtaket)
                includePhrase(OmstillingsstoenadFellesFraser.FyllInn)
            }
}
