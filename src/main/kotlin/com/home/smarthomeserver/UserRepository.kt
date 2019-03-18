package com.home.smarthomeserver

import com.home.smarthomeserver.entity.ChildUserEntity
import com.home.smarthomeserver.entity.ParentUserEntity
import com.home.smarthomeserver.entity.User
import com.home.smarthomeserver.models.ChildUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
interface UserRepository<T : User> : JpaRepository<T, String> {
    fun findUserByUsername(username: String): User?
    fun existsUserByUsername(name: String): Boolean
}

interface ParentUserRepository : UserRepository<ParentUserEntity> {
    fun existsParentUserEntityByUsername(name: String): Boolean
    override fun findUserByUsername(username: String): ParentUserEntity
    fun existsParentUserEntityByEmail(email: String): Boolean

}

interface ChildUserRepository : UserRepository<ChildUserEntity>{
    override fun findUserByUsername(username: String): ChildUserEntity
    override fun existsUserByUsername(name: String): Boolean
}