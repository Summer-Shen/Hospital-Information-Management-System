package com.hims.serviceImpl;

import com.hims.domain.Patient;
import com.hims.domain.User;
import com.hims.repository.PatientRepository;
import com.hims.service.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PatientServiceImpl implements PatientService {
    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private UserServiceImpl userService;

    @Autowired
    public PatientServiceImpl(PatientRepository patientRepository, UserServiceImpl userService) {
        this.patientRepository = patientRepository;
        this.userService = userService;
    }

    public Map<String, Object> getPatientDataPanelByHNurseId(String id) {
        Map<String, Object> map = new HashMap<>();
        List<Integer> wards = userService.findWardIdByHeadNurseId(Integer.parseInt(id));
        List<Patient> patients = new ArrayList<>();
        for (Integer integer : wards) {
            patients.addAll(patientRepository.findByWardId(integer));
        }
        map.put("patient", patients);
        return map;
    }
}
