package com.project.gansul.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Getter
@Setter
@ToString
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
@Table(name = "alcohol_down")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class AlcoholDown extends BasicEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long upId;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(nullable = false)
    private Long price;

    @Column(length = 255, nullable = false)
    private String sfe;

    @Column(length = 50, nullable = false)
    private String alcohol;

    @Column(length = 255, nullable = false)
    private String image;

    @Column(nullable = false)
    private Long clickCnt;
}
