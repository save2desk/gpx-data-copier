package example.save2.xml;

import example.save2.dto.GpxPointDto;

import java.text.ParseException;
import java.util.List;

public interface XmlProcessor {

    List<GpxPointDto> readPoints() throws ParseException;

    void mergePoints(List<GpxPointDto> pointsFrom) throws Exception;

    void savePoints() throws Exception;

}
