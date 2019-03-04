package com.home.smarthomeserver

import com.home.smarthomeserver.entity.ChildUserEntity
import com.home.smarthomeserver.entity.ParentUserEntity
import com.home.smarthomeserver.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
interface UserRepository<T : User> : JpaRepository<T, String> {
    fun findUserByName(name: String): User?

    fun existsUserByName(name: String): Boolean
}

interface ParentUserRepository : UserRepository<ParentUserEntity> {
    override fun existsUserByName(name: String): Boolean

    override fun findUserByName(name: String): ParentUserEntity


}

interface ChildUserRepository : UserRepository<ChildUserEntity>