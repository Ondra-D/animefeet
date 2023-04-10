package com.ondrad.animefeet;

import com.ondrad.animefeet.animeFeetConfig;
import net.runelite.api.Client;
import net.runelite.client.ui.overlay.Overlay;
import net.runelite.client.ui.overlay.OverlayLayer;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.OverlayPosition;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class animeFeetOverlay extends Overlay {

    private BufferedImage image;

    @Inject
    private animeFeetConfig config;


    @Inject
    animeFeetOverlay() throws IOException {
        setPosition(OverlayPosition.DYNAMIC);
        setLayer(OverlayLayer.ALWAYS_ON_TOP);
    }


    public void GETRequest() throws IOException {
        String urlName = "https://api.raphtalia.xyz/feet";
        URL urlForGetReq = new URL(urlName);
        String read = null;
        HttpURLConnection connection = (HttpURLConnection) urlForGetReq.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Cache-Control", "no-cache");
        int codeResponse = connection.getResponseCode();
        if (codeResponse == HttpURLConnection.HTTP_OK) {
            InputStreamReader isrObj = new InputStreamReader(connection.getInputStream());
            BufferedReader bf = new BufferedReader(isrObj);
            StringBuffer responseStr = new StringBuffer();
            while ((read = bf.readLine()) != null) {
                responseStr.append(read);
            }
            bf.close();
            connection.disconnect();

            //IDK how to make this better
            String responseStrString = responseStr.toString();
            String replacedresponse = responseStrString.replace("{\"image\":\"", "");
            String replacedresponse2 = replacedresponse.replace("\"}", "");

            URL imageUrl = new URL(replacedresponse2);
            File imageFile = new File("src/main/resources/feet.png");
            ImageIO.write(ImageIO.read(imageUrl), "png", imageFile);

            image = ImageIO.read(imageFile);

        } else {
            System.out.println("GET Request did not work");
        }

    }




    @Override
    public Dimension render(Graphics2D graphics)
    {
        graphics.drawImage(image, config.xpos(), config.ypos(), config.dimension().width, config.dimension().height, null);
        return null;
    }
}
