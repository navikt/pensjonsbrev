package no.nav.pensjon.brev.alder.model.afp

import no.nav.pensjon.brev.api.model.maler.EmptySaksbehandlerValg
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata

/**
 * Vedtak — innvilgelse av AFP i privat sektor (redigerbar).
 *
 * Ported from the Exstream brevkode `PE_AF_04_111`. Brevkroppen er identisk
 * med autobrev-varianten `PE_AF_04_115`, så pesys-data gjenbrukes fra
 * [InnvilgelseAvAfpAutoDto]. Saksbehandler har ingen valg å ta — alt
 * innhold styres av pesys-data og kan eventuelt redigeres i Skribenten.
 */
data class InnvilgelseAvAfpDto(
    override val saksbehandlerValg: EmptySaksbehandlerValg,
    override val pesysData: InnvilgelseAvAfpAutoDto,
) : RedigerbarBrevdata<EmptySaksbehandlerValg, InnvilgelseAvAfpAutoDto>
