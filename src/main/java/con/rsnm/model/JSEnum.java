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
public class JSEnum {
    private final Class clazz;
    private final String name;
    private final String path;
    private final List<String> values = new ArrayList<String>();
    private final String fullName;
    private boolean isEnum;

    public JSEnum(Class cls) {
        this.clazz = cls;
        this.name = cls.getSimpleName();
        this.path = cls.getPackage().getName();
        this.fullName = cls.getName();
    }

    public void addEntry(String name) {
        this.values.add(name);
    }
}