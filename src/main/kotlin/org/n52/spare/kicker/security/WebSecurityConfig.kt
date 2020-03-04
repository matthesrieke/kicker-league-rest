package org.n52.spare.kicker.security

import org.n52.spare.kicker.repositories.PlayerRepository
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.access.channel.ChannelProcessingFilter

@Configuration
@ConfigurationProperties(prefix = "kicker-security")
@EnableWebSecurity
class WebSecurityConfig : WebSecurityConfigurerAdapter(), InitializingBean {

    private lateinit var userManager: RepositoryUserDetailsManager

    @Autowired
    private val kickerSecurity: KickerSecurityUserProperties ? = null

    @Autowired
    private val playerRepo: PlayerRepository? = null

    @Autowired
    private val passwordEncoder: PasswordEncoder? = null

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/players").authenticated()
                .antMatchers(HttpMethod.GET, "/players/me").authenticated()
                .antMatchers(HttpMethod.POST, "/matches/**").authenticated()
                .antMatchers(HttpMethod.POST, "/matches").authenticated()
                .antMatchers(HttpMethod.GET, "/**").permitAll()
                .and()
                .addFilterBefore(WebMvcCorsFilter(), ChannelProcessingFilter::class.java)
                .httpBasic();
    }

    override fun configure(auth: AuthenticationManagerBuilder?) {
        kickerSecurity!!.users.forEach { u ->
            auth!!.inMemoryAuthentication()
                    .withUser(u.name)
                    .password(u.password)
                    .roles("USER")
        }
    }

    @Bean
    public override fun userDetailsService(): UserDetailsService {
        return RepositoryUserDetailsManager(playerRepo!!, passwordEncoder!!)
    }

    @Bean(name = arrayOf("passwordEncoder"))
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Throws(Exception::class)
    override fun afterPropertiesSet() {
        insertDummyData()
    }

    fun insertDummyData() {
        //		Player p1 = new Player();
        //		p1.setNickName("Mathijsen");
        //		p1.setFirstName("Matthes");
        //		p1.setLastName("Rieke");
        //		p1.setPassword(passwordEncoder.encode("asdf"));
        //
        //		Player p2 = new Player();
        //		p2.setNickName("Staschinho");
        //		p2.setFirstName("Christoph");
        //		p2.setLastName("Stasch");
        //		p2.setPassword(passwordEncoder.encode("asdf2"));
        //
        //		playerRepo.save(p1);
        //		playerRepo.save(p2);
    }

    class AdminConfig {

        private lateinit var user: String
        private lateinit var password: String

    }

}
