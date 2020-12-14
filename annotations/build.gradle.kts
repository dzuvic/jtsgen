/*
 * Copyright (c) 2020 Dragan Zuvic
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
    id("jtsgen.java-common")
    id("jtsgen.publish-maven")
}


java {
    withSourcesJar()
    withJavadocJar()
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
            artifactId = "jtsgen-annotations"
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
                description.set("The Annotation an other Types for the jtsgen annotation processor")
                url.set("http://jtsgen.readthedocs.io")
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
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
