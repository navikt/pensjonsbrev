import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

val javaTarget: String by System.getProperties()
val kotlinVersion: String by project

plugins {
	application
	id("org.springframework.boot") version "3.1.5"
	id("io.spring.dependency-management") version "1.1.3"
	kotlin("jvm")
	kotlin("plugin.spring") version "1.8.22"
}

group = "no.nav.pensjon.brev.tjenestebuss"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web-services")
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	implementation("no.nav.pensjon.pesys-esb-wsclient:psak-esb-wsclient-legacy:2023.11.01-10.31-1bc8315f412e") {
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
			}
		}
	}
	implementation("javax.xml.ws:jaxws-api:2.3.1")
	implementation("com.sun.xml.ws:jaxws-tools:2.3.0.2")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
}

repositories{
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
	}
}
tasks {
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