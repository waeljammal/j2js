package con.rsnm.model;

import lombok.Getter;

import java.lang.reflect.Field;

/**
 * Created by Wael on 27/11/15.
 */
@Getter
public class JSClassProperty {
    private final String name;

    public JSClassProperty(Field field) {
        this.name = field.getName();
    }
}
