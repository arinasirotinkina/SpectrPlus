package com.example.spectrplus.repository.profile

import com.example.spectrplus.entity.profile.Child
import org.springframework.data.jpa.repository.JpaRepository

interface ChildRepository : JpaRepository<Child, Long> {
    fun findByUserId(userId: Long): List<Child>
}