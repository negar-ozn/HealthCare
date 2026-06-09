package com.healthcare.healthcare.entitiy;

import com.healthcare.healthcare.enums.Action;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table
@Entity(name = "AUDIT_LOG")
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Action action; //I don't know yet
    private String entity; //I don't know yet
    private LocalTime timeStamp;
    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private Users user;
}
