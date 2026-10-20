package no.nav.pensjon.brev.alder.maler.endring

import no.nav.pensjon.brev.alder.maler.Brevkategori
import no.nav.pensjon.brev.alder.maler.brev.FeatureToggles
import no.nav.pensjon.brev.alder.maler.vedlegg.vedleggMaanedligPensjonFoerSkatt
import no.nav.pensjon.brev.alder.maler.vedlegg.vedleggMaanedligPensjonFoerSkattAp2025
import no.nav.pensjon.brev.alder.maler.vedlegg.vedleggOrienteringOmRettigheterOgPlikter
import no.nav.pensjon.brev.alder.model.Aldersbrevkoder
import no.nav.pensjon.brev.alder.model.Sakstype
import no.nav.pensjon.brev.alder.model.endring.VedtakOmOmgjoeringDto
import no.nav.pensjon.brev.alder.model.endring.selectors.vedtakOmOmgjoeringDto.pesysData
import no.nav.pensjon.brev.alder.model.endring.selectors.vedtakOmOmgjoeringDto.pesysData.maanedligPensjonFoerSkattAP2025Dto
import no.nav.pensjon.brev.alder.model.endring.selectors.vedtakOmOmgjoeringDto.pesysData.maanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.alder.model.endring.selectors.vedtakOmOmgjoeringDto.pesysData.orienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brev.alder.model.endring.selectors.vedtakOmOmgjoeringDto.pesysData.ugyldigVedtakInformasjon
import no.nav.pensjon.brev.alder.model.endring.selectors.vedtakOmOmgjoeringDto.pesysData.ytelse
import no.nav.pensjon.brev.alder.model.endring.selectors.vedtakOmOmgjoeringDto.ugyldigVedtakInformasjon.vedtakDatoFom
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.saksbehandlervalg
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object VedtakOmOmgjoering : RedigerbarTemplate<VedtakOmOmgjoeringDto> {
    override val kategori = Brevkategori.VEDTAK_ENDRING_OG_REVURDERING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper: Set<Sakstype> = setOf(Sakstype.ALDER)
    override val kode = Aldersbrevkoder.Redigerbar.PE_AP_VEDTAK_OM_OMGJOERING
    override val featureToggle = FeatureToggles.vedtakOmOmgjoering.toggle
    override val template = createTemplate(
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak om omgjøring",
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        val aarsak = saksbehandlervalg("aarsak", "Årsak").enum<VedtakOmOmgjoeringDto.Aarsak>()

        val vedtakOm = fritekst("<innvilgelse/endring/reduksjon/økning osv.>")

        title {
            text(
                bokmal { +pesysData.ytelse + " - vedtak om omgjøring" },
                nynorsk { +"??" },
                english { +"??" }
            )
        }

        outline {
            paragraph {
                text(
                    bokmal { +"Nav viser til vedtak om " + vedtakOm + " datert " + pesysData.ugyldigVedtakInformasjon.vedtakDatoFom.format() + "." },
                    nynorsk { +"??" },
                    english { +"??" }
                )
            }

            paragraph {
                text(
                    bokmal { +"Vi har fattet vedtak om omgjøring av dette vedtaket. " + fritekst("<Kort sammendrag av hva som endres, dvs. en oppsummering av vedtaket).>") },
                    nynorsk { +"??" },
                    english { +"??" }
                )
            }

            paragraph {
                text(
                    bokmal { +"Det rettslige grunnlaget for omgjøringen er forvaltningsloven § 35 første ledd bokstav c, som gir Nav adgang til å omgjøre sitt eget vedtak uten at det er påklaget dersom vedtaket må anses ugyldig." },
                    nynorsk { +"??" },
                    english { +"??" }
                )
            }

            title2 {
                text(
                    bokmal { +"Begrunnelse for vedtaket" },
                    nynorsk { +"??" },
                    english { +"??" }
                )
            }

            showIf(aarsak.equalTo(VedtakOmOmgjoeringDto.Aarsak.feilSivilstand)) {
                paragraph {
                    text(
                        bokmal { +"Hjemmel feil sivilstand." },
                        nynorsk { +"??" },
                        english { +"??" }
                    )
                }
            }.orShowIf(aarsak.equalTo(VedtakOmOmgjoeringDto.Aarsak.feilEPSInntekt)) {
                paragraph {
                    text(
                        bokmal { +"Hjemmel feil EPS inntekt." },
                        nynorsk { +"??" },
                        english { +"??" }
                    )
                }
            }.orShowIf(aarsak.equalTo(VedtakOmOmgjoeringDto.Aarsak.endretOpptjening)) {
                paragraph {
                    text(
                        bokmal { +"Hjemmel endret opptjening." },
                        nynorsk { +"??" },
                        english { +"??" }
                    )
                }
            }.orShowIf(aarsak.equalTo(VedtakOmOmgjoeringDto.Aarsak.feilTrygdetid)) {
                paragraph {
                    text(
                        bokmal { +"Hjemmel feil trygdetid." },
                        nynorsk { +"??" },
                        english { +"??" }
                    )
                }
            }

            paragraph {
                text(
                    bokmal { +fritekst("<Gjengivelse av faktum, vis at tilfellet faller inn under lovbestemmelsen som er nevnt ovenfor.>") },
                    nynorsk { +"??" },
                    english { +"??" }
                )
            }

            paragraph {
                text(
                    bokmal { +"Som en følge av dette er tidligere vedtak om " + vedtakOm + " ugyldig etter forvaltningsloven § 35 første ledd bokstav c." },
                    nynorsk { +"??" },
                    english { +"??" }
                )
            }

            paragraph {
                text(
                    bokmal { +"Vi har kommet til at det ugyldige vedtaket skal omgjøres. " + fritekst("<Nevn de momentene du har vurdert, og hvilken vekt du har gitt dem (se rundskrivet til fvl. §35 første ledd bokstav c))>") },
                    nynorsk { +"??" },
                    english { +"??" }
                )
            }

            paragraph {
                text(
                    bokmal { +fritekst("<Si noe om hva resultatet av den nye vurderingen medfører (f.eks. at vi har beregnet pensjonen på nytt, eller at retten til ytelsen er falt bort, at virkningstidspunktet endres til…>") },
                    nynorsk { +"??" },
                    english { +"??" }
                )
            }
        }

        includeAttachment(
            vedleggOrienteringOmRettigheterOgPlikter,
            pesysData.orienteringOmRettigheterOgPlikterDto,
        )
        includeAttachmentIfNotNull(
            vedleggMaanedligPensjonFoerSkatt,
            pesysData.maanedligPensjonFoerSkattDto,
        )
        includeAttachmentIfNotNull(
            vedleggMaanedligPensjonFoerSkattAp2025,
            pesysData.maanedligPensjonFoerSkattAP2025Dto,
        )
    }
}
