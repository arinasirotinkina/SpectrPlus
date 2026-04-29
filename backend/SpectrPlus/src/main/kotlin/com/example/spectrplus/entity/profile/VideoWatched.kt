package com.example.spectrplus.entity.profile

import jakarta.persistence.*

@Entity
@Table(
    name = "video_watched",
    uniqueConstraints = [UniqueConstraint(columnNames = ["user_id", "video_id"])]
)
class VideoWatched(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "user_id")
    val userId: Long,

    @Column(name = "video_id")
    val videoId: Long
)
