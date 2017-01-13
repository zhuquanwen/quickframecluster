
/**
 * CAS客户端默认登陆页面对应的JS
 * */
var SPE = "am23m4WEG5m346m7weE";
var ACCOUNT_INTO_IN_COOKIE = "ACCOUNT_INTO_IN_COOKIE";
var RECORD_ACCOUNT_CHECKBOX = "RECORD_ACCOUNT_CHECKBOX";
function GetQueryString(name)
{
     var reg = new RegExp("(^|&)"+ name +"=([^&]*)(&|$)");
     var r = window.location.search.substr(1).match(reg);
     if(r!=null)return  unescape(r[2]); return null;
}

$(function() {
	$('.loginbox').css({
		'position' : 'absolute',
		'left' : ($(window).width() - 692) / 2
	});
	$(window).resize(function() {
		$('.loginbox').css({
			'position' : 'absolute',
			'left' : ($(window).width() - 692) / 2
		});
	});
	$('.qrcode').css({
		
	});
	flushValidateCode();
	checkForLoginTicket();
	prepareLoginForm();
	//如果cookie中有账号信息,都出来写入表单
	readAccountInCookie();
});



var casLoginURL;
var loginTicket;
var execution;
function prepareLoginForm() {
//	$1('myLoginForm').action = 'https://localhost:8443/cas/login?' +
//			'service=http://localhost:8088/base/shiro-cas';
	$1("lt").value = loginTicket;
	$1("execution").value = execution;
	switch (error) {
	case "userError":{
		$1("errorSpan").innerHTML = "用户名密码校验失败!";
		break;
	}
	case "error":{
		$1("errorSpan").innerHTML = "非法登录!";
		break;
	}	
	default:
		$1("errorSpan").innerHTML = "";
		break;
	}
	
}

function checkForLoginTicket() {
	var loginTicketProvided = false;
	var query	= '';
//   casLoginURL = 'https://localhost:8443/cas/login';
//   thisPageURL = 'http://localhost:8088/base/toLogin';
//   casLoginURL += '?login-at=' + encodeURIComponent (thisPageURL)
//		   +"&service=http://localhost:8088/base/shiro-cas";
	casLoginURL = $1("CAS_LOGIN_URL").value;
	thisPageURL = $1("LOCAL_LOGIN_URL").value;
	casLoginURL += '?login-at=' + encodeURIComponent (thisPageURL)
				+ "&service=" + $1("LOGIN_SERVICE_PARAM").value;
	loginTicket = GetQueryString("lt");
	var loginTicketProvided;
	if(loginTicket != null){
		loginTicketProvided = true;
	}
	execution = GetQueryString("execution");
	error = GetQueryString("LOGIN_ERROR");

        // 判断是否已经获取到 lt 参数，如果未获取到则跳转至 cas/login 页，并且带上请求参数  get-lt=true。 第一次进该页面时会进行一次跳转
	if (!loginTicketProvided) {
		location.href = casLoginURL + '&get-lt=true';
	}
}

var $1 = function(id){
	return document.getElementById(id);
}

/* 刷新生成验证码 */
function flushValidateCode(){
	var validateImgObject = $1("codeValidateImg");
	validateImgObject.src = "getSysManageLoginCode?time=" + new Date();
}

function beforeLogin(){
	var flag = true;
	//处理账号cookie
	recordAccountInCookie();
	
	//验证码校验
	flag = checkImg();
	return flag;
}
//记录账号信息入cookie
function recordAccountInCookie(){
	var account = $.cookie(ACCOUNT_INTO_IN_COOKIE);
	var ifRecord = $.cookie(RECORD_ACCOUNT_CHECKBOX);
	if(account != null && 
			account != undefined && account != '' ){
		 $.cookie(ACCOUNT_INTO_IN_COOKIE,null);　
	}
	if(ifRecord != null && 
			ifRecord != undefined && ifRecord != '' ){
		$.cookie(RECORD_ACCOUNT_CHECKBOX,null);　
	}
	var checkbox = $("#checkbox").is(':checked');
	if(checkbox && checkbox == true){
		 $.cookie(RECORD_ACCOUNT_CHECKBOX, "true", {expires: 7, secure:false});
		 $.cookie(ACCOUNT_INTO_IN_COOKIE, $(".loginuser").val()+SPE+$(".loginpwd").val(), { expires: 7, secure:false});
	}
}

//读取记录账号信息从cookie
function readAccountInCookie(){
	var account = $.cookie(ACCOUNT_INTO_IN_COOKIE);
	var ifRecord = $.cookie(RECORD_ACCOUNT_CHECKBOX);
	if(account != null && 
			account != undefined && account != '' 
				&& account.split(SPE).length == 2){
		
    	var accountStr = account.split(SPE);
	    if(accountStr != null && accountStr.length == 2){
	    	$(".loginuser").val(accountStr[0] == null ? "" : accountStr[0]);
	    	$(".loginpwd").val(accountStr[1] == null ? "" : accountStr[1]);
	    		
		}else{
			$(".loginuser").val("");
	    	$(".loginpwd").val("");
		}
	}  	
	if(ifRecord == 'true'){
		$("#checkbox").attr("checked",true);
	}else{
		$("#checkbox").attr("checked",false);
	}
	
}
/*校验验证码输入是否正确*/
function checkImg(){
	var code = $1("imageCode").value;
	var flag = false;
    var url = "checkimagecode";
    $.ajax({
    	type : "post",
    	url : url,
    	data : "validateCode=" + code,
    	async : false,
    	success : function(data){
    		if(data=="ok"){
    			flag = true;
            }else{
            	$1("errorSpan").innerHTML = "验证码输入错误!";
                flushValidateCode();
            }
    	},
    	error : function(){
    		$1("errorSpan").innerHTML = "验证码验证错误!";
    		 flushValidateCode();
    	}
    	});
    return flag;
}
