package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.Institusjon.*
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDto
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDtoSelectors.bruker_borINorge
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDtoSelectors.harInnvilgetBarnetilleggFellesBarn
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDtoSelectors.harInnvilgetBarnetilleggSaerkullsbarn
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDtoSelectors.harTilleggForFlereBarn
import no.nav.pensjon.brev.api.model.vedlegg.OrienteringOmRettigheterUfoereDtoSelectors.institusjon_gjeldende
import no.nav.pensjon.brev.maler.fraser.vedlegg.*
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.isNotAnyOf
import no.nav.pensjon.brev.template.dsl.expression.or
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brevbaker.api.model.FellesSelectors.avsenderEnhet
import no.nav.pensjon.brevbaker.api.model.NAVEnhetSelectors.telefonnummer

val vedleggDineRettigheterOgPlikterUfoere =
    createAttachment<LangBokmalNynorskEnglish, OrienteringOmRettigheterUfoereDto>(
        title =
            newText(
                Bokmal to "Dine rettigheter og plikter",
                Nynorsk to "Rettane og pliktene dine",
                English to "Your rights and obligations",
            ),
        includeSakspart = false,
    ) {
        includePhrase(VedleggPlikter)
        val harInnvilgetBarnetillegg = harInnvilgetBarnetilleggFellesBarn or harInnvilgetBarnetilleggSaerkullsbarn

        paragraph {
            list {
                item { includePhrase(VedleggPlikterUT1) }
                item { includePhrase(VedleggPlikterUT2) }

                showIf(
                    bruker_borINorge
                        and institusjon_gjeldende.isNotAnyOf(FENGSEL, HELSE, SYKEHJEM),
                ) {
                    item { includePhrase(VedleggPlikterUT3) }
                    item { includePhrase(VedleggPlikterUT4) }
                }

                item { includePhrase(VedleggPlikterUT5) }
                item { includePhrase(VedleggPlikterEndretSivilstatus) }

                showIf(harInnvilgetBarnetilleggFellesBarn) {
                    item { includePhrase(VedleggPlikterEndretInntektBarnetillegg) }
                }

                showIf(harInnvilgetBarnetillegg) {
                    item { includePhrase(VedleggPlikterUT7(harTilleggForFlereBarn)) }
                    item { includePhrase(VedleggPlikterUT13(harTilleggForFlereBarn)) }
                    item { includePhrase(VedleggPlikterUT14(harTilleggForFlereBarn)) }
                }

                item { includePhrase(VedleggPlikterUT8) }
                item { includePhrase(VedleggPlikterUT9) }
                item { includePhrase(VedleggPlikterUT10) }
                item { includePhrase(VedleggPlikterUT11) }
                item { includePhrase(VedleggPlikterUT12) }
            }
        }
        includePhrase(VedleggVeiledning)
        includePhrase(VedleggInnsynSakUfoeretrygdPesys)
        includePhrase(VedleggHjelpFraAndre)
        includePhrase(VedleggKlagePaaVedtaket(felles.avsenderEnhet.telefonnummer))
    }
