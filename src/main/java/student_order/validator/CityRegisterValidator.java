package student_order.validator;

import student_order.domain.Person;
import student_order.domain.register.AnswerCityRegister;
import student_order.domain.Child;
import student_order.domain.register.AnswerCityRegisterItem;
import student_order.domain.register.CityRegisterResponse;
import student_order.domain.StudentOrder;
import student_order.exception.CityRegisterException;
import student_order.exception.TransportException;
import student_order.validator.register.CityRegisterChecker;
import student_order.validator.register.FakeCityRegisterChecker;

import java.util.List;

public class CityRegisterValidator {
    public static final String IN_CODE = "NO_GRN";

    private CityRegisterChecker personChecker;

    public CityRegisterValidator() {
        personChecker = new FakeCityRegisterChecker();
    }

    public AnswerCityRegister checkCityRegister(StudentOrder so) {
        AnswerCityRegister answerCityRegister = new AnswerCityRegister();

        answerCityRegister.addItem(checkPerson(so.getHusband()));
        answerCityRegister.addItem(checkPerson(so.getWife()));

        for (Child child : so.getChildren()) {
            answerCityRegister.addItem(checkPerson(child));
        }

        return answerCityRegister;
    }

    private AnswerCityRegisterItem checkPerson(Person person) {

        AnswerCityRegisterItem.CityStatus status;
        AnswerCityRegisterItem.CityError error = null;

        try {
            CityRegisterResponse tmp = personChecker.checkPerson(person);
            status = tmp.isExisting() ? AnswerCityRegisterItem.CityStatus.YES :
                    AnswerCityRegisterItem.CityStatus.NO;
        } catch (CityRegisterException ex) {
            ex.printStackTrace();
            status = AnswerCityRegisterItem.CityStatus.ERROR;
            error = new AnswerCityRegisterItem.CityError(ex.getCode(), ex.getMessage());
        } catch (TransportException ex) {
            ex.printStackTrace();
            status = AnswerCityRegisterItem.CityStatus.ERROR;
            error = new AnswerCityRegisterItem.CityError(IN_CODE, ex.getMessage());


        }

        AnswerCityRegisterItem acri = new AnswerCityRegisterItem(status, person, error);

        return acri;
    }
}
