import com.google.common.io.Files;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class AndroidBasic {
    String apiToken = "xyz"; // your API token from https://appetize.io/docs#request-api-token
    String deviceType = "nexus5"; // nexus5, nexus7, nexus9
    String publicKey = "509eq0debke2bgjp3xazvnwqkm"; // replace with your own publicKey after uploading through website or API
    String osVersion = "7.0"; // 4.4, 5.1, 6.0, 7.0 supported
    String proxy = "intercept"; // false for no proxy, or specify your own with http://proxy-example.com:port
    String outputDirectory = "./outputs/";

    public void run() throws Exception {
        new File(outputDirectory).mkdir();

        JSONObject params = new JSONObject(); // will be passed to app at launch
        params.put("hello", "world");
        params.put("userId", 123);

        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("device", deviceType);
        capabilities.setCapability("publicKey", publicKey);
        capabilities.setCapability("osVersion", osVersion);
        capabilities.setCapability("proxy", proxy);
        capabilities.setCapability("params", params.toString());

        URL remote = new URL("https://" + apiToken + "@appium.appetize.io/wd/hub");
        AppiumDriver driver = new AndroidDriver(remote, capabilities);

        System.out.println("session started");

        Thread.sleep(5000);
        takeScreenshot(driver);

        System.out.println("tapping element");
        driver.findElementByXPath("//android.widget.TextView[@text=\"My lists\"]").click();

        Thread.sleep(2000);
        takeScreenshot(driver);

        System.out.println("ending session");
        String sessionId = driver.getSessionId().toString();
        driver.quit();

        // download files
        String timeStamp = "" + System.currentTimeMillis();
        if ("intercept".equals(proxy)) {
            downloadFile("https://api.appetize.io/v1/networkCapture/appiumId/" + sessionId,
                    "appetize-" + deviceType + "-" + osVersion + "-" + publicKey +
                    "-har-" + timeStamp + ".har");
        }

        downloadFile("https://api.appetize.io/v1/debugLog/appiumId/" + sessionId,
                "appetize-" + deviceType + "-" + osVersion + "-" + publicKey +
                        "-debugLog-" + timeStamp + ".txt");

        downloadFile("https://api.appetize.io/v1/screenRecording/appiumId/" + sessionId,
                "appetize-" + deviceType + "-" + osVersion + "-" + publicKey +
                        "-screenRecording-" + timeStamp + ".mp4");
    }

    void takeScreenshot(AppiumDriver driver) throws IOException {
        System.out.println("take screenshot");
        String fileName = "appetize-" + deviceType + "-" + osVersion + "-" + publicKey +
                "-screenshot-" + System.currentTimeMillis() + ".png";
        File destination = new File(outputDirectory, fileName);

        File screenShot = driver.getScreenshotAs(OutputType.FILE);
        Files.move(screenShot, destination);
    }

    void downloadFile(String url, String fileName) throws Exception {
        System.out.println("downloading " + url);
        URL remoteUrl = new URL(url);
        ReadableByteChannel rbc = Channels.newChannel(remoteUrl.openStream());
        FileOutputStream fos = new FileOutputStream(new File(outputDirectory, fileName));
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
    }
}