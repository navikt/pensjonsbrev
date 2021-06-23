package no.nav.pensjon.brev.pdfbygger

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/** TODO 'S
 * escape bruker-input til latex
 * flytt relevante tex filer og pdflatex kompileringer til uuid taggede mapper og bruk in-memory mappe
 * konverter hovedmalen til noe importer-bart hvor vi bare legger inn tekster med kommando
 * bruk alle maler som resources fra classpath, og ikke fra disk
 * gjør felter dynamiske eller konfigurerbare pr mal og lag felter i front-end ut i fra disse
 ** 	lag eget api for å hente felt-informasjon
 **
 */
@SpringBootApplication
class BrevmakerApplication

fun main(args: Array<String>) {
	runApplication<BrevmakerApplication>(*args)
}
