package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.varsel

import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.Element
import no.nav.pensjon.etterlatte.maler.FerdigstillingBrevDTO
import no.nav.pensjon.etterlatte.maler.Hovedmal
import no.nav.pensjon.etterlatte.maler.fraser.common.Constants
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadFellesFraser
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.varsel.OmstillingsstoenadVarselAktivitetspliktDTOSelectors.bosattUtland
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.varsel.OmstillingsstoenadVarselAktivitetspliktDTOSelectors.innhold

data class OmstillingsstoenadVarselAktivitetspliktDTO(
    override val innhold: List<Element>,
    val bosattUtland: Boolean,
) : FerdigstillingBrevDTO

@TemplateModelHelpers
object OmstillingsstoenadVarselAktivitetsplikt : EtterlatteTemplate<OmstillingsstoenadVarselAktivitetspliktDTO>, Hovedmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMSTILLINGSSTOENAD_VARSEL_AKTIVITETSPLIKT

    override val template =
        createTemplate(
            languages = languages(Bokmal, Nynorsk, English),
            letterMetadata =
                LetterMetadata(
                    displayTitle = "Varselbrev - stans om ikke akt.plikt oppfylt",
                    distribusjonstype = LetterMetadata.Distribusjonstype.ANNET,
                    brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
                ),
        ) {
            title {
                text(
                    bokmal { +"Forhåndsvarsel om stans av omstillingsstønaden" },
                    nynorsk { +"Førehandsvarsel om stans av omstillingsstønad" },
                    english { +"Advance notice of adjustment allowance termination" },
                )
            }

            outline {
                konverterElementerTilBrevbakerformat(innhold)
                title2 {
                    text(
                        bokmal { +"Hvordan kan du melde fra?" },
                        nynorsk { +"Korleis kan du melde frå?" },
                        english { +"How do i report changes?" },
                    )
                }
                paragraph {
                    text(
                        bokmal { +"Du kan melde fra til oss om endringer ved å benytte endringsskjema eller ettersende dokumentasjon på ${Constants.OMS_MELD_INN_ENDRING_URL}." },
                        nynorsk { +"Du kan melde frå om endringar ved å bruke endringsskjema eller ettersende dokumentasjon på ${Constants.OMS_MELD_INN_ENDRING_URL}." },
                        english { +"You can report changes by use the change form or submit information on ${Constants.OMS_MELD_INN_ENDRING_URL}." },
                    )
                }

                paragraph {
                    val postadresse = ifElse(bosattUtland, Constants.Utland.POSTADRESSE, Constants.POSTADRESSE)
                    text(
                        bokmal { +"Har du ikke BankID eller annen innloggingsmulighet til vår hjemmeside nav.no, kan du sende brev til " + postadresse + "." },
                        nynorsk { +"Har du ikkje BankID eller andre moglegheiter til å logge på heimesida vår nav.no, må du sende dokumentasjon per post til " + postadresse + "." },
                        english { +"Please send documentation as normal post if you do not use BankID or another login option. Send to " + postadresse + "." },
                    )
                }

                includePhrase(OmstillingsstoenadFellesFraser.HarDuSpoersmaal)
            }
        }
}
