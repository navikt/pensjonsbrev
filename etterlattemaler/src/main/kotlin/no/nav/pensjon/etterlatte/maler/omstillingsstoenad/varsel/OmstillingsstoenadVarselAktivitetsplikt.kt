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
                        English to "How can I submit information?",
                    )
                }
                paragraph {
                    text(
                        Bokmal to "Du kan melde fra om endringer og sende dokumentasjon ved å",
                        Nynorsk to "Du kan melde frå om endringar og sende dokumentasjon ved å",
                        English to "You can report changes and submit documentation by",
                    )

                    val postadresse = ifElse(bosattUtland, Constants.Utland.POSTADRESSE, Constants.POSTADRESSE)

                    list {
                        item {
                            text(
                                Bokmal to "benytte endringsskjema på ${Constants.OMS_MELD_INN_ENDRING_URL}",
                                Nynorsk to "bruke endringsskjema på ${Constants.OMS_MELD_INN_ENDRING_URL}",
                                English to "use the change form on ${Constants.OMS_MELD_INN_ENDRING_URL}",
                            )
                        }
                        item {
                            text(
                                Bokmal to "ettersende dokumentasjon angående omstillingsstønad ved å gå inn på ${Constants.ETTERSENDE_OMS_URL}",
                                Nynorsk to "ettersend dokumentasjon angående omstillingsstønad ved å gå inn på ${Constants.ETTERSENDE_OMS_URL}.",
                                English to
                                        "submit additional documentation regarding adjustment allowance by going to: ${Constants.Engelsk.ETTERSENDE_OMS_URL}.",
                            )
                        }
                        item {
                            textExpr(
                                Bokmal to "sende brev til ".expr() + postadresse,
                                Nynorsk to "send brev til ".expr() + postadresse,
                                English to "send a letter to ".expr() + postadresse,
                            )
                        }
                    }
                }
                includePhrase(OmstillingsstoenadFellesFraser.HarDuSpoersmaal)
            }
        }
}
