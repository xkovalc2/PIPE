package pipe.reachability.state;

import pipe.animation.State;
import pipe.animation.TokenCount;

import java.io.Serializable;
import java.util.Collection;

/**
 * Represents a state of the Petri net in a reachability graph
 * For each place it will contain the number of tokens stored
 * E.g
 * At a given point in time if the following places contain the following tokens:
 * P1 = 1
 * P2 = 2
 * P3 = 3
 * P4 = 1
 * P4 = 0
 *
 * Then the state will be as follows:
 * (1, 2, 3, 1, 0)
 */
public interface ExplorerState extends Serializable {
    /**
     *
     * @param id Place id
     * @return number of tokens for the place with the given id
     */
    Collection<TokenCount> getTokens(String id);

    State getState();

    boolean isTangible();

    void accept(ExplorerStateVisitor visitor);

}
