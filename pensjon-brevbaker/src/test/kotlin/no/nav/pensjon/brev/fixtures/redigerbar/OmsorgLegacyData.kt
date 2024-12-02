package no.nav.pensjon.brev.fixtures.redigerbar

import no.nav.pensjon.brev.api.model.maler.EmptyBrevdata
import no.nav.pensjon.brev.api.model.maler.legacy.Grunnlag
import no.nav.pensjon.brev.api.model.maler.legacy.Kravhode
import no.nav.pensjon.brev.api.model.maler.legacy.OmsorgGodskrGrunnlag
import no.nav.pensjon.brev.api.model.maler.legacy.OmsorgLegacyData
import no.nav.pensjon.brev.api.model.maler.legacy.Vedtaksbrev
import no.nav.pensjon.brev.api.model.maler.legacy.Vedtaksdata
import no.nav.pensjon.brev.api.model.maler.legacy.redigerbar.OmsorgLegacyDataDto
import no.nav.pensjon.brevbaker.api.model.Year
import java.time.LocalDate
import java.time.Month

fun createOmsorgLegacyDataDto() = OmsorgLegacyDataDto(
    saksbehandlerValg = EmptyBrevdata,
    pesysData = OmsorgLegacyDataDto.PesysData(
        omsorgLegacyData = OmsorgLegacyData(
            vedtaksbrev = Vedtaksbrev(
                grunnlag = Grunnlag(
                    omsorgGodskrGrunnlagListe = listOf(
                        OmsorgGodskrGrunnlag(Year(1987)),
                        OmsorgGodskrGrunnlag(Year(1988))
                    )
                ),
                vedtaksdata = Vedtaksdata(
                    kravhode = Kravhode(
                        kravmottattdato = LocalDate.of(2020, Month.JULY, 1)
                    )
                )
            ),
        )
    )
)