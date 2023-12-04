package no.nav.pensjon.brev.skribenten.services

import com.typesafe.config.Config

class SkribentenDatabaseService(config: Config) {
    private val url = config.getString("url").also {
        println(it)
    }
}