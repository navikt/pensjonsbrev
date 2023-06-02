package no.nav.pensjon.etterlatte.maler.omstillingsstoenad

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.ManueltBrevDTO
import no.nav.pensjon.etterlatte.maler.ManueltBrevDTOSelectors.ElementSelectors.children
import no.nav.pensjon.etterlatte.maler.ManueltBrevDTOSelectors.ElementSelectors.type
import no.nav.pensjon.etterlatte.maler.ManueltBrevDTOSelectors.InnerElementSelectors.text
import no.nav.pensjon.etterlatte.maler.ManueltBrevDTOSelectors.innhold
import no.nav.pensjon.etterlatte.maler.fraser.common.OMSFelles
import no.nav.pensjon.etterlatte.maler.fraser.common.Vedtak

@TemplateModelHelpers
object OMSInnvilgelseManuell : EtterlatteTemplate<ManueltBrevDTO> {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMS_INNVILGELSE_MANUELL

    override val template = createTemplate(
        name = kode.name,
        letterDataType = ManueltBrevDTO::class,
        languages = languages(Language.Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - Manuelt brev for omstillingsstønad",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title {
            text(
                Language.Bokmal to "Vi har innvilget søknaden din om omstillingsstoenad",
            )
        }

        outline {
            includePhrase(Vedtak.Overskrift)

            forEach(innhold) { element ->
                showIf(element.type.equalTo(ManueltBrevDTO.ElementType.HEADING_TWO)) {
                    forEach(element.children) { inner ->
                        title1 {
                            ifNotNull(inner.text) {
                                textExpr(Language.Bokmal to it)
                            }
                        }
                    }
                }.orShowIf(element.type.equalTo(ManueltBrevDTO.ElementType.HEADING_THREE)) {
                    forEach(element.children) { inner ->
                        title2 {
                            ifNotNull(inner.text) {
                                textExpr(Language.Bokmal to it)
                            }
                        }
                    }
                }.orShowIf(element.type.equalTo(ManueltBrevDTO.ElementType.PARAGRAPH)) {
                    forEach(element.children) { inner ->
                        paragraph {
                            ifNotNull(inner.text) {
                                textExpr(Language.Bokmal to it)
                            }
                        }
                    }
                }.orShowIf(element.type.equalTo(ManueltBrevDTO.ElementType.BULLETED_LIST)) {
                    paragraph {
                        list {
                            forEach(element.children) { inner ->
                                ifNotNull(inner.text) {
                                    item {
                                        textExpr(Language.Bokmal to it)
                                    }
                                }
                            }
                        }

                    }
                }
            }

            includePhrase(OMSFelles.MeldFraOmEndringer)
            includePhrase(OMSFelles.DuHarRettTilAaKlage)
            includePhrase(OMSFelles.DuHarRettTilInnsyn)
            includePhrase(OMSFelles.HarDuSpoersmaal)
        }

    }
}