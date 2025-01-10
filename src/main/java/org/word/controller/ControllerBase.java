package org.word.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.word.service.TableService;

import java.util.Locale;
import java.util.Map;

@Slf4j
public abstract class ControllerBase {
    @Value("${swagger.url}")
    private String swaggerUrl;

    @Autowired
    protected void setTableService(TableService tableService) {
        this.tableService = tableService;
    }

    protected TableService tableService;

    protected String toWord(HttpServletRequest request, Model model, String url, MultipartFile jsonFile, String jsonStr, Integer download) {
        log.info("to word {}, {}, {}, {}", url, jsonFile, jsonStr, download);
        if (url!=null&&!url.isEmpty()){
            generateModelDataFromUrl(model, url, download);
        } else if (jsonFile!=null&&!jsonFile.isEmpty()){
            generateModelDataFromFile(model, jsonFile, download);
        } else if (jsonStr!=null&&!jsonStr.isEmpty()){
            generateModelDataFromStr(model, jsonStr, download);
        } else {
            generateModelDataFromUrl(model, "", download);
        }

        // 将模型数据存储在会话中
        HttpSession session = request.getSession();
        session.setAttribute("model", model);

        // 是否有本地化的模板
        Locale currentLocale = Locale.getDefault();
        String localizedTemplate = "word-" + currentLocale.getLanguage() + "_" + currentLocale.getCountry();
        String fileName = "/templates/" + localizedTemplate + ".html";

        if (getClass().getResourceAsStream(fileName) != null) {
            log.info(fileName + " resource found");
            return localizedTemplate;
        } else {
            log.info(fileName + " resource not found, using default");
        }
        return "word";
    }

    private String outputFilename(String filename){
        if (filename != null) {
            return filename.replaceAll(".json", "");
        }
        return "toWord";
    }

    protected void generateModelDataFromUrl(Model model, String url, Integer download) {
        url = StringUtils.defaultIfBlank(url, swaggerUrl);
        Map<String, Object> result = tableService.tableList(url);
        String filename = url.substring(url.lastIndexOf("/"));
        model.addAttribute("download", download);
        model.addAttribute("filename", outputFilename(filename));
        model.addAllAttributes(result);
    }
    protected void generateModelDataFromStr(Model model, String jsonStr, Integer download) {
        Map<String, Object> result = tableService.tableListFromString(jsonStr);
        model.addAttribute("download", download);
        model.addAttribute("filename", outputFilename(null));
        model.addAllAttributes(result);
    }
    protected void generateModelDataFromFile(Model model, MultipartFile jsonFile, Integer download) {
        Map<String, Object> result = tableService.tableList(jsonFile);
        String filename = jsonFile.getOriginalFilename();
        model.addAttribute("download", download);
        model.addAttribute("filename", outputFilename(filename));
        model.addAllAttributes(result);
    }
}
