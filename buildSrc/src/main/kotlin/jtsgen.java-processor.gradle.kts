
plugins {
    id("jtsgen.java-common")
    java
}

//repositories {
//    mavenCentral()
//}

val aptOutDir="${projectDir}/src/main/generated"
val aptOutTestDir="${projectDir}/src/test/generated"

dependencies {

    val junitJupiterVersion : String by project
    val mockitoVersion : String by project
    val compileTestingVersion : String by project
    val commonsIoVersion : String by project
    val guavaVersion : String by project
    val generexVersion : String by project
    val log4jVersion : String by project
    val coverallsVersion : String by project
    val immutablesVersion : String by project
    val apiGuardianVersion : String by project

    compileOnly("org.immutables:builder:${immutablesVersion}")
    compileOnly("org.immutables:value:${immutablesVersion}")
    annotationProcessor("org.immutables:value:${immutablesVersion}")

    testCompileOnly("org.apiguardian:apiguardian-api:${apiGuardianVersion}")
    testRuntimeOnly("org.apache.logging.log4j:log4j-core:${log4jVersion}")
    testRuntimeOnly("org.apache.logging.log4j:log4j-jul:${log4jVersion}")

    testImplementation( "com.google.testing.compile:compile-testing:${compileTestingVersion}" )
    testImplementation( "org.mockito:mockito-core:${mockitoVersion}" )
    testImplementation( "commons-io:commons-io:${commonsIoVersion}" )
    testImplementation( "com.google.guava:guava:${guavaVersion}" )
    testImplementation( "com.github.mifmif:generex:${generexVersion}" )


    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitJupiterVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}


sourceSets {
    main {
        java {
            srcDir(aptOutDir)
        }
    }
}

tasks.compileJava {

    options.annotationProcessorGeneratedSourcesDirectory=file(aptOutDir)
    options.compilerArgs.add("-parameters")
}


tasks.test {
    useJUnitPlatform() // <5>
}
