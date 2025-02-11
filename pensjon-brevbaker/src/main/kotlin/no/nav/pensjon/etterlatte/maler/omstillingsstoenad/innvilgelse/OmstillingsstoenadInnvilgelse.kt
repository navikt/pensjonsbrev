package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse

import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.expression.notNull
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.*
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadFellesFraser
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadInnvilgelseFraser
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse.OmstillingsstoenadInnvilgelseDTOSelectors.avdoed
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse.OmstillingsstoenadInnvilgelseDTOSelectors.beregning
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse.OmstillingsstoenadInnvilgelseDTOSelectors.datoVedtakOmgjoering
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse.OmstillingsstoenadInnvilgelseDTOSelectors.erSluttbehandling
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse.OmstillingsstoenadInnvilgelseDTOSelectors.etterbetaling
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse.OmstillingsstoenadInnvilgelseDTOSelectors.harUtbetaling
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse.OmstillingsstoenadInnvilgelseDTOSelectors.informasjonOmOmstillingsstoenadData
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse.OmstillingsstoenadInnvilgelseDTOSelectors.innhold
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse.OmstillingsstoenadInnvilgelseDTOSelectors.innvilgetMindreEnnFireMndEtterDoedsfall
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse.OmstillingsstoenadInnvilgelseDTOSelectors.omsRettUtenTidsbegrensning
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.innvilgelse.OmstillingsstoenadInnvilgelseDTOSelectors.tidligereFamiliepleier
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.InformasjonOmOmstillingsstoenadData
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.beregningAvOmstillingsstoenad
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.dineRettigheterOgPlikter
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.informasjonOmOmstillingsstoenad
import java.time.LocalDate

data class OmstillingsstoenadInnvilgelseDTO(
    override val innhold: List<Element>,
    val avdoed: Avdoed?,
    val beregning: OmstillingsstoenadBeregning,
    val innvilgetMindreEnnFireMndEtterDoedsfall: Boolean,
    val lavEllerIngenInntekt: Boolean?, // TODO: skal fases ut
    val omsRettUtenTidsbegrensning: Boolean = lavEllerIngenInntekt ?: false, // TODO: overtar for lavEllerIngenInntekt
    val harUtbetaling: Boolean,
    val etterbetaling: OmstillingsstoenadEtterbetaling?,
    val tidligereFamiliepleier: Boolean = false,
    val bosattUtland: Boolean = false,
    val erSluttbehandling: Boolean = false,
    val datoVedtakOmgjoering: LocalDate? = null,
) : FerdigstillingBrevDTO {
    val informasjonOmOmstillingsstoenadData = InformasjonOmOmstillingsstoenadData(tidligereFamiliepleier, bosattUtland)
}

@TemplateModelHelpers
object OmstillingsstoenadInnvilgelse : EtterlatteTemplate<OmstillingsstoenadInnvilgelseDTO>, Hovedmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMSTILLINGSSTOENAD_INNVILGELSE

    override val template =
        createTemplate(
            name = kode.name,
            letterDataType = OmstillingsstoenadInnvilgelseDTO::class,
            languages = languages(Bokmal, Nynorsk, English),
            letterMetadata =
                LetterMetadata(
                    displayTitle = "Vedtak - Innvilget omstillingsstønad",
                    isSensitiv = true,
                    distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
                    brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
                ),
        ) {

            title {
                ifNotNull(datoVedtakOmgjoering) {
                    textExpr(
                        Bokmal to "Vi har omgjort vedtaket om omstillingsstønad av ".expr() + it.format(),
                        Nynorsk to "Vi har gjort om vedtaket om omstillingsstønad av ".expr() + it.format(),
                        English to "We have reversed our decision regarding the adjustment allowance on ".expr() + it.format(),
                    )
                }.orShow {
                    text(
                        Bokmal to "Vi har innvilget søknaden din om omstillingsstønad",
                        Nynorsk to "Vi har innvilga søknaden din om omstillingsstønad",
                        English to "We have granted your application for adjustment allowance",
                    )
                }
            }

            outline {
                showIf(datoVedtakOmgjoering.notNull().not()){
                    includePhrase(
                        OmstillingsstoenadInnvilgelseFraser.Vedtak(avdoed, beregning, harUtbetaling, tidligereFamiliepleier, erSluttbehandling),
                    )
                }

                konverterElementerTilBrevbakerformat(innhold)

                showIf(harUtbetaling) {
                    includePhrase(OmstillingsstoenadInnvilgelseFraser.UtbetalingMedEtterbetaling(etterbetaling))
                }
                includePhrase(OmstillingsstoenadInnvilgelseFraser.HvaErOmstillingsstoenad(tidligereFamiliepleier))
                includePhrase(
                    OmstillingsstoenadFellesFraser.HvorLengerKanDuFaaOmstillingsstoenad(
                        beregning,
                        omsRettUtenTidsbegrensning,
                        tidligereFamiliepleier,
                    ),
                )
                showIf(omsRettUtenTidsbegrensning.not()) {
                    includePhrase(
                        OmstillingsstoenadInnvilgelseFraser.Aktivitetsplikt(
                            innvilgetMindreEnnFireMndEtterDoedsfall,
                            tidligereFamiliepleier,
                        ),
                    )
                }
                includePhrase(OmstillingsstoenadFellesFraser.Inntektsendring)
                includePhrase(OmstillingsstoenadFellesFraser.Etteroppgjoer)
                includePhrase(OmstillingsstoenadFellesFraser.MeldFraOmEndringer)
                includePhrase(OmstillingsstoenadFellesFraser.DuHarRettTilAaKlage)
                includePhrase(OmstillingsstoenadFellesFraser.HarDuSpoersmaal)
            }
            includeAttachment(
                beregningAvOmstillingsstoenad(tidligereFamiliepleier = true, innvilgelsesaar = true),
                beregning,
                tidligereFamiliepleier,
            )
            includeAttachment(
                beregningAvOmstillingsstoenad(tidligereFamiliepleier = false, innvilgelsesaar = true),
                beregning,
                tidligereFamiliepleier.not(),
            )

            includeAttachment(informasjonOmOmstillingsstoenad(), informasjonOmOmstillingsstoenadData)

            includeAttachment(dineRettigheterOgPlikter, beregning)
        }
}
