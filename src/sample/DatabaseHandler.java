package sample;

import com.sun.corba.se.spi.orbutil.threadpool.Work;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseHandler extends DB {
    Connection dbConnection;

    public Connection getDbConnection() throws ClassNotFoundException, SQLException {
        String connectionString = "jdbc:mysql://" + db_host + ":" + db_port + "/" + db_name +
                "?characterEncoding=utf8&useConfigs=maxPerformance&";
        Class.forName("com.mysql.jdbc.Driver");
        dbConnection = DriverManager.getConnection(connectionString, db_user, db_pass);
        return dbConnection;
    }

    public void signUpUser(User user) {
        String insert = "INSERT INTO " + DBconst.USER + " (" +
                DBconst.USER_FIRSTNAME + "," + DBconst.USER_NAME + "," + DBconst.USER_MIDDLENAME + "," +
                DBconst.USER_EMAIL + "," + DBconst.USER_PASSWORD + ")" +
                "VALUES(?,?,?,?,?)";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(insert);
            prSt.setString(1, user.getFirstname());
            prSt.setString(2, user.getName());
            prSt.setString(3, user.getMiddlename());
            prSt.setString(4, user.getEmail());
            prSt.setString(5, user.getPassword());
            prSt.executeUpdate();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    public ResultSet getUser(User user) {
        ResultSet resSet = null;
        String select = "SELECT * FROM " + DBconst.USER + " WHERE " +
                DBconst.USER_EMAIL + "=? AND " + DBconst.USER_PASSWORD + "=?";
        try {
            PreparedStatement prSt = getDbConnection().prepareStatement(select);
            prSt.setString(1, user.getEmail());
            prSt.setString(2, user.getPassword());
            resSet = prSt.executeQuery();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        return resSet;
    }

    public ResultSet getProfessions() throws SQLException, ClassNotFoundException {
        String select = "SELECT * FROM " + DBconst.PROFESSIONS;
        PreparedStatement prSt = getDbConnection().prepareStatement(select);
        return prSt.executeQuery();
    }

    public ResultSet getRecordings() throws SQLException, ClassNotFoundException {
        String select = "SELECT * FROM " + DBconst.WORKDATE;
        PreparedStatement prSt = getDbConnection().prepareStatement(select);
        return prSt.executeQuery();
    }

    public ResultSet getRecordings(String prof) throws SQLException, ClassNotFoundException {
        int space1 = prof.indexOf(" ");
        String surname = prof.substring(0, space1);
        int space2 = prof.indexOf(" ", space1 + 1);
        String name = prof.substring(space1 + 1, space2);
        String patr = prof.substring(space2 + 1);
        String select = "SELECT * FROM " + DBconst.EMPLOYEE + " WHERE " +
                DBconst.EMPLOYEE_LASTNAME + "=? AND " + DBconst.EMPLOYEE_NAME + "=? AND " +
                DBconst.EMPLOYEE_MIDDLENAME + "=?";
        PreparedStatement prSt = getDbConnection().prepareStatement(select);
        prSt.setString(1, surname);
        prSt.setString(2, name);
        prSt.setString(3, patr);
        ResultSet resSet = prSt.executeQuery();
        ArrayList id = new ArrayList();
        while (resSet.next())
            id.add(resSet.getString(1));
        select = "SELECT * FROM " + DBconst.WORKDATE + " RIGHT JOIN " + DBconst.EMPLOYEE + " ON " + DBconst.WORKDATE_EMPLOYEE_ID + "=? WHERE " +
                DBconst.EMPLOYEE_ID + "=? AND " + DBconst.WORKDATE_STATUS + "=0";
        prSt = getDbConnection().prepareStatement(select);
        prSt.setString(1, (String) id.get(0));
        prSt.setString(2, (String) id.get(0));
        resSet = prSt.executeQuery();

        return resSet;
    }

    public ResultSet getSpecialists(String proff) throws SQLException, ClassNotFoundException {
        String select = "SELECT * FROM " + DBconst.PROFESSIONS
                + " WHERE " + DBconst.PROFESSIONS_PROFESSIONNAME + "=?";
        PreparedStatement prSt = getDbConnection().prepareStatement(select);
        prSt.setString(1, proff);
        ResultSet resSet = prSt.executeQuery();
        ArrayList proff_id = new ArrayList();
        while (resSet.next())
            proff_id.add(resSet.getString(1));
        select = "SELECT * FROM " + DBconst.EMPLOYEE + " RIGHT JOIN " + DBconst.PROFESSIONS + " ON " + DBconst.EMPLOYEE_PROFESSIONS_ID + "=? WHERE " +
                DBconst.PROFESSIONS_PROFESSIONNAME + "=?";
        prSt = getDbConnection().prepareStatement(select);
        prSt.setString(1, (String) proff_id.get(0));
        prSt.setString(2, proff);
        resSet = prSt.executeQuery();
        return resSet;
    }

    public void insertRecord(String prof, String us_id, String work_dt) throws SQLException, ClassNotFoundException {
        int space1 = prof.indexOf(" ");
        String surname = prof.substring(0, space1);
        int space2 = prof.indexOf(" ", space1 + 1);
        String name = prof.substring(space1 + 1, space2);
        String patr = prof.substring(space2 + 1);
        String select = "SELECT * FROM " + DBconst.EMPLOYEE + " WHERE " +
                DBconst.EMPLOYEE_LASTNAME + "=? AND " + DBconst.EMPLOYEE_NAME + "=? AND " +
                DBconst.EMPLOYEE_MIDDLENAME + "=?";
        PreparedStatement prSt = getDbConnection().prepareStatement(select);
        prSt.setString(1, surname);
        prSt.setString(2, name);
        prSt.setString(3, patr);
        ResultSet resSet = prSt.executeQuery();
        ArrayList emp_id = new ArrayList();
        while (resSet.next()) {
            emp_id.add(resSet.getString(1));
        }
        space1 = work_dt.indexOf(" ");
        String date = work_dt.substring(0, space1);
        String time = work_dt.substring(space1 + 1);
//        String insert = "INSERT INTO " + DBconst.RECEPTION + " ("
//                + DBconst.RECEPTION_EMPLOYEE_ID + "," + DBconst.RECEPTION_USER_ID + ","
//                + DBconst.RECEPTION_WORKDATE_DATE + "," + DBconst.RECEPTION_WORKDATE_TIME + ","
//                + DBconst.RECEPTION_WORKDATE_EMPLOYEE_ID + ") VALUES(?,?,?,?,?)";
//        prSt.setString(1, (String) emp_id.get(0));
//        prSt.setString(2, us_id);
//        prSt.setString(3, date);
//        prSt.setString(4, time);
//        prSt.setString(5, (String) emp_id.get(0));
        String insert = "INSERT INTO " + DBconst.RECEPTION + " ("
                + DBconst.RECEPTION_EMPLOYEE_ID + ","
                + DBconst.RECEPTION_WORKDATE_DATE + "," + DBconst.RECEPTION_WORKDATE_TIME +
                "," + DBconst.RECEPTION_USER_ID + ") VALUES(?,?,?,?)";
        prSt = getDbConnection().prepareStatement(insert);
        prSt.setString(1, (String) emp_id.get(0));
        prSt.setString(2, date);
        prSt.setString(3, time);
        prSt.setString(4, us_id);
        prSt.executeUpdate();
        String update = "UPDATE " + DBconst.WORKDATE + " SET " + DBconst.WORKDATE_STATUS + "='1'"
                + " WHERE " + DBconst.WORKDATE_EMPLOYEE_ID + "=? AND " + DBconst.WORKDATE_DATE +
                "=? AND " + DBconst.WORKDATE_TIME + "=?";
        prSt = getDbConnection().prepareStatement(update);
        prSt.setString(1, (String) emp_id.get(0));
        prSt.setString(2, date);
        prSt.setString(3, time);
        prSt.executeUpdate();
    }

    public void updateUser(String sn, String n, String patr, String m, String pass) throws SQLException, ClassNotFoundException {
        String update = "UPDATE " + DBconst.USER + " SET " + DBconst.USER_FIRSTNAME + "=?, "
                + DBconst.USER_NAME + "=?, " + DBconst.USER_MIDDLENAME + "=?, "
                + DBconst.USER_EMAIL + "=?, " + DBconst.USER_PASSWORD + "=?"
                + " WHERE " + DBconst.USER_ID + "=?";
        PreparedStatement prSt = getDbConnection().prepareStatement(update);
        prSt.setString(1, sn);
        prSt.setString(2, n);
        prSt.setString(3, patr);
        prSt.setString(4, m);
        prSt.setString(5, pass);
        prSt.setString(6, DBconst.CURRENT_USER);
        prSt.executeUpdate();
    }

    public void deleteUser() throws SQLException, ClassNotFoundException {
        String delete = "DELETE FROM " + DBconst.USER + " WHERE " + DBconst.USER_ID + "=?";
        PreparedStatement prSt = getDbConnection().prepareStatement(delete);
        prSt.setString(1, DBconst.CURRENT_USER);
        DBconst.CURRENT_USER = String.valueOf(-1);
        prSt.executeUpdate();
    }

    public ResultSet myRec() throws SQLException, ClassNotFoundException {
        String select = "SELECT " + DBconst.RECEPTION_WORKDATE_DATE + "," + DBconst.RECEPTION_WORKDATE_TIME + "," +
                DBconst.PROFESSIONS_PROFESSIONNAME + " FROM " + DBconst.RECEPTION + "," + DBconst.EMPLOYEE + "," +
                DBconst.PROFESSIONS + " WHERE " + DBconst.RECEPTION_USER_ID + "=? AND "
                + DBconst.RECEPTION_EMPLOYEE_ID + "=" + DBconst.EMPLOYEE + "." + DBconst.EMPLOYEE_ID + " AND " +
                DBconst.EMPLOYEE_PROFESSIONS_ID + "=" + DBconst.PROFESSIONS + "." + DBconst.PROFESSIONS_ID;
        PreparedStatement prSt = getDbConnection().prepareStatement(select);
        prSt.setString(1, DBconst.CURRENT_USER);
        return prSt.executeQuery();
    }

    public ResultSet dataInPA() throws SQLException, ClassNotFoundException {
        String select = "SELECT * FROM " + DBconst.USER + " WHERE " + DBconst.USER_ID + "=?";
        PreparedStatement prSt = getDbConnection().prepareStatement(select);
        prSt.setString(1, DBconst.CURRENT_USER);
        return prSt.executeQuery();
    }

    public ResultSet recInEdit() throws SQLException, ClassNotFoundException {
        String select = "SELECT " + DBconst.RECEPTION_WORKDATE_DATE + "," + DBconst.RECEPTION_WORKDATE_TIME + "," +
                DBconst.EMPLOYEE_LASTNAME + "," + DBconst.EMPLOYEE_NAME + "," + DBconst.EMPLOYEE_MIDDLENAME + " FROM " +
                DBconst.RECEPTION + "," + DBconst.EMPLOYEE + "," + DBconst.PROFESSIONS + " WHERE " +
                DBconst.RECEPTION_USER_ID + "=? AND " + DBconst.RECEPTION_EMPLOYEE_ID + "=" + DBconst.EMPLOYEE + "." +
                DBconst.EMPLOYEE_ID + " AND " + DBconst.EMPLOYEE_PROFESSIONS_ID + "=" + DBconst.PROFESSIONS + "." + DBconst.PROFESSIONS_ID +
                " AND " + DBconst.RECEPTION_WORKDATE_DATE + "=? AND " + DBconst.RECEPTION_WORKDATE_TIME + "=?";
        PreparedStatement prSt = getDbConnection().prepareStatement(select);
        prSt.setString(1, DBconst.CURRENT_USER);
        prSt.setString(2, Controller_PersonalAccount.date);
        prSt.setString(3, Controller_PersonalAccount.time);
        return prSt.executeQuery();
    }


    public void removeRecord(String prof, String work_dt) throws SQLException, ClassNotFoundException {
        int space1 = prof.indexOf(" ");
        String surname = prof.substring(0, space1);
        int space2 = prof.indexOf(" ", space1 + 1);
        String name = prof.substring(space1 + 1, space2);
        String patr = prof.substring(space2 + 1);
        String select = "SELECT * FROM " + DBconst.EMPLOYEE + " WHERE " +
                DBconst.EMPLOYEE_LASTNAME + "=? AND " + DBconst.EMPLOYEE_NAME + "=? AND " +
                DBconst.EMPLOYEE_MIDDLENAME + "=?";
        PreparedStatement prSt = getDbConnection().prepareStatement(select);
        prSt.setString(1, surname);
        prSt.setString(2, name);
        prSt.setString(3, patr);
        ResultSet resSet = prSt.executeQuery();
        ArrayList emp_id = new ArrayList();
        while (resSet.next()) {
            emp_id.add(resSet.getString(1));
        }
        space1 = work_dt.indexOf(" ");
        String date = work_dt.substring(0, space1);
        String time = work_dt.substring(space1 + 1);
        String delete = "DELETE FROM " + DBconst.RECEPTION + " WHERE " +
                DBconst.RECEPTION_USER_ID + "=? AND " + DBconst.RECEPTION_WORKDATE_DATE + "=? AND " +
                DBconst.RECEPTION_WORKDATE_TIME + "=?";
        prSt = getDbConnection().prepareStatement(delete);
        prSt.setString(1, DBconst.CURRENT_USER);
        prSt.setString(2, date);
        prSt.setString(3, time);
        prSt.executeUpdate();
        String update = "UPDATE " + DBconst.WORKDATE + " SET " + DBconst.WORKDATE_STATUS + "='0'"
                + " WHERE " + DBconst.WORKDATE_EMPLOYEE_ID + "=? AND " + DBconst.WORKDATE_DATE +
                "=? AND " + DBconst.WORKDATE_TIME + "=?";
        prSt = getDbConnection().prepareStatement(update);
        prSt.setString(1, (String) emp_id.get(0));
        prSt.setString(2, date);
        prSt.setString(3, time);
        prSt.executeUpdate();
    }

    public ResultSet getDescription(String proff) throws SQLException, ClassNotFoundException {
        String select = "SELECT * FROM " + DBconst.PROFESSIONS + " WHERE " +
                DBconst.PROFESSIONS_PROFESSIONNAME + "=?";
        PreparedStatement prSt = getDbConnection().prepareStatement(select);
        prSt.setString(1, proff);
        return prSt.executeQuery();
    }
}
