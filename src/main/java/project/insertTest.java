package project;

import org.junit.jupiter.api.Test;

import java.sql.*;
import java.text.ParseException;
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

import javax.xml.crypto.Data;

public class insertTest {
    @Test
    void insert_cus() throws SQLException, ClassNotFoundException {
        for (int i =1;i<=20;i++){
            Faker faker = new Faker();
            String name;
            name = faker.name().fullName();
            String pw = faker.number().digits(6);
            String DOB;
            SimpleDateFormat simpleDateFormat= new SimpleDateFormat("yyyy-MM-dd");
            DOB = simpleDateFormat.format(faker.date().birthday(18, 70));
            String email = faker.internet().emailAddress();
            String phone = faker.number().digits(9);
            String gender ;
            int ramdomN = faker.number().numberBetween(0,10);
            System.out.println(ramdomN);
            if (ramdomN <= 6) {
                gender = "Male";
            } else {
                gender = "Famale";
            }
            String plateNum =faker.number().digits(6);
            String model = faker.company().name();
//            System.out.println(name);
//            System.out.println(pw);
//            System.out.println(DOB);
//            System.out.println(email);
//            System.out.println(phone);
            System.out.println(gender);
//            System.out.println(plateNum);
//            System.out.println(model);
            JdbcUtil.sqlCusInsert(name,pw,DOB,email,phone,gender,plateNum,model);
        }

    }
    @Test
    void insert_pro() throws SQLException, ClassNotFoundException {
        for (int i = 1; i <= 100; i++) {
            Faker  faker = new Faker();
            String name;
            name = faker.name().fullName();
            String           pw               = faker.number().digits(6);
            String           DOB;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            DOB = simpleDateFormat.format(faker.date().birthday(18, 70));
            String email   = faker.internet().emailAddress();
            String phone   = faker.number().digits(9);
            String gender;
            int    ramdomN = faker.number().numberBetween(0, 10);
            System.out.println(ramdomN);
            if (ramdomN <= 6) {
                gender = "Male";
            } else {
                gender = "Famale";
            }
            String location = faker.address().streetAddress();

//            System.out.println(name);
//            System.out.println(pw);
//            System.out.println(DOB);
//            System.out.println(email);
//            System.out.println(phone);
            System.out.println(location);
//            System.out.println(plateNum);
//            System.out.println(model);
            JdbcUtil.sqlProInsert(name, pw, DOB, email, phone, gender, location);
        }
    }
    @Test
    void insert_cur_order() throws SQLException, ClassNotFoundException, ParseException {
        for (int i =33; i <= 52; i++){
            String orderStartDate;
            String vehiclePlate;
            String c_location;
            String issue;
            String O_cusNum;
            String isVip;
            Map<String,String> res = new HashMap<String, String>();
            res = JdbcUtil.sqlCusSelect(i);
            String           vipEnd  = res.get("vipEnd");
            SimpleDateFormat curDate = new SimpleDateFormat("yyyy-MM-dd");
            Date             date1   = new Date();
            Date             date2   = new Date();
            int    compare = 0;
            date1 = curDate.parse(vipEnd);
            Date date3 = curDate.parse(curDate.format(date2));
            compare = date1.compareTo(date3);
            if (compare <0){
                isVip = "F";
            }else {
                isVip = "T";
            }
            orderStartDate = curDate.format(date2);
            vehiclePlate = res.get("plateNum");
            double minLon = -34.62639102870706;
            double minLat =  149.9954783709458;
            double maxLon = -34.17087128716962;
            double maxLat =  150.71920274172194;
            Random random = new Random();
            double lon = minLon+random.nextDouble()*0.1;
            double lat = minLat+random.nextDouble()*0.1;
            c_location = lon +"#"+lat;
            issue = "issue";
            O_cusNum = res.get("cusNum");
            JdbcUtil.sqlCurrOrderInsert(orderStartDate, vehiclePlate, c_location, issue, O_cusNum,isVip);
        }

    }
    @Test
    void pro_cur_order() throws SQLException, ClassNotFoundException {
        for (int i =136; i <= 155; i++){
            Faker faker = new Faker();
            String pid = String.valueOf(faker.number().numberBetween(11, 110));
            JdbcUtil.updateCurrentOrder(String.valueOf(i), pid, "processing");
            double minLon = -34.62639102870706;
            double minLat =  149.9954783709458;
            double maxLon = -34.17087128716962;
            double maxLat =  150.71920274172194;
            Random random = new Random();
            double lon = minLon+random.nextDouble()*0.01;
            double lat = minLat+random.nextDouble()*0.01;
            String coordinates = lon +"#"+lat;
            System.out.println(pid+coordinates);
            JdbcUtil.sqlSetEstimatePayment(String.valueOf(i), coordinates);
            JdbcUtil.updateCurrentOrder(String.valueOf(i), pid, "Finished");
        }
    }
    @Test
    void finish_order() throws SQLException, ClassNotFoundException {
        for (int i =136; i <= 155; i++){
            Map<String, String> curOmap = new HashMap<String, String>();
            curOmap = JdbcUtil.sqlcurOrderSelect(String.valueOf(i));
            String vip = curOmap.get("isVip");
            Faker faker = new Faker();
            String payType = "VISA";
            String star = String.valueOf(faker.number().numberBetween(1, 5));
            String payCardNum = faker.number().digits(13);
            String comm = "comm";
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date curdate = new Date();
            String orderEndDate = simpleDateFormat.format(curdate);
            if(vip.equals("T")){
                curOmap.put("orderid", String.valueOf(i));
                curOmap.put("payType", payType);
                curOmap.put("payCardNum", payCardNum);
                curOmap.put("star", star);
                curOmap.put("comm", comm);
                curOmap.put("orderEndDate", orderEndDate);
                curOmap.put("actualPayment","0");
            }else{
                curOmap.put("orderid", String.valueOf(i));
                curOmap.put("payType", payType);
                curOmap.put("payCardNum", payCardNum);
                curOmap.put("star", star);
                curOmap.put("comm", comm);
                curOmap.put("orderEndDate", orderEndDate);
                curOmap.put("actualPayment",curOmap.get("EstimatePayment"));
            }
            JdbcUtil.sqlOrderInsert(curOmap);
        }

    }
}
