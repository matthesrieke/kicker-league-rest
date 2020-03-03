package org.n52.spare.kicker.events

import com.fasterxml.jackson.databind.ObjectMapper
import org.n52.spare.kicker.model.Match
import org.springframework.context.ApplicationEvent


class MatchSubmittedEvent(private val match: Match) : KickerEvent, ApplicationEvent(match) {

    override fun toJson(): String {
        val mapper = ObjectMapper()
        return mapper.writeValueAsString(match)
    }

    override fun toHumanReadableMessage(): String {
        return String.format("New Match played: %s vs. %s - %s : %s",
                match.home.joinToString(", ") { p -> p.nickName },
                match.guest.joinToString(", ") { p -> p.nickName },
                match.score.home,
                match.score.guest)
    }
}