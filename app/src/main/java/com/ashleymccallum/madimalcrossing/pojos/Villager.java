package com.ashleymccallum.madimalcrossing.pojos;


/**
 * Villager Class
 * @author Ashley McCallum
 */
public class Villager {

    private int id;
    private String name;
    private String personality;
    private String birthday;
    private String species;
    private String gender;
    private String hobby;
    private String catchphrase;
    private String iconURI;
    private String imgURI;
    private int spotted = 0;

    public Villager(int id, String name, String personality, String birthday, String species, String gender, String hobby, String catchphrase, String iconURI, String imgURI, int spotted) {
        this.id = id;
        this.name = name;
        this.personality = personality;
        this.birthday = birthday;
        this.species = species;
        this.gender = gender;
        this.hobby = hobby;
        this.catchphrase = catchphrase;
        this.iconURI = iconURI;
        this.imgURI = imgURI;
        this.spotted = spotted;
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

    public String getBirthday() {
        return birthday;
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
