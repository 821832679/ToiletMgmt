package com.pointlion.sys.mvc.common.model;

import com.jfinal.plugin.activerecord.ActiveRecordPlugin;

/**
 * Generated by JFinal, do not modify this file.
 * <pre>
 * Example:
 * public void configPlugin(Plugins me) {
 *     ActiveRecordPlugin arp = new ActiveRecordPlugin(...);
 *     _MappingKit.mapping(arp);
 *     me.add(arp);
 * }
 * </pre>
 */
public class _MappingKit {

	public static void mapping(ActiveRecordPlugin arp) {
		arp.addMapping("sys_user", "id", SysUser.class);//用户
		arp.addMapping("sys_user_role", "id", SysUserRole.class);//用户角色
		arp.addMapping("sys_menu", "id", SysMenu.class);//菜单
		arp.addMapping("sys_role", "id", SysRole.class);//角色
		arp.addMapping("sys_role_auth", "id", SysRoleAuth.class);//角色对应功能权限
		arp.addMapping("sys_org", "id", SysOrg.class);//组织结构
		arp.addMapping("sys_friend", "id", SysFriend.class);//用户好友
		arp.addMapping("oa_notice", "id", OaNotice.class);
		arp.addMapping("oa_notice_user", "id", OaNoticeUser.class);
		arp.addMapping("oa_bumph", "id", OaBumph.class);
		arp.addMapping("oa_bumph_org", "id", OaBumphOrg.class);
		arp.addMapping("oa_bumph_org_user", "id", OaBumphOrgUser.class);
		arp.addMapping("act_re_model", "ID_", ActReModel.class);//流程模型
		arp.addMapping("act_re_procdef", "ID_", ActReProcdef.class);
		arp.addMapping("v_tasklist", "TASKID", VTasklist.class);//任务--视图
		arp.addMapping("sys_custom_setting", "id", SysCustomSetting.class);//自定义设置
		arp.addMapping("oa_res_dct", "id", OaResDct.class);
		arp.addMapping("oa_res_get", "id", OaResGet.class);
		arp.addMapping("cms_content", "id", CmsContent.class);//内容
		arp.addMapping("cms_type", "id", CmsType.class);//内容类型
		arp.addMapping("sys_point", "id", SysPoint.class);//积分
		arp.addMapping("sys_point_user", "id", SysPointUser.class);//用户积分
		arp.addMapping("chat_history", "id", ChatHistory.class);//群聊
		arp.addMapping("oa_leave", "id", OaLeave.class);
		arp.addMapping("oa_use_car", "id", OaUseCar.class);//车辆派送
		arp.addMapping("sys_applog", "id", SysApplog.class);//系统操作日志

		arp.addMapping("t_admin_applog", "Log_Id", AdminApplog.class);//捐赠详情表
		arp.addMapping("swq_t_dbbackup", "bkId", TDbbackup.class); //数据库备份表
		arp.addMapping("t_tpauditstatus", "Status_Code", Tpauditstatus.class);//审核表
		arp.addMapping("t_tpclassify", "TPC_Code", Tpclassify.class);//专业表
		arp.addMapping("t_tporganization", "Org_Id", Tporganization.class);//机构表
		arp.addMapping("t_tppolitical", "TPP_Code", Tppolitical.class);//政治面貌表
		arp.addMapping("t_tpsource", "TP_Id", Tpsource.class);
		arp.addMapping("t_tpqualifications", "TPQ_Code", Tpqualifications.class);//学历表	arp.addMapping("t_tpsource", "TP_Id", Tpsource.class);
		arp.addMapping("t_tplocalization", "Org_Id", Tplocalization.class);
		
		arp.addMapping("t_custom_field_template", "id", TCustomFieldTemplate.class); //自定义模板表
		arp.addMapping("t_ps_cft", "id", TPsCft.class);//模板和业务资源关联表
		arp.addMapping("t_public_service", "id", TPublicService.class);//业务资源表
		arp.addMapping("t_resource", "rid", Resource.class); //资源表
		arp.addMapping("t_suffix", "sid", Suffix.class); //文件格式表
		

		arp.addMapping("t_topic", "id", Topic.class); //主题表
		arp.addMapping("t_topic_type", "id", TopicType.class); //主题类型表
		arp.addMapping("t_answer", "id", Answer.class); //答题表
		arp.addMapping("t_question", "id", Question.class); //指标列表
		arp.addMapping("t_score", "id", Score.class); //评分表
		arp.addMapping("t_answer_excel", "id", AnswerExcel.class); //答题excel统计表
		arp.addMapping("t_topic_delay", "id", TopicDelay.class); //考核延期表
		arp.addMapping("t_file_config", "id", FileConfig.class); //统计文件配置表
	}
}