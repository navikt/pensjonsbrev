package no.nav.pensjon.etterlatte.maler.tilbakekreving

import no.nav.pensjon.brev.api.model.maler.VedleggData
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.Element
import no.nav.pensjon.etterlatte.maler.FerdigstillingBrevDTO
import no.nav.pensjon.etterlatte.maler.Hovedmal
import no.nav.pensjon.etterlatte.maler.fraser.common.SakType
import no.nav.pensjon.etterlatte.maler.fraser.common.format
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBeloeperSelectors.fradragSkatt
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBrevDTOSelectors.data
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBrevDTOSelectors.innhold
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBrevDataSelectors.bosattUtland
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBrevDataSelectors.brukerNavn
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBrevDataSelectors.datoTilsvarBruker
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBrevDataSelectors.datoVarselEllerVedtak
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBrevDataSelectors.doedsbo
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBrevDataSelectors.sakType
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBrevDataSelectors.tilbakekreving
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBrevDataSelectors.varsel
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingDTOSelectors.skalTilbakekreve
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingDTOSelectors.summer
import no.nav.pensjon.etterlatte.maler.vedlegg.klageOgAnke
import java.time.LocalDate

data class TilbakekrevingBrevDTO(
    override val innhold: List<Element>,
    val data: TilbakekrevingBrevData,
) : FerdigstillingBrevDTO

data class TilbakekrevingBrevData(
    val sakType: SakType,
    val bosattUtland: Boolean,
    val brukerNavn: String,
    val doedsbo: Boolean,

    val varsel: TilbakekrevingVarsel,
    val datoVarselEllerVedtak: LocalDate,
    val datoTilsvarBruker: LocalDate?,

    val tilbakekreving: TilbakekrevingDTO
)

data class TilbakekrevingDTO(
    val fraOgMed: LocalDate,
    val tilOgMed: LocalDate,
    val skalTilbakekreve: Boolean,
    val helTilbakekreving: Boolean,
    val perioder: List<TilbakekrevingPeriode>,
    val summer: TilbakekrevingBeloeper
) : VedleggData

data class TilbakekrevingPeriode(
    val maaned: LocalDate,
    val beloeper: TilbakekrevingBeloeper,
    val resultat: TilbakekrevingResultat
)

data class TilbakekrevingBeloeper(
    val feilutbetaling: Kroner,
    val bruttoTilbakekreving: Kroner,
    val nettoTilbakekreving: Kroner,
    val fradragSkatt: Kroner,
    val renteTillegg: Kroner,
    val sumNettoRenter: Kroner
)

enum class TilbakekrevingVarsel {
    EGET_BREV,
    MED_I_ENDRINGSBREV,
    AAPENBART_UNOEDVENDIG,
}

@TemplateModelHelpers
object TilbakekrevingFerdig : EtterlatteTemplate<TilbakekrevingBrevDTO>, Hovedmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.TILBAKEKREVING_FERDIG

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - Tilbakekreving",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {

        title {
            showIf(data.tilbakekreving.skalTilbakekreve) {
                showIf(data.doedsbo) {
                    text(
                        bokmal { +"Dødsboet må betale tilbake " + data.sakType.format() },
                        nynorsk { +"Dødsbuet må betale tilbake " + data.sakType.format() },
                        english { +"The estate must pay reimbursement for " + data.sakType.format() }
                    )
                }.orShow {
                    text(
                        bokmal { +"Du må betale tilbake " + data.sakType.format() },
                        nynorsk { +"Du må betale tilbake " + data.sakType.format() },
                        english { +"You must pay reimbursement for " + data.sakType.format() }
                    )
                }
            }.orShow {
                text(
                    bokmal { +"Du skal ikke betale tilbake " + data.sakType.format() },
                    nynorsk { +"Du skal ikkje betale tilbake " + data.sakType.format() },
                    english { +"No reimbursement for " + data.sakType.format() + " will be claimed" },
                )
            }

        }
        outline {
            showIf(data.doedsbo) {
                includePhrase(TilbakekrevingFraser.ViserTilVarselbrevDoedsbo(data.datoVarselEllerVedtak, data.datoTilsvarBruker))
            }.orShow {
                includePhrase(
                    TilbakekrevingFraser.ViserTilVarselbrev(
                        data.sakType,
                        data.varsel,
                        data.datoVarselEllerVedtak,
                        data.datoTilsvarBruker
                    )
                )
            }

            showIf(data.tilbakekreving.skalTilbakekreve) {
                showIf(data.doedsbo) {
                    includePhrase(
                        TilbakekrevingFraser.KonklusjonTilbakekrevingDoedsbo(data.sakType, data.tilbakekreving, data.brukerNavn)
                    )
                    showIf(data.tilbakekreving.summer.fradragSkatt.greaterThan(0)) {
                        includePhrase(TilbakekrevingFraser.TrukketSkatt)
                    }
                    includePhrase(TilbakekrevingFraser.VedtakGjortEtterLover(data.tilbakekreving))
                    includePhrase(TilbakekrevingFraser.ReferanseTilVedleggDoedsbo)
                    includePhrase(TilbakekrevingFraser.SkattDoedsbo)
                }.orShow {
                    includePhrase(TilbakekrevingFraser.KonklusjonTilbakekreving(data.sakType, data.tilbakekreving))
                    showIf(data.tilbakekreving.summer.fradragSkatt.greaterThan(0)) {
                        includePhrase(TilbakekrevingFraser.TrukketSkatt)
                    }
                    includePhrase(TilbakekrevingFraser.VedtakGjortEtterLover(data.tilbakekreving))
                    includePhrase(TilbakekrevingFraser.ReferanseTilVedlegg)
                    includePhrase(TilbakekrevingFraser.Skatt)
                }
            }.orShow {
                includePhrase(TilbakekrevingFraser.HovedInnholdIngenTilbakekreving(data.sakType, data.tilbakekreving, data.doedsbo))
            }

            konverterElementerTilBrevbakerformat(innhold)

        }

        includeAttachment(tilbakekrevingVedlegg, attachmentData = data.tilbakekreving)

        // Nasjonal
        includeAttachment(
            klageOgAnke(bosattUtland = false, tilbakekreving = true),
            data.bosattUtland.not().and(data.doedsbo.not())
        )
        // Bosatt utland
        includeAttachment(
            klageOgAnke(bosattUtland = true, tilbakekreving = true),
            data.bosattUtland.and(data.doedsbo.not())
        )
    }
}