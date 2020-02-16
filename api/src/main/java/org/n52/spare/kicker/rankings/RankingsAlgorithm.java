package org.n52.spare.kicker.rankings;

import org.n52.spare.kicker.model.Match;
import org.n52.spare.kicker.model.Rank;

import java.util.List;

public interface RankingsAlgorithm {

    List<Rank> calculateForMatches(List<Match> matches);

}
