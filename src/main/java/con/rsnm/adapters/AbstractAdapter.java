package con.rsnm.adapters;

import con.rsnm.model.JSClass;
import con.rsnm.model.JSEnum;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.reflections.Reflections;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Created by Wael on 27/11/15.
 */
public abstract class AbstractAdapter implements IAdapter {
    private List<JSClass> classList = new ArrayList<JSClass>();
    private List<JSEnum> enumClassList = new ArrayList<JSEnum>();
    private Map<String, Boolean> mappedClassList = new HashMap<String, Boolean>();

    public List<JSClass> getClassList() {
        return this.classList;
    }

    public abstract void parse(Reflections reflections);

    protected void addClass(JSClass jsClass) {
        String key = jsClass.getFullName();

        if(!isMapped(key)) {
            mappedClassList.put(key, true);
            classList.add(jsClass);
        }
    }

    /**
     * @return null safe set
     */
    protected Set<Field> findFields(Class<?> classs, Class<? extends Annotation> ann) {
        Set<Field> set = new HashSet<>();
        Class<?> c = classs;
        while (c != null) {
            for (Field field : c.getDeclaredFields()) {
                if (field.isAnnotationPresent(ann)) {
                    set.add(field);
                }
            }
            c = c.getSuperclass();
        }
        return set;
    }

    protected void addEnumClass(JSEnum jsEnum) {
        String key = jsEnum.getFullName();

        if(!isMapped(key)) {
            mappedClassList.put(key, true);
            enumClassList.add(jsEnum);
        }
    }

    protected boolean isMapped(String fullName) {
        return mappedClassList.containsKey(fullName);
    }

    protected void generate() {
        System.out.println("Generating templates");

        for(JSClass jsClass : classList) {
            this.processClassTemplate(jsClass, jsClass.getName(), "js-entity-class.template");
        }

        for(JSEnum enumClass : enumClassList) {
            this.processClassTemplate(enumClass, enumClass.getName(), "js-enum-class.template");
        }
    }

    private void processClassTemplate(Object data, String fileName, String templateName) {
        URL templatePath = AbstractAdapter.class.getClassLoader().getResource("js-entity-class.template");
        VelocityEngine ve = new VelocityEngine();
        Properties props = new Properties();
        String path = new File(templatePath.getFile()).getParent();
        props.put("file.resource.loader.path", path);
        ve.init(props);
        Template template = ve.getTemplate(templateName);
        VelocityContext context = new VelocityContext();
        context.put("class", data);
        StringWriter writer = new StringWriter();
        template.merge(context, writer);
        Writer fileWriter = null;

        try {
            if(!Files.exists(Paths.get("target/generated"))) {
                Files.createDirectories(Paths.get("target/generated"));
            }
            Path outputPath = Paths.get("./target", "generated", fileName + ".js");
            fileWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputPath.toString()), "utf-8"));
            fileWriter.write(writer.toString());
        } catch (IOException ex) {
            // report
        } finally {
            try {fileWriter.close();} catch (Exception ex) {/*ignore*/}

            context.remove("class");
        }
    }
}
