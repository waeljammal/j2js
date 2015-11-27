package con.rsnm.adapters;

import con.rsnm.model.JSClass;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.reflections.Reflections;

import java.io.File;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by Wael on 27/11/15.
 */
public abstract class AbstractAdapter implements IAdapter {
    private List<JSClass> classList = new ArrayList<JSClass>();
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

    protected boolean isMapped(String fullName) {
        return mappedClassList.containsKey(fullName);
    }

    protected void generate() {
        System.out.println("Generating templates");

        for(JSClass jsClass : classList) {
            URL templatePath = AbstractAdapter.class.getClassLoader().getResource("js-entity-class.template");
            VelocityEngine ve = new VelocityEngine();
            Properties props = new Properties();
            String path = new File(templatePath.getFile()).getParent();
            props.put("file.resource.loader.path", path);
            ve.init(props);
            Template template = ve.getTemplate("js-entity-class.template");
            VelocityContext context = new VelocityContext();
            context.put("class", jsClass);
            StringWriter writer = new StringWriter();
            template.merge(context, writer);
            System.out.println(writer.toString());
        }
    }
}
