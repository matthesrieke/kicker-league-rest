package org.n52.spare.kicker.events

import org.springframework.context.ApplicationEvent

interface KickerEvent {

    fun toJson(): String

    fun toHumanReadableMessage(): String

}