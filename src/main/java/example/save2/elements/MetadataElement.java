package example.save2.elements;

import tools.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;

public class MetadataElement {

  @JacksonXmlElementWrapper
  public String name;

  @JacksonXmlElementWrapper
  public String time;

  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }
}
