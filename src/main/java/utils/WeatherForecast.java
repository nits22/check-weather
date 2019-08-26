package utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.Iterator;

public class WeatherForecast implements Runnable{

    //final ThreadLocal<WeatherForecast> weatherForecastThreadLocal = ThreadLocal.withInitial(() -> new WeatherForecast());

    private String location;
    private String responseType;

    WeatherForecast(String location, String responseType){
        this.location = location;
        this.responseType = responseType;
    }

    //gurgaon,hr
    @Override
    public void run() {

        YahooWeatherAPI y = new YahooWeatherAPI();

        String resp = y.getWheatherAPI(location, responseType);


        if(responseType.equals("json")) {
            findRainForecast_JSON(resp, "forecasts");
            FileInputStream fis1 = null;
            String response =null;
            try {
                fis1 = new FileInputStream("src/main/resources/JSONdata");
                BufferedReader source = new BufferedReader(new InputStreamReader(fis1));
                response = new String();
                for (String line; (line = source.readLine()) != null; response += line);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            CompareJSON compareJSON = new CompareJSON();
            System.out.println(compareJSON.compare(response, resp));

        }
        else {
            findRainForecast_XML(resp.toString(),"yweather:condition");
            //rootElement.getElementsByTagName("yweather:condition").item(0).getAttributes().getNamedItem("code")

            try {
                FileInputStream fis1 = new FileInputStream("src/main/resources/XMLdata.xml");
                BufferedReader source = new BufferedReader(new InputStreamReader(fis1));

            CompareXML compareXML = new CompareXML();
            String response = new String();
                for (String line; (line = source.readLine()) != null; response += line);
                System.out.println(compareXML.compare(response, resp));

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void findRainForecast_JSON(String resp, String keyName) {

        JsonElement jelement = new JsonParser().parse(resp);
        JsonObject jobject = jelement.getAsJsonObject();
        JsonArray forecastArray = jobject.getAsJsonArray(keyName);

        Iterator iterator = forecastArray.iterator();

        while (iterator.hasNext()) {
            JsonObject obj = (JsonObject) iterator.next();
            if (obj.get("code").getAsInt() == Condition.RAIN.getCode() && obj.get("text").getAsString().equalsIgnoreCase(Condition.RAIN.getDesc()) ||
                    obj.get("code").getAsInt() == Condition.THUNDERSTORMS.getCode() && obj.get("text").getAsString().equalsIgnoreCase(Condition.THUNDERSTORMS.getDesc()) ||
                    obj.get("code").getAsInt() == Condition.SCATTERED_THUNDERSHOWERS.getCode() && obj.get("text").getAsString().equalsIgnoreCase(Condition.SCATTERED_THUNDERSHOWERS.getDesc()) ||
                    obj.get("code").getAsInt() == Condition.HEAVY_RAIN.getCode() && obj.get("text").getAsString().equalsIgnoreCase(Condition.HEAVY_RAIN.getDesc()) ||
                    obj.get("code").getAsInt() == Condition.MIXED_RAIN_HAIL.getCode() && obj.get("text").getAsString().equalsIgnoreCase(Condition.MIXED_RAIN_HAIL.getDesc()) ||
                    obj.get("code").getAsInt() == Condition.MIXED_RAIN_SNOW.getCode() && obj.get("text").getAsString().equalsIgnoreCase(Condition.MIXED_RAIN_SNOW.getDesc())) {
                System.out.println("Yes, it's gonna rain in next 7 days");
                break;
            }

        }
    }

    public void findRainForecast_XML(String resp, String tagName){
        NodeList list = getXMLTag(tagName, resp.toString());
        //rootElement.getElementsByTagName("yweather:condition").item(0).getAttributes().getNamedItem("code")

        if (list != null && list.getLength() > 0) {
            for (int i = 0; i < list.getLength(); i++) {
                NamedNodeMap nodeMap = list.item(i).getAttributes();

                if (Integer.parseInt(nodeMap.getNamedItem("code").getNodeValue()) == (Condition.RAIN.getCode()) && nodeMap.getNamedItem("text").getNodeValue().equalsIgnoreCase(Condition.RAIN.getDesc()) ||
                        Integer.parseInt(nodeMap.getNamedItem("code").getNodeValue()) == (Condition.THUNDERSTORMS.getCode()) && nodeMap.getNamedItem("text").getNodeValue().equalsIgnoreCase(Condition.THUNDERSTORMS.getDesc()) ||
                        Integer.parseInt(nodeMap.getNamedItem("code").getNodeValue()) == (Condition.SCATTERED_THUNDERSHOWERS.getCode()) && nodeMap.getNamedItem("text").getNodeValue().equalsIgnoreCase(Condition.SCATTERED_THUNDERSHOWERS.getDesc()) ||
                        Integer.parseInt(nodeMap.getNamedItem("code").getNodeValue()) == (Condition.HEAVY_RAIN.getCode()) && nodeMap.getNamedItem("text").getNodeValue().equalsIgnoreCase(Condition.HEAVY_RAIN.getDesc()) ||
                        Integer.parseInt(nodeMap.getNamedItem("code").getNodeValue()) == (Condition.MIXED_RAIN_SNOW.getCode()) && nodeMap.getNamedItem("text").getNodeValue().equalsIgnoreCase(Condition.MIXED_RAIN_SNOW.getDesc()) ||
                        Integer.parseInt(nodeMap.getNamedItem("code").getNodeValue()) == (Condition.MIXED_RAIN_HAIL.getCode()) && nodeMap.getNamedItem("text").getNodeValue().equalsIgnoreCase(Condition.MIXED_RAIN_HAIL.getDesc())) {
                    System.out.println("Yes, it's gonna rain in next 7 days");
                }
            }
        }
    }


    protected static NodeList getXMLTag(String tagName, String xml) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        Document document = null;
        try {
            builder = factory.newDocumentBuilder();
            document = builder.parse(new InputSource(new StringReader(xml)));
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        Element rootElement = document.getDocumentElement();

        NodeList list = rootElement.getElementsByTagName(tagName);
        /*if (list != null && list.getLength() > 0) {
            NodeList subList = list.item(0).getChildNodes();

            if (subList != null && subList.getLength() > 0) {
                return subList.item(0).getNodeValue();
            }
        }*/
//rootElement.getElementsByTagName("yweather:condition").item(0).getAttributes().getNamedItem("code")
        return list;
    }


}
