package com.example.spectrplus.entity.profile

import jakarta.persistence.*
import java.time.LocalDate

@Entity
@Table(name = "plans")
class Plan(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "user_id")
    val userId: Long,

    var date: LocalDate,

    var title: String,

    var time: String,

    var done: Boolean = false
)
