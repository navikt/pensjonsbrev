package no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon

fun maskerFnr(tekst: String?): String? = tekst?.replace("\\b[0-9]{11}\\b".toRegex(), "[MASKERT FNR]")
