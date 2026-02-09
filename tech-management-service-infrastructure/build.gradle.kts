plugins {
    id("org.springframework.boot")
}

dependencies {
    implementation(project(":tech-management-service-domain"))
    implementation(project(":tech-management-service-application"))
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springdoc:springdoc-openapi-starter-webflux-ui:2.6.0")
    runtimeOnly("io.asyncer:r2dbc-mysql:1.0.2")

}
