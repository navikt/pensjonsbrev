package no.nav.brev.brevbaker

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.VedleggData
import no.nav.pensjon.brev.template.BrevTemplate
import kotlin.reflect.KClass

interface LetterDataFactory {
    fun <T : BrevbakerBrevdata> create(templateType: KClass<out BrevTemplate<T, *>>): T
    fun <T : VedleggData> createVedlegg(letterDataType: KClass<T>): T
}