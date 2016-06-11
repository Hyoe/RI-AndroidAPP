package com.recycleit.app;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.*;
import java.sql.*;
import javax.servlet.http.*;
import com.google.appengine.api.utils.SystemProperty;

public class RecycleServlet extends HttpServlet {
    @Override
    public void doGet(HttpServletRequest req, HttpServletResponse resp)throws IOException {

        if (req.getParameter("function").equals("getTaco")) {

            String url = null;

            resp.setContentType("text/html");
            resp.getWriter().println("get Taco function<br>");

            // Load the class that provides the new "jdbc:google:mysql://" prefix.
            try {
                Class.forName("com.mysql.jdbc.GoogleDriver");
                //format for url is........appname:mysqlinstancename/dbname?user=username&password=passsword
                url = "REDACTED"
				Connection conn = DriverManager.getConnection(url);
                ResultSet rs = conn.createStatement().executeQuery("SELECT * FROM users WHERE username = " + "test");

                while (rs.next()) {
                    String id = rs.getString("id");
                    String value = rs.getString("value");

                    resp.getWriter().println("id: " + id + " value: " + value);

                }

                conn.close();

            } catch (Exception e) {
                resp.getWriter().println(e.toString());
            }
        }

        if (req.getParameter("function").equals("doRegister")) {
            String url = null;

            String getUsername = req.getParameter("username");

            resp.setContentType("text/html");

            // Load the class that provides the new "jdbc:google:mysql://" prefix.
            try {
                Class.forName("com.mysql.jdbc.GoogleDriver");
                //format for url is........appname:mysqlinstancename/dbname?user=username&password=passsword
                url = "REDACTED";
                Connection conn = DriverManager.getConnection(url);
                String query = "SELECT username FROM users WHERE username = '" + getUsername + "'";
                ResultSet rs = conn.createStatement().executeQuery(query);

                String username = null;
                while (rs.next()) {
                    username = rs.getString("username");
                }

                if (username != null) {
                    //echo out json that says username is taken
                    resp.getWriter().println("{\"status\":\"usernameTaken\"}");
                    //{"firstName":"John", "lastName":"Doe"}
                } else {

                    //register the user
                    //resp.getWriter().println("username is available");

                    String getPassword = req.getParameter("password");
                    String getEmail = req.getParameter("email");

                    query = "INSERT INTO `users` ( `username` , `pw` , `email` ) VALUES ( ?, ?, ? )";

                    PreparedStatement stmt = conn.prepareStatement(query);
                    stmt.setString(1, getUsername);
                    stmt.setString(2, getPassword);
                    stmt.setString(3, getEmail);
                    int success = 2;
                    success = stmt.executeUpdate();

                    if (success == 1) {
                        //echo out sucessful registration
                        resp.getWriter().println("{\"status\":\"successfulRegistration\"}");
                    } else if (success == 0) {
                        resp.getWriter().println("{\"status\":\"databaseError\"}");
                    }
                }

                conn.close();

            } catch (Exception e) {
                resp.getWriter().println(e.toString());
            }
        }

        if (req.getParameter("function").equals("doLogin")) {
            String url = null;

            String getUsername = req.getParameter("username");
            //String getEmail = req.getParameter("email");
            //String getLogin = req.getParameter("login"); //if we have time - try to make it either or
            String getPassword = req.getParameter("password");

            resp.setContentType("text/html");

            // Load the class that provides the new "jdbc:google:mysql://" prefix.
            try {
                Class.forName("com.mysql.jdbc.GoogleDriver");
                //format for url is........appname:mysqlinstancename/dbname?user=username&password=passsword
                url = "REDACTED";
                Connection conn = DriverManager.getConnection(url);
                //String query = "SELECT * FROM users WHERE username = '" + getUsername + "' AND pw = '" + getPassword + "'";
                String query = "SELECT * FROM users WHERE username = '" + getUsername + "' AND pw = '" + getPassword + "' OR email = '" + getUsername + "' AND pw = '" + getPassword + "'";
                //String query1 = "SELECT place_id FROM favs_comments WHERE username = '" + getUsername + "'"; //TO DO - make sure this is placed in the code when we can make sure username is username (not email) - after username is defined
                ResultSet rs = conn.createStatement().executeQuery(query);
                //ResultSet rs1 = conn.createStatement().executeQuery(query1);

                String username = null;
                String email = null;


                while (rs.next()) {
                    username = rs.getString("username");
                    email = rs.getString("email");
                }
                String loginString = null;
                if (username != null) {
                    //username/password combination works

                    loginString = "{\"status\":\"goodLogin\", \"favorites\": [";
                    //resp.getWriter().println("{\"status\":\"goodLogin\"}");
                    //{"firstName":"John", "lastName":"Doe"}
                } else {
                    resp.getWriter().println("{\"status\":\"incorrectUsernamePassword\"}");
                    conn.close();
                    return;
                }

                String query1 = "SELECT favs_comments.place_id, places.place_name FROM favs_comments JOIN places WHERE username = '" + username + "' AND favs_comments.place_id = places.place_id" ;
                ResultSet rs1 = conn.createStatement().executeQuery(query1);

                if (!rs1.isBeforeFirst()) {
                    loginString += "]}";
                }

                else {
                    boolean first = true;
                    String placeID = null;
                    String placeName = null;
                    loginString += "{";
                    while (rs1.next()) {
                        placeID = rs1.getString("place_id");
                        placeName = rs1.getString("place_name");

                        if (first == false) {
                            loginString += ", {";
                        }
                        loginString += "\"placeid\": " + "\"" + placeID + "\", \"placename\": " + "\"" + placeName + "\"}";
                        first = false;
                    }
                    loginString += "]}";
                }
                resp.getWriter().println(loginString);
                conn.close();
            } catch (Exception e) {
                resp.getWriter().println(e.toString());
            }
        }
/*
        if (req.getParameter("function").equals("queryPlace")) {

        }
*/
        if (req.getParameter("function").equals("doUpdatePlace")) {
            String url = null;
            String getUsername = req.getParameter("username");
            String getPlaceID = req.getParameter("place_id");
            String getFavoriteStatus = req.getParameter("favorite_checked");
            String getReimburseStatus = req.getParameter("reimburse");
            String getMaterials = req.getParameter("materials");
            String getPlaceName = req.getParameter("placename");
            String getPlaceAddress = req.getParameter("placeaddress");
            String getPlacePhone = req.getParameter("placephone");
            String getPlaceWebsite = req.getParameter("placewebsite");

			/*
			String getReimburseStatus = (!req.getParameter("reimburse")) ? "" : "Yes";
			String getMaterials = (!req.getParameter("materials")) ? "" : req.getParameter("materials");
			String getPlaceName = (!req.getParameter("placename")) ? "" : req.getParameter("placename");
			String getPlaceAddress = (!req.getParameter("placeaddress")) ? "" : req.getParameter("placeaddress");
			String getPlacePhone = (!req.getParameter("placephone")) ? "" : req.getParameter("placephone");
			String getPlaceWebsite = (!req.getPlaceWebsite("placewebsite")) ? "" : req.getParameter("placewebsite");
			*/


            resp.setContentType("text/html");



            try {
                Class.forName("com.mysql.jdbc.GoogleDriver");
                //format for url is........appname:mysqlinstancename/dbname?user=username&place_id=place_id
                url = "REDACTED";
                Connection conn = DriverManager.getConnection(url);
                //query to see if already in favorites
                String checkIfFavoriteQuery = "SELECT place_id FROM favs_comments WHERE username = '" + getUsername + "' AND place_id = '" + getPlaceID + "'";

                ResultSet rs = conn.createStatement().executeQuery(checkIfFavoriteQuery);

                if (!rs.isBeforeFirst()) { //empty result set - not in favorites

                    if (getFavoriteStatus.equals("1")) {

                        String query1 = "INSERT INTO favs_comments (username, place_id, comment) VALUES ( ?, ?, ? )";


                        PreparedStatement stmt = conn.prepareStatement(query1);
                        stmt.setString(1, getUsername);
                        stmt.setString(2, getPlaceID);
                        stmt.setString(3, "");
                        int success = 2;
                        success = stmt.executeUpdate();


                    }

                }


                else { //not empty result set
                    if (getFavoriteStatus.equals("0")) {

                        String query2 = "DELETE FROM favs_comments WHERE username = '" + getUsername + "' AND place_id = '" + getPlaceID + "'";

                        PreparedStatement stmt = conn.prepareStatement(query2);

                        int success = 2;
                        success = stmt.executeUpdate();

                    }
                }

                //check of place is already in place_id table
                String checkIfPlacesQuery = "SELECT place_id FROM places WHERE place_id = '" + getPlaceID + "'";

                ResultSet rs1 = conn.createStatement().executeQuery(checkIfPlacesQuery);

                if (!rs1.isBeforeFirst()) { //empty result set - not in favorites

                    if (getFavoriteStatus.equals("1")) {

                        //String query3 = "INSERT INTO places (place_id, place_name, place_address, place_phone, place_website) VALUES ( ?, ?, ?, ?, ? )";
                        String query3 = "INSERT INTO places (place_id, place_lat, place_lng, place_name, place_address,place_phone, place_website, place_email) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                        //String query3 = "INSERT INTO places VALUES ( 'test', '', '', 'test', 'test', 'test', 'test', 'test' )";


                        PreparedStatement stmt1 = conn.prepareStatement(query3);

                        stmt1.setString(1, getPlaceID);
                        stmt1.setString(2, "0.00000");
                        stmt1.setString(3, "0.00000");
                        stmt1.setString(4, getPlaceName);
                        stmt1.setString(5, getPlaceAddress);
                        stmt1.setString(6, getPlacePhone);
                        stmt1.setString(7, getPlaceWebsite);
                        stmt1.setString(8, "");


                        int success = 2;
                        success = stmt1.executeUpdate();

                    }

                }


                //check if place is already in materials_prices
                String checkIfMatTableQuery = "SELECT place_id FROM materials_prices WHERE place_id = '" + getPlaceID + "'";

                ResultSet rs2 = conn.createStatement().executeQuery(checkIfMatTableQuery);

                if (!rs2.isBeforeFirst()) { //empty result set - not in materials_prices table

                    String query4 = "INSERT INTO materials_prices (place_id, material_type, material_reimburse) VALUES ( ?, ?, ? )";


                    PreparedStatement stmt = conn.prepareStatement(query4);
                    stmt.setString(1, getPlaceID);
                    stmt.setString(2, getMaterials);
                    stmt.setString(3, getReimburseStatus);
                    int success = 2;
                    success = stmt.executeUpdate();

                }

                else { //place already in materials_prices table - update materials and reimburse

                    String query5 = "UPDATE materials_prices SET material_type='" + getMaterials + "', material_reimburse='" + getReimburseStatus + "' WHERE place_id='" + getPlaceID + "'";

                    PreparedStatement stmt = conn.prepareStatement(query5);

                    int success = 2;
                    success = stmt.executeUpdate();


                }


                //echo out valid update
                resp.getWriter().println("{\"status\":\"validUpdate\"}");

                conn.close();

            } catch (Exception e) {
                resp.getWriter().println(e.toString());
            }

        }

        if (req.getParameter("function").equals("doRetrievePlaceDetails")) {
            String url = null;
            String getUsername = req.getParameter("username");
            String getPlaceID = req.getParameter("place_id");
            String favoriteStatus = null;
            String materials = null;
            String reimburseStatus = null;

			
            resp.setContentType("text/html");
            
            // Load the class that provides the new "jdbc:google:mysql://" prefix.
            try {
                Class.forName("com.mysql.jdbc.GoogleDriver");
                //format for url is........appname:mysqlinstancename/dbname?user=username&password=passsword
                url = "REDACTED";
				Connection conn = DriverManager.getConnection(url);


                String checkFavoritesQuery = "SELECT place_id FROM favs_comments WHERE username = '" + getUsername + "' AND place_id = '" + getPlaceID + "'";
                ResultSet rs3 = conn.createStatement().executeQuery(checkFavoritesQuery);
                if (!rs3.isBeforeFirst()) {
                    favoriteStatus = "0";
                } else {
                    favoriteStatus = "1";
                }

                String retrieveDetailsQuery = "SELECT * FROM materials_prices WHERE place_id = '" + getPlaceID + "'";
                ResultSet rs4 = conn.createStatement().executeQuery(retrieveDetailsQuery);
                if (!rs4.next()) { //empty result set
                    materials = "";
                    reimburseStatus = "";
                }
                else {  //result found
                    materials = rs4.getString("material_type");
                    reimburseStatus = rs4.getString("material_reimburse");
                }

                resp.getWriter().println("{\"isFavorite\": " + "\"" + favoriteStatus + "\", \"reimburse\": " + "\"" + reimburseStatus + "\", \"materials\": " + "\"" + materials + "\"}");
            } catch (Exception e) {
                resp.getWriter().println(e.toString());
            }

        }


    }
}

