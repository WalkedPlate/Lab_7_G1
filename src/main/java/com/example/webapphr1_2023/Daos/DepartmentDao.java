package com.example.webapphr1_2023.Daos;


import com.example.webapphr1_2023.Beans.Countries;
import com.example.webapphr1_2023.Beans.Department;
import com.example.webapphr1_2023.Beans.Employee;
import com.example.webapphr1_2023.Beans.Location;

import java.sql.*;
import java.util.ArrayList;

public class DepartmentDao extends DaoBase {

    public ArrayList<Department> lista() {

        //Daos necesarios
        EmployeeDao eDao = new EmployeeDao();
        LocationDao lDao = new LocationDao();

        ArrayList<Department> list = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("select * from departments")) {

            while (rs.next()) {
                Department department = new Department();
                department.setDepartmentId(rs.getInt(1));
                department.setDepartmentName(rs.getString(2));

                department.setManager(eDao.obtenerEmpleado(rs.getInt("manager_id")));
                department.setLocation(lDao.obtenerLocation(rs.getInt("location_id")));

                list.add(department);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);

        }
        return list;
    }

    public int maximoIdDepartments(){

        String sql = "select max(department_id) from departments";
        int maxIdDepartments = 0;

        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            if (rs.next()) {
                maxIdDepartments = rs.getInt(1);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);

        }
        return maxIdDepartments;

    }

    public void crearDepartment(Department department) {

        String sql = "INSERT INTO departments (department_id, department_name, manager_id, location_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1,maximoIdDepartments()+10);
            pstmt.setString(2,department.getDepartmentName());

            if(department.getManager() == null){
                pstmt.setNull(3,Types.INTEGER);
            }
            else{
                pstmt.setInt(3,department.getManager().getEmployeeId());
            }
            if(department.getLocation() == null){
                pstmt.setNull(4,Types.INTEGER);
            }
            else{
                pstmt.setInt(4,department.getLocation().getLocationId());
            }
            pstmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }


    public Department obtenerDepartment(int departmentId) {


        //Daos necesarios
        EmployeeDao eDao = new EmployeeDao();
        LocationDao lDao = new LocationDao();

        Department department = new Department();
        String sql = "select * from departments where department_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, departmentId);

            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {
                    department.setDepartmentId(rs.getInt(1));
                    department.setDepartmentName(rs.getString(2));
                    department.setManager(eDao.obtenerEmpleado(rs.getInt("manager_id")));
                    department.setLocation(lDao.obtenerLocation(rs.getInt("location_id")));
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return department;
    }

    public void actualizarDepartment(Department department) {

        String sql = "update departments set department_name = ?, manager_id = ?, location_id = ? where department_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {


            pstmt.setString(1,department.getDepartmentName());

            if(department.getManager() == null){
                pstmt.setNull(2,Types.INTEGER);
            }
            else{
                pstmt.setInt(2,department.getManager().getEmployeeId());
            }
            if(department.getLocation() == null){
                pstmt.setNull(3,Types.INTEGER);
            }
            else{
                pstmt.setInt(3,department.getLocation().getLocationId());
            }
            pstmt.setInt(4,department.getDepartmentId());

            pstmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public void borrarDepartment(int departmentId) {

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement("DELETE FROM departments WHERE department_id = ?")) {

            pstmt.setInt(1, departmentId);
            pstmt.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }



}