package com.pointlion.sys.mvc.admin.zjscore;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.druid.util.StringUtils;
import com.jfinal.aop.Before;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.pointlion.sys.interceptor.MainPageTitleInterceptor;
import com.pointlion.sys.mvc.common.base.BaseController;
import com.pointlion.sys.mvc.common.model.Answer;
import com.pointlion.sys.mvc.common.model.Question;
import com.pointlion.sys.mvc.common.model.Resource;
import com.pointlion.sys.mvc.common.model.Score;
import com.pointlion.sys.mvc.common.model.SysOrg;
import com.pointlion.sys.mvc.common.model.SysUser;
import com.pointlion.sys.mvc.common.model.Topic;
import com.pointlion.sys.mvc.common.model.TopicType;
import com.pointlion.sys.mvc.common.utils.ArabicToChineseUtil;
import com.pointlion.sys.mvc.common.utils.DateUtils;
import com.pointlion.sys.mvc.common.utils.ImageResizeUtil;
import com.pointlion.sys.mvc.common.utils.SysOrgUtil;

@Before(MainPageTitleInterceptor.class)
public class ZjScoreManagementController extends BaseController {

	/*************************** 专家评分管理---开始 ***********************/

	/**
	 * 获得专家评分管理页面
	 */
	public void getListPage() {
		render("/WEB-INF/admin/zjscore/list.html");
	}
	
	/**
	 * 获得专家评分管理数据
	 * @throws UnsupportedEncodingException 
	 */
	public void listData() throws UnsupportedEncodingException {
		String username = getSessionAttr("username");
		SysUser currUser = SysUser.dao.findbyUserName(username);
		String curr = getPara("pageNumber");
		Float currFloat = Float.valueOf(curr);
		Integer currPage = currFloat.intValue();
		String pageSize = getPara("pageSize");
		String Searcht1 = getPara("SearchValue");
		String orgid = getPara("orgid");
		String orgids = "";
		String Searcht = Searcht1;
		if (Searcht1 != "") {
			Searcht = URLDecoder.decode(Searcht1, "UTF-8");
		}
		if(StrKit.notBlank(orgid)){
			List<SysOrg> sysOrgList = SysOrg.dao.getChildrenAll(orgid);
			orgids = SysOrgUtil.getOrgListForString(sysOrgList, orgid);
		}else{
			
			String ids = "";
			if (StringUtils.isEmpty(currUser.getOrgid())) {
				ids = "#root";
			} else {
				ids = currUser.getOrgid();
			}
			List<SysOrg> sysOrgList = SysOrg.dao.getChildrenAll(ids);
			orgids = SysOrgUtil.getOrgListForString(sysOrgList, currUser.getOrgid());
		}
		Page<Answer> page = Answer.dao.getAnswerPage(currPage, Integer.valueOf(pageSize),Searcht,orgids);
		for (Answer answer : page.getList()) {
			if(StrKit.notBlank(answer.getPersonId())){
				SysUser user = SysUser.dao.findById(answer.getPersonId());
				answer.put("username", user.getName());
			}
			
			if(StrKit.notBlank(answer.getMarkid())){
				SysUser mark = SysUser.dao.findById(answer.getMarkid());
				answer.put("markname", mark.getName());
			}
		}
		renderPage(page.getList(), "", page.getTotalRow());
	}

	/**
	 * 获得指标管理页面
	 */
	public void getListByIDPage() {
		String id = getPara("id");
		String topicid = getPara("topicid");
		if (StrKit.notBlank(id)) {
			Answer templpate = Answer.dao.findById(id);
			setAttr("t", templpate);
		}
		if(StrKit.notBlank(topicid)){
			setAttr("topicid", topicid);
		}
		setAttr("topictype", TopicType.dao.getTopicByStatus("1"));
		setAttr("imgurl", PropKit.get("image.domain"));
		render("/WEB-INF/admin/zjscore/edit.html");
	}

	/**
	 * 追加保存
	 */
	public void saveAppend() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Score score = getModel(Score.class);
		String username = getSessionAttr("username");
		SysUser user = SysUser.dao.findbyUserName(username);
		
		if (StrKit.notBlank(score.getId())) {
			String zjtime = sf.format(new Date());
			Score data = Score.dao.findById(score.getId());
			if(data.getZjpfms()==null)
				data.setZjpfms("");
			score.setZjpfms(data.getZjpfms()+"\n\n"+user.getUsername()+"于"+zjtime+"追加了评论：\n"+score.getZjpfms());
			score.setZjId(user.getId());
			score.setZjTime(zjtime);
			score.setZjName(user.getUsername());
			score.update();
		}
		renderSuccess();
	}
	
	/**
	 * 保存
	 */
	public void save() {
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Score score = getModel(Score.class);
		String username = getSessionAttr("username");
		SysUser user = SysUser.dao.findbyUserName(username);

		if (StrKit.notBlank(score.getId())) {
			score.setZjId(user.getId());
			score.setZjTime(sf.format(new Date()));
			score.setZjName(user.getUsername());
			score.update();
		}
		renderSuccess();
	}

	/************************ 数据库管理---结束 *************************************************/

	/**************************************************************************/
	private String pageTitle = "专家指标评分";// 模块页面标题
	private String breadHomeMethod = "getListPage";// 面包屑首页方法

	public Map<String, String> getPageTitleBread() {
		Map<String, String> pageTitleBread = new HashMap<String, String>();
		pageTitleBread.put("pageTitle", pageTitle);
		pageTitleBread.put("breadHomeMethod", breadHomeMethod);
		return pageTitleBread;
	}
}