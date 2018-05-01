import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.apache.batik.apps.rasterizer.SVGConverter;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;

public class Main {

    public static void main(String[] args) {

        try {
            File fXmlFile = new File("./src/DeutschlandMitStaedten.svg");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse("./src/DeutschlandMitStaedten.svg");

            NodeList list = doc.getElementsByTagName("g");
            Element el = doc.getElementById("Thüringen");
            Element containerElement = (Element) list.item(0);
            NodeList pathList = containerElement.getElementsByTagName("path");
            Element firstState = (Element) pathList.item(0);
            String stateName = firstState.getAttribute("id");

            FederalState state = new FederalState(stateName, firstState.getAttribute("d"));
            System.out.println(state.getStartPoint().component1());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
