package org.n52.spare.kicker.rankings

import org.n52.spare.kicker.model.Match
import org.n52.spare.kicker.model.Player
import org.n52.spare.kicker.model.Rank

class MatchpointsAlgorithmImpl : RankingsAlgorithm {
	
	private val ranks: MutableMap<Player, Rank> = mutableMapOf()
	
	override fun calculateForMatches(matches: List<Match>): List<Rank> {
		if (matches.isEmpty()) return mutableListOf<Rank>()
		
		matches.forEach{m ->
			if (m.score == null) return@forEach
			
			val guest = m.guest!!
			val home = m.home!!

			plusMatchForTeam(guest)
			plusMatchForTeam(home)

			when {
				m.score!!.guest == m.score!!.home -> {
					plusPointsForTeam(guest, 1)
					plusPointsForTeam(home, 1)
				}
				m.score!!.guest > m.score!!.home -> {
					plusPointsForTeam(guest, 3)
				}
				else -> {
					plusPointsForTeam(home, 3)
				}
			}
		}
		
		var result: List<Rank> = ArrayList(ranks.values)
		result = result.sortedWith(compareByDescending<Rank>{it.points}.thenBy{it.totalMatches})

		var currentPoints = result[0].points
		var currentMatches = result[0].totalMatches
		var currentRank = 1
		var actualRank = 1

		result.forEach{t ->
			if (t.points == currentPoints && t.totalMatches == currentMatches) {
				t.rank = currentRank
			} else {
				t.rank = actualRank
				currentRank = actualRank
			}
			actualRank++
		}

		return result
	}
	
	private fun plusMatchForTeam(players: List<Player>) {
		players.forEach{p -> plusMatch(p)}
	}

	private fun plusMatch(player: Player) {
		val r: Rank
		if (ranks.containsKey(player)) {
			r = ranks[player]!!
		}
		else {
			r = Rank()
			r.player = player
			ranks[player] = r
		}

		r.totalMatches += 1
	}

	private fun plusPointsForTeam(players: List<Player>, points: Int) {
		players.forEach{p -> plusPoints(p, points)}
	}

	private fun plusPoints(player: Player, points: Int) {
		if (ranks.containsKey(player)) {
			val r = ranks[player]!!
			r.points += points
		}
	}
	
}

