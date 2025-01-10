package org.word.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.URLEncoder;

@Controller
@Api(tags = "Download")
public class DownloadController {
    @Autowired
    private SpringTemplateEngine springTemplateEngine;

    /**
     * 下载 doc 文档
     *
     * @param response
     */
    @ApiOperation(value = "下载 doc 文档", notes = "", tags = {"Word"})
    @ApiResponses(value = {@ApiResponse(code = 200, message = "请求成功。")})
    @RequestMapping(value = "/downloadWord", method = {RequestMethod.GET})
    public void downloadWord(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 从会话中获取上一次请求的模型数据
        HttpSession session = request.getSession();
        Model model = (Model) session.getAttribute("model");

        if (model != null) {
            model.addAttribute("download", 0);
            writeContentToResponse(model, response);
        } else {
            response.sendRedirect("/");
        }
    }

    protected void writeContentToResponse(Model model, HttpServletResponse response) {
        Context context = new Context();
        context.setVariables(model.asMap());
        String content = springTemplateEngine.process("word", context);
        response.setContentType("application/octet-stream;charset=utf-8");
        response.setCharacterEncoding("utf-8");
        try (BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream())) {
            response.setHeader("Content-disposition", "attachment;filename=" + URLEncoder.encode(model.getAttribute("filename") + ".doc", "utf-8"));
            byte[] bytes = content.getBytes();
            bos.write(bytes, 0, bytes.length);
            bos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
