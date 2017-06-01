package com.fyp.hp.realestate;

/**
 * Created by Shah on 25/04/2017.
 */

public class GetEstatesClass {

public  int id;
    public String getReal_estatename() {
        return real_estatename;
    }

    public void setReal_estatename(String real_estatename) {
        this.real_estatename = real_estatename;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String real_estatename;

    @Override
    public String toString() {
        return real_estatename;
    }
}
