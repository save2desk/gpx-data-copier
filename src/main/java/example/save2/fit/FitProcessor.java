package example.save2.fit;

import example.save2.xml.dto.GpxPointDto;

import java.util.List;

public interface FitProcessor {

    List<GpxPointDto> readPoints() throws Exception;

}
