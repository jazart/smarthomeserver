package com.home.smarthomeserver

import com.home.smarthomeserver.entity.ChildUser
import com.home.smarthomeserver.entity.ParentUser
import com.home.smarthomeserver.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.repository.NoRepositoryBean

@NoRepositoryBean
interface UserRepository<T : User> : JpaRepository<T, String> {
    fun findUserByName(name: String): User?

    fun existsUserByName(name: String): Boolean
}

interface ParentUserRepository : UserRepository<ParentUser> {
    override fun existsUserByName(name: String): Boolean

    override fun findUserByName(name: String): ParentUser


}

interface ChildUserRepository : UserRepository<ChildUser>