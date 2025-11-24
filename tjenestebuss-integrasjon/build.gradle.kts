import org.jetbrains.kotlin.gradle.dsl.JvmTarget

val javaTarget: String by System.getProperties()

plugins {
	application
	kotlin("jvm")
}

group = "no.nav.pensjon.brev.tjenestebuss"
version = "0.0.1"

repositories {
    maven {
        url = uri("https://github-package-registry-mirror.gc.nav.no/cached/maven-release")
    }
}

application {
	mainClass.set("no.nav.pensjon.brev.tjenestebuss.tjenestebussintegrasjon.TjenestebussIntegrasjonApplicationKt")
}

// Før du oppdaterer cxf-versjonen, sjekk readme-fila for denne modulen.
val cxfVersion = "4.1.4"
val tjenestespesifikasjonerVersion = "1.2024.10.21-13.17-04e1c7bb6f55"
dependencies {
	implementation(libs.ktor.serialization.jackson)
	implementation(libs.ktor.server.callId)
	implementation(libs.ktor.server.callLogging)
	implementation(libs.ktor.server.content.negotiation)
	implementation(libs.ktor.server.core.jvm)
	implementation(libs.ktor.server.netty.jvm)
	implementation(libs.ktor.server.status.pages)
	implementation(libs.ktor.client.auth)
	implementation(libs.ktor.server.auth.jwt)
	implementation(libs.ktor.client.cio)
	implementation(libs.ktor.client.content.negotiation)
	implementation(libs.bundles.logging)

	implementation("no.nav.tjenestespesifikasjoner.pensjon:samhandler-tjenestespesifikasjon:$tjenestespesifikasjonerVersion") {
        exclude("com.sun.xml.ws", "jaxws-ri")
        exclude("com.sun.xml.bind", "jaxb-core")
        exclude("org.eclipse.angus", "angus-mail")
    }

    // Denne brukes transitivt gjennom samhandler-tjenestespesifikasjon og cxf-rt-frontend-jaxws.
    // Der har de per nå 2.0.3, som er sårbar, så vi overstyrer til patchen ett hakk før, som har retta en alvorlig CVE
    // Fjern denne rada, samt exclude av transitiv avhengighet, når vi får ny cxf-versjon eller ny samhandler-versjon med oppdatert angus-mail.
    implementation("org.eclipse.angus:angus-mail:2.0.4")

	implementation("org.apache.cxf:cxf-rt-features-logging:$cxfVersion")
	implementation("org.apache.cxf:cxf-rt-frontend-jaxws:$cxfVersion") {
        exclude("org.eclipse.angus", "angus-mail")
    }
	implementation("org.apache.cxf:cxf-rt-ws-policy:$cxfVersion")
	implementation("org.apache.cxf:cxf-rt-transports-http:$cxfVersion")

	implementation(libs.bundles.metrics)

	// Test
    testImplementation(libs.bundles.junit)
	testImplementation(libs.ktor.server.test.host)
    testImplementation("org.apache.cxf:cxf-rt-transports-http-jetty:$cxfVersion")
	testImplementation(libs.hamkrest)
}

sourceSets {
	main {
		resources {
			srcDir("secrets")
		}
	}
}

kotlin {
	compilerOptions {
		jvmTarget.set(JvmTarget.fromTarget(javaTarget))
        freeCompilerArgs.add("-Xjsr305=strict")
    }
}

tasks {
	compileJava {
		targetCompatibility = javaTarget
	}
	compileTestJava {
		targetCompatibility = javaTarget
	}
    test {
        useJUnitPlatform()
    }
    build {
        dependsOn(installDist)
    }
}
