package student_order.validator;

import student_order.domain.children.AnswerChildren;
import student_order.domain.StudentOrder;

public class ChildrenValidator {

    public AnswerChildren checkChildren(StudentOrder so) {
        System.out.println("Children check is running");

        return new AnswerChildren();

    }
}
