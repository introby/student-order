package student_order.validator;

import student_order.domain.student.AnswerStudent;
import student_order.domain.StudentOrder;

public class StudentValidator {

    public AnswerStudent checkStudent(StudentOrder so) {
        System.out.println("Студенты проверяются");

        return new AnswerStudent();

    }
}
