package con.rsnm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Wael on 27/11/15.
 */
@Getter
public class JSClass {
    private final Class clazz;
    private final String name;
    private final String path;
    private final List<JSClassProperty> fields = new ArrayList<JSClassProperty>();
    private final String fullName;
    private boolean isEnum;

    public JSClass(Class cls) {
        this.clazz = cls;
        this.name = cls.getSimpleName();
        this.path = cls.getPackage().getName();
        this.fullName = cls.getName();
    }

    public void addField(Field field) {
        if(field.getAnnotation(JsonIgnore.class) == null) {
            JSClassProperty newField = new JSClassProperty(field);
            this.fields.add(newField);
        }
    }

    public void setIsEnum(boolean isEnum) {
        this.isEnum = isEnum;
    }

    public boolean getIsEnum() {
        return this.isEnum;
    }
}