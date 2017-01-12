package com.lanthanh.admin.icareapp.data.manager;

import com.lanthanh.admin.icareapp.data.manager.base.Manager;
import com.lanthanh.admin.icareapp.presentation.model.ModelUser;

/**
 * Created by ADMIN on 08-Jan-17.
 */

public interface CustomerManager extends Manager {
    String CUSTOMER_ID_KEY = "customer_id";
    String CUSTOMER_ID_KEY_2 = "cus_id";
    String CUSTOMER_NAME_KEY = "cus_name";
    String CUSTOMER_USERNAME_KEY = "username";
    String CUSTOMER_USERNAME_KEY_2 = "login_id";
    String CUSTOMER_PASSWORD_KEY = "password";
    String CUSTOMER_ADDRESS_KEY = "address";
    String CUSTOMER_DOB_KEY = "dob";
    String CUSTOMER_GENDER_KEY = " gender";
    String CUSTOMER_EMAIL_KEY = "email";
    String CUSTOMER_PHONE_KEY = "phone";
    String CUSTOMER_UPDATE_DATE = "update_date";
    String  logIn(String username, String password);
    boolean checkUserExistence(String username);
    boolean insertNewCustomer(String username, String password);
    int     getCustomerId(String username);
    boolean updateCustomer(ModelUser user);
    boolean updateCustomerPassword(String username, String password);
}