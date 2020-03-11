package org.n52.spare.kicker.rankings

import org.n52.spare.kicker.model.Match
import org.n52.spare.kicker.model.Player
import org.n52.spare.kicker.model.Rank
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.stream.Collectors
import kotlin.math.pow

class EloAlgorithmImpl : RankingsAlgorithm {

    private val log: Logger = LoggerFactory.getLogger(EloAlgorithmImpl::class.java)

    val INITIAL_ELO = 1000

    class TeamResult {
        public var score: Double = 0.0
        public var elo: Double = 0.0
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
        sortedMatches.forEach { m ->
            val winners: List<Player> = getWinnerLoser(m, true)
            val losers: List<Player> = getWinnerLoser(m, false)

            val winnerTeam = prepareTeamsWithWinnerFlag(winners, true)
            val loserTeam = prepareTeamsWithWinnerFlag(losers, false)

            log.info("Match: {}", m)
            log.info("Elo before: winner: {}, loser: {}", winnerTeam.elo, loserTeam.elo)

            val eloChange = calculateEloChange(winnerTeam.elo, loserTeam.elo)
            log.info("Elo change winner: {}", eloChange);
            log.info("Elo change loser: {}", eloChange  * -1);

            // apply same Elo change for all players of a match --> no "contribution" or weight applied
            // set Elo for the player objects
            allPlayers.stream().filter{p -> losers.contains(p)}.forEach{p ->
                p.rating = if (p.rating != null) p.rating - eloChange else INITIAL_ELO - eloChange
                matchCounts[p] = matchCounts[p]!! + 1
            }
            allPlayers.stream().filter{p -> winners.contains(p)}.forEach{p ->
                p.rating = if (p.rating != null) p.rating + eloChange else INITIAL_ELO + eloChange
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

    private fun prepareTeamsWithWinnerFlag(players: List<Player>, isWinner: Boolean): TeamResult {
        val teamResult = TeamResult()
        teamResult.elo = players.stream().map {p ->
            if (p.rating == null) INITIAL_ELO else p.rating
        }.mapToInt { i -> i }.average().asDouble

        teamResult.winner = isWinner
        teamResult.score = if (isWinner) 1.0 else 0.0
        return teamResult
    }

    private fun getWinnerLoser(m: Match, selectWinner: Boolean): List<Player> {
        return if (m.score.guest > m.score.home) {
            if (selectWinner) m.guest else m.home
        } else {
            if (selectWinner) m.home else m.guest
        }
    }

    private fun calculateExpectedScore(targetElo: Double, otherElo: Double): Double {
        val transformedTargetElo: Double = 10.0.pow(targetElo / 400.0)
        val transformedOtherElo: Double = 10.0.pow(otherElo / 400.0)
        return (transformedTargetElo / (transformedTargetElo + transformedOtherElo))
    }

    private fun calculateEloChange(winnerElo: Double, loserElo: Double, kFactor: Int = 32): Int {
        val score = 1.0
        val expectedScore = calculateExpectedScore(winnerElo, loserElo)
        return (kFactor * (score - expectedScore)).toInt()
    }

}