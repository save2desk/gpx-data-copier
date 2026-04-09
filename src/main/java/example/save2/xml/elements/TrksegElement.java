package example.save2.xml.elements;

import tools.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

import java.util.List;

public class TrksegElement {

  @JacksonXmlElementWrapper(localName = "trkpt", useWrapping = false)
  public List<TrkptElement> trkpt;

  public List<TrkptElement> getTrkpt() {
    return trkpt;
  }

}
