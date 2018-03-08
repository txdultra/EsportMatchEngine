package com.cj.engine.api.rest.transformers;

import com.cj.engine.model.MatchInfo;
import com.cj.engine.model.MatchPatternInfo;
import com.cj.engine.storage.IPatternStorage;
import com.cj.engine.transfomers.DocTransformer;
import com.cj.engine.api.rest.result.MatchDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>Create Time: 2018年03月02日</p>
 * <p>@author tangxd</p>
 **/
@Service
public class MatchDtoTransformer implements DocTransformer<MatchInfo, MatchDto> {

    @Autowired
    private IPatternStorage patternStorage;

    @Override
    public MatchDto transform(MatchInfo data) {
        MatchDto dto = new MatchDto();
        dto.setId(data.getId());
        dto.setTitle(data.getTitle());
        dto.setState(data.getState());
        dto.setProperties(data.getMap());
        List<MatchPatternInfo> patterns = patternStorage.gets(data.getId());
        dto.setPatternIds(patterns.stream().map(MatchPatternInfo::getId).collect(Collectors.toCollection(ArrayList::new)));
        return dto;
    }
}
