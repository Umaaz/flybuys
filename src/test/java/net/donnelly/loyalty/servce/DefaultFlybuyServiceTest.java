package net.donnelly.loyalty.servce;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

/**
 * Created by bdonnell on 18/06/16.
 */
public class DefaultFlybuyServiceTest {

    private DefaultFlybuyService defaultFlybuyService;

    @Before
    public void setUp() throws Exception {
        defaultFlybuyService = new DefaultFlybuyService();
    }

    @After
    public void tearDown() throws Exception {
        DefaultFlybuyService.reset();
        defaultFlybuyService = null;
    }

    @Test
    public void getPoints() throws Exception {
        Assert.assertEquals(100D, defaultFlybuyService.getPoints("bdonnell"), 0.0d);
        Assert.assertEquals(10D, defaultFlybuyService.getPoints("adonnell"), 0.0d);
        Assert.assertEquals(123D, defaultFlybuyService.getPoints("sdonnell"), 0.0d);
    }

    @Test
    public void setPoints() throws Exception {
        Assert.assertEquals(100D, defaultFlybuyService.getPoints("bdonnell"), 0.0d);
        defaultFlybuyService.setPoints("bdonnell", 10);
        Assert.assertEquals(10D, defaultFlybuyService.getPoints("bdonnell"), 0.0d);
        defaultFlybuyService.setPoints("bdonnell", 111);
        Assert.assertEquals(111D, defaultFlybuyService.getPoints("bdonnell"), 0.0d);
    }

    @Test
    public void validateCard() throws Exception {
        final Map<String, Object> user = defaultFlybuyService.validateCard("1234567890");
        Assert.assertNotNull(user);
        Assert.assertEquals("bdonnell", user.get("userId"));
        Assert.assertEquals("1234567890", user.get("cardNumber"));
        Assert.assertEquals(100D, user.get("userPoints"));
    }

}