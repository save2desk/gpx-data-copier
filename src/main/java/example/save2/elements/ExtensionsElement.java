package example.save2.elements;

import example.save2.GpxNamespaces;
import tools.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class ExtensionsElement {

  @JacksonXmlProperty(localName = "TrackPointExtension", namespace = GpxNamespaces.GPXTPX_NAMESPACE)
  public TrackPointExtensionElement trackPointExtension;

  @JacksonXmlProperty(localName = "osmand:speed")
  public String speed;

  public TrackPointExtensionElement getTrackPointExtension() {
    return trackPointExtension;
  }

  public String getSpeed() {
    return speed;
  }

}
