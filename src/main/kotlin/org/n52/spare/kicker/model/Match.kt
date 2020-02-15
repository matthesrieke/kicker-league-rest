package org.n52.spare.kicker.model

import java.util.Date

import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.OneToOne
import javax.persistence.Table

import com.fasterxml.jackson.annotation.JsonView

@Entity
@Table(name = "matches")
class Match {

    @JsonView(Views.Basic::class)
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long? = null

    @JsonView(Views.Basic::class)
    @Column(nullable = false)
    var dateTime: Date? = null

    @JsonView(Views.Basic::class)
    @ManyToOne(optional = false)
    var home: Player? = null

    @JsonView(Views.Basic::class)
    @ManyToOne(optional = false)
    var guest: Player? = null

    @JsonView(Views.Basic::class)
    @OneToMany(cascade = arrayOf(CascadeType.ALL))
    var events: List<MatchEvent>? = null

    @JsonView(Views.Basic::class)
    @OneToOne(cascade = arrayOf(CascadeType.ALL), optional = false)
    var score: Score? = null

    @JsonView(Views.Basic::class)
    @Column(nullable = false)
    var homeApproved: Boolean? = false

    @JsonView(Views.Basic::class)
    @Column(nullable = false)
    var guestApproved: Boolean? = false
}
