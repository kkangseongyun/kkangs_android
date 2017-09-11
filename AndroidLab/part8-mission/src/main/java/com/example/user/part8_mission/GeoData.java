package com.example.user.part8_mission;

import java.util.ArrayList;

public class GeoData {

	
	public static ArrayList<BankData> getAddressData(){
        ArrayList<BankData> list=new ArrayList<>();
        BankData data=new BankData();
        data.bankName="국민은행";
        data.bankLat=37.502268;
        data.bankLng=127.040707;
        list.add(data);

        data=new BankData();
        data.bankName="한국은행";
        data.bankLat=37.500600;
        data.bankLng=127.038143;
        list.add(data);

        data=new BankData();
        data.bankName="신한은행";
        data.bankLat=37.501928;
        data.bankLng=127.037698;

        list.add(data);
        return list;
	}
}
class BankData {
	String bankName;
	double bankLat;
	double bankLng;
}