package org.n52.spare.kicker.rankings

import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert.assertThat
import org.junit.jupiter.api.Test

import org.n52.spare.kicker.model.Match
import org.n52.spare.kicker.model.Player
import org.n52.spare.kicker.model.Score
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

class EloAlgorithmImplTest {

    private val log: Logger = LoggerFactory.getLogger(EloAlgorithmImplTest::class.java)

    @Test
    fun testRankings() {
        var ma = EloAlgorithmImpl()
        var ranks = ma.calculateForMatches(this.dummyMatchesA())
        log.info("Ranks: {}", ranks);

        assertThat(ranks.filter{r -> r.player.id == 1L}[0].rank, CoreMatchers.`is`(2))
        assertThat(ranks.filter{r -> r.player.id == 2L}[0].rank, CoreMatchers.`is`(1))
        assertThat(ranks.filter{r -> r.player.id == 1L}[0].points, CoreMatchers.`is`(984))
        assertThat(ranks.filter{r -> r.player.id == 2L}[0].points, CoreMatchers.`is`(1016))
        assertThat(ranks.filter{r -> r.player.id == 1L}[0].totalMatches, CoreMatchers.`is`(1))
        assertThat(ranks.filter{r -> r.player.id == 2L}[0].totalMatches, CoreMatchers.`is`(1))

        ma = EloAlgorithmImpl()
        ranks = ma.calculateForMatches(this.dummyMatchesB())
        log.info("Ranks: {}", ranks);

        assertThat(ranks.filter{r -> r.player.id == 1L}[0].rank, CoreMatchers.`is`(3))
        assertThat(ranks.filter{r -> r.player.id == 2L}[0].rank, CoreMatchers.`is`(1))
        assertThat(ranks.filter{r -> r.player.id == 3L}[0].rank, CoreMatchers.`is`(2))
        assertThat(ranks.filter{r -> r.player.id == 1L}[0].totalMatches, CoreMatchers.`is`(2))
        assertThat(ranks.filter{r -> r.player.id == 2L}[0].totalMatches, CoreMatchers.`is`(2))
        assertThat(ranks.filter{r -> r.player.id == 3L}[0].totalMatches, CoreMatchers.`is`(2))

        ma = EloAlgorithmImpl()
        ranks = ma.calculateForMatches(this.dummyMatchesC())
        log.info("Ranks: {}", ranks);

        assertThat(ranks.filter{r -> r.player.id == 1L}[0].rank, CoreMatchers.`is`(1))
        assertThat(ranks.filter{r -> r.player.id == 2L}[0].rank, CoreMatchers.`is`(2))
        assertThat(ranks.filter{r -> r.player.id == 1L}[0].totalMatches, CoreMatchers.`is`(2))
        assertThat(ranks.filter{r -> r.player.id == 2L}[0].totalMatches, CoreMatchers.`is`(2))
    }

    private fun dummyMatchesA(): List<Match> {
        val result = mutableListOf<Match>()
        val p1 = Player()
        p1.id = 1L
        p1.nickName = "wurst"
        p1.lastName = "wat"

        val p2 = Player()
        p2.id = 2L
        p2.nickName = "hurz"
        p2.lastName = "jo"

        val s = Score()
        s.guest = 6
        s.home = 4

        val m = Match()
        m.home = Collections.singletonList(p1)
        m.guest = Collections.singletonList(p2)
        m.score = s

        result.add(m)

        return result
    }

    private fun dummyMatchesB(): List<Match> {
        val result = mutableListOf<Match>()
        val p1 = Player()
        p1.id = 1L
        p1.nickName = "wurst"
        p1.lastName = "wat"

        val p2 = Player()
        p2.id = 2L
        p2.nickName = "hurz"
        p2.lastName = "jo"

        val p3 = Player()
        p3.id = 3L
        p3.nickName = "1337"
        p3.lastName = "0wn"

        val s1 = Score()
        s1.home = 4
        s1.guest = 6

        val m1 = Match()
        m1.home = Collections.singletonList(p1)
        m1.guest = Collections.singletonList(p2)
        m1.score = s1

        result.add(m1)

        val s2 = Score()
        s2.home = 0
        s2.guest = 6

        val m2 = Match()
        m2.home = Collections.singletonList(p1)
        m2.guest = Collections.singletonList(p3)
        m2.score = s2

        result.add(m2)

        val s3 = Score()
        s3.home = 6
        s3.guest = 1

        val m3 = Match()
        m3.home = Collections.singletonList(p2)
        m3.guest = Collections.singletonList(p3)
        m3.score = s3

        result.add(m3)

        return result
    }

    private fun dummyMatchesC(): List<Match> {
        val result = mutableListOf<Match>()
        val p1 = Player()
        p1.id = 1L
        p1.nickName = "wurst"
        p1.lastName = "wat"

        val p2 = Player()
        p2.id = 2L
        p2.nickName = "hurz"
        p2.lastName = "jo"

        val s1 = Score()
        s1.home = 4
        s1.guest = 6

        val m1 = Match()
        m1.home = Collections.singletonList(p1)
        m1.guest = Collections.singletonList(p2)
        m1.score = s1

        result.add(m1)

        val s2 = Score()
        s2.home = 6
        s2.guest = 4

        val m2 = Match()
        m2.home = Collections.singletonList(p1)
        m2.guest = Collections.singletonList(p2)
        m2.score = s2

        result.add(m2)

        return result
    }
}