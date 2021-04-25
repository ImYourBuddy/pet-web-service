package com.imyourbuddy.petwebapp.model.projection;

public interface PetExpertRequestQueryResult {
    long getId();
    long getUser();
    String getUserName();
    String getName();
    String getQualification();
    boolean getHelp();
}
