rootProject.name = "tricefal-core"

include(":application")
include(":application:authentication-keycloak")
include(":application:authentication-okta")
include(":application:account")
include(":application:note")

include(":domain")
include(":domain:authentication")
include(":domain:account")
include(":domain:note")

include(":infrastructure")
include(":infrastructure:account")
include(":infrastructure:encryption")
include(":infrastructure:login")
include(":infrastructure:notification")
include(":infrastructure:note")
include(":infrastructure:profile")
include(":infrastructure:signup")
include(":infrastructure:storage")

include(":infrastructure:iam-client")
include(":infrastructure:iam-client:iam-client-interface")
include(":infrastructure:iam-client:okta-client")
include(":infrastructure:iam-client:keycloak-client")