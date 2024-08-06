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
import no.nav.pensjon.etterlatte.maler.BrevDTO
import no.nav.pensjon.etterlatte.maler.Element
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.informasjon.OmstillingsstoenadMottattSoeknadRedigerbartDTOSelectors.borINorgeEllerIkkeAvtaleland
import java.time.LocalDate

data class OmstillingsstoenadMottattSoeknadRedigerbartDTO(
    override val innhold: List<Element>,
    val mottattDato: LocalDate,
    val borINorgeEllerIkkeAvtaleland: Boolean,
) : BrevDTO

@TemplateModelHelpers
object OmstillingsstoenadMottattSoeknadRedigerbart :
    EtterlatteTemplate<OmstillingsstoenadMottattSoeknadRedigerbartDTO> {
    override val kode: EtterlatteBrevKode =
        EtterlatteBrevKode.OMSTILLINGSSTOENAD_INFORMASJON_MOTTATT_SOEKNAD_REDIGERBART

    override val template =
        createTemplate(
            name = kode.name,
            letterDataType = OmstillingsstoenadMottattSoeknadRedigerbartDTO::class,
            languages = languages(Bokmal, Nynorsk, English),
            letterMetadata =
                LetterMetadata(
                    displayTitle = "Vi har mottatt søknaden din om omstillingsstønad",
                    isSensitiv = false,
                    distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
                    brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
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
                showIf(borINorgeEllerIkkeAvtaleland) {
                    title2 {
                        text(
                            Bokmal to "Behandlingstid",
                            Nynorsk to "Behandlingstid",
                            English to "Processing time",
                        )
                    }
                    paragraph {
                        text(
                            Bokmal to
                                "Søknaden din vil bli behandlet så snart som mulig og senest innen <fritekst: antall uker/måneder>. " +
                                "Hvis søknaden ikke blir avgjort i løpet av denne tiden, vil du høre nærmere fra oss. " +
                                "Du kan finne vår saksbehandlingstid på nav.no/saksbehandlingstider#omstillingsstonad.",
                            Nynorsk to
                                "Søknaden din vil bli behandla så snart som mogleg og sinast innan <fritekst: antall uker/måneder>. " +
                                "Dersom saka di ikkje har blitt ferdigbehandla innan denne tida, vil du få nærmare beskjed. " +
                                "Du finn saksbehandlingstida vår på nav.no/saksbehandlingstider#omstillingsstonad.",
                            English to
                                "NAV will process your application as soon as possible, and within <fritekst: antall uker/måneder> " +
                                "at the latest. If your application is not processed within this time frame, " +
                                "you will hear from us again. Read more about processing times at nav.no/saksbehandlingstider#omstillingsstonad.",
                        )
                    }
                }.orShow {
                    title2 {
                        text(
                            Bokmal to "Søknaden vil ikke bli behandlet",
                            Nynorsk to "Søknaden vil ikkje bli behandla",
                            English to "Your application will not be processed",
                        )
                    }
                    paragraph {
                        text(
                            Bokmal to
                                "Du må kontakte trygdemyndigheten i landet du bor i for å søke om omstillingsstønad. " +
                                "Dette landets trygdemyndigheter vil sende søknaden videre til NAV. " +
                                "Mottatt søknad er derfor avbrutt og vil ikke bli behandlet.",
                            Nynorsk to
                                "Du må kontakte trygdemaktene i landet du bur i for å søkje om omstillingsstønad. " +
                                "Trygdemaktene i landet du bur i vil sende søknaden til NAV. " +
                                "Motteken søknad er avbrutt og vil ikkje bli behandla.",
                            English to
                                "You must contact the national insurance authority in the country where you live to apply " +
                                "for an adjustment allowance. This country’s national insurance authority will then " +
                                "forward your application to NAV. The application we received has therefore been terminated and will not be processed. ",
                        )
                    }
                }
            }
        }
}
