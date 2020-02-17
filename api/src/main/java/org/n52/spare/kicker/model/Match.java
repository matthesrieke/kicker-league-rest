package org.n52.spare.kicker.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import org.n52.spare.kicker.model.Views.Basic;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "matches")
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class Match {

    public static enum MatchType {
        oneVersusOne,
        twoVersusTwo
    }

    @JsonView({ Basic.class })
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @JsonView({ Basic.class })
    @Column(nullable = false)
    private Date dateTime;

    @JsonView({ Basic.class })
    @ManyToMany()
    private List<Player> home;

    @JsonView({ Basic.class })
    @ManyToMany()
    private List<Player> guest;

    @JsonView({ Basic.class })
    @OneToMany(cascade = { CascadeType.ALL })
    private List<MatchEvent> events;

    @JsonView({ Basic.class })
    @OneToOne(cascade = { CascadeType.ALL })
    private Score score;

    @JsonView(Basic.class)
    @Column(nullable = true)
    private Boolean homeApproved = false;

    @JsonView(Basic.class)
    @Column(nullable = true)
    private Boolean guestApproved = false;

    @JsonView({ Views.Details.class })
    @Column(nullable = true)
    private String comment;

    @JsonView(Basic.class)
    @Column(nullable = true)
    private String type;

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

    public final List<Player> getHome() {
        return this.home;
    }

    public final void setHome(List<Player> var1) {
        this.home = var1;
    }

    public final List<Player> getGuest() {
        return this.guest;
    }

    public final void setGuest(List<Player> var1) {
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

    public Boolean getHomeApproved() {
        return homeApproved;
    }

    public void setHomeApproved(Boolean homeApproved) {
        this.homeApproved = homeApproved;
    }

    public Boolean getGuestApproved() {
        return guestApproved;
    }

    public void setGuestApproved(Boolean guestApproved) {
        this.guestApproved = guestApproved;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}
