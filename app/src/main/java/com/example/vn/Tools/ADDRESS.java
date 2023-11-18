package com.example.vn.Tools;

import android.content.Context;

import com.example.vn.Models.Address;
import com.example.vn.Models.District;
import com.example.vn.Models.Province;
import com.example.vn.Models.Ward;

public class ADDRESS {
    public static Province province = null;
    public static District district = null;
    public static Ward ward = null;

    public static Address aDefault(Context context) {
        Address address = null;
        for (int i = 0; i < LIST.listAddress.size(); i++) {
            if (LIST.listAddress.get(i).get_id().equals(TOOLS.getDefaulAddress(context))) {
                address = LIST.listAddress.get(i);
                break;
            }
        }
        if(address==null&& !LIST.listAddress.isEmpty()){
            address = LIST.listAddress.get(0);
        }
        return address;
    }
}
