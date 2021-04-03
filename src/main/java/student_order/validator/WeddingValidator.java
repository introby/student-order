package student_order.validator;

import student_order.domain.wedding.AnswerWedding;
import student_order.domain.StudentOrder;

public class WeddingValidator {

    public AnswerWedding checkWedding(StudentOrder so) {
        System.out.println("Wedding запущен");

        return new AnswerWedding();

    }
}
