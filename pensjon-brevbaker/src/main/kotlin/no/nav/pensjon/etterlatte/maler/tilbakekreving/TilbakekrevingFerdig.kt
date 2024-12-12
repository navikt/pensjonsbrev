package no.nav.pensjon.etterlatte.maler.tilbakekreving

import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.textExpr
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
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBrevDTOSelectors.bosattUtland
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBrevDTOSelectors.brukerNavn
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBrevDTOSelectors.datoTilsvarBruker
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBrevDTOSelectors.datoVarselEllerVedtak
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBrevDTOSelectors.doedsbo
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBrevDTOSelectors.innhold
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBrevDTOSelectors.sakType
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBrevDTOSelectors.tilbakekreving
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingBrevDTOSelectors.varsel
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingDTOSelectors.skalTilbakekreve
import no.nav.pensjon.etterlatte.maler.tilbakekreving.TilbakekrevingDTOSelectors.summer
import no.nav.pensjon.etterlatte.maler.vedlegg.klageOgAnke
import java.time.LocalDate

data class TilbakekrevingBrevDTO(
    override val innhold: List<Element>,
    val sakType: SakType,
    val bosattUtland: Boolean,
    val brukerNavn: String,
    val doedsbo: Boolean,

    val varsel: TilbakekrevingVarsel,
    val datoVarselEllerVedtak: LocalDate,
    val datoTilsvarBruker: LocalDate?,

    val tilbakekreving: TilbakekrevingDTO
) : FerdigstillingBrevDTO

data class TilbakekrevingDTO(
    val fraOgMed: LocalDate,
    val tilOgMed: LocalDate,
    val skalTilbakekreve: Boolean,
    val helTilbakekreving: Boolean,
    val perioder: List<TilbakekrevingPeriode>,
    val summer: TilbakekrevingBeloeper
)

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
        name = kode.name,
        letterDataType = TilbakekrevingBrevDTO::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - Tilbakekreving",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            showIf(tilbakekreving.skalTilbakekreve) {
                showIf(doedsbo) {
                    textExpr(
                        Bokmal to "Dødsboet må betale tilbake ".expr() + sakType.format(),
                        Nynorsk to "Dødsbuet må betale tilbake ".expr() + sakType.format(),
                        English to "The estate must pay reimbursement for ".expr() + sakType.format()
                    )
                }.orShow {
                    textExpr(
                        Bokmal to "Du må betale tilbake ".expr() + sakType.format(),
                        Nynorsk to "Du må betale tilbake ".expr() + sakType.format(),
                        English to "You must pay reimbursement for ".expr() + sakType.format()
                    )
                }
            }.orShow {
                textExpr(
                    Bokmal to "Du skal ikke betale tilbake ".expr() + sakType.format(),
                    Nynorsk to "Du skal ikkje betale tilbake ".expr() + sakType.format(),
                    English to "No reimbursement for ".expr() + sakType.format() + " will be claimed",
                )
            }

        }
        outline {
            showIf(doedsbo) {
                includePhrase(TilbakekrevingFraser.ViserTilVarselbrevDoedsbo(datoVarselEllerVedtak, datoTilsvarBruker))
            }.orShow {
                includePhrase(
                    TilbakekrevingFraser.ViserTilVarselbrev(
                        sakType,
                        varsel,
                        datoVarselEllerVedtak,
                        datoTilsvarBruker
                    )
                )
            }

            showIf(tilbakekreving.skalTilbakekreve) {
                showIf(doedsbo) {
                    includePhrase(
                        TilbakekrevingFraser.KonklusjonTilbakekrevingDoedsbo(sakType, tilbakekreving, brukerNavn)
                    )
                    showIf(tilbakekreving.summer.fradragSkatt.greaterThan(0)) {
                        includePhrase(TilbakekrevingFraser.TrukketSkatt)
                    }
                    includePhrase(TilbakekrevingFraser.VedtakGjortEtterLover(tilbakekreving))
                    includePhrase(TilbakekrevingFraser.ReferanseTilVedleggDoedsbo)
                    includePhrase(TilbakekrevingFraser.SkattDoedsbo)
                }.orShow {
                    includePhrase(TilbakekrevingFraser.KonklusjonTilbakekreving(sakType, tilbakekreving))
                    showIf(tilbakekreving.summer.fradragSkatt.greaterThan(0)) {
                        includePhrase(TilbakekrevingFraser.TrukketSkatt)
                    }
                    includePhrase(TilbakekrevingFraser.VedtakGjortEtterLover(tilbakekreving))
                    includePhrase(TilbakekrevingFraser.ReferanseTilVedlegg)
                    includePhrase(TilbakekrevingFraser.Skatt)
                }
            }.orShow {
                includePhrase(TilbakekrevingFraser.HovedInnholdIngenTilbakekreving(sakType, tilbakekreving, doedsbo))
            }

            konverterElementerTilBrevbakerformat(innhold)

        }

        includeAttachment(tilbakekrevingVedlegg, tilbakekreving)

        // Nasjonal
        includeAttachment(
            klageOgAnke(bosattUtland = false, tilbakekreving = true),
            innhold,
            bosattUtland.not().and(doedsbo.not())
        )
        // Bosatt utland
        includeAttachment(
            klageOgAnke(bosattUtland = true, tilbakekreving = true),
            innhold,
            bosattUtland.and(doedsbo.not())
        )
    }
}