package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvUttaksgradStansBrukerEllerVergeDtoSelectors.pesysData
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvUttaksgradStansBrukerEllerVergeDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvUttaksgradStansBrukerEllerVergeDtoSelectors.AlderspensjonVedVirkSelectors.regelverkType
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvUttaksgradStansBrukerEllerVergeDtoSelectors.AlderspensjonVedVirkSelectors.skjermingstilleggInnvilget
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvUttaksgradStansBrukerEllerVergeDtoSelectors.KravSelectors.virkDatoFom
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvUttaksgradStansBrukerEllerVergeDtoSelectors.PesysDataSelectors.alderspensjonVedVirk
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvUttaksgradStansBrukerEllerVergeDtoSelectors.PesysDataSelectors.dineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.VedtakEndringAvUttaksgradStansBrukerEllerVergeDtoSelectors.PesysDataSelectors.krav
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlage
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object VedtakEndringAvUttaksgradStansInitiertAvBrukerEllerVerge :
    RedigerbarTemplate<VedtakEndringAvUttaksgradStansBrukerEllerVergeDto> {

    override val kategori = TemplateDescription.Brevkategori.VEDTAK_ENDRING_OG_REVURDERING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper: Set<Sakstype> = setOf(Sakstype.ALDER)
    override val kode = Pesysbrevkoder.Redigerbar.PE_AP_ENDRET_UTTAKSGRAD_STANS_BRUKER_ELLER_VERGE
    override val template = createTemplate(
        languages = languages(Language.Bokmal, Language.Nynorsk, Language.English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - endring av uttaksgrad (stans)",
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
            // stansAPInnledn_001
            paragraph {
                text(
                    bokmal { + "Vi viser til søknaden din, og stanser utbetalingen av alderspensjonen fra " + pesysData.krav.virkDatoFom.format() + "." },
                    nynorsk { + "Vi viser til søknaden din, og stansar utbetalinga av alderspensjonen frå " + pesysData.krav.virkDatoFom.format() + "." },
                    english { + "This is in reference to your application. We are stopping payment of your retirement pension from " + pesysData.krav.virkDatoFom.format() + "." },
                )
            }

            showIf(pesysData.alderspensjonVedVirk.skjermingstilleggInnvilget) {
                // fortsattSkjermingstillegg_001
                paragraph {
                    text(
                        bokmal { + "Du får fortsatt utbetalt skjermingstillegget til uføre. Vedtaket er gjort etter folketrygdloven §§ 19-9a, 19-10 og 19-12." },
                        nynorsk { + "Du får fortsatt utbetalt skjermingstillegget til uføre. Vedtaket er gjort etter folketrygdlova §§ 19-9a, 19-10 og 19-12." },
                        english { + "You will still receive the supplement for disabled people. This decision was made pursuant to the provisions of §§ 19-9a, 19-10 and 19-12 of the National Insurance Act." }
                    )
                }
            }.orShowIf(pesysData.alderspensjonVedVirk.regelverkType.equalTo(AlderspensjonRegelverkType.AP2011)) {
                // endrUtaksgradAP2011_001
                paragraph {
                    text(
                        bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 19-10, 19-12 og 22-12." },
                        nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 19-10, 19-12 og 22-12." },
                        english { + "This decision was made pursuant to the provisions of §§ 19-10, 19-12 and 22-12 of the National Insurance Act." }
                    )
                }
            }.orShowIf(pesysData.alderspensjonVedVirk.regelverkType.equalTo(AlderspensjonRegelverkType.AP2016)) {
                // endrUtaksgradAP2016_001
                paragraph {
                    text(
                        bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 19-10, 19-12, 19-15, 20-14, 20-16, 20-19 og 22-12." },
                        nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 19-10, 19-12, 19-15, 20-14, 20-16, 20-19 og 22-12." },
                        english { + "This decision was made pursuant to the provisions of §§ 19-10, 19-12, 19-15, 20-14, 20-16, 20-19 and 22-12 of the National Insurance Act." }
                    )
                }
            }.orShowIf(pesysData.alderspensjonVedVirk.regelverkType.equalTo(AlderspensjonRegelverkType.AP2025)) {
                // endrUtaksgradAP2025Soknad_001
                paragraph {
                    text(
                        bokmal { + "Vedtaket er gjort etter folketrygdloven §§ 20-14, 20-16 og 22-13." },
                        nynorsk { + "Vedtaket er gjort etter folketrygdlova §§ 20-14, 20-16 og 22-13." },
                        english { + "This decision was made pursuant to the provisions of §§ 20-14, 20-16 and 22-13 of the National Insurance Act." }
                    )
                }
            }

            paragraph {
                text(
                    bokmal { + "Du må sende oss en ny søknad når du ønsker å ta ut alderspensjon. En eventuell endring kan tidligst skje måneden etter at vi har mottatt søknaden." },
                    nynorsk { + "Du må sende oss ein ny søknad når du ønskjer å ta ut alderspensjonen. Ei eventuell endring kan tidlegast skje månaden etter at vi har mottatt søknaden." },
                    english { + "You have to submit an application when you want to start drawing your retirement pension. Any change will be implemented at the earliest the month after we have received the application." }
                )
            }

            includePhrase(Felles.RettTilAAKlage)
            includePhrase(Felles.RettTilInnsyn(vedleggDineRettigheterOgMulighetTilAaKlage))
            includePhrase(Felles.HarDuSpoersmaal.alder)

        }
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlage, pesysData.dineRettigheterOgMulighetTilAaKlageDto)
    }
}