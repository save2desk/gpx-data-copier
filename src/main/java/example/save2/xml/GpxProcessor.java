package example.save2.xml;

import example.save2.xml.dto.GpxPointDto;
import example.save2.xml.elements.GpxElement;

import java.util.List;

public interface GpxProcessor {

    List<GpxPointDto> readPoints() throws Exception;

    void mergePoints(List<GpxPointDto> pointsFrom) throws Exception;

    void saveGpxElementIntoFile() throws Exception;

    GpxElement createDefaultGpxElement(List<GpxPointDto> points) throws Exception;

    void simplifyGpx() throws Exception;

}
