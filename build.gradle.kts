plugins {
    id("java")
}

group = "com.nibble"
version = "1.0-SNAPSHOT"

subprojects{
    apply (plugin="java")

    java{
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    repositories {
        mavenCentral()
    }
    dependencies {
        testImplementation(platform("org.junit:junit-bom:5.10.0"))
        testImplementation("org.junit.jupiter:junit-jupiter")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    }
    tasks.test {
        useJUnitPlatform()
    }

}



