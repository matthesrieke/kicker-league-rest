package org.n52.spare.kicker.resources

import java.util.Optional

import org.n52.spare.kicker.model.Rank
import org.n52.spare.kicker.repositories.RankRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import com.fasterxml.jackson.annotation.JsonView
import org.n52.spare.kicker.model.PageableResponse
import org.n52.spare.kicker.model.Views

@RestController
@RequestMapping("/rankings")
class RankingsController {

    @Autowired
    private val rankRepository: RankRepository? = null


    @JsonView(Views.Basic::class)
    @RequestMapping("")
    fun collection(@RequestParam(required = false) page: Optional<Int>,
                   @RequestParam(required = false) size: Optional<Int>): PageableResponse<Rank> {
        val pageable = rankRepository!!.findAll(PageRequest.of(page.orElse(0), size.orElse(10)))
        return PageableResponse.from(pageable)
    }

}
