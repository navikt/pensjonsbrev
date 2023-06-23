package no.nav.pensjon.etterlatte

import no.nav.pensjon.brev.template.LetterTemplate
import no.nav.pensjon.etterlatte.maler.*
import no.nav.pensjon.etterlatte.maler.barnepensjon.BarnepensjonInnvilgelse
import no.nav.pensjon.etterlatte.maler.barnepensjon.SoeskenjusteringRevurdering
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.OMSInnvilgelseAuto
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.OMSInnvilgelseManuell
import no.nav.pensjon.etterlatte.maler.omstillingsstoenad.OMSOpphoerManuell


val prodAutobrevTemplates: Set<EtterlatteTemplate<*>> =
    setOf(
        BarnepensjonInnvilgelse,
        OMSInnvilgelseAuto,
        OMSInnvilgelseManuell,
        OMSOpphoerManuell,
        SoeskenjusteringRevurdering
    )

val prodRedigerbareTemplates: Set<EtterlatteTemplate<*>> = emptySet()

class TemplateResource(
    autobrevTemplates: Set<EtterlatteTemplate<*>> = prodAutobrevTemplates,
    redigerbareTemplates: Set<EtterlatteTemplate<*>> = prodRedigerbareTemplates,
) {
    private val autoBrevMap: Map<EtterlatteBrevKode, EtterlatteTemplate<*>> =
        autobrevTemplates.associateBy { it.kode }

    private val redigerbareBrevMap: Map<EtterlatteBrevKode, EtterlatteTemplate<*>> =
        redigerbareTemplates.associateBy { it.kode }

    fun getAutoBrev(): Set<EtterlatteBrevKode> =
        autoBrevMap.keys

    fun getAutoBrev(kode: EtterlatteBrevKode): LetterTemplate<*, *>? =
        autoBrevMap[kode]?.template

    fun getRedigerbareBrev(): Set<EtterlatteBrevKode> =
        redigerbareBrevMap.keys

    fun getRedigerbartBrev(kode: EtterlatteBrevKode): LetterTemplate<*, *>? =
        redigerbareBrevMap[kode]?.template
}