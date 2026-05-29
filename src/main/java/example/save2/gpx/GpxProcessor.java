package example.save2.gpx;

import example.save2.gpx.dto.GpxPointDto;
import example.save2.gpx.elements.GpxElement;

import java.util.List;

public interface GpxProcessor {

    GpxElement readGpxElement(String pathString);

    List<GpxPointDto> readPoints(GpxElement gpxElement) throws Exception;

    void mergePoints(List<GpxPointDto> points, GpxElement gpxElementTo) throws Exception;

    GpxElement createDefaultGpxElement(List<GpxPointDto> points) throws Exception;

    void simplifyGpx(String pathString) throws Exception;

    void saveGpxElementIntoFile(GpxElement gpxElement, String pathString) throws Exception;

}
