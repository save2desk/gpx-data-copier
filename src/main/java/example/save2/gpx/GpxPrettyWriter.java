package example.save2.gpx;

import org.codehaus.stax2.XMLStreamWriter2;

import javax.xml.stream.XMLStreamException;

public class GpxPrettyWriter {

    private final XMLStreamWriter2 writer;
    private int indentLevel = 0;
    private boolean newLine = true;

    public GpxPrettyWriter(XMLStreamWriter2 writer) {
        this.writer = writer;
    }

    private void writeIndent() throws XMLStreamException {
        if (newLine) {
            writer.writeCharacters("\n");
            for (int i = 0; i < indentLevel; i++) {
                writer.writeCharacters("  ");
            }
            newLine = false;
        }
    }

    public void writeStartElement(String localName) throws XMLStreamException {
        writeIndent();
        writer.writeStartElement(localName);
        indentLevel++;
        newLine = true;
    }

    public void writeStartElement(String prefix, String localName, String namespaceURI) throws XMLStreamException {
        writeIndent();
        writer.writeStartElement(prefix, localName, namespaceURI);
        indentLevel++;
        newLine = true;
    }

    public void writeEndElement() throws XMLStreamException {
        indentLevel--;
        if (newLine) {
            writeIndent();
        }
        writer.writeEndElement();
        newLine = true;
    }

    public void writeAttribute(String localName, String value) throws XMLStreamException {
        try {
            writer.writeAttribute(localName, value);
        } catch (NullPointerException _) {
            throw new NullPointerException("localName: " + localName + ", value: " + value);
        }
    }

    public void writeAttribute(String prefix, String namespaceURI, String localName, String value) throws XMLStreamException {
        writer.writeAttribute(prefix, namespaceURI, localName, value);
    }

    public void writeDefaultNamespace(String namespaceURI) throws XMLStreamException {
        writer.writeDefaultNamespace(namespaceURI);
    }

    public void writeNamespace(String prefix, String namespaceURI) throws XMLStreamException {
        writer.writeNamespace(prefix, namespaceURI);
    }

    public void writeStartDocument(String encoding, String version) throws XMLStreamException {
        writer.writeStartDocument(encoding, version);
    }

    public void writeEndDocument() throws XMLStreamException {
        writer.writeEndDocument();
    }

    public void writeCharacters(String text) throws XMLStreamException {
        writer.writeCharacters(text);
        newLine = false;
    }


}
