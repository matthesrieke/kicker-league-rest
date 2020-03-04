package org.n52.spare.kicker.events

import com.fasterxml.jackson.databind.ObjectMapper
import org.n52.spare.kicker.model.Match
import org.n52.spare.kicker.model.Rank
import org.springframework.context.ApplicationEvent
import java.lang.StringBuilder
import kotlin.math.min

class RankingUpdatedEvent(private val rankingUpdate: RankingUpdate) : KickerEvent, ApplicationEvent(rankingUpdate) {


    class RankingUpdate(public val rankingBefore: List<Rank>, public val rankingAfter: List<Rank>) {

    }

    override fun toJson(): String {
        val om = ObjectMapper()
        val asMap = HashMap<String, List<Rank>>()
        asMap["before"] = rankingUpdate.rankingBefore
        asMap["after"] = rankingUpdate.rankingAfter
        return om.writeValueAsString(asMap)
    }

    override fun toHumanReadableMessage(): String {
        var sb = StringBuilder()
        for (i in 0..min(2, rankingUpdate.rankingAfter.size - 1)) {
            val rank = rankingUpdate.rankingAfter[i]
            var rankBefore = rankingUpdate.rankingBefore.find { r -> r.player.nickName == rank.player.nickName }

            var deltaString: String
            deltaString = if (rankBefore != null) {
                var posBefore = rankingUpdate.rankingBefore.indexOf(rankBefore)
                if (posBefore < i) {
                    "-" + (i - posBefore)
                } else {
                    "+" + (posBefore - i)
                }
            } else {
                "new"
            }

            sb.append(i+1).append(".: ").append(rank.player.nickName).append(" (").append(deltaString).append("), ")
        }
        sb.removeSuffix(", ")
        return sb.toString()
    }
}