package no.nav.pensjon.brev.api.model.maler

import no.nav.pensjon.brevbaker.api.model.Kroner
import no.nav.pensjon.brevbaker.api.model.Year

data class VedtakOmEtteroppgjoerEtterbetalingAutoDto(
    val avviksbelopTFBUtenMinus: Kroner,
    val avviksbelopTSBUtenMinus: Kroner,
    val avviksbelopUTUtenMinus: Kroner,
    val avviksbelopUtenMinus: Kroner,
    val uforetrygdEtteroppgjorPeriodeFomYear: Year,
    val persongrunnlagslisteUforetrygdEtteroppgjorDetaljBrukerSumInntekterUT: Kroner,
    val vedtaksdataEtteroppgjorResultatInntektUT: Kroner,
    val vedtaksdataEtteroppgjorResultatTidligereBelopTFB: Kroner,
    val vedtaksdataEtteroppgjorResultatTidligereBelopTSB: Kroner,
    val vedtaksdaEtteroppgjorResultatTidligereBelopUT: Kroner,
    val vedtaksdataEtteroppgjorResultatTotalBelopTFB: Kroner,
    val vedtaksbrevVedtaksdataEtteroppgjorResultatTotalBelopTSB: Kroner,
    val vedtaksdataEtteroppgjorResultatTotalBelopUT: Kroner,
) : BrevbakerBrevdata