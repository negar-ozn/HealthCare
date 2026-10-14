-- ROLE_ADMIN --
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
         CROSS JOIN permissions p
WHERE r.role = 'ROLE_ADMIN';

-- ROLE_DOCTOR --
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
         JOIN permissions p
              ON p.permission IN (
                                  'PATIENT_READ',
                                  'PATIENT_UPDATE',
                                  'SCHEDULE_READ',
                                  'APPOINTMENT_READ',
                                  'APPOINTMENT_CREATE',
                                  'APPOINTMENT_UPDATE',
                                  'LAB_RESULT_READ',
                                  'PROFILE_READ_SELF',
                                  'PASSWORD_RESET'
                  )

WHERE r.role = 'ROLE_DOCTOR';

-- ROLE_NURSE --
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
         JOIN permissions p
              ON p.permission IN (
                                  'PATIENT_READ',
                                  'PATIENT_UPDATE',
                                  'APPOINTMENT_READ',
                                  'LAB_RESULT_READ',
                                  'PROFILE_READ_SELF',
                                  'PASSWORD_RESET'
                  )

WHERE r.role = 'ROLE_NURSE';

-- ROLE_RECEPTIONIST --
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
         JOIN permissions p
              ON p.permission IN (
                                  'PATIENT_CREATE',
                                  'PATIENT_READ',
                                  'DOCTOR_READ',
                                  'SCHEDULE_READ',
                                  'SCHEDULE_CREATE',
                                  'SCHEDULE_UPDATE',
                                  'SCHEDULE_DELETE',
                                  'APPOINTMENT_READ',
                                  'APPOINTMENT_CREATE',
                                  'APPOINTMENT_UPDATE',
                                  'APPOINTMENT_CANCEL',
                                  'PROFILE_READ_SELF',
                                  'PASSWORD_RESET'
                  )

WHERE r.role = 'ROLE_RECEPTIONIST';

-- ROLE_PATIENT --
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
         JOIN permissions p
              ON p.permission IN (
                                  'PATIENT_READ',
                                  'APPOINTMENT_READ',
                                  'LAB_RESULT_READ',
                                  'PROFILE_READ_SELF',
                                  'PASSWORD_RESET'
                  )

WHERE r.role = 'ROLE_PATIENT';

-- ROLE_LAB_TECHNICIAN --
INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
         JOIN permissions p
              ON p.permission IN (
                                  'PATIENT_READ',
                                  'LAB_RESULT_CREATE',
                                  'LAB_RESULT_UPDATE',
                                  'LAB_RESULT_READ',
                                  'PROFILE_READ_SELF',
                                  'PASSWORD_RESET'
                  )

WHERE r.role = 'ROLE_LAB_TECHNICIAN';