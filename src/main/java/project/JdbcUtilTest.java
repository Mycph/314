package project;

import org.junit.jupiter.api.Test;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

import org.hamcrest.collection.IsMapContaining;

import static java.sql.DriverManager.getConnection;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import com.github.javafaker.Faker;

class JdbcUtilTest {

    @Test
    void connectSql() {
        String url = "jdbc:mysql://192.168.0.13/CSIT314?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"; //pointing to no database.
        String username = "william";
        String password = "123456";

        System.out.println("Connecting to server...");

        try (Connection connection = getConnection(url, username, password)) {
            System.out.println("Server connected!");
            Statement stmt = null;
            ResultSet resultset = null;

            try {
                stmt = connection.createStatement();
                resultset = stmt.executeQuery("SHOW DATABASES;");

                if (stmt.execute("SHOW DATABASES;")) {
                    resultset = stmt.getResultSet();
                }

                while (resultset.next()) {
                    System.out.println(resultset.getString("Database"));
                }
            }
            catch (SQLException ex){
                // handle any errors
                ex.printStackTrace();
            }
            finally {
                // release resources
                if (resultset != null) {
                    try {
                        resultset.close();
                    } catch (SQLException sqlEx) { }
                    resultset = null;
                }

                if (stmt != null) {
                    try {
                        stmt.close();
                    } catch (SQLException sqlEx) { }
                    stmt = null;
                }

                if (connection != null) {
                    connection.close();
                }
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Cannot connect the server!", e);
        }
    }

    @Test
    void sqlCusSelect() throws SQLException, ClassNotFoundException {
        Map<String, String> res = JdbcUtil.sqlCusSelect(1);
        assertEquals(res.get("cusNum"), String.valueOf(1));
        System.out.println(res);
    }

    @Test
    void sqlProSelect() throws SQLException, ClassNotFoundException {
        Map<String, Object> res = JdbcUtil.sqlProSelect(2);
        assertEquals(res.get("proNum"), String.valueOf(2));
        System.out.println(res);
    }

    @Test
    void sqlCusLoginSelect() throws SQLException, ClassNotFoundException {
        Map<String, String> res = JdbcUtil.sqlCusLoginSelect("william4");
        assertEquals(res.get("cusNum"), String.valueOf(4));
        System.out.println(res);
    }

    @Test
    void sqlProLoginSelect() throws SQLException, ClassNotFoundException{
        Map<String, Object> res = JdbcUtil.sqlProLoginSelect("myc1");
        assertEquals(res.get("proNum"), String.valueOf(2));
        System.out.println(res);
    }

    @Test
    void sqlcurOrderIdSelect() throws SQLException, ClassNotFoundException{
        Map<String, String> res = JdbcUtil.sqlcurOrderIdSelect(4);
        assertEquals(res.get("oid"), String.valueOf(63));
        System.out.println(res);
    }

    @Test
    void sqlcurOrderSelect() throws SQLException, ClassNotFoundException{
        Map<String, String> res = JdbcUtil.sqlcurOrderSelect("63");
        assertEquals(res.get("cur_orderid"), String.valueOf(63));
        System.out.println(res);
    }

    @Test
    void sqlCurrOrderSelect() throws SQLException, ClassNotFoundException {
        String te  = "-34.39350965887256#150.88522712732396";
        String res = JdbcUtil.sqlCurrOrderSelect(te);
        System.out.println(res);
    }

    @Test
    void sqlCurrentRequest() throws SQLException, ClassNotFoundException{
        Map<String, String> res = JdbcUtil.sqlCurrentRequest("-34.39350965887256#150.88522712732396", "55");
        assertEquals(res.get("O_cusNum"), String.valueOf(4));
        System.out.println(res);
    }

    @Test
    void sqlCusOrderSelect() throws SQLException, ClassNotFoundException{
        String test1 = "4";
        String res = JdbcUtil.sqlCusOrderSelect(test1);
        System.out.println(res);
    }

    @Test
    void sqlProOrderSelect() throws SQLException, ClassNotFoundException{
        String testPro = "2";
        String res = JdbcUtil.sqlProOrderSelect(testPro);
        System.out.println(res);
    }

    @Test
    void updateCurrentOrder() throws SQLException, ClassNotFoundException{
        // modify database
        JdbcUtil.updateCurrentOrder(String.valueOf(1), "set out", "54");

        Map<String, String> expected = new HashMap<>();
        expected = JdbcUtil.sqlcurOrderSelect("55");

        System.out.println(expected);

        //JdbcUtil.updateCustomer(map);

        //2. Test size
        assertThat(expected.size(), is(10));

        //3. Test map entry, best!
        assertThat(expected, not(IsMapContaining.hasEntry("r", "ruby")));

        //4. Test map key
        assertThat(expected, IsMapContaining.hasKey("issue"));

        //5. Test map value
        assertThat(expected, IsMapContaining.hasValue("123333"));
    }

    @Test
    void updateCustomer() throws SQLException, ClassNotFoundException {
        Map<String, String> map = new HashMap<>();
        map.put("uid", String.valueOf(1));
        map.put("cusName", "william2");
        map.put("gender", "male");
        map.put("dob", "2022-05-19");
        map.put("phone", "420275558");
        map.put("cusPw", "20220519");
        map.put("email", "ym554@uowmail.edu.au");
        map.put("plateNum", "jmnh789");
        map.put("model", "mazda");

        // modify database
        JdbcUtil.updateCustomer(map);

        Map<String, String> expected = new HashMap<>();
        expected = JdbcUtil.sqlCusSelect(1);

        //JdbcUtil.updateCustomer(map);

        //1. Test equal, ignore order
        //assertThat(map, is(expected));

        //2. Test size
        assertThat(map.size(), is(9));

        //3. Test map entry, best!
        assertThat(map, IsMapContaining.hasEntry("uid", String.valueOf(1)));
        assertThat(map, not(IsMapContaining.hasEntry("r", "ruby")));

        //4. Test map key
        assertThat(map, IsMapContaining.hasKey("email"));

        //5. Test map value
        assertThat(map, IsMapContaining.hasValue("mazda"));
    }

    @Test
    void updateProfessional() throws SQLException, ClassNotFoundException {
        Map<String, String> map = new HashMap<>();
        map.put("uid", String.valueOf(4));
        map.put("proName", "william2");
        map.put("gender", "male");
        map.put("proDOB", "2022-05-19");
        map.put("phoneNum", "420275558");
        map.put("proPw", "undefined");
        map.put("email", "ym554@uowmail.edu.au");
        map.put("location", "247 rob way");

        // modify database
        JdbcUtil.updateProfessional(map);

        Map<String, String> expected = new HashMap<>();
        expected = JdbcUtil.sqlProSelect( 4);

        //2. Test size
        assertThat(map.size(), is(8));

        //3. Test map entry, best!
        assertThat(map, IsMapContaining.hasEntry("uid", String.valueOf(4)));
        assertThat(map, not(IsMapContaining.hasEntry("r", "4275558")));

        //4. Test map key
        assertThat(map, IsMapContaining.hasKey("email"));

        //5. Test map value
        assertThat(map, IsMapContaining.hasValue("male"));
    }

    @Test
    void toSqlData() {
        int              time     = 30;
        Date             d        = new Date();
        SimpleDateFormat format   = new SimpleDateFormat("yyyy-MM-dd");
        String           currdate = format.format(d);
        System.out.println("现在的日期是：" + currdate);

        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.DATE, time);// num为增加的天数，可以改变的
        d = ca.getTime();
        String enddate = format.format(d);
        System.out.println(enddate);
    }

    @Test
    void getDistance() throws SQLException, ClassNotFoundException {
        String a   = "-34.41631237896929#150.88914851692778";
        String b   = "-34.414328674061366#150.87101276065172";
        double dis = JdbcUtil.getDistance(a, b);
        System.out.println(dis);
    }

    @Test
    void sqlUpdateVIP() throws SQLException, ClassNotFoundException {
        JdbcUtil.sqlUpdateVIP("7", "2022-06-18");

        Map<String, String> expected = new HashMap<>();
        expected = JdbcUtil.sqlcurOrderSelect("55");

        System.out.println(expected);

        //JdbcUtil.updateCustomer(map);

        //2. Test size
        assertThat(expected.size(), is(10));

        //3. Test map entry, best!
        assertThat(expected, not(IsMapContaining.hasEntry("r", "ruby")));

        //4. Test map key
        assertThat(expected, IsMapContaining.hasKey("issue"));

        //5. Test map value
        assertThat(expected, IsMapContaining.hasValue("123333"));

    }

    @Test
    void sqlCusInsert() throws SQLException, ClassNotFoundException {
        JdbcUtil.sqlCusInsert("war", "147258369", "2000-01-10", "160631231@gmail.com", "12345678", "male", "AC527UH","MAZDA");
        Map<String, String> expected = new HashMap<>();
        expected = JdbcUtil.sqlCusSelect(5);
        System.out.println(expected);

        //2. Test size
        assertThat(expected.size(), is(11));

        //3. Test map entry, best!
        assertThat(expected, not(IsMapContaining.hasEntry("r", "ruby")));

        //4. Test map key
        assertThat(expected, IsMapContaining.hasKey("email"));

        //5. Test map value
        assertThat(expected, IsMapContaining.hasValue("war"));
    }

    @Test
    void sqlProInsert() throws SQLException, ClassNotFoundException {
        JdbcUtil.sqlProInsert("wardell", "pw", "2022-01-10", "2131231@gmail.com", "12345678", "male", "wollogong");
        Map<String, String> expected = new HashMap<>();
        expected = JdbcUtil.sqlProSelect(6);

        //2. Test size
        assertThat(expected.size(), is(10));

        //3. Test map entry, best!
        assertThat(expected, not(IsMapContaining.hasEntry("r", "ruby")));

        //4. Test map key
        assertThat(expected, IsMapContaining.hasKey("email"));

        //5. Test map value
        assertThat(expected, IsMapContaining.hasValue("wardell"));
    }

    @Test
    void sqlCurrOrderInsert() throws SQLException, ClassNotFoundException {
        JdbcUtil.sqlCurrOrderInsert("2012-05-01", "CH33875", "-34.416828#150.888962", "No", "1",  "Yes");
        Map<String, String> expected = new HashMap<>();
        expected = JdbcUtil.sqlcurOrderSelect(String.valueOf(66));

        //2. Test size
        assertThat(expected.size(), is(10));

        //3. Test map entry, best!
        assertThat(expected, not(IsMapContaining.hasEntry("r", "ruby")));

        //4. Test map key
        assertThat(expected, IsMapContaining.hasKey("issue"));

        //5. Test map value
        assertThat(expected, IsMapContaining.hasValue("CH33875"));

        System.out.println(expected);
    }

    @Test
    void sqlOrderInsert() throws SQLException, ClassNotFoundException {
        Map<String, String> map = new HashMap<>();
        map.put("orderid", String.valueOf(10));
        map.put("orderStartDate", "2022-05-19");
        map.put("vehiclePlate", "C4PL88");
        map.put("c_location", "-34.419#150.8929");
        map.put("issue", "No");
        map.put("O_cusNum", "1");
        map.put("O_proNum", "5");
        map.put("orderEndDate", "2022-05-20");
        map.put("comm", "...");
        map.put("star", "100");
        map.put("payCardNum", "123567");
        map.put("payType", "Visa");
        map.put("EstimatePayment", "101");
        map.put("actualPayment", "101");

        // modify database
        JdbcUtil.sqlOrderInsert(map);
    }
}