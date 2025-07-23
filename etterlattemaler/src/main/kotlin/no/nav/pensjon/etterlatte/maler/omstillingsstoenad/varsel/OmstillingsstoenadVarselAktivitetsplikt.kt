package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.varsel

import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
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
                        English to "How do i report changes?",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Du kan melde fra til oss om endringer ved å benytte endringsskjema eller ettersende dokumentasjon på ${Constants.OMS_MELD_INN_ENDRING_URL}.",
                        Nynorsk to "Du kan melde frå om endringar ved å bruke endringsskjema eller ettersende dokumentasjon på ${Constants.OMS_MELD_INN_ENDRING_URL}.",
                        English to "You can report changes by use the change form or submit information on ${Constants.OMS_MELD_INN_ENDRING_URL}.",
                    )
                }

                paragraph {
                    val postadresse = ifElse(bosattUtland, Constants.Utland.POSTADRESSE, Constants.POSTADRESSE)
                    textExpr(
                        Bokmal to "Har du ikke BankID eller annen innloggingsmulighet til vår hjemmeside nav.no, kan du sende brev til ".expr() + postadresse + ".",
                        Nynorsk to "Har du ikkje BankID eller andre moglegheiter til å logge på heimesida vår nav.no, må du sende dokumentasjon per post til ".expr() + postadresse + ".",
                        English to "Please send documentation as normal post if you do not use BankID or another login option. Send to ".expr() + postadresse + ".",
                    )
                }

                includePhrase(OmstillingsstoenadFellesFraser.HarDuSpoersmaal)
            }
        }
}
