//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.frontofficeapi.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.util.Objects;

@Entity
public class Request {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;
    @Column(
            nullable = false
    )
    private String patientName;
    @Column(
            nullable = false
    )
    private String bloodType;
    @Column(
            nullable = false
    )
    private String city;
    @Column(
            nullable = false
    )
    private String transfusionCenter;
    @Column(
            nullable = false
    )
    private String contactPhone;
    @Column(
            nullable = false
    )
    private String urgencyLevel;
    private String additionalMessage;

    public Request() {
    }

    public Request(String patientName, String bloodType, String city, String transfusionCenter, String contactPhone, String urgencyLevel, String additionalMessage) {
        this.patientName = patientName;
        this.bloodType = bloodType;
        this.city = city;
        this.transfusionCenter = transfusionCenter;
        this.contactPhone = contactPhone;
        this.urgencyLevel = urgencyLevel;
        this.additionalMessage = additionalMessage;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPatientName() {
        return this.patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getBloodType() {
        return this.bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
    }

    public String getCity() {
        return this.city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTransfusionCenter() {
        return this.transfusionCenter;
    }

    public void setTransfusionCenter(String transfusionCenter) {
        this.transfusionCenter = transfusionCenter;
    }

    public String getContactPhone() {
        return this.contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getUrgencyLevel() {
        return this.urgencyLevel;
    }

    public void setUrgencyLevel(String urgencyLevel) {
        this.urgencyLevel = urgencyLevel;
    }

    public String getAdditionalMessage() {
        return this.additionalMessage;
    }

    public void setAdditionalMessage(String additionalMessage) {
        this.additionalMessage = additionalMessage;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            Request request = (Request)o;
            return Objects.equals(this.id, request.id);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.id});
    }
}
