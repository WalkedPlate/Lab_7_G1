package com.example.webapphr1_2023.Controllers;

import com.example.webapphr1_2023.Beans.Countries;
import com.example.webapphr1_2023.Beans.Employee;
import com.example.webapphr1_2023.Beans.Location;
import com.example.webapphr1_2023.Daos.CountriesDao;
import com.example.webapphr1_2023.Daos.LocationDao;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;

@WebServlet(name = "LocationServlet", urlPatterns = {"/LocationServlet"})
public class LocationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String action = req.getParameter("action") == null ? "lista" : req.getParameter("action");
        LocationDao locationDao = new LocationDao();
        CountriesDao countriesDao = new CountriesDao();
        RequestDispatcher view;

        switch (action){
            case "lista":
                ArrayList<Location> listaLocation = locationDao.obtenerListaLocations();
                req.setAttribute("locationList", listaLocation);
                view = req.getRequestDispatcher("location/list.jsp");
                view.forward(req, resp);
                break;
            case "agregar":
                req.setAttribute("listLocation",locationDao.obtenerListaLocations());
                req.setAttribute("listCountries",countriesDao.obtenerListaCountries());                ;
                view = req.getRequestDispatcher("location/formNew.jsp");
                view.forward(req, resp);
                break;
            case "editar":

                if (req.getParameter("id") != null) {

                    String locationIdString = req.getParameter("id");
                    int locationId = 0;
                    try {
                        locationId = Integer.parseInt(locationIdString);
                    } catch (NumberFormatException ex) {
                        resp.sendRedirect("LocationServlet");

                    }

                    Location location = locationDao.obtenerLocation(locationId);

                    if (location != null) {
                        req.setAttribute("location", location);
                        req.setAttribute("listLocation",locationDao.obtenerListaLocations());
                        req.setAttribute("listCountries",countriesDao.obtenerListaCountries());
                        view = req.getRequestDispatcher("location/formEditar.jsp");
                        view.forward(req, resp);
                    } else {
                        resp.sendRedirect("LocationServlet");
                    }

                } else {
                    resp.sendRedirect("LocationServlet");
                }

                break;
            case "borrar":
                if (req.getParameter("id") != null) {
                    String locationIdString = req.getParameter("id");
                    int locationId = 0;
                    try {
                        locationId = Integer.parseInt(locationIdString);
                    } catch (NumberFormatException ex) {
                        resp.sendRedirect("LocationServlet");
                    }

                    Location location = locationDao.obtenerLocation(locationId);

                    if (location != null) {
                        locationDao.borrarLocation(locationId);
                    }
                }

                resp.sendRedirect("LocationServlet");
                break;
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String action = req.getParameter("action") == null ? "lista" : req.getParameter("action");


        LocationDao locationDao = new LocationDao();

        Location location = new Location();
        location.setStreet_address(req.getParameter("street_address"));
        location.setPostal_code(req.getParameter("postal_code"));
        location.setCity(req.getParameter("city"));
        //Employee manager = employeeDao.obtenerEmpleado(Integer.parseInt(req.getParameter("manager_id")));
        location.setState_province(req.getParameter("state_province"));


        //location.setState_province(req.getParameter("state_province").equals("NULL") ? null : new BigDecimal(req.getParameter("state_province"))));


        Countries countries = new Countries();
        System.out.println("Hola GAAAA");
        System.out.println(req.getRequestDispatcher("country_id"));
        countries.setCountry_id(req.getParameter("country_id"));
        location.setCountries(countries);

        switch (action) {
            case "guardar":
                locationDao.guardarLocation(location);
                System.out.println(req.getParameter("street_address"));
                System.out.println(req.getParameter("postal_code"));
                System.out.println(req.getParameter("city"));
                System.out.println(req.getParameter("country_id"));

                resp.sendRedirect("LocationServlet");
                break;

            case "actualizar":
                System.out.println(req.getParameter("street_address"));
                System.out.println(req.getParameter("postal_code"));
                System.out.println(req.getParameter("city"));
                System.out.println(req.getParameter("country_id"));
                System.out.println(req.getParameter("location_id"));
                location.setLocationId(Integer.parseInt(req.getParameter("location_id"))); //no olvidar que para actualizar se debe enviar el ID

                locationDao.actualizarLocation(location);

                resp.sendRedirect("LocationServlet");

                break;
        }
    }
}