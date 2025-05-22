package no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer

import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.Element
import no.nav.pensjon.etterlatte.maler.FerdigstillingBrevDTO
import no.nav.pensjon.etterlatte.maler.Hovedmal
import no.nav.pensjon.etterlatte.maler.fraser.common.Felles
import no.nav.pensjon.etterlatte.maler.fraser.omstillingsstoenad.OmstillingsstoenadFellesFraser
import no.nav.pensjon.etterlatte.maler.konverterElementerTilBrevbakerformat
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerForhaandsvarselBrevDTOSelectors.data
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerForhaandsvarselBrevDTOSelectors.innhold
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerForhaandsvarselDTOSelectors.beregningsVedleggData
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerForhaandsvarselDTOSelectors.bosattUtland
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerForhaandsvarselDTOSelectors.etteroppgjoersAar
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.etteroppgjoer.EtteroppgjoerForhaandsvarselDTOSelectors.resultatType
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer.EtteroppgjoerGrunnlagDTO
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer.BeregningsVedleggData
import no.nav.pensjon.etterlatte.maler.vedlegg.omstillingsstoenad.etteroppgjoer.beregningsVedlegg
import java.time.LocalDate

data class EtteroppgjoerUtbetalingDTO(
    val inntekt: Kroner,
    val faktiskInntekt: Kroner,
    val avviksBeloep: Kroner
)

data class EtteroppgjoerForhaandsvarselBrevDTO(
    override val innhold: List<Element>,
    val data: EtteroppgjoerForhaandsvarselDTO
) : FerdigstillingBrevDTO

enum class EtteroppgjoerResultatType{
    TILBAKEKREVING,
    ETTERBETALING,
    IKKE_ETTEROPPGJOER
}

data class EtteroppgjoerForhaandsvarselDTO(
    val vedleggInnhold: List<Element>,

    val bosattUtland: Boolean,
    val norskInntekt: Boolean,
    val etteroppgjoersAar: Int,
    val rettsgebyrBeloep: Kroner,
    val resultatType: EtteroppgjoerResultatType,
    val stoenad: Kroner,
    val faktiskStoenad: Kroner,
    val avviksBeloep: Kroner,
    val grunnlag: EtteroppgjoerGrunnlagDTO
){
    val utbetalingData = EtteroppgjoerUtbetalingDTO(stoenad, faktiskStoenad, avviksBeloep)
    val beregningsVedleggData = BeregningsVedleggData(vedleggInnhold, etteroppgjoersAar, utbetalingData, grunnlag)
    val dagensDato: LocalDate = LocalDate.now()
}

@TemplateModelHelpers
object EtteroppgjoerForhaandsvarsel : EtterlatteTemplate<EtteroppgjoerForhaandsvarselBrevDTO>, Hovedmal {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.OMS_EO_FORHAANDSVARSEL

    override val template = createTemplate(
        name = kode.name,
        letterDataType = EtteroppgjoerForhaandsvarselBrevDTO::class,
        languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
        letterMetadata = LetterMetadata(
            displayTitle = "Varsel - Etteroppgjør",
            isSensitiv = true,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.INFORMASJONSBREV,
        ),
    ) {
        title {
            // hvis ingen endring
            showIf(data.resultatType.equalTo(EtteroppgjoerResultatType.IKKE_ETTEROPPGJOER)){
                textExpr(
                    Language.Bokmal to "Informasjon om etteroppgjør av omstillingsstønad for ".expr() + data.etteroppgjoersAar.format(),
                    Language.Nynorsk to "".expr() + data.etteroppgjoersAar.format(),
                    Language.English to "".expr() + data.etteroppgjoersAar.format(),
                )
            }.orShow {
                // hvis for mye eller for lite utbetalt
                textExpr(
                    Language.Bokmal to "Forhåndsvarsel om etteroppgjør av omstillingsstønad for ".expr() + data.etteroppgjoersAar.format(),
                    Language.Nynorsk to "".expr() + data.etteroppgjoersAar.format(),
                    Language.English to "".expr() + data.etteroppgjoersAar.format(),
                )
            }
        }

        outline {

            konverterElementerTilBrevbakerformat(innhold)

            title2 {
                text(
                    Language.Bokmal to "Sjekk beregningen og meld fra hvis noe er feil",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
            }
            paragraph {
                textExpr(
                    Language.Bokmal to "Se ny beregning av omstillingsstønaden din for inntektsåret ".expr() + data.etteroppgjoersAar.format() + " i vedlegget “Opplysninger om etteroppgjøret”. Du må kontrollere om inntektene som er oppgitt er riktig. ",
                    Language.Nynorsk to "".expr(),
                    Language.English to "".expr(),
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Hvis opplysningene er feil, må du gi beskjed innen tre uker. Hvis du ikke svarer, får du et vedtak basert på de opplysningene som står i vedlegget. Hvis du melder fra om feil, vil vi vurdere de nye opplysningene før du får et vedtak.",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
            }

            showIf(data.resultatType.equalTo(EtteroppgjoerResultatType.TILBAKEKREVING)) {
                paragraph {
                    text(
                        Language.Bokmal to "I vedtaket får du informasjon om hvordan du kan betale tilbake for mye utbetalt omstillingsstønad og hvordan du kan klage på vedtaket.",
                        Language.Nynorsk to "",
                        Language.English to "",
                    )
                }
            }

            includePhrase(Felles.HvordanMelderDuFra)
            includePhrase(OmstillingsstoenadFellesFraser.HarDuIkkeBankID(data.bosattUtland))
            includePhrase(OmstillingsstoenadFellesFraser.HarDuSpoersmaal)
        }

        includeAttachment(beregningsVedlegg, data.beregningsVedleggData)
    }
}
