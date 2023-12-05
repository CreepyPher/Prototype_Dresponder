package com.example.prototype_dresponder.Modals;

public class AdminDashList {
    private String Name;
    private String imglink;
    private String AccID;

    public AdminDashList(String name, String imglink,String ID) {
        Name = name;
        this.imglink = imglink;
        this.AccID = ID;
    }

    public String getName() {
        return Name;
    }
    public void setName(String name) {
        Name = name;
    }
    public String getImglink() {
        return imglink;
    }
    public void setImglink(String imglink) {
        this.imglink = imglink;
    }

    public String getAccID() {
        return AccID;
    }

    public void setAccID(String accID) {
        AccID = accID;
    }
}
