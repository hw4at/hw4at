package surl.server;

import com.jayway.restassured.RestAssured;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static com.jayway.restassured.RestAssured.get;
import static org.hamcrest.Matchers.equalTo;

/**
 * Integration Test
 */
public class ShortURLVerticleIT {

    @BeforeClass
    public static void before() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 9988;
    }

    @Test
    public void simpleTest() {
    }

    @AfterClass
    public static void after() {
        RestAssured.reset();
    }
}
