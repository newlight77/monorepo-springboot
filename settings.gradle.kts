rootProject.name = "tricefal-core"

include(":application")
include(":application:authentication-keycloak")
include(":application:authentication-okta")
include(":application:note")

include(":domain")
include(":domain:authentication")
include(":domain:freelance")
include(":domain:metafile")
include(":domain:mission")
include(":domain:note")

include(":infrastructure")
include(":infrastructure:freelance")
include(":infrastructure:encryption")
include(":infrastructure:login")
include(":infrastructure:mission")
include(":infrastructure:notification")
include(":infrastructure:note")
include(":infrastructure:profile")
include(":infrastructure:signup")
include(":infrastructure:storage")

include(":infrastructure:iam-client")
include(":infrastructure:iam-client:iam-client-interface")
include(":infrastructure:iam-client:okta-client")
include(":infrastructure:iam-client:keycloak-client")