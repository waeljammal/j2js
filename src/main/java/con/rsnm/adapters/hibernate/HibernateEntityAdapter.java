package con.rsnm.adapters.hibernate;

import con.rsnm.adapters.AbstractAdapter;
import con.rsnm.model.JSClass;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;

import javax.persistence.Column;
import javax.persistence.Entity;
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
            this.parseEntity(cls);
        }

        generate();
    }

    private void parseEntity(Class cls) {
        if(isMapped(cls.getName())) {
            return;
        }

        JSClass jsClass = toJsClass(cls);
        Reflections ref = new Reflections(cls, new FieldAnnotationsScanner());
        Set<Field> fields = ref.getFieldsAnnotatedWith(Column.class);

        for (Field field : fields) {
            jsClass.addField(field);

            if(field.isEnumConstant() || field.getType().isEnum()) {
                if(isMapped(field.getType().getClass().getName())) {
                    continue;
                }

                JSClass jsEnum = new JSClass(field.getType());
                jsEnum.setIsEnum(true);

                addClass(jsEnum);
            }
        }

        addClass(jsClass);
    }

    private JSClass toJsClass(Class cls) {
        JSClass jsClass = new JSClass(cls);

        return jsClass;
    }
}
