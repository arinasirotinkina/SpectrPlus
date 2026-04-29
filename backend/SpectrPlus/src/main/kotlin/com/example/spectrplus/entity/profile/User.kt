package com.example.spectrplus.entity.profile

import jakarta.persistence.*
import jakarta.persistence.Id

@Entity
@Table(name = "users")
class User(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    @Column(unique = true)
    var email: String,

    var phone: String,

    var firstName: String,

    var lastName: String,

    var password: String,

    /** Город; колонка в БД остаётся address для совместимости со старыми инсталляциями */
    @Column(name = "address")
    var city: String? = null,

    var avatarUrl: String? = null,

    @Enumerated(EnumType.STRING)
    var accountRole: AccountRole = AccountRole.PARENT,

    var showChildInPublicProfile: Boolean = false,

    var specialistProfession: String? = null,

    @Column(columnDefinition = "TEXT")
    var specialistEducation: String? = null,

    var specialistExperienceYears: Int? = null
)