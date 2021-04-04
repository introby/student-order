package student_order.dao;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import student_order.domain.*;
import student_order.exception.DaoException;

import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

public class StudentOrderDaoImplTest {

    @BeforeClass
    public static void startUp() throws Exception {

        DBInit.startUp();
    }

    @Test
    public void saveStudentOrder() throws DaoException {

        StudentOrder so = buildStudentOrder(10);
        Long id = new StudentOrderDaoImpl().saveStudentOrder(so);
    }

    @Test(expected = DaoException.class)
    public void saveStudentOrderError() throws DaoException {

            StudentOrder so = buildStudentOrder(10);
            so.getHusband().setSurName(null);
            Long id = new StudentOrderDaoImpl().saveStudentOrder(so);

    }

    @Test
    public void getStudentOrders() throws DaoException {

        List<StudentOrder> list = new StudentOrderDaoImpl().getStudentOrders();
    }

    public StudentOrder buildStudentOrder(long id) {
        StudentOrder so = new StudentOrder();
        so.setStudentOrderID(id);
        so.setMarriageCertificateId("" + (123456000 + id));
        so.setMarriageDate(LocalDate.of(2016, 7, 4));
        RegisterOffice ro = new RegisterOffice(1L, "", "");
        so.setMarriageOffice(ro);

        Street street = new Street(1L, "First street");

        Address address = new Address("19500", street, "12", "", "142");

        Adult husband = new Adult("Petrov", "Petr", "Petrovich", LocalDate.of(1997, 6, 7));
        husband.setPassportSeries("" + (1000 + id));
        husband.setPassportNumber("" + (100000 + id));
        husband.setPassportIssueDate(LocalDate.of(2017, 9, 15));
        PassportOffice po1 = new PassportOffice(1L, "", "");
        husband.setIssueDepartment(po1);
        husband.setStudentID("" + (100000 + id));
        husband.setAddress(address);
        husband.setUniversity(new University(2L, ""));
        husband.setStudentID("HH12345");

        Adult wife = new Adult("Petrova", "Marina", "Aleksandrovna", LocalDate.of(1995, 1, 17));
        wife.setPassportSeries("" + (2000 + id));
        wife.setPassportNumber("" + (200000 + id));
        wife.setPassportIssueDate(LocalDate.of(2018, 10, 2));
        PassportOffice po2 = new PassportOffice(2L, "", "");
        wife.setIssueDepartment(po2);
        wife.setStudentID("" + (200000 + id));
        wife.setAddress(address);
        wife.setUniversity(new University(3L, ""));
        wife.setStudentID("WW12345");

        Child child1 = new Child("Petrova", "Nadezhda", "Petrovna", LocalDate.of(2018, 11, 24));
        child1.setCertificateNumber("" + (300000 + id));
        child1.setCertificateIssueDate(LocalDate.of(2018, 11, 25));
        RegisterOffice ro2 = new RegisterOffice(2L, "", "");
        child1.setIssueDepartment(ro2);
        child1.setAddress(address);

        Child child2 = new Child("Petrov", "Vasiliy", "Petrovich", LocalDate.of(2015, 5, 2));
        child2.setCertificateNumber("" + (400000 + id));
        child2.setCertificateIssueDate(LocalDate.of(2015, 6, 6));
        RegisterOffice ro3 = new RegisterOffice(3L, "", "");
        child2.setIssueDepartment(ro3);
        child2.setAddress(address);

        so.setHusband(husband);
        so.setWife(wife);
        so.addChild(child1);
        so.addChild(child2);

        return so;

    }
}