package com.example.cubesschool8.supermarket.data.response;

import com.example.cubesschool8.supermarket.data.DataChangeProfileData;
import com.example.cubesschool8.supermarket.data.DataPasswordChange;

import java.util.ArrayList;

/**
 * Created by cubesschool8 on 10/5/16.
 */
public class ResponsePasswordChange {

    public ResponsePasswordChangePom data;

    public class ResponsePasswordChangePom {

        public String status;
        public String message;
        public String error;
        public ArrayList<DataPasswordChange> results;

    }
}
