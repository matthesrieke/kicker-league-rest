package org.n52.spare.kicker.resources

import com.fasterxml.jackson.annotation.JsonView
import org.n52.spare.kicker.events.MatchSubmittedEvent
import org.n52.spare.kicker.model.*
import org.n52.spare.kicker.repositories.MatchRepository
import org.n52.spare.kicker.repositories.PlayerRepository
import org.n52.spare.kicker.security.RepositoryUserDetailsManager
import org.n52.spare.kicker.security.WebSecurityConfig
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.*
import java.util.*
import kotlin.collections.ArrayList


@RestController
@RequestMapping("/matches")
class MatchesController : InitializingBean {

    @Autowired
    private val matchRepository: MatchRepository? = null

    @Autowired
    private val playerRepository: PlayerRepository? = null

    @Autowired
    private val security: WebSecurityConfig? = null

    @Autowired
    private val applicationEventPublisher: ApplicationEventPublisher? = null

    @JsonView(Views.Basic::class)
    @RequestMapping("")
    fun collection(@RequestParam(required = false) page: Optional<Int>,
                   @RequestParam(required = false) size: Optional<Int>): PageableResponse<Match> {
        val pageable = matchRepository!!.findAll(PageRequest.of(page.orElse(0), size.orElse(10),
                Sort.by(Sort.Direction.DESC, "dateTime")))
        return PageableResponse.from(pageable)
    }

    @JsonView(Views.Details::class)
    @RequestMapping("", params = arrayOf("expanded=true"))
    fun collectionWithDetails(@RequestParam(required = false) page: Optional<Int>,
                              @RequestParam(required = false) size: Optional<Int>): PageableResponse<Match> {
        return collection(page, size)
    }

    @JsonView(Views.Details::class)
    @RequestMapping("/{id}", method = [RequestMethod.GET])
    fun single(@PathVariable id: Long): Match {
        return matchRepository!!.findById(id).get()
    }

    @RequestMapping("/{id}", method = [RequestMethod.DELETE])
    fun delete(@PathVariable id: Long): Map<String, Boolean> {
        matchRepository!!.deleteById(id);
        return Collections.singletonMap("deleted", true);
    }

    @JsonView(Views.Details::class)
    @RequestMapping("/{id}/approve", method = [RequestMethod.PUT])
    fun approve(@PathVariable id: Long, auth: Authentication): Match {
        var match = matchRepository!!.findById(id).get()

        var requestingPlayer = (security!!.userDetailsService() as RepositoryUserDetailsManager).playerFromAuthentication(auth)

        when {
            !match.guest.none { p -> p.id == requestingPlayer.id } -> match.guestApproved = true
            !match.home.none { p -> p.id == requestingPlayer.id } -> match.homeApproved = true
            else -> throw IllegalArgumentException("Not allowed to approve match")
        }

        matchRepository!!.save(match)

        return match
    }

    @JsonView(Views.Basic::class)
    @RequestMapping("", method = arrayOf(RequestMethod.POST))
    fun createMatch(@RequestBody match: Match?): Match {
        if (match != null) {
            if (match.id != null) {
                throw IllegalArgumentException("'id' must not be provided")
            }

            if (match.dateTime == null) {
                match.dateTime = Date()
            }

            this.resolvePlayers(match);

            if (match.home.size > 1) {
                match.type = Match.MatchType.twoVersusTwo.name
            } else {
                match.type = Match.MatchType.oneVersusOne.name
            }

            val result = matchRepository!!.save<Match?>(match)
            applicationEventPublisher!!.publishEvent(MatchSubmittedEvent(match))
            return result
        }
        throw IllegalArgumentException("no match object found")
    }

    fun resolvePlayers(match: Match): Match {
        val guests = match.guest.map { p ->  playerRepository!!.findByName(p.nickName)}.filter{p -> p.isPresent}.map { p -> p.get() }
        val homes = match.home.map { p ->  playerRepository!!.findByName(p.nickName)}.filter{p -> p.isPresent}.map { p -> p.get() }

        if (guests.size != match.guest.size) {
            throw IllegalArgumentException("A Guest player was not found")
        }

        if (homes.size != match.home.size) {
            throw IllegalArgumentException("A Home player was not found")
        }


        match.guest = guests
        match.home = homes

        return match;
    }

    @Throws(Exception::class)
    override fun afterPropertiesSet() {
//        insertDummyData()
    }

    fun insertDummyData() {
//        matchRepository!!.deleteAll()
//        val p3 = Player();
//        p3.nickName = "Peter";
//        p3.password = "\$2a\$10\$sLqDPwTQ9UQT4zEzRyPIJeuQqgjRoJm6OXlYy6akFwQLCAqLHOzqq";
//
//        val p4 = Player();
//        p4.nickName = "Klaus";
//        p4.password = "\$2a\$10\$sLqDPwTQ9UQT4zEzRyPIJeuQqgjRoJm6OXlYy6akFwQLCAqLHOzqq";
//
//        playerRepository!!.save(p3);
//        playerRepository!!.save(p4);
//

        val p1 = playerRepository!!.findByName("Mathijsen").get()
        val p2 = playerRepository!!.findByName("Staschinho").get()
        val p3 = playerRepository!!.findByName("Peter").get()
        val p4 = playerRepository!!.findByName("Klaus").get()

        val m = Match();
        m.dateTime = Date();
        m.home = mutableListOf(p1, p2)
        m.guest = mutableListOf(p3, p4)
        m.guestApproved = true;
        val s = Score();
        s.guest = 2;
        s.home = 6;
        m.score = s;
        m.homeApproved = false;

        val events = ArrayList<MatchEvent>();
        val e1 = MatchEvent();
        e1.dateTime = Date();
        e1.guestScore = 1;
        events.add(e1);
        e1.match = m;
        val e2 = MatchEvent();
        e2.dateTime = Date(e1.dateTime!!.time + 10000);
        e2.fulltime = true;
        e2.match = m;
        events.add(e2);

        m.events = events;

        matchRepository!!.save(m);
    }

}
