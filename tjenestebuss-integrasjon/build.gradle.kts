import io.ktor.plugin.features.*

val javaTarget: String by System.getProperties()
val kotlinVersion: String by System.getProperties()
val ktorVersion: String by System.getProperties()
val logbackVersion: String by project
val logstashVersion: String by project

plugins {
	application
	kotlin("jvm")
	id("io.ktor.plugin")
}

group = "no.nav.pensjon.brev.tjenestebuss"
version = "0.0.1"

application {
	mainClass.set("no.nav.pensjon.brev.tjenestebussintegrasjon.TjenestebussIntegrasjonApplicationKt")
}

data class GithubImageRegistry(override val toImage: Provider<String>, override val username: Provider<String>, override val password: Provider<String>) : DockerImageRegistry

ktor {
	fatJar {
		archiveFileName.set("app.jar")
	}
	docker {
		jreVersion.set(JavaVersion.VERSION_17)
		localImageName.set("pensjon-skribenten")
		imageTag.set(providers.environmentVariable("IMAGE_TAG").orElse("latest"))
		// TODO add workflow for deploying
		//externalRegistry.set(
		//	GithubImageRegistry(
		//		toImage = providers.environmentVariable(//TODOIMAGE_SKRIBENTEN_BACKEND),
		//		username = providers.environmentVariable(//TODOGITHUB_REPOSITORY),
		//		password = providers.environmentVariable(//TODOGITHUB_TOKEN),
		//	)
		//)
	}
}

val cxfVersion = "3.6.0"
dependencies {
	implementation("io.ktor:ktor-serialization-jackson:$ktorVersion")
	implementation("io.ktor:ktor-server-call-id:$ktorVersion")
	implementation("io.ktor:ktor-server-call-logging:$ktorVersion")
	implementation("io.ktor:ktor-server-content-negotiation:$ktorVersion")
	implementation("io.ktor:ktor-server-core-jvm:$ktorVersion")
	implementation("io.ktor:ktor-server-netty-jvm:$ktorVersion")
	implementation("io.ktor:ktor-server-status-pages:$ktorVersion")
	implementation("io.ktor:ktor-client-auth:$ktorVersion")
	implementation("io.ktor:ktor-client-cio:$ktorVersion")
	implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")

	implementation("no.nav.pensjon.pesys-esb-wsclient:pcom-esb-wsclient-legacy:2023.11.01-10.31-1bc8315f412e") {
	}
	implementation("no.nav.pensjon.pesys-esb-wsclient:psak-esb-wsclient-legacy:2023.11.01-10.31-1bc8315f412e") {
	}
	implementation("javax.xml.ws:jaxws-api:2.3.1")
	implementation("com.sun.xml.ws:jaxws-tools:2.3.0.2")
	implementation("com.sun.xml.bind:jaxb-impl:3.0.2")
	implementation("org.apache.cxf:cxf-rt-features-logging:$cxfVersion")
	implementation("org.apache.cxf:cxf-rt-frontend-jaxws:$cxfVersion")
	implementation("org.apache.cxf:cxf-rt-ws-policy:$cxfVersion")
	implementation("org.apache.cxf:cxf-rt-transports-http:$cxfVersion")

}

repositories {
	maven {
		// Create a token at https://github.com/settings/tokens/new with package.read
		// Then create a gradle.properties file in $HOME/.gradle with the following:
		// gpr.user=<your github username>
		// gpr.token=<the token>
		url = uri("https://maven.pkg.github.com/navikt/pesys-esb-wsclient")
		credentials {
			username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
			password = project.findProperty("gpr.token") as String? ?: System.getenv("GITHUB_TOKEN")
		}
		metadataSources {
			artifact() //Look directly for artifact
		}
		content {
			includeGroup("no.nav.pensjon.pesys-esb-wsclient")
		}
	}
}

tasks {
	withType<Test>{
		useJUnitPlatform()
	}
	compileKotlin {
		kotlinOptions{
			jvmTarget = javaTarget
			freeCompilerArgs += "-Xjsr305=strict"
		}
	}
	compileTestKotlin {
		kotlinOptions{
			jvmTarget = javaTarget
			freeCompilerArgs += "-Xjsr305=strict"
		}
	}
	compileJava {
		targetCompatibility = javaTarget
	}
	compileTestJava {
		targetCompatibility = javaTarget
	}
}

sourceSets {
	main {
		resources {
			srcDir("secrets")
		}
	}
}
