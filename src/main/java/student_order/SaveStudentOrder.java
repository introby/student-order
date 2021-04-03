package student_order;

import student_order.dao.DictionaryDaoImpl;
import student_order.dao.StudentDaoImpl;
import student_order.domain.*;

import java.time.LocalDate;
import java.util.List;

public class SaveStudentOrder {
    public static void main(String[] args) throws Exception {
//        List<Street> d = new DictionaryDaoImpl().findStreets("Про");
//        for (Street s : d) {
//            System.out.println(s.getStreetName());
//
//        }
//
//        List<PassportOffice> po = new DictionaryDaoImpl().findPassportOffices("010020000000");
//        for (PassportOffice passportOffice : po) {
//            System.out.println(passportOffice.getOfficeName());
//
//        }
//
//        List<RegisterOffice> ro = new DictionaryDaoImpl().findRegisterOffices("020020020000");
//        for (RegisterOffice registerOffice : ro) {
//            System.out.println(registerOffice.getOfficeName());
//
//        }
//
//        List<CountryArea> ca1 = new DictionaryDaoImpl().findAreas("");
//        for (CountryArea countryArea : ca1) {
//            System.out.println(countryArea.getAreaId() + ":" + countryArea.getAreaName());
//
//        }
//
//        List<CountryArea> ca2 = new DictionaryDaoImpl().findAreas("020000000000");
//        for (CountryArea countryArea : ca2) {
//            System.out.println(countryArea.getAreaId() + ":" + countryArea.getAreaName());
//
//        }
//
//        List<CountryArea> ca3 = new DictionaryDaoImpl().findAreas("020010000000");
//        for (CountryArea countryArea : ca3) {
//            System.out.println(countryArea.getAreaId() + ":" + countryArea.getAreaName());
//
//        }
//
//        List<CountryArea> ca4 = new DictionaryDaoImpl().findAreas("020010010000");
//        for (CountryArea countryArea : ca4) {
//            System.out.println(countryArea.getAreaId() + ":" + countryArea.getAreaName());
//
//        }

//        StudentOrder s = buildStudentOrder(10);
        StudentDaoImpl dao = new StudentDaoImpl();
//        Long id = dao.saveStudentOrder(s);
//        System.out.println(id);

        List<StudentOrder> soList = dao.getStudentOrders();
        for (StudentOrder studentOrder : soList) {
            System.out.println(studentOrder.getStudentOrderID());
        }

    }

    static long saveStudentOrder(StudentOrder studentOrder) {
        long answer = 199;
        System.out.println("saveStudentOrder");

        return answer;

    }

    static StudentOrder buildStudentOrder(long id) {
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