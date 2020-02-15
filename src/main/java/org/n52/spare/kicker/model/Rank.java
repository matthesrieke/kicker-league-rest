package org.n52.spare.kicker.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import org.n52.spare.kicker.model.Views.Basic;

@JsonInclude(JsonInclude.Include.NON_NULL)
public final class Rank {
    @JsonView({ Basic.class })
    private int rank;
    @JsonView({ Basic.class })
    private int points;
    @JsonView({ Basic.class })
    private int totalMatches;
    @JsonView({ Basic.class })

    private Player player;

    public Rank() {
    }

    public Rank(Player p) {
        this.player = p;
    }

    public final int getRank() {
        return this.rank;
    }

    public final void setRank(int var1) {
        this.rank = var1;
    }

    public final int getPoints() {
        return this.points;
    }

    public final void setPoints(int var1) {
        this.points = var1;
    }

    public final int getTotalMatches() {
        return this.totalMatches;
    }

    public final void setTotalMatches(int var1) {
        this.totalMatches = var1;
    }

    public final Player getPlayer() {
        return this.player;
    }

    public final void setPlayer(Player var1) {
        this.player = var1;
    }
}
