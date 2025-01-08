package org.word.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.word.model.Table;
import org.word.service.TableService;
import org.word.utils.JsonUtils;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public abstract class TableServiceBase implements TableService {
    @Autowired
    private RestTemplate restTemplate;
    @Override
    public Map<String, Object> tableList(String swaggerUrl) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            String jsonStr = restTemplate.getForObject(swaggerUrl, String.class);
            resultMap = tableListFromString(jsonStr);
        } catch (Exception e) {
            log.error("parse error", e);
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> tableList(MultipartFile jsonFile) {
        Map<String, Object> resultMap = new HashMap<>();
        try {
            String jsonStr = new String(jsonFile.getBytes());
            resultMap = tableListFromString(jsonStr);
        } catch (Exception e) {
            log.error("parse error", e);
        }
        return resultMap;
    }

    @Override
    public Map<String, Object> tableListFromString(String jsonStr) {
        Map<String, Object> resultMap = new HashMap<>();
        List<Table> result = new ArrayList<>();
        try {
            Map<String, Object> map = getResultFromString(result, jsonStr);
            Map<String, List<Table>> tableMap = result.stream().parallel().collect(Collectors.groupingBy(Table::getTitle));
            resultMap.put("tableMap", new TreeMap<>(tableMap));
            resultMap.put("info", map.get("info"));

            log.debug(JsonUtils.writeJsonStr(resultMap));
        } catch (Exception e) {
            log.error("parse error", e);
        }
        return resultMap;
    }

    abstract protected Map<String, Object> getResultFromString(List<Table> result, String jsonStr) throws IOException;
}
