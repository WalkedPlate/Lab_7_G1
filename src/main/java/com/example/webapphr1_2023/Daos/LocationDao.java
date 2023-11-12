package com.example.webapphr1_2023.Daos;

import com.example.webapphr1_2023.Beans.Countries;
import com.example.webapphr1_2023.Beans.Employee;
import com.example.webapphr1_2023.Beans.Job;
import com.example.webapphr1_2023.Beans.Location;

import java.sql.*;
import java.util.ArrayList;

public class LocationDao extends DaoBase{
    public ArrayList<Location> obtenerListaLocations() {
        ArrayList<Location> listaLocation = new ArrayList<>();

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("select * from locations l\n" +
                     "\t\tinner join countries c on l.country_id = c.country_id");) {

            while (rs.next()) {
                Location location = new Location();
                location.setLocationId(rs.getInt(1));
                location.setStreet_address(rs.getString(2));
                location.setPostal_code(rs.getString(3));
                location.setCity(rs.getString(4));
                location.setState_province(rs.getString(5));

                Countries countries = new Countries();
                countries.setCountry_name(rs.getString(8));
                location.setCountries(countries);


                listaLocation.add(location);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaLocation;

    }

    public Location obtenerLocation(int locationId) {

        Location location = null;

        String sql = "select * from locations l\n" +
                "\t\tinner join countries c on l.country_id = c.country_id Where l.location_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, locationId);

            try (ResultSet rs = pstmt.executeQuery()) {

                while (rs.next()) {
                    location = new Location();
                    location.setLocationId(rs.getInt(1));
                    location.setStreet_address(rs.getString(2));
                    location.setPostal_code(rs.getString(3));
                    location.setCity(rs.getString(4));
                    location.setState_province(rs.getString(5));

                    Countries countries = new Countries();
                    countries.setCountry_name(rs.getString(8));
                    location.setCountries(countries);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return location;
    }
}


