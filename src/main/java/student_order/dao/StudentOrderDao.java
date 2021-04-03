package student_order.dao;

import student_order.domain.StudentOrder;
import student_order.exception.DaoException;

import java.util.List;

public interface StudentOrderDao {

    Long saveStudentOrder (StudentOrder studentOrder) throws DaoException;

    List<StudentOrder> getStudentOrders() throws DaoException;
}
