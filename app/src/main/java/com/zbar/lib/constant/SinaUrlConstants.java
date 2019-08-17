package com.zbar.lib.constant;

public class SinaUrlConstants {
	//Server
	public static final String SAE_Server_URL = "http://lzedu.sinaapp.com/SA/";
    //Login
	public static final String SAE_LoginUrl = "http://lzedu.sinaapp.com/SA/Teacher_Login.php"; //登录验证
    public static final String SAE_Teacher_Reg_Url = "http://lzedu.sinaapp.com/SA/Sa_Teacher_Reg.php";//用户注册
    public static final String SAE_Teacher_GetPWD_Url = "http://lzedu.sinaapp.com/SA/Sa_Teacher_GetPWD.php";//忘记密码
    //Shake
    public static final String SAE_ShakeUrl = "http://lzedu.sinaapp.com/SA/SA_RandStudentOne.php";//摇一摇
    //Comment
    public static final String SAE_SaveCommentUrl = "http://lzedu.sinaapp.com/SA/SA_SaveComment.php";//保存评语
    public static final String SAE_GetCommentJsonUrl = "http://lzedu.sinaapp.com/SA/SA_GetCommentJson.php";//获取评语
    public static final String SAE_TopCommentUrl = "http://lzedu.sinaapp.com/SA/SA_TopComment.php";//置顶评语
    public static final String SAE_DelCommentUrl = "http://lzedu.sinaapp.com/SA/SA_DelComment.php";//删除评语
    //CheckHomeWork
    public static final String SAE_CheckHomeWorkUrl = "http://lzedu.sinaapp.com/SA/Student_HomeWork_Checker.php";//检查家庭作业本
    //ScoreShop
    public static final String SAE_SaveScoreShop_URL = "http://lzedu.sinaapp.com/SA/SA_SaveScoreShop.php";//保存奖品
    public static final String SAE_GetScoreShopJsonUrl = "http://lzedu.sinaapp.com/SA/SA_GetScoreShopJson.php";//获取奖品
    public static final String SAE_UPLOAD_ScoreShopPic_URL = "http://lzedu.sinaapp.com/SA/SA_ScoreShopPic.php";//上传并保存奖品图片
    public static final String SAE_ScoreShopPicUrl = "http://lzedu-sa.stor.sinaapp.com/UploadFiles/pImg/new_photo.png";//奖品图片
    public static final String SAE_DelScoreShopUrl = "http://lzedu.sinaapp.com/SA/SA_DelScoreShop.php";//删除奖品
    //SAE API
    public static final String SAE_UPLOAD_UpdateTeacherFace_URL = "http://lzedu.sinaapp.com/SA/SA_UpdateTeacherFacePic.php";//上传教师头像
    public static final String SAE_UPLOAD_StudentClassWork_URL = "http://lzedu.sinaapp.com/SA/SA_StudentClassWorkPic.php";//上传学生课堂照片
    public static final String SAE_ShowClassPic_URL = "http://lzedu.sinaapp.com/SA/SA_ShowClassPic.php";//学生课堂照片展示控制程序

	private SinaUrlConstants() {
	}
	
	public static class Extra {

	}

}
