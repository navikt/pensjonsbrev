package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.maler.fraser.vedlegg.EttersendeDokumentasjon
import no.nav.pensjon.brev.maler.fraser.vedlegg.InnledningPraktiskInformasjon
import no.nav.pensjon.brev.maler.fraser.vedlegg.InntekterSomKanHoldesUtenforEtteroppgjoeret
import no.nav.pensjon.brev.maler.fraser.vedlegg.SlikBetalerDuTilbake
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.newText

// TODO: Conditional - this attachment is mandatory with letter UT_EO_FORHAANDSVARSEL_FEILUTBETALING_AUTO
// The conditional for showing the letter is: ResultatEO or ResultatForrigeEO = 'tilbakekr'

@TemplateModelHelpers
val vedleggPraktiskInformasjonEtteroppgjoerUfoeretrygd =
    createAttachment<LangBokmalNynorskEnglish, Unit>(
        title = newText(
            Bokmal to "Praktisk informasjon om etteroppgjør",
            Nynorsk to "Praktisk informasjon om etteroppgjer",
            English to "Practical information about the post-settlement"
        ),
        includeSakspart = false,
    ) {
        includePhrase(InnledningPraktiskInformasjon)
        includePhrase(SlikBetalerDuTilbake)
        includePhrase(InntekterSomKanHoldesUtenforEtteroppgjoeret)
        includePhrase(EttersendeDokumentasjon)
    }