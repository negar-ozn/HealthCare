package com.healthcare.healthcare.dto;

import com.healthcare.healthcare.entitiy.Users;
import com.healthcare.healthcare.enums.Action;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuditLogDto {

    private Long id;
    private Users userId;
    private Action action; //I don't know yet
    private String entity; //I don't know yet
    private LocalTime timeStamp;
}
