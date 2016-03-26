package cn.cococode.archite.chapter2.service;


import cn.cococode.archite.chapter2.helper.DatabaseHelper;
import cn.cococode.archite.chapter2.model.Customer;
import cn.cococode.archite.chapter2.util.PropsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Created by DELL on 2016-03-24.
 */
public class CustomerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropsUtil.class);

    //请注意这里面的方法，sql语句的耦合程度不同

    public List<Customer> getCustomerList(){
        String sql = "SELECT * FROM customer";
        return DatabaseHelper.queryEntityList(Customer.class, sql);
    }

    public Customer getCustomer(long id){
        return null;
    }

    public boolean createCustomer(Map<String, Object> fieldMap){
        return DatabaseHelper.insertEntity(Customer.class, fieldMap);
    }

    public boolean updateCustomer(long id, Map<String, Object> fieldMap){
        return DatabaseHelper.updateEntity(Customer.class, id, fieldMap);
    }

    public boolean deleteCustomer(long id){
        return  DatabaseHelper.deleteEntity(Customer.class, id);
    }
}
