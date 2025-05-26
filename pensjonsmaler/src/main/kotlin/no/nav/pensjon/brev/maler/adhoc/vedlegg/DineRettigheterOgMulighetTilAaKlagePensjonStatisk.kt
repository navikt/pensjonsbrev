package no.nav.pensjon.brev.maler.adhoc.vedlegg

import no.nav.pensjon.brev.api.model.maler.EmptyVedleggBrevdata
import no.nav.pensjon.brev.maler.fraser.common.Constants
import no.nav.pensjon.brev.maler.fraser.vedlegg.*
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.expr
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText

@TemplateModelHelpers
val dineRettigheterOgMulighetTilAaKlagePensjonStatisk =
    createAttachment<LangBokmalNynorskEnglish, EmptyVedleggBrevdata>(
        title = newText(
            Bokmal to "Dine rettigheter og mulighet til å klage",
            Nynorsk to "Rettane dine og høve til å klage",
            English to "Your rights and how to appeal"
        ),
        includeSakspart = false,
    ) {
        val telefonnummer = Constants.navKontaktsenterPensjon.expr()

        includePhrase(VedleggVeiledning)
        includePhrase(VedleggInnsynSakPensjon(telefonnummer, Constants.NAV_URL.expr()))
        includePhrase(VedleggHjelpFraAndre)
        includePhrase(VedleggKlagePaaVedtaket(telefonnummer))
    }

