package io.oneprofile.core.signup

import java.time.Instant
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
@Table(name = "comment")
data class CommentEntity(
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Id
        var id: Long? = null,

        @NotNull
        @Size(min = 10, max = 100)
        @Column(name = "target_subject", length = 100)
        var targetSubject: String? = null,

        @Column(name = "summary", length = 150)
        var summary: String? = null,

        @Column(name = "comment", length = 2000)
        var comment: String? = null,

        @Column(name = "author", length = 50)
        var author: String? = null,

        @Column(name = "rating")
        var rating: String? = null,

        @Column(name = "last_date")
        var lastDate: Instant? = null,

)

fun toEntity(domain: CommentDomain): CommentEntity {
        return CommentEntity(
                id = null,
                targetSubject = domain.targetSubject,
                summary = domain.summary,
                comment = domain.comment,
                rating = domain.rating.toString(),
                author = domain.author,
                lastDate = domain.lastDate
        )
}

fun fromEntity(entity: CommentEntity): CommentDomain {
        return CommentDomain.Builder()
                .targetSubject(entity.targetSubject)
                .summary(entity.summary)
                .comment(entity.comment)
                .author(entity.author)
                .rating(Rating.valueOf(entity.rating!!))
                .lastDate(entity.lastDate)
                .build()
}