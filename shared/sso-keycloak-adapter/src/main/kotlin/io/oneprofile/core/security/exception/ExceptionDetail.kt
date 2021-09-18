package io.oneprofile.core.security.exception

internal class ExceptionDetail(
    val classname: String? = null,
    val date: String? = null,
    val message: String? = null,
    val path: String? = null,
    val params: String? = null) {

    data class Builder (
            var classname: String? = null,
            var date: String? = null,
            var message: String? = null,
            var path: String? = null,
            var params: String? = null) {
        fun classname(classname: String?) = apply { this.classname = classname }
        fun date(date: String?) = apply { this.date = date }
        fun message(message: String?) = apply { this.message = message }
        fun path(path: String?) = apply { this.path = path }
        fun params(params: String?) = apply { this.params = params }

        fun build() = ExceptionDetail(
                classname,
                date,
                message,
                path,
                params)
    }
}