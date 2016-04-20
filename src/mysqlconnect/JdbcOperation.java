package mysqlconnect;

import domain.Vehicle;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Created by yuxiao on 16/4/19.
 */
public class JdbcOperation {
    public static Connection getConn() {
        String driver = "com.mysql.jdbc.Driver";
        String url = "jdbc:mysql://10.2.9.42:3306/data_analysis?characterEncoding=GB2312";
        String username = "housir";
        String password = "123456";
        Connection conn = null;
        try {
            Class.forName(driver); //classLoader,加载对应驱动
            conn = (Connection) DriverManager.getConnection(url, username, password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static int insert(Vehicle vehicle,Connection conn) {
        int i = 0;
        String sql = "insert into classification (plate,plate_color,local_code,industry_code,xingzheng_code,yehu_code,now_local_code,typeself_catalogue,manage_catalogue,manage_area)" +
                " values(?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement pstmt;
        try {
            pstmt = (PreparedStatement) conn.prepareStatement(sql);
            pstmt.setString(1, vehicle.getPlate());
            pstmt.setInt(2, vehicle.getPlate_color());
            pstmt.setInt(3, vehicle.getLocal_code());
            pstmt.setInt(4, vehicle.getIndustry_code());
            pstmt.setInt(5, vehicle.getXingzheng_code());
            pstmt.setInt(6, vehicle.getYehu_code());
            pstmt.setInt(7, vehicle.getNow_local_code());
            pstmt.setInt(8, vehicle.getTypeself_catalogue());
            pstmt.setInt(9, vehicle.getManage_catalogue());
            pstmt.setString(10, vehicle.getManage_area());
            i = pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return i;
    }

    @Test
    public void test() throws Exception{

        Connection connection = JdbcOperation.getConn();
        Vehicle vehicle = new Vehicle();
        vehicle.setPlate("江苏");
        vehicle.setTypeself_catalogue(1);
        vehicle.setManage_catalogue(2);
        int i = JdbcOperation.insert(vehicle,connection);
    }
}
