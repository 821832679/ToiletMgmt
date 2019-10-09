package com.pointlion.sys.mvc.common.model;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.druid.util.StringUtils;
import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.pointlion.sys.mvc.common.dto.ZtreeNode;
import com.pointlion.sys.mvc.common.model.base.BaseSysOrg;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class SysOrg extends BaseSysOrg<SysOrg> {
	public static final SysOrg dao = new SysOrg();
	
	/***
	 * 根据主键查询
	 */
	public SysOrg getById(String id){
		return SysOrg.dao.findById(id);
	}
	/***
	 * 删除
	 * @param ids
	 */
	@Before(Tx.class)
	public void deleteByIds(String ids){
    	String idarr[] = ids.split(",");
    	for(String id : idarr){
    		SysOrg o = SysOrg.dao.getById(id);
    		o.delete();
    	}
	}
	/***
	 * 根据id 查询孩子
	 * @param id
	 * @return
	 */
	public List<SysOrg> getChildrenByPid(String id){
		if (StringUtils.isEmpty(id)) {
			return SysOrg.dao.find("select * from sys_org");
		}
		return SysOrg.dao.find("select * from sys_org m where m.parent_id='"+id+"' order by m.sort");
	}
	/***
	 * 递归
	 * 查询孩子
	 * @param id
	 * @return
	 */
	public List<SysOrg> getChildrenAll(String id){
		List<SysOrg> list =  getChildrenByPid(id);//根据id查询孩子
		for(SysOrg o : list){
			System.out.println(o.getName());
			o.setChildren(getChildrenAll(o.getId()));
		}
		return list;
	}
	/***
	 * 菜单转成ZTreeNode
	 * @param 
	 * olist 数据
	 * open  是否展开所有
	 * ifOnlyLeaf 是否只选叶子
	 * @return
	 */
	public List<ZtreeNode> toZTreeNode(List<SysOrg> olist,Boolean open,Boolean ifOnlyLeaf){
		List<ZtreeNode> list = new ArrayList<ZtreeNode>();
		for(SysOrg o : olist){
			ZtreeNode node = toZTreeNode(o);
			if(o.getChildren()!=null&&o.getChildren().size()>0){//如果有孩子
				node.setChildren(toZTreeNode(o.getChildren(),open,ifOnlyLeaf));
				if(ifOnlyLeaf){//如果只选叶子
					node.setNocheck(true);
				}
			}
			node.setOpen(open);
			list.add(node);
		}
		return list;
	}
	/***
	 * 菜单转成ZtreeNode
	 * @param 
	 * @return
	 */
	public ZtreeNode toZTreeNode(SysOrg menu){
		ZtreeNode node = new ZtreeNode();
		node.setId(menu.getId());
		node.setName(menu.getName());
		return node;
	}
	
	/***
	 * 根据id 查询孩子分页
	 * @param id
	 * @return
	 */
	public Page<Record> getChildrenPageByPid(int pnum,int psize, String pid){
		String sql = " from sys_org o1 LEFT JOIN sys_org o2 on o1.parent_id=o2.id ";
		if(StrKit.notBlank(pid)){
			sql = sql + " where o1.parent_id='"+pid+"' ";
		}
		sql = sql + " order by o1.sort ";
		return Db.paginate(pnum, psize, "select o1.* , o2.name parent_name ", sql);
	}
	public SysOrg findByOrgName(String nodeName) {
		return dao.findFirst("select * from sys_org where name = ?",nodeName);
	}
	
	/**
	 * 查询孩子机构
	 * @param orgid
	 * @return
	 */
	public List<SysOrg> findByParentId(String orgid) {

	
		String sql = "select * from sys_org where parent_id = ?";
		List<SysOrg> orgs = dao.find(sql, orgid);
		return orgs;
	}
	
	public List<SysOrg> getbyExcList(String topicid){
		return dao.find("SELECT qxu.username qxusername, t.evaluate evaluate, t.describle describle, t.marktime marktime, t.zjname zjname, t.zjpfbz zjpfbz, t.zjpfms zjpfms, t.zjtime zjtime, u.id userid, u.username username, ps.`name` parentname, s.* FROM sys_org s LEFT JOIN sys_org ps ON ps.id = s.parent_id LEFT JOIN sys_user u ON u.orgid = s.id LEFT JOIN t_score t ON t.userid = u.id LEFT JOIN sys_user qxu ON t.personid = qxu.id WHERE s.type = 2 AND u. STATUS = '1' AND t.topicid = '"+topicid+"' ORDER BY s.areaCode ASC");
	}
	
}
