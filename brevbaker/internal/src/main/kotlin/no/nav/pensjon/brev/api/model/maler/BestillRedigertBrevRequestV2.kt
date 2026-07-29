package no.nav.pensjon.brev.api.model

import no.nav.brev.brevbaker.markup.Attachment
import no.nav.brev.brevbaker.markup.LetterMarkup
import no.nav.pensjon.brev.api.model.maler.BrevRequest
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brevbaker.api.model.AlltidValgbartVedleggKode
import no.nav.pensjon.brevbaker.api.model.BrevbakerFelles
import no.nav.pensjon.brevbaker.api.model.BrevbakerType
import no.nav.pensjon.brevbaker.api.model.LanguageCode
import no.nav.pensjon.brevbaker.api.model.PDFVedleggTittel

/**
 * Skribenten sin bestilling av et ferdig redigert brev, med markup fra `brevbaker:markup` (v2) i
 * stedet for den gamle `LetterMarkup`-modellen i api-model-common.
 *
 * Dette er nettopp typen som ikke lot seg plassere i verken `markup` eller `api-model-common`: den
 * kombinerer markup-modellen med `felles`/`brevkode`/`letterData` fra api-model-common, og er kun
 * intern. Den bor derfor her, i `brevbaker:internal`, som er den eneste modulen som ser begge deler.
 *
 * Fordi all intern serialisering nå går gjennom [no.nav.brev.brevbaker.internal.serialize.InternalObjectMapper],
 * trengs det ingen hybrid-serializer som blander Jackson og kotlinx – som var hele poenget med å
 * flytte markup over på Jackson.
 */
data class BestillRedigertBrevRequestV2<T : Brevkode<T>>(
    val kode: T,
    val letterData: RedigerbarBrevdata<*, *>,
    val felles: BrevbakerFelles,
    val language: LanguageCode,
    val letterMarkup: LetterMarkup,
    val alltidValgbareVedlegg: List<AlltidValgbartVedleggKode>,
    val redigerteVedlegg: Map<BrevbakerType.VedleggId, Attachment>,
    val pdfVedlegg: List<PDFVedleggTittel> = listOf(),
) : BrevRequest<T>
