package com.hebe.hebemanyepxa.repository

import com.hebe.hebemanyepxa.model.ApparelImage
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ImageRepository : JpaRepository<ApparelImage, Long> {
    fun findByApparelId(apparelId: Long): List<ApparelImage>
    fun findByApparelIdAndIsPrimaryTrue(apparelId: Long): ApparelImage?
    fun deleteByApparelId(apparelId: Long)
}