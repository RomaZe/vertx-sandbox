import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.tasks.testing.logging.TestLogEvent.*

plugins {
  java
  application
  id("com.github.johnrengelman.shadow") version "7.0.0"
  id("io.spring.dependency-management") version "1.0.1.RELEASE"
}

group = "io.zoro.vertx"
version = "1.0.0-SNAPSHOT"

repositories {
  mavenCentral()
}

val vertxVersion = "4.2.1"
val versionJackson = "2.11.4"
val junitJupiterVersion = "5.7.0"

val mainVerticleName = "io.zoro.vertx.MainVerticle"
val launcherClassName = "io.vertx.core.Launcher"

val watchForChange = "src/**/*"
val doOnChange = "${projectDir}/gradlew classes"

application {
  mainClass.set(launcherClassName)
}

dependencyManagement {
  imports {
    mavenBom("org.apache.logging.log4j:log4j-bom:2.14.1")
  }
}

dependencies {
  implementation(platform("io.vertx:vertx-stack-depchain:$vertxVersion"))
  implementation("io.vertx:vertx-core")
  implementation("io.vertx:vertx-jdbc-client")
  implementation("io.agroal:agroal-api:1.13")
  implementation("io.agroal:agroal-pool:1.13")
  implementation("org.postgresql:postgresql:42.3.1")
  implementation("com.fasterxml.jackson.core:jackson-databind:$versionJackson")
  implementation("org.apache.logging.log4j:log4j-api")
  implementation("org.apache.logging.log4j:log4j-core")
  implementation("org.apache.logging.log4j:log4j-slf4j-impl")
  implementation("org.slf4j:slf4j-api:1.7.30")
  testImplementation("io.vertx:vertx-junit5")
  testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
}

java {
  sourceCompatibility = JavaVersion.VERSION_11
  targetCompatibility = JavaVersion.VERSION_11
}

tasks.withType<ShadowJar> {
  archiveClassifier.set("fat")
  manifest {
    attributes(mapOf("Main-Verticle" to mainVerticleName))
  }
  mergeServiceFiles()
}

tasks.withType<Test> {
  useJUnitPlatform()
  testLogging {
    events = setOf(PASSED, SKIPPED, FAILED)
  }
}

tasks.withType<JavaExec> {
  args = listOf("run", mainVerticleName, "--redeploy=$watchForChange", "--launcher-class=$launcherClassName", "--on-redeploy=$doOnChange")
}
