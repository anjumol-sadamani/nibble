plugins {
    id("io.spring.dependency-management") version "1.1.4"
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.security:spring-security-bom:6.2.1")
    }
}

dependencies {
    implementation("org.springframework.security:spring-security-web")
    implementation("org.springframework:spring-web")

    compileOnly("jakarta.servlet:jakarta.servlet-api:6.0.0")

    implementation("io.jsonwebtoken:jjwt-api:0.12.3")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.12.3")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.12.3")
}