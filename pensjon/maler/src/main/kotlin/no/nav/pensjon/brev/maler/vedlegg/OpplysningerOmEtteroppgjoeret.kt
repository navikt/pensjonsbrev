package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.BarnetilleggSelectors.felles
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.barnetillegg
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.harFaattForMye
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.harGjenlevendeTillegg
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.pensjonsgivendeInntekt
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.pensjonsgivendeInntektBruktIBeregningen
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.periode
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.totaltAvvik
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.ufoeretrygd
import no.nav.pensjon.brev.maler.fraser.vedlegg.*
import no.nav.pensjon.brev.template.LangBokmalNynorskEnglish
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.expression.notNull
import no.nav.pensjon.brev.template.dsl.expression.safe
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.text

@TemplateModelHelpers
val vedleggOpplysningerOmEtteroppgjoeret = createAttachment<LangBokmalNynorskEnglish, OpplysningerOmEtteroppgjoeretDto>(
    title = {
        text(
            bokmal { +"Opplysninger om etteroppgjøret" },
            nynorsk { +"Opplysningar om etteroppgjeret" },
            english { +"Information about the settlement" },
        )
    },
    includeSakspart = true,
) {

    includePhrase(Introduksjon(periode))
    includePhrase(FikkSkulleFaattTabell(periode, harFaattForMye, harGjenlevendeTillegg, ufoeretrygd, barnetillegg, totaltAvvik))
    includePhrase(DuHarFaattAvviksBeloep(totaltAvvik, harFaattForMye, periode))
    includePhrase(OmBeregningAvUfoeretrygd(barnetillegg, harGjenlevendeTillegg, pensjonsgivendeInntekt, periode, pensjonsgivendeInntektBruktIBeregningen, ufoeretrygd))
    ifNotNull(barnetillegg) {
        includePhrase(OmBeregningAvBarnetillegg(it, periode))
    }
    includePhrase(ErOpplysningeneOmInntektFeil(barnetillegg.safe { felles }.notNull()))
}

