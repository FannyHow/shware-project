package com.netcracker.ncfallprojectrepo.module;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name="userproject")
public class UserProject {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_accepted")
    private Boolean isAccepted;

    @Column(name = "is_leader")
    private boolean isLeader;

    @Column(name = "is_favourite")
    private boolean isFavourite;

    @Column(name = "invitation_time")
    private LocalDateTime invitation_time;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_id", referencedColumnName = "id")
    private Project project;

    public UserProject(User user, Boolean isAccepted, boolean isLeader) {
        this.user = user;
        this.isAccepted = isAccepted;
        this.isLeader = isLeader;
        this.invitation_time = LocalDateTime.now();
    }

    public UserProject(User user, Boolean isAccepted, boolean isLeader, Project project) {
        this.user = user;
        this.isAccepted = isAccepted;
        this.isLeader = isLeader;
        this.invitation_time = LocalDateTime.now();
        this.project = project;
    }

    public UserProject(User user, Boolean isFavourite, Project project) {
        this.user = user;
        this.isAccepted = false;
        this.isLeader = false;
        this.isFavourite = isFavourite;
        this.invitation_time = LocalDateTime.now();
        this.project = project;
    }
}
