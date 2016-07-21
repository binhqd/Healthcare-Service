/*
 * Copyright (c) 2016, WSO2 Inc. (http://wso2.com) All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.wso2.service.healthcare;

import com.google.gson.Gson;
import org.wso2.service.healthcare.Utils.HealthCareUtil;
import org.wso2.service.healthcare.daos.*;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.HashMap;
import java.util.List;

/**
 * This is the Microservice resource class.
 * See <a href="https://github.com/wso2/msf4j#getting-started">https://github.com/wso2/msf4j#getting-started</a>
 * for the usage of annotations.
 *
 * @since 1.0.0-SNAPSHOT
 */
@Path("/healthcare")
public class HealthcareService {

    public HealthcareService() {
        fillCategories();
        HealthcareDao.doctorsList.add((new Doctor("ANURA BANAGALA", "Asiri Hospital", "SURGERY", "9.00 a.m - 11.00 a.m", 7000)));
        HealthcareDao.doctorsList.add((new Doctor("A.N.K. ABAYAJEEWA", "Durdans Hospital", "SURGERY", "8.00 a.m - 10.00 a.m", 12000)));
        HealthcareDao.doctorsList.add((new Doctor("ANIL P AMBAWATTA", "Apollo Hospitals", "SURGERY", "3.00 p.m - 5.00 p.m", 8000)));
        HealthcareDao.doctorsList.add((new Doctor("K. ALAGARATNAM", "Nawaloka Hospital", "CARDIOLOGY", "9.00 a.m - 11.00 a.m", 10000)));
        HealthcareDao.doctorsList.add((new Doctor("SANJAYA ABEYGUNASEKARA", "Asiri Hospital", "CARDIOLOGY", "8.00 a.m - 10.00 a.m", 4000)));
        HealthcareDao.doctorsList.add((new Doctor("K. ALAGARATNAM", "Durdans Hospital", "GYNAECOLOGY", "9.00 a.m - 11.00 a.m", 8000)));
        HealthcareDao.doctorsList.add((new Doctor("SANJAYA ABEYGUNASEKARA", "Asiri Hospital", "GYNAECOLOGY", "8.00 a.m - 10.00 a.m", 11000)));
        HealthcareDao.doctorsList.add((new Doctor("K. ALAGARATNAM", "Asiri Hospital", "ENT", "9.00 a.m - 11.00 a.m", 4500)));
        HealthcareDao.doctorsList.add((new Doctor("SANJAYA ABEYGUNASEKARA", "Asiri Hospital", "ENT", "8.00 a.m - 10.00 a.m", 6750)));
        HealthcareDao.doctorsList.add((new Doctor("AJITH AMARASINGHE", "Durdans Hospital", "PAEDIATRY", "9.00 a.m - 11.00 a.m", 5500)));
        HealthcareDao.doctorsList.add((new Doctor("SANJAYA ABEYGUNASEKARA", "Nawaloka Hospital", "PAEDIATRY", "8.00 a.m - 10.00 a.m", 10000)));
    }

    @GET
    @Path("/{category}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCategories(@PathParam("category") String category) {
        List<Doctor> stock = HealthcareDao.findDoctorByCategory(category);
        Gson gson = new Gson();
        String jsonResponse;

        if (stock != null && stock.size() > 0) {
            jsonResponse = gson.toJson(stock);
        } else {
            jsonResponse = "{\"Status\":\"Could not find any entry for the requested Categiry\"}";
        }

        return Response.ok(jsonResponse, MediaType.APPLICATION_JSON).build();
    }

    @POST
    @Path("/payments")
    public Response settlePayment(Appointment appointment) {
        //Check for the appointment number validity
        Gson gson = new Gson();
        if (appointment.getAppointmentNumber() >= 0) {
            Payment payment = HealthCareUtil.createNewPaymentEntry(appointment);
            payment.setStatus("Settled");
            HealthcareDao.payments.put(payment.getPaymentID(), payment);
            String jsonResponse = gson.toJson(payment);
            return Response.ok(jsonResponse, MediaType.APPLICATION_JSON).build();
        } else {
            String jsonResponse = "{\"Status\":\"Error.Could not Find the Requested appointment ID\"}";
            return Response.ok(jsonResponse, MediaType.APPLICATION_JSON).build();
        }
    }

    @GET
    @Path("/payments")
    public Response getAllPayments() {
        Gson gson = new Gson();
        HashMap payments = HealthcareDao.payments;
        String jsonResponse = gson.toJson(payments);
        return Response.ok(jsonResponse, MediaType.APPLICATION_JSON).build();
    }

    @GET
    @Path("/payments/payment/{payment_id}")
    public Response getPaymentDetails(@PathParam("payment_id") String payment_id) {
        Gson gson = new Gson();
        Payment payment = HealthcareDao.payments.get(payment_id);
        String jsonResponse;

        if (payment != null) {
            jsonResponse = gson.toJson(payment);
        } else {
            jsonResponse = "{\"Status\":\"Invalid payment id provided\"}";
        }
        return Response.ok(jsonResponse, MediaType.APPLICATION_JSON).build();
    }

    public void fillCategories() {
        HealthcareDao.catergories.add("SURGERY");
        HealthcareDao.catergories.add("CARDIOLOGY");
        HealthcareDao.catergories.add("GYNAECOLOGY");
        HealthcareDao.catergories.add("ENT");
        HealthcareDao.catergories.add("PAEDIATRY");
    }
}
