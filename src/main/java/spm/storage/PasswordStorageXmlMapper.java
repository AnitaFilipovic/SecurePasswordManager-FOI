package spm.storage;

import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

public final class PasswordStorageXmlMapper {
    public static Document toXml(PasswordStorageRoot root) throws ParserConfigurationException {
        Document xmlDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element xmlRoot = xmlDocument.createElement("root");

        for (PasswordStorageFolder folder : root) {
            Element xmlFolder = xmlDocument.createElement("folder");
            xmlFolder.setAttribute("name", folder.getName());

            for (PasswordStorageItem item : folder) {
                Element xmlItem = xmlDocument.createElement("item");
                xmlItem.setAttribute("name", item.getName());

                for (PasswordStorageData data : item) {
                    Element xmlData = xmlDocument.createElement("data");
                    xmlData.setAttribute("title", data.getTitle());
                    xmlData.setAttribute("username", data.getUsername());
                    xmlData.setAttribute("password", data.getPassword());
                    xmlData.setAttribute("url", data.getUrl());
                    xmlData.setAttribute("port", data.getPort());

                    xmlItem.appendChild(xmlData);
                }
                xmlFolder.appendChild(xmlItem);
            }
            xmlRoot.appendChild(xmlFolder);
        }

        xmlDocument.appendChild(xmlRoot);
        return xmlDocument;
    }

    public static PasswordStorageRoot fromXml(String xmlString) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder xmlDocumentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        InputSource inputSource = new InputSource(new StringReader(xmlString));
        Document xmlDocument = xmlDocumentBuilder.parse(inputSource);

        PasswordStorageRoot passwordStorageRoot = new PasswordStorageRoot();
        Node nodeRoot = xmlDocument.getElementsByTagName("root").item(0);
        NodeList nodeFolders = nodeRoot.getChildNodes();
        int folderCount = nodeRoot.getChildNodes().getLength();
        for (int i = 0; i < folderCount; i++) {
            PasswordStorageFolder passwordStorageFolder = new PasswordStorageFolder();
            Node nodeFolder = nodeFolders.item(i);
            NamedNodeMap folderAttributes = nodeFolder.getAttributes();
            if (folderAttributes.getNamedItem("name") != null) {
                passwordStorageFolder.setName(folderAttributes.getNamedItem("name").getNodeValue());
            }

            NodeList nodeItems = nodeFolder.getChildNodes();
            int itemCount = nodeItems.getLength();
            for (int j = 0; j < itemCount; j++) {
                PasswordStorageItem passwordStorageItem = new PasswordStorageItem();
                Node nodeItem = nodeItems.item(j);
                NamedNodeMap itemAttributes = nodeItem.getAttributes();
                if (itemAttributes.getNamedItem("name") != null) {
                    passwordStorageItem.setName(itemAttributes.getNamedItem("name").getNodeValue());
                }

                NodeList nodeDatas = nodeItem.getChildNodes();
                int dataCount = nodeDatas.getLength();
                for (int k = 0; k < dataCount; k++) {
                    PasswordStorageData passwordStorageData = new PasswordStorageData();
                    Node nodeData = nodeDatas.item(k);
                    NamedNodeMap dataAttributes = nodeData.getAttributes();
                    if (dataAttributes.getNamedItem("title") != null) {
                        passwordStorageData.setTitle(dataAttributes.getNamedItem("title").getNodeValue());
                    }
                    if (dataAttributes.getNamedItem("username") != null) {
                        passwordStorageData.setUsername(dataAttributes.getNamedItem("username").getNodeValue());
                    }
                    if (dataAttributes.getNamedItem("password") != null) {
                        passwordStorageData.setPassword(dataAttributes.getNamedItem("password").getNodeValue());
                    }
                    if (dataAttributes.getNamedItem("url") != null) {
                        passwordStorageData.setUrl(dataAttributes.getNamedItem("url").getNodeValue());
                    }
                    if (dataAttributes.getNamedItem("port") != null) {
                        passwordStorageData.setPort(dataAttributes.getNamedItem("port").getNodeValue());
                    }

                    passwordStorageItem.add(passwordStorageData);
                }

                passwordStorageFolder.add(passwordStorageItem);
            }

            passwordStorageRoot.add(passwordStorageFolder);
        }

        return passwordStorageRoot;
    }

    public static String toString(PasswordStorageRoot passwordStorageRoot) throws ParserConfigurationException, TransformerException {
        Document xml = PasswordStorageXmlMapper.toXml(passwordStorageRoot);
        StringWriter writer = new StringWriter();
        Transformer transformer = TransformerFactory.newInstance().newTransformer();
        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        transformer.transform(new DOMSource(xml), new StreamResult(writer));
        return writer.getBuffer().toString();
    }
}
