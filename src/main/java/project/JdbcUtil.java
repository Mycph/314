package project;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

//import org.json.JSONArray;

//import org.json.JSONObject;

import javax.xml.crypto.Data;


//test
public class JdbcUtil {
    public static Connection connectSql() throws ClassNotFoundException, SQLException {

        // MySQL 8.0
        final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
        final String DB_URL      = "jdbc:mysql://localhost/csit314?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";

        final String USER = "william";
        final String PASS = "123456";

        Connection conn = null;
        Statement  stmt = null;

        Class.forName(JDBC_DRIVER);


        System.out.println("connecting...");
        conn = DriverManager.getConnection(DB_URL, USER, PASS);
        return conn;
    }


    //sql select
    //TODO check the password!!! dont forget

    public static Map sqlCusSelect(int userID) throws SQLException, ClassNotFoundException {
        Connection        conn = connectSql();
        String            sql  = "select * from CUSTOMER where cusNum = ?;";
        PreparedStatement psmt = conn.prepareStatement(sql);
        psmt.setInt(1, userID);
        ResultSet rs     = psmt.executeQuery();
        Map<String,String> res = new HashMap<String, String>();

        while (rs.next()) {
            res.put("cusNum", String.valueOf(rs.getInt("cusNum")));
            res.put("cusName",rs.getString("cusName"));
            res.put("gender",rs.getString("gender"));
            res.put("cusDOB",rs.getString("cusDOB"));
            res.put("phoneNum",rs.getString("phoneNum"));
            res.put("cusPw",rs.getString("cusPw"));
            res.put("email",rs.getString("email"));
            res.put("vipStart",rs.getString("vipStart"));
            res.put("vipEnd",rs.getString("vipEnd"));
            res.put("vehicleModel",rs.getString("vehicleModel"));
            res.put("plateNum",rs.getString("plateNum"));
        }
        System.out.println(res);
        return res;
    }

    public static Map sqlProSelect(int userID) throws SQLException, ClassNotFoundException {
        Connection conn = connectSql();
        String            sql  = "select * from PROFESSIONAL where proNum = ?;";
        PreparedStatement psmt = conn.prepareStatement(sql);
        psmt.setInt(1, userID);
        ResultSet    rs     = psmt.executeQuery();
        Map<String,String> res = new HashMap<String, String>();
        while (rs.next()) {
            res.put("proNum", String.valueOf(rs.getInt("proNum")));
            res.put("proName", rs.getString("proName"));
            res.put("gender", rs.getString("gender"));
            res.put("proDOB", rs.getString("proDOB"));
            res.put("phoneNum", rs.getString("phoneNum"));
            res.put("proPw", rs.getString("proPw"));
            res.put("email", rs.getString("email"));
            res.put("pLevel", String.valueOf(rs.getFloat("pLevel")));
            res.put("balance", String.valueOf(rs.getFloat("balance")));
            res.put("location", rs.getString("location"));
        }
        System.out.println(res);
        return res;
    }
    public static Map sqlCusLoginSelect(String username) throws SQLException, ClassNotFoundException {
        Connection conn = connectSql();
        System.out.println("name = "+username);
        String            sql  = "select cusPw,cusNum,vipEnd from CUSTOMER where cusName = ?;";
        PreparedStatement psmt = conn.prepareStatement(sql);
        psmt.setString(1, username);
        ResultSet    rs     = psmt.executeQuery();
        Map<String,String> res = new HashMap<String, String>();
        Map<String,String> orders= new HashMap<String, String>();
        while (rs.next()) {
            res.put("cusPw",rs.getString("cusPw"));
            res.put("cusNum",rs.getString("cusNum"));
            res.put("vipEnd",rs.getString("vipEnd"));
        }
        return res;
    }

    public static Map sqlProLoginSelect(String username) throws SQLException, ClassNotFoundException {
        Connection conn = connectSql();

        String            sql  = "select proPw,proNum from PROFESSIONAL where proName = ?;";
        PreparedStatement psmt = conn.prepareStatement(sql);
        psmt.setString(1, username);
        ResultSet    rs     = psmt.executeQuery();
        Map<String,String> res = new HashMap<String, String>();
        while (rs.next()) {
            res.put("proPw", rs.getString("proPw"));
            res.put("proNum",rs.getString("proNum"));

        }
        return res;
    }
    public static Map sqlcurOrderIdSelect(int  userId) throws SQLException, ClassNotFoundException {
        Connection conn = connectSql();

        String            sql  = "select * from cur_orders where O_cusNum = ?;";
        PreparedStatement psmt = conn.prepareStatement(sql);
        psmt.setInt(1, userId);
        ResultSet    rs     = psmt.executeQuery();
        Map<String,String> res = new HashMap<String, String>();
        while (rs.next()) {
            res.put("oid", rs.getString("cur_orderid"));
        }
        return res;
    }
    public static Map sqlcurOrderSelect(String oid) throws SQLException, ClassNotFoundException {
        Connection conn = connectSql();

        String            sql  = "select * from cur_orders where cur_orderid = ?;";
        PreparedStatement psmt = conn.prepareStatement(sql);
        psmt.setInt(1, Integer.parseInt(oid));
        ResultSet    rs     = psmt.executeQuery();
        Map<String,String> res = new HashMap<String, String>();
        while (rs.next()) {
            res.put("cur_orderid", rs.getString("cur_orderid"));
            res.put("orderStartDate",rs.getString("orderStartDate"));
            res.put("vehiclePlate", rs.getString("vehiclePlate"));
            res.put("EstimatePayment",rs.getString("EstimatePayment"));
            res.put("c_location", rs.getString("c_location"));
            res.put("issue",rs.getString("issue"));
            res.put("O_cusNum",rs.getString("O_cusNum"));
            res.put("O_proNum",rs.getString("O_proNum"));
            res.put("sstate",rs.getString("sstate"));
            res.put("isVip",rs.getString("isVip"));
        }
        return res;
    }
    public static String sqlCurrOrderSelect(String address) throws SQLException, ClassNotFoundException {
        Connection        conn   = connectSql();
        String            sql    = "select * from cur_orders,CUSTOMER where sstate = 'waiting' and cur_orders.O_cusNum = CUSTOMER.cusNum;";
        PreparedStatement psmt   = conn.prepareStatement(sql);
        ResultSet  rs     = psmt.executeQuery();
        Map<String, String> res = new LinkedHashMap<String,String>();
        String temp = "[";
        while (rs.next()){
            double dis = getDistance(address,rs.getString("c_location"));
            if (dis <= 50){
                res.put("curorder",rs.getString("cur_orderid"));
                res.put("cusName",rs.getString("cusName"));
                res.put("issue",rs.getString("issue"));
                res.put("distance", String.valueOf(dis));
                String temp1 = JSONLIKE.myMap2JSON(res);
                temp += temp1+",";
            }
        }
        temp = temp.substring(0,temp.length()-1);
        temp +="]";
        System.out.println("this is JDBC: "+temp);
        return temp;
    }
    public static void sqlSetEstimatePayment(String oid,String coordinates) throws SQLException, ClassNotFoundException {
        Connection conn = connectSql();

        String            sql  = "select * from cur_orders where cur_orderid =?;";
        PreparedStatement psmt = conn.prepareStatement(sql);
        psmt.setString(1,oid);
        ResultSet    rs     = psmt.executeQuery();
        String location = "";
        while(rs.next()){
            location = rs.getString("c_location");
        }
        // 上面的通了
        System.out.println("this is oid: "+oid);
        double price = setPrice(getDistance(coordinates,location));
        float temp = (float)price;
        // sql 语句不通
        String newsql = "update cur_orders set EstimatePayment = ? where cur_orderid = ?;";
        System.out.println("this is price: "+  temp);
        Connection conn1 = connectSql();
        PreparedStatement psmt1   = conn1.prepareStatement(newsql);
        psmt1.setFloat(1,temp);
        psmt1.setInt(2,Integer.parseInt(oid));
        psmt1.execute();
    }
    public static double setPrice(double distance){
        return distance*15+30;
    }
    public static Map sqlCurrentRequest(String address, String requestID) throws SQLException, ClassNotFoundException {
        Connection        conn   = connectSql();
        String            sql    = "select * from cur_orders,CUSTOMER where cur_orderid = ? and CUSTOMER.cusNum = cur_orders.O_cusNum;";
        PreparedStatement psmt   = conn.prepareStatement(sql);
        psmt.setString(1,requestID);
        ResultSet  rs     = psmt.executeQuery();
        Map<String, String> res = new LinkedHashMap<String,String>();
        while (rs.next()){
                res.put("curorder",rs.getString("cur_orderid"));
                res.put("O_cusNum",rs.getString("O_cusNum"));
                res.put("cusName",rs.getString("cusName"));
                res.put("c_location",rs.getString("c_location"));
                res.put("issue",rs.getString("issue"));
                res.put("vehiclePlate",rs.getString("vehiclePlate"));
        }
        return res;
    }
    private static double rad(double d){
        return d * Math.PI / 180.0;
    }
    public static double getDistance(String ca, String pa){
        final  double EARTH_RADIUS = 6378.137;
        String[] caset = ca.split("#");
        double clon = Double.parseDouble(caset[0]);
        double clat = Double.parseDouble(caset[1]);
        String[] paset = pa.split("#");
        double plon = Double.parseDouble(paset[0]);
        double plat = Double.parseDouble(paset[1]);
        double radLat1 = rad(clat);
        double radLat2 = rad(plat);
        double a = radLat1 - radLat2;
        double b = rad(clon) - rad(plon);
        double s = 2 *Math.asin(Math.sqrt(Math.pow(Math.sin(a/2),2)+Math.cos(radLat1)*Math.cos(radLat2)*Math.pow(Math.sin(b/2),2)));
        s = s * EARTH_RADIUS;
        return s;
    }
    public static String sqlOrderSelect(String userNum) throws SQLException, ClassNotFoundException {
        Connection        conn   = connectSql();
        String            sql    = "select * from orders where O_cusNum = ?";
        PreparedStatement psmt   = conn.prepareStatement(sql);
        psmt.setString(1, userNum);
        ResultSet  rs     = psmt.executeQuery();
        String res = "";
        while (rs.next()) {
            res = rs.getString(1);
        }
        return res;
    }


    //sql insert customer
    public static void sqlCusInsert(String username, String password, String DOB, String email, String phoneNum, String gender,String plateNum,String model) throws SQLException, ClassNotFoundException {
        Connection con = connectSql();
        String     sql = "";
        sql = "insert into CUSTOMER (cusPw,cusName,cusDOB,phoneNum,vipStart,vipEnd,email,gender,plateNum,vehicleModel) values (?,?,?,?,?,?,?,?,?,?);";
        PreparedStatement psmt = con.prepareStatement(sql);
        psmt.setString(1, password);
        psmt.setString(2, username);
        psmt.setString(3, DOB);
        psmt.setString(4, phoneNum);
        psmt.setString(5, "2022-01-01");
        psmt.setString(6, "2022-01-01");
        psmt.setString(7, email);
        psmt.setString(8, gender);
        psmt.setString(9, plateNum);
        psmt.setString(10, model);
        psmt.execute();
        con.close();
    }

    //sql insert professional
    public static void sqlProInsert(String username, String password, String DOB, String email, String phoneNum, String gender, String location) throws SQLException, ClassNotFoundException {
        Connection        con  = connectSql();
        String            sql  = "insert into PROFESSIONAL (proPw,proName,proDOB,phoneNum,pLevel,balance,location,email,gender) values (?,?,?,?,?,?,?,?,?);";
        PreparedStatement psmt = con.prepareStatement(sql);
        psmt.setString(1, password);
        psmt.setString(2, username);
        psmt.setString(3, DOB);
        psmt.setString(4, phoneNum);
        psmt.setFloat(5, 5);
        psmt.setFloat(6, 0);
        psmt.setString(7, location);
        psmt.setString(8, email);
        psmt.setString(9, gender);
        psmt.execute();
        con.close();
    }
    public static String sqlCusOrderSelect(String O_cusNum) throws SQLException, ClassNotFoundException {
        Connection        conn   = connectSql();
        String            sql    = "select * from orders where O_cusNum = ?";
        PreparedStatement psmt   = conn.prepareStatement(sql);
        psmt.setString(1, O_cusNum);
        ResultSet  rs     = psmt.executeQuery();
        Map<String,String> res = new LinkedHashMap<String, String>();
        String temp1 ="[";
        while (rs.next()) {
            res.put("orderid", String.valueOf(rs.getInt("orderid")));
            res.put("orderStartDate",rs.getString("orderStartDate"));
            res.put("vehiclePlate",rs.getString("vehiclePlate"));
            res.put("location",rs.getString("location"));
            res.put("issue",rs.getString("issue"));
            res.put("O_cusNum",rs.getString("O_cusNum"));
            res.put("O_proNum",rs.getString("O_proNum"));
            res.put("OrderEndDate",rs.getString("OrderEndDate"));
            res.put("review",rs.getString("review"));
            res.put("rating",rs.getString("rating"));
            res.put("payCardNum",rs.getString("payCardNum"));
            res.put("payType",rs.getString("payType"));
            res.put("EstimatePayment",rs.getString("EstimatePayment"));
            res.put("actualPayment",rs.getString("actualPayment"));

            String temp = JSONLIKE.myMap2JSON(res);
            temp1 += temp+",";
        }
        temp1=temp1.substring(0,temp1.length()-1);
        temp1+="]";
        System.out.println("sqlCusOrderSelect");
        System.out.println(temp1);
        return temp1;
    }
    public static String sqlProOrderSelect(String O_proNum) throws SQLException, ClassNotFoundException {
        Connection        conn   = connectSql();
        String            sql    = "select * from orders where O_proNum = ?";
        PreparedStatement psmt   = conn.prepareStatement(sql);
        psmt.setString(1, O_proNum);
        ResultSet  rs     = psmt.executeQuery();
        Map<String,String> res = new LinkedHashMap<String, String>();
        String temp1 ="[";
        while (rs.next()) {
            res.put("orderid", String.valueOf(rs.getInt("orderid")));
            res.put("orderStartDate",rs.getString("orderStartDate"));
            res.put("vehiclePlate",rs.getString("vehiclePlate"));
            res.put("location",rs.getString("location"));
            res.put("issue",rs.getString("issue"));
            res.put("O_cusNum",rs.getString("O_cusNum"));
            res.put("O_proNum",rs.getString("O_proNum"));
            res.put("OrderEndDate",rs.getString("OrderEndDate"));
            res.put("review",rs.getString("review"));
            res.put("rating",rs.getString("rating"));
            res.put("payCardNum",rs.getString("payCardNum"));
            res.put("payType",rs.getString("payType"));
            res.put("EstimatePayment",rs.getString("EstimatePayment"));
            res.put("actualPayment",rs.getString("actualPayment"));
            String temp = JSONLIKE.myMap2JSON(res);
            temp1 += temp+",";
        }
        temp1=temp1.substring(0,temp1.length()-1);
        temp1+="]";
        System.out.println("sqlCusOrderSelect");
        System.out.println(temp1);
        return temp1;
    }
    public static void sqlCurrOrderInsert(String orderStartDate, String vehiclePlate, String c_location, String issue, String O_cusNum, String isVip) throws SQLException, ClassNotFoundException {
        Connection        con  = connectSql();
        String state = "waiting";
        String            sql  = "insert into cur_orders (orderStartDate,vehiclePlate,c_location,issue,O_cusNum,sstate,isVIP) values (?,?,?,?,?,?,?);";
        PreparedStatement psmt = con.prepareStatement(sql);
        psmt.setString(1, orderStartDate);
        psmt.setString(2, vehiclePlate);
        psmt.setString(3, c_location);
        psmt.setString(4, issue);
        psmt.setString(5, O_cusNum);
        psmt.setString(6, state);
        psmt.setString(7, isVip);

        psmt.execute();
        con.close();
    }

    public static void sqlOrderInsert(Map<String,String> map) throws SQLException, ClassNotFoundException {
        Connection        con  = connectSql();
        String oid = map.get("orderid");
        String payType = map.get("payType");
        String payCardNum = map.get("payCardNum");
        float rating = Float.parseFloat(map.get("star"));
        String review = map.get("comm");
        String orderEndDate = map.get("orderEndDate");
        String  orderStartDate = map.get("orderStartDate");
        String  vehiclePlate = map.get("vehiclePlate");
        String  location = map.get("c_location");
        String  issue = map.get("issue");
        String  O_cusNum = map.get("O_cusNum");
        String  O_proNum = map.get("O_proNum");
        String EstimatePayment = map.get("EstimatePayment");
        String actualPayment = map.get("actualPayment");
        String            sql  = "insert into orders values (?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
        PreparedStatement psmt = con.prepareStatement(sql);
        psmt.setString(1, oid);
        psmt.setString(2, orderStartDate);
        psmt.setString(3, vehiclePlate);
        psmt.setString(4, location);
        psmt.setString(5, issue);
        psmt.setString(6, O_cusNum);
        psmt.setString(7, O_proNum);
        psmt.setString(8, orderEndDate);
        psmt.setString(9, review);
        psmt.setFloat(10, rating);
        psmt.setString(11, payCardNum);
        psmt.setString(12, payType);
        psmt.setString(13, EstimatePayment);
        psmt.setString(14, actualPayment);
        System.out.println("database");
        psmt.execute();
        con.close();
    }

    //sql insert vehicle
    public static void sqlVehInsert(int userID,String plateNum,String model ) throws SQLException, ClassNotFoundException {
        Connection con = connectSql();
        String            sql         = "insert into VEHICLE (cusNum,plateNum,model) values (?,?,?)";
        PreparedStatement psmt        = con.prepareStatement(sql);
        psmt.setInt(1, userID);
        psmt.setString(2, plateNum);
        psmt.setString(3, model);
        con.close();
    }

//    //sql insert order
//    public static void sqlOrdInsert(String x) throws SQLException, ClassNotFoundException {
//        Connection con = connectSql();
//
//        String            sql            = "insert into ORDERS (orderID,orderStartDate,customerID,price,vehiclePlate,location,issue,professional,orderEndDate,review,rating,payCardNum,payType) values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
//        PreparedStatement psmt           = con.prepareStatement(sql);
//        int               columnOfSql    = 1;
//        psmt.setString(columnOfSql, orderID);
//        psmt.setString(columnOfSql++, orderStartDate);
//        psmt.setInt(columnOfSql++, customerID.getUserID());
//        psmt.setDouble(columnOfSql++, price);
//        psmt.setString(columnOfSql++, vehiclePlate.getPlateNum());
//        psmt.setString(columnOfSql++, location);
//        psmt.setString(columnOfSql++, issue);
//        psmt.setInt(columnOfSql++, professional.getUserID());
//        psmt.setString(columnOfSql++, orderEndDate);
//        psmt.setString(columnOfSql++, review);
//        psmt.setFloat(columnOfSql++, rating);
//        psmt.setString(columnOfSql++, payCardNum);
//        psmt.setString(columnOfSql++, String.valueOf(payType));
//
//        con.close();
//    }

    //sql update customer
    public static void updateCustomer(Map<String, String> map) throws SQLException, ClassNotFoundException {
        Connection con = connectSql();
        int    userID   = Integer.parseInt(map.get("uid"));
        String userName = map.get("cusName");
        String gender   = map.get("gender");
        String DOB      = map.get("dob");
        String phoneNum = map.get("phone");
        String password = map.get("cusPw");
        String email    = map.get("email");
        String plateNum = map.get("plateNum");
        String vehicleModel = map.get("model");
        System.out.println("New_userName = "+userName);
        String sql = "" +
                     "update CUSTOMER " +
                     "set cusName=?,gender=?,cusDOB=?,phoneNum=?,cusPw=?,email=?,plateNum=?,vehicleModel=?" +
                     "where cusNum=?";
        //预编译sql语句
        PreparedStatement psmt        = con.prepareStatement(sql);
        //先对应SQL语句，给SQL语句传递参数
        psmt.setInt(9, userID);
        psmt.setString(1, userName);
        psmt.setString(2, gender);
        psmt.setString(3, DOB);
        psmt.setString(4, phoneNum);
        psmt.setString(5, password);
        psmt.setString(6, email);
        psmt.setString(7, plateNum);
        psmt.setString(8, vehicleModel);
        //执行SQL语句
        psmt.execute();
    }
    public static void updateCurrentOrder(String oid,String pid,String sstate) throws SQLException, ClassNotFoundException {
        Connection con = connectSql();
        String sql = "update cur_orders " +
                "set O_proNum=? ,sstate = ?" +
                "where cur_orderid=?";
        //预编译sql语句
        PreparedStatement psmt        = con.prepareStatement(sql);
        //先对应SQL语句，给SQL语句传递参数
        psmt.setString(1, pid);
        psmt.setString(2, sstate);
        psmt.setString(3, oid);
        //执行SQL语句
        psmt.execute();
    }

    //sql update Professional
    public static void updateProfessional(Map<String,String> map) throws SQLException, ClassNotFoundException {
        Connection con = connectSql();
        System.out.println("updateProfessional");
        System.out.println(map);
        int    userID   = Integer.parseInt(map.get("uid"));
        String userName = map.get("proName");
        String gender   = map.get("gender");
        String DOB      = map.get("proDOB");
        String phoneNum = map.get("phoneNum");
        String password = map.get("proPw");
        String email    = map.get("email");
        String location = map.get("location");
        String sql = "" +
                     "update PROFESSIONAL " +
                     "set proName=?,gender=?,proDOB=?,phoneNum=?,proPw=?,email=?,location=?" +
                     "where proNum=?";

        PreparedStatement psmt        = con.prepareStatement(sql);
        psmt.setInt(8, userID);
        psmt.setString(1, userName);
        psmt.setString(2, gender);
        psmt.setString(3, DOB);
        psmt.setString(4, phoneNum);
        psmt.setString(5, password);
        psmt.setString(6, email);
        psmt.setString(7, location);

        psmt.execute();
    }

    //sql delete
//    public boolean sqlDeleteVehicle(JSONObject ob) throws SQLException, ClassNotFoundException {
//        Connection        conn     = connectSql();
//        int               cusID    = ob.getInt("cusID");
//        String            plantNum = ob.getString("plantNum");
//        String            sql      = "delete * from VEHICLE where cusID = ? and plantNum = ?";
//        PreparedStatement psmt     = conn.prepareStatement(sql);
//        psmt.setInt(1, cusID);
//        psmt.setString(2, plantNum);
//        return psmt.execute();
//    }


    //TODO
    //sql语句查询最后一个已存在的用户ID
    public int getNewID() throws SQLException, ClassNotFoundException {
        Connection        conn  = connectSql();
        String            sql   = "select MAX(cusNum) from CUSTOMER;";
        PreparedStatement psmt  = conn.prepareStatement(sql);
        ResultSet         rs    = psmt.executeQuery();
        int               newID = rs.getInt("cusID");
        newID++;
        return newID;
    }

    public static java.sql.Date toSqlData(String data) {
        SimpleDateFormat sdf   = new SimpleDateFormat("yyyy-MM-dd");
        java.sql.Date    sdate = null; //初始化
        try {
            java.util.Date udate = sdf.parse(data);
            sdate = new java.sql.Date(udate.getTime()); //2013-01-14
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return sdate;
    }

    public static void sqlUpdateVIP(String uid, String date) throws SQLException, ClassNotFoundException {
        Connection con = connectSql();
        String            sql         = "update CUSTOMER set vipEnd=? where cusNum=?";
        PreparedStatement psmt        = con.prepareStatement(sql);
        psmt.setString(1, date);
        psmt.setString(2, uid);
        psmt.execute();
        con.close();
    }
}
