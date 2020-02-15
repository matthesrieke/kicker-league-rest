package org.n52.spare.kicker.model;

import com.fasterxml.jackson.annotation.JsonView;
import org.n52.spare.kicker.model.Views.Basic;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Entity
@Table(
        name = "matches"
)
public final class Match {
    @JsonView({Basic.class})
    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    private Long id;

    @JsonView({Basic.class})
    @Column(
            nullable = false
    )
    private Date dateTime;

    @JsonView({Basic.class})
    @ManyToOne(
            optional = false
    )
    private Player home;

    @JsonView({Basic.class})
    @ManyToOne(
            optional = false
    )
    private Player guest;

    @JsonView({Basic.class})
    @OneToMany(
            cascade = {CascadeType.ALL}
    )
    private List<MatchEvent> events;

    @JsonView({Basic.class})
    @OneToOne(
            cascade = {CascadeType.ALL}
    )
    private Score score;

    @JsonView({Views.Details.class})
    @Column(
            nullable = true
    )
    private String comment;


    public final Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public final Date getDateTime() {
        return this.dateTime;
    }

    public final void setDateTime(Date var1) {
        this.dateTime = var1;
    }


    public final Player getHome() {
        return this.home;
    }

    public final void setHome(Player var1) {
        this.home = var1;
    }


    public final Player getGuest() {
        return this.guest;
    }

    public final void setGuest(Player var1) {
        this.guest = var1;
    }


    public final List<MatchEvent> getEvents() {
        return this.events;
    }

    public final void setEvents(List<MatchEvent> var1) {
        this.events = var1;
    }


    public final Score getScore() {
        return this.score;
    }

    public final void setScore(Score var1) {
        this.score = var1;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
