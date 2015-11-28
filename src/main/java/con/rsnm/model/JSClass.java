package con.rsnm.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by Wael on 27/11/15.
 */
@Getter
public class JSClass {
    private final Class clazz;
    private final String name;
    private final String path;
    private final List<JSClassProperty> fields = new ArrayList<JSClassProperty>();
    private final HashSet<String> mappedFields = new HashSet<String>();
    private final String fullName;
    private boolean isEnum;
    private final HashSet<String> imports = new HashSet();

    public JSClass(Class cls) {
        this.clazz = cls;
        this.name = cls.getSimpleName();
        this.path = cls.getPackage().getName();
        this.fullName = cls.getName();
    }

    public JSClassProperty addField(Field field, boolean asArray) {
        if(!mappedFields.contains(field.getName())) {
            JSClassProperty newField = new JSClassProperty(field, asArray);
            this.fields.add(newField);
            mappedFields.add(field.getName());

            return newField;
        }

        return null;
    }

    public void addField(String name) {
        JSClassProperty newField = new JSClassProperty(name);
        this.fields.add(newField);
    }

    public void addImport(String name) {
        if(!this.imports.contains(name)) {
            this.imports.add(name);
        }
    }

    public void setIsEnum(boolean isEnum) {
        this.isEnum = isEnum;
    }

    public boolean getIsEnum() {
        return this.isEnum;
    }
}