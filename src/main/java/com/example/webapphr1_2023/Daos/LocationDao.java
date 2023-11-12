package com.example.webapphr1_2023.Daos;

import com.example.webapphr1_2023.Beans.Job;
import com.example.webapphr1_2023.Beans.Location;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class LocationDao extends DaoBase{
    public ArrayList<Location> obtenerListaLocations() {
        ArrayList<Location> listaLocation = new ArrayList<>();

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM locations");) {

            while (rs.next()) {
                Location location = new Location();
                job.setJobId(rs.getString(1));
                job.setJobTitle(rs.getString(2));
                job.setMinSalary(rs.getInt(3));
                job.setMaxSalary(rs.getInt(4));

                listaTrabajos.add(job);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaTrabajos;

    }
}
