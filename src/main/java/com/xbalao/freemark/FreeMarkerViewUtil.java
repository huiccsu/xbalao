package com.xbalao.freemark;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.support.RequestContextUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class FreeMarkerViewUtil extends FreeMarkerView {
	public static final String CREATE_HTML="create_html";
    @Override
    protected void doRender(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
            throws Exception {
        // Expose model to JSP tags (as request attributes).
        exposeModelAsRequestAttributes(model, request);
        // Expose all standard FreeMarker hash models.
        SimpleHash fmModel = buildTemplateModel(model, request, response);

        if (logger.isDebugEnabled()) {
            logger.debug("Rendering FreeMarker 模版 [" + getUrl() + "] in FreeMarkerView '" + getBeanName() + "'");
        }
        // Grab the locale-specific version of the template.
        Locale locale = RequestContextUtils.getLocale(request);

        /*
         * 默认生成静态文件,除非在编写ModelAndView时指定CREATE_HTML = false, 这样对静态文件生成的粒度控制更细一点
         * 例如：ModelAndView mav = new ModelAndView("search");
         * mav.addObject(Const.CREATE_HTML, false);
         */
        if (Boolean.FALSE.equals(model.get(CREATE_HTML))) {
            processTemplate(getTemplate(locale), fmModel, response);
        } else {
        	String htmlPath=(String)model.get("htmlPath");
            createHTML(getTemplate(locale), fmModel,htmlPath, request, response);
            processTemplate(getTemplate(locale), fmModel, response);
        }
    }

    public void createHTML(Template template, SimpleHash model,String htmlPath, HttpServletRequest request,
            HttpServletResponse response) throws IOException, TemplateException, ServletException {
        // 站点根目录的绝对路径
        // 静态页面绝对路径
        System.out.println("静态页面绝对路径===========>>:"+htmlPath);
        File htmlFile = new File(htmlPath);
        if (!htmlFile.getParentFile().exists()) {
            htmlFile.getParentFile().mkdirs();
        }
        if (!htmlFile.exists()) {
            htmlFile.createNewFile();
        }
        Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(htmlFile), "UTF-8"));
        // 处理模版
        template.process(model, out);
        out.flush();
        out.close();
        /* 将请求转发到生成的htm文件 */
        //request.getRequestDispatcher(htmlPath).forward(request, response);
    }

}