package Services;

import javax.ejb.Remote;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Random;

@WebService(endpointInterface = "Services.ISStatusServiceInterface")
@Remote(ISStatusService.class)
public class ISStatusService implements ISStatusServiceInterface {

    @Override
    public String[] statusesToBean(Integer[] ids) {
        Random random = new Random();
        Integer count = ids.length;
        String[] statuses = new String[count];
        for (int i = 0; i < count; i++) {
            if(ids[i] % 2 == 0) {
                int randomValue = random.nextInt(100);
                if(randomValue <= 90) {
                    statuses[i] = isConnection("http://www.google.com");
                }
                else {
                    statuses[i] = isConnection("http://www.errorconnection.com");
                }
            }
            else {
                int randomValue = random.nextInt(100);
                if(randomValue <= 90) {
                    statuses[i] = isConnection("http://www.google.com");
                }
                else {
                    statuses[i] = isConnection("http://www.errorconnection.com");
                }
            }
        }
        return statuses;
    }

    public String isConnection(String testURL) {
        try {
            URL url = new URL(testURL);
            HttpURLConnection urlConnect = (HttpURLConnection) url.openConnection();
            Object objectData = urlConnect.getContent();
        } catch (UnknownHostException | MalformedURLException exception) {
            return "Down";
        } catch (IOException e) {
            return "Down";
        }
        return "Active";
    }

    public static void main(String[] argv) {
        Endpoint.publish("http://localhost:1488/ISStatusService", new ISStatusService());
    }
}
