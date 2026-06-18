package no.nav.pensjon.brev.template

/**
 * Stabil og unik identifikator for et redigerbart vedlegg innenfor et brev.
 * Brukes til å koble en saksbehandlers overstyring til riktig vedlegg.
 */
@JvmInline
value class VedleggId(val id: String)
