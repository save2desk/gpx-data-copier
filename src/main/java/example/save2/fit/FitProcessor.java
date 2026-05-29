package example.save2.fit;

import example.save2.gpx.dto.GpxPointDto;

import java.util.List;

public interface FitProcessor {

    List<GpxPointDto> readPoints(String pathString) throws Exception;

}
