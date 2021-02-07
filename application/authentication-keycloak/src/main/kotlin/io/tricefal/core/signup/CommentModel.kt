package io.tricefal.core.signup

import java.time.Instant

class CommentModel(var targetSubject: String?,
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

        fun build() = CommentModel(
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

fun toModel(domain: CommentDomain): CommentModel {
    return CommentModel.Builder()
        .targetSubject(domain.targetSubject)
        .summary(domain.summary)
        .comment(domain.comment)
        .author(domain.author)
        .rating(domain.rating)
        .lastDate(domain.lastDate)
        .build()
}

fun fromModel(model: CommentModel): CommentDomain {
    return CommentDomain.Builder()
        .targetSubject(model.targetSubject)
        .summary(model.summary)
        .comment(model.comment)
        .author(model.author)
        .rating(model.rating)
        .lastDate(model.lastDate)
        .build()
}