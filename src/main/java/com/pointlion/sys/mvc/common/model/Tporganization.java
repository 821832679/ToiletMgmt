package com.pointlion.sys.mvc.common.model;

import java.util.ArrayList;
import java.util.List;

import com.pointlion.sys.mvc.common.dto.ZtreeNode;
import com.pointlion.sys.mvc.common.model.base.BaseTporganization;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class Tporganization extends BaseTporganization<Tporganization> {
	
public static final Tporganization dao = new Tporganization();
	
	public List<Tporganization> findTop(){
		
		List<Tporganization> tpo = dao.find("select * from t_tporganization where ParentCode ='0'");
		return tpo;
	}

	public List<Tporganization> findChild(Integer orgId) {
		
		List<Tporganization> tpoChild = dao.find("select * from t_tporganization where ParentCode =?", orgId);
		return tpoChild;
	}

	public List<Tporganization> getAllTopOrg() {
		List<Tporganization> topOrgList = dao.find("select * from t_tporganization where ParentCode = '1'");
		return topOrgList;
	}

	public List<ZtreeNode> toZTreeNode(List<Tporganization> topOrgList, boolean open) {
		List<ZtreeNode> list = new ArrayList<ZtreeNode>();
		for(Tporganization org : topOrgList){
			ZtreeNode node = toZTreeNode(org);
			if(org.findChild(org.getOrgId())!=null&&org.findChild(org.getOrgId()).size()>0){
				node.setChildren(toZTreeNode(org.findChild(org.getOrgId()),open));
			}
			node.setOpen(open);
			list.add(node);
		}
		return list;
		
	}
	
	public ZtreeNode toZTreeNode(Tporganization org){
		ZtreeNode node = new ZtreeNode();
		node.setId(org.getOrgId().toString());
		node.setName(org.getName());
		return node;
	}

	
}