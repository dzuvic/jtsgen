plugins {
    java
    `java-library`
}

dependencies {
    compileOnly(project(":annotations"))
    annotationProcessor(project(":processor"))
}