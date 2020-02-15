package org.n52.spare.kicker.security

import org.n52.spare.kicker.model.Player
import org.n52.spare.kicker.repositories.PlayerRepository
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.provisioning.UserDetailsManager
import org.springframework.stereotype.Component

class RepositoryUserDetailsManager(private val playerRepo: PlayerRepository, private val passwordEncoder: PasswordEncoder) : UserDetailsManager {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val opt = this.playerRepo.findByName(username)
        if (opt.isPresent) {
            return fromPlayer(opt.get())
        }
        throw UsernameNotFoundException("User not found: $username")
    }

    override fun createUser(user: UserDetails) {}

    override fun updateUser(user: UserDetails) {}

    override fun deleteUser(username: String) {}

    override fun changePassword(oldPassword: String, newPassword: String) {
        val auth = SecurityContextHolder.getContext().authentication
        if (auth != null) {
            val details = auth.principal as UserDetails
            if (!passwordEncoder.matches(oldPassword, details.password)) {
                throw IllegalStateException("Old password did not match")
            }

            val playerOpt = playerRepo.findByName(details.username)
            if (playerOpt.isPresent) {
                val player = playerOpt.get()
                player.password = passwordEncoder.encode(newPassword)
                playerRepo.save(player)
            }
        }
    }

    override fun userExists(username: String): Boolean {
        val opt = this.playerRepo.findByName(username)
        return opt.isPresent
    }

    public fun playerFromAuthentication(auth: Authentication): Player {
        val details = auth.principal as UserDetails
        val playerOpt = playerRepo.findByName(details.username)
        return playerOpt.get()
    }

    private fun fromPlayer(p: Player): UserDetails {
        return User.builder()
                .username(p.nickName!!)
                .password(p.password!!)
                .roles("USER")
                .build()
    }


}
