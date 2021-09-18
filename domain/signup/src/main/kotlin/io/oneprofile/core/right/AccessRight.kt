package io.oneprofile.core.right

enum class AccessRight(val label: String) {
    AC_BILLING_READ("ac_billing_r"),
    AC_BILLING_WRTIE("ac_billing_w"),
    AC_CLIENT_READ("ac_client_r"),
    AC_CLIENT_WRTIE("ac_client_w"),
    AC_COLLABORATOR_READ("ac_collaborator_r"),
    AC_COLLABORATOR_WRITE("ac_collaborator_w"),
    AC_CONTRACT_READ("ac_contract_r"),
    AC_CONTRACT_WRITE("ac_contract_w"),
    AC_FREELANCE_READ("ac_freelance_r"),
    AC_FREELANCE_WRITE("ac_freelance_w"),
    AC_STEERING_READ("ac_steering_r"),
    AC_STEERING_WRITE("ac_steering_w"),
    AC_TRICEFAL_READ("ac_tricefal_r"),
    AC_TRICEFAL_WRTIE("ac_tricefal_w")
}