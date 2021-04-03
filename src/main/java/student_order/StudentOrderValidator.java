package student_order;

import student_order.domain.*;
import student_order.domain.children.AnswerChildren;
import student_order.domain.register.AnswerCityRegister;
import student_order.domain.student.AnswerStudent;
import student_order.domain.wedding.AnswerWedding;
import student_order.mail.MailSender;
import student_order.validator.ChildrenValidator;
import student_order.validator.CityRegisterValidator;
import student_order.validator.StudentValidator;
import student_order.validator.WeddingValidator;

import java.util.LinkedList;
import java.util.List;

public class StudentOrderValidator {

    private CityRegisterValidator cityRegisterValidator;
    private WeddingValidator weddingValidator;
    private ChildrenValidator childrenValidator;
    private StudentValidator studentValidator;
    private MailSender mailSender;

    public StudentOrderValidator() {

        cityRegisterValidator = new CityRegisterValidator();
        weddingValidator = new WeddingValidator();
        childrenValidator = new ChildrenValidator();
        studentValidator = new StudentValidator();
        mailSender = new MailSender();

    }

    public static void main(String[] args) {
        StudentOrderValidator studentOrderValidator = new StudentOrderValidator();
        studentOrderValidator.checkAll();

    }

    public void checkAll() {

        List<StudentOrder> soList = readStudentOrders();

        for (StudentOrder studentOrder : soList) {
            checkOneOrder(studentOrder);

        }

    }

    static List<StudentOrder> readStudentOrders() {
        List<StudentOrder> studentOrdersList = new LinkedList<>();

        for (int i = 0; i < 5; i++) {
            StudentOrder so = SaveStudentOrder.buildStudentOrder(i);
            studentOrdersList.add(so);

        }

        return studentOrdersList;
    }

    public void checkOneOrder(StudentOrder so) {

        AnswerCityRegister cityAnswer = checkCityRegister(so);

//        AnswerWedding weddingAnswer = checkWedding(so);
//        AnswerChildren childrenAnswer = checkChildren(so);
//        AnswerStudent studentAnswer = checkStudent(so);
//
//        sendMail(so);

    }

    public AnswerCityRegister checkCityRegister(StudentOrder so) {
        return cityRegisterValidator.checkCityRegister(so);

    }

    public AnswerWedding checkWedding(StudentOrder so) {
        return weddingValidator.checkWedding(so);

    }

    public AnswerChildren checkChildren(StudentOrder so) {
        return childrenValidator.checkChildren(so);

    }

    public AnswerStudent checkStudent(StudentOrder so) {
        return studentValidator.checkStudent(so);

    }

    public void sendMail(StudentOrder so) {
        mailSender.sendMail(so);
    }

}
