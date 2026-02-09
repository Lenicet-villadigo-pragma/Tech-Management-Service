rootProject.name = "Tech-Management-Service"

include(
    "tech-management-service-domain",
    "tech-management-service-application",
    "tech-management-service-infrastructure"
)

project(":tech-management-service-domain").projectDir = file("tech-management-service-domain")
project(":tech-management-service-application").projectDir = file("tech-management-service-application")
project(":tech-management-service-infrastructure").projectDir = file("tech-management-service-infrastructure")


