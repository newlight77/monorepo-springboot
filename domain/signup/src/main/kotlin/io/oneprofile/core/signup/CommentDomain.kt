package io.oneprofile.core.signup

import java.time.Instant

class CommentDomain(var targetSubject: String?,
                    var summary: String?,
                    var comment: String?,
                    var author: String?,
                    var rating: Rating?,
                    var lastDate: Instant?
) {
    data class Builder (
        var targetSubject: String? = null,
        var summary: String? = null,
        var comment: String? = null,
        var author: String? = null,
        var rating: Rating? = Rating.BLANK,
        var lastDate: Instant? = null,
    ) {
        fun targetSubject(targetSubject: String?) = apply { this.targetSubject = targetSubject }
        fun summary(summary: String?) = apply { this.summary = summary }
        fun comment(comment: String?) = apply { this.comment = comment }
        fun author(author: String?) = apply { this.author = author }
        fun rating(rating: Rating?) = apply { this.rating = rating }
        fun lastDate(lastDate: Instant?) = apply { this.lastDate = lastDate }

        fun build() = CommentDomain(
            targetSubject = targetSubject,
            summary = summary,
            comment = comment,
            author = author,
            rating = rating,
            lastDate = lastDate
        )
    }
}

enum class Rating(val rate: Int) {
    BLANK(0),
    ONE(1),
    TWO(1),
    THREE(3),
    FOUR(4),
    FIVE(5)
}