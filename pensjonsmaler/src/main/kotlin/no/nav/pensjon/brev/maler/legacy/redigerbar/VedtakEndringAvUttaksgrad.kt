package no.nav.pensjon.brev.maler.legacy.redigerbar

import no.nav.pensjon.brev.api.model.AlderspensjonRegelverkType
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvUttaksgradDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvUttaksgradDto.KravInitiertAv.*
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvUttaksgradDtoSelectors.AlderspensjonVedVirkSelectors.opphortBarnetillegg
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvUttaksgradDtoSelectors.AlderspensjonVedVirkSelectors.opphortEktefelletillegg
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvUttaksgradDtoSelectors.AlderspensjonVedVirkSelectors.privatAFPErBrukt
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvUttaksgradDtoSelectors.AlderspensjonVedVirkSelectors.regelverkType
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvUttaksgradDtoSelectors.AlderspensjonVedVirkSelectors.totalPensjon
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvUttaksgradDtoSelectors.AlderspensjonVedVirkSelectors.uforeKombinertMedAlder
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvUttaksgradDtoSelectors.AlderspensjonVedVirkSelectors.uttaksgrad
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvUttaksgradDtoSelectors.BeregnetPensjonPerManedSelectors.antallBeregningsperioderPensjon
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvUttaksgradDtoSelectors.KravSelectors.kravInitiertAv
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvUttaksgradDtoSelectors.KravSelectors.virkDatoFom
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvUttaksgradDtoSelectors.PesysDataSelectors.alderspensjonVedVirk
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvUttaksgradDtoSelectors.PesysDataSelectors.beregnetPensjonPerManed
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvUttaksgradDtoSelectors.PesysDataSelectors.krav
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvUttaksgradDtoSelectors.PesysDataSelectors.maanedligPensjonFoerSkattDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvUttaksgradDtoSelectors.PesysDataSelectors.orienteringOmRettigheterOgPlikterDto
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.VedtakEndringAvUttaksgradDtoSelectors.pesysData
import no.nav.pensjon.brev.maler.fraser.common.Constants.UTBETALINGER_URL
import no.nav.pensjon.brev.maler.vedlegg.vedleggMaanedligPensjonFoerSkatt
import no.nav.pensjon.brev.maler.vedlegg.vedleggOrienteringOmRettigheterOgPlikter
import no.nav.pensjon.brev.model.format
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.format
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.isNotAnyOf
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.text
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.LetterMetadata

@TemplateModelHelpers
object VedtakEndringAvUttaksgrad : RedigerbarTemplate<VedtakEndringAvUttaksgradDto> {
    override val kategori = TemplateDescription.Brevkategori.VEDTAK_ENDRING_OG_REVURDERING
    override val brevkontekst = TemplateDescription.Brevkontekst.VEDTAK
    override val sakstyper: Set<Sakstype> = setOf(Sakstype.ALDER)
    override val kode = Pesysbrevkoder.Redigerbar.PE_AP_ENDRET_UTTAKSGRAD
    override val template = createTemplate(
        name = kode.name,
        letterDataType = VedtakEndringAvUttaksgradDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - endring av uttaksgrad",
            isSensitiv = false,
            distribusjonstype = LetterMetadata.Distribusjonstype.VIKTIG,
            brevtype = LetterMetadata.Brevtype.VEDTAKSBREV,
        )
    ) {
        title {
            // innvilgelseAPTittel_001
            showIf(pesysData.krav.kravInitiertAv.isOneOf(BRUKER, VERGE)) {
                textExpr(
                    Bokmal to "Vi har innvilget søknaden din om ".expr() + pesysData.alderspensjonVedVirk.uttaksgrad.format() + " prosent alderspensjon",
                    Nynorsk to "Vi har innvilga søknaden din om ".expr() + pesysData.alderspensjonVedVirk.uttaksgrad.format() + " prosent alderspensjon",
                    English to "We have granted your application for ".expr() + pesysData.alderspensjonVedVirk.uttaksgrad.format() + " percent retirement pension",
                )
            }.orShow {
                // endringAPTittel_002
                text(
                    Bokmal to "Vi endrer alderspensjonen din",
                    Nynorsk to "Vi endrar alderspensjonen din",
                    English to "We are altering your retirement pension"
                )
            }
        }

        outline {
            // vedtakOverskriftPesys_001
            title1 {
                text(
                    Bokmal to "Vedtak",
                    Nynorsk to "Vedtak",
                    English to "Decision"
                )
            }

            // Velg årsak til endring
            showIf(pesysData.alderspensjonVedVirk.uforeKombinertMedAlder and pesysData.krav.kravInitiertAv.isNotAnyOf(BRUKER, VERGE)) {
                paragraph {
                    // endringGradAPOktUFGInnledn_001
                    text(
                        Bokmal to "Vi endrer alderspensjonen din fordi uføregraden din har økt.",
                        Nynorsk to "Vi endrar alderspensjonen din fordi uføregraden din har auka.",
                        English to "We are altering your retirement pension because your degree of disability has increased."
                    )
                }

                paragraph {
                    // endringGradAPInnvUTInnledn_001
                    text(
                        Bokmal to "Vi endrer alderspensjonen din fordi du har fått innvilget uføretrygd.",
                        Nynorsk to "Vi endrar alderspensjonen din fordi du har fått innvilga uføretrygd.",
                        English to "We are altering your retirement pension because you have been granted disability benefit."
                    )
                }
            }

            showIf(not(pesysData.alderspensjonVedVirk.uforeKombinertMedAlder) and pesysData.krav.kravInitiertAv.isNotAnyOf(BRUKER, VERGE)) {
                paragraph {
                    // endringAPOpptjenInnledn_001
                    text(
                        Bokmal to "Vi viser til varselbrevet vi har sendt deg. Vi endrer alderspensjonen din fordi du ikke lenger har høy nok opptjening.",
                        Nynorsk to "Vi viser til varselbrevet vi har sendt deg. Vi endrar alderspensjonen din fordi du ikkje lenger har høg nok opptening.",
                        English to "Please refer to the notice letter we sent you. We are changing your retirement pension because you no longer have high enough pensionable earnings."
                    )
                }
            }

            showIf(pesysData.alderspensjonVedVirk.uforeKombinertMedAlder) {
                // Phrase innvilgelseAPogUTInnledn_001
                paragraph {
                    textExpr(
                        Bokmal to "Du får ".expr() + pesysData.alderspensjonVedVirk.totalPensjon.format() + " kroner hver måned før skatt fra " + pesysData.krav.virkDatoFom.format() + ". Du får alderspensjon fra folketrygden i tillegg til uføretrygden din.",
                        Nynorsk to "Du får ".expr() + pesysData.alderspensjonVedVirk.totalPensjon.format() + " kroner kvar månad før skatt frå " + pesysData.krav.virkDatoFom.format() + ". Du får alderspensjon frå folketrygda ved sida av uføretrygda di.",
                        English to "You will receive NOK ".expr() + pesysData.alderspensjonVedVirk.totalPensjon.format() + " every month before tax from " + pesysData.krav.virkDatoFom.format() + ". You will receive retirement pension through the National Insurance Scheme in addition to your disability benefit.",
                    )
                }
            }.orShow {
                // innvilgelseAPInnledn_001
                paragraph {
                    textExpr(
                        Bokmal to "Du får ".expr() + pesysData.alderspensjonVedVirk.totalPensjon.format() + " kroner hver måned før skatt fra " + pesysData.krav.virkDatoFom.format() + " i alderspensjon fra folketrygden.",
                        Nynorsk to "Du får ".expr() + pesysData.alderspensjonVedVirk.totalPensjon.format() + " kroner kvar månad før skatt frå " + pesysData.krav.virkDatoFom.format() + " i alderspensjon frå folketrygda.",
                        English to "You will receive NOK ".expr() + pesysData.alderspensjonVedVirk.totalPensjon.format() + " every month before tax from " + pesysData.krav.virkDatoFom.format() + " as retirement pension from the National Insurance Scheme.",
                    )
                }
            }

            showIf(pesysData.alderspensjonVedVirk.privatAFPErBrukt) {
                // innvilgelseAPogAFPPrivat_001
                paragraph {
                    textExpr(
                        Bokmal to "Du får ".expr() + pesysData.alderspensjonVedVirk.uttaksgrad.format() + " prosent alderspensjon fordi summen av alderspensjonen og den avtalefestede pensjonen din (AFP) gjør at du har rett til alderspensjon før du fyller 67 år.",
                        Nynorsk to "Du får ".expr() + pesysData.alderspensjonVedVirk.uttaksgrad.format() + " prosent alderspensjon fordi summen av alderspensjonen og den avtalefesta pensjonen din (AFP) gjer at du har rett til alderspensjon før 67 år.",
                        English to "You have been granted ".expr() + pesysData.alderspensjonVedVirk.uttaksgrad.format() + " percent retirement pension because your total retirement pension and contractual early retirement pension (AFP) makes you eligible for retirement pension before the age of 67."
                    )
                }
            }

            paragraph {
                // utbetalingsInfoMndUtbet_001
                text(
                    Bokmal to "Hvis du har andre pensjonsytelser som for eksempel AFP eller tjenestepensjon, blir de utbetalt i tillegg til alderspensjonen. Alderspensjonen din utbetales innen den 20. hver måned. Du finner oversikt over utbetalingene dine på $UTBETALINGER_URL.",
                    Nynorsk to "Dersom du har andre pensjonsytingar som for eksempel AFP eller tenestepensjon, kjem slik utbetaling i tillegg til alderspensjonen. Alderspensjonen din blir betalt ut innan den 20. i kvar månad. Du finn meir informasjon om utbetalingane dine på $UTBETALINGER_URL.",
                    English to "If you have occupational pensions from other schemes, this will be paid in addition to your retirement pension. Your pension will be paid at the latest on the 20th of each month. See the more detailed information on what you will receive at $UTBETALINGER_URL."
                )
            }

            showIf(pesysData.beregnetPensjonPerManed.antallBeregningsperioderPensjon.greaterThan(1) and pesysData.alderspensjonVedVirk.totalPensjon.greaterThan(0)) {
                // flereBeregningsperioderVedlegg_001
                paragraph {
                    text(
                        Bokmal to "Du kan lese mer om andre beregningsperioder i vedlegget.",
                        Nynorsk to "Du kan lese meir om andre berekningsperiodar i vedlegget.",
                        English to "There is more information about other calculation periods in the attachment."
                    )
                }
            }

            showIf(pesysData.alderspensjonVedVirk.regelverkType.equalTo(AlderspensjonRegelverkType.AP2011)) {
                //  endrUtaksgradAP2011_001
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 19-10, 19-12 og 22-12.",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 19-10, 19-12 og 22-12.",
                        English to "This decision was made pursuant to the provisions of §§ 19-10, 19-12 and 22-12 of the National Insurance Act."
                    )
                }
            }.orShowIf(pesysData.alderspensjonVedVirk.regelverkType.equalTo(AlderspensjonRegelverkType.AP2016)) {
                // endrUtaksgradAP2016_001
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 19-10, 19-12, 19-15, 20-14, 20-16, 20-19 og 22-12.",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 19-10, 19-12, 19-15, 20-14, 20-16, 20-19 og 22-12.",
                        English to "This decision was made pursuant to the provisions of §§ 19-10, 19-12, 19-15, 20-14, 20-16, 20-19 and 22-12 of the National Insurance Act."
                    )
                }
            }.orShowIf(pesysData.alderspensjonVedVirk.regelverkType.equalTo(AlderspensjonRegelverkType.AP2025) and pesysData.krav.kravInitiertAv.isOneOf(BRUKER, VERGE)) {
                // endrUtaksgradAP2025Soknad_001
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 20-14, 20-16 og 22-13.",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 20-14, 20-16 og 22-13.",
                        English to "This decision was made pursuant to the provisions of §§ 20-14, 20-16 and 22-13 of the National Insurance Act."
                    )
                }
            }.orShowIf(pesysData.alderspensjonVedVirk.regelverkType.equalTo(AlderspensjonRegelverkType.AP2025) and pesysData.krav.kravInitiertAv.isOneOf(NAV, SOSIALKONTOR, KONV, ADVOKAT)) {
                // endrUtaksgradAP2025_001
                paragraph {
                    text(
                        Bokmal to "Vedtaket er gjort etter folketrygdloven §§ 20-14 og 20-16.",
                        Nynorsk to "Vedtaket er gjort etter folketrygdlova §§ 20-14 og 20-16.",
                        English to "This decision was made pursuant to the provisions of §§ 20-14 and 20-16 of the National Insurance Act."
                    )
                }
            }

            showIf(pesysData.krav.kravInitiertAv.isOneOf(BRUKER, VERGE) and pesysData.alderspensjonVedVirk.opphortEktefelletillegg and not(pesysData.alderspensjonVedVirk.opphortBarnetillegg)) {
                paragraph {
                    // opphorETBegrunn_001
                    textExpr(
                        Bokmal to "Ektefelletillegget opphører fra ".expr() + pesysData.krav.virkDatoFom.format() + " fordi du bare kan få dette tillegget når du har 100 prosent alderspensjon. Dette går fram av folketrygdloven § 3-24.",
                        Nynorsk to "Ektefelletillegget vert avslutta frå ".expr() + pesysData.krav.virkDatoFom.format() + " fordi du berre kan få dette tillegget når du har 100 prosent alderspensjon. Dette går fram av folketrygdlova § 3-24.",
                        English to "You will no longer receive spouse supplement from ".expr() + pesysData.krav.virkDatoFom.format() + " because you can only receive this supplement when you are receiving a full (100 percent) retirement pension. This follows from section 3-24 of the National Insurance Act."
                    )
                }
            }.orShowIf(pesysData.krav.kravInitiertAv.isOneOf(BRUKER, VERGE) and not(pesysData.alderspensjonVedVirk.opphortEktefelletillegg) and pesysData.alderspensjonVedVirk.opphortBarnetillegg) {
                // opphorBTBegrunn_001
                paragraph {
                    textExpr(
                        Bokmal to "Barnetillegget opphører fra ".expr() + pesysData.krav.virkDatoFom.format() + " fordi du bare kan få dette tillegget når du har 100 prosent alderspensjon. Dette går fram av folketrygdloven § 3-25.",
                        Nynorsk to "Barnetillegget vert avslutta frå ".expr() + pesysData.krav.virkDatoFom.format() + " fordi du berre kan få dette tillegget når du har 100 prosent alderspensjon. Dette går fram av folketrygdlova § 3-25.",
                        English to "You will no longer receive child supplement from ".expr() + pesysData.krav.virkDatoFom.format() + " because you can only receive this supplement when you are receiving a full (100 percent) retirement pension. This follows from section 3-25 of the National Insurance Act."
                    )
                }
            }.orShowIf(pesysData.krav.kravInitiertAv.isOneOf(BRUKER, VERGE) and pesysData.alderspensjonVedVirk.opphortEktefelletillegg and pesysData.alderspensjonVedVirk.opphortBarnetillegg) {
                // opphorBTETBegrunn_001
                paragraph {
                    textExpr(
                        Bokmal to "Barne- og ektefelletillegget opphører fra ".expr() + pesysData.krav.virkDatoFom.format() + " fordi du må ha 100 prosent alderspensjon for å ha rett til disse tilleggene. Dette går fram av folketrygdloven §§ 3-25 og 3-24.",
                        Nynorsk to "Barne- og ektefelletillegget vert avslutta frå ".expr() + pesysData.krav.virkDatoFom.format() + " fordi du må ha 100 prosent alderspensjon for å ha rett til desse tillegga. Dette går fram av folketrygdlova §§ 3-25 og 3-24.",
                        English to "You will no longer receive child supplement and spouse supplement from ".expr() + pesysData.krav.virkDatoFom.format() + " because you must be receiving a full (100 percent) retirement pension to be entitled to these supplements. This follows from sections 3-25 and 3-24 of the National Insurance Act."
                    )
                }
            }




            }

        includeAttachment(vedleggOrienteringOmRettigheterOgPlikter, pesysData.orienteringOmRettigheterOgPlikterDto)
        includeAttachment(vedleggMaanedligPensjonFoerSkatt, pesysData.maanedligPensjonFoerSkattDto)
        // v10
        // v5

    }

}