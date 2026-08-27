package com.healthcare.healthcare_auth.util;

import com.healthcare.healthcare_auth.enums.Permissions;
import com.healthcare.healthcare_auth.enums.UserRole;

import java.util.HashSet;
import java.util.Set;

public class CommonService {

    public Set<Permissions> permissionsIdentifier(String role) {

        return switch (role) {
            case "ROLE_PATIENT" -> {
                Set<Permissions> permissions = new HashSet<>();
                permissions.add(Permissions.PATIENT_CREATE);
                permissions.add(Permissions.PATIENT_READ);
                permissions.add(Permissions.PATIENT_UPDATE);
                permissions.add(Permissions.PATIENT_DELETE);
                yield permissions;
            }
            case "ROLE_DOCTOR" -> {
                Set<Permissions> permissions = new HashSet<>();
                permissions.add(Permissions.DOCTOR_CREATE);
                permissions.add(Permissions.DOCTOR_UPDATE);
                permissions.add(Permissions.DOCTOR_READ);
                yield permissions;
            }
            case "ROLE_NURSE" -> {
                Set<Permissions> permissions = new HashSet<>();
                permissions.add(Permissions.NURSE_CREATE);
                permissions.add(Permissions.NURSE_UPDATE);
                permissions.add(Permissions.NURSE_READ);
                yield permissions;
            }
            case "ROLE_RECEPTIONIST" -> {
                Set<Permissions> permissions = new HashSet<>();
                permissions.add(Permissions.RECEPTIONIST_READ);
                permissions.add(Permissions.RECEPTIONIST_CREATE);
                permissions.add(Permissions.RECEPTIONIST_UPDATE);
                permissions.add(Permissions.RECEPTIONIST_DELETE);
                yield permissions;
            }
            case "ROLE_LAB_TECHNICIAN" -> {
                Set<Permissions> permissions = new HashSet<>();
                permissions.add(Permissions.LAB_RESULT_CREATE);
                permissions.add(Permissions.LAB_RESULT_UPDATE);
                permissions.add(Permissions.LAB_RESULT_READ);
                yield permissions;
            }
            default -> throw new IllegalStateException("Unexpected value: " + role);
        };
    }

    public UserRole roleIdentifier(String role) {

        return switch (role) {
            case "ROLE_ADMIN" -> UserRole.ROLE_ADMIN;
            case "ROLE_DOCTOR" -> UserRole.ROLE_DOCTOR;
            case "ROLE_NURSE" -> UserRole.ROLE_NURSE;
            case "ROLE_RECEPTIONIST" -> UserRole.ROLE_RECEPTIONIST;
            case "ROLE_LAB_TECHNICIAN" -> UserRole.ROLE_LAB_TECHNICIAN;
            default -> throw new IllegalStateException("Unexpected value: " + role);
        };
    }
}
