package net.donnelly.loyalty.rest.api;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.donnelly.loyalty.rest.RestServer;
import net.donnelly.loyalty.servce.DefaultFlybuyService;
import org.apache.http.HttpEntity;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by bdonnell on 18/06/16.
 */
public class FlyBuysTest {

    private RestServer restServer;
    private CloseableHttpClient httpClient;

    @Before
    public void setUp() throws Exception {
        restServer = new RestServer("127.0.0.1", Integer.getInteger("flybuys.test.port", 8080));
        restServer.start();
        httpClient = HttpClients.createDefault();
    }

    @After
    public void tearDown() throws Exception {
        restServer.stop();
        httpClient.close();
        DefaultFlybuyService.reset();
    }

    @Test
    public void validateCard() throws Exception {
        {
            final HttpPost httpPost = new HttpPost("http://127.0.0.1:" + Integer.getInteger("flybuys.test.port", 8080) + "/rest/flybuys/validatecard/753951");
            final CloseableHttpResponse execute = httpClient.execute(httpPost);
            final StatusLine statusLine = execute.getStatusLine();
            final int statusCode = statusLine.getStatusCode();
            Assert.assertEquals("The card number is bad.", 400, statusCode);
            execute.close();
        }
        {
            final HttpPost httpPost = new HttpPost("http://127.0.0.1:" + Integer.getInteger("flybuys.test.port", 8080) + "/rest/flybuys/validatecard/1234567890");
            final CloseableHttpResponse execute = httpClient.execute(httpPost);
            final StatusLine statusLine = execute.getStatusLine();
            final int statusCode = statusLine.getStatusCode();
            Assert.assertEquals("This is a good card number.", 200, statusCode);
            final HttpEntity entity = execute.getEntity();
            final InputStream responseAsStream = entity.getContent();
            final Map<String, Object> responseAsMap = toJson(responseAsStream);

            Assert.assertEquals("bdonnell", responseAsMap.get("userId"));
            Assert.assertEquals("1234567890", responseAsMap.get("cardNumber"));
            Assert.assertEquals(100D, responseAsMap.get("userPoints"));
        }
    }

    @Test
    public void getPoints() throws Exception {
        final HttpGet httpGet = new HttpGet("http://127.0.0.1:" + Integer.getInteger("flybuys.test.port", 8080) + "/rest/flybuys/points/bdonnell");
        final CloseableHttpResponse execute = httpClient.execute(httpGet);
        final InputStream content = execute.getEntity().getContent();
        final Map<String, Object> toJson = toJson(content);

        Assert.assertEquals(100D, toJson.get("bdonnell"));
    }

    @Test
    public void creditPoints() throws Exception {
        final HttpPut httpPut = new HttpPut("http://127.0.0.1:" + Integer.getInteger("flybuys.test.port", 8080) + "/rest/flybuys/points/bdonnell/11");
        final CloseableHttpResponse execute = httpClient.execute(httpPut);
        final InputStream content = execute.getEntity().getContent();
        final Map<String, Object> toJson = toJson(content);

        Assert.assertEquals(111D, toJson.get("bdonnell"));
    }

    @Test
    public void debitPoints() throws Exception {
        final HttpDelete httpDelete = new HttpDelete("http://127.0.0.1:" + Integer.getInteger("flybuys.test.port", 8080) + "/rest/flybuys/points/bdonnell/11");
        final CloseableHttpResponse execute = httpClient.execute(httpDelete);
        final InputStream content = execute.getEntity().getContent();
        final Map<String, Object> toJson = toJson(content);

        Assert.assertEquals(89D, toJson.get("bdonnell"));
    }


    private static Map<String, Object> toJson(final InputStream responseAsStream) throws Exception {
        final int available = responseAsStream.available();
        final byte[] bytes = new byte[available];
        responseAsStream.read(bytes);
        final String responseAsJSON = new String(bytes);

        JsonFactory factory = new JsonFactory();
        ObjectMapper mapper = new ObjectMapper(factory);
        TypeReference<HashMap<String, Object>> typeRef
                = new TypeReference<HashMap<String, Object>>() {
        };

        return mapper.readValue(responseAsJSON, typeRef);
    }
}