package org.n52.spare.kicker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.context.config.annotation.RefreshScope
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@SpringBootApplication
@RefreshScope
@EnableDiscoveryClient
class KickerLeagueRestApplication

fun main(args: Array<String>) {
    runApplication<KickerLeagueRestApplication>(*args);
}
