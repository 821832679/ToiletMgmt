package com.pointlion.sys.mvc.admin.score;

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
import com.pointlion.sys.mvc.common.model.AnswerExcel;
import com.pointlion.sys.mvc.common.model.Question;
import com.pointlion.sys.mvc.common.model.Resource;
import com.pointlion.sys.mvc.common.model.Score;
import com.pointlion.sys.mvc.common.model.SysOrg;
import com.pointlion.sys.mvc.common.model.SysUser;
import com.pointlion.sys.mvc.common.model.Topic;
import com.pointlion.sys.mvc.common.model.TopicDelay;
import com.pointlion.sys.mvc.common.model.TopicType;
import com.pointlion.sys.mvc.common.utils.ArabicToChineseUtil;
import com.pointlion.sys.mvc.common.utils.DateUtils;
import com.pointlion.sys.mvc.common.utils.ImageResizeUtil;
import com.pointlion.sys.mvc.common.utils.SysOrgUtil;
import com.pointlion.sys.mvc.common.utils.UuidUtil;

@Before(MainPageTitleInterceptor.class)
public class ScoreManagementController extends BaseController {

	/*************************** 审核管理---开始 ***********************/

	/**
	 * 获得审核管理页面
	 */
	public void getListPage() {
		render("/WEB-INF/admin/score/list.html");
	}
	
	/**
	 * 获得审核管理数据
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
		String isperson = getPara("isperson");
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
		
		
		Page<Answer> page = Answer.dao.getAnswerPage(currPage, Integer.valueOf(pageSize),Searcht,orgids,isperson);
		for (Answer answer : page.getList()) {
			if(StrKit.notBlank(answer.getPersonId())){
				SysUser user = SysUser.dao.findById(answer.getPersonId());
				answer.put("username", user.getName());
			}
			
			if(StrKit.notBlank(answer.getMarkid())){
				SysUser mark = SysUser.dao.findById(answer.getMarkid());
				answer.put("markname", mark.getName());
			}
			
			Topic topic = Topic.dao.findById(answer.getTopicId());
			answer.put("title", topic.getName());
			answer.put("qxbegintime", DateUtils.covert2YMd(DateUtils.convert2YMdhmsTime(topic.getBeginTime())));
			answer.put("qxendtime", DateUtils.covert2YMd(DateUtils.convert2YMdhmsTime(topic.getQxEndTime())));
			
			//查询是否延期
			TopicDelay delay = TopicDelay.dao.find(answer.getTopicId(), answer.getPersonId());
			if(delay!=null && delay.getStat()==1){
				answer.put("yqendtime", DateUtils.convert2YMdhm(DateUtils.convert2YMdhmsTime(delay.getYqendTime())));
			}
			//查询是否延期审核
			TopicDelay delaysh = TopicDelay.dao.find(answer.getTopicId(), currUser.getId());
			if(delaysh!=null && delaysh.getStat()==1){
				answer.put("yqshendtime", DateUtils.convert2YMdhm(DateUtils.convert2YMdhmsTime(delaysh.getYqendTime())));
			}
		}
		
		renderPage(page.getList(), "", page.getTotalRow());
	}
	
	/**
	 * 获得审核列表管理数据
	 * @throws UnsupportedEncodingException 
	 */
	public void listDatas() throws UnsupportedEncodingException {
		String username = getSessionAttr("username");
		SysUser currUser = SysUser.dao.findbyUserName(username);
		String curr = getPara("pageNumber");
		Float currFloat = Float.valueOf(curr);
		Integer currPage = currFloat.intValue();
		String pageSize = getPara("pageSize");
		String Searcht1 = getPara("SearchValue");
		String orgid = getPara("orgid");
		String evaluates = getPara("evaluates");
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
		
		
		Page<Answer> page = Answer.dao.getAnswerPages(currPage, Integer.valueOf(pageSize),Searcht,orgids,evaluates);
		for (Answer answer : page.getList()) {
			if(StrKit.notBlank(answer.getPersonId())){
				SysUser user = SysUser.dao.findById(answer.getPersonId());
				answer.put("username", user.getName());
			}
			
			if(StrKit.notBlank(answer.getMarkid())){
				SysUser mark = SysUser.dao.findById(answer.getMarkid());
				answer.put("markname", mark.getName());
			}
			
			Topic topic = Topic.dao.findById(answer.getTopicId());
			answer.put("title", topic.getName());
			answer.put("qxbegintime", DateUtils.covert2YMd(DateUtils.convert2YMdhmsTime(topic.getBeginTime())));
			answer.put("qxendtime", DateUtils.covert2YMd(DateUtils.convert2YMdhmsTime(topic.getQxEndTime())));
			
			//查询是否延期
			TopicDelay delay = TopicDelay.dao.find(answer.getTopicId(), answer.getPersonId());
			if(delay!=null && delay.getStat()==1){
				answer.put("yqendtime", DateUtils.convert2YMdhm(DateUtils.convert2YMdhmsTime(delay.getYqendTime())));
			}
			//查询是否延期审核
			TopicDelay delaysh = TopicDelay.dao.find(answer.getTopicId(), currUser.getId());
			if(delaysh!=null && delaysh.getStat()==1){
				answer.put("yqshendtime", DateUtils.convert2YMdhm(DateUtils.convert2YMdhmsTime(delaysh.getYqendTime())));
			}
		}
		
		renderPage(page.getList(), "", page.getTotalRow());
	}
	
	
	/**
	 * 获得指标管理页面
	 */
	public void getListByIDPage() {
		String topicid = getPara("topicid");
		String userid = getPara("userid");
		if (topicid != null && !"".equals(topicid)) {
			Topic topic = Topic.dao.findById(topicid);
			if (topic != null) {
				boolean bol = DateUtils.isEffectiveDate(new Date(), DateUtils.convert2YMdhmsTime(topic.getQxBeginTime()), DateUtils.convert2YMdhmsTime(topic.getQxEndTime()));
				setAttr("topicTimeStat", bol);
				topic.setBeginTime(DateUtils.convert2YMdhms(DateUtils.convert2YMdhmsTime(topic.getBeginTime())));
				topic.setEndTime(DateUtils.convert2YMdhms(DateUtils.convert2YMdhmsTime(topic.getEndTime())));
				topic.setQxBeginTime(DateUtils.convert2YMdhms(DateUtils.convert2YMdhmsTime(topic.getQxBeginTime())));
				topic.setQxEndTime(DateUtils.convert2YMdhms(DateUtils.convert2YMdhmsTime(topic.getQxEndTime())));
				topic.setZjBeginTime(DateUtils.convert2YMdhms(DateUtils.convert2YMdhmsTime(topic.getZjBeginTime())));
				topic.setZjEndTime(DateUtils.convert2YMdhms(DateUtils.convert2YMdhmsTime(topic.getZjEndTime())));
				setAttr("topic", topic);
			}
		}
		String username = getSessionAttr("username");
		SysUser user = SysUser.dao.findbyUserName(username);
		//判断是否延期审核
		TopicDelay delaysh = TopicDelay.dao.find(topicid, user.getId());
		if(delaysh!=null && delaysh.getStat()==1){
			setAttr("yqshendtime", DateUtils.convert2YMdhm(DateUtils.convert2YMdhmsTime(delaysh.getYqendTime())));
			setAttr("topicTimeStat", true);
		}
		setAttr("topicid", topicid);
		setAttr("name", user.getName());
		Map<String,String> map = getSjHtml(topicid,userid);
		setAttr("sjhtml", map.get("content"));
		setAttr("total", map.get("total"));
		
		String id = getPara("id");
		if (StrKit.notBlank(id)) {
			Score templpate = Score.dao.findById(id);
			if(templpate.getMarkTime()==null){
				templpate.setScore("");
			}
			setAttr("t", templpate);
		}
		render("/WEB-INF/admin/score/edit.html");
	}
	
	/**
	 * 获得编辑页面
	 */
	public void getEditPage() {
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
		render("/WEB-INF/admin/score/nedit.html");
	}
	
	public Map<String,String> getSjHtml(String topicid,String userid) {
		Map<String,String> map = new HashMap<>();
		StringBuffer buff = new StringBuffer();
		Page<Question> page = Question.dao.getQuestionPage(1, 100, topicid,null,null);
		List<TopicType> tlist = TopicType.dao.getTopicByStatus("1");
		int index = 1;
		for (TopicType type : tlist) {
			StringBuffer sb = new StringBuffer();
			boolean bol = false;
			sb.append("<div class='panel panel-default space'>");
			sb.append("<div class='panel-heading'>");
			sb.append("<h3 class='panel-title'>"+ArabicToChineseUtil.foematInteger(index)+"."+type.getName()+"</h3>");
			sb.append("</div>");
			sb.append("<div class='panel-body'>");
			for (int i = 0; i < page.getList().size(); i++) {
				Question question = page.getList().get(i);
				if(type.getId().equals(question.getTypeid())){
					Page<Answer> page2 = Answer.dao.getAnswerByquestion(1, 100, question.getId(), userid);
					String content = "";
					String evaluate = "";
					String answerid = "";
					String rids = "";
					String fid = "";
					if(page2!=null && page2.getList().size()>0){
						Answer ob = page2.getList().get(0);
//						content = ob.getDescrible();
						evaluate = ob.getEvaluate();
						answerid = ob.getId();
						rids = ob.getRId();
						fid = ob.getFId();
					}
					sb.append("<p>");
					sb.append(question.getDescrible());
					if(StrKit.notBlank(question.getFId())){
						sb.append("<p id='uploadAnswerFile"+(i + 1)+"'>");
						if (!StringUtils.isEmpty(fid)) {
							Resource resource = Resource.dao.findById(fid);
							if (resource != null) {
								String fileName = resource.getRname();
								String href = this.getRequest().getContextPath() + "/admin/resources/downFile?rid=" + fid;
								sb.append("<div id='file"+fid+"'><a class='answer-file' href='" + href + "'>" + fileName + "</a></div>");
							}
						}
						sb.append("</p>");
					}
					sb.append("<textarea  class='form-control' style='width:1610px;height:300px;' id='content"+(i + 1)+"' name='"+question.getId()+"' questionid='"+question.getId()+"' readOnly>"+content+"</textarea>");
					sb.append("<p id='uploadFile"+(i + 1)+"'>");
					if (!StringUtils.isEmpty(rids)) {
						String rid[] = rids.split(",");
						int count = 0;
						for (int j = 0; j < rid.length; j++) {
							count++;
							Resource resource = Resource.dao.findById(rid[j]);
							if (resource != null) {
								String fileName = resource.getRname();
								String href = this.getRequest().getContextPath() + "/admin/resources/downFile?rid=" + rid[j];
								String ahref = "<a href='" + href + "'>";
								if(ImageResizeUtil.isImgSuffix(resource.getSuffix())){
									ahref = "<a href='" + href + "' target='_blank'>";
								}
								if (count > 1) {
									sb.append("<br><div id='file"+rid[j]+"'>"+ ahref + fileName + "</a></div>");
								} else {
									sb.append("<div id='file"+rid[j]+"'>"+ ahref + fileName + "</a></div>");
								}
							}
						}
					}
					sb.append("</p>");
					sb.append("<div style='width:100%;float: left;'>");
					sb.append("<lable style='float:left;'>评价：</lable><div class='radio-check'><input type='radio' name='"+answerid+"' id='"+answerid+"1' value='优秀' onclick='saveAnswer(this)' "+("优秀".equals(evaluate)?"checked=true":"")+"/><label for='"+answerid+"1'>优秀</label></div>");
					sb.append("<div class='radio-check'><input type='radio' name='"+answerid+"' id='"+answerid+"2' value='合格' onclick='saveAnswer(this)' "+("合格".equals(evaluate)?"checked=true":"")+"/><label for='"+answerid+"2'>合格</label></div>");
					sb.append("<div class='radio-check'><input type='radio' name='"+answerid+"' id='"+answerid+"3' value='不合格' onclick='saveAnswer(this)' "+("不合格".equals(evaluate)?"checked=true":"")+"/><label for='"+answerid+"3'>不合格</label></div>");
					/*sb.append("评价：<label style='margin-left:10px;height: 25px;'><input name='"+answerid+"' type='radio' value='优秀' onclick='saveAnswer(this)' "+("优秀".equals(evaluate)?"checked=true":"")+"/>优秀</label>");
					sb.append("<label style='margin-left:10px;height: 25px;'><input name='"+answerid+"' type='radio' value='合格' onclick='saveAnswer(this)' "+("合格".equals(evaluate)?"checked=true":"")+"/>合格</label>");
					sb.append("<label style='margin-left:10px;height: 25px;'><input name='"+answerid+"' type='radio' value='不合格' onclick='saveAnswer(this)' "+("不合格".equals(evaluate)?"checked=true":"")+"/>不合格</label>");*/
					sb.append("</div>");
					sb.append("</p>");
					bol = true;
				}
			}
			sb.append("</div>");
			sb.append("</div>");
			if(bol){
				buff.append(sb.toString());
				index++;
			}
		}
		map.put("content", buff.toString());
		map.put("total", page.getList().size()+"");
		return map;
	}
	
	/**
	 * 保存
	 */
	public void save() {
		Answer answer = getModel(Answer.class);
		String topicid = answer.getTopicId();
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String username = getSessionAttr("username");
		SysUser user = SysUser.dao.findbyUserName(username);
		
		if (StrKit.notBlank(answer.getId())) {
			answer.setUpdatetime(DateUtils.getYMdhmsTime());
			answer.setMarkid(user.getId());
			answer.setMarktime(DateUtils.getYMdhmsTime());
			answer.update();
		} else {
			Topic topic = Topic.dao.findById(topicid);
			boolean bol = DateUtils.isEffectiveDate(new Date(), DateUtils.convert2YMdhmsTime(topic.getBeginTime()), DateUtils.convert2YMdhmsTime(topic.getEndTime()));
			if(!bol){
				//判断是否延期考核
				TopicDelay delay = TopicDelay.dao.find(topicid, user.getId());
				if(delay==null || delay.getStat()!=1){
					renderError("考核未开始或已结束");
					return;
				}
			}
			//删除原有评价和回答指标
			List<Score> scores = Score.dao.getScoreList(topicid, user.getId());
			for (Score s : scores) {
				s.delete();
			}
			List<Answer> answers = Answer.dao.getAnswerList(topicid, user.getId());
			for (Answer a : answers) {
				a.delete();
				AnswerExcel.dao.deleteByAnswerid(a.getId());
			}
			//保存指标
			Page<Question> page = Question.dao.getQuestionPage(1, 100, topicid,null,null);
			for (int i = 0; i < page.getList().size(); i++) {
				Question question = page.getList().get(i);
				answer = getModel(Answer.class);
				answer.setQuestionId(question.getId());
				answer.setTopicId(topicid);
				answer.setId(UuidUtil.getUUID());
				answer.setPersonId(user.getId());
				answer.setCreateTime(sf.format(new Date()));
				bol = answer.save();
			}
			Score score = getModel(Score.class);
			score.setId(UuidUtil.getUUID());
			score.setUserId(user.getId());
			score.setTopicId(topicid);
			score.setCreateTime(sf.format(new Date()));
			score.save();
		}
		renderSuccess();
	}

	/************************ 数据库管理---结束 *************************************************/

	/**************************************************************************/
	private String pageTitle = "区县指标审核";// 模块页面标题
	private String breadHomeMethod = "getListPage";// 面包屑首页方法

	public Map<String, String> getPageTitleBread() {
		Map<String, String> pageTitleBread = new HashMap<String, String>();
		pageTitleBread.put("pageTitle", pageTitle);
		pageTitleBread.put("breadHomeMethod", breadHomeMethod);
		return pageTitleBread;
	}
}