package no.nav.pensjon.brev.planleggepensjon.simulering.vedlegg

import no.nav.pensjon.brev.planleggepensjon.simulering.ApSimuleringDto
import no.nav.pensjon.brev.planleggepensjon.simulering.ApSimuleringDtoSelectors.simulering
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringSelectors.maanedligAlderspensjonForKnekkpunkter
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringV1MaanedligAlderspensjonForKnekkpunkterSelectors.vedGradertUttak
import no.nav.pensjon.brev.planleggepensjon.simulering.SimuleringV1MaanedligAlderspensjonForKnekkpunkterSelectors.vedHeltUttak
import no.nav.pensjon.brev.planleggepensjon.simulering.tabeller.OpptjeningKapittel19Tabell
import no.nav.pensjon.brev.planleggepensjon.simulering.tabeller.OpptjeningKapittel20Tabell
import no.nav.pensjon.brev.template.LangBokmal
import no.nav.pensjon.brev.template.createAttachment
import no.nav.pensjon.brev.template.dsl.helpers.TemplateModelHelpers
import no.nav.pensjon.brev.template.dsl.text

@TemplateModelHelpers
val opptjeningVedlegg = createAttachment<LangBokmal, ApSimuleringDto>(
    title = {
        text(bokmal { +"Slik har vi beregnet pensjonen din" })
    },
    includeSakspart = false,
) {
    ifNotNull(simulering.maanedligAlderspensjonForKnekkpunkter) { knekkpunkter ->
        ifNotNull(knekkpunkter.vedGradertUttak) { alderspensjon ->
            includePhrase(OpptjeningKapittel19Tabell(alderspensjon))
            includePhrase(OpptjeningKapittel20Tabell(alderspensjon))
        }
        includePhrase(OpptjeningKapittel19Tabell(knekkpunkter.vedHeltUttak))
        includePhrase(OpptjeningKapittel20Tabell(knekkpunkter.vedHeltUttak))
    }
}
