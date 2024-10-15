package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.avslag

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.*
import no.nav.pensjon.etterlatte.maler.fraser.common.Vedtak
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadAvslagFraser
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadFellesFraser
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.avslag.OmstillingstoenadAvslagDTOSelectors.avdoed
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.avslag.OmstillingstoenadAvslagDTOSelectors.bosattUtland
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.avslag.OmstillingstoenadAvslagDTOSelectors.erSluttbehandling
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.avslag.OmstillingstoenadAvslagDTOSelectors.innhold
import no.nav.pensjon.etterlatte.maler.vedlegg.klageOgAnke

data class OmstillingstoenadAvslagDTO(
    override val innhold: List<Element>,
    val bosattUtland: Boolean,
    val erSluttbehandling: Boolean = false,
    val avdoed: Avdoed,
) : FerdigstillingBrevDTO

@TemplateModelHelpers
object OmstillingsstoenadAvslag : EtterlatteTemplate<OmstillingstoenadAvslagDTO>, Hovedmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMSTILLINGSSTOENAD_AVSLAG

    override val template =
        createTemplate(
            name = kode.name,
            letterDataType = OmstillingstoenadAvslagDTO::class,
            languages = languages(Bokmal, Nynorsk, English),
            letterMetadata =
                LetterMetadata(
                    displayTitle = "Vedtak - avslag",
                    isSensitiv = true,
                    distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
                    brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
                ),
        ) {
            title {
                text(
                    Bokmal to "Vi har avslått søknaden din om omstillingsstønad",
                    Nynorsk to "Vi har avslått søknaden din om omstillingsstønad",
                    English to "We have rejected your application for adjustment allowance",
                )
            }

            outline {
                includePhrase(OmstillingsstoenadAvslagFraser.Vedtak(avdoed, erSluttbehandling))

                includePhrase(Vedtak.BegrunnelseForVedtaket)

                konverterElementerTilBrevbakerformat(innhold)

                includePhrase(OmstillingsstoenadFellesFraser.DuHarRettTilAaKlageAvslagOpphoer)
                includePhrase(OmstillingsstoenadFellesFraser.DuHarRettTilInnsyn)
                includePhrase(OmstillingsstoenadFellesFraser.HarDuSpoersmaal)
            }

            // Nasjonal
            includeAttachment(klageOgAnke(bosattUtland = false), innhold, bosattUtland.not())

            // Bosatt utland
            includeAttachment(klageOgAnke(bosattUtland = true), innhold, bosattUtland)
        }
}
