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
                     "\t\tleft join countries c on l.country_id = c.country_id ORDER BY location_id");) {

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
                "\t\tleft join countries c on l.country_id = c.country_id Where l.location_id = ?";

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
                    //countries.setCountry_id(rs.getString(7));
                    countries.setCountry_name(rs.getString(8));

                    location.setCountries(countries);
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return location;
    }
    public void guardarLocation(Location location) {

        String sql = "INSERT INTO locations (location_id, street_address, postal_code, city, state_province, country_id) "
                + "VALUES (?, ?, ?, ?, ?,?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, maximoIdLocations()+100);
            pstmt.setString(2, location.getStreet_address());
            pstmt.setString(3, location.getPostal_code());
            pstmt.setString(4, location.getCity());
            pstmt.setString(5, location.getState_province());
            System.out.println(location.getCountries().getCountry_id());
            if(location.getCountries() == null){
                pstmt.setNull(6, Types.CHAR);
            }
            else{
                pstmt.setString(6, location.getCountries().getCountry_id());

            }
            pstmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public int maximoIdLocations(){
        String sql = "Select max(location_id) from locations";
        int maxIdLocations = 0;
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)){

             if (rs.next()){
                 maxIdLocations = rs.getInt(1);
             }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return maxIdLocations;
    }

    public void actualizarLocation(Location location) {

        String sql = "UPDATE locations "
                + "SET street_address = ?, "
                + "postal_code = ?, "
                + "city = ?, "
                + "state_province = ?, "
                + "country_id = ?, "
                + "WHERE location_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, location.getStreet_address());
            pstmt.setString(2, location.getPostal_code());
            pstmt.setString(3, location.getCity());
            pstmt.setString(4, location.getState_province());
            pstmt.setString(5, location.getCountries().getCountry_id());
            pstmt.setInt(6, location.getLocationId());

            pstmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    public void borrarLocation(int locationId) {

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM locations WHERE location_id = ?")) {

            pstmt.setInt(1, locationId);
            pstmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}


