package configuration;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import javax.sql.DataSource;
import org.apache.commons.dbcp2.BasicDataSourceFactory;



public class DBUtils {
public static DataSource dataSource;
    
    static {
        try {
            InputStream in = DBUtils.class.getClassLoader()
                    .getResourceAsStream("db.properties");
            Properties properties = new Properties();
            properties.load(in);
            dataSource = BasicDataSourceFactory.createDataSource(properties);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * return datasource object
     * @return 
     */
    public static DataSource getDataSource(){
        return dataSource;
    }

    /**
     * get connection from database connection pool
     * @return 
     */
    public static Connection getConnection(){
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    
    /**
     * release the connection resources
     */
    public static void releaseResources(ResultSet resultSet, PreparedStatement ps, Connection connection) {

        try {
            if (resultSet != null)
                resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            resultSet = null;
            try {
                if (ps != null)
                    ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                ps = null;
                try {
                    if (connection != null)
                        connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                } finally {
                    connection = null;
                }
            }
        }
    }
    
    public static void main(String[] args) {
    	Connection conn = DBUtils.getConnection();
    	System.out.println("Connection is created.");
    	DBUtils.releaseResources(null, null, conn);
    }
    
}
