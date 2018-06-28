import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.apache.batik.apps.rasterizer.SVGConverter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.*;
import java.io.File;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class Main {
    public static void main(String[] args) {

        List<FederalState> fedList = new ArrayList<FederalState>();

        try {
            File fXmlFile = new File("./src/DeutschlandMitStaedten.svg");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse("./src/DeutschlandMitStaedten.svg");

            NodeList list = doc.getElementsByTagName("g");
            Element el = doc.getElementById("Thüringen");
            Element containerElement = (Element) list.item(0);
            NodeList pathList = containerElement.getElementsByTagName("path");

            // Iterate through all elements/states in list
            for (int i = 0; i < pathList.getLength(); i++) {
                Element element = (Element) pathList.item(i);
                String stateName = element.getAttribute("id");
                //System.out.println(stateName);

                FederalState state = new FederalState(stateName, element.getAttribute("d"));
                fedList.add(state);
                //System.out.println(state.volume());
            }

            // Sort by volume (ascending)
            Collections.sort(fedList, (FederalState s1, FederalState s2) ->{
                return Float.compare(s1.volume(), s2.volume());
            });

            // print out to check order of states by volume
            for( FederalState s: fedList)
            {
                System.out.println(s.getName());
                System.out.println(s.volume());
            }

            

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}

