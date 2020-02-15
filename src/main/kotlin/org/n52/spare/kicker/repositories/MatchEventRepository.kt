package org.n52.spare.kicker.repositories

import org.n52.spare.kicker.model.MatchEvent
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.stereotype.Repository

@Repository
interface MatchEventRepository : PagingAndSortingRepository<MatchEvent, Long>
