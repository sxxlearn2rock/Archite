package cn.cococode.archite.chapter2.helper;

import cn.cococode.archite.chapter2.util.CollectionUtil;
import cn.cococode.archite.chapter2.util.PropsUtil;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * Created by DELL on 2016-03-25.
 */
public class DatabaseHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropsUtil.class);
    private static final QueryRunner QUERY_RUNNER = new QueryRunner();
    private static final ThreadLocal<Connection> CONNECTION_THREAD_LOCAL = new ThreadLocal<>();
    private static final BasicDataSource DATA_SOURCE;


    static {
        //根据就近原则，maven会先读取test文件中中的config.properties
        Properties conf = PropsUtil.loadProps("config.properties");
        String driver = conf.getProperty("jdbc.driver");
        String url = conf.getProperty("jdbc.url");
        String username = conf.getProperty("jdbc.username");
        String passwd = conf.getProperty("jdbc.password");

        DATA_SOURCE = new BasicDataSource();
        DATA_SOURCE.setDriverClassName(driver);
        DATA_SOURCE.setUrl(url);
        DATA_SOURCE.setUsername(username);
        DATA_SOURCE.setPassword(passwd);

    }

    public static Connection getConnection(){
        Connection conn = CONNECTION_THREAD_LOCAL.get();
        if (conn == null){
            try {
                conn = DATA_SOURCE.getConnection();
            }catch (SQLException e){
                LOGGER.error("get connection failure", e);
                throw new RuntimeException(e);
            }finally {
                CONNECTION_THREAD_LOCAL.set(conn);
            }

        }
        return conn;
    }
//    有了数据库连接池，不用手动关闭连接，连接池来管理每个链接的生命周期
//    public static void closeConnection(){
//        Connection conn = CONNECTION_THREAD_LOCAL.get();
//        if (conn != null){
//            try {
//                conn.close();
//            }catch (SQLException e){
//                LOGGER.error("close connection failure!", e);
//                throw new RuntimeException(e);
//            }finally {
//                CONNECTION_THREAD_LOCAL.remove();
//            }
//        }
//    }

    public static <T> List<T> queryEntityList(Class<T> entityClass, String sql, Object... params){
        List<T> entityList = null;
        Connection conn = getConnection();
        try{
            entityList = QUERY_RUNNER.query(conn, sql, new BeanListHandler<T>(entityClass), params);
        }catch (SQLException e){
            LOGGER.error("query entity list failure!", e);
            throw new RuntimeException(e);
        }
        return entityList;
    }

    private static <T> T queryEntity(Class<T> entityClass, String sql, Object... params){
        T entity;
        Connection conn = getConnection();
        try{
            entity = QUERY_RUNNER.query(conn, sql, new BeanHandler<T>(entityClass), params);
        }catch (SQLException e){
            LOGGER.error("query single entity failure!", e);
            throw new RuntimeException(e);
        }
        return entity;
    }

    private static List<Map<String, Object>> executeQuery(String sql, Object... params){
        List<Map<String, Object>> result = null;
        try{
            Connection conn = getConnection();
            result = QUERY_RUNNER.query(conn, sql, new MapListHandler(), params);
        }catch (SQLException e){
            LOGGER.error("execute query failure!", e);
            throw new RuntimeException(e);
        }
        return result;
    }

    private static int executeUpdate(String sql, Object... params){
        int rows = 0;
        try{
            Connection conn = getConnection();
            rows = QUERY_RUNNER.update(conn, sql, params);
        }catch (SQLException e){
            LOGGER.error("execute update failure!", e);
            throw new RuntimeException(e);
        }
        return rows;
    }

    public static <T> boolean insertEntity(Class<T> entityClass, Map<String, Object> fieldMap){
        if (CollectionUtil.isEmpty(fieldMap)){
            LOGGER.error("can not insert entity: fieldMap is empty");
            return false;
        }

        //注意类名和表名大小写不一致，但是sql语句是不区分大小写的
        String sql = "INSERT INTO " + getTableName(entityClass);
        StringBuilder columns = new StringBuilder("(");
        StringBuilder values = new StringBuilder("(");
        for (String field : fieldMap.keySet()){
            columns.append(field).append(", ");
            values.append("?, ");
        }

        columns.replace(columns.lastIndexOf(","), columns.length(), ")");
        values.replace(values.lastIndexOf(","), values.length(), ")");
        sql += columns + " VALUES " + values;

        Object[] params = fieldMap.values().toArray();

        return executeUpdate(sql, params) == 1;
    }

    public static <T> boolean updateEntity(Class<T> classEntity, long id, Map<String, Object> fieldMap){
        if (CollectionUtil.isEmpty(fieldMap)){
            LOGGER.error("can not update entity: fieldMap is empty");
            return false;
        }

        String sql = "UPDATE " + getTableName(classEntity) + " SET ";
        StringBuilder columns = new StringBuilder("(");
        for (String field : fieldMap.keySet()){
            columns.append(field).append("=?, ");
        }

        sql += columns.substring(0, columns.lastIndexOf(",")) + " WHERE id=?";
        List<Object> paramsList = new ArrayList<>();
        paramsList.addAll(fieldMap.values());
        paramsList.add(id);
        Object[] params = paramsList.toArray();

        return executeUpdate(sql, params) == 1;
    }

    public static <T> boolean deleteEntity(Class<T> classEntity, long id){
        String sql = " DELETE FROM " + getTableName(classEntity) + " WHERE id=?";
        return executeUpdate(sql, id) == 1;
    }

    public static void executeSqlFile(String filePath){
        //这里如果使用FileReader，filaPath就必须是绝对路径
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(filePath);
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));

        try {
            String sql = "";
            while ((sql = reader.readLine()) != null){
                executeUpdate(sql);
            }
        }catch (IOException e){
            LOGGER.error("execute sql file failure!", e);
            throw new RuntimeException(e);
        }

    }

    private static String getTableName(Class<?> clazz){
        return clazz.getSimpleName();
    }

}
