package con.rsnm.adapters;

import con.rsnm.model.JSClass;
import org.reflections.Reflections;

import java.util.List;
import java.util.Set;

/**
 * Created by Wael on 27/11/15.
 */
public interface IAdapter {
    void parse(Reflections reflections);
    public List<JSClass> getClassList();

}
