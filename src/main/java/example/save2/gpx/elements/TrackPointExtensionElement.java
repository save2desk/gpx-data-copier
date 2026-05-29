package example.save2.gpx.elements;

import com.fasterxml.jackson.annotation.JsonInclude;
import example.save2.gpx.GpxNamespaces;
import tools.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class TrackPointExtensionElement {

  @JacksonXmlProperty(localName = "atemp", namespace = GpxNamespaces.GPXTPX_NAMESPACE)
  public AtempElement atemp;

  @JacksonXmlProperty(localName = "hr", namespace = GpxNamespaces.GPXTPX_NAMESPACE)
  @JsonInclude(JsonInclude.Include.NON_NULL)
  public HrElement hr;

  public AtempElement getAtemp() {
    return atemp;
  }

  public HrElement getHr() {
    return hr;
  }
}
