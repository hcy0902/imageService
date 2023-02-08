package integrationTests;

import com.image.Bridge.ImaggaClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ImaggaClientIntegrationTest {

   @LocalServerPort
   private int port;
   private String baseUrl="http://localhost";


    //@Test
   // public void getObjectsFromImageSuccess(){}
}
