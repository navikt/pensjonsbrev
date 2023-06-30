package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.vedlegg.PraktiskInformasjonEtteroppgjoerDto
import no.nav.pensjon.brev.maler.fraser.vedlegg.EttersendeDokumentasjon
import no.nav.pensjon.brev.maler.fraser.vedlegg.InnledningPraktiskInformasjon
import no.nav.pensjon.brev.maler.fraser.vedlegg.InntekterSomKanHoldesUtenforEtteroppgjoeret
import no.nav.pensjon.brev.maler.fraser.vedlegg.SlikBetalerDuTilbake
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText

// TODO: Conditional for showing the attachment is that letter UT_EO_FORHAANDSVARSEL_AUTO produseres

@TemplateModelHelpers
val vedleggPraktiskInformasjonEtteroppgjoer =
    createAttachment<LangBokmalNynorskEnglish, PraktiskInformasjonEtteroppgjoerDto>(
        title = newText(
            Bokmal to "Praktisk informasjon",
            Nynorsk to "Praktisk informasjon",
            English to "Practical information"
        ),
        includeSakspart = false,
    ) {
        includePhrase(InnledningPraktiskInformasjon)
        includePhrase(SlikBetalerDuTilbake)
        includePhrase(InntekterSomKanHoldesUtenforEtteroppgjoeret)
        includePhrase(EttersendeDokumentasjon)
    }