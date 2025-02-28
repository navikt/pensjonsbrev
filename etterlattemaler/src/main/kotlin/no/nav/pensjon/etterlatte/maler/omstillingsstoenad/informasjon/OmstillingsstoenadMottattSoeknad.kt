package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.informasjon

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.LetterMetadataEtterlatte
import no.nav.pensjon.etterlatte.maler.RedigerbartUtfallBrevDTO
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadFellesFraser
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.informasjon.OmstillingsstoenadMottattSoeknadDTOSelectors.borINorgeEllerIkkeAvtaleland
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.informasjon.OmstillingsstoenadMottattSoeknadDTOSelectors.mottattDato
import java.time.LocalDate

data class OmstillingsstoenadMottattSoeknadDTO(
    val mottattDato: LocalDate,
    val borINorgeEllerIkkeAvtaleland: Boolean,
) : RedigerbartUtfallBrevDTO

@TemplateModelHelpers
object OmstillingsstoenadMottattSoeknad : EtterlatteTemplate<OmstillingsstoenadMottattSoeknadDTO> {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMSTILLINGSSTOENAD_INFORMASJON_MOTTATT_SOEKNAD

    override val template =
        createTemplate(
            name = kode.name,
            letterDataType = OmstillingsstoenadMottattSoeknadDTO::class,
            languages = languages(Bokmal, Nynorsk, English),
            letterMetadata =
                LetterMetadataEtterlatte(
                    displayTitle = "Vi har mottatt søknaden din om omstillingsstønad",
                    isSensitiv = false,
                    distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
                    brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
                ),
        ) {
            title {
                text(
                    Bokmal to "Vi har mottatt søknaden din om omstillingsstønad",
                    Nynorsk to "Vi har fått søknaden din om omstillingsstønad",
                    English to "We received your application for adjustment allowance",
                )
            }
            outline {
                paragraph {
                    textExpr(
                        Bokmal to "Vi viser til søknaden din som vi mottok ".expr() + mottattDato.format() + ".",
                        Nynorsk to "Vi viser til søknaden din som vi tok imot ".expr() + mottattDato.format() + ".",
                        English to "We refer to your application that we received ".expr() + mottattDato.format() + ".",
                    )
                }
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
                                    "Nav will process your application as soon as possible, and within <fritekst: antall uker/måneder> " +
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
                                    "Dette landets trygdemyndigheter vil sende søknaden videre til Nav. " +
                                    "Mottatt søknad er derfor avbrutt og vil ikke bli behandlet.",
                            Nynorsk to
                                    "Du må kontakte trygdemaktene i landet du bur i for å søkje om omstillingsstønad. " +
                                    "Trygdemaktene i landet du bur i vil sende søknaden til Nav. " +
                                    "Motteken søknad er avbrutt og vil ikkje bli behandla.",
                            English to
                                    "You must contact the national insurance authority in the country where you live to apply " +
                                    "for an adjustment allowance. This country’s national insurance authority will then " +
                                    "forward your application to Nav. The application we received has therefore been terminated and will not be processed.",
                        )
                    }
                }

                showIf(borINorgeEllerIkkeAvtaleland) {
                    title2 {
                        text(
                            Bokmal to "Du må melde fra om endringer",
                            Nynorsk to "Meld frå om endringar",
                            English to "Report changes",
                        )
                    }
                    paragraph {
                        text(
                            Bokmal to
                                "Vi ber om at du holder oss orientert om forhold som kan ha betydning for avgjørelsen av søknaden din, som",
                            Nynorsk to
                                "Vi ber om at du held oss orientert om forhold som kan få betydning for avgjerda vi tek i klaga di. Døme på slike forhold er",
                            English to
                                "Please keep us informed about any changes that can have significance for our decision concerning your appeal, such as",
                        )
                        list {
                            item {
                                text(
                                    Bokmal to "endringer av nåværende familie- eller omsorgsforhold",
                                    Nynorsk to "endringar i noverande familie- eller omsorgsforhold",
                                    English to "changes in family or care relationships",
                                )
                            }
                            item {
                                text(
                                    Bokmal to "flytting eller opphold i et annet land over tid",
                                    Nynorsk to "flytting til eller langvarig opphald i eit anna land",
                                    English to "relocation or residence in another country over time",
                                )
                            }
                            item {
                                text(
                                    Bokmal to "varig opphold i institusjon",
                                    Nynorsk to "varig opphald på ein institusjon",
                                    English to "permanent residence in an institution",
                                )
                            }
                        }
                    }
                }

                includePhrase(OmstillingsstoenadFellesFraser.HarDuSpoersmaal)
            }
        }
}
