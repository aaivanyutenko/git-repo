package by.gsu.mathan.xml;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class MathDOCXXMLHandler extends DefaultHandler {
	DocumentBuilderFactory builderFactory;
	DocumentBuilder builder;
	private Document document;
	private Node current;
	private Element currentParagr;
	private boolean plainText;
	private boolean mathText;
	private boolean textStyleBold;
	private StringWriter output;
	TransformerFactory transformerFactory;
	Transformer transformer;

	public MathDOCXXMLHandler(String title, StringWriter file) {
		builderFactory = null;
		builder = null;
		document = null;
		current = null;
		currentParagr = null;
		plainText = false;
		mathText = false;
		textStyleBold = false;
		transformerFactory = null;
		transformer = null;
		try {
			builderFactory = DocumentBuilderFactory.newInstance();
			builder = builderFactory.newDocumentBuilder();
			String documentText = (new StringBuilder("<html><head><title>")).append(title).append("</title><meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"></meta><script type=\"text/javascript\" src=\"http://cdn.mathjax.org/mathjax/latest/MathJax.js?config=MML_HTMLorMML\"></script></head><body><div style=\"text-align: justify;\"></div></body></html>").toString();
			document = builder.parse(new InputSource(new StringReader(documentText)));
			transformerFactory = TransformerFactory.newInstance();
			transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty("omit-xml-declaration", "yes");
			transformer.setOutputProperty("encoding", "UTF-8");
			transformer.setOutputProperty("indent", "no");
			output = file;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		}
	}

	public void characters(char ch[], int start, int length) throws SAXException {
		super.characters(ch, start, length);
		String text = (new String(ch)).substring(start, start + length);
		if (plainText) {
			Node textNode = document.createTextNode(text);
			current.appendChild(textNode);
			return;
		}
		if (mathText) {
			try {
				text = parseText(text);
				Document textDocument = builder.parse(new InputSource(new StringReader((new StringBuilder("<root>")).append(text).append("</root>").toString())));
				NodeList textElements = textDocument.getDocumentElement().getChildNodes();
				for (int i = 0; i < textElements.getLength(); i++) {
					Node item = textElements.item(i);
					Node documentItem = document.createElement(item.getNodeName());
					documentItem.setTextContent(item.getTextContent());
					current.appendChild(documentItem);
				}

			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				System.out.println(text);
			}
			return;
		} else {
			return;
		}
	}

	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		super.startElement(uri, localName, qName, attributes);
		if (qName.startsWith("m:")) {
			if (qName.equals("m:oMath")) {
				Element math = document.createElement("math");
				math.setAttribute("xmlns", "http://www.w3.org/1998/Math/MathML");
				current.appendChild(math);
				current = math;
				return;
			}
			if (qName.equals("m:r") || qName.equals("m:num") || qName.equals("m:den") || qName.equals("m:e")) {
				Node row = document.createElement("mrow");
				current.appendChild(row);
				current = row;
				return;
			}
			if (qName.equals("m:f")) {
				Node frac = document.createElement("mfrac");
				current.appendChild(frac);
				current = frac;
				return;
			}
			if (qName.equals("m:rad")) {
				Node sqrt = document.createElement("msqrt");
				current.appendChild(sqrt);
				current = sqrt;
				return;
			}
			if (qName.equals("m:deg"))
				return;
			if (qName.equals("m:sSub")) {
				Node sub = document.createElement("msub");
				current.appendChild(sub);
				current = sub;
				return;
			}
			if (qName.equals("m:sSup")) {
				Node sup = document.createElement("msup");
				current.appendChild(sup);
				current = sup;
				return;
			}
			if (qName.equals("m:t")) {
				mathText = true;
				return;
			}
			if (qName.equals("m:d")) {
				Element mfenced = document.createElement("mfenced");
				current.appendChild(mfenced);
				current = mfenced;
				return;
			}
			if (qName.equals("m:begChr")) {
				((Element) current).setAttribute("open", attributes.getValue("m:val"));
				return;
			}
			if (qName.equals("m:endChr")) {
				((Element) current).setAttribute("close", attributes.getValue("m:val"));
				return;
			}
		}
		if (qName.startsWith("w:")) {
			if (qName.equals("w:body")) {
				NodeList bodyList = document.getElementsByTagName("body");
				Node body = bodyList.item(0).getFirstChild();
				current = body;
				return;
			}
			if (qName.equals("w:p")) {
				Element p = document.createElement("p");
				currentParagr = p;
				current.appendChild(p);
				current = p;
				return;
			}
			if (qName.equals("w:jc") && "center".equals(attributes.getValue("w:val")))
				currentParagr.setAttribute("align", "center");
			if (qName.equals("w:r")) {
				plainText = true;
				return;
			}
			if (qName.equals("w:b")) {
				if (!"b".equals(current.getNodeName())) {
					Node b = document.createElement("b");
					current.appendChild(b);
					current = b;
				}
				textStyleBold = true;
				return;
			}
			if (qName.equals("w:t")) {
				if (!textStyleBold && "b".equals(current.getNodeName()))
					current = current.getParentNode();
				return;
			}
		}
	}

	public void endElement(String uri, String localName, String qName) throws SAXException {
		super.endElement(uri, localName, qName);
		if (qName.startsWith("m:")) {
			if (qName.equals("m:num") || qName.equals("m:den") || qName.equals("m:r") || qName.equals("m:sSup") || qName.equals("m:f") || qName.equals("m:rad") || qName.equals("m:e") || qName.equals("m:sSub")) {
				current = current.getParentNode();
				return;
			}
			if (qName.equals("m:oMath")) {
				current = current.getParentNode();
				return;
			}
			if (qName.equals("m:d")) {
				current = current.getParentNode();
				return;
			}
			if (qName.equals("m:t")) {
				mathText = false;
				return;
			}
		}
		if (qName.startsWith("w:")) {
			if (qName.equals("w:p")) {
				current = current.getParentNode();
				return;
			}
			if (qName.equals("w:body")) {
				try {
					DOMSource source = new DOMSource(document);
					transformer.transform(source, new StreamResult(output));
				} catch (TransformerException e) {
					e.printStackTrace();
				}
				return;
			}
			if (qName.equals("w:r")) {
				plainText = false;
				textStyleBold = false;
				return;
			}
		}
	}

	private String parseText(String source) {
		return source.replaceAll("<", "__").replaceAll(">", "_").replaceAll("([A-Za-z\u03A6\u03BE\u03B5]+)", "<mi>$1</mi>").replaceAll("([-+=\uFFFD'\u2208,\u2260\u2200\u2203:\u2264\u27F9\u2026])", "<mo>$1</mo>").replaceAll("(\\d)", "<mn>$1</mn>").replaceAll("(\\s)", "<mtext>&#x205F;&#x200A;</mtext>").replaceAll("__", "<mo>&lt;</mo>").replaceAll("_", "<mo>&gt;</mo>");
	}

	public String toString() {
		String out = null;
		try {
			StringWriter stringWriter = new StringWriter();
			StreamResult result = new StreamResult(stringWriter);
			DOMSource source = new DOMSource(document);
			transformer.transform(source, result);
			out = stringWriter.toString();
		} catch (TransformerException e) {
			e.printStackTrace();
		}
		return out;
	}
}
