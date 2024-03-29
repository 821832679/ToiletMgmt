package com.pointlion.sys.mvc.common.model;

import java.util.ArrayList;
import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.pointlion.sys.mvc.common.model.base.BaseTCustomFieldTemplate;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class TCustomFieldTemplate extends BaseTCustomFieldTemplate<TCustomFieldTemplate> {
	
	public final static TCustomFieldTemplate dao =  new TCustomFieldTemplate();
	
	/**
	 * 获得分页数据
	 */
	public Page<TCustomFieldTemplate> getPmPage(Integer curr , Integer pagesize){
		return TCustomFieldTemplate.dao.paginate(curr, pagesize, "select * ", " from t_custom_field_template");
	}
	
	/***
	 * 删除
	 * @param ids
	 */
	@Before(Tx.class)
	public void deleteByIds(String ids){
		String idarr[] = ids.split(",");
    	for(String id : idarr){
    		TCustomFieldTemplate template = TCustomFieldTemplate.dao.findById(id);
    		template.delete();
    	}
	}

	//查询所有的context
	public List<CustomJson> findAllContext() {
		List<TCustomFieldTemplate> templateList = dao.find("select * from t_custom_field_template");
		List<CustomJson> jsons =  new ArrayList<CustomJson>();
		for(int i = 0;i<templateList.size();i++){
			CustomJson json = new CustomJson();
			json.setLabel(templateList.get(i).getName());
			json.setFieldName(templateList.get(i).getFieldName());
			json.setReadOnly("true");
			json.setType("textfield");
			json.setDefaultValue("");
			//json.setValue("#(j?j."+templateList.get(i).getFieldName()+":'')");
			jsons.add(json);
		}
		return jsons;
	}
	
}
