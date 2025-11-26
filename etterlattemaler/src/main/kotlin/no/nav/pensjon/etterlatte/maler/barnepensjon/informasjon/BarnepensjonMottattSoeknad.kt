package no.nav.pensjon.etterlatte.maler.barnepensjon.informasjon

import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.RedigerbartUtfallBrevDTO
import no.nav.pensjon.etterlatte.maler.barnepensjon.informasjon.BarnepensjonMottattSoeknadDTOSelectors.borINorgeEllerIkkeAvtaleland
import no.nav.pensjon.etterlatte.maler.barnepensjon.informasjon.BarnepensjonMottattSoeknadDTOSelectors.bosattUtland
import no.nav.pensjon.etterlatte.maler.barnepensjon.informasjon.BarnepensjonMottattSoeknadDTOSelectors.erOver18aar
import no.nav.pensjon.etterlatte.maler.barnepensjon.informasjon.BarnepensjonMottattSoeknadDTOSelectors.mottattDato
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.BarnepensjonFellesFraser
import java.time.LocalDate

data class BarnepensjonMottattSoeknadDTO(
    val mottattDato: LocalDate,
    val erOver18aar: Boolean,
    val bosattUtland: Boolean,
    val borINorgeEllerIkkeAvtaleland: Boolean,
) : RedigerbartUtfallBrevDTO

@TemplateModelHelpers
object BarnepensjonMottattSoeknad : EtterlatteTemplate<BarnepensjonMottattSoeknadDTO> {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BARNEPENSJON_INFORMASJON_MOTTATT_SOEKNAD

    override val template =
        createTemplate(
            languages = languages(Bokmal, Nynorsk, English),
            letterMetadata =
                LetterMetadata(
                    displayTitle = "Vi har mottatt søknaden din om barnepensjon",
                    isSensitiv = false,
                    distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
                    brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
                ),
        ) {
            title {
                text(
                    bokmal { +"Vi har mottatt søknaden din om barnepensjon" },
                    nynorsk { +"Vi har fått søknaden din om barnepensjon" },
                    english { +"We received your application for a children's pension" },
                )
            }
            outline {
                paragraph {
                    text(
                        bokmal { +"Vi viser til søknaden din som vi mottok " + mottattDato.format() + "." },
                        nynorsk { +"Vi fekk søknad frå deg " + mottattDato.format() + "." },
                        english { +"We refer to your application that we received " + mottattDato.format() + "." },
                    )
                }
                showIf(borINorgeEllerIkkeAvtaleland) {
                    title2 {
                        text(
                            bokmal { +"Behandlingstid" },
                            nynorsk { +"Behandlingstid" },
                            english { +"Processing time" },
                        )
                    }
                    paragraph {
                        text(
                            bokmal { +
                                "Søknaden din vil bli behandlet så snart som mulig og senest innen <fritekst: antall uker/måneder>. " +
                                "Hvis søknaden ikke blir avgjort i løpet av denne tiden, vil du høre nærmere fra oss. " +
                                "Du kan finne vår saksbehandlingstid på nav.no/saksbehandlingstider#barnepensjon." },
                            nynorsk { +
                                "Søknaden din vil bli behandla så snart som mogleg og seinast innan <fritekst: antall uker/måneder>. " +
                                "Dersom søknaden ikkje blir avgjort i løpet av denne tida, vil du få nærare beskjed. " +
                                "Du finn saksbehandlingstida vår på nav.no/saksbehandlingstider#barnepensjon." },
                            english { +
                                "Nav will process your application as soon as possible, and within <fritekst: antall uker/måneder> " +
                                "at the latest. If your application is not processed within this time frame, " +
                                "you will hear from us again. Read more about processing times at nav.no/saksbehandlingstider#barnepensjon." },
                        )
                    }
                }.orShow {
                    title2 {
                        text(
                            bokmal { +"Søknaden vil ikke bli behandlet" },
                            nynorsk { +"Søknaden vil ikkje bli behandla" },
                            english { +"Your application will not be processed" },
                        )
                    }
                    paragraph {
                        text(
                            bokmal { +
                                "Du må kontakte trygdemyndigheten i landet du bor i for å søke om barnepensjon. " +
                                "Dette landets trygdemyndigheter vil sende søknaden videre til Nav. " +
                                "Mottatt søknad er derfor avbrutt og vil ikke bli behandlet." },
                            nynorsk { +
                                "Du må kontakte trygdemaktene i landet du bur i for å søkje om barnepensjon. " +
                                "Trygdemaktene i landet du bur i vil sende søknaden til Nav. " +
                                "Motteken søknad er avbroten og vil ikkje bli behandla." },
                            english { +
                                "You must contact the national insurance authority in the country where you live to apply " +
                                "for a children's pension. This country’s national insurance authority will then " +
                                "forward your application to Nav. The application we received has therefore been terminated and will not be processed." },
                        )
                    }
                }

                showIf(borINorgeEllerIkkeAvtaleland) {
                    title2 {
                        text(
                            bokmal { +"Du må melde fra om endringer" },
                            nynorsk { +"Meld frå om endringar" },
                            english { +"Report changes" },
                        )
                    }
                    paragraph {
                        text(
                            bokmal { +
                                "Du må holde oss orientert om forhold som kan ha betydning for avgjørelsen av søknaden din, som" },
                            nynorsk { +
                                "Vi ber om at du held oss orientert om forhold som kan få betydning for avgjerda vi tek i klaga di. " +
                                "Døme på slike forhold er" },
                            english { +
                                "Please keep us informed about any changes that can have significance for our decision concerning your appeal, such as" },
                        )
                        list {
                            item {
                                text(
                                    bokmal { +"endringer av nåværende familie- eller omsorgsforhold" },
                                    nynorsk { +"endringar i noverande familie- eller omsorgsforhold" },
                                    english { +"changes in family or care relationships" },
                                )
                            }
                            item {
                                text(
                                    bokmal { +"flytting eller opphold i et annet land over tid" },
                                    nynorsk { +"flytting til eller langvarig opphald i eit anna land" },
                                    english { +"relocation or residence in another country over time" },
                                )
                            }
                            item {
                                text(
                                    bokmal { +"varig opphold i institusjon" },
                                    nynorsk { +"varig opphald på ein institusjon" },
                                    english { +"permanent residence in an institution" },
                                )
                            }
                        }
                    }
                }

                includePhrase(BarnepensjonFellesFraser.HarDuSpoersmaal(erOver18aar.not(), bosattUtland))
            }
        }
}
