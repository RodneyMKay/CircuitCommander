package me.jangroen.circuitcommander.logiccircuit;

import me.jangroen.circuitcommander.logiccircuit.entities.LogicalEntity;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

public class LogicalCircuit extends LogicalComponent {
    private String title;
    private List<LogicalEntity> logicalEntityList;

    public LogicalCircuit(String title) {
        super(UUID.randomUUID());
        this.title = title;
        this.logicalEntityList = new ArrayList<>();
    }

    public void addEntity(LogicalEntity logicalEntity) {
        logicalEntityList.add(logicalEntity);
    }

    public void addAllEntities(Collection<LogicalEntity> logicalEntities) {
        logicalEntityList.addAll(logicalEntities);
    }

    private Element getProject(Document document) {
        Element root = document.createElement("lc:Project");

        Element projectId = document.createElement("lc:ProjectId");
        projectId.appendChild(document.createTextNode(UUID.randomUUID().toString()));
        root.appendChild(projectId);

        Element name = document.createElement("lc:Name");
        name.appendChild(document.createTextNode(this.title));
        root.appendChild(name);

        Element openCircuit = document.createElement("lc:LogicalCircuitId");
        openCircuit.appendChild(document.createTextNode(uuid.toString()));
        root.appendChild(openCircuit);

        return root;
    }

    private Element getCircuit(Document document) {
        Element root = document.createElement("lc:LogicalCircuit");

        Element circuitId = document.createElement("lc:LogicalCircuitId");
        circuitId.appendChild(document.createTextNode(uuid.toString()));
        root.appendChild(circuitId);

        Element name = document.createElement("lc:Name");
        name.appendChild(document.createTextNode(this.title));
        root.appendChild(name);

        Element notation = document.createElement("lc:Notation");
        notation.appendChild(document.createTextNode(this.title));
        root.appendChild(notation);

        return root;
    }

    public void writeTo(OutputStream outputStream) {
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

            Element rootElement = document.createElement("lc:CircuitProject");
            rootElement.setAttribute("xmlns:lc", "http://LogicCircuit.net/2.0.0.7/CircuitProject.xsd");
            document.appendChild(rootElement);
            document.setXmlVersion("1.0");

            rootElement.appendChild(getProject(document));
            rootElement.appendChild(getCircuit(document));

            for(LogicalEntity logicalEntity : logicalEntityList) {
                Element entity = logicalEntity.getEntity(document);
                Element symbol = logicalEntity.getEntitySymbol(document);
                if(entity != null) rootElement.appendChild(entity);
                if(symbol != null) rootElement.appendChild(symbol);
            }

            DOMSource domSource = new DOMSource(document);
            StreamResult consoleResult = new StreamResult(outputStream);
            Transformer transformer = TransformerFactory.newInstance().newTransformer();

            transformer.getOutputProperties().clear();
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");

            transformer.transform(domSource, consoleResult);
        } catch (TransformerException | ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
}
