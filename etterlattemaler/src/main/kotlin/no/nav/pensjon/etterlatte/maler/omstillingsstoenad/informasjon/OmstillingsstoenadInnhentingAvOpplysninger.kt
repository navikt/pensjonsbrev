package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.informasjon

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
import no.nav.pensjon.etterlatte.maler.RedigerbartUtfallBrevDTO
import no.nav.pensjon.etterlatte.maler.fraser.common.Felles
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadFellesFraser
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.informasjon.OmstillingsstoenadInnhentingAvOpplysningerDTOSelectors.borIUtlandet

data class OmstillingsstoenadInnhentingAvOpplysningerDTO(
    val borIUtlandet: Boolean,
) : RedigerbartUtfallBrevDTO

@TemplateModelHelpers
object OmstillingsstoenadInnhentingAvOpplysninger : EtterlatteTemplate<OmstillingsstoenadInnhentingAvOpplysningerDTO> {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMSTILLINGSSTOENAD_INNHENTING_AV_OPPLYSNINGER

    override val template =
        createTemplate(
            name = kode.name,
            letterDataType = OmstillingsstoenadInnhentingAvOpplysningerDTO::class,
            languages = languages(Bokmal, Nynorsk, English),
            letterMetadata =
                LetterMetadata(
                    displayTitle = "Du må sende oss flere opplysninger",
                    isSensitiv = false,
                    distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
                    brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
                ),
        ) {
            title {
                text(
                    Bokmal to "Du må sende oss flere opplysninger",
                    Nynorsk to "Du må sende oss fleire opplysningar",
                    English to "You must submit additional information",
                )
            }

            outline {
                paragraph {
                    text(
                        Bokmal to "UTFALL",
                        Nynorsk to "UTFALL",
                        English to "UTFALL",
                    )
                }
                includePhrase(Felles.HvordanSendeOpplysninger(borIUtlandet))
                includePhrase(OmstillingsstoenadFellesFraser.HarDuSpoersmaal)
            }
        }
}
