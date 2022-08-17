package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.Institusjon.*
import no.nav.pensjon.brev.api.model.Sivilstand.*
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterAlderDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterAlderDtoSelectors.barnetilleggVedvirk_innvilgetBarnetillegFellesbarn
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterAlderDtoSelectors.barnetilleggVedvirk_innvilgetBarnetilleggSaerkullsbarn
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterAlderDtoSelectors.bruker_borINorge
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterAlderDtoSelectors.bruker_sivilstand
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterAlderDtoSelectors.ektefelletilleggVedvirk_innvilgetEktefelletillegg
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterAlderDtoSelectors.eps_borSammenMedBrukerGjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterAlderDtoSelectors.institusjon_gjeldende
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterAlderDtoSelectors.instutisjon_epsInstitusjonGjeldende
import no.nav.pensjon.brev.maler.fraser.*
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText

// Conditional for showing the attachment is: sakstype = ALDER && vedtakResultat = INNVL

@TemplateModelHelpers
val orienteringOmRettigheterOgPlikterAlder =
    createAttachment<LangBokmalNynorskEnglish, OrienteringOmRettigheterAlderDto>(
        title = newText(
            Bokmal to "Dine rettigheter og plikter",
            Nynorsk to "Dine rettar og plikter",
            English to "Your rights and obligations"
        ),
        includeSakspart = true,
    ) {
        includePhrase(VedleggPlikter_001)
        list {

            showIf(institusjon_gjeldende.isNotAnyOf(FENGSEL, HELSE, SYKEHJEM)) {
                showIf(bruker_borINorge) {
                    item { includePhrase(VedleggPlikterAP2_001) }
                }.orShow {
                    item { includePhrase(VedleggPlikterAP3_001) }
                }
                showIf(bruker_sivilstand.isOneOf(ENSLIG, ENKE)) {
                    item { includePhrase(VedleggPlikterAP1_001) }
                }
            }

            showIf(
                eps_borSammenMedBrukerGjeldende
                        and instutisjon_epsInstitusjonGjeldende.isNotAnyOf(INGEN)
            ){

                showIf(bruker_sivilstand.isOneOf(GIFT)) {
                    item { includePhrase(VedleggPlikterAP4_002) }
                }.orShowIf(bruker_sivilstand.isOneOf(PARTNER)) {
                    item { includePhrase(VedleggPlikterAP13_002) }
                }.orShowIf(bruker_sivilstand.isOneOf(SAMBOER1_5, SAMBOER3_2)) {
                    item { includePhrase(VedleggPlikterAP15_002) }
                }
            }

            showIf(
                eps_borSammenMedBrukerGjeldende
                        and institusjon_gjeldende.isNotAnyOf(FENGSEL, HELSE, SYKEHJEM)
                        and instutisjon_epsInstitusjonGjeldende.isNotAnyOf(INGEN)
            ) {
                item { includePhrase(VedleggPlikterAPFlytterFraHverandre(bruker_sivilstand)) }
                showIf(bruker_sivilstand.isOneOf(SAMBOER1_5, SAMBOER3_2)) {
                    item { includePhrase(VedleggPlikterAP16_001) }
                    item { includePhrase(VedleggPlikterAP17_001) }
                    item { includePhrase(VedleggPlikterAP19_001) }
                }
            }

            showIf(
                not(eps_borSammenMedBrukerGjeldende)
                    and institusjon_gjeldende.isNotAnyOf(SYKEHJEM)
                    and instutisjon_epsInstitusjonGjeldende.isNotAnyOf(SYKEHJEM)){

                showIf(bruker_sivilstand.isOneOf(GIFT_LEVER_ADSKILT, GIFT)){
                    item { includePhrase(VedleggPlikterAP8_001) }
                }.orShowIf(bruker_sivilstand.isOneOf(PARTNER, PARTNER_LEVER_ADSKILT)){
                    item { includePhrase(VedleggPlikterAP11_001) }
                }
            }

            showIf(
                bruker_sivilstand.isOneOf(GIFT, GIFT_LEVER_ADSKILT, PARTNER, PARTNER_LEVER_ADSKILT)
            ) {
                item { includePhrase(VedleggPlikterAP9_001) }
            }
            showIf(
                bruker_sivilstand.isOneOf(GIFT)
            ) {
                item { includePhrase(VedleggPlikterAP7_001) }
            }
            showIf(
                bruker_sivilstand.isOneOf(PARTNER, PARTNER_LEVER_ADSKILT)
            ) {
                item { includePhrase(VedleggPlikterAP12_001) }
            }
            showIf(
                bruker_sivilstand.isOneOf(GIFT, GIFT_LEVER_ADSKILT, PARTNER, PARTNER_LEVER_ADSKILT)
                        and not(eps_borSammenMedBrukerGjeldende)
                        and institusjon_gjeldende.isNotAnyOf(FENGSEL, HELSE, SYKEHJEM)
                        and instutisjon_epsInstitusjonGjeldende.isNotAnyOf(INGEN)
            ) {
                item { includePhrase(VedleggPlikterAP10_001) }
            }

            showIf(
                bruker_sivilstand.isNotAnyOf(ENSLIG, ENKE)
                        and (eps_borSammenMedBrukerGjeldende)
                        and institusjon_gjeldende.isNotAnyOf(FENGSEL, HELSE, SYKEHJEM)
                        and instutisjon_epsInstitusjonGjeldende.isNotAnyOf(INGEN)
            ) {
                item {
                    includePhrase(VedleggPlikterAP5_001(bruker_sivilstand))
                }
            }

            showIf(
                bruker_sivilstand.isOneOf(ENSLIG, ENKE)
                        and institusjon_gjeldende.isNotAnyOf(FENGSEL, HELSE, SYKEHJEM)
                        and bruker_borINorge
            ) {
                item { includePhrase(VedleggPlikterAP26_001) }
            }

            showIf(
                institusjon_gjeldende.isNotAnyOf(FENGSEL, HELSE, SYKEHJEM)
                        and bruker_borINorge
            ) {
                item { includePhrase(VedleggPlikterAP27_001) }
            }
        }

        includePhrase(VedleggPlikterHvorforMeldeAP_001)
        showIf(
            barnetilleggVedvirk_innvilgetBarnetillegFellesbarn
                    and barnetilleggVedvirk_innvilgetBarnetilleggSaerkullsbarn
                    and not(ektefelletilleggVedvirk_innvilgetEktefelletillegg)
        ) {
            includePhrase(VedleggPlikterRettTilBarnetilleggAP_001)
        }
        showIf(
            ektefelletilleggVedvirk_innvilgetEktefelletillegg
                    and not(barnetilleggVedvirk_innvilgetBarnetillegFellesbarn)
                    and not(barnetilleggVedvirk_innvilgetBarnetilleggSaerkullsbarn)
        ) {
            includePhrase(VedleggPlikterRettTilEktefelletilleggAP_001(bruker_sivilstand))
        }
        showIf(
            barnetilleggVedvirk_innvilgetBarnetillegFellesbarn
                    or barnetilleggVedvirk_innvilgetBarnetilleggSaerkullsbarn //TODO hva henger denne or egentlig sammen med?
                    and ektefelletilleggVedvirk_innvilgetEktefelletillegg
        ) {
            includePhrase(VedleggPlikterRettTilEktefelletilleggOgBarnetilleggAP_001(bruker_sivilstand))
        }
        showIf(
            barnetilleggVedvirk_innvilgetBarnetillegFellesbarn
                    or barnetilleggVedvirk_innvilgetBarnetilleggSaerkullsbarn //TODO hva henger denne or egentlig sammen med?
                    and not(ektefelletilleggVedvirk_innvilgetEktefelletillegg)
        ) {
            includePhrase(VedleggPlikterinntektsprovingBTFellesBarnSaerkullsbarnAP_001)
        }
        showIf(
            barnetilleggVedvirk_innvilgetBarnetillegFellesbarn
                    or barnetilleggVedvirk_innvilgetBarnetilleggSaerkullsbarn //TODO hva henger denne or egentlig sammen med?
                    and ektefelletilleggVedvirk_innvilgetEktefelletillegg
        ) {
            includePhrase(VedleggPlikterinntektsprovingBTOgETAP_001)
        }
        showIf(
            not(barnetilleggVedvirk_innvilgetBarnetillegFellesbarn)
                    and not(barnetilleggVedvirk_innvilgetBarnetilleggSaerkullsbarn)
                    and ektefelletilleggVedvirk_innvilgetEktefelletillegg
        ) {
            includePhrase(VedleggPlikterinntektsprovingETAP_001)
        }

        includePhrase(InfoAPBeskjed_001)
        includePhrase(VedleggVeiledning_001)
        includePhrase(VedleggInnsynSakPensjon_001)
        includePhrase(VedleggHjelpFraAndre_001)
        includePhrase(VedleggKlagePesys_001)
    }
