package com.example.justin_c.firebaseexample;

/**
 * Created by Justin_C on 5/3/17.
 */

public class PetInformation {
    private Integer Age;
    private String Breed;
    private String Description;
    private String Gender;
    private String Name;
    private String shelter_id;

    public PetInformation() {
    }

    public Integer getAge() {
        return Age;
    }

    public String getBreed() {
        return Breed;
    }

    public String getDescription() {
        return Description;
    }

    public String getGender() {
        return Gender;
    }

    public void setAge(Integer age) {
        Age = age;
    }

    public void setBreed(String breed) {
        Breed = breed;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setShelter_id(String shelter_id) {
        this.shelter_id = shelter_id;
    }

    public String getName() {
        return Name;

    }

    public String getShelter_id() {
        return shelter_id;
    }
}
