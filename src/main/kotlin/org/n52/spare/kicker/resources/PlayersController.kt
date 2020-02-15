package org.n52.spare.kicker.resources

import java.security.Principal

import org.n52.spare.kicker.repositories.PlayerRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest

import com.fasterxml.jackson.annotation.JsonView
import org.n52.spare.kicker.model.Match
import org.n52.spare.kicker.model.PageableResponse
import org.n52.spare.kicker.model.Player
import org.n52.spare.kicker.model.Views
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/players")
class PlayersController {

    @Autowired
    private val playerRepository: PlayerRepository? = null


    @JsonView(Views.Basic::class)
    @RequestMapping("")
    fun collection(@RequestParam(required = false) page: Optional<Int>,
                   @RequestParam(required = false) size: Optional<Int>): PageableResponse<Player> {
        val result = playerRepository!!.findAll(PageRequest.of(page.orElse(0), size.orElse(10)))
        return PageableResponse.from(result);
    }

    @JsonView(Views.Details::class)
    @RequestMapping("/{id}")
    fun single(@PathVariable id: Long?): Player {
        return playerRepository!!.findById(id!!).get()
    }

    @RequestMapping(value = "", method = arrayOf(RequestMethod.POST))
    fun createPlayer(@RequestBody player: Player?): Player {
        if (player != null) {
            if (player.id != null) {
                throw IllegalArgumentException("'id' must not be provided")
            }

            if (player.nickName == null) {
                throw IllegalArgumentException("'nickName' must be provided")
            }

            // TODO players are created on the fly atm; TODO auth layer
            player.password = BCryptPasswordEncoder().encode("kicker")

            return playerRepository!!.save<Player?>(player)
        }
        throw IllegalArgumentException("no player object provided")
    }

    @RequestMapping("/me")
    fun user(user: Principal?): Player {
        if (user != null) {
            val opt = playerRepository!!.findByName(user.name)
            if (opt.isPresent) {
                return opt.get()
            }
        }

        throw IllegalStateException("Invalid user")
    }
}
