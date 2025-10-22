package no.nav.pensjon.brev

import brev.adhoc.*
import brev.aldersovergang.InfoAldersovergangEps60AarAuto
import brev.aldersovergang.InfoAldersovergangEps62AarAuto
import brev.aldersovergang.InfoFyller67AarSaerskiltSats
import brev.aldersovergang.VedtakAldersovergang67AarGarantitilleggAuto
import brev.sivilstand.EndringAvAlderspensjonPgaGarantitillegg
import brev.sivilstand.EndringAvAlderspensjonSivilstand
import brev.sivilstand.EndringAvAlderspensjonSivilstandAuto
import brev.sivilstand.EndringAvAlderspensjonSivilstandSaerskiltSats
import no.nav.brev.brevbaker.AllTemplates
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata
import no.nav.pensjon.brev.template.AutobrevTemplate
import no.nav.pensjon.brev.template.RedigerbarTemplate

object AlderTemplates : AllTemplates {
    override fun hentAutobrevmaler(): Set<AutobrevTemplate<BrevbakerBrevdata>> = setOf(
        InfoFyller67AarSaerskiltSats,
        InfoAldersovergangEps60AarAuto,
        InfoAldersovergangEps62AarAuto,
        VedtakAldersovergang67AarGarantitilleggAuto,
        AdhocAFPInformasjonOekningToleransebeloep,
        AdhocAlderspensjonFraFolketrygden,
        AdhocAlderspensjonFraFolketrygden2,
        AdhocAlderspensjonGjtOpprydding,
        AdhocAlderspensjonGjtVarselBrev,
        AdhocSkjermingstilleggFeilBeroertBruker,
        AdhocSkjermingstilleggFeilMottaker,
        AdhocVarselTilBrukerForsoergingstilleggIkkeTilUtbetaling,
        AdhocVarselTilBrukerMedForsoergingstilleggTilUtbetaling,
        EndringAvAlderspensjonSivilstandAuto,
        FeilUtsendingAvGjenlevenderett,
    )

    override fun hentRedigerbareMaler(): Set<RedigerbarTemplate<out RedigerbarBrevdata<*, *>>> = setOf(
        EndringAvAlderspensjonPgaGarantitillegg,
        EndringAvAlderspensjonSivilstand,
        EndringAvAlderspensjonSivilstandSaerskiltSats,
    )
}