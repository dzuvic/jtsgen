/*
 * Copyright (c) 2017-2020 Dragan Zuvic
 *
 * This file is part of jtsgen.
 *
 * jtsgen is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * jtsgen is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with jtsgen.  If not, see http://www.gnu.org/licenses/
 *
 */

plugins {
    `java-library`
    id("jtsgen.java-processor")
    id("jtsgen.publish-maven")
    jacoco
}

description = "The jtsgen annotation processor"

dependencies {
    api(project(":annotations"))
}

java {
    withSourcesJar()
    withJavadocJar()
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}

jacoco {
    val jacocoVersion : String by project
    toolVersion = jacocoVersion
    reportsDir = file("$buildDir/jacoco")
}


tasks.test {
    extensions.configure(JacocoTaskExtension::class) {
        destinationFile = file("$buildDir/jacoco/jacocoTest.exec")
        excludes = listOf(
            "/dz/jtsgen/processor/dsl/model/TSMappedTerminalBuilder*",
            "/dz/jtsgen/processor/dsl/model/TSMappedTypeContainerBuilder*",
            "/dz/jtsgen/processor/dsl/model/TSMappedTypeVarBuilder*",
            "/dz/jtsgen/processor/dsl/model/TypeMappingExpressionBuilder*",
            "/dz/jtsgen/processor/dsl/parser/TokenBuilder*",
            "/dz/jtsgen/processor/model/NameSpaceMappingBuilder*",
            "/dz/jtsgen/processor/model/TSEnumBuilder*",
            "/dz/jtsgen/processor/model/TSEnumMemberBuilder*",
            "/dz/jtsgen/processor/model/TSInterfaceBuilder*",
            "/dz/jtsgen/processor/model/TSModuleInfoBuilder*",
            "/dz/jtsgen/processor/model/TSRegularMemberBuilder*",
            "/dz/jtsgen/processor/model/TSTypeVariableBuilder*",
            "/dz/jtsgen/processor/jtp/info/TSProcessingInfoBuilder*",
            "/dz/jtsgen/processor/jtp/info/ExecutableElementHelperImplBuilder*"
        )
    }
}



tasks.jacocoTestReport {
    reports {
        xml.isEnabled = false
        csv.isEnabled = false
        html.isEnabled = false
    }
}

tasks.javadoc {
    exclude("dz/jtsgen/processor/model/**")
}

tasks.jar {
    manifest {
        attributes(
            mapOf(
                "Implementation-Title" to "TSGen Annotations",
                "Implementation-Version" to project.version
            )
        )
    }
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = "jtsgen-processor"
            from(components["java"])
            versionMapping {
                usage("java-api") {
                    fromResolutionOf("runtimeClasspath")
                }
                usage("java-runtime") {
                    fromResolutionResult()
                }
            }
            pom {
                name.set("JTSGEN Annotations")
                description.set("An annotation processor, that created TypeScript types from Java")
                url.set("http://jtsgen.readthedocs.io")
                licenses {
                    license {
                        name.set("GNU General Public License (GPL) version 3.0")
                        url.set("https://www.gnu.org/licenses/gpl-3.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("dzuvic")
                        name.set("Dragan Zuvic")
                        email.set("git.zuvic@posteo.de")
                    }
                }
                scm {
                    connection.set("scm:git:https://github.com/dzuvic/jtsgen.git")
                    developerConnection.set("scm:git:https://github.com/dzuvic/jtsgen.git")
                    url.set("https://github.com/dzuvic/jtsgen.git")
                }
            }
        }
    }
}


signing {
    sign(publishing.publications["mavenJava"])
}
