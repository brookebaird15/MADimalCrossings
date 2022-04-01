package com.ashleymccallum.madimalcrossing.pojos;


/**
 * Villager Class
 * @author Ashley McCallum
 */
public class Villager {

    private int id;
    private int spotted;
    private String name;
    private String personality;
    private String species;
    private String url;
    private String gender;
    private String hobby;
    private String catchphrase;
    private String iconURI;
    private String imgURI;
    private String birthMonth;
    private String birthDay;
    private String sign;
    private String houseExtURI;
    private String houseIntURI;

    public Villager(int id, int spotted, String name, String personality, String species, String url, String gender, String hobby, String catchphrase, String iconURI, String imgURI, String birthMonth, String birthDay, String sign, String houseExtURI, String houseIntURI) {
        this.id = id;
        this.spotted = spotted;
        this.name = name;
        this.personality = personality;
        this.species = species;
        this.url = url;
        this.gender = gender;
        this.hobby = hobby;
        this.catchphrase = catchphrase;
        this.iconURI = iconURI;
        this.imgURI = imgURI;
        this.birthMonth = birthMonth;
        this.birthDay = birthDay;
        this.sign = sign;
        this.houseExtURI = houseExtURI;
        this.houseIntURI = houseIntURI;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPersonality() {
        return personality;
    }

    public String getUrl() {
        return url;
    }

    public String getBirthMonth() {
        return birthMonth;
    }

    public String getBirthDay() {
        return birthDay;
    }

    public String getSign() {
        return sign;
    }

    public String getHouseExtURI() {
        return houseExtURI;
    }

    public String getHouseIntURI() {
        return houseIntURI;
    }

    public String getSpecies() {
        return species;
    }

    public String getGender() {
        return gender;
    }

    public String getHobby() {
        return hobby;
    }

    public String getCatchphrase() {
        return catchphrase;
    }

    public void setCatchphrase(String catchphrase) {
        this.catchphrase = catchphrase;
    }

    public String getIconURI() {
        return iconURI;
    }

    public String getImgURI() {
        return imgURI;
    }

    public int getSpotted() {
        return spotted;
    }

    public void setSpottedStatus() {
        if(spotted == 0) {
            spotted = 1;
        } else {
            spotted = 0;
        }
    }
}
