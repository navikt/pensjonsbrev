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
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadFellesFraser
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.informasjon.OmstillingsstoenadInnhentingAvOpplysningerDTOSelectors.borIUtlandet

data class OmstillingsstoenadInnhentingAvOpplysningerDTO(
    val borIUtlandet: Boolean,
)

@TemplateModelHelpers
object OmstillingsstoenadInnhentingAvOpplysninger : EtterlatteTemplate<OmstillingsstoenadInnhentingAvOpplysningerDTO> {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMSTILLINGSSTOENAD_INFORMASJON_INNHENTING_AV_OPPLYSNINGER

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
                title2 {
                    text(
                        Bokmal to "Hvordan sende opplysninger til oss?",
                        Nynorsk to "Korleis melder du frå om endringar?",
                        English to "How to submit information to NAV?",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Du kan logge deg inn på våre nettsider for å sende oss opplysninger. " +
                            "Du kan også chatte eller sende melding via ${Constants.SKRIVTILOSS_URL}. " +
                            "Har du ikke BankID eller annen innloggingsmulighet til vår hjemmeside ${Constants.NAV_URL}, " +
                            "kan du kontakte oss på telefon.",
                        Nynorsk to "Du kan logge deg inn på nettsidene våre for å sende oss opplysningar. " +
                            "Du kan også chatte eller sende melding via ${Constants.SKRIVTILOSS_URL}. " +
                            "Har du ikkje BankID eller andre moglegheiter til å logge på heimesida vår ${Constants.NAV_URL}, " +
                            "kan du kontakte oss på telefon.",
                        English to "You can log in to our website to submit information. " +
                            "You can also use ${Constants.Engelsk.SKRIVTILOSS_URL} to chat with us or send us a message. " +
                            "If you do not have BankID or another option to log in to our website, ${Constants.NAV_URL}, " +
                            "you can also call us.",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Skal du sende oss noe per post må du bruke adressen",
                        Nynorsk to "Skal du sende oss noko per post, bruker du adressa",
                        English to "If you are submitting anything through the mail, you must use the following address:",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "NAV skanning",
                        Nynorsk to "NAV skanning",
                        English to "NAV skanning",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Postboks 1400",
                        Nynorsk to "Postboks 1400",
                        English to "Postboks 1400",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "0109 Oslo",
                        Nynorsk to "0109 Oslo",
                        English to "0109 Oslo",
                    )
                }
                showIf(borIUtlandet) {
                    paragraph {
                        text(
                            Bokmal to "Norge/Norway",
                            Nynorsk to "Norge/Norway",
                            English to "Norge/Norway",
                        )
                    }
                }
                includePhrase(OmstillingsstoenadFellesFraser.HarDuSpoersmaal)
            }
        }
}
