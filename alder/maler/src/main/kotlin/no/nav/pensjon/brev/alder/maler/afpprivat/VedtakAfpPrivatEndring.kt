package no.nav.pensjon.brev.alder.maler.afpprivat

import no.nav.pensjon.brev.alder.maler.Brevkategori
import no.nav.pensjon.brev.alder.maler.afpprivat.fraser.AfpPrivatFraser
import no.nav.pensjon.brev.alder.maler.brev.FeatureToggles
import no.nav.pensjon.brev.alder.maler.felles.HarDuSpoersmaal
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.Sakstype
import no.nav.pensjon.brev.alder.model.afpprivat.AfpPrivatBeregningEndringSelectors.kompensasjonstillegg
import no.nav.pensjon.brev.alder.model.afpprivat.AfpPrivatBeregningEndringSelectors.kronetillegg
import no.nav.pensjon.brev.alder.model.afpprivat.AfpPrivatBeregningEndringSelectors.livsvarig
import no.nav.pensjon.brev.alder.model.afpprivat.AfpPrivatBeregningEndringSelectors.sumAfpFoerSkatt
import no.nav.pensjon.brev.alder.model.afpprivat.VedtakAfpPrivatEndringDto
import no.nav.pensjon.brev.alder.model.afpprivat.VedtakAfpPrivatEndringDtoSelectors.PesysDataSelectors.beregning
import no.nav.pensjon.brev.alder.model.afpprivat.VedtakAfpPrivatEndringDtoSelectors.PesysDataSelectors.borIForNorge
import no.nav.pensjon.brev.alder.model.afpprivat.VedtakAfpPrivatEndringDtoSelectors.PesysDataSelectors.brukerAlder
import no.nav.pensjon.brev.alder.model.afpprivat.VedtakAfpPrivatEndringDtoSelectors.PesysDataSelectors.virkningFom
import no.nav.pensjon.brev.alder.model.afpprivat.VedtakAfpPrivatEndringDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.TemplateDescription.ISakstype
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.lessThan
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

/**
 * Redigerbart vedtak — endring av AFP i privat sektor.
 *
 * Konvertert fra Exstream-malen `PE_AF_04_114`. Brevet har bokmål og nynorsk
 * i originalen. Auto-varianten er [VedtakAfpPrivatEndringOpptjeningAuto]
 * (`PE_AF_04_113`).
 */
@TemplateModelHelpers
object VedtakAfpPrivatEndring : RedigerbarTemplate<VedtakAfpPrivatEndringDto> {

    override val kode = Aldersbrevkoder.Redigerbar.PE_AFP_PRIVAT_ENDRING

    override val featureToggle = FeatureToggles.vedtakAfpPrivatEndring.toggle

    override val kategori = Brevkategori.VEDTAK_ENDRING_OG_REVURDERING

    override val brevkontekst: TemplateDescription.Brevkontekst = TemplateDescription.Brevkontekst.VEDTAK

    override val sakstyper: Set<ISakstype> = setOf(Sakstype.AFP_PRIVAT)

    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - endring av AFP i privat sektor",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                bokmal { +"Avtalefestet pensjon (AFP) i privat sektor - melding om endring" },
                nynorsk { +"Avtalefesta pensjon (AFP) i privat sektor - melding om endring" },
            )
        }

        outline {
            paragraph {
                text(
                    bokmal {
                        +"Nav har bestemt at din AFP i privat sektor skal endres fra " +
                            pesysData.virkningFom.format() + " fordi opptjeningsgrunnlaget er endret."
                    },
                    nynorsk {
                        +"Nav har bestemt at din AFP i privat sektor skal endrast frå " +
                            pesysData.virkningFom.format() + ", fordi oppteningsgrunnlaget er endra."
                    },
                )
            }

            paragraph {
                text(
                    bokmal {
                        +"Vedtaket er gjort etter bestemmelsene i lov om statstilskott til arbeidstakere som tar ut " +
                            "avtalefestet pensjon i privat sektor (AFP-tilskottsloven)."
                    },
                    nynorsk {
                        +"Vedtaket er gjort etter føresegnene i lov om statstilskott til arbeidstakarar som tek ut " +
                            "avtalefesta pensjon i privat sektor (AFP-tilskotslova)."
                    },
                )
            }

            includePhrase(
                AfpPrivatFraser.AfpBeloepPerMaanedTabell(
                    livsvarig = pesysData.beregning.livsvarig,
                    kronetillegg = pesysData.beregning.kronetillegg,
                    kompensasjonstillegg = pesysData.beregning.kompensasjonstillegg,
                    sumAfpFoerSkatt = pesysData.beregning.sumAfpFoerSkatt,
                )
            )

            includePhrase(AfpPrivatFraser.Levealdersjustering)

            includePhrase(AfpPrivatFraser.KomponentLivsvarig(pesysData.beregning.livsvarig))
            includePhrase(AfpPrivatFraser.KomponentKronetillegg(pesysData.beregning.kronetillegg))
            includePhrase(AfpPrivatFraser.KomponentKompensasjonstillegg(pesysData.beregning.kompensasjonstillegg))

            includePhrase(AfpPrivatFraser.AfpOgAlderspensjon)

            showIf(pesysData.brukerAlder.lessThan(70)) {
                includePhrase(AfpPrivatFraser.OpptjeningEtter61Aar)
            }

            includePhrase(AfpPrivatFraser.ArbeidUtenReduksjon)

            includePhrase(AfpPrivatFraser.MaanedligUtbetaling)

            showIf(pesysData.borIForNorge) {
                includePhrase(AfpPrivatFraser.SkattINorge(pesysData.beregning.kompensasjonstillegg))
            }

            showIf(pesysData.borIForNorge.not()) {
                includePhrase(AfpPrivatFraser.SkattIUtlandet(pesysData.beregning.kompensasjonstillegg))
            }

            includePhrase(AfpPrivatFraser.DinPensjonSkattetrekk)

            includePhrase(AfpPrivatFraser.InnsynForvaltningsloven18)

            includePhrase(AfpPrivatFraser.KlagerettFolketrygdloven2112)

            paragraph {
                text(
                    bokmal { +"Ta kontakt med Nav dersom du ønsker mer informasjon." },
                    nynorsk { +"Ta kontakt med Nav dersom du ønskjer meir informasjon." },
                )
            }
        }
    }
}
