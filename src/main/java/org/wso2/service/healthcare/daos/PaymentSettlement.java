package org.wso2.service.healthcare.daos;

/**
 * Created by nadeeshaan on 7/20/16.
 */
public class PaymentSettlement {
    int appointmentID;
    Patient patient;
    String card_number;

    public PaymentSettlement(int appointmentID, Patient patient, String card_number) {
        this.appointmentID = appointmentID;
        this.patient = patient;
        this.card_number = card_number;
    }

    public int getAppointmentID() {
        return appointmentID;
    }

    public void setAppointmentID(int appointmentID) {
        this.appointmentID = appointmentID;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getCard_number() {
        return card_number;
    }

    public void setCard_number(String card_number) {
        this.card_number = card_number;
    }
}
