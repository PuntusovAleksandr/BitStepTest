package com.aleksandrp.bitsteptest.api.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by AleksandrP on 25.09.2017.
 */

public class NewUserModel implements Parcelable {

    private String email;
    private String organisation;
    private String locale;
    private String site;
    private String password;
    private String phone;
    private String mPath;

    public NewUserModel() {
    }

    public NewUserModel(String mEmail, String mOrganisation, String mLocale, String mSite,
                        String mPassword, String mPhone, String mPath) {
        this.email = mEmail;
        this.organisation = mOrganisation;
        this.locale = mLocale;
        this.site = mSite;
        this.password = mPassword;
        this.phone = mPhone;
        this.mPath = mPath;
    }

    public NewUserModel(Parcel mParcel) {
        String[] data = new String[7];
        mParcel.readStringArray(data);
        email= data[0];
        organisation= data[1];
        locale= data[2];
        site= data[3];
        password= data[4];
        phone= data[5];
        mPath= data[6];
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String mEmail) {
        email = mEmail;
    }

    public String getOrganisation() {
        return organisation;
    }

    public void setOrganisation(String mOrganisation) {
        organisation = mOrganisation;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String mLocale) {
        locale = mLocale;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String mSite) {
        site = mSite;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String mPassword) {
        password = mPassword;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String mPhone) {
        phone = mPhone;
    }

    public String getPath() {
        return mPath;
    }

    public void setPath(String mPath) {
        this.mPath = mPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                email,
                organisation,
                locale,
                site,
                password,
                phone,
                mPath
                 });
    }

    public static final Parcelable.Creator<NewUserModel> CREATOR = new Parcelable.Creator<NewUserModel>() {

        @Override
        public NewUserModel createFromParcel(Parcel source) {
            return new NewUserModel(source);
        }

        @Override
        public NewUserModel[] newArray(int size) {
            return new NewUserModel[size];
        }
    };
}
