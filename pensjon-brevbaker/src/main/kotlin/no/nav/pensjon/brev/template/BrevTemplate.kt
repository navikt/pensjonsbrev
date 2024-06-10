package no.nav.pensjon.brev.template

import no.nav.pensjon.brev.api.model.maler.BrevbakerBrevdata
import no.nav.pensjon.brev.api.model.maler.Brevkode
import no.nav.pensjon.brev.api.model.maler.RedigerbarBrevdata

interface BrevTemplate<out LetterData : BrevbakerBrevdata, Kode : Enum<Kode>> : HasModel<LetterData> {
    val template: LetterTemplate<*, LetterData>
    val kode: Kode
}

interface RedigerbarTemplate<LetterData : RedigerbarBrevdata<out BrevbakerBrevdata, out BrevbakerBrevdata>> :
    BrevTemplate<LetterData, Brevkode.Redigerbar>

interface AutobrevTemplate<out LetterData : BrevbakerBrevdata> : BrevTemplate<LetterData, Brevkode.AutoBrev>