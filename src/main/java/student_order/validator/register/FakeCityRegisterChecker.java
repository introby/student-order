package student_order.validator.register;

import student_order.domain.Adult;
import student_order.domain.Child;
import student_order.domain.register.CityRegisterResponse;
import student_order.domain.Person;
import student_order.exception.CityRegisterException;
import student_order.exception.TransportException;

public class FakeCityRegisterChecker implements CityRegisterChecker {

    private static final String GOOD_1 = "1000";
    private static final String GOOD_2 = "2000";
    private static final String BAD_1 = "1001";
    private static final String BAD_2 = "2001";
    private static final String ERROR_1 = "1002";
    private static final String ERROR_2 = "2002";
    private static final String ERROR_T1 = "1003";
    private static final String ERROR_T2 = "2003";

    public CityRegisterResponse checkPerson(Person person) throws CityRegisterException, TransportException {

        CityRegisterResponse res = new CityRegisterResponse();

        if (person instanceof Adult) {
            Adult t = (Adult) person;
            String ps = t.getPassportSeries();


            if (ps.equals(GOOD_1) || ps.equals(GOOD_2)) {
                res.setExisting(true);
                res.setTemporal(false);
            }

            if (ps.equals(BAD_1) || ps.equals(BAD_2)) {
                res.setExisting(false);
            }

            if (ps.equals(ERROR_1) || ps.equals(ERROR_2)) {
                CityRegisterException ex = new CityRegisterException("1", "GRN ERROR" + ps);
                throw ex;
            }

            if (ps.equals(ERROR_T1) || ps.equals(ERROR_T2)) {
                TransportException ex = new TransportException("Transport ERROR" + ps);
                throw ex;
            }
        }

        if (person instanceof Child) {
            res.setExisting(true);
            res.setTemporal(true);
        }

        System.out.println(res);

        return res;
    }
}
