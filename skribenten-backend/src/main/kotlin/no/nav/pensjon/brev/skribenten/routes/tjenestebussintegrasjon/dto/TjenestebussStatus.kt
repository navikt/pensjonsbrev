package no.nav.pensjon.brev.skribenten.routes.tjenestebussintegrasjon.dto

data class TjenestebussStatus(val overall: Boolean, val services: Map<String, Boolean?>, val errors: Map<String, String>)
