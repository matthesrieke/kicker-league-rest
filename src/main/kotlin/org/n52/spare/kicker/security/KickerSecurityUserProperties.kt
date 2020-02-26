package org.n52.spare.kicker.security

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component
import java.util.*

@Component
@ConfigurationProperties("kicker.security")
class KickerSecurityUserProperties {

    var users: List<User> = ArrayList()

    class User {

        var name: String? = null
        var password: String? = null


    }

}