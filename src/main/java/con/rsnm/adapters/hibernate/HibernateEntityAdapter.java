package con.rsnm.adapters.hibernate;

import com.fasterxml.jackson.annotation.JsonIgnore;
import con.rsnm.adapters.AbstractAdapter;
import con.rsnm.model.JSClass;
import con.rsnm.model.JSClassProperty;
import con.rsnm.model.JSEnum;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.lang.reflect.Field;
import java.util.Set;

/**
 * Created by Wael on 27/11/15.
 */
public class HibernateEntityAdapter extends AbstractAdapter {
    @Override
    public void parse(Reflections reflections) {
        Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(Entity.class);

        for(Class cls : annotated) {
            if(!cls.isEnum()) {
                this.parseEntity(cls);
            } else {
                this.parseEnum(cls);
            }
        }

        generate();
    }

    private JSEnum parseEnum(Class cls) {
        JSEnum jsEnum = new JSEnum(cls);

        Object[] enumValues = cls.getEnumConstants();

        for(Object enumValue : enumValues) {
            jsEnum.addEntry(enumValue.toString());
        }

        addEnumClass(jsEnum);

        return jsEnum;
    }

    private void parseEntity(Class cls) {
        if(isMapped(cls.getName())) {
            return;
        }

        JSClass jsClass = toJsClass(cls);
        Set<Field> fields = findFields(cls, ManyToMany.class);
        parseFields(jsClass, fields, true);
        fields = findFields(cls, Column.class);
        parseFields(jsClass, fields, false);

        addClass(jsClass);
    }

    private void parseFields(JSClass jsClass, Set<Field> fields, boolean array) {
        for (Field field : fields) {
            if(field.getAnnotation(JsonIgnore.class) == null) {
                JSClassProperty prop = jsClass.addField(field, array);

                if(prop != null && prop.isArray()) {
                    jsClass.addImport(prop.getGenericType());
                }

                if(field.isEnumConstant() || field.getType().isEnum()) {
                    JSEnum enumCls = parseEnum(field.getType());
                    jsClass.addImport(enumCls.getName());

                    if(isMapped(field.getType().getClass().getName())) {
                        continue;
                    }
                }
            }
        }
    }

    private JSClass toJsClass(Class cls) {
        JSClass jsClass = new JSClass(cls);

        return jsClass;
    }
}
