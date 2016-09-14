package com.example.cubesschool8.supermarket.tool;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cubesschool8.supermarket.R;

/**
 * Created by cubesschool8 on 9/14/16.
 */
public class MessageObject {


    public static final int MESSAGE_ERROR = 0, MESSAGE_SUCCESS = 1, MESSAGE_INFO = 2;


    public int stringResource;
    public int time;
    public int type;
    public OnMessageClickListener listener;




    public interface OnMessageClickListener {
        void onClick();

    }

    public MessageObject() {
        time = 5000;
        stringResource = R.string.server_error;
        type = MESSAGE_ERROR;
        listener = null;

    }

    public MessageObject(int stringResource, int time, int type, OnMessageClickListener listener) {
        this.stringResource = stringResource;
        this.type = type;
        this.time = time;
        this.listener = listener;
    }
}
