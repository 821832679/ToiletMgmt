package com.pointlion.sys.mvc.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({"serial", "unchecked"})
public abstract class BaseTpclassify<M extends BaseTpclassify<M>> extends Model<M> implements IBean {

	public M setTpcCode(java.lang.Integer tpcCode) {
		set("TPC_Code", tpcCode);
		return (M)this;
	}
	
	public java.lang.Integer getTpcCode() {
		return getInt("TPC_Code");
	}

	public M setValue(java.lang.String Value) {
		set("Value", Value);
		return (M)this;
	}
	
	public java.lang.String getValue() {
		return getStr("Value");
	}

	public M setOrder(java.lang.Integer Order) {
		set("Order", Order);
		return (M)this;
	}
	
	public java.lang.Integer getOrder() {
		return getInt("Order");
	}

	public M setPY(java.lang.String PY) {
		set("PY", PY);
		return (M)this;
	}
	
	public java.lang.String getPY() {
		return getStr("PY");
	}

	public M setRemark(java.lang.String Remark) {
		set("Remark", Remark);
		return (M)this;
	}
	
	public java.lang.String getRemark() {
		return getStr("Remark");
	}

	public M setMenderId(java.lang.String menderId) {
		set("Mender_Id", menderId);
		return (M)this;
	}
	
	public java.lang.String getMenderId() {
		return getStr("Mender_Id");
	}

	public M setModifyTime(java.util.Date ModifyTime) {
		set("ModifyTime", ModifyTime);
		return (M)this;
	}
	
	public java.util.Date getModifyTime() {
		return get("ModifyTime");
	}

	public M setCreatorId(java.lang.String creatorId) {
		set("Creator_Id", creatorId);
		return (M)this;
	}
	
	public java.lang.String getCreatorId() {
		return getStr("Creator_Id");
	}

	public M setCreateTime(java.util.Date CreateTime) {
		set("CreateTime", CreateTime);
		return (M)this;
	}
	
	public java.util.Date getCreateTime() {
		return get("CreateTime");
	}

}
