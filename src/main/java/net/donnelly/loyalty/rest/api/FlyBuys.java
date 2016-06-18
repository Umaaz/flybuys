package net.donnelly.loyalty.rest.api;

import net.donnelly.loyalty.api.IFlyBuyService;
import net.donnelly.loyalty.servce.DefaultFlybuyService;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

/**
 * This type represents the main REST API for the flybuys point gambling system.
 * <p>
 * Created by bdonnell on 18/06/16.
 */
@Path("flybuys")
public class FlyBuys {

    private static final IFlyBuyService flyBuysService = new DefaultFlybuyService();

    /**
     * <ul>
     * <li>Allows: POST</li>
     * <li>Produces: Application Json</li>
     * </ul>
     * <p>
     * Validates that the card number is correct.
     * </p>
     *
     * @param cardnum the card number to check
     * @return {@link Response.Status#OK} if the card is valid with a json response in the body with the user details.
     * Else a {@link javax.ws.rs.core.Response.Status#BAD_REQUEST} is returned.
     */
    @POST
    @Path("validatecard/{cardnum}")
    @Produces(MediaType.APPLICATION_JSON)
    public Object validateCard(@PathParam("cardnum") final String cardnum) {
        final Map<String, Object> user = flyBuysService.validateCard(cardnum);
        if (user != null) {
            return user;
        }
        return Response.status(Response.Status.BAD_REQUEST).build();
    }


    /**
     * <ul>
     * <li>Allows: GET</li>
     * <li>Produces: Application Json</li>
     * </ul>
     * <p>
     * Get the current number of points for ths suplied user.
     * </p>
     *
     * @param id the user id to get points for
     * @return a json object with the number of points available for the supplied user
     */
    @GET
    @Path("points/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Double> getPoints(@PathParam("id") final String id) {
        final Map<String, Double> hashMap = new HashMap<>();
        hashMap.put(id, flyBuysService.getPoints(id));
        return hashMap;
    }

    /**
     * <ul>
     * <li>Allows: PUT</li>
     * <li>Produces: Application Json</li>
     * </ul>
     * <p>
     * Credit points onto the supplied users account.
     * </p>
     *
     * @param id     the user id to credit points on
     * @param points the number of points to credit
     * @return a json object with the new number of points for this user
     */
    @PUT
    @Path("points/{id}/{points}")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Double> creditPoints(@PathParam("id") final String id, @PathParam("points") final double points) {
        final Map<String, Double> hashMap = new HashMap<>();
        final double currentPoints = flyBuysService.getPoints(id);
        final double newPoints = points + currentPoints;
        flyBuysService.setPoints(id, newPoints);
        hashMap.put(id, newPoints);
        return hashMap;
    }


    /**
     * <ul>
     * <li>Allows: PUT</li>
     * <li>Produces: Application Json</li>
     * </ul>
     * <p>
     * Debit points from the supplied users account.
     * </p>
     *
     * @param id     the user id to debit points on
     * @param points the number of points to debit
     * @return a json object with the new number of points for this user
     */
    @DELETE
    @Path("points/{id}/{points}")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, Double> debitPoints(@PathParam("id") final String id, @PathParam("points") final double points) {
        final Map<String, Double> hashMap = new HashMap<>();
        final double currentPoints = flyBuysService.getPoints(id);
        final double newPoints = currentPoints - points;
        flyBuysService.setPoints(id, newPoints);
        hashMap.put(id, newPoints);
        return hashMap;
    }
}
