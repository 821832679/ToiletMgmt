package com.pointlion.sys.mvc.admin.answer;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.CellType;

import com.jfinal.aop.Before;
import com.jfinal.kit.PropKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.pointlion.sys.interceptor.MainPageTitleInterceptor;
import com.pointlion.sys.mvc.common.base.BaseController;
import com.pointlion.sys.mvc.common.model.Answer;
import com.pointlion.sys.mvc.common.model.AnswerExcel;
import com.pointlion.sys.mvc.common.model.FileConfig;
import com.pointlion.sys.mvc.common.model.Question;
import com.pointlion.sys.mvc.common.model.Resource;
import com.pointlion.sys.mvc.common.model.Score;
import com.pointlion.sys.mvc.common.model.SysUser;
import com.pointlion.sys.mvc.common.model.Topic;
import com.pointlion.sys.mvc.common.model.TopicDelay;
import com.pointlion.sys.mvc.common.model.TopicType;
import com.pointlion.sys.mvc.common.utils.DateUtils;
import com.pointlion.sys.mvc.common.utils.UuidUtil;

@Before(MainPageTitleInterceptor.class)
public class AnswerManagementController extends BaseController {

	/*************************** 指标管理---开始 ***********************/

	/**
	 * 获得指标管理页面
	 */
	public void getListPage() {
		String topicid = getPara("topicid");
		setAttr("topicid", topicid);
		render("/WEB-INF/admin/answer/list.html");
	}
	
	/**
	 * 数据导入
	 * @title importWeb
	 */
	public void importWeb() {
		String topicid = getPara("topicid");
		setAttr("topicid", topicid);
		render("/WEB-INF/admin/answer/import.html");
	}
	
	/**
	 * 获得指标管理数据
	 */
	public void listData() {
		String curr = getPara("pageNumber");
		String topicid = getPara("topicid");
		String username = getSessionAttr("username");
		SysUser user = SysUser.dao.findbyUserName(username);
		Float currFloat = Float.valueOf(curr);
		Integer currPage = currFloat.intValue();
		String pageSize = getPara("pageSize");
		Page<Answer> page = Answer.dao.getAnswerPageByUser(currPage, Integer.valueOf(pageSize), topicid, user.getId());
		for (Answer answer : page.getList()) {
			BigDecimal bi = new BigDecimal("10000");
			if(answer.getPlanned()!=null)
				answer.setPlanned(answer.getPlanned().divide(bi, 4, RoundingMode.HALF_UP));
			if(answer.getRamount()!=null)
				answer.setRamount(answer.getRamount().divide(bi, 4, RoundingMode.HALF_UP));
			if(answer.getSubsidy()!=null)
				answer.setSubsidy(answer.getSubsidy().divide(bi, 4, RoundingMode.HALF_UP));
			if(answer.getSamount()!=null)
				answer.setSamount(answer.getSamount().divide(bi, 4, RoundingMode.HALF_UP));
		}
		renderPage(page.getList(), "", page.getTotalRow());
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
		render("/WEB-INF/admin/answer/edit.html");
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
	
	/***
	 * 删除
	 * 
	 * @throws Exception
	 */
	public void delete() throws Exception {
		Answer.dao.deleteByIds(getPara("ids"));
		renderSuccess();
	}
	
	/**
	 * 刷新绑定用户
	 * @title refreshAnswerPersion
	 */
	public void refreshAnswerPersion(){
		Integer oknum = 0;
		List<Answer> answers = Answer.dao.getAnswerBynotPersion();
		for (Answer answer : answers) {
			SysUser user = SysUser.dao.getLikeByUsername(answer.getDistrict());
			if(user!=null){
				answer.setPersonId(user.getId());
				answer.update();
				oknum++;
			}
		}
		renderSuccess(oknum.toString());
	}
	
	/**
	 * 用户批量绑定数据
	 * @title subBinding
	 */
	public void subBinding(){
		String personid = getPara("personid");
		if(StrKit.notBlank(getPara("answerids")) && StrKit.notBlank(personid)){
			String[] answerids = getPara("answerids").split(",");
			for (String id : answerids) {
				Answer answer = new Answer();
				answer.setId(id);
				answer.setPersonId(personid);
				answer.update();
			}
			renderSuccess();
		}else{
			renderError("请选择绑定用户");
		}
	}
	
	/**
	 * 下载导入模板
	 * @title downloadByAnswer
	 */
	public void downloadByAnswer(){
		String topicid = getPara("topicid");
		Question question = Question.dao.getQuestionFirstByTopicid(topicid);
		if(question!=null && StrKit.notBlank(question.getFId())){
			renderSuccess(question.getFId());
		}else{
			renderError();
		}
	}
	
	/**
	 * 上传excel厕所数据导入
	 * @title importByAnswer
	 */
	public void importByAnswer(){
		String fid = getPara("fid");
		String topicid = getPara("topicid");
		String username = getSessionAttr("username");
		SysUser user = SysUser.dao.findbyUserName(username);
		
		Answer answer = new Answer();
		answer.setTopicId(topicid);
		answer.setPersonId(user.getId());
		
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
					delFile(fid);	//导入完成后删除文件
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
		BigDecimal b = new BigDecimal("10000");
		//横向
		for (int i = 1; i < rowLength; i++) {
			Answer answer = new Answer();
			HSSFRow hssfRow = hssfSheet.getRow(i); // 循环获取所有行
			answer.setCode(hssfRow.getCell(0).getStringCellValue());	//厕所编号
			answer.setName(hssfRow.getCell(1).getStringCellValue());	//项目名称
			answer.setType(hssfRow.getCell(2).getStringCellValue());	//项目类别
			answer.setIstag(hssfRow.getCell(3).getStringCellValue());	//是否标注
			answer.setUnit(hssfRow.getCell(4).getStringCellValue());	//业主单位
			String ramount = hssfRow.getCell(5).getStringCellValue();
			if(StrKit.notBlank(ramount))
				answer.setRamount(new BigDecimal(ramount).multiply(b));	//实际投资额(万元)
			String samount = hssfRow.getCell(6).getStringCellValue();
			if(StrKit.notBlank(samount))
				answer.setSamount(new BigDecimal(samount).multiply(b));	//实际政府补贴(万元)
			if(hssfRow.getCell(7)!=null && hssfRow.getCell(7).getNumericCellValue()!=0){
				Date startime = hssfRow.getCell(7).getDateCellValue();
				answer.setStarTime(DateUtils.covert2YMd(startime));	//实际开工日期
			}
			
			if(hssfRow.getCell(8)!=null && hssfRow.getCell(8).getNumericCellValue()!=0){
				Date cleanTime = hssfRow.getCell(8).getDateCellValue();
				answer.setCleanTime(DateUtils.covert2YMd(cleanTime));	//实际完工日期
			}
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
				answer.setPlanned(new BigDecimal(planned).multiply(b));
			
			String subsidy = hssfRow.getCell(26).getStringCellValue();	//计划政府补贴(万元)
			if(StrKit.notBlank(subsidy))
				answer.setSubsidy(new BigDecimal(subsidy).multiply(b));
			
			if(hssfRow.getCell(27)!=null && hssfRow.getCell(27).getNumericCellValue()!=0){
				Date begintime = hssfRow.getCell(27).getDateCellValue();
				answer.setBegintime(DateUtils.covert2YMd(begintime));	//实际完工日期
			}
			
			if(hssfRow.getCell(28)!=null && hssfRow.getCell(28).getNumericCellValue()!=0){
				Date endtime = hssfRow.getCell(28).getDateCellValue();
				answer.setEndtime(DateUtils.covert2YMd(endtime));	//实际完工日期
			}
			answer.setState(hssfRow.getCell(29).getStringCellValue());	//完成情况
			answer.setYear(hssfRow.getCell(30).getStringCellValue());	//所属年份
			answer.setCreateTime(DateUtils.getYMdhmsTime());	//创建时间
			answer.setPinlevel(hssfRow.getCell(33).getStringCellValue());	//评定等级
			
			if(hssfRow.getCell(34)!=null && hssfRow.getCell(34).getNumericCellValue()!=0){
				Date pindate = hssfRow.getCell(34).getDateCellValue();
				if(pindate!=null){
					answer.setPindate(DateUtils.covert2YMd(pindate));	//评定时间
				}
			}
			
			answer.setPinunit(hssfRow.getCell(35).getStringCellValue());	//评定单位
			answer.setPinopinion(hssfRow.getCell(36).getStringCellValue());	//评定意见
			
			answer.setManageunit(hssfRow.getCell(37).getStringCellValue()); //管理单位
			answer.setManageinfo(hssfRow.getCell(38).getStringCellValue()); //管理人员
			answer.setManagetel(hssfRow.getCell(39).getStringCellValue()); //管理人员联系方式
			answer.setDefender(hssfRow.getCell(40).getStringCellValue()); //维护人员
			answer.setDefendertel(hssfRow.getCell(41).getStringCellValue()); //维护人员电话
			
			String totalinvest = hssfRow.getCell(42).getStringCellValue();
			if(StrKit.notBlank(totalinvest))
				answer.setTotalinvest(new BigDecimal(totalinvest).multiply(b));	//总投资
			
			String zysubsidy = hssfRow.getCell(43).getStringCellValue();
			if(StrKit.notBlank(zysubsidy))
				answer.setZysubsidy(new BigDecimal(zysubsidy).multiply(b));	//中央补贴
			
			String ssubsidy = hssfRow.getCell(44).getStringCellValue();
			if(StrKit.notBlank(ssubsidy))
				answer.setSsubsidy(new BigDecimal(ssubsidy).multiply(b));	//省财政补贴
			
			answer.setEvaluate("未审核");
			
			answer.setTopicId(param.getTopicId());
			answer.setPersonId(param.getPersonId());
			
			//判断是否已经存在数据
			Answer data = answer.getAnswerByCode(answer.getCode());
			if(data!=null){
				answer.setId(data.getId());
				answer.update();
			}else{
				answer.setId(UuidUtil.getUUID());
				answer.save();
			}
		}
	}
	
	@SuppressWarnings("resource")
	public void read(File file, AnswerExcel answerExcel, FileConfig config) throws Exception {
		// 读excele 到数据库
		if (!file.exists())
			System.out.println("文件不存在");
		POIFSFileSystem poifsFileSystem = new POIFSFileSystem(new FileInputStream(file));
		HSSFWorkbook hssfWorkbook = new HSSFWorkbook(poifsFileSystem);
		HSSFSheet hssfSheet = hssfWorkbook.getSheetAt(0); // 数据表
		int rowLength = hssfSheet.getLastRowNum() + 1;// 行数
		//判断是横向还是纵向
		if(config.getDirection() == 1){
			int sortvalue = 1;
			//横向
			for (int i = config.getBegin(); i < (rowLength-config.getEnd()); i++) {
				HSSFRow hssfRow = hssfSheet.getRow(i); // 循环获取所有行
				int colLength = hssfRow.getLastCellNum();// 列长度
				StringBuilder sb = new StringBuilder();
				sb.append("[");
				
				for (int j = 0; j < colLength; j++) {	//循环每一列
					HSSFCell hssfCell = hssfRow.getCell(j); // 获取结果
					if (hssfCell != null) {
						if(hssfCell.getCellTypeEnum() == CellType.STRING){
							sb.append(j==0?"'"+hssfCell.getStringCellValue()+"'":","+"'"+hssfCell.getStringCellValue()+"'");
						}else if (hssfCell.getCellTypeEnum() == CellType.NUMERIC) {
							// 判断参数类型
					        if(HSSFDateUtil.isCellDateFormatted(hssfCell)){
					            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
					            String date = sdf.format(HSSFDateUtil.getJavaDate(hssfCell.getNumericCellValue()));
					            sb.append(j==0?"'"+date+"'":","+"'"+date+"'");
					        }else {
					        	hssfCell.setCellType(CellType.STRING);
					        	sb.append(j==0?"'"+hssfCell.getStringCellValue()+"'":","+"'"+hssfCell.getStringCellValue()+"'");
					        }
						}
					}else{
						sb.append(j==0?"''":",''");
					}
				}
				sb.append("]");
				answerExcel.setId(UuidUtil.getUUID());
				answerExcel.setTitle(answerExcel.getTarget());
				answerExcel.setAnswervaljson(sb.toString());
				answerExcel.setSortvalue(sortvalue);
				answerExcel.save();
				sortvalue++;
			}
		}else if(config.getDirection() == 0){
			//纵向
			//从第一行循环起
			for (int i = 1; i < rowLength; i++) {
				HSSFRow hssfRow = hssfSheet.getRow(i); // 循环获取所有行
				HSSFCell hssfCell0 = hssfRow.getCell(0); // 获取标题
				HSSFCell hssfCell1 = hssfRow.getCell(1); // 获取结果
				if (hssfCell0 != null) {
					hssfCell0.setCellType(CellType.STRING);
				} 
				if (hssfCell1 != null) {
					hssfCell1.setCellType(CellType.STRING);
				}
				answerExcel.setId(UuidUtil.getUUID());
				answerExcel.setTitle(hssfCell0.getStringCellValue());
				answerExcel.setAnswerval(hssfCell1.getStringCellValue());
				answerExcel.setSortvalue(i);
				answerExcel.save();
			}
		}
	}
	
	/***
	 * 删除服务器文件
	 * 
	 * @throws Exception
	 */
	public void delFile() throws Exception {
		String fidParam = getPara("fid");
		delFile(fidParam);
		renderSuccess();
	}
	
	private void delFile(String fidParam) throws Exception {
		Resource ob = Resource.dao.findById(fidParam);
		if (ob != null) {
			String fileName = ob.getRname();
			String filePath = ob.getUploadPath();
			String path = filePath + "/" + fileName;
			File file = new File(path);
			if (file.exists()) {
				file.delete();// 删除附件
			}
		}
		Resource.dao.deleteById(fidParam);// 删除附件上传记录
	}
	
	

	/************************ 数据库管理---结束 *************************************************/

	/**************************************************************************/
	private String pageTitle = "旅游厕所统计查询";// 模块页面标题
	private String breadHomeMethod = "getListPage";// 面包屑首页方法

	public Map<String, String> getPageTitleBread() {
		String topicid = getPara("topicid");
		Map<String, String> pageTitleBread = new HashMap<String, String>();
		pageTitleBread.put("pageTitle", pageTitle);
		pageTitleBread.put("breadHomeMethod", breadHomeMethod+"?topicid="+topicid);
		return pageTitleBread;
	}
}
