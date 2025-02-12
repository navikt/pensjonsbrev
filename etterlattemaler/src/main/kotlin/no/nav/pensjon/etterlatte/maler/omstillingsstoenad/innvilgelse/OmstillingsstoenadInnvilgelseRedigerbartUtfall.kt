package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.Delmal
import no.nav.pensjon.etterlatte.maler.RedigerbartUtfallBrevDTO
import no.nav.pensjon.etterlatte.maler.fraser.common.Vedtak
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadInnvilgelseFraser
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse.OmstillingsstoenadInnvilgelseRedigerbartUtfallDTOSelectors.etterbetaling
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse.OmstillingsstoenadInnvilgelseRedigerbartUtfallDTOSelectors.tidligereFamiliepleier
import java.time.LocalDate

data class OmstillingsstoenadInnvilgelseRedigerbartUtfallDTO(
    val virkningsdato: LocalDate,
    val utbetalingsbeloep: Kroner,
    val etterbetaling: Boolean,
    val tidligereFamiliepleier: Boolean = false,
) : RedigerbartUtfallBrevDTO

@TemplateModelHelpers
object OmstillingsstoenadInnvilgelseRedigerbartUtfall :
    EtterlatteTemplate<OmstillingsstoenadInnvilgelseRedigerbartUtfallDTO>,
    Delmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMSTILLINGSSTOENAD_INNVILGELSE_UTFALL

    override val template =
        createTemplate(
            name = kode.name,
            letterDataType = OmstillingsstoenadInnvilgelseRedigerbartUtfallDTO::class,
            languages = languages(Bokmal, Nynorsk, English),
            letterMetadata =
                LetterMetadata(
                    displayTitle = "Vedtak - begrunnelse for innvilgelse",
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
                includePhrase(Vedtak.BegrunnelseForVedtaket)
                includePhrase(
                    OmstillingsstoenadInnvilgelseFraser.BegrunnelseForVedtaketRedigerbart(etterbetaling, tidligereFamiliepleier),
                )
            }
        }
}
