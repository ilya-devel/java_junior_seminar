plugins {
    id("java")
}

group = "seminar"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("org.hibernate.orm:hibernate-platform:6.4.4.Final")
    implementation("org.hibernate:hibernate-core:6.4.4.Final")
    implementation("com.h2database:h2:2.2.224")
    implementation("jakarta.persistence:jakarta.persistence-api:3.2.0-M2")
    implementation("jakarta.transaction:jakarta.transaction-api")
}

tasks.test {
    useJUnitPlatform()
}