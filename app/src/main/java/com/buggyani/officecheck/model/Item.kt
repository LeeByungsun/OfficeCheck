package com.buggyani.officecheck.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Item(
    @SerializedName("bzowrRgstNo") var bzowrRgstNo: String,//사업장등록번호
    @SerializedName("dataCrtYm") var dataCrtYm: Int,   //자료생성년월
    @SerializedName("ldongAddrMgplDgCd") var ldongAddrMgplDgCd: Int,//법정동주소광역시코드
    @SerializedName("ldongAddrMgplSgguCd") var ldongAddrMgplSgguCd: Int,//법정동주소시군구코드
    @SerializedName("ldongAddrMgplSgguEmdCd") var ldongAddrMgplSgguEmdCd: Int,//법정동주소읍면동 코드
    @SerializedName("seq") var seq: Int,//식별번호
    @SerializedName("wkplJnngStcd") var wkplJnngStcd: Int,//사업장 가입상태코드
    @SerializedName("wkplNm") var wkplNm: String,//사업장명
    @SerializedName("wkplRoadNmDtlAddr") var wkplRoadNmDtlAddr: String,//사업장 도로명상세
    @SerializedName("wkplStylDvcd") var wkplStylDvcd: Int,//사업장 형태구분코드

//detail
    @SerializedName("adptDt") var adptDt: Int,//사업장등록일
    @SerializedName("crrmmNtcAmt") var crrmmNtcAmt: Int,//당월고지금액
    @SerializedName("jnngpCnt") var jnngpCnt: Int,//가입자수
    @SerializedName("scsnDt") var scsnDt: String,//사업장 탈퇴일
    @SerializedName("vldtVlKrnNm") var vldtVlKrnNm: String,//사업장업종코드명
    @SerializedName("wkplIntpCd") var wkplIntpCd: String,//사업업종코드

//term
    @SerializedName("nwAcqzrCnt") var nwAcqzrCnt: Int,//월별 취업자수
    @SerializedName("lssJnngpCnt") var lssJnngpCnt: Int//월별 퇴직자수
) : Serializable