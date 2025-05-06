package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.Sakstype
import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgMulighetTilAaKlageDto
import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgMulighetTilAaKlageDtoSelectors.brukerUnder18Aar_safe
import no.nav.pensjon.brev.api.model.vedlegg.DineRettigheterOgMulighetTilAaKlageDtoSelectors.sakstype
import no.nav.pensjon.brev.maler.fraser.vedlegg.VedleggHjelpFraAndre
import no.nav.pensjon.brev.maler.fraser.vedlegg.VedleggInnsynSakPensjon
import no.nav.pensjon.brev.maler.fraser.vedlegg.VedleggInnsynSakUfoeretrygdPesysNoenDokumenter
import no.nav.pensjon.brev.maler.fraser.vedlegg.VedleggInnsynSakUnder18
import no.nav.pensjon.brev.maler.fraser.vedlegg.VedleggKlagePaaVedtaket
import no.nav.pensjon.brev.maler.fraser.vedlegg.VedleggVeiledning
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.Bokmal
import no.nav.pensjon.brev.template.Language.English
import no.nav.pensjon.brev.template.Language.Nynorsk
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.and
import no.nav.pensjon.brev.template.dsl.expression.equalTo
import no.nav.pensjon.brev.template.dsl.expression.ifNull
import no.nav.pensjon.brev.template.dsl.expression.notEqualTo
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText
import no.nav.pensjon.brevbaker.api.model.FellesSelectors.avsenderEnhet
import no.nav.pensjon.brevbaker.api.model.NAVEnhetSelectors.nettside
import no.nav.pensjon.brevbaker.api.model.NAVEnhetSelectors.telefonnummer

// V00001 i metaforce
@TemplateModelHelpers
val vedleggDineRettigheterOgMulighetTilAaKlage =
    createAttachment<LangBokmalNynorskEnglish, DineRettigheterOgMulighetTilAaKlageDto>(
        title = newText(
            Bokmal to "Dine rettigheter og mulighet til å klage",
            Nynorsk to "Rettane dine og høve til å klage",
            English to "Your rights and how to appeal",
        ),
        includeSakspart = false
    ) {
        includePhrase(VedleggVeiledning)
        showIf(sakstype.notEqualTo(Sakstype.UFOREP)) {
            includePhrase(VedleggInnsynSakPensjon(felles.avsenderEnhet.telefonnummer, nettside = felles.avsenderEnhet.nettside))
        }.orShow {
            includePhrase(VedleggInnsynSakUfoeretrygdPesysNoenDokumenter)
        }
        showIf(sakstype.equalTo(Sakstype.BARNEP) and argument.brukerUnder18Aar_safe.ifNull(false).equalTo(true)) {
            includePhrase(VedleggInnsynSakUnder18)
        }
        includePhrase(VedleggHjelpFraAndre)
        includePhrase(VedleggKlagePaaVedtaket(felles.avsenderEnhet.telefonnummer))
    }