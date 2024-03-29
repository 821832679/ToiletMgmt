package com.pointlion.sys.mvc.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({ "serial", "unchecked" })
public abstract class BaseQuestion<M extends BaseQuestion<M>> extends Model<M> implements IBean {

	public M setId(java.lang.String id) {
		set("id", id);
		return (M) this;
	}

	public java.lang.String getId() {
		return getStr("id");
	}
	
	public M setTypeid(java.lang.String typeid) {
		set("typeid", typeid);
		return (M) this;
	}
	
	public java.lang.String getTypeid() {
		return getStr("typeid");
	}

	public M setDescrible(java.lang.String describle) {
		set("describle", describle);
		return (M) this;
	}

	public java.lang.String getDescrible() {
		return getStr("describle");
	}

	public M setUserId(java.lang.String userid) {
		set("userid", userid);
		return (M) this;
	}

	public java.lang.String getUserId() {
		return getStr("userid");
	}

	public M setCreateTime(java.lang.String createtime) {
		set("createtime", createtime);
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
	
	public M setEvaluate(java.lang.String evaluate) {
		set("evaluate", evaluate);
		return (M) this;
	}
	
	public java.lang.String getEvaluate() {
		return getStr("evaluate");
	}

	public M setScore(java.lang.String score) {
		set("score", score);
		return (M) this;
	}

	public java.lang.String getScore() {
		return getStr("score");
	}

	public M setSortValue(java.lang.Integer sortvalue) {
		set("sortvalue", sortvalue);
		return (M) this;
	}

	public java.lang.String getSortValue() {
		return getStr("sortvalue");
	}

	public M setStatus(java.lang.String status) {
		set("status", status);
		return (M) this;
	}

	public java.lang.String getStatus() {
		return getStr("status");
	}
	
	public M setFId(java.lang.String fid) {
		set("fid", fid);
		return (M) this;
	}
	
	public java.lang.String getFId() {
		return getStr("fid");
	}
	
	public M setIsstatis(java.lang.Integer isstatis) {
		set("isstatis", isstatis);
		return (M) this;
	}
	
	public java.lang.Integer getIsstatis() {
		return getInt("isstatis");
	}
}
