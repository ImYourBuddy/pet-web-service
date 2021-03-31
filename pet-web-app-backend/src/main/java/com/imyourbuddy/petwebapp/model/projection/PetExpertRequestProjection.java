package com.imyourbuddy.petwebapp.model.projection;

public interface PetExpertRequestProjection {
    long getId();
    long getUser();
    String getUserName();
    String getName();
    String getQualification();
    boolean getHelp();
}
