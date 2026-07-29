package no.nav.pensjon.brev.alder.maler

import no.nav.pensjon.brev.template.AlltidValgbartVedlegg
import no.nav.pensjon.brev.alder.maler.vedlegg.alltidValgbare.skjemaForBankopplysninger as skjemaForBankopplysningerTemplate
import no.nav.pensjon.brev.alder.maler.vedlegg.alltidValgbare.uttaksskjema as uttaksskjemaTemplate
import no.nav.pensjon.brevbaker.api.model.AlltidValgbartVedleggBrevkode
import no.nav.pensjon.brevbaker.api.model.LanguageCode

object AlderAlltidValgbareVedlegg {
    private val skjemaForBankopplysninger = AlltidValgbartVedlegg(
        skjemaForBankopplysningerTemplate,
        AlltidValgbartVedleggBrevkode(
            kode = "SKJEMA_FOR_BANKOPPLYSNINGER",
            visningstekst = "Skjema for bankopplysninger",
            spraak = setOf(LanguageCode.BOKMAL, LanguageCode.ENGLISH),
        ),
    )
    private val uttaksskjema = AlltidValgbartVedlegg(
        uttaksskjemaTemplate,
        AlltidValgbartVedleggBrevkode(
            kode = "UTTAKSSKJEMA",
            visningstekst = "Uttaksskjema",
            spraak = setOf(LanguageCode.BOKMAL, LanguageCode.ENGLISH),
        ),
    )

    val vedlegg: Set<AlltidValgbartVedlegg<*>> = setOf(skjemaForBankopplysninger, uttaksskjema)
}
