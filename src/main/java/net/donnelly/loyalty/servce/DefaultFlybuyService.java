package net.donnelly.loyalty.servce;

import net.donnelly.loyalty.api.IFlyBuyService;

import java.util.HashMap;
import java.util.Map;

/**
 * This is the default implementation of the {@link IFlyBuyService} that simply uses a couple of
 * {@link HashMap}s to maintain the state.
 * <p>
 * If really implemented this would in fact use a Database of other data source to maintain the data.
 * <p>
 * Created by bdonnell on 18/06/16.
 */
public class DefaultFlybuyService implements IFlyBuyService {

    private static final Map<String, Double> USER_POINTS = new HashMap<>();
    private static final Map<String, String> USER_CARDS = new HashMap<>();

    static {
        reset();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getPoints(final String id) {
        return USER_POINTS.get(id);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPoints(final String id, final double points) {
        USER_POINTS.put(id, points);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, Object> validateCard(final String cardnum) {
        if (cardnum.length() != 10) {
            return null;
        }
        final String userForCard = USER_CARDS.get(cardnum);
        if (userForCard == null) {
            return null;
        }
        final Double userPoints = USER_POINTS.get(userForCard);
        final Map<String, Object> map = new HashMap<>();
        map.put("userId", userForCard);
        map.put("cardNumber", cardnum);
        map.put("userPoints", userPoints);
        return map;
    }

    //// TODO: 18/06/16 remove this

    /**
     * This is used for the tests to reset the data.
     */
    public static void reset() {
        USER_POINTS.put("bdonnell", 100D);
        USER_POINTS.put("adonnell", 10D);
        USER_POINTS.put("sdonnell", 123D);

        USER_CARDS.put("1234567890", "bdonnell");
        USER_CARDS.put("2345678901", "adonnell");
        USER_CARDS.put("3456789012", "sdonnell");
    }
}
