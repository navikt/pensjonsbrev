package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgMulighetTilAaKlageDtoSelectors.brukerUnder18Aar
import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgMulighetTilAaKlageDtoSelectors.sakstype
import no.nav.pensjon.brev.maler.fraser.vedlegg.*
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brevbaker.api.model.FellesSelectors.avsenderEnhet
import no.nav.pensjon.brevbaker.api.model.NavEnhetSelectors.nettside
import no.nav.pensjon.brevbaker.api.model.NavEnhetSelectors.telefonnummer

// V00001 i metaforce
@TemplateModelHelpers
val vedleggDineRettigheterOgMulighetTilAaKlage =
    createAttachment<LangBokmalNynorskEnglish, DineRettigheterOgMulighetTilAaKlageDto>(
        title = {
            text(
                bokmal { +"Dine rettigheter og mulighet til å klage" },
                nynorsk { +"Rettane dine og høve til å klage" },
                english { +"Your rights and how to appeal" },
            )
        },
        includeSakspart = false
    ) {
        includePhrase(VedleggVeiledning)
        showIf(sakstype.notEqualTo(Sakstype.UFOREP)) {
            includePhrase(VedleggInnsynSakPensjon(felles.avsenderEnhet.telefonnummer, nettside = felles.avsenderEnhet.nettside))
        }.orShow {
            includePhrase(VedleggInnsynSakUfoeretrygdPesysNoenDokumenter)
        }
        showIf(sakstype.equalTo(Sakstype.BARNEP) and argument.brukerUnder18Aar.ifNull(false).equalTo(true)) {
            includePhrase(VedleggInnsynSakUnder18)
        }
        includePhrase(VedleggHjelpFraAndre)
        includePhrase(VedleggKlagePaaVedtaket(felles.avsenderEnhet.telefonnummer))
    }