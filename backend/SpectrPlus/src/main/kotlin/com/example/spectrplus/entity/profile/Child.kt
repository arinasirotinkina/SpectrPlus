package com.example.spectrplus.entity.profile

import jakarta.persistence.*

@Entity
@Table(name = "children")
class Child(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    val userId: Long,

    var name: String,

    var age: Int,

    var gender: String,

    var diagnosis: String,

    var features: String,

    var notes: String
)