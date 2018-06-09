package com.xbalao.freemark;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.servlet.ServletException;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class CreateHtmlFreeMark{

    public static void createHTMLforNeed (FreeMarkerConfigurer freeMarkerConfigurer,ModelAndView model) throws Exception, TemplateException, ServletException {
        //获取模板
        Template template = getTemple(freeMarkerConfigurer,model.getViewName());
        // 站点根目录的绝对路径
        // 静态页面绝对路径
        String htmlPath = (String)model.getModelMap().get("htmlPath");
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
        template.process(model.getModel(), out);
        out.flush();
        out.close();
    }

    /**
     *
     * @param freeMarkerConfigurer
     * @param templeAddress
     * @return
     */
    public static Template getTemple(FreeMarkerConfigurer freeMarkerConfigurer,String templeAddress)throws Exception{
        // 1、从FreeMarkerConfigurer对象中获得Configuration对象。
        Configuration configuration = freeMarkerConfigurer.getConfiguration();
        // 2、使用Configuration对象获得Template对象。
        return configuration.getTemplate(templeAddress);
    }
}