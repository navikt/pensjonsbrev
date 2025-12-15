package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.maler.EmptyVedleggData
import no.nav.pensjon.brev.maler.fraser.vedlegg.EttersendeDokumentasjon
import no.nav.pensjon.brev.maler.fraser.vedlegg.InnledningPraktiskInformasjon
import no.nav.pensjon.brev.maler.fraser.vedlegg.InntekterSomKanHoldesUtenforEtteroppgjoeret
import no.nav.pensjon.brev.maler.fraser.vedlegg.SlikBetalerDuTilbake
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers

// TODO: Conditional - this attachment is mandatory with letter UT_EO_FORHAANDSVARSEL_FEILUTBETALING_AUTO
// The conditional for showing the letter is: ResultatEO or ResultatForrigeEO = 'tilbakekr'

@TemplateModelHelpers
val vedleggPraktiskInformasjonEtteroppgjoerUfoeretrygd =
    createAttachment<LangBokmalNynorskEnglish, EmptyVedleggData>(
        title = {
            text(
                bokmal { +"Praktisk informasjon om etteroppgjøret" },
                nynorsk { +"Praktisk informasjon om etteroppgjeret" },
                english { +"Practical information about the settlement" }
            )
        },
        includeSakspart = false,
    ) {
        includePhrase(InnledningPraktiskInformasjon)
        includePhrase(SlikBetalerDuTilbake)
        includePhrase(InntekterSomKanHoldesUtenforEtteroppgjoeret)
        includePhrase(EttersendeDokumentasjon)
    }