package no.nav.pensjon.brev.api.model

/**
 * Kategoriene og sakstypene en redigerbar mal kan merkes med.
 *
 * Disse ligger i brevdata og ikke sammen med [TemplateDescription] fordi det er *bestillerne* —
 * api-modellene og malene — som implementerer dem. `TemplateDescription` er selve HTTP-svaret og
 * hører hjemme i brevbaker-api, som bestillerne ikke skal trenge å kompilere mot.
 */
interface IBrevkategori {
    val kode: String
}

interface ISakstype {
    val kode: String
}
