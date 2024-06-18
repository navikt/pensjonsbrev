package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.varsel

import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.*
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadFellesFraser
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.varsel.OmstillingsstoenadVarselAktivitetspliktDTOSelectors.innhold

data class OmstillingsstoenadVarselAktivitetspliktDTO(
    override val innhold: List<Element>,
) : BrevDTO

@TemplateModelHelpers
object OmstillingsstoenadVarselAktivitetsplikt : EtterlatteTemplate<OmstillingsstoenadVarselAktivitetspliktDTO>, Hovedmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMSTILLINGSSTOENAD_VARSEL_AKTIVITETSPLIKT

    override val template =
        createTemplate(
            name = kode.name,
            letterDataType = OmstillingsstoenadVarselAktivitetspliktDTO::class,
            languages = languages(Bokmal, Nynorsk, English),
            letterMetadata =
                LetterMetadata(
                    displayTitle = "Varselbrev - stans om ikke akt.plikt oppfylt",
                    isSensitiv = true,
                    distribusjonstype = LetterMetadata.Distribusjonstype.ANNET,
                    brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
                ),
        ) {
            title {
                text(
                    Bokmal to "Forhåndsvarsel om stans av omstillingsstønaden",
                    Nynorsk to "Førehandsvarsel om stans av omstillingsstønad",
                    English to "Advance notice of adjustment allowance termination",
                )
            }

            outline {
                konverterElementerTilBrevbakerformat(innhold)
                title2 {
                    text(
                        Bokmal to "Hvordan kan du melde fra?",
                        Nynorsk to "Korleis kan du melde frå?",
                        English to "How can I submit information?",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Du kan melde fra om endringer og sende dokumentasjon ved å",
                        Nynorsk to "Du kan melde frå om endringar og sende dokumentasjon ved å",
                        English to "You can report changes and submit documentation by",
                    )
                    list {
                        item {
                            text(
                                Bokmal to "sende en melding på ${Constants.SKRIVTILOSS_URL} (her får du ikke lagt ved dokumentasjon)",
                                Nynorsk to "Send ei melding på ${Constants.SKRIVTILOSS_URL} (her får du ikkje lagt ved dokumentasjon).",
                                English to
                                    "sending a message through ${Constants.Engelsk.SKRIVTILOSS_URL} (you cannot attach any documentationhere)",
                            )
                        }
                        item {
                            text(
                                Bokmal to "ettersende dokumentasjon fra søknad om omstillingsstønad. " +
                                    "Dette gjør du ved å gå inn på ${Constants.ETTERSENDE_OMS_URL}",
                                Nynorsk to "ettersend dokumentasjon frå søknad om omstillingsstønad. " +
                                    "Dette gjer du ved å gå inn på ${Constants.ETTERSENDE_OMS_URL}.",
                                English to
                                        "submit additional documentation on the application for adjustment allowance. " +
                                        "You can do this by going to ${Constants.ETTERSENDE_OMS_URL}.",
                            )
                        }
                        item {
                            text(
                                Bokmal to "sende brev til NAV skanning ${Constants.POSTADRESSE}",
                                Nynorsk to "Send brev til ${Constants.POSTADRESSE}",
                                English to "send a letter to ${Constants.POSTADRESSE}",
                            )
                        }
                    }
                }
                includePhrase(OmstillingsstoenadFellesFraser.HarDuSpoersmaal)
            }
        }
}
