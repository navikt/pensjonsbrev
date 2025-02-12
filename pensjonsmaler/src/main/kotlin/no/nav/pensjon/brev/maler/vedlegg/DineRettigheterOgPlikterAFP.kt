package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.Institusjon.*
import no.nav.pensjon.brev.api.model.Sivilstand.ENKE
import no.nav.pensjon.brev.api.model.Sivilstand.ENSLIG
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterAfpDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterAfpDtoSelectors.bruker_borINorge
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterAfpDtoSelectors.bruker_sivilstand
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterAfpDtoSelectors.institusjon_gjeldende
import no.nav.pensjon.brev.maler.fraser.vedlegg.*
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.isOneOf
import no.nav.pensjon.brev.template.dsl.expression.not
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brevbaker.api.model.FellesSelectors.avsenderEnhet
import no.nav.pensjon.brevbaker.api.model.NAVEnhetSelectors.telefonnummer

// Conditional for showing the attachment is: sakstype = AFP && vedtakResultat = INNVL

@TemplateModelHelpers
val dineRettigheterOgPlikterAFP =
    createAttachment<LangBokmalNynorskEnglish, OrienteringOmRettigheterAfpDto>(
        title =
            newText(
                Bokmal to "Dine rettigheter og plikter",
                Nynorsk to "Dine rettar og plikter",
                English to "Your rights and obligations",
            ),
        includeSakspart = false,
    ) {
        includePhrase(VedleggPlikterAFP)
        paragraph {
            list {
                item { includePhrase(VedleggPlikterAFP1) }
                showIf(bruker_sivilstand.isOneOf(ENSLIG, ENKE)) {
                    item { includePhrase(VedleggPlikterAFP2) }
                }

                showIf(not(institusjon_gjeldende.isOneOf(FENGSEL, HELSE, SYKEHJEM))) {
                    showIf(bruker_borINorge) {
                        item { includePhrase(VedleggPlikterAFP3) }
                    }.orShow {
                        item { includePhrase(VedleggPlikterAFP4) }
                    }
                }
            }
        }
        includePhrase(VedleggVeiledning)
        includePhrase(VedleggInnsynSakPensjon(felles.avsenderEnhet.telefonnummer))
        includePhrase(VedleggHjelpFraAndre)
        includePhrase(VedleggKlagePaaVedtaket(felles.avsenderEnhet.telefonnummer))
    }
