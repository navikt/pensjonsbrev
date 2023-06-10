package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.vedlegg.PraktiskInformasjonEtterOppgjoerDto
import no.nav.pensjon.brev.maler.fraser.vedlegg.etteroppgjoer.EttersendeDokumentasjon
import no.nav.pensjon.brev.maler.fraser.vedlegg.etteroppgjoer.InnledningPraktiskInformasjon
import no.nav.pensjon.brev.maler.fraser.vedlegg.etteroppgjoer.InntekterSomKanHoldesUtenforEtteroppgjoeret
import no.nav.pensjon.brev.maler.fraser.vedlegg.etteroppgjoer.SlikBetalerDuTilbake
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText

// Conditional for showing the attachment is that PE_UT_23_001 Varsel - etteroppgjør av uføretrygd ved feil utbetaling produseres
@TemplateModelHelpers

val vedleggPraktiskInformasjonEtterOppgjoer =
    createAttachment<LangBokmalNynorskEnglish, PraktiskInformasjonEtterOppgjoerDto>(
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