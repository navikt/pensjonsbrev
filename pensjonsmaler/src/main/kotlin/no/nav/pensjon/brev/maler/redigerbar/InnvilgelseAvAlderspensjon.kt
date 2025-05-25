package no.nav.pensjon.brev.maler.redigerbar

import no.nav.pensjon.brev.api.model.MetaforceSivilstand.ENKE
import no.nav.pensjon.brev.api.model.MetaforceSivilstand.ENSLIG
import no.nav.pensjon.brev.api.model.MetaforceSivilstand.GIFT
import no.nav.pensjon.brev.api.model.MetaforceSivilstand.PARTNER
import no.nav.pensjon.brev.api.model.MetaforceSivilstand.SAMBOER_1_5
import no.nav.pensjon.brev.api.model.MetaforceSivilstand.SAMBOER_3_2
import no.nav.pensjon.brev.api.model.MetaforceSivilstand.UKJENT
import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.Sakstype.*
import no.nav.pensjon.brev.api.model.TemplateDescription
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkategori.FOERSTEGANGSBEHANDLING
import no.nav.pensjon.brev.api.model.TemplateDescription.Brevkontekst.ALLE
import no.nav.pensjon.brev.api.model.maler.Pesysbrevkoder
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDto
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.AlderspensjonVedVirkSelectors.uttaksgrad
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.BarnetilleggVedVirkSelectors.innvilgetBarnetilleggFellesbarn_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.BarnetilleggVedVirkSelectors.innvilgetBarnetilleggSaerkullsbarn_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.BeregnetPensjonPerManedVedVirkSelectors.barnetilleggFellesbarn_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.BeregnetPensjonPerManedVedVirkSelectors.barnetilleggSaerkullsbarn_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.EktefelletilleggVedVirkSelectors.innvilgetEktefelletillegg_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.alderspensjonVedVirk
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.barnetilleggVedVirk
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.beregnetPensjonPerManedVedVirk
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.beregnetPensjonPerManedVedVirk_safe
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.ektefelletilleggVedVirk
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.regelverkType
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.sakstype
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.PesysDataSelectors.sivilstand
import no.nav.pensjon.brev.api.model.maler.redigerbar.InnvilgelseAvAlderspensjonDtoSelectors.pesysData
import no.nav.pensjon.brev.maler.fraser.common.Redigerbar.SaksType
import no.nav.pensjon.brev.model.bestemtForm
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.RedigerbarTemplate
import no.nav.pensjon.brev.template.dsl.createTemplate
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.expression.greaterThan
import no.nav.pensjon.brev.template.dsl.expression.ifElse
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.isNull
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.expression.plus
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.languages
import no.nav.pensjon.brev.template.dsl.textExpr
import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.LetterMetadata
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Distribusjonstype.VEDTAK
import no.nav.pensjon.brevbaker.api.model.LetterMetadata.Brevtype.VEDTAKSBREV



@TemplateModelHelpers
object InnvilgelseAvAlderspensjon : RedigerbarTemplate<InnvilgelseAvAlderspensjonDto> {
    override val kategori: TemplateDescription.Brevkategori = FOERSTEGANGSBEHANDLING
    override val brevkontekst: TemplateDescription.Brevkontekst = ALLE
    override val sakstyper: Set<Sakstype> = setOf(ALDER)
    override val kode = Pesysbrevkoder.Redigerbar.PE_AP_INNVILGELSE
    override val template = createTemplate(
        name = kode.name,
        letterDataType = InnvilgelseAvAlderspensjonDto::class,
        languages = languages(Bokmal, Nynorsk, English),
        letterMetadata = LetterMetadata(
            displayTitle = "Vedtak - innvilgelse av alderspensjon",
            isSensitiv = false,
            distribusjonstype = VEDTAK,
            brevtype = VEDTAKSBREV
        )
    ) {
        val uttaksgrad = pesysData.alderspensjonVedVirk.uttaksgrad.ifNull(then = (0))
        val regelverkType = pesysData.regelverkType
        val innvilgetEktefelletillegg =
            pesysData.ektefelletilleggVedVirk.innvilgetEktefelletillegg_safe.ifNull(then = false)
        val innvilgetBarnetilleggSaerkullsbarn =
            pesysData.barnetilleggVedVirk.innvilgetBarnetilleggSaerkullsbarn_safe.ifNull(then = false)
        val innvilgetBarnetilleggFellesbarn =
            pesysData.barnetilleggVedVirk.innvilgetBarnetilleggFellesbarn_safe.ifNull(then = false)
        val ektefelletillegg = pesysData.beregnetPensjonPerManedVedVirk_safe.ifNull(then = Kroner(0))
        val barnetilleggFellesbarn =
            pesysData.beregnetPensjonPerManedVedVirk.barnetilleggFellesbarn_safe.ifNull(then = (0))
        val barnetilleggSaerkullsbarn =
            pesysData.beregnetPensjonPerManedVedVirk.barnetilleggSaerkullsbarn_safe.ifNull(then = (0))
        val sivilstand = pesysData.sivilstand
        val sivilstandBestemtStorBokstav = pesysData.sivilstand.bestemtForm(storBokstav = true)
        val sivilstandBestemtLitenBokstav = pesysData.sivilstand.bestemtForm(storBokstav = false)




        title {
            textExpr(
                Bokmal to "Vi har innvilget søknaden din om ".expr() + uttaksgrad + " prosent alderspensjon",
                Nynorsk to "Vi har innvilga søknaden din om ".expr() + uttaksgrad + " prosent alderspensjon",
                English to "We have granted your application for ".expr() + uttaksgrad + " percent retirement pension",
            )
            includePhrase(SaksType(pesysData.sakstype))
        }

        outline {

            showIf(
                (innvilgetEktefelletillegg and ektefelletillegg.greaterThan(0))
                        and not(innvilgetBarnetilleggFellesbarn)
                        and not(innvilgetBarnetilleggSaerkullsbarn)
            ) {
                // innvETAP
                paragraph {
                    val navn = fritekst("navn")
                    val beloep = fritekst("beløp")
                    textExpr(
                        Bokmal to "I tillegg til alderspensjonen får du også ektefelletillegg for ".expr() + navn + "." +
                                " Hvis den samlede inntekten din blir høyere enn ".expr() + beloep + " kroner vil vi redusere dette tillegget.",
                        Nynorsk to "I tillegg til alderspensjonen får du også ektefelletillegg for ".expr() + navn + "." +
                                " Dersom den samla inntekta di blir høgare enn ".expr() + beloep + " kroner vil vi redusere dette tillegget.",
                        English to "In addition to retirement pension, you are also entitled to a spouse supplement for ".expr() + navn + "." +
                                " If your total income exceeds NOK ".expr() + beloep + ", this supplement will be reduced."
                    )
                }
            }.orShowIf(
                (innvilgetBarnetilleggFellesbarn and barnetilleggFellesbarn.notEqualTo(0))
                        or (innvilgetBarnetilleggSaerkullsbarn and barnetilleggSaerkullsbarn.notEqualTo(0))
                        and not(innvilgetEktefelletillegg)
            ) {
                // innvBTAP
                paragraph {
                    val navn = fritekst("navn")
                    val beloep = fritekst("beløp")
                    textExpr(
                        Bokmal to "I tillegg til alderspensjon får du også barnetillegg for ".expr() + navn + "." +
                                " Hvis den samlede inntekten din/deres blir høyere enn ".expr() + beloep + " kroner vil vi redusere dette tillegget.",
                        Nynorsk to "I tillegg til alderspensjon får du også barnetillegg for ".expr() + navn + "." +
                                " Dersom den samla inntekta di/dykkar blir høgare enn ".expr() + beloep + " kroner vil vi redusere dette tillegget.",
                        English to "In addition to retirement pension, you are also entitled to a child supplement for ".expr() + navn + "." +
                                " If your total income exceeds NOK ".expr() + beloep + ", this supplement will be reduced."
                    )
                }
            }.orShowIf(
                (innvilgetEktefelletillegg and ektefelletillegg.notEqualTo(0))
                        and (innvilgetBarnetilleggFellesbarn and barnetilleggFellesbarn.notEqualTo(0))
                        or (innvilgetBarnetilleggSaerkullsbarn and barnetilleggSaerkullsbarn.notEqualTo(0))
            ) {
                // innvETBTAP
                paragraph {
                    val navn = fritekst("navn")
                    val beloep = fritekst("beloep")
                    textExpr(
                        Bokmal to "I tillegg til alderspensjon får du også ektefelle- og barnetillegg for ".expr() + navn + " og ".expr() + navn + "." +
                                " Hvis den samlede inntekten din/deres blir høyere enn ".expr() + beloep + " kroner vil vi redusere disse tilleggene.",
                        Nynorsk to "I tillegg til alderspensjon får du også ektefelle- og barnetillegg for ".expr() + navn + " og ".expr() + navn + "." +
                                " Dersom den samla inntekta di/dykkar blir høgare enn ".expr() + beloep + " kroner vil vi redusere desse tillegga.",
                        English to "In addition to retirement pension, you are also entitled to a spouse supplement and a child supplement for ".expr() + navn + " og ".expr() + navn + "." +
                                " If your total income exceeds NOK ".expr() + beloep + ", these supplements will be reduced."
                    )
                }
            }
            showIf(
                sivilstand.isOneOf(ENSLIG, ENKE, UKJENT)
                        and (innvilgetEktefelletillegg and ektefelletillegg.notEqualTo(0))
                        or (innvilgetBarnetilleggFellesbarn and barnetilleggFellesbarn.notEqualTo(0))
                        or (innvilgetBarnetilleggSaerkullsbarn and barnetilleggSaerkullsbarn.notEqualTo(0))
            ) {
                // innvFTAPIngunUtbInntekt_002
                paragraph {
                    val navn = fritekst("navn")
                    textExpr(
                        Bokmal to "I tillegg til alderspensjonen får du også ".expr() + ifElse(
                            innvilgetEktefelletillegg,
                            ifTrue = "ektefelletillegg",
                            "barnetillegg"
                        ) + " for ".expr() + navn + ", men det vil ikke bli utbetalt fordi den samlede inntekten din er for høy.",
                        Nynorsk to "I tillegg til alderspensjonen får du også  for ".expr() + ifElse(
                            innvilgetEktefelletillegg,
                            ifTrue = "ektefelletillegg",
                            "barnetillegg"
                        ) + ", men det vil ikkje bli betalt ut fordi den samla inntekta di er for høg.",
                        English to "In addition to retirement pension, you are also entitled to a ".expr() + ifElse(
                            innvilgetEktefelletillegg,
                            ifTrue = "spouse supplement",
                            "child supplement"
                        ) + " for ".expr() + navn + "; however, this will not be paid, because your total income is above the threshold for this supplement."
                    )

                }
            }.orShowIf(
                sivilstand.isOneOf(GIFT, SAMBOER_1_5, SAMBOER_3_2, PARTNER)
                        and (innvilgetEktefelletillegg and ektefelletillegg.isNull())
                        or (innvilgetBarnetilleggFellesbarn and barnetilleggFellesbarn.isNull())
                        or (innvilgetBarnetilleggSaerkullsbarn and barnetilleggSaerkullsbarn.isNull())
            ) {
                // innvFTAPIngunUtbSamletInntekt_002
                paragraph {
                    val navn = fritekst("navn")
                    textExpr(
                        Bokmal to "I tillegg til alderspensjonen får du også ".expr() + ifElse(
                            innvilgetEktefelletillegg,
                            ifTrue = "ektefelletillegg",
                            ifFalse = "barnetillegg"
                        ) + " for ".expr() + navn + ", men det vil ikke bli utbetalt fordi den samlede inntekten til deg og [_Script Script_beregnetPensjonPerManedVedVirk_brukersSivilstand_bestemtFormLitenForbokstav_] din er for høy.",
                        Nynorsk to "I tillegg til alderspensjonen får du også [_Script Script_barnetillegg_ektefelletillegg_] for ".expr() + navn + ", men det vil ikkje bli betalt ut fordi den samla inntekta til deg og [_Script Script_beregnetPensjonPerManedVedVirk_brukersSivilstand_bestemtFormLitenForbokstav_] din er for høg.",
                        English to "In addition to retirement pension, you are also entitled to a [_Script Script_barnetillegg_ektefelletillegg_] for ".expr() + navn + "; however, this will not be paid, because your and your [_Script Script_beregnetPensjonPerManedVedVirk_brukersSivilstand_bestemtFormLitenForbokstav_]’s combined total income is above the threshold for this supplement."
                    )
                }
            }
        }
    }
}




