package con.rsnm.model;

import lombok.Getter;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by Wael on 27/11/15.
 */
@Getter
public class JSClassProperty {
    private String name;
    private boolean isArray = false;
    private Class<?> type;
    private String jsType;
    private String fieldType;
    private String genericType;

    public JSClassProperty(Field field, boolean isArray) {
        this.name = field.getName();
        this.type = field.getType();
        this.jsType = "";
        this.type = null;
        this.isArray = isArray;
        processField(field);
    }

    public JSClassProperty(Field field) {
        this.name = field.getName();
        this.type = field.getType();
        this.jsType = "";
        this.type = null;

        processField(field);
    }

    private void processField(Field field) {
        Class c = field.getType();

        // Register string types based on primitive known type conversions
        if (c.isPrimitive()) {
            if (c.equals(Integer.class) || c.equals(Integer.TYPE)) {
                this.jsType = getFieldType("number");
            } else if (c.equals(Boolean.class) || c.equals(Boolean.TYPE)) {
                this.jsType = getFieldType("boolean");
            }
        } else if (c.isEnum()) {
            this.jsType = getFieldType(c.getSimpleName());
        } else if (c.equals(String.class) || c.getName() == "java.lang.String") {
            this.jsType = getFieldType("string");
        } else if(isArray) {
            ParameterizedType paramType = (ParameterizedType)field.getGenericType();
            Type typeArgument = paramType.getActualTypeArguments()[0];
            String[] parts = typeArgument.getTypeName().split("\\.");
            this.jsType = getFieldType(parts[parts.length - 1]);
            this.genericType = parts[parts.length - 1];
        } else {
            this.jsType = getFieldType(c.getSimpleName());
        }
    }

    private String asType(String name) {
        return ":" + name;
    }

    private String asArray(String name) {
        return ":Array<" + name + ">";
    }

    public JSClassProperty(String name) {
        this.name = name;
    }

    public String getFieldType(String name) {
        return this.isArray ? asArray(name) : asType(name);
    }
}
