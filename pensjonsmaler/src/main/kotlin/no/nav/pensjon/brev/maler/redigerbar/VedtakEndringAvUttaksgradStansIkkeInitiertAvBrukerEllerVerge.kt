package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvUttaksgradStansIkkeBrukerEllerVergeDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvUttaksgradStansIkkeBrukerEllerVergeDto.SaksbehandlerValg.Aarsak
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvUttaksgradStansIkkeBrukerEllerVergeDtoSelectors.AlderspensjonVedVirkSelectors.regelverkType
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvUttaksgradStansIkkeBrukerEllerVergeDtoSelectors.KravSelectors.virkDatoFom
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvUttaksgradStansIkkeBrukerEllerVergeDtoSelectors.PesysDataSelectors.alderspensjonVedVirk
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvUttaksgradStansIkkeBrukerEllerVergeDtoSelectors.PesysDataSelectors.dineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvUttaksgradStansIkkeBrukerEllerVergeDtoSelectors.PesysDataSelectors.krav
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvUttaksgradStansIkkeBrukerEllerVergeDtoSelectors.SaksbehandlerValgSelectors.aarsak
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvUttaksgradStansIkkeBrukerEllerVergeDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvUttaksgradStansIkkeBrukerEllerVergeDtoSelectors.saksbehandlerValg
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlage
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
//000101 i Doksys
object VedtakEndringAvUttaksgradStansIkkeInitiertAvBrukerEllerVerge :
    RedigerbarTemplate<VedtakEndringAvUttaksgradStansIkkeBrukerEllerVergeDto> {
    override val kategori = TemplateDescription.Brevkategori.VEDTAK_ENDRING_OG_REVURDERING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper: Set<Sakstype> = setOf(Sakstype.ALDER)
    override val kode = Pesysbrevkoder.Redigerbar.PE_AP_ENDRET_UTTAKSGRAD_STANS_IKKE_BRUKER_VERGE
    override val template = createTemplate(
        letterDataType = VedtakEndringAvUttaksgradStansIkkeBrukerEllerVergeDto::class,
        languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - endring av uttaksgrad (stans)",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VEDTAK,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title {
            text(
                bokmal { + "Vi stanser utbetalingen av alderspensjonen din" },
                nynorsk { + "Vi stansar utbetalinga av alderspensjonen din" },
                english { + "We are stopping your retirement pension" }
            )
        }

        outline {
            includePhrase(Vedtak.Overskrift)

            showIf(saksbehandlerValg.aarsak.equalTo(Aarsak.ufoeregradErOekt)) {
                paragraph {
                    // stansAPOktUFG_001
                    text(
                        bokmal { + "Vi stanser utbetalingen av alderspensjonen din fra " + pesysData.krav.virkDatoFom.format() + " fordi uføregraden din er endret." },
                        nynorsk { + "Vi stansar utbetalinga av alderspensjonen din frå " + pesysData.krav.virkDatoFom.format() + " fordi uføregraden din er endra." },
                        english { + "We are stopping payment of your retirement pension from " + pesysData.krav.virkDatoFom.format() + " because your degree of disability has changed." },
                    )
                }
            }.orShowIf(saksbehandlerValg.aarsak.equalTo(Aarsak.ufoeretrygdErInnvilget)) {
                paragraph {
                    // stansAPInnvUT_001
                    text(
                        bokmal { + "Vi stanser utbetalingen av alderspensjonen din fra " + pesysData.krav.virkDatoFom.format() + " fordi du har fått innvilget uføretrygd." },
                        nynorsk { + "Vi stansar utbetalinga av alderspensjonen din frå " + pesysData.krav.virkDatoFom.format() + " fordi du har fått innvilga uføretrygd." },
                        english { + "We are stopping payment of your retirement pension from " + pesysData.krav.virkDatoFom.format() + " because you have been granted disability benefit." },
                    )
                }
            }.orShowIf(saksbehandlerValg.aarsak.equalTo(Aarsak.pensjonsopptjeningenErEndret)) {
                paragraph {
                    // stansAPOpptjen_001
                    text(
                        bokmal { + "Vi viser til varselbrevet vi har sendt deg. Nav stanser utbetalingen av alderspensjonen din fra " + pesysData.krav.virkDatoFom.format() + " fordi du ikke lenger har høy nok opptjening." },
                        nynorsk { + "Vi viser til varselbrevet vi har sendt deg. Nav stansar utbetalinga av alderspensjonen din frå " + pesysData.krav.virkDatoFom.format() + " fordi du ikkje lenger har høg nok opptening." },
                        english { + "We refer to the notice letter we sent you. Nav is stopping payment of your retirement pension from " + pesysData.krav.virkDatoFom.format() + " because you no longer have high enough pension earnings." },
                    )
                }
            }

            // stansAPUTgradBegrunn_001_]
            includePhrase(Vedtak.BegrunnelseOverskrift)
            paragraph {
                text(
                    bokmal { + "Du har ikke lenger rett til å ta ut alderspensjon fordi uføregraden din er høyere enn 80 prosent. Vi har derfor stanset alderspensjonen din." },
                    nynorsk { + "Du har ikkje lenger rett til å ta ut alderspensjon fordi uføregraden din er høgare enn 80 prosent. Vi har derfor stansa alderspensjonen din." },
                    english { + "You are no longer entitled to draw your retirement pension because your degree of disability is higher than 80 percent. We have therefore stopped your retirement pension." }
                )
            }
            // TODO Saksbehandlervalg under data-styring. Kan føre til at valg ikke har noen effekt., men valgene gjenbrukes i alle cases, så det går fint.
            showIf(pesysData.alderspensjonVedVirk.regelverkType.equalTo(AlderspensjonRegelverkType.AP2011)) { // radiobutton i doksys
                // endrUtaksgradAP2011_001 - Uføretrygd er innvilget eller uføregrad er økt
                // TODO Saksbehandlervalg under data-styring. Kan føre til at valg ikke har noen effekt.
                showIf(saksbehandlerValg.aarsak.isOneOf(Aarsak.ufoeregradErOekt, Aarsak.ufoeretrygdErInnvilget)) {
                    paragraph {
                        text(
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 19-10, 19-12 og 22-12." },
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 19-10, 19-12 og 22-12." },
                            english { + "This decision was made pursuant to the provisions of §§ 19-10, 19-12 and 22-12 of the National Insurance Act." }
                        )
                    }
                }.orShowIf(saksbehandlerValg.aarsak.equalTo(Aarsak.pensjonsopptjeningenErEndret)) {
                    // avslagAP2011TidligUttakHjemmel_001 - Pensjonsopptjeningen er endret
                    // TODO Saksbehandlervalg under data-styring. Kan føre til at valg ikke har noen effekt.
                    paragraph {
                        text(
                            bokmal { + "Vedtaket er gjort etter folketrygdloven § 19-11." },
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova § 19-11." },
                            english { + "This decision was made pursuant to the provisions of § 19-11 of the National Insurance Act." }
                        )
                    }
                }
            }.orShowIf(pesysData.alderspensjonVedVirk.regelverkType.equalTo(AlderspensjonRegelverkType.AP2016)) { // radiobuttons
                // avslagAP2016TidligUttakHjemmel_001 - Pensjonsopptjeningen er endret
                showIf(saksbehandlerValg.aarsak.equalTo(Aarsak.pensjonsopptjeningenErEndret)) {
                    paragraph {
                        text(
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 19-11, 19-15, 20-15 og 20-19." },
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 19-11, 19-15, 20-15 og 20-19." },
                            english { + "This decision was made pursuant to the provisions of §§ 19-11, 19-15, 20-15 and 20-19 of the National Insurance Act." }
                        )
                    }
                }.orShowIf(saksbehandlerValg.aarsak.isOneOf(Aarsak.ufoeregradErOekt, Aarsak.ufoeretrygdErInnvilget)) {
                    // endrUtaksgradAP2016_001 - Uføretrygd er innvilget eller uføregrad er økt
                    paragraph {
                        text(
                            bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 19-10, 19-12, 19-15, 20-14, 20-16, 20-19 og 22-12." },
                            nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 19-10, 19-12, 19-15, 20-14, 20-16, 20-19 og 22-12." },
                            english { + "This decision was made pursuant to the provisions of §§ 19-10, 19-12, 19-15, 20-14, 20-16, 20-19 and 22-12 of the National Insurance Act." }
                        )
                    }
                }
            }.orShowIf(pesysData.alderspensjonVedVirk.regelverkType.equalTo(AlderspensjonRegelverkType.AP2025)) { // radiobuttons
                    // avslagAP2025TidligUttakHjemmel_001 - Pensjonsopptjeningen er endret
                    showIf(saksbehandlerValg.aarsak.equalTo(Aarsak.pensjonsopptjeningenErEndret)) {
                        paragraph {
                            text(
                                bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 20-15 og 22-13." },
                                nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 20-15 og 22-13." },
                                english { + "This decision was made pursuant to the provisions of §§ 20-15 and 22-13 of the National Insurance Act." }
                            )
                        }
                    }.orShowIf(saksbehandlerValg.aarsak.isOneOf(Aarsak.ufoeregradErOekt, Aarsak.ufoeretrygdErInnvilget)) {
                        // endrUtaksgradAP2025_001 - Uføretrygd er innvilget eller uføregrad er økt
                        paragraph {
                            text(
                                bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 20-14 og 20-16." },
                                nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 20-14 og 20-16." },
                                english { + "This decision was made pursuant to the provisions of §§ 20-14 and 20-16 of the National Insurance Act." }
                            )
                        }
                    }
                }

            //TODO: Denne bør bruke Felles.DuHarRettTilAaKlage, men formuleringa er per nå litt ulik
            title1 {
                text(
                    bokmal { + "Du har rett til å klage" },
                    nynorsk { + "Du har rett til å klage" },
                    english { + "You have the right to appeal" }
                )
            }
            paragraph {
                text(
                    bokmal { + "Hvis du mener vedtaket er feil, kan du klage innen seks uker fra den datoen du fikk vedtaket. Klagen skal være skriftlig. Du finner skjema og informasjon på ${Constants.KLAGE_URL}." },
                    nynorsk { + "Om du meiner at vedtaket er feil, kan du klage innan seks veker frå den datoen du fekk vedtaket. Klaga skal vera skriftleg. Du finn skjema og informasjon på ${Constants.KLAGE_URL}." },
                    english { + "If you think the decision is wrong, you may appeal the decision within six weeks of the date on which you received notice of the decision. Your appeal must be made in writing. You will find a form you can use and more information about appeals at ${Constants.KLAGE_URL}." }
                )
            }
            paragraph {
                text(
                    bokmal { + "I vedlegget får du vite mer om hvordan du går fram." },
                    nynorsk { + "I vedlegget får du vite meir om korleis du går fram." },
                    english { + "The attachment includes information on how to proceed." }
                )
            }
            includePhrase(Felles.RettTilInnsyn(vedleggDineRettigheterOgMulighetTilAaKlage))
            includePhrase(Felles.HarDuSpoersmaal.alder)

        }
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlage, pesysData.dineRettigheterOgMulighetTilAaKlageDto)
    }
}