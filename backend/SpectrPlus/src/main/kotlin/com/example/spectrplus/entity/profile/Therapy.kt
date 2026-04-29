package com.example.spectrplus.entity.profile

import jakarta.persistence.*

@Entity
@Table(name = "therapies")
class Therapy(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val childId: Long,

    val title: String,

    val description: String
)