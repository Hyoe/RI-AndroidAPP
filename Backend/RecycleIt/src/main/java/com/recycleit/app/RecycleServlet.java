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
                url = "jdbc:google:mysql://recycleit-1293:recycle-it-sql/Recycle_It?user=root&password=#kickascii";
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
            //http://recycleit-1293.appspot.com/test?function=doRegister&username=test

            // Load the class that provides the new "jdbc:google:mysql://" prefix.
            try {
                Class.forName("com.mysql.jdbc.GoogleDriver");
                //format for url is........appname:mysqlinstancename/dbname?user=username&password=passsword
                url = "jdbc:google:mysql://recycleit-1293:recycle-it-sql/Recycle_It?user=root&password=#kickascii";
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
            //http://recycleit-1293.appspot.com/test?function=doRegister&username=test

            // Load the class that provides the new "jdbc:google:mysql://" prefix.
            try {
                Class.forName("com.mysql.jdbc.GoogleDriver");
                //format for url is........appname:mysqlinstancename/dbname?user=username&password=passsword
                url = "jdbc:google:mysql://recycleit-1293:recycle-it-sql/Recycle_It?user=root&password=#kickascii";
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
        if (req.getParameter("function").equals("updateFavorite")) {
            String url = null;
            String getUsername = req.getParameter("username");
            String getPlaceID = req.getParameter("place_id");
            String getFavoriteStatus = req.getParameter("favorite_checked");

            resp.setContentType("text/html");
            //test url user without the favorite place (to add):  http://recycleit-1293.appspot.com/test?function=updateFavorite&username=test&place_id=FAKEPLACEX&favorite_checked=1
            //test url user with the favorite place (to delete):  http://recycleit-1293.appspot.com/test?function=updateFavorite&username=test&place_id=FAKEPLACE4&favorite_checked=0


            try {
                Class.forName("com.mysql.jdbc.GoogleDriver");
                //format for url is........appname:mysqlinstancename/dbname?user=username&place_id=place_id
                url = "jdbc:google:mysql://recycleit-1293:recycle-it-sql/Recycle_It?user=root&password=#kickascii";
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

                        if (success == 1) {
                            //echo out valid update
                            resp.getWriter().println("{\"status\":\"validUpdate\"}");
                        } else if (success == 0) {
                            resp.getWriter().println("{\"status\":\"databaseError\"}");
                        }
                    }

                }


                else { //not empty result set
                    if (getFavoriteStatus.equals("0")) {

                        String query2 = "DELETE FROM favs_comments WHERE username = '" + getUsername + "' AND place_id = '" + getPlaceID + "'";

                        PreparedStatement stmt = conn.prepareStatement(query2);

                        int success = 2;
                        success = stmt.executeUpdate();

                        if (success == 1) {
                            //echo out valid update
                            resp.getWriter().println("{\"status\":\"validUpdate\"}");
                        } else if (success == 0) {
                            resp.getWriter().println("{\"status\":\"databaseError\"}");
                        }

                    }
                }


                conn.close();

            } catch (Exception e) {
                resp.getWriter().println(e.toString());
            }

        }



    }
}

