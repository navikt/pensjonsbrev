package no.nav.pensjon.brev.skribenten.services

import com.fasterxml.jackson.module.kotlin.readValue
import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.redigerbar.InformasjonOmSaksbehandlingstidDto
import java.time.LocalDate
import kotlin.test.Test


class BrevredigeringTest {

    @Test
    fun x() {
        val xx = objectMapper.writeValueAsString(InformasjonOmSaksbehandlingstidDto(
            ytelse = "class",
            land = "Norge",
            inkluderVenterSvarAFP = null,
            svartidUker = 8961,
            mottattSoeknad = LocalDate.now()
        ))
        println(xx)
        objectMapper.readValue<BrevbakerBrevdata>(xx)
    }
}