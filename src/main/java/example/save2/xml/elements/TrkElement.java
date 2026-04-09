package example.save2.xml.elements;

import tools.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import tools.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public class TrkElement {

  @JacksonXmlProperty(isAttribute = true)
  public String name;

  @JacksonXmlProperty(isAttribute = true)
  public String type;

  @JacksonXmlElementWrapper(localName = "trkseg", useWrapping = false)
  public List<TrksegElement> trkseg;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public List<TrksegElement> getTrkseg() {
    return trkseg;
  }

  public void setTrkseg(List<TrksegElement> trkseg) {
    this.trkseg = trkseg;
  }
}
