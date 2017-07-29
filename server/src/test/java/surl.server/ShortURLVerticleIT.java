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
    public void checkThatWeCanRetrieveIndividualProduct() {
        int id = get("/api/whiskies").then().assertThat()
                .statusCode(200)
                .extract()
                .jsonPath().getInt("find { it.name=='Bowmore 15 Years Laimrig' }.id");
        // Now get the individual resource and check the content
        get("/api/whiskies/" + id).then()
                .assertThat()
                .statusCode(200)
                .body("name", equalTo("Bowmore 15 Years Laimrig"))
                .body("origin", equalTo("Scotland, Islay"))
                .body("id", equalTo(id));
    }

    @AfterClass
    public static void after() {
        RestAssured.reset();
    }
}
