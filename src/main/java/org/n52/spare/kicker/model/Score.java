package org.n52.spare.kicker.model;

import com.fasterxml.jackson.annotation.JsonView;
import org.n52.spare.kicker.model.Views.Basic;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(
        name = "scores"
)
public final class Score {
    @Id
    @GeneratedValue(
            strategy = GenerationType.AUTO
    )
    private Long id;
    @JsonView({Basic.class})
    private int home;
    @JsonView({Basic.class})
    private int guest;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public final int getHome() {
        return this.home;
    }

    public final void setHome(int var1) {
        this.home = var1;
    }

    public final int getGuest() {
        return this.guest;
    }

    public final void setGuest(int var1) {
        this.guest = var1;
    }
}
