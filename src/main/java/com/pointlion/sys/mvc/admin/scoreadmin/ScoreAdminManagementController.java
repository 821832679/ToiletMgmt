package com.pointlion.sys.mvc.admin.scoreadmin;

import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

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
import com.pointlion.sys.mvc.common.utils.ExportExcel;
import com.pointlion.sys.mvc.common.utils.ImageResizeUtil;
import com.pointlion.sys.mvc.common.utils.SysOrgUtil;
import com.pointlion.sys.mvc.common.utils.UuidUtil;

@Before(MainPageTitleInterceptor.class)
public class ScoreAdminManagementController extends BaseController {

	/*************************** 评分管理---开始 ***********************/

	/**
	 * 获得评分管理页面
	 */
	public void getListPage() {
		render("/WEB-INF/admin/scoreadmin/list.html");
	}
	
	/**
	 * 获得评分管理页面
	 */
	public void getListPageModel() {
		render("/WEB-INF/admin/scoremodel/list.html");
	}
	
	/**
	 * 选择用户绑定数据页面
	 * @title bindingWeb
	 */
	public void bindingWeb(){
		String answerids = getPara("answerids");
		setAttr("answerids", answerids);
		render("/WEB-INF/admin/scoreadmin/binding.html");
	}

	/**
	 * 获得评分管理数据
	 * @throws UnsupportedEncodingException 
	 */
	public void listData() throws UnsupportedEncodingException {
		String username = getSessionAttr("username");
		SysUser currUser = SysUser.dao.findbyUserName(username);
		String ids = "";
		if (StringUtils.isEmpty(currUser.getOrgid())) {
			ids = "#root";
		} else {
			ids = currUser.getOrgid();
		}
		List<SysOrg> sysOrgList = SysOrg.dao.getChildrenAll(ids);
		String result = SysOrgUtil.getOrgListForString(sysOrgList, currUser.getOrgid());
		
		String curr = getPara("pageNumber");
		Float currFloat = Float.valueOf(curr);
		Integer currPage = currFloat.intValue();
		String pageSize = getPara("pageSize");
		String Searcht1 = getPara("SearchValue");
		String orgid = getPara("orgid");
		String Searcht = Searcht1;
		if (Searcht1 != "") {
			Searcht = URLDecoder.decode(Searcht1, "UTF-8");
		}
		Page<Score> page = Score.dao.getScorePage(currPage, Integer.valueOf(pageSize),Searcht, orgid,result);
		List<Score> list = new ArrayList<Score>();
		for (int i = 0; i < page.getList().size(); i++) {
			Score score = page.getList().get(i);
			SysUser user = SysUser.dao.findById(score.getUserId());
			score.setUserName(user.getName());
			if (score.getPersonId() != null) {
				user = SysUser.dao.findById(score.getPersonId());
				score.setPerson(user.getName());
			}
			Topic topic = Topic.dao.findById(score.getTopicId());
			if (topic != null) {
				score.setTitle(topic.getName());
				score.setQxBeginTime(DateUtils.covert2YMd(DateUtils.convert2YMdhmsTime(topic.getBeginTime())));
				score.setQxEndTime(DateUtils.covert2YMd(DateUtils.convert2YMdhmsTime(topic.getQxEndTime())));
				score.setZjBeginTime(DateUtils.covert2YMd(DateUtils.convert2YMdhmsTime(topic.getZjBeginTime())));
				score.setZjEndTime(DateUtils.covert2YMd(DateUtils.convert2YMdhmsTime(topic.getZjEndTime())));
			}
			list.add(score);
		}
		renderPage(list, "", page.getTotalRow());
	}

	/**
	 * 获得指标管理页面
	 */
	public void getListByIDPage() {
		String id = getPara("id");
		if (StrKit.notBlank(id)) {
			Answer templpate = Answer.dao.findById(id);
			setAttr("t", templpate);
		}
		setAttr("topictype", TopicType.dao.getTopicByStatus("1"));
		setAttr("imgurl", PropKit.get("image.domain"));
		render("/WEB-INF/admin/scoreadmin/edit.html");
	}
	
	/**
	 * 获得指标管理页面
	 */
	public void getListByIDModel() {
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
		render("/WEB-INF/admin/scoremodel/edit.html");
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
					String answerid = "";
					String rids = "";
					String fid = "";
					if(page2!=null && page2.getList().size()>0){
						Answer ob = page2.getList().get(0);
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
									sb.append("<br><div id='file"+rid[j]+"'>"+ ahref + fileName + "</a><span><a class='label label-default' href='javascript:void(0)' onclick=delFile('"+question.getId()+"','"+answerid+"','"+rid[j]+"') style='margin-left: 20px;padding: .2em .6em 0.2em;'>删除</a></span></div>");
								} else {
									sb.append("<div id='file"+rid[j]+"'>"+ ahref + fileName + "</a><span><a class='label label-default' href='javascript:void(0)' onclick=delFile('"+question.getId()+"','"+answerid+"','"+rid[j]+"') style='margin-left: 20px;padding: .2em .6em 0.2em;'>删除</a></span></div>");
								}
							}
						}
					}
					sb.append("</p>");
					if (StringUtils.isEmpty(rids)) {
						sb.append("<input type='hidden' id='fileRid" + question.getId() + "' name='fileRid"+ question.getId() + "' value=''>");
					} else {
						sb.append("<input type='hidden' id='fileRid" + question.getId() + "' name='fileRid"+ question.getId() + "' value='" + rids + "'>");
					}
					sb.append("<input type='file' class='btn btn-info data-toolbar' id='fileText"+(i + 1)+"' name='fileText"+(i + 1)+"' placeholder='请选择文件' style='display: inline;'/>");
					sb.append("<a class='btn btn-info data-toolbar' href='javascript:void(0)' onclick=uploadFile('"+question.getId()+"','"+(i + 1)+"','"+answerid+"') >上传</a>");
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
	 * 保存上传附件
	 * @title saveUpload
	 */
	public void saveUpload(){
		String answerid = getPara("answerid");
		String rid = getPara("rid");
		Answer answer = Answer.dao.findById(answerid);
		if(answer!=null){
			String rids = answer.getRId();
			if(StrKit.notBlank(rids)){
				rids+=","+rid;
				answer.setRId(rids);
			}else{
				answer.setRId(rid);
			}
			answer.update();
			renderSuccess();
		}else{
			renderError("上传失败，当前指标数据已更新，请刷新页面后重试");
		}
	}
	
	/**
	 * 下载统计页面
	 * @title downloadWeb
	 */
	public void downloadWeb(){
		List<Topic> tlist = Topic.dao.getlist();
		setAttr("topicList", tlist);
		render("/WEB-INF/admin/scoreadmin/download.html");
	}
	
	/**
	 * 下载统计excel
	 * @throws UnsupportedEncodingException 
	 */
	public void downloadExl() throws UnsupportedEncodingException {
		String username = getSessionAttr("username");
		SysUser currUser = SysUser.dao.findbyUserName(username);
		String ids = "";
		if (StringUtils.isEmpty(currUser.getOrgid())) {
			ids = "#root";
		} else {
			ids = currUser.getOrgid();
		}
		List<SysOrg> sysOrgList = SysOrg.dao.getChildrenAll(ids);
		String result = SysOrgUtil.getOrgListForString(sysOrgList, currUser.getOrgid());
		String Searcht1 = getPara("SearchValue");
		String orgid = getPara("orgid");
		String Searcht = Searcht1;
		if (Searcht1 != "") {
			Searcht = URLDecoder.decode(Searcht1, "UTF-8");
		}
		Page<Score> page = Score.dao.getScorePage(1, 100000,Searcht, orgid,result);
		
		
		String title = "考核指标录入统计";
        String[] rowsName = new String[]{"序号","录入人","机构名称","指标录入时间","已录入指标数量（总15个指标）","未录入指标任务（均用数字表示）"};
        List<Object[]>  dataList = new ArrayList<Object[]>();
        Object[] objs = null;
       
        if(page.getList()!=null && page.getList().size()>0){
        	 //获取指标集合
            Map<String, Question> qmap = Question.dao.getQuestionAllByMap();
            for (int i = 0; i < page.getList().size(); i++) {
            	Score score = page.getList().get(i);
    			SysUser user = SysUser.dao.findById(score.getUserId());
    			if (score.getPersonId() != null) {
    				user = SysUser.dao.findById(score.getPersonId());
    				score.setPerson(user.getName());
    			}
                objs = new Object[rowsName.length];
                objs[0] = i;
                objs[1] = user.getName();
                objs[2] = score.getOrgName();
                SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
                String date = df.format(DateUtils.convert2YMdhmsTime(score.getCreateTime()));
                objs[3] = date;
                //统计录入情况
                int ok = 0;
                String no = " ";
                List<Answer> answers = Answer.dao.getAnswerList(score.getTopicId(), score.getUserId());
                if(answers!=null && answers.size()>0){
                	for (Answer a : answers) {
						if(StrKit.isBlank(a.getRId()) && StrKit.isBlank(a.getFId())){
							no+= qmap.get(a.getQuestionId()).getSortValue()+",";
						}else{
							ok++;
						}
					}
                }
                objs[4] = ok;
                objs[5] = no;
                dataList.add(objs);
            }
        }
        
        ExportExcel ex = new ExportExcel(title, rowsName, dataList);
        try {
			ex.export(getResponse());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("数据导出失败，原因："+e.getMessage());
		}
        System.out.println("数据导出成功");
        renderNull();
	}
	
	/**
	 * 下载统计excel
	 * @throws UnsupportedEncodingException 
	 */
	public void downloadAll() throws UnsupportedEncodingException {
		String topicid = getPara("topicid");
		List<SysOrg> orglist = SysOrg.dao.getbyExcList(topicid);
		String title = "考核指标录入统计";
        String[] rowsName = new String[]{"序号","录入人","所属市名称","区县名称","指标录入时间","已录入指标数量（总15个指标）","未录入指标任务（均用数字表示）","区县评分人","区县评分时间","区县评分结果","区县评分说明","专家评分人","专家评分时间","专家评分结果","专家评分说明"};
        List<Object[]>  dataList = new ArrayList<Object[]>();
        Object[] objs = null;
       
        if(orglist!=null && orglist.size()>0){
        	 //获取指标集合
            Map<String, Question> qmap = Question.dao.getQuestionAllByMap();
            for (int i = 0; i < orglist.size(); i++) {
            	SysOrg org = orglist.get(i);
                objs = new Object[rowsName.length];
                objs[0] = i;
                objs[1] = org.getUsername();
                objs[2] = org.getParentname();
                objs[3] = org.getName();
               
                //统计录入情况
                int ok = 0;
                String no = " ";
                List<Answer> answers = Answer.dao.getAnswerList(topicid, org.getUserid());
                if(answers!=null && answers.size()>0){
                	objs[4] = " ";
                    if(StrKit.notBlank(answers.get(0).getCreateTime())){
                    	 SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
                         String date = df.format(DateUtils.convert2YMdhmsTime(answers.get(0).getCreateTime()));
                         objs[4] = date;
                    }
                	for (Answer a : answers) {
						if(StrKit.isBlank(a.getRId()) && StrKit.isBlank(a.getFId())){
							no+= qmap.get(a.getQuestionId()).getSortValue()+",";
						}else{
							ok++;
						}
					}
                }else{
                	objs[4] = " ";
                	no="未录入指标";
                }
                objs[5] = ok;
                objs[6] = no;
                objs[7] = StrKit.notBlank(org.getQxusername())?org.getQxusername():" ";
                objs[8] = " ";
                if(StrKit.notBlank(org.getMarkTime())){
               	 	SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
                    String date = df.format(DateUtils.convert2YMdhmsTime(org.getMarkTime()));
                    objs[8] = date;
                }
                objs[9] = StrKit.notBlank(org.getEvaluate())?org.getEvaluate():" ";
                objs[10] = StrKit.notBlank(org.getDescrible())?org.getDescrible():" ";
                objs[11] = StrKit.notBlank(org.getZjName())?org.getZjName():" ";
                objs[12] = " ";
                if(StrKit.notBlank(org.getZjTime())){
                	SimpleDateFormat df = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
                	String date = df.format(DateUtils.convert2YMdhmsTime(org.getZjTime()));
                	objs[12] = date;
                }
                objs[13] = StrKit.notBlank(org.getZjpfbz())?org.getZjpfbz():" ";
                objs[14] = StrKit.notBlank(org.getZjpfms())?org.getZjpfms():" ";
                dataList.add(objs);
            }
        }
        
        ExportExcel ex = new ExportExcel(title, rowsName, dataList);
        try {
			ex.export(getResponse());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("数据导出失败，原因："+e.getMessage());
		}
        System.out.println("数据导出成功");
        renderNull();
	}

	/**
	 * 保存
	 */
	public void save() {
		Answer answer = getModel(Answer.class);
		answer.update();
		renderSuccess();
	}
	
	
	/**
	 * 数据导入
	 * @title importWeb
	 */
	public void importWeb() {
		List<Topic> tlist = Topic.dao.getlist();
		setAttr("topicList", tlist);
		render("/WEB-INF/admin/scoreadmin/import.html");
	}
	
	/**
	 * 上传excel厕所数据导入
	 * @title importByAnswer
	 */
	public void importByAnswer(){
		String fid = getPara("fid");
		String topicid = getPara("topicid");
		
		Answer answer = new Answer();
		answer.setTopicId(topicid);
		
		Resource resource = Resource.dao.findById(fid);
		if(resource!=null){
			File file = new File(resource.getUploadPath()+"/"+resource.getRname());
			try {
				
				this.readByAnswer(file, answer);
				renderSuccess();
			} catch (Exception e) {
				System.out.println("excel数据导入失败，原因："+e.getMessage());
				e.printStackTrace();
				renderError("数据导入失败！");
				return;
			} finally {
				try {
					Resource ob = Resource.dao.findById(fid);
					if (ob != null) {
						String fileName = ob.getRname();
						String filePath = ob.getUploadPath();
						String path = filePath + "/" + fileName;
						File f = new File(path);
						if (f.exists()) {
							f.delete();// 删除附件
						}
					}
					Resource.dao.deleteById(fid);// 删除附件上传记录
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * excel导入旅游厕所数据到数据库
	 * @title readByAnswer
	 * @param file
	 * @param answer
	 * @param config
	 * @throws Exception
	 */
	@SuppressWarnings("resource")
	public void readByAnswer(File file, Answer param) throws Exception {
		// 读excele 到数据库
		if (!file.exists())
			new Exception("文件不存在");
		POIFSFileSystem poifsFileSystem = new POIFSFileSystem(new FileInputStream(file));
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(poifsFileSystem);
		HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0); // 数据表
		int rowLength = hssfSheet.getLastRowNum() + 1;// 行数
	    //将字符串转化为date类型，格式2016-10-12
	    SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss z", Locale.ENGLISH);
		//横向
		for (int i = 1; i < rowLength; i++) {
			Answer answer = new Answer();
			answer.setTopicId(param.getTopicId());
			HSSFRow hssfRow = hssfSheet.getRow(i); // 循环获取所有行
			answer.setCode(hssfRow.getCell(0).getStringCellValue());	//厕所编号
			answer.setName(hssfRow.getCell(1).getStringCellValue());	//项目名称
			answer.setType(hssfRow.getCell(2).getStringCellValue());	//项目类别
			answer.setIstag(hssfRow.getCell(3).getStringCellValue());	//是否标注
			answer.setUnit(hssfRow.getCell(4).getStringCellValue());	//业主单位
			String ramount = hssfRow.getCell(5).getStringCellValue();
			answer.setRamount(StrKit.notBlank(ramount)?new BigDecimal(ramount):null);	//实际投资额(万元)
			String samount = hssfRow.getCell(6).getStringCellValue();
			answer.setSamount(StrKit.notBlank(samount)?new BigDecimal(samount):null);	//实际政府补贴(万元)
			answer.setStarTime(hssfRow.getCell(7).getStringCellValue());	//实际开工日期
			answer.setCleanTime(hssfRow.getCell(8).getStringCellValue());	//实际完工日期
			answer.setNature(hssfRow.getCell(9).getStringCellValue());	//建设性质
			answer.setProvince(hssfRow.getCell(10).getStringCellValue());	//省份
			answer.setCity(hssfRow.getCell(11).getStringCellValue());	//市州
			answer.setDistrict(hssfRow.getCell(12).getStringCellValue());	//地区
			answer.setAddress(hssfRow.getCell(13).getStringCellValue());	//详细地址
			answer.setToileType(hssfRow.getCell(14).getStringCellValue());	//厕所类别
			answer.setLevel(hssfRow.getCell(15).getStringCellValue());	//拟建等级
			String menum = hssfRow.getCell(16).getStringCellValue();	//男厕位数量
			if(StrKit.notBlank(menum))
				answer.setMenum(Integer.valueOf(menum));
			
			String womanum = hssfRow.getCell(17).getStringCellValue();	//女厕位数量
			if(StrKit.notBlank(womanum))
				answer.setWomanum(Integer.valueOf(womanum));
			
			String barriernum = hssfRow.getCell(18).getStringCellValue();	//无障碍厕位数量
			if(StrKit.notBlank(barriernum))
				answer.setBarriernum(Integer.valueOf(barriernum));
			
			String threenum = hssfRow.getCell(19).getStringCellValue();	//第三卫生间数量
			if(StrKit.notBlank(threenum))
				answer.setThreenum(Integer.valueOf(threenum));
			
			String currencynum = hssfRow.getCell(20).getStringCellValue();	//男女通用厕位数量
			if(StrKit.notBlank(currencynum))
				answer.setCurrencynum(Integer.valueOf(currencynum));
			
			String areas = hssfRow.getCell(21).getStringCellValue();	//厕所面积
			if(StrKit.notBlank(areas))
				answer.setAreas(new BigDecimal(areas));
			
			answer.setLongitude(hssfRow.getCell(22).getStringCellValue());	//厕所经度
			answer.setLatitude(hssfRow.getCell(23).getStringCellValue());	//厕所纬度
			answer.setManages(hssfRow.getCell(24).getStringCellValue());	//管理形式
			String planned = hssfRow.getCell(25).getStringCellValue();	//计划投资额(万元)
			if(StrKit.notBlank(planned))
				answer.setPlanned(new BigDecimal(planned));
			
			String subsidy = hssfRow.getCell(26).getStringCellValue();	//计划政府补贴(万元)
			if(StrKit.notBlank(subsidy))
				answer.setSubsidy(new BigDecimal(subsidy));
			
			answer.setBegintime(hssfRow.getCell(27).getStringCellValue());	//计划开工日期
			answer.setEndtime(hssfRow.getCell(28).getStringCellValue());	//计划完工日期
			answer.setState(hssfRow.getCell(29).getStringCellValue());	//完成情况
			answer.setYear(hssfRow.getCell(30).getStringCellValue());	//所属年份
			answer.setCreateTime(hssfRow.getCell(31).getStringCellValue());	//创建时间
			answer.setUpdatetime(hssfRow.getCell(32).getStringCellValue());	//更新时间
			answer.setPinlevel(hssfRow.getCell(33).getStringCellValue());	//评定等级
			
			String pindate = hssfRow.getCell(34).getStringCellValue();
			if(StrKit.notBlank(pindate)){
				if(pindate.indexOf("GMT")>0){
					pindate = pindate.replace("GMT", "").replaceAll("\\(.*\\)", "");
					answer.setPindate(DateUtils.covert2YMd(format.parse(pindate)));	//评定时间
				}else
					answer.setPindate(pindate);	//评定时间
			}
			answer.setPinunit(hssfRow.getCell(35).getStringCellValue());	//评定单位
			answer.setPinopinion(hssfRow.getCell(36).getStringCellValue());	//评定意见
			answer.setEvaluate("未审核");
			
			//判断该厕所所属哪个区县(管理员适用)
			SysUser user = SysUser.dao.getLikeByUsername(answer.getDistrict());
			if(user!=null){
				answer.setPersonId(user.getId());
			}
			
			//判断是否已经存在数据
			Answer data = answer.getAnswerByCode(answer.getCode());
			if(data!=null){
				answer.setId(data.getId());
				answer.setPersonId(answer.getPersonId());
				answer.update();
			}else{
				answer.setId(UuidUtil.getUUID());
				answer.save();
			}
		}
	}

	/************************ 数据库管理---结束 *************************************************/

	/**************************************************************************/
	private String pageTitle = "区县指标评分";// 模块页面标题
	private String breadHomeMethod = "getListPage";// 面包屑首页方法

	public Map<String, String> getPageTitleBread() {
		Map<String, String> pageTitleBread = new HashMap<String, String>();
		pageTitleBread.put("pageTitle", pageTitle);
		pageTitleBread.put("breadHomeMethod", breadHomeMethod);
		return pageTitleBread;
	}
}