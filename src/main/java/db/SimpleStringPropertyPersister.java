package db;


import com.j256.ormlite.field.FieldType;
import com.j256.ormlite.field.SqlType;
import com.j256.ormlite.field.types.StringType;
import javafx.beans.property.SimpleStringProperty;

///**
//* Created by will on 10/14/14.
//*/
public class SimpleStringPropertyPersister extends StringType {

    private static final SimpleStringPropertyPersister singleTon = new SimpleStringPropertyPersister();

    private SimpleStringPropertyPersister() {
        super(SqlType.STRING, new Class<?>[] { String.class });
    }

    public static SimpleStringPropertyPersister getSingleton() {
        return singleTon;
    }

    @Override
    public Object javaToSqlArg(FieldType fieldType, Object javaObject) {
        SimpleStringProperty s = (SimpleStringProperty) javaObject;
        if (s == null) {
            return null;
        } else {
            return s.get();
        }
    }

    @Override
    public Object sqlArgToJava(FieldType fieldType, Object sqlArg, int columnPos) {
        return new SimpleStringProperty((String)sqlArg);
    }

}
