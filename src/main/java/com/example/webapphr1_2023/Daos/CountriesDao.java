package com.example.webapphr1_2023.Daos;

import com.example.webapphr1_2023.Beans.Countries;
import com.example.webapphr1_2023.Beans.Job;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class CountriesDao extends DaoBase{
    public ArrayList<Countries> obtenerListaCountries() {
        ArrayList<Countries> listaCountries = new ArrayList<>();

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM countries");) {

            while (rs.next()) {
                Countries countries = new Countries();
                countries.setCountry_id(rs.getString(1));
                countries.setCountry_name(rs.getString(2));


                listaCountries.add(countries);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaCountries;

    }
}
