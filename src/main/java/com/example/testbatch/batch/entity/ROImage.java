package com.example.testbatch.batch.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;
import java.util.Date;

@ToString
@Getter
@NoArgsConstructor
@Entity
@Table(name = "RO_IMG_P")
public class ROImage {

    /**
     * RO번호
     */
//    @Id
    @Column(name = "RO_NO", nullable = false, columnDefinition = "VARCHAR(13)")
    private String roNO;

    /**
     * 이미지일련번호
     */
    @Id
    @Column(name = "IMG_SN", nullable = false, columnDefinition = "NUMBER(5)")
    private Long imgSn;

    /**
     * 원본이미지URL
     */
    @Setter
    @Column(name = "ORIGIN_IMG_URL", columnDefinition = "VARCHAR(1000)")
    private String originImgUrl;

    /**
     * 썸네일이미지URL
     */
    @Setter
    @Column(name = "THUMB_IMG_URL", columnDefinition = "VARCHAR(1000)")
    private String thumbImgUrl;

    /**
     * 사용여부
     */
    @Column(name = "USE_YN", columnDefinition = "VARCHAR(1)")
    private String useYn;

    /**
     * 원천시스템명
     */
    @Column(name = "SRC_SYS_NM", columnDefinition = "VARCHAR(100)")
    private String srcSysNm;

    /**
     * 생성자ID
     */
    @Column(name = "CRER_ID", columnDefinition = "VARCHAR(20)")
    private String crerId;

    /**
     * 생성일시
     */
    @Column(name = "CRE_DTM", columnDefinition = "TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creDtm;

    /**
     * 원천이미지경로
     */
    @Column(name = "SRC_IMG_URL", columnDefinition = "VARCHAR(512)")
    private String srcImgUrl;

    /**
     * 수정자ID
     */
    @Column(name = "UPDR_ID", columnDefinition = "VARCHAR(100)")
    private String updrId;

    /**
     * 수정일시
     */
    @Column(name = "MDFY_DTM", columnDefinition = "VARCHAR(20)")
    private String mdfyDtm;

    /**
     * 회사이름
     */
    @Column(name = "CPN_NM", columnDefinition = "VARCHAR(10)")
    private String cpnNm;

    /**
     * 리사이징상태
     */
    @Setter
    @Column(name = "RSZ_STAT", columnDefinition = "VARCHAR(20)")
    private String rszStat;

    /**
     * 테스트용
     */
    @Setter
    @Column(name = "TEST_TXT", columnDefinition = "VARCHAR(500)")
    private String testTxt;
}