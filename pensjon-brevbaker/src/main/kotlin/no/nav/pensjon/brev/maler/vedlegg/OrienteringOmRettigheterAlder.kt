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
                    item { includePhrase(vedleggPlikterAP2_001) }
                }.orShow {
                    item { includePhrase(vedleggPlikterAP3_001) }
                }
                showIf(bruker_sivilstand.isOneOf(ENSLIG, ENKE)) {
                    item { includePhrase(vedleggPlikterAP1_001) }
                }
            }

            showIf(
                eps_borSammenMedBrukerGjeldende
                        and instutisjon_epsInstitusjonGjeldende.isNotAnyOf(INGEN)
            ){

                showIf(bruker_sivilstand.isOneOf(GIFT)) {
                    item { includePhrase(vedleggPlikterAP4_002) }
                }.orShowIf(bruker_sivilstand.isOneOf(PARTNER)) {
                    item { includePhrase(vedleggPlikterAP13_002) }
                }.orShowIf(bruker_sivilstand.isOneOf(SAMBOER1_5, SAMBOER3_2)) {
                    item { includePhrase(vedleggPlikterAP15_002) }
                }
            }

            showIf(
                eps_borSammenMedBrukerGjeldende
                        and institusjon_gjeldende.isNotAnyOf(FENGSEL, HELSE, SYKEHJEM)
                        and instutisjon_epsInstitusjonGjeldende.isNotAnyOf(INGEN)
            ) {
                item { includePhrase(vedleggPlikterAPFlytterFraHverandre, bruker_sivilstand) }
                showIf(bruker_sivilstand.isOneOf(SAMBOER1_5, SAMBOER3_2)) {
                    item { includePhrase(vedleggPlikterAP16_001) }
                    item { includePhrase(vedleggPlikterAP17_001) }
                    item { includePhrase(vedleggPlikterAP19_001) }
                }
            }

            showIf(
                not(eps_borSammenMedBrukerGjeldende)
                    and institusjon_gjeldende.isNotAnyOf(SYKEHJEM)
                    and instutisjon_epsInstitusjonGjeldende.isNotAnyOf(SYKEHJEM)){

                showIf(bruker_sivilstand.isOneOf(GIFT_LEVER_ADSKILT, GIFT)){
                    item { includePhrase(vedleggPlikterAP8_001) }
                }.orShowIf(bruker_sivilstand.isOneOf(PARTNER, PARTNER_LEVER_ADSKILT)){
                    item { includePhrase(vedleggPlikterAP11_001) }
                }
            }

            showIf(
                bruker_sivilstand.isOneOf(GIFT, GIFT_LEVER_ADSKILT, PARTNER, PARTNER_LEVER_ADSKILT)
            ) {
                item { includePhrase(vedleggPlikterAP9_001) }
            }
            showIf(
                bruker_sivilstand.isOneOf(GIFT)
            ) {
                item { includePhrase(vedleggPlikterAP7_001) }
            }
            showIf(
                bruker_sivilstand.isOneOf(PARTNER, PARTNER_LEVER_ADSKILT)
            ) {
                item { includePhrase(vedleggPlikterAP12_001) }
            }
            showIf(
                bruker_sivilstand.isOneOf(GIFT, GIFT_LEVER_ADSKILT, PARTNER, PARTNER_LEVER_ADSKILT)
                        and not(eps_borSammenMedBrukerGjeldende)
                        and institusjon_gjeldende.isNotAnyOf(FENGSEL, HELSE, SYKEHJEM)
                        and instutisjon_epsInstitusjonGjeldende.isNotAnyOf(INGEN)
            ) {
                item { includePhrase(vedleggPlikterAP10_001) }
            }

            showIf(
                bruker_sivilstand.isNotAnyOf(ENSLIG, ENKE)
                        and (eps_borSammenMedBrukerGjeldende)
                        and institusjon_gjeldende.isNotAnyOf(FENGSEL, HELSE, SYKEHJEM)
                        and instutisjon_epsInstitusjonGjeldende.isNotAnyOf(INGEN)
            ) {
                item {
                    includePhrase(vedleggPlikterAP5_001, bruker_sivilstand)
                }
            }

            showIf(
                bruker_sivilstand.isOneOf(ENSLIG, ENKE)
                        and institusjon_gjeldende.isNotAnyOf(FENGSEL, HELSE, SYKEHJEM)
                        and bruker_borINorge
            ) {
                item { includePhrase(vedleggPlikterAP26_001) }
            }

            showIf(
                institusjon_gjeldende.isNotAnyOf(FENGSEL, HELSE, SYKEHJEM)
                        and bruker_borINorge
            ) {
                item { includePhrase(vedleggPlikterAP27_001) }
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
