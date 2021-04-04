package student_order.dao;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class SimpleRunner {

    public static void main(String[] args) {
        SimpleRunner sr = new SimpleRunner();

        sr.runTests();
    }

    private void runTests() {

        try {
            Class cl = Class.forName("student_order.dao.DictionaryDaoImplTest");

            Constructor cst = cl.getConstructor();
            Object entity = cst.newInstance();

            final Method[] methods = cl.getMethods();
            for (Method method : methods) {
                final Test ann = method.getAnnotation(Test.class);
                if (ann != null) {
                    method.invoke(entity);
                }

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }
}
