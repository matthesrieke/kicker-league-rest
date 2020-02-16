package org.n52.spare.kicker.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import org.n52.spare.kicker.model.Views.Basic;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "match_events")
@JsonInclude(JsonInclude.Include.NON_NULL)
public final class MatchEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private Match match;

    @JsonView({ Basic.class })
    @Column(nullable = false)
    private Date dateTime;

    @JsonView({ Basic.class })
    private Integer homeScore;

    @JsonView({ Basic.class })
    private Integer guestScore;

    @JsonView({ Basic.class })
    private Boolean halftime;

    @JsonView({ Basic.class })
    private Boolean fulltime;

    @JsonView({ Basic.class })
    private Boolean overtime;

    @JsonView({ Basic.class })
    private Boolean finished;

    @JsonView({ Basic.class })
    private Boolean pause;

    @JsonView({ Basic.class })
    private Boolean resume;

    @JsonView({ Basic.class })
    private Boolean cancel;

    public final Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public final Match getMatch() {
        return this.match;
    }

    public final void setMatch(Match var1) {
        this.match = var1;
    }

    public final Date getDateTime() {
        return this.dateTime;
    }

    public final void setDateTime(Date var1) {
        this.dateTime = var1;
    }

    public final Integer getHomeScore() {
        return this.homeScore;
    }

    public final void setHomeScore(Integer var1) {
        this.homeScore = var1;
    }

    public final Integer getGuestScore() {
        return this.guestScore;
    }

    public final void setGuestScore(Integer var1) {
        this.guestScore = var1;
    }

    public final Boolean getHalftime() {
        return this.halftime;
    }

    public final void setHalftime(Boolean var1) {
        this.halftime = var1;
    }

    public final Boolean getFulltime() {
        return this.fulltime;
    }

    public final void setFulltime(Boolean var1) {
        this.fulltime = var1;
    }

    public final Boolean getOvertime() {
        return this.overtime;
    }

    public final void setOvertime(Boolean var1) {
        this.overtime = var1;
    }

    public final Boolean getFinished() {
        return this.finished;
    }

    public final void setFinished(Boolean var1) {
        this.finished = var1;
    }

    public final Boolean getPause() {
        return this.pause;
    }

    public final void setPause(Boolean var1) {
        this.pause = var1;
    }

    public final Boolean getResume() {
        return this.resume;
    }

    public final void setResume(Boolean var1) {
        this.resume = var1;
    }

    public final Boolean getCancel() {
        return this.cancel;
    }

    public final void setCancel(Boolean var1) {
        this.cancel = var1;
    }
}
