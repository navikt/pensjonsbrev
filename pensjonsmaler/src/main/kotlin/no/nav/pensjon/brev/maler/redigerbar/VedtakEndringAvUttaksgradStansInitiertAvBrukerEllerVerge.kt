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
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.fraser.common.Felles
import no.nav.pensjon.brev.maler.fraser.common.Vedtak
import no.nav.pensjon.brev.maler.vedlegg.vedleggDineRettigheterOgMulighetTilAaKlage
import no.nav.pensjon.brev.template.Language
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object VedtakEndringAvUttaksgradStansInitiertAvBrukerEllerVerge :
    RedigerbarTemplate<VedtakEndringAvUttaksgradStansBrukerEllerVergeDto> {
    override val kategori = TemplateDescription.Brevkategori.VEDTAK_ENDRING_OG_REVURDERING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper: Set<Sakstype> = setOf(Sakstype.ALDER)
    override val kode = Pesysbrevkoder.Redigerbar.PE_AP_ENDRET_UTTAKSGRAD_STANS_BRUKER_ELLER_VERGE
    override val template = createTemplate(
        name = kode.name,
        letterDataType = VedtakEndringAvUttaksgradStansBrukerEllerVergeDto::class,
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
                Language.Bokmal to "Vi stanser utbetalingen av alderspensjonen din",
                Language.Nynorsk to "Vi stansar utbetalinga av alderspensjonen din",
                Language.English to "We are stopping your retirement pension"
            )
        }

        outline {
            includePhrase(Vedtak.Overskrift)
            // stansAPInnledn_001
            paragraph {
                textExpr(
                    Language.Bokmal to "Vi viser til søknaden din, og stanser utbetalingen av alderspensjonen fra ".expr() + pesysData.krav.virkDatoFom.format() + ".",
                    Language.Nynorsk to "Vi viser til søknaden din, og stansar utbetalinga av alderspensjonen frå ".expr() + pesysData.krav.virkDatoFom.format() + ".",
                    Language.English to "This is in reference to your application. We are stopping payment of your retirement pension from ".expr() + pesysData.krav.virkDatoFom.format() + ".",
                )
            }

            showIf(pesysData.alderspensjonVedVirk.skjermingstilleggInnvilget) {
                // fortsattSkjermingstillegg_001
                paragraph {
                    text(
                        Language.Bokmal to "Du får fortsatt utbetalt skjermingstillegget til uføre. Vedtaket er gjort etter folketrygdloven §§ 19-9a, 19-10 og 19-12.",
                        Language.Nynorsk to "Du får fortsatt utbetalt skjermingstillegget til uføre. Vedtaket er gjort etter folketrygdlova §§ 19-9a, 19-10 og 19-12.",
                        Language.English to "You will still receive the supplement for disabled people. This decision was made pursuant to the provisions of §§ 19-9a, 19-10 and 19-12 of the National Insurance Act."
                    )
                }
            }.orShowIf(pesysData.alderspensjonVedVirk.regelverkType.equalTo(AlderspensjonRegelverkType.AP2011)) {
                // endrUtaksgradAP2011_001
                paragraph {
                    text(
                        Language.Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 19-10, 19-12 og 22-12.",
                        Language.Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 19-10, 19-12 og 22-12.",
                        Language.English to "This decision was made pursuant to the provisions of §§ 19-10, 19-12 and 22-12 of the National Insurance Act."
                    )
                }
            }.orShowIf(pesysData.alderspensjonVedVirk.regelverkType.equalTo(AlderspensjonRegelverkType.AP2016)) {
                // endrUtaksgradAP2016_001
                paragraph {
                    text(
                        Language.Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 19-10, 19-12, 19-15, 20-14, 20-16, 20-19 og 22-12.",
                        Language.Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 19-10, 19-12, 19-15, 20-14, 20-16, 20-19 og 22-12.",
                        Language.English to "This decision was made pursuant to the provisions of §§ 19-10, 19-12, 19-15, 20-14, 20-16, 20-19 and 22-12 of the National Insurance Act."
                    )
                }
            }.orShowIf(pesysData.alderspensjonVedVirk.regelverkType.equalTo(AlderspensjonRegelverkType.AP2025)) {
                // endrUtaksgradAP2025Soknad_001
                paragraph {
                    text(
                        Language.Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 20-14, 20-16 og 22-13.",
                        Language.Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 20-14, 20-16 og 22-13.",
                        Language.English to "This decision was made pursuant to the provisions of §§ 20-14, 20-16 and 22-13 of the National Insurance Act."
                    )
                }
            }

            paragraph {
                text(
                    Language.Bokmal to "Du må sende oss en ny søknad når du ønsker å ta ut alderspensjon. En eventuell endring kan tidligst skje måneden etter at vi har mottatt søknaden.",
                    Language.Nynorsk to "Du må sende oss ein ny søknad når du ønskjer å ta ut alderspensjonen. Ei eventuell endring kan tidlegast skje månaden etter at vi har mottatt søknaden.",
                    Language.English to "You have to submit an application when you want to start drawing your retirement pension. Any change will be implemented at the earliest the month after we have received the application."
                )
            }

            //TODO: Denne bør bruke Felles.DuHarRettTilAaKlage, men formuleringa er per nå litt ulik
            title1 {
                text(
                    Language.Bokmal to "Du har rett til å klage",
                    Language.Nynorsk to "Du har rett til å klage",
                    Language.English to "You have the right to appeal"
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "Hvis du mener vedtaket er feil, kan du klage innen seks uker fra den datoen du fikk vedtaket. Klagen skal være skriftlig. Du finner skjema og informasjon på ${Constants.KLAGE_URL}.",
                    Language.Nynorsk to "Om du meiner at vedtaket er feil, kan du klage innan seks veker frå den datoen du fekk vedtaket. Klaga skal vera skriftleg. Du finn skjema og informasjon på ${Constants.KLAGE_URL}.",
                    Language.English to "If you think the decision is wrong, you may appeal the decision within six weeks of the date on which you received notice of the decision. Your appeal must be made in writing. You will find a form you can use and more information about appeals at ${Constants.KLAGE_URL}."
                )
            }
            paragraph {
                text(
                    Language.Bokmal to "I vedlegget får du vite mer om hvordan du går fram.",
                    Language.Nynorsk to "I vedlegget får du vite meir om korleis du går fram.",
                    Language.English to "The attachment includes information on how to proceed."
                )
            }
            includePhrase(Felles.RettTilInnsyn(vedleggDineRettigheterOgMulighetTilAaKlage))
            includePhrase(Felles.HarDuSpoersmaal.alder)

        }
        includeAttachment(vedleggDineRettigheterOgMulighetTilAaKlage, pesysData.dineRettigheterOgMulighetTilAaKlageDto)
    }
}