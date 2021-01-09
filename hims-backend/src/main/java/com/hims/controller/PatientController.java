package com.hims.controller;

import com.hims.controller.request.AddPatientRequest;
import com.hims.domain.NatReport;
import com.hims.domain.Patient;
import com.hims.serviceImpl.PatientServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
public class PatientController {
    @Autowired
    private PatientServiceImpl patientService;

    @Autowired
    public PatientController(PatientServiceImpl patientService) {
        this.patientService = patientService;
    }

//    @PostMapping("/addPatient")
//    @Transactional
//    public ResponseEntity<?> addPatient(@RequestBody AddPatientRequest request) {
//        Patient patient = new Patient(request.getName(), request.getAge(), request.getPhone(),
//                request.getAddress(), request.getRating(), Integer.parseInt(request.getENurseId()));
//        int id = patientService.insertNewPatient(patient);
//        NatReport natReport = new NatReport(id, request.getNATResult(), request.getTestDate(), request.getTestTime(), request.getRating());
//        patientService.insertNewNATReport(natReport);
//        return new ResponseEntity<>(patientService.transferPatient(id, request.getRating()), HttpStatus.OK);
//    }

    @GetMapping("/addPatient")
    @Transactional
    public ResponseEntity<?> addPatient(@RequestParam("ENurseId") String ENurseId,
                                        @RequestParam("NATResult") String NATResult,
                                        @RequestParam("address") String address,
                                        @RequestParam("age") String age,
                                        @RequestParam("name") String name,
                                        @RequestParam("phone") String phone,
                                        @RequestParam("rating") String rating,
                                        @RequestParam("testDate") String testDate,
                                        @RequestParam("testTime") String testTime) {
        Patient patient = new Patient(name, age, phone,
                address, rating, Integer.parseInt(ENurseId));
        int id = patientService.insertNewPatient(patient);
        NatReport natReport = new NatReport(id, NATResult, testDate, testTime, rating);
        patientService.insertNewNATReport(natReport);
        return new ResponseEntity<>(patientService.transferPatient(id, rating), HttpStatus.OK);
    }
}
