package org.n52.spare.kicker.slack

import org.n52.spare.kicker.events.MatchSubmittedEvent
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import java.util.*


@Component
class SlackBotPublisher : InitializingBean {

    private val log: Logger = LoggerFactory.getLogger(SlackBotPublisher::class.java)

    @Value("\${kicker.slack.postUrl}")
    lateinit var slackPostUrl: String

    @Value("\${kicker.publicUiUrl}")
    lateinit var publicUiUrl: String


    @Autowired
    lateinit var rest: RestTemplate

    override fun afterPropertiesSet() {
        log.info("SlackBotPublisher initialized: {}", slackPostUrl)
    }

    @Bean
    fun restTemplate(builder: RestTemplateBuilder): RestTemplate? { // Do any additional configuration here
        return builder.build()
    }

    @Async
    @EventListener
    fun handleContextStart(event: MatchSubmittedEvent) {
        log.info("Received spring custom event - " + event.toHumanReadableMessage())
        val message = event.toHumanReadableMessage() + " - check out at: " + publicUiUrl + "matches"
        rest.postForLocation(slackPostUrl, Collections.singletonMap("text", message))
    }

}