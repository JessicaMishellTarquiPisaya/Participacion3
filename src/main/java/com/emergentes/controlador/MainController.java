package com.emergentes.controlador;

import com.emergentes.modelo.Libro;
import com.emergentes.utiles.ConexionDB;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "MainController", urlPatterns = {"/MainController"})
public class MainController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            String op;
            op = (request.getParameter("op") != null) ? request.getParameter("op") : "list ";
            ArrayList<Libro> lista = new ArrayList<Libro>();
            ConexionDB canal = new ConexionDB();
            Connection conn = canal.conectar();
            PreparedStatement ps;

            ResultSet rs;
            if (op.equals("list ")) {
            //Para listar los datos
                String sql = "select * from libros ";
            //Consulta de selección y almacenarlo en una colección
                ps = conn.prepareStatement(sql);
                rs = ps.executeQuery();
                while (rs.next()) {
                    Libro lib = new Libro();
                    lib.setId(rs.getInt("id"));
                    lib.setIsbn(rs.getString("isbn"));
                    lib.setTitulo(rs.getString("titulo"));
                    lib.setCategoria(rs.getString("categoria"));
                    lista.add(lib);
                }
                request.setAttribute("lista", lista);
                // Enviar al index . jsp para mostrar la informacion
                request.getRequestDispatcher("index.jsp").forward(request, response);
            }

            if (op.equals("nuevo ")) {
            //Instanciar un objeto de la clase Libro
                Libro li = new Libro();
                System.out.println(li.toString());
            // El objeto se pone como atributo de request
                request.setAttribute("lib", li);
            // Redireccionar a editar.jsp
                request.getRequestDispatcher("editar.jsp").forward(request, response);
            }
        

            if (op.equals("eliminar")) {
            //Obtener el id
                int id = Integer.parseInt(request.getParameter("id"));
            //Realizar la eliminación en la base de datos
                String sql = "delete from 1ibros where id = ?";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, id);
                ps.executeUpdate();
            // Redireccionar a MainController
                response.sendRedirect("MainController");
            }
        } catch (SQLException ex) {
            System.out.println("ERROR AL CONEXTAR "+ex.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            System.out.println("Valor de ID " + id);
            String isbn = request.getParameter("isbn");
            String titulo = request.getParameter("titulo ");
            String categoria = request.getParameter(" categoria");
            Libro lib = new Libro();
            lib.setId(id);

            lib.setIsbn(isbn);
            lib.setTitulo(titulo);
            lib.setCategoria(categoria);

            ConexionDB canal = new ConexionDB();
            Connection conn = canal.conectar();
            PreparedStatement ps;
            ResultSet rs;
            if (id == 0) {
            //Nuevo registro
                String sql = "insert into 1ibros (isbn, titulo, categoria) values (?,?, ?)";
                ps = conn.prepareStatement(sql);
                ps.setString(1, lib.getIsbn());
                ps.setString(2, lib.getTitulo());
                ps.setString(3, lib.getCategoria());
                ps.executeUpdate();
            } else {
            //Edición de registro
            }
            response.sendRedirect("MainController");
        } catch (SQLException ex) {
            System.out.println("Error en SQL " + ex.getMessage());
        }
    }
}