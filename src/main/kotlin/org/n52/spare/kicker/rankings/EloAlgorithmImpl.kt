package org.n52.spare.kicker.rankings

import org.n52.spare.kicker.model.Match
import org.n52.spare.kicker.model.Player
import org.n52.spare.kicker.model.Rank
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.stream.Collectors
import kotlin.math.pow
import kotlin.math.roundToInt

class EloAlgorithmImpl : RankingsAlgorithm {

    private val log: Logger = LoggerFactory.getLogger(EloAlgorithmImpl::class.java)

    val INITIAL_ELO = 1000

    class LocalPlayerScore {
        public lateinit var player: Player
        public var score: Double = 0.0
        public var elo: Int = 0
        public var winner: Boolean = false;
    }

    override fun calculateForMatches(matches: List<Match>): List<Rank> {
        var allPlayers: MutableList<Player> = ArrayList()
        matches.stream().forEach { m -> m.home.forEach{ x -> allPlayers.add(x)}; m.guest.forEach{ x -> allPlayers.add(x)} }
        allPlayers = allPlayers.stream().distinct().collect(Collectors.toList())

        val matchCounts: MutableMap<Player, Int> = HashMap()

        var playerRanks: MutableList<Rank> = allPlayers.stream().distinct().map{p ->
            matchCounts[p] = 0
            val r = Rank()
            r.player = p
            r
        }.collect(Collectors.toList())

        val sortedMatches = matches.sortedBy { m -> m.dateTime }

        // calculate the Elo ratings, one match by one
        // current limitation: only 1 vs 1 supported
        sortedMatches.filter{ m -> m.home.size == 1 && m.guest.size == 1}.forEach { m ->
            val winners: List<Player> = getWinnerLoser(m, true)
            val losers: List<Player> = getWinnerLoser(m, false)

            val winner = prepareTeamsWithWinnerFlag(winners, true)
            val loser = prepareTeamsWithWinnerFlag(losers, false)

            log.info("Match: {}", m)
            log.info("Elo before: {}: {}, {}: {}", winner.player.nickName, winner.elo, loser.player.nickName, loser.elo)

            val winnerChange = calculateEloChange(winner, loser)
            log.info("Elo change winner: {}", winnerChange);

            val loserChange = calculateEloChange(loser, winner)
            log.info("Elo change loser: {}", loserChange);

            winner.elo += winnerChange
            loser.elo += loserChange

            log.info("Elo after: {}: {}, {}: {}", winner.player.nickName, winner.elo, loser.player.nickName, loser.elo)

            // set Elo for the player objects
            allPlayers.stream().filter{p -> p == loser.player}.forEach{p ->
                p.rating = loser.elo
                matchCounts[p] = matchCounts[p]!! + 1
            }
            allPlayers.stream().filter{p -> p == winner.player}.forEach{p ->
                p.rating = winner.elo
                matchCounts[p] = matchCounts[p]!! + 1
            }
        }

        // sort by rating
        playerRanks.sortByDescending { r -> r.player.rating }

        // apply rank, total matches and rating to the players
        var actualRank = 1
        playerRanks.forEach{t ->
            t.rank = actualRank++
            t.points = t.player.rating
            t.totalMatches = matchCounts[t.player] ?: 0
        }

        return playerRanks
    }

    private fun prepareTeamsWithWinnerFlag(players: List<Player>, isWinner: Boolean): LocalPlayerScore {
        return players.stream().map { p ->
            val lps = LocalPlayerScore()
            lps.player = p
            if (p.rating == null || p.rating == 0) {
                lps.elo = INITIAL_ELO
            } else {
                lps.elo = p.rating
            }
            lps.winner = isWinner
            lps
        }.findFirst().get()
    }

    private fun getWinnerLoser(m: Match, selectWinner: Boolean): List<Player> {
        return if (m.score.guest > m.score.home) {
            if (selectWinner) m.guest else m.home
        } else {
            if (selectWinner) m.home else m.guest
        }
    }

    private fun calculateScore(isTargetPlayerWinner: Boolean, isOtherPlayerWinner: Boolean): Double {
        if (isTargetPlayerWinner && isOtherPlayerWinner) {
            // consider both true as a draw
            return 0.5;
        }
        return if (isTargetPlayerWinner) {
            1.0;
        } else {
            0.0;
        }
    }

    private fun calculateExpectedScore(targetPlayerElo: Int, otherPlayerElo: Int): Double {
        val transformedTargetElo: Double = 10.0.pow(targetPlayerElo / 400.0)
        val transformedOtherElo: Double = 10.0.pow(otherPlayerElo / 400.0)
        return (transformedTargetElo / (transformedTargetElo + transformedOtherElo))
    }

    private fun calculateEloChange(targetPlayer: LocalPlayerScore, otherPlayer: LocalPlayerScore, kFactor: Int = 32): Int {
        val score = calculateScore(targetPlayer.winner, otherPlayer.winner)
        val expectedScore = calculateExpectedScore(targetPlayer.elo, otherPlayer.elo)
        return (kFactor * (score - expectedScore)).toInt()
    }

    private fun calculateElo(players: List<LocalPlayerScore>): List<LocalPlayerScore> {
        throw NotImplementedError("not yet implemented")
    }

}