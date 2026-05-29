package no.nav.pensjon.brev.alder.maler.afpprivat

import no.nav.pensjon.brev.alder.maler.afpprivat.fraser.AfpPrivatFraser
import no.nav.pensjon.brev.alder.maler.felles.HarDuSpoersmaal
import no.nav.pensjon.brev.alder.maler.vedlegg.vedleggOversiktOverPensjonenAfpPrivat
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.afpprivat.AfpPrivatBeregningEndringSelectors.kompensasjonstillegg
import no.nav.pensjon.brev.alder.model.afpprivat.AfpPrivatBeregningEndringSelectors.kronetillegg
import no.nav.pensjon.brev.alder.model.afpprivat.AfpPrivatBeregningEndringSelectors.livsvarig
import no.nav.pensjon.brev.alder.model.afpprivat.AfpPrivatBeregningEndringSelectors.sumAfpFoerSkatt
import no.nav.pensjon.brev.alder.model.afpprivat.VedtakAfpPrivatEndringOpptjeningAutoDto
import no.nav.pensjon.brev.alder.model.afpprivat.VedtakAfpPrivatEndringOpptjeningAutoDtoSelectors.beregning
import no.nav.pensjon.brev.alder.model.afpprivat.VedtakAfpPrivatEndringOpptjeningAutoDtoSelectors.borIForNorge
import no.nav.pensjon.brev.alder.model.afpprivat.VedtakAfpPrivatEndringOpptjeningAutoDtoSelectors.brukerAlder
import no.nav.pensjon.brev.alder.model.afpprivat.VedtakAfpPrivatEndringOpptjeningAutoDtoSelectors.oversiktOverPensjonen
import no.nav.pensjon.brev.alder.model.afpprivat.VedtakAfpPrivatEndringOpptjeningAutoDtoSelectors.virkningFom
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.lessThan
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

/**
 * Auto-vedtak — endring av opptjening for AFP i privat sektor.
 *
 * Konvertert fra Exstream-malen `PE_AF_04_113`. Brevet har kun bokmål.
 * Den redigerbare varianten av samme situasjon er
 * [VedtakAfpPrivatEndring] (`PE_AF_04_114`).
 */
@TemplateModelHelpers
object VedtakAfpPrivatEndringOpptjeningAuto : AutobrevTemplate<VedtakAfpPrivatEndringOpptjeningAutoDto> {

    override val kode = Aldersbrevkoder.AutoBrev.PE_AFP_PRIVAT_ENDRING_OPPTJENING_AUTO

    override val template = createTemplate(
        languages = languages(Bokmal),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - endring av opptjening - AFP i privat sektor",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        ),
    ) {
        title {
            text(
                bokmal { +"Avtalefestet pensjon (AFP) i privat sektor - melding om endring" },
            )
        }

        outline {
            paragraph {
                text(
                    bokmal {
                        +"Nav har omregnet pensjonen din og utbetalingen er endret med virkning fra " +
                            virkningFom.format() + " fordi opptjeningsgrunnlaget er endret."
                    },
                )
            }

            paragraph {
                text(
                    bokmal {
                        +"AFP i privat sektor gis etter bestemmelsene i lov om statstilskott til arbeidstakere som tar ut " +
                            "avtalefestet pensjon i privat sektor (AFP-tilskottsloven). Fellesordningen for AFP har funnet " +
                            "at du oppfyller de avtalemessige vilkårene for rett til AFP. Nav har avgjort andre spørsmål " +
                            "om retten til pensjon, blant annet beregningen. Beregningsreglene står i paragrafene 6 til 11 " +
                            "i AFP-tilskottsloven."
                    },
                )
            }

            includePhrase(
                AfpPrivatFraser.AfpBeloepPerMaanedTabell(
                    livsvarig = beregning.livsvarig,
                    kronetillegg = beregning.kronetillegg,
                    kompensasjonstillegg = beregning.kompensasjonstillegg,
                    sumAfpFoerSkatt = beregning.sumAfpFoerSkatt,
                )
            )

            includePhrase(AfpPrivatFraser.KomponentLivsvarig(beregning.livsvarig))
            includePhrase(AfpPrivatFraser.KomponentKronetillegg(beregning.kronetillegg))
            includePhrase(AfpPrivatFraser.KomponentKompensasjonstillegg(beregning.kompensasjonstillegg))

            title1 {
                text(
                    bokmal { +"Begrunnelse for vedtaket" },
                )
            }

            paragraph {
                text(
                    bokmal {
                        +"Pensjonen din er omregnet fordi det er endringer i opptjeningsgrunnlaget. Opptjening kan være " +
                            "pensjonsgivende inntekt og/eller omsorgsopptjening. Ny opptjening for det året du fylte " +
                            "61 år kan tas med i beregningen av pensjonen fra og med det året du fyller 63 år. Ved " +
                            "korrigeringer i opptjening for ferdiglignede år vil Nav gjøre følgende:"
                    },
                )
                list {
                    item {
                        text(
                            bokmal {
                                +"Hvis endringen medfører en økning i pensjonen, får du en etterbetaling fra det " +
                                    "tidspunktet opptjeningen kan bli medregnet."
                            },
                        )
                    }
                    item {
                        text(
                            bokmal {
                                +"Hvis endringen medfører en reduksjon i pensjonen, blir utbetalingen satt ned med " +
                                    "virkning fra første mulige måned etter at Nav fikk melding om endringen."
                            },
                        )
                    }
                }
            }

            title1 {
                text(
                    bokmal { +"Din AFP blir levealdersjustert" },
                )
            }

            includePhrase(AfpPrivatFraser.Levealdersjustering)

            includePhrase(AfpPrivatFraser.AfpOgAlderspensjon)

            showIf(brukerAlder.lessThan(70)) {
                includePhrase(AfpPrivatFraser.OpptjeningEtter61Aar)
            }

            includePhrase(AfpPrivatFraser.ArbeidUtenReduksjon)

            includePhrase(AfpPrivatFraser.MaanedligUtbetaling)

            showIf(borIForNorge) {
                includePhrase(AfpPrivatFraser.SkattINorge(beregning.kompensasjonstillegg))
            }

            includePhrase(AfpPrivatFraser.DinPensjonSkattetrekk)

            title1 {
                text(
                    bokmal { +"Din rett til innsyn og klage" },
                )
            }

            includePhrase(AfpPrivatFraser.InnsynForvaltningsloven18)
            includePhrase(AfpPrivatFraser.KlagerettFolketrygdloven2112)

            paragraph {
                text(
                    bokmal { +"Ta kontakt med Nav dersom du ønsker mer informasjon." },
                )
            }

            includePhrase(HarDuSpoersmaal.afpPrivat)
        }

        includeAttachmentIfNotNull(vedleggOversiktOverPensjonenAfpPrivat, oversiktOverPensjonen)
    }
}
