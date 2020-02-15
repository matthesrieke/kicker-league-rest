package org.n52.spare.kicker.security

import org.n52.spare.kicker.repositories.PlayerRepository
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.access.channel.ChannelProcessingFilter
import org.springframework.security.web.csrf.CookieCsrfTokenRepository

@Configuration
@EnableWebSecurity
class WebSecurityConfig : WebSecurityConfigurerAdapter(), InitializingBean {

    @Autowired
    private val playerRepo: PlayerRepository? = null

    @Autowired
    private val passwordEncoder: PasswordEncoder? = null

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
                .httpBasic().and()
                .authorizeRequests()
                .antMatchers("/**").permitAll()
                .and()
                .addFilterBefore(WebMvcCorsFilter(), ChannelProcessingFilter::class.java)
                .csrf().disable();
    }

    @Bean
    public override fun userDetailsService(): UserDetailsService {
        return this.userManager!!
    }

    @Bean(name = arrayOf("passwordEncoder"))
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Throws(Exception::class)
    override fun afterPropertiesSet() {
        this.userManager = RepositoryUserDetailsManager(playerRepo!!, passwordEncoder!!)
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

}
