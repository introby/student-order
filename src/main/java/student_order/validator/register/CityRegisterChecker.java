package student_order.validator.register;

import student_order.domain.register.CityRegisterResponse;
import student_order.domain.Person;
import student_order.exception.CityRegisterException;
import student_order.exception.TransportException;

public interface CityRegisterChecker {

    CityRegisterResponse checkPerson(Person person) throws CityRegisterException, TransportException;

}
