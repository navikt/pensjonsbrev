package no.nav.pensjon.brev.maler.vedlegg

import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDto
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.BarnetilleggSelectors.felles_safe
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.barnetillegg
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.harFaattForMye
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.harGjenlevendeTillegg
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.pensjonsgivendeInntekt
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.pensjonsgivendeInntektBruktIBeregningen
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.periode
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.totaltAvvik
import no.nav.pensjon.brev.api.model.vedlegg.OpplysningerOmEtteroppgjoeretDtoSelectors.ufoeretrygd
import no.nav.pensjon.brev.maler.fraser.vedlegg.*
import no.nav.pensjon.brev.template.*
import no.nav.pensjon.brev.template.Language.*
import no.nav.pensjon.brev.template.dsl.*
import no.nav.pensjon.brev.template.dsl.expression.*
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers

@TemplateModelHelpers
val vedleggOpplysningerOmEtteroppgjoeret =
    createAttachment<LangBokmalNynorskEnglish, OpplysningerOmEtteroppgjoeretDto>(
        title =
            newText(
                Bokmal to "Opplysninger om etteroppgjøret",
                Nynorsk to "Opplysningar om etteroppgjeret",
                English to "Information about the settlement",
            ),
        includeSakspart = true,
    ) {

        includePhrase(Introduksjon(periode))
        includePhrase(FikkSkulleFaattTabell(periode, harFaattForMye, harGjenlevendeTillegg, ufoeretrygd, barnetillegg, totaltAvvik))
        includePhrase(DuHarFaattAvviksBeloep(totaltAvvik, harFaattForMye, periode))
        includePhrase(OmBeregningAvUfoeretrygd(barnetillegg, harGjenlevendeTillegg, pensjonsgivendeInntekt, periode, pensjonsgivendeInntektBruktIBeregningen, ufoeretrygd))
        ifNotNull(barnetillegg) {
            includePhrase(OmBeregningAvBarnetillegg(it, periode))
        }
        includePhrase(ErOpplysningeneOmInntektFeil(barnetillegg.felles_safe.notNull()))
    }
