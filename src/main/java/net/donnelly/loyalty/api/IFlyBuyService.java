package net.donnelly.loyalty.api;

import java.util.Map;

/**
 * This type represents the interface to the actual data.
 * <p>
 * This would ideally be implemented by a Database backed service. Or can call of to another REST api.
 * <p>
 * Created by bdonnell on 18/06/16.
 */
public interface IFlyBuyService {

    /**
     * Get the current number of points on a users account;
     *
     * @param id the id for the user account
     * @return the current number of points on this account
     */
    double getPoints(final String id);

    /**
     * Set the number of points on an account to the give number.
     *
     * @param id     the id for the account
     * @param points the new number of points
     */
    void setPoints(final String id, final double points);

    /**
     * Validate the give card number, and retrieve the details for that card.
     *
     * @param cardnum the card number to validate
     * @return the details for the user behind the card. Included username, and current number of points.
     */
    Map<String, Object> validateCard(final String cardnum);
}
