package com.cucund.security.common.plugin;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.ClassPathResource;

import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.Template;

public class FtlTranslationService {
    public static final String UTF_8 = "UTF-8";
    private static Log logger = LogFactory.getLog(FtlTranslationService.class);
    private Configuration conf = new Configuration();

    /**
     * rootObj.put("enums", BeansWrapper.getDefaultInstance().getEnumModels());
     * rootObj.put("static",
     * BeansWrapper.getDefaultInstance().getStaticModels());
     */
    public void init() throws Exception {
        conf.setOutputEncoding(UTF_8);
        conf.setDefaultEncoding(UTF_8);
        conf.setClassForTemplateLoading(FtlTranslationService.class, "/freemarker/");
        try {
            ClassPathResource res = new ClassPathResource("freemarker/prop/freemarker.properties");
            conf.setSettings(res.getInputStream());
        } catch (Exception ex) {
            logger.warn("freemarker setting file error!  please check /META-INF/freemarker/prop/freemarker.properties!");
        }
    }

    public void translate(Object data, String tplName, String output) throws Exception {
        Map<String, Object> rootObj = new HashMap<String, Object>(3);
        rootObj.put("enums", BeansWrapper.getDefaultInstance().getEnumModels());
        rootObj.put("static", BeansWrapper.getDefaultInstance().getStaticModels());
        rootObj.put("data", data);
        Template t = conf.getTemplate(tplName, UTF_8);
        File outputFile = new File(output);
        outputFile.getParentFile().mkdirs();
        Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), StandardCharsets.UTF_8));
		
        // StringWriter writer = new StringWriter();
        t.process(rootObj, writer);
        // String html = writer.getBuffer().toString();
        rootObj.clear();
        // return html;
    }

    public void translateByString(Object data, String templateStr, String output) throws Exception {
        Map<String, Object> rootObj = new HashMap<String, Object>(3);
        rootObj.put("enums", BeansWrapper.getDefaultInstance().getEnumModels());
        rootObj.put("static", BeansWrapper.getDefaultInstance().getStaticModels());
        rootObj.put("data", data);

        Template template = new Template("template", new StringReader(templateStr), conf);
        template.setEncoding(UTF_8);
        File outputFile = new File(output);
        outputFile.getParentFile().mkdirs();
        Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile), "UTF-8"));
        template.process(rootObj, writer);
        
        rootObj.clear();
    }
    
    public String returnTranslateByString(Object data, String templateStr) throws Exception {
        Map<String, Object> rootObj = new HashMap<String, Object>(3);
        rootObj.put("enums", BeansWrapper.getDefaultInstance().getEnumModels());
        rootObj.put("static", BeansWrapper.getDefaultInstance().getStaticModels());
        rootObj.put("data", data);
        Template template = new Template("template", new StringReader(templateStr), conf);
        template.setEncoding(UTF_8);
        Writer writer = new StringWriter();
        template.process(rootObj, writer);
        rootObj.clear();
        return writer.toString();
    }
}
