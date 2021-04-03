package student_order.dao;

import student_order.config.Config;
import student_order.domain.*;
import student_order.exception.DaoException;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class StudentDaoImpl implements StudentOrderDao {

    private static final String INSERT_ORDER = "INSERT INTO jc_student_order(" +
            "student_order_status, student_order_date, " +
            "h_surname, h_givenname, h_patronymic, h_date_of_birth, h_passport_series, h_passport_number, h_passport_date, h_passport_office_id, " +
            "h_post_index, h_street_code, h_building, h_extension, h_apartment, h_university_id, h_student_number, " +
            "w_surname, w_givenname, w_patronymic, w_date_of_birth, w_passport_series, w_passport_number, w_passport_date, w_passport_office_id, " +
            "w_post_index, w_street_code, w_building, w_extension, w_apartment, w_university_id, w_student_number, " +
            "certificate_id, register_office_id, marriage_date)" +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String INSERT_CHILD = "INSERT INTO jc_student_child(" +
            "student_order_id, c_surname, c_givenname, c_patronymic, c_date_of_birth, c_certificate_number, c_certificate_date, c_register_office_id, " +
            "c_post_index, c_street_code, c_building, c_extension, c_apartment)" +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String SELECT_ORDERS = "select so.*, ro.r_office_area_id, ro.r_office_name, " +
            "po_h.p_office_area_id as h_p_office_area_id, po_h.p_office_name as h_p_office_name, " +
            "po_w.p_office_area_id as w_p_office_area_id, po_w.p_office_name as w_p_office_name " +
            "from jc_student_order so " +
            "join jc_register_office ro on so.register_office_id = ro.r_office_id " +
            "join jc_passport_office po_h on so.h_passport_office_id = po_h.p_office_id " +
            "join jc_passport_office po_w on so.w_passport_office_id = po_w.p_office_id " +
            "where student_order_status = ? " +
            "order by student_order_date limit ?";

    private static final String SELECT_CHILD = "select soc.*, ro.r_office_area_id as c_r_office_area_id, ro.r_office_name as c_r_office_name " +
            "from jc_student_child soc " +
            "inner join jc_register_office ro " +
            "on soc.c_register_office_id = ro.r_office_id " +
            "where soc.student_order_id in ";

    private static final String SELECT_ORDERS_FULL = "select so.*, ro.r_office_area_id, ro.r_office_name, " +
            "po_h.p_office_area_id as h_p_office_area_id, po_h.p_office_name as h_p_office_name, " +
            "po_w.p_office_area_id as w_p_office_area_id, po_w.p_office_name as w_p_office_name, " +
            "soc.*, ro_c.r_office_area_id as c_r_office_area_id, ro_c.r_office_name as c_r_office_name " +
            "from jc_student_order so " +
            "inner join jc_register_office ro on so.register_office_id = ro.r_office_id " +
            "inner join jc_passport_office po_h on so.h_passport_office_id = po_h.p_office_id " +
            "inner join jc_passport_office po_w on so.w_passport_office_id = po_w.p_office_id " +
            "inner join jc_student_child soc on so.student_order_id = soc.student_order_id " +
            "inner join jc_register_office ro_c on soc.c_register_office_id = ro_c.r_office_id " +
            "where student_order_status = ? " +
            "order by so.student_order_id limit ?";

    //TODO refactoring - create one method
    private Connection getConnection() throws SQLException {

        return DriverManager.getConnection(Config.getProperties(Config.DB_URL), Config.getProperties(Config.DB_LOGIN), Config.getProperties(Config.DB_PASSWORD));
    }

    @Override
    public Long saveStudentOrder(StudentOrder so) throws DaoException {

        Long result = -1L;

        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(INSERT_ORDER, new String[]{"student_order_id"})) {

            connection.setAutoCommit(false);

            try {
                // Header
                stmt.setInt(1, StudentOrderStatus.START.ordinal());
                stmt.setTimestamp(2, java.sql.Timestamp.valueOf(LocalDateTime.now()));

                // Husband and wife
                setParamsForAdult(stmt, 3, so.getHusband());
                setParamsForAdult(stmt, 18, so.getWife());

                // Marriage
                stmt.setString(33, so.getMarriageCertificateId());
                stmt.setLong(34, so.getMarriageOffice().getOfficeId());
                stmt.setDate(35, java.sql.Date.valueOf(so.getMarriageDate()));

                stmt.executeUpdate();

                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next())
                    result = generatedKeys.getLong(1);

                saveChildren(connection, so, result);

                connection.commit();

            } catch (SQLException ex) {
                connection.rollback();
                throw ex;
            }

        } catch (SQLException ex) {
            throw new DaoException(ex);

        }
        return result;
    }

    private void saveChildren(Connection connection, StudentOrder so, Long soId) throws SQLException {
        try (PreparedStatement stmt = connection.prepareStatement(INSERT_CHILD)) {
            for (Child child : so.getChildren()) {
                stmt.setLong(1, soId);
                setParamsForChild(stmt, child);
                stmt.addBatch();

            }
            stmt.executeBatch();
        }
    }

    private void setParamsForChild(PreparedStatement stmt, Child child) throws SQLException {
        setParamsForPerson(stmt, 2, child);
        stmt.setString(6, child.getCertificateNumber());
        stmt.setDate(7, java.sql.Date.valueOf(child.getCertificateIssueDate()));
        stmt.setLong(8, child.getIssueDepartment().getOfficeId());
        setParamsForAddress(stmt, 9, child);
    }

    private void setParamsForAdult(PreparedStatement stmt, int start, Adult adult) throws SQLException {
        setParamsForPerson(stmt, start, adult);
        stmt.setString(start + 4, adult.getPassportSeries());
        stmt.setString(start + 5, adult.getPassportNumber());
        stmt.setDate(start + 6, java.sql.Date.valueOf(adult.getPassportIssueDate()));
        stmt.setLong(start + 7, adult.getIssueDepartment().getOfficeId());
        setParamsForAddress(stmt, start + 8, adult);
        stmt.setLong(start + 13, adult.getUniversity().getUniversityId());
        stmt.setString(start + 14, adult.getStudentID());
    }

    private void setParamsForPerson(PreparedStatement stmt, int start, Person person) throws SQLException {
        stmt.setString(start, person.getSurName());
        stmt.setString(start + 1, person.getGivenName());
        stmt.setString(start + 2, person.getPatronymic());
        stmt.setDate(start + 3, java.sql.Date.valueOf(person.getDateOfBirthday()));
    }

    private void setParamsForAddress(PreparedStatement stmt, int start, Person person) throws SQLException {
        Address personAddress = person.getAddress();
        stmt.setString(start, personAddress.getPostCode());
        stmt.setLong(start + 1, personAddress.getStreet().getStreetCode());
        stmt.setString(start + 2, personAddress.getBuilding());
        stmt.setString(start + 3, personAddress.getExtension());
        stmt.setString(start + 4, personAddress.getApartment());
    }

    @Override
    public List<StudentOrder> getStudentOrders() throws DaoException {
        return getStudentOrdersOneSelect();
//        return getStudentOrdersTwoSelect();
    }

    private List<StudentOrder> getStudentOrdersOneSelect() throws DaoException {
        List<StudentOrder> result = new LinkedList<>();
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(SELECT_ORDERS_FULL)) {

            Map<Long, StudentOrder> maps = new HashMap<>();
            stmt.setInt(1, StudentOrderStatus.START.ordinal());
            int limit = Integer.parseInt(Config.getProperties(Config.DB_LIMIT));
            stmt.setInt(2, limit);
            ResultSet rs = stmt.executeQuery();
            int counter = 0;
            while (rs.next()) {
                Long soId = rs.getLong("student_order_id");
                if (!maps.containsKey(soId)) {

                    StudentOrder so = getFullStudentOrder(rs);

                    result.add(so);
                    maps.put(soId, so);
                }
                StudentOrder so = maps.get(soId);
                so.addChild(fillChild(rs));
                counter++;

                if (counter >= limit) {
                    result.remove(result.size() - 1);
                }

            }

        } catch (SQLException ex) {
            throw new DaoException(ex);
        }
        return result;
    }

    private List<StudentOrder> getStudentOrdersTwoSelect() throws DaoException {
        List<StudentOrder> result = new LinkedList<>();
        try (Connection connection = getConnection();
             PreparedStatement stmt = connection.prepareStatement(SELECT_ORDERS)) {

            stmt.setInt(1, StudentOrderStatus.START.ordinal());
            stmt.setInt(2, Integer.parseInt(Config.getProperties(Config.DB_LIMIT)));
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                StudentOrder so = getFullStudentOrder(rs);

                result.add(so);
            }

            findChildren(connection, result);

        } catch (SQLException ex) {
            throw new DaoException(ex);
        }
        return result;
    }

    private StudentOrder getFullStudentOrder(ResultSet rs) throws SQLException {
        StudentOrder so = new StudentOrder();

        fillStudentOrder(rs, so);
        fillMarriage(rs, so);

        so.setHusband(fillAdult(rs, "h_"));
        so.setWife(fillAdult(rs, "w_"));
        return so;
    }

    private void fillStudentOrder(ResultSet rs, StudentOrder so) throws SQLException {
        so.setStudentOrderID(rs.getLong("student_order_id"));
        so.setStudentOrderDate(rs.getTimestamp("student_order_date").toLocalDateTime());
        so.setStudentOrderStatus(StudentOrderStatus.fromValue(rs.getInt("student_order_status")));

    }

    private Adult fillAdult(ResultSet rs, String prefix) throws SQLException {
        Adult adult = new Adult();
        adult.setSurName(rs.getString(prefix + "surname"));
        adult.setGivenName(rs.getString(prefix + "givenname"));
        adult.setPatronymic(rs.getString(prefix + "patronymic"));
        adult.setDateOfBirthday(rs.getDate(prefix + "date_of_birth").toLocalDate());
        adult.setPassportSeries(rs.getString(prefix + "passport_series"));
        adult.setPassportNumber(rs.getString(prefix + "passport_number"));
        adult.setPassportIssueDate(rs.getDate(prefix + "passport_date").toLocalDate());

        Long poId = rs.getLong(prefix + "passport_office_id");
        String poAriaId = rs.getString(prefix + "p_office_area_id");
        String poName = rs.getString(prefix + "p_office_name");
        PassportOffice po = new PassportOffice(poId, poAriaId, poName);
        adult.setIssueDepartment(po);

        Address address = new Address();
        address.setPostCode(rs.getString(prefix + "post_index"));

        Street st = new Street(rs.getLong(prefix + "street_code"), "");
        address.setStreet(st);

        address.setBuilding(rs.getString(prefix + "building"));
        address.setExtension(rs.getString(prefix + "extension"));
        address.setApartment(rs.getString(prefix + "apartment"));
        adult.setAddress(address);

        University university = new University(rs.getLong(prefix + "university_id"), "");

        adult.setUniversity(university);
        adult.setStudentID(rs.getString(prefix + "student_number"));

        return adult;
    }

    private void fillMarriage(ResultSet rs, StudentOrder so) throws SQLException {
        so.setMarriageCertificateId(rs.getString("certificate_id"));
        so.setMarriageDate(rs.getDate("marriage_date").toLocalDate());

        Long roId = rs.getLong("register_office_id");
        String roAreaId = rs.getString("r_office_area_id");
        String roName = rs.getString("r_office_name");
        RegisterOffice ro = new RegisterOffice(roId, roAreaId, roName);
        so.setMarriageOffice(ro);
    }

    private void findChildren(Connection connection, List<StudentOrder> result) throws SQLException {
        String cl = "(" + result.stream().map(studentOrder -> String.valueOf(studentOrder.getStudentOrderID()))
                .collect(Collectors.joining(",")) + ")";

        final Map<Long, StudentOrder> maps = result.stream().collect(Collectors
                .toMap(studentOrder -> studentOrder.getStudentOrderID(), studentOrder -> studentOrder));

        try (PreparedStatement stmt = connection.prepareStatement(SELECT_CHILD + cl)) {

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Child child = fillChild(rs);
                StudentOrder so = maps.get(rs.getLong("student_order_id"));
                so.addChild(child);
            }
        }
    }

    private Child fillChild(ResultSet rs) throws SQLException {
        String surName = rs.getString("c_surname");
        String givenName = rs.getString("c_givenname");
        String patronymic = rs.getString("c_patronymic");
        LocalDate dateOfBirth = rs.getDate("c_date_of_birth").toLocalDate();

        Child child = new Child(surName, givenName, patronymic, dateOfBirth);

        child.setCertificateNumber(rs.getString("c_certificate_number"));
        child.setCertificateIssueDate(rs.getDate("c_certificate_date").toLocalDate());

        Long roId = rs.getLong("c_register_office_id");
        String roAreaId = rs.getString("c_r_office_area_id");
        String roName = rs.getString("c_r_office_name");
        RegisterOffice ro = new RegisterOffice(roId, roAreaId, roName);
        child.setIssueDepartment(ro);

        Address address = new Address();
        Street st = new Street(rs.getLong("c_street_code"), "");
        address.setStreet(st);
        address.setPostCode(rs.getString("c_post_index"));
        address.setBuilding(rs.getString("c_building"));
        address.setExtension(rs.getString("c_extension"));
        address.setApartment(rs.getString("c_apartment"));
        child.setAddress(address);

        return child;

    }
}
