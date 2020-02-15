package org.n52.spare.kicker.repositories

import org.n52.spare.kicker.model.Player
import java.util.Optional

import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface PlayerRepository : PagingAndSortingRepository<Player, Long> {

    @Query("select p from Player p where lower(nickName) = lower(:name)")
    fun findByName(@Param("name") name: String): Optional<Player>

}
