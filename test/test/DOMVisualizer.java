package test;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Entity;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * A simple test class that will output the DOM interpretation of a test document.
 * @author cmiller
 */
public class DOMVisualizer {

    public DOMVisualizer() {
    }

	@Test
	public void outputStuff() {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setValidating(false);
		dbf.setSchema(null);
		DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException ex) {
			Logger.getLogger(DOMVisualizer.class.getName()).log(Level.SEVERE, null, ex);
		}
		Document doc=null;
		try {
			doc = db.parse(new File("FSCode Test Text"));
		} catch (SAXException ex) {
			Logger.getLogger(DOMVisualizer.class.getName()).log(Level.SEVERE, null, ex);
		} catch (IOException ex) {
			Logger.getLogger(DOMVisualizer.class.getName()).log(Level.SEVERE, null, ex);
		}
		echo(doc);
	}

    int indent=0;
    String basicIndent="   ";

    private void outputIndentation() {
        for (int i = 0; i < indent; i++) {
            System.out.print(basicIndent);
        }
    }

    private void printlnCommon(Node n) {
        System.out.print(" nodeName=\"" + n.getNodeName() + "\"");

        String val = n.getNamespaceURI();
        if (val != null) {
            System.out.print(" uri=\"" + val + "\"");
        }

        val = n.getPrefix();
        if (val != null) {
            System.out.print(" pre=\"" + val + "\"");
        }

        val = n.getLocalName();
        if (val != null) {
            System.out.print(" local=\"" + val + "\"");
        }

        val = n.getNodeValue();
        if (val != null) {
            System.out.print(" nodeValue=");
            if (val.trim().equals("")) {
                // Whitespace
                System.out.print("[WS]");
            } else {
                System.out.print("\"" + n.getNodeValue() + "\"");
            }
        }
        System.out.println();
    }

    public void echo(Node n) {
	outputIndentation();

        int type = n.getNodeType();
        switch (type) {
        case Node.ATTRIBUTE_NODE:
            System.out.print("ATTR:");
            printlnCommon(n);
            break;
        case Node.CDATA_SECTION_NODE:
            System.out.print("CDATA:");
            printlnCommon(n);
            break;
        case Node.COMMENT_NODE:
            System.out.print("COMM:");
            printlnCommon(n);
            break;
        case Node.DOCUMENT_FRAGMENT_NODE:
            System.out.print("DOC_FRAG:");
            printlnCommon(n);
            break;
        case Node.DOCUMENT_NODE:
            System.out.print("DOC:");
            printlnCommon(n);
            break;
        case Node.DOCUMENT_TYPE_NODE:
            System.out.print("DOC_TYPE:");
            printlnCommon(n);

            NamedNodeMap nodeMap = ((DocumentType)n).getEntities();
            indent += 2;
            for (int i = 0; i < nodeMap.getLength(); i++) {
                Entity entity = (Entity)nodeMap.item(i);
                echo(entity);
            }
            indent -= 2;
            break;
        case Node.ELEMENT_NODE:
            System.out.print("ELEM:");
            printlnCommon(n);

            NamedNodeMap atts = n.getAttributes();
            indent += 2;
            for (int i = 0; i < atts.getLength(); i++) {
                Node att = atts.item(i);
                echo(att);
            }
            indent -= 2;
            break;
        case Node.ENTITY_NODE:
            System.out.print("ENT:");
            printlnCommon(n);
            break;
        case Node.ENTITY_REFERENCE_NODE:
            System.out.print("ENT_REF:");
            printlnCommon(n);
            break;
        case Node.NOTATION_NODE:
            System.out.print("NOTATION:");
            printlnCommon(n);
            break;
        case Node.PROCESSING_INSTRUCTION_NODE:
            System.out.print("PROC_INST:");
            printlnCommon(n);
            break;
        case Node.TEXT_NODE:
            System.out.print("TEXT:");
            printlnCommon(n);
            break;
        default:
            System.out.print("UNSUPPORTED NODE: " + type);
            printlnCommon(n);
            break;
        }

        indent++;
        for (Node child = n.getFirstChild(); child != null;
             child = child.getNextSibling()) {
            echo(child);
        }
        indent--;

    }

}