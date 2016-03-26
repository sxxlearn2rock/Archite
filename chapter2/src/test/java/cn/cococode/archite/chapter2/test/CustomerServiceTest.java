package cn.cococode.archite.chapter2.test;

import cn.cococode.archite.chapter2.helper.DatabaseHelper;
import cn.cococode.archite.chapter2.model.Customer;
import cn.cococode.archite.chapter2.service.CustomerService;
import org.junit.Before;
import org.junit.Test;
import  static  org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by DELL on 2016-03-24.
 */
public class CustomerServiceTest {
    private final CustomerService customerService;
    public CustomerServiceTest(){
        customerService = new CustomerService();
    }

    @Before
    public void init(){
        //将resource文件夹设置为了测试资源文件根目录，Maven会去这些资源文件根目录寻找相关文件
        DatabaseHelper.executeSqlFile("sql/customer_init.sql");
    }

    @Test
    public void getCustmoerListTest(){
        List<Customer> customerList = customerService.getCustomerList();
        assertEquals(2, customerList.size());
        for (Customer customer : customerList){
            System.out.println(customer);
        }
    }

    @Test
    public void createCustomerTest() throws Exception {
        Map<String, Object> fieldMap = new HashMap<String, Object>();
        fieldMap.put("name", "customer100");
        fieldMap.put("contact", "John");
        fieldMap.put("telephone", "13512345678");
        boolean result = customerService.createCustomer(fieldMap);
        assertTrue(result);
    }

}
