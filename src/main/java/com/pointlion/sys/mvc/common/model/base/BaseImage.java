package com.pointlion.sys.mvc.common.model.base;

import com.jfinal.plugin.activerecord.Model;
import com.jfinal.plugin.activerecord.IBean;

/**
 * Generated by JFinal, do not modify this file.
 */
@SuppressWarnings({ "serial", "unchecked" })
public abstract class BaseImage<M extends BaseImage<M>> extends Model<M> implements IBean {

	public M setId(java.lang.String id) {
		set("id", id);
		return (M) this;
	}

	public java.lang.String getId() {
		return getStr("id");
	}

	public M setUserId(java.lang.String userid) {
		set("userid", userid);
		return (M) this;
	}

	public java.lang.String getUserId() {
		return getStr("userid");
	}

	public M setCategoryId(java.lang.String categoryid) {
		set("categoryid", categoryid);
		return (M) this;
	}

	public java.lang.String getCategoryId() {
		return getStr("categoryid");
	}

	public M setConfig(java.lang.String config) {
		set("config", config);
		return (M) this;
	}

	public java.lang.String getConfig() {
		return getStr("config");
	}

	public M setCreateDate(java.lang.String createdate) {
		set("createdate", createdate);
		return (M) this;
	}

	public java.lang.String getCreateDate() {
		return getStr("createdate");
	}

	public M setName(java.lang.String name) {
		set("name", name);
		return (M) this;
	}

	public java.lang.String getName() {
		return getStr("name");
	}

	public M setImageUrl(java.lang.String imageurl) {
		set("imageurl", imageurl);
		return (M) this;
	}

	public java.lang.String getImageUrl() {
		return getStr("imageurl");
	}

	public M setWidth(java.lang.String width) {
		set("width", width);
		return (M) this;
	}

	public java.lang.String getWidth() {
		return getStr("width");
	}

	public M setHeight(java.lang.String height) {
		set("height", height);
		return (M) this;
	}

	public java.lang.String getHeight() {
		return getStr("height");
	}

	public M setMd5str(java.lang.String md5str) {
		set("md5str", md5str);
		return (M) this;
	}

	public java.lang.String getMd5str() {
		return getStr("md5str");
	}

	public M setImageType(java.lang.String imagetype) {
		set("imagetype", imagetype);
		return (M) this;
	}

	public java.lang.String getImageType() {
		return getStr("imagetype");
	}
}