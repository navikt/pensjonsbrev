package no.nav.pensjon.etterlatte.maler.barnepensjon.migrering

import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.etterlatte.EtterlatteBrevKode
import no.nav.pensjon.etterlatte.EtterlatteTemplate
import no.nav.pensjon.etterlatte.maler.BarnepensjonBeregningSelectors.beregningsperioder
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.BarnepensjonOmregnetNyttRegelverkDTOSelectors.beregning
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.BarnepensjonOmregnetNyttRegelverkDTOSelectors.erBosattUtlandet
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.BarnepensjonOmregnetNyttRegelverkDTOSelectors.erUnder18Aar
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.BarnepensjonOmregnetNyttRegelverkDTOSelectors.etterbetaling
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.BarnepensjonOmregnetNyttRegelverkDTOSelectors.utbetaltEtterReform
import no.nav.pensjon.etterlatte.maler.barnepensjon.migrering.BarnepensjonOmregnetNyttRegelverkDTOSelectors.utbetaltFoerReform
import no.nav.pensjon.etterlatte.maler.fraser.barnepensjon.BarnepensjonInnvilgelseFraser
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.dineRettigheterOgPlikter
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.informasjonTilDegSomMottarBarnepensjon
import no.nav.pensjon.etterlatte.maler.vedlegg.barnepensjon.utlandInformasjonTilDegSomMottarBarnepensjon
import no.nav.pensjon.etterlatte.maler.vedlegg.informasjonTilDegSomHandlerPaaVegneAvBarnet
import no.nav.pensjon.etterlatte.maler.vedlegg.utlandInformasjonTilDegSomHandlerPaaVegneAvBarnet


@TemplateModelHelpers
object EnkeltVedtakOmregningNyttRegelverk : EtterlatteTemplate<BarnepensjonOmregnetNyttRegelverkDTO> {
    override val kode: EtterlatteBrevKode = EtterlatteBrevKode.BARNEPENSJON_VEDTAK_OMREGNING
    override val template: LetterTemplate<*, BarnepensjonOmregnetNyttRegelverkDTO> = createTemplate(
        name = kode.name,
        letterDataType = BarnepensjonOmregnetNyttRegelverkDTO::class,
        languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
        letterMetadata = LetterMetadata(
            displayTitle = "Endring av barnepensjon",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV
        )
    ) {
        title {
            text(
                Language.Bokmal to "Vedtak - endring av barnepensjon",
                Language.Nynorsk to "Utkast til vedtak – endring av barnepensjon",
                Language.English to "",
            )
        }
        outline {
            paragraph {
                text(
                    Language.Bokmal to "Vi viser til at du er innvilget barnepensjon. " +
                            "Stortinget har vedtatt nye regler for barnepensjon som gjelder fra 1. januar 2024.",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "De nye reglene for barnepensjon gir",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
            }
            paragraph {
                list {
                    item {
                        text(
                            Language.Bokmal to "økt sats",
                            Language.Nynorsk to "",
                            Language.English to "",
                        )
                    }
                    item {
                        text(
                            Language.Bokmal to "økt aldersgrense fra 18 år til 20 år",
                            Language.Nynorsk to "",
                            Language.English to "",
                        )
                    }
                    item {
                        text(
                            Language.Bokmal to "ingen søskenjustering",
                            Language.Nynorsk to "",
                            Language.English to "",
                        )
                    }
                }
            }

            title2 {
                text(
                    Language.Bokmal to "Hva betyr endringene for deg?",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
            }

            paragraph {
                text(
                    Language.Bokmal to "Du får høyere barnepensjon.",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
            }
            paragraph {
                textExpr(
                    Language.Bokmal to "Du fikk ".expr() + utbetaltFoerReform.format()
                            + " kroner per måned i pensjon til 31. desember 2023. " +
                            "Du får " + utbetaltEtterReform.format() + " kroner før skatt per måned fra 1. januar 2024.",
                    Language.Nynorsk to "Stortinget har vedteke nye reglar for barnepensjon. Du får høgare barnepensjon. Du har ".expr() +
                            utbetaltFoerReform.format() + " kroner per månad i pensjon fram til 31. desember 2023. Du får " + utbetaltEtterReform.format() +
                            " kroner før skatt per månad frå og med 1. januar 2024.",
                    Language.English to "".expr(),
                )
            }

            paragraph {
                text(
                    Language.Bokmal to "Vedtaket er gjort etter bestemmelsene om barnepensjon i folketrygdloven §§ 18-4, 18-5 og 22-13.",
                    Language.Nynorsk to "",
                    Language.English to "",
                )
            }

            includePhrase(
                BarnepensjonInnvilgelseFraser.UtbetalingAvBarnepensjon(
                    beregning.beregningsperioder,
                    etterbetaling
                )
            )
            includePhrase(BarnepensjonInnvilgelseFraser.MeldFraOmEndringer)
            includePhrase(BarnepensjonInnvilgelseFraser.DuHarRettTilAaKlage)
            includePhrase(BarnepensjonInnvilgelseFraser.HarDuSpoersmaal(erUnder18Aar, erBosattUtlandet))
        }
    }
}
