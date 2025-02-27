package org.word.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.word.service.TableService;

import javax.validation.Valid;

@Controller
@Api(tags = "OpenAPI 3.0")
@Slf4j
public class OpenApiController extends ControllerBase {
    @Autowired
    @Override
    protected void setTableService(TableService openApiTableService) {
        super.setTableService(openApiTableService);
    }
    /**
     * 将 Open API 文档转换成 html 并预览，然后可以下载为 word 文档
     *
     * @param model
     * @param url   需要转换成 word 文档的资源地址
     * @param jsonFile 需要转化成 word 文档的json文件
     * @param jsonStr 需要转化成 word 文档的json字符串
     * @return
     */
    @ApiOperation(value = "将 Open API 文档转换成 html 并预览，然后可以下载为 word 文档", response = String.class, tags = {"Word"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求成功。", response = String.class)})
    @RequestMapping(value = "/openApiToWord", method = {RequestMethod.POST})
    public ResponseEntity<String> toWord(HttpServletRequest request, Model model,
                                        @ApiParam(value = "资源地址", required = false) @RequestParam(value = "url", required = false) String url,
                                        @ApiParam(value = "Open API json file", required = false) @Valid @RequestPart(value = "jsonFile", required = false) MultipartFile jsonFile,
                                        @ApiParam(value = "Open API json string", required = false) @Valid @RequestParam(value = "jsonStr", required = false) String jsonStr,
                                        @ApiParam(value = "是否下载", required = false) @RequestParam(value = "download", required = false, defaultValue = "1") Integer download) {
        return super.toWord(request, model, url, jsonFile, jsonStr, download);
    }
}