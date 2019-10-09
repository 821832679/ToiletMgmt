package com.pointlion.sys.mvc.common.model.base;

import java.math.BigDecimal;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({ "serial", "unchecked" })
public abstract class BaseAnswer<M extends BaseAnswer<M>> extends Model<M> implements IBean {

	public M setId(java.lang.String id) {
		set("id", id);
		return (M) this;
	}

	public java.lang.String getId() {
		return getStr("id");
	}

	public M setCode(java.lang.String code) {
		set("code", code);
		return (M) this;
	}

	public java.lang.String getCode() {
		return getStr("code");
	}
	
	public M setName(java.lang.String name) {
		set("name", name);
		return (M) this;
	}
	
	public java.lang.String getName() {
		return getStr("name");
	}
	
	public M setType(java.lang.String type) {
		set("type", type);
		return (M) this;
	}
	
	public java.lang.String getType() {
		return getStr("type");
	}
	
	public M setIstag(java.lang.String istag) {
		if(StrKit.notBlank(istag)){
			set("istag", istag);
		}
		return (M) this;
	}
	
	public java.lang.String getIstag() {
		return getStr("istag");
	}
	
	public M setUnit(java.lang.String unit) {
		if(StrKit.notBlank(unit)){
			set("unit", unit);
		}
		return (M) this;
	}
	
	public java.lang.String getUnit() {
		return getStr("unit");
	}
	
	public M setRamount(java.math.BigDecimal ramount) {
		set("ramount", ramount);
		return (M) this;
	}
	
	public java.math.BigDecimal getRamount() {
		return getBigDecimal("ramount");
	}
	
	public M setSamount(java.math.BigDecimal samount) {
		set("samount", samount);
		return (M) this;
	}
	
	public java.math.BigDecimal getSamount() {
		return getBigDecimal("samount");
	}
	
	public M setStarTime(java.lang.String startime) {
		if(StrKit.notBlank(startime)){
			set("startime", startime);
		}
		return (M) this;
	}
	
	public java.lang.String getStarTime() {
		return getStr("startime");
	}
	
	public M setCleanTime(java.lang.String cleantime) {
		if(StrKit.notBlank(cleantime)){
			set("cleantime", cleantime);
		}
		return (M) this;
	}
	
	public java.lang.String getCleanTime() {
		return getStr("cleantime");
	}
	
	public M setNature(java.lang.String nature) {
		if(StrKit.notBlank(nature)){
			set("nature", nature);
		}
		return (M) this;
	}
	
	public java.lang.String getNature() {
		return getStr("nature");
	}
	
	public M setProvince(java.lang.String province) {
		if(StrKit.notBlank(province)){
			set("province", province);
		}
		return (M) this;
	}
	
	public java.lang.String getProvince() {
		return getStr("province");
	}
	
	public M setCity(java.lang.String city) {
		if(StrKit.notBlank(city)){
			set("city", city);
		}
		return (M) this;
	}
	
	public java.lang.String getCity() {
		return getStr("city");
	}
	
	public M setDistrict(java.lang.String district) {
		if(StrKit.notBlank(district)){
			set("district", district);
		}
		return (M) this;
	}
	
	public java.lang.String getDistrict() {
		return getStr("district");
	}
	
	public M setAddress(java.lang.String address) {
		if(StrKit.notBlank(address)){
			set("address", address);
		}
		return (M) this;
	}
	
	public java.lang.String getAddress() {
		return getStr("address");
	}
	
	public M setToileType(java.lang.String toiletype) {
		if(StrKit.notBlank(toiletype)){
			set("toiletype", toiletype);
		}
		return (M) this;
	}
	
	public java.lang.String getToileType() {
		return getStr("toiletype");
	}
	
	public M setLevel(java.lang.String level) {
		if(StrKit.notBlank(level)){
			set("level", level);
		}
		return (M) this;
	}
	
	public java.lang.String getLevel() {
		return getStr("level");
	}
	
	public M setMenum(java.lang.Integer menum) {
		if(menum != null){
			set("menum", menum);
		}
		return (M) this;
	}
	
	public java.lang.Integer getMenum() {
		return getInt("menum");
	}
	
	public M setWomanum(java.lang.Integer womanum) {
		if(womanum!=null){
			set("womanum", womanum);
		}
		return (M) this;
	}
	
	public java.lang.Integer getWomanum() {
		return getInt("womanum");
	}
	
	public M setBarriernum(java.lang.Integer barriernum) {
		if(barriernum!=null){
			set("barriernum", barriernum);
		}
		return (M) this;
	}
	
	public java.lang.Integer getBarriernum() {
		return getInt("barriernum");
	}
	
	public M setThreenum(java.lang.Integer threenum) {
		if(threenum!=null){
			set("threenum", threenum);
		}
		return (M) this;
	}
	
	public java.lang.Integer getThreenum() {
		return getInt("threenum");
	}
	
	public M setCurrencynum(java.lang.Integer currencynum) {
		if(currencynum!=null){
			set("currencynum", currencynum);
		}
		return (M) this;
	}
	
	public java.lang.Integer getCurrencynum() {
		return getInt("currencynum");
	}
	
	public M setAreas(java.math.BigDecimal areas) {
		if(areas!=null){
			set("areas", areas);
		}
		return (M) this;
	}
	
	public java.math.BigDecimal getAreas() {
		return getBigDecimal("areas");
	}
	
	public M setLongitude(java.lang.String longitude) {
		if(StrKit.notBlank(longitude)){
			set("longitude", longitude);
		}
		return (M) this;
	}
	
	public java.lang.String getLongitude() {
		return getStr("longitude");
	}
	
	public M setLatitude(java.lang.String latitude) {
		if(StrKit.notBlank(latitude)){
			set("latitude", latitude);
		}
		return (M) this;
	}
	
	public java.lang.String getLatitude() {
		return getStr("latitude");
	}
	
	public M setManages(java.lang.String manages) {
		if(StrKit.notBlank(manages)){
			set("manages", manages);
		}
		return (M) this;
	}
	
	public java.lang.String getManages() {
		return getStr("manages");
	}
	
	public M setPlanned(java.math.BigDecimal planned) {
		set("planned", planned);
		return (M) this;
	}
	
	public java.math.BigDecimal getPlanned() {
		return getBigDecimal("planned");
	}
	
	public M setSubsidy(java.math.BigDecimal subsidy) {
		set("subsidy", subsidy);
		return (M) this;
	}
	
	public java.math.BigDecimal getSubsidy() {
		return getBigDecimal("subsidy");
	}
	
	public M setBegintime(java.lang.String begintime) {
		if(StrKit.notBlank(begintime)){
			set("begintime", begintime);
		}
		return (M) this;
	}
	
	public java.lang.String getBegintime() {
		return getStr("begintime");
	}
	
	public M setEndtime(java.lang.String endtime) {
		if(StrKit.notBlank(endtime)){
			set("endtime", endtime);
		}
		return (M) this;
	}
	
	public java.lang.String getEndtime() {
		return getStr("endtime");
	}
	
	public M setState(java.lang.String state) {
		if(StrKit.notBlank(state)){
			set("state", state);
		}
		return (M) this;
	}
	
	public java.lang.String getState() {
		return getStr("state");
	}
	
	public M setYear(java.lang.String year) {
		if(StrKit.notBlank(year)){
			set("year", year);
		}
		return (M) this;
	}
	
	public java.lang.String getYear() {
		return getStr("year");
	}
	
	public M setPinlevel(java.lang.String pinlevel) {
		if(StrKit.notBlank(pinlevel)){
			set("pinlevel", pinlevel);
		}
		return (M) this;
	}
	
	public java.lang.String getPinlevel() {
		return getStr("pinlevel");
	}
	
	public M setPindate(java.lang.String pindate) {
		if(StrKit.notBlank(pindate)){
			set("pindate", pindate);
		}
		return (M) this;
	}
	
	public java.lang.String getPindate() {
		return getStr("pindate");
	}
	
	public M setPinunit(java.lang.String pinunit) {
		if(StrKit.notBlank(pinunit)){
			set("pinunit", pinunit);
		}
		return (M) this;
	}
	
	public java.lang.String getPinunit() {
		return getStr("pinunit");
	}
	
	public M setPinopinion(java.lang.String pinopinion) {
		if(StrKit.notBlank(pinopinion)){
			set("pinopinion", pinopinion);
		}
		return (M) this;
	}
	
	public java.lang.String getPinopinion() {
		return getStr("pinopinion");
	}
	
	public M setUpdatetime(java.lang.String updatetime) {
		if(StrKit.notBlank(updatetime)){
			set("updatetime", updatetime);
		}
		return (M) this;
	}
	
	public java.lang.String getUpdatetime() {
		return getStr("updatetime");
	}
	
	public M setCreateTime(java.lang.String createtime) {
		if(StrKit.notBlank(createtime)){
			set("createtime", createtime);
		}
		return (M) this;
	}

	public java.lang.String getCreateTime() {
		return getStr("createtime");
	}

	public M setTopicId(java.lang.String topicid) {
		set("topicid", topicid);
		return (M) this;
	}

	public java.lang.String getTopicId() {
		return getStr("topicid");
	}

	public M setQuestionId(java.lang.String questionid) {
		set("questionid", questionid);
		return (M) this;
	}

	public java.lang.String getQuestionId() {
		return getStr("questionid");
	}

	public M setEvaluate(java.lang.String evaluate) {
		set("evaluate", evaluate);
		return (M) this;
	}
	
	public java.lang.String getEvaluate() {
		return getStr("evaluate");
	}
	
	public M setMarkid(java.lang.String markid) {
		set("markid", markid);
		return (M) this;
	}
	
	public java.lang.String getMarkid() {
		return getStr("markid");
	}
	
	public M setMarktime(java.lang.String marktime) {
		set("marktime", marktime);
		return (M) this;
	}
	
	public java.lang.String getMarktime() {
		return getStr("marktime");
	}
	
	public M setPersonId(java.lang.String personid) {
		set("personid", personid);
		return (M) this;
	}

	public java.lang.String getPersonId() {
		return getStr("personid");
	}
	
	public M setRId(java.lang.String rid) {
		set("rid", rid);
		return (M) this;
	}

	public java.lang.String getRId() {
		return getStr("rid");
	}
	
	public M setFId(java.lang.String fid) {
		set("fid", fid);
		return (M) this;
	}
	
	public java.lang.String getFId() {
		return getStr("fid");
	}
	
	public M setRpath(java.lang.String rpath) {
		set("rpath", rpath);
		return (M) this;
	}
	
	public java.lang.String getRpath() {
		return getStr("rpath");
	}
	
	public M setManageunit(java.lang.String manageunit) {
		set("manageunit", manageunit);
		return (M) this;
	}
	
	public java.lang.String getManageunit() {
		return getStr("manageunit");
	}
	
	public M setManageinfo(java.lang.String manageinfo) {
		set("manageinfo", manageinfo);
		return (M) this;
	}
	
	public java.lang.String getManageinfo() {
		return getStr("manageinfo");
	}
	
	public M setManagetel(java.lang.String managetel) {
		set("managetel", managetel);
		return (M) this;
	}
	
	public java.lang.String getManagetel() {
		return getStr("managetel");
	}
	
	public M setDefender(java.lang.String defender) {
		set("defender", defender);
		return (M) this;
	}
	
	public java.lang.String getDefender() {
		return getStr("defender");
	}
	
	public M setDefendertel(java.lang.String defendertel) {
		set("defendertel", defendertel);
		return (M) this;
	}
	
	public java.lang.String getDefendertel() {
		return getStr("defendertel");
	}
	
	public M setTotalinvest(java.math.BigDecimal totalinvest) {
		set("totalinvest", totalinvest);
		return (M) this;
	}
	
	public java.math.BigDecimal getTotalinvest() {
		return getBigDecimal("totalinvest");
	}
	
	public M setZysubsidy(java.math.BigDecimal zysubsidy) {
		set("zysubsidy", zysubsidy);
		return (M) this;
	}
	
	public java.math.BigDecimal getZysubsidy() {
		return getBigDecimal("zysubsidy");
	}
	
	public M setSsubsidy(java.math.BigDecimal ssubsidy) {
		set("ssubsidy", ssubsidy);
		return (M) this;
	}
	
	public java.math.BigDecimal getSsubsidy() {
		return getBigDecimal("ssubsidy");
	}
	
}
