package com.example.mymessenger.template;

public class UidCat {
    private final String Uid1;
    private final String Uid2;
    private String result;

    public UidCat(String Uid1, String Uid2) {
        this.Uid1 = Uid1;
        this.Uid2 = Uid2;
    }

    public String createUid() {
        int i = 0;

        if (Uid1.equals(Uid2))
            return null;
        else {
            char[] uid1 = Uid1.toUpperCase().toCharArray();
            char[] uid2 = Uid2.toUpperCase().toCharArray();
            while (uid1[i] != '\0') {
                if (uid1[i] > uid2[i]) {
                    result = Uid1 + "+" + Uid2;
                    break;
                } else if (uid1[i] < uid2[i]) {
                    result = Uid2 + "+" + Uid1;
                    break;
                }
            }
            return result;
        }

    }


}
