package org.n52.spare.kicker.repositories

import org.n52.spare.kicker.model.Rank
import org.n52.spare.kicker.rankings.MatchpointsAlgorithmImpl
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component

@Component
class RankRepository {

    @Autowired
    private val matchRepository: MatchRepository? = null

    fun findAll(page: PageRequest?): Page<Rank> {
        val ranks = MatchpointsAlgorithmImpl().calculateForMatches(matchRepository!!.findAll().toList())

        return if (page != null) {
            val windowed = ranks.drop(page.offset.toInt()).take(page.pageSize)
            PageImpl(windowed,
                    PageRequest.of((page.offset / page.pageSize).toInt(), page.pageSize),
                    ranks.size.toLong())
        } else {
            PageImpl(ranks)
        }
    }

}
