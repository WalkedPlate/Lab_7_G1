package com.example.webapphr1_2023.Controllers;

import com.example.webapphr1_2023.Beans.Department;
import com.example.webapphr1_2023.Beans.Employee;
import com.example.webapphr1_2023.Beans.Job;
import com.example.webapphr1_2023.Beans.Location;
import com.example.webapphr1_2023.Daos.DepartmentDao;
import com.example.webapphr1_2023.Daos.EmployeeDao;
import com.example.webapphr1_2023.Daos.JobDao;
import com.example.webapphr1_2023.Daos.LocationDao;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;

@WebServlet(name = "DepartmentServlet", urlPatterns = {"/DepartmentServlet"})
public class DepartmentServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String action = req.getParameter("action") == null ? "lista" : req.getParameter("action");

        EmployeeDao employeeDao = new EmployeeDao();
        DepartmentDao departmentDao = new DepartmentDao();
        LocationDao locationDao= new LocationDao();

        switch (action) {
            case "lista":
                RequestDispatcher view;
                req.setAttribute("departmentList", departmentDao.lista());
                view = req.getRequestDispatcher("department/list.jsp");
                view.forward(req, resp);
                break;
            case "formCrear":

                req.setAttribute("listaJefes",employeeDao.listarEmpleados());
                req.setAttribute("listaLocations",locationDao.obtenerListaLocations());
                req.getRequestDispatcher("department/form_crear.jsp").forward(req,resp);

                break;
            case "editar":

                if (req.getParameter("id") != null) {
                    String departmentIdString = req.getParameter("id");
                    int departmentId = 0;
                    try {
                        departmentId = Integer.parseInt(departmentIdString);
                    } catch (NumberFormatException ex) {
                        resp.sendRedirect("DepartmentServlet");

                    }

                    Department department = departmentDao.obtenerDepartment(departmentId);
                    if (department.getDepartmentName() != null) {
                        req.setAttribute("department", department);
                        req.setAttribute("listaJefes",employeeDao.listarEmpleados());
                        req.setAttribute("listaLocations",locationDao.obtenerListaLocations());
                        req.getRequestDispatcher("department/formularioEditar.jsp").forward(req, resp);
                    } else {
                        resp.sendRedirect("DepartmentServlet");
                    }

                } else {
                    resp.sendRedirect("DepartmentServlet");
                }


                break;
            case "borrar":

                if (req.getParameter("id") != null) {
                    String departmentIdString = req.getParameter("id");
                    int departmentId = 0;
                    try {
                        departmentId = Integer.parseInt(departmentIdString);
                    } catch (NumberFormatException ex) {
                        resp.sendRedirect("EmployeeServlet");
                    }

                    Department department = departmentDao.obtenerDepartment(departmentId);

                    if (department.getDepartmentName() != null) {
                        departmentDao.borrarDepartment(departmentId);
                    }
                }
                resp.sendRedirect("DepartmentServlet");
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        EmployeeDao employeeDao = new EmployeeDao();
        JobDao jobDao = new JobDao();
        DepartmentDao departmentDao = new DepartmentDao();
        LocationDao locationDao= new LocationDao();

        String action = req.getParameter("action") == null ? "lista" : req.getParameter("action");

        Department department = new Department();
        department.setDepartmentName(req.getParameter("department_name"));

        Employee manager = employeeDao.obtenerEmpleado(Integer.parseInt(req.getParameter("manager_id")));
        Location location = locationDao.obtenerLocation(Integer.parseInt(req.getParameter("location_id")));
        department.setManager(manager);
        department.setLocation(location);

        switch (action) {
            case "guardar":
                departmentDao.crearDepartment(department);
                resp.sendRedirect(req.getContextPath() + "/DepartmentServlet");
                break;
            case "actualizar":

                department.setDepartmentId(Integer.parseInt(req.getParameter("department_id"))); //no olvidar que para actualizar se debe enviar el ID
                departmentDao.actualizarDepartment(department);

                resp.sendRedirect(req.getContextPath() + "/DepartmentServlet");


                break;
        }


    }
}
