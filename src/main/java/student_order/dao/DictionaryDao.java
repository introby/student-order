package student_order.dao;

import student_order.domain.CountryArea;
import student_order.domain.PassportOffice;
import student_order.domain.RegisterOffice;
import student_order.domain.Street;
import student_order.exception.DaoException;

import java.util.List;

public interface DictionaryDao {

    List<Street> findStreets(String pattern) throws DaoException;
    List<PassportOffice> findPassportOffices(String areaId) throws DaoException;
    List<RegisterOffice> findRegisterOffices(String areaId) throws DaoException;
    List<CountryArea> findAreas(String areaId) throws DaoException;

}
