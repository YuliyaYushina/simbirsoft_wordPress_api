package tests;


import config.Configuration;
import io.restassured.RestAssured;
import io.restassured.authentication.AuthenticationScheme;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.aeonbits.owner.ConfigFactory;

public class BaseTest {
        protected static final Configuration config = ConfigFactory.create(Configuration.class);
        protected final RequestSpecification spec = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setAccept(ContentType.JSON)
                .setBaseUri(config.baseUrl())
                .setBasePath(config.basePath())
                .setAuth(RestAssured.preemptive().basic(config.user(), config.password()))
                .build();

}
