package example.save2.xml.elements;

import example.save2.xml.GpxNamespaces;
import tools.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import tools.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import tools.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "gpx")
public class GpxElement {

  @JacksonXmlProperty(isAttribute = true)
  public String version;

  @JacksonXmlProperty(isAttribute = true)
  public String creator;

  @JacksonXmlProperty(isAttribute = true)
  public String xmlns;

  @JacksonXmlProperty(isAttribute = true, namespace = "xmlns:xsi")
  public String xsiNamespace;

  @JacksonXmlProperty(isAttribute = true, namespace = "xmlns:gte")
  public String gteNamespace;

  @JacksonXmlProperty(isAttribute = true, namespace = "xmlns:gpxtpx")
  public String gpxtpxNamespace = GpxNamespaces.GPXTPX_NAMESPACE;

  @JacksonXmlProperty(isAttribute = true, namespace = "xmlns:gpxx")
  public String gpxxNamespace = GpxNamespaces.GPXX_NAMESPACE;

  @JacksonXmlProperty(isAttribute = true)
  public String targetNamespace;

  @JacksonXmlProperty(isAttribute = true)
  public String elementFormDefault;

  @JacksonXmlProperty(isAttribute = true, namespace = "xsi")
  public String schemaLocation;

  @JacksonXmlElementWrapper(useWrapping = false)
  public MetadataElement metadata;

  @JacksonXmlElementWrapper(useWrapping = false)
  public TrkElement trk;

  @JacksonXmlElementWrapper
  public String extensions;

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getCreator() {
    return creator;
  }

  public void setCreator(String creator) {
    this.creator = creator;
  }

  public TrkElement getTrk() {
    return trk;
  }

  public void setTrk(TrkElement trk) {
    this.trk = trk;
  }

  public MetadataElement getMetadata() {
    return metadata;
  }

  public void setMetadata(MetadataElement metadata) {
    this.metadata = metadata;
  }

}
