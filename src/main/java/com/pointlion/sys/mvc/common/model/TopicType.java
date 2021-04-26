package com.pointlion.sys.mvc.common.model;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.pointlion.sys.mvc.common.model.base.BaseTopicType;
import com.pointlion.sys.mvc.common.utils.StringUtil;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class TopicType extends BaseTopicType<TopicType> {

	public final static TopicType dao = new TopicType();

	/**
	 * 获得分页数据
	 */
	public Page<TopicType> getTopicByStatusPage(Integer curr, Integer pagesize, String status) {
		return TopicType.dao.paginate(curr, pagesize, "select *",
				" from t_topic_type where 1=1 "+ (StringUtil.isNotBlank(status)?" and status="+status:"")+ " order by sortvalue asc");
	}
	
	/**
	 * 获取全部数据
	 */
	public List<TopicType> getTopicByStatus(String status) {
		return TopicType.dao.find("select * from t_topic_type where 1=1 "+ (StringUtil.isNotBlank(status)?" and status="+status:"") +" order by sortvalue asc");
	}

	/***
	 * 删除
	 * 
	 * @param ids
	 */
	@Before(Tx.class)
	public void deleteByIds(String ids) {
		String idarr[] = ids.split(",");
		for (String id : idarr) {
			TopicType topic = TopicType.dao.findById(id);
			topic.delete();
		}
	}

	/***
	 * 启用/禁用
	 * 
	 * @param ids
	 */
	@Before(Tx.class)
	public void statusByIds(String ids, String status) {
		String idarr[] = ids.split(",");
		for (String id : idarr) {
			TopicType topic = TopicType.dao.findById(id);
			topic.setStatus(status);
			topic.update();
		}
	}
}