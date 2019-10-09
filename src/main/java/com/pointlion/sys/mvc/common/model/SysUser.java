package com.pointlion.sys.mvc.common.model;

import java.util.List;

import com.jfinal.aop.Before;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.pointlion.sys.mvc.common.model.base.BaseSysUser;
import com.pointlion.sys.mvc.common.utils.UuidUtil;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class SysUser extends BaseSysUser<SysUser> {
	public static final SysUser dao = new SysUser();
	
	/***
	 * 根据主键获取用户名
	 * @param userid
	 * @return
	 */
	public String getUsername(String userid){
		return getById(userid).getUsername();
	}
	
	/***
	 * 根据主键查询
	 */
	public SysUser getById(String id){
		return SysUser.dao.findById(id);
	}
	/***
	 * 根据用户名查询用户
	 * @param username
	 * @return
	 */
	public SysUser getByUsername(String username){
		return dao.findFirst("SELECT * from sys_user u where u.username='"+username+"'");
	}
	
	/***
	 * 根据用户名查询用户
	 * @param username
	 * @return
	 */
	public SysUser getLikeByUsername(String username){
		return dao.findFirst("SELECT * from sys_user u where u.username like '%"+username+"%'");
	}
	
	/***
	 * 获取用户所有的角色
	 */
	public List<SysRole> getAllRole(String id){
		return SysRole.dao.find("select r.* from sys_user_role u ,sys_role r where u.role_id=r.id and u.user_id='"+id+"'");
	}
	/***
	 * 根据机构下所有用户
	 */
	public List<SysUser> getUserListByOrgId(String orgid){
		return SysUser.dao.find("SELECT * from sys_user u where u.orgid='"+orgid+"'");
	}
	/***
	 * 获取分页
	 */
	public Page<Record> getPage(int pnum,int psize, String orgid){
		String sql  = " from sys_user u LEFT JOIN sys_org o  on o.id = u.orgid  where 1=1";
		if(StrKit.notBlank(orgid)){
			sql = sql + " and u.orgid='"+orgid+"' ";
		}
		sql = sql + " order by sort ";
		return Db.paginate(pnum, psize, " select u.*,o.name orgname ", sql);
	}
	
	/***
	 * 获取分页
	 */
	public Page<Record> getListPage(int pnum,int psize,String roleid){
		String sql  = " from sys_user u LEFT JOIN sys_role o  on o.order = u.roleid  where 1=1";
		if(StrKit.notBlank(roleid)){
			sql = sql + " and u.roleid='"+roleid+"' ";
		}
		sql = sql + " order by sort ";
		return Db.paginate(pnum, psize, " select u.*,o.name rolename ", sql);
	}
	
	/***
	 * 获取分页
	 */
	public Page<SysUser> findListPage(Integer pnum,Integer psize, String Roletype,String Searcht,String code){
		
     
		if(Searcht==""){
			if(Roletype=="")
			{
				if(code.equals(""))  //若为空，全部查询
				{
					return SysUser.dao.paginate(pnum, psize, "select u.*,o.name name1 ", " from sys_user u LEFT JOIN sys_role o on  u.roleid=o.id");
				}
				else  //若不为空，模糊查询
				{
					return SysUser.dao.paginate(pnum, psize, "select u.*,o.name name1 ", " from sys_user u LEFT JOIN sys_role o on  u.roleid=o.id where u.areaCode like '"+code+"%'");
				}
				
			};
		  }
		  else{
			  if(code.equals("")){
				return SysUser.dao.paginate(pnum, psize, "select u.*,o.name name1", " from sys_user u LEFT JOIN sys_role o on  u.roleid=o.id where CONCAT(u.username,u.roleid,u.name,sex) like concat('%', '"+Searcht+"','%')");
			  }
			  else{
				return SysUser.dao.paginate(pnum, psize, "select u.*,o.name name1", " from sys_user u LEFT JOIN sys_role o on  u.roleid=o.id where CONCAT(u.username,u.roleid,u.name,sex) like concat('%', '"+Searcht+"','%') and u.areaCode like '"+code+"%'");
			  }
		  }
			
		if(code.equals("")){
	      return SysUser.dao.paginate(pnum, psize, "select u.*,o.name name1", " from sys_user u LEFT JOIN sys_role o on  u.roleid=o.id  where u.roleid='"+Roletype+"'");
		}
		else{
		  return SysUser.dao.paginate(pnum, psize, "select u.*,o.name name1", " from sys_user u LEFT JOIN sys_role o on  u.roleid=o.id  where u.roleid='"+Roletype+"' and u.areaCode like '"+code+"%'");
		}
	}
	
	
	/***
	 * 删除
	 * @param ids
	 */
	@Before(Tx.class)
	public void deleteByIds(String ids){
    	String idarr[] = ids.split(",");
    	for(String id : idarr){
    		SysUser o = SysUser.dao.getById(id);
    		o.delete();
    	}
	}
	/***
	 * 删除当前用户所有项角色
	 */
	@Before(Tx.class)
	public Integer deleteRoleByUserid(String userid){
		return Db.update("delete from sys_user_role  where user_id='"+userid+"'");
	}
	
	/***
	 * 给用户赋值角色
	 * @param userid
	 * @param roledata
	 */
	@Before(Tx.class)
	public void giveUserRole(String userid ,String roledata){
		SysUser user = SysUser.dao.findById(userid);
		deleteRoleByUserid(userid);//删除用户下所有关系
		String rolearr[] = roledata.split(",");
    	for(String role : rolearr){//循环插入所有新关系
    		SysUserRole ur = new SysUserRole();
    		ur.setId(UuidUtil.getUUID());
    		ur.setUserId(userid);
    		ur.setRoleId(role);
    		ur.save();
    		user.setRoleid(role);
    	}
    	user.update();
	}
	
	/**
	 * 通过用户名查询用户
	 * @param username
	 * @return
	 */
	public SysUser findbyUserName(String username) {
		SysUser currUser = dao.findFirst("select * from sys_user where username = ?",username);
		return currUser;
	}

	public List<SysUser> findbyAreaCode(String code) {

		String sql = "select * from sys_user where areaCode like '"+code+"%'";
		List<SysUser> users = dao.find(sql);
		return users;
	}
}
