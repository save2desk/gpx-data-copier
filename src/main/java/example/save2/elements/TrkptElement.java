package example.save2.elements;

import com.fasterxml.jackson.annotation.JsonInclude;
import tools.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import tools.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class TrkptElement {

  @JacksonXmlProperty(isAttribute = true)
  public String lat;

  @JacksonXmlProperty(isAttribute = true)
  public String lon;

  @JacksonXmlElementWrapper
  public EleElement ele;

  @JacksonXmlElementWrapper
  public TimeElement time;

  @JacksonXmlElementWrapper
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public HdopElement hdop;

  @JacksonXmlElementWrapper
  public ExtensionsElement extensions;

  public String getLat() {
    return lat;
  }

  public String getLon() {
    return lon;
  }

  public EleElement getEle() {
    return ele;
  }

  public TimeElement getTime() {
    return time;
  }

  public HdopElement getHdop() {
    return hdop;
  }

  public ExtensionsElement getExtensions() {
    return extensions;
  }
}
