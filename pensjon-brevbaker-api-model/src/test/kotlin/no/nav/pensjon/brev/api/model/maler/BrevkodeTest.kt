package no.nav.pensjon.brev.api.model.maler

import org.junit.Assert.assertThrows
import java.util.stream.Collectors
import java.util.stream.IntStream
import kotlin.test.Test

class BrevkodeTest {

    @Test
    fun `tittel paa under 50 tegn er OK for redigerbar`() {
        val langTittel = IntStream.range(0, 15).mapToObj { "a" }.collect(Collectors.joining())
        RedigerbarBrevkode(langTittel)
    }
    @Test
    fun `tittel paa over 50 tegn feiler for redigerbar`() {
        val langTittel = IntStream.range(0, 51).mapToObj { "b" }.collect(Collectors.joining())
        assertThrows(IllegalArgumentException::class.java) { RedigerbarBrevkode(langTittel) }
    }


    @Test
    fun `tittel paa under 50 tegn er OK for automatisk`() {
        val langTittel = IntStream.range(0, 15).mapToObj { "c" }.collect(Collectors.joining())
        AutomatiskBrevkode(langTittel)
    }
    @Test
    fun `tittel paa over 50 tegn feiler for automatisk`() {
        val langTittel = IntStream.range(0, 51).mapToObj { "d" }.collect(Collectors.joining())
        assertThrows(IllegalArgumentException::class.java) { AutomatiskBrevkode(langTittel) }
    }

    @Test
    fun `alle brevkoder skal være unike`(){
        val redigerbareBrev = Pesysbrevkoder.Redigerbar.entries.map { it.toString() }
        val autobrev = Pesysbrevkoder.AutoBrev.entries.map { it.toString() }
        assert(redigerbareBrev.intersect(autobrev.toSet()).isEmpty())
    }

}